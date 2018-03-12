package com.tempus.proyectos.tempusx;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.ThemedSpinnerAdapter;
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
import com.tempus.proyectos.data.process.ProcessSyncTS;
import com.tempus.proyectos.data.process.ProcessSyncUSB;
import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.servicios.OverlayShowingService;
import com.tempus.proyectos.tcpSerial.UsrTCP;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;


import java.io.IOException;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivitySincronizacion extends Activity {

    // test de push

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
    QueriesLogTerminal queriesLogTerminal;


    /* --- Declaración de Variables Globales --- */

    public static List<String> logSincronizacion;

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;

    TabHost host;

    ImageView btnMasterSincronizacion;
    Button btnAccessVPN;
    //Button btnTest;
    //TextView txvInternet;
    //TextView txvOrigen;
    //TextView txvOrigenConnection;

    ImageButton btnSaveServerDB;
    ImageButton btnClearServerDB;

    EditText edtSyncHost;
    EditText edtSyncDB;
    EditText edtSyncPort;
    EditText edtSyncUser;
    EditText edtSyncPass;

    Button btnWsGuardar;

    EditText edtWsServer;
    EditText edtWsCompany;
    EditText edtWsPort;
    EditText edtWsUser;
    EditText edtWsPass;

    Button btnWsnGuardar;
    ImageButton btnSaveWsn;
    ImageButton btnClearWsn;

    EditText edtWsnServer;
    EditText edtWsnCompany;
    EditText edtWsnPort;
    EditText edtWsnUser;
    EditText edtWsnPass;

    TextView txvLogWsLevel01;
    TextView txvLogWsLevel02;
    TextView txvLogWsLevel03;
    TextView txvLogWsLevel04;

    TextView txvLogWsnLevel01;
    TextView txvLogWsnLevel02;
    TextView txvLogWsnLevel03;
    TextView txvLogWsnLevel04;

    TextView txvLogServerDBLevel01;

    boolean internet = false;
    boolean servidor = false;
    boolean basedatos = false;

    TextView lblrep0;
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

    Button btnUsbMarcas;

    /* --- MainEthernet --- */
    MainEthernet mainEthernet = new MainEthernet();

    /* --- UsrTCP --- */
    UsrTCP usrTCP = new UsrTCP();

    private Fechahora fechahora = new Fechahora();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);

        /* Request user permissions in runtime */
        ActivityCompat.requestPermissions(ActivitySincronizacion.this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                100);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        util = new Utilities();

        parameters1 = new Parameters();
        parameters2 = new Parameters();
        parameters3 = new Parameters();
        parameters4 = new Parameters();

        queriesParameters = new QueriesParameters(ActivityPrincipal.context);
        queriesLogTerminal = new QueriesLogTerminal();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Sincronizacion";
        logSincronizacion = new ArrayList<String>();

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

        btnMasterSincronizacion = (ImageView) findViewById(R.id.btnMasterSincronizacion);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterSincronizacion,"/tempus/img/config/","logo.png");
        btnAccessVPN = (Button) findViewById(R.id.btnAccessVPN);
        //btnTest = (Button) findViewById(R.id.btnTest);
        //txvInternet = (TextView) findViewById(R.id.txvInternet);
        //txvOrigen = (TextView) findViewById(R.id.txvOrigen);
        //txvOrigenConnection = (TextView) findViewById(R.id.txvOrigenConnection);


        edtSyncHost = (EditText) findViewById(R.id.edtSyncHost);
        edtSyncDB = (EditText) findViewById(R.id.edtSyncDB);
        edtSyncPort = (EditText) findViewById(R.id.edtSyncPort);
        edtSyncUser = (EditText) findViewById(R.id.edtSyncUser);
        edtSyncPass = (EditText) findViewById(R.id.edtSyncPass);

        btnWsGuardar = (Button) findViewById(R.id.btnWsGuardar);
        btnWsnGuardar = (Button) findViewById(R.id.btnWsnGuardar);
        btnSaveServerDB = (ImageButton) findViewById(R.id.btnSaveServerDB);
        btnClearServerDB = (ImageButton) findViewById(R.id.btnClearServerDB);
        btnSaveWsn = (ImageButton) findViewById(R.id.btnSaveWsn);
        btnClearWsn = (ImageButton) findViewById(R.id.btnClearWsn);

        edtWsServer = (EditText) findViewById(R.id.edtWsServer);
        edtWsCompany = (EditText) findViewById(R.id.edtWsCompany);
        edtWsPort = (EditText) findViewById(R.id.edtWsPort);
        edtWsUser = (EditText) findViewById(R.id.edtWsUser);
        edtWsPass = (EditText) findViewById(R.id.edtWsPass);

        edtWsnServer = (EditText) findViewById(R.id.edtWsnServer);
        edtWsnCompany = (EditText) findViewById(R.id.edtWsnCompany);
        edtWsnPort = (EditText) findViewById(R.id.edtWsnPort);
        edtWsnUser = (EditText) findViewById(R.id.edtWsnUser);
        edtWsnPass = (EditText) findViewById(R.id.edtWsnPass);

        txvLogWsLevel01 = (TextView) findViewById(R.id.txvLogWsLevel01);
        txvLogWsLevel02 = (TextView) findViewById(R.id.txvLogWsLevel02);
        txvLogWsLevel03 = (TextView) findViewById(R.id.txvLogWsLevel03);
        txvLogWsLevel04 = (TextView) findViewById(R.id.txvLogWsLevel04);

        txvLogWsnLevel01 = (TextView) findViewById(R.id.txvLogWsnLevel01);
        txvLogWsnLevel02 = (TextView) findViewById(R.id.txvLogWsnLevel02);
        txvLogWsnLevel03 = (TextView) findViewById(R.id.txvLogWsnLevel03);
        txvLogWsnLevel04 = (TextView) findViewById(R.id.txvLogWsnLevel04);

        txvLogServerDBLevel01 = (TextView) findViewById(R.id.txvLogServerDBLevel01);

        btnReplicar = (Button) findViewById(R.id.btnReplicar);

        lblrep0 = (TextView) findViewById(R.id.lblrep0);
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

        btnUsbMarcas = (Button) findViewById(R.id.btnUsbMarcas);

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
        spec.setIndicator("SERVIDOR DB");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Tab4");
        spec.setContent(R.id.tabSync4);
        spec.setIndicator("WS");
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec("Tab5");
        spec.setContent(R.id.tabSync5);
        spec.setIndicator("WS NATIVO");
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

        //txvInternet.setBackgroundColor(Color.RED);
        //txvOrigen.setBackgroundColor(Color.RED);

        connectivity = new Connectivity();


        Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString() + " " + ActivityPrincipal.parametersSock.size());

        if(ActivityPrincipal.parametersSock.size() < 3){
            getParametersSock();
        }else if(ActivityPrincipal.parametersSock.size() == 3){
            //AT+SOCK=<Protocol>,<IP address>,<Port><CR>
            Log.v(TAG,"parametersSock " + ActivityPrincipal.parametersSock.toString());

            if(ActivityPrincipal.parametersSock.get(1).length() < 16 && ActivityPrincipal.parametersSock.get(1).length() > 0){
                edtWsServer.setText(ActivityPrincipal.parametersSock.get(1));
                edtWsCompany.setText(ActivityPrincipal.parametersWebService_01.split(",")[1]);
                edtWsPort.setText(ActivityPrincipal.parametersSock.get(2));
                edtWsUser.setText(ActivityPrincipal.parametersWebService_01.split(",")[3]);
                edtWsPass.setText(ActivityPrincipal.parametersWebService_01.split(",")[4]);
            }else{
                getParametersSock();
            }

        }

        Log.v(TAG,"parametersWsn " + ActivityPrincipal.parametersWsn.toString() + " " + ActivityPrincipal.parametersWsn.size());

        if(ActivityPrincipal.parametersWsn.size() < 5){
            getParametersWsn();
        }else if(ActivityPrincipal.parametersWsn.size() == 5){
            //192.168.0.1,TEMPUS_WS_T10,80,TEMPUS,TEMPUSSCA
            Log.v(TAG,"parametersWsn " + ActivityPrincipal.parametersWsn.toString());
            edtWsnServer.setText(ActivityPrincipal.parametersWsn.get(0));
            edtWsnCompany.setText(ActivityPrincipal.parametersWsn.get(1));
            edtWsnPort.setText(ActivityPrincipal.parametersWsn.get(2));
            edtWsnUser.setText(ActivityPrincipal.parametersWsn.get(3));
            edtWsnPass.setText(ActivityPrincipal.parametersWsn.get(4));
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

        btnSaveServerDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(TAG,"Parámetros a guardar:" + edtSyncHost.getText().toString() + ":" + edtSyncPort.getText().toString() + "/" + edtSyncDB.getText().toString() + "/" + edtSyncUser.getText().toString() + "," + edtSyncPass.getText().toString());

                if (edtSyncHost.getText().toString().isEmpty() || edtSyncDB.getText().toString().isEmpty() || edtSyncPort.getText().toString().isEmpty() || edtSyncUser.getText().toString().isEmpty() || edtSyncPass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Debe llenar correctamente los campos", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySincronizacion.this, R.style.AlertDialogCustom));

                    builder
                            .setTitle("Guardar Servidor de Base de Datos")
                            .setMessage("¿Desea guardar los parametros?")
                            .setIcon(android.R.drawable.ic_dialog_dialer)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    try{
                                        Log.v(TAG,"Guardando parametros");

                                        QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
                                        queriesServicios.update("SERVIDOR_DATOS_PRINCIPAL",edtSyncHost.getText().toString(),edtSyncHost.getText().toString(),"",edtSyncDB.getText().toString(),edtSyncPort.getText().toString(),edtSyncUser.getText().toString(),edtSyncPass.getText().toString(),false);

                                        queriesLogTerminal.insertLogTerminal(TAG,
                                                "DB" + "|" +
                                                edtSyncHost.getText().toString() + "|" +
                                                edtSyncDB.getText().toString() + "|" +
                                                edtSyncPort.getText().toString() + "|" +
                                                edtSyncUser.getText().toString() + "|" +
                                                edtSyncPass.getText().toString()
                                                ,ActivityPrincipal.UserSession);

                                        WifiManager wifiManager = (WifiManager) ActivityPrincipal.context.getSystemService(Context.WIFI_SERVICE);
                                        if(wifiManager.isWifiEnabled()){
                                            wifiManager.setWifiEnabled(false);
                                            wifiManager.setWifiEnabled(true);
                                        }

                                        Toast.makeText(getApplicationContext(),"Servidor de BD actualizado", Toast.LENGTH_SHORT).show();

                                        Log.v(TAG,"Parametros guardados");

                                    }catch (Exception e){
                                        Log.e(TAG,"btnSaveServerDB.setOnClickListener " + e.getMessage());
                                    }

                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }

            }
        });

        btnClearServerDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySincronizacion.this, R.style.AlertDialogCustom));

                builder
                        .setTitle("Limpiar Servidor de Base de Datos")
                        .setMessage("¿Desea limpiar los parametros?")
                        .setIcon(android.R.drawable.ic_dialog_dialer)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try{
                                    Log.v(TAG,"Limpiando parametros");

                                    edtSyncHost.setText("");
                                    edtSyncPort.setText("");
                                    edtSyncDB.setText("");
                                    edtSyncUser.setText("");
                                    edtSyncPass.setText("");

                                    QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
                                    queriesServicios.update("SERVIDOR_DATOS_PRINCIPAL",null,null,null,null,null,null,null,true);

                                    WifiManager wifiManager = (WifiManager) ActivityPrincipal.context.getSystemService(Context.WIFI_SERVICE);
                                    if(wifiManager.isWifiEnabled()){
                                        wifiManager.setWifiEnabled(false);
                                        wifiManager.setWifiEnabled(true);
                                    }
                                    Toast.makeText(getApplicationContext(),"Servidor de BD actualizado", Toast.LENGTH_SHORT).show();

                                    Log.v(TAG,"Parametros guardados");

                                }catch (Exception e){
                                    Log.e(TAG,"btnClearServerDB.setOnClickListener " + e.getMessage());
                                }

                            }})
                        .setNegativeButton(android.R.string.no, null).show();

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
                                    edtWsCompany.setEnabled(false);
                                    edtWsPort.setEnabled(false);
                                    edtWsUser.setEnabled(false);
                                    edtWsPass.setEnabled(false);
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
                                    edtWsCompany.setEnabled(true);
                                    edtWsPort.setEnabled(true);
                                    edtWsUser.setEnabled(true);
                                    edtWsPass.setEnabled(true);
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

                                            ActivityPrincipal.parametersWebService_01 = "192.168.0.1" + "," + edtWsCompany.getText().toString() + "," + "80" + "," + edtWsUser.getText().toString() + "," + edtWsPass.getText().toString();
                                            saveParameter("WEBSERVICE_01",ActivityPrincipal.parametersWebService_01);

                                            if(ActivityPrincipal.parametersSock.get(1).length() < 16){
                                                queriesLogTerminal.insertLogTerminal(TAG,
                                                        "WS" + "|" +
                                                                edtWsServer.getText().toString() + "|" +
                                                                edtWsCompany.getText().toString() + "|" +
                                                                edtWsPort.getText().toString() + "|" +
                                                                edtWsUser.getText().toString() + "|" +
                                                                edtWsPass.getText().toString()
                                                        ,ActivityPrincipal.UserSession);
                                            }

                                        }
                                    }catch (Exception e){
                                        Log.e(TAG,"parametersArray " + e.getMessage());
                                    }

                                    edtWsServer.setEnabled(true);
                                    edtWsCompany.setEnabled(true);
                                    edtWsPort.setEnabled(true);
                                    edtWsUser.setEnabled(true);
                                    edtWsPass.setEnabled(true);
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


        btnSaveWsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edtWsnServer.getText().toString().isEmpty() || edtWsnCompany.getText().toString().isEmpty() || edtWsnPort.getText().toString().isEmpty() || edtWsnUser.getText().toString().isEmpty() || edtWsnPass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Debe llenar correctamente los campos", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySincronizacion.this, R.style.AlertDialogCustom));

                    builder
                            .setTitle("Guardar Web Service Nativo")
                            .setMessage("¿Desea guardar los parametros?")
                            .setIcon(android.R.drawable.ic_dialog_dialer)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    try{
                                        Log.v(TAG,"Guardando parametros");


                                        /*
                                        QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
                                        queriesServicios.update("SERVIDOR_DATOS_PRINCIPAL",edtSyncHost.getText().toString(),edtSyncHost.getText().toString(),"",edtSyncDB.getText().toString(),edtSyncPort.getText().toString(),edtSyncUser.getText().toString(),edtSyncPass.getText().toString(),false);

                                        queriesLogTerminal.insertLogTerminal(TAG,
                                                "DB" + "|" +
                                                        edtSyncHost.getText().toString() + "|" +
                                                        edtSyncDB.getText().toString() + "|" +
                                                        edtSyncPort.getText().toString() + "|" +
                                                        edtSyncUser.getText().toString() + "|" +
                                                        edtSyncPass.getText().toString()
                                                ,ActivityPrincipal.UserSession);

                                        WifiManager wifiManager = (WifiManager) ActivityPrincipal.context.getSystemService(Context.WIFI_SERVICE);
                                        if(wifiManager.isWifiEnabled()){
                                            wifiManager.setWifiEnabled(false);
                                            wifiManager.setWifiEnabled(true);
                                        }

                                        Toast.makeText(getApplicationContext(),"Servidor de BD actualizado", Toast.LENGTH_SHORT).show();

                                        Log.v(TAG,"Parametros guardados");

                                        */

                                    }catch (Exception e){
                                        Log.e(TAG,"btnSaveServerDB.setOnClickListener " + e.getMessage());
                                    }

                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }


            }
        });

        /*
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
        */

        btnUsbMarcas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread threadUsbMarcas = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {


                            Log.v(TAG,"threadUsbMarcas inicio ");
                            //Toast.makeText(ActivityPrincipal.context,"Configurando Ethernet",Toast.LENGTH_LONG).show();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnUsbMarcas.setEnabled(false);
                                    btnUsbMarcas.setText("Procesando");
                                }
                            });

                            Log.v(TAG,"threadUsbMarcas otgmode " + ProcessSyncUSB.otgmode);
                            while(!ProcessSyncUSB.otgmode){
                                Log.v(TAG,"threadUsbMarcas otgmode while " + ProcessSyncUSB.otgmode);
                                Thread.sleep(250);
                            }
                            Thread.sleep(1000);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //btnUsbMarcas.setTextSize(40);
                                    btnUsbMarcas.setText(".");
                                }
                            });
                            Thread.sleep(250);


                            while(ProcessSyncUSB.otgmode){
                                Log.v(TAG,"threadUsbMarcas otgmode while " + ProcessSyncUSB.otgmode);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(ProcessSyncUSB.lfilenamemarcaciones > 0){
                                            DecimalFormat decimalFormat = new DecimalFormat("#.00");
                                            btnUsbMarcas.setText(decimalFormat.format(ProcessSyncUSB.lfilenamemarcaciones / 1000) + " KB");
                                        }else{
                                            if(btnUsbMarcas.getText().length() == 3){
                                                btnUsbMarcas.setText(".");
                                            }else{
                                                btnUsbMarcas.setText(btnUsbMarcas.getText() + ".");
                                            }
                                            //btnUsbMarcas.setText("0.00 KB");
                                        }

                                    }
                                });
                                Thread.sleep(250);
                            }

                            Log.v(TAG,"ProcessSyncUSB.msg " + ProcessSyncUSB.msg);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnUsbMarcas.setEnabled(false);
                                    btnUsbMarcas.setText(ProcessSyncUSB.msg);
                                }

                            });
                            Thread.sleep(2000);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(ProcessSyncUSB.msg.equals("Archivo Guardado")) {
                                        btnUsbMarcas.setEnabled(false);
                                        btnUsbMarcas.setText("Extraer USB");
                                    }
                                }

                            });
                            Thread.sleep(3000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnUsbMarcas.setEnabled(true);
                                    btnUsbMarcas.setText("Guardar Marcaciones");
                                }
                            });



                            //Toast.makeText(ActivityPrincipal.context,"Ethernet Configurado",Toast.LENGTH_LONG).show();
                            Log.v(TAG,"threadUsbMarcas fin ");
                        } catch (Exception e) {
                            Log.e(TAG,"threadUsbMarcas " + e.getMessage());
                        }

                    }
                });

                // Solcititar permisos
                // int permissionCheck = ContextCompat.checkSelfPermission(ActivitySincronizacion.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


                // Iniciar proceso para guardar marcaciones BCK
                // Hilo para la extracción de marcas y escritura del archivo .json de marcas en el USB externo
                ProcessSyncUSB processSyncUSB = new ProcessSyncUSB();
                processSyncUSB.startOTGOnOff();
                // Hilo para mostrar mensajes en el boton "Guardar Marcaciones" mientras se copia a USB al archivo marcaciones
                threadUsbMarcas.start();

                //Toast.makeText(ActivityPrincipal.context,"Procesando",Toast.LENGTH_SHORT).show();

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

        //
        Log.v(TAG,"statusReplicateReading.start()");
        statusReplicateReading.start();

        Log.v(TAG,"statusLog.start()");
        statusLog.start();

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
                            edtWsCompany.setEnabled(false);
                            edtWsPort.setEnabled(false);
                            edtWsUser.setEnabled(false);
                            edtWsPass.setEnabled(false);
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
                            edtWsCompany.setEnabled(true);
                            edtWsPort.setEnabled(true);
                            edtWsUser.setEnabled(true);
                            edtWsPass.setEnabled(true);
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
                                    edtWsCompany.setText(ActivityPrincipal.parametersWebService_01.split(",")[1]);
                                    edtWsPort.setText(ActivityPrincipal.parametersSock.get(2));
                                    edtWsUser.setText(ActivityPrincipal.parametersWebService_01.split(",")[3]);
                                    edtWsPass.setText(ActivityPrincipal.parametersWebService_01.split(",")[4]);

                                }
                            }catch (Exception e){
                                Log.e(TAG,"parametersArray " + e.getMessage());
                            }

                            edtWsServer.setEnabled(true);
                            edtWsCompany.setEnabled(true);
                            edtWsPort.setEnabled(true);
                            edtWsUser.setEnabled(true);
                            edtWsPass.setEnabled(true);
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


    private void getParametersWsn(){

        try{
            List<Parameters> parametersList = queriesParameters.select_one_row("WEBSERVICEN_01");
            ActivityPrincipal.parametersWsn.add(parametersList.get(0).getValue().split(",")[0]);
            ActivityPrincipal.parametersWsn.add(parametersList.get(0).getValue().split(",")[1]);
            ActivityPrincipal.parametersWsn.add(parametersList.get(0).getValue().split(",")[2]);
            ActivityPrincipal.parametersWsn.add(parametersList.get(0).getValue().split(",")[3]);
            ActivityPrincipal.parametersWsn.add(parametersList.get(0).getValue().split(",")[4]);

            if(!ActivityPrincipal.parametersWsn.isEmpty()){
                edtWsnServer.setText(ActivityPrincipal.parametersWsn.get(0));
                edtWsnCompany.setText(ActivityPrincipal.parametersWsn.get(1));
                edtWsnPort.setText(ActivityPrincipal.parametersWsn.get(2));
                edtWsnUser.setText(ActivityPrincipal.parametersWsn.get(3));
                edtWsnPass.setText(ActivityPrincipal.parametersWsn.get(4));
            }

        }catch (Exception e){
            Log.e(TAG,"getParametersWsn " + e.getMessage());
        }

    }

    Thread statusReplicateReading = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.v(TAG,"statusReplicateReading");
            while (true) {
                try {
                    //Log.v(TAG,"inicio statusReplicateReading");
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                if(QueriesPersonalTipolectoraBiometria.statusReplicate.equalsIgnoreCase("")){
                                    if(lblrep0.getText().toString().equalsIgnoreCase("Forzar Replica:")){
                                        lblrep0.setText("Forzar Replica:.");
                                    }else if(lblrep0.getText().toString().equalsIgnoreCase("Forzar Replica:.")){
                                        lblrep0.setText("Forzar Replica:..");
                                    }else if(lblrep0.getText().toString().equalsIgnoreCase("Forzar Replica:..")){
                                        lblrep0.setText("Forzar Replica:...");
                                    }else if(lblrep0.getText().toString().equalsIgnoreCase("Forzar Replica:...")){
                                        lblrep0.setText("Forzar Replica:");
                                    }
                                }else{
                                    lblrep0.setText("Forzar Replica: " + QueriesPersonalTipolectoraBiometria.statusReplicate);
                                }
                        }
                    });
                }catch (InterruptedException e){
                    Log.e(TAG,"statusReplicateReading InterruptedException " + e.toString());
                    return;
                }catch (Exception e) {
                    Log.e(TAG,"statusReplicateReading lblrep0.setText " + e.getMessage());
                }
            }
        }
    });

    private long saveParameter(String idparameter,String value){
        Parameters parameters = new Parameters();
        parameters.setIdparameter(idparameter);
        parameters.setParameter("");
        parameters.setValue(value);
        parameters.setSubparameters("");
        parameters.setEnable(1);
        parameters.setFechaHoraSinc(fechahora.getFechahora());
        Log.v(TAG,"parameters " + parameters.toString());
        long output = -2;
        try{
            output =  queriesParameters.updateParameter(parameters);
            Log.v(TAG,"saveParameter " + output);
        }catch (Exception e){
            Log.e(TAG,"saveParameter " + e.getMessage());
        }

        return output;

    }


    Thread statusLog = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.v(TAG,"statusLog");
            while (true) {
                try{
                    Thread.sleep(500);
                    //Log.v(TAG,"inicio statusLog");
                    //Log.v(TAG,"logWsLevel01>>>" + MainEthernet.logWsLevel01);
                    //Log.v(TAG,"logWsLevel02>>>" + MainEthernet.logWsLevel02);
                    //Log.v(TAG,"logWsLevel03>>>" + MainEthernet.logWsLevel03);
                    //Log.v(TAG,"logWsLevel04>>>" + MainEthernet.logWsLevel04);

                    if(txvLogWsLevel01.getText().toString().equalsIgnoreCase(MainEthernet.logWsLevel01)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel01.setText(MainEthernet.logWsLevel01);
                                txvLogWsLevel01.setTextColor(Color.BLACK);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel01.setTextColor(Color.GREEN);
                                txvLogWsLevel01.setText(MainEthernet.logWsLevel01);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel01.setTextColor(Color.BLACK);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel01.setTextColor(Color.GREEN);
                            }
                        });
                    }

                    if(txvLogWsLevel02.getText().toString().equalsIgnoreCase(MainEthernet.logWsLevel02)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel02.setText(MainEthernet.logWsLevel02);
                                txvLogWsLevel02.setTextColor(Color.BLACK);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel02.setTextColor(Color.GREEN);
                                txvLogWsLevel02.setText(MainEthernet.logWsLevel02);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel02.setTextColor(Color.BLACK);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel02.setTextColor(Color.GREEN);
                            }
                        });
                    }

                    if(txvLogWsLevel03.getText().toString().equalsIgnoreCase(MainEthernet.logWsLevel03)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel03.setText(MainEthernet.logWsLevel03);
                                txvLogWsLevel03.setTextColor(Color.BLACK);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel03.setTextColor(Color.GREEN);
                                txvLogWsLevel03.setText(MainEthernet.logWsLevel03);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel03.setTextColor(Color.BLACK);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel03.setTextColor(Color.GREEN);
                            }
                        });
                    }

                    if(txvLogWsLevel04.getText().toString().equalsIgnoreCase(MainEthernet.logWsLevel04)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel04.setText(MainEthernet.logWsLevel04);
                                txvLogWsLevel04.setTextColor(Color.BLACK);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel04.setTextColor(Color.BLUE);
                                txvLogWsLevel04.setText(MainEthernet.logWsLevel04);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel04.setTextColor(Color.BLACK);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogWsLevel04.setTextColor(Color.BLUE);
                            }
                        });
                    }

                    if(txvLogServerDBLevel01.getText().toString().equalsIgnoreCase(ConexionServidor.logserverDBLevel01)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogServerDBLevel01.setText(ConexionServidor.logserverDBLevel01);
                                txvLogServerDBLevel01.setTextColor(Color.BLACK);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogServerDBLevel01.setTextColor(Color.GREEN);
                                txvLogServerDBLevel01.setText(ConexionServidor.logserverDBLevel01);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogServerDBLevel01.setTextColor(Color.BLACK);
                            }
                        });
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvLogServerDBLevel01.setTextColor(Color.GREEN);
                            }
                        });
                    }

                }catch (InterruptedException  e){
                    Log.e(TAG,"statusLog InterruptedException " + e.toString());
                    return;
                }catch (Exception e){
                    Log.e(TAG,"statusLog " + e.getMessage());
                }
            }
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy");
        try{
            if(statusLog.isAlive()){
                Log.v(TAG,"Deteniendo statusLog");
                statusLog.interrupt();
                Log.v(TAG,"statusLog detenido");
            }
            if(statusReplicateReading.isAlive()){
                Log.v(TAG,"Deteniendo statusReplicateReading");
                statusReplicateReading.interrupt();
                Log.v(TAG,"statusReplicateReading detenido");
            }
        }catch (Exception e){
            Log.e(TAG,"statusLog.stop() " + e.getMessage());
        }
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0
                        || grantResults[0] == PackageManager.PERMISSION_GRANTED
                        || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    /* User checks permission. */

                } else {
                    Toast.makeText(ActivitySincronizacion.this, "Permission is denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }


}
