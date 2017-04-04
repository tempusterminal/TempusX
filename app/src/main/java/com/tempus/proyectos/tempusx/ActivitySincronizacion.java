package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Servicios;
import com.tempus.proyectos.data.queries.QueriesServicios;
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
    TextView txvOrigenConnection;

    Button btnSyncGuardar;

    EditText edtSyncHost;
    EditText edtSyncDB;
    EditText edtSyncPort;
    EditText edtSyncUser;
    EditText edtSyncPass;

    boolean internet = false;
    boolean servidor = false;
    boolean basedatos = false;

    TextView lblHora1;
    TextView lblHora2;
    TextView lblHora3;
    TextView lblHora4;

    ImageButton btnConfHora1;
    ImageButton btnConfHora2;
    ImageButton btnConfHora3;
    ImageButton btnConfHora4;

    Switch swtHoraConf1;
    Switch swtHoraConf2;
    Switch swtHoraConf3;
    Switch swtHoraConf4;

    Button btnReplicar;

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
        txvOrigenConnection = (TextView) findViewById(R.id.txvOrigenConnection);

        btnSyncGuardar = (Button) findViewById(R.id.btnSyncGuardar);

        edtSyncHost = (EditText) findViewById(R.id.edtSyncHost);
        edtSyncDB = (EditText) findViewById(R.id.edtSyncDB);
        edtSyncPort = (EditText) findViewById(R.id.edtSyncPort);
        edtSyncUser = (EditText) findViewById(R.id.edtSyncUser);
        edtSyncPass = (EditText) findViewById(R.id.edtSyncPass);

        btnReplicar = (Button) findViewById(R.id.btnReplicar);

        lblHora1 = (TextView) findViewById(R.id.lblHora1);
        lblHora2 = (TextView) findViewById(R.id.lblHora2);
        lblHora3 = (TextView) findViewById(R.id.lblHora3);
        lblHora4 = (TextView) findViewById(R.id.lblHora4);

        btnConfHora1 = (ImageButton) findViewById(R.id.btnConfHora1);
        btnConfHora2 = (ImageButton) findViewById(R.id.btnConfHora2);
        btnConfHora3 = (ImageButton) findViewById(R.id.btnConfHora3);
        btnConfHora4 = (ImageButton) findViewById(R.id.btnConfHora4);

        swtHoraConf1 = (Switch) findViewById(R.id.swtHoraConf1);
        swtHoraConf2 = (Switch) findViewById(R.id.swtHoraConf2);
        swtHoraConf3 = (Switch) findViewById(R.id.swtHoraConf3);
        swtHoraConf4 = (Switch) findViewById(R.id.swtHoraConf4);

        btnReplicar = (Button) findViewById(R.id.btnReplicar);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);


        host = (TabHost)findViewById(R.id.tabHostSync);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab1");
        spec.setContent(R.id.tabSync1);
        spec.setIndicator("REPLICA");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab2");
        spec.setContent(R.id.tabSync2);
        spec.setIndicator("SERVICIOS");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab3");
        spec.setContent(R.id.tabSync3);
        spec.setIndicator("SERVIDOR");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Tab4");
        spec.setContent(R.id.tabSync4);
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
                database = edtSyncDB.getText().toString();
                port = edtSyncPort.getText().toString();
                user = edtSyncUser.getText().toString();
                pass = edtSyncPass.getText().toString();

                Log.d("SYNC SAVE",host+" "+database+" "+port+" "+user+" "+pass);

                if (host.isEmpty() || database.isEmpty() || port.isEmpty() || user.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Debe llenar correctamente los campos", Toast.LENGTH_SHORT).show();
                } else {
                    QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
                    queriesServicios.update("SERVIDOR_DATOS_PRINCIPAL",host,host,"",database,port,user,pass);

                    Toast.makeText(getApplicationContext(),"Actualizado", Toast.LENGTH_SHORT).show();

                    Log.d("SYNC SAVE","EXITO!");
                }



            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            internet = false;
                            servidor = false;
                            basedatos = false;

                            try {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Probando conexión ... ", Toast.LENGTH_LONG).show();
                                    }
                                });

                                try {
                                    Log.d("IP_TEST","http://www.google.com");
                                    internet = connectivity.isURLReachable(getApplicationContext(),"http://www.google.com","url");
                                } catch(Exception e) {
                                    Log.wtf("IP_TEST","http://www.google.com");
                                }

                                // Ping Servidor

                                String servidorDatos = "";

                                try {
                                    Log.d("IP_TEST","entrando");
                                    QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
                                    List<Servicios> servidor_datos_principal = queriesServicios.BuscarServicios("SERVIDOR_DATOS_PRINCIPAL");
                                    Log.d("IP_TEST","tam: "+servidor_datos_principal.size());
                                    for (int i = 0; i < servidor_datos_principal.size(); i++){
                                        servidorDatos = servidor_datos_principal.get(i).getHost();
                                        break;
                                    }
                                    Log.d("IP_TEST","datos: "+ servidorDatos);
                                    servidor = connectivity.isURLReachable(getApplicationContext(),servidorDatos,"ip");
                                } catch(Exception e) {
                                    Log.wtf("IP_TEST",servidorDatos);
                                }



                                // Ping Base de Datos

                                try {
                                    basedatos = connectivity.isValidConnection();
                                } catch(Exception e){
                                    Log.wtf("IP_TEST","basedatos "+e);
                                }



                                Log.d("PING","LINEA"+internet+servidor+basedatos);

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

                                        if (basedatos){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    txvOrigenConnection.setBackgroundColor(Color.GREEN);
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    txvOrigenConnection.setBackgroundColor(Color.RED);
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

                thread.start();
            }
        });

        btnConfHora1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int h = 0;
                int m = 0;
                String val = lblHora1.getText().toString();
                if (!val.contains("-")){
                    h = Integer.parseInt(val.split(":")[0]);
                    m = Integer.parseInt(val.split(":")[1]);
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivitySincronizacion.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTimeLabel(lblHora1,selectedHour,selectedMinute);
                    }
                }, h, m, true);
                mTimePicker.setTitle("Horario 1");
                mTimePicker.show();
            }
        });

        btnConfHora2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int h = 0;
                int m = 0;
                String val = lblHora2.getText().toString();
                if (!val.contains("-")){
                    h = Integer.parseInt(val.split(":")[0]);
                    m = Integer.parseInt(val.split(":")[1]);
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivitySincronizacion.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTimeLabel(lblHora2,selectedHour,selectedMinute);
                    }
                }, h, m, true);
                mTimePicker.setTitle("Horario 2");
                mTimePicker.show();
            }
        });

        btnConfHora3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int h = 0;
                int m = 0;
                String val = lblHora3.getText().toString();
                if (!val.contains("-")){
                    h = Integer.parseInt(val.split(":")[0]);
                    m = Integer.parseInt(val.split(":")[1]);
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivitySincronizacion.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTimeLabel(lblHora3,selectedHour,selectedMinute);
                    }
                }, h, m, true);
                mTimePicker.setTitle("Horario 3");
                mTimePicker.show();
            }
        });

        btnConfHora4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int h = 0;
                int m = 0;
                String val = lblHora4.getText().toString();
                if (!val.contains("-")){
                    h = Integer.parseInt(val.split(":")[0]);
                    m = Integer.parseInt(val.split(":")[1]);
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivitySincronizacion.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTimeLabel(lblHora4,selectedHour,selectedMinute);
                    }
                }, h, m, true);
                mTimePicker.setTitle("Horario 4");
                mTimePicker.show();
            }
        });

        btnReplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySincronizacion.this, R.style.AlertDialogCustom));

                builder
                        .setTitle("Replicado a Demanda")
                        .setMessage("¿Desea intentar replicar ahora mismo?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                ActivityPrincipal.isReplicating = true;
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });



        boolean internet = util.isOnline(this);
        Log.e("Tempus: ", "Coneccion Test -> " + String.valueOf(internet));

        try {
            getDatosServidor();
        } catch(Exception e) {
            e.printStackTrace();
        }

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

    public void getDatosServidor() {
        QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
        List<Servicios> servidor_datos_principal = queriesServicios.BuscarServicios("SERVIDOR_DATOS_PRINCIPAL");
        for (int i = 0; i < servidor_datos_principal.size(); i++){
            edtSyncDB.setText(servidor_datos_principal.get(i).getDatabase());
            edtSyncHost.setText(servidor_datos_principal.get(i).getHost());
            edtSyncPort.setText(servidor_datos_principal.get(i).getPort());
            edtSyncUser.setText(servidor_datos_principal.get(i).getUser());
            edtSyncPass.setText(servidor_datos_principal.get(i).getPass());
            break;
        }
    }

    public void setTimeLabel(TextView txvHora, int hora, int minuto) {
        String h = String.valueOf(hora);
        if (h.length()==1){
            h = "0"+h;
        }

        String m = String.valueOf(minuto);
        if (m.length()==1){
            m = "0"+m;
        }
        txvHora.setText(h+":"+m);
    }

    public void cargarHorario() {
        setTimeLabel(lblHora1,0,0);
        setTimeLabel(lblHora2,0,0);
        setTimeLabel(lblHora3,0,0);
        setTimeLabel(lblHora4,0,0);
    }

    public void guardarHorario(String horario, TextView txv) {// Especifica horario (1,2.3,4)
        String hora = txv.getText().toString();
    }

    public void administrarHorario(String horario, boolean enabled) {// Especifica horario (1,2.3,4)
        if (enabled) {

        } else {

        }
    }

}
