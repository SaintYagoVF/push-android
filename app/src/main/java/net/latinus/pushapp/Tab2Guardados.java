package net.latinus.pushapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.latinus.pushapp.Entidades.Bandeja;
import net.latinus.pushapp.Entidades.Guardados;
import net.latinus.pushapp.Utilidades.Utilidades;

import java.util.ArrayList;

public class Tab2Guardados extends Fragment {


    ListView listViewGuardado;
    ArrayList<String> listaInformacion;
    ArrayList<Guardados> listaGuardados;

    private ArrayAdapter adaptador;

    ConexionSQLiteHelper conn;

    private String tituloGuardado;
    private String contenidoGuardado;

    private String fechaGuardado;



    private String dataGuardado;
    private String imagenGuardado;
    private String urlGuardado;

    Integer[] imageIDarray;

    //Fecha
    String[] dateArray;

    //Título
    String[] nameArray;

    //Info
    String[] infoArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2guardados, container, false);


        listViewGuardado= (ListView) rootView.findViewById(R.id.guardadoListView);
        conn=new ConexionSQLiteHelper(getActivity().getApplicationContext(),"bd_usuarios",null,1);


        llenarCamposListView();



       /* adaptador=new ArrayAdapter(getActivity().getApplicationContext(),R.layout.milistview,listaInformacion);
        listViewGuardado.setAdapter(adaptador);
        */
        CustomListAdapter whatever = new CustomListAdapter(getActivity(), dateArray,nameArray, infoArray, imageIDarray);
        listViewGuardado.setAdapter(whatever);




        listViewGuardado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String informacion=listaGuardados.get(pos).getTituloGuardado();



                Toast.makeText(getActivity().getApplicationContext(),informacion,Toast.LENGTH_SHORT).show();

                Guardados user2=listaGuardados.get(pos);



                Bundle bundle2=new Bundle();
                bundle2.putSerializable("guardado",user2);


                /*
                mDrawerLayout.closeDrawer(GravityCompat.START);

                */
                Intent intent5 = new Intent(getActivity(), DetalleGuardadosActivity.class);
                intent5.putExtras(bundle2);
                startActivity(intent5);
                // finish();



            }
        });

        listViewGuardado.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Integer idTabla=listaGuardados.get(position).getIdGuardado();
                //Toast.makeText(getActivity().getApplicationContext(),"Click Largo, id Tabla es:"+idTabla,Toast.LENGTH_SHORT).show();

                // Ask the user if they have used the registration code.
                Utilidades.showConfirmationDialog(getActivity(), getString(R.string.confirm_regCodeUse), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

                            try{

                                SQLiteDatabase db=conn.getWritableDatabase();

                                // db.execSQL("delete from "+ Utilidades.TABLA_REG_CLIENTE+" where "+Utilidades.CAMPO_ID_TABLA_REG_CLIENTE+" in "+idTabla);
                                String sql="delete from "+Utilidades.TABLA_GUARDADOS+" where "+Utilidades.CAMPO_ID_TABLA_GUARDADOS+"="+idTabla+"";

                                db.execSQL(sql);

                                db.close();





                                llenarCamposListView();
                                CustomListAdapter whatever = new CustomListAdapter(getActivity(), dateArray,nameArray, infoArray, imageIDarray);
                                listViewGuardado.setAdapter(whatever);

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

            SQLiteDatabase db = conn.getReadableDatabase();

            Guardados usuario = null;
            listaGuardados = new ArrayList<Guardados>();
            //select * from usuarios
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_GUARDADOS, null);

            while (cursor.moveToNext()) {
                usuario = new Guardados();
                usuario.setIdGuardado(cursor.getInt(0));
                usuario.setTituloGuardado(cursor.getString(1));
                usuario.setContenidoGuardado(cursor.getString(2));
                usuario.setFechaGuardado(cursor.getString(3));
                usuario.setDataGuardado(cursor.getString(4));
                usuario.setImagenGuardado(cursor.getString(5));
                usuario.setUrlGuardado(cursor.getString(6));


                Log.d("Título Push:", usuario.getTituloGuardado());
                Log.d("Contenido Push:", usuario.getContenidoGuardado());
                Log.d("Fecha Push:", usuario.getFechaGuardado());
                Log.d("Data Push:", usuario.getDataGuardado());
                Log.d("ImagenPush:", usuario.getImagenGuardado());
                Log.d("UrlPush:", usuario.getUrlGuardado());




                tituloGuardado= usuario.getTituloGuardado();
                contenidoGuardado=usuario.getContenidoGuardado();

                fechaGuardado=usuario.getFechaGuardado();



                dataGuardado=usuario.getDataGuardado();
                imagenGuardado=usuario.getImagenGuardado();
                urlGuardado=usuario.getUrlGuardado();




                listaGuardados.add(usuario);
            }
            obtenerLista();

        }
        catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){

                Toast.makeText(getActivity().getApplicationContext(), "No existen notificaciones todavía", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();
        dateArray=new String[listaGuardados.size()];
        nameArray=new String[listaGuardados.size()];
        infoArray=new String[listaGuardados.size()];
        imageIDarray=new Integer[listaGuardados.size()];

        for (int i=0; i<listaGuardados.size();i++){
            listaInformacion.add(listaGuardados.get(i).getTituloGuardado());


            dateArray[i] = listaGuardados.get(i).getFechaGuardado();
            nameArray[i] = listaGuardados.get(i).getTituloGuardado();
            infoArray[i] = listaGuardados.get(i).getContenidoGuardado();
            imageIDarray[i] = R.drawable.ico_push_final;

        }


    }
}