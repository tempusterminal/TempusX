package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class TableTerminalTipolect {

    public static final String TABLE_NAME = "TERMINAL_TIPOLECT";

    public static final String IdTerminalTipolect = "ID_TERMINAL_TIPOLECT";
    public static final String Idterminal = "IDTERMINAL";
    public static final String IdTipoLect = "ID_TIPO_LECT";
    public static final String Flag = "FLAG";
    public static final String IdAutorizacion = "ID_AUTORIZACION";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            IdTerminalTipolect + " INTEGER NOT NULL, " +
            Idterminal + " TEXT NULL, " +
            IdTipoLect + " INTEGER NULL, " +
            Flag + " INTEGER NULL, " +
            IdAutorizacion + " INTEGER NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (ID_TERMINAL_TIPOLECT) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            IdTerminalTipolect + ", " +
            Idterminal + ", " +
            IdTipoLect + ", " +
            Flag + ", " +
            IdAutorizacion + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;

}
