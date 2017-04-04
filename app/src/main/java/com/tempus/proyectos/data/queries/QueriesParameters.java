package com.tempus.proyectos.data.queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.model.Llamadas;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.tables.TableLlamadas;
import com.tempus.proyectos.data.tables.TableParameters;
import com.tempus.proyectos.util.Fechahora;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurrutiam on 30/03/2017.
 */

public class QueriesParameters {
    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesParameters(Context context) {
        this.context = context;
    }

    public QueriesParameters open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public long insert(Parameters parameters){

        this.open();
        long rowaffected = -1;
        //conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableParameters.Idparameter, parameters.Idparameter);
        contentValues.put(TableParameters.Parameter, parameters.Parameter);
        contentValues.put(TableParameters.Value, parameters.Value);
        contentValues.put(TableParameters.Subparameters, parameters.Subparameters);
        contentValues.put(TableParameters.Enable, parameters.Enable);
        contentValues.put(TableParameters.FechaHoraSinc, parameters.FechaHoraSinc);


        rowaffected = database.insert(TableParameters.TABLE_NAME, null, contentValues);

        this.close();

        return rowaffected;

    }

    public void update(Parameters parameters){

        this.open();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TableParameters.Parameter, parameters.Parameter);
        contentValues.put(TableParameters.Value, parameters.Value);
        contentValues.put(TableParameters.Subparameters, parameters.Subparameters);
        contentValues.put(TableParameters.Enable, parameters.Enable);
        contentValues.put(TableParameters.FechaHoraSinc, parameters.FechaHoraSinc);

        database.update(TableParameters.TABLE_NAME,contentValues,TableParameters.Idparameter + " = ? ",new String[] { parameters.getIdparameter() });
        this.close();

    }


    public List<Parameters> select(){
        Parameters parameters = new Parameters();
        List<Parameters> parametersList =  new ArrayList<Parameters>();

        this.open();
        Cursor cursor = database.rawQuery(TableParameters.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                parameters = new Parameters();
                parameters.setIdparameter(cursor.getString(cursor.getColumnIndex(TableParameters.Idparameter)));
                parameters.setParameter(cursor.getString(cursor.getColumnIndex(TableParameters.Parameter)));
                parameters.setValue(cursor.getString(cursor.getColumnIndex(TableParameters.Value)));
                parameters.setSubparameters(cursor.getString(cursor.getColumnIndex(TableParameters.Subparameters)));
                parameters.setEnable(cursor.getInt(cursor.getColumnIndex(TableParameters.Enable)));
                parameters.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableParameters.FechaHoraSinc)));

                parametersList.add(parameters);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return parametersList;
    }


    public List<Parameters> select_one_row(String idparameter){

        Parameters parameters = new Parameters();
        List<Parameters> parametersList =  new ArrayList<Parameters>();

        String query = TableParameters.SELECT_TABLE + " " +
                "WHERE " + TableParameters.Idparameter + " = ? ";

        //Log.d("Autorizaciones","Parameters select_one_row: " + query + " - " + idparameter);

        this.open();
        Cursor cursor = database.rawQuery(query, new String[] { String.valueOf(idparameter)});
        if(cursor.moveToNext()){
            do{
                parameters = new Parameters();
                parameters.setIdparameter(cursor.getString(cursor.getColumnIndex(TableParameters.Idparameter)));
                parameters.setParameter(cursor.getString(cursor.getColumnIndex(TableParameters.Parameter)));
                parameters.setValue(cursor.getString(cursor.getColumnIndex(TableParameters.Value)));
                parameters.setSubparameters(cursor.getString(cursor.getColumnIndex(TableParameters.Subparameters)));
                parameters.setEnable(cursor.getInt(cursor.getColumnIndex(TableParameters.Enable)));
                parameters.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableParameters.FechaHoraSinc)));

                parametersList.add(parameters);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return parametersList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableParameters.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }

    public void poblar(){
        Fechahora fechahora =  new Fechahora();

        this.open();
        //database.beginTransaction();

        database.execSQL("DELETE FROM LLAMADAS");

        // Parametros Horas de Replicado
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORA_REPLICADO_1','','00:00:00','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORA_REPLICADO_2','','00:00:00','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORA_REPLICADO_3','','00:00:00','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORA_REPLICADO_4','','00:00:00','','0','" + fechahora.getFechahora() + "');");

        //Parametros Usuario y Password
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('USUARIO_TERMINAL','','tempus','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('PASS_TERMINAL','','tempus','','0','" + fechahora.getFechahora() + "');");
        //database.endTransaction();
        this.close();
    }


}

