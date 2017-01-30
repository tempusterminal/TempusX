package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 12/01/2017.
 */

public class TableTerminalSerial {
    public static final String TABLE_NAME = "TERMINAL_SERIAL";

    public static final String Idserial = "IDSERIAL";
    public static final String Mac = "MAC";
    public static final String Enable = "ENABLE";
    public static final String LoopConn = "LOOP_CONN";
    public static final String TryConn = "TRY_CONN";
    public static final String TryConnBt = "TRY_CONN_BT";
    public static final String TryReconn = "TRY_RECONN";
    public static final String TryReconnBt = "TRY_RECONN_BT";
    public static final String EnableReboot = "ENABLE_REBOOR";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Idserial + " TEXT NOT NULL, " +
            Mac + " TEXT NULL, " +
            Enable + " INTEGER NULL, " +
            LoopConn + " INTEGER NULL, " +
            TryConn + " INTEGER NULL, " +
            TryConnBt + " INTEGER NULL, " +
            TryReconn + " INTEGER NULL, " +
            TryReconnBt + " INTEGER NULL, " +
            EnableReboot + " INTEGER NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (" + Idserial + ") " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Idserial + ", " +
            Mac + ", " +
            Enable + ", " +
            LoopConn + ", " +
            TryConn + ", " +
            TryConnBt + ", " +
            TryReconn + ", " +
            TryReconnBt + ", " +
            EnableReboot + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;

}
