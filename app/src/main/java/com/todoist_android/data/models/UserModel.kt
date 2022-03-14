package com.todoist_android.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var id: String? = null,
    var display_name: String? = null,
    var email: String? = null,
    var photo: String? = null,
    var username: String? = null,
    var password: String? = null

):Parcelable