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
import com.tempus.proyectos.data.model.Estados;
import com.tempus.proyectos.data.tables.TableEstados;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class QueriesEstados {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesEstados(Context context) {
        this.context = context;
    }

    public QueriesEstados open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public long insert(Estados estados){

        this.open();
        long rowaffected = -1;
        //conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableEstados.Estado, estados.Estado);
        contentValues.put(TableEstados.Descripcion, estados.Descripcion);
        contentValues.put(TableEstados.RequiereAsistencia, estados.RequiereAsistencia);
        contentValues.put(TableEstados.FechaHoraSinc, estados.FechaHoraSinc);

        rowaffected = database.insert(TableEstados.TABLE_NAME, null, contentValues);

        this.close();

        return rowaffected;

    }

    public void update(Estados estados){

        this.open();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TableEstados.Descripcion, estados.Descripcion);
        contentValues.put(TableEstados.RequiereAsistencia, estados.RequiereAsistencia);
        contentValues.put(TableEstados.FechaHoraSinc, estados.FechaHoraSinc);

        //database.update(TablePersonalTipolectoraBiometria.TABLE_NAME,contentValues,TablePersonalTipolectoraBiometria.IndiceBiometria + " = ? " + " AND " + TablePersonalTipolectoraBiometria.IdTipoDetaBio + " = ? ", new String[] { String.valueOf(biometrias.getIndiceBiometria()), String.valueOf(biometrias.getIdTipoDetaBio()) });
        database.update(TableEstados.TABLE_NAME,contentValues,TableEstados.Estado + " = ? ",new String[] { estados.getEstado() });
        this.close();

    }

    public List<Estados> select(){

        Estados estados = new Estados();
        List<Estados> estadosList =  new ArrayList<Estados>();


        Cursor cursor = database.rawQuery(TableEstados.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                estados = new Estados();
                estados.setEstado(cursor.getString(cursor.getColumnIndex(TableEstados.Estado)));
                estados.setDescripcion(cursor.getString(cursor.getColumnIndex(TableEstados.Descripcion)));
                estados.setRequiereAsistencia(cursor.getInt(cursor.getColumnIndex(TableEstados.RequiereAsistencia)));
                estados.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableEstados.FechaHoraSinc)));

                estadosList.add(estados);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return estadosList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableEstados.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }

}
