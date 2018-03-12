package com.tempus.proyectos.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ecernar on 02/11/2016.
 */

public class Utilities {

    private String TAG = "UT-UT";

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

    public String decimalToHex(int num) {
        if(num > 0 && num <= 9){
            return "0" + String.valueOf(num);
        }else if(num > 9){
            // For storing remainder
            int rem;

            // For storing result
            String str2="";

            // Digits in hexadecimal number system
            char hex[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

            while(num>0)
            {
                rem=num%16;
                str2=hex[rem]+str2;
                num=num/16;
            }

            if(str2.length() == 1){
                str2 = "0" + str2;
            }

            return str2;
        }else{
            return "30";
        }

    }

    public String asciiToHex(String asciiValue){
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int) chars[i]));
            //Log.v(TAG,"asciiToHex " + asciiValue + ">" + hex.toString());
        }
        return hex.toString();
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


    public String getChecksum(String hex, int l){
        hex = hex.replace(" ","");
        String sumHex = "";
        int sum = 0;
        if(hex.length() % 2 == 0){
            while(hex.length() > 0){
                if(hex.length()>2){
                    sum += Integer.parseInt(hex.substring(0,2),16);
                    hex = hex.substring(2);
                }else if(hex.length() == 2){
                    sum += Integer.parseInt(hex,16);
                    hex = "";
                }
            }
        }else{
            sum = 0;
        }

        sumHex = Integer.toHexString(sum);

        if(sumHex.length() == l){
            return sumHex;
        }else if(sumHex.length() > l){
            return sumHex.substring(sumHex.length() - l);
        }else if(sumHex.length() < l){
            while(!(sumHex.length() == l)){
                sumHex = "0" + sumHex;
            }
            return sumHex;
        }

        return "";

    }

    public String invertHex(String hex, int part){
        int l = hex.length();
        ArrayList<String> stringArrayList = new ArrayList<String>();
        String output = "";
        try{
            for(int i = 0; i < l/part; i++){
                stringArrayList.add(hex.substring(0,part));
                if(hex.length() > part){
                    hex = hex.substring(part);
                }
            }

            for(int i = 1; i <= stringArrayList.size(); i++){
                output += stringArrayList.get(stringArrayList.size() - i);
            }
        }catch (Exception e){
            Log.e(TAG,"invertHex " + e.getMessage());
        }

        return output;
    }

    // FIXME: 08/01/2018  optimizar cuando el exa sea de 3 de 2 de 1 de 0 etc cifras o mas de 4
    public String getSizeData(String data) {
        int temp = data.length() / 2;
        String len = decimalToHex(temp);
        if (len.length() == 2)
            len = "00" + len;
        else
            Log.e(TAG, "getSizeData: tamaños diferentes de 2 digitos no validados ", new Exception(" len value is : " + len));
        return len;
    }

    public long getApkNameToNumber(String apkName){
        String apkNameNumber = "";
        // Verificar que cumple con la medida
        if(apkName.length() == 14){
            apkNameNumber = apkName.substring(4,8) + apkName.substring(2,4) + apkName.substring(0,2) + apkName.substring(8);
            // Verificar que cumple con la medida
            if(apkNameNumber.length() == 14){
                try{
                    return Long.valueOf(apkNameNumber);
                }catch (Exception e){
                    return 0;
                }
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }


    public String getFilenameInDirectory(String filename, String extension, String directory){
        // Buscar un archivo con extension (opcional) de un directorio
        // En caso que la extensión este vacia o sea nula, se buscará el nombre del archivo y se devolverá con la extensión que tenga
        // En caso que la extensión tenga un valor, se buscará el nombre del archivo junto con la extensión
        // En caso de no encontrar el nombre del archivo y extension (opcional) se devolvera vacio
        try{
            Process process = Runtime.getRuntime().exec("ls " + directory + " -l");

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = null;
            while (true){
                line = bufferedReader.readLine();
                if(line != null){
                    //Log.v(TAG,"" + line);
                    // Se verifica si la linea enlistada contiene el nombre del archivo
                    if(line.contains(filename)){
                        //Log.v(TAG,">>>" + line.substring(line.indexOf(filename),line.indexOf(".")) + "<<<");
                        // En caso que la linea enlistada contenga el nombre del archivo
                        // Se verifica que el nombre del archivo encontrado sea igual al nombre del archivo de la variable filename
                        // por ejemplo: Si se busca el archivo 'config' se enlistará todos los archivos que contengan esos caracteres 'config1', 'config', 'configuracion', etc
                        // Pero se busca determinar cual es el nombre de archivo igual a la variable filename
                        if(line.substring(line.indexOf(filename),line.indexOf(".")).equalsIgnoreCase(filename)){
                            //Log.v(TAG,">>>>>>" + line.substring(line.indexOf(filename),line.indexOf(".")) + "<<<<<<");
                            //Log.v(TAG,">>>>>>" + line.substring(line.indexOf(filename)) + "<<<<<<");
                            // En caso se encuentre el archivo buscado
                            // Se verifica si se buscará con extensión
                            // Si no se busca la extensión se devolverá el archivo con la extensión que tenga
                            // por ejemplo: Si se busca el archivo 'config' sin extensión, se devolverá el archivo que cumpla con llamarse config 'config.sql' o 'config.txt'
                            // Si se busca con extensión se de volverá solo si tiene la misma extensión de la variable extension
                            // por ejemplo: Si se busca el archivo 'config' con extensión 'sql', se devolverá el archivo que cumpla con llamarse config y que tenga extensión sql 'config.sql', sino se devuelve vacio
                            if(extension == null || extension.isEmpty()){
                                return line.substring(line.indexOf(filename));
                            }else{
                                if(line.substring(line.indexOf(filename)).contains("." + extension)){
                                    return line.substring(line.indexOf(filename));
                                }else{
                                    return "";
                                }
                            }
                        }
                    }
                }else{
                    break;
                }
            }
            process.waitFor();
        }catch (Exception e){
            Log.e(TAG,"getFilenameInDirectory " + e.getMessage());
        }
        return "";
    }




    public boolean isRooted() {
        boolean rooted = false;
        Process process;
        try {
            process = Runtime.getRuntime().exec("su");
            process.getOutputStream().close();
            process.getInputStream().close();
            int close = process.waitFor();
            if (0 == close) {
                rooted = true;
            }
        } catch (IOException e) {
            Log.e(TAG, "isRooted: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Log.e(TAG, "isRooted: " + e.getMessage(), e);
        }
        return rooted;
    }




}

















