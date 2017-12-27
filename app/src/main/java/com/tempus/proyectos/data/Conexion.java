package com.tempus.proyectos.data;

/**
 * Created by gurrutiam on 03/11/2016.
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tempus.proyectos.data.queries.QueriesAutorizaciones;
import com.tempus.proyectos.data.queries.QueriesBiometrias;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.data.tables.TableEmpresas;
import com.tempus.proyectos.data.tables.TableEstados;
import com.tempus.proyectos.data.tables.TableLlamadas;
import com.tempus.proyectos.data.tables.TableLogTerminal;
import com.tempus.proyectos.data.tables.TableMarcaciones;
import com.tempus.proyectos.data.tables.TableParameters;
import com.tempus.proyectos.data.tables.TablePerTipolectTerm;
import com.tempus.proyectos.data.tables.TablePersonal;
import com.tempus.proyectos.data.tables.TablePersonalTipolectoraBiometria;
import com.tempus.proyectos.data.tables.TableServicios;
import com.tempus.proyectos.data.tables.TableTarjetaPersonalTipolectora;
import com.tempus.proyectos.data.tables.TableTerminal;
import com.tempus.proyectos.data.tables.TableTerminalConfiguracion;
import com.tempus.proyectos.data.tables.TableTerminalSerial;
import com.tempus.proyectos.data.tables.TableTerminalServicios;
import com.tempus.proyectos.data.tables.TableTerminalTipolect;
import com.tempus.proyectos.data.tables.TableTipoDetalleBiometria;
import com.tempus.proyectos.data.tables.TableTipoLectora;
import com.tempus.proyectos.data.view.ViewAutorizaciones;
import com.tempus.proyectos.data.view.ViewBiometrias;


public class Conexion extends SQLiteOpenHelper{

    private String TAG = "DA-CO";


    static final String DB_NAME = "TEMPUSPLUS.db";

    static final int DB_VERSION = 1;




    public Conexion(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // OBJETOS TABLAS


        TableEmpresas tableEmpresas = new TableEmpresas();
        TableEstados tableEstados = new TableEstados();
        TableMarcaciones tableMarcaciones = new TableMarcaciones();
        TablePersonal tablePersonal = new TablePersonal();
        TablePersonalTipolectoraBiometria tablePersonalTipolectoraBiometria = new TablePersonalTipolectoraBiometria();
        TablePerTipolectTerm tablePerTipolectTerm = new TablePerTipolectTerm();
        TableServicios tableServicios = new TableServicios();
        TableTarjetaPersonalTipolectora tableTarjetaPersonalTipolectora = new TableTarjetaPersonalTipolectora();
        TableTerminal tableTerminal = new TableTerminal();
        TableTerminalConfiguracion tableTerminalConfiguracion = new TableTerminalConfiguracion();
        TableTerminalServicios tableTerminalServicios = new TableTerminalServicios();
        TableTerminalTipolect tableTerminalTipolect = new TableTerminalTipolect();
        TableTipoDetalleBiometria tableTipoDetalleBiometria = new TableTipoDetalleBiometria();
        TableTipoLectora tableTipoLectora = new TableTipoLectora();
        TableLlamadas tableLlamadas = new TableLlamadas();
        TableTerminalSerial terminalSerial = new TableTerminalSerial();
        TableParameters tableParameters = new TableParameters();

        TableLogTerminal tableLogTerminal = new TableLogTerminal();


        // -----------------------------------------------------
        // CREACION DE TABLAS
        // -----------------------------------------------------
        // CREATE tableEmpresas
        Log.d("Autorizaciones",tableEmpresas.CREATE_TABLE);
        try{
            db.execSQL(tableEmpresas.DROP_TABLE);
            db.execSQL(tableEmpresas.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableEstados.CREATE_TABLE);
        // CREATE tableEstados
        try{
            db.execSQL(tableEstados.DROP_TABLE);
            db.execSQL(tableEstados.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableMarcaciones.CREATE_TABLE);
        // CREATE tableMarcaciones
        try{
            //No Elimina Tabla por Seguridad
            //db.execSQL(tableMarcaciones.DROP_TABLE);
            db.execSQL(tableMarcaciones.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tablePersonal.CREATE_TABLE);
        // CREATE tablePersonal
        try{
            db.execSQL(tablePersonal.DROP_TABLE);
            db.execSQL(tablePersonal.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tablePersonalTipolectoraBiometria.CREATE_TABLE);
        // CREATE TtblePersonalTipolectoraBiometria
        try{
            db.execSQL(tablePersonalTipolectoraBiometria.DROP_TABLE);
            db.execSQL(tablePersonalTipolectoraBiometria.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tablePerTipolectTerm.CREATE_TABLE);
        // CREATE tablePerTipolectTerm
        try{
            db.execSQL(tablePerTipolectTerm.DROP_TABLE);
            db.execSQL(tablePerTipolectTerm.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableServicios.CREATE_TABLE);
        // CREATE tableServicios
        try{
            db.execSQL(tableServicios.DROP_TABLE);
            db.execSQL(tableServicios.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableTarjetaPersonalTipolectora.CREATE_TABLE);
        // CREATE tableTarjetaPersonalTipolectora
        try{
            db.execSQL(tableTarjetaPersonalTipolectora.DROP_TABLE);
            db.execSQL(tableTarjetaPersonalTipolectora.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableTerminal.CREATE_TABLE);
        // CREATE tableTerminal
        try{
            //db.execSQL(tableTerminal.DROP_TABLE);
            db.execSQL(tableTerminal.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableTerminalConfiguracion.CREATE_TABLE);
        // CREATE tableTerminalConfiguracion
        try{
            db.execSQL(tableTerminalConfiguracion.DROP_TABLE);
            db.execSQL(tableTerminalConfiguracion.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableTerminalServicios.CREATE_TABLE);
        // CREATE tableTerminalServicios
        try{
            db.execSQL(tableTerminalServicios.DROP_TABLE);
            db.execSQL(tableTerminalServicios.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableTerminalTipolect.CREATE_TABLE);
        // CREATE tableTerminalTipolect
        try{
            db.execSQL(tableTerminalTipolect.DROP_TABLE);
            db.execSQL(tableTerminalTipolect.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableTipoDetalleBiometria.CREATE_TABLE);
        // CREATE tableTipoDetalleBiometria
        try{
            db.execSQL(tableTipoDetalleBiometria.DROP_TABLE);
            db.execSQL(tableTipoDetalleBiometria.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------
        Log.d("Autorizaciones",tableTipoLectora.CREATE_TABLE);
        // CREATE tableTipoLectora
        try{
            db.execSQL(tableTipoLectora.DROP_TABLE);
            db.execSQL(tableTipoLectora.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------


        // ------------------------------------------------------
        Log.d("Autorizaciones",tableLlamadas.CREATE_TABLE);
        // CREATE tableTipoLectora
        try{
            db.execSQL(tableLlamadas.DROP_TABLE);
            db.execSQL(tableLlamadas.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------


        // ------------------------------------------------------
        Log.d("Autorizaciones",terminalSerial.CREATE_TABLE);
        // CREATE tableTipoLectora
        try{
            db.execSQL(terminalSerial.DROP_TABLE);
            db.execSQL(terminalSerial.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------


        // ------------------------------------------------------
        Log.d("Autorizaciones",tableParameters.CREATE_TABLE);
        // CREATE tableTipoLectora
        try{
            db.execSQL(tableParameters.DROP_TABLE);
            db.execSQL(tableParameters.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------

        // ------------------------------------------------------
        Log.v(TAG,tableLogTerminal.CREATE_TABLE);
        // CREATE tableTipoLectora
        try{
            db.execSQL(tableLogTerminal.DROP_TABLE);
            db.execSQL(tableLogTerminal.CREATE_TABLE);
        }catch (SQLException e){
            //Log.d("Autorizaciones",e.getMessage());
        }
        // ------------------------------------------------------

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        // OBJETOS TABLAS
        //TableEmpresas tableEmpresas = new TableEmpresas();
        //ELIMINACION DE TABLAS
        //db.execSQL(tableEmpresas.DROP_TABLE);

        onCreate(db);

    }
}
