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
import com.tempus.proyectos.data.model.Servicios;
import com.tempus.proyectos.data.tables.TableServicios;

/**
 * Created by GURRUTIAM on 11/11/2016.
 */

public class QueriesServicios {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesServicios(Context context) {
        this.context = context;
    }

    public QueriesServicios open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(Servicios servicios){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableServicios.IdServicios, servicios.IdServicios);
        contentValues.put(TableServicios.Descripcion, servicios.Descripcion);
        contentValues.put(TableServicios.Host, servicios.Host);
        contentValues.put(TableServicios.Ip, servicios.Ip);
        contentValues.put(TableServicios.Instance, servicios.Instance);
        contentValues.put(TableServicios.Database, servicios.Database);
        contentValues.put(TableServicios.Port, servicios.Port);
        contentValues.put(TableServicios.User, servicios.User);
        contentValues.put(TableServicios.Pass, servicios.Pass);
        contentValues.put(TableServicios.IdAutorizacion, servicios.IdAutorizacion);
        contentValues.put(TableServicios.FechaHoraSinc, servicios.FechaHoraSinc);


        database.insert(TableServicios.TABLE_NAME, null, contentValues);

    }

    public List<Servicios> select(){

        Servicios servicios = new Servicios();
        List<Servicios> serviciosList =  new ArrayList<Servicios>();


        Cursor cursor = database.rawQuery(TableServicios.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                servicios = new Servicios();
                servicios.setIdServicios(cursor.getInt(cursor.getColumnIndex(TableServicios.IdServicios)));
                servicios.setDescripcion(cursor.getString(cursor.getColumnIndex(TableServicios.Descripcion)));
                servicios.setHost(cursor.getString(cursor.getColumnIndex(TableServicios.Host)));
                servicios.setIp(cursor.getString(cursor.getColumnIndex(TableServicios.Ip)));
                servicios.setInstance(cursor.getString(cursor.getColumnIndex(TableServicios.Instance)));
                servicios.setDatabase(cursor.getString(cursor.getColumnIndex(TableServicios.Database)));
                servicios.setPort(cursor.getString(cursor.getColumnIndex(TableServicios.Port)));
                servicios.setUser(cursor.getString(cursor.getColumnIndex(TableServicios.User)));
                servicios.setPass(cursor.getString(cursor.getColumnIndex(TableServicios.Pass)));
                servicios.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableServicios.IdAutorizacion)));
                servicios.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableServicios.FechaHoraSinc)));


                serviciosList.add(servicios);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return serviciosList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableServicios.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }

}
