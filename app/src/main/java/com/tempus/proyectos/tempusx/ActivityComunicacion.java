package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkAddress;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class ActivityComunicacion extends Activity {

    String TAG;

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Connectivity connectivity;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

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

    String Eth0Name;
    String Eth0Interface;

    String Wlan0Name;
    String Wlan0Interface;

    List<InterfaceAddress> interfaceDataEth0;
    List<InterfaceAddress> interfaceDataWlan0;

    int size = 0;
    List<ScanResult> results;

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;

    /* --- Declaración de Componentes de la Interfaz --- */

    ImageView btnMasterComunicacion;
    EditText edtEthIp;
    EditText edtEthMascara;
    EditText edtEthPuerta;

    EditText edtWlanIp;
    EditText edtWlanMascara;
    EditText edtWlanPuerta;

    TabHost host;
    ListView lstWifi;

    Button buttonWifi;

    Switch swtStatusEth0;
    Switch swtStatusWlan0;
    Switch swtStatusPpp0;

    TextView txvIndicador;
    TextView txvWifiState;
    TextView txvWifiConfigArea;
    TextView txvWifiConfigSSID;
    TextView txvWifiConfigCapab;
    TextView txvWifiConfigPass;
    EditText edtWifiConfigPass;
    CheckBox chkWifiConfigMostrarPass;
    Button btnWifiConfigCancelar;
    Button btnWifiConfigConectar;
    RadioButton rbnWifiStatic;
    RadioButton rbnWifiDynamic;
    Button btnConfRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        connectivity = new Connectivity();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Comunicacion";
        TAG = "ActivityComunicacion";

        /* --- Inicialización de Variables Locales --- */

        interfaceEth0Enabled = false;
        interfaceWlan0Enabled = false;
        interfacePpp00Enabled = false;

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterComunicacion = (ImageView) findViewById(R.id.btnMasterComunicacion);

        edtWlanIp = (EditText) findViewById(R.id.edtWlanIp);
        edtWlanMascara = (EditText) findViewById(R.id.edtWlanMascara);
        edtWlanPuerta = (EditText) findViewById(R.id.edtWlanPuerta);

        edtEthIp = (EditText) findViewById(R.id.edtWlanIp);
        edtEthMascara = (EditText) findViewById(R.id.edtWlanMascara);
        edtEthPuerta = (EditText) findViewById(R.id.edtWlanPuerta);

        lstWifi = (ListView)findViewById(R.id.lstWifi);
        buttonWifi = (Button) findViewById(R.id.buttonWifi);

        swtStatusEth0 = (Switch) findViewById(R.id.swtStatusEth0);
        swtStatusWlan0 = (Switch) findViewById(R.id.swtStatusWlan0);
        swtStatusPpp0 = (Switch) findViewById(R.id.swtStatusPpp0);

        txvIndicador = (TextView) findViewById(R.id.txvIndicador);


        if (connectivity.existSIM(ActivityComunicacion.this)){
            txvIndicador.setText("SIM INSERTADA");
        } else {
            txvIndicador.setText("SIM NO INSERTADA");
        }


        //this.adapter = new SimpleAdapter(ActivityComunicacion.this, arraylist, android.R.layout.simple_list_item_1, new String[] { ITEM_KEY }, new int[] { android.R.id.text1 });
        this.adapter = new SimpleAdapter(ActivityComunicacion.this, arraylist, R.layout.spinner_item, new String[] { ITEM_KEY }, new int[] { android.R.id.text1 });
        lstWifi.setAdapter(this.adapter);

        host = (TabHost)findViewById(R.id.tabHostComm);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("WIFI");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("GPRS");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("ETHERNET");
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

        //View w1 = host.getTabWidget().getChildTabViewAt(2);
        //w1.setVisibility(View.INVISIBLE);


        txvWifiState = (TextView) findViewById(R.id.txvWifiState);
        txvWifiConfigArea = (TextView) findViewById(R.id.txvWifiConfigArea);
        txvWifiConfigCapab = (TextView) findViewById(R.id.txvWifiConfigCapab);
        txvWifiConfigSSID = (TextView) findViewById(R.id.txvWifiConfigSSID);
        txvWifiConfigPass = (TextView) findViewById(R.id.txvWifiConfigPass);
        edtWifiConfigPass = (EditText) findViewById(R.id.edtWifiConfigPass);
        chkWifiConfigMostrarPass = (CheckBox) findViewById(R.id.chkWifiConfigMostrarPass);
        btnWifiConfigConectar = (Button) findViewById(R.id.btnWifiConfigConectar);
        btnWifiConfigCancelar = (Button) findViewById(R.id.btnWifiConfigCancelar);
        rbnWifiStatic = (RadioButton) findViewById(R.id.rbnWifiStatic);
        rbnWifiDynamic = (RadioButton) findViewById(R.id.rbnWifiDynamic);
        btnConfRed =  (Button) findViewById(R.id.btnConfRed);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);
        manageAreaWifiConfig(false);
        disableDataWifi();

        /* --- Inicialización de Parametros Generales --- */

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        registerReceiver(broadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


        /* ------------------------------ */

        /* --- Eventos --- */

        btnMasterComunicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unregisterReceiver(broadcastReceiver);
                ui.goToActivity(ActivityComunicacion.this, ActivityMenu.class , "","");
            }
        });

        buttonWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scanWifiToListView();
                    }
                });
            }
        });

        swtStatusWlan0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                arraylist.clear();
                adapter.notifyDataSetChanged();

                if (isChecked) {
                    wifi.setWifiEnabled(true);
                    Log.v(TAG,"WIFI TRUE");

                    scanWifiToListView();

                    Thread thWifiState = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edtWlanIp.setEnabled(true);
                                    edtWlanMascara.setEnabled(true);
                                    edtWlanPuerta.setEnabled(true);
                                }
                            });

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rbnWifiDynamic.setEnabled(true);
                                    rbnWifiStatic.setEnabled(true);
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




                                try {
                                    checkWifiTypeConnection();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    });

                    thWifiState.start();

                    Toast.makeText(getApplicationContext(),"Wifi Activado", Toast.LENGTH_SHORT).show();
                } else {
                    wifi.setWifiEnabled(false);
                    Log.v(TAG,"WIFI FALSE");

                    disableDataWifi();

                    Toast.makeText(getApplicationContext(),"Wifi Desactivado", Toast.LENGTH_SHORT).show();
                }


            }
        });

        swtStatusPpp0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (connectivity.existSIM(ActivityComunicacion.this)){
                        connectivity.setMobileDataState(ActivityComunicacion.this,true);
                    } else {
                        ui.showAlert(ActivityComunicacion.this,"warning","No hay SIM insertada.");
                    }

                } else {
                    if (connectivity.existSIM(ActivityComunicacion.this)){
                        connectivity.setMobileDataState(ActivityComunicacion.this,false);
                    } else {
                        ui.showAlert(ActivityComunicacion.this,"warning","No hay SIM insertada.");
                    }

                }
            }
        });

        lstWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    manageAreaWifiConfig(true);
                    txvWifiConfigSSID.setText(results.get(position).SSID);
                    txvWifiConfigCapab.setText(results.get(position).capabilities);
                    Log.v(TAG, "SSID - " + results.get(position).SSID);
                    Log.v(TAG, "BSSID - " + results.get(position).BSSID);
                    Log.v(TAG, "Caracteristicas - " + results.get(position).capabilities);
                    Log.v(TAG, "Nivel - " + results.get(position).level);
                    Log.v(TAG, "Frecuencia - " + results.get(position).frequency);
                    Log.v(TAG, "Tiempo - " + results.get(position).timestamp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnWifiConfigCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageAreaWifiConfig(false);
            }
        });

        btnWifiConfigConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datos = txvWifiConfigCapab.getText().toString();
                String ssid = txvWifiConfigSSID.getText().toString();
                String pass = edtWifiConfigPass.getText().toString();

                if (ssid.length()>0 && pass.length()>0) {
                    Boolean isWPA = false;
                    if (datos.contains("WEP")){
                        isWPA = false;
                    } else {
                        if (datos.contains("WPA")){
                            isWPA = true;
                        }
                    }
                    connectWifi(isWPA,ssid,pass);
                    manageAreaWifiConfig(false);
                } else {
                    Toast.makeText(getApplicationContext(),"Debe colocar un SSID y/o CONTRASEÑA válidos",Toast.LENGTH_SHORT);
                }
            }
        });

        btnConfRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip = edtWlanIp.getText().toString();
                String mask = edtWlanMascara.getText().toString();
                String gw = edtWlanPuerta.getText().toString();
                boolean dhcp = false;

                if (rbnWifiDynamic.isChecked()){
                    dhcp = true;
                }

                changeWifiConfiguration(dhcp, ip, convertIpToBytes(mask), "8.8.8.8",gw);

                String params1[] = {"su","-c","ifconfig","down"};
                String params2[] = {"su","-c","ifconfig","up"};

                Shell sh = new Shell();
                sh.exec(params1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sh.exec(params2);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                checkWifiTypeConnection();
                checkDataWifiConnection();

            }
        });

        rbnWifiStatic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edtWlanIp.setEnabled(true);
                    edtWlanMascara.setEnabled(true);
                    edtWlanPuerta.setEnabled(true);
                }
            }
        });

        rbnWifiDynamic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtWlanIp.setEnabled(false);
                    edtWlanMascara.setEnabled(false);
                    edtWlanPuerta.setEnabled(false);
                }
            }
        });

        inicializarSwitches();
        checkWifiConnection();
        checkWifiTypeConnection();
        checkDataWifiConnection();

        edtEthIp.setEnabled(false);
        edtEthMascara.setEnabled(false);
        edtEthPuerta.setEnabled(false);


        //Log.d(TAG, String.valueOf(connectivity.isConnectedMobile(this.getApplicationContext())));
        //Log.d(TAG, String.valueOf(connectivity.getMobileDataState(ActivityComunicacion.this)));
        //Log.d(TAG, connectivity.getNetworkClass(getApplicationContext()));
        //connectivity.setMobileDataState(ActivityComunicacion.this,true);

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "RECIBIENDO DATA ... ");
            scanWifiToListView();
        }
    };

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

    public int convertIpToBytes(String ip){
        int res = 0;
        return res;
    }

    public void scanWifiToListView(){

        results = wifi.getScanResults();
        size = results.size();
        Log.v(TAG, String.valueOf(results));
        arraylist.clear();
        wifi.startScan();

        if (wifi.isWifiEnabled() ) {
            try {
                Log.v(TAG, "---------------------------------------------------------------------------->");
                for (int i = 0; i < size; i++) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put(ITEM_KEY, results.get(i).SSID);
                    arraylist.add(item);
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.v(TAG,"EXCEPCION WIFI 1"+e.getMessage());
            }
        } else {
            try {
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.v(TAG, "EXCEPCION WIFI 2" + e.getMessage());
            }
        }
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

    public void checkWifiTypeConnection(){
        try {
            String tipoIp = "";
            String res = "";

            res = connectivity.getWifiConfiguration(ActivityComunicacion.this.getApplicationContext());
            Log.d(TAG,"GET WIFI CONFIGURATION: " + res);
            tipoIp = res.split("\n")[0].split(": ")[1];

            Log.d(TAG,tipoIp.trim() + "~");

            if (tipoIp.equalsIgnoreCase("static")){
                Log.d(TAG,"xxxxxxxx static xxxxxxxx");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rbnWifiStatic.setChecked(true);
                        rbnWifiDynamic.setChecked(false);
                        Log.d(TAG,"xxxxxxxx static xxxxxxxx");
                    }
                });
            } else {
                Log.d(TAG,"xxxxxxxx dynamic xxxxxxxx");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rbnWifiDynamic.setChecked(true);
                        rbnWifiStatic.setChecked(false);
                        Log.d(TAG,"xxxxxxxx dynamic xxxxxxxx");
                    }
                });
            }

            Log.d(TAG,"----------> "+ rbnWifiStatic.isChecked() + " - " + rbnWifiDynamic.isChecked());
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
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
            String params[] = {"su", "-c", "busybox", "ifconfig"};
            String data = sh.exec(params);
            //Log.v(TAG,"checkDataWifiConnection: " + data);

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
        rbnWifiDynamic.setChecked(false);
        rbnWifiStatic.setChecked(false);
        edtWlanIp.setText("");
        edtWlanMascara.setText("");
        edtWlanPuerta.setText("");

        rbnWifiDynamic.setEnabled(false);
        rbnWifiStatic.setEnabled(false);
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

    public void manageAreaWifiConfig(boolean visible){
        if (visible) {
            txvWifiConfigArea.setVisibility(View.VISIBLE);
            txvWifiConfigSSID.setVisibility(View.VISIBLE);
            txvWifiConfigCapab.setVisibility(View.VISIBLE);
            txvWifiConfigPass.setVisibility(View.VISIBLE);
            edtWifiConfigPass.setVisibility(View.VISIBLE);
            chkWifiConfigMostrarPass.setVisibility(View.VISIBLE);
            btnWifiConfigConectar.setVisibility(View.VISIBLE);
            btnWifiConfigCancelar.setVisibility(View.VISIBLE);
        } else {
            txvWifiConfigArea.setVisibility(View.INVISIBLE);
            txvWifiConfigSSID.setVisibility(View.INVISIBLE);
            txvWifiConfigCapab.setVisibility(View.INVISIBLE);
            txvWifiConfigPass.setVisibility(View.INVISIBLE);
            edtWifiConfigPass.setVisibility(View.INVISIBLE);
            chkWifiConfigMostrarPass.setVisibility(View.INVISIBLE);
            btnWifiConfigConectar.setVisibility(View.INVISIBLE);
            btnWifiConfigCancelar.setVisibility(View.INVISIBLE);
        }
    }

    public void connectWifi(boolean isWPA, String ssid, String pass){

        //create wifi connection
        String networkSSID = ssid;
        String networkPass = pass;

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes

        //For WPA network you need to add passphrase like this:

        if (isWPA) {
            conf.preSharedKey = "\""+ networkPass +"\"";
        }

        //Then, you need to add it to Android wifi manager settings:
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        //And finally, you might need to enable it, so Android conntects to it:
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }

    public void inicializarSwitches() {
        // ETH

        // WIFI
        if (wifi.isWifiEnabled()) {
            swtStatusWlan0.setChecked(true);
        } else {
            swtStatusWlan0.setChecked(false);
        }

        // PPP
    }

























    public void changeWifiConfiguration(boolean dhcp, String ip, int prefix, String dns1, String gateway) {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!wm.isWifiEnabled()) {
            // wifi is disabled
            return;
        }
        // get the current wifi configuration
        WifiConfiguration wifiConf = null;
        WifiInfo connectionInfo = wm.getConnectionInfo();
        List<WifiConfiguration> configuredNetworks = wm.getConfiguredNetworks();
        if(configuredNetworks != null) {
            for (WifiConfiguration conf : configuredNetworks){
                if (conf.networkId == connectionInfo.getNetworkId()){
                    wifiConf = conf;
                    break;
                }
            }
        }
        if(wifiConf == null) {
            // wifi is not connected
            return;
        }
        try {
            Class<?> ipAssignment = wifiConf.getClass().getMethod("getIpAssignment").invoke(wifiConf).getClass();
            Object staticConf = wifiConf.getClass().getMethod("getStaticIpConfiguration").invoke(wifiConf);
            if(dhcp) {
                Log.d("CONF WIFI","DHCP ESTABLISHED");
                wifiConf.getClass().getMethod("setIpAssignment", ipAssignment).invoke(wifiConf, Enum.valueOf((Class<Enum>) ipAssignment, "DHCP"));
                if(staticConf != null) {
                    staticConf.getClass().getMethod("clear").invoke(staticConf);
                }
            } else {
                Log.d("CONF WIFI","STATIC ESTABLISHED");
                wifiConf.getClass().getMethod("setIpAssignment", ipAssignment).invoke(wifiConf, Enum.valueOf((Class<Enum>) ipAssignment, "STATIC"));
                if(staticConf == null) {
                    Class<?> staticConfigClass = Class.forName("android.net.StaticIpConfiguration");
                    staticConf = staticConfigClass.newInstance();
                }
                // STATIC IP AND MASK PREFIX
                Constructor<?> laConstructor = LinkAddress.class.getConstructor(InetAddress.class, int.class);
                LinkAddress linkAddress = (LinkAddress) laConstructor.newInstance(
                        InetAddress.getByName(ip),
                        prefix);
                staticConf.getClass().getField("ipAddress").set(staticConf, linkAddress);
                // GATEWAY
                staticConf.getClass().getField("gateway").set(staticConf, InetAddress.getByName(gateway));
                // DNS
                List<InetAddress> dnsServers = (List<InetAddress>) staticConf.getClass().getField("dnsServers").get(staticConf);
                dnsServers.clear();
                dnsServers.add(InetAddress.getByName(dns1));
                dnsServers.add(InetAddress.getByName("8.8.8.8")); // Google DNS as DNS2 for safety
                // apply the new static configuration
                wifiConf.getClass().getMethod("setStaticIpConfiguration", staticConf.getClass()).invoke(wifiConf, staticConf);
            }
            // apply the configuration change
            boolean result = wm.updateNetwork(wifiConf) != -1; //apply the setting
            if(result) result = wm.saveConfiguration(); //Save it
            if(result) wm.reassociate(); // reconnect with the new static IP
        } catch(Exception e) {
            e.printStackTrace();
        }
    }









}

