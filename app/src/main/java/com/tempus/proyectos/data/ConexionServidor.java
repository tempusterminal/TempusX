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
    private static final String user = "SA";
    private static final String pass = "1234567";
    private static final String port = "1433";
    private static final String database = "EDITORA";
    private static final String host = "192.168.0.68";
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

}
