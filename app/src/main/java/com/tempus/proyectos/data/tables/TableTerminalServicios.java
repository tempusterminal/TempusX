package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class TableTerminalServicios {

    public static final String TABLE_NAME = "TERMINAL_SERVICIOS";

    public static final String IdTerminalServicios = "ID_TERMINAL_SERVICIOS";
    public static final String Idterminal = "IDTERMINAL";
    public static final String IdServicios = "IDSERVICIOS";
    public static final String Flag = "FLAG";
    public static final String IdAutorizacion = "IDAUTORIZACION";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            IdTerminalServicios + " INTEGER NOT NULL, " +
            Idterminal + " TEXT NOT NULL, " +
            IdServicios + " INTEGER NOT NULL, " +
            Flag + " INTEGER NULL, " +
            IdAutorizacion + " INTEGER NULL DEFAULT 0, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (ID_TERMINAL_SERVICIOS, IDTERMINAL, IDSERVICIOS) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            IdTerminalServicios + ", " +
            Idterminal + ", " +
            IdServicios + ", " +
            Flag + ", " +
            IdAutorizacion + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;

}
