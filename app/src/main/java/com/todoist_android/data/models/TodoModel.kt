package com.todoist_android.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoModel(
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var reminder: String? = null,
    var due_date: String?= null,
    var todoId: Int? = null,
    var status: String? = null

) : Parcelable{

    fun getSampleTodo(): TodoModel{
        return TodoModel(
            id = 123,
            title = "Do homework",
            description ="Do homework",
            reminder ="2:00 pm",
            due_date =" 12/04/2022",
            todoId =1,
            status ="created"

        )
    }

}