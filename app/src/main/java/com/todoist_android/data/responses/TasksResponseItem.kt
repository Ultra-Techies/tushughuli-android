package com.todoist_android.data.responses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TasksResponseItem(
    val description: String?,
    val title: String?,
    var dueDate: String?,
    val reminder: String?,
    val status: String?,
    val id: Int,

): Parcelable