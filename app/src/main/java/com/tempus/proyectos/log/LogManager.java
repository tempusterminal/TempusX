package com.tempus.proyectos.log;

import android.os.Environment;
import android.util.Log;

import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.InternalFile;


public class LogManager {

    public static final String DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/tempus/";
    public static final String FILE = "log.txt";
    InternalFile internalFile;
    Fechahora fechahora;

    public LogManager() {
        internalFile = new InternalFile();
        fechahora = new Fechahora();
    }

    public void RegisterLog(
            String log,
            String tipo,
            String fvalue,
            String svalue,
            String user,
            String query) {

        String logString = "" +
                "LOG: " + log + "|" +
                "TIPO: " + tipo + "|" +
                "PRIMER_VALOR: " + fvalue + "|" +
                "SEGUNDO_VALOR: " + svalue + "|" +
                "USUARIO: " + user + "|" +
                "QUERY: " + query;

        Log.v("LOG_MANAGER", logString);

        internalFile.write(logString,FILE);
    }

    public void RegisterLogTXT(String cadena){
        String fh = fechahora.getFechahora();
        //internalFile.validarDirectorio(DIRECTORY);
        //internalFile.validarArchivo(DIRECTORY + FILE);
        Log.d("DIRECTORIO XD", DIRECTORY);
        internalFile.writeToAppend(fh + ":" + cadena, DIRECTORY + FILE);
    }

}
