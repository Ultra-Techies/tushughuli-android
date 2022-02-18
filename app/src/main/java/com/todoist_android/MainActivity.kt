package com.todoist_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.todoist_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
    }
}