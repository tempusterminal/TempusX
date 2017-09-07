package com.tempus.proyectos.data.process;

import android.content.Context;
import android.database.SQLException;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.data.DBManagerServidor;
import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.model.PersonalTipolectoraBiometria;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;

/**
 * Created by gurrutiam on 12/12/2016.
 */

public class ProcessSyncTS extends Thread{
    private String TAG = "DA-PR-TS";
    private Thread hilo;
    private String nombreHilo;
    private QueriesMarcaciones queriesMarcaciones;
    private QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;
    private Context context;
    private Connection connection = null;
    private ConexionServidor conexionServidor;



    public ProcessSyncTS(String nombreHilo) {
        this.nombreHilo = nombreHilo;
        Log.d("Autorizaciones","Creando Hilo " + nombreHilo);
    }

    public void run(){
        Log.d("Autorizaciones","Ejecutando Hilo " + nombreHilo);
        conexionServidor = new ConexionServidor();
        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(context);
        queriesMarcaciones = new QueriesMarcaciones(context);

        while(true){
            try{
                if(connection == null){
                    connection = conexionServidor.getInstance().getConnection();
                }else{
                    connection = conexionServidor.getInstance().getConnection();
                }

                if(connection != null){

                    try{
                        //queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(context);
                        List<PersonalTipolectoraBiometria> personalTipolectoraBiometriaList = queriesPersonalTipolectoraBiometria.select_one_row();
                        ProcessSync processSync = new ProcessSync();

                        if(personalTipolectoraBiometriaList.isEmpty()){
                            Log.v(TAG,"Sin biometrias por pasar");
                        }else{
                            Log.v(TAG,"Biometria a sincronizar: " + personalTipolectoraBiometriaList.get(0).toString());
                            try{
                                if(processSync.syncBiometrias(personalTipolectoraBiometriaList.get(0)) > 0){
                                    //queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(context);
                                    queriesPersonalTipolectoraBiometria.ActualizarBiometriaEnviadaServidor(personalTipolectoraBiometriaList.get(0).getIndiceBiometria(),personalTipolectoraBiometriaList.get(0).getIdTipoDetaBio());
                                }else{
                                    Log.v(TAG,"No se completo la sincronización de biometrias");
                                }
                            }catch(SQLException e){
                                Log.e(TAG,"ProcessSyncTS.run SQLException SQLServer: " + e.toString());
                            }catch(Exception e){
                                Log.e(TAG,"(1)ProcessSyncTS.run Exception: " + e.toString());
                            }
                        }

                        Thread.sleep(3000);


                    }catch (SQLException e){
                        Log.e(TAG,"ProcessSyncTS.run SQLException SQLServer Hilo " + nombreHilo + ": " + e.getMessage());
                    }catch (InterruptedException e){
                        Log.e(TAG,"ProcessSyncTS.run InterruptedException Hilo " + nombreHilo + ": " + e.getMessage());
                    }catch (ExceptionInInitializerError e){
                        Log.e(TAG,"ProcessSyncTS.run ExceptionInInitializerError Hilo " + nombreHilo + ": " + e.getMessage());
                    }catch (Exception e){
                        Log.e(TAG,"ProcessSyncTS.run Exception Hilo " + nombreHilo + ": " + e.getMessage());
                    }


                    try{
                        //queriesMarcaciones = new QueriesMarcaciones(context);
                        List<Marcaciones> marcacionesList = queriesMarcaciones.select_one_row();
                        ProcessSync processSync = new ProcessSync();

                        if(marcacionesList.isEmpty()){
                            Log.v(TAG,"Sin marcaciones por pasar");
                        }else{
                            Log.v(TAG,"Marcacion a sincronizar: " + marcacionesList.get(0).toString());
                            try{
                                if(processSync.syncMarcaciones(marcacionesList.get(0)) > 0){
                                    //queriesMarcaciones = new QueriesMarcaciones(context);
                                    queriesMarcaciones.ActualizarSincronizado(marcacionesList.get(0),1);
                                }else{
                                    Log.v(TAG,"No se completo la sincronización de marcaciones");
                                }
                            }catch(SQLException e){
                                Log.e(TAG,"ProcessSyncTS.run SQLException SQLServer: " + e.toString());
                            }catch(Exception e){
                                Log.e(TAG,"ProcessSyncTS.run Exception: " + e.toString());
                            }
                        }
                        // //////////////////////////////////

                        Thread.sleep(5000);

                    }catch (SQLException e){
                        Log.e(TAG,"ProcessSyncTS.run SQLException SQLServer Hilo " + nombreHilo + ": " + e.getMessage());
                    }catch (InterruptedException e){
                        Log.e(TAG,"ProcessSyncTS.run InterruptedException Hilo " + nombreHilo + ": " + e.getMessage());
                    }catch (ExceptionInInitializerError e){
                        Log.e(TAG,"ProcessSyncTS.run ExceptionInInitializerError Hilo " + nombreHilo + ": " + e.getMessage());
                    }catch (Exception e){
                        Log.e(TAG,"ProcessSyncTS.run Exception Hilo " + nombreHilo + ": " + e.getMessage());
                    }

                    connection.close();

                }else{
                    Log.v(TAG,"conexionServidor " + connection);
                    try{
                        Thread.sleep(5000);
                    }catch (Exception ex){

                    }
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

    public void start(Context context){
        Log.v(TAG,"Iniciando Hilo " + nombreHilo);
        if(hilo == null){
            hilo = new Thread(nombreHilo);
            super.start();
        }
        this.context = context;
    }

}
