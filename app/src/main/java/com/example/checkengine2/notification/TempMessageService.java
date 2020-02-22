package com.example.checkengine2.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.example.checkengine2.R;
import com.example.checkengine2.controller.AverageControllerActivity;
import com.example.checkengine2.model.TemperatureDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Usługa uruchomiona, co oznacza że usługa ta działa w tle przez dowolnie długi czas i nie jest powiązana z żadną aktywnością.

public class TempMessageService extends IntentService {

    public static final int NOTIFICATION_ID = 5455;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TemperatureDao temperatureDao;
    private Handler handler;
    private Float temperatureValue, maxTemperatureValue;

    public TempMessageService() {
        super("TempMessageService");
    }

    //W metodzie onHandleIntent należy określić co ma realizować utworzona usługa. Jest ona uruchamiana w wątku w tle.
    @Override
    protected void onHandleIntent(Intent intent) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        temperatureDao = new TemperatureDao();

        maxTemperatureValue = intent.getFloatExtra("MaxValue", 25);

        int idValue, newIdValue;
        try {

            temperatureDao.executeConnection();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Powiadomienia zostały włączone",
                            Toast.LENGTH_SHORT).show();
                }
            });

            idValue = temperatureDao.getLastId();

            while (firebaseUser != null) {
                firebaseUser = firebaseAuth.getCurrentUser();
                newIdValue = temperatureDao.getLastId();
                temperatureValue = temperatureDao.getLastTempValue();
                if (temperatureValue >= maxTemperatureValue && newIdValue > idValue) {
                    idValue = newIdValue;
                    showText();
                }
            }
            if (firebaseUser == null) {
                temperatureDao.closeNotification();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                        "Powiadomienia zostaja wyłączone. Możesz je aktywować w ustawieniach",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
            temperatureDao.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z bazą danych",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //Metoda onStartCommand jest wywoływana w wątku głównym.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showText()
    {
        Intent intent = new Intent(this, AverageControllerActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(AverageControllerActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText("Przekroczona została wartość temperatury: " + temperatureValue + " stopni.")
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
