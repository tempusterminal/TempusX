package com.tempus.proyectos.log;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.tempus.proyectos.tempusx.ActivityPrincipal;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by gurrutiam on 25/08/2017.
 */

public class MonitorApp {

    private final String TAG = "LG-MAPP";

    private int indexFor = 0;
    private Activity activity;
    private ActivityManager manager;
    private int condicionApp = -1;

    //private List<ActivityManager.RunningAppProcessInfo> infoRunProcess;
    private String app;

    public MonitorApp() {

    }

    public class LookErrorApp extends Thread{
        private Thread hilo;
        private String nombreHilo;
        //private List<ActivityManager.ProcessErrorStateInfo> stateInRun;


        public LookErrorApp(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);

            while (true) {
                try {
                    Thread.sleep(1000);
                    Log.v(TAG,"LookErrorApp - Inicio de Revisión ");
                    List<ActivityManager.ProcessErrorStateInfo> stateInRun = manager.getProcessesInErrorState();
                    if (stateInRun != null) {
                        Log.v(TAG,"stateInRun " + stateInRun.toString());
                        if (stateInRun.size() > 0) {
                            for (indexFor = 0; indexFor < stateInRun.size(); indexFor++) {
                                condicionApp = stateInRun.get(indexFor).condition;
                                //obteniendo el nombre de la app para forzar el cierre
                                app=stateInRun.get(indexFor).processName;
                                switch (condicionApp) {
                                    case ActivityManager.ProcessErrorStateInfo.CRASHED:
                                        Log.v(TAG,"LookErrorApp = " + ActivityManager.ProcessErrorStateInfo.CRASHED);
                                        appCaida();
                                        break;
                                    case ActivityManager.ProcessErrorStateInfo.NOT_RESPONDING:
                                        Log.v(TAG,"LookErrorApp = " + ActivityManager.ProcessErrorStateInfo.NOT_RESPONDING);
                                        appNoResponde();
                                        break;
                                    case ActivityManager.ProcessErrorStateInfo.NO_ERROR:
                                        Log.v(TAG,"LookErrorApp = " + ActivityManager.ProcessErrorStateInfo.NO_ERROR);
                                        break;
                                }
                            }
                        }
                    }else{
                        Log.v(TAG,"stateInRun " + stateInRun.toString());
                        //Thread.sleep(1000);
                    }
                    Log.v(TAG,"LookErrorApp - Fin de Revisión");
                } catch (InterruptedException e) {
                    Log.e(TAG,"stateInRun " + e.getMessage());
                }

            }

        }

        public void start(){
            Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);

            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }

    private void appCaida() {
        Log.e( TAG, "appCaida: Preparando para cerrar"  );
        this.forceStop();
    }

    private void appNoResponde() {
        Log.e( TAG, "appNoResponde: Preparando para cerrar" );
        this.forceStop();
    }

    private void forceStop() {

        try {
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes("adb shell" + "\n");
            os.flush();
            os.writeBytes("am force-stop " + app + "\n");
            os.flush();
            os.close();
            suProcess.waitFor();
        } catch (IOException e) {
            Log.e(TAG,"forceStop IOException " + e.getMessage());
            //Toast.makeText(activity.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Log.e(TAG,"forceStop SecurityException " + e.getMessage());
            //Toast.makeText(activity.getApplicationContext(), "Can't get root access2", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG,"forceStop Exception " + e.getMessage());
            //Toast.makeText(activity.getApplicationContext(), "Can't get root access3", Toast.LENGTH_LONG).show();
        }
    }


    public void starLookErrorApp(Activity activity){
        //Log.v(TAG,"starLookErrorApp");
        //this.activity = activity;
        //manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        //manager = (ActivityManager) activity.getSystemService(activity.getApplicationContext().ACTIVITY_SERVICE);
        manager = (ActivityManager) ActivityPrincipal.context.getSystemService(ActivityPrincipal.context.ACTIVITY_SERVICE);
        LookErrorApp lookErrorApp = new LookErrorApp("starLookErrorApp");
        lookErrorApp.start();
        //Log.v(TAG,"starLookErrorApp ok");

    }




    public class MonitorearApp extends AsyncTask<Void, Void, Void> {
        private final String TAG = "MA";

        private int indexFor = 0;
        private Activity activity;
        private ActivityManager manager;
        private int condicionApp = -1;
        private List<ActivityManager.ProcessErrorStateInfo> stateInRun;
        //private List<ActivityManager.RunningAppProcessInfo> infoRunProcess;
        private String app;

        public MonitorearApp(Activity activity) {

            this.activity = activity;
            manager = (ActivityManager) this.activity.getSystemService(Context.ACTIVITY_SERVICE);
        }
        @Override
        protected Void doInBackground(Void... params) {

            while (true) {
                Log.v(TAG,"MonitorearApp inicio");
                try {
                    Thread.sleep(1000);
                    stateInRun = manager.getProcessesInErrorState();
                    if (stateInRun != null) {
                        if (stateInRun.size() > 0) {
                            for (indexFor = 0; indexFor < stateInRun.size(); indexFor++) {
                                condicionApp = stateInRun.get(indexFor).condition;
                                //obteniendo el nombre de la app para forzar el cierre
                                app=stateInRun.get(indexFor).processName;
                                switch (condicionApp) {
                                    case ActivityManager.ProcessErrorStateInfo.CRASHED:
                                        appCaida();
                                        break;
                                    case ActivityManager.ProcessErrorStateInfo.NOT_RESPONDING:
                                        appNoResponde();
                                        break;

                                    case ActivityManager.ProcessErrorStateInfo.NO_ERROR:
                                        break;
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG,"MonitorearApp " + e.getMessage());
                }
                Log.v(TAG,"MonitorearApp fin");

            }
        }

        private void appCaida() {
            Log.e( TAG, "appCaida: Preparando para cerrar"  );
            this.forceStop();
        }

        private void appNoResponde() {
            Log.e( TAG, "appNoResponde: Preparando para cerrar" );
            this.forceStop();
        }

        private void forceStop() {

            try {
                Process suProcess = Runtime.getRuntime()
                        .exec("su");
                DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

                os.writeBytes("adb shell" + "\n");
                os.flush();
                os.writeBytes("am force-stop " + app + "\n");
                os.flush();
                os.close();
                suProcess.waitFor();
            } catch (IOException ex) {
                ex.getMessage();
                Toast.makeText(activity.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG)
                        .show();
            } catch (SecurityException ex) {
                Toast.makeText(activity.getApplicationContext(), "Can't get root access2", Toast.LENGTH_LONG)
                        .show();
            } catch (Exception ex) {
                Toast.makeText(activity.getApplicationContext(), "Can't get root access3", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    public void execMonitorearApp(Activity activity){
        MonitorearApp monitorearApp = new MonitorearApp(activity);
        monitorearApp.execute();

    }



}
