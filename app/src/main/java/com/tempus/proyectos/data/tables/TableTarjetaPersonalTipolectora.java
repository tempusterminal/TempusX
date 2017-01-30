package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 09/11/2016.
 */

public class TableTarjetaPersonalTipolectora {

    public static final String TABLE_NAME = "TARJETA_PERSONAL_TIPOLECTORA";

    public static final String Empresa = "EMPRESA";
    public static final String Codigo = "CODIGO";
    public static final String IdTipoLect = "ID_TIPO_LECT";
    public static final String ValorTarjeta = "VALOR_TARJETA";
    public static final String FechaIni = "FECHA_INI";
    public static final String FechaFin = "FECHA_FIN";
    public static final String FechaCreacion = "FECHA_CREACION";
    public static final String FechaAnulacion = "FECHA_ANULACION";
    public static final String IdAutorizacion = "ID_AUTORIZACION";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Empresa + " TEXT NOT NULL, " +
            Codigo + " TEXT NOT NULL, " +
            IdTipoLect + " INTEGER NOT NULL, " +
            ValorTarjeta + " TEXT NULL, " +
            FechaIni + " DATETIME NULL, " +
            FechaFin + " DATETIME NULL, " +
            FechaCreacion + " DATETIME NULL, " +
            FechaAnulacion + " DATETIME NULL, " +
            IdAutorizacion + " INTEGER NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (EMPRESA, CODIGO, ID_TIPO_LECT) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Empresa + ", " +
            Codigo + ", " +
            IdTipoLect + ", " +
            ValorTarjeta + ", " +
            FechaIni + ", " +
            FechaFin + ", " +
            FechaCreacion + ", " +
            FechaAnulacion + ", " +
            IdAutorizacion + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;



}
