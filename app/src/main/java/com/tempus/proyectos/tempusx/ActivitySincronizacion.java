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
import android.widget.CompoundButton;
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

import com.tempus.proyectos.bluetoothSerial.MainEthernet;
import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.model.Servicios;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.servicios.OverlayShowingService;
import com.tempus.proyectos.tcpSerial.UsrTCP;
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

    String TAG = "TX-ASY";

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Utilities util;
    Connectivity connectivity;

    Parameters parameters1;
    Parameters parameters2;
    Parameters parameters3;
    Parameters parameters4;

    QueriesParameters queriesParameters;


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

    Button btnWsGuardar;

    EditText edtWsServer;
    EditText edtWsPort;

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

    /* --- MainEthernet --- */
    MainEthernet mainEthernet = new MainEthernet();

    /* --- UsrTCP --- */
    UsrTCP usrTCP = new UsrTCP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        util = new Utilities();

        parameters1 = new Parameters();
        parameters2 = new Parameters();
        parameters3 = new Parameters();
        parameters4 = new Parameters();

        queriesParameters = new QueriesParameters(ActivityPrincipal.context);

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

        btnWsGuardar = (Button) findViewById(R.id.btnWsGuardar);

        edtWsServer = (EditText) findViewById(R.id.edtWsServer);
        edtWsPort = (EditText) findViewById(R.id.edtWsPort);

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

        cargarHorario();


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
        spec.setIndicator("WEB SERVICE");
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec("Tab5");
        spec.setContent(R.id.tabSync5);
        spec.setIndicator("TEST");
        host.addTab(spec);

        //Tab 6
        spec = host.newTabSpec("Tab6");
        spec.setContent(R.id.tabSync6);
        spec.setIndicator("USB");
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


        Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());

        if(ActivityPrincipal.parametersSock.size() == 0){
            getParametersSock();
        }else if(ActivityPrincipal.parametersSock.size() == 3){
            //AT+SOCK=<Protocol>,<IP address>,<Port><CR>
            Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());
            edtWsServer.setText(ActivityPrincipal.parametersSock.get(1));
            edtWsPort.setText(ActivityPrincipal.parametersSock.get(2));

        }


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


        btnWsGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "";
                String parameter = "";

                Thread threadHttpcfg = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.v(TAG,"threadHttpcfg inicio ");
                            //Toast.makeText(ActivityPrincipal.context,"Configurando Ethernet",Toast.LENGTH_LONG).show();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edtWsServer.setEnabled(false);
                                    edtWsPort.setEnabled(false);
                                    btnWsGuardar.setEnabled(false);
                                    btnWsGuardar.setText("Configurando");
                                }
                            });

                            Log.v(TAG,"threadHttpcfg enableSetEthernet " + MainEthernet.enableSetEthernet);
                            while(!MainEthernet.enableSetEthernet){
                                Log.v(TAG,"threadHttpcfg enableSetEthernet while " + MainEthernet.enableSetEthernet);
                                Thread.sleep(250);
                            }
                            Thread.sleep(1000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnWsGuardar.setText(".");
                                }
                            });

                            while(MainEthernet.atCommandMode){
                                Log.v(TAG,"threadEthernetcfg atCommandMode while " + MainEthernet.atCommandMode);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(btnWsGuardar.getText().length() == 3){
                                            btnWsGuardar.setText(".");
                                        }else{
                                            btnWsGuardar.setText(btnWsGuardar.getText() + ".");
                                        }
                                    }
                                });
                                Thread.sleep(1000);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edtWsServer.setEnabled(true);
                                    edtWsPort.setEnabled(true);
                                    btnWsGuardar.setEnabled(true);
                                    btnWsGuardar.setText("Configurar");
                                }
                            });


                            Log.v(TAG,"hexStringcfg " + MainEthernet.hexStringcfg);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        //AT+SOCK=<Protocol>,<IP address>,<Port><CR>
                                        String[] parametersArray = usrTCP.getParameters(MainEthernet.hexStringcfg);
                                        Log.v(TAG,"parametersArray[" + parametersArray.length + "] = " + parametersArray[0] + " - " + parametersArray[1] + " - " +parametersArray[2]);
                                        if(parametersArray.length>0){
                                            Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());
                                            ActivityPrincipal.parametersSock.clear();
                                            Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());
                                            ActivityPrincipal.parametersSock.add(parametersArray[0]);
                                            ActivityPrincipal.parametersSock.add(parametersArray[1]);
                                            ActivityPrincipal.parametersSock.add(parametersArray[2]);
                                            Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());

                                            edtWsServer.setText(ActivityPrincipal.parametersSock.get(1));
                                            edtWsPort.setText(ActivityPrincipal.parametersSock.get(2));

                                        }
                                    }catch (Exception e){
                                        Log.e(TAG,"parametersArray " + e.getMessage());
                                    }

                                    edtWsServer.setEnabled(true);
                                    edtWsPort.setEnabled(true);
                                    btnWsGuardar.setEnabled(true);
                                    btnWsGuardar.setText("Configurar");
                                }
                            });

                            //Toast.makeText(ActivityPrincipal.context,"Ethernet Configurado",Toast.LENGTH_LONG).show();
                            Log.v(TAG,"threadHttpcfg fin ");
                        } catch (Exception e) {
                            Log.e(TAG,"threadHttpcfg " + e.getMessage());
                        }

                    }
                });


                //mainEthernet.startEthernetATCommand("AT+WANN","=STATIC,192.168.0.78,255.255.255.0,192.168.0.2");
                //AT+SOCK=<Protocol>,<IP address>,<Port><CR>

                Log.v(TAG,"edtWsServer (validando) " + edtWsServer.getText().toString());
                msg = usrTCP.validateParameters("SERVIDOR", edtWsServer.getText().toString());
                Log.v(TAG,"msg " + msg);
                if(msg.length()==0){
                    Log.v(TAG,"Parametros Validos (HTTP)");
                    parameter = "=HTPC" + "," + edtWsServer.getText().toString() + "," + edtWsPort.getText().toString();
                    Log.v(TAG,"parameter " + parameter);
                    mainEthernet.startEthernetATCommand("AT+SOCK",parameter,true,true,true);
                    threadHttpcfg.start();
                }else{
                    Toast.makeText(ActivityPrincipal.context, msg, Toast.LENGTH_SHORT).show();
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
                                                    txvInternet.setText("OK");
                                                    txvInternet.setBackgroundColor(Color.GREEN);
                                                }
                                            });

                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    txvInternet.setText("KO");
                                                    txvInternet.setBackgroundColor(Color.RED);
                                                }
                                            });
                                        }

                                        if (servidor){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    txvOrigen.setText("OK");
                                                    txvOrigen.setBackgroundColor(Color.GREEN);
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    txvOrigen.setText("KO");
                                                    txvOrigen.setBackgroundColor(Color.RED);
                                                }
                                            });
                                        }

                                        if (basedatos){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    txvOrigenConnection.setText("OK");
                                                    txvOrigenConnection.setBackgroundColor(Color.GREEN);
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    txvOrigenConnection.setText("KO");
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
                        actualizarHorario(parameters1,"HORA_REPLICADO_1", selectedHour, selectedMinute, 0, lblHora1);
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
                        actualizarHorario(parameters2,"HORA_REPLICADO_2", selectedHour, selectedMinute, 0, lblHora2);
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
                        actualizarHorario(parameters3,"HORA_REPLICADO_3", selectedHour, selectedMinute, 0, lblHora3);
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
                        actualizarHorario(parameters4,"HORA_REPLICADO_4", selectedHour, selectedMinute, 0, lblHora4);
                    }
                }, h, m, true);
                mTimePicker.setTitle("Horario 4");
                mTimePicker.show();
            }
        });

        swtHoraConf1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int hora = Integer.parseInt(lblHora1.getText().toString().split(":")[0]);
                int min = Integer.parseInt(lblHora1.getText().toString().split(":")[1]);

                if (isChecked){
                    actualizarHorario(parameters1,"HORA_REPLICADO_1", hora, min, 1, lblHora1);;
                } else {
                    actualizarHorario(parameters1,"HORA_REPLICADO_1", hora, min, 0, lblHora1);;
                }

            }
        });

        swtHoraConf2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int hora = Integer.parseInt(lblHora2.getText().toString().split(":")[0]);
                int min = Integer.parseInt(lblHora2.getText().toString().split(":")[1]);

                if (isChecked){
                    actualizarHorario(parameters2,"HORA_REPLICADO_2", hora, min, 1, lblHora2);;
                } else {
                    actualizarHorario(parameters2,"HORA_REPLICADO_2", hora, min, 0, lblHora2);;
                }
            }
        });

        swtHoraConf3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int hora = Integer.parseInt(lblHora3.getText().toString().split(":")[0]);
                int min = Integer.parseInt(lblHora3.getText().toString().split(":")[1]);

                if (isChecked){
                    actualizarHorario(parameters3,"HORA_REPLICADO_3", hora, min, 1, lblHora3);;
                } else {
                    actualizarHorario(parameters3,"HORA_REPLICADO_3", hora, min, 0, lblHora3);;
                }
            }
        });

        swtHoraConf4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int hora = Integer.parseInt(lblHora4.getText().toString().split(":")[0]);
                int min = Integer.parseInt(lblHora4.getText().toString().split(":")[1]);

                if (isChecked){
                    actualizarHorario(parameters4,"HORA_REPLICADO_4", hora, min, 1, lblHora4);;
                } else {
                    actualizarHorario(parameters4,"HORA_REPLICADO_4", hora, min, 0, lblHora4);;
                }
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

    public String getTimeLabel(int hora, int minuto) {
        String h = String.valueOf(hora);
        if (h.length()==1){
            h = "0"+h;
        }

        String m = String.valueOf(minuto);
        if (m.length()==1){
            m = "0"+m;
        }
        return h+":"+m;
    }

    public void cargarHorario() {
        String h1, h2, h3, h4;
        int b1, b2, b3, b4;

        try {
            List<Parameters> listParameterses1 = queriesParameters.select_one_row("HORA_REPLICADO_1");
            List<Parameters> listParameterses2 = queriesParameters.select_one_row("HORA_REPLICADO_2");
            List<Parameters> listParameterses3 = queriesParameters.select_one_row("HORA_REPLICADO_3");
            List<Parameters> listParameterses4 = queriesParameters.select_one_row("HORA_REPLICADO_4");

            h1 = listParameterses1.get(0).getValue();
            h2 = listParameterses2.get(0).getValue();
            h3 = listParameterses3.get(0).getValue();
            h4 = listParameterses4.get(0).getValue();

            b1 = listParameterses1.get(0).getEnable();
            b2 = listParameterses2.get(0).getEnable();
            b3 = listParameterses3.get(0).getEnable();
            b4 = listParameterses4.get(0).getEnable();

            lblHora1.setText(h1.substring(0,5));
            lblHora2.setText(h2.substring(0,5));
            lblHora3.setText(h3.substring(0,5));
            lblHora4.setText(h4.substring(0,5));

            if (b1 == 1){ swtHoraConf1.setChecked(true); } else { swtHoraConf1.setChecked(false); }

            if (b2 == 1){ swtHoraConf2.setChecked(true); } else { swtHoraConf2.setChecked(false); }

            if (b3 == 1){ swtHoraConf3.setChecked(true); } else { swtHoraConf3.setChecked(false); }

            if (b4 == 1){ swtHoraConf4.setChecked(true); } else { swtHoraConf4.setChecked(false); }

        } catch(Exception e){

        }


    }

    public void actualizarHorario(Parameters parameter, String idParameter, int hora, int minuto, int activo, TextView txv) {// Especifica horario (1,2.3,4) BOTON CONFIG
        String horario = getTimeLabel(hora,minuto);
        parameter.setIdparameter(idParameter);
        parameter.setValue(horario + ":00");
        parameter.setEnable(activo);
        queriesParameters.update(parameter);
        txv.setText(horario);
    }

    public void administrarHorario(Parameters parameter, String idParameter, boolean enabled) {// Especifica horario (1,2.3,4) SWITCH
        Log.d("administrarHorario",parameter + "-" + idParameter + "-" + enabled);
        parameter.setIdparameter(idParameter);
        parameter.setValue(parameter.getValue());
        //if (enabled) {
        //    parameter.setEnable(1);
        //    queriesParameters.update(parameter);
        //} else {
        //    parameter.setEnable(0);
        //    queriesParameters.update(parameter);
        //}
    }



    public void getParametersSock(){

        mainEthernet.startEthernetATCommand("AT+SOCK","",false,true,true);

        Thread threadHttpcfg = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG,"threadHttpcfg runOnUiThread inicio ");
                try {
                    Log.v(TAG,"threadHttpcfg inicio ");
                    //Toast.makeText(ActivityPrincipal.context,"Configurando Ethernet",Toast.LENGTH_LONG).show();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edtWsServer.setEnabled(false);
                            edtWsPort.setEnabled(false);
                            btnWsGuardar.setEnabled(false);
                            btnWsGuardar.setText("Actualizando");
                        }
                    });

                    Log.v(TAG,"threadHttpcfg enableSetEthernet " + MainEthernet.enableSetEthernet);
                    while(!MainEthernet.enableSetEthernet){
                        Log.v(TAG,"threadHttpcfg enableSetEthernet while " + MainEthernet.enableSetEthernet);
                        Thread.sleep(250);
                    }
                    Thread.sleep(1000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnWsGuardar.setText(".");
                        }
                    });

                    while(MainEthernet.atCommandMode){
                        Log.v(TAG,"threadHttpcfg atCommandMode while " + MainEthernet.atCommandMode);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(btnWsGuardar.getText().length() == 3){
                                    btnWsGuardar.setText(".");
                                }else{
                                    btnWsGuardar.setText(btnWsGuardar.getText() + ".");
                                }
                            }
                        });
                        Thread.sleep(1000);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edtWsServer.setEnabled(true);
                            edtWsPort.setEnabled(true);
                            btnWsGuardar.setEnabled(true);
                            btnWsGuardar.setText("Configurar");
                        }
                    });


                    //String[] parametersArray = usrTCP.getParameters(MainEthernet.hexStringcfg);
                    //Log.v(TAG,"parametersArray[" + parametersArray.length + "] = " + parametersArray[0] + " - " + parametersArray[1] + " - " +parametersArray[2] + " - " +parametersArray[3]);

                    Log.v(TAG,"hexStringcfg " + MainEthernet.hexStringcfg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                String[] parametersArray = usrTCP.getParameters(MainEthernet.hexStringcfg);
                                //Log.v(TAG,"parametersArray[" + parametersArray.length + "] = " + parametersArray[0] + " - " + parametersArray[1] + " - " +parametersArray[2]);
                                if(parametersArray.length>0){
                                    //Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());
                                    ActivityPrincipal.parametersSock.clear();
                                    //Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());

                                    ActivityPrincipal.parametersSock.add(parametersArray[0]);
                                    ActivityPrincipal.parametersSock.add(parametersArray[1]);
                                    ActivityPrincipal.parametersSock.add(parametersArray[2]);
                                    Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());

                                    edtWsServer.setText(ActivityPrincipal.parametersSock.get(1));
                                    edtWsPort.setText(ActivityPrincipal.parametersSock.get(2));

                                }
                            }catch (Exception e){
                                Log.e(TAG,"parametersArray " + e.getMessage());
                            }

                            edtWsServer.setEnabled(true);
                            edtWsPort.setEnabled(true);
                            btnWsGuardar.setEnabled(true);
                            btnWsGuardar.setText("Configurar");
                        }
                    });

                    //Toast.makeText(ActivityPrincipal.context,"Ethernet Configurado",Toast.LENGTH_LONG).show();
                    Log.v(TAG,"threadHttpcfg fin ");
                } catch (Exception e) {
                    Log.e(TAG,"threadHttpcfg " + e.getMessage());
                }
                Log.v(TAG,"threadHttpcfg runOnUiThread fin ");
            }
        });

        threadHttpcfg.start();

    }





}
