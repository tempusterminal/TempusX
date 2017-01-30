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
import com.tempus.proyectos.data.model.TarjetaPersonalTipolectora;
import com.tempus.proyectos.data.tables.TableTarjetaPersonalTipolectora;

/**
 * Created by GURRUTIAM on 11/11/2016.
 */

public class QueriesTarjetaPersonalTipolectora {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTarjetaPersonalTipolectora(Context context) {
        this.context = context;
    }

    public QueriesTarjetaPersonalTipolectora open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(TarjetaPersonalTipolectora tarjetaPersonalTipolectora){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTarjetaPersonalTipolectora.Empresa, tarjetaPersonalTipolectora.Empresa);
        contentValues.put(TableTarjetaPersonalTipolectora.Codigo, tarjetaPersonalTipolectora.Codigo);
        contentValues.put(TableTarjetaPersonalTipolectora.IdTipoLect, tarjetaPersonalTipolectora.IdTipoLect);
        contentValues.put(TableTarjetaPersonalTipolectora.ValorTarjeta, tarjetaPersonalTipolectora.ValorTarjeta);
        contentValues.put(TableTarjetaPersonalTipolectora.FechaIni, tarjetaPersonalTipolectora.FechaIni);
        contentValues.put(TableTarjetaPersonalTipolectora.FechaFin, tarjetaPersonalTipolectora.FechaFin);
        contentValues.put(TableTarjetaPersonalTipolectora.FechaCreacion, tarjetaPersonalTipolectora.FechaCreacion);
        contentValues.put(TableTarjetaPersonalTipolectora.FechaAnulacion, tarjetaPersonalTipolectora.FechaAnulacion);
        contentValues.put(TableTarjetaPersonalTipolectora.IdAutorizacion, tarjetaPersonalTipolectora.IdAutorizacion);
        contentValues.put(TableTarjetaPersonalTipolectora.FechaHoraSinc, tarjetaPersonalTipolectora.FechaHoraSinc);


        database.insert(TableTarjetaPersonalTipolectora.TABLE_NAME, null, contentValues);

    }

    public List<TarjetaPersonalTipolectora> select(){

        TarjetaPersonalTipolectora tarjetaPersonalTipolectora = new TarjetaPersonalTipolectora();
        List<TarjetaPersonalTipolectora> tarjetaPersonalTipolectoraList =  new ArrayList<TarjetaPersonalTipolectora>();


        Cursor cursor = database.rawQuery(TableTarjetaPersonalTipolectora.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                tarjetaPersonalTipolectora = new TarjetaPersonalTipolectora();
                tarjetaPersonalTipolectora.setEmpresa(cursor.getString(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.Empresa)));
                tarjetaPersonalTipolectora.setCodigo(cursor.getString(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.Codigo)));
                tarjetaPersonalTipolectora.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.IdTipoLect)));
                tarjetaPersonalTipolectora.setValorTarjeta(cursor.getString(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.ValorTarjeta)));
                tarjetaPersonalTipolectora.setFechaIni(cursor.getString(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.FechaIni)));
                tarjetaPersonalTipolectora.setFechaFin(cursor.getString(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.FechaFin)));
                tarjetaPersonalTipolectora.setFechaCreacion(cursor.getString(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.FechaCreacion)));
                tarjetaPersonalTipolectora.setFechaAnulacion(cursor.getString(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.FechaAnulacion)));
                tarjetaPersonalTipolectora.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.IdAutorizacion)));
                tarjetaPersonalTipolectora.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTarjetaPersonalTipolectora.FechaHoraSinc)));


                tarjetaPersonalTipolectoraList.add(tarjetaPersonalTipolectora);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return tarjetaPersonalTipolectoraList;
    }


    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableTarjetaPersonalTipolectora.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }

}