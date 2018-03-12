package com.tempus.proyectos.log;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.tempusx.ActivityPrincipal;

import java.util.List;

/**
 * Created by gurrutiam on 25/08/2017.
 Created by hider on 23/01/2018
 Monitorea la aplicacion en caso que se genere algun tipo de error ANR [APPLICATION NOT RESPONDING]
 y/o error no controlado <P>tecnicamente ejecuta u Thread infinito comprobando el estado de la App en
 caso que haya algun tipo de ANR o un error no controlado, inmediatamente cierra la App</P> <p>al
 cerrar la app es necesario iniciar la App de alguna manera </p>

 para llamar a esta funcinalinad cree un nuevo objeto en su actividad principal metodo onCreate en inicie el hilo
 new MonitorApp(this).start();
 */

public class MonitorApp extends Thread{

    private String TAG = "LG-MAPP";

    private int condicionApp = -1;
    private Activity activity;
    private ActivityManager manager;
    private List<ActivityManager.ProcessErrorStateInfo> stateInRun;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public MonitorApp(Activity activity) {
        //this.setPriority(MAX_PRIORITY);
        this.activity = activity;
        manager = (ActivityManager) ActivityPrincipal.context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void closeApp(){
        bluetoothAdapter.disable();
        activity.finish();
        activity.finishAndRemoveTask();
        Process.killProcess(Process.myPid());
    }

    @Override
    public void run() {
        QueriesLogTerminal queriesLogTerminal = new QueriesLogTerminal();
        while (ActivityPrincipal.MAPP) {
            try {
                // Log.v(TAG,"Monitoreando App");
                Thread.sleep(1000);
                stateInRun = manager.getProcessesInErrorState();
                if (stateInRun != null) {
                    if (stateInRun.size() > 0) {
                        for (int i = 0; i < stateInRun.size(); i++) {
                            condicionApp = stateInRun.get(i).condition;
                            switch (condicionApp) {
                                case ActivityManager.ProcessErrorStateInfo.CRASHED:
                                    Log.v(TAG, "run: appCaida: cerrando app con killProcess");
                                    queriesLogTerminal.insertLogTerminal(TAG,"CRASHED","");
                                    bluetoothAdapter.disable();
                                    activity.finish();
                                    activity.finishAndRemoveTask();
                                    Process.killProcess(Process.myPid());
                                    break;
                                case ActivityManager.ProcessErrorStateInfo.NOT_RESPONDING:
                                    Log.v(TAG, "run: ANR cerrando app ");
                                    queriesLogTerminal.insertLogTerminal(TAG,"NOT_RESPONDING","");
                                    bluetoothAdapter.disable();
                                    activity.finish();
                                    activity.finishAndRemoveTask();
                                    Process.killProcess(Process.myPid());
                                    break;
                                case ActivityManager.ProcessErrorStateInfo.NO_ERROR:
                                    break;
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "MonitorApp " + e.getMessage());
            }
        }

    }


}
