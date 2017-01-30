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
import com.tempus.proyectos.data.model.Terminal;
import com.tempus.proyectos.data.tables.TableTerminal;

/**
 * Created by GURRUTIAM on 11/11/2016.
 */

public class QueriesTerminal {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTerminal(Context context) {
        this.context = context;
    }

    public QueriesTerminal open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(Terminal terminal){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTerminal.Idterminal, terminal.Idterminal);
        contentValues.put(TableTerminal.Descripcion, terminal.Descripcion);
        contentValues.put(TableTerminal.Habilitado, terminal.Habilitado);
        contentValues.put(TableTerminal.Mac, terminal.Mac);
        contentValues.put(TableTerminal.Modelo, terminal.Modelo);
        contentValues.put(TableTerminal.Firmware, terminal.Firmware);
        contentValues.put(TableTerminal.Software, terminal.Software);
        contentValues.put(TableTerminal.Hardware, terminal.Hardware);
        contentValues.put(TableTerminal.Chasis, terminal.Chasis);
        contentValues.put(TableTerminal.Ups, terminal.Ups);
        contentValues.put(TableTerminal.NumCel, terminal.NumCel);
        contentValues.put(TableTerminal.IdAutorizacion, terminal.IdAutorizacion);
        contentValues.put(TableTerminal.FechaHoraSinc, terminal.FechaHoraSinc);

        database.insert(TableTerminal.TABLE_NAME, null, contentValues);

    }

    public List<Terminal> select(){

        Terminal terminal = new Terminal();
        List<Terminal> terminalList =  new ArrayList<Terminal>();

        Cursor cursor = database.rawQuery(TableTerminal.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                terminal = new Terminal();
                terminal.setIdterminal(cursor.getString(cursor.getColumnIndex(TableTerminal.Idterminal)));
                terminal.setDescripcion(cursor.getString(cursor.getColumnIndex(TableTerminal.Descripcion)));
                terminal.setHabilitado(cursor.getInt(cursor.getColumnIndex(TableTerminal.Habilitado)));
                terminal.setMac(cursor.getString(cursor.getColumnIndex(TableTerminal.Mac)));
                terminal.setModelo(cursor.getString(cursor.getColumnIndex(TableTerminal.Modelo)));
                terminal.setFirmware(cursor.getString(cursor.getColumnIndex(TableTerminal.Firmware)));
                terminal.setSoftware(cursor.getString(cursor.getColumnIndex(TableTerminal.Software)));
                terminal.setHardware(cursor.getString(cursor.getColumnIndex(TableTerminal.Hardware)));
                terminal.setChasis(cursor.getString(cursor.getColumnIndex(TableTerminal.Chasis)));
                terminal.setUps(cursor.getString(cursor.getColumnIndex(TableTerminal.Ups)));
                terminal.setNumCel(cursor.getString(cursor.getColumnIndex(TableTerminal.NumCel)));
                terminal.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTerminal.IdAutorizacion)));
                terminal.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTerminal.FechaHoraSinc)));

                terminalList.add(terminal);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return terminalList;
    }


    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableTerminal.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }


}
