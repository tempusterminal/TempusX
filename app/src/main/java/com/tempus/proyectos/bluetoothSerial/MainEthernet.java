package com.tempus.proyectos.bluetoothSerial;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.tempus.proyectos.data.model.Llamadas;
import com.tempus.proyectos.data.process.ProcessSync;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            //Log.v(TAG,"Esperando btSocketEthernet.getInputStream().read(rawBytes)");
            lenghtrawBytes = btSocketEthernet.getInputStream().read(rawBytes);
            //Log.v(TAG,"lenght -> " + lenghtrawBytes );
            String hs = "";
            hs = new String(util.hexStringToByteArray(util.byteArrayToHexString(rawBytes).substring(0,lenghtrawBytes*2)), StandardCharsets.UTF_8);

            Log.v(TAG,"LLEGO ASCCI (" + hs.length() + ") -> " + hs );
            approximationToFix = 0;
            hexString += hs;
            //Log.v(TAG,"LLEGO ASCCI+ -> " + hexString );
            //Log.v(TAG,"hexString.length() -> " + hexString.length() );
            //validateHexString(hexString);

            String vHexString = findHexString(hexString,"{","}");
            //String vHexString = findHexString(hexString,"[","]");

            if(vHexString.length()>0){
                Log.v(TAG,"validateHexString: " + vHexString);
                vHexString = validateJson(vHexString);
                Log.v(TAG,"validateJson: " + vHexString);

                if(vHexString.length()>0){

                    if(syncData == 1){
                        Log.v(TAG,"jsonObject: " + keyvalueJsonObject(vHexString,"CADENA",0).substring(0,20) + " ... " + keyvalueJsonObject(vHexString,"CADENA",0).substring(keyvalueJsonObject(vHexString,"CADENA",0).length()-20,keyvalueJsonObject(vHexString,"CADENA",0).length()));
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
                        jsonObject = new JSONObject(vHexString);
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

            }

        }catch(Exception e){
            Log.e(TAG,"prepareHexString: " + e.getMessage());
            approximationToFix = 0;
            hearing = true;
            if(e.getMessage().contains("bt socket closed")){
                ActivityPrincipal.STATUS_ETHERNET = false;
            }
            try{
                Thread.sleep(7000);
            }catch (Exception ex){

            }
        }

        return jsonArray.toString();

    }

    public String findHexString(String hs, String cs, String ce){
        try{
            if(start == -1){
                start = hs.indexOf(cs);
            }
            if(start != -1){
                // Si viene json con longitud y token 0 en la cadena: 386a2{EMPRESA:""...}0
                if(start > 0){

                    //Log.v(TAG,"Longitud a recibir: " + hs.substring(0,start));
                    //Log.v(TAG,"Longitud a recibir: " + Integer.parseInt(hs.substring(0,start).trim(),16));
                    if(hs.length() > Integer.parseInt(hs.substring(0,start).trim(),16)){
                        for(int i = 0; i < 15; i++){
                            try{
                                //Log.v(TAG,"Caracter final a evaluar: " + hs.substring((hs.length() - i),(hs.length() - i) + 1));
                                if(ce.equalsIgnoreCase(hs.substring((hs.length() - i),(hs.length() - i) + 1))){
                                    end = hs.length() - i;
                                    Log.v(TAG,"Caracter final " + end + " <" + hs.substring(end).trim() + ">");
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
                Log.v(TAG,"hexStrings resumen: " + hexStrings.substring(0,15) + " ... " + hexStrings.substring(hexStrings.length()-15,hexStrings.length()));
                hexString = "";
                start = -1;
                end = -1;
            }else{
                hexStrings = "";
            }
        }catch(Exception e){
            hexString = "";
            start = -1;
            end = -1;
            approximationToFix = 0;
            hearing = true;
            Log.e(TAG,"validateHexString: " + e.getMessage());
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

            //IMPLEMENTAR EL ENLACE A ProcessSync()
            //processSync.insertUpdateData(aavaluesiu,"PERSONAL","EMPRESA,CODIGO","APELLIDO_PATERNO,APELLIDO_MATERNO,NOMBRES,FECHA_DE_NACIMIENTO,FECHA_DE_INGRESO,FECHA_DE_CESE,ESTADO,TIPO_HORARIO,ICONO,NRO_DOCUMENTO,FECHA_HORA_SINC",ActivityPrincipal.context);

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
                avaluesiu.add(i,value);
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

    public void callsws(int mode, ArrayList<String> da, String opcion){
        // 0 = recibir parametros para la siguiente llamada
        // 1 = recibir parametros para insercion en base de datos

        //typeJson = object or array
        if(mode == 0){
            syncData = 0;

        }else if (mode == 1){
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

        if(opcion.equals("TERMINAL")){
            //data = "OPCION=TERMINAL&EMPRESA=TEMPUS_WS_T10&USER=TEMPUS&PASS=TEMPUSSCA&IP=0.0.0.0&MAC=00-00-00-00-00-00&HOSTNAME=TERMINAL_T10" + "";
            data = "OPCION=TERMINAL&" + da.get(0) + "&" + da.get(1) + "&" + da.get(2) + "&" + da.get(3) + "&" + da.get(4) + "&" + da.get(5) + "";
            Log.v(TAG,"Streamdata " + data);
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),data);
        }else if(opcion.equals("EXEC_DATA")){
            //data = "OPCION=EXEC_DATA&DATA={\"session\":15966,\"llamada\":\"551_EXEC_L2\",\"parametros\":\"'SYNC_PERSONAL_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,05/01/2014 15:15:56.069'\"}";
            data = "OPCION=EXEC_DATA&" + da.get(0) + "";
            Log.v(TAG,"Streamdata " + data);
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),data);
        }else if(opcion.equals("SUBSTR_FILE")){
            //data = "OPCION=SUBSTR_FILE&FILENAME=" + fn + "&START=" + i*200000 + "&LENGTH=" + (i+1)*200000 + "";
            data = "OPCION=SUBSTR_FILE&" + da.get(0) + "&" + da.get(1) + "&" + da.get(2) + "";
            Log.v(TAG,"Streamdata " + data);
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),data);
        }else if(opcion.equals("FIX")){
            writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"");

        }
    }


    public class EthernetExecuting extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private int lfn;
        private int lcut = 100000;


        public EthernetExecuting(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creating " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Executing " + nombreHilo);

            jsonAuthenticate = new JSONObject();
            ArrayList<String> dataArray = new ArrayList<String>();

            while(true){
                try{

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

                        if(llamadasList.size() == 0){
                            llamadasList = processSync.getLlamadas(ActivityPrincipal.context);
                            Log.v(TAG,"llamadasList.size() " + llamadasList.size());
                            for(int i = 0; i <llamadasList.size(); i++ ){
                                Log.v(TAG,"llamadasList " + i + " - " + llamadasList.get(i));
                            }
                        }
                        llamadasList = processSync.getLlamadas(ActivityPrincipal.context);
                        if(llamadasList.size() == 0){
                            Log.v(TAG," Sin llamadas por ejecutar " + String.valueOf(llamadasList.size()));
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

                                // 3. Llamada para extraer data delñarchivo json creado en el Servidor

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
                            }
                        }
                    }



                    /*
                    for(int i = 0; i < (lfn/200000); i++){
                        try{
                            Log.v(TAG,"EthernetExecuting: " + "hearing2 " + String.valueOf(hearing));
                            //callsws(0);
                            //writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=SUBSTR_FILE&FILENAME=" + fn + "&START=" + i*200000 + "&LENGTH=" + (i+1)*200000 + "");
                            Log.v(TAG,"EthernetExecuting: " + "hexStrings final parte_" + (i+1) + " -> " + hexStrings);
                            while(!hearing){
                                Log.v(TAG,"EthernetExecuting: " + "hearing3 " + String.valueOf(hearing));
                                Thread.sleep(2000);
                            }
                            Thread.sleep(1000);
                        }catch(Exception e){
                            Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                            try{
                                Thread.sleep(1000);
                            }catch(Exception ex){

                            }
                        }
                    }
                    */


                }catch (Exception e){
                    Log.e(TAG,"EthernetExecuting: " + e.getMessage());
                    try{
                        Thread.sleep(3000);
                    }catch (Exception ex){

                    }
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
                    prepareHexString(ActivityPrincipal.btSocketEthernet);
                    //Thread.sleep(1000);
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


    public void startEthernetReading(){
        EthernetReading ethernetReading = new EthernetReading("EthernetReading");
        ethernetReading.start();
    }

    public void startEthernetExecuting(){
        EthernetExecuting ethernetExecuting = new EthernetExecuting("EthernetExecuting");
        ethernetExecuting.start();
    }

    public void startEthernetFixing(){
        EthernetFixing ethernetFixing = new EthernetFixing("EthernetFixing");
        ethernetFixing.start();
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
