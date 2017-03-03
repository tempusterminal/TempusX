package com.tempus.proyectos.data;

import android.database.SQLException;
import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import android.os.StrictMode;


/**
 * Created by gurrutiam on 07/12/2016.
 */

public class ConexionServidor {
    private static ConexionServidor instance = null;
    // private static final String user = "SA";
    // private static final String pass = "D1R3S42013";
    // private static final String port = "58723";
    // private static final String database = "TEMPUS_T9";
    // private static final String host = "129.21.22.136"; // 172.20.1.119 //172.20.1.100

    private static final String user = "SA";
    private static final String pass = "tempus+123";
    private static final String port = "49758";
    private static final String database = "CROVISA_T10";
    private static final String host = "192.168.1.103"; // 172.20.1.119 //172.20.1.100

    private static final String url = "jdbc:jtds:sqlserver://" + host + ":" + port + ";" + "databaseName=" + database + ";" + "user=" + user + ";" + "password=" + pass + ";";
    private static Connection connection = null;

    public ConexionServidor() {
    }

    public static ConexionServidor getInstance(){
        try{
            if(instance == null){
                instance = new ConexionServidor();
            }
        }catch (Exception e){
            Log.d("Autorizaciones","Error : " + e.getMessage());
        }

        return instance;
    }

    public Connection getConnection(){
        try {
            if (connection == null) {
                connection = conectar();
            }
        }catch (Exception e){
            Log.d("Autorizaciones","Error : " + e.getMessage());
        }
        return connection;
    }

    public Connection conectar(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            Log.d("Autorizaciones","Intentando Conexion MSSQLSERVER...");
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url);
            Log.d("Autorizaciones","Conexion MSSQLSERVER OK");
        }catch (ClassNotFoundException e) {
            Log.d("Autorizaciones","Class not Found: " + e.getMessage());
        }catch (java.sql.SQLException e) {
            Log.d("Autorizaciones","Sql: " + e.getMessage());
        }catch (Exception e){
            Log.d("Autorizaciones","Error general: " + e.getStackTrace() + " - " + e.toString() + " - " + e.getMessage());
        }

        return connection;

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
