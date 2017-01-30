package com.tempus.proyectos.data;

/**
 * Created by gurrutiam on 12/12/2016.
 */
import android.database.SQLException;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.util.Fechahora;

public class DBManagerServidor {

    private Connection connection = null;

    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;


    public DBManagerServidor() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void select(){
        String sql = "SELECT COUNT(*) FROM TEMPUS.MARCACION_EXTERNO";
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Log.d("Autorizaciones","Cantidad de registros: " + resultSet.getString(1));
            }

        }catch (java.sql.SQLException e) {
            Log.d("Autorizaciones","SQLException: " + e.getMessage());
        }
    }


    public void PersonalNuevo(String fh){

        Fechahora fechahora = new Fechahora();

        String sql = "SELECT * FROM TEMPUS.PERSONAL_EXTERNO " +
                "WHERE FECHA_HORA_SINC > '" + fechahora.getFechahoraSqlServer(fh) + "' " +
                "ORDER BY FECHA_HORA_SINC ASC";

        Log.d("Autorizaciones",sql);

    }



    public void Sync(){

        DBManagerServidor dbManagerServidor = new DBManagerServidor();


        String sql = "SELECT COUNT(*) FROM TEMPUS.MARCACION_EXTERNO";
        try{
            preparedStatement = connection.prepareCall("");
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Log.d("Autorizaciones","Cantidad de registros: " + resultSet.getString(1));
            }

        }catch (java.sql.SQLException e) {
            Log.d("Autorizaciones","SQLException: " + e.getMessage());
        }

    }










}
