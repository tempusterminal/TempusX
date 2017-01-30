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
import com.tempus.proyectos.data.model.TerminalServicios;
import com.tempus.proyectos.data.tables.TableTerminalServicios;

/**
 * Created by GURRUTIAM on 11/11/2016.
 */

public class QueriesTerminalServicios {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTerminalServicios(Context context) {
        this.context = context;
    }

    public QueriesTerminalServicios open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(TerminalServicios terminalServicios){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTerminalServicios.IdTerminalServicios, terminalServicios.IdTerminalServicios);
        contentValues.put(TableTerminalServicios.Idterminal, terminalServicios.Idterminal);
        contentValues.put(TableTerminalServicios.IdServicios, terminalServicios.IdServicios);
        contentValues.put(TableTerminalServicios.Flag, terminalServicios.Flag);
        contentValues.put(TableTerminalServicios.IdAutorizacion, terminalServicios.IdAutorizacion);
        contentValues.put(TableTerminalServicios.FechaHoraSinc, terminalServicios.FechaHoraSinc);

        database.insert(TableTerminalServicios.TABLE_NAME, null, contentValues);

    }

    public List<TerminalServicios> select(){

        TerminalServicios terminalServicios = new TerminalServicios();
        List<TerminalServicios> terminalServiciosList =  new ArrayList<TerminalServicios>();

        Cursor cursor = database.rawQuery(TableTerminalServicios.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                terminalServicios = new TerminalServicios();
                terminalServicios.setIdTerminalServicios(cursor.getInt(cursor.getColumnIndex(TableTerminalServicios.IdTerminalServicios)));
                terminalServicios.setIdterminal(cursor.getString(cursor.getColumnIndex(TableTerminalServicios.Idterminal)));
                terminalServicios.setIdServicios(cursor.getInt(cursor.getColumnIndex(TableTerminalServicios.IdServicios)));
                terminalServicios.setFlag(cursor.getInt(cursor.getColumnIndex(TableTerminalServicios.Flag)));
                terminalServicios.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTerminalServicios.IdAutorizacion)));
                terminalServicios.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTerminalServicios.FechaHoraSinc)));

                terminalServiciosList.add(terminalServicios);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return terminalServiciosList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableTerminalServicios.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }


}
