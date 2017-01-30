package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 09/01/2017.
 */

public class TableLlamadas {

    public static final String TABLE_NAME = "LLAMADAS";

    public static final String Idllamada = "IDLLAMADA";
    public static final String Llamada = "LLAMADA";
    public static final String Parameters = "PARAMETERS";
    public static final String TableName = "TABLE_NAME";
    public static final String Primarykey = "PRIMARYKEY";
    public static final String Columns = "COLUMNS";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Idllamada + " TEXT NOT NULL, " +
            Llamada + " TEXT NULL, " +
            Parameters + " TEXT NULL, " +
            TableName + " TEXT NULL, " +
            Primarykey + " TEXT NULL, " +
            Columns + " TEXT NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (" + Idllamada + ") " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Idllamada + ", " +
            Llamada + ", " +
            Parameters + ", " +
            TableName + ", " +
            Primarykey + ", " +
            Columns + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;

}
