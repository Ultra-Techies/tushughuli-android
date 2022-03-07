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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.todoist_android.R
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.data.responses.TasksResponseItem
import com.todoist_android.databinding.ActivityMainBinding
import com.todoist_android.ui.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userPreferences: UserPreferences
    var userId: Int = 0

    private val objects = arrayListOf<Any>()
    private val finalObjects = arrayListOf<Any>()

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

        binding.swipeContainer.post(Runnable {
            binding.swipeContainer.isRefreshing = true
            fetchTasks()
        })

        viewModel.task.observe(this, Observer { it ->
            when (it) {
                is APIResource.Success -> {
                    Log.d("MainActivity", "Tasks: ${it}")

                    binding.swipeContainer.isRefreshing = false
                    binding.recyclerView.removeItemDecoration(StickyHeaderItemDecoration())

                    it.value.forEach {
                        objects.add(it)
                    }
                    //sort objects by it.status (progress, created, completed)
                    objects.sortBy {
                        when (it) {
                            is TasksResponseItem -> it.status
                            else -> "progress"
                        }
                    }
                    objects.reverse()

                    //loop through the objects, if the item's status changes from current status
                    //add it to the finalObjects then add string to finalObjects
                    //then set the current status to the new status
                    var currentStatus = ""
                    objects.forEach {
                        if (it is TasksResponseItem) {
                            if (it.status != currentStatus) {
                                if(it.status.isBlank()){
                                    finalObjects.add("Unknown Status")
                                }else {
                                    finalObjects.add(it.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()) else it.toString() })
                                }
                                currentStatus = it.status
                                finalObjects.add(it)
                            }
                            else {
                                finalObjects.add(it)
                            }
                        }
                    }

                    //Setup RecyclerView
                    binding.recyclerView.adapter = ToDoAdapter(finalObjects)
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.addItemDecoration(StickyHeaderItemDecoration())
                }
                is APIResource.Loading -> {
                    Log.d("MainActivity", "Loading...")
                    binding.swipeContainer.isRefreshing = true

                    //clear finalObjects and objects
                    finalObjects.clear()
                    objects.clear()

                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
                is APIResource.Error -> {
                    binding.swipeContainer.isRefreshing = false
                    Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
                    Log.d("MainActivity", "Error: ${it.toString()}")
                }
            }
        })

        binding.buttonNewTask.setOnClickListener {
            val modalBottomSheet = BottomSheetFragment()
            modalBottomSheet.show(supportFragmentManager, BottomSheetFragment.TAG)
        }
    }

    private fun fetchTasks() {

        //TODO: Remove this, for testing purposes only
        viewModel.getTasks("")

        //TODO: remove above code above and use the below code once API is ready
        //viewModel.getTasks(userId.toString())

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

    override fun onRefresh() {
        fetchTasks()
    }
}
