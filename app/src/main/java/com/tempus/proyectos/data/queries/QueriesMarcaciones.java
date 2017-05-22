package com.tempus.proyectos.data.queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.tables.TableMarcaciones;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class QueriesMarcaciones {

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    public QueriesMarcaciones() {

    }

    public QueriesMarcaciones(Context context) {
        this.context = context;
    }

    public QueriesMarcaciones open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        conexion.close();
    }

    public void insert(Marcaciones marcaciones){

        this.open();
        //conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableMarcaciones.Empresa, marcaciones.Empresa);
        contentValues.put(TableMarcaciones.Codigo, marcaciones.Codigo);
        contentValues.put(TableMarcaciones.Fechahora, marcaciones.Fechahora);
        contentValues.put(TableMarcaciones.ValorTarjeta, marcaciones.ValorTarjeta);
        contentValues.put(TableMarcaciones.HoraTxt, marcaciones.HoraTxt);
        contentValues.put(TableMarcaciones.EntSal, marcaciones.EntSal);
        contentValues.put(TableMarcaciones.Flag, marcaciones.Flag);
        contentValues.put(TableMarcaciones.Fecha, marcaciones.Fecha);
        contentValues.put(TableMarcaciones.Hora, marcaciones.Hora);
        contentValues.put(TableMarcaciones.Idterminal, marcaciones.Idterminal);
        contentValues.put(TableMarcaciones.IdTipoLect, marcaciones.IdTipoLect);
        contentValues.put(TableMarcaciones.FlgActividad, marcaciones.FlgActividad);
        contentValues.put(TableMarcaciones.IdUsuario, marcaciones.IdUsuario);
        contentValues.put(TableMarcaciones.TmpListar, marcaciones.TmpListar);
        contentValues.put(TableMarcaciones.Autorizado, marcaciones.Autorizado);
        contentValues.put(TableMarcaciones.TipoOperacion, marcaciones.TipoOperacion);
        contentValues.put(TableMarcaciones.Sincronizado, marcaciones.Sincronizado);
        contentValues.put(TableMarcaciones.Datos, marcaciones.Datos);
        contentValues.put(TableMarcaciones.IdTipoLect, marcaciones.IdTipoLect);

        database.insert(TableMarcaciones.TABLE_NAME, null, contentValues);

        this.close();
    }

    public List<Marcaciones> select(){

        Marcaciones marcaciones = new Marcaciones();
        List<Marcaciones> marcacionesList =  new ArrayList<Marcaciones>();

        this.open();
        Cursor cursor = database.rawQuery(TableMarcaciones.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                marcaciones = new Marcaciones();
                marcaciones.setEmpresa(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Empresa)));
                marcaciones.setCodigo(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Codigo)));
                marcaciones.setFechahora(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Fechahora)));
                marcaciones.setValorTarjeta(cursor.getString(cursor.getColumnIndex(TableMarcaciones.ValorTarjeta)));
                marcaciones.setHoraTxt(cursor.getString(cursor.getColumnIndex(TableMarcaciones.HoraTxt)));
                marcaciones.setEntSal(cursor.getString(cursor.getColumnIndex(TableMarcaciones.EntSal)));
                marcaciones.setFlag(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Flag)));
                marcaciones.setFecha(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Fecha)));
                marcaciones.setHora(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Hora)));
                marcaciones.setIdterminal(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Idterminal)));
                marcaciones.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.IdTipoLect)));
                marcaciones.setFlgActividad(cursor.getString(cursor.getColumnIndex(TableMarcaciones.FlgActividad)));
                marcaciones.setIdUsuario(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.IdUsuario)));
                marcaciones.setTmpListar(cursor.getString(cursor.getColumnIndex(TableMarcaciones.TmpListar)));
                marcaciones.setAutorizado(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.Autorizado)));
                marcaciones.setTipoOperacion(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.TipoOperacion)));
                marcaciones.setSincronizado(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.Sincronizado)));
                marcaciones.setDatos(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Datos)));
                marcaciones.setValorDatoContenido(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.ValorDatoContenido)));
                marcacionesList.add(marcaciones);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return marcacionesList;
    }

    public List<Marcaciones> select_one_row(){

        Marcaciones marcaciones = new Marcaciones();
        List<Marcaciones> marcacionesList =  new ArrayList<Marcaciones>();

        this.open();
        Cursor cursor = database.rawQuery(TableMarcaciones.SELECT_ONE_ROW_TABLE, null);
        if(cursor.moveToNext()){
            do{
                marcaciones = new Marcaciones();
                marcaciones.setEmpresa(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Empresa)));
                marcaciones.setCodigo(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Codigo)));
                marcaciones.setFechahora(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Fechahora)));
                marcaciones.setValorTarjeta(cursor.getString(cursor.getColumnIndex(TableMarcaciones.ValorTarjeta)));
                marcaciones.setHoraTxt(cursor.getString(cursor.getColumnIndex(TableMarcaciones.HoraTxt)));
                marcaciones.setEntSal(cursor.getString(cursor.getColumnIndex(TableMarcaciones.EntSal)));
                marcaciones.setFlag(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Flag)));
                marcaciones.setFecha(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Fecha)));
                marcaciones.setHora(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Hora)));
                marcaciones.setIdterminal(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Idterminal)));
                marcaciones.setIdTipoLect(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.IdTipoLect)));
                marcaciones.setFlgActividad(cursor.getString(cursor.getColumnIndex(TableMarcaciones.FlgActividad)));
                marcaciones.setIdUsuario(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.IdUsuario)));
                marcaciones.setTmpListar(cursor.getString(cursor.getColumnIndex(TableMarcaciones.TmpListar)));
                marcaciones.setAutorizado(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.Autorizado)));
                marcaciones.setTipoOperacion(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.TipoOperacion)));
                marcaciones.setSincronizado(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.Sincronizado)));
                marcaciones.setDatos(cursor.getString(cursor.getColumnIndex(TableMarcaciones.Datos)));
                marcaciones.setValorDatoContenido(cursor.getInt(cursor.getColumnIndex(TableMarcaciones.ValorDatoContenido)));
                marcacionesList.add(marcaciones);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return marcacionesList;
    }


    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*) " +
                "FROM " + TableMarcaciones.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }

    public String ModoMarcacion(String ValorTarjeta, String Idterminal, int IdTipoLect, String FlgActividad, String Fechahora, String ModoMarcacion){

        String output;
        String apellidosnombres = "";
        String valortarjeta = ValorTarjeta;
        String mensaje = "";
        String mensajedetalle = "";
        String lectorasiguiente = "0";
        boolean capturarlectorasiguiente = false;
        int insert = 1;

        String parametervalue;

        QueriesParameters queriesParameters = new QueriesParameters(ActivityPrincipal.context);
        Autorizaciones autorizaciones = new Autorizaciones();
        String idparametersMarcaciones = "";

        try{
            if(ModoMarcacion.equals("")){
                idparametersMarcaciones = queriesParameters.idparameterToValue("PARAMETERS_MARCACIONES");
                // TECLADO_MANO,DNI_MANO
            }else{
                idparametersMarcaciones = ModoMarcacion;
                // TECLADO_MANO
            }
        }catch(Exception e){
            Log.d("Autorizaciones","ModoMarcacion Exception " + e.getMessage());
        }

        Log.d("Autorizaciones","idparametersMarcaciones = " + idparametersMarcaciones);

        if(idparametersMarcaciones.equals("")){
            autorizaciones = this.GestionarMarcaciones(ValorTarjeta,Idterminal,IdTipoLect,FlgActividad,Fechahora,insert);
            apellidosnombres = autorizaciones.getApellidoPaterno() + " " + autorizaciones.getApellidoMaterno() + " " + autorizaciones.getNombres().substring(0,1);
            valortarjeta = autorizaciones.getValorTarjeta();
            mensaje = autorizaciones.getMensaje();
            mensajedetalle = autorizaciones.getMensajeDetalle();

            /*
            if(!autorizaciones.getMensaje().equalsIgnoreCase("null")){
                apellidosnombres = autorizaciones.getApellidoPaterno() + " " + autorizaciones.getApellidoMaterno() + " " + autorizaciones.getNombres().substring(0,1);
                valortarjeta = autorizaciones.getValorTarjeta();
                mensaje = autorizaciones.getMensaje();
                mensajedetalle = autorizaciones.getMensajeDetalle();
            }else{
                mensaje = "MARCACION NO AUTORIZADA";
            }
            */
            lectorasiguiente = "0";
        }else{
            Log.d("Autorizaciones","Marcacion por Modos ");
            String[] idparametermarcacionarray = idparametersMarcaciones.split(",");
            // TECLADO_MANO [0] - DNI_MANO [1]

            for(int i = 0; i < idparametermarcacionarray.length; i++){
                parametervalue = queriesParameters.idparameterToValue(idparametermarcacionarray[i]);
                // 1,0;9,1  -> lectora,verifica;lectora,inserta
                Log.d("Autorizaciones","parametervalue = " + parametervalue);
                String[] modosmarcasarray = parametervalue.split(";");
                // 1,0 [0] - 9,1 [1]

                for(int y = 0; y < modosmarcasarray.length; y++){
                    Log.d("Autorizaciones","modosmarcasarray[" + y + "] = " + modosmarcasarray[y]);
                    String[] modomarcaarray = modosmarcasarray[y].split(",");
                    // 1 [0] - 0 [1]
                    Log.d("Autorizaciones","modomarcaarray[] = " + modomarcaarray[0] + " - " + modomarcaarray[1]);

                    if(capturarlectorasiguiente){
                        lectorasiguiente = modomarcaarray[0];
                    }

                    Log.d("Autorizaciones","verificacion = " + modomarcaarray[0] + ":" + IdTipoLect);

                    if(modomarcaarray[0].equals(String.valueOf(IdTipoLect))){
                        Log.d("Autorizaciones","verificacion OK = " + modomarcaarray[0] + ":" + IdTipoLect);
                        insert = Integer.parseInt(modomarcaarray[1]);
                        // 0 = verifica/no inserta
                        // 1 = inserta
                        capturarlectorasiguiente = true;
                        autorizaciones = this.GestionarMarcaciones(ValorTarjeta,Idterminal,IdTipoLect,FlgActividad,Fechahora,insert);

                        apellidosnombres = autorizaciones.getApellidoPaterno() + " " + autorizaciones.getApellidoMaterno() + " " + autorizaciones.getNombres().substring(0,1);
                        valortarjeta = autorizaciones.getValorTarjeta();
                        mensaje = autorizaciones.getMensaje();
                        mensajedetalle = autorizaciones.getMensajeDetalle();
                        ModoMarcacion = idparametermarcacionarray[i];

                        /*
                        if(!autorizaciones.getMensaje().equalsIgnoreCase("null")){
                            apellidosnombres = autorizaciones.getApellidoPaterno() + " " + autorizaciones.getApellidoMaterno() + " " + autorizaciones.getNombres().substring(0,1);
                            valortarjeta = autorizaciones.getValorTarjeta();
                            mensaje = autorizaciones.getMensaje();
                            mensajedetalle = autorizaciones.getMensajeDetalle();
                            ModoMarcacion = idparametermarcacionarray[i];
                        }else{
                            mensaje = "MARCACION NO AUTORIZADA";
                            lectorasiguiente = "0";
                        }
                        */

                    }

                }
            }
        }

        output = apellidosnombres + "," + valortarjeta + "," + mensaje + "," + mensajedetalle + "," + lectorasiguiente + "," + ModoMarcacion;
        Log.d("Autorizaciones","output = " + output);
        return output;
    }

    public Autorizaciones GestionarMarcaciones(String ValorTarjeta, String Idterminal, int IdTipoLect, String FlgActividad, String Fechahora, int Insert){

        Log.d("Autorizaciones","GestionarMarcaciones: " + ValorTarjeta);
        Log.d("Autorizaciones","GestionarMarcaciones: " + Idterminal);
        Log.d("Autorizaciones","GestionarMarcaciones: " + IdTipoLect);
        Log.d("Autorizaciones","GestionarMarcaciones: " + FlgActividad);
        Log.d("Autorizaciones","GestionarMarcaciones: " + Fechahora);
        Log.d("Autorizaciones","GestionarMarcaciones: " + Insert);

        int a = 0;
        int flagEnableidtipolect = 1;
        String nullvalortarjeta = "0";
        int estadoPermiteAsistencia = 1;
        int permisoHabilitado = 1;
        QueriesAutorizaciones queriesAutorizaciones = new QueriesAutorizaciones(context);
        QueriesTerminalTipolect queriesTerminalTipolect = new QueriesTerminalTipolect(context);
        List<Autorizaciones> autorizacionesList = new ArrayList<Autorizaciones>();
        Autorizaciones autorizaciones = new Autorizaciones();

        Log.d("Autorizaciones","QueriesMarcaciones.GestionarMarcaciones: ");

        if(queriesTerminalTipolect.ConsultarLectora(Idterminal,IdTipoLect) == flagEnableidtipolect){
            if(ValorTarjeta != nullvalortarjeta && ValorTarjeta != null){
                autorizacionesList = queriesAutorizaciones.buscarAutorizaciones(ValorTarjeta,IdTipoLect,Idterminal);
                if(!autorizacionesList.isEmpty()){
                    if(!MarcacionRepetida(autorizacionesList.get(a), Fechahora, FlgActividad)){
                        if(autorizacionesList.get(a).getEstadoRequiereAsistencia() == estadoPermiteAsistencia){
                            if(autorizacionesList.get(a).getFlagPerTipoLectTerm() == permisoHabilitado){

                                autorizaciones.setNombres(autorizacionesList.get(a).getNombres());
                                autorizaciones.setApellidoPaterno(autorizacionesList.get(a).getApellidoPaterno());
                                autorizaciones.setApellidoMaterno(autorizacionesList.get(a).getApellidoMaterno());
                                autorizaciones.setValorTarjeta(ValorTarjeta);
                                autorizaciones.setIcono(autorizacionesList.get(a).getIcono());
                                autorizaciones.setMensaje("MARCACION AUTORIZADA");
                                autorizaciones.setMensajeDetalle("");

                                //INSERT MARCACION
                                Fechahora fechahora = new Fechahora();

                                Marcaciones marcaciones = new Marcaciones();
                                marcaciones.setEmpresa(autorizacionesList.get(a).getEmpresa());
                                marcaciones.setCodigo(autorizacionesList.get(a).getCodigo());
                                marcaciones.setFechahora(Fechahora);
                                marcaciones.setValorTarjeta(autorizacionesList.get(a).getValorTarjeta());
                                marcaciones.setHoraTxt(fechahora.getHora(Fechahora));
                                marcaciones.setEntSal("0");
                                marcaciones.setFlag("1");
                                marcaciones.setFecha(fechahora.getFechahoracero(Fechahora));
                                marcaciones.setHora(fechahora.getFechacerohora(Fechahora));
                                marcaciones.setIdterminal(Idterminal);
                                marcaciones.setIdTipoLect(IdTipoLect);
                                marcaciones.setFlgActividad(FlgActividad);
                                marcaciones.setIdUsuario(1);
                                marcaciones.setTmpListar("0");
                                marcaciones.setAutorizado(1);
                                marcaciones.setTipoOperacion(1);
                                marcaciones.setSincronizado(0);
                                //marcaciones.setDatos(null);
                                //marcaciones.setValorDatoContenido(0);
                                if(Insert == 1){
                                    this.insert(marcaciones);
                                    Log.d("Autorizaciones","Marcacion: " + marcaciones.toString());
                                    Log.d("Autorizaciones","Marcacion Registrada");
                                }else{
                                    Log.d("Autorizaciones","Marcacion: " + marcaciones.toString());
                                    Log.d("Autorizaciones","Marcacion Verificada");
                                }

                            }else{
                                autorizaciones.setNombres(autorizacionesList.get(a).getNombres());
                                autorizaciones.setApellidoPaterno(autorizacionesList.get(a).getApellidoPaterno());
                                autorizaciones.setApellidoMaterno(autorizacionesList.get(a).getApellidoMaterno());
                                autorizaciones.setValorTarjeta(ValorTarjeta);
                                autorizaciones.setIcono(autorizacionesList.get(a).getIcono());
                                autorizaciones.setMensaje("MARCACION NO AUTORIZADA");
                                autorizaciones.setMensajeDetalle("NO TIENE PERMISO EN ESTA LECTORA");
                            }
                        }else{
                            autorizaciones.setNombres(autorizacionesList.get(a).getNombres());
                            autorizaciones.setApellidoPaterno(autorizacionesList.get(a).getApellidoPaterno());
                            autorizaciones.setApellidoMaterno(autorizacionesList.get(a).getApellidoMaterno());
                            autorizaciones.setIcono(autorizacionesList.get(a).getIcono());
                            autorizaciones.setValorTarjeta(ValorTarjeta);
                            autorizaciones.setMensaje("MARCACION NO AUTORIZADA");
                            autorizaciones.setMensajeDetalle("ESTADO NO PERMITE MARCACION");
                        }
                    }else{
                        autorizaciones.setNombres(autorizacionesList.get(a).getNombres());
                        autorizaciones.setApellidoPaterno(autorizacionesList.get(a).getApellidoPaterno());
                        autorizaciones.setApellidoMaterno(autorizacionesList.get(a).getApellidoMaterno());
                        autorizaciones.setIcono(autorizacionesList.get(a).getIcono());
                        autorizaciones.setValorTarjeta(ValorTarjeta);
                        autorizaciones.setMensaje("MARCACION REPETIDA");
                        autorizaciones.setMensajeDetalle("");
                    }
                }
                else{
                    autorizaciones.setValorTarjeta(ValorTarjeta);
                    autorizaciones.setMensaje("MARCACION NO AUTORIZADA");
                    autorizaciones.setMensajeDetalle("TARJETA/BIOME NO REGISTRADA");
                }
            }else{
                autorizaciones.setValorTarjeta(ValorTarjeta);
                autorizaciones.setMensaje("MARCACION NO AUTORIZADA");
                autorizaciones.setMensajeDetalle("TARJETA/BIOME NO SE RECONOCE");
            }
        }else{
            autorizaciones.setMensaje("MARCACION NO AUTORIZADA");
            autorizaciones.setMensajeDetalle("LECTORA NO HABILITADA");
        }



        for(int i = 0; i < autorizacionesList.size(); i++){
            Log.d("Autorizaciones",autorizacionesList.get(i).toString());
            a = i;
        }


        return autorizaciones;
    }


    public boolean MarcacionRepetida(Autorizaciones autorizaciones, String Fechahora,String FlgActividad){

        this.open();
        int count = 0;
        int pasttime = -10; // -x segundos, minutos, horas en el pasado
        int futuretime = 3; // +x segundos, minutos, horas en el futuro
        int repeatType = 2; //1=MarcaRepetida por ValorTarjeta e IdTipoLect - 2=MarcaRepetida por Empresa y Codigo


        String query =
                "SELECT " +
                        "COUNT(*) " +
                        "FROM " + TableMarcaciones.TABLE_NAME + " " +
                        "WHERE (DATETIME('" + Fechahora + "','" + pasttime + " SECOND') <= DATETIME(" + TableMarcaciones.Fechahora + ") " +
                        "AND DATETIME('" + Fechahora + "','" + futuretime + " SECOND') > DATETIME(" + TableMarcaciones.Fechahora + ")) ";



        if(repeatType == 1){

            query = query + "AND " + TableMarcaciones.IdTipoLect + " = ? " +
                    "AND " + TableMarcaciones.ValorTarjeta + " = ? " +
                    "AND " + TableMarcaciones.FlgActividad + " = ? ";
            Log.d("Autorizaciones", query);

            Cursor cursor = database.rawQuery(query, new String[] { String.valueOf(autorizaciones.getIdTipoLect()), autorizaciones.getValorTarjeta(), FlgActividad});
            if(cursor.moveToNext()){
                count = cursor.getInt(0);
                Log.d("Autorizaciones", "Cantidad de marcaciones repetidas: " + count);
            }while(cursor.moveToNext());

            cursor.close();

        }else if(repeatType == 2){

            query = query + "AND " + TableMarcaciones.Empresa + " = ? " +
                    "AND " + TableMarcaciones.Codigo + " = ? " +
                    "AND " + TableMarcaciones.FlgActividad + " = ? ";
            Log.d("Autorizaciones", query);

            Cursor cursor = database.rawQuery(query, new String[] { autorizaciones.getEmpresa(), autorizaciones.getCodigo(), FlgActividad});
            if(cursor.moveToNext()){
                count = cursor.getInt(0);
                Log.d("Autorizaciones", "Cantidad de marcaciones repetidas: " + count);
            }while(cursor.moveToNext());

            cursor.close();
        }

        this.close();


        if(count == 0){
            return false;
        } else{
            return true;
        }

    }

    public void ActualizarSincronizado(Marcaciones marcaciones, int sincronizado){
        this.open();
        //conexion.onCreate(database);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableMarcaciones.Sincronizado, sincronizado);

        database.update(TableMarcaciones.TABLE_NAME, contentValues, TableMarcaciones.Empresa + " = ? AND " + TableMarcaciones.Codigo + " = ? AND " + TableMarcaciones.Fechahora + " = ? ", new String[] { marcaciones.getEmpresa(), marcaciones.getCodigo(), marcaciones.getFechahora()});
        Log.d("Autorizaciones","Registro actualizado");
        this.close();
    }


    public void FechahoraSqlite(){
        this.open();
        String query = "SELECT DATETIME('now');";

        Log.d("Autorizaciones", query);

        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            Log.d("Autorizaciones", "Fechahora del servicio SQLITE: " + cursor.getString(0));
        }while(cursor.moveToNext());

        cursor.close();
        this.close();

    }




}

