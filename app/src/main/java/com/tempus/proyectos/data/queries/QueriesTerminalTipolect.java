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
import com.tempus.proyectos.data.model.TerminalTipolect;
import com.tempus.proyectos.data.tables.TableTerminalTipolect;

/**
 * Created by GURRUTIAM on 11/11/2016.
 */

public class QueriesTerminalTipolect {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTerminalTipolect(Context context) {
        this.context = context;
    }

    public QueriesTerminalTipolect open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(TerminalTipolect terminalTipolect){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTerminalTipolect.IdTerminalTipolect, terminalTipolect.IdTerminalTipolect);
        contentValues.put(TableTerminalTipolect.Idterminal, terminalTipolect.Idterminal);
        contentValues.put(TableTerminalTipolect.IdTipoLect, terminalTipolect.IdTipoLect);
        contentValues.put(TableTerminalTipolect.Flag, terminalTipolect.Flag);
        contentValues.put(TableTerminalTipolect.IdAutorizacion, terminalTipolect.IdAutorizacion);
        contentValues.put(TableTerminalTipolect.FechaHoraSinc, terminalTipolect.FechaHoraSinc);

        database.insert(TableTerminalTipolect.TABLE_NAME, null, contentValues);

    }

    public List<TerminalTipolect> select(){

        TerminalTipolect terminalTipolect = new TerminalTipolect();
        List<TerminalTipolect> terminalTipolectList =  new ArrayList<TerminalTipolect>();

        Cursor cursor = database.rawQuery(TableTerminalTipolect.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                terminalTipolect = new TerminalTipolect();
                terminalTipolect.setIdTerminalTipolect(cursor.getInt(cursor.getColumnIndex(TableTerminalTipolect.IdTerminalTipolect)));
                terminalTipolect.setIdterminal(cursor.getString(cursor.getColumnIndex(TableTerminalTipolect.Idterminal)));
                terminalTipolect.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TableTerminalTipolect.IdTipoLect)));
                terminalTipolect.setFlag(cursor.getInt(cursor.getColumnIndex(TableTerminalTipolect.Flag)));
                terminalTipolect.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTerminalTipolect.IdAutorizacion)));
                terminalTipolect.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTerminalTipolect.FechaHoraSinc)));

                terminalTipolectList.add(terminalTipolect);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return terminalTipolectList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableTerminalTipolect.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }


    public int ConsultarLectora(String idterminal, int idlectora){

        TerminalTipolect terminalTipolect = new TerminalTipolect();
        List<TerminalTipolect> terminalTipolectList =  new ArrayList<TerminalTipolect>();

        String query = TableTerminalTipolect.SELECT_TABLE + " " +
                "WHERE " + TableTerminalTipolect.Idterminal + " = ? " +
                "AND " + TableTerminalTipolect.IdTipoLect + " = ? " +
                "LIMIT 1";

        this.open();

        Cursor cursor = database.rawQuery(query, new String[] { idterminal, String.valueOf(idlectora)});
        if(cursor.moveToNext()){
            do{
                terminalTipolect = new TerminalTipolect();
                terminalTipolect.setIdTerminalTipolect(cursor.getInt(cursor.getColumnIndex(TableTerminalTipolect.IdTerminalTipolect)));
                terminalTipolect.setIdterminal(cursor.getString(cursor.getColumnIndex(TableTerminalTipolect.Idterminal)));
                terminalTipolect.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TableTerminalTipolect.IdTipoLect)));
                terminalTipolect.setFlag(cursor.getInt(cursor.getColumnIndex(TableTerminalTipolect.Flag)));
                terminalTipolect.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTerminalTipolect.IdAutorizacion)));
                terminalTipolect.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTerminalTipolect.FechaHoraSinc)));

                terminalTipolectList.add(terminalTipolect);
            }while (cursor.moveToNext());
        }

        cursor.close();

        this.close();
        return terminalTipolect.getFlag();

    }

}

