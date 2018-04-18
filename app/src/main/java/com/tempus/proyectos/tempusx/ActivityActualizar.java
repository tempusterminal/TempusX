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
import android.widget.TextView;
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

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;

    ImageView btnMasterExportar;
    Button btnInstallAPK;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        // Aumentar el brillo al maximo brillo establecido
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = (float) (ActivityPrincipal.maxBrilloAhorroEnergia/100);
        getWindow().setAttributes(layoutParams);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Exportar";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        txvFondo1 = (TextView) findViewById(R.id.txvFondo1);
        txvFondo2 = (TextView) findViewById(R.id.txvFondo2);
        txvBarraInf = (TextView) findViewById(R.id.txvBarraInf);
        txvLinea1 = (TextView) findViewById(R.id.txvLinea1);
        txvLinea2 = (TextView) findViewById(R.id.txvLinea2);
        txvLinea3 = (TextView) findViewById(R.id.txvLinea3);
        txvLinea4 = (TextView) findViewById(R.id.txvLinea4);

        ActivityPrincipal.setBackgroundColorOnTextView(txvFondo1,ActivityPrincipal.parametersColorsUI.split(",")[0],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvFondo2,ActivityPrincipal.parametersColorsUI.split(",")[1],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvBarraInf,ActivityPrincipal.parametersColorsUI.split(",")[2],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea1,ActivityPrincipal.parametersColorsUI.split(",")[3],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea2,ActivityPrincipal.parametersColorsUI.split(",")[4],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea3,ActivityPrincipal.parametersColorsUI.split(",")[5],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea4,ActivityPrincipal.parametersColorsUI.split(",")[6],"#777777");

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
                //new DownloadFileFromURL().execute("http://192.168.0.14/TempusX/tempusx_v1.apk");

                Log.e("Tempus: ", "Instalando");
                Toast.makeText(getApplicationContext(),"Instalando actualizaciones ...",Toast.LENGTH_SHORT);
                InstallAPK(Environment.getExternalStorageDirectory().toString()  + "/tempus/tempusx_v1.apk");
                Log.e("Tempus: ", "Instalado");
                Toast.makeText(getApplicationContext(),"Sistema actualizado ... reiniciando ...",Toast.LENGTH_SHORT);

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // En caso que se detecte un evento sobre la pantalla el contadorAhorroEnergia se reiniciara en 1
        ActivityPrincipal.contadorAhorroEnergia = 1;

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



    class DownloadFileFromDevice extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting install");

            pDialog = new ProgressDialog(ActivityActualizar.this);
            pDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            pDialog.setMessage("Verificando actualizaciones ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            try {

                publishProgress("Verificando actualizaciones ...");

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
