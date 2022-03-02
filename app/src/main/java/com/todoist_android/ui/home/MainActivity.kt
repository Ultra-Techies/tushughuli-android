package com.todoist_android.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.todoist_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Receiving data from the previous activity
        val userId = intent.getIntExtra("userId", 0)

        Toast.makeText(this, "Welcome back $userId", Toast.LENGTH_LONG).show()
    }
}
