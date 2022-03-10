package com.todoist_android.data.network

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log

class NotificationService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)
        Log.d("NotificationService", "Notification Service Running...")
        //periodic polling of api/tasks and checking if any task is due in intervals of 24hr, 12hrs, 6hrs, 3hrs, 1hr
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }
    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }
}