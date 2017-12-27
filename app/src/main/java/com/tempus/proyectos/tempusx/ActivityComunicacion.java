package com.tempus.proyectos.tempusx;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkAddress;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.bluetoothSerial.MainEthernet;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.tcpSerial.UsrTCP;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class ActivityComunicacion extends Activity {

    final int REQUEST_READ_PHONE_STATE = 1;
    private TelephonyManager telephonyManager;
    private SignalStrength      signalStrength;

    String TAG = "TX-AC";

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Connectivity connectivity;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    String macWlan;
    String macEth;

    String mask;
    String s_dns1;
    String s_dns2;
    String s_gateway;
    String s_ipAddress;
    String s_leaseDuration;
    String s_netmask;
    String s_serverAddress;

    WifiManager wifi;

    Boolean interfaceEth0Enabled;
    Boolean interfaceWlan0Enabled;
    Boolean interfacePpp00Enabled;


    /* --- Declaración de Componentes de la Interfaz --- */

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;

    ImageView btnMasterComunicacion;
    EditText edtEthIp;
    EditText edtEthMascara;
    EditText edtEthPuerta;
    EditText edtEthDns;

    EditText edtWlanIp;
    EditText edtWlanMascara;
    EditText edtWlanPuerta;

    TextView lblDescDataState;
    TextView lblDescSimState;
    TextView lblDescNetworkType;
    TextView lblDescCountrySim;
    TextView lblDescOperatorSim;
    TextView lblDescSenial;


    TabHost host;

    Button btnWifi;
    Button btnEth;

    Switch swtStatusEth0;
    Switch swtDhcpEth0;
    Switch swtStatusWlan0;
    Switch swtStatusPpp0;


    TextView txvWifiState;

    TextView txvStatusWlan0;
    TextView txvStatusPpp0;
    TextView txvStatusEth0;

    /* --- MainEthernet --- */
    MainEthernet mainEthernet = new MainEthernet();

    /* --- UsrTCP --- */
    UsrTCP usrTCP = new UsrTCP();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion);

        int PERMISSION_READ_PHONE_STATE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (PERMISSION_READ_PHONE_STATE != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"GPRS DATA " + "PERMISSION_READ_PHONE_STATE: FALSE");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            Log.d(TAG,"GPRS DATA " + "PERMISSION_READ_PHONE_STATE: TRUE");
        }

        Log.wtf(TAG,"POINT " + "PUNTO 1");

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        connectivity = new Connectivity();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Comunicacion";
        //TAG = "ActivityComunicacion";

        /* --- Inicialización de Variables Locales --- */

        interfaceEth0Enabled = false;
        interfaceWlan0Enabled = false;
        interfacePpp00Enabled = false;

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

        edtWlanIp = (EditText) findViewById(R.id.edtWlanIp);
        edtWlanMascara = (EditText) findViewById(R.id.edtWlanMascara);
        edtWlanPuerta = (EditText) findViewById(R.id.edtWlanPuerta);

        btnMasterComunicacion = (ImageView) findViewById(R.id.btnMasterComunicacion);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterComunicacion,"/tempus/img/config/","logo.png");

        edtEthIp = (EditText) findViewById(R.id.edtEthIp);
        edtEthMascara = (EditText) findViewById(R.id.edtEthMascara);
        edtEthPuerta = (EditText) findViewById(R.id.edtEthPuerta);
        edtEthDns = (EditText) findViewById(R.id.edtEthDns);

        btnWifi = (Button) findViewById(R.id.btnWifi);
        btnEth = (Button) findViewById(R.id.btnEth);

        lblDescDataState = (TextView) findViewById(R.id.lblDescDataState);
        lblDescSimState = (TextView) findViewById(R.id.lblDescSimState);
        lblDescNetworkType = (TextView) findViewById(R.id.lblDescNetworkType);
        lblDescCountrySim = (TextView) findViewById(R.id.lblDescCountrySim);
        lblDescOperatorSim = (TextView) findViewById(R.id.lblDescOperatorSim);
        lblDescSenial = (TextView) findViewById(R.id.lblDescSenial);

        swtStatusEth0 = (Switch) findViewById(R.id.swtStatusEth0);
        swtDhcpEth0 = (Switch) findViewById(R.id.swtDhcpEth0);
        swtStatusWlan0 = (Switch) findViewById(R.id.swtStatusWlan0);
        swtStatusPpp0 = (Switch) findViewById(R.id.swtStatusPpp0);

        txvStatusWlan0 = (TextView) findViewById(R.id.txvStatusWlan0);
        txvStatusPpp0 = (TextView) findViewById(R.id.txvStatusPpp0);
        txvStatusEth0 = (TextView) findViewById(R.id.txvStatusEth0);

        Log.wtf(TAG,"POINT " + "PUNTO 2");

        if (isMobileDataEnabled()){
            swtStatusPpp0.setChecked(true);
        } else {
            swtStatusPpp0.setChecked(false);
        }

        Log.wtf(TAG,"POINT " + "PUNTO 3");

        host = (TabHost)findViewById(R.id.tabHostComm);
        host.setup();

        //Tab 1

        TabHost.TabSpec spec;

        if (ActivityPrincipal.INTERFACE_WLAN){
            spec = host.newTabSpec("Tab1");
            spec.setContent(R.id.tab3);
            spec.setIndicator("WIFI");
            host.addTab(spec);
        }


        //Tab 2
        if (ActivityPrincipal.INTERFACE_PPP){
            spec = host.newTabSpec("Tab2");
            spec.setContent(R.id.tab2);
            spec.setIndicator("GPRS");
            host.addTab(spec);
        }


        //Tab 3
        if (ActivityPrincipal.INTERFACE_ETH){
            spec = host.newTabSpec("Tab3");
            spec.setContent(R.id.tab1);
            spec.setIndicator("ETHERNET");
            host.addTab(spec);
        }


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

        Log.wtf(TAG,"POINT " + "PUNTO 4");

        txvWifiState = (TextView) findViewById(R.id.txvWifiState);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);
        disableDataWifi();



        Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());

        if(ActivityPrincipal.parametersEthernet.size() == 0){
            getParametersEth();
        }else if(ActivityPrincipal.parametersEthernet.size() == 4){
            Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());

            edtEthIp.setText(ActivityPrincipal.parametersEthernet.get(1));
            edtEthMascara.setText(ActivityPrincipal.parametersEthernet.get(2));
            edtEthPuerta.setText(ActivityPrincipal.parametersEthernet.get(3));

            if(ActivityPrincipal.parametersEthernet.get(0).equalsIgnoreCase("DHCP")){
                getParametersEth();
                //swtDhcpEth0.setChecked(true);
                //edtEthIp.setEnabled(false);
                //edtEthMascara.setEnabled(false);
                //edtEthPuerta.setEnabled(false);
            }else{
                swtDhcpEth0.setChecked(false);
                edtEthIp.setEnabled(true);
                edtEthMascara.setEnabled(true);
                edtEthPuerta.setEnabled(true);
            }
        }





        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // Listener for the signal strength.
        final PhoneStateListener mListener = new PhoneStateListener() {
            int mSignalStrength = 0;
            public static final int INVALID = Integer.MAX_VALUE;
            @Override
            public void onSignalStrengthsChanged(SignalStrength sStrength) {
                signalStrength = sStrength;

                Log.e(TAG,"mListener INICIO " + String.valueOf(mSignalStrength));

                //Log.e("SIGNAL 1", String.valueOf(signalStrength.getCdmaDbm()));
                //Log.e("SIGNAL 2", String.valueOf(signalStrength.getCdmaEcio()));
                //Log.e("SIGNAL 3", String.valueOf(signalStrength.getEvdoDbm()));
                //Log.e("SIGNAL 4", String.valueOf(signalStrength.getEvdoSnr()));
                //Log.e("SIGNAL 5", String.valueOf(signalStrength.getEvdoEcio()));
                //Log.e("SIGNAL 6", String.valueOf(signalStrength.getGsmSignalStrength()));
                //Log.e("SIGNAL 7", String.valueOf(signalStrength.getGsmBitErrorRate()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mSignalStrength = signalStrength.getLevel();
                    Log.e(TAG,"SIGNAL getLevel " + String.valueOf(mSignalStrength));
                }

                int signalStrengthDbm = getSignalStrengthByName(signalStrength, "getDbm");
                int signalStrengthAsuLevel = getSignalStrengthByName(signalStrength, "getAsuLevel");
                Log.e(TAG,"SIGNAL getDbm " + String.valueOf(signalStrengthDbm));
                Log.e(TAG,"SIGNAL getAsuLevel " + String.valueOf(signalStrengthAsuLevel));

                loadDataSIM();
                if (!lblDescSimState.getText().toString().equalsIgnoreCase("AUSENTE")) {
                    lblDescSenial.setText(getLevelSignal(mSignalStrength));
                } else {
                    lblDescSenial.setText("DESCONOCIDO (*)");
                }
            }

            private int getSignalStrengthByName(SignalStrength signalStrength, String methodName) {
                try {
                    Class classFromName = Class.forName(SignalStrength.class.getName());
                    java.lang.reflect.Method method = classFromName.getDeclaredMethod(methodName);
                    Object object = method.invoke(signalStrength);
                    return (int)object;
                }
                catch (Exception ex) {
                    return INVALID;
                }
            }
        };


        Log.wtf(TAG,"POINT " + "PUNTO 5");
        try {
            telephonyManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        } catch (Exception e) {
            Log.e(TAG,"registerListener " + e.getMessage());
        }


        /* --- Inicialización de Parametros Generales --- */

        /* ------------------------------ */

        /* --- Eventos --- */

        btnMasterComunicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityComunicacion.this, ActivityMenu.class , "","");
            }
        });

        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                Intent startMain = getPackageManager().getLaunchIntentForPackage("com.tempus.ecernar.overlaywifi");
                startActivity(startMain);
            }
        });

        btnEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "";
                String parameter = "";

                Thread threadEthernetcfg = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.v(TAG,"threadEthernetcfg inicio ");
                            //Toast.makeText(ActivityPrincipal.context,"Configurando Ethernet",Toast.LENGTH_LONG).show();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    swtDhcpEth0.setEnabled(false);
                                    edtEthIp.setEnabled(false);
                                    edtEthMascara.setEnabled(false);
                                    edtEthPuerta.setEnabled(false);
                                    edtEthDns.setEnabled(false);
                                    btnEth.setEnabled(false);
                                    btnEth.setText("Configurando");
                                }
                            });

                            Log.v(TAG,"threadEthernetcfg enableSetEthernet " + MainEthernet.enableSetEthernet);
                            while(!MainEthernet.enableSetEthernet){
                                Log.v(TAG,"threadEthernetcfg enableSetEthernet while " + MainEthernet.enableSetEthernet);
                                Thread.sleep(250);
                            }
                            Thread.sleep(1000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnEth.setText(".");
                                }
                            });

                            while(MainEthernet.atCommandMode){
                                Log.v(TAG,"threadEthernetcfg atCommandMode while " + MainEthernet.atCommandMode);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(btnEth.getText().length() == 3){
                                            btnEth.setText(".");
                                        }else{
                                            btnEth.setText(btnEth.getText() + ".");
                                        }
                                    }
                                });
                                Thread.sleep(1000);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    swtDhcpEth0.setEnabled(true);
                                    edtEthIp.setEnabled(true);
                                    edtEthMascara.setEnabled(true);
                                    edtEthPuerta.setEnabled(true);
                                    edtEthDns.setEnabled(true);
                                    btnEth.setEnabled(true);
                                    btnEth.setText("Configurar");
                                }
                            });


                            Log.v(TAG,"hexStringcfg " + MainEthernet.hexStringcfg);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        String[] parametersArray = usrTCP.getParameters(MainEthernet.hexStringcfg);
                                        Log.v(TAG,"parametersArray[" + parametersArray.length + "] = " + parametersArray[0] + " - " + parametersArray[1] + " - " +parametersArray[2] + " - " +parametersArray[3]);
                                        if(parametersArray.length>0){
                                            Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());
                                            ActivityPrincipal.parametersEthernet.clear();
                                            Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());
                                            ActivityPrincipal.parametersEthernet.add(parametersArray[0]);
                                            ActivityPrincipal.parametersEthernet.add(parametersArray[1]);
                                            ActivityPrincipal.parametersEthernet.add(parametersArray[2]);
                                            ActivityPrincipal.parametersEthernet.add(parametersArray[3]);
                                            Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());

                                            edtEthIp.setText(ActivityPrincipal.parametersEthernet.get(1));
                                            edtEthMascara.setText(ActivityPrincipal.parametersEthernet.get(2));
                                            edtEthPuerta.setText(ActivityPrincipal.parametersEthernet.get(3));

                                        }
                                    }catch (Exception e){
                                        Log.e(TAG,"parametersArray " + e.getMessage());
                                    }

                                    if(ActivityPrincipal.parametersEthernet.get(0).equalsIgnoreCase("DHCP")){
                                        swtDhcpEth0.setChecked(true);
                                        edtEthIp.setEnabled(false);
                                        edtEthMascara.setEnabled(false);
                                        edtEthPuerta.setEnabled(false);
                                    }else{
                                        swtDhcpEth0.setChecked(false);
                                        edtEthIp.setEnabled(true);
                                        edtEthMascara.setEnabled(true);
                                        edtEthPuerta.setEnabled(true);
                                    }

                                    edtEthDns.setEnabled(true);
                                    btnEth.setEnabled(true);
                                    btnEth.setText("Configurar");
                                }
                            });

                            //Toast.makeText(ActivityPrincipal.context,"Ethernet Configurado",Toast.LENGTH_LONG).show();
                            Log.v(TAG,"threadEthernetcfg fin ");
                        } catch (Exception e) {
                            Log.e(TAG,"threadEthernetcfg " + e.getMessage());
                        }

                    }
                });


                //mainEthernet.startEthernetATCommand("AT+WANN","=STATIC,192.168.0.78,255.255.255.0,192.168.0.2");
                if(swtDhcpEth0.isChecked()){
                    Log.v(TAG,"Parametros Validos (DHCP)");
                    parameter = "=DHCP";
                    mainEthernet.startEthernetATCommand("AT+WANN",parameter,true,true,true);
                    threadEthernetcfg.start();
                }else{
                    Log.v(TAG,"edtEthIp (validando) " + edtEthIp.getText().toString());
                    msg = validateParameters("IP", edtEthIp.getText().toString());
                    Log.v(TAG,"msg " + msg);
                    if(msg.replace(" ","").length()==0){
                        Log.v(TAG,"edtEthMascara (validando) " + edtEthMascara.getText().toString());
                        msg = validateParameters("Máscara",edtEthMascara.getText().toString());
                        Log.v(TAG,"msg " + msg);
                        if(msg.replace(" ","").length()==0){
                            Log.v(TAG,"edtEthPuerta (validando) " + edtEthPuerta.getText().toString());
                            msg = validateParameters("P. Enlace",edtEthPuerta.getText().toString());
                            Log.v(TAG,"msg " + msg);
                            if(msg.replace(" ","").length() > 0){
                                Toast.makeText(ActivityPrincipal.context, msg, Toast.LENGTH_SHORT).show();
                            }else{
                                Log.v(TAG,"Parametros Validos (STATIC)");
                                parameter = "=STATIC" + "," + edtEthIp.getText().toString() + "," + edtEthMascara.getText().toString() + "," + edtEthPuerta.getText().toString();
                                Log.v(TAG,"parameter " + parameter);
                                mainEthernet.startEthernetATCommand("AT+WANN",parameter,true,true,true);
                                threadEthernetcfg.start();
                            }
                        }else{
                            Toast.makeText(ActivityPrincipal.context, msg, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ActivityPrincipal.context, msg, Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        swtStatusEth0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v(TAG,"swtStatusEth0 onCheckedChanged " + isChecked);
                try{
                    DBManager db = new DBManager(ActivityPrincipal.context);
                    db.open();
                    if (isChecked) {
                        db.execSQL("UPDATE TERMINAL_CONFIGURACION SET PARAMETRO = 'Ethernet,1'");
                        Log.v(TAG,"isChecked " + "Ethernet,1");
                        edtEthIp.setEnabled(true);
                        edtEthMascara.setEnabled(true);
                        edtEthPuerta.setEnabled(true);
                        edtEthDns.setEnabled(true);

                    } else {
                        db.execSQL("UPDATE TERMINAL_CONFIGURACION SET PARAMETRO = 'Ethernet,0'");
                        Log.v(TAG,"isChecked " + "Ethernet,0");
                        edtEthIp.setEnabled(false);
                        edtEthMascara.setEnabled(false);
                        edtEthPuerta.setEnabled(false);
                        edtEthDns.setEnabled(false);

                    }
                    db.close();
                }catch(Exception e){
                    Log.e(TAG,"swtStatusEth0 onCheckedChanged " + e.getMessage());
                }

            }
        });

        swtDhcpEth0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v(TAG,"swtDhcpEth0 onCheckedChanged " + isChecked);
                try{
                    if (isChecked) {
                        //edtEthIp.setText("");
                        //edtEthMascara.setText("");
                        //edtEthPuerta.setText("");
                        //edtEthDns.setText("");

                        edtEthIp.setEnabled(false);
                        edtEthMascara.setEnabled(false);
                        edtEthPuerta.setEnabled(false);
                        edtEthDns.setEnabled(false);
                    } else {
                        if(ActivityPrincipal.parametersEthernet.size() == 4){
                            Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());
                            edtEthIp.setText(ActivityPrincipal.parametersEthernet.get(1));
                            edtEthMascara.setText(ActivityPrincipal.parametersEthernet.get(2));
                            edtEthPuerta.setText(ActivityPrincipal.parametersEthernet.get(3));
                        }
                        edtEthIp.setEnabled(true);
                        edtEthMascara.setEnabled(true);
                        edtEthPuerta.setEnabled(true);
                        edtEthDns.setEnabled(true);
                    }

                }catch(Exception e){
                    Log.e(TAG,"swtDhcpEth0 onCheckedChanged " + e.getMessage());
                }

            }
        });

        swtStatusWlan0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    wifi.setWifiEnabled(true);
                    Log.v(TAG,"WIFI TRUE");

                    Thread thWifiState = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edtWlanIp.setEnabled(false);
                                    edtWlanMascara.setEnabled(false);
                                    edtWlanPuerta.setEnabled(false);
                                }
                            });

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (wifi.isWifiEnabled() && swtStatusWlan0.isChecked()){
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    checkWifiConnection();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    checkDataWifiConnection();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    thWifiState.start();
                } else {
                    wifi.setWifiEnabled(false);
                    Log.v(TAG,"WIFI FALSE");
                    disableDataWifi();
                }


            }
        });

        swtStatusPpp0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v(TAG,"swtStatusPpp0 onCheckedChanged " + isChecked);
                try{
                    if (isChecked) {
                        if (connectivity.existSIM(ActivityComunicacion.this)){
                            connectivity.setMobileDataState(ActivityComunicacion.this,true);
                        } else {
                            ui.showAlert(ActivityComunicacion.this,"warning","No hay SIM insertada.");
                            swtStatusPpp0.setChecked(false);
                        }

                    } else {
                        if (connectivity.existSIM(ActivityComunicacion.this)){
                            connectivity.setMobileDataState(ActivityComunicacion.this,false);
                        } else {
                            ui.showAlert(ActivityComunicacion.this,"warning","No hay SIM insertada.");
                            swtStatusPpp0.setChecked(false);
                        }

                    }
                }catch(Exception e){
                    Log.e(TAG,"swtStatusPpp0 onCheckedChanged " + e.getMessage());
                }

            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                checkWifiConnection();
                checkDataWifiConnection();
            }
        });

        t.start();

        Log.wtf(TAG,"POINT PUNTO 8");

        Shell sh = new Shell();
        String params[] = {"su","-c","busybox ifconfig"};
        String cadena = sh.exec(params);

        macWlan = connectivity.getMacAddress(cadena,"wlan0");
        macEth = connectivity.getMacAddress(cadena,"eth0");

        txvStatusWlan0.append(" "+ macWlan);
        txvStatusEth0.append(" "+ macEth);

        Log.d(TAG, ">>" + macWlan);
        Log.d(TAG, ">>" + macEth);

        Log.wtf(TAG,"POINT PUNTO 9");

        inicializarSwitches();

        Log.wtf(TAG,"POINT PUNTO 10");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    connectivity.TelephoneManager(this);
                }
                break;
            default:
                break;
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

    public void checkWifiConnection(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mWifi.getState();

        if (mWifi.isConnected()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txvWifiState.setText("SSID: " + wifi.getConnectionInfo().getSSID());
                }
            });
            Log.v(TAG, "WIFI CONECTADO");
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txvWifiState.setText("SSID: -");
                }
            });
            Log.v(TAG, "WIFI DESCONECTADO");
        }
    }



    public void checkDataWifiConnection() {
        try {
            Log.v(TAG,"checkDataWifiConnection: ");

            wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dinfo = wifi.getDhcpInfo();

            s_dns1=String.valueOf(intToIp(dinfo.dns1));
            s_dns2=String.valueOf(intToIp(dinfo.dns2));
            s_gateway=String.valueOf(intToIp(dinfo.gateway));
            s_ipAddress=String.valueOf(intToIp(dinfo.ipAddress));
            s_leaseDuration=String.valueOf(intToIp(dinfo.leaseDuration));
            s_netmask=String.valueOf(intToIp(dinfo.netmask));
            s_serverAddress=String.valueOf(intToIp(dinfo.serverAddress));

            Shell sh = new Shell();
            String params[] = {"su", "-c", "busybox ifconfig"};
            String data = sh.exec(params);

            if (s_ipAddress.equalsIgnoreCase("0.0.0.0")){
                s_ipAddress = "";
            }

            if (s_gateway.equalsIgnoreCase("0.0.0.0")){
                s_gateway = "";
            }

            mask = "";

            if (data.contains("wlan0")) {
                Log.v(TAG,"checkDataWifiConnection: Wlan Encontrada");
                String dataArray[] = data.split("\n");
                for (int i = 0; i < dataArray.length; i++){
                    if (dataArray[i].contains("wlan0")){
                        String linea = dataArray[i+1];
                        String d[] = linea.toLowerCase()
                                          .trim()
                                          .replace("inet addr:","~")
                                          .replace("bcast:","~")
                                          .replace("mask:","~").split("~");
                        mask = d[3];
                        break;
                    }
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edtWlanIp.setText(s_ipAddress);
                    edtWlanMascara.setText(mask);
                    edtWlanPuerta.setText(s_gateway);
                }
            });


        } catch (Exception e) {
            Log.v(TAG,e.toString());
        }
    }

    public void disableDataWifi() {
        txvWifiState.setText("SSID: -");

        Log.d(TAG,"disableDataWifi");
        edtWlanIp.setText("");
        edtWlanMascara.setText("");
        edtWlanPuerta.setText("");

        edtWlanIp.setEnabled(false);
        edtWlanMascara.setEnabled(false);
        edtWlanPuerta.setEnabled(false);
    }

    public String intToIp(int i) {

        return  ( i & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 24 ) & 0xFF );
    }

    public void inicializarSwitches() {
        // WIFI
        if (wifi.isWifiEnabled()) {
            swtStatusWlan0.setChecked(true);
        } else {
            swtStatusWlan0.setChecked(false);
        }
    }

    public void loadDataSIM() {
        String data[] = connectivity.TelephoneManager(this);
        try {
            lblDescDataState.setText(loadMobileDataState(data[3]));
            lblDescSimState.setText(loadSimCardState(data[14]));
            lblDescNetworkType.setText(data[10]);
            lblDescCountrySim.setText(data[11].toUpperCase());
            lblDescOperatorSim.setText(data[13]);
            lblDescSenial.setText(data[0]);
        } catch(Exception e) {
            Log.e(TAG,"loadDataSIM " + e.getMessage());
        }
    }

    public String loadMobileDataState(String id) {
        switch (id) {
            case "0":
                return "DESCONECTADO";
            case "1":
                return "CONECTANDO";
            case "2":
                return "CONECTADO";
            case "3":
                return "SUSPENDIDO";
            default:
                return "UNKNOWN (*)";
        }
    }

    public String loadSimCardState(String id) {
        switch (id) {
            case "0":
                return "DESCONOCIDO";
            case "1":
                return "AUSENTE";
            case "2":
                return "PIN REQUERIDO";
            case "3":
                return "PUK REQUERIDO";
            case "4":
                return "RED BLOQUEADA";
            case "5":
                return "LISTO";
            case "6":
                return "ERR / NOT_READY";
            case "7":
                return "ERR / PERM_DISABLED";
            case "8":
                return "ERR / CARD_IO_ERROR";
            default:
                return "UNKNOWN (*)";
        }
    }

    public String getLevelSignal(int signal) {

        String med = "";
        String val = "";

        for (int i = 0; i < 5; i++){
            if (i > signal) {
                med = med + " ";
            } else {
                med = med + "=";
            }
        }

        if (signal == 0){
            val = "MUY MALA";
        } else if (signal == 1) {
            val = "MALA";
        } else if (signal == 2) {
            val = "MODERADA";
        } else if (signal == 3) {
            val = "BUENA";
        } else if (signal == 4) {
            val = "EXCELENTE";
        } else {
            val = "DESCONOCIDO";
        }

        return "|"+med+"| " + val;
    }

    public Boolean isMobileDataEnabled(){
        Object connectivityService = getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean)m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String validateParameters(String name, String parameteEth) {
        Log.v(TAG,"parameteEth " + parameteEth);
        String[] octets = parameteEth.split("\\.");
        Log.v(TAG,"validateParameters octets " + octets.length + " - " + octets.toString());

        if(octets.length != 4) {
            return name + " (Octetos incompletos)";
        }else{
            for(String octet : octets) {
                try{
                    int val = Integer.parseInt(octet);
                    if(val > 255 || val < 0) {
                        return name + " (Octeto fuera de rango)";
                    }
                }catch (Exception e){
                    Log.e(TAG,"Octeto inválido " + e.getMessage());
                    return name + " (Octeto inválido)";
                }
            }
        }

        return "";

    }


    public void getParametersEth(){

        mainEthernet.startEthernetATCommand("AT+WANN","",false,true,true);

        Thread threadEthernetcfg = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG,"threadEthernetcfg runOnUiThread inicio ");
                try {
                    Log.v(TAG,"threadEthernetcfg inicio ");
                    //Toast.makeText(ActivityPrincipal.context,"Configurando Ethernet",Toast.LENGTH_LONG).show();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swtDhcpEth0.setEnabled(false);
                            edtEthIp.setEnabled(false);
                            edtEthMascara.setEnabled(false);
                            edtEthPuerta.setEnabled(false);
                            edtEthDns.setEnabled(false);
                            btnEth.setEnabled(false);
                            btnEth.setText("Actualizando");
                        }
                    });

                    Log.v(TAG,"threadEthernetcfg enableSetEthernet " + MainEthernet.enableSetEthernet);
                    while(!MainEthernet.enableSetEthernet){
                        Log.v(TAG,"threadEthernetcfg enableSetEthernet while " + MainEthernet.enableSetEthernet);
                        Thread.sleep(250);
                    }
                    Thread.sleep(1000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnEth.setText(".");
                        }
                    });

                    while(MainEthernet.atCommandMode){
                        Log.v(TAG,"threadEthernetcfg atCommandMode while " + MainEthernet.atCommandMode);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(btnEth.getText().length() == 3){
                                    btnEth.setText(".");
                                }else{
                                    btnEth.setText(btnEth.getText() + ".");
                                }
                            }
                        });
                        Thread.sleep(1000);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swtDhcpEth0.setEnabled(true);
                            edtEthIp.setEnabled(true);
                            edtEthMascara.setEnabled(true);
                            edtEthPuerta.setEnabled(true);
                            edtEthDns.setEnabled(true);
                            btnEth.setEnabled(true);
                            btnEth.setText("Configurar");
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
                                //Log.v(TAG,"parametersArray[" + parametersArray.length + "] = " + parametersArray[0] + " - " + parametersArray[1] + " - " +parametersArray[2] + " - " +parametersArray[3]);
                                if(parametersArray.length>0){
                                    //Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());
                                    ActivityPrincipal.parametersEthernet.clear();
                                    //Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());

                                    ActivityPrincipal.parametersEthernet.add(parametersArray[0]);
                                    ActivityPrincipal.parametersEthernet.add(parametersArray[1]);
                                    ActivityPrincipal.parametersEthernet.add(parametersArray[2]);
                                    ActivityPrincipal.parametersEthernet.add(parametersArray[3]);
                                    Log.v(TAG,"parametersEthernet " + ActivityPrincipal.parametersEthernet.toString());

                                    edtEthIp.setText(ActivityPrincipal.parametersEthernet.get(1));
                                    edtEthMascara.setText(ActivityPrincipal.parametersEthernet.get(2));
                                    edtEthPuerta.setText(ActivityPrincipal.parametersEthernet.get(3));

                                }
                            }catch (Exception e){
                                Log.e(TAG,"parametersArray " + e.getMessage());
                            }

                            if(ActivityPrincipal.parametersEthernet.get(0).equalsIgnoreCase("DHCP")){
                                swtDhcpEth0.setChecked(true);
                                edtEthIp.setEnabled(false);
                                edtEthMascara.setEnabled(false);
                                edtEthPuerta.setEnabled(false);
                            }else{
                                swtDhcpEth0.setChecked(false);
                                edtEthIp.setEnabled(true);
                                edtEthMascara.setEnabled(true);
                                edtEthPuerta.setEnabled(true);
                            }

                            edtEthDns.setEnabled(true);
                            btnEth.setEnabled(true);
                            btnEth.setText("Configurar");
                        }
                    });

                    //Toast.makeText(ActivityPrincipal.context,"Ethernet Configurado",Toast.LENGTH_LONG).show();
                    Log.v(TAG,"threadEthernetcfg fin ");
                } catch (Exception e) {
                    Log.e(TAG,"threadEthernetcfg " + e.getMessage());
                }
                Log.v(TAG,"threadEthernetcfg runOnUiThread fin ");
            }
        });

        threadEthernetcfg.start();

    }















}






















