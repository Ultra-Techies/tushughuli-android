package com.todoist_android.ui.home

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.todoist_android.data.responses.TasksResponseItem
import com.todoist_android.ui.getTimeDifference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class NotificationService : Service() {

    var bundles:Bundle = Bundle()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotificationService", "Notification Services is running...")

        //receive our list of tasks from the intent
        if(intent !=null)
        {
            try {
                bundles = intent.extras!!.get("bundle") as Bundle
            }
            catch (e:Exception)
            {
                Log.d("NotificationService", "Error: $e")
            }

            if(bundles!=null)
            {
                val tasksList = bundles.getSerializable("data") as ArrayList<TasksResponseItem>
                Log.d("NotificationService", "Passed Data: "+tasksList.size)

                //sort tasksList by due date ascending
                tasksList.sortBy { it.due_date }

                Log.d("NotificationService", "Sorted Data: $tasksList")

                //loop through tasksList
                for(i in 0 until tasksList.size){
                    var content = tasksList[i].description
                    var hours = getTimeDifference(tasksList[i].due_date!!)[1]
                    var mins = getTimeDifference(tasksList[i].due_date!!)[2]
                    var title = tasksList[i].title

                    if (hours == 0 && mins > 0 && mins < 60) {
                        //check to see if mins is within 1 to 30
                        title = "${tasksList[i].title} is due in $mins mins"
                        if (mins <= 30) {
                            content = "You have $mins minutes left to complete $title"
                            //send notification
                            val notifier = Notifier(this)
                            notifier.sendNotification(title, content.toString(), tasksList[i].description.toString())
                        }
                    }
                    else if (hours > 0 && mins > 0 && mins < 60) {
                        //check to see if hours is within 1 to 6
                        if (hours <= 6) {
                            title = "${tasksList[i].title} is due in $hours hours and $mins mins"
                            content = "You have $hours hours and $mins minutes left to complete $title"

                            //send notification
                            val notifier = Notifier(this)
                            notifier.sendNotification(title, content.toString(), tasksList[i].description.toString())
                        }
                    }
                    else if (hours > 0 && mins == 0) {
                        //check to see if hours is within 7 to 12
                        if (hours <= 12) {
                            title = "${tasksList[i].title} is due in $hours hours"
                            content = "You have $hours hours left to complete $title"

                            //send notification
                            val notifier = Notifier(this)
                            notifier.sendNotification(title, content.toString(), tasksList[i].description.toString())
                        }
                    }
                    else if (hours == 0 && mins == 0) {
                        title = "${tasksList[i].title} is overdue"
                        //send notification
                        val notifier = Notifier(this)
                        notifier.sendNotification(title, content.toString(), tasksList[i].description.toString())
                    }
                    else {
                        title = "${tasksList[i].title} is due soon"
                    }

                    var delay = (hours * 60) + mins

                    //only trigger runnable if minutes is not -1, this will make sure only due tasks are shown
                    if(mins >= 0){
                        val runner = Runnable {
                            val notifier = Notifier(this)
                            if(!title.isNullOrBlank()){
                                notifier.sendNotification(title, content.toString(), tasksList[i].description.toString())
                            }
                        }
                        val handler = Handler()
                        handler.postDelayed(runner,1000*delay.toLong()*60)
                        Log.d("NotificationService", "$title")
                    }
                }
            }
        }

        //wait for 30 minutes then restart service for a fresh check
        GlobalScope.launch(Dispatchers.Main) {
            delay(TimeUnit.SECONDS.toMillis(1800))
            if (intent != null) {
                onTaskRemoved(intent)
            }
        }


        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }
}