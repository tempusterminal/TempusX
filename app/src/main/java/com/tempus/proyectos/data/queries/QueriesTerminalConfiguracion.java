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
import com.tempus.proyectos.data.model.TerminalConfiguracion;
import com.tempus.proyectos.data.tables.TableTerminalConfiguracion;

/**
 * Created by GURRUTIAM on 11/11/2016.
 */

public class QueriesTerminalConfiguracion {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesTerminalConfiguracion(Context context) {
        this.context = context;
    }

    public QueriesTerminalConfiguracion open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(TerminalConfiguracion terminalConfiguracion){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableTerminalConfiguracion.Idterminal, terminalConfiguracion.Idterminal);
        contentValues.put(TableTerminalConfiguracion.TipoInstalacion, terminalConfiguracion.TipoInstalacion);
        contentValues.put(TableTerminalConfiguracion.TipoAhorroEnergia, terminalConfiguracion.TipoAhorroEnergia);
        contentValues.put(TableTerminalConfiguracion.BateriaResistencia, terminalConfiguracion.BateriaResistencia);
        contentValues.put(TableTerminalConfiguracion.HusoHorario, terminalConfiguracion.HusoHorario);
        contentValues.put(TableTerminalConfiguracion.Parametro, terminalConfiguracion.Parametro);
        contentValues.put(TableTerminalConfiguracion.FechaHoraServidor, terminalConfiguracion.FechaHoraServidor);
        contentValues.put(TableTerminalConfiguracion.Reboot, terminalConfiguracion.Reboot);
        contentValues.put(TableTerminalConfiguracion.HoraReboot, terminalConfiguracion.HoraReboot);
        contentValues.put(TableTerminalConfiguracion.Replicado, terminalConfiguracion.Replicado);
        contentValues.put(TableTerminalConfiguracion.HoraInicioReplicado, terminalConfiguracion.HoraInicioReplicado);
        contentValues.put(TableTerminalConfiguracion.HoraFinReplicado, terminalConfiguracion.HoraFinReplicado);
        contentValues.put(TableTerminalConfiguracion.IdAutorizacion, terminalConfiguracion.IdAutorizacion);
        contentValues.put(TableTerminalConfiguracion.FechaHoraSinc, terminalConfiguracion.FechaHoraSinc);

        database.insert(TableTerminalConfiguracion.TABLE_NAME, null, contentValues);

    }

    public List<TerminalConfiguracion> select(){

        TerminalConfiguracion terminalConfiguracion = new TerminalConfiguracion();
        List<TerminalConfiguracion> terminalConfiguracionList =  new ArrayList<TerminalConfiguracion>();

        Cursor cursor = database.rawQuery(TableTerminalConfiguracion.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                terminalConfiguracion = new TerminalConfiguracion();
                terminalConfiguracion.setIdterminal(cursor.getString(cursor.getColumnIndex(TableTerminalConfiguracion.Idterminal)));
                terminalConfiguracion.setTipoInstalacion(cursor.getInt(cursor.getColumnIndex(TableTerminalConfiguracion.TipoInstalacion)));
                terminalConfiguracion.setTipoAhorroEnergia(cursor.getInt(cursor.getColumnIndex(TableTerminalConfiguracion.TipoAhorroEnergia)));
                terminalConfiguracion.setBateriaResistencia(cursor.getInt(cursor.getColumnIndex(TableTerminalConfiguracion.BateriaResistencia)));
                terminalConfiguracion.setHusoHorario(cursor.getInt(cursor.getColumnIndex(TableTerminalConfiguracion.HusoHorario)));
                terminalConfiguracion.setParametro(cursor.getString(cursor.getColumnIndex(TableTerminalConfiguracion.Parametro)));
                terminalConfiguracion.setFechaHoraServidor(cursor.getString(cursor.getColumnIndex(TableTerminalConfiguracion.FechaHoraServidor)));
                terminalConfiguracion.setReboot(cursor.getInt(cursor.getColumnIndex(TableTerminalConfiguracion.Reboot)));
                terminalConfiguracion.setHoraReboot(cursor.getString(cursor.getColumnIndex(TableTerminalConfiguracion.HoraReboot)));
                terminalConfiguracion.setReplicado(cursor.getInt(cursor.getColumnIndex(TableTerminalConfiguracion.Replicado)));
                terminalConfiguracion.setHoraInicioReplicado(cursor.getString(cursor.getColumnIndex(TableTerminalConfiguracion.HoraInicioReplicado)));
                terminalConfiguracion.setHoraFinReplicado(cursor.getString(cursor.getColumnIndex(TableTerminalConfiguracion.HoraFinReplicado)));
                terminalConfiguracion.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TableTerminalConfiguracion.IdAutorizacion)));
                terminalConfiguracion.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TableTerminalConfiguracion.FechaHoraSinc)));

                terminalConfiguracionList.add(terminalConfiguracion);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return terminalConfiguracionList;
    }


    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TableTerminalConfiguracion.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }


}
