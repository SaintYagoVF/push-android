package net.latinus.pushapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


//onesignal


import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;



public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mToggle;

    TextView tituloPas;
    TextView usuarioPas;


    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Nombre = "nombreKey";


    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);



        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(getApplicationContext()))
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .init();

        //Tabs divide pantalla

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



        //Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.abrir, R.string.cerrar);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       //Navigation View



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View headerView = navigationView.getHeaderView(0);


        tituloPas = (TextView) headerView.findViewById(R.id.menuTitulo);
        usuarioPas = (TextView) headerView.findViewById(R.id.menuUsuario);

        if(sharedpreferences.getString(Nombre,"")==""){

            tituloPas.setText("TODAVÍA NO SE HA REGISTRADO");
            usuarioPas.setText("Elija la opción Registrarse ");

        }else{
            tituloPas.setText("BIENVENIDO");
            usuarioPas.setText(sharedpreferences.getString(Nombre,""));

        }


        setupNavigationDrawerContent(navigationView);









    }


   /*  @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());

    }
     */





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {



                            switch (menuItem.getItemId()) {
                                case R.id.registro:
                                    menuItem.setChecked(true);

                                    Toast.makeText(MainActivity.this, "Pantalla: " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                    mDrawerLayout.closeDrawer(GravityCompat.START);
                                    Intent intent = new Intent(MainActivity.this, NuevaEmpresaActivity.class);
                                    //intent.putExtras(basket);
                                    startActivity(intent);



                                    return true;

                                case R.id.config:
                                    menuItem.setChecked(true);

                                    Toast.makeText(MainActivity.this, "Pantalla: " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                    mDrawerLayout.closeDrawer(GravityCompat.START);
                                    Intent intent2 = new Intent(MainActivity.this, ConfiguracionActivity.class);
                                    //intent.putExtras(basket);
                                    startActivity(intent2);


                                    return true;

                                case R.id.empresas:
                                    menuItem.setChecked(true);

                                    Toast.makeText(MainActivity.this, "Pantalla: " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                    mDrawerLayout.closeDrawer(GravityCompat.START);
                                    Intent intent3 = new Intent(MainActivity.this,EmpresasActivity.class);
                                    //intent.putExtras(basket);
                                    startActivity(intent3);


                                    return true;


                            }
                            return true;





                    }
                });
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){

                case 0:
                    Tab1Bandeja tab1=new Tab1Bandeja();
                    return tab1;


                case 1:
                    Tab2Guardados tab2=new Tab2Guardados();
                    return tab2;

                    default:
                        return null;




            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }




    }
}


