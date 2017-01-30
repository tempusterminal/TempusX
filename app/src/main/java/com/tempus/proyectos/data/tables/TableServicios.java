package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 09/11/2016.
 */

public class TableServicios {

    public static final String TABLE_NAME = "SERVICIOS";

    public static final String IdServicios = "IDSERVICIOS";
    public static final String Descripcion = "DESCRIPCION";
    public static final String Host = "HOST";
    public static final String Ip = "IP";
    public static final String Instance = "INSTANCE";
    public static final String Database = "DATABASE";
    public static final String Port = "PORT";
    public static final String User = "USER";
    public static final String Pass = "PASS";
    public static final String IdAutorizacion = "IDAUTORIZACION";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            IdServicios + " INTEGER NOT NULL, " +
            Descripcion + " TEXT NULL, " +
            Host + " TEXT NULL, " +
            Ip + " TEXT NULL, " +
            Instance + " TEXT NULL, " +
            Database + " TEXT NULL, " +
            Port + " TEXT NULL, " +
            User + " TEXT NULL, " +
            Pass + " TEXT NULL, " +
            IdAutorizacion + " INTEGER NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (IDSERVICIOS) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            IdServicios + ", " +
            Descripcion + ", " +
            Host + ", " +
            Ip + ", " +
            Instance + ", " +
            Database + ", " +
            Port + ", " +
            User + ", " +
            Pass + ", " +
            IdAutorizacion + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;

}
