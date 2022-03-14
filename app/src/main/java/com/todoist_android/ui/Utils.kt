package com.todoist_android.ui

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.todoist_android.R
import com.todoist_android.data.network.APIResource
import com.todoist_android.ui.auth.LoginFragment
import org.junit.jupiter.api.fail
import java.text.SimpleDateFormat
import java.util.*


const val BASE_URL = "https://621ce943768a4e1020b93731.mockapi.io/api/v1/"

//const val BASE_URL ="http://192.168.0.108:8080/api"

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

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        action?.let {
            snackbar.setAction("Retry") {
                it()
            }
        }
    snackbar.show()
}

fun View.handleApiError (
    failure: APIResource.Error,
    action:(() -> Unit)? = null
){
    when {
        failure.isNetworkError -> snackbar("Network Error", action)
        failure.errorCode == 401 -> {
            snackbar("Unauthorized request", action)
        }
        failure.errorCode == 404 -> {
            snackbar("Resource not found", action)
        }
        failure.errorCode == 422 -> {
            snackbar("Validation error", action)
        }
        failure.errorCode == 500 -> {
            snackbar("Internal server error", action)
        }
        failure.errorCode == 503 -> {
            snackbar("Service unavailable", action)
        }
        failure.errorCode == 504 -> {
            snackbar("Gateway timeout", action)
        }
        failure.errorCode == 0 -> {
            snackbar("Unknown error", action)
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            snackbar(error, action)
        }
    }
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

//get time difference between now(current date time) to due date of format 2022/03/11 17:10:00
fun getTimeDifference(date: String): Array<Int> {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault())
    val currentDate = Date()
    val dueDate = dateFormat.parse(date)
    val diff = dueDate.time - currentDate.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    //position 0 is days, position 1 is hours, position 2 is minutes
    return arrayOf(days.toInt() ,hours.toInt(), minutes.toInt())
}




