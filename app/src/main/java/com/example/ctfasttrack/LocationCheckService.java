package com.example.ctfasttrack;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static com.example.ctfasttrack.LocationTrack.location;

public class LocationCheckService extends Service {
    CountDownTimer ct1;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    int distanceinmeter=50;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();

         ct1=new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if(!preferences.getString("tolatitude","").equals("")&&location!=null)
                {
                    Location locationA = new Location("point A");

                    locationA.setLatitude(location.getLatitude());
                    locationA.setLongitude(location.getLongitude());

                    Location locationB = new Location("point B");

                    locationB.setLatitude(Double.parseDouble(preferences.getString("tolatitude","")));
                    locationB.setLongitude(Double.parseDouble(preferences.getString("tolongitude","")));

                    float distance = locationA.distanceTo(locationB);

                    if(distance<distanceinmeter)
                    {
                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.bus1)
                                        .setContentTitle("CTFastTrack")
                                        .setContentText("You have just reached the station. Please give your valuable feedback");

                        Intent notificationIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(contentIntent);
                        builder.setAutoCancel(true);

                        // Add as notification
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(0, builder.build());


                        editor.putString("tolatitude","").commit();
                    }else
                    {
                        ct1.start();
                    }
                }

            }
        };
        ct1.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
