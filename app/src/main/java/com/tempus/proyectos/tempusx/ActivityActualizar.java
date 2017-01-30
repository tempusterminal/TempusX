package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tempus.proyectos.util.UserInterfaceM;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class ActivityActualizar extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    ImageView btnMasterExportar;
    Button btnInstallAPK;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Exportar";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterExportar = (ImageView) findViewById(R.id.btnMasterActualizar);
        btnInstallAPK = (Button) findViewById(R.id.btnInstallAPK);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);

        /* --- Inicialización de Parametros Generales --- */

        /* --- Eventos --- */



        //edtApkName.setText(Environment.getExternalStorageDirectory().toString()  + "/Download/system.apk");



        btnMasterExportar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityActualizar.this, ActivityMenu.class , "","");
            }
        });

        btnInstallAPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new DownloadFileFromURL().execute("http://www.mirrorupdtxd.pe.hu/dev/tempusx_v1.apk");
                new DownloadFileFromURL().execute("http://192.168.0.14/TempusX/tempusx_v1.apk");
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                ui.initScreen(this);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            pDialog = new ProgressDialog(ActivityActualizar.this);
            pDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            pDialog.setMessage("Descargando actualizaciones ... por favor espere ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                OutputStream output = new FileOutputStream(root+"/Download/tempusx_v1.apk");
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                Log.e("Tempus: ", "Instalando");

                publishProgress("Instalando actualizaciones ...");

                InstallAPK(Environment.getExternalStorageDirectory().toString()  + "/Download/tempusx_v1.apk");

                Log.e("Tempus: ", "Instalado");

                publishProgress("Sistema actualizado ... reiniciando ...");

                reboot();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }



        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");

            pDialog.dismiss();
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setMessage(progress[0]);
        }

    }

    public void InstallAPK(String filename){
        File file = new File(filename);
        if(file.exists()){
            Log.e("TEMPUS: ","Si existe");
            try {
                String command;
                command = "pm install -r " + filename;
                Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
                proc.waitFor();

                Log.e("TEMPUS: ","Acabo");
            } catch (Exception e) {
                Log.e("TEMPUS: ",e.getMessage());
            }
        } else {
            Log.e("TEMPUS: ","No existe");
        }
    }

    public void reboot() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i("TEMPUS: ", "No se puede reiniciar!!!!!!!!!!!!!!!!", ex);
        }
    }
}
