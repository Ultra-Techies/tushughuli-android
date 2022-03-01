package com.todoist_android.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.todoist_android.R
import com.todoist_android.databinding.ActivityMainBinding
import com.todoist_android.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {

            if (binding.etUsername.text.isNullOrEmpty()){
                binding.etUsername.error = "Please Enter Your User Name"
                return@setOnClickListener
            }
            if (binding.etUsername.text.toString().trim().length<= 2){
                binding.etUsername.error ="Enter a Name with more than two words"
                return@setOnClickListener
            }
            if (binding.etEmail.text.isNullOrEmpty()){
                binding.etEmail.error ="Please Enter your Email"
                return@setOnClickListener
            }
            if (!validateEmail(binding.etEmail.text.toString())){
                binding.etEmail.error = "Please Enter a valid Email"
                return@setOnClickListener
            }
            if (binding.etPassword.text.isNullOrEmpty()){
                binding.etPassword.error ="Please enter your password"
                return@setOnClickListener
            }

            if (binding.etConfirmPassword.text.isNullOrEmpty()){
                binding.etConfirmPassword.error ="Confirm your Password"
                return@setOnClickListener

            }
            if(binding.etPassword.text.toString().trim() != binding.etConfirmPassword.text.toString().trim()){

                binding.etConfirmPassword.error ="Passwords do not match"
                return@setOnClickListener
            }
        }


    }
}