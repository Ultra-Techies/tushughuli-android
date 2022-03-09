package com.todoist_android.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.todoist_android.databinding.ActivityProfileBinding
import com.todoist_android.ui.SplashActivity
import com.todoist_android.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding

    @Inject
    lateinit var userPreferences: UserPreferences

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
        userPreferences = UserPreferences(this)
        //Receiving data from the previous activity
        val userId = intent.getIntExtra("userId", 0)

        //Back button on toolbar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.buttonLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, which ->
                    performLogout()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

        viewModel.getUser(userId.toString())

        viewModel.user.observe(this, Observer {
            when (it) {
                is APIResource.Success -> {
                    Log.d("ProfileActivity", "profile: ${it.value.profile_photo}")
                    binding.tvUsername.text = it.value.display_name
                    binding.tvEmail.text = it.value.email
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
                    Log.d("ProfileActivity", "User: ${it}")
                }
                is APIResource.Loading -> {
                    Log.d("ProfileActivity", "Loading...")
                }
                is APIResource.Error -> {
                    binding.root.handleApiError(it)
                    Log.d("ProfileActivity", "Error: ${it.toString()}")
                }
            }
        })
    }

    fun performLogout() = lifecycleScope.launch {
        userPreferences.clearToken()
        Intent(this@ProfileActivity, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.also {
            startActivity(it)
        }
    }
}