package net.latinus.pushapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.latinus.pushapp.Entidades.Bandeja;
import net.latinus.pushapp.Entidades.Empresas;
import net.latinus.pushapp.Utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


public class Tab1Bandeja extends Fragment{

    ListView listViewBandeja;
    ArrayList<String> listaInformacion;
    ArrayList<Bandeja> listaBandeja;

    private ArrayAdapter adaptador;

    ConexionSQLiteHelper conn;

    private String tituloBandeja;
    private String contenidoBandeja;

    private String fechaBandeja;



    private String dataBandeja;
    private String imagenBandeja;
    private String urlBandeja;


    Integer[] imageIDarray;

    //Fecha
    String[] dateArray;

    //Título
   String[] nameArray;

    //Info
   String[] infoArray;

   //SharedPreferences

    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String Token = "tokenKey";

    public static final String Registrado = "registradoKey";
    public static final String IdEmpresa = "idEmpresaKey";
    public static final String NombreEmpresa = "nombreEmpresaKey";

    static SharedPreferences sharedpreferences;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1bandeja, container, false);

        listViewBandeja= (ListView) rootView.findViewById(R.id.bandejaListView);
        conn=new ConexionSQLiteHelper(getActivity().getApplicationContext(),"bd_usuarios",null,1);

        sincronizarMensajes();


        //SharedPreferences

        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);




       /* adaptador=new ArrayAdapter(getActivity().getApplicationContext(),R.layout.milistview,listaInformacion);
        listViewBandeja.setAdapter(adaptador);
        */

        llenarCamposListView();

        CustomListAdapter whatever = new CustomListAdapter(getActivity(), dateArray,nameArray, infoArray, imageIDarray);
        listViewBandeja.setAdapter(whatever);

        listViewBandeja.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String informacion=listaBandeja.get(pos).getTituloBandeja();



                Toast.makeText(getActivity().getApplicationContext(),informacion,Toast.LENGTH_SHORT).show();

                Bandeja user2=listaBandeja.get(pos);



                Bundle bundle2=new Bundle();
                bundle2.putSerializable("bandeja",user2);


                /*
                mDrawerLayout.closeDrawer(GravityCompat.START);

                */
                Intent intent5 = new Intent(getActivity(), DetalleBandejaActivity.class);
                intent5.putExtras(bundle2);
                startActivity(intent5);
               // finish();



            }
        });

        listViewBandeja.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final Integer idTabla=listaBandeja.get(position).getIdBandeja();
                //Toast.makeText(getActivity().getApplicationContext(),"Click Largo, id Tabla es:"+idTabla,Toast.LENGTH_SHORT).show();

                // Ask the user if they have used the registration code.
                Utilidades.showConfirmationDialog(getActivity(), getString(R.string.confirm_regCodeUse), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

                            try{

                                SQLiteDatabase db=conn.getWritableDatabase();

                                // db.execSQL("delete from "+ Utilidades.TABLA_REG_CLIENTE+" where "+Utilidades.CAMPO_ID_TABLA_REG_CLIENTE+" in "+idTabla);
                                String sql="delete from "+Utilidades.TABLA_BANDEJA+" where "+Utilidades.CAMPO_ID_TABLA_BANDEJA+"="+idTabla+"";

                                db.execSQL(sql);

                                db.close();





                                llenarCamposListView();
                                CustomListAdapter whatever = new CustomListAdapter(getActivity(), dateArray,nameArray, infoArray, imageIDarray);
                                listViewBandeja.setAdapter(whatever);

                            }

                            catch (Exception ex){
                                Toast.makeText(getActivity().getApplicationContext(), "No se pudo eliminar la Notificación" , Toast.LENGTH_SHORT).show();


                            }
                        } else {
                            // Return back to the Activity screen
                            dialog.cancel();
                        }
                    }
                });




                return true;
            }
        });



        return rootView;



    }


    private void llenarCamposListView() {

        try {

            /*
            SQLiteDatabase db1=conn.getWritableDatabase();

            try {
                db1.execSQL("delete from " + Utilidades.TABLA_BANDEJA);
            }
            catch (SQLiteException e){
                if (e.getMessage().contains("no such table")){

                    //Toast.makeText(getApplicationContext(), "No existen Datos de Clientes", Toast.LENGTH_LONG).show();
                }
            }
            db1.close();
            */

            SQLiteDatabase db = conn.getReadableDatabase();

            Bandeja usuario = null;
            listaBandeja = new ArrayList<Bandeja>();
            //select * from usuarios
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_BANDEJA, null);

            while (cursor.moveToNext()) {
                usuario = new Bandeja();
                usuario.setIdBandeja(cursor.getInt(0));
                usuario.setIdextBandeja(cursor.getString(1));
                usuario.setTituloBandeja(cursor.getString(2));
                usuario.setContenidoBandeja(cursor.getString(3));
                usuario.setFechaBandeja(cursor.getString(4));
                usuario.setDataBandeja(cursor.getString(5));
                usuario.setImagenBandeja(cursor.getString(6));
                usuario.setUrlBandeja(cursor.getString(7));


                try{
                    Log.d("ID externo Push:", usuario.getIdextBandeja());
                    Log.d("Título Push:", usuario.getTituloBandeja());
                    Log.d("Contenido Push:", usuario.getContenidoBandeja());
                    Log.d("Fecha Push:", usuario.getFechaBandeja());
                    Log.d("Data Push:", usuario.getDataBandeja());
                    Log.d("ImagenPush:", usuario.getImagenBandeja());
                    Log.d("UrlPush:", usuario.getUrlBandeja());
                }
                catch(Exception ex){

                }





               tituloBandeja= usuario.getTituloBandeja();
              contenidoBandeja=usuario.getContenidoBandeja();

               fechaBandeja=usuario.getFechaBandeja();



                dataBandeja=usuario.getDataBandeja();
                imagenBandeja=usuario.getImagenBandeja();
                urlBandeja=usuario.getUrlBandeja();




                listaBandeja.add(usuario);
            }
            obtenerLista();

            db.close();


        }
        catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){

                Toast.makeText(getActivity().getApplicationContext(), "No existen notificaciones todavía", Toast.LENGTH_LONG).show();
            }
        }

        CustomListAdapter whatever = new CustomListAdapter(getActivity(), dateArray,nameArray, infoArray, imageIDarray);
        listViewBandeja.setAdapter(whatever);

    }

    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();

        dateArray=new String[listaBandeja.size()];
        nameArray=new String[listaBandeja.size()];
        infoArray=new String[listaBandeja.size()];
        imageIDarray=new Integer[listaBandeja.size()];

        for (int i=0; i<listaBandeja.size();i++){
            listaInformacion.add(listaBandeja.get(i).getTituloBandeja());



            dateArray[i] = listaBandeja.get(i).getFechaBandeja();
            nameArray[i] = listaBandeja.get(i).getTituloBandeja();
            infoArray[i] = listaBandeja.get(i).getContenidoBandeja();
            imageIDarray[i] = R.drawable.ico_push_final;

        }


    }

    public void sincronizarMensajes(){




        //String url = "http://192.168.100.25:8080/api/movil/obtenerMensajesPush";
        String url = "https://firmadigitaldesa.latinus.net/microservices-push-movil/api/movil/obtenerMensajesPush";

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //final String token=sharedpreferences.getString(Token,"");

        String token = ((MainActivity)getActivity()).getValuePreference();
        Log.d("tokenPush: ",token);

        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("authHeader",token);




        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            JSONArray jsonArray = response.getJSONArray("pushRespuesta");

                            if (jsonArray.length()==0){
                                Toast.makeText(getActivity().getApplicationContext(), "No has recibido promociones por el momento", Toast.LENGTH_SHORT).show();
                            }else {



                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject general = jsonArray.getJSONObject(i);

                                    String idMensaje = general.getString("id");

                                    Log.d("Mensaje Id:",idMensaje);

                                    String tituloMensaje = general.getString("nombreCampania");

                                    Log.d("Mensaje Título:",tituloMensaje);

                                    String contenidoMensaje = general.getString("textoCampania");

                                    Log.d("Mensaje Contenido:",contenidoMensaje);

                                    String imagenMensaje = general.getString("imagenCampania");

                                    Log.d("Mensaje Imagen:",imagenMensaje);

                                    String urlMensaje = general.getString("urlCampania");

                                    Log.d("Mensaje Url:",urlMensaje);



                                    try {

                                        boolean existePush=false;

                                        SQLiteDatabase db2 = conn.getReadableDatabase();


                                        Cursor cursor = db2.rawQuery("SELECT * FROM " + Utilidades.TABLA_BANDEJA, null);

                                        while (cursor.moveToNext()) {

                                           if(idMensaje.equals(cursor.getString(1)))
                                               existePush=true;

                                        }

                                        db2.close();

                                        if(existePush==true)
                                            continue;




                                        SQLiteDatabase db = conn.getWritableDatabase();

                                        ContentValues values = new ContentValues();

                                        if(idMensaje==null)
                                            values.put(Utilidades.CAMPO_ID_TABLAEXTERNA_BANDEJA," ");
                                        else
                                            values.put(Utilidades.CAMPO_ID_TABLAEXTERNA_BANDEJA, idMensaje);

                                        if(tituloMensaje==null)
                                            values.put(Utilidades.CAMPO_TITULO_BANDEJA," ");
                                        else
                                            values.put(Utilidades.CAMPO_TITULO_BANDEJA, tituloMensaje);
                                        if(contenidoMensaje==null)
                                            values.put(Utilidades.CAMPO_CONTENIDO_BANDEJA," ");
                                        else
                                            values.put(Utilidades.CAMPO_CONTENIDO_BANDEJA, contenidoMensaje);
                                       /* if(fechaKey==null)
                                            values.put(Utilidades.CAMPO_FECHA_BANDEJA," ");
                                        else
                                            values.put(Utilidades.CAMPO_FECHA_BANDEJA, fechaKey);
                                        if(dataKey==null)
                                            values.put(Utilidades.CAMPO_DATA_BANDEJA," ");
                                        else
                                            values.put(Utilidades.CAMPO_DATA_BANDEJA, dataKey);
                                            */
                                        if(imagenMensaje==null)
                                            values.put(Utilidades.CAMPO_IMAGEN_BANDEJA," ");
                                        else
                                            values.put(Utilidades.CAMPO_IMAGEN_BANDEJA, imagenMensaje);

                                        if(urlMensaje==null)
                                            values.put(Utilidades.CAMPO_URL_BANDEJA," ");
                                        else
                                            values.put(Utilidades.CAMPO_URL_BANDEJA, urlMensaje);



                                        db.insert(Utilidades.TABLA_BANDEJA, Utilidades.CAMPO_ID_TABLA_BANDEJA, values);

                                        db.close();





                                    }
                                    catch (Exception ex){
                                        Toast.makeText(getActivity().getApplicationContext(), "Error al guardar Push", Toast.LENGTH_LONG).show();



                                    }



                                }




                            }

                            llenarCamposListView();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "ErrorenMMensajes: " + error.getMessage());



                Toast.makeText(getActivity().getApplicationContext(),"ErrorMensajes : "+error.getMessage(),  Toast.LENGTH_LONG).show();
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
