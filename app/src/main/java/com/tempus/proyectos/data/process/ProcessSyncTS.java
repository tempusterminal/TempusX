package com.tempus.proyectos.data.process;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.DBManagerServidor;
import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.model.LogTerminal;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.model.PersonalTipolectoraBiometria;
import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.tempusx.ActivitySincronizacion;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Fechahora;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gurrutiam on 12/12/2016.
 */

public class ProcessSyncTS extends Thread{
    private String TAG = "DA-PR-TS";
    private Thread hilo;
    private String nombreHilo;
    private QueriesMarcaciones queriesMarcaciones;
    private QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;
    private QueriesLogTerminal queriesLogTerminal;
    private ProcessSync processSync;
    private Context context;
    private Activity activity;
    private Connection connection = null;


    public ProcessSyncTS(String nombreHilo) {
        this.nombreHilo = nombreHilo;
        Log.v(TAG,"Creando Hilo " + nombreHilo);
    }

    public void run(){
        Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(context);
        queriesMarcaciones = new QueriesMarcaciones(context);
        queriesLogTerminal = new QueriesLogTerminal();
        processSync = new ProcessSync();

        Connectivity connectivity = new Connectivity();


        while(true){
            try {

                //Log.v(TAG,"isWifiEnabled=" + wifiManager.isWifiEnabled() + " " + "existSIM=" + connectivity.existSIM(activity));
                // Verificar que el WIFI este habilitado o que exista SIM para poder realizar una consulta en la red
                if (wifiManager.isWifiEnabled() || connectivity.existSIM(activity)) {

                    // No se verifica si la conexion a servidor de DB o WS es exitosa
                    // Antes se verifica si existe data por enviar
                    // (Se evita la verificacion a DB o WS para no consumir datos de red)

                    // Enviar biometrias
                    try {
                        List<PersonalTipolectoraBiometria> personalTipolectoraBiometriaList = queriesPersonalTipolectoraBiometria.select_one_row();

                        if (personalTipolectoraBiometriaList.isEmpty()) {
                            Log.v(TAG, "Sin biometrias por pasar");
                        } else {
                            Log.v(TAG, "Biometria a sincronizar: " + personalTipolectoraBiometriaList.get(0).toString());
                            try {
                                if (processSync.syncBiometrias(personalTipolectoraBiometriaList.get(0)) > 0) {
                                    queriesPersonalTipolectoraBiometria.ActualizarBiometriaEnviadaServidor(personalTipolectoraBiometriaList.get(0).getIndiceBiometria(), personalTipolectoraBiometriaList.get(0).getIdTipoDetaBio());
                                } else {
                                    Log.v(TAG, "No se completo la sincronización de biometrias");
                                }
                            } catch (SQLException e) {
                                Log.e(TAG, "ProcessSyncTS.run SQLException SQLServer: " + e.toString());
                            } catch (Exception e) {
                                Log.e(TAG, "(1)ProcessSyncTS.run Exception: " + e.toString());
                            }
                        }

                        Thread.sleep(3000);


                    } catch (SQLException e) {
                        Log.e(TAG, "ProcessSyncTS.run SQLException SQLServer Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (InterruptedException e) {
                        Log.e(TAG, "ProcessSyncTS.run InterruptedException Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (ExceptionInInitializerError e) {
                        Log.e(TAG, "ProcessSyncTS.run ExceptionInInitializerError Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (Exception e) {
                        Log.e(TAG, "ProcessSyncTS.run Exception Hilo " + nombreHilo + ": " + e.getMessage());
                    }


                    // Enviar Marcaciones
                    try {
                        List<Marcaciones> marcacionesList = queriesMarcaciones.select_one_row();

                        if (marcacionesList.isEmpty()) {
                            Log.v(TAG, "Sin marcaciones por pasar");
                        } else {
                            Log.v(TAG, "Marcacion a sincronizar: " + marcacionesList.get(0).toString());

                            try {
                                if (processSync.syncMarcaciones(marcacionesList.get(0)) > 0) {
                                    queriesMarcaciones.ActualizarSincronizado(marcacionesList.get(0),1);
                                } else {
                                    Log.v(TAG, "No se completo la sincronización de marcaciones");
                                }
                            } catch (SQLException e) {
                                Log.e(TAG, "ProcessSyncTS.run SQLException SQLServer: " + e.toString());
                            } catch (Exception e) {
                                Log.e(TAG, "ProcessSyncTS.run Exception: " + e.toString());
                            }

                        }
                        // //////////////////////////////////

                        Thread.sleep(5000);

                    } catch (SQLException e) {
                        Log.e(TAG, "ProcessSyncTS.run SQLException SQLServer Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (InterruptedException e) {
                        Log.e(TAG, "ProcessSyncTS.run InterruptedException Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (ExceptionInInitializerError e) {
                        Log.e(TAG, "ProcessSyncTS.run ExceptionInInitializerError Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (Exception e) {
                        Log.e(TAG, "ProcessSyncTS.run Exception Hilo " + nombreHilo + ": " + e.getMessage());
                    }


                    // Deshabilitar temporalmente sincro GoogleDrive 27/12/2017
                    /*
                    try {
                        // -----------------------------------------------------------------------------
                        // -----------------------------------------------------------------------------
                        // -----------------------------------------------------------------------------
                        // Sincronizacion por Google Drive
                        // Sincronizacion por Google Drive

                        List<Marcaciones> marcacionesList = queriesMarcaciones.select_one_row();

                        if(marcacionesList.isEmpty()){
                            Log.v(TAG,"Sin marcaciones por pasar");
                        }else{
                            Log.v(TAG,"Marcacion a sincronizar: " + marcacionesList.get(0).toString());

                            try{
                                if(processSync.syncMarcacionesWsG(marcacionesList.get(0)) > 0){
                                    Log.v(TAG,"Marcación sincronizada");
                                    //queriesMarcaciones = new QueriesMarcaciones(context);
                                    queriesMarcaciones.ActualizarSincronizado(marcacionesList.get(0),1);
                                }else{
                                    Log.v(TAG,"No se completo la sincronización de marcaciones");
                                }
                            }catch(Exception e){
                                Log.e(TAG,"syncMarcacionesWsG " + e.toString());
                            }
                        }

                        Thread.sleep(5000);

                    } catch (SQLException e) {
                        Log.e(TAG, "ProcessSyncTS.run SQLException SQLServer Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (InterruptedException e) {
                        Log.e(TAG, "ProcessSyncTS.run InterruptedException Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (ExceptionInInitializerError e) {
                        Log.e(TAG, "ProcessSyncTS.run ExceptionInInitializerError Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (Exception e) {
                        Log.e(TAG, "ProcessSyncTS.run Exception Hilo " + nombreHilo + ": " + e.getMessage());
                    }
                    */



                    // Enviar Log Terminal
                    try {
                        List<LogTerminal> logTerminalList = queriesLogTerminal.select_one_row();

                        if (logTerminalList.isEmpty()) {
                            Log.v(TAG,"Sin logTerminal por pasar");
                        } else {
                            Log.v(TAG,"logTerminal a sincronizar: " + logTerminalList.get(0).toString());

                            try {
                                if (processSync.syncLogTerminal(logTerminalList.get(0)) > 0) {
                                    queriesLogTerminal.ActualizarSincronizado(logTerminalList.get(0), 1);
                                } else {
                                    Log.v(TAG, "No se completo la sincronización de logTerminal");
                                }
                            } catch (SQLException e) {
                                Log.e(TAG, "syncLogTerminal SQLException " + e.getMessage());
                            } catch (Exception e) {
                                Log.e(TAG, "syncLogTerminal " + e.getMessage());
                            }

                        }
                        // //////////////////////////////////
                        Thread.sleep(1000);

                    } catch (SQLException e) {
                        Log.e(TAG, "logTerminalList SQLException " + nombreHilo + ": " + e.getMessage());
                    } catch (InterruptedException e) {
                        Log.e(TAG, "logTerminalList InterruptedException " + nombreHilo + ": " + e.getMessage());
                    } catch (ExceptionInInitializerError e) {
                        Log.e(TAG, "logTerminalList Hilo " + nombreHilo + ": " + e.getMessage());
                    } catch (Exception e) {
                        Log.e(TAG, "logTerminalList Hilo " + nombreHilo + ": " + e.getMessage());
                    }



                }else{
                    Thread.sleep(5000);
                    Log.v(TAG,"No existen medios para realizar consultas por red, intente habilitar WIFI o habilitar/insertar una SIM");
                }

            }catch (Exception e){
                Log.e(TAG,"" + e.getMessage());
                try{
                    Thread.sleep(5000);
                }catch (Exception ex){

                }
            }

        }
    }

    public void start(Context context, Activity activity){
        Log.v(TAG,"Iniciando Hilo " + nombreHilo);
        if(hilo == null){
            hilo = new Thread(nombreHilo);
            super.start();
        }
        this.context = context;
        this.activity = activity;
    }




}
