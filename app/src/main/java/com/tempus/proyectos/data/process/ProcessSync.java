package com.tempus.proyectos.data.process;

import android.app.AlarmManager;
import android.content.Context;
import android.database.SQLException;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.data.ConexionWebService;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Llamadas;
import com.tempus.proyectos.data.model.LogTerminal;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.model.PersonalTipolectoraBiometria;
import com.tempus.proyectos.data.queries.QueriesEstados;
import com.tempus.proyectos.data.queries.QueriesLlamadas;
import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by gurrutiam on 22/12/2016.
 */

public class ProcessSync {

    private String TAG = "DA-PR-PSY";


    private Connection connection = null;
    private Statement statement;
    private ResultSet resultSet;
    private CallableStatement callableStatement;
    private PreparedStatement preparedStatement;

    private QueriesEstados queriesEstados;
    private QueriesLlamadas queriesLlamadas;
    private QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;
    private QueriesMarcaciones queriesMarcaciones;
    private QueriesLogTerminal queriesLogTerminal;
    private DBManager dbManager;
    private ArrayList<ArrayList<String>> aavaluesiu = new ArrayList<ArrayList<String>>();
    private ArrayList<String> avaluesiu = new ArrayList<String>();
    private int ivaluesiu = 0;


    public ProcessSync() {

    }

    public List<Llamadas> getLlamadas(Context context){
        String process = "SYNC_ESTADOS_TX,SYNC_EMPRESAS_TX,SYNC_TIPO_LECTORA_TX,SYNC_TIPO_DETALLE_BIOMETRIA_TX,SYNC_TERMINAL_TX,SYNC_TERMINAL_TIPOLECT_TX,SYNC_PERSONAL_TX,SYNC_PER_TIPOLECT_TERM_TX,SYNC_TARJETA_PERSONAL_TIPOLECTORA_TX,SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_TX";

        //String process = "SYNC_ESTADOS_TX,SYNC_EMPRESAS_TX,SYNC_TIPO_LECTORA_TX,SYNC_TIPO_DETALLE_BIOMETRIA_TX,SYNC_TERMINAL_TX,SYNC_TERMINAL_TIPOLECT_TX,SYNC_PERSONAL_TX,SYNC_PER_TIPOLECT_TERM_TX";
        //String process = "SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_TX";
        //String process = "SYNC_PERSONAL_TX";
        //String process = "SYNC_ESTADOS_TX";
        //process = "";
        String[] processarray = process.split(",");

        //Log.v(TAG, "processarray.length: " + String.valueOf(processarray.length) );
        queriesLlamadas = new QueriesLlamadas(context);
        List<Llamadas> llamadasList = new ArrayList<Llamadas>();
        try{
            for(int i = 0; i < processarray.length; i++){
                llamadasList.add(queriesLlamadas.select_one_row(processarray[i]).get(0));
                //Log.v(TAG,"llamadasList.size(): " + String.valueOf(llamadasList.size()));
            }
            //Log.v(TAG,"getLlamadas: " + llamadasList.toString());
        }catch(Exception e){
            Log.e(TAG,"getLlamadas: " + e.getMessage());
        }
        return llamadasList;
    }

    public String prepareParametersLlamadas(String parameters, Context context){
        //String insert = "";
        //String values = "";
        //String update = "";
        //String set = "";
        //String where = "";
        String parametersnamesvalues = "";
        String[] parametersnamesarray = parameters.split(";");
        String[] parameter;
        //String[] allcolumnsarray = (primarykey + "," + columns).split(",");
        //String[] primarykeyarray = primarykey.split(",");
        //String[] columnsarray = columns.split(",");
        //int resultSetCount = 0;

        dbManager = new DBManager(context);
        //Log.v (TAG,"parametersnamesarray.length: " + parametersnamesarray.length);

        if(parametersnamesarray.length > 0){
            for(int i = 0; i < parametersnamesarray.length; i++){

                parameter = parametersnamesarray[i].split("&");
                //Log.v (TAG,"parametro nombre y valor: " + parameter[0] + " - " + parameter[1]);
                try{
                    parametersnamesvalues = parametersnamesvalues + parameter[0] + "," + dbManager.valexecSQL(parameter[1]);
                }catch(SQLException e){
                    Log.v (TAG,"Error 01: " + e.getMessage());
                }
                if(i < parametersnamesarray.length - 1){
                    parametersnamesvalues = parametersnamesvalues + ";";
                }
            }
        }

        return parametersnamesvalues;

    }

    public ArrayList<ArrayList<String>> executeLlamadasServer(String idllamada, String llamada, String parametersnamesvalues, String primarykey, String columns){
        String[] allcolumnsarray = (primarykey + "," + columns).split(",");

        ArrayList<ArrayList<String>> aaviu = new ArrayList<ArrayList<String>>();
        ArrayList<String> aviu = new ArrayList<String>();

        ConexionServidor conexionServidor = new ConexionServidor();
        if(connection == null){
            connection = conexionServidor.getInstance().getConnection();
        }

        String sql = llamada + " '" + idllamada + "'" + ",'','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'";
        //Log.v(TAG,sql);
        Log.v(TAG,"Llamada: " + idllamada + " - " + parametersnamesvalues);

        try{
            if(connection.isClosed()){
                //Log.v(TAG,"Intentando restablecer conexion: " + connection.toString());
                connection = conexionServidor.conectar();
            }

            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            aaviu = new ArrayList<ArrayList<String>>();
            ivaluesiu = 0;
            while(resultSet.next()){
                try{
                    aviu = new ArrayList<String>();
                    for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++){
                        aviu.add(i,resultSet.getString(allcolumnsarray[i]));
                        //Log.v(TAG,"resultSet(" + ivaluesiu + "," + i + "): " + resultSet.getString(allcolumnsarray[i]));
                    }

                    aaviu.add(ivaluesiu,aviu);
                    //Log.v(TAG,"aaviu: " + ivaluesiu + " - " + aaviu.toString());

                    ivaluesiu++;

                }catch(Exception e){
                    Log.e(TAG,"prepareParametersLlamadas.values error: " + e.getMessage());
                }
            }
            resultSet.close();
        }catch (java.sql.SQLException e) {
            Log.e(TAG,"executeLlamadasServer SQLException " + e.getMessage());
        }catch(Exception e){
            Log.e(TAG,"executeLlamadasServer " + e.getMessage());
        }

        return aaviu;

    }

    public ArrayList<ArrayList<String>> executeLlamadasWs(String idllamada, String llamada, String parametersnamesvalues, String primarykey, String columns){
        String[] allcolumnsarray = (primarykey + "," + columns).split(",");
        String respuesta = "";

        ArrayList<ArrayList<String>> namesvalues = new ArrayList<ArrayList<String>>();
        ArrayList<String> namevalue = new ArrayList<String>();

        ArrayList<ArrayList<String>> aaviu = new ArrayList<ArrayList<String>>();
        ArrayList<String> aviu = new ArrayList<String>();

        ConexionWebService conexionWebService = new ConexionWebService();
        JSONArray jsonArray = new JSONArray();

        if(ConexionWebService.authenticated == true){
            try{
                // {"session":17922,"llamada":"607_EXEC_L2","parametros":"'SYNC_PERSONAL_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,20/09/2014 18:46:03.775'"}
                // {"session":17922,"llamada":"607_EXEC_L2","parametros":"'SYNC_PERSONAL_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,20/09/2014 18:46:03.775'"}
                //String data = llamada + " '" + idllamada + "'" + ",'','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'";

                namevalue.add("OPCION");
                namevalue.add("GENERAR_DATA3");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                namevalue.add("DATA");
                namevalue.add("{\"session\":" + ConexionWebService.jsonAuthenticated.getString("ID_SESSION") + ",\"llamada\":\"" + ConexionWebService.jsonAuthenticated.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + idllamada + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                Log.v(TAG,"Llamada: " + idllamada + " - " + parametersnamesvalues);
                Log.v(TAG,"namesvalues: " + namesvalues.toString());

                respuesta = conexionWebService.llamadasST(namesvalues);
                Log.v(TAG,"respuesta <" + idllamada + "> " + respuesta);

                if(respuesta.isEmpty()){
                    Log.v(TAG,"respuesta vacia (no json)");
                }else{
                    jsonArray = new JSONArray(respuesta);
                    aaviu = new ArrayList<ArrayList<String>>();
                    ivaluesiu = 0;
                    for(ivaluesiu = 0; ivaluesiu < jsonArray.length(); ivaluesiu++){
                        try{
                            aviu = new ArrayList<String>();
                            //Log.v(TAG, jsonArray.getJSONObject(ivaluesiu).length() + " - " + jsonArray.getJSONObject(ivaluesiu).toString());
                            for(int i = 0; i < allcolumnsarray.length; i++){
                                String value = jsonArray.getJSONObject(ivaluesiu).getString(allcolumnsarray[i]);
                                try{
                                    value = new JSONObject(value).getString("date");
                                }catch (Exception e){
                                }

                                if(value.equalsIgnoreCase("null")){
                                    aviu.add(i,null);
                                }else{
                                    aviu.add(i,value);
                                }
                                //Log.v(TAG,"value " + value);
                                //Log.v(TAG,"resultSet(" + ivaluesiu + "," + i + "): " + resultSet.getString(allcolumnsarray[i]));
                            }
                            aaviu.add(ivaluesiu,aviu);
                            //Log.v(TAG,"aaviu: " + ivaluesiu + " - " + aaviu.toString());

                        }catch(Exception e){
                            Log.e(TAG,"prepareParametersLlamadas.values error: " + e.getMessage());
                        }
                    }
                }

            }catch(Exception e){
                Log.e(TAG,"executeLlamadasWs " + e.getMessage());
            }
        }else{
            conexionWebService.autenticar();
            Log.v(TAG,"Autenticación en proceso");
        }

        return aaviu;

    }


    public void insertUpdateData(ArrayList<ArrayList<String>> aaviu, String tablename, String primarykey, String columns, Context context){
        String insert = "";
        String values = "";
        String update = "";
        String set = "";
        String where = "";

        String[] primarykeyarray = primarykey.split(",");
        String[] columnsarray = columns.split(",");

        dbManager = new DBManager(context);
        dbManager.open();

        if(aaviu.size()>0){
            try {
                //Log.v(TAG,"Insertando Tabla " + tablename + ":");

                for (int i = 0; i < aaviu.size(); i++) {
                    values = "";
                    for (int y = 0; y < aaviu.get(i).size(); y++) {
                        //Log.v(TAG,"valuesiu[" + i + "," + y + "]: " + aaviu.get(i).get(y));

                        if (aaviu.get(i).get(y) != null) {
                            values = values + "'" + aaviu.get(i).get(y) + "'";
                        } else {
                            values = values + "" + aaviu.get(i).get(y) + "";
                        }
                        if (y < aaviu.get(i).size() - 1) {
                            values = values + ",";
                        }

                    }
                    //Log.v(TAG,"values: " + values);

                    insert = "INSERT INTO " + tablename + "(" + primarykey + "," + columns + ") " +
                            "VALUES(" + values + ");";

                    try {
                        dbManager.execSQL(insert);
                        //dbManager.close();
                        //Thread.sleep(50);
                        //Log.v(TAG,"Registro insertado");
                        Log.v(TAG, "BTS-MAET " + "(" + (i + 1) + "/" + aaviu.size() + ")" + " " + insert);

                    } catch (SQLException e) {
                        //Log.e(TAG,"update insert " + tablename + ": " + e.getMessage());

                        where = "";
                        if (primarykeyarray.length > 0) {
                            for (int y = 0; y < primarykeyarray.length; y++) {
                                where = where + primarykeyarray[y] + " = '" + aaviu.get(i).get(y) + "'";
                                if (y < primarykeyarray.length - 1) {
                                    where = where + " AND ";
                                }
                            }
                        }

                        set = "";
                        if (columnsarray.length > 0) {
                            for (int z = 0; z < columnsarray.length; z++) {
                                if (aaviu.get(i).get(z + primarykeyarray.length) != null) {
                                    set = set + columnsarray[z] + "='" + aaviu.get(i).get(z + primarykeyarray.length) + "'";
                                } else {
                                    set = set + columnsarray[z] + "=" + aaviu.get(i).get(z + primarykeyarray.length) + "";
                                }
                                if (z < columnsarray.length - 1) {
                                    set = set + ", ";
                                }

                            }
                        }

                        update = "UPDATE " + tablename +
                                " SET " + set +
                                " WHERE " + where +
                                ";";

                        try {
                            dbManager.execSQL(update);
                            //dbManager.close();
                            //Thread.sleep(50);
                            //Log.v(TAG,"Registro actualizado");
                            Log.v(TAG,"BTS-MAET " + "(" + (i + 1) + "/" + aaviu.size() + ")" + " " + update);
                                /*
                                if(i%1000==0){
                                    Thread.sleep(3000);
                                }
                                */
                        } catch (SQLException ex) {
                            Log.e(TAG, "Error SQL update " + ex.getMessage());
                            Thread.sleep(50);
                        } catch (Exception ex) {
                            Log.e(TAG, "Error update " + ex.getMessage());
                            Thread.sleep(50);
                        }
                    }
                }


            } catch (SQLException e) {
                Log.e(TAG, "insertUpdateData No se pudo realizar la inserción / actualización " + e.getMessage());
            } catch(Exception e){
                Log.e(TAG, "insertUpdateData Error general " + e.getMessage());
            }finally {
                //dbManager.close();
            }
        }else{
            Log.v(TAG, "BTS-MAET " + "Sin registros por sincronizar");
        }

    }

    public int syncMarcaciones(Marcaciones marcaciones){
        //Este método o función envía las marcaciones pors sincronizar al servidor principal

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
        Log.v(TAG,sql);

        try{
            if(connection.isClosed()){
                Log.v(TAG,"Intentando restablecer conexion: " + connection.toString());
                connection = conexionServidor.conectar();
            }
            statement = connection.createStatement();
            rowaffected = statement.executeUpdate(sql);

            Log.v(TAG,"Registro insertado");

        }catch (SQLException e) {
            Log.v(TAG,"DBManagerServidor.syncMarcaciones SQLException SQLite: " + e.getMessage());
        }catch (java.sql.SQLException e) {
            Log.v(TAG,"DBManagerServidor.syncMarcaciones SQLException SQLServer: " + e.getMessage());
            if(e.getMessage().contains("No se puede insertar una clave duplicada en el objeto")){
                rowaffected = 1;
                Log.v(TAG,"Registro ya insertado");
            }
        }finally {
            try{
                connection.close();
                Log.v(TAG,"Conexion cerrada");
            }catch(Exception e){
                Log.e(TAG,"Error cerrando conexion " + e.getMessage());
            }
        }
        Log.v(TAG,"Cantidad de filas afectadas: " + String.valueOf(rowaffected));
        return rowaffected;
    }

    public void syncMarcacionesWs(Marcaciones marcaciones){
        //Este método o función envía las marcaciones pors sincronizar al web service
        queriesMarcaciones = new QueriesMarcaciones(ActivityPrincipal.context);
        ArrayList<ArrayList<String>> namesvalues = new ArrayList<ArrayList<String>>();
        ArrayList<String> namevalue = new ArrayList<String>();
        String respuesta;
        ConexionWebService conexionWebService = new ConexionWebService();
        JSONArray jsonArray;
        JSONObject jsonObject;

        Fechahora fechahora = new Fechahora();
        int rowaffected = 0;
        ConexionServidor conexionServidor = new ConexionServidor();

        if(ConexionWebService.authenticated == true){

            try{
                String parametersnamesvalues = "pEMPRESA," + marcaciones.getEmpresa() +
                        ";pCODIGO," + marcaciones.getCodigo() +
                        ";pFECHAHORA," + fechahora.getFechahoraSqlServer(marcaciones.getFechahora()) +
                        ";pNUMERO_TARJETA," + marcaciones.getValorTarjeta() +
                        ";pHORATXT," + marcaciones.getHoraTxt() +
                        ";pENT_SAL," + marcaciones.getEntSal() +
                        ";pFLAG," + marcaciones.getFlag() +
                        ";pFECHA," + fechahora.getFechahoraSqlServer(marcaciones.getFecha()) +
                        ";pHORA," + fechahora.getFechahoraSqlServer(marcaciones.getHora()) +
                        ";pIDTERMINAL," + marcaciones.getIdterminal() +
                        ";pIDLECTORA," + marcaciones.getIdTipoLect() +
                        ";pFLG_ACTIVIDAD," + marcaciones.getFlgActividad() +
                        ";pIDUSUARIO," + marcaciones.getIdUsuario() +
                        ";pTMP_LISTAR," + marcaciones.getTmpListar() +
                        ";pDATOS," + marcaciones.getDatos();

                namevalue.add("OPCION");
                namevalue.add("GENERAR_DATA3");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                namevalue.add("DATA");
                namevalue.add("{\"session\":" + ConexionWebService.jsonAuthenticated.getString("ID_SESSION") + ",\"llamada\":\"" + ConexionWebService.jsonAuthenticated.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + "SYNC_MARCACIONES_TX" + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                //Log.v(TAG,"Llamada: " + idllamada + " - " + parametersnamesvalues);
                //Log.v(TAG,"namesvalues: " + namesvalues.toString());

                respuesta = conexionWebService.llamadasST(namesvalues);
                //jsonObject = new JSONObject(respuesta);


                Log.v(TAG, "respuesta syncMarcacionesWs: " + respuesta);
                if (respuesta.contains("[{\"MESSAGE\":1}]")) {
                    Log.v(TAG,"Sincronización de marcación OK (i)");
                    queriesMarcaciones.ActualizarSincronizado(marcaciones, 1);
                } else if (respuesta.contains("[{\"MESSAGE\":\"1\"}]")) {
                    Log.v(TAG,"Sincronización de marcación OK (s)");
                    queriesMarcaciones.ActualizarSincronizado(marcaciones, 1);
                } else if (respuesta.contains("Infracción de la restricción PRIMARY KEY")) {
                    Log.v(TAG,"Sincronización de marcación OK (Infracción de la restricción)");
                    queriesMarcaciones.ActualizarSincronizado(marcaciones, 1);
                }else if (respuesta.contains("ORA-00001")) {
                    Log.v(TAG,"Sincronización de marcación OK (ORA-00001)");
                    queriesMarcaciones.ActualizarSincronizado(marcaciones, 1);
                } else if (respuesta.contains("No se puede insertar el valor NULL")) {
                    Log.v(TAG, "No se completo la sincronización de marcaciones -> La columna no admite valores NULL");
                } else {
                    Log.v(TAG, "No se completo la sincronización de marcaciones");
                }

            }catch (Exception e){
                Log.v(TAG,"syncMarcacionesWs " + e.getMessage());
            }
        }

        //return rowaffected;
    }

    public int syncMarcacionesWsG(Marcaciones marcaciones){
        //Este método o función envía las marcaciones pors sincronizar al servidor Google
        int rowaffected = 0;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //URL url = new URL("https://script.google.com/macros/s/AKfycbzGBO_zkOSZke0B4DHmpxGaDd9tOc9TsuWW2X_fG-SOB2s9KfE/exec");
            String url = HttpUrl.parse("https://script.google.com/macros/s/AKfycbzQQcRUoudjcNTDRQJ98fovIBfXaW1wq0nmwPkJXBJ_6LKNkmqG/exec").toString();

            Log.v(TAG,"marcaciones>>>" + marcaciones.toString());

            String datos = "";
            if(marcaciones.getDatos() == null){
                datos = "";
            }else if(marcaciones.getDatos().equalsIgnoreCase("null")){
                datos = "";
            }else{
                datos = marcaciones.getDatos();
            }
            RequestBody formBody = new FormBody.Builder()
                    .add("empresa",marcaciones.getEmpresa())
                    .add("codigo",marcaciones.getCodigo())
                    .add("fechahora",marcaciones.getFechahora())
                    .add("numero_tarjeta",marcaciones.getValorTarjeta())
                    .add("horatxt",marcaciones.getHoraTxt())
                    .add("ent_sal",marcaciones.getEntSal())
                    .add("flag",marcaciones.getFlag())
                    .add("fecha",marcaciones.getFecha())
                    .add("hora",marcaciones.getHora())
                    .add("idterminal",marcaciones.getIdterminal())
                    .add("idlectora",String.valueOf(marcaciones.getIdTipoLect()))
                    .add("flg_actividad",marcaciones.getFlgActividad())
                    .add("idusuario",String.valueOf(marcaciones.getIdUsuario()))
                    .add("tmp_listar",marcaciones.getTmpListar())
                    .add("datos",datos)
                    .add("id","1tWS9sGSc3ULsW1ODR9N9skGDr_pnhNgHr2mhtGUXZZI")
                    .build();

            Log.v(TAG,"formBody>>>" + formBody.toString());

            OkHttpClient client = new OkHttpClient().newBuilder()
                    //.connectTimeout(2, TimeUnit.SECONDS)
                    //.readTimeout(2, TimeUnit.SECONDS)
                    //.writeTimeout(2, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Log.v(TAG,"request>>>" + request.toString());

            Response response = client.newCall(request).execute();
            String respuesta = response.body().string();
            Log.v(TAG,"response>>>" + respuesta);

            if(respuesta.contains("Insertion successful")){
                rowaffected = 1;
                Log.v(TAG,"Registro insertado");
            }

            Log.v(TAG,"callWSGoogle OK");
        } catch (Exception e) {
            Log.e(TAG,"callWSGoogle " + e.getMessage());
        }

        Log.v(TAG,"Cantidad de filas afectadas: " + String.valueOf(rowaffected));
        return rowaffected;
    }

    public int syncBiometrias(PersonalTipolectoraBiometria personalTipolectoraBiometria) {
        //Este método o función envía las biometrias pors sincronizar al servidor principal
        Fechahora fechahora = new Fechahora();
        int rowaffected = 0;
        String ValorBiometria = "";
        String ImagenBiometria = "";
        ConexionServidor conexionServidor = new ConexionServidor();

        if(personalTipolectoraBiometria.getValorBiometria() == null){
            ValorBiometria = "NULL";
        }else{
            ValorBiometria = "'" + personalTipolectoraBiometria.getValorBiometria() + "'";
        }

        if(personalTipolectoraBiometria.getImagenBiometria() == null){
            ImagenBiometria = "NULL";
        }else{
            ImagenBiometria = "'" + personalTipolectoraBiometria.getImagenBiometria() + "'";
        }

        if(connection == null){
            connection = conexionServidor.getInstance().getConnection();
        }

        String sql = "UPDATE TEMPUS.PERSONAL_TIPOLECTORA_BIOMETRIA " +
                "SET PERSONAL_TIPOLECTORA_BIOMETRIA.VALOR_BIOMETRIA = " + ValorBiometria  + ", " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.IMAGEN_BIOMETRIA = " + ImagenBiometria + ", " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.FECHA_HORA_SINC = '" + fechahora.getFechahoraSync(personalTipolectoraBiometria.getFechaHoraSinc())+ "', " +
                "PERSONAL_TIPOLECTORA_BIOMETRIA.FECHA_BIOMETRIA = '" + personalTipolectoraBiometria.getFechaBiometria() + "' " +
                "WHERE PERSONAL_TIPOLECTORA_BIOMETRIA.ID_PER_TIPOLECT_BIO = " + personalTipolectoraBiometria.getIdPerTipolectBio() + " " +
                "AND PERSONAL_TIPOLECTORA_BIOMETRIA.INDICE_BIOMETRIA = " + personalTipolectoraBiometria.getIndiceBiometria() + " " +
                "AND PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA = '" + personalTipolectoraBiometria.getEmpresa() + "' " +
                "AND PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO = '" + personalTipolectoraBiometria.getCodigo() + "' " +
                ";";
        Log.v(TAG,sql);

        try{
            if(connection.isClosed()){
                Log.v(TAG,"Intentando restablecer conexion: " + connection.toString());
                connection = conexionServidor.conectar();
            }
            statement = connection.createStatement();
            rowaffected = statement.executeUpdate(sql);

            Log.v(TAG,"Registro actualizado");
        }catch (SQLException e) {
            Log.v(TAG,"DBManagerServidor.syncMarcaciones SQLException SQLite: " + e.getMessage());
        }catch (java.sql.SQLException e) {
            Log.v(TAG,"DBManagerServidor.syncMarcaciones SQLException SQLServer: " + e.getMessage());
        }finally {
            try{
                connection.close();
                Log.v(TAG,"Conexion cerrada");
            }catch(Exception e){
                Log.e(TAG,"Error cerrando conexion " + e.getMessage());
            }
        }
        Log.v(TAG,"Cantidad de filas afectadas: " + String.valueOf(rowaffected));
        return rowaffected;

    }

    public void syncBiometriasWs(PersonalTipolectoraBiometria personalTipolectoraBiometria) {
        //Este método o función envía las biometrias pors sincronizar al servidor web service
        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(ActivityPrincipal.context);

        ArrayList<ArrayList<String>> namesvalues = new ArrayList<ArrayList<String>>();
        ArrayList<String> namevalue = new ArrayList<String>();
        String respuesta;
        ConexionWebService conexionWebService = new ConexionWebService();
        JSONArray jsonArray;

        Fechahora fechahora = new Fechahora();
        int rowaffected = 0;

        if(ConexionWebService.authenticated == true){

            try{
                String parametersnamesvalues = "pID_PER_TIPOLECT_BIO," + personalTipolectoraBiometria.getIdPerTipolectBio() +
                        ";pINDICE_BIOMETRIA," + personalTipolectoraBiometria.getIndiceBiometria() +
                        ";pEMPRESA," + personalTipolectoraBiometria.getEmpresa() +
                        ";pCODIGO," + personalTipolectoraBiometria.getCodigo() +
                        ";pVALOR_BIOMETRIA," + personalTipolectoraBiometria.getValorBiometria() +
                        ";pIMAGEN_BIOMETRIA," + personalTipolectoraBiometria.getImagenBiometria() +
                        ";pFECHA_BIOMETRIA," + personalTipolectoraBiometria.FechaBiometria +
                        ";pFECHA_HORA_SINC," + fechahora.getFechahoraSync(personalTipolectoraBiometria.getFechaHoraSinc());

                //Log.v(TAG,"parametersnamesvalues syncBiometriasWs:" + parametersnamesvalues);

                namevalue.add("OPCION");
                namevalue.add("GENERAR_DATA3");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                namevalue.add("DATA");
                namevalue.add("{\"session\":" + ConexionWebService.jsonAuthenticated.getString("ID_SESSION") + ",\"llamada\":\"" + ConexionWebService.jsonAuthenticated.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + "SYNC_PERSONAL_TIPOLECTORA_BIOMETRIA_SERVER_TX" + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                //Log.v(TAG,"Llamada: " + idllamada + " - " + parametersnamesvalues);
                //Log.v(TAG,"namesvalues: " + namesvalues.toString());

                respuesta = conexionWebService.llamadasST(namesvalues);
                //jsonArray = new JSONArray(respuesta);


                Log.v(TAG,"respuesta syncBiometriasWs: " + respuesta);

                if(respuesta.contains("{\"MESSAGE\":1}")){
                    queriesPersonalTipolectoraBiometria.ActualizarBiometriaEnviadaServidor(personalTipolectoraBiometria.getIndiceBiometria(),personalTipolectoraBiometria.getIdTipoDetaBio());
                    Log.v(TAG,"EthernetExecuting " + "Se completo la sincronización de biometrias (i)");
                }else if(respuesta.contains("{\"MESSAGE\":\"1\"}")){
                    queriesPersonalTipolectoraBiometria.ActualizarBiometriaEnviadaServidor(personalTipolectoraBiometria.getIndiceBiometria(),personalTipolectoraBiometria.getIdTipoDetaBio());
                    Log.v(TAG,"EthernetExecuting " + "Se completo la sincronización de biometrias (s)");
                }else if(respuesta.contains("{\"MESSAGE\":0}")){
                    Log.v(TAG,"EthernetExecuting " + "No se completo la sincronización de biometrias -> " + respuesta);
                }else{
                    Log.v(TAG,"EthernetExecuting " + "No se completo la sincronización de biometrias");
                }

            }catch (Exception e){
                Log.v(TAG,"syncBiometriasWs " + e.getMessage());
            }
        }
        //return rowaffected;
    }


    public int syncLogTerminal(LogTerminal logTerminal){
        //Este método o función envía los logTerminal por sincronizar al servidor principal
        Fechahora fechahora = new Fechahora();
        int rowaffected = 0;
        ConexionServidor conexionServidor = new ConexionServidor();
        String resultado = "";

        if(connection == null){
            connection = conexionServidor.getInstance().getConnection();
        }

        String sql = "[TEMPUS].[TSP_EJECUTAR_LOTE_DATA2] 'SYNC_LOG_TERMINAL_TX','','',' ','','','LOTE_DATA','1','" +
                "pIDTERMINAL," + logTerminal.getIdterminal() + ";" +
                "pTAG," + logTerminal.getTag() + ";" +
                "pVALUE," + logTerminal.getValue() + ";" +
                "pUSER," + logTerminal.getUser() + ";" +
                "pFECHAHORA," + fechahora.getFechahoraFullSqlServer(logTerminal.getFechahora()) + "'";

        Log.v(TAG,sql);

        try{
            if(connection.isClosed()){
                Log.v(TAG,"Intentando restablecer conexion: " + connection.toString());
                connection = conexionServidor.conectar();
            }
            callableStatement = connection.prepareCall(sql);
            if(callableStatement.execute()){
                resultSet = callableStatement.getResultSet();
            }

            while(resultSet.next()){
                resultado = resultSet.getString("MESSAGE");
            }

            if(resultado.equalsIgnoreCase("1")){
                rowaffected = 1;
                Log.v(TAG,"Registro insertado");
            }else if(resultado.contains("No se puede insertar una clave duplicada en el objeto")){
                rowaffected = 1;
                Log.v(TAG,"Registro ya insertado");
            }

        }catch (SQLException e) {
            Log.v(TAG,"DBManagerServidor.syncLogTerminal SQLException " + e.getMessage());
        }catch (java.sql.SQLException e) {
            Log.v(TAG,"DBManagerServidor.syncLogTerminal sql.SQLException " + e.getMessage());
        }finally {
            try{
                connection.close();
                Log.v(TAG,"Conexion cerrada");
            }catch(Exception e){
                Log.e(TAG,"Error cerrando conexion " + e.getMessage());
            }
        }
        Log.v(TAG,"Cantidad de filas afectadas: " + String.valueOf(rowaffected));
        return rowaffected;
    }

    public void syncLogTerminalWs(LogTerminal logTerminal){
        //Este método o función envía los logTerminal por sincronizar al servidor principal
        queriesLogTerminal = new QueriesLogTerminal();
        ArrayList<ArrayList<String>> namesvalues = new ArrayList<ArrayList<String>>();
        ArrayList<String> namevalue = new ArrayList<String>();
        String respuesta;
        ConexionWebService conexionWebService = new ConexionWebService();
        JSONArray jsonArray;
        JSONObject jsonObject;

        Fechahora fechahora = new Fechahora();
        int rowaffected = 0;
        ConexionServidor conexionServidor = new ConexionServidor();
        String resultado = "";


        Log.v(TAG,"syncLogTerminalWs Verificando conexion");
        if(ConexionWebService.authenticated == true){

            Log.v(TAG,"syncLogTerminalWs Conexion autenticada");
            try{
                String parametersnamesvalues = "pIDTERMINAL," + logTerminal.getIdterminal() +
                        ";pTAG," + logTerminal.getTag() +
                        ";pVALUE," + logTerminal.getValue() +
                        ";pUSER," + logTerminal.getUser() +
                        ";pFECHAHORA," + fechahora.getFechahoraFullSqlServer(logTerminal.getFechahora());

                namevalue.add("OPCION");
                namevalue.add("GENERAR_DATA3");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                namevalue.add("DATA");
                namevalue.add("{\"session\":" + ConexionWebService.jsonAuthenticated.getString("ID_SESSION") + ",\"llamada\":\"" + ConexionWebService.jsonAuthenticated.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + "SYNC_LOG_TERMINAL_TX" + "','','',' ','','','LOTE_DATA','1','" + parametersnamesvalues + "'\"}");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                //Log.v(TAG,"Llamada: " + idllamada + " - " + parametersnamesvalues);
                //Log.v(TAG,"namesvalues: " + namesvalues.toString());

                respuesta = conexionWebService.llamadasST(namesvalues);
                //jsonObject = new JSONObject(respuesta);

                Log.v(TAG, "respuesta syncLogTerminalWs: " + respuesta);
                if (respuesta.contains("[{\"MESSAGE\":1}]")) {
                    Log.v(TAG,"Sincronización de logTerminal OK (i)");
                    queriesLogTerminal.ActualizarSincronizado(logTerminal, 1);
                } else if (respuesta.contains("[{\"MESSAGE\":\"1\"}]")) {
                    Log.v(TAG,"Sincronización de logTerminal OK (s)");
                    queriesLogTerminal.ActualizarSincronizado(logTerminal, 1);
                } else if (respuesta.contains("Infracción de la restricción PRIMARY KEY")) {
                    Log.v(TAG,"Sincronización de logTerminal OK (Infracción de la restricción)");
                    queriesLogTerminal.ActualizarSincronizado(logTerminal, 1);
                }else if (respuesta.contains("ORA-00001")) {
                    Log.v(TAG,"Sincronización de logTerminal OK (ORA-00001)");
                    queriesLogTerminal.ActualizarSincronizado(logTerminal, 1);
                } else if (respuesta.contains("No se puede insertar el valor NULL")) {
                    Log.v(TAG, "No se completo la sincronización de logTerminal -> La columna no admite valores NULL");
                } else {
                    Log.v(TAG, "No se completo la sincronización de logTerminal");
                }

            }catch (Exception e){
                Log.e(TAG,"syncLogTerminalWs " + e.getMessage());
            }
        }

        //return rowaffected;
    }



    public void ProcessLlamadas(Context context){
        //Log.v(TAG, "processarray.length: " + String.valueOf(processarray.length) );
        //queriesLlamadas = new QueriesLlamadas(context);
        try{
            List<Llamadas> llamadasList = getLlamadas(context);
            //Log.v(TAG,"llamadasList.size(): " + String.valueOf(llamadasList.size()));
            if(llamadasList.size() == 0){
                Log.v(TAG," Sin llamadas por ejecutar " + String.valueOf(llamadasList.size()));
            }else{
                for(int i = 0; i < llamadasList.size(); i++){
                    //Log.v(TAG,llamadasList.get(i).toString());
                    ActivityPrincipal.controlFlagSyncAutorizaciones = true;
                    try{
                        if(ActivityPrincipal.controlFlagSyncAutorizaciones){
                            String parametersnamesvalues = prepareParametersLlamadas(llamadasList.get(i).getParameters(), context);
                            Log.v(TAG,"parametersnamesvalues: " + parametersnamesvalues);
                            aavaluesiu = executeLlamadasServer(llamadasList.get(i).getIdllamada(), llamadasList.get(i).getLlamada(), parametersnamesvalues, llamadasList.get(i).getPrimarykey(), llamadasList.get(i).getColumns());
                            insertUpdateData(aavaluesiu,llamadasList.get(i).getTableName(),llamadasList.get(i).getPrimarykey(), llamadasList.get(i).getColumns(),context);
                            ActivityPrincipal.controlFlagSyncAutorizaciones = false;
                        }
                        Thread.sleep(5000);
                    }catch(Exception e){
                        //Log.v(TAG,"ProcessSync.ProcessLlamadas Error: " + e.getMessage());
                        ActivityPrincipal.controlFlagSyncAutorizaciones = false;
                    }
                }
            }

            Thread.sleep(1000);
        }catch(Exception ex){

        }
    }

    public void syncFechahora(){
        //Fechahora del Terminal
        int anoi = 0;
        int mesi = 0;
        int diai = 0;
        int horai = 0;
        int minutoi = 0;
        int segundoi = 0;
        int milisegundoi = 0;

        //Fechahora del Servidor
        int anof = 0;
        int mesf = 0;
        int diaf = 0;
        int horaf = 0;
        int minutof = 0;
        int segundof = 0;
        int milisegundof = 0;

        ConexionServidor conexionServidor = new ConexionServidor();
        if(connection == null){
            connection = conexionServidor.getInstance().getConnection();
        }

        try{
            if(connection.isClosed()){
                //Log.v(TAG,"Intentando restablecer conexion: " + connection.toString());
                connection = conexionServidor.conectar();
            }

            //Log.v(TAG,"Consultando fechahora servidor ");
            preparedStatement = connection.prepareStatement("SELECT GETDATE()");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                /*
                Log.v(TAG,"Fecha Hora Servidor: " + resultSet.getString(1));
                Log.v(TAG,"Año: " + resultSet.getString(1).substring(0,4));
                Log.v(TAG,"Mes: " + resultSet.getString(1).substring(5,7));
                Log.v(TAG,"Día: " + resultSet.getString(1).substring(8,10));
                Log.v(TAG,"Hora: " + resultSet.getString(1).substring(11,13));
                Log.v(TAG,"Minuto: " + resultSet.getString(1).substring(14,16));
                Log.v(TAG,"Segundo: " + resultSet.getString(1).substring(17,19));
                Log.v(TAG,"Milisegundo: " + resultSet.getString(1).substring(20));
                */

                Calendar calendar = Calendar.getInstance();

                anoi = calendar.get(Calendar.YEAR);
                // Se suma 1 al mes debido a que considera los meses del 0 al 11
                mesi = calendar.get(Calendar.MONTH) + 1;
                diai = calendar.get(Calendar.DAY_OF_MONTH);
                horai = calendar.get(Calendar.HOUR_OF_DAY);
                minutoi = calendar.get(Calendar.MINUTE);
                segundoi = calendar.get(Calendar.SECOND);
                milisegundoi = calendar.get(Calendar.MILLISECOND);

                anof = Integer.parseInt(resultSet.getString(1).substring(0,4));
                mesf = Integer.parseInt(resultSet.getString(1).substring(5,7));
                diaf = Integer.parseInt(resultSet.getString(1).substring(8,10));
                horaf = Integer.parseInt(resultSet.getString(1).substring(11,13));
                minutof = Integer.parseInt(resultSet.getString(1).substring(14,16));
                segundof = Integer.parseInt(resultSet.getString(1).substring(17,19));
                milisegundof = Integer.parseInt(resultSet.getString(1).substring(20)) * (int) Math.pow(10,(3 - resultSet.getString(1).substring(20).length()));

                milisegundoi = milisegundoi * (int) Math.pow(10,(3 - String.valueOf(milisegundoi).length()));
                //milisegundof = milisegundof * (int) Math.pow(10,(3 - String.valueOf(milisegundof).length()));


                //Log.v(TAG,"FechaHora Del Terminal: " + anoi + "-" + mesi + "-" + diai + " " + horai + ":" + minutoi + ":" + segundoi + "." + milisegundoi);
                //Log.v(TAG,"FechaHora Del Servidor: " + resultSet.getString(1));// + " - " + milisegundof);

                // Comparar que el año sea el mismo

                if((anoi - anof) == 0){
                    // Comparar que el mes sea el mismo
                    if((mesi - mesf) == 0){
                        // Comparar que el día sea el mismo
                        if((diai - diaf) == 0){
                            // Comparar que la hora sea el mismo
                            if((horai - horaf) == 0){
                                // Comparar que el minuto sea el mismo
                                if((minutoi - minutof) == 0){
                                    // Comparar que el segundo sea el mismo
                                    if((segundoi - segundof) == 0){
                                        // terminal comparacion de milisegundos
                                        if(milisegundoi > milisegundof){
                                            if((milisegundoi - milisegundof) > 250){
                                                //Cambio de Fechahora
                                                Log.v(TAG,"Diferencia de milisegundo (servidor atrasado): " + (milisegundoi - milisegundof));
                                                setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                                            }else{
                                                //Log.v(TAG,"Diferencia de milisegundo (servidor atrasado): " + (milisegundoi - milisegundof));
                                                //Log.v(TAG,"No requiere actualizacion de Fechahora");

                                            }
                                        }else if(milisegundof > milisegundoi){
                                            if((milisegundof - milisegundoi) > 250){
                                                //Cambio de Fechahora
                                                Log.v(TAG,"Diferencia de milisegundo (servidor adelantado):" + (milisegundoi - milisegundof));
                                                setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                                            }else{
                                                //Log.v(TAG,"Diferencia de milisegundo (servidor adelantado):" + (milisegundoi - milisegundof));
                                                //Log.v(TAG,"No requiere actualizacion de Fechahora");
                                            }
                                        }else{
                                            //Log.v(TAG,"No requiere actualizacion de Fechahora");
                                        }
                                    }else{
                                        Log.v(TAG,"Diferencia de Segundo: " + (segundoi - segundof));
                                        setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                                    }
                                }else{
                                    Log.v(TAG,"Diferencia de Minuto: " + (minutoi - minutof));
                                    setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                                }
                            }else{
                                Log.v(TAG,"Diferencia de Hora: " + (horai - horaf));
                                setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                            }
                        }else{
                            Log.v(TAG,"Diferencia de Dia: " + (diai - diaf));
                            setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                        }
                    }else{
                        Log.v(TAG,"Diferencia de Mes: " + (mesi - mesf));
                        setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                    }
                }else{
                    Log.v(TAG,"Diferencia de Año: " + (anoi - anof));
                    setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                }
                //Log.v(TAG,"--------------------------------------------------------------");

            }

        }catch(Exception e){
            Log.e(TAG,"syncFechahora() " + e.getMessage());
        }

    }

    public void syncFechahora(String fechahoraServer){

        //Fechahora del Terminal
        int anoi = 0;
        int mesi = 0;
        int diai = 0;
        int horai = 0;
        int minutoi = 0;
        int segundoi = 0;
        int milisegundoi = 0;

        //Fechahora del Servidor
        int anof = 0;
        int mesf = 0;
        int diaf = 0;
        int horaf = 0;
        int minutof = 0;
        int segundof = 0;
        int milisegundof = 0;

        try{

            /*
            Log.v(TAG,"Fecha Hora Servidor: " + fechahoraServer);
            Log.v(TAG,"Año: " + fechahoraServer.substring(0,4));
            Log.v(TAG,"Mes: " + fechahoraServer.substring(5,7));
            Log.v(TAG,"Día: " + fechahoraServer.substring(8,10));
            Log.v(TAG,"Hora: " + fechahoraServer.substring(11,13));
            Log.v(TAG,"Minuto: " + fechahoraServer.substring(14,16));
            Log.v(TAG,"Segundo: " + fechahoraServer.substring(17,19));
            Log.v(TAG,"Milisegundo: " + fechahoraServer.substring(20));
            */

            Calendar calendar = Calendar.getInstance();

            anoi = calendar.get(Calendar.YEAR);
            // Se suma 1 al mes debido a que considera los meses del 0 al 11
            mesi = calendar.get(Calendar.MONTH) + 1;
            diai = calendar.get(Calendar.DAY_OF_MONTH);
            horai = calendar.get(Calendar.HOUR_OF_DAY);
            minutoi = calendar.get(Calendar.MINUTE);
            segundoi = calendar.get(Calendar.SECOND);
            milisegundoi = calendar.get(Calendar.MILLISECOND);

            anof = Integer.parseInt(fechahoraServer.substring(0,4));
            mesf = Integer.parseInt(fechahoraServer.substring(5,7));
            diaf = Integer.parseInt(fechahoraServer.substring(8,10));
            horaf = Integer.parseInt(fechahoraServer.substring(11,13));
            minutof = Integer.parseInt(fechahoraServer.substring(14,16));
            segundof = Integer.parseInt(fechahoraServer.substring(17,19));
            milisegundof = Integer.parseInt(fechahoraServer.substring(20)) * (int) Math.pow(10,(3 - fechahoraServer.substring(20).length()));

            milisegundoi = milisegundoi * (int) Math.pow(10,(3 - String.valueOf(milisegundoi).length()));
            //milisegundof = milisegundof * (int) Math.pow(10,(3 - String.valueOf(milisegundof).length()));

            //Log.v(TAG,"FechaHora Del Terminal: " + anoi + "-" + mesi + "-" + diai + " " + horai + ":" + minutoi + ":" + segundoi + "." + milisegundoi);
            //Log.v(TAG,"FechaHora Del Servidor: " + fechahoraServer);// + " - " + milisegundof);

            // Comparar que el año sea el mismo

            if((anoi - anof) == 0){
                // Comparar que el mes sea el mismo
                if((mesi - mesf) == 0){
                    // Comparar que el día sea el mismo
                    if((diai - diaf) == 0){
                        // Comparar que la hora sea el mismo
                        if((horai - horaf) == 0){
                            // Comparar que el minuto sea el mismo
                            if((minutoi - minutof) == 0){
                                // Comparar que el segundo sea el mismo
                                if((segundoi - segundof) == 0){
                                    // terminal comparacion de milisegundos
                                    if(milisegundoi > milisegundof){
                                        if((milisegundoi - milisegundof) > 250){
                                            //Cambio de Fechahora
                                            Log.v(TAG,"BTS-MAET Diferencia de milisegundo (servidor atrasado): " + (milisegundoi - milisegundof));
                                            setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                                        }else{
                                            //Log.v(TAG,"Diferencia de milisegundo (servidor atrasado): " + (milisegundoi - milisegundof));
                                            //Log.v(TAG,"No requiere actualizacion de Fechahora");

                                        }
                                    }else if(milisegundof > milisegundoi){
                                        if((milisegundof - milisegundoi) > 250){
                                            //Cambio de Fechahora
                                            Log.v(TAG,"BTS-MAET Diferencia de milisegundo (servidor adelantado):" + (milisegundoi - milisegundof));
                                            setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                                        }else{
                                            //Log.v(TAG,"Diferencia de milisegundo (servidor adelantado):" + (milisegundoi - milisegundof));
                                            //Log.v(TAG,"No requiere actualizacion de Fechahora");
                                        }
                                    }else{
                                        //Log.v(TAG,"No requiere actualizacion de Fechahora");
                                    }
                                }else{
                                    Log.v(TAG,"BTS-MAET Diferencia de Segundo: " + (segundoi - segundof));
                                    setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                                }
                            }else{
                                Log.v(TAG,"BTS-MAET Diferencia de Minuto: " + (minutoi - minutof));
                                setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                            }
                        }else{
                            Log.v(TAG,"BTS-MAET Diferencia de Hora: " + (horai - horaf));
                            setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                        }
                    }else{
                        Log.v(TAG,"BTS-MAET Diferencia de Dia: " + (diai - diaf));
                        setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                    }
                }else{
                    Log.v(TAG,"BTS-MAET Diferencia de Mes: " + (mesi - mesf));
                    setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
                }
            }else{
                Log.v(TAG,"BTS-MAET Diferencia de Año: " + (anoi - anof));
                setFechahoraTerminal(anof,mesf,diaf,horaf,minutof,segundof,milisegundof);
            }
            //Log.v(TAG,"--------------------------------------------------------------");

        }catch(Exception e){
            Log.v(TAG,"BTS-MAET ProcessSync.syncFechahora Error: " + e.getMessage());
        }

    }

    public void syncFechahoraWs(){

        //Este método o función envía los logTerminal por sincronizar al servidor principal
        queriesLogTerminal = new QueriesLogTerminal();
        ArrayList<ArrayList<String>> namesvalues = new ArrayList<ArrayList<String>>();
        ArrayList<String> namevalue = new ArrayList<String>();
        String respuesta;
        ConexionWebService conexionWebService = new ConexionWebService();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        Fechahora fechahora = new Fechahora();
        int rowaffected = 0;
        ConexionServidor conexionServidor = new ConexionServidor();
        String resultado = "";


        Log.v(TAG,"syncFechahoraWs Verificando conexion");
        if(ConexionWebService.authenticated == true){

            Log.v(TAG,"syncFechahoraWs Conexion autenticada");
            try{
                namevalue.add("OPCION");
                namevalue.add("GENERAR_DATA3");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                namevalue.add("DATA");
                namevalue.add("{\"session\":" + ConexionWebService.jsonAuthenticated.getString("ID_SESSION") + ",\"llamada\":\"" + ConexionWebService.jsonAuthenticated.getString("ID_CONEXION") + "_" + "EXEC_L2" + "\",\"parametros\":\"'" + "SYNC_FECHAHORA_TX" + "','','',' ','','','LOTE_DATA','1','" + "" + "'\"}");

                namesvalues.add(namevalue);
                namevalue = new ArrayList<String>();

                //Log.v(TAG,"Llamada: " + idllamada + " - " + parametersnamesvalues);
                //Log.v(TAG,"namesvalues: " + namesvalues.toString());

                respuesta = conexionWebService.llamadasST(namesvalues);
                try{
                    jsonObject = new JSONObject(respuesta);
                }catch (Exception e){
                    try{
                        jsonArray = new JSONArray(respuesta);
                    }catch (Exception ex){
                    }
                }


                Log.v(TAG, "respuesta syncFechahoraWs: " + respuesta);
                if(jsonObject.toString().contains("FECHAHORA")){
                    Log.v(TAG,"Procesando actualización de FECHAHORA Object " + jsonObject.getString("FECHAHORA"));
                    syncFechahora(jsonObject.getString("FECHAHORA")); //{"FECHAHORA":"2017-08-21 14:34:30.210"}
                }else if(jsonArray.toString().contains("FECHAHORA")){
                    Log.v(TAG,"Procesando actualización de FECHAHORA Array " + jsonArray.getJSONObject(0).getString("FECHAHORA"));
                    syncFechahora(jsonArray.getJSONObject(0).getString("FECHAHORA")); //[{"FECHAHORA":"2017-08-21 14:34:30.210"}]
                }else{
                    Log.v(TAG,"No se detectó la secuencia FECHAHORA");
                }

            }catch (Exception e){
                Log.e(TAG,"syncLogTerminalWs " + e.getMessage());
            }
        }

        //return rowaffected;

    }


    public void setFechahoraTerminal(int ano, int mes, int dia, int hora, int minuto, int segundo, int milisegundo){

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR,ano);
        // Se resta 1 al mes debido a que considera los meses del 0 al 11
        calendar.set(Calendar.MONTH,mes-1);
        calendar.set(Calendar.DAY_OF_MONTH,dia);
        calendar.set(Calendar.HOUR_OF_DAY,hora);
        calendar.set(Calendar.MINUTE,minuto);
        calendar.set(Calendar.SECOND,segundo);
        calendar.set(Calendar.MILLISECOND,milisegundo);

        AlarmManager alarmManager = (AlarmManager) ActivityPrincipal.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setTime(calendar.getTimeInMillis());

        Log.v(TAG,"Fechahora del Terminal Actualizada: " + dia + "/" + mes + "/" + ano + " " + hora + ":" + minuto + ":" + segundo + "." + milisegundo);

    }


    public void syncSuprema(Context context){
        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(context);

        if (ActivityPrincipal.ctrlThreadReplicadoEnabled){
            if(ActivityPrincipal.isReplicating){
                try{
                    Log.v(TAG,"REPLICADO INICIADO");
                    ActivityPrincipal.objSuprema.limpiarTramaSuprema();
                    queriesPersonalTipolectoraBiometria.ReplicarBiometria();
                }catch(Exception e){
                    //Log.v(TAG,"REPLICADO A FALSO");
                    ActivityPrincipal.isReplicating = false;
                }

            }else{
                Log.v(TAG,"Sin horarios de Replicado");

                if(queriesPersonalTipolectoraBiometria.iniciarReplicado() == 1){
                    Log.v(TAG,"Horario de Replica, habilitando Replicado");
                    ActivityPrincipal.isReplicating = true;
                    /*
                    try{
                        ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"DeleteAllTemplates",null);
                        Thread.sleep(1000);
                    }catch(Exception e){
                        Log.v(TAG,"Error Enrolamiento: " + e.getMessage());
                    }
                    ActivityPrincipal.objSuprema.limpiarTramaSuprema();

                    DBManager db = new DBManager(context);
                    db.execSQL("UPDATE PERSONAL_TIPOLECTORA_BIOMETRIA SET SINCRONIZADO = 0 WHERE 1 = 1;");

                    */
                }
            }
        }

    }


    public int CountResultSet(ResultSet rs){
        int resultSetCount = 0;
        try{
            while(rs.next()){
                resultSetCount++;
                Log.v(TAG,"p: " + rs.getRow());
            }
            rs.beforeFirst();
        }catch(Exception e){

        }
        return  resultSetCount;
    }





    public void callWebService(){


        try {


            String url = HttpUrl.parse("http://192.168.0.42:80/Web_ServiceTempus/COntrolador/Direct_WS.php").toString();

            RequestBody formBody = new FormBody.Builder()
                    .add("O","T")
                    .add("EMPRESA","TEMPUS_WS_T10")
                    .add("USER","TEMPUS")
                    .add("PASS","TEMPUSSCA")
                    .add("IP","0.0.0.0")
                    .add("MAC","00-00-00-00-00-00")
                    .add("HOSTNAME","TERMINAL")
                    .build();

            Log.v(TAG,"formBody>>>" + formBody.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Log.v(TAG,"request>>>" + request.toString());

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Response response = client.newCall(request).execute();

            String respuesta = response.body().string();
            Log.v(TAG,"response>>>" + respuesta);


            Log.v(TAG,"callWebService OK");
        } catch (Exception e) {
            Log.e(TAG,"callWebService " + e.getMessage());
        }




        /*
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("O","T");
            map.put("EMPRESA","TEMPUS_WS_T10");
            map.put("USER","TEMPUS");
            map.put("PASS","TEMPUSSCA");
            map.put("IP","0.0.0.0");
            map.put("MAC","00-00-00-00-00-00");
            map.put("HOSTNAME","TERMINAL");

            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, Object> param : map.entrySet()) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append('&');
                }
                stringBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                stringBuilder.append('=');
                stringBuilder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            Log.v(TAG,"Parametros ok");

            byte[] postDataBytes = stringBuilder.toString().getBytes("UTF-8");

            Log.v(TAG,"Parametros ok postDataBytes");

            URL url = new URL("http://192.168.0.42:80/Web_ServiceTempus/COntrolador/Direct_WS.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            Log.v(TAG,"Parametros ok httpURLConnection 1");

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.getOutputStream().write(postDataBytes);

            Log.v(TAG,"Parametros ok postDataBytes httpURLConnection 2");



            Reader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            String respuesta = "";
            for (int c; (c = in.read()) >= 0; respuesta = respuesta + (char) c){

            }
            Log.v(TAG,"respuesta=" + respuesta);

        }catch(Exception e){
            Log.e(TAG,"callWebService error: " + e.getMessage());
        */


    }

    public void ProcessLlamadasWs(Context context){
        //Log.v(TAG, "processarray.length: " + String.valueOf(processarray.length) );
        //queriesLlamadas = new QueriesLlamadas(context);


        // REVISAR TRATAMIENTO DE NULL EN JSON a BD

        try{
            List<Llamadas> llamadasList = getLlamadas(context);
            Log.v(TAG,"llamadasList.size(): " + String.valueOf(llamadasList.size() + " " + llamadasList.toString()));
            if(llamadasList.size() == 0){
                Log.v(TAG," Sin llamadas por ejecutar " + String.valueOf(llamadasList.size()));
            }else{

                //Sincronizar Fechahora
                syncFechahoraWs();

                for(int i = 0; i < llamadasList.size(); i++){
                    Log.v(TAG,llamadasList.get(i).toString());
                    ActivityPrincipal.controlFlagSyncAutorizaciones = true;
                    try{
                        if(ActivityPrincipal.controlFlagSyncAutorizaciones){
                            String parametersnamesvalues = prepareParametersLlamadas(llamadasList.get(i).getParameters(), context);
                            Log.v(TAG,"parametersnamesvalues: " + parametersnamesvalues);
                            aavaluesiu = executeLlamadasWs(llamadasList.get(i).getIdllamada(), llamadasList.get(i).getLlamada(), parametersnamesvalues, llamadasList.get(i).getPrimarykey(), llamadasList.get(i).getColumns());
                            insertUpdateData(aavaluesiu,llamadasList.get(i).getTableName(),llamadasList.get(i).getPrimarykey(), llamadasList.get(i).getColumns(),context);
                            ActivityPrincipal.controlFlagSyncAutorizaciones = false;
                        }
                        Thread.sleep(5000);
                    }catch(Exception e){
                        //Log.v(TAG,"ProcessSync.ProcessLlamadas Error: " + e.getMessage());
                        ActivityPrincipal.controlFlagSyncAutorizaciones = false;
                    }
                }
            }

            Thread.sleep(1000);
        }catch(Exception e){

        }
    }

    public void callWSGoogle1(){
        //QueriesMarcaciones queriesMarcaciones = new QueriesMarcaciones(ActivityPrincipal.context);
        //List<Marcaciones> marcacionesList = queriesMarcaciones.select_one_row();

        //if(marcacionesList.size() > 0){
            try{
                Log.v(TAG,"callWSGoogle iniciar");
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("name","TERMINAL");
                map.put("country","Peru");
                map.put("id","1ExnrFxJRovzdGNfrkO5BSG3ef_z0omCF_1UIQ27CQ4g");
                //map.put("USER","TEMPUS");
                //map.put("PASS","TEMPUSSCA");
                //map.put("IP","0.0.0.0");
                //map.put("MAC","00-00-00-00-00-00");
                //map.put("HOSTNAME","TERMINAL");

                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry<String, Object> param : map.entrySet()) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append('&');
                    }
                    stringBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    stringBuilder.append('=');
                    stringBuilder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                Log.v(TAG,"stringBuilder>" + stringBuilder);
                byte[] postDataBytes = stringBuilder.toString().getBytes("UTF-8");

                Log.v(TAG,"callWSGoogle conectando");
                URL url = new URL("https://script.google.com/macros/u/0/s/AKfycbzQQcRUoudjcNTDRQJ98fovIBfXaW1wq0nmwPkJXBJ_6LKNkmqG/exec");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                Log.v(TAG,"callWSGoogle conexion ok " + httpURLConnection.toString());

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoOutput(true);

                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                httpURLConnection.getOutputStream().write(postDataBytes);
                Log.v(TAG,"callWSGoogle finalizar");

                /*
                Reader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                String respuesta = "";
                for (int c; (c = in.read()) >= 0; respuesta = respuesta + (char) c){
                    Log.v(TAG,"Sin horarios de Replicado");
                }
                */

            }catch(Exception e){
                Log.e(TAG,"callWSGoogle " + e.getMessage());
            }
        //}

    }






















}

