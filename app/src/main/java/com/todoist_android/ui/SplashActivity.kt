package com.todoist_android.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.material.snackbar.Snackbar
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.databinding.ActivitySplashBinding
import com.todoist_android.ui.auth.AuthActivity
import com.todoist_android.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashViewModel>()

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

                    //check to see if user still exists
                    viewModel.getUser(userId.toString())

                    viewModel.user.observe(this, Observer {
                        when (it) {
                            is APIResource.Success -> {
                                Intent(this@SplashActivity, MainActivity::class.java).also {
                                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    it.putExtra("userId", userId)
                                    startActivity(it)
                                }
                            }
                            is APIResource.Loading -> {
                                Log.d("SplashActivity", "Loading...")
                                progressBar.visibility = android.view.View.VISIBLE
                            }
                            is APIResource.Error -> {

                                progressBar.visibility = android.view.View.GONE
                                binding.root.handleApiError(it, action = {
                                    startActivity(Intent(this@SplashActivity, SplashActivity::class.java))
                                })
                                GlobalScope.launch(Dispatchers.Main) {
                                    delay(TimeUnit.SECONDS.toMillis(2))
                                    userPreferences.clearToken();
                                    startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                                    finish()
                                }
                                Log.d("SplashActivity", "Error: ${it as APIResource.Error}")
                            }
                        }
                    })
                } else {

                    GlobalScope.launch(Dispatchers.Main) {
                        delay(TimeUnit.SECONDS.toMillis(2))
                        Log.i("TAG", "this will be called after 2 seconds")
                        startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                        finish()
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