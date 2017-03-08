package com.tempus.proyectos.data.process;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;

/**
 * Created by gurrutiam on 12/12/2016.
 */

public class ProcessMarcas extends Thread{

    private Thread hilo;
    private String nombreHilo;

    private QueriesMarcaciones queriesMarcaciones;
    Fechahora fechahora = new Fechahora();
    private Context context;


    public ProcessMarcas(String nombreHilo) {
        this.nombreHilo = nombreHilo;
        Log.d("Autorizaciones","Creando Hilo " + nombreHilo);
    }


    public void run(){
        Log.d("Autorizaciones","Ejecutando Hilo " + nombreHilo);

        while(true){
            try{
                /*
                queriesMarcaciones = new QueriesMarcaciones(context);
                Autorizaciones autorizaciones = queriesMarcaciones.GestionarMarcaciones("46388059","4",2,"001",fechahora.getFechahora());
                Log.d("Autorizaciones","Resultado de Busqueda de Autorizaciones: " + autorizaciones.toString());
                */

                ProcessSync processSync = new ProcessSync();
                processSync.Process(context);

                Thread.sleep(1000);
            }catch (SQLException e){
                Log.d("Autorizaciones","Error SQL Hilo " + nombreHilo + ": " + e.toString());
            }catch (InterruptedException e){
                Log.d("Autorizaciones","Error Interrupcion Hilo " + nombreHilo + ": " + e.toString());
            }catch (Exception e){
                Log.d("Autorizaciones","Error General Hilo " + nombreHilo + ": " + e.toString());
            }
        }

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
