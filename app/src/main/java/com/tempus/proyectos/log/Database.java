package com.tempus.proyectos.log;

import android.util.Log;

import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.LogTerminal;
import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;

import java.util.ArrayList;

/**
 * Created by gurrutiam on 03/01/2018.
 */

public class Database {
    private String TAG = "LG-DB";

    public static ArrayList<ArrayList<String>> informationDB =  new ArrayList<ArrayList<String>>();
    public static String sizeDB = "";
    public static String lastModifiedDB = "";
    public static boolean getActive;
    DBManager dbManager;
    QueriesLogTerminal queriesLogTerminal;
    private String logterminal = "";
    Fechahora fechahora;

    public Database() {

    }

    private class DatabaseReading extends Thread{
        private Thread hilo;
        private String nombreHilo;

        public DatabaseReading(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
            getActive = false;
            dbManager = new DBManager(ActivityPrincipal.context);
            queriesLogTerminal = new QueriesLogTerminal();
            fechahora = new Fechahora();
            String line = "";
            String s = "|";
            int sumCount = -1;
            int sumCountTemp = -1;

            try{
                informationDB = dbManager.getInformationDatabase();
                sizeDB = dbManager.getSizeDB();
                lastModifiedDB = dbManager.getLastModifiedDB();
            }catch (Exception e){
                Log.e(TAG,"informationDB, sizeDB, lastModifiedDB " + e.getMessage());
            }

            while(true){
                try{
                    Thread.sleep(1000);

                    // En modo activo cada segundo se realiza la consulta de información a la base de datos del terminal
                    // El modo activo se habilita al ingresar a Menu Sistema
                    if(getActive){
                        informationDB = dbManager.getInformationDatabase();
                        sizeDB = dbManager.getSizeDB();
                        lastModifiedDB = dbManager.getLastModifiedDB();
                    // Si no esta en modo activo, cada 5 minutos se realiza la consulta de información a la base de datos del terminal
                    }else{
                        if(Integer.valueOf(fechahora.getFechahora().substring(14,16)) % 5 == 0 && Integer.valueOf(fechahora.getFechahora().substring(17,19)) % 60 == 0){
                            informationDB = dbManager.getInformationDatabase();
                            sizeDB = dbManager.getSizeDB();
                            lastModifiedDB = dbManager.getLastModifiedDB();
                        }
                    }


                    // Se reinicia los acumuladores de información
                    line = "";
                    sumCountTemp = -1;

                    // Se acumula la información recorriendo el array de array string informationDB
                    // Este array contiene información de cada tabla, count, max y min fecha hora sync
                    for(int i = 0; i < informationDB.size(); i++){
                        // No se considera la tabla LOG_TERMINAL para la suma de registros debido a que esta tabla esta constantemente registrando eventos
                        // Si ell count de LOG_TERMINAL esta aumentando constantemente esto va a generar que se registre información a cada momento
                        if(!informationDB.get(i).get(0).equalsIgnoreCase("LOG_TERMINAL")){
                            sumCountTemp += Integer.valueOf(informationDB.get(i).get(1));
                        }

                        if(i >= informationDB.size() - 1){
                            line += informationDB.get(i).get(1);
                            break;
                        }else{
                            line += informationDB.get(i).get(1) + s;
                        }
                    }
                    logterminal = line + s + sizeDB.replace(" ","") + s + lastModifiedDB.replace(" ","");

                    Log.v(TAG,"queriesLogTerminal " + logterminal + " >>> " + sumCount + "(" + sumCountTemp + ")" + " " + "getAct(" + getActive + ")");
                    // Evaluar si hay aumento o disminución de la cantidad de registros en la base de datos del terminal
                    // En caso se detecte variación se registra el evento de la tabla LOG_TERMINAL
                    if(sumCountTemp != sumCount){
                        //se registra el evento de la tabla LOG_TERMINAL cada 5 segundos
                        if(Integer.valueOf(fechahora.getFechahora().substring(17,19)) % 5 == 0){
                            try{
                                Log.v(TAG,"queriesLogTerminal.insertLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG, logterminal , ""));
                            }catch (Exception e){
                                Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
                            }
                        }
                        sumCount = sumCountTemp;
                    }

                    // Cada 60 minutos se registra el estado de la base de datos del terminal en la tabla LOG_TERMINAL
                    if(Integer.valueOf(fechahora.getFechahora().substring(14,16)) % 60 == 0 && Integer.valueOf(fechahora.getFechahora().substring(17,19)) % 60 == 0){
                        try{
                            Log.v(TAG,"queriesLogTerminal.insertLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG, logterminal , ""));
                        }catch (Exception e){
                            Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
                        }
                    }

                }catch (Exception e){
                    Log.e(TAG,"Error General Hilo " + nombreHilo + ": " + e.getMessage());
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

    public void startDatabaseReading(){
        DatabaseReading batteryLifeReading = new DatabaseReading("DatabaseReading");
        batteryLifeReading.start();
    }
}
