package com.tempus.proyectos.data.queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.model.PersonalTipolectoraBiometria;
import com.tempus.proyectos.data.tables.TablePersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Utilities;

import org.apache.commons.net.io.Util;

import static com.tempus.proyectos.tempusx.ActivityPrincipal.btSocket02;
import static com.tempus.proyectos.tempusx.ActivityPrincipal.context;
import static com.tempus.proyectos.tempusx.ActivityPrincipal.isReplicating;
import static com.tempus.proyectos.tempusx.ActivityPrincipal.isReplicatingTemplate;
import static com.tempus.proyectos.tempusx.ActivityPrincipal.objSuprema;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class QueriesPersonalTipolectoraBiometria {
    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;
    private Utilities util;

    public QueriesPersonalTipolectoraBiometria(Context context) {
        this.context = context;
    }

    public QueriesPersonalTipolectoraBiometria open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(PersonalTipolectoraBiometria personalTipolectoraBiometria){

        //conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TablePersonalTipolectoraBiometria.IdPerTipolectBio, personalTipolectoraBiometria.Empresa);
        contentValues.put(TablePersonalTipolectoraBiometria.IndiceBiometria, personalTipolectoraBiometria.IndiceBiometria);
        contentValues.put(TablePersonalTipolectoraBiometria.Empresa, personalTipolectoraBiometria.Empresa);
        contentValues.put(TablePersonalTipolectoraBiometria.Codigo, personalTipolectoraBiometria.Codigo);
        contentValues.put(TablePersonalTipolectoraBiometria.IdTipoLect, personalTipolectoraBiometria.IdTipoLect);
        contentValues.put(TablePersonalTipolectoraBiometria.ValorTarjeta, personalTipolectoraBiometria.ValorTarjeta);
        contentValues.put(TablePersonalTipolectoraBiometria.IdTipoDetaBio, personalTipolectoraBiometria.IdTipoDetaBio);
        contentValues.put(TablePersonalTipolectoraBiometria.ValorBiometria, personalTipolectoraBiometria.ValorBiometria);
        contentValues.put(TablePersonalTipolectoraBiometria.FechaBiometria, personalTipolectoraBiometria.FechaBiometria);
        contentValues.put(TablePersonalTipolectoraBiometria.ImagenBiometria, personalTipolectoraBiometria.ImagenBiometria);
        contentValues.put(TablePersonalTipolectoraBiometria.IdAutorizacion, personalTipolectoraBiometria.IdAutorizacion);
        contentValues.put(TablePersonalTipolectoraBiometria.Sincronizado, personalTipolectoraBiometria.Sincronizado);
        contentValues.put(TablePersonalTipolectoraBiometria.FechaHoraSinc, personalTipolectoraBiometria.FechaHoraSinc);

        database.insert(TablePersonalTipolectoraBiometria.TABLE_NAME, null, contentValues);

    }

    public List<PersonalTipolectoraBiometria> select(){

        PersonalTipolectoraBiometria personalTipolectoraBiometria = new PersonalTipolectoraBiometria();
        List<PersonalTipolectoraBiometria> personalTipolectoraBiometriaList =  new ArrayList<PersonalTipolectoraBiometria>();


        Cursor cursor = database.rawQuery(TablePersonalTipolectoraBiometria.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                personalTipolectoraBiometria = new PersonalTipolectoraBiometria();
                personalTipolectoraBiometria.setIdPerTipolectBio(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdPerTipolectBio)));
                personalTipolectoraBiometria.setIndiceBiometria(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)));
                personalTipolectoraBiometria.setEmpresa(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.Empresa)));
                personalTipolectoraBiometria.setCodigo(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.Codigo)));
                personalTipolectoraBiometria.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdTipoLect)));
                personalTipolectoraBiometria.setValorTarjeta(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.ValorTarjeta)));
                personalTipolectoraBiometria.setIdTipoDetaBio(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdTipoDetaBio)));
                personalTipolectoraBiometria.setValorBiometria(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.ValorBiometria)));
                personalTipolectoraBiometria.setFechaBiometria(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.FechaBiometria)));
                personalTipolectoraBiometria.setImagenBiometria(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.ImagenBiometria)));
                personalTipolectoraBiometria.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdAutorizacion)));
                personalTipolectoraBiometria.setSincronizado(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.Sincronizado)));
                personalTipolectoraBiometria.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.FechaHoraSinc)));
                personalTipolectoraBiometriaList.add(personalTipolectoraBiometria);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return personalTipolectoraBiometriaList;
    }


    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TablePersonalTipolectoraBiometria.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }



    /*
    public List<Biometrias> EvaluarBiometrias(int IdTipoLect, String ValorTarjeta){
        Log.d("Autorizaciones","Evaluar Biometrias");

        QueriesBiometrias queriesBiometrias = new QueriesBiometrias(context);
        queriesBiometrias.open();

        List<Biometrias> biometriasList = queriesBiometrias.BuscarBiometrias(IdTipoLect, ValorTarjeta,ValorTarjeta);

        if(biometriasList.isEmpty()){
            Biometrias biometrias = new Biometrias();
            biometrias.setMensaje("PERSONAL NO REGISTRADO");
            biometriasList.add(biometrias);
        }

        for(int i = 0; i < biometriasList.size(); i++){
            Log.d("Autorizaciones",biometriasList.get(i).toString());
        }

        queriesBiometrias.close();
        return biometriasList;
    }
    */

    public String RegistrarBiometrias(Biometrias biometrias, String ValorBiometria){


        Log.d("Autorizaciones","Registrar Biometrias");

        this.open();
        Fechahora fechahora = new Fechahora();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TablePersonalTipolectoraBiometria.ValorBiometria, ValorBiometria);
        contentValues.put(TablePersonalTipolectoraBiometria.Sincronizado,2);
        contentValues.put(TablePersonalTipolectoraBiometria.FechaBiometria,fechahora.getFecha(fechahora.getFechahora()));
        contentValues.put(TablePersonalTipolectoraBiometria.FechaHoraSinc,fechahora.getFechahora());

        try{
            database.update(TablePersonalTipolectoraBiometria.TABLE_NAME,contentValues,TablePersonalTipolectoraBiometria.IndiceBiometria + " = ? " + " AND " + TablePersonalTipolectoraBiometria.IdTipoDetaBio + " = ? ", new String[] { String.valueOf(biometrias.getIndiceBiometria()), String.valueOf(biometrias.getIdTipoDetaBio()) });
            return "BIOMETRIA ENROLADA";
        }catch(Exception e){
            Log.d("Autorizaciones","Error en el registro de Biometria: " + e.getMessage());
            return "FALLO AL ENROLAR " + e.getMessage();
        }finally {
            this.close();
        }

    }



    public int ActualizarBiometria(int IndiceBiometria, int IdTipoDetaBio){

        this.open();
        int sincronizado = 1; // ESTADO BIOMETRIA REPLICADA
        Fechahora fechahora = new Fechahora();


        ContentValues contentValues = new ContentValues();
        contentValues.put(TablePersonalTipolectoraBiometria.Sincronizado,sincronizado);
        contentValues.put(TablePersonalTipolectoraBiometria.FechaBiometria,fechahora.getFecha(fechahora.getFechahora()));
        contentValues.put(TablePersonalTipolectoraBiometria.FechaHoraSinc,fechahora.getFechahora());

        try{
            database.update(TablePersonalTipolectoraBiometria.TABLE_NAME,contentValues,TablePersonalTipolectoraBiometria.IndiceBiometria + " = ? " + " AND " + TablePersonalTipolectoraBiometria.IdTipoDetaBio + " = ? ", new String[] { String.valueOf(IndiceBiometria), String.valueOf(IdTipoDetaBio) });
            Log.v("TEMPUS: ","Biometria Actualizada a Estado " + sincronizado + " (Biometria Replicada)");
            return 1;
        }catch(Exception e){
            Log.d("Autorizaciones","QueriesPersonalTipolectoraBiometria.ActualizarBiometria Error al Actualizar Biometria: " + e.getMessage());
            return 0;
        }finally {
            this.close();
        }


    }

    public int ActualizarBiometriaEnviadaServidor(int IndiceBiometria, int IdTipoDetaBio){

        this.open();
        int sincronizado = 4; // ESTADO BIOMETRIA ENVIADA AL SERVIDOR
        Fechahora fechahora = new Fechahora();


        ContentValues contentValues = new ContentValues();
        contentValues.put(TablePersonalTipolectoraBiometria.Sincronizado,sincronizado);
        contentValues.put(TablePersonalTipolectoraBiometria.FechaBiometria,fechahora.getFecha(fechahora.getFechahora()));
        contentValues.put(TablePersonalTipolectoraBiometria.FechaHoraSinc,fechahora.getFechahora());

        try{
            database.update(TablePersonalTipolectoraBiometria.TABLE_NAME,contentValues,TablePersonalTipolectoraBiometria.IndiceBiometria + " = ? " + " AND " + TablePersonalTipolectoraBiometria.IdTipoDetaBio + " = ? ", new String[] { String.valueOf(IndiceBiometria), String.valueOf(IdTipoDetaBio) });
            Log.v("TEMPUS: ","Biometria Actualizada a Estado " + sincronizado + " (Biometria Enviada al Servidor)");
            return 1;
        }catch(Exception e){
            Log.d("Autorizaciones","QueriesPersonalTipolectoraBiometria.ActualizarBiometria Error al Actualizar Biometria: " + e.getMessage());
            return 0;
        }finally {
            this.close();
        }

    }

    public String CompletarBiometria(String template){

        String ValorBiometria = "";
        String[] bytebiometria = template.split(" ");

        for(int i = 0; i < bytebiometria.length; i++){
            if(bytebiometria[i].length() == 1){
                ValorBiometria = ValorBiometria + "0" + bytebiometria[i] + " ";
            }else{
                ValorBiometria = ValorBiometria + bytebiometria[i] + " ";
            }
        }

        return ValorBiometria;
    }

    public String EliminarBiometrias(Biometrias biometrias){
        Log.d("Autorizaciones","Eliminar Biometrias");

        this.open();
        Fechahora fechahora = new Fechahora();

        ContentValues contentValues = new ContentValues();
        contentValues.putNull(TablePersonalTipolectoraBiometria.ValorBiometria);
        contentValues.put(TablePersonalTipolectoraBiometria.Sincronizado,2);
        contentValues.put(TablePersonalTipolectoraBiometria.FechaBiometria,fechahora.getFecha(fechahora.getFechahora()));
        contentValues.put(TablePersonalTipolectoraBiometria.FechaHoraSinc,fechahora.getFechahora());

        try{
            database.update(TablePersonalTipolectoraBiometria.TABLE_NAME,contentValues,TablePersonalTipolectoraBiometria.IndiceBiometria + " = ? " , new String[] { String.valueOf(biometrias.getIndiceBiometria()) });
            return "BIOMETRIA ELIMINADA";
        }catch(Exception e){
            Log.d("Autorizaciones","Error en el registro de Biometria: " + e.getMessage());
            return "FALLO AL ELIMINAR " + e.getMessage();
        }finally {
            this.close();
        }

    }

    public List<PersonalTipolectoraBiometria> select_one_row(){

        this.open();
        PersonalTipolectoraBiometria personalTipolectoraBiometria = new PersonalTipolectoraBiometria();
        List<PersonalTipolectoraBiometria> personalTipolectoraBiometriaList =  new ArrayList<PersonalTipolectoraBiometria>();

        String query = TablePersonalTipolectoraBiometria.SELECT_BIOMETRIA_SINCRONIZAR;

        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToNext()){
            do{
                personalTipolectoraBiometria = new PersonalTipolectoraBiometria();
                personalTipolectoraBiometria.setIdPerTipolectBio(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdPerTipolectBio)));
                personalTipolectoraBiometria.setIndiceBiometria(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)));
                personalTipolectoraBiometria.setEmpresa(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.Empresa)));
                personalTipolectoraBiometria.setCodigo(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.Codigo)));
                personalTipolectoraBiometria.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdTipoLect)));
                personalTipolectoraBiometria.setValorTarjeta(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.ValorTarjeta)));
                personalTipolectoraBiometria.setIdTipoDetaBio(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdTipoDetaBio)));
                personalTipolectoraBiometria.setValorBiometria(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.ValorBiometria)));
                personalTipolectoraBiometria.setFechaBiometria(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.FechaBiometria)));
                personalTipolectoraBiometria.setImagenBiometria(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.ImagenBiometria)));
                personalTipolectoraBiometria.setIdAutorizacion(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdAutorizacion)));
                personalTipolectoraBiometria.setSincronizado(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.Sincronizado)));
                personalTipolectoraBiometria.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.FechaHoraSinc)));
                personalTipolectoraBiometriaList.add(personalTipolectoraBiometria);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return personalTipolectoraBiometriaList;
    }

    public void ReplicarBiometria(){

        int biometriasPorReplicar = 0;
        int biometriasReplicadas = 0;
        int biometriasNoReplicadas = 0;
        int biometriasActualizadas = 0;
        this.open();
        Cursor cursor = database.rawQuery(TablePersonalTipolectoraBiometria.SELECT_BIOMETRIA_REPLICAR, null);
        biometriasPorReplicar = cursor.getCount();
        Log.v("TEMPUS: ","Proceso de REPLICA");
        Log.v("TEMPUS: ","ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");

        if(cursor.moveToNext()){
            Log.v("TEMPUS: ","Cantidad de Biometrias a Replicar: " + String.valueOf(cursor.getCount()));
            try{
                ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"FreeScanOff",null);
                Log.v("TEMPUS: ","Huella Apagada");
                Log.v("TEMPUS: ","Preparando para Replicar...");
            }catch(Exception e){
                Log.v("TEMPUS: ","QueriesPersonalTipolectoraBiometria.ReplicarBiometria Error apagando Huella: " + e.getMessage());
            }


            do{

                String indice = String.valueOf(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)));
                String template = CompletarBiometria(cursor.getString(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.ValorBiometria)));
                String[] sizetemplate = objSuprema.SizeTemplate(template.replace(" ",""));
                indice = objSuprema.convertCardToEnroll(indice);

                String[] parametro = (objSuprema.convertCardToEnroll(indice) + "," + (sizetemplate[0] + "0" + sizetemplate[1] + "0000") + "," + template.replace(" ","")).split(",");

                if(template.replace(" ","").length() == 512){
                    try{

                        objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"EnrollByTemplate",parametro);
                        isReplicatingTemplate = true;



                        while(isReplicatingTemplate){
                            //Esperando que termine de replicar
                            //Log.v("TEMPUS: ","Esperando is Replicatinfg False");
                            //isReplicatingTemplate = true;




                        }

                        Log.v("TEMPUS: ","+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        Log.v("TEMPUS: ","Biometria Replicada con Exito");
                        try{
                            ActualizarBiometria(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)),cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IdTipoDetaBio)));
                            biometriasActualizadas++;
                        }catch(Exception e){
                            Log.v("TEMPUS: ","writeToSuprema.EnrollByTemplate Error Actualizando Estado 1 (Replicado) SQLite: " + e.getMessage());
                        }

                        Log.v("TEMPUS: ","Indice Biometria: " + indice);
                        Log.v("TEMPUS: ","Longitud: " + template.replace(" ","").length() / 2 + " bytes");
                        Log.v("TEMPUS: ","Template: " + template);

                        biometriasReplicadas++;

                    }catch(Exception e){
                        Log.v("TEMPUS: ","writeToSuprema.EnrollByTemplate Error " + e.getMessage());
                        isReplicating = false;
                    }
                }else{
                    Log.v("TEMPUS: ","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    Log.v("TEMPUS: ","Biometria NO Replicada");
                    Log.v("TEMPUS: ","Indice Biometria: " + indice);
                    Log.v("TEMPUS: ","Longitud: " + template.replace(" ","").length() / 2 + " bytes");
                    Log.v("TEMPUS: ","Template: " + template);

                    biometriasNoReplicadas++;
                }

                objSuprema.limpiarTramaSuprema();

                Log.v("TEMPUS: ","Cantidad de Biometrias: " + biometriasPorReplicar + " > (+) Replicadas: " + biometriasReplicadas + " > (x) No Replicadas: " + biometriasNoReplicadas + " > (o) Actualizadas: " + biometriasActualizadas);


            }while(cursor.moveToNext());

            try{
                ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"FreeScanOn",null);
                Log.v("TEMPUS: ","Huella Encendida");
                Log.v("TEMPUS: ","Finalizando Replica...");
            }catch(Exception e){
                Log.v("TEMPUS: ","QueriesPersonalTipolectoraBiometria.ReplicarBiometria Error encendiendo Huella: " + e.getMessage());
            }


        }else{
            Log.v("TEMPUS: ","Cantidad de Biometrias a Replicar: " + String.valueOf(cursor.getCount()));
            Log.v("TEMPUS: ","Proceso de Replica NO Iniciado, NO existen Biometrias por Replicar");
        }



        Log.v("TEMPUS: ","oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        Log.v("TEMPUS: ","Resumen -> Biometrias: " + biometriasPorReplicar + " > (+) Replicadas: " + biometriasReplicadas + " > (x) No Replicadas: " + biometriasNoReplicadas + " > (o) Actualizadas: " + biometriasActualizadas);
        Log.v("TEMPUS: ","oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");

        isReplicating = false;

        cursor.close();
        this.close();

        //return 1;
    }


    public int iniciarReplicado(){
        //Esta función o método tiene como objetivo detectar las horas de replicado
        //La consulta se realiza a la tabla PARAMETERS, IDPARAMETER = HORA_REPLICADO_1, HORA_REPLICADO_2, HORA_REPLICADO_3, HORA_REPLICADO_4
        //this.open();
        //Se declaran variables para la hora actual y para la hora de replica
        int hora = 0;
        int minuto = 0;
        int segundo = 0;

        int horar = 0;
        int minutor = 0;
        int segundor = 0;

        String idparameters = "HORA_REPLICADO_1,HORA_REPLICADO_2,HORA_REPLICADO_3,HORA_REPLICADO_4";
        String[] idparametersarray = idparameters.split(",");

        //Log.d("Autorizaciones","idparametersarray[i] : " + idparametersarray[0] + " - " + idparametersarray[1] + " - " + idparametersarray[2] + " - " + idparametersarray[3]);

        Fechahora fechahora = new Fechahora();
        QueriesParameters queriesParameters = new QueriesParameters(context);

        for(int i = 0; i < idparametersarray.length; i++){
            //Log.d("Autorizaciones","idparametersarray[i] : " + idparametersarray[i]);

            //List<Parameters> parametersList = new ArrayList<Parameters>();
            List<Parameters> parametersList = queriesParameters.select_one_row(idparametersarray[i]);

            //fechahora.getHora(fechahora.getFechahora());
            //parametersList.get(0).getIdparameter();

            hora = Integer.parseInt(fechahora.getHora(fechahora.getFechahora()).substring(0,2));
            minuto = Integer.parseInt(fechahora.getHora(fechahora.getFechahora()).substring(3,5));
            segundo = Integer.parseInt(fechahora.getHora(fechahora.getFechahora()).substring(6));

            horar = Integer.parseInt(parametersList.get(0).getValue().substring(0,2));
            minutor = Integer.parseInt(parametersList.get(0).getValue().substring(3,5));
            segundor = Integer.parseInt(parametersList.get(0).getValue().substring(6));

            //Log.d("Autorizaciones","Hora Actual: " + hora + " " + minuto + " " + segundo);
            //Log.d("Autorizaciones","Hora Replica: " + horar + " " + minutor + " " + segundor);


            if(parametersList.get(0).getEnable() == 1){
                if(hora == horar){
                    if(minuto == minutor){
                        ///Log.d("Autorizaciones","REPLICA ACTIVA");
                        return 1;
                    /*
                    if((segundo - segundor) >= 0 || (segundo - segundor) <= 10){
                        //Log.d("Autorizaciones","REPLICA ACTIVA");
                    }
                    */
                    }
                }
            }


        }

        //this.close();
        return 0;
    }



}
