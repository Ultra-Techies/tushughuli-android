package com.todoist_android.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todoist_android.R
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.databinding.ActivityMainBinding
import com.todoist_android.ui.SplashActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userPreferences: UserPreferences
    var userId: Int = 0

    //Adapter Items
    private val objects = arrayListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        setSupportActionBar(binding.toolbar)
        title = "Hi username"

        //Receiving data from the previous activity
        val userId = intent.getIntExtra("userId", 0)

        Toast.makeText(this, "Welcome back $userId", Toast.LENGTH_LONG).show()

        //Mock data
        for (header in 0..100) {
            objects.add("Header - $header")
            for (item in 1..3) {
                objects.add(item)
            }
        }

        //Setup RecyclerView
        binding.rcvMain?.adapter = MainAdapter(objects)
        binding.rcvMain?.layoutManager = LinearLayoutManager(this)
        binding.rcvMain?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.rcvMain?.addItemDecoration(StickyHeaderItemDecoration())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                Intent(this, ProfileActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.putExtra("userId", userId)
                    startActivity(it)
                }
                true
            }

//            R.id.action_settings -> {
//                startActivity(Intent(this, SettingsActivity::class.java))
//                true
//            }

            R.id.action_logout -> {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.apply {
                    setTitle("Logout?")
                    setMessage("Are you sure you want to logout?")
                    setPositiveButton("Yes") { _, _ ->
                        performLogout()
                    }
                    setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }
                }.create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun performLogout() = lifecycleScope.launch {
        userPreferences.clearToken()
        Intent(this@MainActivity, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.also {
            startActivity(it)
        }
    }
}
