package com.tempus.proyectos.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ecernar on 02/11/2016.
 */

public class Utilities {

    public String getCRC16CCITT(String inputStr, int polynomial, int crc, boolean isHex) {
        int strLen = inputStr.length();
        int[] intArray;

        if (isHex) {
            if (strLen % 2 != 0) {
                inputStr = inputStr.substring(0, strLen - 1) + "0"
                        + inputStr.substring(strLen - 1, strLen);
                strLen++;
            }

            intArray = new int[strLen / 2];
            int ctr = 0;
            for (int n = 0; n < strLen; n += 2) {
                intArray[ctr] = Integer.valueOf(inputStr.substring(n, n + 2), 16);
                ctr++;
            }
        } else {
            intArray = new int[inputStr.getBytes().length];
            int ctr = 0;
            for (byte b : inputStr.getBytes()) {
                intArray[ctr] = b;
                ctr++;
            }
        }

        // main code for computing the 16-bit CRC-CCITT
        for (int b : intArray) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }

        crc &= 0xFFFF;
        return Integer.toHexString(crc).toUpperCase();
    }

    public String hexToBin(String s) {
        String preBin = new BigInteger(s, 16).toString(2);
        Integer length = preBin.length();
        if (length < 8) {
            for (int i = 0; i < 8 - length; i++) {
                preBin = "0" + preBin;
            }
        }
        return preBin;
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public String byteArrayToHexString(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    public String convertHexToString(String hex){
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for( int i=0; i<hex.length()-1; i+=2 ){
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char)decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

    public String[] convertHashMapToArray(HashMap<Integer,String> mapa, int tam){
        String s[] = new String[tam];
        for(int i=0; i<tam; i++){
            s[i] = mapa.get(i);
        }
        return s;
    }

    public void convertHexToASCII(String hex){
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        Log.v("TEMPUS: ", String.valueOf(output));
    }

    public int convertHexToDecimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }

    public double convertHexToDouble(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        double val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }

    public static int[] byteArrayToIntArray(byte[] barray) {
        int[] iarray = new int[barray.length];
        int i = 0;
        for (byte b : barray)
            iarray[i++] = b & 0xff;
        return iarray;
    }



    public void sleep(int milisegundos){
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean inRangeTime(String hora1, String hora2){

        Boolean resultado = false;

        try {
            String[] h1 = hora1.split(":");
            String[] h2 = hora2.split(":");

            GregorianCalendar cal = new GregorianCalendar();

            cal.set(GregorianCalendar.HOUR, Integer.parseInt(h1[0]));
            cal.set(GregorianCalendar.MINUTE, Integer.parseInt(h1[1]));
            cal.set(GregorianCalendar.SECOND, Integer.parseInt(h1[2]));

            Date horaA = cal.getTime();

            cal.set(GregorianCalendar.HOUR, Integer.parseInt(h2[0]));
            cal.set(GregorianCalendar.MINUTE, Integer.parseInt(h2[1]));
            cal.set(GregorianCalendar.SECOND, Integer.parseInt(h2[2]));

            Date horaB = cal.getTime();

            Date ahora = new Date();

            if (ahora.compareTo(horaA) >= 0 && ahora.compareTo(horaB) <= 0){
                resultado = true;
            }
        } catch(Exception e) {
            Log.d("Error",e.getMessage());
        }

        return resultado;
    }


    public boolean isPortOpen(final String ip, final int port, final int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        } catch(ConnectException ce){
            ce.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isOnline(Activity activity) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)activity.getSystemService(activity.getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
    }

    public void getAllAppsInstalled(Activity activity){
        final PackageManager pm = activity.getPackageManager();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("APK", "Installed package :" + packageInfo.packageName);
            Log.d("APK", "Source dir : " + packageInfo.sourceDir);
            Log.d("APK", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
    }


}

















