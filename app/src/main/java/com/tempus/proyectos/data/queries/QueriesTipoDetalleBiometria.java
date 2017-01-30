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
import com.tempus.proyectos.data.model.TipoDetalleBiometria;
import com.tempus.proyectos.data.tables.TableTipoDetalleBiometria;

/**
 * Created by gurrutiam on 11/11/2016.
 */

public class QueriesTipoDetalleBiometria {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTipoDetalleBiometria(Context context) {
        this.context = context;
    }

    public QueriesTipoDetalleBiometria open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(TipoDetalleBiometria tipoDetalleBiometria){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTipoDetalleBiometria.IdTipoDetaBio, tipoDetalleBiometria.IdTipoDetaBio);
        contentValues.put(TableTipoDetalleBiometria.Descripcion, tipoDetalleBiometria.Descripcion);
        contentValues.put(TableTipoDetalleBiometria.IdTipoLect, tipoDetalleBiometria.IdTipoLect);
        contentValues.put(TableTipoDetalleBiometria.IdAutorizacion, tipoDetalleBiometria.IdAutorizacion);
        contentValues.put(TableTipoDetalleBiometria.FechaHoraSinc, tipoDetalleBiometria.FechaHoraSinc);

        database.insert(TableTipoDetalleBiometria.TABLE_NAME, null, contentValues);

    }

    public List<TipoDetalleBiometria> select(){

        TipoDetalleBiometria tipoDetalleBiometria = new TipoDetalleBiometria();
        List<TipoDetalleBiometria> tipoDetalleBiometriaList =  new ArrayList<TipoDetalleBiometria>();

        Cursor cursor = database.rawQuery(TableTipoDetalleBiometria.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                tipoDetalleBiometria = new TipoDetalleBiometria();
                tipoDetalleBiometria.setIdTipoDetaBio(cursor.getInt(cursor.getColumnIndex(TableTipoDetalleBiometria.IdTipoDetaBio)));
                tipoDetalleBiometria.setDescripcion(cursor.getString(cursor.getColumnIndex(TableTipoDetalleBiometria.Descripcion)));
                tipoDetalleBiometria.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TableTipoDetalleBiometria.IdTipoLect)));
                tipoDetalleBiometria.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTipoDetalleBiometria.IdAutorizacion)));
                tipoDetalleBiometria.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTipoDetalleBiometria.FechaHoraSinc)));

                tipoDetalleBiometriaList.add(tipoDetalleBiometria);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return tipoDetalleBiometriaList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableTipoDetalleBiometria.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }



}
