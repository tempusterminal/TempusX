package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class TableTerminalConfiguracion {

    public static final String TABLE_NAME = "TERMINAL_CONFIGURACION";

    public static final String Idterminal = "IDTERMINAL";
    public static final String TipoInstalacion = "TIPO_INSTALACION";
    public static final String TipoAhorroEnergia = "TIPO_AHORRO_ENERGIA";
    public static final String BateriaResistencia = "BATERIA_RESISTENCIA";
    public static final String HusoHorario = "HUSO_HORARIO";
    public static final String Parametro = "PARAMETRO";
    public static final String FechaHoraServidor = "FECHA_HORA_SERVIDOR";
    public static final String Reboot = "REBOOT";
    public static final String HoraReboot = "HORA_REBOOT";
    public static final String Replicado = "REPLICADO";
    public static final String HoraInicioReplicado = "HORA_INICIO_REPLICADO";
    public static final String HoraFinReplicado = "HORA_FIN_REPLICADO";
    public static final String IdAutorizacion = "ID_AUTORIZACION";
    public static final String FechaHoraSinc = "FECHA_HORA_SINC";


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Idterminal + " TEXT NOT NULL, " +
            TipoInstalacion + " INTEGER NULL DEFAULT 2, " + // 1=MOVIL 2=FIJO
            TipoAhorroEnergia + " INTEGER NULL DEFAULT 0, " + //0=NUNCA 1=SOLO DESCONECTADO 2=SIEMPRE
            BateriaResistencia + " INTEGER NULL DEFAULT -1, " +
            HusoHorario + " INTEGER NULL DEFAULT -5, " +
            Parametro + " TEXT NULL DEFAULT '100', " +
            FechaHoraServidor + " TEXT NULL DEFAULT '', " +
            Reboot + " TEXT NULL DEFAULT 0, " +
            HoraReboot + " TEXT NULL DEFAULT '01:00:00', " +
            Replicado + " INTEGER NULL DEFAULT 0, " +
            HoraInicioReplicado + " TEXT NULL DEFAULT '00:00:00', " +
            HoraFinReplicado + " TEXT NULL DEFAULT '23:59:59', " +
            IdAutorizacion + " INTEGER NULL DEFAULT 0, " +
            FechaHoraSinc + " DATETIME NULL, " +
            "PRIMARY KEY (IDTERMINAL) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Idterminal + ", " +
            TipoInstalacion + ", " +
            TipoAhorroEnergia + ", " +
            BateriaResistencia + ", " +
            HusoHorario + ", " +
            Parametro + ", " +
            FechaHoraServidor + ", " +
            Reboot + ", " +
            HoraReboot + ", " +
            Replicado + ", " +
            HoraInicioReplicado + ", " +
            HoraFinReplicado + ", " +
            IdAutorizacion + ", " +
            FechaHoraSinc + " " +
            "FROM " + TABLE_NAME;

}
