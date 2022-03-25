package com.todoist_android.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.todoist_android.R
import com.todoist_android.data.responses.TasksResponseItem
import com.todoist_android.ui.getTimeDifference
import kotlinx.android.synthetic.main.listitem_item.view.*

class ToDoAdapter (private val objects: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var onEditTaskCallback : (() -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = objects[position]
        if (holder is ViewHolder && item is TasksResponseItem) {
            holder.duetime.text = formatTime(item.dueDate, item.status)
            holder.tv.text = item.title
            //show status_icon
            if (item.status == "created") {
                holder.status_icon.setImageResource(R.drawable.black_circle)
            }
            else if (item.status == "progress") {
                holder.status_icon.setImageResource(R.drawable.yellow_circle)
            }
            else if (item.status == "done") {
                holder.status_icon.setImageResource(R.drawable.done_circle)
            }
            else {
                holder.status_icon.setImageResource(R.drawable.grey_circle)
            }

        } else if (holder is HeaderViewHolder && item is String) {
            //if item is "Created" change to "Todo"
            if (item == "Created") {
                holder.tv.text = "Todo"
            } else {
                holder.tv.text = item
            }
        }

        holder.itemView.setOnClickListener {
            if (holder is HeaderViewHolder && item is String) {
                //Do nothing: a cool feature to work on would be to collapse the list or expand it
            } else {
                val bottomSheetEditTaskFragment = BottomSheetEditTaskFragment.newInstance(item as TasksResponseItem){
                    onEditTaskCallback?.invoke()
                }
                bottomSheetEditTaskFragment.show( (holder.itemView.context as AppCompatActivity).supportFragmentManager, "edit_task" )
            }
        }

    }

    private fun formatTime(dueDate: String?, status: String?): CharSequence? {
        if (dueDate == null) {
            return null
        }
        val timeDifference = getTimeDifference(dueDate.trim())
        return if (timeDifference == null) {
            dueDate
        } else {
            if(status.toString().lowercase() == "done") {
                "Completed"
            }
            else if(timeDifference[0] > 0) {
                "Due in ${timeDifference[0]} days"
            }
            else if (timeDifference[1] > 0) {
                "Due in ${timeDifference[1]} hours"
            }
            else if (timeDifference[2] > 0) {
                "Due in ${timeDifference[2]} minutes"
            }
            else {
                "Task is overdue"
            }
        }
    }

    override fun getItemCount() = objects.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            HeaderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.listitem_header, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.listitem_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (objects[position] is TasksResponseItem) 1 else 0
    }

    inner class HeaderViewHolder(v: View) : StickyViewHolder(v) {
        val tv = v.tv
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv = v.tv
        val status_icon = v.status_icon
        val duetime = v.duetime
    }
}