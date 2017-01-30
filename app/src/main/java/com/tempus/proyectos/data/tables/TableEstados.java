package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class TableEstados {

    public static final String TABLE_NAME = "ESTADOS";

    public static final String Estado = "ESTADO";
    public static final String Descripcion = "DESCRIPCION";
    public static final String RequiereAsistencia = "REQUIERE_ASISTENCIA";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Estado + " TEXT NOT NULL, " +
            Descripcion + " TEXT NULL, " +
            RequiereAsistencia + " INTEGER NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (ESTADO) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;


    public static final String SELECT_TABLE = "SELECT " +
            Estado + ", " +
            Descripcion + ", " +
            RequiereAsistencia + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;
}
