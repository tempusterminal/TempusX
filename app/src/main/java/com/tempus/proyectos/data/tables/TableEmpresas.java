package com.tempus.proyectos.data.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class TableEmpresas{
    public static final String TABLE_NAME = "EMPRESAS";

    public static final String Empresa = "EMPRESA";
    public static final String NombreCorto = "NOMBRE_CORTO";
    public static final String Nombre = "NOMBRE";
    public static final String Icono = "ICONO";
    public static final String Direccion = "DIRECCION";
    public static final String Ruc = "RUC";
    public static final String Bloqueado = "BLOQUEADO";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    /*
    static final String DB_NAME = "TEMPUSPLUS.db";

    static final int DB_VERSION = 1;
    */

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Empresa + " TEXT NOT NULL, " +
            NombreCorto + " TEXT NULL, " +
            Nombre + " TEXT NULL, " +
            Icono + " TEXT NULL, " +
            Direccion + " TEXT NULL, " +
            Ruc + " TEXT NULL, " +
            Bloqueado + " INTEGER NULL DEFAULT 0, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (EMPRESA) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    /*
    public TableEmpresas(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Autorizaciones",CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    */


}
