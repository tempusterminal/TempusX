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
        database.close();
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
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BT_00','','00:00:00:00:00:00,1234','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BT_01','','00:00:00:00:00:00,1234','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BT_02','','00:00:00:00:00:00,1234','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BT_03','','00:00:00:00:00:00,1234','','0','" + fechahora.getFechahora() + "');");

        //Parametros Operacion del Terminal
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('TIPO_TERMINAL','','0','','0','" + fechahora.getFechahora() + "');");

        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('MODO_EVENTO','','false','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('BOTONES_EVENTO','','00000000','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('TEXTO_BOTONES_EVENTO','','1,2,3,4,5,6,7,8','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('VALOR_BOTONES_EVENTO','','001,002,003,004,005,006,007,008','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('MODO_PRINTER','','false','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('VALUES_PRINTER_MESSAGE','','0,1;0,2;0,3;0,4;0,5;0,6','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('PRINTER_MESSAGE','','20 20 20 20','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('MODO_REFRIGERIO','','false','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('MENSAJE_MARCACIONES','','MARCACION AUTORIZADA,;MARCACION AUTORIZADA,NO TIENE PERMISO EN ESTA LECTORA;MARCACION AUTORIZADA,ESTADO NO PERMITE MARCACION;MARCACION REPETIDA,;MARCACION AUTORIZADA,TARJETA/BIOME NO REGISTRADA;MARCACION AUTORIZADA,TARJETA/BIOME NO SE RECONOCE;MARCACION AUTORIZADA,LECTORA NO HABILITADA','','0','" + fechahora.getFechahora() + "');");

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

        //Parametros marcacion
        //database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('PARAMETERS_MARCACIONES','','TECLADO_MANO,DNI_MANO','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('PARAMETERS_MARCACIONES','','','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('TECLADO_MANO','','1,0;10,1','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('DNI_MANO','','2,0;10,1','','0','" + fechahora.getFechahora() + "');");

        //Parametro Relay
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('RELAYS','','RELAY_01,0,00;RELAY_02,0,00','','0','" + fechahora.getFechahora() + "');");
        //Parametros Horarios Relay
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('HORARIOSRELAY','','HORARIO_RELAY_01,00:00:00,00:00:00,0000000;HORARIO_RELAY_02,00:00:00,00:00:00,0000000;HORARIO_RELAY_03,00:00:00,00:00:00,0000000;HORARIO_RELAY_04,00:00:00,00:00:00,0000000;HORARIO_RELAY_05,00:00:00,00:00:00,0000000;HORARIO_RELAY_06,00:00:00,00:00:00,0000000;HORARIO_RELAY_07,00:00:00,00:00:00,0000000;HORARIO_RELAY_08,00:00:00,00:00:00,0000000;HORARIO_RELAY_09,00:00:00,00:00:00,0000000;HORARIO_RELAY_10,00:00:00,00:00:00,0000000;HORARIO_RELAY_11,00:00:00,00:00:00,0000000;HORARIO_RELAY_12,00:00:00,00:00:00,0000000;HORARIO_RELAY_13,00:00:00,00:00:00,0000000;HORARIO_RELAY_14,00:00:00,00:00:00,0000000;HORARIO_RELAY_15,00:00:00,00:00:00,0000000;HORARIO_RELAY_16,00:00:00,00:00:00,0000000;HORARIO_RELAY_17,00:00:00,00:00:00,0000000;HORARIO_RELAY_18,00:00:00,00:00:00,0000000;HORARIO_RELAY_19,00:00:00,00:00:00,0000000;HORARIO_RELAY_20,00:00:00,00:00:00,0000000;HORARIO_RELAY_21,00:00:00,00:00:00,0000000;HORARIO_RELAY_22,00:00:00,00:00:00,0000000;HORARIO_RELAY_23,00:00:00,00:00:00,0000000;HORARIO_RELAY_24,00:00:00,00:00:00,0000000;HORARIO_RELAY_25,00:00:00,00:00:00,0000000;HORARIO_RELAY_26,00:00:00,00:00:00,0000000;HORARIO_RELAY_27,00:00:00,00:00:00,0000000;HORARIO_RELAY_28,00:00:00,00:00:00,0000000;HORARIO_RELAY_29,00:00:00,00:00:00,0000000;HORARIO_RELAY_30,00:00:00,00:00:00,0000000;HORARIO_RELAY_31,00:00:00,00:00:00,0000000;HORARIO_RELAY_32,00:00:00,00:00:00,0000000;','','0','" + fechahora.getFechahora() + "');");

        //Parametros Insert Marcaciones
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('INSERTMARCACIONES','','10000000','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('MARCACIONREPETIDA','','10','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('FOTOPERSONAL','','false','','0','" + fechahora.getFechahora() + "');");

        //Parametros Colors UI
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('COLORSUI','','#cecece,#cecece,#cecece,#777777,#777777,#777777,#777777,#000000,#ffffff','','0','" + fechahora.getFechahora() + "');");

        //Parametros Servicios
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('WEBSERVICE_01','','192.168.0.1,TEMPUS_WS_T10,80,TEMPUS,TEMPUSSCA','','0','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('WEBSERVICEN_01','','192.168.0.1,/Web_ServiceTempus/COntrolador/Direct_WS.php,TEMPUS_WS_T10,80,TEMPUS,TEMPUSSCA','','0','" + fechahora.getFechahora() + "');");

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

