package net.latinus.pushapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.latinus.pushapp.Entidades.Bandeja;
import net.latinus.pushapp.Entidades.Guardados;

public class DetalleGuardadosActivity extends AppCompatActivity {

    TextView tituloGuardado,contenidoGuardado,fechaGuardado,dataGuardado,urlGuardado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_guardados);


        tituloGuardado = (TextView) findViewById(R.id.textTituloG);
        contenidoGuardado = (TextView) findViewById(R.id.textContenidoG);
        fechaGuardado = (TextView) findViewById(R.id.textFechaG);
        dataGuardado = (TextView) findViewById(R.id.textDataG);
        urlGuardado = (TextView) findViewById(R.id.textUrlG);


        Bundle objetoEnviado = getIntent().getExtras();
        Guardados user = null;

        if (objetoEnviado != null) {
            user = (Guardados) objetoEnviado.getSerializable("guardado");


            tituloGuardado.setText(user.getTituloGuardado());
            contenidoGuardado.setText(user.getContenidoGuardado());
            fechaGuardado.setText(user.getFechaGuardado());
            dataGuardado.setText(user.getDataGuardado());
            urlGuardado.setText(user.getUrlGuardado());

            new CargarImagenTask((ImageView) findViewById(R.id.imageViewPushG)).execute(user.getImagenGuardado());


        }
    }
}
