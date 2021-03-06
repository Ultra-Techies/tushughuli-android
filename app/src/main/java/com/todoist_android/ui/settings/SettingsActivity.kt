package com.todoist_android.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.todoist_android.R
import com.todoist_android.data.models.UserModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.databinding.ActivitySettingsBinding
import com.todoist_android.ui.SplashActivity
import com.todoist_android.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModels<SettingsViewModel>()

    @Inject
    lateinit var userPreferences: UserPreferences

    var loggedInUserId: Int = 0
    var profile_photo: String = "https://raw.githubusercontent.com/Ultra-Techies/Todolist-Frontend/develop/src/assets/images/Avatar.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
        binding.progressbar.isVisible = false

        val loggedInUserId = intent.getIntExtra("userId", 0)

        //if userId is 0, then the user is not logged in
        if (loggedInUserId == 0) {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            this.loggedInUserId = loggedInUserId
        }

        userPreferences = UserPreferences(this)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        //Get user data
        viewModel.getUser(loggedInUserId.toString())
        viewModel.user.observe(this, Observer {
            when (it) {
                is APIResource.Success -> {
                    disableInput(false)
                    binding.progressbar.isVisible = false

                    binding.etUsername.setText(it.value.username)
                    binding.etEmail.setText(it.value.email)
                    Picasso.get()
                        .load(it.value.photo)
                        .error(R.drawable.default_profile_pic)
                        .into(binding.userProfilePhoto, object : Callback {
                            override fun onSuccess() {
                                //
                            }

                            override fun onError(e: Exception?) {
                                Snackbar.make(
                                    binding.root,
                                    "Error loading profile photo",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                        })
                    Log.d("SettingsActivity", "User: ${it}")
                }
                is APIResource.Loading -> {
                    Log.d("SettingsActivity", "Loading...")
                    disableInput(true)
                    binding.progressbar.isVisible = true
                }
                is APIResource.Error -> {
                    binding.root.handleApiError(it)
                    Log.d("SettingsActivity", "Error: ${it.toString()}")
                    disableInput(true)
                    binding.progressbar.isVisible = false
                }
            }
        })

        //delete all user's tasks then delete user observer
        viewModel.userDeleteTasks.observe(this, Observer {
            when (it) {
                is APIResource.Success -> {
                    //delete user
                    viewModel.deleteUser(loggedInUserId.toString())
                    viewModel.userDelete.observe(this, Observer {
                        when (it) {
                            is APIResource.Success -> {
                                binding.progressbar.isVisible = false
                                //if message is not null, then the user was deleted
                                it.value.message?.let {
                                    redirectToSplash()
                                }
                            }
                            is APIResource.Loading -> {
                                Log.d("SettingsActivity", "Deleting...")
                                binding.progressbar.isVisible = true
                            }
                            is APIResource.Error -> {
                                binding.progressbar.isVisible = false
                                binding.root.handleApiError(it)
                                if(it.errorCode == 404) {
                                    redirectToSplash()
                                }
                            }
                        }
                    })
                }
                is APIResource.Loading -> {
                    Log.d("SettingsActivity", "Loading...")
                    disableInput(true)
                    binding.progressbar.isVisible = true
                }
                is APIResource.Error -> {
                    binding.root.handleApiError(it)
                    Log.d("SettingsActivity", "Error: ${it.toString()}")
                    disableInput(true)
                    binding.progressbar.isVisible = false
                }
            }
        })

        //update user observe
        this.lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.editUserResponse.collect {
                    when(it){
                        is APIResource.Success->{
                            disableInput(false)
                            binding.progressbar.isVisible = false
                            Snackbar.make(binding.root, "User updated", Snackbar.LENGTH_SHORT).show()
                        }
                        is APIResource.Error ->{
                            binding.root.handleApiError(it)
                            disableInput(true)
                            binding.progressbar.isVisible = false
                        }
                        is APIResource.Loading -> {
                            disableInput(true)
                            binding.progressbar.isVisible = true
                        }
                    }
                }
            }
        }

        //fetch notifications state from shared preferences
        userPreferences.notificationState.asLiveData().observe(this) {
            Log.d("SettingsActivity", "notification state: $it")
            if (!it.isNullOrEmpty()) {
                val notificationState: Boolean = it.toString().toBoolean()
                binding.appNotifications.isChecked = notificationState

            } else {
                binding.appNotifications.isChecked = false
            }
        }

        binding.appNotifications.setOnCheckedChangeListener { _, isChecked ->
            binding.appNotifications.isChecked = isChecked
            viewModel.saveNotificationState(isChecked)
        }

        binding.buttonDeleteAccount.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes") { dialog, which ->
                    viewModel.deleteAllTasks(loggedInUserId.toString())

                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun disableInput(b: Boolean) {
        binding.etEmail.isFocusable = !b
        binding.etEmail.isFocusableInTouchMode = !b

        binding.etUsername.isFocusable = !b
        binding.etUsername.isFocusableInTouchMode = !b

        binding.etPassword.isFocusable = b
        binding.etPassword.isFocusableInTouchMode = !b

        binding.appNotifications.isEnabled = !b
    }

    fun redirectToSplash() = lifecycleScope.launch {
        userPreferences.clearToken()
        Intent(this@SettingsActivity, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.also {
            startActivity(it)
        }
    }

    fun saveUserDetails(updateUserRequest: UserModel) = lifecycleScope.launch {
            binding.progressbar.isVisible = true

            if (binding.etUsername.text.toString().isEmpty()) {
                binding.etUsername.error = "Username is required"
            }

            if (binding.etEmail.text.toString().isEmpty()) {
                binding.etEmail.error = "Email is required"
            }

            Log.d("SettingsActivity: ", updateUserRequest.toString())

            lifecycleScope.launchWhenStarted {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateUser(updateUserRequest).collect {
                    when (it) {
                        is APIResource.Success -> {
                            binding.progressbar.isVisible = false
                            Snackbar.make(binding.root, "User updated successfully", Snackbar.LENGTH_SHORT).show()
                        }

                        is APIResource.Error -> {
                            binding.progressbar.isVisible = false
                            binding.root.handleApiError(it)
                        }
                        is APIResource.Loading -> {
                            binding.progressbar.isVisible = true
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveDetails -> {

                val updateUserRequest = UserModel(
                    id = loggedInUserId.toString(),
                    username = binding.etUsername.text.toString().trim(),
                    email = binding.etEmail.text.toString().trim(),
                    password = binding.etPassword.text.toString().trim(),
                    photo = profile_photo
                )

                saveUserDetails(updateUserRequest)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}