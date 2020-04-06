package net.latinus.pushapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.latinus.pushapp.Utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context){
        super(context, "bd_usuarios", null, 1);
    }

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_BANDEJA);
        db.execSQL(Utilidades.CREAR_TABLA_GUARDADOS);
        db.execSQL(Utilidades.CREAR_TABLA_EMPRESAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_BANDEJA);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_GUARDADOS);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_EMPRESAS);


        onCreate(db);
    }




}
