package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class TableTerminal {

    public static final String TABLE_NAME = "TERMINAL";

    public static final String Idterminal = "IDTERMINAL";
    public static final String Descripcion = "DESCRIPCION";
    public static final String Habilitado = "HABILITADO";
    public static final String Mac = "MAC";
    public static final String Modelo = "MODELO";
    public static final String Firmware = "FIRMWARE";
    public static final String Software = "SOFTWARE";
    public static final String Hardware = "HARDWARE";
    public static final String Chasis = "CHASIS";
    public static final String Ups = "UPS";
    public static final String NumCel = "NUM_CEL";
    public static final String IdAutorizacion = "ID_AUTORIZACION";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Idterminal + " TEXT NOT NULL, " +
            Descripcion + " TEXT NULL, " +
            Habilitado + " INTEGER NULL DEFAULT 1, " +
            Mac + " TEXT NULL DEFAULT '00:00:00:00:00:00', " +
            Modelo + " TEXT NULL, " +
            Firmware + " TEXT NULL, " +
            Software + " TEXT NULL, " +
            Hardware + " TEXT NULL, " +
            Chasis + " TEXT NULL, " +
            Ups + " TEXT NULL, " +
            NumCel + " TEXT NULL, " +
            IdAutorizacion + " TEXT NULL, " +
            FechaHoraSinc + " TEXT NULL, " +
            "PRIMARY KEY (IDTERMINAL) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Idterminal + ", " +
            Descripcion + ", " +
            Habilitado + ", " +
            Mac + ", " +
            Modelo + ", " +
            Firmware + ", " +
            Software + ", " +
            Hardware + ", " +
            Chasis + ", " +
            Ups + ", " +
            NumCel + ", " +
            IdAutorizacion + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;


}
