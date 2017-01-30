package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class TableTipoLectora {


    public static final String TABLE_NAME = "TIPO_LECTORA";

    public static final String IdTipoLect = "ID_TIPO_LECT";
    public static final String Descripcion = "DESCRIPCION";
    public static final String Biometria = "BIOMETRIA";
    public static final String IdAutorizacion = "ID_AUTORIZACION";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            IdTipoLect + " INTEGER NOT NULL, " +
            Descripcion + " TEXT NULL, " +
            Biometria + " INTEGER NULL, " +
            IdAutorizacion + " INTEGER NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (ID_TIPO_LECT) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            IdTipoLect + ", " +
            Descripcion + ", " +
            Biometria + ", " +
            IdAutorizacion + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;


}
