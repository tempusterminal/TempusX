package com.tempus.proyectos.data.process;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.tempus.proyectos.bluetoothSerial.MainArduino;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurrutiam on 11/08/2017.
 */

public class ProcessSyncUSB {

    String TAG = "DA-PR-USB";

    OTGOnOff otgOnOff;
    Utilities utilities = new Utilities();

    private String filename;
    private static String ruta;
    private static String rutaotg;
    private File file;
    private Fechahora fechahora;
    public static boolean otgmode = false;
    public static String msg;
    public static long lfilenamemarcaciones;

    QueriesLogTerminal queriesLogTerminal;


    private void writeToArduino(OutputStream out, String opcion) {

        //244F415841 0013 42 3131313131303031
        String cabecera = "244F415841";

        String longitud = "0013";

        String mensaje = "";
        String checksum = "0000";
        String cola = "41";

        //cabecera = "$0AXA";
        //longitud = "";
        //checksum = "";
        //cola = "A";

        switch (opcion){
            //case "AndroidPWRoff":
            //    longitud = "0013";
            //    mensaje = "42" + "31313131 3030 3130 3131 303030303030";
            //    break;
            //case "AndroidPWRon":
            //    longitud = "0013";
            //    mensaje = "42" + "31313131 3030 3131 3131 303030303030";
            //    break;
            case "OTGenable":
                longitud = "0013";
                //mensaje = "42" + "31313131 3131 3130 3130 303030303030";
                mensaje = "42" + "4e4e4e01 4e4e 4e4e 4e4e 014e4e4e4e4e";
                //mensaje = "B" + "1111111011000000";
                break;
            case "OTGdisable":
                longitud = "0013";
                mensaje = "42" + "31313131 3130 3131 3130 303030303030";
                //mensaje = "B" + "1111101111000000";
                break;

            default:
                break;
        }

        //longitud = "";

        mensaje = mensaje.replace(" ","");
        checksum = utilities.getChecksum(cabecera + longitud + mensaje,4);
        String tramaFinal = cabecera + longitud + mensaje + checksum + cola;

        Log.v(TAG,"Salio - " + opcion + " -> " + tramaFinal);

        Log.v(TAG, "Write Activado");
        try {
            byte[] a = tramaFinal.getBytes();
            out.write(utilities.hexStringToByteArray(tramaFinal));
            Log.v(TAG, "Write Finalizado");
        } catch (Exception e) {
            Log.e(TAG,"writeToArduino: " + e.getMessage());
        }
    }


    private class OTGOnOff extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private ArrayList<String> marcaciones = new ArrayList<String>();
        private long sizeFileMarcaciones = -1;
        private long freeSpaceUsbOtg = -1;


        private String parametersnamesvalues = "";

        public OTGOnOff(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            //Looper.prepare();
            lfilenamemarcaciones = 0;
            otgmode = true;
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
            fechahora = new Fechahora();
            queriesLogTerminal = new QueriesLogTerminal();
            List<Marcaciones> marcacionesList =  new ArrayList<Marcaciones>();

            // Variables para log terminal
            // statusSyncMarcaciones es una variable para determinar el estado final de la sincronizacion de marcaciones al USB
            // statusSyncMarcaciones = -1 no determinado / por defecto
            // statusSyncMarcaciones = 1 sincronizacion satisfactoria
            // statusSyncMarcaciones = 0 problemas en la sincronizacion
            // statusSyncMarcaciones = 2 problemas en el directorio USB
            int statusSyncMarcaciones = -1;

            try {
                Log.v(TAG,"Iniciando");
                Thread.sleep(2000);

                //writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"AndroidPWRoff");
                //Thread.sleep(2000);


                try{
                    QueriesMarcaciones queriesMarcaciones = new QueriesMarcaciones(ActivityPrincipal.context);
                    marcacionesList = queriesMarcaciones.select_nosync();
                }catch (Exception e){
                    Log.e(TAG,"select_nosync " + e.getMessage());
                }

                Log.v(TAG,"marcacionesList(" + marcacionesList.size() + ") =" + marcacionesList.toString());

                if(marcacionesList.size() > 0){
                    for(int i = 0; i < marcacionesList.size(); i++){
                        parametersnamesvalues = "pEMPRESA," + marcacionesList.get(i).getEmpresa() +
                            ";pCODIGO," + marcacionesList.get(i).getCodigo() +
                            ";pFECHAHORA," + fechahora.getFechahoraSqlServer(marcacionesList.get(i).getFechahora()) +
                            ";pNUMERO_TARJETA," + marcacionesList.get(i).getValorTarjeta() +
                            ";pHORATXT," + marcacionesList.get(i).getHoraTxt() +
                            ";pENT_SAL," + marcacionesList.get(i).getEntSal() +
                            ";pFLAG," + marcacionesList.get(i).getFlag() +
                            ";pFECHA," + fechahora.getFechahoraSqlServer(marcacionesList.get(i).getFecha()) +
                            ";pHORA," + fechahora.getFechahoraSqlServer(marcacionesList.get(i).getHora()) +
                            ";pIDTERMINAL," + marcacionesList.get(i).getIdterminal() +
                            ";pIDLECTORA," + marcacionesList.get(i).getIdTipoLect() +
                            ";pFLG_ACTIVIDAD," + marcacionesList.get(i).getFlgActividad() +
                            ";pIDUSUARIO," + marcacionesList.get(i).getIdUsuario() +
                            ";pTMP_LISTAR," + marcacionesList.get(i).getTmpListar() +
                            ";pDATOS," + marcacionesList.get(i).getDatos();

                        marcaciones.add("{\"llamada\":\"" + "EXEC_L2" + "\",\"parametros\":\"'" + "SYNC_MARCACIONES_TX" + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}");
                    }

                    Log.v(TAG,"marcaciones(" + marcaciones.size() + ") = " + marcaciones.toString());

                    writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"OTGenable");
                    Thread.sleep(7000);
                    if(ActivityPrincipal.versionSDK >= 23){
                        Process process = Runtime.getRuntime().exec("ls /mnt/m_internal_storage -l");

                        InputStream inputStream = process.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        String line = null;
                        while (true){
                            line = bufferedReader.readLine();
                            if(line != null){
                                Log.v(TAG,"" + line);
                                if(line.contains("m_internal_storage")){
                                    Log.v(TAG,">>>" + line.substring(line.indexOf("/storage")) + "<<<");
                                    rutaotg = line.substring(line.indexOf("/storage")) + "/";
                                }
                            }else{
                                break;
                            }
                        }
                        process.waitFor();

                        ruta = Environment.getExternalStoragePublicDirectory("") + "/"; // /storage/emulated/0/

                    }else{
                        ruta = Environment.getExternalStoragePublicDirectory("") + "/"; // /storage/emulated/0/
                        rutaotg = ruta.replace("emulated","usbotg").replace("0","");
                    }


                    // Calcular el peso del archivo de marcaciones
                    sizeFileMarcaciones = marcaciones.toString().length();
                    // Calcular el espacio libre en el USB
                    freeSpaceUsbOtg = freeMemory(rutaotg);
                    Log.v(TAG,"sizeFileMarcaciones " + sizeFileMarcaciones);
                    Log.v(TAG,"freeSpaceUsbOtg " + freeSpaceUsbOtg);

                    // Verificar que exista espacio suficiente para guardar el archivo en el USB
                    if(freeSpaceUsbOtg > sizeFileMarcaciones){
                        Log.v(TAG,"rutaotg " + rutaotg);
                        filename = "M_" + "ID_" + ActivityPrincipal.idTerminal + "_" + fechahora.getFechahoraName() + ".txt";
                        msg = saveFile(rutaotg,filename,marcaciones);

                        // Mantener OTG activo mientras se crea el archivo
                        // Tiempo de espera para crear el archivo
                        long templfm = getSizeFile(rutaotg,filename);
                        for(int i = 1; i <= 7; i++){
                            writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"OTGenable");
                            if(templfm == -1){
                                Log.v(TAG,"Creando archivo, continuar habilitando OTG");
                            }else if(templfm >= 0) {
                                Log.v(TAG, "Archivo creado");
                                lfilenamemarcaciones = getSizeFile(rutaotg,filename);
                                break;
                            }else if(i == 7){
                                Log.v(TAG, "Tiempo de espera agotado (Crear archivo)");
                            }else if(msg.equalsIgnoreCase("Archivo Guardado")){
                                Log.v(TAG, "Archivo Guardado");
                                break;
                            }
                            Log.v(TAG,"lfilenamemarcaciones vs templfm (creating) " + lfilenamemarcaciones + "/" + templfm);
                            Thread.sleep(1000);
                            templfm = getSizeFile(rutaotg,filename);
                        }

                        // Mantener OTG activo mientras se escribe el archivo
                        // Tiempo de espera para escribir en el archivo
                        for(int i = 1; i <= 7; i++){
                            writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"OTGenable");
                            if(lfilenamemarcaciones > 0) {
                                Log.v(TAG, "Escribiendo archivo");
                                lfilenamemarcaciones = getSizeFile(rutaotg,filename);
                                Thread.sleep(1000);
                                templfm = getSizeFile(rutaotg,filename);
                                break;
                            }else if(lfilenamemarcaciones == 0){
                                Log.v(TAG, "Intentando escribir archivo");
                            }else if(i == 7){
                                Log.v(TAG, "Tiempo de espera agotado (Escribir archivo)");
                            }else if(msg.equalsIgnoreCase("Archivo Guardado")){
                                Log.v(TAG, "Archivo Guardado");
                                break;
                            }
                            Log.v(TAG,"lfilenamemarcaciones vs templfm (writing) " + lfilenamemarcaciones + "/" + templfm);
                            Thread.sleep(1000);
                            lfilenamemarcaciones = getSizeFile(rutaotg,filename);
                        }


                        while(lfilenamemarcaciones < templfm){
                            writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"OTGenable");
                            lfilenamemarcaciones = getSizeFile(rutaotg,filename);
                            Thread.sleep(3000);
                            templfm = getSizeFile(rutaotg,filename);
                            if(lfilenamemarcaciones == templfm){
                                Thread.sleep(10000);
                                break;
                            }else if(msg.equalsIgnoreCase("Archivo Guardado")){
                                statusSyncMarcaciones = 1;
                                Log.v(TAG, "Archivo Guardado");
                                Thread.sleep(10000);
                                break;
                            }
                            Log.v(TAG,"lfilenamemarcaciones vs templfm (writing more) " + lfilenamemarcaciones + "/" + templfm);
                        }

                        if(!msg.equalsIgnoreCase("Archivo Guardado")){
                            statusSyncMarcaciones = 0;
                        }

                    }else{
                        statusSyncMarcaciones = 0;
                        msg = "Falta espacio USB";
                    }

                    //writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"OTGdisable");
                    Thread.sleep(2000);

                }else if(marcacionesList.size() == 0){
                    msg = "Sin Marcaciones";
                }

                // TERMINAR LOG_TERMINAL
                queriesLogTerminal.insertLogTerminal(TAG,"USBM" + "|" +
                                statusSyncMarcaciones + "|" +
                                marcacionesList.size() + "|" +
                                sizeFileMarcaciones  + "|" +
                                freeSpaceUsbOtg
                        ,ActivityPrincipal.UserSession);

                otgmode = false;
                Log.v(TAG,"Finalizando");
            } catch (Exception e){
                otgmode = false;
                Log.e(TAG,"OTGOnOff " + e.getMessage());
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


    public void startOTGOnOff(){
        otgOnOff = new OTGOnOff("otgOnOff");
        otgOnOff.start();
    }

    private String saveFile(String ruta, String filename,ArrayList<String> contenido){
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        String msg = "";
        // Añadir el subdirectorio /marcaciones
        ruta += "/marcaciones/";
        File file = new File(ruta);
        try{

            // Solicitar permisos para escribir en storage

            Log.v(TAG,"Verficación de directorio " + file.mkdir());

            fileOutputStream = new FileOutputStream(ruta + filename + "");


            /*
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write("[");
            for(int i = 0; i < contenido.size(); i++){
                outputStreamWriter.write(contenido.get(i));
                if(i < contenido.size() - 1 ){
                    outputStreamWriter.write(",");
                }
            }
            outputStreamWriter.write("]");

            outputStreamWriter.flush();
            outputStreamWriter.close();
            */

            fileOutputStream.write("[".getBytes());
            for(int i = 0; i < contenido.size(); i++){
                fileOutputStream.write(contenido.get(i).getBytes());
                if(i < contenido.size() - 1 ){
                    fileOutputStream.write(",".getBytes());
                }
            }
            fileOutputStream.write("[".getBytes());
            fileOutputStream.close();


            //Toast.makeText(ActivityPrincipal.context, "Guardado", Toast.LENGTH_SHORT).show();
            msg = "Archivo Guardado";
        }catch (FileNotFoundException e){
            Log.e(TAG,"saveBCK FileNotFoundException: " + e.getMessage());
            msg = e.getMessage();
            msg = "Error USB";
        }catch (IOException e){
            Log.e(TAG,"saveBCK IOException: " + e.getMessage());
            msg = e.getMessage();
            msg = "Error al Guardar";
        }catch (Exception e){
            Log.e(TAG,"saveBCK Exception: " + e.getMessage());
            msg = e.getMessage();
            msg = "Error General";
        }
        return msg;
    }

    private String completezeros(String text, int len){
        for(int i = text.length(); i < len; i++){
            text = "0" + text;
        }
        return text;
    }


    private long getSizeFile(String ruta, String filename){
        long len = 0;
        try{
            File file = new File(ruta + filename);
            len = file.length();
            //Log.v(TAG,"getSizeFile len " + len);
        }catch(Exception e){
            len = -1;
            Log.e(TAG,"getSizeFile " + e.getMessage());
        }

        return len;

    }


    private int totalMemory(String ruta){
        StatFs statFs = new StatFs(ruta);
        int Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
        return Total;
    }

    private long freeMemory(String ruta){
        long free = -1;
        try{
            StatFs statFs = new StatFs(ruta);
            free  = statFs.getFreeBytes();
        }catch (Exception e){
            Log.e(TAG,"freeMemory " + e.getMessage());
        }
        return free;
    }

    private int busyMemory(String ruta){
        StatFs statFs = new StatFs(ruta);
        int Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
        int Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
        int Busy  = Total - Free;
        return Busy;
    }







}
