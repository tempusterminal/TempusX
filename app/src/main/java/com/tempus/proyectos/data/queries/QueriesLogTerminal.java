package com.tempus.proyectos.data.queries;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.model.LogTerminal;
import com.tempus.proyectos.data.tables.TableLogTerminal;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurrutiam on 19/10/2017.
 */

public class QueriesLogTerminal {

    String TAG = "DQ-QUELT";

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    private Fechahora fechahora;

    public QueriesLogTerminal() {
        this.context = ActivityPrincipal.context;
    }

    public QueriesLogTerminal open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public long insertLogTerminal(String tag, String value, String user){

        long rowaffected = -1;
        //conexion.onCreate(database);
        fechahora = new Fechahora();

        this.open();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TableLogTerminal.Idterminal, ActivityPrincipal.idTerminal);
        contentValues.put(TableLogTerminal.Tag, tag);
        contentValues.put(TableLogTerminal.Value, value);
        contentValues.put(TableLogTerminal.User, user);
        contentValues.put(TableLogTerminal.Sincronizado, 0);
        contentValues.put(TableLogTerminal.Fechahora, fechahora.getFechahoraFull());


        rowaffected = database.insert(TableLogTerminal.TABLE_NAME, null, contentValues);

        this.close();

        return rowaffected;

    }


    public List<LogTerminal> select_one_row(){

        LogTerminal logTerminal = new LogTerminal();
        List<LogTerminal> logTerminalList =  new ArrayList<LogTerminal>();

        this.open();
        Cursor cursor = database.rawQuery(TableLogTerminal.SELECT_ONE_ROW_TABLE, null);
        if(cursor.moveToNext()){
            do{
                logTerminal = new LogTerminal();
                logTerminal.setIdterminal(cursor.getString(cursor.getColumnIndex(TableLogTerminal.Idterminal)));
                logTerminal.setTag(cursor.getString(cursor.getColumnIndex(TableLogTerminal.Tag)));
                logTerminal.setValue(cursor.getString(cursor.getColumnIndex(TableLogTerminal.Value)));
                logTerminal.setUser(cursor.getString(cursor.getColumnIndex(TableLogTerminal.User)));
                logTerminal.setSincronizado(cursor.getInt(cursor.getColumnIndex(TableLogTerminal.Sincronizado)));
                logTerminal.setFechahora(cursor.getString(cursor.getColumnIndex(TableLogTerminal.Fechahora)));
                logTerminalList.add(logTerminal);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return logTerminalList;
    }

    public void ActualizarSincronizado(LogTerminal logTerminal, int sincronizado){
        this.open();
        //conexion.onCreate(database);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableLogTerminal.Sincronizado, sincronizado);

        database.update(TableLogTerminal.TABLE_NAME, contentValues, TableLogTerminal.Tag + " = ? AND " + TableLogTerminal.Fechahora + " = ? ", new String[] { logTerminal.getTag(), logTerminal.getFechahora()});
        Log.v(TAG,"BTS-MAET Registro actualizado");
        this.close();
    }


}
