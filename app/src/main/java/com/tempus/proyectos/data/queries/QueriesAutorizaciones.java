package com.tempus.proyectos.data.queries;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.view.ViewAutorizaciones;

/**
 * Created by gurrutiam on 15/11/2016.
 */

public class QueriesAutorizaciones {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesAutorizaciones() {

    }
    public QueriesAutorizaciones(Context context) {
        this.context = context;
    }

    public QueriesAutorizaciones open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void drop(){
        this.open();
        database.execSQL(ViewAutorizaciones.DROP_VIEW);
        Log.d("Autorizaciones","Vista eliminada exitosamente");
        this.close();
    }

    public void create(){
        this.open();
        database.execSQL(
                "CREATE VIEW IF NOT EXISTS " + ViewAutorizaciones.VIEW_NAME + " AS " +
                        "SELECT " + "" +
                        "PERSONAL.EMPRESA AS " + ViewAutorizaciones.Empresa + "," +
                        "PERSONAL.CODIGO AS " + ViewAutorizaciones.Codigo + "," +
                        "PERSONAL.APELLIDO_PATERNO AS " + ViewAutorizaciones.ApellidoPaterno + "," +
                        "PERSONAL.APELLIDO_MATERNO AS " + ViewAutorizaciones.ApellidoMaterno + "," +
                        "PERSONAL.NOMBRES AS " + ViewAutorizaciones.Nombres + "," +
                        "PERSONAL.ICONO AS " + ViewAutorizaciones.Icono + "," +
                        "ESTADOS.REQUIERE_ASISTENCIA AS " + ViewAutorizaciones.EstadoRequiereAsistencia + "," +
                        "TERMINAL_TIPOLECT.FLAG AS " + ViewAutorizaciones.FlagTerminalTipolect + "," +
                        "PER_TIPOLECT_TERM.FLAG AS " + ViewAutorizaciones.FlagPerTipolectTerm + "," +
                        "TERMINAL_TIPOLECT.IDTERMINAL AS " + ViewAutorizaciones.Idterminal + "," +
                        "TARJETA_PERSONAL_TIPOLECTORA.ID_TIPO_LECT AS " + ViewAutorizaciones.IdTipoLect + "," +
                        "TARJETA_PERSONAL_TIPOLECTORA.VALOR_TARJETA AS " + ViewAutorizaciones.ValorTarjeta + "," +
                        "CASE " + "" +
                        "WHEN ESTADOS.REQUIERE_ASISTENCIA = 1 " + "" +
                        "AND TERMINAL_TIPOLECT.FLAG = 1 " + "" +
                        "AND PER_TIPOLECT_TERM.FLAG = 1 " + "" +
                        "AND LENGTH(TARJETA_PERSONAL_TIPOLECTORA.VALOR_TARJETA) > 0 " + "" +
                        "THEN 'MARCACION AUTORIZADA' " + "" +
                        "ELSE 'MARCACION NO AUTORIZADA' " + "" +
                        "END AS " + ViewAutorizaciones.Mensaje + "," +
                        "CASE " + "" +
                        "WHEN ESTADOS.REQUIERE_ASISTENCIA = 1 " + "" +
                        "AND TERMINAL_TIPOLECT.FLAG = 1 " + "" +
                        "AND PER_TIPOLECT_TERM.FLAG = 1 " + "" +
                        "AND LENGTH(TARJETA_PERSONAL_TIPOLECTORA.VALOR_TARJETA) > 0 " + "" +
                        "THEN '' " + "" +
                        "WHEN ESTADOS.REQUIERE_ASISTENCIA = 0 " + "" +
                        "THEN 'ESTADO NO PERMITE MARCACION' " + "" +
                        "WHEN TERMINAL_TIPOLECT.FLAG = 0 " + "" +
                        "THEN 'LECTORA NO HABILITADA' " + "" +
                        "WHEN PER_TIPOLECT_TERM.FLAG = 0 " + "" +
                        "THEN 'PERMISO NO HABILITADO' " + "" +
                        "ELSE '' " + "" +
                        "END AS " + ViewAutorizaciones.MensajeDetalle + " " +
                        "FROM PERSONAL AS PERSONAL " + "" +
                        "LEFT OUTER JOIN ESTADOS AS ESTADOS " + "" +
                        "ON PERSONAL.ESTADO = ESTADOS.ESTADO " + "" +
                        "LEFT OUTER JOIN TARJETA_PERSONAL_TIPOLECTORA AS TARJETA_PERSONAL_TIPOLECTORA " + "" +
                        "ON PERSONAL.EMPRESA = TARJETA_PERSONAL_TIPOLECTORA.EMPRESA " + "" +
                        "AND PERSONAL.CODIGO = TARJETA_PERSONAL_TIPOLECTORA.CODIGO " + "" +
                        "LEFT OUTER JOIN PER_TIPOLECT_TERM AS PER_TIPOLECT_TERM " + "" +
                        "ON TARJETA_PERSONAL_TIPOLECTORA.EMPRESA = PER_TIPOLECT_TERM.EMPRESA " + "" +
                        "AND TARJETA_PERSONAL_TIPOLECTORA.CODIGO = PER_TIPOLECT_TERM.CODIGO " + "" +
                        "AND TARJETA_PERSONAL_TIPOLECTORA.ID_TIPO_LECT = PER_TIPOLECT_TERM.ID_TIPO_LECT " + "" +
                        "LEFT OUTER JOIN TERMINAL_TIPOLECT AS TERMINAL_TIPOLECT " + "" +
                        "ON PER_TIPOLECT_TERM.ID_TERMINAL_TIPOLECT = TERMINAL_TIPOLECT.ID_TERMINAL_TIPOLECT " + "" +
                        "WHERE (PERSONAL.ESTADO != '002' OR PERSONAL.FECHA_DE_CESE IS NULL)" +
                        ";"
        );
        Log.d("Autorizaciones",
                "CREATE VIEW IF NOT EXISTS " + ViewAutorizaciones.VIEW_NAME + " AS " +
                        "SELECT " + "" +
                        "PERSONAL.EMPRESA AS " + ViewAutorizaciones.Empresa + "," +
                        "PERSONAL.CODIGO AS " + ViewAutorizaciones.Codigo + "," +
                        "PERSONAL.APELLIDO_PATERNO AS " + ViewAutorizaciones.ApellidoPaterno + "," +
                        "PERSONAL.APELLIDO_MATERNO AS " + ViewAutorizaciones.ApellidoMaterno + "," +
                        "PERSONAL.NOMBRES AS " + ViewAutorizaciones.Nombres + "," +
                        "PERSONAL.ICONO AS " + ViewAutorizaciones.Icono + "," +
                        "ESTADOS.REQUIERE_ASISTENCIA AS " + ViewAutorizaciones.EstadoRequiereAsistencia + "," +
                        "TERMINAL_TIPOLECT.FLAG AS " + ViewAutorizaciones.FlagTerminalTipolect + "," +
                        "PER_TIPOLECT_TERM.FLAG AS " + ViewAutorizaciones.FlagPerTipolectTerm + "," +
                        "TERMINAL_TIPOLECT.IDTERMINAL AS " + ViewAutorizaciones.Idterminal + "," +
                        "TARJETA_PERSONAL_TIPOLECTORA.ID_TIPO_LECT AS " + ViewAutorizaciones.IdTipoLect + "," +
                        "TARJETA_PERSONAL_TIPOLECTORA.VALOR_TARJETA AS " + ViewAutorizaciones.ValorTarjeta + "," +
                        "CASE " + "" +
                        "WHEN ESTADOS.REQUIERE_ASISTENCIA = 1 " + "" +
                        "AND TERMINAL_TIPOLECT.FLAG = 1 " + "" +
                        "AND PER_TIPOLECT_TERM.FLAG = 1 " + "" +
                        "AND LENGTH(TARJETA_PERSONAL_TIPOLECTORA.VALOR_TARJETA) > 0 " + "" +
                        "THEN 'MARCACION AUTORIZADA' " + "" +
                        "ELSE 'MARCACION NO AUTORIZADA' " + "" +
                        "END AS " + ViewAutorizaciones.Mensaje + "," +
                        "CASE " + "" +
                        "WHEN ESTADOS.REQUIERE_ASISTENCIA = 1 " + "" +
                        "AND TERMINAL_TIPOLECT.FLAG = 1 " + "" +
                        "AND PER_TIPOLECT_TERM.FLAG = 1 " + "" +
                        "AND LENGTH(TARJETA_PERSONAL_TIPOLECTORA.VALOR_TARJETA) > 0 " + "" +
                        "THEN '' " + "" +
                        "WHEN ESTADOS.REQUIERE_ASISTENCIA = 0 " + "" +
                        "THEN 'ESTADO NO PERMITE MARCACION' " + "" +
                        "WHEN TERMINAL_TIPOLECT.FLAG = 0 " + "" +
                        "THEN 'LECTORA NO HABILITADA' " + "" +
                        "WHEN PER_TIPOLECT_TERM.FLAG = 0 " + "" +
                        "THEN 'PERMISO NO HABILITADO' " + "" +
                        "ELSE '' " + "" +
                        "END AS " + ViewAutorizaciones.MensajeDetalle + " " +
                        "FROM PERSONAL AS PERSONAL " + "" +
                        "LEFT OUTER JOIN ESTADOS AS ESTADOS " + "" +
                        "ON PERSONAL.ESTADO = ESTADOS.ESTADO " + "" +
                        "LEFT OUTER JOIN TARJETA_PERSONAL_TIPOLECTORA AS TARJETA_PERSONAL_TIPOLECTORA " + "" +
                        "ON PERSONAL.EMPRESA = TARJETA_PERSONAL_TIPOLECTORA.EMPRESA " + "" +
                        "AND PERSONAL.CODIGO = TARJETA_PERSONAL_TIPOLECTORA.CODIGO " + "" +
                        "LEFT OUTER JOIN PER_TIPOLECT_TERM AS PER_TIPOLECT_TERM " + "" +
                        "ON TARJETA_PERSONAL_TIPOLECTORA.EMPRESA = PER_TIPOLECT_TERM.EMPRESA " + "" +
                        "AND TARJETA_PERSONAL_TIPOLECTORA.CODIGO = PER_TIPOLECT_TERM.CODIGO " + "" +
                        "AND TARJETA_PERSONAL_TIPOLECTORA.ID_TIPO_LECT = PER_TIPOLECT_TERM.ID_TIPO_LECT " + "" +
                        "LEFT OUTER JOIN TERMINAL_TIPOLECT AS TERMINAL_TIPOLECT " + "" +
                        "ON PER_TIPOLECT_TERM.ID_TERMINAL_TIPOLECT = TERMINAL_TIPOLECT.ID_TERMINAL_TIPOLECT " + "" +
                        "WHERE (PERSONAL.ESTADO != '002' OR PERSONAL.FECHA_DE_CESE IS NULL)" +
                        ";"
        );
        Log.d("Autorizaciones","Vista creada exitosamente");
        this.close();
    }



    public List<Autorizaciones> select(){
        Autorizaciones autorizaciones = new Autorizaciones();

        List<Autorizaciones> autorizacionesList =  new ArrayList<Autorizaciones>();

        String query = "SELECT " +
                ViewAutorizaciones.Empresa + ", " +
                ViewAutorizaciones.Codigo + ", " +
                ViewAutorizaciones.ApellidoPaterno + ", " +
                ViewAutorizaciones.ApellidoMaterno + ", " +
                ViewAutorizaciones.Nombres + ", " +
                ViewAutorizaciones.Icono + ", " +
                ViewAutorizaciones.EstadoRequiereAsistencia + ", " +
                ViewAutorizaciones.FlagPerTipolectTerm + ", " +
                ViewAutorizaciones.FlagTerminalTipolect + ", " +
                ViewAutorizaciones.Idterminal + ", " +
                ViewAutorizaciones.IdTipoLect + ", " +
                ViewAutorizaciones.ValorTarjeta + ", " +
                ViewAutorizaciones.Mensaje + ", " +
                ViewAutorizaciones.MensajeDetalle + " " +
                "FROM " + ViewAutorizaciones.VIEW_NAME;

        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToNext()){
            do{
                autorizaciones = new Autorizaciones();

                autorizaciones.setEmpresa(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Empresa)));
                autorizaciones.setCodigo(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Codigo)));
                autorizaciones.setApellidoPaterno(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.ApellidoPaterno)));
                autorizaciones.setApellidoMaterno(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.ApellidoMaterno)));
                autorizaciones.setNombres(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Nombres)));
                autorizaciones.setIcono(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Icono)));
                autorizaciones.setEstadoRequiereAsistencia(cursor.getInt(cursor.getColumnIndex(ViewAutorizaciones.EstadoRequiereAsistencia)));
                autorizaciones.setFlagPerTipoLectTerm(cursor.getInt(cursor.getColumnIndex(ViewAutorizaciones.FlagPerTipolectTerm)));
                autorizaciones.setFlagTerminalTipoLect(cursor.getInt(cursor.getColumnIndex(ViewAutorizaciones.FlagTerminalTipolect)));
                autorizaciones.setIdterminal(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Idterminal)));
                autorizaciones.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(ViewAutorizaciones.IdTipoLect)));
                autorizaciones.setValorTarjeta(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.ValorTarjeta)));
                autorizaciones.setMensaje(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Mensaje)));
                autorizaciones.setMensajeDetalle(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.MensajeDetalle)));
                autorizacionesList.add(autorizaciones);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return autorizacionesList;
    }


    public List<Autorizaciones> buscarAutorizaciones(String ValorTarjeta, int IdTipoLect, String Idterminal){
        Autorizaciones autorizaciones = new Autorizaciones();

        List<Autorizaciones> autorizacionesList =  new ArrayList<Autorizaciones>();

        String query = "SELECT " +
                ViewAutorizaciones.Empresa + ", " +
                ViewAutorizaciones.Codigo + ", " +
                ViewAutorizaciones.ApellidoPaterno + ", " +
                ViewAutorizaciones.ApellidoMaterno + ", " +
                ViewAutorizaciones.Nombres + ", " +
                ViewAutorizaciones.Icono + ", " +
                ViewAutorizaciones.EstadoRequiereAsistencia + ", " +
                ViewAutorizaciones.FlagPerTipolectTerm + ", " +
                ViewAutorizaciones.FlagTerminalTipolect + ", " +
                ViewAutorizaciones.IdTipoLect + ", " +
                ViewAutorizaciones.ValorTarjeta + ", " +
                ViewAutorizaciones.Mensaje + ", " +
                ViewAutorizaciones.MensajeDetalle + " " +
                "FROM " + ViewAutorizaciones.VIEW_NAME + " " +
                "WHERE " + ViewAutorizaciones.IdTipoLect + " = ? " +
                "AND " + ViewAutorizaciones.ValorTarjeta + " = ? " +
                "AND " + ViewAutorizaciones.Idterminal + " = ? " +
                "LIMIT 1;";

        Log.d("Autorizaciones",query);

        this.open();
        Cursor cursor = database.rawQuery(query, new String[] { String.valueOf(IdTipoLect), ValorTarjeta,  Idterminal});


        if(cursor.moveToNext()){
            do{
                autorizaciones = new Autorizaciones();

                autorizaciones.setEmpresa(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Empresa)));
                autorizaciones.setCodigo(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Codigo)));
                autorizaciones.setApellidoPaterno(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.ApellidoPaterno)));
                autorizaciones.setApellidoMaterno(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.ApellidoMaterno)));
                autorizaciones.setNombres(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Nombres)));
                autorizaciones.setIcono(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Icono)));
                autorizaciones.setEstadoRequiereAsistencia(cursor.getInt(cursor.getColumnIndex(ViewAutorizaciones.EstadoRequiereAsistencia)));
                autorizaciones.setFlagPerTipoLectTerm(cursor.getInt(cursor.getColumnIndex(ViewAutorizaciones.FlagPerTipolectTerm)));
                autorizaciones.setFlagTerminalTipoLect(cursor.getInt(cursor.getColumnIndex(ViewAutorizaciones.FlagTerminalTipolect)));
                autorizaciones.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(ViewAutorizaciones.IdTipoLect)));
                autorizaciones.setValorTarjeta(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.ValorTarjeta)));
                autorizaciones.setMensaje(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.Mensaje)));
                autorizaciones.setMensajeDetalle(cursor.getString(cursor.getColumnIndex(ViewAutorizaciones.MensajeDetalle)));
                autorizacionesList.add(autorizaciones);

                //Log.d("Autorizaciones",autorizaciones.toString());

            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return autorizacionesList;
    }


}

