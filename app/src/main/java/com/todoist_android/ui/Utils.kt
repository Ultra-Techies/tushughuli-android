package com.todoist_android.ui

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.todoist_android.R
import java.text.SimpleDateFormat
import java.util.*


const val BASE_URL = "https://621ce943768a4e1020b93731.mockapi.io/api/v1/"

private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


fun validateEmail(email: String): Boolean {
    return email.matches(emailPattern.toRegex())
}


fun todayDate(): String {
    val todayDate = Calendar.getInstance().time
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(todayDate)
}


fun formartDate(date: String, originalFormat: String, expectedFormat: String): String {
    return try {
        val originalDateTime = SimpleDateFormat(originalFormat, Locale.getDefault()).parse(date)
        SimpleDateFormat(expectedFormat, Locale.getDefault()).format(originalDateTime!!)
    } catch (e: Exception) {
        date
    }
}

fun pickDate(supportFragmentManager: FragmentManager, onSelected: (String, Long) -> Unit) {
    val calendarConstraintBuilder =
        CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())

    val materialDateBuilder =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("SELECT DATE ")
            .setCalendarConstraints(calendarConstraintBuilder.build())

    val materialDatePicker = materialDateBuilder.build()
    materialDatePicker.show(supportFragmentManager, "tag")
    materialDatePicker.addOnPositiveButtonClickListener { timeInMilliseconds ->
        // Respond to positive button click.
        onSelected(materialDatePicker.headerText, timeInMilliseconds)
    }
}

fun pickTime(supportFragmentManager: FragmentManager, onTimeSelected: (String) -> Unit) {
    val picker =
        MaterialTimePicker.Builder()
            .setTitleText("Select Appointment time")
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(10)
            .build()
    picker.show(supportFragmentManager, "tag")
    picker.addOnPositiveButtonClickListener {
        val pickedHour: Int = picker.hour
        val pickedMinute: Int = picker.minute

        var formattedTime: String = when {
            pickedHour > 12 -> {
                if (pickedMinute < 10) {
                    "${picker.hour - 12}:0${picker.minute} PM"
                } else {
                    "${picker.hour - 12}:${picker.minute} PM"
                }
            }
            pickedHour == 12 -> {
                if (pickedMinute < 10) {
                    "${picker.hour}:0${picker.minute} PM"
                } else {
                    "${picker.hour}:${picker.minute} PM"
                }
            }
            pickedHour == 0 -> {
                if (pickedMinute < 10) {
                    "${picker.hour + 12}:0${picker.minute} am"
                } else {
                    "${picker.hour + 12}:${picker.minute} am"
                }
            }
            else -> {
                if (pickedMinute < 10) {
                    "${picker.hour}:0${picker.minute} am"
                } else {
                    "${picker.hour}:${picker.minute} am"
                }
            }
        }

        onTimeSelected(formattedTime)

    }
}

fun popupMenu(context: Context, view: View, statusSelected: (String) -> Unit) {
    val popup = PopupMenu(context, view)
    popup.inflate(R.menu.set_status_menu)
    popup.setOnMenuItemClickListener {
        when (it.itemId) {
            R.id.item_created -> {
                statusSelected("created")
            }
            R.id.item_progress -> {
                statusSelected("progress")
            }
        }
        true
    }
    popup.show()
}


fun popupMenuTwo(context: Context, view: View, statusSelected: (String) -> Unit) {
    val popup = PopupMenu(context, view)
    popup.inflate(R.menu.set_full_status_menu)
    popup.setOnMenuItemClickListener {
        when (it.itemId) {
            R.id.item_created_two -> {
                statusSelected("created")
            }
            R.id.item_progress_two -> {
                statusSelected("progress")
            }
            R.id.item_completed ->{
                statusSelected("Completed")
            }
        }
        true
    }
    popup.show()
}




