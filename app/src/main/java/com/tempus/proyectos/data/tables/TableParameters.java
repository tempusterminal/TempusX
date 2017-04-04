package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 30/03/2017.
 */

public class TableParameters {

    public static final String TABLE_NAME = "PARAMETERS";

    public static final String Idparameter = "IDPARAMETER";
    public static final String Parameter = "PARAMETER";
    public static final String Value = "VALUE";
    public static final String Subparameters = "SUBPARAMETER";
    public static final String Enable = "ENABLE";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Idparameter + " TEXT NOT NULL, " +
            Parameter + " TEXT NULL, " +
            Value + " TEXT NULL, " +
            Subparameters + " TEXT NULL, " +
            Enable + " INTEGER NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (" + Idparameter + ") " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Idparameter + ", " +
            Parameter + ", " +
            Value + ", " +
            Subparameters + ", " +
            Enable + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;

}
