package com.tempus.proyectos.data.process;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.util.Log;

import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Fechahora;

import java.sql.Connection;

/**
 * Created by gurrutiam on 12/12/2016.
 */

public class ProcessSyncST extends Thread{
    private String TAG = "DA-PR-ST";
    private Thread hilo;
    private String nombreHilo;

    private Context context;
    private Activity activity;
    private Connection connection = null;
    private ConexionServidor conexionServidor;


    public ProcessSyncST(String nombreHilo) {
        this.nombreHilo = nombreHilo;
        Log.v(TAG,"Creando Hilo " + nombreHilo);
    }


    public void run(){
        Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
        conexionServidor = new ConexionServidor();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Connectivity connectivity = new Connectivity();
        ProcessSync processSync = new ProcessSync();

        while(true){

            try {

                //Log.v(TAG,"isWifiEnabled=" + wifiManager.isWifiEnabled() + " " + "existSIM=" + connectivity.existSIM(activity));
                // Verificar que el WIFI este habilitado o que exista SIM para poder realizar una consulta en la red
                if (wifiManager.isWifiEnabled() || connectivity.existSIM(activity)) {

                    if (connection == null) {
                        Log.v(TAG,"Abriendo conexion, conexion anterior " + connection);
                        connection = conexionServidor.getInstance().getConnection();
                    } else {
                        Log.v(TAG,"Volviendo a abriendo conexion, conexion anterior " + String.valueOf(connection));
                        connection = conexionServidor.getInstance().getConnection();
                    }

                    if (connection != null) {
                        Log.v(TAG,"Conexion exitosa " + String.valueOf(connection));

                        try{
                            //Log.v(TAG,"Hilo " + nombreHilo + " - Ciclo");

                            if(ActivityPrincipal.ctrlThreadSyncAutorizacionesEnabled){
                                //Log.v(TAG,"Hilo " + nombreHilo + " - - > ctrlThread Habilitado");
                                // Llamadas para poblar autorizaciones
                                processSync.ProcessLlamadas(context);
                            }

                            Thread.sleep(5000);
                        }catch (SQLException e){
                            Log.e(TAG,"Error SQL Hilo " + nombreHilo + ": " + e.toString());
                        }catch (InterruptedException e){
                            Log.e(TAG,"Error Interrupcion Hilo " + nombreHilo + ": " + e.toString());
                        }catch (Exception e){
                            Log.e(TAG,"Error General Hilo " + nombreHilo + ": " + e.toString());
                        }

                    } else {
                        Log.v(TAG, "conexionServidor " + connection);
                        try {
                            Thread.sleep(5000);
                        } catch (Exception ex) {

                        }
                    }

                }else{
                    Thread.sleep(5000);
                    Log.v(TAG,"No existen medios para realizar consultas por red, intente habilitar WIFI o habilitar/insertar una SIM");;
                }


                // CONSULTA PARA INICIAR REPLICA
                // Esta consulta se hace sin verificar la conexion WIFI o SIM debido a que es un proceso interno entre la DB de terminal y el huellero
                // Se verifica que el terminal sea TIPO_TERMINAL = 2, es decir, este habilitado para biometría
                if(ActivityPrincipal.TIPO_TERMINAL == 2){
                    processSync.syncSuprema(context);
                }else{
                    Log.v(TAG,"Terminal NO habilitado para biometría, replica de biometrías no se iniciará");;
                }

            }catch (Exception e){
                Log.e(TAG,"" + e.getMessage());
                try{
                    Thread.sleep(5000);
                }catch (Exception ex){

                }
            }

        }

        // //////////////////////////////////
        //Log.d("Autorizaciones","Fin Hilo");
    }


    public void start(Context context, Activity activity){
        //Looper.prepare();
        Log.v(TAG,"Iniciando Hilo " + nombreHilo);
        if(hilo == null){
            hilo = new Thread(nombreHilo);
            super.start();
        }
        this.context = context;
        this.activity = activity;
    }



}
