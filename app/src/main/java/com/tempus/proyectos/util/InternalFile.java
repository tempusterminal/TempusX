package com.tempus.proyectos.util;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
}
