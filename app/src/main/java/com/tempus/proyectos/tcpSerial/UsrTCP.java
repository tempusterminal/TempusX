package com.tempus.proyectos.tcpSerial;

import android.util.Log;

/**
 * Created by gurrutiam on 04/08/2017.
 */

public class UsrTCP {

    String TAG = "TS-UT";


    public String[] getParameters(String parameters){
        Log.v(TAG,"parameters " + parameters);
        // parameters -> a+ok+OK+OK=STATIC,192.168.0.77,255.255.255.0,192.168.0.2+OK
        // a+ok+OK+ERR=-1+OK=STATIC,192.168.0.79,255.255.255.0,192.168.0.2
        int start = -1;
        int end = -1;

        do{
            // Buscar la posicion de inicio de los parametros
            start = parameters.indexOf("=") + 1;
            // Eliminar lo anterior al inicio de los parametros
            // parameters = STATIC,192.168.0.77,255.255.255.0,192.168.0.2+OK
            parameters = parameters.substring(start);
        }while(parameters.contains("="));


        Log.v(TAG,"parameters " + parameters);

        do{
            // Buscar la posicion de fin de los parametros
            end = parameters.indexOf("A");
            if(end != -1){
                // Eliminar lo posterior al fin de los parametros
                // parameters = STATIC,192.168.0.77,255.255.255.0,192.168.0.2
                parameters = parameters.substring(0,end);
            }
        }while(parameters.contains("A"));

        do{
            // Buscar la posicion de fin de los parametros
            end = parameters.indexOf("+");
            if(end != -1){
                // Eliminar lo posterior al fin de los parametros
                // parameters = STATIC,192.168.0.77,255.255.255.0,192.168.0.2
                parameters = parameters.substring(0,end);
            }
        }while(parameters.contains("+"));

        Log.v(TAG,"parameters " + parameters);

        String[] parametersArray = parameters.split("\\,");

        return parametersArray;
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


}
