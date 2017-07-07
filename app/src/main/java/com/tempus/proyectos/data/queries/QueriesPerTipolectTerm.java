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
import com.tempus.proyectos.data.model.PerTipolectTerm;
import com.tempus.proyectos.data.tables.TablePerTipolectTerm;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class QueriesPerTipolectTerm {
    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesPerTipolectTerm(Context context) {
        this.context = context;
    }

    public QueriesPerTipolectTerm open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public void insert(PerTipolectTerm perTipolectTerm){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TablePerTipolectTerm.Empresa, perTipolectTerm.Empresa);
        contentValues.put(TablePerTipolectTerm.Codigo, perTipolectTerm.Codigo);
        contentValues.put(TablePerTipolectTerm.IdTerminalTipolect, perTipolectTerm.IdTerminalTipolect);
        contentValues.put(TablePerTipolectTerm.IdTipoLect, perTipolectTerm.IdTipoLect);
        contentValues.put(TablePerTipolectTerm.Flag, perTipolectTerm.Flag);
        contentValues.put(TablePerTipolectTerm.IdAutorizacion, perTipolectTerm.IdAutorizacion);
        contentValues.put(TablePerTipolectTerm.FechaHoraSinc, perTipolectTerm.FechaHoraSinc);

        database.insert(TablePerTipolectTerm.TABLE_NAME, null, contentValues);

    }

    public List<PerTipolectTerm> select(){

        PerTipolectTerm perTipolectTerm = new PerTipolectTerm();
        List<PerTipolectTerm> perTipolectTermList =  new ArrayList<PerTipolectTerm>();


        Cursor cursor = database.rawQuery(TablePerTipolectTerm.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                perTipolectTerm = new PerTipolectTerm();
                perTipolectTerm.setEmpresa(cursor.getString(cursor.getColumnIndex(TablePerTipolectTerm.Empresa)));
                perTipolectTerm.setCodigo(cursor.getString(cursor.getColumnIndex(TablePerTipolectTerm.Codigo)));
                perTipolectTerm.setIdTerminalTipolect(cursor.getInt(cursor.getColumnIndex(TablePerTipolectTerm.IdTerminalTipolect)));
                perTipolectTerm.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TablePerTipolectTerm.IdTipoLect)));
                perTipolectTerm.setFlag(cursor.getInt(cursor.getColumnIndex(TablePerTipolectTerm.Flag)));
                perTipolectTerm.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TablePerTipolectTerm.IdAutorizacion)));
                perTipolectTerm.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TablePerTipolectTerm.FechaHoraSinc)));

                perTipolectTermList.add(perTipolectTerm);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return perTipolectTermList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TablePerTipolectTerm.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }
    
}
