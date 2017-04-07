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
import com.tempus.proyectos.data.model.Llamadas;
import com.tempus.proyectos.data.tables.TableLlamadas;
import com.tempus.proyectos.util.Fechahora;


/**
 * Created by gurrutiam on 10/01/2017.
 */

public class QueriesLlamadas {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesLlamadas(Context context) {
        this.context = context;
    }

    public QueriesLlamadas open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public long insert(Llamadas llamadas){

        this.open();
        long rowaffected = -1;
        //conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableLlamadas.Idllamada, llamadas.Idllamada);
        contentValues.put(TableLlamadas.Llamada, llamadas.Llamada);
        contentValues.put(TableLlamadas.Parameters, llamadas.Parameters);
        contentValues.put(TableLlamadas.TableName, llamadas.TableName);
        contentValues.put(TableLlamadas.Primarykey, llamadas.Primarykey);
        contentValues.put(TableLlamadas.Columns, llamadas.Columns);
        contentValues.put(TableLlamadas.FechaHoraSinc, llamadas.FechaHoraSinc);


        rowaffected = database.insert(TableLlamadas.TABLE_NAME, null, contentValues);

        this.close();

        return rowaffected;

    }

    public void update(Llamadas llamadas){

        this.open();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TableLlamadas.Llamada, llamadas.Llamada);
        contentValues.put(TableLlamadas.Parameters, llamadas.Parameters);
        contentValues.put(TableLlamadas.TableName, llamadas.TableName);
        contentValues.put(TableLlamadas.Primarykey, llamadas.Primarykey);
        contentValues.put(TableLlamadas.Columns, llamadas.Columns);
        contentValues.put(TableLlamadas.FechaHoraSinc, llamadas.FechaHoraSinc);

        database.update(TableLlamadas.TABLE_NAME,contentValues,TableLlamadas.Idllamada + " = ? ",new String[] { llamadas.getIdllamada() });
        this.close();

    }

    public List<Llamadas> select(){

        Llamadas llamadas = new Llamadas();

        List<Llamadas> llamadasList =  new ArrayList<Llamadas>();

        Cursor cursor = database.rawQuery(TableLlamadas.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                llamadas = new Llamadas();
                llamadas.setIdllamada(cursor.getString(cursor.getColumnIndex(TableLlamadas.Idllamada)));
                llamadas.setLlamada(cursor.getString(cursor.getColumnIndex(TableLlamadas.Llamada)));
                llamadas.setParameters(cursor.getString(cursor.getColumnIndex(TableLlamadas.Parameters)));
                llamadas.setTableName(cursor.getString(cursor.getColumnIndex(TableLlamadas.TableName)));
                llamadas.setPrimarykey(cursor.getString(cursor.getColumnIndex(TableLlamadas.Primarykey)));
                llamadas.setColumns(cursor.getString(cursor.getColumnIndex(TableLlamadas.Columns)));
                llamadas.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableLlamadas.FechaHoraSinc)));

                llamadasList.add(llamadas);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return llamadasList;
    }

    public List<Llamadas> select_one_row(String idllamada){

        Llamadas llamadas = new Llamadas();
        List<Llamadas> llamadasList =  new ArrayList<Llamadas>();

        String query = TableLlamadas.SELECT_TABLE + " " +
                "WHERE " + TableLlamadas.Idllamada + " = ? ";

        //Log.d("Autorizaciones", query + " - " + idllamada);

        this.open();
        Cursor cursor = database.rawQuery(query, new String[] { String.valueOf(idllamada)});
        if(cursor.moveToNext()){
            do{
                llamadas = new Llamadas();
                llamadas.setIdllamada(cursor.getString(cursor.getColumnIndex(TableLlamadas.Idllamada)));
                llamadas.setLlamada(cursor.getString(cursor.getColumnIndex(TableLlamadas.Llamada)));
                llamadas.setParameters(cursor.getString(cursor.getColumnIndex(TableLlamadas.Parameters)));
                llamadas.setTableName(cursor.getString(cursor.getColumnIndex(TableLlamadas.TableName)));
                llamadas.setPrimarykey(cursor.getString(cursor.getColumnIndex(TableLlamadas.Primarykey)));
                llamadas.setColumns(cursor.getString(cursor.getColumnIndex(TableLlamadas.Columns)));
                llamadas.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableLlamadas.FechaHoraSinc)));

                llamadasList.add(llamadas);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return llamadasList;
    }


    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableLlamadas.TABLE_NAME;

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

        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_ESTADOS_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM ESTADOS\",'ESTADOS','ESTADO','DESCRIPCION,REQUIERE_ASISTENCIA,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");

        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_EMPRESAS_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM EMPRESAS\",'EMPRESAS','EMPRESA','NOMBRE_CORTO,NOMBRE,ICONO,DIRECCION,RUC,BLOQUEADO,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_TIPO_LECTORA_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM TIPO_LECTORA\",'TIPO_LECTORA','ID_TIPO_LECT','DESCRIPCION,BIOMETRIA,ID_AUTORIZACION,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_TIPO_DETALLE_BIOMETRIA_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM TIPO_DETALLE_BIOMETRIA\",'TIPO_DETALLE_BIOMETRIA','ID_TIPO_DETA_BIO','DESCRIPCION,ID_AUTORIZACION,ID_TIPO_LECT,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");

        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_TERMINAL_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM TERMINAL;pIDTERMINAL&SELECT IDTERMINAL FROM TERMINAL LIMIT 1\",'TERMINAL','IDTERMINAL','DESCRIPCION,HABILITADO,MAC,MODELO,FIRMWARE,SOFTWARE,HARDWARE,CHASIS,UPS,NUM_CEL,ID_AUTORIZACION,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_TERMINAL_TIPOLECT_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM TERMINAL_TIPOLECT;pIDTERMINAL&SELECT IDTERMINAL FROM TERMINAL LIMIT 1\",'TERMINAL_TIPOLECT','ID_TERMINAL_TIPOLECT','IDTERMINAL,ID_TIPO_LECT,FLAG,ID_AUTORIZACION,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");

        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_PERSONAL_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM PERSONAL\",'PERSONAL','EMPRESA,CODIGO','CENTRO_DE_COSTO,APELLIDO_PATERNO,APELLIDO_MATERNO,NOMBRES,FECHA_DE_NACIMIENTO,FECHA_DE_INGRESO,FECHA_DE_CESE,ESTADO,TIPO_HORARIO,ICONO,NRO_DOCUMENTO,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_PER_TIPOLECT_TERM_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM PER_TIPOLECT_TERM;pIDTERMINAL&SELECT IDTERMINAL FROM TERMINAL LIMIT 1\",'PER_TIPOLECT_TERM','EMPRESA,CODIGO,ID_TERMINAL_TIPOLECT,ID_TIPO_LECT','FLAG,ID_AUTORIZACION,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_TARJETA_PERSONAL_TIPOLECTORA_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM TARJETA_PERSONAL_TIPOLECTORA\",'TARJETA_PERSONAL_TIPOLECTORA','EMPRESA,CODIGO,ID_TIPO_LECT','VALOR_TARJETA,FECHA_INI,FECHA_FIN,FECHA_CREACION,FECHA_ANULACION,ID_AUTORIZACION,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");
        database.execSQL("INSERT INTO LLAMADAS(IDLLAMADA,LLAMADA,PARAMETERS,TABLE_NAME,PRIMARYKEY,COLUMNS,FECHA_HORA_SINC) VALUES('SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_TX','EXEC [TEMPUS].[TSP_EJECUTAR_LOTE_DATA2]',\"pFECHA_HORA_SINC&SELECT IFNULL(STRFTIME('%d/%m/%Y %H:%M:%f',MAX(FECHA_HORA_SINC)),'01/01/2014 00:00:00.000') AS FECHA_HORA_SINC FROM PERSONAL_TIPOLECTORA_BIOMETRIA;pIDTERMINAL&SELECT IDTERMINAL FROM TERMINAL LIMIT 1\",'PERSONAL_TIPOLECTORA_BIOMETRIA','ID_PER_TIPOLECT_BIO,INDICE_BIOMETRIA,EMPRESA,CODIGO,ID_TIPO_LECT','VALOR_TARJETA,ID_TIPO_DETA_BIO,VALOR_BIOMETRIA,FECHA_BIOMETRIA,IMAGEN_BIOMETRIA,ID_AUTORIZACION,SINCRONIZADO,FECHA_HORA_SINC','" + fechahora.getFechahora() + "');");

        //database.endTransaction();
        this.close();

    }




}

