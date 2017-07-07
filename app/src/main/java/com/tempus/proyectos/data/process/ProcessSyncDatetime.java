package com.tempus.proyectos.data.process;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;

/**
 * Created by gurrutiam on 28/03/2017.
 */

public class ProcessSyncDatetime extends Thread{
    private Thread hilo;
    private String nombreHilo;

    private Context context;

    public ProcessSyncDatetime(String nombreHilo) {
        this.nombreHilo = nombreHilo;
        Log.d("Autorizaciones","Creando Hilo " + nombreHilo);
    }


    public void run(){
        Log.d("Autorizaciones","Ejecutando Hilo " + nombreHilo);

        while(true){
            try{
                ProcessSync processSync = new ProcessSync();
                processSync.syncFechahora();
                Thread.sleep(1000);
            }catch (SQLException e){
                Log.d("Autorizaciones","Error SQL Hilo " + nombreHilo + ": " + e.toString());
                try{
                    Thread.sleep(1000);
                }catch(Exception ex){

                }
            }catch (InterruptedException e){
                Log.d("Autorizaciones","Error Interrupcion Hilo " + nombreHilo + ": " + e.toString());
                try{
                    Thread.sleep(1000);
                }catch(Exception ex){

                }
            }catch (Exception e){
                Log.d("Autorizaciones","Error General Hilo " + nombreHilo + ": " + e.toString());
                try{
                    Thread.sleep(1000);
                }catch(Exception ex){

                }
            }

        }
        // //////////////////////////////////
        //Log.d("Autorizaciones","Fin Hilo");
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
