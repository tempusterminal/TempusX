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
import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.model.PersonalTipolectoraBiometria;
import com.tempus.proyectos.data.tables.TablePersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Utilities;

import static com.tempus.proyectos.tempusx.ActivityPrincipal.objSuprema;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class QueriesPersonalTipolectoraBiometria {
    private String TAG = "DQ-QUEPTB";

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;
    private Utilities util = new Utilities();
    QueriesLogTerminal queriesLogTerminal = new QueriesLogTerminal();

    public static String statusReplicate = "";

    public QueriesPersonalTipolectoraBiometria(Context context) {
        this.context = context;
    }

    public QueriesPersonalTipolectoraBiometria open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
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

        Log.v(TAG,query);
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


        Log.v(TAG,"Registrar Biometrias");

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
            Log.e(TAG,"Error en el registro de Biometria: " + e.getMessage());
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
            if(IdTipoDetaBio == 0){
                database.update(TablePersonalTipolectoraBiometria.TABLE_NAME,contentValues,TablePersonalTipolectoraBiometria.IndiceBiometria + " = ? ", new String[] { String.valueOf(IndiceBiometria) });
            }else{
                database.update(TablePersonalTipolectoraBiometria.TABLE_NAME,contentValues,TablePersonalTipolectoraBiometria.IndiceBiometria + " = ? " + " AND " + TablePersonalTipolectoraBiometria.IdTipoDetaBio + " = ? ", new String[] { String.valueOf(IndiceBiometria), String.valueOf(IdTipoDetaBio) });
            }

            Log.v(TAG,"Biometria Actualizada a Estado " + sincronizado + " (Biometria Replicada)");
            return 1;
        }catch(Exception e){
            Log.e(TAG,"QueriesPersonalTipolectoraBiometria.ActualizarBiometria Error al Actualizar Biometria: " + e.getMessage());
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
            Log.v(TAG,"BTS-MAET Biometria Actualizada a Estado " + sincronizado + " (Biometria Enviada al Servidor)");
            return 1;
        }catch(Exception e){
            Log.e(TAG,"QueriesPersonalTipolectoraBiometria.ActualizarBiometria Error al Actualizar Biometria: " + e.getMessage());
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
        Log.v(TAG,"Eliminar Biometrias");

        this.open();
        Fechahora fechahora = new Fechahora();

        ContentValues contentValues = new ContentValues();
        contentValues.putNull(TablePersonalTipolectoraBiometria.ValorBiometria);
        contentValues.put(TablePersonalTipolectoraBiometria.Sincronizado,2);
        contentValues.put(TablePersonalTipolectoraBiometria.FechaBiometria,fechahora.getFecha(fechahora.getFechahora()));
        contentValues.put(TablePersonalTipolectoraBiometria.FechaHoraSinc,fechahora.getFechahora());

        Log.v(TAG,"eliminabiometria: " + biometrias.toString());

        try{
            database.update(TablePersonalTipolectoraBiometria.TABLE_NAME,contentValues,TablePersonalTipolectoraBiometria.IndiceBiometria + " = ? AND " + TablePersonalTipolectoraBiometria.IdTipoLect + " = ? " , new String[] { String.valueOf(biometrias.getIndiceBiometria()), String.valueOf(biometrias.getIdTipoLect()) });
            return "BIOMETRIA ELIMINADA";
        }catch(Exception e){
            Log.e(TAG,"Error en el registro de Biometria: " + e.getMessage());
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

    public ArrayList<PersonalTipolectoraBiometria> select_indice_biometria(String indiceBiometria){

        this.open();
        PersonalTipolectoraBiometria personalTipolectoraBiometria = new PersonalTipolectoraBiometria();
        ArrayList<PersonalTipolectoraBiometria> personalTipolectoraBiometriaList =  new ArrayList<PersonalTipolectoraBiometria>();

        String query = TablePersonalTipolectoraBiometria.SELECT_INDICE_BIOMETRIA_REPLICAR
                + "WHERE " + TablePersonalTipolectoraBiometria.IndiceBiometria + " = '" + indiceBiometria + "' "
                + "ORDER BY " + TablePersonalTipolectoraBiometria.IdTipoDetaBio + " ASC;";

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

    public ArrayList<String> listarIndicesEnrrolados(){
        ActivityPrincipal.iswithX = true;
        ArrayList<String> indicesArrayList = new ArrayList<String>();
        boolean gettingData = false;
        //40 86 00000000 00100000 00 D60A + no data
        String parametrosLTX[] = new String[5];
        parametrosLTX[0] = "86";
        parametrosLTX[1] = "00000000";
        parametrosLTX[2] = "00100000";
        parametrosLTX[3] = "00";
        parametrosLTX[4] = "";
        objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"ExtendedDataTransferProtocol",parametrosLTX); // LT : List User ID

        //Revisar lista de indices biometrias para caso shougang
        // NO ENCIENDE HUELLA AL TERMINAR PROCESO

        Log.v(TAG,"listarIndicesEnrrolados se envió petición para recibir indices biometria");
        try{
            String indicesHexa = "";
            for(int i = 0; i <= 6000; i++){

                // cuando la cadena supere los 52 caracteres,
                // entonces se comenzara a evaluar la data que viene despues (los indices)
                // 4086000500000028000061540a
                // 4086010000000028000000ef0a
                if(ActivityPrincipal.withx.length() > 52){
                    //Respuesta esperada
                    //40 86 55000000 a8020000 61 26 0a = 26 longitud
                    //40 86 01000000 a8020000 00 71 0a = 26 longitud
                    //000006690001b163 000006690001b163 ... = (nro de indices * 16) longitud
                    //0c5b0000 = (checksum invertido) 8 longitud.0

                    // longitud repesentada en 4 bytes 00 00 00 00 de toda la data que vendra despues de los dos comandos hasta antes del checksum final
                    int longitud = 0;
                    if(ActivityPrincipal.withx.substring(12,20).equalsIgnoreCase(ActivityPrincipal.withx.substring(38,46))){
                        //Si la longitud de data que vendrá == longitud total de data
                        //40 86 55000000 a8020000 61 26 0a => a8020000 longitud total de data
                        //40 86 01000000 a8020000 00 71 0a => a8020000 lontigud de data que vendrá
                        longitud = util.convertHexToDecimal(util.invertHex(ActivityPrincipal.withx.substring(12,20),2)) * 2;
                        gettingData = false;
                    }else{
                        //Si la longitud de data que vendrá != longitud total de data
                        //40 86 00050000 00280000 61 54 0a => 00280000 longitud total de data
                        //40 86 03000000 00100000 00 d9 0a => 00100000 lontigud de data que vendrá
                        //Si estas longitudes no son iguales, se esperará que llegue la longitud 00100000, luego se volverá a solicitar más data con DATA_OK
                        longitud = util.convertHexToDecimal(util.invertHex(ActivityPrincipal.withx.substring(38,46),2)) * 2;
                        gettingData = true;
                    }
                    //Log.v(TAG,"gettingData " + gettingData + " - " + ActivityPrincipal.withx.substring(12,20) + "/" + ActivityPrincipal.withx.substring(38,46));

                    // Verificar si el paquete de datos cumple con la longitud esperada
                    if(ActivityPrincipal.withx.length() == ((26 + 26) + (longitud) + 8)){
                        //Log.v(TAG,"checksum indices=" + util.invertHex(util.getChecksum(ActivityPrincipal.withx.substring(52,ActivityPrincipal.withx.length()-8),8),2));
                        //Log.v(TAG,"checksum original=" + ActivityPrincipal.withx.substring(ActivityPrincipal.withx.length()-8));
                        // Verificar si el checkSum corresponde al paquete de datos
                        // El checkSum corresponde a la data sin contar con los 26 + 26 caracteres
                        if(util.invertHex(util.getChecksum(ActivityPrincipal.withx.substring(52,ActivityPrincipal.withx.length()-8),8),2).equalsIgnoreCase(ActivityPrincipal.withx.substring(ActivityPrincipal.withx.length()-8))){
                            String parametrosXok[] = new String[5];
                            parametrosXok[0] = "86";
                            parametrosXok[1] = "00000000";
                            parametrosXok[2] = "00000000";
                            parametrosXok[3] = "83";
                            parametrosXok[4] = ""; //40 86 00000000 00000000 83 ## 0A

                            if(gettingData){
                                ActivityPrincipal.withx = "";
                                objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"ExtendedDataTransferProtocol",parametrosXok); // LT : List User ID
                                Log.v(TAG,"gettingData " + gettingData + " - Solicitando más data");
                            }else{
                                // Obtener el paquete que contiene los indices biometrias en hexadecimal
                                indicesHexa = ActivityPrincipal.withx.substring(52,ActivityPrincipal.withx.length()-8);
                                // Capturar los n indices del paquete de datos
                                for(int y = 0; y < indicesHexa.length()/16; y++){
                                    //Cortar la cadena de indices de 16 en 16
                                    //00000602000046530000060200017479000007020000016c -> 0000060200004653 0000060200017479 000007020000016c
                                    try{
                                        //De la cadena de 16 caracteres 0000060200004653 solo tomar los 8 caracteres 00000602 y eliminar los 0 a la izquiera
                                        String indiceTemp = String.valueOf(Integer.valueOf(indicesHexa.substring((y*16),(y*16) + 8)));
                                        //Log.v(TAG,"indiceTemp a evaluar=" + indiceTemp);
                                        if(!indicesArrayList.contains(indiceTemp)){
                                            indicesArrayList.add(indiceTemp);
                                        }
                                    }catch (Exception e){
                                        Log.v(TAG,"indiceTemp " + e.getMessage());
                                    }

                                }
                                Log.v(TAG,"listarIndicesEnrrolados ---------------------------------------");
                                Log.v(TAG,"indices enrrolados (" + indicesArrayList.size() + ")=" + indicesArrayList.toString());

                                //objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"ExtendedDataTransferProtocol",parametrosXok); // LT : List User ID

                                //Tiempo de descanso para poder utilizar otros comandos
                                Thread.sleep(1000);
                                break;
                            }
                        }
                    }
                }
                Thread.sleep(10);
            }
        }catch (Exception e){
            Log.e(TAG,"listarIndicesEnrrolados " + e.getMessage());
        }
        //Log.v(TAG,"iswithX=" + ActivityPrincipal.iswithX);
        ActivityPrincipal.iswithX = false;
        Log.v(TAG,"iswithX=" + ActivityPrincipal.iswithX);
        try{
            //Thread.sleep(500);
        }catch (Exception e){

        }
        return indicesArrayList;
    }

    public void eliminarBiometriasPersonalCesado(ArrayList<String> indicesArrayList){
        int indicesBiometriasPorEliminar = 0;
        int indicesBiometriasEliminados = 0;
        int indicesBiometriasNoEncontrado = 0;
        int indicesBiometriasNoEliminados = 0;
        int indicesBiometriasActualizados = 0;

        this.open();
        Cursor cursor = database.rawQuery(TablePersonalTipolectoraBiometria.SELECT_PERSONAL_CESADO_CON_SIN_HUELLA, null);
        indicesBiometriasPorEliminar = cursor.getCount();
        Log.v(TAG,"eliminarBiometriasPersonalCesado ---------------------------------------");
        Log.v(TAG,"Cantidad de Indices Biometrias a Eliminar: " + String.valueOf(cursor.getCount()));
        if(indicesBiometriasPorEliminar > 0){
            ActivityPrincipal.isDeleting = true;

            while(cursor.moveToNext()){
                try{
                    String indice = String.valueOf(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)));
                    Log.v(TAG,"eliminarBiometriasPersonalCesado indice = " + indice);
                    //Verificar que exista dentro de la lista de indices enrrolados
                    if(indicesArrayList.contains(indice)){
                        String parametros[] = new String[1];
                        parametros[0] = ActivityPrincipal.objSuprema.convertCardToEnroll(indice);
                        objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"DeleteTemplate",parametros);

                        for(int i = 0; i <= 300; i++){
                            if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("SUCCESS") || ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("NOT_FOUND")){
                                if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("SUCCESS")){
                                    indicesBiometriasEliminados++;
                                    //Log.v(TAG,"Indice eliminado");
                                }else if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("NOT_FOUND")){
                                    //Log.v(TAG,"Indice no encontrado");
                                    indicesBiometriasNoEncontrado++;
                                }
                                ActivityPrincipal.huellaDelete1 = "";
                                ActivityPrincipal.huellaDeleteFlag1 = "";

                            // Actualizar la biometria eliminada del huellero en la base de datos
                            if(ActualizarBiometria(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)),0) == 1){
                                //Log.v(TAG,"Indice actualizado");
                                indicesBiometriasActualizados++;
                            }

                                break;
                            }
                            if(i == 300){
                                Log.v(TAG,"Time out, sending Cancel to Suprema");
                                indicesBiometriasNoEliminados++;
                                objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"Cancel",null);
                            }
                            Thread.sleep(10);
                        }
                    }else{
                        indicesBiometriasNoEncontrado++;

                        // Actualizar la biometria no encontrada del huellero en la base de datos
                        if(ActualizarBiometria(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)),0) == 1){
                            //Log.v(TAG,"Indice actualizado");
                            indicesBiometriasActualizados++;
                        }

                    }

                    statusReplicate = "(EBPC=" + indicesBiometriasPorEliminar + ") " + "(OK=" + indicesBiometriasEliminados + "+" + indicesBiometriasNoEncontrado + ") " + "(KO=" + indicesBiometriasNoEliminados + ") " + "(UP=" + indicesBiometriasActualizados + ")";
                    Log.v(TAG,statusReplicate);
                }catch (Exception e){
                    Log.e(TAG,"cursor " + e.getMessage());
                }
            };

            ActivityPrincipal.isDeleting = false;

        }
        statusReplicate = "(RES)> " + "(EBPC=" + indicesBiometriasPorEliminar + ") " + "(OK=" + indicesBiometriasEliminados + "+" + indicesBiometriasNoEncontrado + ") " + "(KO=" + indicesBiometriasNoEliminados + ") " + "(UP=" + indicesBiometriasActualizados + ")";
        Log.v(TAG,statusReplicate);

        queriesLogTerminal.insertLogTerminal(TAG,"EBPC" + "|" +
                indicesBiometriasPorEliminar + "|" +
                indicesBiometriasEliminados + "|" +
                indicesBiometriasNoEncontrado + "|" +
                indicesBiometriasNoEliminados + "|" +
                indicesBiometriasActualizados,ActivityPrincipal.UserSession);
    }

    public void eliminarBiometriasPersonalActivoSinHuella(ArrayList<String> indicesArrayList){
        int indicesBiometriasPorEliminar = 0;
        int indicesBiometriasEliminados = 0;
        int indicesBiometriasNoEncontrado = 0;
        int indicesBiometriasNoEliminados = 0;
        int indicesBiometriasActualizados = 0;

        this.open();
        Cursor cursor = database.rawQuery(TablePersonalTipolectoraBiometria.SELECT_PERSONAL_ACTIVO_SIN_HUELLA, null);
        indicesBiometriasPorEliminar = cursor.getCount();
        Log.v(TAG,"eliminarBiometriasPersonalActivoSinHuella ---------------------------------------");
        Log.v(TAG,"Cantidad de Indices Biometrias a Eliminar: " + String.valueOf(cursor.getCount()));
        if(indicesBiometriasPorEliminar > 0){
            ActivityPrincipal.isDeleting = true;

            //Log.v(TAG,"indicesArrayList (" + indicesArrayList.size() + ")=" + indicesArrayList.toString());
            while(cursor.moveToNext()){
                try{
                    String indice = String.valueOf(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)));
                    Log.v(TAG,"eliminarBiometriasPersonalActivoSinHuella indice = " + indice);
                    if(indicesArrayList.contains(indice)){
                        String parametros[] = new String[1];
                        parametros[0] = ActivityPrincipal.objSuprema.convertCardToEnroll(indice);
                        objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"DeleteTemplate",parametros);

                        for(int i = 0; i <= 300; i++){
                            Log.v(TAG,"ActivityPrincipal.huellaDelete1=" + ActivityPrincipal.huellaDelete1);
                            if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("SUCCESS") || ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("NOT_FOUND")){
                                if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("SUCCESS")){
                                    indicesBiometriasEliminados++;
                                    //Log.v(TAG,"Indice eliminado");
                                }else if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("NOT_FOUND")){
                                    //Log.v(TAG,"Indice no encontrado");
                                    indicesBiometriasNoEncontrado++;
                                }
                                ActivityPrincipal.huellaDelete1 = "";
                                ActivityPrincipal.huellaDeleteFlag1 = "";

                            // Actualizar la biometria eliminada del huellero en la base de datos
                            if(ActualizarBiometria(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)),0) == 1){
                                //Log.v(TAG,"Indice actualizado");
                                indicesBiometriasActualizados++;
                            }

                                break;
                            }
                            if(i == 300){
                                Log.v(TAG,"Time out, sending Cancel to Suprema");
                                indicesBiometriasNoEliminados++;
                                objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"Cancel",null);
                            }
                            Thread.sleep(10);
                        }
                    }else{
                        indicesBiometriasNoEncontrado++;

                        // Actualizar la biometria no encontrada del huellero en la base de datos
                        if(ActualizarBiometria(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)),0) == 1){
                            //Log.v(TAG,"Indice actualizado");
                            indicesBiometriasActualizados++;
                        }

                    }

                    statusReplicate = "(EBPASH=" + indicesBiometriasPorEliminar + ") " + "(OK=" + indicesBiometriasEliminados + "+" + indicesBiometriasNoEncontrado + ") " + "(KO=" + indicesBiometriasNoEliminados + ") " + "(UP=" + indicesBiometriasActualizados + ")";
                    Log.v(TAG,statusReplicate);
                }catch (Exception e){
                    Log.e(TAG,"cursor " + e.getMessage());
                }
            };

            ActivityPrincipal.isDeleting = false;

        }
        statusReplicate = "(RES)> " + "(EBPASH=" + indicesBiometriasPorEliminar + ") " + "(OK=" + indicesBiometriasEliminados + "+" + indicesBiometriasNoEncontrado + ") " + "(KO=" + indicesBiometriasNoEliminados + ") " + "(UP=" + indicesBiometriasActualizados + ")";
        Log.v(TAG,statusReplicate);

        queriesLogTerminal.insertLogTerminal(TAG,"EBPASH" + "|" +
                indicesBiometriasPorEliminar + "|" +
                indicesBiometriasEliminados + "|" +
                indicesBiometriasNoEncontrado + "|" +
                indicesBiometriasNoEliminados + "|" +
                indicesBiometriasActualizados,ActivityPrincipal.UserSession);
    }

    public void replicarBiometriasPersonalActivoConHuella(){
        int indicesBiometriasPorReplicar = 0;
        int indicesBiometriasReplicados = 0;
        int indicesBiometriasNoReplicados = 0;
        int indicesBiometriasActualizados = 0;

        String indice;
        String templates = "";
        int nroTemplates = 0;

        //int biometriasPorReplicar = 0;
        //int biometriasReplicadas = 0;
        //int biometriasNoReplicadas = 0;
        //int biometriasActualizadas = 0;

        this.open();
        Cursor cursor = database.rawQuery(TablePersonalTipolectoraBiometria.SELECT_PERSONAL_ACTIVO_CON_HUELLA, null);
        indicesBiometriasPorReplicar = cursor.getCount();
        Log.v(TAG,"replicarBiometriasPersonalActivoConHuella ---------------------------------------");
        Log.v(TAG,"Cantidad de Indices Biometrias a Replicar: " + indicesBiometriasPorReplicar);
        if(indicesBiometriasPorReplicar > 0){
            ActivityPrincipal.isReplicatingTemplate = true;

            while(cursor.moveToNext()){
                try{
                    indice = String.valueOf(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)));
                    Log.v(TAG,"replicarBiometriasPersonalActivoConHuella indice = " + indice);

                    // -----------------------------------------------------------------------------
                    // -----------------------------------------------------------------------------
                    // Eliminar espacio de memoria
                    // -----------------------------------------------------------------------------
                    // -----------------------------------------------------------------------------
                    ActivityPrincipal.isDeleting = true;
                    try{
                        String parametros[] = new String[1];
                        //indice = String.valueOf(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)));
                        parametros[0] = ActivityPrincipal.objSuprema.convertCardToEnroll(indice);
                        //Log.v(TAG,"Limpiar espacio antes de replicar " + Limpiar);
                        objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"DeleteTemplate",parametros);

                        for(int i = 0; i <= 300; i++){
                            if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("SUCCESS") || ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("NOT_FOUND")){
                                if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("SUCCESS")){
                                    Log.v(TAG,"Limpiando espacio, indice eliminado " + indice);
                                }else if(ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("NOT_FOUND")){
                                    Log.v(TAG,"Limpiando espacio, indice no encontrado " + indice);
                                }
                                ActivityPrincipal.huellaDelete1 = "";
                                ActivityPrincipal.huellaDeleteFlag1 = "";
                                break;
                            }
                            if(i == 300){
                                Log.v(TAG,"Time out, sending Cancel to Suprema");
                                objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"Cancel",null);
                            }
                            Thread.sleep(10);
                        }

                    }catch (Exception e){
                        Log.e(TAG,"Limpiando espacio " + e.getMessage());
                    }
                    ActivityPrincipal.isDeleting = false;

                    //ActivityPrincipal.btSocket02.getOutputStream().write(util.hexStringToByteArray("4087000006020001000200D20A"));
                    //ActivityPrincipal.btSocket02.getOutputStream().write(util.hexStringToByteArray("4087010000000002000000CA0A" + "44290a0ec22255461c01888c1f03110d44076786180b9009250c0c094a0ce40511121e883f146a083615f68a4f15e8041e1789101a19230b302108124f24e4841225258524279692002aa6821c2e2e09492fde8f4f30508d333415ab3c34f29a4638d9914f3b4d09343c385605402d8b37414ca1124b3c880e4c3e08444ed0903c52d88c0a58348a4d58d786135b4c0e0e634814286362063367e1844f69df87096b2a1b0c6e4aac286fe105ccdde01222bccde01233bbccde2233bbbcce2333aaabcd2344aaaabc2344aa99ab245599889955669877776667887776666687666655559765554444975544444497544444440000000000000000000000000000441a0f13962255464303fd85181192874e117903411304864e16fb89381b87032c1d130d5635060412379d84683f7c830f501c0a2753218868596c8d6164f495616761154d688c9c696fe38557706ea12c74a684577c4c97607c530c507e36941585220555893c8e5f8e4a922092a786fffddee00000ffffffddee000001ffffcddeee00011fffccdddee000111fccccccde0000112ccccccdde000112ccccccddee0011fccccccddee0011fcccccccdee00111ccccccccee00111bbbbbbbbde01112bbbbbbbbcd01122fababbbbcd01233fbbaabbbbd01334fabbaaaaaa74444fbaaaaaaa976554fbaaaaaa9986555ffabbbbaa987655fffbbbbaa98755f000" + "BACC0000"));

                    // -----------------------------------------------------------------------------
                    // -----------------------------------------------------------------------------
                    // Enviar orden de replicado ETX
                    // -----------------------------------------------------------------------------
                    // -----------------------------------------------------------------------------
                    //indice = String.valueOf(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)));
                    //Log.v(TAG,"replicarBiometriasPersonalActivoConHuella indice = " + indice);

                    ArrayList<PersonalTipolectoraBiometria> personalTipolectoraBiometriaList = select_indice_biometria(indice);
                    Log.v(TAG,"personalTipolectoraBiometriaList = " + personalTipolectoraBiometriaList.toString());
                    //40 87 00000602 00010002 00 D2 0A
                    //40 87 01000000 00020000 00 CA 0A + huellas... + checkSum(00000000)

                    templates = "";
                    nroTemplates = 0;
                    // Calcular size (longitud de template + nro de templates)
                    for(int i = 0; i < personalTipolectoraBiometriaList.size(); i++){
                        if(!(personalTipolectoraBiometriaList.get(i).getValorBiometria() == null)){
                            String template = CompletarBiometria(personalTipolectoraBiometriaList.get(i).getValorBiometria());
                            template = template.replace(" ","");
                            if(template.length() == 512){
                                templates += template;
                                nroTemplates++;
                            }
                        }
                    }

                    //Log.v(TAG,"nroTemplates=" + nroTemplates + " - " + "templates=" + templates);

                    if(nroTemplates > 0){
                        String parametros[] = new String[2];
                        parametros[0] = ActivityPrincipal.objSuprema.convertCardToEnroll(indice);

                        if(nroTemplates == 1){
                            parametros[1] = "00010001";
                        }else if(nroTemplates == 2){
                            parametros[1] = "00010002";
                        }

                        //40 87 00000602 00010002 00 D2 0A
                        Log.v(TAG,"parametros=" + parametros[0] + ">>>" + parametros[1]);
                        objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"EnrollByTemplateX",parametros);

                        for(int i = 0; i <= 300; i++){
                            if(ActivityPrincipal.huellaEnrollFlag.equalsIgnoreCase("SUCCESS")){
                                Log.v(TAG,"ActivityPrincipal.huellaEnroll=" + ActivityPrincipal.huellaEnroll);
                                ActivityPrincipal.huellaEnrollFlag = "";
                                ActivityPrincipal.huellaEnroll = "";

                                // -----------------------------------------------------------------
                                // -----------------------------------------------------------------
                                // Enviar data de replicado para ETX
                                // -----------------------------------------------------------------
                                // -----------------------------------------------------------------

                                //40 87 01000000 00020000 00 CA 0A + huellas... + checkSum(00000000)
                                String parametrosETX[] = new String[5];
                                parametrosETX[0] = "87";
                                parametrosETX[1] = "01000000";
                                if(nroTemplates == 1){
                                    parametrosETX[2] = "00010000";
                                }else if(nroTemplates == 2){
                                    parametrosETX[2] = "00020000";
                                }

                                parametrosETX[3] = "00";
                                parametrosETX[4] = templates + util.invertHex(util.getChecksum(templates,8),2);
                                Log.v(TAG,"parametrosX=" + parametrosETX[0] + ">>>" + parametrosETX[1] + ">>>" + parametrosETX[2] + ">>>" + parametrosETX[3] + ">>>" + parametrosETX[4]);
                                objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"ExtendedDataTransferProtocol",parametrosETX);

                                for(int y = 0; y <= 300; y++){
                                    if(ActivityPrincipal.huellaEnrollFlag.equalsIgnoreCase("SUCCESS")){
                                        Log.v(TAG,"ActivityPrincipal.huellaEnroll=" + ActivityPrincipal.huellaEnroll);
                                        ActivityPrincipal.huellaEnroll = "";
                                        ActivityPrincipal.huellaEnrollFlag = "";
                                        indicesBiometriasReplicados++;

                                        // Actualizar la biometria replicada en huellero en la base de datos
                                        if(ActualizarBiometria(cursor.getInt(cursor.getColumnIndex(TablePersonalTipolectoraBiometria.IndiceBiometria)),0) == 1){
                                            //Log.v(TAG,"Indice actualizado");
                                            indicesBiometriasActualizados++;
                                        }

                                        break;
                                    }

                                    if(y == 300){
                                        Log.v(TAG,"Time out, sending Cancel to Suprema");
                                        indicesBiometriasNoReplicados++;
                                        objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"Cancel",null);
                                    }
                                    Thread.sleep(10);
                                }

                                break;
                            }

                            if(i == 300){
                                Log.v(TAG,"Time out, sending Cancel to Suprema");
                                objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"Cancel",null);
                            }
                            Thread.sleep(10);
                        }

                    }

                    statusReplicate = "(RBPACH=" + indicesBiometriasPorReplicar + ") " + "(OK=" + indicesBiometriasReplicados + ") " + "(KO=" + indicesBiometriasNoReplicados + ") " + "(UP=" + indicesBiometriasActualizados + ")";
                    Log.v(TAG,statusReplicate);
                }catch (Exception e){
                    Log.v(TAG,"ActivityPrincipal.huellaEnroll=" + ActivityPrincipal.huellaEnroll);

                    //En el caso que el proceso tenga un error, se contara como huellano replicada
                    indicesBiometriasNoReplicados++;
                    statusReplicate = "(RBPACH=" + indicesBiometriasPorReplicar + ") " + "(OK=" + indicesBiometriasReplicados + ") " + "(KO=" + indicesBiometriasNoReplicados + ") " + "(UP=" + indicesBiometriasActualizados + ")";
                    Log.v(TAG,statusReplicate);
                    Log.e(TAG,"cursor " + e.getMessage());
                }


            };

            ActivityPrincipal.isReplicatingTemplate = false;

        }

        statusReplicate = "(RES)> " + "(RBPACH=" + indicesBiometriasPorReplicar + ") " + "(OK=" + indicesBiometriasReplicados + ") " + "(KO=" + indicesBiometriasNoReplicados + ") " + "(UP=" + indicesBiometriasActualizados + ")";
        Log.v(TAG,statusReplicate);

        queriesLogTerminal.insertLogTerminal(TAG,"RBPACH" + "|" +
                indicesBiometriasPorReplicar + "|" +
                indicesBiometriasReplicados + "|" +
                indicesBiometriasNoReplicados + "|" +
                indicesBiometriasActualizados,ActivityPrincipal.UserSession);

    }

    public void freeScanOnOffSuprema(boolean freeScan){
        ActivityPrincipal.isWriting = true;
        try{
            if(freeScan){
                ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"FreeScanOn",null);
                Log.v(TAG,"Huella Encendida");
            }else{
                ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"FreeScanOff",null);
                Log.v(TAG,"Huella Apagada");
            }

            for(int i = 0; i <= 300; i++){
                Log.v(TAG,"ActivityPrincipal.huellaWrite=" + ActivityPrincipal.huellaWrite);
                //Log.v(TAG,"ActivityPrincipal.iswithX=" + ActivityPrincipal.iswithX);
                //Log.v(TAG,"ActivityPrincipal.withx=" + ActivityPrincipal.withx);
                if(ActivityPrincipal.huellaWriteFlag.equalsIgnoreCase("SUCCESS")){
                    ActivityPrincipal.huellaWrite = "";
                    ActivityPrincipal.huellaWriteFlag = "";
                    break;
                }

                if(i == 300){
                    Log.v(TAG,"Time out, sending Cancel to Suprema");
                    objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"Cancel",null);
                }
                Thread.sleep(10);
            }

            //Thread.sleep(500);
            //4001840000000000000061260a

        }catch(Exception e){
            Log.e(TAG,"freeScanOnOffSuprema " + e.getMessage());
        }
        ActivityPrincipal.isWriting = false;
    }



    public void ReplicarBiometria(){
        Log.v(TAG,"Inicio de proceso ReplicarBiometria() ----------------------------------------------------");
        ActivityPrincipal.isReplicating = false;

        /*
        try{
            ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"FreeScanOff",null);
            //4001840000000000000061260a
            Log.v(TAG,"Huella Apagada");
        }catch(Exception e){
            Log.e(TAG,"ReplicarBiometria apagando Huella " + e.getMessage());
        }
        */
        freeScanOnOffSuprema(false);

        try{
            ArrayList<String> indicesArrayList = new ArrayList<>();
            // Extraer lista de indices enrolados en el huellero
            indicesArrayList = listarIndicesEnrrolados();

            // Eliminamos indices enrolados de personal cesado (Si exiten en el huellero)
            eliminarBiometriasPersonalCesado(indicesArrayList);

            // Eliminamos indices enrolados de personal activo sin huella (Si exiten en el huellero)
            eliminarBiometriasPersonalActivoSinHuella(indicesArrayList);

            // Enrolamos templates de personal activo con huella
            replicarBiometriasPersonalActivoConHuella();
        }catch(Exception e){
            Log.e(TAG,"ReplicarBiometria " + e.getMessage());
        }


        freeScanOnOffSuprema(true);
        /*
        try{
            ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"FreeScanOn",null);
            Log.v(TAG,"Huella Encendida");
        }catch(Exception e){
            Log.e(TAG,"ReplicarBiometria encendiendo Huella: " + e.getMessage());
        }
        */

        Log.v(TAG,"Fin de proceso ReplicarBiometria() ----------------------------------------------------");
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
