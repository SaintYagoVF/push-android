package net.latinus.pushapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import net.latinus.pushapp.Entidades.Bandeja;
import net.latinus.pushapp.Utilidades.Utilidades;

import org.json.JSONObject;

import java.util.ArrayList;

public class ExampleNotificationOpenedHandler extends AppCompatActivity implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.

    ConexionSQLiteHelper conn;

    ListView listViewBandeja;
    ArrayList<String> listaInformacion;
    ArrayList<Bandeja> listaBandeja;

    private Context mContext;

    public ExampleNotificationOpenedHandler (Context context){

        this.mContext = context;
    }



    @Override
    public void notificationOpened(OSNotificationOpenResult result) {



        conn=new ConexionSQLiteHelper(mContext,"bd_usuarios",null,1);

        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String launchUrl = result.notification.payload.launchURL; // update docs launchUrl


        String title = result.notification.payload.title;
        String body = result.notification.payload.body;

        String bigPicture = result.notification.payload.bigPicture;

/*
        Log.i("OneSignalExample", "Título : " + title);

        Log.i("OneSignalExample", "Datos: " + body);

        Log.i("OneSignalExample", "Imagen: " + bigPicture);


        Log.i("OneSignalExample", "URL: " + launchUrl);


 */

        String fechaKey = "";
        String dataKey = null;
        Object activityToLaunch = MainActivity.class;

        if (data != null) {
            fechaKey = data.optString("fecha", null);
            dataKey= data.optString("data", null);

            if (fechaKey != null)
                Log.i("OneSignalExample", "La Fecha del push es: " + fechaKey);

            if (dataKey != null)
                Log.i("OneSignalExample", "la Data del push es: " + dataKey);
        }


        /*
      try {




            SQLiteDatabase db = conn.getWritableDatabase();


            ContentValues values = new ContentValues();
          if(title==null)
              values.put(Utilidades.CAMPO_TITULO_BANDEJA," ");
          else
            values.put(Utilidades.CAMPO_TITULO_BANDEJA, title);
          if(body==null)
              values.put(Utilidades.CAMPO_CONTENIDO_BANDEJA," ");
          else
            values.put(Utilidades.CAMPO_CONTENIDO_BANDEJA, body);
          if(fechaKey==null)
              values.put(Utilidades.CAMPO_FECHA_BANDEJA," ");
          else
            values.put(Utilidades.CAMPO_FECHA_BANDEJA, fechaKey);
          if(dataKey==null)
              values.put(Utilidades.CAMPO_DATA_BANDEJA," ");
          else
            values.put(Utilidades.CAMPO_DATA_BANDEJA, dataKey);
          if(bigPicture==null)
              values.put(Utilidades.CAMPO_IMAGEN_BANDEJA," ");
          else
            values.put(Utilidades.CAMPO_IMAGEN_BANDEJA, bigPicture);

            if(launchUrl==null)
                values.put(Utilidades.CAMPO_URL_BANDEJA," ");
            else
            values.put(Utilidades.CAMPO_URL_BANDEJA, launchUrl);



            db.insert(Utilidades.TABLA_BANDEJA, Utilidades.CAMPO_ID_TABLA_BANDEJA, values);

            db.close();





        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error al guardar Push", Toast.LENGTH_LONG).show();



        }
      */


        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            if (result.action.actionID.equals("comprar")) {
                Log.i("OneSignalExample", "button id called: " + result.action.actionID);
                activityToLaunch = ComprarActivity.class;
            } else
                Log.i("OneSignalExample", "button id called: " + result.action.actionID);


        }


      /*  // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
        Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("data", dataKey);
        Log.i("OneSignalExample", "Data Pasada = " + dataKey);
        // startActivity(intent);
        startActivity(intent);

*/


        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
        /*
           <application ...>
             <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
           </application>
        */
    }




}