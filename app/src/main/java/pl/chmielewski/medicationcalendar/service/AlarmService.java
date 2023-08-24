package pl.chmielewski.medicationcalendar.service;


import static pl.chmielewski.medicationcalendar.application.App.CHANNEL_ID;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MEDICAMENT_ADDITIONAL_INFO;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MEDICAMENT_DOSE;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MEDICAMENT_NAME;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.activities.RingActivity;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String alarmTitle = String.format("%s Alarm", intent.getStringExtra(MEDICAMENT_NAME));
        String alarmText = String.format("Nazwa leku: %s \nDawka leku: %s\nDodatkowe informacje: %s\n",intent.getStringExtra(MEDICAMENT_NAME), intent.getStringExtra(MEDICAMENT_DOSE),intent.getStringExtra(MEDICAMENT_ADDITIONAL_INFO));
        Intent notificationIntent = new Intent(this, RingActivity.class);
        notificationIntent.putExtra("alarmText",alarmText);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle() .bigText(alarmText);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText(alarmText)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .setStyle(bigTextStyle)
                .build();
        startForeground(1, notification);

        mediaPlayer.start();

        long[] pattern = { 0, 100, 1000 };
        vibrator.vibrate(pattern, 0);



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
