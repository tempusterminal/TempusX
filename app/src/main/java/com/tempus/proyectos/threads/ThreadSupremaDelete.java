package com.tempus.proyectos.threads;

import android.app.Activity;
import android.util.Log;

import com.tempus.proyectos.tempusx.ActivityBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

/**
 * Created by ecernar on 15/11/2016.
 */

public class ThreadSupremaDelete implements Runnable {

    Utilities util;
    private Activity activity;
    boolean control1 = true;

    @Override
    public void run() {
        int timeout = 50; // 10 segundos
        int contador = 0;

        String parametros[] = new String[1];
        parametros[0] = ActivityPrincipal.objSuprema.convertCardToEnroll(ActivityBiometria.valorTarjeta);
        ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"DeleteTemplate",parametros);

        while (control1){
            if (contador > timeout || ActivityBiometria.accionCancel) {
                Log.v("TEMPUS:","Tiempo excedido Delete");
                cancelarEnroll();
            } else {
                if (!ActivityPrincipal.huellaDelete1.isEmpty()) {
                    Log.v("TEMPUS:","Deleting (1) ... "+ActivityPrincipal.huellaDelete1);
                    ActivityPrincipal.huellaDelete1 = "";
                    control1 = false;
                }else {
                    Log.v("TEMPUS:","Primer bucle");
                    contador = contador + 1;
                    util.sleep(200);
                }
            }
        }
    }

    public void cancelarEnroll(){
        ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"Cancel",null);

        control1 = false;

        ActivityPrincipal.huellaDelete1 = "";
        ActivityPrincipal.isDeleting = false;
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
