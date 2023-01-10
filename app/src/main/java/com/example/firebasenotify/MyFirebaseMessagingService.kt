package com.example.firebasenotify

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
const val chanel_id = "channel_id"
const val chanel_name="firebasenotify"
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){
           try {
               generateNotification(
                   remoteMessage.notification!!.title!!,
                   remoteMessage.notification!!.body!!
               )
           } catch (e: Exception){
               println("Error")
           }
        }
    }
    private fun generateNotification(title:String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)//show push under other app
        val pendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT)
        val builder = NotificationCompat.Builder(applicationContext, chanel_id)
            .setSmallIcon(com.google.android.material.R.drawable.mtrl_ic_cancel)//иконка
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))//вибрация
            .setOnlyAlertOnce(true)//звонок
            .setContentIntent(pendingIntent)//передаем отложенный интент

        builder.setContent(getRemoteView(title,message))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(chanel_id, chanel_name,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())



    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.example.firebasenotify",R.layout.push_notification)
        remoteViews.setTextViewText(R.id.tv_Tittle, title)
        remoteViews.setTextViewText(R.id.tv_Message, message)
        remoteViews.setImageViewResource(R.id.imageView, R.drawable.ic_launcher_background)
        return  remoteViews
    }

}