package com.tempus.proyectos.threads;

import android.app.Activity;
import android.util.Log;

import com.tempus.proyectos.bluetoothSerial.MainHandPunch;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

/**
 * Created by ecernar on 20/05/2017.
 */

public class ThreadHandPunchVerify implements Runnable {

    Utilities util;
    private Activity activity;
    MainHandPunch hp;
    String template;

    public ThreadHandPunchVerify(Activity activity, String template){
        util = new Utilities();
        this.activity = activity;
        hp = new MainHandPunch(activity);
        this.template = template;
    }

    @Override
    public void run() {
        Log.d("MarcacionMasterTAG","Paso 1");
        hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "ABORT", null);
        util.sleep(50);

        Log.d("MarcacionMasterTAG","Paso 2");
        hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "VERIFY_ON_EXTERNAL_DATA", this.template);
        util.sleep(50);

        boolean continuar = true;

        Log.d("MarcacionMasterTAG","Paso 3");
        while (continuar) {
            String res = hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "SEND_STATUS_CRC", null);
            String tmp = hp.OperarStatus(res,"");

            if (tmp.equalsIgnoreCase("Exito")){
                Log.d("MarcacionMasterTAG","EXITO");
                continuar = false;
            }

            if (tmp.equalsIgnoreCase("Fallo")){
                Log.d("MarcacionMasterTAG","FALLO");
                continuar = false;
            }

            util.sleep(50);
        }

        hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "SEND_TEMPLATE", null);
        util.sleep(50);
    }
}
