package net.latinus.pushapp.Utilidades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.content.SharedPreferences;

import net.latinus.pushapp.ConexionSQLiteHelper;
import net.latinus.pushapp.LauncherActivity;
import net.latinus.pushapp.MainActivity;
import net.latinus.pushapp.RegistroActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class ServiciosHttp {


    //ProgressDialog
   public static ProgressDialog pDialog;


   //SQL

    public static ConexionSQLiteHelper conn;

    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String Token = "tokenKey";

    public static final String Registrado = "registradoKey";
    public static final String IdEmpresa = "idEmpresaKey";
    public static final String NombreEmpresa = "nombreEmpresaKey";

    static SharedPreferences sharedpreferences;


    public static Boolean pushBienvenida(final Context context, String oneSignalToken, String usuario){



        String url = "https://onesignal.com/api/v1/notifications";

        String[] player_id = {oneSignalToken};



        RequestQueue queue = Volley.newRequestQueue(context);

        Map<String, String> texto= new HashMap<String, String>();
        texto.put("en","Estimado/a "+usuario+".¡Te has registrado correctamente! Aprovecha todos los beneficios que te brindamos a través de nuestra App");

        Map<String, String> titulo= new HashMap<String, String>();
        titulo.put("en","¡Bienvenido/a a GetNote!");


        Map<String, Object> postParam= new HashMap<String, Object>();
        postParam.put("app_id","1bfb9336-dcbb-4154-b0c3-ca2de049c1cf");
        postParam.put("include_player_ids",player_id);
        postParam.put("android_accent_color","F1993D");
        postParam.put("small_icon","ic_push2");
        postParam.put("large_icon","https://img.imagenescool.com/ic/bienvenidos/bienvenidos_002.jpg");
        postParam.put("big_picture","https://img.imagenescool.com/ic/bienvenidos/bienvenidos_002.jpg");
        postParam.put("contents",texto);
        postParam.put("headings",titulo);



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "ErrorPush: " + error.getMessage());



               // Toast.makeText(context, "ErrorPush : "+error.getMessage(),  Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic MzA3NjEwYjUtZWZlYy00Njc5LWE2Y2UtYzQ5YjMwNDRhNTVj");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);

        return true;

    }


    public static boolean registrarUsuarioAPI(final Context context, String usuarioRegistro, String emailRegistro, String fechaRegistro, String claveRegistro, String telefonoRegistro){




        String url = "http://192.168.100.25:8080/api/auth/registrar";

        String[] rol = {"ROLE_MOVIL_PUSH"};



        RequestQueue queue = Volley.newRequestQueue(context);



        Map<String, Object> postParam= new HashMap<String, Object>();
        postParam.put("name",usuarioRegistro);
        postParam.put("email",emailRegistro);
        postParam.put("username",emailRegistro);
        postParam.put("fechaNacimiento",fechaRegistro);
        postParam.put("role",rol);
        postParam.put("password",claveRegistro);
        postParam.put("telefono",telefonoRegistro);




        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {



                        Log.d("RespuestaRegistro", response.toString());


                        try {

                            JSONObject jObjstatus = response;

                            Boolean status=jObjstatus.getBoolean("respuesta");

                            if(status==true) {

                                //loginUsuarioAPI();



                            }

                            else {



                                Toast.makeText(context, "Ya existe un correo similar, registrado en la plataforma", Toast.LENGTH_LONG).show();

                            }






                        } catch (JSONException e) {


                            e.printStackTrace();


                            Toast.makeText(context,
                                    "Registro Fallido. Inténtelo más tarde",  Toast.LENGTH_LONG).show();


                        }





                        //   mTextViewResultado.append(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("APIRegistro", "ErrorPush: " + error.getMessage());



                Toast.makeText(context,
                        "ErrorPush : "+error.getMessage(),  Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);


        return true;
    }



    public static void loginUsuarioAPI(final Context context, String emailRegistro, String claveRegistro, final String oneSignalToken, final Double latitud, final Double longitud, final String usuarioRegistro, final ArrayList<Integer> listaEmpresas, final Activity activity){

        // Progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);


        final String[] token = {""};

        String url = "http://192.168.100.25:8080/api/auth/login";




        RequestQueue queue = Volley.newRequestQueue(context);



        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("username",emailRegistro);
        postParam.put("password",claveRegistro);




        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {



                        Log.d("RespuestaLogin", response.toString());


                        try {

                            JSONObject jObjstatus = response;

                            Boolean status=jObjstatus.getBoolean("message");

                            if(status==true) {

                                String tokenInterno=jObjstatus.getString("accessToken");

                               token[0] = tokenInterno;


                                Log.d("tokenes",token[0]);

                                //SharedPreferences

                                sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                                try {

                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    editor.putString(Token, tokenInterno);

                                    editor.commit();

                                    Log.d("tokenes","Token Guardado");

                                    crearTokenAPI(context,oneSignalToken,latitud,longitud,usuarioRegistro,listaEmpresas,activity);



                                } catch (Exception e) {

                                    Toast.makeText(context, "No se pudo loguear en el sistema. Inténtelo más tarde", Toast.LENGTH_LONG).show();

                                    e.printStackTrace();

                                }




                            }

                            else {

                                Toast.makeText(context, "No se pudo loguear en el sistema. Inténtelo más tarde", Toast.LENGTH_LONG).show();

                            }






                        } catch (JSONException e) {


                            e.printStackTrace();


                            Toast.makeText(context,
                                    "Registro Fallido. Inténtelo más tarde",  Toast.LENGTH_LONG).show();
                        }





                        //   mTextViewResultado.append(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "ErrorPush: " + error.getMessage());

                //Log.d("ErrorPush:  ",error.getMessage());



             Toast.makeText(context,"Nombre de Usuario o contraseña inválidos",  Toast.LENGTH_LONG).show();




            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);




    }



    public static void crearTokenAPI(final Context context, final String oneSignalToken, final Double latitud, final Double longitud, final String usuarioRegistro, final ArrayList<Integer> listaEmpresas, final Activity activity){




        String url = "http://192.168.100.25:8080/api/movil/actualizarTokenOneSignal";




        RequestQueue queue = Volley.newRequestQueue(context);

        final String token=sharedpreferences.getString(Token,"");

        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("authHeader",token);
        postParam.put("tokenOneSignal",oneSignalToken);




        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {



                        Log.d("RespuestaToken", response.toString());


                        try {

                            JSONObject jObjstatus = response;

                            Boolean status=jObjstatus.getBoolean("respuesta");

                            if(status==true) {


                               crearUbicacionAPI(context,token,latitud,longitud,usuarioRegistro,oneSignalToken,listaEmpresas,activity);


                            }

                            else {
                                //hideDialog();
                                Toast.makeText(context, "No se pudo loguear en el sistema. Inténtelo más tarde", Toast.LENGTH_LONG).show();

                            }






                        } catch (JSONException e) {


                            e.printStackTrace();
                           // hideDialog();

                            Toast.makeText(context,
                                    "Registro Fallido. Inténtelo más tarde",  Toast.LENGTH_LONG).show();
                        }





                        //   mTextViewResultado.append(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "ErrorPush: " + error.getMessage());



                Toast.makeText(context,
                        "ErrorPush : "+error.getMessage(),  Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                String token=sharedpreferences.getString(Token,"");

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);

    }


    public static void crearUbicacionAPI(final Context context, final String token, final Double latitud, final Double longitud, final String usuarioRegistro, final String oneSignalTok,final ArrayList<Integer> listaEmpresas, final Activity activity){




        String url = "http://192.168.100.25:8080/api/movil/agregarUbicacionMovil";

        RequestQueue queue = Volley.newRequestQueue(context);


        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("authHeader",token);
        postParam.put("latitud",latitud);
        postParam.put("longitud",longitud);




        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {



                        Log.d("RespuestaUbicacion", response.toString());


                        try {

                            JSONObject jObjstatus = response;

                            Boolean status=jObjstatus.getBoolean("respuesta");

                            if(status==true) {


                                if(sharedpreferences.getBoolean(Registrado,false)==true){

                                    pushBienvenida(context,oneSignalTok,usuarioRegistro);

                                    crearEmpresaAPI(context,token,listaEmpresas,activity);

                                }else{

                                    pushBienvenida(context,oneSignalTok,usuarioRegistro);

                                    obtenerEmpresaAPI(context,token, activity);

                                  /*  Toast.makeText(context, "¡Acceso Exitoso!", Toast.LENGTH_LONG).show();
                                    activity.finish();

                                    activity.startActivity(new Intent(activity, MainActivity.class));
                                    */

                                }


                               // crearUbicacionAPI(context,oneSignalToken,latitud,longitud);




                            }

                            else {
                                //hideDialog();
                                Toast.makeText(context, "No se pudo loguear en el sistema. Inténtelo más tarde", Toast.LENGTH_LONG).show();

                            }






                        } catch (JSONException e) {


                            e.printStackTrace();
                            // hideDialog();

                            Toast.makeText(context,
                                    "Registro Fallido. Inténtelo más tarde",  Toast.LENGTH_LONG).show();
                        }





                        //   mTextViewResultado.append(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "ErrorPush: " + error.getMessage());



                Toast.makeText(context,
                        "ErrorPush : "+error.getMessage(),  Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                String token=sharedpreferences.getString(Token,"");

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);

    }


    public static void crearEmpresaAPI(final Context context, final String token,final ArrayList<Integer> listaEmpresas, final Activity activity){


        for(int i=0; i<listaEmpresas.size();i++){


        String url = "http://192.168.100.25:8080/api/movil/subscribirEmpresa";

        RequestQueue queue = Volley.newRequestQueue(context);
        //Integer idEmpresa=sharedpreferences.getInt(IdEmpresa,0);




        Integer idEmpresa=listaEmpresas.get(i);

        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("authHeader",token);
        postParam.put("idEmpresaVincular",idEmpresa);





        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {



                        Log.d("RespuestaEmpresaReg", response.toString());


                        try {

                            JSONObject jObjstatus = response;

                            Boolean status=jObjstatus.getBoolean("respuesta");

                            if(status==true) {


                                Integer idEmpr = jObjstatus.getInt("id");

                                Log.d("regempresa Id", idEmpr.toString());

                                String nombreEmpr = jObjstatus.getString("nombre");

                                Log.d("regempresa nombre", nombreEmpr);
                                String logoEmpr = jObjstatus.getString("imagen");

                                Log.d("regempresa logo", logoEmpr);



                                try {




                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    editor.putBoolean(Registrado, false);

                                    editor.commit();

                                    conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);

                                    SQLiteDatabase db = conn.getWritableDatabase();


                                    ContentValues values = new ContentValues();
                                    values.put(Utilidades.CAMPO_ID_TABLAEXTERNA_EMPRESAS,idEmpr);
                                    values.put(Utilidades.CAMPO_NOMBRE_EMPRESAS, nombreEmpr);
                                    values.put(Utilidades.CAMPO_LOGO_EMPRESAS, logoEmpr);

                                    db.insert(Utilidades.TABLA_EMPRESAS, Utilidades.CAMPO_ID_TABLA_EMPRESAS, values);

                                    db.close();



                                } catch (Exception e) {


                                    e.printStackTrace();

                                    Log.d("regempresa error", e.toString());
                                }


                                Toast.makeText(context, "¡Registro Exitoso!", Toast.LENGTH_LONG).show();

                                activity.finish();

                                activity.startActivity(new Intent(activity, MainActivity.class));

                                // crearUbicacionAPI(context,oneSignalToken,latitud,longitud);




                            }

                            else {
                                //hideDialog();
                                Toast.makeText(context, "No se pudo loguear en el sistema. Inténtelo más tarde", Toast.LENGTH_LONG).show();

                            }






                        } catch (JSONException e) {


                            e.printStackTrace();
                            // hideDialog();

                            Toast.makeText(context,
                                    "Registro Fallido. Inténtelo más tarde",  Toast.LENGTH_LONG).show();
                        }





                        //   mTextViewResultado.append(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "ErrorPush: " + error.getMessage());



                Toast.makeText(context,
                        "ErrorPush : "+error.getMessage(),  Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                String token=sharedpreferences.getString(Token,"");

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);


    }
    }


    public static void nuevaEmpresaAPI(final Context context, final String token, final Activity activity,  final Integer idEmpresa){




        String url = "http://192.168.100.25:8080/api/movil/subscribirEmpresa";

        RequestQueue queue = Volley.newRequestQueue(context);


        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("authHeader",token);
        postParam.put("idEmpresaVincular",idEmpresa);





        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {



                        Log.d("RespuestaEmpresaReg", response.toString());


                        try {

                            JSONObject jObjstatus = response;

                            Boolean status=jObjstatus.getBoolean("respuesta");



                            if(status==true) {


                                Integer idEmpr = jObjstatus.getInt("id");

                                Log.d("regempresa Id", idEmpr.toString());

                                String nombreEmpr = jObjstatus.getString("nombre");

                                Log.d("regempresa nombre", nombreEmpr);
                                String logoEmpr = jObjstatus.getString("imagen");

                                Log.d("regempresa logo", logoEmpr);

                                // crearUbicacionAPI(context,oneSignalToken,latitud,longitud);



                                try {




                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    editor.putBoolean(Registrado, false);

                                    editor.commit();

                                    conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);

                                    SQLiteDatabase db = conn.getWritableDatabase();


                                    ContentValues values = new ContentValues();
                                    values.put(Utilidades.CAMPO_ID_TABLAEXTERNA_EMPRESAS,idEmpr);
                                    values.put(Utilidades.CAMPO_NOMBRE_EMPRESAS, nombreEmpr);
                                    values.put(Utilidades.CAMPO_LOGO_EMPRESAS, logoEmpr);

                                    db.insert(Utilidades.TABLA_EMPRESAS, Utilidades.CAMPO_ID_TABLA_EMPRESAS, values);

                                    db.close();



                                } catch (Exception e) {


                                    e.printStackTrace();

                                    Log.d("regempresa error", e.toString());
                                }



                                Toast.makeText(context, "¡Registro Exitoso!", Toast.LENGTH_LONG).show();

                                activity.finish();

                                activity.startActivity(new Intent(activity, MainActivity.class));

                            }

                            else {
                                //hideDialog();
                                Toast.makeText(context, "La empresa ya se encuentra registrada en su sistema", Toast.LENGTH_LONG).show();

                            }






                        } catch (JSONException e) {


                            e.printStackTrace();
                            // hideDialog();

                            Toast.makeText(context,
                                    "La empresa ya se encuentra registrada en su sistema",  Toast.LENGTH_LONG).show();
                        }





                        //   mTextViewResultado.append(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "ErrorPush: " + error.getMessage());



                Toast.makeText(context,
                        "ErrorPush : "+error.getMessage(),  Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                String token=sharedpreferences.getString(Token,"");

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);

    }




    public static void obtenerEmpresaAPI(final Context context, final String token, final Activity activity){




        String url = "http://192.168.100.25:8080/api/movil/obtenerEmpresas";

        RequestQueue queue = Volley.newRequestQueue(context);


        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("authHeader",token);

        JSONArray json=new JSONArray();
        json.put(postParam);




        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {


                        try {



                            conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);

                            SQLiteDatabase db=conn.getWritableDatabase();


                        Log.d("RespuestaEmpresaReg", response.toString());




                            JSONArray jsonArray = response.getJSONArray("empresaRespuesta");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject general = jsonArray.getJSONObject(i);

                                int idEmpresa = general.getInt("id");

                                String nombreEmpresa = general.getString("nombre");
                                String logoEmpresa = general.getString("imagen");

                                ContentValues values = new ContentValues();
                                values.put(Utilidades.CAMPO_ID_TABLAEXTERNA_EMPRESAS,idEmpresa);
                                values.put(Utilidades.CAMPO_NOMBRE_EMPRESAS, nombreEmpresa);
                                values.put(Utilidades.CAMPO_LOGO_EMPRESAS, logoEmpresa);



                                db.insert(Utilidades.TABLA_EMPRESAS, Utilidades.CAMPO_ID_TABLA_EMPRESAS, values);


                            }

                            db.close();
                        }
                        catch (JSONException ex){

                        }


                        Toast.makeText(context, "¡Acceso Exitoso!", Toast.LENGTH_LONG).show();
                        activity.finish();

                        activity.startActivity(new Intent(activity, MainActivity.class));

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "ErrorPush: " + error.getMessage());



                Toast.makeText(context,
                        "ErrorPush : "+error.getMessage(),  Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                String token=sharedpreferences.getString(Token,"");

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);

    }

    public static void desvincularEmpresaAPI(final Context context, final String token, final Integer idEmpresa){




        String url = "http://192.168.100.25:8080/api/movil/desvincularEmpresa";

        RequestQueue queue = Volley.newRequestQueue(context);


        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("authHeader",token);
        postParam.put("idEmpresaDesvincular",idEmpresa);





        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {



                        Log.d("RespuestaEmpresaReg", response.toString());


                        try {

                            JSONObject jObjstatus = response;

                            Boolean status=jObjstatus.getBoolean("respuesta");



                            if(status==true) {
                                Toast.makeText(context, "Empresa desvinculada, ya no recibirá notificaciones de la misma", Toast.LENGTH_LONG).show();
                            }

                            else {
                                //hideDialog();
                                Toast.makeText(context, "No se pudo desvincular a la empresa", Toast.LENGTH_LONG).show();

                            }






                        } catch (JSONException e) {


                            e.printStackTrace();
                            // hideDialog();

                            Toast.makeText(context,
                                    "No se pudo desvincular a la empresa, asegúrese de tener internet",  Toast.LENGTH_LONG).show();
                        }





                        //   mTextViewResultado.append(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "ErrorPush: " + error.getMessage());

                //Log.d("ErrorDesvinculacion ",error.getMessage());

                //Toast.makeText(context,"ErrorDesvinculacion : "+error.getMessage(),  Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {




                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);

    }
}
