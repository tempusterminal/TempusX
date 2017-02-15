package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivitySincronizacion extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Utilities util;

    /* --- Declaración de Variables Globales --- */

    public static List<String> logSincronizacion;

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    TabHost host;

    ImageView btnMasterSincronizacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        util = new Utilities();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Sincronizacion";
        logSincronizacion = new ArrayList<String>();

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterSincronizacion = (ImageView) findViewById(R.id.btnMasterSincronizacion);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);


        host = (TabHost)findViewById(R.id.tabHostSync);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab1");
        spec.setContent(R.id.tabSync1);
        spec.setIndicator("PROCESOS");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab2");
        spec.setContent(R.id.tabSync2);
        spec.setIndicator("ORIGEN DE DATOS");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab3");
        spec.setContent(R.id.tabSync3);
        spec.setIndicator("TEST");
        host.addTab(spec);

        TabWidget widget = host.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tabline);
            tv.setTextSize(15);
        }

        //View w3 = host.getTabWidget().getChildTabViewAt(2);
        //w3.setVisibility(View.INVISIBLE);
//
        //View w1 = host.getTabWidget().getChildTabViewAt(1);
        //w1.setVisibility(View.INVISIBLE);






        /* --- Inicialización de Parametros Generales --- */

        btnMasterSincronizacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivitySincronizacion.this, ActivityMenu.class , "","");
            }
        });


        boolean internet = util.isOnline(this);
        Log.e("Tempus: ", "Coneccion Test -> " + String.valueOf(internet));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                ui.initScreen(this);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
