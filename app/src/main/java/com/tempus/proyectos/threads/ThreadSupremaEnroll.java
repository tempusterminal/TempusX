package com.tempus.proyectos.threads;

import android.app.Activity;
import android.util.Log;

import com.tempus.proyectos.tempusx.ActivityBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

/**
 * Created by ecernar on 15/11/2016.
 */

public class ThreadSupremaEnroll implements Runnable {

    Utilities util;
    private Activity activity;
    boolean control1 = true;
    boolean control2 = true;

    public ThreadSupremaEnroll(Activity activity){
        util = new Utilities();
        this.activity = activity;
    }

    @Override
    public void run() {

        int timeout = 50; // 10 segundos
        int contador = 0;

        // Enviamos Peticion para enrolar
        String parametros[] = new String[1];
        parametros[0] = ActivityPrincipal.objSuprema.convertCardToEnroll(ActivityBiometria.valorTarjeta);
        ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"EnrollByScan",parametros);

        while (control1) {
            if (contador > timeout || ActivityBiometria.accionCancel) {
                Log.v("TEMPUS:","Tiempo excedido");
                cancelarEnroll();
            } else {
                if (!ActivityPrincipal.huellaEnroll1.isEmpty()) {
                    Log.v("TEMPUS:","Enrolling (1) ... "+ActivityPrincipal.huellaEnroll1);
                    ActivityPrincipal.huellaEnroll1 = "";
                    control1 = false;
                }else {
                    Log.v("TEMPUS:","Primer bucle");
                    contador = contador + 1;
                    util.sleep(200);
                }
            }
        }

        // Enviamos Peticion para extraer huella
        if (control2) {
            ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"ReadTemplate",parametros);
        }

        while (control2) {
            if (!ActivityPrincipal.huellaEnroll2.isEmpty()) {
                Log.v("TEMPUS:","Enrolling (2) ... "+ActivityPrincipal.huellaEnroll2);
                ActivityPrincipal.huellaEnroll2 = "";
                control2 = false;
                cancelarEnroll();
            }else {
                Log.v("TEMPUS:","Segundo bucle");
                util.sleep(200);
            }
        }

        Log.v("TEMPUS:","Terminando Procesos!!!!!!!!!!!!!!!!!!!!!");

        Thread.currentThread().interrupt();
    }

    public void cancelarEnroll(){
        ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"Cancel",null);

        control1 = false;
        control2 = false;

        ActivityPrincipal.huellaEnroll1 = "";
        ActivityPrincipal.huellaEnroll2 = "";
        ActivityPrincipal.isEnrolling = false;
        ActivityBiometria.ocupado = false;
        ActivityBiometria.accionCancel = false;

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ActivityBiometria.manageScreenEnroll(false);
                ActivityBiometria.dialog.viewDialog.dismiss();
            }
        });
    }
}
