package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
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
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

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

public class ActivityComunicacion extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Connectivity connectivity;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    String s_dns1 ;
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

    TextView txvWifiState;
    TextView txvWifiConfigArea;
    TextView txvWifiConfigSSID;
    TextView txvWifiConfigCapab;
    TextView txvWifiConfigPass;
    EditText edtWifiConfigPass;
    CheckBox chkWifiConfigMostrarPass;
    Button btnWifiConfigCancelar;
    Button btnWifiConfigConectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        connectivity = new Connectivity();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Comunicacion";

        /* --- Inicialización de Variables Locales --- */

        interfaceEth0Enabled = false;
        interfaceWlan0Enabled = false;
        interfacePpp00Enabled = false;

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterComunicacion = (ImageView) findViewById(R.id.btnMasterComunicacion);

        edtWlanIp = (EditText) findViewById(R.id.edtWlanIp);
        edtWlanMascara = (EditText) findViewById(R.id.edtWlanMascara);
        edtWlanPuerta = (EditText) findViewById(R.id.edtWlanPuerta);

        lstWifi = (ListView)findViewById(R.id.lstWifi);
        buttonWifi = (Button) findViewById(R.id.buttonWifi);

        swtStatusEth0 = (Switch) findViewById(R.id.swtStatusEth0);
        swtStatusWlan0 = (Switch) findViewById(R.id.swtStatusWlan0);
        swtStatusPpp0 = (Switch) findViewById(R.id.swtStatusPpp0);

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
        spec.setIndicator("ETHERNET");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("GPRS");
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

        View w3 = host.getTabWidget().getChildTabViewAt(2);
        w3.setVisibility(View.INVISIBLE);

        View w1 = host.getTabWidget().getChildTabViewAt(1);
        w1.setVisibility(View.INVISIBLE);


        txvWifiState = (TextView) findViewById(R.id.txvWifiState);
        txvWifiConfigArea = (TextView) findViewById(R.id.txvWifiConfigArea);
        txvWifiConfigCapab = (TextView) findViewById(R.id.txvWifiConfigCapab);
        txvWifiConfigSSID = (TextView) findViewById(R.id.txvWifiConfigSSID);
        txvWifiConfigPass = (TextView) findViewById(R.id.txvWifiConfigPass);
        edtWifiConfigPass = (EditText) findViewById(R.id.edtWifiConfigPass);
        chkWifiConfigMostrarPass = (CheckBox) findViewById(R.id.chkWifiConfigMostrarPass);
        btnWifiConfigConectar = (Button) findViewById(R.id.btnWifiConfigConectar);
        btnWifiConfigCancelar = (Button) findViewById(R.id.btnWifiConfigCancelar);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);
        //NetworkInfo();
        manageAreaWifiConfig(false);

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
                        if (wifi.isWifiEnabled()){
                            arraylist.clear();
                            wifi.startScan();

                            try {
                                for (int i = 0; i < size; i++) {
                                    HashMap<String, String> item = new HashMap<String, String>();
                                    item.put(ITEM_KEY, results.get(i).SSID);
                                    arraylist.add(item);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                Log.v("TEMPUS: ","EXCEPCION REGISTERRECEIVER WIFI"+e.getMessage());
                            }
                        }
                    }
                });
            }
        });

        swtStatusWlan0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                arraylist.clear();
                adapter.notifyDataSetChanged();

                if (isChecked){
                    wifi.setWifiEnabled(true);
                    Log.v("TEMPUS: ","WIFI TRUE");
                } else {
                    wifi.setWifiEnabled(false);
                    Log.v("TEMPUS: ","WIFI FALSE");
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
                    Log.v("TEMPUS: ", "SSID - " + results.get(position).SSID);
                    Log.v("TEMPUS: ", "BSSID - " + results.get(position).BSSID);
                    Log.v("TEMPUS: ", "Caracteristicas - " + results.get(position).capabilities);
                    Log.v("TEMPUS: ", "Nivel - " + results.get(position).level);
                    Log.v("TEMPUS: ", "Frecuencia - " + results.get(position).frequency);
                    Log.v("TEMPUS: ", "Tiempo - " + results.get(position).timestamp);
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
                    Toast.makeText(getApplicationContext(),"Debe colocar un SSIS y/o CONTRASEÑA válidos",Toast.LENGTH_SHORT);
                }



            }
        });

        inicializarSwitches();
        startWifi();

        Log.d("Conn", String.valueOf(connectivity.isConnectedMobile(this.getApplicationContext())));
        Log.d("Conn", String.valueOf(connectivity.getMobileDataState(ActivityComunicacion.this)));
        Log.d("Conn", connectivity.getNetworkClass(getApplicationContext()));
        connectivity.setMobileDataState(ActivityComunicacion.this,true);

    }





    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    mWifi.getState();

                    if (mWifi.isConnected()) {
                        txvWifiState.setText("SSID: " + wifi.getConnectionInfo().getSSID());
                        Log.v("TEMPUS: ", "ESTADO DE WIFI CONECTADO");
                    } else {
                        txvWifiState.setText("SSID: -");
                        Log.v("TEMPUS: ", "ESTADO DE WIFI DESCONECTADO");
                    }

                    results = wifi.getScanResults();
                    size = results.size();
                    Log.v("TEMPUS: ", String.valueOf(results));
                    Log.v("TEMPUS: ", "-------------------->" + String.valueOf(wifi.isWifiEnabled()));
                    arraylist.clear();
                    wifi.startScan();

                    if (wifi.isWifiEnabled()) {
                        try {
                            getInforWIFI();
                            for (int i = 0; i < size; i++) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put(ITEM_KEY, results.get(i).SSID);
                                arraylist.add(item);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Log.v("TEMPUS: ","EXCEPCION WIFI 1"+e.getMessage());
                        }
                    } else {
                        try {
                            adapter.notifyDataSetChanged();
                        } catch(Exception e) {
                            Log.v("TEMPUS: ","EXCEPCION WIFI 2"+e.getMessage());
                        }

                    }
                }
            });
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




    public void NetworkInfo(){
        try {
            HashMap<String, Boolean> Interfaces = new HashMap<String, Boolean>();
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {

                Interfaces.put(netint.getName(),netint.isUp());

                Log.v("TEMPUS: ", String.valueOf(netint.getName()) + "~" + String.valueOf(netint.isUp()));


                switch (netint.getName()) {

                    case "wlan0":

                        if (netint.isUp()){
                            interfaceWlan0Enabled = true;

                            Wlan0Name = String.valueOf(netint.getDisplayName());
                            Wlan0Interface = String.valueOf(netint.getInterfaceAddresses());
                            interfaceDataWlan0 = netint.getInterfaceAddresses();
                        }

                        break;

                    case "eth0":

                        if (netint.isUp()){
                            interfaceEth0Enabled = true;

                            Eth0Name = String.valueOf(netint.getDisplayName());
                            Eth0Interface = String.valueOf(netint.getInterfaceAddresses());
                            interfaceDataEth0 = netint.getInterfaceAddresses();
                        }

                        break;
                    default:
                        break;
                }

            }

            Log.v("TEMPUS: ", String.valueOf(Interfaces));


            if (interfaceEth0Enabled) {
                swtStatusEth0.setChecked(true);
            } else {
                swtStatusEth0.setChecked(false);
            }

            if (interfaceWlan0Enabled) {

                swtStatusWlan0.setChecked(true);
/*
                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                String dhcpInfo = wm.getDhcpInfo().toString();

                String []datos = dhcpInfo.split(" ");
                String ip = datos[1];
                String puerta = datos[3];
                String mascara = datos[5];

                edtWlanIp.setText(ip);
                edtWlanMascara.setText(mascara);
                edtWlanPuerta.setText(puerta);
*/

            } else {
                swtStatusWlan0.setChecked(false);
            }

            if (interfacePpp00Enabled) {

            } else {

            }







/*
            if (interfaceEth0Enabled){
                Log.v("TEMPUS: ", "--> E");
                edtIp.setText(String.valueOf(interfaceDataEth0.get(1).getAddress()));
                edtMascara.setText("");
                edtPuerta.setText("");

            } else {
                if (interfaceWlan0Enabled) {
                    Log.v("TEMPUS: ", "--> W");
                    edtIp.setText(String.valueOf(interfaceDataWlan0.get(1).getAddress()));
                    edtMascara.setText("");
                    edtPuerta.setText("");
                } else {
                    Log.v("TEMPUS: ", "--> NO DISPONIBLE");
                    edtIp.setText("NO DISPONIBLE");
                    edtMascara.setText("NO DISPONIBLE");
                    edtPuerta.setText("NO DISPONIBLE");
                }
            }

*/


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void ConnectionWifiInfo(){
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);

        String dhcpInfo = wm.getDhcpInfo().toString();

        Log.v("TEMPUS: ",dhcpInfo);

        //editText3.setText(dhcpInfo);

        String []datos = dhcpInfo.split(" ");
        String ip = datos[1];
        String puerta = datos[3];
        String mascara = datos[5];

        edtWlanIp.setText(ip);
        edtWlanMascara.setText(mascara);
        edtWlanPuerta.setText(puerta);
    }

    public void ConnectionEthernetInfo(){
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);

        String dhcpInfo = wm.getDhcpInfo().toString();

        Log.v("TEMPUS: ",dhcpInfo);

        String []datos = dhcpInfo.split(" ");
        String ip = datos[1];
        String puerta = datos[3];
        String mascara = datos[5];

        edtWlanIp.setText(ip);
        edtWlanMascara.setText(mascara);
        edtWlanPuerta.setText(puerta);
    }

    public String displayInterfaceInformation(NetworkInterface netint) throws SocketException {

        String info = "";

        info = netint.getDisplayName() + " --- " + netint.getName() + " ---: ";

        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            info = info + inetAddress;
            Log.v("TEMPUS: ",info);
        }
        return info;
    }

    public void setData(){

        try {
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "192.168.0.240");
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_DNS1, "192.168.0.2");
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_DNS2, "192.168.0.3");
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_GATEWAY, "192.168.0.2");
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_NETMASK, "255.255.255.0");
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, "1");


            getSystemService(ACCESSIBILITY_SERVICE);

        } catch(Exception e) {
            e.printStackTrace();
        }

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

    public void getInforWIFI() {
        DhcpInfo d = wifi.getDhcpInfo();
        //s_dns1="DNS 1: "+String.valueOf(Formatter.formatIpAddress(d.dns1));
        //s_dns2="DNS 2: "+String.valueOf(Formatter.formatIpAddress(d.dns2));
        //s_gateway="Default Gateway: "+String.valueOf(Formatter.formatIpAddress(d.gateway));
        //s_ipAddress="IP Address: "+String.valueOf(Formatter.formatIpAddress(d.ipAddress));
        //s_netmask="Subnet Mask: "+String.valueOf(Formatter.formatIpAddress(d.netmask));
        //s_serverAddress="Server IP: "+String.valueOf(Formatter.formatIpAddress(d.serverAddress));
        //Log.v("TEMPUS: ","Network Info\n"+s_dns1+"\n"+s_dns2+"\n"+s_gateway+"\n"+s_ipAddress+"\n"+s_leaseDuration+"\n"+s_netmask+"\n"+s_serverAddress);

        try {
            edtWlanIp.setText(String.valueOf(Formatter.formatIpAddress(d.ipAddress)));
            edtWlanMascara.setText(String.valueOf(Formatter.formatIpAddress(d.netmask)));
            edtWlanPuerta.setText(String.valueOf(Formatter.formatIpAddress(d.gateway)));
        } catch (Exception e) {
            Log.v("TEMPUS: ",e.toString());
        }

    }

    public void getInforWIFI2() {
        WifiInfo connectionInfo = wifi.getConnectionInfo();
        String strWifiInfo =
                "SSID: " + connectionInfo.getSSID() + "\n" +
                        "BSSID: " + connectionInfo.getBSSID() + "\n" +
                        "IP Address: " + String.format("%d.%d.%d.%d",
                        (connectionInfo.getIpAddress() & 0xff),
                        (connectionInfo.getIpAddress() >> 8 & 0xff),
                        (connectionInfo.getIpAddress() >> 16 & 0xff),
                        (connectionInfo.getIpAddress() >> 24 & 0xff)) + "\n" +
                        "MAC Address: " + connectionInfo.getMacAddress() + "\n" +
                        "LinkSpeed: " + connectionInfo.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS + "\n" +
                        "Rssi: " + connectionInfo.getRssi() + "dBm" + "\n";
        Log.v("Tempus: ",strWifiInfo);

    }

    public void startWifi(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mWifi.getState();

        if (mWifi.isConnected()) {
            txvWifiState.setText("SSID: " + wifi.getConnectionInfo().getSSID());
            Log.v("TEMPUS: ", "ESTADO DE WIFI CONECTADO");
        } else {
            txvWifiState.setText("SSID: -");
            Log.v("TEMPUS: ", "ESTADO DE WIFI DESCONECTADO");
        }

        results = wifi.getScanResults();
        size = results.size();
        Log.v("TEMPUS: ", String.valueOf(results));
        Log.v("TEMPUS: ", "-------------------->" + String.valueOf(wifi.isWifiEnabled()));
        arraylist.clear();
        wifi.startScan();

        if (wifi.isWifiEnabled()) {
            try {
                getInforWIFI();
                for (int i = 0; i < size; i++) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put(ITEM_KEY, results.get(i).SSID);
                    arraylist.add(item);
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.v("TEMPUS: ","EXCEPCION WIFI 1"+e.getMessage());
            }
        } else {
            try {
                adapter.notifyDataSetChanged();
            } catch(Exception e) {
                Log.v("TEMPUS: ","EXCEPCION WIFI 2"+e.getMessage());
            }

        }
    }

    public void inicializarSwitches() {
        // ETH

        // WIFI
        if (wifi.isWifiEnabled()) {
            swtStatusWlan0.setChecked(true);
            getInforWIFI();
        } else {
            swtStatusWlan0.setChecked(false);
        }

        // PPP
    }

    /*
    public String getIpAssignment(){

        String myenumvalue="";
        WifiConfiguration wifiConf = null;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration conf : configuredNetworks){
            if (conf.networkId == connectionInfo.getNetworkId()){
                wifiConf = conf;
                break;
            }
        }
        if(wifiConf != null){

            try {
                Object Enumer = getDeclaredField(wifiConf, "ipAssignment");
                myenumvalue = Enumer.toString();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }

        return myenumvalue;
    }
    */

}
