package com.tom.learnkoltin.common.twilio_voice

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tom.learnkoltin.R
import com.tom.learnkoltin.presentation.main.MainActivity

class CallingService : Service() {
    companion object {
        const val CHANNEL_ID = "Calling channel id"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showIncomingCallPopup()
        return START_STICKY
    }

    @SuppressLint("RemoteViewLayout")
    private fun showIncomingCallPopup() {
        val hangupIntent = Intent(applicationContext, HangUpBroadcast::class.java)
        val hangupPendingIntent = PendingIntent.getBroadcast(applicationContext, 0, hangupIntent, PendingIntent.FLAG_IMMUTABLE)
        val incomingCallIntent = Intent(applicationContext, IncomingCallActivity::class.java)
        val incomingCallPendingIntent = PendingIntent.getActivity(applicationContext, 0, incomingCallIntent, PendingIntent.FLAG_IMMUTABLE)
        val answerIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val answerPendingIntent = PendingIntent.getActivity(applicationContext, 0, answerIntent, PendingIntent.FLAG_IMMUTABLE)

        val customView = RemoteViews(packageName, R.layout.incoming_call_popup).apply {
            setOnClickPendingIntent(R.id.btnAcceptCall, answerPendingIntent)
            setOnClickPendingIntent(R.id.btnRejectCall, hangupPendingIntent)
        }

        createNotificationChanel()
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContent(customView)
            .setFullScreenIntent(incomingCallPendingIntent, true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVibrate(longArrayOf(0, 500, 1000))
            .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.incoming))
            .setAutoCancel(true)
        startForeground(1024, notification.build())
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val name = "Incoming call"
            val important = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, important)
            channel.setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.incoming), audioAttributes)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
