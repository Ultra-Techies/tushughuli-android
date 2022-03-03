package com.todoist_android.ui.home

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.todoist_android.databinding.ActivityMainBinding
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.Context


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Receiving data from the previous activity
        val userId = intent.getIntExtra("userId", 0)



        Toast.makeText(this, "Welcome back $userId", Toast.LENGTH_LONG).show()

        binding.buttonNewTask.setOnClickListener {
            val modalBottomSheet = BottomSheetFragment()
            modalBottomSheet.show(supportFragmentManager, BottomSheetFragment.TAG)
        }
    }
}
