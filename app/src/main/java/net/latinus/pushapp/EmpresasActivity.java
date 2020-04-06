package net.latinus.pushapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.latinus.pushapp.Entidades.Bandeja;
import net.latinus.pushapp.Entidades.Empresas;
import net.latinus.pushapp.Utilidades.ServiciosHttp;
import net.latinus.pushapp.Utilidades.Utilidades;

import java.util.ArrayList;

public class EmpresasActivity extends AppCompatActivity {

    ListView listViewEmpresa;
    ArrayList<String> listaInformacion;
    ArrayList<Empresas> listaEmpresa;
    private ArrayAdapter adaptador;

    ConexionSQLiteHelper conn;


    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Token = "tokenKey";

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        listViewEmpresa= (ListView)findViewById(R.id.empresasListView);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);

        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        llenarCamposListView();





        listViewEmpresa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Integer idTabla=listaEmpresa.get(position).getIdEmpresa();

                final Integer idTabla_externa=listaEmpresa.get(position).getId_tabEmpresa();
                //Toast.makeText(getActivity().getApplicationContext(),"Click Largo, id Tabla es:"+idTabla,Toast.LENGTH_SHORT).show();

                // Ask the user if they have used the registration code.
                Utilidades.showConfirmationDialog(EmpresasActivity.this, getString(R.string.confirm_empresa), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

                            try{

                                SQLiteDatabase db=conn.getWritableDatabase();

                                // db.execSQL("delete from "+ Utilidades.TABLA_REG_CLIENTE+" where "+Utilidades.CAMPO_ID_TABLA_REG_CLIENTE+" in "+idTabla);
                                String sql="delete from "+Utilidades.TABLA_EMPRESAS+" where "+Utilidades.CAMPO_ID_TABLA_EMPRESAS+"="+idTabla+"";

                                db.execSQL(sql);

                                db.close();



                                String token=sharedpreferences.getString(Token,"");
                                ServiciosHttp.desvincularEmpresaAPI(getApplicationContext(),token,idTabla_externa);



                                llenarCamposListView();


                            }

                            catch (Exception ex){
                                Toast.makeText(getApplicationContext(), "No se pudo eliminar la Empresa" , Toast.LENGTH_SHORT).show();


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
    }



    private void llenarCamposListView() {

        try {

            SQLiteDatabase db = conn.getReadableDatabase();

            Empresas usuario = null;
            listaEmpresa = new ArrayList<Empresas>();
            //select * from usuarios
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_EMPRESAS, null);

            while (cursor.moveToNext()) {
                usuario = new Empresas();
                usuario.setIdEmpresa(cursor.getInt(0));
                usuario.setId_tabEmpresa(cursor.getInt(1));
                usuario.setNombreEmpresa(cursor.getString(2));
                usuario.setLogoEmpresa(cursor.getString(3));

                Log.d("empresas","id"+cursor.getInt(0));
                Log.d("empresas","id_Ext"+cursor.getInt(1));
                Log.d("empresas","nombre"+cursor.getString(2));
                Log.d("empresas","logo"+cursor.getString(3));



                listaEmpresa.add(usuario);
            }
            obtenerLista();

        }
        catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){

                Toast.makeText(getApplicationContext(), "No existen Datos de Clientes", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();

        for (int i=0; i<listaEmpresa.size();i++){
            listaInformacion.add(i+1+") "+listaEmpresa.get(i).getNombreEmpresa());

        }

        adaptador=new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaInformacion);
        listViewEmpresa.setAdapter(adaptador);
    }



}
