package com.todoist_android.ui.home

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class NotificationService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotificationService", "onStartCommand")
        val title = intent?.getStringExtra("title") ?: ""
        val content = intent?.getStringExtra("content") ?: ""
        val delayMins = intent?.getIntExtra("delay", 0) ?: 0L

        val runner = Runnable {
            val notifier = Notifier(this)
            notifier.sendNotification(title, content)
        }

        val handler = Handler()
        handler.postDelayed(runner,1000*delayMins.toLong()*60)

        return START_STICKY
    }
}