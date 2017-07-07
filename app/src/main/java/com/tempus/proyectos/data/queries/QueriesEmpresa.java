package com.tempus.proyectos.data.queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.model.Empresas;
import com.tempus.proyectos.data.tables.TableEmpresas;

/**
 * Created by gurrutiam on 08/11/2016.
 */

public class QueriesEmpresa {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesEmpresa(Context context) {
        this.context = context;
    }

    public QueriesEmpresa open() throws SQLException{
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public void insert(Empresas empresas){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableEmpresas.Empresa, empresas.Empresa);
        contentValues.put(TableEmpresas.NombreCorto, empresas.NombreCorto);
        contentValues.put(TableEmpresas.Nombre, empresas.Nombre);
        contentValues.put(TableEmpresas.Icono, empresas.Icono);
        contentValues.put(TableEmpresas.Direccion, empresas.Direccion);
        contentValues.put(TableEmpresas.Ruc, empresas.Ruc);
        contentValues.put(TableEmpresas.Bloqueado, empresas.Bloqueado);
        contentValues.put(TableEmpresas.FechaHoraSinc, empresas.FechaHoraSinc);
        database.insert(TableEmpresas.TABLE_NAME, null, contentValues);

    }

    public List<Empresas> select(){
        Empresas empresas = new Empresas();
        List<Empresas> empresasList =  new ArrayList<Empresas>();

        String query = "SELECT " +
                TableEmpresas.Empresa + ", " +
                TableEmpresas.NombreCorto + ", " +
                TableEmpresas.Nombre + ", " +
                TableEmpresas.Icono + ", " +
                TableEmpresas.Direccion + ", " +
                TableEmpresas.Ruc + ", " +
                TableEmpresas.Bloqueado + ", " +
                TableEmpresas.FechaHoraSinc + " " +
                "FROM " + TableEmpresas.TABLE_NAME;

        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToNext()){
            do{
                empresas = new Empresas();
                empresas.setEmpresa(cursor.getString(cursor.getColumnIndex(TableEmpresas.Empresa)));
                empresas.setNombreCorto(cursor.getString(cursor.getColumnIndex(TableEmpresas.NombreCorto)));
                empresas.setNombre(cursor.getString(cursor.getColumnIndex(TableEmpresas.Nombre)));
                empresas.setIcono(cursor.getString(cursor.getColumnIndex(TableEmpresas.Icono)));
                empresas.setDireccion(cursor.getString(cursor.getColumnIndex(TableEmpresas.Direccion)));
                empresas.setRuc(cursor.getString(cursor.getColumnIndex(TableEmpresas.Ruc)));
                empresas.setBloqueado(cursor.getInt(cursor.getColumnIndex(TableEmpresas.Bloqueado)));
                empresas.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableEmpresas.FechaHoraSinc)));

                empresasList.add(empresas);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return empresasList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableEmpresas.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }

    public void delete(){
        database.delete(TableEmpresas.TABLE_NAME, null, null);
    }


}
