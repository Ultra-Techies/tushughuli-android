package com.todoist_android.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.todoist_android.R
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.databinding.ActivitySettingsBinding
import com.todoist_android.ui.SplashActivity
import com.todoist_android.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModels<SettingsViewModel>()

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
        val userId = intent.getIntExtra("userId", 0)
        userPreferences = UserPreferences(this)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        //Get user data
        viewModel.getUser(userId.toString())
        viewModel.user.observe(this, Observer {
            when (it) {
                is APIResource.Success -> {
                    binding.etUsername.setText(it.value.username)
                    binding.etEmail.setText(it.value.email)
                    Picasso.get()
                        .load(it.value.profile_photo)
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
                }
                is APIResource.Error -> {
                    Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
                    Log.d("SettingsActivity", "Error: ${it.toString()}")
                }
            }
        })

        //delete user observer
        viewModel.userDelete.observe(this, Observer {
            when (it) {
                is APIResource.Success -> {
                    performDelete()
                }
                is APIResource.Loading -> {
                    Log.d("SettingsActivity", "Deleting...")
                }
                is APIResource.Error -> {
                    Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
                    Log.d("SettingsActivity", "Error: ${it.toString()}")
                }
            }
        })

        binding.appNotifications.isChecked = false
        binding.appNotifications.setOnCheckedChangeListener { _, isChecked ->
            binding.appNotifications.isChecked = isChecked
        }

        binding.buttonDeleteAccount.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes") { dialog, which ->
                    viewModel.deleteUser(userId.toString())
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    fun performDelete() = lifecycleScope.launch {
        Toast.makeText(this@SettingsActivity, "User deleted", Toast.LENGTH_SHORT).show()
        userPreferences.clearToken()
        Intent(this@SettingsActivity, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.also {
            startActivity(it)
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
                //save details
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}