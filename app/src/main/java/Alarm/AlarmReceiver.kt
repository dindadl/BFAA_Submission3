package Alarm

import Alarm.AlarmReceiver.Companion.EXTRA_MESSAGE
import Alarm.AlarmReceiver.Companion.ID_REPEAT
import Alarm.AlarmReceiver.Companion.TIME_FORMAT
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import submission.submission2_3.MainActivity
import submission.submission2_3.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val ID_REPEAT = 101
        const val TIME_FORMAT = "09:00"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        showAlarmNotification(context!!, message)
    }

    private fun showAlarmNotification(context: Context, message: String?) {
        val channelid = "channel_github"
        val channelname = "Notification Github channel"

        val aintent = Intent(context, MainActivity::class.java)
        aintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, aintent, PendingIntent.FLAG_ONE_SHOT)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, channelid)
            .setSmallIcon(R.drawable.alarm)
            .setContentTitle("Github User Daily Reminder")
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(sound)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelid, channelname, NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(channelid)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(101, notification)
    }

    fun setRepeat(context: Context, message: String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val timeArray =
            TIME_FORMAT.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)


        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEAT, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context, "Repeating alarm is on", Toast.LENGTH_SHORT).show()
    }


    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEAT
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Repeating alarm is off", Toast.LENGTH_SHORT).show()
    }
}

