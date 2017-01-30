package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 09/11/2016.
 */

public class TablePerTipolectTerm {

    public static final String TABLE_NAME = "PER_TIPOLECT_TERM";

    public static final String Empresa = "EMPRESA";
    public static final String Codigo = "CODIGO";
    public static final String IdTerminalTipolect = "ID_TERMINAL_TIPOLECT";
    public static final String IdTipoLect = "ID_TIPO_LECT";
    public static final String Flag = "FLAG";
    public static final String IdAutorizacion = "ID_AUTORIZACION";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Empresa + " TEXT NOT NULL, " +
            Codigo + "  TEXT NOT NULL, " +
            IdTerminalTipolect + " INTEGER  NULL, " +
            IdTipoLect + " INTEGER NOT NULL, " +
            Flag + " INTEGER NULL, " +
            IdAutorizacion + " INTEGER NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (EMPRESA,CODIGO,ID_TERMINAL_TIPOLECT,ID_TIPO_LECT) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Empresa + ", " +
            Codigo + ", " +
            IdTerminalTipolect + ", " +
            IdTipoLect + ", " +
            Flag + ", " +
            IdAutorizacion + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;




}