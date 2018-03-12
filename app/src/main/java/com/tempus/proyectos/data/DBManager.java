package com.tempus.proyectos.data;

/**
 * Created by gurrutiam on 03/11/2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tempus.proyectos.data.model.Estados;
import com.tempus.proyectos.data.queries.QueriesAutorizaciones;
import com.tempus.proyectos.data.queries.QueriesBiometrias;
import com.tempus.proyectos.data.queries.QueriesLlamadas;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.data.queries.QueriesTerminalConfiguracion;
import com.tempus.proyectos.data.tables.TableEstados;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.InternalFile;
import com.tempus.proyectos.util.Utilities;

public class DBManager {
    private String TAG = "DA-DBM";
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

    public void copyDB(String DATABASE_PATH, String DATABASE_NAME){
        try{
            Fechahora fechahora = new Fechahora();
            Log.v(TAG,"preparando copia de " + Conexion.DB_NAME);
            InputStream inputStream = new FileInputStream(context.getDatabasePath(Conexion.DB_NAME));
            Log.v(TAG,"inputStream " + Conexion.DB_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME + "_" + fechahora.getFechahoraName() + ".db";
            Log.v(TAG,"directorio destino " + outFileName);
            OutputStream mOutputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            inputStream.close();
            /*
            try {
                String command = "sqlite3 " + outFileName + " \n" +
                        ".output " + outFileName.replace(".db",".sql") + " \n" +
                        ".dump " + " \n" +
                        ".exit " + " \n";
                Log.v(TAG,"command: \n" + command);
                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();

                Log.v(TAG,"runShellCommand ejecutado");
            } catch (InterruptedException e) {
                Log.e(TAG, "runShellCommand: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "runShellCommand: " + e.getMessage());
            }
            */
        }catch (IOException e) {
            Log.e(TAG,"copyDB IO " + e.getMessage());
        }catch (Exception e) {
            Log.e(TAG,"copyDB " + e.getMessage());
        }
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

    public String getSizeDB(){
        File fileBD = context.getDatabasePath("TEMPUSPLUS.db");
        String longitud = String.valueOf(fileBD.length());
        String longitudespacios = "";

        int vueltas = (int) longitud.length() / 3;
        int resto = longitud.length() % 3;
        for(int i = 0; i <= vueltas; i++){
            if(i == 0){
                longitudespacios += longitud.substring(i,resto + (i*3)) + " ";
            }else{
                longitudespacios += longitud.substring(resto + ((i-1)*3),resto + (i*3)) + " ";
            }
        }

        return longitudespacios;
    }

    public String getLastModifiedDB(){
        File fileBD = context.getDatabasePath("TEMPUSPLUS.db");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yy HH mm ss");
        Date lastModified = new Date(fileBD.lastModified());

        return simpleDateFormat.format(lastModified);
    }

    public ArrayList<ArrayList<String>> getInformationDatabase(){
        ArrayList<ArrayList<String>> information =  new ArrayList<ArrayList<String>>();

        this.open();
        Cursor cursor = database.rawQuery(INFORMATION_DATABASE, null);
        if(cursor.moveToNext()){
            do{
                ArrayList<String> info =  new ArrayList<String>();
                info.add(cursor.getString(0));
                info.add(cursor.getString(1));
                info.add(cursor.getString(2));
                info.add(cursor.getString(3));

                information.add(info);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return information;
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

    // Esta clase ejecuta el contenido de un archivo linea por linea
    // cada linea se ejecuta sobre la base de datos del terminal
    public class BackupBd extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private int type;
        private int delete;
        private String directory = "";
        private String filename = "";
        private Utilities utilities = new Utilities();

        public BackupBd(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);

            if(type == 1){
                directory = Environment.getExternalStoragePublicDirectory("") + "/tempus/config/"; // /storage/emulated/0/ + ... + filename
                filename = "config";
            }else if(type == 2){
                directory = Environment.getExternalStoragePublicDirectory("") + "/tempus/"; // /storage/emulated/0/ + ... + filename
                filename = "backupdb";
            }

            try{
                Log.v(TAG,"BackupBd inicio");

                if(delete == 1){
                    deletealltables();
                }

                open();

                FileReader fr = new FileReader(directory + utilities.getFilenameInDirectory(filename,null,directory));
                BufferedReader br = new BufferedReader(fr);

                String linea;
                int line = 1;
                while((linea = br.readLine()) != null){
                    try{
                        Log.v(TAG,"linea(" + (line++) + ")=" + linea);
                        execSQL(linea);
                    }catch (Exception e){
                        Log.e(TAG,"BackupBd " + e.getMessage());
                    }
                }

                fr.close();
                close();

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


    public static final String INFORMATION_DATABASE = "SELECT 'EMPRESAS' AS TABLA, COUNT(*) AS REGISTROS, MAX(EMPRESAS.FECHA_HORA_SINC) AS MAX, MIN(EMPRESAS.FECHA_HORA_SINC) AS MIN  FROM EMPRESAS " +
            "UNION " +
            "SELECT 'ESTADOS', COUNT(*), MAX(ESTADOS.FECHA_HORA_SINC), MIN(ESTADOS.FECHA_HORA_SINC) FROM ESTADOS " +
            "UNION " +
            "SELECT 'LLAMADAS', COUNT(*), MAX(LLAMADAS.FECHA_HORA_SINC), MIN(LLAMADAS.FECHA_HORA_SINC) FROM LLAMADAS " +
            "UNION " +
            "SELECT 'LOG_TERMINAL', COUNT(*), MAX(LOG_TERMINAL.FECHAHORA), MIN(LOG_TERMINAL.FECHAHORA) FROM LOG_TERMINAL " +
            "UNION " +
            "SELECT 'MARCACIONES', COUNT(*), MAX(MARCACIONES.FECHAHORA), MIN(MARCACIONES.FECHAHORA) FROM MARCACIONES " +
            "UNION " +
            "SELECT 'PARAMETERS', COUNT(*), MAX(PARAMETERS.FECHA_HORA_SINC), MIN(PARAMETERS.FECHA_HORA_SINC) FROM PARAMETERS " +
            "UNION " +
            "SELECT 'PER_TIPOLECT_TERM', COUNT(*), MAX(PER_TIPOLECT_TERM.FECHA_HORA_SINC), MIN(PER_TIPOLECT_TERM.FECHA_HORA_SINC) FROM PER_TIPOLECT_TERM " +
            "UNION " +
            "SELECT 'PERSONAL', COUNT(*), MAX(PERSONAL.FECHA_HORA_SINC), MIN(PERSONAL.FECHA_HORA_SINC) FROM PERSONAL " +
            "UNION " +
            "SELECT 'PERSONAL_TIPOLECTORA_BIOMETRIA', COUNT(*), MAX(PERSONAL_TIPOLECTORA_BIOMETRIA.FECHA_HORA_SINC), MIN(PERSONAL_TIPOLECTORA_BIOMETRIA.FECHA_HORA_SINC) FROM PERSONAL_TIPOLECTORA_BIOMETRIA " +
            "UNION " +
            "SELECT 'SERVICIOS', COUNT(*), MAX(SERVICIOS.FECHA_HORA_SINC), MIN(SERVICIOS.FECHA_HORA_SINC) FROM SERVICIOS " +
            "UNION " +
            "SELECT 'TARJETA_PERSONAL_TIPOLECTORA', COUNT(*), MAX(TARJETA_PERSONAL_TIPOLECTORA.FECHA_HORA_SINC), MIN(TARJETA_PERSONAL_TIPOLECTORA.FECHA_HORA_SINC) FROM TARJETA_PERSONAL_TIPOLECTORA " +
            "UNION " +
            "SELECT 'TERMINAL', COUNT(*), MAX(TERMINAL.FECHA_HORA_SINC), MIN(TERMINAL.FECHA_HORA_SINC) FROM TERMINAL " +
            "UNION " +
            "SELECT 'TERMINAL_TIPOLECT', COUNT(*), MAX(TERMINAL_TIPOLECT.FECHA_HORA_SINC), MIN(TERMINAL_TIPOLECT.FECHA_HORA_SINC) FROM TERMINAL_TIPOLECT " +
            "UNION " +
            "SELECT 'TIPO_DETALLE_BIOMETRIA', COUNT(*), MAX(TIPO_DETALLE_BIOMETRIA.FECHA_HORA_SINC), MIN(TIPO_DETALLE_BIOMETRIA.FECHA_HORA_SINC) FROM TIPO_DETALLE_BIOMETRIA " +
            "UNION " +
            "SELECT 'TIPO_LECTORA', COUNT(*), MAX(TIPO_LECTORA.FECHA_HORA_SINC), MIN(TIPO_LECTORA.FECHA_HORA_SINC) FROM TIPO_LECTORA;";




}
