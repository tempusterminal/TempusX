package com.tempus.proyectos.data.queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Terminal;
import com.tempus.proyectos.data.tables.TableTerminal;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;

/**
 * Created by GURRUTIAM on 11/11/2016.
 */

public class QueriesTerminal {

    private String TAG = "DQ-QUETER";

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTerminal(Context context) {
        this.context = context;
    }

    public QueriesTerminal open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public void insert(Terminal terminal){


        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTerminal.Idterminal, terminal.Idterminal);
        contentValues.put(TableTerminal.Descripcion, terminal.Descripcion);
        contentValues.put(TableTerminal.Habilitado, terminal.Habilitado);
        contentValues.put(TableTerminal.Mac, terminal.Mac);
        contentValues.put(TableTerminal.Modelo, terminal.Modelo);
        contentValues.put(TableTerminal.Firmware, terminal.Firmware);
        contentValues.put(TableTerminal.Software, terminal.Software);
        contentValues.put(TableTerminal.Hardware, terminal.Hardware);
        contentValues.put(TableTerminal.Chasis, terminal.Chasis);
        contentValues.put(TableTerminal.Ups, terminal.Ups);
        contentValues.put(TableTerminal.NumCel, terminal.NumCel);
        contentValues.put(TableTerminal.IdAutorizacion, terminal.IdAutorizacion);
        contentValues.put(TableTerminal.FechaHoraSinc, terminal.FechaHoraSinc);

        database.insert(TableTerminal.TABLE_NAME, null, contentValues);

    }

    public List<Terminal> select(){

        Terminal terminal = new Terminal();
        List<Terminal> terminalList =  new ArrayList<Terminal>();

        Cursor cursor = database.rawQuery(TableTerminal.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                terminal = new Terminal();
                terminal.setIdterminal(cursor.getString(cursor.getColumnIndex(TableTerminal.Idterminal)));
                terminal.setDescripcion(cursor.getString(cursor.getColumnIndex(TableTerminal.Descripcion)));
                terminal.setHabilitado(cursor.getInt(cursor.getColumnIndex(TableTerminal.Habilitado)));
                terminal.setMac(cursor.getString(cursor.getColumnIndex(TableTerminal.Mac)));
                terminal.setModelo(cursor.getString(cursor.getColumnIndex(TableTerminal.Modelo)));
                terminal.setFirmware(cursor.getString(cursor.getColumnIndex(TableTerminal.Firmware)));
                terminal.setSoftware(cursor.getString(cursor.getColumnIndex(TableTerminal.Software)));
                terminal.setHardware(cursor.getString(cursor.getColumnIndex(TableTerminal.Hardware)));
                terminal.setChasis(cursor.getString(cursor.getColumnIndex(TableTerminal.Chasis)));
                terminal.setUps(cursor.getString(cursor.getColumnIndex(TableTerminal.Ups)));
                terminal.setNumCel(cursor.getString(cursor.getColumnIndex(TableTerminal.NumCel)));
                terminal.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTerminal.IdAutorizacion)));
                terminal.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTerminal.FechaHoraSinc)));

                terminalList.add(terminal);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return terminalList;
    }


    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableTerminal.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }


    public int ActualizarIdterminal(String Idterminal){

        this.open();
        Fechahora fechahora =  new Fechahora();
        DBManager dbManager = new DBManager(context);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableTerminal.Idterminal,Idterminal);
        contentValues.put(TableTerminal.FechaHoraSinc,fechahora.getFechahora());

        try{
            if(!ActivityPrincipal.controlFlagSyncAutorizaciones){
                database.update(TableTerminal.TABLE_NAME,contentValues,null,null);
                // Set idterminal con el nuevo Idterminal
                ActivityPrincipal.idTerminal = Idterminal;
                // Limpiar los registros de las tablas
                dbManager.all("0,1,0,0,0,0");

                Log.v(TAG,"Idterminal Actualizado a " + Idterminal);
            }
            return 1;
        }catch(Exception e){
            Log.e(TAG,"QueriesTerminal.ActualizarIdterminal Error " + e.getMessage());
            return 0;
        }finally {
            this.close();
        }

    }

    private class ActualizarIdterminal extends Thread{
        private Thread hilo;
        private String nombreHilo;

        private String Idterminal;

        public ActualizarIdterminal(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);

            try{

                while(ActivityPrincipal.controlFlagSyncAutorizaciones){
                    Thread.sleep(250);
                }

                ActualizarIdterminal(Idterminal);

                Log.v(TAG,"Fin de ejecución Hilo");
            }catch (Exception e){
                Log.e(TAG,"Error General Hilo " + nombreHilo + ": " + e.getMessage());
                try{
                    Thread.sleep(1000);
                }catch(Exception ex){

                }
            }
        }

        public void start(String Idterminal){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            this.Idterminal = Idterminal;
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }

    public void startActualizarIdterminal(String Idterminal){
        ActualizarIdterminal actualizarIdterminal = new ActualizarIdterminal("ActualizarIdterminal");
        actualizarIdterminal.start(Idterminal);
    }




}
