package com.tempus.proyectos.data;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import android.os.StrictMode;

import com.tempus.proyectos.data.model.Servicios;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.tempusx.ActivityPrincipal;


/**
 * Created by gurrutiam on 07/12/2016.
 */

public class ConexionServidor {

    private static String TAG = "DA-CS";

    private static ConexionServidor instance = null;
    // private static final String user = "SA";
    // private static final String pass = "D1R3S42013";
    // private static final String port = "58723";
    // private static final String database = "TEMPUS_T9";
    // private static final String host = "129.21.22.136"; // 172.20.1.119 //172.20.1.100

    private static String user = "";
    private static String pass = "";
    private static String port = "";
    private static String database = "";
    private static String ip = "";
    private static String host = "";

    private static String url = "";//"jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databaseName=" + database + ";" + "user=" + user + ";" + "password=" + pass + ";";
    private static Connection connection = null;

    public static String logserverDBLevel01 = "";

    public ConexionServidor() {
        //Iniciar Parametros

        QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
        List<Servicios> serviciosList =  new ArrayList<Servicios>();
        serviciosList = queriesServicios.BuscarServicios("SERVIDOR_DATOS_PRINCIPAL");

        user = serviciosList.get(0).getUser();
        pass = serviciosList.get(0).getPass();
        port = serviciosList.get(0).getPort();
        database = serviciosList.get(0).getDatabase();
        ip = serviciosList.get(0).getIp();
        host = serviciosList.get(0).getHost();

        url = "jdbc:jtds:sqlserver://" + host + ":" + port + ";" + "databaseName=" + database + ";" + "user=" + user + ";" + "password=" + pass + ";" + "";
        // //////////////////////////////////

        //Log.v(TAG,"ConexionServidor reinicio de parámetros");

    }

    public static ConexionServidor getInstance(){
        try{
            if(instance == null){
                instance = new ConexionServidor();
            }
        }catch (Exception e){
            Log.e(TAG,"getInstance " + e.getMessage());
        }

        return instance;
    }

    public Connection getConnection(){
        try {
            if (connection == null) {
                connection = conectar();
            }
        }catch (Exception e){
            Log.e(TAG,"getConnection " + e.getMessage());
        }
        return connection;
    }

    public Connection conectar(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            //Log.d("Autorizaciones","Intentando Conexion MSSQLSERVER...");
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url);

            logserverDBLevel01 = "Conexion Servidor de datos OK " + String.valueOf(connection);
            Log.v(TAG,"Conexion MSSQLSERVER OK");
        }catch (ClassNotFoundException e) {
            Log.v(TAG,"conectar ClassNotFoundException " + e.getMessage());
            logserverDBLevel01 = e.getMessage();
        }catch (java.sql.SQLException e) {
            Log.v(TAG,"conectar SQLException " + e.getMessage());
            logserverDBLevel01 = e.getMessage();
        }catch (Exception e){
            Log.v(TAG,"conectar " + e.getStackTrace() + " - " + e.toString() + " - " + e.getMessage());
            logserverDBLevel01 = e.getMessage();
        }

        return connection;

    }

    public boolean testConexionServidor(){
        Connection conn = null;
        PreparedStatement prep;
        ResultSet resu;
        String sql = "SELECT @@SERVERNAME AS SERVERNAME";

        if(conn == null){
            conn = ConexionServidor.getInstance().getConnection();
        }

        Log.v(TAG,"testConexionServidor Iniciando Test");

        try{
            if(conn.isClosed()){
                //Log.d("Autorizaciones","Intentando restablecer conexion: " + connection.toString());
                conn = ConexionServidor.this.conectar();
            }
            prep = conn.prepareStatement(sql);
            resu = prep.executeQuery();
            return resu.next();
        }catch(Exception e){
            Log.e(TAG,"testConexionServidor Sql Error: " + e.getMessage());
            return false;
        }finally {
            try{
                conn.close();
            }catch (Exception e){
            }
            Log.v(TAG,"testConexionServidor Finalizando Test");
        }
    }


    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }

    public static String getPort() {
        return port;
    }

    public static String getDatabase() {
        return database;
    }

    public static String getHost() {
        return host;
    }

}
