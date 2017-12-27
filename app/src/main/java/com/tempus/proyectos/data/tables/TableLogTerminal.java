package com.tempus.proyectos.data.tables;

/**
 * Created by gurrutiam on 19/10/2017.
 */

public class TableLogTerminal {

    public static final String TABLE_NAME = "LOG_TERMINAL";

    public static final String Idterminal = "IDTERMINAL";
    public static final String Tag = "TAG";
    public static final String Fechahora = "FECHAHORA";
    public static final String Value = "VALUE";
    public static final String User = "USER";
    public static final String Sincronizado = "SINCRONIZADO";


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Idterminal + " TEXT NOT NULL, " +
            Tag + " TEXT NOT NULL, " +
            Fechahora + " DATETIME NULL, " +
            Value + " TEXT NULL, " +
            User + " TEXT NULL, " +
            Sincronizado + " INTEGER NULL DEFAULT 0, " +
            "PRIMARY KEY (IDTERMINAL,TAG,FECHAHORA)" +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String SELECT_TABLE = "SELECT " +
            Idterminal + ", " +
            Tag + ", " +
            Fechahora + ", " +
            Value + ", " +
            User + ", " +
            Sincronizado + " " +
            "FROM " + TABLE_NAME;


    public static final String SELECT_ONE_ROW_TABLE = "SELECT " +
            Idterminal + ", " +
            Tag + ", " +
            Fechahora + ", " +
            Value + ", " +
            User + ", " +
            Sincronizado + " " +
            "FROM " + TABLE_NAME + " " +
            "WHERE " + Sincronizado + " = 0 " +
            "ORDER BY " + Fechahora + " ASC " +
            "LIMIT 1" ;

}
