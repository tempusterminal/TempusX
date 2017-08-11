package com.tempus.proyectos.bluetoothSerial;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.tempus.proyectos.data.model.Llamadas;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.model.PersonalTipolectoraBiometria;
import com.tempus.proyectos.data.process.ProcessSync;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityComunicacion;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gurrutiam on 10/05/2017.
 */

public class MainEthernet {
    String TAG = "BTS-MAET";

    private int lenghtrawBytes = -1;
    byte[] rawBytes = new byte[10240];
    private String hexString = "";
    private String hexStrings = "";
    private String jsonItems = "";
    private JSONArray jsonArray = new JSONArray();
    private JSONObject jsonObject = new JSONObject();
    private int start = -1;
    private int end = -1;
    private ArrayList<ArrayList<String>> keyvalueBySend = new ArrayList<ArrayList<String>>();
    private static boolean hearing;
    private static int syncData;
    private static int typeJson;
    private static JSONObject jsonAuthenticate;
    private ArrayList<ArrayList<String>> aavaluesiu = new ArrayList<ArrayList<String>>();
    private ArrayList<String> avaluesiu = new ArrayList<String>();
    private List<Llamadas> llamadasList = new ArrayList<Llamadas>();
    private int illamadasList;
    Utilities util = new Utilities();
    ProcessSync processSync = new ProcessSync();
    private int loopfn;
    private int ilfn;
    private int approximationToFix = 0;
    private int limitToFix = 30;
    public static boolean atCommandMode = true;
    public static boolean enableSetEthernet = false;

    private int lhs = 0;

    public static String hexStringcfg = "";


    private QueriesMarcaciones queriesMarcaciones = new QueriesMarcaciones(ActivityPrincipal.context);
    private QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(ActivityPrincipal.context);

    EthernetReading ethernetReading;
    EthernetExecuting ethernetExecuting;
    EthernetFixing ethernetFixing;

    public void writeData(OutputStream out, String data) {
        Log.v(TAG, "Write Activado");
        try {
            byte[] a = data.getBytes();
            out.write(a);
            Log.v(TAG, "Write Finalizado");
        } catch (Exception e) {
            Log.e(TAG,"ETHERNET BT ERROR: " + e.getMessage());
        }
    }


    public String prepareHexString(Bluetooth btSocketEthernet){

        try{
            Log.v(TAG,"Esperando btSocketEthernet.getInputStream().read(rawBytes)");
            lenghtrawBytes = btSocketEthernet.getInputStream().read(rawBytes);
            Log.v(TAG,"Recibiendo btSocketEthernet.getInputStream().read(rawBytes)");
            //Log.v(TAG,"lenght -> " + lenghtrawBytes );
            String hs = "";
            hs = new String(util.hexStringToByteArray(util.byteArrayToHexString(rawBytes).substring(0,lenghtrawBytes*2)), StandardCharsets.UTF_8);


            if(atCommandMode){

                //Log.v(TAG,"LLEGO ASCCI (config) (" + hs.length() + ") -> " + hs );
                approximationToFix = 0;
                hexStringcfg += hs;
                //Log.v(TAG,"LLEGO ASCCI (config) (" + hexStringcfg.length() + ") -> " + hexStringcfg);
                hexStringcfg = hexStringcfg.replaceAll("\n","");
                Log.v(TAG,"LLEGO ASCCI (config) (" + hexStringcfg.length() + ") -> " + hexStringcfg);
                //String vHexStringcfg = findHexString(hexStringcfg,"+OK=","+OK");
                //Log.v(TAG,"findHexString: " + vHexStringcfg);

            }else{
                lhs += hs.length();
                Log.v(TAG,"LLEGO ASCCI (" + hs.length() + "=>" + lhs + ") -> " + hs );
                approximationToFix = 0;
                hexString += hs;
                //Log.v(TAG,"LLEGO ASCCI+ -> " + hexString );
                //Log.v(TAG,"hexString.length() -> " + hexString.length() );
                //validateHexString(hexString);

                //Log.v(TAG,"------------------------- 1");
                String vHexString = findHexString(hexString,"{","}");
                if(vHexString.length() == 0){
                    vHexString = findHexString(hexString,"[","]");
                }
                //Log.v(TAG,"------------------------- 2");

                if(vHexString.length()>0){
                    //Log.v(TAG,"------------------------- 3");
                    Log.v(TAG,"findHexString: " + vHexString);
                    vHexString = validateJson(vHexString);
                    //Log.v(TAG,"------------------------- 4");
                    Log.v(TAG,"validateJson: " + vHexString);

                    if(vHexString.length()>0){
                        //Log.v(TAG,"------------------------- 5");

                        if(syncData == 1){
                            if(keyvalueJsonObject(vHexString,"CADENA",0).length() > 30){
                                Log.v(TAG,"jsonObject: " + keyvalueJsonObject(vHexString,"CADENA",0).substring(0,20) + " ... " + keyvalueJsonObject(vHexString,"CADENA",0).substring(keyvalueJsonObject(vHexString,"CADENA",0).length()-20,keyvalueJsonObject(vHexString,"CADENA",0).length()));
                            }else{
                                Log.v(TAG,"jsonObject: " + keyvalueJsonObject(vHexString,"CADENA",0));
                            }
                            // Concatenar los value con key CADENA enviadas en las llamada
                            jsonItems += keyvalueJsonObject(vHexString,"CADENA",0);
                            Log.v(TAG,"jsonItems+= " + jsonItems.toString().substring(0,20) + " ... " + jsonItems.substring(jsonItems.length()-20,jsonItems.length()));
                            //jsonItems = validateJson(jsonItems);

                            Log.v(TAG,"prepareHexString (ilfn+1) vs loopfn = " + (ilfn+1) + "/" + loopfn);
                            if((ilfn+1) == loopfn){
                                try{
                                    jsonArray = new JSONArray(jsonItems);
                                }catch(Exception e){
                                    Log.e(TAG,"prepareHexString -> jsonArray = new JSONArray(jsonItems) length(" + jsonArray.length() + ") " + e.getMessage());
                                    approximationToFix = 0;
                                    hearing = true;
                                }

                                if(jsonArray.length()>0){
                                    Log.v(TAG,"jsonArray::: " + jsonArray.toString().substring(0,20) + " ... " + jsonArray.toString().substring(jsonArray.toString().length()-20,jsonArray.toString().length()));
                                    //Log.v(TAG,"jsonArray::: " + jsonArray.toString().substring(0,20) + " ... " );
                                    String[] allcolumnsarray = (llamadasList.get(illamadasList).getPrimarykey() + "," + llamadasList.get(illamadasList).getColumns()).split(",");
                                    jsonArrayToArrayList(jsonArray.toString(),allcolumnsarray);
                                    Log.v(TAG,"aavaluesiu: " + aavaluesiu.toString());
                                    jsonArray = new JSONArray();
                                    Log.v(TAG,"jsonArray new JSONArray(): " + jsonArray.toString());

                                    if(aavaluesiu.size()>0){
                                        Log.v(TAG,"insertUpdateData inicio: " + aavaluesiu.toString());
                                        Log.v(TAG,"insertUpdateData llamadasList: " + llamadasList.get(illamadasList).getTableName() + " - " + llamadasList.get(illamadasList).getPrimarykey() + " - " + llamadasList.get(illamadasList).getColumns());
                                        if((aavaluesiu.size()/40) > limitToFix){
                                            limitToFix = aavaluesiu.size() / 20;
                                        }
                                        processSync.insertUpdateData(aavaluesiu,llamadasList.get(illamadasList).getTableName(),llamadasList.get(illamadasList).getPrimarykey(),llamadasList.get(illamadasList).getColumns(),ActivityPrincipal.context);
                                        limitToFix = 30;
                                        Log.v(TAG,"insertUpdateData fin: " + aavaluesiu.toString());

                                        aavaluesiu.clear();
                                        Log.v(TAG,"aavaluesiu.clear(): " + aavaluesiu.toString());
                                        approximationToFix = 0;
                                        hearing = true;
                                    }
                                }
                            }else{
                                approximationToFix = 0;
                                hearing = true;
                            }
                        }else if(syncData == 0){
                            // antes jsonObject = new JSONObject(vHexString);
                            // modificado para envio de marcas y biometrias
                            try{
                                jsonObject = new JSONObject(vHexString);
                            }catch(Exception e){
                                Log.e(TAG,"prepareHexString jsonObject " + e.getMessage());
                                try{
                                    jsonArray = new JSONArray(vHexString);
                                }catch(Exception ex){
                                    Log.e(TAG,"prepareHexString jsonObject " + e.getMessage());
                                }
                            }

                            //Log.v(TAG,"jsonObject -> " + jsonObject.toString() );
                            approximationToFix = 0;
                            hearing = true;
                            //if(jsonObject.length()>0){
                            //    hearing = true;
                            //}

                        }


                    }


                    //jsonArray = new JSONArray(vHexString);
                    //Log.v(TAG,"jsonArray.length: " + jsonArray.length());
                    //Log.v(TAG,"jsonArray: " + jsonArray.toString());

                    //for(int i=0; i<jsonArray.length(); i++){
                    //    Log.v(TAG,"jsonArray(" + i + "): " + jsonArray.get(i).toString());
                    //    Log.v(TAG,"jsonObject: " + jsonArray.getJSONObject(i).getString("APELLIDO_PATERNO") + " - " + jsonArray.getJSONObject(i).getString("APELLIDO_MATERNO") + " - " + jsonArray.getJSONObject(i).getString("FECHA_DE_NACIMIENTO"));
                    //}

                    //keyJsonObject(vHexString,"CADENA");



                /*
                for(int i=0; i<jsonArray.length(); i++){
                    Log.v(TAG,"jsonArray(" + i + "): " + jsonArray.get(i).toString());
                    Log.v(TAG,"jsonObject: " + jsonArray.getJSONObject(i).getString("APELLIDO_PATERNO") + " - " + jsonArray.getJSONObject(i).getString("APELLIDO_MATERNO") + " - " + jsonArray.getJSONObject(i).getString("FECHA_DE_NACIMIENTO"));
                }
                */

                }else{
                    //approximationToFix = 0;
                    //hearing = true;
                }
            }

        }catch(Exception e){
            Log.e(TAG,"prepareHexString: " + e.getMessage());
            approximationToFix = 0;
            hearing = true;
            if(e.getMessage().contains("bt socket closed")){
                Log.e(TAG,"Socket opening...");
                ActivityPrincipal.STATUS_ETHERNET = false;
                ActivityPrincipal.STATUS_ETHERNET = ActivityPrincipal.btSocketEthernet.ConnectBT();

            }else if(e.getMessage().contains("null object")){
                Log.e(TAG,"Socket opening - null object...");
                ActivityPrincipal.STATUS_ETHERNET = false;
                ActivityPrincipal.STATUS_ETHERNET = ActivityPrincipal.btSocketEthernet.ConnectBT();
            }else{
                Log.e(TAG,"Another troubles with bt ethernet");
                //ActivityPrincipal.STATUS_ETHERNET = false;
                //ActivityPrincipal.STATUS_ETHERNET = ActivityPrincipal.btSocketEthernet.ConnectBT();
            }
            try{
                Thread.sleep(1000);
            }catch (Exception ex){

            }
        }

        return jsonArray.toString();

    }

    public String findHexString(String hs, String cs, String ce){
        try{
            if(hs.length()>0){
                if(start == -1){
                    start = hs.indexOf(cs);
                }
                if(start != -1){
                    // Si viene json con longitud y token 0 en la cadena: 386a2{EMPRESA:""...}0
                    if(start > 0){

                        //Log.v(TAG,"Longitud a recibir: " + hs.substring(0,start));
                        //Log.v(TAG,"Longitud a recibir: " + Integer.parseInt(hs.substring(0,start).trim(),16));
                        if(hs.length()+200 > Integer.parseInt(hs.substring(0,start).trim(),16)){
                            for(int i = 0; i < 15; i++){
                                try{
                                    //Log.v(TAG,"Caracter final a evaluar: " + hs.substring((hs.length() - i),(hs.length() - i) + 1));
                                    if(ce.equalsIgnoreCase(hs.substring((hs.length() - i),(hs.length() - i) + 1))){
                                        end = hs.length() - i;
                                        //Log.v(TAG,"Caracter final " + end + " <" + hs.substring(end).trim() + ">");
                                        i = 15;
                                    }
                                }catch (Exception e){
                                    //Log.e(TAG,"Caracter final no encontrado " + e.getMessage());
                                }
                            }
                        }
                        // Si solo viene json en la cadena: {EMPRESA:""...}
                    }else if(start == 0){
                        if(ce.equalsIgnoreCase(hs.substring((hs.length() - 1),(hs.length() - 1) + 1))){
                            end = hs.length()-1;
                            Log.v(TAG,"Caracter final- " + end + " <" + hs.substring(end).trim() + ">");
                        }
                    }
                }

                // Si se ubica caracter de inicio y caracter final dentro la cadena
                if(start != -1 && end != -1){
                    Log.v(TAG,"caracteres encontrados en la posición: " + start + " ... " + end);
                    //Log.v(TAG,"hexString completo: " + hexString);
                    hexStrings = hexString.substring(start,(end+1));
                    //Log.v(TAG,"hexStrings completo: " + hexStrings);
                    if(hexStrings.length()>30){
                        Log.v(TAG,"hexStrings resumen: " + hexStrings.substring(0,15) + " ... " + hexStrings.substring(hexStrings.length()-15,hexStrings.length()));
                    }else{
                        Log.v(TAG,"hexStrings resumen: " + hexStrings);
                    }
                    hexString = "";
                    start = -1;
                    end = -1;
                    lhs = 0;
                }else{
                    hexStrings = "";
                }
            }else{
                hexStrings = "";
            }
        }catch(Exception e){
            hexString = "";
            start = -1;
            end = -1;
            approximationToFix = 0;
            hearing = true;
            Log.e(TAG,"findHexString: " + e.getMessage());
        }

        return hexStrings;
    }

    public String validateJson(String hs){
        try {
            new JSONObject(hs);
            return hs;
        } catch (JSONException e) {
            Log.e(TAG,"validateJson JSONObject: " + e.getMessage());
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(hs);
                return hs;
            } catch (JSONException ex) {
                Log.e(TAG,"validateJson JSONArray: " + ex.getMessage());
                return "";
            }
        }
    }

    public void jsonArrayToArrayList(String vHexString, String [] allcolumnsarray){
        //
        JSONArray ja;
        try{
            ja = new JSONArray(vHexString);
            aavaluesiu = new ArrayList<ArrayList<String>>();
            //Log.v(TAG,"ja.length(): " + ja.length());
            for(int i = 0; i < ja.length(); i++){
                aavaluesiu.add(i,jsonObjectToArrayList(ja.get(i).toString(),allcolumnsarray));
            }

            /*
            Log.v(TAG,"aavaluesiu.size()" + aavaluesiu.size());
            for(int i = 0; i < aavaluesiu.size(); i++){
                Log.v(TAG,"aavaluesiu[" + (i+1) + "]: " + aavaluesiu.get(i).toString());
            }
            */

        }catch (Exception e){
            Log.e(TAG,"jsonArrayToArrayList " + e.getMessage());
        }
    }

    public ArrayList jsonObjectToArrayList(String vHexString, String [] allcolumnsarray){
        //
        JSONObject jo;
        try{

            jo = new JSONObject(vHexString);
            avaluesiu = new ArrayList<String>();
            //Log.v(TAG,"allcolumnsarray.length: " + allcolumnsarray.length);
            for(int i = 0; i < allcolumnsarray.length; i++){
                String value = keyvalueJsonObject(jo.getString(allcolumnsarray[i]),"date",1);
                if(value.equalsIgnoreCase("null")){
                    avaluesiu.add(i,null);
                }else{
                    avaluesiu.add(i,value);
                }

            }

            /*
            Log.v(TAG,"avaluesiu.size(): " + avaluesiu.size());
            Log.v(TAG,"avaluesiu.toString()" + " -> " + avaluesiu.toString());
            for(int i = 0; i < avaluesiu.size(); i++){
                Log.v(TAG,allcolumnsarray[i] + " -> " + avaluesiu.get(i));
            }
            Log.v(TAG,"jsonObjectToArrayList: " + vHexString);
            Log.v(TAG,"jsonObjectToArrayList: " + "----------------------------------------------");
            */

        }catch (Exception e){
            Log.e(TAG,"jsonObjectToArrayList " + e.getMessage());
        }

        return avaluesiu;

    }

    public String keyvalueJsonObject(String vHexString, String key, int mode) {
        // Extraer los valores de una llave
        // mode 0 = devuelve "" en caso de no encontrar value en key
        // mode 1 = devuelve vHexString
        JSONObject jo;
        try{
            jo = new JSONObject(vHexString);
            return jo.getString(key);
        }catch (Exception e){
            //Log.e(TAG,"keyJsonObject " + e.getMessage());
            if(mode == 0){
                return "";
            }else{
                return vHexString;
            }
        }
    }



    public void testerLlamadas(){

        //writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=TERMINAL&EMPRESA=TEMPUS_WS_T10&USER=TEMPUS&PASS=TEMPUSSCA&IP=0.0.0.0&MAC=00-00-00-00-00-00&HOSTNAME=TERMINAL_T10" + "");
        //writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=GENERAR_DATA&DATA={\"session\":15966,\"llamada\":\"551_EXEC_L2\",\"parametros\":\"'SYNC_PERSONAL_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,05/01/2014 15:15:56.069'\"}");
        //writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=EXEC_DATA&DATA={\"session\":15966,\"llamada\":\"551_EXEC_L2\",\"parametros\":\"'SYNC_PERSONAL_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,05/01/2014 15:15:56.069'\"}");
        //writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=SUBSTR_FILE&FILENAME=FILE_5941778175cc9&START=0&LENGTH=200000");

        //concatPartHexString("FILE_5941778175cc9",419980);




        //return "";
    }

    public void callsws(int mode, ArrayList<String> da, String opcion) {
        // 0 = recibir parametros para la siguiente llamada
        // 1 = recibir parametros para insercion en base de datos

        //typeJson = object or array
        if (mode == 0) {
            syncData = 0;

        } else if (mode == 1) {
            syncData = 1;

        }

        String data = "";
        /*
        Log.v(TAG,"keyvalueBySend " + keyvalueBySend);
        for(int i = 0; i < keyvalueBySend.size(); i++){
            data += keyvalueBySend.get(1).toString() + "=" + keyvalueBySend.get(2).toString();
            if(i < keyvalueBySend.size() - 1){
                data += "&";
            }
        }
        keyvalueBySend.clear();
        Log.v(TAG,"Streamdata " + data);
        */

        if (opcion.equals("TERMINAL")) {
            //data = "OPCION=TERMINAL&EMPRESA=TEMPUS_WS_T10&USER=TEMPUS&PASS=TEMPUSSCA&IP=0.0.0.0&MAC=00-00-00-00-00-00&HOSTNAME=TERMINAL_T10" + "";
            data = "O=T&" + da.get(0) + "&" + da.get(1) + "&" + da.get(2) + "&" + da.get(3) + "&" + da.get(4) + "&" + da.get(5) + "";
            Log.v(TAG, "Streamdata " + data);
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(), data);
        } else if (opcion.equals("EXEC_DATA")) {
            //data = "OPCION=EXEC_DATA&DATA={\"session\":15966,\"llamada\":\"551_EXEC_L2\",\"parametros\":\"'SYNC_PERSONAL_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,05/01/2014 15:15:56.069'\"}";
            data = "O=E&" + da.get(0) + "";
            Log.v(TAG, "Streamdata " + data);
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(), data);
        } else if (opcion.equals("SUBSTR_FILE")) {
            //data = "OPCION=SUBSTR_FILE&FILENAME=" + fn + "&START=" + i*200000 + "&LENGTH=" + (i+1)*200000 + "";
            data = "O=S&" + da.get(0) + "&" + da.get(1) + "&" + da.get(2) + "";
            Log.v(TAG, "Streamdata " + data);
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(), data);
        } else if(opcion.equals("DELETE")){
            data = "O=D&" + da.get(0);
            Log.v(TAG, "Streamdata " + data);
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(), data);
        } else if(opcion.equals("CREAR")) {
            data = "O=C&" + da.get(0) + "&" + da.get(1) + "&" + da.get(2) + "";
            Log.v(TAG, "Streamdata " + data);
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(), data);
        } else if(opcion.equals("FIX")){
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"");

        }
    }


    public class EthernetExecuting extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private int lfn;
        private int lcut = 100000;

        private int idt;
        private int loopdt;
        private int ldt;
        private int lcutdt = 300;

        public EthernetExecuting(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creating " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Executing " + nombreHilo);

            jsonAuthenticate = new JSONObject();
            ArrayList<String> dataArray = new ArrayList<String>();
            String data = "";
            Fechahora fechahora = new Fechahora();
            queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(ActivityPrincipal.context);
            queriesMarcaciones = new QueriesMarcaciones(ActivityPrincipal.context);
            atCommandMode = false;

            while(true){
                try{
                    Log.v(TAG,"EthernetExecuting next loop");
                    if(ActivityPrincipal.STATUS_ETHERNET){
                        Thread.sleep(3000);
                        jsonItems = "";
                        loopfn = 0;
                        ilfn = -2;
                        Log.v(TAG,"EthernetExecuting: " + "hearing (iniciando tareas) " + String.valueOf(hearing));
                        //Log.v(TAG,"jsonAuthenticate " + jsonAuthenticate.length());

                        if(jsonAuthenticate.length() == 0){
                            // 1. Llamada para iniciar sesión
                            dataArray = new ArrayList<String>();
                            dataArray.add("EMPRESA=TEMPUS_WS_T10");
                            dataArray.add("USER=TEMPUS");
                            dataArray.add("PASS=TEMPUSSCA");
                            dataArray.add("IP=0.0.0.0");
                            dataArray.add("MAC=00-00-00-00-00-00");
                            dataArray.add("HOSTNAME=TERMINAL_T10");

                            hearing = false;
                            callsws(0,dataArray,"TERMINAL");
                            dataArray.clear();

                            while(!hearing){
                                Log.v(TAG,"EthernetExecuting: " + "hearing (autenticando) " + String.valueOf(hearing));
                                Thread.sleep(2000);
                            }

                            Log.v(TAG,"jsonObject " + jsonObject.toString());
                            if(jsonObject.getString("ESTADO").equalsIgnoreCase("true")){
                                jsonAuthenticate = new JSONObject(jsonObject.getString("DATA"));
                                Log.v(TAG,"jsonAuthenticate " + jsonAuthenticate.toString());
                            }
                            jsonObject = new JSONObject();
                        }

                        // 2. Llamada para ejecutar objeto de base de datos del servidor

                        if(jsonAuthenticate.length() > 0){

                            // Enviar biometrias
                            for(int i = 0; i < 11; i++){
                                try{

                                    List<PersonalTipolectoraBiometria> personalTipolectoraBiometriaList = queriesPersonalTipolectoraBiometria.select_one_row();

                                    if(personalTipolectoraBiometriaList.isEmpty()){
                                        Log.v(TAG,"Sin biometrias por pasar");
                                        i = 11;
                                    }else{
                                        Log.v(TAG,"Biometria a sincronizar: " + personalTipolectoraBiometriaList.get(0).toString());
                                        try{
                                            //String parametersnamesvalues = "";
                                            String parametersnamesvalues = "pID_PER_TIPOLECT_BIO," + personalTipolectoraBiometriaList.get(0).getIdPerTipolectBio() +
                                                    ";pINDICE_BIOMETRIA," + personalTipolectoraBiometriaList.get(0).getIndiceBiometria() +
                                                    ";pEMPRESA," + personalTipolectoraBiometriaList.get(0).getEmpresa() +
                                                    ";pCODIGO," + personalTipolectoraBiometriaList.get(0).getCodigo() +
                                                    ";pVALOR_BIOMETRIA," + personalTipolectoraBiometriaList.get(0).getValorBiometria() +
                                                    ";pIMAGEN_BIOMETRIA," + personalTipolectoraBiometriaList.get(0).getImagenBiometria() +
                                                    ";pFECHA_BIOMETRIA," + personalTipolectoraBiometriaList.get(0).FechaBiometria +
                                                    ";pFECHA_HORA_SINC," + fechahora.getFechahoraSync(personalTipolectoraBiometriaList.get(0).getFechaHoraSinc());

                                            data = "{\"session\":" + jsonAuthenticate.getString("ID_SESSION") + ",\"llamada\":\"" + jsonAuthenticate.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + "SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_SERVER_TX" + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}";
                                            Log.v(TAG,"data = " + data);
                                            //dataArray.add("DATA=" + "{\"session\":" + jsonAuthenticate.getString("ID_SESSION") + ",\"llamada\":\"" + jsonAuthenticate.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + "SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_SERVER_TX" + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}");

                                            ldt = data.length();
                                            if(ldt == 0){
                                                loopdt = 0;
                                            }else if(ldt < lcutdt){
                                                loopdt = 1;
                                            }else if(ldt >= lcutdt){
                                                loopdt = 0;
                                                while((lcutdt * loopdt) < ldt){
                                                    loopdt++;
                                                }
                                            }else{
                                                loopdt = 0;
                                            }

                                            Log.v(TAG,"ldt=" + ldt + "loopdt=" + loopdt + ",lcutdt=" + lcutdt);
                                            // Concatenar data de biometrias en el servidor
                                            String FILENAME = "";
                                            for(idt = 0; idt < loopdt; idt++){
                                                Log.v(TAG,"Concatenando " + "parte " + (idt + 1) + "/" + loopdt + " en FILENAME " + FILENAME);
                                                dataArray.add("FILENAME=" + FILENAME);
                                                dataArray.add("TIPO=" + "1");
                                                //Log.v(TAG,"idt vs loopdt === " + idt + "-" + loopdt);
                                                if(idt < loopdt - 1) {
                                                    Log.v(TAG,"Concatenando -> " + data.substring(idt * lcutdt, (idt + 1) * lcutdt));
                                                    dataArray.add("STRING=" + data.substring(idt * lcutdt, (idt + 1) * lcutdt));
                                                }else if(idt >= loopdt - 1){
                                                    Log.v(TAG,"Ejecudanto -> " + data.substring(idt * lcutdt));
                                                    dataArray.add("STRING=" + data.substring(idt * lcutdt));
                                                }

                                                hearing = false;
                                                jsonItems = "";

                                                callsws(0,dataArray,"CREAR");
                                                dataArray.clear();

                                                while(!hearing){
                                                    Log.v(TAG,"EthernetExecuting: " + "hearing2 (esperando " + "concatenacion de data biometrias" + ") " + String.valueOf(hearing));
                                                    Thread.sleep(2000);
                                                }

                                                try{
                                                    // consultar el nombre del archivo creado
                                                    FILENAME = jsonObject.getString("FILENAME");
                                                    Log.v(TAG,"EthernetExecuting: " + jsonObject.toString() + "");

                                                    //Thread.sleep(1000);
                                                }catch(Exception e){
                                                    Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                                                    try{
                                                        Thread.sleep(1000);
                                                    }catch(Exception ex){
                                                    }
                                                }

                                            }

                                            // Ejecutar data de biometrias en el servidor
                                            try{
                                                Log.v(TAG,"Ejecutar data de biometrias en el servidor");
                                                dataArray.add("FILENAME=" + FILENAME);
                                                dataArray.add("TIPO=" + "4");
                                                dataArray.add("STRING=" + "");

                                                hearing = false;
                                                jsonItems = "";

                                                callsws(0,dataArray,"CREAR");
                                                dataArray.clear();

                                                while(!hearing){
                                                    Log.v(TAG,"EthernetExecuting: " + "hearing2 (esperando " + "ejecucion de data biometrias" + ") " + String.valueOf(hearing));
                                                    Thread.sleep(2000);
                                                }

                                            }catch (Exception e){
                                                Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                                                try{
                                                    Thread.sleep(1000);
                                                }catch(Exception ex){
                                                }
                                            }


                                            /*
                                            hearing = false;
                                            jsonItems = "";
                                            callsws(0,dataArray,"EXEC_DATA");
                                            dataArray.clear();

                                            while(!hearing){
                                                Log.v(TAG,"EthernetExecuting: " + "hearing3 (esperando " + "respuesta de biometrias" + ") " + String.valueOf(hearing));
                                                Thread.sleep(2000);
                                            }

                                            */

                                            /*
                                            try{
                                                // revisar resultado de sincronizacion de biometria
                                                dataArray.add("FILENAME=" + jsonObject.getString("NOMBRE_ARCHIVO"));
                                                dataArray.add("START=" + 0);
                                                dataArray.add("LENGTH=" + 100000);

                                                Log.v(TAG,"EthernetExecuting: " + "hearing2 " + String.valueOf(hearing));
                                                hearing = false;
                                                callsws(0,dataArray,"SUBSTR_FILE");
                                                //dataArray.clear();
                                                //jsonObject = new JSONObject();

                                                //writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=SUBSTR_FILE&FILENAME=" + fn + "&START=" + i*200000 + "&LENGTH=" + (i+1)*200000 + "");
                                                Log.v(TAG,"EthernetExecuting: " + "hexStrings final parte-" + 1 + "/" + 1 + " -> " + hexStrings);
                                                while(!hearing){
                                                    Log.v(TAG,"EthernetExecuting: " + "hearing3 (esperando parte-" + 1 + "/" + 1 + ") " + String.valueOf(hearing));
                                                    Thread.sleep(2000);
                                                }
                                                Thread.sleep(1000);
                                            }catch(Exception e){
                                                dataArray.clear();
                                                Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                                                try{
                                                    Thread.sleep(1000);
                                                }catch(Exception ex){
                                                }
                                            }
                                            */

                                            Log.v(TAG,"EthernetExecuting " + "revisión de jsonArray.toString(): " + jsonArray.toString());
                                            if(jsonArray.toString().equalsIgnoreCase("[]")){
                                                queriesPersonalTipolectoraBiometria.ActualizarBiometriaEnviadaServidor(personalTipolectoraBiometriaList.get(0).getIndiceBiometria(),personalTipolectoraBiometriaList.get(0).getIdTipoDetaBio());
                                            }else{
                                                Log.v(TAG,"EthernetExecuting " + "No se completo la sincronización de biometrias");
                                            }

                                            // Eliminar el archivo creado en el servidor
                                            try{
                                                //dataArray.add("FILENAME=" + jsonObject.getString("NOMBRE_ARCHIVO"));
                                                dataArray.add("FILENAME=" + FILENAME);

                                                Log.v(TAG,"EthernetExecuting: " + "hearing4 " + String.valueOf(hearing));
                                                hearing = false;
                                                callsws(0,dataArray,"DELETE");
                                                dataArray.clear();

                                                Log.v(TAG,"EthernetExecuting: " + "hexStrings final parte-1" + " -> " + hexStrings);
                                                while(!hearing){
                                                    Log.v(TAG,"EthernetExecuting: " + "hearing5 (esperando parte-1" +  ") " + String.valueOf(hearing));
                                                    Thread.sleep(2000);
                                                }
                                                Thread.sleep(3000);
                                            }catch (Exception e){
                                                dataArray.clear();
                                                Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                                                try{
                                                    Thread.sleep(1000);
                                                }catch(Exception ex){

                                                }
                                            }



                                        }catch(SQLException e){
                                            Log.e(TAG,"EthernetExecuting " + "SQLException " + e.toString());
                                        }catch(Exception e){
                                            Log.e(TAG,"EthernetExecuting " + "SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_SERVER_TX " + e.getMessage());
                                        }
                                    }
                                }catch (Exception e){
                                    Log.e(TAG,"EthernetExecuting " + "SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_SERVER_TX general" + e.getMessage());
                                }
                            }

                            // Tiempo de espera para permitir configuración de Ethernet
                            try{
                                enableSetEthernet = true;
                                Log.v(TAG,"enableSetEthernet inicio: " + enableSetEthernet);
                                Thread.sleep(1000);
                                while (atCommandMode){
                                    Log.v(TAG,"enableSetEthernet while: " + enableSetEthernet);
                                    Thread.sleep(1000);
                                }
                                enableSetEthernet = false;
                                Log.v(TAG,"enableSetEthernet fin: " + enableSetEthernet);
                            }catch (Exception e){
                                enableSetEthernet = false;
                                Log.e(TAG,"enableSetEthernet: " + enableSetEthernet);
                            }

                            // Enviar marcaciones
                            for(int i = 0; i < 11; i++) {

                                try {

                                    List<Marcaciones> marcacionesList = queriesMarcaciones.select_one_row();

                                    if (marcacionesList.isEmpty()) {
                                        Log.v(TAG,"Sin marcaciones por pasar");
                                        i = 11;
                                    } else {
                                        Log.v(TAG,"Marcacion a sincronizar: " + marcacionesList.get(0).toString());
                                        try {
                                            //String parametersnamesvalues = "";
                                            String parametersnamesvalues = "pEMPRESA," + marcacionesList.get(0).getEmpresa() +
                                                    ";pCODIGO," + marcacionesList.get(0).getCodigo() +
                                                    ";pFECHAHORA," + fechahora.getFechahoraSqlServer(marcacionesList.get(0).getFechahora()) +
                                                    ";pNUMERO_TARJETA," + marcacionesList.get(0).getValorTarjeta() +
                                                    ";pHORATXT," + marcacionesList.get(0).getHoraTxt() +
                                                    ";pENT_SAL," + marcacionesList.get(0).getEntSal() +
                                                    ";pFLAG," + marcacionesList.get(0).getFlag() +
                                                    ";pFECHA," + fechahora.getFechahoraSqlServer(marcacionesList.get(0).getFecha()) +
                                                    ";pHORA," + fechahora.getFechahoraSqlServer(marcacionesList.get(0).getHora()) +
                                                    ";pIDTERMINAL," + marcacionesList.get(0).getIdterminal() +
                                                    ";pIDLECTORA," + marcacionesList.get(0).getIdTipoLect() +
                                                    ";pFLG_ACTIVIDAD," + marcacionesList.get(0).getFlgActividad() +
                                                    ";pIDUSUARIO," + marcacionesList.get(0).getIdUsuario() +
                                                    ";pTMP_LISTAR," + marcacionesList.get(0).getTmpListar() +
                                                    ";pDATOS," + marcacionesList.get(0).getDatos();

                                            dataArray.add("DATA=" + "{\"session\":" + jsonAuthenticate.getString("ID_SESSION") + ",\"llamada\":\"" + jsonAuthenticate.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + "SYNC_MARCACIONES_TX" + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}");


                                            hearing = false;
                                            jsonItems = "";
                                            callsws(0, dataArray, "EXEC_DATA");
                                            dataArray.clear();

                                            while (!hearing) {
                                                Log.v(TAG, "EthernetExecuting: " + "hearing3 (esperando " + "respuesta de marcaciones" + ") " + String.valueOf(hearing));
                                                Thread.sleep(2000);
                                            }

                                            try {
                                                // revisar resultado de sincronizacion de marcaciones
                                                dataArray.add("FILENAME=" + jsonObject.getString("NOMBRE_ARCHIVO"));
                                                dataArray.add("START=" + 0);
                                                dataArray.add("LENGTH=" + 100000);

                                                Log.v(TAG, "EthernetExecuting: " + "hearing2 " + String.valueOf(hearing));
                                                hearing = false;
                                                callsws(0, dataArray, "SUBSTR_FILE");
                                                //dataArray.clear();
                                                //jsonObject = new JSONObject();

                                                //writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=SUBSTR_FILE&FILENAME=" + fn + "&START=" + i*200000 + "&LENGTH=" + (i+1)*200000 + "");
                                                Log.v(TAG, "EthernetExecuting: " + "hexStrings final parte-" + 1 + "/" + 1 + " -> " + hexStrings);
                                                while (!hearing) {
                                                    Log.v(TAG, "EthernetExecuting: " + "hearing3 (esperando parte-" + 1 + "/" + 1 + ") " + String.valueOf(hearing));
                                                    Thread.sleep(2000);
                                                }
                                                Thread.sleep(1000);
                                            } catch (Exception e) {
                                                dataArray.clear();
                                                Log.e(TAG, "EthernetExecuting: " + e.getMessage());
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (Exception ex) {
                                                }
                                            }

                                            // Eliminar el archivo creado en el servidor
                                            try{
                                                //dataArray.add("FILENAME=" + jsonObject.getString("NOMBRE_ARCHIVO"));
                                                Log.v(TAG,"EthernetExecuting: " + "hearing4 " + String.valueOf(hearing));
                                                hearing = false;
                                                callsws(0,dataArray,"DELETE");
                                                dataArray.clear();

                                                Log.v(TAG,"EthernetExecuting: " + "hexStrings final parte-1" + " -> " + hexStrings);
                                                while(!hearing){
                                                    Log.v(TAG,"EthernetExecuting: " + "hearing5 (esperando parte-1" +  ") " + String.valueOf(hearing));
                                                    Thread.sleep(2000);
                                                }
                                                Thread.sleep(1000);
                                            }catch (Exception e){
                                                dataArray.clear();
                                                Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                                                try{
                                                    Thread.sleep(1000);
                                                }catch(Exception ex){

                                                }
                                            }


                                            Log.v(TAG, "EthernetExecuting " + "revisión de jsonArray.toString(): " + jsonArray.toString());
                                            if (jsonArray.toString().equalsIgnoreCase("[]")) {
                                                queriesMarcaciones.ActualizarSincronizado(marcacionesList.get(0), 1);
                                            } else if (jsonArray.toString().contains("Infracción de la restricción PRIMARY KEY")) {
                                                queriesMarcaciones.ActualizarSincronizado(marcacionesList.get(0), 1);
                                            } else {
                                                Log.v(TAG, "EthernetExecuting " + "No se completo la sincronización de marcaciones");
                                            }

                                        } catch (SQLException e) {
                                            Log.e(TAG, "EthernetExecuting " + "SQLException " + e.toString());
                                        } catch (Exception e) {
                                            Log.e(TAG, "EthernetExecuting " + "SYNC_MARCACIONES_TX " + e.getMessage());
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "EthernetExecuting " + "SYNC_MARCACIONES_TX general" + e.getMessage());
                                }
                            }

                            // Tiempo de espera para permitir configuración de Ethernet
                            try{
                                enableSetEthernet = true;
                                Log.v(TAG,"enableSetEthernet inicio: " + enableSetEthernet);
                                Thread.sleep(1000);
                                while (atCommandMode){
                                    Log.v(TAG,"enableSetEthernet while: " + enableSetEthernet);
                                    Thread.sleep(1000);
                                }
                                enableSetEthernet = false;
                                Log.v(TAG,"enableSetEthernet fin: " + enableSetEthernet);
                            }catch (Exception e){
                                enableSetEthernet = false;
                                Log.e(TAG,"enableSetEthernet: " + enableSetEthernet);
                            }

                            ///*
                            // Obtener llamadas para autorizaciones
                            if(llamadasList.size() == 0){
                                llamadasList = processSync.getLlamadas(ActivityPrincipal.context);
                                Log.v(TAG,"llamadasList.size() " + llamadasList.size());
                                for(int i = 0; i <llamadasList.size(); i++ ){
                                    Log.v(TAG,"llamadasList " + i + " - " + llamadasList.get(i));
                                }
                            }
                            //llamadasList = processSync.getLlamadas(ActivityPrincipal.context);

                            // ¿Existen llamadas por ejecutar?
                            if(llamadasList.size() == 0){
                                Log.v(TAG,"Sin llamadas por ejecutar " + String.valueOf(llamadasList.size()));
                            }else{
                                for(illamadasList = 0; illamadasList < llamadasList.size(); illamadasList++){
                                    Log.v(TAG,"illamadasList: " + illamadasList);
                                    String parametersnamesvalues = processSync.prepareParametersLlamadas(llamadasList.get(illamadasList).getParameters(), ActivityPrincipal.context);

                                    dataArray.add("DATA=" + "{\"session\":" + jsonAuthenticate.getString("ID_SESSION") + ",\"llamada\":\"" + jsonAuthenticate.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + llamadasList.get(illamadasList).getIdllamada() + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}");

                                    hearing = false;
                                    jsonItems = "";
                                    callsws(0,dataArray,"EXEC_DATA");
                                    dataArray.clear();

                                    while(!hearing){
                                        Log.v(TAG,"EthernetExecuting: " + "hearing3 (esperando " + llamadasList.get(illamadasList).getIdllamada() + ") " + String.valueOf(hearing));
                                        Thread.sleep(2000);
                                    }

                                    // 3. Llamada para extraer data del archivo json creado en el Servidor

                                    Log.v(TAG,"jsonObject " + jsonObject.toString());
                                    lfn = jsonObject.getInt("LONGITUD");
                                    if(lfn == 0){
                                        loopfn = 0;
                                    }else if(lfn < lcut){
                                        loopfn = 1;
                                    }else if(lfn >= lcut){
                                        loopfn = 0;
                                        while((lcut * loopfn) < lfn){
                                            loopfn++;
                                        }
                                    }else{
                                        loopfn = 0;
                                    }
                                    Log.v(TAG,"EthernetExecuting: jsonObject LONGITUD: " + lfn + " en " + loopfn + " parte(s)");

                                    for(ilfn = 0; ilfn < loopfn; ilfn++){
                                        try{
                                            dataArray.add("FILENAME=" + jsonObject.getString("NOMBRE_ARCHIVO"));
                                            dataArray.add("START=" + (ilfn*lcut));
                                            dataArray.add("LENGTH=" + (lcut));

                                            Log.v(TAG,"EthernetExecuting: " + "hearing2 " + String.valueOf(hearing));
                                            hearing = false;
                                            callsws(1,dataArray,"SUBSTR_FILE");
                                            dataArray.clear();
                                            //jsonObject = new JSONObject();

                                            //writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=SUBSTR_FILE&FILENAME=" + fn + "&START=" + i*200000 + "&LENGTH=" + (i+1)*200000 + "");
                                            Log.v(TAG,"EthernetExecuting: " + "hexStrings final parte-" + (ilfn+1) + "/" + loopfn + " -> " + hexStrings);
                                            while(!hearing){
                                                Log.v(TAG,"EthernetExecuting: " + "hearing3 (esperando parte-" + (ilfn+1) + "/" + loopfn + ") " + String.valueOf(hearing));
                                                Thread.sleep(2000);
                                            }
                                            Thread.sleep(3000);
                                        }catch(Exception e){
                                            Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                                            try{
                                                Thread.sleep(1000);
                                            }catch(Exception ex){

                                            }
                                        }
                                    }

                                    // Eliminar el archivo creado en el servidor
                                    try{
                                        dataArray.add("FILENAME=" + jsonObject.getString("NOMBRE_ARCHIVO"));
                                        Log.v(TAG,"EthernetExecuting: " + "hearing4 " + String.valueOf(hearing));
                                        hearing = false;
                                        callsws(0,dataArray,"DELETE");
                                        dataArray.clear();

                                        Log.v(TAG,"EthernetExecuting: " + "hexStrings final parte-1" + " -> " + hexStrings);
                                        while(!hearing){
                                            Log.v(TAG,"EthernetExecuting: " + "hearing5 (esperando parte-1" +  ") " + String.valueOf(hearing));
                                            Thread.sleep(2000);
                                        }
                                        Thread.sleep(3000);
                                    }catch (Exception e){
                                        Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                                        try{
                                            Thread.sleep(1000);
                                        }catch(Exception ex){

                                        }
                                    }


                                    // Tiempo de espera para permitir configuración de Ethernet
                                    try{
                                        enableSetEthernet = true;
                                        Log.v(TAG,"enableSetEthernet inicio: " + enableSetEthernet);
                                        Thread.sleep(1000);
                                        while (atCommandMode){
                                            Log.v(TAG,"enableSetEthernet while: " + enableSetEthernet);
                                            Thread.sleep(1000);
                                        }
                                        enableSetEthernet = false;
                                        Log.v(TAG,"enableSetEthernet fin: " + enableSetEthernet);
                                    }catch (Exception e){
                                        enableSetEthernet = false;
                                        Log.e(TAG,"enableSetEthernet: " + enableSetEthernet);
                                    }
                                }
                            }
                            //*/


                        }

                    }else{
                        Thread.sleep(3000);
                    }
                }catch (Exception e){
                    Log.e(TAG,"EthernetExecuting: " + e.getMessage());

                    // Tiempo de espera para permitir configuración de Ethernet
                    try{
                        enableSetEthernet = true;
                        Log.v(TAG,"enableSetEthernet inicio: " + enableSetEthernet);
                        Thread.sleep(1000);
                        while (atCommandMode){
                            Log.v(TAG,"enableSetEthernet while: " + enableSetEthernet);
                            Thread.sleep(1000);
                        }
                        enableSetEthernet = false;
                        Log.v(TAG,"enableSetEthernet fin: " + enableSetEthernet);
                    }catch (Exception ex){
                        enableSetEthernet = false;
                        Log.e(TAG,"enableSetEthernet: " + enableSetEthernet);
                    }
                    /*
                    try{
                        Thread.sleep(3000);
                    }catch (Exception ex){

                    }
                    */
                }
            }



        }

        public void start(){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                //this.fn = fn;
                //this.lfn = lfn;
                super.start();
            }
        }

    }


    public class EthernetReading extends Thread{
        private Thread hilo;
        private String nombreHilo;

        public EthernetReading(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);

            while(true){

                try{
                    Log.v(TAG,"EthernetReading loop");
                    prepareHexString(ActivityPrincipal.btSocketEthernet);
                    //Thread.sleep(1000);

                    //Thread.sleep(1000);
                }catch (Exception e){
                    Log.v(TAG,"Error General Hilo " + nombreHilo + ": " + e.toString());
                    try{
                        Thread.sleep(1000);
                    }catch(Exception ex){

                    }

                }
            }

        }

        public void start(){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }

    public class EthernetFixing extends Thread{
        private Thread hilo;
        private String nombreHilo;

        public EthernetFixing(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
            ArrayList<String> dataArrayFix = new ArrayList<String>();
            approximationToFix = 0;

            while(true){
                try{
                    //
                    if(hearing == false){
                        approximationToFix++;
                    }else{
                        approximationToFix = 0;
                    }
                    Log.v(TAG,"EthernetFixing approximationToFix vs limitToFix: " + approximationToFix + "/" + limitToFix);
                    //Log.v(TAG,"jsonItems-> " + jsonArray.length() + " - " + jsonItems.toString());
                    if(approximationToFix > limitToFix){
                        //dataArrayFix.add("DATA=" + "{\"session\":" + "9999" + ",\"llamada\":\"" + "551" + "_" + "EXEC_L2" + "\",\"parametros\":\"'SYNC_PERSONAL_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,05/01/2014 15:15:56.069'\"}");
                        hexString = "";
                        start = -1;
                        end = -1;
                        approximationToFix = 0;
                        hearing = true;
                        //callsws(0,dataArrayFix,"EXEC_DATA");
                        //dataArrayFix.clear();
                    }

                    //Log.v(TAG,"ethernetExecuting.isAlive() = " + ethernetExecuting.isAlive());
                    //Log.v(TAG,"ethernetReading.isAlive() = " + ethernetReading.isAlive());
                    if(!ethernetReading.isAlive()){
                        ethernetReading.start();
                    }
                    if(!ethernetExecuting.isAlive()){
                        ethernetExecuting.start();
                    }
                    Thread.sleep(1000);
                }catch (Exception e){
                    Log.d(TAG,"Error General Hilo " + nombreHilo + ": " + e.toString());
                    try{
                        Thread.sleep(1000);
                    }catch(Exception ex){

                    }

                }
            }
        }

        public void start(){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }


    public class EthernetATCommand extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private String atcommand;
        private String parameters;

        private boolean set;
        private boolean get;
        private boolean restart;

        public EthernetATCommand(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);

            approximationToFix = 0;
            hexStringcfg = "";

            try{
                Log.v(TAG,"EthernetATCommand enableSetEthernet " + enableSetEthernet);
                while(!enableSetEthernet){
                    Log.v(TAG,"EthernetATCommand enableSetEthernet while " + enableSetEthernet);
                    Thread.sleep(250);
                }
            }catch (Exception e){
                Log.e(TAG,"EthernetATCommand enableSetEthernet " + enableSetEthernet);
            }

            atCommandMode = true;
            String trama = "";
            try {
                Log.v(TAG,"Iniciando configuración por comandos AT");

                /* Activando AT Command Mode en Modulo */
                trama = "+++";
                ActivityPrincipal.btSocketEthernet.getOutputStream().write(trama.getBytes());
                Log.v(TAG,"setAtCommand SALIO: " + trama);
                Thread.sleep(500);

                trama = "a";
                ActivityPrincipal.btSocketEthernet.getOutputStream().write(trama.getBytes());
                Log.v(TAG,"setAtCommand SALIO: " + trama);
                Thread.sleep(1000);

                //---------------------------------------------------------------------------
                 /* Test AT Command Mode en Modulo */
                //trama = "AT+VER" + "\r";
                //ActivityPrincipal.btSocketEthernet.getOutputStream().write(trama.getBytes());
                //Log.v(TAG,"setAtCommand SALIO: " + trama);
                //Thread.sleep(1000);

                //---------------------------------------------------------------------------

            /*
                AT+WANN<CR>
                AT+WANN=<Mode>,<IP address>,<Mask>,<Gateway><CR>

                AT+DNS<CR>
                AT+DNS=<Address><CR>
            */

                if(set){
                    trama = atcommand + parameters + "\r" + "\n";
                    ActivityPrincipal.btSocketEthernet.getOutputStream().write(trama.getBytes());
                    Log.v(TAG,"setAtCommand SALIO: " + trama);
                    Thread.sleep(3000);
                }

                if(get){
                    trama = atcommand + "\r" + "\n";
                    ActivityPrincipal.btSocketEthernet.getOutputStream().write(trama.getBytes());
                    Log.v(TAG,"setAtCommand SALIO: " + trama);
                    Thread.sleep(3000);
                }


                //---------------------------------------------------------------------------
                if(restart){
                    /* Desactivando(Restart) AT Command Mode en Modulo */
                    trama = "AT+Z" + "\r" + "\n";
                    ActivityPrincipal.btSocketEthernet.getOutputStream().write(trama.getBytes());
                    Log.v(TAG,"setAtCommand SALIO: " + trama);
                    Thread.sleep(5000);
                }else{
                    /* Desactivando (go out) AT Command Mode en Modulo */
                    trama = "AT+ENTM" + "\r"+ "\n";
                    ActivityPrincipal.btSocketEthernet.getOutputStream().write(trama.getBytes());
                    Log.v(TAG,"setAtCommand SALIO: " + trama);
                    Thread.sleep(2000);
                }
                //---------------------------------------------------------------------------

                Log.v(TAG,"Finalizando configuración por comandos AT");
            } catch (IOException e) {
                Log.e(TAG,"setAtCommand IOExc " + e.getMessage());
            } catch (Exception e){
                Log.e(TAG,"setAtCommand " + e.getMessage());
            }
            //hexString = "";
            //lhs = 0;
            //start = -1;
            //end = -1;
            //lhs = 0;
            //jsonObject = new JSONObject();
            hearing = true;
            atCommandMode = false;

            Log.v(TAG,"EthernetATCommand hexStringcfg = " + hexStringcfg.replaceAll("\n",""));

            /*
            while(true){
                try{

                    Thread.sleep(1000);
                }catch (Exception e){
                    Log.d(TAG,"Error General Hilo " + nombreHilo + ": " + e.toString());
                    try{
                        Thread.sleep(1000);
                    }catch(Exception ex){

                    }

                }
            }
            */


        }

        public void start(String atcommand, String parameters, boolean set, boolean get, boolean restart){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            this.atcommand = atcommand;
            this.parameters = parameters;
            this.set = set;
            this.get = get;
            this.restart = restart;

            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }


    public void startEthernetReading(){
        ethernetReading = new EthernetReading("EthernetReading");
        ethernetReading.start();
    }

    public void startEthernetExecuting(){
        ethernetExecuting = new EthernetExecuting("EthernetExecuting");
        ethernetExecuting.start();
    }

    public void startEthernetFixing(){
        ethernetFixing = new EthernetFixing("EthernetFixing");
        ethernetFixing.start();
    }

    public int startEthernetATCommand(String atcommand, String parameters, boolean set, boolean get, boolean restart){
        EthernetATCommand ethernetATCommand = new EthernetATCommand("startEthernetATCommand");
        ethernetATCommand.start(atcommand, parameters, set, get, restart);

        return 0;
    }



    @Override
    public String toString() {
        return "MainEthernet{" +
                "TAG='" + TAG + '\'' +
                ", lenghtrawBytes=" + lenghtrawBytes +
                //", rawBytes=" + Arrays.toString(rawBytes) +
                ", hexString='" + hexString + '\'' +
                ", hexStrings='" + hexStrings + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", util=" + util +
                '}';
    }
}
