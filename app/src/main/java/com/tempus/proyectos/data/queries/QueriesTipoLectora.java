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
import com.tempus.proyectos.data.model.TipoLectora;
import com.tempus.proyectos.data.tables.TableTipoLectora;

/**
 * Created by gurrutiam on 11/11/2016.
 */

public class QueriesTipoLectora {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTipoLectora(Context context) {
        this.context = context;
    }

    public QueriesTipoLectora open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public void insert(TipoLectora tipoLectora){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTipoLectora.IdTipoLect, tipoLectora.IdTipoLect);
        contentValues.put(TableTipoLectora.Descripcion, tipoLectora.Descripcion);
        contentValues.put(TableTipoLectora.Biometria, tipoLectora.Biometria);
        contentValues.put(TableTipoLectora.IdAutorizacion, tipoLectora.IdAutorizacion);
        contentValues.put(TableTipoLectora.FechaHoraSinc, tipoLectora.FechaHoraSinc);

        database.insert(TableTipoLectora.TABLE_NAME, null, contentValues);

    }

    public List<TipoLectora> select(){

        TipoLectora tipoLectora = new TipoLectora();
        List<TipoLectora> tipoLectoraList =  new ArrayList<TipoLectora>();

        Cursor cursor = database.rawQuery(TableTipoLectora.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                tipoLectora = new TipoLectora();
                tipoLectora.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TableTipoLectora.IdTipoLect)));
                tipoLectora.setDescripcion(cursor.getString(cursor.getColumnIndex(TableTipoLectora.Descripcion)));
                tipoLectora.setBiometria(cursor.getInt(cursor.getColumnIndex(TableTipoLectora.Biometria)));
                tipoLectora.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTipoLectora.IdAutorizacion)));
                tipoLectora.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTipoLectora.FechaHoraSinc)));

                tipoLectoraList.add(tipoLectora);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return tipoLectoraList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableTipoLectora.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }


}
