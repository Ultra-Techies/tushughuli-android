package com.todoist_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.todoist_android.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
    }
}