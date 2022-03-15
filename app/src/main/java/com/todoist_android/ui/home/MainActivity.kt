package com.todoist_android.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.todoist_android.R
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.data.responses.TasksResponseItem
import com.todoist_android.databinding.ActivityMainBinding
import com.todoist_android.ui.SplashActivity
import com.todoist_android.ui.auth.AuthActivity
import com.todoist_android.ui.handleApiError
import com.todoist_android.ui.profile.ProfileActivity
import com.todoist_android.ui.profile.ProfileViewModel
import com.todoist_android.ui.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userPreferences: UserPreferences

    var loggedInUserId: Int = 0
    var notificationState: Boolean = false

    private val objects = arrayListOf<Any>()
    private val finalObjects = arrayListOf<Any>()
    private val filteredTasks = ArrayList<TasksResponseItem>() //This list will contain only items of type TasksResponseItem (excluding headers)

    private val viewModel by viewModels<MainViewModel>()

    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        setSupportActionBar(binding.toolbar)
        title = "Hi"

        var currentStatus = ""

        //Receiving data from the previous activity
        val loggedInUserId = intent.getIntExtra("userId", 0)

        //if userId is 0, then the user is not logged in
        if (loggedInUserId == 0) {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            this.loggedInUserId = loggedInUserId
        }

        binding.swipeContainer.setOnRefreshListener(this)
        binding.swipeContainer.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        binding.swipeContainer.post {
            binding.swipeContainer.isRefreshing = true
            finalObjects.clear()
            objects.clear()
            filteredTasks.clear()

            currentStatus = ""
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.task.observe(this, Observer { it ->
            when (it) {
                is APIResource.Success -> {
                    Log.d("MainActivity", "Tasks: ${it}")

                    // if objects anf final objects is not empty clear the list
                    if ( objects.isNotEmpty() ) objects.clear()
                    if ( finalObjects.isNotEmpty() ) finalObjects.clear()
                    if ( filteredTasks.isNotEmpty() ) filteredTasks.clear()

                    binding.swipeContainer.isRefreshing = false
                    binding.recyclerView.removeItemDecoration(StickyHeaderItemDecoration())

                    it.value.forEach {
                        objects.add(it)
                    }

                    if(objects.size == 0) {
                        showEmptyState(VISIBLE)

                    } else {
                        showEmptyState(GONE)
                    }
                    //sort objects by it.status (progress, created, done)
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
                    currentStatus = ""
                    objects.forEach {
                        if (it is TasksResponseItem) {
                            if (it.status != currentStatus) {
                                if(it.status!!.isNullOrEmpty()){
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
                                currentStatus = it.status
                            }
                        }
                    }

                    //filter finalObjects to an ArrayList of items of type TasksResponseItem
                    finalObjects.filterIsInstance<TasksResponseItem>().forEach {
                        Log.d("MainActivity", "FinalObjects: ${it}")
                        //empty list of type TasksResponseItem

                        filteredTasks.add(it)
                    }

                    //pass the filteredTasks to the scheduler function
                    scheduleAlert(binding.recyclerView, filteredTasks)

                    //Setup RecyclerView
                    var todoAdapter = ToDoAdapter(finalObjects)
                    todoAdapter.onEditTaskCallback = {
                        fetchTasks()
                    }
                    binding.recyclerView.adapter = todoAdapter
                    binding.recyclerView.addItemDecoration(StickyHeaderItemDecoration())
                }
                is APIResource.Loading -> {
                    Log.d("MainActivity", "Loading...")
                    binding.swipeContainer.isRefreshing = true

                    //clear finalObjects and objects
                    finalObjects.clear()
                    objects.clear()
                    currentStatus = ""
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
                is APIResource.Error -> {
                    currentStatus = ""
                    binding.swipeContainer.isRefreshing = false
                    binding.root.handleApiError(it, action = {
                        fetchTasks()
                    })
                    showEmptyState(VISIBLE)
                    Log.d("MainActivity", "Error: ${it.toString()}")
                }
            }
        })

        binding.buttonNewTask.setOnClickListener {
            val modalBottomSheet = BottomSheetFragment{
                fetchTasks()
            }
            modalBottomSheet.show(supportFragmentManager, BottomSheetFragment.TAG)
        }

        //get user details
        profileViewModel.getUser(loggedInUserId.toString())

        profileViewModel.user.observe(this, Observer {
            when (it) {
                is APIResource.Success -> {
                    title = "Hi ${it.value.username}"
                    Log.d("MainActivity", "User: ${it}")
                }
                is APIResource.Loading -> {
                    Log.d("ProfileActivity", "Loading...")
                }
                is APIResource.Error -> {
                    binding.root.handleApiError(it, action = {
                        fetchTasks()
                    })
                    Log.d("ProfileActivity", "Error: ${it.toString()}")
                }
            }
        })

        //check notification_state
        userPreferences.notificationState.asLiveData().observe(this) {
            if (it.isNullOrEmpty()) {
                Log.d("MainActivity", "notification state has not been initialized")
                notificationState = false
                stopService(Intent(this, NotificationService::class.java))
            } else {
                notificationState = it.toBoolean()
                Log.d("MainActivity", "notification state: $notificationState")
                if (!notificationState) {
                    stopService(Intent(this, NotificationService::class.java))
                }
            }
        }
    }

    private fun showEmptyState(visible: Int) {
        binding.emptyTag.visibility = visible
        binding.emptyIcon.visibility = visible
    }

    override fun onResume() {
        super.onResume()
        fetchTasks()
    }

    override fun onRefresh() {
        fetchTasks()
    }

    private fun fetchTasks() {
        Log.d("MainActivity", "Fetching tasks...")
        viewModel.getTasks(loggedInUserId.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                Intent(this, ProfileActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.putExtra("userId", loggedInUserId)
                    startActivity(it)
                }
                true
            }

            R.id.action_settings -> {
                Intent(this, SettingsActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.putExtra("userId", loggedInUserId)
                    startActivity(it)
                }
                true
            }

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



    fun scheduleAlert(view: View, finalObjects: ArrayList<TasksResponseItem>){
        Log.d("MainActivity", "Notification status: $notificationState")
        val intent = Intent(this, NotificationService::class.java)

        var bundle = Bundle()
        bundle!!.putSerializable("data",finalObjects)
        intent.putExtra("bundle",bundle)

        startService(intent)
    }
}
