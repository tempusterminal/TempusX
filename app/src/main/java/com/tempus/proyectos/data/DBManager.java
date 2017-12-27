package com.tempus.proyectos.data;

/**
 * Created by gurrutiam on 03/11/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.model.Estados;
import com.tempus.proyectos.data.queries.QueriesAutorizaciones;
import com.tempus.proyectos.data.queries.QueriesBiometrias;
import com.tempus.proyectos.data.queries.QueriesLlamadas;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.data.queries.QueriesTerminalConfiguracion;
import com.tempus.proyectos.data.tables.TableEstados;

public class DBManager {
    private static String TAG = "DA-DBM";
    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;


    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        conexion.close();
    }

    public void create(){
        this.open();
        conexion.onCreate(database);
        this.close();
    }

    public void listarTables(){
        this.open();
        String query = "select name from sqlite_master where type = 'table'";

        Cursor cursor = database.rawQuery(query, null);
        Log.d("Autorizaciones", "--------------------------------------------------------------------------------");
        Log.d("Autorizaciones", "LISTA DE TABLAS ----------------------------------------------------------------");
        if(cursor.moveToNext()){
            do{
                Log.d("Autorizaciones", cursor.getString(0));
            }while (cursor.moveToNext());
        }
        Log.d("Autorizaciones", "--------------------------------------------------------------------------------");

        cursor.close();
        this.close();
    }

    public void listarViews(){
        this.open();
        String query = "select name from sqlite_master where type = 'view'";

        Cursor cursor = database.rawQuery(query, null);
        Log.d("Autorizaciones", "--------------------------------------------------------------------------------");
        Log.d("Autorizaciones", "LISTA DE VISTAS ----------------------------------------------------------------");
        if(cursor.moveToNext()){
            do{
                Log.d("Autorizaciones", cursor.getString(0));
            }while (cursor.moveToNext());
        }
        Log.d("Autorizaciones", "--------------------------------------------------------------------------------");

        cursor.close();
        this.close();
    }

    public void deletealltables(){
        this.open();
        database.execSQL("BEGIN TRANSACTION;");
        database.execSQL("DELETE FROM ESTADOS;");
        database.execSQL("DELETE FROM EMPRESAS;");

        //database.execSQL("DELETE FROM TERMINAL;");
        //database.execSQL("INSERT INTO TERMINAL(IDTERMINAL) VALUES('1');");

        database.execSQL("DELETE FROM TERMINAL_TIPOLECT;");
        database.execSQL("DELETE FROM TIPO_LECTORA;");
        database.execSQL("DELETE FROM TIPO_DETALLE_BIOMETRIA;");
        database.execSQL("DELETE FROM PERSONAL;");
        database.execSQL("DELETE FROM PER_TIPOLECT_TERM;");
        database.execSQL("DELETE FROM TARJETA_PERSONAL_TIPOLECTORA;");
        database.execSQL("DELETE FROM PERSONAL_TIPOLECTORA_BIOMETRIA;");
        database.execSQL("COMMIT;");
        this.close();

    }

    public void all(String lote){

        String[] lotearray = lote.split(",");

        if(lotearray[0].equals("1")){
            // Crear la BD TEMPUSPLUS.db
            this.create();
        }

        if(lotearray[1].equals("1")){
            // Limpiar Registros de la BD
            this.deletealltables();
        }

        if(lotearray[2].equals("1")){
            // Listar las Tablas
            this.listarTables();
        }

        if(lotearray[3].equals("1")){
            // Crear las Vistas
            QueriesAutorizaciones queriesAutorizaciones = new QueriesAutorizaciones(context);
            queriesAutorizaciones.drop();
            queriesAutorizaciones.create();

            QueriesBiometrias queriesBiometrias = new QueriesBiometrias(context);
            queriesBiometrias.drop();
            queriesBiometrias.create();
        }

        if(lotearray[4].equals("1")){
            // Listar las Vistas
            this.listarViews();
        }

        if(lotearray[5].equals("1")){
            // Poblar Tablas Iniciales

            // Tabla Llamadas para sincronizar autorizaciones
            QueriesLlamadas queriesLlamadas = new QueriesLlamadas(context);
            queriesLlamadas.poblar();

            // Tabla Servicios para conectar a servidores externos
            QueriesServicios queriesServicios = new QueriesServicios(context);
            queriesServicios.poblar();

            // Tabla TerminalConfiguracion
            QueriesTerminalConfiguracion queriesTerminalConfiguracion = new QueriesTerminalConfiguracion(context);
            queriesTerminalConfiguracion.poblar();

            // Tabla Parameters
            QueriesParameters queriesParameters = new QueriesParameters(context);
            queriesParameters.poblar();

        }

        DBManager dbManager = new DBManager(context);
        if(dbManager.valexecSQL("SELECT COUNT(*) FROM TERMINAL").equals("0")){
            dbManager.execSQL("INSERT INTO TERMINAL(IDTERMINAL) VALUES (0)");
        }
        // //////////////////////////////////





    }

    public void execSQL(String sql){


        if(!database.isOpen()){
            this.open();
        }
        //database.beginTransaction();
        database.execSQL(sql);
        //database.endTransaction();
        this.close();
    }

    public String valexecSQL(String sql){
        String value = "";
        this.open();
        //database.beginTransaction();
        Cursor cursor = database.rawQuery(sql,null);
        //database.endTransaction();

        if(cursor.moveToNext()){
            value = cursor.getString(0);
        }while(cursor.moveToNext());

        cursor.close();
        this.close();

        return value;
    }

    public class BackupBd extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private int type;
        private int delete;
        private String filename = "";

        public BackupBd(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);

            if(type == 1){
                filename = "/tempus/config/config.sql";
            }else if(type == 2){
                filename = "/tempus/backupdb.sql";
            }

            try{
                Log.v(TAG,"BackupBd inicio");

                if(delete == 1){
                    deletealltables();
                }

                DBManager dbManager = new DBManager(context);
                dbManager.open();

                FileReader fr = new FileReader(Environment.getExternalStoragePublicDirectory("") + filename); // /storage/emulated/0/ + filename
                BufferedReader br = new BufferedReader(fr);

                String linea;
                while((linea = br.readLine()) != null){
                    try{
                        Log.v(TAG,"linea = " + linea);
                        dbManager.execSQL(linea);
                    }catch (Exception e){
                        Log.e(TAG,"BackupBd " + e.getMessage());
                    }

                }

                fr.close();

                dbManager.close();


                Log.v(TAG,"BackupBd Finalizando");
            } catch (Exception e){
                Log.e(TAG,"BackupBd " + e.getMessage());
            }
        }

        public void start(int type, int delete){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            this.type = type;
            this.delete = delete;
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }

    public void startBackupBd(int type, int delete){
        BackupBd backupBd = new BackupBd("startBackupBd");
        backupBd.start(type, delete);
    }









}
