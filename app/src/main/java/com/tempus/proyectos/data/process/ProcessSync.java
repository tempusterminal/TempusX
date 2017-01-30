package com.tempus.proyectos.data.process;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.DBManagerServidor;
import com.tempus.proyectos.data.model.Estados;
import com.tempus.proyectos.data.model.Llamadas;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.queries.QueriesEstados;
import com.tempus.proyectos.data.queries.QueriesLlamadas;
import com.tempus.proyectos.data.tables.TableEstados;
import com.tempus.proyectos.util.Fechahora;

/**
 * Created by gurrutiam on 22/12/2016.
 */

public class ProcessSync {


    private Connection connection = null;
    private Statement statement;
    private ResultSet resultSet;
    private CallableStatement callableStatement;
    private PreparedStatement preparedStatement;

    private QueriesEstados queriesEstados;
    private QueriesLlamadas queriesLlamadas;
    private DBManager dbManager;


    public ProcessSync() {

    }

    public void SyncG(String fechahora, String idterminal, Context context){
        //Fechahora fh = new Fechahora();
        List <Estados> estadosList = new ArrayList<Estados>();
        Estados estados = new Estados();

        ConexionServidor conexionServidor = new ConexionServidor();
        if(connection == null){
            connection = conexionServidor.getInstance().getConnection();
        }

        String sql = "EXEC TEMPUS.USP_ETL_ESTADOS '" + fechahora + "'";
        Log.d("Autorizaciones",sql);

        try{

            if(connection.isClosed()){
                Log.d("Autorizaciones","Intentando restablecer conexion: " + connection.toString());
                connection = conexionServidor.conectar();
            }

            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                estados = new Estados();
                estados.setEstado(resultSet.getString(TableEstados.Estado));
                estados.setDescripcion(resultSet.getString(TableEstados.Descripcion));
                estados.setRequiereAsistencia(resultSet.getInt(TableEstados.RequiereAsistencia));
                estados.setFechaHoraSinc(resultSet.getString(TableEstados.FechaHoraSinc));
                Log.d("Autorizaciones",estados.toString());
                estadosList.add(estados);
            }

            Log.d("Autorizaciones","Size: " + estadosList.size());

            for(int i = 0; i < estadosList.size(); i++){
                try{
                    queriesEstados = new QueriesEstados(context);
                    Log.d("Autorizaciones","Insertando Tabla Estados...");
                    if(queriesEstados.insert(estadosList.get(i)) > 0){
                        Log.d("Autorizaciones","Row " + (i + 1) + ": " + estadosList.get(i).toString());
                        Log.d("Autorizaciones","Inserción con exito");
                    }else{
                        Log.d("Autorizaciones","No se pudo insertar...");
                        Log.d("Autorizaciones", "Actualizando Tabla Estados...");
                        queriesEstados.update(estadosList.get(i));
                        Log.d("Autorizaciones", "Row " + i + ": " + estadosList.get(i).toString());
                        Log.d("Autorizaciones", "Actualización con exito");
                    }
                }catch(SQLException e){
                    Log.d("Autorizaciones","No se pudo realizar la inserción / actualización");
                }
            }

        }catch(SQLException e){
            Log.d("Autorizaciones","Error SQLite: " + e.getMessage());
        }catch (java.sql.SQLException e) {
            Log.d("Autorizaciones","Error SQLServer: " + e.getMessage());
        }

    }


    public void Sync(String idllamada, String llamada, String parameters, String tablename, String primarykey, String columns, Context context){
        String insert = "";
        String values = "";
        String update = "";
        String set = "";
        String where = "";
        String parametersnamesvalues = "";
        String[] parametersnamesarray = parameters.split(";");
        String[] parameter;
        String[] allcolumnsarray = (primarykey + "," + columns).split(",");
        String[] primarykeyarray = primarykey.split(",");
        String[] columnsarray = columns.split(",");

        dbManager = new DBManager(context);



        //Log.d ("Autorizaciones","parametros varios: " + parametersnamesarray[0] + " - " + parametersnamesarray[1]);
        Log.d ("Autorizaciones","parametersnamesarray.length: " + parametersnamesarray.length);

        if(parametersnamesarray.length > 0){
            for(int i = 0; i < parametersnamesarray.length; i++){

                parameter = parametersnamesarray[i].split("&");
                Log.d ("Autorizaciones","parametro nombre y valor: " + parameter[0] + " - " + parameter[1]);
                try{

                    parametersnamesvalues = parametersnamesvalues + parameter[0] + "," + dbManager.valexecSQL(parameter[1]);
                }catch(SQLException e){
                    Log.d ("Autorizaciones","Error 01: " + e.getMessage());
                }
                if(i < parametersnamesarray.length - 1){
                    parametersnamesvalues = parametersnamesvalues + ";";
                }
            }
        }

        Log.d ("Autorizaciones","Parametros: " + parametersnamesvalues);

        ConexionServidor conexionServidor = new ConexionServidor();
        if(connection == null){
            connection = conexionServidor.getInstance().getConnection();
        }

        String sql = llamada + " '" + idllamada + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'";
        Log.d("Autorizaciones",sql);

        try{
            if(connection.isClosed()){
                Log.d("Autorizaciones","Intentando restablecer conexion: " + connection.toString());
                connection = conexionServidor.conectar();
            }

            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){

                try{
                    Log.d ("Autorizaciones","Insertando Tabla " + tablename + ":");


                    values = "";
                    for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++){
                        if(resultSet.getString(allcolumnsarray[i]) != null){
                            values = values + "'" + resultSet.getString(allcolumnsarray[i]) + "'";
                        }else{
                            values = values + "" + resultSet.getString(allcolumnsarray[i]) + "";
                        }
                        if(i < resultSet.getMetaData().getColumnCount() - 1){
                            values = values + ",";
                        }
                    }

                    insert = "INSERT INTO " + tablename + "(" + primarykey + "," + columns + ") " +
                            "VALUES(" + values + ");";
                    Log.d("Autorizaciones","RowInsert: " + insert);


                    try{
                        dbManager.execSQL(insert);
                        Log.d("Autorizaciones","Registro insertado");
                    }catch(SQLException e){
                        Log.d("Autorizaciones","Actualizando Tabla " + tablename + ": " + e.getMessage());

                        set = "";
                        if(columnsarray.length > 0){
                            for(int i = 0; i < columnsarray.length ; i++){
                                if(resultSet.getString(columnsarray[i]) != null){
                                    set = set + columnsarray[i] + "='" + resultSet.getString(columnsarray[i]) + "'";
                                }else{
                                    set = set + columnsarray[i] + "=" + resultSet.getString(columnsarray[i]) + "";
                                }
                                if(i < columnsarray.length - 1){
                                    set = set + ", ";
                                }
                            }
                        }

                        where = "";
                        if(primarykeyarray.length > 0){
                            for(int i = 0; i < primarykeyarray.length ; i++){
                                where = where + primarykeyarray[i] + " = '" + resultSet.getString(primarykeyarray[i]) + "'";
                                if(i < primarykeyarray.length - 1){
                                    where = where + " AND ";
                                }
                            }
                        }

                        update = "UPDATE " + tablename +
                                " SET " + set +
                                " WHERE " + where +
                                ";";

                        Log.d("Autorizaciones","RowUpdate: " + update);
                        try{
                            dbManager.execSQL(update);
                            Log.d("Autorizaciones","Registro actualizado");
                        }catch(SQLException ex){
                            Log.d("Autorizaciones","Error SQL: " + ex.getMessage());
                        }

                    }

                }catch(SQLException e){
                    Log.d("Autorizaciones","No se pudo realizar la inserción / actualización");
                }

            }


        }catch(SQLException e){
            Log.d("Autorizaciones","Error SQLite: " + e.getMessage());
        }catch (java.sql.SQLException e) {
            Log.d("Autorizaciones","Error SQLServer: " + e.getMessage());
        }

    }

    public int syncMarcaciones(Marcaciones marcaciones) throws java.sql.SQLException {
        Fechahora fechahora = new Fechahora();
        int rowaffected = 0;
        ConexionServidor conexionServidor = new ConexionServidor();

        if(connection == null){
            connection = conexionServidor.getInstance().getConnection();
        }

        String sql = "INSERT INTO TEMPUS.MARCACIONES(EMPRESA,CODIGO,FECHAHORA,NUMERO_TARJETA,HORATXT,ENT_SAL,FLAG,FECHA,HORA,IDTERMINAL,IDLECTORA,IDMEMO,FLG_ACTIVIDAD,IDUSUARIO,IDENTIFICADOR,TMP_LISTAR,FECHA_REGISTRO,DATOS) VALUES(" +
                "'" + marcaciones.getEmpresa() + "'," +
                "'" + marcaciones.getCodigo() + "'," +
                "'" + fechahora.getFechahoraSqlServer(marcaciones.getFechahora()) + "'," +
                "'" + marcaciones.getValorTarjeta() + "'," +
                "'" + marcaciones.getHoraTxt() + "'," +
                "'" + marcaciones.getEntSal() + "'," +
                "'" + marcaciones.getFlag() + "'," +
                "'" + fechahora.getFechahoraSqlServer(marcaciones.getFecha()) + "'," +
                "'" + fechahora.getFechahoraSqlServer(marcaciones.getHora()) + "'," +
                "'" + marcaciones.getIdterminal() + "'," +
                "'" + marcaciones.getIdTipoLect() + "'," +
                "" + null + "," +
                "'" + marcaciones.getFlgActividad() + "'," +
                "'" + marcaciones.getIdUsuario() + "'," +
                "" + null + "," +
                "'" + marcaciones.getTmpListar() + "'," +
                "" + "GETDATE()" + "," +
                "" + null + ")";
        Log.d("Autorizaciones",sql);

        try{
            if(connection.isClosed()){
                Log.d("Autorizaciones","Intentando restablecer conexion: " + connection.toString());
                connection = conexionServidor.conectar();
            }
            statement = connection.createStatement();
            rowaffected = statement.executeUpdate(sql);

            Log.d("Autorizaciones","Registro insertado");

        }catch (SQLException e) {
            Log.d("Autorizaciones","DBManagerServidor.syncMarcaciones SQLException SQLite: " + e.getMessage());
        }catch (java.sql.SQLException e) {
            Log.d("Autorizaciones","DBManagerServidor.syncMarcaciones SQLException SQLServer: " + e.getMessage());
            if(e.getMessage().contains("No se puede insertar una clave duplicada en el objeto")){
                rowaffected = 1;
                Log.d("Autorizaciones","Registro ya insertado");
            }
        }finally {
            connection.close();
            Log.d("Autorizaciones","Conexion cerrada");
        }
        Log.d("Autorizaciones","Cantidad de filas afectadas: " + String.valueOf(rowaffected));
        return rowaffected;
    }


    public void Process(Context context){

        String process = "SYNC_ESTADOS_TX,SYNC_EMPRESAS_TX,SYNC_TIPO_LECTORA_TX,SYNC_TIPO_DETALLE_BIOMETRIA_TX,SYNC_TERMINAL_TX,SYNC_TERMINAL_TIPOLECT_TX,SYNC_PERSONAL_TX,SYNC_PER_TIPOLECT_TERM_TX,SYNC_TARJETA_PERSONAL_TIPOLECTORA_TX,SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_TX";
        //String process = "SYNC_ESTADOS_TX,SYNC_EMPRESAS_TX,SYNC_TIPO_LECTORA_TX,SYNC_TIPO_DETALLE_BIOMETRIA_TX,SYNC_TERMINAL_TX,SYNC_TERMINAL_TIPOLECT_TX,SYNC_PERSONAL_TX,SYNC_PER_TIPOLECT_TERM_TX";
        //String process = "SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_TX";
        String[] processarray = process.split(",");

        Log.d("Autorizaciones", "processarray.length: " + String.valueOf(processarray.length) );
        queriesLlamadas = new QueriesLlamadas(context);

        for(int i = 0; i < processarray.length; i++){

            List<Llamadas> llamadasList = queriesLlamadas.select_one_row(processarray[i]);

            Log.d("Autorizaciones","llamadasList.size(): " + String.valueOf(llamadasList.size()));
            for(int y = 0; y < llamadasList.size(); y++){
                Log.d("Autorizaciones",llamadasList.get(y).toString());
                try{
                    Sync(llamadasList.get(y).getIdllamada(),llamadasList.get(y).getLlamada(),llamadasList.get(y).getParameters(),llamadasList.get(y).getTableName(),llamadasList.get(y).getPrimarykey(),llamadasList.get(y).getColumns(),context);
                }catch(Exception e){
                    Log.d("Autorizaciones","ProcessSync.Process Error: " + e.getMessage());
                }

            }

        }

    }




}

