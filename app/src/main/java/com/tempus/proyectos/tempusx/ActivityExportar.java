package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempus.proyectos.util.UserInterfaceM;

public class ActivityExportar extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    ImageView btnMasterExportar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportar);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Exportar";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterExportar = (ImageView) findViewById(R.id.btnMasterExportar);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);

        /* --- Inicialización de Parametros Generales --- */

        /* --- Eventos --- */

        btnMasterExportar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityExportar.this, ActivityMenu.class , "","");
            }
        });

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
