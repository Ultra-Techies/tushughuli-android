package com.todoist_android.view

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*


const val BASE_URL = "https://621ce943768a4e1020b93731.mockapi.io/api/v1/"

private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

fun validateEmail(email: String): Boolean {
    return email.matches(emailPattern.toRegex())
}

fun EditText.showKeyboard() {
    if (requestFocus()) {
        (context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, SHOW_IMPLICIT)
        setSelection(text.length)
    }
}

fun View.hideKeyboard() {
    val closeKeyboard =
        this.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    closeKeyboard.hideSoftInputFromWindow(this.windowToken, 0)
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
        date;
    }
}



