package com.tempus.proyectos.data.queries;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.model.Biometrias;

import com.tempus.proyectos.data.view.ViewBiometrias;

/**
 * Created by gurrutiam on 28/11/2016.
 */

public class QueriesBiometrias {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;


    public QueriesBiometrias() {

    }

    public QueriesBiometrias(Context context) {
        this.context = context;
    }

    public QueriesBiometrias open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public void drop(){
        this.open();
        database.execSQL(ViewBiometrias.DROP_VIEW);
        Log.d("Autorizaciones","Vista eliminada exitosamente");
        this.close();
    }

    public void create(){
        this.open();

        database.execSQL("CREATE VIEW IF NOT EXISTS " + ViewBiometrias.VIEW_NAME + " AS " +
                "SELECT " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.ID_TIPO_DETA_BIO AS " + ViewBiometrias.IdTipoDetaBio + ", " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.INDICE_BIOMETRIA AS " + ViewBiometrias.IndiceBiometria + ", " +
                "PERSONAL.EMPRESA AS " + ViewBiometrias.Empresa + ", " +
                "PERSONAL.CODIGO AS " + ViewBiometrias.Codigo + ", " +
                "PERSONAL.NRO_DOCUMENTO AS " + ViewBiometrias.NroDocumento + ", " +
                "PERSONAL.APELLIDO_PATERNO AS " + ViewBiometrias.ApellidoPaterno + ", " +
                "PERSONAL.APELLIDO_MATERNO AS " + ViewBiometrias.ApellidoMaterno + ", " +
                "PERSONAL.NOMBRES AS " + ViewBiometrias.Nombres + ", " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.ID_TIPO_LECT AS " + ViewBiometrias.IdTipoLect + ", " +
                "CASE " +
                "WHEN PERSONAL_TIPOLECTORA_BIOMETRIA.VALOR_BIOMETRIA IS NULL " +
                "THEN 0 " +
                "ELSE 1 " +
                "END AS " + ViewBiometrias.ValorBiometria + ", " +
                "PER_TIPOLECT_TERM.FLAG AS " + ViewBiometrias.FlagPerTipolectTerm + " " +
                "FROM PERSONAL AS PERSONAL " +
                "INNER JOIN PERSONAL_TIPOLECTORA_BIOMETRIA AS PERSONAL_TIPOLECTORA_BIOMETRIA " +
                "ON PERSONAL.EMPRESA = PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA " +
                "AND PERSONAL.CODIGO = PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO " +
                "INNER JOIN PER_TIPOLECT_TERM AS PER_TIPOLECT_TERM " +
                "ON PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA = PER_TIPOLECT_TERM.EMPRESA " +
                "AND PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO = PER_TIPOLECT_TERM.CODIGO " +
                "AND PERSONAL_TIPOLECTORA_BIOMETRIA.ID_TIPO_LECT = PER_TIPOLECT_TERM.ID_TIPO_LECT" +
                ";"
        );

        Log.d("Autorizaciones","CREATE VIEW IF NOT EXISTS " + ViewBiometrias.VIEW_NAME + " AS " +
                "SELECT " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.ID_TIPO_DETA_BIO AS " + ViewBiometrias.IdTipoDetaBio + ", " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.INDICE_BIOMETRIA AS " + ViewBiometrias.IndiceBiometria + ", " +
                "PERSONAL.EMPRESA AS " + ViewBiometrias.Empresa + ", " +
                "PERSONAL.CODIGO AS " + ViewBiometrias.Codigo + ", " +
                "PERSONAL.NRO_DOCUMENTO AS " + ViewBiometrias.NroDocumento + ", " +
                "PERSONAL.APELLIDO_PATERNO AS " + ViewBiometrias.ApellidoPaterno + ", " +
                "PERSONAL.APELLIDO_MATERNO AS " + ViewBiometrias.ApellidoMaterno + ", " +
                "PERSONAL.NOMBRES AS " + ViewBiometrias.Nombres + ", " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.ID_TIPO_LECT AS " + ViewBiometrias.IdTipoLect + ", " +
                "CASE " +
                "WHEN PERSONAL_TIPOLECTORA_BIOMETRIA.VALOR_BIOMETRIA IS NULL " +
                "THEN 0 " +
                "ELSE 1 " +
                "END AS " + ViewBiometrias.ValorBiometria + ", " +
                "PER_TIPOLECT_TERM.FLAG AS " + ViewBiometrias.FlagPerTipolectTerm + " " +
                "FROM PERSONAL AS PERSONAL " +
                "INNER JOIN PERSONAL_TIPOLECTORA_BIOMETRIA AS PERSONAL_TIPOLECTORA_BIOMETRIA " +
                "ON PERSONAL.EMPRESA = PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA " +
                "AND PERSONAL.CODIGO = PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO " +
                "INNER JOIN PER_TIPOLECT_TERM AS PER_TIPOLECT_TERM " +
                "ON PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA = PER_TIPOLECT_TERM.EMPRESA " +
                "AND PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO = PER_TIPOLECT_TERM.CODIGO " +
                "AND PERSONAL_TIPOLECTORA_BIOMETRIA.ID_TIPO_LECT = PER_TIPOLECT_TERM.ID_TIPO_LECT" +
                ";"
        );

        Log.d("Autorizaciones","Vista creada exitosamente");

        this.close();
    }


    public List<Biometrias> select(){
        Biometrias biometrias = new Biometrias();

        List<Biometrias> biometriasList =  new ArrayList<Biometrias>();

        String query = "SELECT " +
                ViewBiometrias.IdTipoDetaBio + ", " +
                ViewBiometrias.IndiceBiometria + ", " +
                ViewBiometrias.Empresa + ", " +
                ViewBiometrias.Codigo + ", " +
                ViewBiometrias.NroDocumento + ", " +
                ViewBiometrias.ApellidoPaterno + ", " +
                ViewBiometrias.ApellidoMaterno + ", " +
                ViewBiometrias.Nombres + ", " +
                ViewBiometrias.IdTipoLect + ", " +
                ViewBiometrias.ValorBiometria + ", " +
                ViewBiometrias.FlagPerTipolectTerm + " " +
                "FROM " + ViewBiometrias.VIEW_NAME;

        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToNext()){
            do{
                biometrias = new Biometrias();

                biometrias.setIdTipoDetaBio(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.IdTipoDetaBio)));
                biometrias.setIndiceBiometria(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.IndiceBiometria)));
                biometrias.setEmpresa(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Empresa)));
                biometrias.setCodigo(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Codigo)));
                biometrias.setNroDocumento(cursor.getString(cursor.getColumnIndex(ViewBiometrias.NroDocumento)));
                biometrias.setApellidoPaterno(cursor.getString(cursor.getColumnIndex(ViewBiometrias.ApellidoPaterno)));
                biometrias.setApellidoMaterno(cursor.getString(cursor.getColumnIndex(ViewBiometrias.ApellidoMaterno)));
                biometrias.setNombres(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Nombres)));
                biometrias.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.IdTipoLect)));
                biometrias.setValorBiometria(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.ValorBiometria)));
                biometrias.setFlagPerTipoLectTerm(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.FlagPerTipolectTerm)));

                if(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.FlagPerTipolectTerm)) == 1){
                    if(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.ValorBiometria)) == 0){
                        biometrias.setMensaje("1 BIOMETRIA POR ENROLAR");
                    }else if(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.ValorBiometria)) == 1){
                        biometrias.setMensaje("0 BIOMETRIA POR ENROLAR");
                    }
                }else{
                    biometrias.setMensaje("0 PERMISOS PARA ENROLAR");
                }

                biometriasList.add(biometrias);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return biometriasList;
    }

    public List<Biometrias> BuscarBiometrias(int IdTipoLect, String Empresa, String Codigo){
        Biometrias biometrias = new Biometrias();

        List<Biometrias> biometriasList =  new ArrayList<Biometrias>();

        String query = "SELECT " +
                ViewBiometrias.IdTipoDetaBio + ", " +
                ViewBiometrias.IndiceBiometria + ", " +
                ViewBiometrias.Empresa + ", " +
                ViewBiometrias.Codigo + ", " +
                ViewBiometrias.NroDocumento + ", " +
                ViewBiometrias.ApellidoPaterno + ", " +
                ViewBiometrias.ApellidoMaterno + ", " +
                ViewBiometrias.Nombres + ", " +
                ViewBiometrias.IdTipoLect + ", " +
                ViewBiometrias.ValorBiometria + ", " +
                ViewBiometrias.FlagPerTipolectTerm + " " +
                "FROM " + ViewBiometrias.VIEW_NAME + " " +
                "WHERE " + ViewBiometrias.IdTipoLect + " = ? " +
                "AND " + ViewBiometrias.Empresa + " = ? " +
                "AND " + ViewBiometrias.Codigo + " = ? " +
                ";";

        Log.d("Autorizaciones",query);

        this.open();
        Cursor cursor = database.rawQuery(query, new String[] { String.valueOf(IdTipoLect), Empresa, Codigo });
        if(cursor.moveToNext()){
            do{
                biometrias = new Biometrias();

                biometrias.setIdTipoDetaBio(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.IdTipoDetaBio)));
                biometrias.setIndiceBiometria(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.IndiceBiometria)));
                biometrias.setEmpresa(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Empresa)));
                biometrias.setCodigo(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Codigo)));
                biometrias.setNroDocumento(cursor.getString(cursor.getColumnIndex(ViewBiometrias.NroDocumento)));
                biometrias.setApellidoPaterno(cursor.getString(cursor.getColumnIndex(ViewBiometrias.ApellidoPaterno)));
                biometrias.setApellidoMaterno(cursor.getString(cursor.getColumnIndex(ViewBiometrias.ApellidoMaterno)));
                biometrias.setNombres(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Nombres)));
                biometrias.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.IdTipoLect)));
                biometrias.setValorBiometria(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.ValorBiometria)));
                biometrias.setFlagPerTipoLectTerm(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.FlagPerTipolectTerm)));

                if(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.FlagPerTipolectTerm)) == 1){
                    if(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.ValorBiometria)) == 0){
                        biometrias.setMensaje("ENRROLAMIENTO DISPONIBLE");
                    }else if(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.ValorBiometria)) == 1){
                        biometrias.setMensaje("ENRROLAMIENTO NO DISPONIBLE ");
                    }
                }else{
                    biometrias.setMensaje("SIN PERMISOS");
                }

                biometriasList.add(biometrias);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return biometriasList;
    }



    public List<Biometrias> ListarPersonalBiometria(int IdTipoLect, String ValorTarjeta){
        Biometrias biometrias = new Biometrias();
        List<Biometrias> biometriasList =  new ArrayList<Biometrias>();

        String query = "SELECT " +
                "EMPRESAS.EMPRESA || '-' || EMPRESAS.NOMBRE_CORTO AS " + ViewBiometrias.Empresa + ", " +
                "PERSONAL.CODIGO AS " + ViewBiometrias.Codigo + ", " +
                "PERSONAL.NRO_DOCUMENTO AS " + ViewBiometrias.NroDocumento + ", " +
                "PERSONAL.APELLIDO_PATERNO AS " + ViewBiometrias.ApellidoPaterno + ", " +
                "PERSONAL.APELLIDO_MATERNO AS " + ViewBiometrias.ApellidoMaterno + ", " +
                "PERSONAL.NOMBRES AS " + ViewBiometrias.Nombres + ", " +
                "PER_TIPOLECT_TERM.FLAG AS " + ViewBiometrias.FlagPerTipolectTerm + " " +
                "FROM PERSONAL " +
                "INNER JOIN EMPRESAS " +
                "ON EMPRESAS.EMPRESA = PERSONAL.EMPRESA " +
                "INNER JOIN PER_TIPOLECT_TERM " +
                "ON PERSONAL.EMPRESA = PER_TIPOLECT_TERM.EMPRESA " +
                "AND PERSONAL.CODIGO = PER_TIPOLECT_TERM.CODIGO " +
                "WHERE PER_TIPOLECT_TERM.ID_TIPO_LECT = ? " +
                "AND (PERSONAL.CODIGO = ? " +
                "OR PERSONAL.NRO_DOCUMENTO = ?) " +
                "AND (PERSONAL.ESTADO != '002' OR PERSONAL.FECHA_DE_CESE IS NULL) " +
                ";";

        Log.d("Autorizaciones",query);

        this.open();
        Cursor cursor = database.rawQuery(query, new String[] { String.valueOf(IdTipoLect), ValorTarjeta, ValorTarjeta });
        if(cursor.moveToNext()){
            do{
                biometrias = new Biometrias();

                biometrias.setEmpresa(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Empresa)));
                biometrias.setCodigo(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Codigo)));
                biometrias.setNroDocumento(cursor.getString(cursor.getColumnIndex(ViewBiometrias.NroDocumento)));
                biometrias.setApellidoPaterno(cursor.getString(cursor.getColumnIndex(ViewBiometrias.ApellidoPaterno)));
                biometrias.setApellidoMaterno(cursor.getString(cursor.getColumnIndex(ViewBiometrias.ApellidoMaterno)));
                biometrias.setNombres(cursor.getString(cursor.getColumnIndex(ViewBiometrias.Nombres)));
                biometrias.setFlagPerTipoLectTerm(cursor.getInt(cursor.getColumnIndex(ViewBiometrias.FlagPerTipolectTerm)));

                biometriasList.add(biometrias);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return biometriasList;
    }





}
