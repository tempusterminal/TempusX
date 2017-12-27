package com.tempus.proyectos.data.tables;

import com.tempus.proyectos.util.Fechahora;

/**
 * Created by gurrutiam on 09/11/2016.
 */

public class TablePersonalTipolectoraBiometria {

    public static final String TABLE_NAME = "PERSONAL_TIPOLECTORA_BIOMETRIA";

    public static final String IdPerTipolectBio = "ID_PER_TIPOLECT_BIO";
    public static final String IndiceBiometria = "INDICE_BIOMETRIA";
    public static final String Empresa = "EMPRESA";
    public static final String Codigo = "CODIGO";
    public static final String IdTipoLect = "ID_TIPO_LECT";
    public static final String ValorTarjeta = "VALOR_TARJETA";
    public static final String IdTipoDetaBio = "ID_TIPO_DETA_BIO";
    public static final String ValorBiometria = "VALOR_BIOMETRIA";
    public static final String FechaBiometria = "FECHA_BIOMETRIA";
    public static final String ImagenBiometria = "IMAGEN_BIOMETRIA";
    public static final String IdAutorizacion = "ID_AUTORIZACION";
    public static final String Sincronizado = "SINCRONIZADO";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";

    private static Fechahora fechahora = new Fechahora();


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            IdPerTipolectBio + " INTEGER NOT NULL, " +
            IndiceBiometria + " INTEGER NOT NULL, " +
            Empresa + " TEXT NOT NULL, " +
            Codigo + " TEXT NOT NULL, " +
            IdTipoLect + " INTEGER NOT NULL, " +
            ValorTarjeta + " TEXT NULL, " +
            IdTipoDetaBio + " INTEGER NULL, " +
            ValorBiometria + " TEXT NULL, " +
            FechaBiometria + " DATETIME NULL, " +
            ImagenBiometria + " TEXT NULL, " +
            IdAutorizacion + " INTEGER NULL, " +
            Sincronizado + " INTEGER NULL DEFAULT 0, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (ID_PER_TIPOLECT_BIO, INDICE_BIOMETRIA, EMPRESA, CODIGO, ID_TIPO_LECT) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            IdPerTipolectBio + ", " +
            IndiceBiometria + ", " +
            Empresa + ", " +
            Codigo + ", " +
            IdTipoLect + ", " +
            ValorTarjeta + ", " +
            IdTipoDetaBio + ", " +
            ValorBiometria + ", " +
            FechaBiometria + ", " +
            ImagenBiometria + ", " +
            IdAutorizacion + ", " +
            Sincronizado + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;

    public static final String SELECT_BIOMETRIA_REPLICAR = "SELECT " +
            IdPerTipolectBio + ", " +
            IndiceBiometria + ", " +
            Empresa + ", " +
            Codigo + ", " +
            IdTipoLect + ", " +
            ValorTarjeta + ", " +
            IdTipoDetaBio + ", " +
            ValorBiometria + ", " +
            FechaBiometria + ", " +
            ImagenBiometria + ", " +
            IdAutorizacion + ", " +
            Sincronizado + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME + " " +
            "WHERE " + ValorBiometria + " IS NOT NULL " +
            "AND " + Sincronizado + " = 0 " +
            "ORDER BY " + IndiceBiometria + " ASC " +
            "; ";

    public static final String SELECT_BIOMETRIA_SINCRONIZAR = "SELECT " +
            IdPerTipolectBio + ", " +
            IndiceBiometria + ", " +
            Empresa + ", " +
            Codigo + ", " +
            IdTipoLect + ", " +
            ValorTarjeta + ", " +
            IdTipoDetaBio + ", " +
            ValorBiometria + ", " +
            FechaBiometria + ", " +
            ImagenBiometria + ", " +
            IdAutorizacion + ", " +
            Sincronizado + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME + " " +
            //"WHERE " + ValorBiometria + " IS NOT NULL " +
            "WHERE " + Sincronizado + " = 2 " +
            "ORDER BY " + IndiceBiometria + " ASC " +
            "LIMIT 1 " +
            "; ";

    public static final String SELECT_INDICE_BIOMETRIA_REPLICAR = "SELECT " +
            IdPerTipolectBio + ", " +
            IndiceBiometria + ", " +
            Empresa + ", " +
            Codigo + ", " +
            IdTipoLect + ", " +
            ValorTarjeta + ", " +
            IdTipoDetaBio + ", " +
            ValorBiometria + ", " +
            FechaBiometria + ", " +
            ImagenBiometria + ", " +
            IdAutorizacion + ", " +
            Sincronizado + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME + " "
            //"WHERE " + ValorBiometria + " IS NOT NULL " +
            //"AND " + Sincronizado + " = 0 " +
            //"ORDER BY " + IndiceBiometria + " ASC " +
            //"ORDER BY " + IdTipoDetaBio + " ASC " +
            //"; "
            ;

    public static final String SELECT_PERSONAL_CESADO_CON_SIN_HUELLA = "SELECT " +
            "DISTINCT " + IndiceBiometria + " " +
            "FROM " + TABLE_NAME + " " +
            "INNER JOIN PERSONAL " +
            "ON PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA = PERSONAL.EMPRESA " +
            "AND PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO = PERSONAL.CODIGO " +
            "WHERE " + Sincronizado + " = 0 " +
            "AND (PERSONAL.FECHA_DE_CESE <= DATETIME ('" + fechahora.getFechahoracero(fechahora.getFechahora()) + "') OR ESTADO = '002') " +
            "; ";

    public static final String SELECT_PERSONAL_ACTIVO_SIN_HUELLA = "SELECT " +
            "DISTINCT " + IndiceBiometria + " " +
            "FROM " + TABLE_NAME + " " +
            "INNER JOIN PERSONAL " +
            "ON PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA = PERSONAL.EMPRESA " +
            "AND PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO = PERSONAL.CODIGO " +
            "WHERE " + Sincronizado + " = 0 " +
            "AND PERSONAL_TIPOLECTORA_BIOMETRIA.VALOR_BIOMETRIA IS NULL " +
            "AND (PERSONAL.FECHA_DE_CESE IS NULL AND PERSONAL.ESTADO != '002') " +
            "; ";

    public static final String SELECT_PERSONAL_ACTIVO_CON_HUELLA = "SELECT " +
            "DISTINCT " + IndiceBiometria + " " +
            "FROM " + TABLE_NAME + " " +
            "INNER JOIN PERSONAL " +
            "ON PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA = PERSONAL.EMPRESA " +
            "AND PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO = PERSONAL.CODIGO " +
            "WHERE " + Sincronizado + " = 0 " +
            "AND PERSONAL_TIPOLECTORA_BIOMETRIA.VALOR_BIOMETRIA IS NOT NULL " +
            "AND (PERSONAL.FECHA_DE_CESE IS NULL AND PERSONAL.ESTADO != '002') " +
            "; ";


}
