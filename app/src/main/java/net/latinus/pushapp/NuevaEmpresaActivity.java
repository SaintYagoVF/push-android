package net.latinus.pushapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import net.latinus.pushapp.Utilidades.ServiciosHttp;
import net.latinus.pushapp.Utilidades.Utilidades;

public class NuevaEmpresaActivity extends AppCompatActivity {

    Button btnNuevo;

    //QR
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    //SQL
    ConexionSQLiteHelper conn;

    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Token = "tokenKey";


    public static final String Registrado = "registradoKey";
    public static final String IdEmpresa = "idEmpresaKey";
    public static final String NombreEmpresa = "nombreEmpresaKey";

    SharedPreferences sharedpreferences;

    String[] parts;


    String nombreEmpresa=null;
    Integer nombreApp=null;


    //ProgressDialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_empresa);

        //SQL
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);


        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        btnNuevo=(Button)findViewById(R.id.btnRegistroNuevaEmpresa);


        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_REQUEST_CODE);
                    }else{
                        Intent i = new Intent(NuevaEmpresaActivity.this, QrCodeActivity.class);
                        startActivityForResult( i,REQUEST_CODE_QR_SCAN);

                    }



                }else{

                    Intent i = new Intent(NuevaEmpresaActivity.this,QrCodeActivity.class);
                    startActivityForResult( i,REQUEST_CODE_QR_SCAN);

                }
                } catch(Exception e) {

                    e.printStackTrace();
                }

            }


        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {


            case MY_CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(NuevaEmpresaActivity.this,QrCodeActivity.class);
                    startActivityForResult( i,REQUEST_CODE_QR_SCAN);


                } else {

                    Toast.makeText(this, "Necesita Permiso de Cámara", Toast.LENGTH_LONG).show();

                }
                break;
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode != Activity.RESULT_OK) {
            hideDialog();
            Log.i("qrresultado", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(NuevaEmpresaActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("No se pudo escanear el código QR");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            final String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.i("qrresultado", "Have scan result in your app activity :" + result);


            pDialog.setMessage("Por favor, espere....");
            pDialog.setTitle("Registrando");


            pDialog.setCancelable(true);
            showDialog();

            parts = result.split(";");

            for(int i=0;i<parts.length;i++){
                Log.i("qrstring",parts[i]);



                if(parts[i].contains("idEmpresa=")){


                    nombreApp=Integer.parseInt(parts[i].substring(10));
                }

                if(parts[i].contains("nombre=")){
                    nombreEmpresa=parts[i].substring(7);


                }
            }
            Log.i("qrstring", nombreEmpresa);
            Log.i("qrstring", nombreApp.toString());


            try {

                registroBase(result);

                try {

                    String token=sharedpreferences.getString(Token,"");
                    ServiciosHttp.nuevaEmpresaAPI(getApplicationContext(),token,NuevaEmpresaActivity.this,nombreApp);


                } catch (Exception ex) {
                    Log.d("ErrorLogin", ex.toString());
                    hideDialog();
                }


            } catch (Exception ex) {

                hideDialog();

                Log.d("ErrorLogin", ex.toString());

                AlertDialog alertDialog = new AlertDialog.Builder(NuevaEmpresaActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Código QR incorrecto");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 3s = 3000ms
                    hideDialog();

                }
            }, 3000);


        }
    }


    //Progress Dialog
    //Show Dialog
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    //Hide Dialog
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void registroBase(String result){
        String empresa2="";
        String app2;
        Integer idempresa2=1;



        String parts2[] = result.split(";");

        for(int i=0;i<parts2.length;i++){
           // Log.i("qrstring",parts2[i]);

            if(parts2[i].contains("idEmpresa=")){


                idempresa2=Integer.parseInt(parts[i].substring(10));
            }

            if(parts2[i].contains("nombre=")){
                empresa2=parts[i].substring(7);


            }

        }

        try {


            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putBoolean(Registrado, true);

            editor.commit();



        } catch (Exception e) {


            e.printStackTrace();
        }

        try {


            SharedPreferences.Editor editor2 = sharedpreferences.edit();

            editor2.putInt(IdEmpresa, idempresa2);

            editor2.commit();



        } catch (Exception e) {


            e.printStackTrace();
        }

        try {


            SharedPreferences.Editor editor3 = sharedpreferences.edit();

            editor3.putString(NombreEmpresa, empresa2);

            editor3.commit();



        } catch (Exception e) {


            e.printStackTrace();
        }

        /*
        SQLiteDatabase db = conn.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID_TABLAEXTERNA_EMPRESAS,idempresa2);
        values.put(Utilidades.CAMPO_NOMBRE_EMPRESAS, empresa2);




        db.insert(Utilidades.TABLA_EMPRESAS, Utilidades.CAMPO_ID_TABLA_EMPRESAS, values);

        db.close();
        */
        //Toast.makeText(getApplicationContext(), "Cliente Creado", Toast.LENGTH_LONG).show();

    }




}





