package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.servicios.OverlayShowingService;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import org.apache.commons.net.io.ToNetASCIIInputStream;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivitySincronizacion extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Utilities util;
    Connectivity connectivity;

    /* --- Declaración de Variables Globales --- */

    public static List<String> logSincronizacion;

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    TabHost host;

    ImageView btnMasterSincronizacion;
    Button btnAccessVPN;
    Button btnTest;
    TextView txvInternet;
    TextView txvOrigen;

    Button btnSyncGuardar;

    EditText edtSyncHost;
    EditText edtSyncDB;
    EditText edtSyncPort;
    EditText edtSyncUser;
    EditText edtSyncPass;

    boolean internet = false;
    boolean servidor = false;


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
        btnAccessVPN = (Button) findViewById(R.id.btnAccessVPN);
        btnTest = (Button) findViewById(R.id.btnTest);
        txvInternet = (TextView) findViewById(R.id.txvInternet);
        txvOrigen = (TextView) findViewById(R.id.txvOrigen);

        btnSyncGuardar = (Button) findViewById(R.id.btnSyncGuardar);

        edtSyncHost = (EditText) findViewById(R.id.edtSyncHost);
        edtSyncDB = (EditText) findViewById(R.id.edtSyncDB);
        edtSyncPort = (EditText) findViewById(R.id.edtSyncPort);
        edtSyncUser = (EditText) findViewById(R.id.edtSyncUser);
        edtSyncPass = (EditText) findViewById(R.id.edtSyncPass);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);


        host = (TabHost)findViewById(R.id.tabHostSync);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab1");
        spec.setContent(R.id.tabSync1);
        spec.setIndicator("SERVICIOS");
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
        //View w1 = host.getTabWidget().getChildTabViewAt(1);
        //w1.setVisibility(View.INVISIBLE);

        txvInternet.setBackgroundColor(Color.RED);
        txvOrigen.setBackgroundColor(Color.RED);

        connectivity = new Connectivity();


        /* --- Inicialización de Parametros Generales --- */

        btnMasterSincronizacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivitySincronizacion.this, ActivityMenu.class , "","");
            }
        });

        btnAccessVPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Configurando VPN",Toast.LENGTH_SHORT).show();

                t.start();

                Intent launchIntent2 = getPackageManager().getLaunchIntentForPackage("net.openvpn.openvpn");
                if (launchIntent2 != null) {
                    startActivity(launchIntent2);
                }


                //Intent launchIntent1 = getPackageManager().getLaunchIntentForPackage("com.tempus.ecernar.overlayapp");
                //if (launchIntent1 != null) {
                //    startActivity(launchIntent1);
                //}

            }
        });

        btnSyncGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = "";
                String database = "";
                String port = "";
                String user = "";
                String pass = "";

                host = edtSyncHost.getText().toString();
                database = edtSyncHost.getText().toString();
                port = edtSyncHost.getText().toString();
                user = edtSyncHost.getText().toString();
                pass = edtSyncHost.getText().toString();

                Log.d("SYNC SAVE",host+" "+database+" "+port+" "+user+" "+pass);
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.start();
            }
        });

        boolean internet = util.isOnline(this);
        Log.e("Tempus: ", "Coneccion Test -> " + String.valueOf(internet));
    }


    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent launchIntent1 = getPackageManager().getLaunchIntentForPackage("com.tempus.ecernar.overlayapp");
            if (launchIntent1 != null) {
                startActivity(launchIntent1);
            }
        }
    });


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

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            try  {
                internet = false;
                servidor = false;

                try {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Probando conexión ... ", Toast.LENGTH_LONG).show();
                        }
                    });

                    internet = connectivity.isURLReachable(getApplicationContext(),"http://www.google.com","url");
                    servidor = connectivity.isURLReachable(getApplicationContext(),ConexionServidor.getHost(),"ip");

                    Log.d("PING","LINEA"+internet+servidor);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Terminó", Toast.LENGTH_LONG).show();
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (internet){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txvInternet.setBackgroundColor(Color.GREEN);
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txvInternet.setBackgroundColor(Color.RED);
                                    }
                                });
                            }

                            if (servidor){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txvOrigen.setBackgroundColor(Color.GREEN);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txvOrigen.setBackgroundColor(Color.RED);
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}
