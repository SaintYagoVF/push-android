package net.latinus.pushapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import net.latinus.pushapp.Utilidades.Utilidades;
import net.latinus.pushapp.Utilidades.ServiciosHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class RegistroActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    //OneSignal
    private String oneSignalToken;

    Button btnRegistrar, btnBuscarEmpresa;

    EditText txtUsuario, txtEmail, txtClave;

    TextView btnFecha;
    public static final String Token = "tokenKey";


    String usuarioRegistro,emailRegistro,claveRegistro,fechaRegistro;

    String[] parts;



    //SQL
    ConexionSQLiteHelper conn;



    ConnectivityDetector connectivityDetector;

    String nombreEmpresa=null;
   Integer nombreApp=null;

    //Datepicker
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    //ProgressDialog
    private ProgressDialog pDialog;

    //Popup

    Dialog myDialog;
    ListView listViewEmpresas;
    EditText filtroEmpresas;

    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Nombre = "nombreKey";


    public static final String Registrado = "registradoKey";
    public static final String IdEmpresa = "idEmpresaKey";
    public static final String NombreEmpresa = "nombreEmpresaKey";

    SharedPreferences sharedpreferences;

    //QR
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    //GPS
    private static final String TAG = "RegistroActivity";

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private double Latitud=0.0;
    private double Longitud=0.0;

    private LocationManager locationManager;

    //SMS
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //SQL
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_usuarios", null, 1);

        //GPS


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone

        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnRegistrar = (Button) findViewById(R.id.btnRegistroNuevaEmpresa);
        btnBuscarEmpresa = (Button) findViewById(R.id.btnRegistroObtenerEmpresas);
        btnFecha = (TextView) findViewById(R.id.btnFecha);

        txtUsuario = (EditText) findViewById(R.id.txtUsuarioRegistro);
        txtEmail = (EditText) findViewById(R.id.txtEmailRegistro);
        txtClave = (EditText) findViewById(R.id.txtClave);

        myDialog = new Dialog(this);


        //SMS
        try{

            if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                Log.d("Numero celular",mPhoneNumber);
                txtEmail.setText(mPhoneNumber);
                return;
            } else {
                requestPermission();
            }

        }catch (Exception ex){
            txtEmail.setText("No tiene SIM");
        }

        //SMS



       /*
        ArrayList<String> list;

        list = new ArrayList<String>();
        list.add("Mujer");
        list.add("Hombre");
        list.add("Otro");


        txtSexo= (Spinner) findViewById(R.id.txtSexo);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,list);
        txtSexo.setAdapter(adapter);


        InputFilter[] filters = {
                new InputFilter.AllCaps(),
                new InputFilter.LengthFilter(8),};
        txtUsuario.setFilters(filters);

   */
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //OneSignal


        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(getApplicationContext()))
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .init();


        connectivityDetector = new ConnectivityDetector(getBaseContext());


        if (connectivityDetector.checkConnectivityStatus()) {

            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();

            try {
                oneSignalToken = String.valueOf(status.getSubscriptionStatus().getUserId());

                Log.i("onesignalid", oneSignalToken);


            } catch (Exception ex) {


            }


        } else {
            connectivityDetector.showAlertDialog(RegistroActivity.this, "ERROR", "No hay conexión a Internet");
        }


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (connectivityDetector.checkConnectivityStatus()) {


                    usuarioRegistro = txtUsuario.getText().toString();
                    emailRegistro = txtEmail.getText().toString().trim();
                    claveRegistro = txtClave.getText().toString();
                    fechaRegistro = btnFecha.getText().toString().trim();

                    if (usuarioRegistro.isEmpty() || emailRegistro.isEmpty() || claveRegistro.isEmpty() || fechaRegistro.isEmpty() || claveRegistro.length() < 6) {
                        Toast.makeText(RegistroActivity.this, "Llene todos los campos, la clave debe contener almenos 6 dígitos", Toast.LENGTH_LONG).show();
                    } else {


                        try {


                            if (android.os.Build.VERSION.SDK_INT >= 23) {
                                if (checkSelfPermission(Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            MY_CAMERA_REQUEST_CODE);
                                } else {
                                    Intent i = new Intent(RegistroActivity.this, QrCodeActivity.class);
                                    startActivityForResult(i, REQUEST_CODE_QR_SCAN);

                                }


                            } else {

                                Intent i = new Intent(RegistroActivity.this, QrCodeActivity.class);
                                startActivityForResult(i, REQUEST_CODE_QR_SCAN);

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }



                    /*

                    connectivityDetector = new ConnectivityDetector(getBaseContext());



                    OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();



                    try {
                        oneSignalToken = String.valueOf(status.getSubscriptionStatus().getUserId());

                        Log.i("onesignalid",oneSignalToken );


                    }
                    catch (Exception ex){


                    }


                        if (oneSignalToken == null)
                            Toast.makeText(RegistroActivity.this, "No se pudo registrar. Inténtelo más tarde", Toast.LENGTH_SHORT).show();

                        else {



                            try {


                                final FirebaseDatabase db = FirebaseDatabase.getInstance();

                                final DatabaseReference dr1 = db.getReference("identities");

                                final DatabaseReference dr2 = db.getReference("identities/" + usuarioRegistro);

                                dr1.child("/"+usuarioRegistro).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){

                                            Toast.makeText(RegistroActivity.this,"El Usuario ya existe",Toast.LENGTH_SHORT).show();

                                        }else{


                                            dr2.child("/tokenOneSignal").setValue(oneSignalToken);
                                            nuevoUsuario();


                                            Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_LONG).show();
                                            finish();

                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            } catch (Exception ex) {
                                Toast.makeText(getApplicationContext(), "No se pudo registrar el dispositivo", Toast.LENGTH_LONG).show();
                                Log.i("errorcrearusuario", ex.toString());


                            }

                        }

                        */
                    }


                } else {
                    connectivityDetector.showAlertDialog(RegistroActivity.this, "ERROR", "No hay conexión a Internet");
                }


            }
        });

        btnBuscarEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(RegistroActivity.this, "Empresas Registradas", Toast.LENGTH_SHORT).show();




                Button btnCancelar2;
                myDialog.setContentView(R.layout.popup_empresas);


                filtroEmpresas = (EditText)myDialog.findViewById(R.id.filtroEmpresas);


                btnCancelar2 = (Button)myDialog.findViewById(R.id.btnPopupEmpCancelar);

                listViewEmpresas= (ListView)myDialog.findViewById(R.id.listViewTodasEmpresas);


                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();

                btnCancelar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
            }
        });


        txtClave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txtClave.getRight() - txtClave.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here




                        txtClave.setTransformationMethod(HideReturnsTransformationMethod.getInstance());



                        return true;
                    }
                }
                return false;
            }
        });




    }


    private void nuevoUsuario(){


            try {




                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Nombre, txtUsuario.getText().toString());

                editor.commit();



            } catch (Exception e) {


                e.printStackTrace();
            }



    }

    private void registroBase(String result){
        String empresa2="";
        String app2;
        Integer idempresa2=1;



        String parts2[] = result.split(";");

        for(int i=0;i<parts2.length;i++){
            Log.i("qrstring",parts2[i]);

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
        //Toast.makeText(getApplicationContext(), "Cliente Creado", Toast.LENGTH_LONG).show();
        */

    }

    //SMS
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }


    public void showDatePickerDialog(View v) {



        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(RegistroActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        btnFecha.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {


            case MY_CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(RegistroActivity.this,QrCodeActivity.class);
                    startActivityForResult( i,REQUEST_CODE_QR_SCAN);


                } else {

                    Toast.makeText(this, "Necesita Permiso de Cámara", Toast.LENGTH_LONG).show();

                }
                //SMS
                try{

                    TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
                            ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) !=      PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    String mPhoneNumber = tMgr.getLine1Number();
                    Log.d("Numero de celular: ",mPhoneNumber);
                    txtEmail.setText(mPhoneNumber);

                }catch (Exception ex){
                    txtEmail.setText("No tiene SIM");
                }


                break;

        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if(resultCode != Activity.RESULT_OK)
        {
            hideDialog();
            Log.i("qrresultado","COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(RegistroActivity.this).create();
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
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            final String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.i("qrresultado","Have scan result in your app activity :"+ result);
            /*
            AlertDialog alertDialog = new AlertDialog.Builder(RegistroActivity.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            */


           parts = result.split(";");

            for(int i=0;i<parts.length;i++){
                Log.i("qrstring",parts[i]);

                if(parts[i].contains("nombre=")){
                    nombreEmpresa=parts[i].substring(7);
                }
                if(parts[i].contains("idEmpresa=")){
                    nombreApp=Integer.parseInt(parts[i].substring(10));
                }
            }

            try {
                Log.i("qrstring", nombreEmpresa);
                Log.i("qrstring", nombreApp.toString());


                OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();



                try {
                    oneSignalToken = String.valueOf(status.getSubscriptionStatus().getUserId());

                    Log.i("onesignalid",oneSignalToken );


                }
                catch (Exception ex){


                }


                if (oneSignalToken == null)
                    Toast.makeText(RegistroActivity.this, "No se pudo registrar. Inténtelo más tarde", Toast.LENGTH_SHORT).show();

                else {

                    pDialog.setMessage("Por favor, espere....");
                    pDialog.setTitle("Registrando");


                    pDialog.setCancelable(true);
                    showDialog();




                    /*
                  if(ServiciosHttp.registrarUsuarioAPI(getApplicationContext(),usuarioRegistro,emailRegistro,fechaRegistro,claveRegistro)==true)
                  {

                      Log.d("APILoginTokenRecibido:","Registro Correcto");


                      try {
                          ServiciosHttp.loginUsuarioAPI(getApplicationContext(), emailRegistro, claveRegistro, oneSignalToken, Latitud, Longitud, usuarioRegistro, RegistroActivity.this);
                          nuevoUsuario();
                          registroBase(result);
                      }
                      catch (Exception ex){
                          hideDialog();
                      }


                  }


                  else{
                      hideDialog();
                  }

                */

                    loginFirebase(result);

                /*
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 3s = 3000ms
                            hideDialog();

                        }
                    }, 3000);
                    */

                }





            }
            catch (Exception ex){

                hideDialog();

                Log.d("ErrorLogin",ex.toString());

                AlertDialog alertDialog = new AlertDialog.Builder(RegistroActivity.this).create();
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




            nombreEmpresa=null;
            nombreApp=null;


        }
        else{
            hideDialog();
        }

       // hideDialog();

    }

    private void loginFirebase(final String result) {

        connectivityDetector = new ConnectivityDetector(getBaseContext());



        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();



        try {
            oneSignalToken = String.valueOf(status.getSubscriptionStatus().getUserId());

            Log.i("onesignalid",oneSignalToken );


        }
        catch (Exception ex){


        }


        if (oneSignalToken == null)
            Toast.makeText(RegistroActivity.this, "No se pudo registrar. Inténtelo más tarde", Toast.LENGTH_SHORT).show();

        else {



            try {


                final FirebaseDatabase db = FirebaseDatabase.getInstance();

                final DatabaseReference dr1 = db.getReference("identities");

                final DatabaseReference dr2 = db.getReference("identities/juan/"+usuarioRegistro);

                dr1.child("/juan/"+usuarioRegistro).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            Toast.makeText(RegistroActivity.this,"El Usuario ya existe",Toast.LENGTH_SHORT).show();

                        }else{


                            dr2.child("/tokenOneSignal").setValue(oneSignalToken);
                            nuevoUsuario();
                            registroBase(result);

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(Token, oneSignalToken);

                            editor.commit();


                            Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_LONG).show();

                            //finish();
                            //ServiciosHttp.pushBienvenida();

                            startActivity(new Intent(RegistroActivity.this, MainActivity.class));

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "No se pudo registrar el dispositivo", Toast.LENGTH_LONG).show();
                Log.i("errorcrearusuario", ex.toString());


            }

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

    //GPS



    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

       // Log.i(TAG, "Latitud: " + location.getLatitude());
        //Log.i(TAG, "Longitud: " + location.getLongitude());
       // mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
       // mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        Latitud=location.getLatitude();
        Longitud=location.getLongitude();

        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setTitle("Habilitar GPS")
                .setMessage("El GPS se encuentra deshabilitado.\nPor favor, habilítalo para " +
                        "usar esta aplicación")
                .setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }









}

