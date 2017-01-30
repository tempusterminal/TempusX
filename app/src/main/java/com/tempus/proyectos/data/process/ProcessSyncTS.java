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
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.util.Fechahora;

/**
 * Created by gurrutiam on 12/12/2016.
 */

public class ProcessSyncTS extends Thread{
    private Thread hilo;
    private String nombreHilo;
    private QueriesMarcaciones queriesMarcaciones;
    Marcaciones marcaciones = new Marcaciones();
    Fechahora fechahora = new Fechahora();
    private Context context;



    public ProcessSyncTS(String nombreHilo) {
        this.nombreHilo = nombreHilo;
        Log.d("Autorizaciones","Creando Hilo " + nombreHilo);
    }

    public void run(){
        Log.d("Autorizaciones","Ejecutando Hilo " + nombreHilo);

        while(true){
            try{
                queriesMarcaciones = new QueriesMarcaciones(context);
                List<Marcaciones> marcacionesList = queriesMarcaciones.select_one_row();
                ProcessSync processSync = new ProcessSync();

                if(marcacionesList.isEmpty()){
                    Log.d("Autorizaciones","Sin marcaciones por pasar");
                }else{
                    Log.d("Autorizaciones","Marcacion a sincronizar: " + marcacionesList.get(0).toString());
                    try{
                        if(processSync.syncMarcaciones(marcacionesList.get(0)) > 0){
                            queriesMarcaciones = new QueriesMarcaciones(context);
                            queriesMarcaciones.ActualizarSincronizado(marcacionesList.get(0),1);
                        }else{
                            Log.d("Autorizaciones","No se completo la sincronización de marcaciones");
                        }
                    }catch(SQLException e){
                        Log.d("Autorizaciones","ProcessSyncTS.run SQLException SQLServer: " + e.toString());
                    }catch(Exception e){
                        Log.d("Autorizaciones","ProcessSyncTS.run Exception: " + e.toString());
                    }
                }

                Thread.sleep(5000);

            }catch (SQLException e){
                Log.d("Autorizaciones","ProcessSyncTS.run SQLException SQLServer Hilo " + nombreHilo + ": " + e.getMessage());
            }catch (InterruptedException e){
                Log.d("Autorizaciones","ProcessSyncTS.run InterruptedException Hilo " + nombreHilo + ": " + e.getMessage());
            }catch (ExceptionInInitializerError e){
                Log.d("Autorizaciones","ProcessSyncTS.run ExceptionInInitializerError Hilo " + nombreHilo + ": " + e.getMessage());
            }catch (Exception e){
                Log.d("Autorizaciones","ProcessSyncTS.run Exception Hilo " + nombreHilo + ": " + e.getMessage());
            }
        }
    }

    public void start(Context context){
        Log.d("Autorizaciones","Iniciando Hilo " + nombreHilo);
        if(hilo == null){
            hilo = new Thread(nombreHilo);
            super.start();
        }
        this.context = context;
    }

}
