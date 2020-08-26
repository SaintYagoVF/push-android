package net.latinus.pushapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class ExampleNotificationReceivedHandler extends AppCompatActivity implements OneSignal.NotificationReceivedHandler {

    private Context mContext;
    private Activity activity;
    private Intent intent;

    public ExampleNotificationReceivedHandler (Context context, Activity activity){

        this.mContext = context;
        this.activity = activity;
        this.intent= intent;
    }
    @Override
    public void notificationReceived(OSNotification notification) {

        activity.startActivity(new Intent(activity, MainActivity.class));



        JSONObject data = notification.payload.additionalData;
        String notificationID = notification.payload.notificationID;
        String title = notification.payload.title;
        String body = notification.payload.body;
        String smallIcon = notification.payload.smallIcon;
        String largeIcon = notification.payload.largeIcon;
        String bigPicture = notification.payload.bigPicture;
        String launchUrl = notification.payload.launchURL;
        String smallIconAccentColor = notification.payload.smallIconAccentColor;
        String sound = notification.payload.sound;
        String ledColor = notification.payload.ledColor;
        int lockScreenVisibility = notification.payload.lockScreenVisibility;
        String groupKey = notification.payload.groupKey;
        String groupMessage = notification.payload.groupMessage;
        String fromProjectNumber = notification.payload.fromProjectNumber;
        String rawPayload = notification.payload.rawPayload;


       // Log.i("OneSignalExample", "NotificationID received: " + notificationID);

       Log.i("OneSignalExample", "Título : " + title);

        Log.i("OneSignalExample", "Datos: " + body);

        Log.i("OneSignalExample", "Imagen: " + bigPicture);

        Log.i("OneSignalExample", "URL: " + launchUrl);


        String fechaKey;
        String dataKey = null;
        Object activityToLaunch = MainActivity.class;

        if (data != null) {

            /*

            fechaKey = data.optString("fecha", null);
            dataKey= data.optString("data", null);

            if (fechaKey != null)
                Log.i("OneSignalExample", "La Fecha del push es: " + fechaKey);

            if (dataKey != null)
                Log.i("OneSignalExample", "la Data del push es: " + dataKey);

                */
        }
    }
}

