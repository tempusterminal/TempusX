package com.tempus.proyectos.util;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ecernar on 02/01/2017.
 */

public class InternalFile{

    static final int READ_BLOCK_SIZE = 100;

    public void write(String text, String filename) {
        try {
            FileOutputStream fileout = new FileOutputStream(new File(filename));
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(text);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read(String filename) {
        String resultado = "";
        try {
            FileInputStream fileIn = new FileInputStream (new File(filename));
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();

            resultado = s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public void writeJsonArray(JSONArray json, String filename) {
        write(json.toString(),filename);
    }

    public JSONArray readJsonArray(String filename) {
        JSONArray json = null;
        String resultado = read(filename);
        try {
            JSONArray jarray = new JSONArray(resultado);
            json = jarray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void validarDirectorio(String archivo) {
        File f = new File(archivo);

        if (f.exists() && f.isDirectory()) {
            Log.d("validarDirectorio_PATH", "EXISTE");

        } else {
            Log.d("validarDirectorio_PATH", "NO EXISTE, CREANDO DIRECTORIO ... ");
            try {
                f.mkdir();
                Log.d("validarDirectorio_PATH", "DIRECTORIO CREADO");
            } catch (Exception e) {
                Log.e("validarDirectorio_PATH", "Error " + e.getMessage());
            }
        }

    }

    public void validarArchivo(String archivo) {
        File f = new File(archivo);

        if (f.exists()) {
            Log.d("validarArchivo_PATH", "EXISTE");

        } else {
            Log.d("validarArchivo_PATH", "NO EXISTE, CREANDO ARCHIVO ... ");
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                Log.d("validarArchivo_PATH", "ARCHIVO CREADO");
            } catch (Exception e) {
                Log.e("validarArchivo_PATH", "Error " + e.getMessage());
            }
        }

    }


    public void writeToFileAppend(String directory, String filename, String data ){

        Log.d("FILE_TX", "Inicio writeToFileAppend");

        File out;
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;

        out = new File(new File(directory), filename);

        if (out.exists()==false) {

            Log.d("FILE_TX", "out.exists() == false");

            try {
                out.createNewFile();
            } catch (IOException e) {
                Log.d("FILE_TX", e.getMessage());
            }

        }

        try {
            outStream = new FileOutputStream(out, false);
            Log.d("FILE_TX", "outStream = new FileOutputStream(out, false)");
        } catch (FileNotFoundException e) {
            Log.d("FILE_TX", e.getMessage());
        }
        outStreamWriter = new OutputStreamWriter(outStream);

        try {
            outStreamWriter.append(data);
            outStreamWriter.flush();
            Log.d("FILE_TX", "flush");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FILE_TX", e.getMessage());
        }

    }

    public void writeToAppend(String str, String file) {
        BufferedWriter bw = null;

        try {
            // APPEND MODE SET HERE
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(str);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {                       // always close the file
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {
                // just ignore it
            }
        } // end try/catch/finally
    }
}
