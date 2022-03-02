package com.todoist_android.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.asLiveData
import com.google.android.material.snackbar.Snackbar
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.databinding.ActivitySplashBinding
import com.todoist_android.ui.auth.AuthActivity
import com.todoist_android.ui.home.MainActivity
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isOnline(this)){
            //region: check if user token or id is present in shared preferences
            val userPreferences = UserPreferences(this)
            userPreferences.todoToken.asLiveData().observe(this) {
                if (!it.isNullOrEmpty()) {
                    Log.d("SplashActivity", "token is not null or empty")
                    val userId: Int = it.toString().toInt()
                    Intent(this, MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        it.putExtra("userId", userId)
                        startActivity(it)
                    }
                    finish()
                } else {
                    //count for 2 seconds
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.SECONDS.toMillis(2))
                        withContext(Dispatchers.Main) {
                            Log.i("TAG", "this will be called after 2 seconds")
                            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                            finish()
                        }
                    }
                }
            }

            //endregion

        }else{
            Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_LONG).show()
            binding.progressBar.visibility = android.view.View.GONE
        }
    }

    //kotlin: check network connection
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}