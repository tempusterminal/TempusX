package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.tempus.proyectos.util.UserInterfaceM;

import java.io.File;
import java.util.Set;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

public class ActivityHistorial extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;

    ImageView btnMasterHistorial;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Aumentar el brillo al maximo brillo establecido
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = (float) (ActivityPrincipal.maxBrilloAhorroEnergia/100);
        getWindow().setAttributes(layoutParams);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Historial";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        txvFondo1 = (TextView) findViewById(R.id.txvFondo1);
        txvFondo2 = (TextView) findViewById(R.id.txvFondo2);
        txvBarraInf = (TextView) findViewById(R.id.txvBarraInf);
        txvLinea1 = (TextView) findViewById(R.id.txvLinea1);
        txvLinea2 = (TextView) findViewById(R.id.txvLinea2);
        txvLinea3 = (TextView) findViewById(R.id.txvLinea3);
        txvLinea4 = (TextView) findViewById(R.id.txvLinea4);

        ActivityPrincipal.setBackgroundColorOnTextView(txvFondo1,ActivityPrincipal.parametersColorsUI.split(",")[0],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvFondo2,ActivityPrincipal.parametersColorsUI.split(",")[1],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvBarraInf,ActivityPrincipal.parametersColorsUI.split(",")[2],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea1,ActivityPrincipal.parametersColorsUI.split(",")[3],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea2,ActivityPrincipal.parametersColorsUI.split(",")[4],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea3,ActivityPrincipal.parametersColorsUI.split(",")[5],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea4,ActivityPrincipal.parametersColorsUI.split(",")[6],"#777777");

        btnMasterHistorial = (ImageView) findViewById(R.id.btnMasterHistorial);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterHistorial,"/tempus/img/config/","logo.png");

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);

        /* --- Inicialización de Parametros Generales --- */

        btnMasterHistorial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityHistorial.this, ActivityMenu.class , "","");
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // En caso que se detecte un evento sobre la pantalla el contadorAhorroEnergia se reiniciara en 1
        ActivityPrincipal.contadorAhorroEnergia = 1;

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
