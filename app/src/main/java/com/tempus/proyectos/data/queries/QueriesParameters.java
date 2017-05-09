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

        database.execSQL("DELETE FROM PARAMETERS");

        // Parametros Horas de Replicado
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORA_REPLICADO_1','','00:00:00','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORA_REPLICADO_2','','00:00:00','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORA_REPLICADO_3','','00:00:00','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORA_REPLICADO_4','','00:00:00','','0','" + fechahora.getFechahora() + "');");

        //Parametros Usuario y Password
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('USUARIO_TERMINAL','','tempus','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('PASS_TERMINAL','','tempus','','0','" + fechahora.getFechahora() + "');");

        //Parametros Bluetooth
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BT_01','','00:00:00:00:00:00','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BT_02','','00:00:00:00:00:00','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BT_03','','00:00:00:00:00:00','','0','" + fechahora.getFechahora() + "');");

        //Parametros Operacion del Terminal
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('TIPO_TERMINAL','','0','','0','" + fechahora.getFechahora() + "');");

        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('MODO_EVENTO','','0','','0','" + fechahora.getFechahora() + "');");

        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BTN_EVENTO_1','','1','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BTN_EVENTO_2','','2','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BTN_EVENTO_3','','3','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BTN_EVENTO_4','','4','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BTN_EVENTO_5','','5','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BTN_EVENTO_6','','6','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BTN_EVENTO_7','','7','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BTN_EVENTO_8','','8','','0','" + fechahora.getFechahora() + "');");

        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('INTERFACE_ETH','','0','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('INTERFACE_WLAN','','0','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('INTERFACE_PPP','','0','','0','" + fechahora.getFechahora() + "');");

        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('TIEMPO_MARCACION','','5','','0','" + fechahora.getFechahora() + "');");

        //Parametros
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('PARAMETERS_MARCACIONES','','TECLADO_MANO,DNI_MANO','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('TECLADO_MANO','','1,0;9,1','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('DNI_MANO','','2,0;9,1','','0','" + fechahora.getFechahora() + "');");

        //database.endTransaction();
        this.close();
    }

    public int validateParameterValue(String parametersvalues, int validate){
        /*
            INPUT
            IDPARAMETER,VALUE;IDPARAMETER,VALUE; ...
            example: USUARIO_TERMINAL,tempus;PASS_TERMINAL,tempus

            PROCESS
            Compara cada value de parameter con PARAMETERS.VALUE (solo en el caso que validate = 1 entonces se consulta PARAMETERS.ENABLE)
            Procesa los outputs de cada comparación para dar un solo output
                si todos los outputs[] = 0 entonces output = 0
                si todos los outputs[] = 1 entonces output = 1
                si alguno de los outputs[] = 2 entonces output = 2

            OUTPUT
            0 = value(s) NO coincide con PARAMETERS.VALUE
            1 = value(s) SI coincide con PARAMETERS.VALUE
            2 = PARAMETERS.ENABLE = 0 (solo en el caso que validate = 1 entonces se consulta PARAMETERS.ENABLE)
        */

        String[] parametervalue = parametersvalues.split(";");
        String[] items = new String[2];
        List<Parameters> parametersList = new ArrayList<Parameters>();
        int[] outputs = new int[parametervalue.length];
        int output = 1;

        String a = "";

        for(int i = 0; i < parametervalue.length; i++){

            if(parametervalue[i].split(",").length == 1){
                items[0] = parametervalue[i].substring(0,parametervalue[i].length() - 1);
                items[1] = "";
            }else if(parametervalue[i].split(",").length == 2){
                items = parametervalue[i].split(",");
            }

            parametersList = this.select_one_row(items[0]);

            //Log.d("Autorizaciones","IDPARAMETER: " + items[0]);
            //Log.d("Autorizaciones","VALUE: " + parametersList.get(0).getValue() + " = " + items[1]);
            //Log.d("Autorizaciones","validate: " + validate);

            if(parametersList.size() > 0){
                //Valida si se consulta ENABLE en la tabla PARAMETERS
                if(validate == 1){
                    if(parametersList.get(0).getEnable() == 1){
                        if(parametersList.get(0).getValue().equals(items[1])){
                            outputs[i] = 1;
                        }else{
                            outputs[i] = 0;
                        }
                    }else{
                        outputs[i] = 2;
                    }
                }else if(validate == 0){
                    if(parametersList.get(0).getValue().equals(items[1])){
                        outputs[i] = 1;
                    }else{
                        outputs[i] = 0;
                    }
                }
            }else{
                outputs[i] = 0;
            }

        }

        //Procesar respuesta final
        for(int i = 0; i < outputs.length; i++){
            if(outputs[i] == 0){
                output = output * outputs[i];
            }else if(outputs[i] == 1){
                output = output * outputs[i];
            }else if(outputs[i] == 2){
                output = 2;
            }
        }

        return output;
    }

    public Parameters selectParameter(String idparameter){

        // Select un registro de la tabla Parameters

        Parameters parameters = new Parameters();

        String query = TableParameters.SELECT_TABLE + " " +
                "WHERE " + TableParameters.Idparameter + " = ? " +
                " LIMIT 1;";

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
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return parameters;
    }


    public long updateParameter(Parameters parameters){

        // Update un registro de la tabla Parameters

        this.open();
        long rowaffected = -1;
        ContentValues contentValues = new ContentValues();

        contentValues.put(TableParameters.Parameter, parameters.Parameter);
        contentValues.put(TableParameters.Value, parameters.Value);
        contentValues.put(TableParameters.Subparameters, parameters.Subparameters);
        contentValues.put(TableParameters.Enable, parameters.Enable);
        contentValues.put(TableParameters.FechaHoraSinc, parameters.FechaHoraSinc);

        rowaffected = database.update(TableParameters.TABLE_NAME,contentValues,TableParameters.Idparameter + " = ? ",new String[] { parameters.getIdparameter() });
        this.close();

        return rowaffected;

    }


    public String idparameterToValue(String idparameter){

        // Select un registro de la tabla Parameters

        Parameters parameters = new Parameters();

        String query = TableParameters.SELECT_TABLE + " " +
                "WHERE " + TableParameters.Idparameter + " = ? " +
                " LIMIT 1;";

        Log.d("Autorizaciones","Parameters idparameterToValue: " + query + " - " + idparameter);

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
                Log.d("Autorizaciones","parameters = " + parameters.toString());
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return parameters.getValue();
    }


}

