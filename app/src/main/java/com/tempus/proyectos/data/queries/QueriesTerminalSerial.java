package com.tempus.proyectos.data.queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.model.TerminalSerial;
import com.tempus.proyectos.data.tables.TableTerminalSerial;
import com.tempus.proyectos.data.view.ViewBiometrias;
import com.tempus.proyectos.util.Fechahora;

/**
 * Created by gurrutiam on 15/02/2017.
 */

public class QueriesTerminalSerial {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTerminalSerial() {

    }

    public QueriesTerminalSerial(Context context) {
        this.context = context;
    }

    public QueriesTerminalSerial open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public void drop(){
        this.open();
        database.execSQL(ViewBiometrias.DROP_VIEW);
        Log.d("Autorizaciones","Vista eliminada exitosamente");
        this.close();
    }


    public void insert(TerminalSerial terminalSerial){

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTerminalSerial.Idserial, terminalSerial.Idserial);
        /*



         */

    }


    public void update(){
        this.open();
        Fechahora fechahora = new Fechahora();

        ContentValues contentValues = new ContentValues();
        /*




         */


    }






}
