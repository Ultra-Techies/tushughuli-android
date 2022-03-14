package com.todoist_android.data.responses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TasksResponseItem(
    val description: String?,
    val dueDate: String?,
    val id: String?,
    val status: String?,
    val reminder: String?,
    val title: String?
): Parcelable