package net.latinus.pushapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.latinus.pushapp.Entidades.Bandeja;
import net.latinus.pushapp.Utilidades.Utilidades;

public class DetalleBandejaActivity extends AppCompatActivity {

    TextView tituloBandeja,contenidoBandeja,fechaBandeja,dataBandeja,urlBandeja;

    Button btnGuardar;

    private String txttituloBandeja;
    private String txtcontenidoBandeja;

    private String txtfechaBandeja;



    private String txtdataBandeja;
    private String txtimagenBandeja;
    private String txturlBandeja;

    ConexionSQLiteHelper conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_bandeja);

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);



        tituloBandeja=(TextView)findViewById(R.id.textTitulo);
        contenidoBandeja=(TextView)findViewById(R.id.textContenido);
        fechaBandeja=(TextView)findViewById(R.id.textFecha);
        dataBandeja=(TextView)findViewById(R.id.textData);
        urlBandeja=(TextView)findViewById(R.id.textUrl);
        btnGuardar = (Button)findViewById(R.id.btnGuardarBandeja);




        Bundle objetoEnviado=getIntent().getExtras();
        Bandeja user=null;

        if(objetoEnviado!=null){
            user= (Bandeja)objetoEnviado.getSerializable("bandeja");



            txttituloBandeja=user.getTituloBandeja();
            txtcontenidoBandeja=user.getContenidoBandeja();
            txtfechaBandeja=user.getFechaBandeja();
            txtdataBandeja=user.getDataBandeja();
            txtimagenBandeja=user.getImagenBandeja();
            txturlBandeja=user.getUrlBandeja();



            tituloBandeja.setText(txttituloBandeja);
            contenidoBandeja.setText(txtcontenidoBandeja);
            fechaBandeja.setText(txtfechaBandeja);
            dataBandeja.setText(txtdataBandeja);
            urlBandeja.setText(txturlBandeja);

            new CargarImagenTask((ImageView) findViewById(R.id.imageViewPush)) .execute(txtimagenBandeja);



        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                try {




                    SQLiteDatabase db = conn.getWritableDatabase();


                    ContentValues values = new ContentValues();

                        values.put(Utilidades.CAMPO_TITULO_GUARDADOS, txttituloBandeja);

                        values.put(Utilidades.CAMPO_CONTENIDO_GUARDADOS, txtcontenidoBandeja);

                        values.put(Utilidades.CAMPO_FECHA_GUARDADOS, txtfechaBandeja);

                        values.put(Utilidades.CAMPO_DATA_GUARDADOS, txtdataBandeja);

                        values.put(Utilidades.CAMPO_IMAGEN_GUARDADOS, txtimagenBandeja);


                        values.put(Utilidades.CAMPO_URL_GUARDADOS, txturlBandeja);



                    db.insert(Utilidades.TABLA_GUARDADOS, Utilidades.CAMPO_ID_TABLA_GUARDADOS, values);

                    db.close();



                    Toast.makeText(getApplicationContext(), "Notificación Guardada", Toast.LENGTH_SHORT).show();

                    btnGuardar.setEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);




                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Error al Guardar Notificación", Toast.LENGTH_LONG).show();



                }






            }
        });



        btnGuardar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.getBackground().setAlpha(150);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });
    }
}
