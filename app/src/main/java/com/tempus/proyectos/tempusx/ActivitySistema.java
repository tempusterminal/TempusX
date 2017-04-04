package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.data.queries.QueriesTerminal;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;


public class ActivitySistema extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    String resultadoIP;

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    EditText edtSistIdTerminal;
    TextView txvSistFirmware;
    TextView txvSistSoftware;
    TextView txvSistIp;
    TextView txvSistTec1;
    Button btnActualizarID;

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
        txvSistTec1 = (TextView) findViewById(R.id.txvSistTec1);
        btnActualizarID = (Button) findViewById(R.id.btnActualizarID);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);

        /* --- Inicialización de Parametros Generales --- */

        /* --- Eventos --- */

        btnMasterSistema.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivitySistema.this, ActivityMenu.class , "","");
            }
        });

        btnActualizarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueriesTerminal queriesTerminal = new QueriesTerminal(ActivityPrincipal.context);
                int i = queriesTerminal.ActualizarIdterminal(edtSistIdTerminal.getText().toString());
                if (i == 1) {
                    Toast.makeText(getApplicationContext(),"ID Actualizados Correctamente",Toast.LENGTH_SHORT).show();
                    ActivityPrincipal.idTerminal = edtSistIdTerminal.getText().toString();
                } else {
                    Toast.makeText(getApplicationContext(),"Fallo al Actualizar",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ManageData();
        showIP();
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

    public void showIP(){
        Shell sh = new Shell();
        String params[] = {"su","-c","ifconfig"};
        String cadena = sh.exec(params);
        String arreglo[] = cadena.split("\n");

        String wifi = " - ";
        String eth = " - ";

        for (int i = 0 ; i < arreglo.length; i++){
            if (ActivityPrincipal.INTERFACE_WLAN && arreglo[i].contains("wlan0")){
                try {
                    wifi = arreglo[i+1].toLowerCase().trim().split(":")[1].replace("bcast","");
                } catch(Exception e){
                    Log.wtf("showIP","WLAN0" + e);
                }

            }

            if (ActivityPrincipal.INTERFACE_ETH && arreglo[i].contains("eth0")){
                try {
                    eth = arreglo[i+1].toLowerCase().trim().split(":")[1].replace("bcast","");
                } catch(Exception e){
                    Log.wtf("showIP","WLAN0" + e);
                }
            }
        }

        String var1 = "";
        String var2 = "";

        if (ActivityPrincipal.INTERFACE_WLAN){
            var1 = "wlan:" + wifi;
        }

        if (ActivityPrincipal.INTERFACE_ETH){
            var2 = "eth:" + eth;
        }

        resultadoIP = var1 + "/" + var2;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txvSistIp.setText(resultadoIP);
            }
        });
    }

    public void ManageData() {
        edtSistIdTerminal.setText(ActivityPrincipal.idTerminal);
        txvSistFirmware.setText("J");
        txvSistSoftware.setText("E");
        txvSistIp.setText("");
        //txvSistTec1.setText("DNI");
        txvSistTec1.setText("HUELLA");
        //txvSistTec3.setText("TECLADO");
    }
}
