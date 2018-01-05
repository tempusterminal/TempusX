package com.tempus.proyectos.data.queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.Conexion;
import com.tempus.proyectos.data.model.Personal;
import com.tempus.proyectos.data.tables.TablePersonal;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

/**
 * Created by gurrutiam on 10/11/2016.
 */

public class QueriesPersonal {

    private String TAG = "DQ-QUEPER";

    private Conexion conexion;
    private Context context;
    private SQLiteDatabase database;

    private File fileFotoPersonal = new File(Environment.getExternalStoragePublicDirectory("") + "/tempus/img/personal/sync/original/");

    public QueriesPersonal(Context context) {
        this.context = context;
    }

    public QueriesPersonal open() throws SQLException {
        conexion = new Conexion(context);
        database = conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        database.close();
        conexion.close();
    }

    public void insert(Personal personal){

        conexion.onCreate(database);

        ContentValues contentValues = new ContentValues();

        contentValues.put(TablePersonal.Empresa, personal.Empresa);
        contentValues.put(TablePersonal.Codigo, personal.Codigo);
        contentValues.put(TablePersonal.CentroDeCosto, personal.CentroDeCosto);
        contentValues.put(TablePersonal.ApellidoPaterno, personal.ApellidoPaterno);
        contentValues.put(TablePersonal.ApellidoMaterno, personal.ApellidoMaterno);
        contentValues.put(TablePersonal.Nombres, personal.Nombres);
        contentValues.put(TablePersonal.FechaDeNacimiento, personal.FechaDeNacimiento);
        contentValues.put(TablePersonal.FechaDeIngreso, personal.FechaDeIngreso);
        contentValues.put(TablePersonal.FechaDeCese, personal.FechaDeCese);
        contentValues.put(TablePersonal.Estado, personal.Estado);
        contentValues.put(TablePersonal.TipoHorario, personal.TipoHorario);
        contentValues.put(TablePersonal.Icono, personal.Icono);
        contentValues.put(TablePersonal.NroDocumento, personal.NroDocumento);
        contentValues.put(TablePersonal.FechaHoraSinc, personal.FechaHoraSinc);

        database.insert(TablePersonal.TABLE_NAME, null, contentValues);

    }

    public List<Personal> select(){

        Personal personal = new Personal();
        List<Personal> personalList =  new ArrayList<Personal>();


        Cursor cursor = database.rawQuery(TablePersonal.SELECT_TABLE, null);
        if(cursor.moveToNext()){
            do{
                personal = new Personal();
                personal.setEmpresa(cursor.getString(cursor.getColumnIndex(TablePersonal.Empresa)));
                personal.setCodigo(cursor.getString(cursor.getColumnIndex(TablePersonal.Codigo)));
                personal.setCentroDeCosto(cursor.getString(cursor.getColumnIndex(TablePersonal.CentroDeCosto)));
                personal.setApellidoPaterno(cursor.getString(cursor.getColumnIndex(TablePersonal.ApellidoPaterno)));
                personal.setApellidoMaterno(cursor.getString(cursor.getColumnIndex(TablePersonal.ApellidoMaterno)));
                personal.setNombres(cursor.getString(cursor.getColumnIndex(TablePersonal.Nombres)));
                personal.setFechaDeNacimiento(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeNacimiento)));
                personal.setFechaDeIngreso(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeIngreso)));
                personal.setFechaDeCese(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeCese)));
                personal.setEstado(cursor.getString(cursor.getColumnIndex(TablePersonal.Estado)));
                personal.setTipoHorario(cursor.getString(cursor.getColumnIndex(TablePersonal.TipoHorario)));
                personal.setIcono(cursor.getString(cursor.getColumnIndex(TablePersonal.Icono)));
                personal.setNroDocumento(cursor.getString(cursor.getColumnIndex(TablePersonal.NroDocumento)));
                personal.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaHoraSinc)));
                personalList.add(personal);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return personalList;
    }

    public ArrayList<Personal> selectPersonalByCodNroDoc(String codbrodoc){

        Personal personal = new Personal();
        ArrayList<Personal> personalList =  new ArrayList<Personal>();

        this.open();
        Cursor cursor = database.rawQuery(TablePersonal.SELECT_TABLE + " WHERE CODIGO = '" + codbrodoc + "' OR NRO_DOCUMENTO = '" + codbrodoc + "'", null);
        if(cursor.moveToNext()){
            do{
                personal = new Personal();
                personal.setEmpresa(cursor.getString(cursor.getColumnIndex(TablePersonal.Empresa)));
                personal.setCodigo(cursor.getString(cursor.getColumnIndex(TablePersonal.Codigo)));
                personal.setCentroDeCosto(cursor.getString(cursor.getColumnIndex(TablePersonal.CentroDeCosto)));
                personal.setApellidoPaterno(cursor.getString(cursor.getColumnIndex(TablePersonal.ApellidoPaterno)));
                personal.setApellidoMaterno(cursor.getString(cursor.getColumnIndex(TablePersonal.ApellidoMaterno)));
                personal.setNombres(cursor.getString(cursor.getColumnIndex(TablePersonal.Nombres)));
                personal.setFechaDeNacimiento(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeNacimiento)));
                personal.setFechaDeIngreso(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeIngreso)));
                personal.setFechaDeCese(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaDeCese)));
                personal.setEstado(cursor.getString(cursor.getColumnIndex(TablePersonal.Estado)));
                personal.setTipoHorario(cursor.getString(cursor.getColumnIndex(TablePersonal.TipoHorario)));
                personal.setIcono(cursor.getString(cursor.getColumnIndex(TablePersonal.Icono)));
                personal.setNroDocumento(cursor.getString(cursor.getColumnIndex(TablePersonal.NroDocumento)));
                personal.setFechaHoraSinc(cursor.getString(cursor.getColumnIndex(TablePersonal.FechaHoraSinc)));
                personalList.add(personal);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return personalList;
    }

    public int count(){

        int count = 0;
        String query = "SELECT " +
                "COUNT(*)" +
                "FROM " + TablePersonal.TABLE_NAME;

        Log.d("Autorizaciones", query);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }while(cursor.moveToNext());

        cursor.close();

        return count;

    }


    public ArrayList<String> select_icono_personal(){
        ArrayList<String> allfotopersonaldb =  new ArrayList<String>();

        this.open();
        Cursor cursor = database.rawQuery(TablePersonal.SELECT_TABLE + " WHERE ICONO LIKE '%.%' ", null);
        if(cursor.moveToNext()){
            do{
                allfotopersonaldb.add(cursor.getString(cursor.getColumnIndex(TablePersonal.Icono)));
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return allfotopersonaldb;
    }



    public ArrayList<String> getFotoPersonalBySync(){
        ArrayList<String> allfotopersonaldb =  new ArrayList<String>();
        ArrayList<String> fotopersonalbysync =  new ArrayList<String>();
        boolean add;
        try{
            allfotopersonaldb = select_icono_personal();
            Log.v(TAG,"allfotopersonaldb " + allfotopersonaldb.toString());

            //Listar todos los archivos del directorio fotopersonal
            String[] allfotopersonaldir;
            allfotopersonaldir = fileFotoPersonal.list(new FilenameFilter(){
                @Override
                public boolean accept(File directory, String fileName) {
                    if (!fileName.equalsIgnoreCase("Thumbs.db")) {
                        return true;
                    }
                    return false;
                }
            });

            Log.v(TAG,"allfotopersonaldir " + allfotopersonaldir.length);

            for(int i = 0; i < allfotopersonaldir.length; i++){
                Log.v(TAG,"allfotopersonaldir[" + i + "]=" + allfotopersonaldir[i]);
            }

            //Comparar campo ICONO de PERSONAL en base de datos con los archivos en el directorio fotopersonal
            //Todos los archivos que esten en BD pero no es directorio se agregaran a la lista por sincronizar
            for(int i = 0; i < allfotopersonaldb.size(); i++){
                add = false;
                for(int y = 0; y < allfotopersonaldir.length; y++){
                    if(allfotopersonaldb.get(i).equalsIgnoreCase(allfotopersonaldir[y])){
                        add = true;
                    }
                }
                if(add){
                    Log.v(TAG,"Ya existe el archivo " + allfotopersonaldb.get(i).toString());
                }else{
                    Log.v(TAG,"Agregando a la lista por sincronizar " + allfotopersonaldb.get(i).toString());
                    fotopersonalbysync.add(allfotopersonaldb.get(i));
                }
            }

            Log.v(TAG,"fotopersonalbysync " + fotopersonalbysync.toString());

        }catch (Exception e){
            Log.e(TAG,"getFotoPersonalBySync " + e.getMessage());
        }

        return fotopersonalbysync;

    }

    public int setImageFotoPersonal(String imageString, String filename){

        try{
            byte[] pdfAsBytes = Base64.decode(imageString, 0);

            File filePath = new File(Environment.getExternalStoragePublicDirectory("") + "/tempus/img/personal/sync/original/" + filename);
            FileOutputStream os = new FileOutputStream(filePath, true);
            os.write(pdfAsBytes);
            os.flush();
            os.close();

            return 1;

        }catch (Exception e){
            Log.e(TAG,"setImageFotoPersonal " + e.getMessage());
            return 0;
        }

    }


    public long setFotoPersonalToNull(String fotopersonal){

        this.open();
        long rowaffected = -1;
        ContentValues contentValues = new ContentValues();

        contentValues.putNull(TablePersonal.Icono);
        //contentValues.put(TableParameters.FechaHoraSinc, parameters.FechaHoraSinc);

        rowaffected = database.update(TablePersonal.TABLE_NAME,contentValues,TablePersonal.Icono + " = ? ",new String[] { fotopersonal });
        this.close();

        return rowaffected;
    }




}
