package com.tempus.proyectos.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tempus.proyectos.data.ConexionServidor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by ecernar on 09/01/2017.
 */

public class Connectivity {
    /**
     * Get the network info
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && Connectivity.isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

    public String getMobileTypeConnection(int type) {
        switch(type){
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT"; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA"; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE"; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO 0"; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO A"; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS"; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA"; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA"; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA"; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS"; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
            case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                return "EHRPD"; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                return "EVDO B"; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                return "HSPAP"; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                return "IDEN"; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                return "LTE"; // ~ 10+ Mbps
            // Unknown
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "DESCONOCIDO"; // ~ 10+ Mbps
            default:
                return "UNKNOWN";
        }
    }

    public void setMobileDataState(Activity activity, boolean mobileDataEnabled){
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod){
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        }
        catch (Exception ex)
        {
            Log.e("Error", "Error setting mobile data state", ex);
        }
    }

    public boolean getMobileDataState(Activity activity){
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod)
            {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);

                return mobileDataEnabled;
            }
        }
        catch (Exception ex)
        {
            Log.e("Error", "Error getting mobile data state", ex);
        }

        return false;
    }

    public String getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }

    public boolean existSIM(Activity activity){
        Boolean resultado = false;
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT){
            resultado = true;
        }
        return resultado;
    }

    public String getWifiConfiguration(Context context){

        String myenumvalue="";
        WifiConfiguration wifiConf = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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
                Object ipConfiguration = wifiConf.getClass().getMethod("getIpConfiguration").invoke(wifiConf);
                //Object ipAssignment = getEnumValue("android.net.IpConfiguration$IpAssignment", "STATIC");
                ;
                Log.d("RESULTADO XD", ipConfiguration.toString());
                myenumvalue = ipConfiguration.toString();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return myenumvalue;
    }

    public boolean isURLReachable(Context context, String host, String tipo) {

        if (tipo.equalsIgnoreCase("ip")){
            try {
                if (host.isEmpty() || host == "" || host == "0.0.0.0"){
                    return false;
                } else {
                    boolean reachable1 = InetAddress.getByName(host).isReachable(10000); // 10 seg Timeout
                    return reachable1;
                }

            } catch (IOException e) {
                Log.wtf("isURLReachable","IP - " + e.getMessage());
            }
        } else {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL(host);   // Change to "http://google.com" for www  test.
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(10 * 1000);          // 10 s.
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                        Log.wtf("Connection", "Success !");
                        return true;
                    } else {
                        return false;
                    }
                } catch (MalformedURLException e1) {
                    Log.wtf("isURLReachable","IP - MalformedURLException" + e1.getMessage());
                    return false;
                } catch (IOException e) {
                    Log.wtf("isURLReachable","IP - IOException" + e.getMessage());
                    return false;
                }
            }
        }

        return false;
    }


    public boolean isValidConnection(){
        boolean res;
        ConexionServidor cs = new ConexionServidor();
        res = cs.testConexionServidor();
        return res;
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

    public String getMacAddress(String cadena, String networkInterface){
        String resultado = "";
        String salida = cadena;
        try {
            String salidaArray[] = salida.split("\n");
            String tmp = "";
            for (int i = 0 ; i < salidaArray.length; i++) {
                if (salidaArray[i].contains(networkInterface)){
                    tmp = salidaArray[i];
                    Log.v("TEMPUS: ","getMacAddress SALIDA > "+tmp);
                    break;
                }
            }
            resultado = tmp.trim().toLowerCase().split("hwaddr")[1].trim().toUpperCase();
            Log.v("TEMPUS: ","getMacAddress ..."+resultado+"...");
        } catch (Exception e) {
            Log.v("TEMPUS: ","getMacAddress > "+e.getMessage());
        }

        return resultado;
    }

    public void turnGPSOn(Activity activity) {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        activity.sendBroadcast(intent);
    }

    public void turnGPSOff(Activity activity) {
        String provider = Settings.Secure.getString(activity.getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            activity.getApplicationContext().sendBroadcast(poke);
        }
    }

    public String[] TelephoneManager(Activity activity){

        String data[]={"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""};

        TelephonyManager mTelephonyMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        try { data[0] = mTelephonyMgr.getDeviceId(); } catch (Exception e) {};
        try { data[1] = mTelephonyMgr.getLine1Number(); } catch (Exception e) {};
        try { data[2] = mTelephonyMgr.getCellLocation().toString(); } catch (Exception e) {};
        try { data[3] = String.valueOf(mTelephonyMgr.getDataState()); } catch (Exception e) {};
        try { data[4] = mTelephonyMgr.getDeviceSoftwareVersion(); } catch (Exception e) {};
        try { data[5] = mTelephonyMgr.getMmsUAProfUrl(); } catch (Exception e) {};
        try { data[6] = mTelephonyMgr.getMmsUserAgent(); } catch (Exception e) {};
        try { data[7] = mTelephonyMgr.getNetworkCountryIso(); } catch (Exception e) {};
        try { data[8] = mTelephonyMgr.getNetworkOperator(); } catch (Exception e) {};
        try { data[9] = mTelephonyMgr.getNetworkOperatorName(); } catch (Exception e) {};
        try { data[10] = getMobileTypeConnection(mTelephonyMgr.getNetworkType()); } catch (Exception e) {};
        try { data[11] = mTelephonyMgr.getSimCountryIso(); } catch (Exception e) {};
        try { data[12] = mTelephonyMgr.getSimOperator(); } catch (Exception e) {};
        try { data[13] = mTelephonyMgr.getSimOperatorName(); } catch (Exception e) {};
        try { data[14] = String.valueOf(mTelephonyMgr.getSimState()); } catch (Exception e) {};
        try { data[15] = mTelephonyMgr.getSimSerialNumber(); } catch (Exception e) {};

        String output = "---->\n" +
                "deviceId: " + data[0] + "\n" +
                "line1Number: " + data[1] + "\n" +
                "cellLocation: " + data[2] + "\n" +
                "dataState: " + data[3] + "\n" +
                "deviceSoftwareVersion: " + data[4] + "\n" +
                "mmsUAProfUrl: " + data[5] + "\n" +
                "mmsUserAgent: " + data[6] + "\n" +
                "networkCountryIso: " + data[7] + "\n" +
                "networkOperator: " + data[8] + "\n" +
                "networkOperatorName: " + data[9] + "\n" +
                "networkType (String): " + data[10] + "\n" +
                "simCountryIso: " + data[11] + "\n" +
                "simOperator: " + data[12] + "\n" +
                "simOperatorName: " + data[13] + "\n" +
                "simState: " + data[14] + "\n" +
                "simSerialNumber: " + data[15];

        Log.d("GPRS DATA", output);

        return data;
    }


}
