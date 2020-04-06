package net.latinus.pushapp.Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import net.latinus.pushapp.R;

public class Utilidades {

   public static Dialog showConfirmationDialog(Activity context, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setTitle(R.string.title_confirm)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(context.getString(R.string.label_yes), listener)
                .setNegativeButton(context.getString(R.string.label_no), listener)
                .setCancelable(false);

        return builder.show();
    }
 public static Dialog showConfirmationDialog2(Context context, String msg, DialogInterface.OnClickListener listener) {
  AlertDialog.Builder builder = new AlertDialog.Builder(context);
  builder.setMessage(msg)
          .setTitle(R.string.title_confirm)
          .setIcon(android.R.drawable.ic_dialog_info)
          .setPositiveButton(context.getString(R.string.label_yes), listener)
          .setNegativeButton(context.getString(R.string.label_no), listener)
          .setCancelable(false);

  return builder.show();
 }



    //Constantes campos tabla bandeja
    public static final String TABLA_BANDEJA="bandeja";
    public static final String CAMPO_ID_TABLA_BANDEJA="idtab_bandeja";
    public static final String CAMPO_TITULO_BANDEJA="titulo_bandeja";
    public static final String CAMPO_CONTENIDO_BANDEJA="contenido_bandeja";
    public static final String CAMPO_FECHA_BANDEJA="fecha_bandeja";
    public static final String CAMPO_DATA_BANDEJA="data_bandeja";
    public static final String CAMPO_IMAGEN_BANDEJA="imagen_bandeja";
    public static final String CAMPO_URL_BANDEJA="url_bandeja";


    public static final String CREAR_TABLA_BANDEJA="CREATE TABLE " +
            ""+TABLA_BANDEJA+" ("+CAMPO_ID_TABLA_BANDEJA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_TITULO_BANDEJA+" TEXT,"+CAMPO_CONTENIDO_BANDEJA+" TEXT,"+CAMPO_FECHA_BANDEJA+" TEXT,"+CAMPO_DATA_BANDEJA+" TEXT,"+CAMPO_IMAGEN_BANDEJA+" TEXT,"+CAMPO_URL_BANDEJA+" TEXT)";


    //Constantes campos tabla guardados
    public static final String TABLA_GUARDADOS="guardados";
    public static final String CAMPO_ID_TABLA_GUARDADOS="idtab_guardados";
    public static final String CAMPO_TITULO_GUARDADOS="titulo_guardados";
    public static final String CAMPO_CONTENIDO_GUARDADOS="contenido_guardados";
    public static final String CAMPO_FECHA_GUARDADOS="fecha_guardados";
    public static final String CAMPO_DATA_GUARDADOS="data_guardados";
    public static final String CAMPO_IMAGEN_GUARDADOS="imagen_guardados";
    public static final String CAMPO_URL_GUARDADOS="url_guardados";


    public static final String CREAR_TABLA_GUARDADOS="CREATE TABLE " +
            ""+TABLA_GUARDADOS+" ("+CAMPO_ID_TABLA_GUARDADOS+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_TITULO_GUARDADOS+" TEXT,"+CAMPO_CONTENIDO_GUARDADOS+" TEXT,"+CAMPO_FECHA_GUARDADOS+" TEXT,"+CAMPO_DATA_GUARDADOS+" TEXT,"+CAMPO_IMAGEN_GUARDADOS+" TEXT,"+CAMPO_URL_GUARDADOS+" TEXT)";

 //Constantes campos tabla empresas
 public static final String TABLA_EMPRESAS="empresas";
 public static final String CAMPO_ID_TABLA_EMPRESAS="idtab_empresas";
    public static final String CAMPO_ID_TABLAEXTERNA_EMPRESAS="idtabext_empresas";
 public static final String CAMPO_NOMBRE_EMPRESAS="nombre_empresas";
    public static final String CAMPO_LOGO_EMPRESAS="logo_empresas";

 public static final String CREAR_TABLA_EMPRESAS="CREATE TABLE " +
         ""+TABLA_EMPRESAS+" ("+CAMPO_ID_TABLA_EMPRESAS+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_ID_TABLAEXTERNA_EMPRESAS+" INTEGER, "+CAMPO_NOMBRE_EMPRESAS+" TEXT, "
         +CAMPO_LOGO_EMPRESAS+" TEXT)";



}
