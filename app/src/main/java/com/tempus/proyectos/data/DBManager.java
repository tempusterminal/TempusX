package com.tempus.proyectos.data;

/**
 * Created by gurrutiam on 03/11/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.model.Estados;
import com.tempus.proyectos.data.tables.TableEstados;

public class DBManager {
    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;


    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close() {
        conexion.close();
    }

    public void create(){
        this.open();
        conexion.onCreate(database);
        this.close();
    }

    public void tables(){
        this.open();
        String query = "select name from sqlite_master where type = 'table'";

        Cursor cursor = database.rawQuery(query, null);
        Log.d("Autorizaciones", "--------------------------------------------------------------------------------");
        Log.d("Autorizaciones", "LISTA DE TABLAS ----------------------------------------------------------------");
        if(cursor.moveToNext()){
            do{
                Log.d("Autorizaciones", cursor.getString(0));
            }while (cursor.moveToNext());
        }
        Log.d("Autorizaciones", "--------------------------------------------------------------------------------");

        cursor.close();
        this.close();
    }

    public void views(){
        this.open();
        String query = "select name from sqlite_master where type = 'view'";

        Cursor cursor = database.rawQuery(query, null);
        Log.d("Autorizaciones", "--------------------------------------------------------------------------------");
        Log.d("Autorizaciones", "LISTA DE VISTAS ----------------------------------------------------------------");
        if(cursor.moveToNext()){
            do{
                Log.d("Autorizaciones", cursor.getString(0));
            }while (cursor.moveToNext());
        }
        Log.d("Autorizaciones", "--------------------------------------------------------------------------------");

        cursor.close();
        this.close();
    }

    public void dropalltables(){
        this.open();
        database.execSQL("BEGIN TRANSACTION;");
        database.execSQL("DELETE FROM ESTADOS;");
        database.execSQL("DELETE FROM EMPRESAS;");

        //database.execSQL("DELETE FROM TERMINAL;");
        //database.execSQL("INSERT INTO TERMINAL(IDTERMINAL) VALUES('1');");


        database.execSQL("DELETE FROM TERMINAL_TIPOLECT;");
        database.execSQL("DELETE FROM TIPO_LECTORA;");
        database.execSQL("DELETE FROM TIPO_DETALLE_BIOMETRIA;");
        database.execSQL("DELETE FROM PERSONAL;");
        database.execSQL("DELETE FROM PER_TIPOLECT_TERM;");
        database.execSQL("DELETE FROM TARJETA_PERSONAL_TIPOLECTORA;");
        database.execSQL("DELETE FROM PERSONAL_TIPOLECTORA_BIOMETRIA;");
        database.execSQL("COMMIT;");
        this.close();

    }

    public void all(){

        this.create();
        this.dropalltables();
        this.tables();
        this.views();

    }

    public void execSQL(String sql){

        this.open();
        //database.beginTransaction();
        database.execSQL(sql);
        //database.endTransaction();
        this.close();
    }

    public String valexecSQL(String sql){
        String value = "";
        this.open();
        //database.beginTransaction();
        Cursor cursor = database.rawQuery(sql,null);
        //database.endTransaction();

        if(cursor.moveToNext()){
            value = cursor.getString(0);
        }while(cursor.moveToNext());

        cursor.close();
        this.close();

        return value;
    }



}
