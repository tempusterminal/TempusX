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
import com.tempus.proyectos.data.model.Personal;
import com.tempus.proyectos.data.tables.TablePersonal;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class QueriesPersonal {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesPersonal(Context context) {
        this.context = context;
    }

    public QueriesPersonal open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public void insert(Personal personal){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TablePersonal.Empresa, personal.Empresa);
        contentValues.put(TablePersonal.Codigo, personal.Codigo);
        contentValues.put(TablePersonal.CentroDeCosto, personal.CentroDeCosto);
        contentValues.put(TablePersonal.ApellidoPaterno, personal.ApellidoPaterno);
        contentValues.put(TablePersonal.ApellidoMaterno, personal.ApellidoMaterno);
        contentValues.put(TablePersonal.Nombres, personal.Nombres);
        contentValues.put(TablePersonal.FechaDeNacimiento, personal.FechaDeNacimiento);
        contentValues.put(TablePersonal.FechaDeIngreso, personal.FechaDeIngreso);
        contentValues.put(TablePersonal.FechaDeCese, personal.FechaDeCese);
        contentValues.put(TablePersonal.Estado, personal.Estado);
        contentValues.put(TablePersonal.TipoHorario, personal.TipoHorario);
        contentValues.put(TablePersonal.Icono, personal.Icono);
        contentValues.put(TablePersonal.NroDocumento, personal.NroDocumento);
        contentValues.put(TablePersonal.FechaHoraSinc, personal.FechaHoraSinc);

        database.insert(TablePersonal.TABLE_NAME, null, contentValues);

    }

    public List<Personal> select(){

        Personal personal = new Personal();
        List<Personal> personalList =  new ArrayList<Personal>();


        Cursor cursor = database.rawQuery(TablePersonal.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                personal = new Personal();
                personal.setEmpresa(cursor.getString(cursor.getColumnIndex(TablePersonal.Empresa)));
                personal.setCodigo(cursor.getString(cursor.getColumnIndex(TablePersonal.Codigo)));
                personal.setCentroDeCosto(cursor.getString(cursor.getColumnIndex(TablePersonal.CentroDeCosto)));
                personal.setApellidoPaterno(cursor.getString(cursor.getColumnIndex(TablePersonal.ApellidoPaterno)));
                personal.setApellidoMaterno(cursor.getString(cursor.getColumnIndex(TablePersonal.ApellidoMaterno)));
                personal.setNombres(cursor.getString(cursor.getColumnIndex(TablePersonal.Nombres)));
                personal.setFechaDeNacimiento(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeNacimiento)));
                personal.setFechaDeIngreso(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeIngreso)));
                personal.setFechaDeCese(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeCese)));
                personal.setEstado(cursor.getString(cursor.getColumnIndex(TablePersonal.Estado)));
                personal.setTipoHorario(cursor.getString(cursor.getColumnIndex(TablePersonal.TipoHorario)));
                personal.setIcono(cursor.getString(cursor.getColumnIndex(TablePersonal.Icono)));
                personal.setNroDocumento(cursor.getString(cursor.getColumnIndex(TablePersonal.NroDocumento)));
                personal.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaHoraSinc)));
                personalList.add(personal);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return personalList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TablePersonal.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }

}
