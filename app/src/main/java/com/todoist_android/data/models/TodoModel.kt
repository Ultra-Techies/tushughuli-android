package com.todoist_android.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoModel(
    var title: String? = null,
    var description: String? = null,
    var due_date: String?= null,
    var id: String? = null,
    var status: String? = null

):Parcelable
