package net.latinus.pushapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LauncherActivity extends AppCompatActivity {

    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Token = "tokenKey";




    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(sharedpreferences.getString(Token,"")==""){
            startActivity(new Intent(LauncherActivity.this, LoginActivity.class));

        }else{
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));

        }

        finish();
    }
}
