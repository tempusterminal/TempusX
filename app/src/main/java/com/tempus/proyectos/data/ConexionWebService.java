package com.tempus.proyectos.data;

import android.os.StrictMode;
import android.util.Log;

import com.tempus.proyectos.tempusx.ActivityPrincipal;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gurrutiam on 14/03/2018.
 */

public class ConexionWebService {

    private String TAG = "DA-CWS";

    private static String host = "";
    private static String httpdUrl = "";
    private static String company = "";
    private static String port = "";
    private static String user = "";
    private static String pass = "";

    private static String url = "";

    public static boolean authenticated = false;
    public static JSONObject jsonAuthenticated;

    public static String logWsnLevel01 = "";
    public static String logWsnLevel02 = "";
    public static String logWsnLevel03 = "";
    public static String logWsnLevel04 = "";

    public ConexionWebService() {
        host = ActivityPrincipal.parametersWsn.split(",")[0];
        //host = "192.168.0.42";
        httpdUrl = ActivityPrincipal.parametersWsn.split(",")[1];
        company = ActivityPrincipal.parametersWsn.split(",")[2];
        port = ActivityPrincipal.parametersWsn.split(",")[3];
        user = ActivityPrincipal.parametersWsn.split(",")[4];
        pass = ActivityPrincipal.parametersWsn.split(",")[5];

        Log.v(TAG,">>>" + host + ">" + httpdUrl + ">" + company + ">" + port + ">" + user + ">" + pass);

        url = HttpUrl.parse("http://" + host + ":" + port + "" + httpdUrl).toString();

        Log.v(TAG,"url>" + url);
    }

    public void updateUrl(){
        host = ActivityPrincipal.parametersWsn.split(",")[0];
        httpdUrl = ActivityPrincipal.parametersWsn.split(",")[1];
        company = ActivityPrincipal.parametersWsn.split(",")[2];
        port = ActivityPrincipal.parametersWsn.split(",")[3];
        user = ActivityPrincipal.parametersWsn.split(",")[4];
        pass = ActivityPrincipal.parametersWsn.split(",")[5];

        url = HttpUrl.parse("http://" + host + ":" + port + "" + httpdUrl).toString();
    }

    public boolean autenticar(){

        RequestBody formBody = new FormBody.Builder()
                .add("O","T")
                .add("EMPRESA",company)
                .add("USER",user)
                .add("PASS",pass)
                .add("IP","0.0.0.0")
                .add("MAC","00-00-00-00-00-00")
                .add("HOSTNAME","TERMINAL")
                .build();

        try{
            logWsnLevel01 = url + ">" + company + ">" + user + ">" + pass;
            logWsnLevel02 = "";
        }catch (Exception e){
        }

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            Response response = client.newCall(request).execute();
            String respuesta = response.body().string();
            Log.v(TAG,"response>>>" + respuesta);
            JSONObject jsonObject = new JSONObject(respuesta);
            logWsnLevel02 = respuesta;
            if(jsonObject.getString("ESTADO").equalsIgnoreCase("true")){
                jsonAuthenticated = new JSONObject(jsonObject.getString("DATA"));
                Log.v(TAG,"jsonAuthenticate " + jsonAuthenticated.toString());
                authenticated = true;
            }else{
                authenticated = false;
            }
        }catch (Exception e){
            if(e.getMessage().contains("ailed to connect to")){
                logWsnLevel02 = "Verificar parámetros WS Nativo y/o conexión a Red (LAN/Internet/Red móvil/SimCard/Datos para navegar) " + e.getMessage();
            }else{
                logWsnLevel02 = e.getMessage();
            }
            Log.e(TAG,"autenticar " + e.getMessage());
        }

        return authenticated;
    }


    public String llamadasST(ArrayList<ArrayList<String>> namesvalues){

        Log.v(TAG,"namesvalues " + namesvalues.toString());
        FormBody.Builder builder;
        RequestBody formBody;
        Request request;
        OkHttpClient client;
        Response response;
        String respuesta = "";

        builder = new FormBody.Builder();
        for(int i = 0; i < namesvalues.size(); i++){
            builder.add(namesvalues.get(i).get(0),namesvalues.get(i).get(1));
        }
        formBody = builder.build();
        try{
            logWsnLevel01 = namesvalues.get(1).get(1);
            logWsnLevel02 = "";
        }catch (Exception e){

        }

        request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client = new OkHttpClient().newBuilder()
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            response = client.newCall(request).execute();
            respuesta = response.body().string();
            if(respuesta.equalsIgnoreCase("[]")){
                logWsnLevel02 = "Sin registros por sincronizar en esta petición " + respuesta;
            }else if(respuesta.length() > 100){
                logWsnLevel02 = "(" + respuesta.length() + ") ... " + respuesta.substring(0,100);
            }else{
                logWsnLevel02 = respuesta;
            }
        }catch (Exception e){
            if(e.getMessage().contains("ailed to connect to")){
                logWsnLevel02 = "Verificar parámetros WS Nativo y/o conexión a Red (LAN/Internet/Red móvil/SimCard/Datos para navegar) " + e.getMessage();
            }else if(e.getMessage().equalsIgnoreCase("timeout")){
                logWsnLevel02 = "Tiempo de espera agotado en la petición a WS Nativo " + e.getMessage();
            }else if(e.getMessage().equalsIgnoreCase("null")){
                logWsnLevel02 = "Respuesta nula " + e.getMessage();
            }else{
                logWsnLevel02 = e.getMessage();
            }

            Log.e(TAG,"llamadasST " + e.getMessage());
        }

        Log.v(TAG,"response>>>" + respuesta);
        return respuesta;
    }









}
