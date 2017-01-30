package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempus.proyectos.util.UserInterfaceM;


public class ActivitySistema extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    EditText edtSistIdTerminal;
    TextView txvSistFirmware;
    TextView txvSistSoftware;
    TextView txvSistIp;
    TextView txvSistAhorro;
    TextView txvSistTec1;
    TextView txvSistTec2;
    TextView txvSistTec3;

    ImageView btnMasterSistema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sistema);

        /* --- Inicialización de Objetos --- */
        Log.v("TEMPUS: ","INCIANDO SISTEMA");

        ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Sistema";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterSistema = (ImageView) findViewById(R.id.btnMasterSistema);

        edtSistIdTerminal = (EditText) findViewById(R.id.edtSistIdTerminal);
        txvSistFirmware = (TextView) findViewById(R.id.txvSistFirmware);
        txvSistSoftware = (TextView) findViewById(R.id.txvSistSoftware);
        txvSistIp = (TextView) findViewById(R.id.txvSistIp);
        txvSistAhorro = (TextView) findViewById(R.id.txvSistAhorro);
        txvSistTec1 = (TextView) findViewById(R.id.txvSistTec1);
        txvSistTec2 = (TextView) findViewById(R.id.txvSistTec2);
        txvSistTec3 = (TextView) findViewById(R.id.txvSistTec3);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);

        /* --- Inicialización de Parametros Generales --- */

        /* --- Eventos --- */

        btnMasterSistema.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivitySistema.this, ActivityMenu.class , "","");
            }
        });

        ManageData();

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

    public void ManageData() {
        edtSistIdTerminal.setText("100");
        txvSistFirmware.setText("J");
        txvSistSoftware.setText("E");
        txvSistIp.setText("0.0.0.0");
        txvSistAhorro.setText("Siempre");
        txvSistTec1.setText("DNI");
        txvSistTec2.setText("PROXIMIDAD");
        txvSistTec3.setText("HUELLA");
    }
}
