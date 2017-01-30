package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 09/11/2016.
 */

public class TablePersonal {

    public static final String TABLE_NAME = "PERSONAL";

    public static final String Empresa = "EMPRESA";
    public static final String Codigo = "CODIGO";
    public static final String CentroDeCosto = "CENTRO_DE_COSTO";
    public static final String ApellidoPaterno = "APELLIDO_PATERNO";
    public static final String ApellidoMaterno = "APELLIDO_MATERNO";
    public static final String Nombres = "NOMBRES";
    public static final String FechaDeNacimiento = "FECHA_DE_NACIMIENTO";
    public static final String FechaDeIngreso = "FECHA_DE_INGRESO";
    public static final String FechaDeCese = "FECHA_DE_CESE";
    public static final String Estado = "ESTADO";
    public static final String TipoHorario = "TIPO_HORARIO";
    public static final String Icono = "ICONO";
    public static final String NroDocumento = "NRO_DOCUMENTO";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Empresa + " TEXT NOT NULL, " +
            Codigo + " TEXT NOT NULL, " +
            CentroDeCosto + " TEXT NULL, " +
            ApellidoPaterno + " TEXT NULL, " +
            ApellidoMaterno + " TEXT NULL, " +
            Nombres + " TEXT NULL, " +
            FechaDeNacimiento + " DATETIME NULL, " +
            FechaDeIngreso + " DATETIME NULL, " +
            FechaDeCese + " DATETIME NULL, " +
            Estado + " TEXT NOT NULL DEFAULT '001', " +
            TipoHorario + " TEXT NULL DEFAULT '001', " +
            Icono + " TEXT NULL, " +
            NroDocumento + " TEXT NULL, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (EMPRESA, CODIGO) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Empresa + ", " +
            Codigo + ", " +
            CentroDeCosto + ", " +
            ApellidoPaterno + ", " +
            ApellidoMaterno + ", " +
            Nombres + ", " +
            FechaDeNacimiento + ", " +
            FechaDeIngreso + ", " +
            FechaDeCese + ", " +
            Estado + ", " +
            TipoHorario + ", " +
            Icono + ", " +
            NroDocumento + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;



}
