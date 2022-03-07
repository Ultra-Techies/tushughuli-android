package com.todoist_android.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.todoist_android.R
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.data.responses.TasksResponseItem
import com.todoist_android.databinding.ActivityMainBinding
import com.todoist_android.ui.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userPreferences: UserPreferences
    var userId: Int = 0

    private val objects = arrayListOf<Any>()

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        setSupportActionBar(binding.toolbar)
        title = "Hi username"

        //Receiving data from the previous activity
        val userId = intent.getIntExtra("userId", 0)

        //if userId is 0, then the user is not logged in
        if (userId == 0) {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            this.userId = userId
        }

        //TODO: Remove this, for testing purposes only
        viewModel.getTasks("")

        //TODO: remove above code and use the below code once API is ready
        //viewModel.getTasks(userId.toString())

        viewModel.task.observe(this, Observer { it ->
            when (it) {
                is APIResource.Success -> {
                    Log.d("MainActivity", "Tasks: ${it}")

                    //loop through the tasks and add them to the recycler view if status is progress
                    objects.add("In Progress")
                    it.value.forEach {
                        if (it.status == "progress") {
                            objects.add(it)
                        }
                    }

                    //loop through the tasks and add them to the recycler view if status is created
                    objects.add("Created")
                    it.value.forEach {
                        if (it.status == "created") {
                            objects.add(it)
                        }
                    }

                    //loop through the tasks and add them to the recycler view if status is completed
                    objects.add("Done")
                    it.value.forEach {
                        if (it.status == "completed") {
                            objects.add(it)
                        }
                    }

                    //Setup RecyclerView
                    binding.recyclerView.adapter = ToDoAdapter(objects)
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                    binding.recyclerView.addItemDecoration(StickyHeaderItemDecoration())
                }
                is APIResource.Loading -> {
                    Log.d("MainActivity", "Loading...")
                }
                is APIResource.Error -> {
                    Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
                    Log.d("MainActivity", "Error: ${it.toString()}")
                }
            }
        })
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
