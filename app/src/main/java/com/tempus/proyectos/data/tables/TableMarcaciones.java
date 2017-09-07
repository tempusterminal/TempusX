package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 09/11/2016.
 */

public class TableMarcaciones {
    public static final String TABLE_NAME = "MARCACIONES";

    public static final String Empresa = "EMPRESA";
    public static final String Codigo = "CODIGO";
    public static final String Fechahora = "FECHAHORA";
    public static final String ValorTarjeta = "VALOR_TARJETA";
    public static final String HoraTxt = "HORATXT";
    public static final String EntSal = "ENT_SAL";
    public static final String Flag = "FLAG";
    public static final String Fecha = "FECHA";
    public static final String Hora = "HORA";
    public static final String Idterminal = "IDTERMINAL";
    public static final String IdTipoLect = "ID_TIPO_LECT";
    public static final String FlgActividad = "FLG_ACTIVIDAD";
    public static final String IdUsuario = "IDUSUARIO";
    public static final String TmpListar = "TMP_LISTAR";
    public static final String Autorizado = "AUTORIZADO";
    public static final String TipoOperacion = "TIPO_OPERACION";
    public static final String Sincronizado = "SINCRONIZADO";
    public static final String Datos = "DATOS";
    public static final String ValorDatoContenido = "VALOR_DATO_CONTENIDO";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Empresa + " TEXT NOT NULL, " +
            Codigo + " TEXT NOT NULL, " +
            Fechahora + " DATETIME NOT NULL, " +
            ValorTarjeta + " TEXT NULL DEFAULT '', " +
            HoraTxt + " TEXT NULL, " +
            EntSal + " TEXT NULL DEFAULT 0, " +
            Flag + " TEXT NULL, " +
            Fecha + " DATETIME NULL, " +
            Hora + " DATETIME NULL, " +
            Idterminal + " TEXT NOT NULL, " +
            IdTipoLect + " INTEGER NOT NULL, " +
            FlgActividad + " TEXT NULL, " +
            IdUsuario + " INTEGER NULL, " +
            TmpListar + " TEXT NULL, " +
            Autorizado + " INTEGER NULL, " +
            TipoOperacion + " INTEGER NULL, " +
            Sincronizado + " INTEGER NULL DEFAULT 0, " +
            Datos + " TEXT NULL DEFAULT '', " +
            ValorDatoContenido + " INTEGER NULL DEFAULT -1, " +
            "PRIMARY KEY (EMPRESA, CODIGO, FECHAHORA) " +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Empresa + ", " +
            Codigo + ", " +
            Fechahora + ", " +
            ValorTarjeta + ", " +
            HoraTxt + ", " +
            EntSal + ", " +
            Flag + ", " +
            Fecha + ", " +
            Hora + ", " +
            Idterminal + ", " +
            IdTipoLect + ", " +
            FlgActividad + ", " +
            IdUsuario + ", " +
            TmpListar + ", " +
            Autorizado + ", " +
            TipoOperacion + ", " +
            Sincronizado + ", " +
            Datos + ", " +
            ValorDatoContenido + " " +
            "FROM " + TABLE_NAME;


    public static final String SELECT_ONE_ROW_TABLE = "SELECT " +
            Empresa + ", " +
            Codigo + ", " +
            Fechahora + ", " +
            ValorTarjeta + ", " +
            HoraTxt + ", " +
            EntSal + ", " +
            Flag + ", " +
            Fecha + ", " +
            Hora + ", " +
            Idterminal + ", " +
            IdTipoLect + ", " +
            FlgActividad + ", " +
            IdUsuario + ", " +
            TmpListar + ", " +
            Autorizado + ", " +
            TipoOperacion + ", " +
            Sincronizado + ", " +
            Datos + ", " +
            ValorDatoContenido + " " +
            "FROM " + TABLE_NAME + " " +
            "WHERE " + Sincronizado + " = 0 " +
            "ORDER BY " + Fechahora + " ASC " +
            "LIMIT 1" ;

    public static final String SELECT_NOSYNC_TABLE = "SELECT " +
            Empresa + ", " +
            Codigo + ", " +
            Fechahora + ", " +
            ValorTarjeta + ", " +
            HoraTxt + ", " +
            EntSal + ", " +
            Flag + ", " +
            Fecha + ", " +
            Hora + ", " +
            Idterminal + ", " +
            IdTipoLect + ", " +
            FlgActividad + ", " +
            IdUsuario + ", " +
            TmpListar + ", " +
            Autorizado + ", " +
            TipoOperacion + ", " +
            Sincronizado + ", " +
            Datos + ", " +
            ValorDatoContenido + " " +
            "FROM " + TABLE_NAME + " " +
            "WHERE " + Sincronizado + " = 0 " +
            "ORDER BY " + Fechahora + " ASC ";


}
