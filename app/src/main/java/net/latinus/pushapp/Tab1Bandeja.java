package net.latinus.pushapp;

import android.content.DialogInterface;
import android.content.Intent;
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

import net.latinus.pushapp.Entidades.Bandeja;
import net.latinus.pushapp.Utilidades.Utilidades;

import org.json.JSONObject;

import java.util.ArrayList;


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1bandeja, container, false);

        listViewBandeja= (ListView) rootView.findViewById(R.id.bandejaListView);
        conn=new ConexionSQLiteHelper(getActivity().getApplicationContext(),"bd_usuarios",null,1);


        llenarCamposListView();



       /* adaptador=new ArrayAdapter(getActivity().getApplicationContext(),R.layout.milistview,listaInformacion);
        listViewBandeja.setAdapter(adaptador);
        */



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

            SQLiteDatabase db = conn.getReadableDatabase();

            Bandeja usuario = null;
            listaBandeja = new ArrayList<Bandeja>();
            //select * from usuarios
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_BANDEJA, null);

            while (cursor.moveToNext()) {
                usuario = new Bandeja();
                usuario.setIdBandeja(cursor.getInt(0));
                usuario.setTituloBandeja(cursor.getString(1));
                usuario.setContenidoBandeja(cursor.getString(2));
                usuario.setFechaBandeja(cursor.getString(3));
                usuario.setDataBandeja(cursor.getString(4));
                usuario.setImagenBandeja(cursor.getString(5));
                usuario.setUrlBandeja(cursor.getString(6));


                Log.d("Título Push:", usuario.getTituloBandeja());
                Log.d("Contenido Push:", usuario.getContenidoBandeja());
                Log.d("Fecha Push:", usuario.getFechaBandeja());
                Log.d("Data Push:", usuario.getDataBandeja());
                Log.d("ImagenPush:", usuario.getImagenBandeja());
                Log.d("UrlPush:", usuario.getUrlBandeja());




               tituloBandeja= usuario.getTituloBandeja();
              contenidoBandeja=usuario.getContenidoBandeja();

               fechaBandeja=usuario.getFechaBandeja();



                dataBandeja=usuario.getDataBandeja();
                imagenBandeja=usuario.getImagenBandeja();
                urlBandeja=usuario.getUrlBandeja();




                listaBandeja.add(usuario);
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
}
