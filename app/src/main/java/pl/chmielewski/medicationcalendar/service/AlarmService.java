package pl.chmielewski.medicationcalendar.service;


import static pl.chmielewski.medicationcalendar.application.App.CHANNEL_ID;
import static pl.chmielewski.medicationcalendar.broadcastReceiver.AlarmBroadcastReceiver.ALARM_ID;

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
import pl.chmielewski.medicationcalendar.ring.RingActivity;
import pl.chmielewski.medicationcalendar.data.alarm.Alarm;
import pl.chmielewski.medicationcalendar.data.alarm.AlarmRepository;
import pl.chmielewski.medicationcalendar.data.medicament.Medicament;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private NotificationManager notificationManager;
    private AlarmRepository alarmRepository;
   private int alarmId;

    @Override
    public void onCreate() {
        super.onCreate();
        alarmRepository = new AlarmRepository(getApplication());
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Alarm alarm =(Alarm) intent.getSerializableExtra("ALARM_OBJECT");
        Medicament medicament =(Medicament) intent.getSerializableExtra("MEDICAMENT_OBJECT");
        alarmId=intent.getIntExtra(ALARM_ID,0);
        String alarmTitle = String.format("%s Alarm", medicament.getMedicamentName());
        String alarmText = String.format("Dawka leku: %s\nDodatkowe informacje: %s\n",
                medicament.getMedicamentDose()
                ,medicament.getMedicamentAdditionalInfo());

        Intent notificationIntent = new Intent(this, RingActivity.class);
        notificationIntent.putExtra("alarmText",alarmText);
        notificationIntent.putExtra("MEDICAMENT_OBJECT",medicament);
        notificationIntent.putExtra("ALARM_OBJECT",alarm);
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

            if (alarmId != 0 && alarm!=null) {
                alarm.setStarted(false);
                alarmRepository.update(alarm);

            }

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
