package com.tempus.proyectos.threads;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.tempus.proyectos.tempusx.ActivityBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

/**
 * Created by ecernar on 24/04/2017.
 */

public class ThreadHandPunchEnroll implements Runnable {

    Utilities util;
    private Activity activity;
    public boolean exito;
    public String mensajeRespuesta;

    public ThreadHandPunchEnroll(Activity activity){
        util = new Utilities();
        this.activity = activity;
        exito = false;
        mensajeRespuesta = "";
    }

    @Override
    public void run() {

        boolean resultado = false;
        int idDetaBio = ActivityBiometria.idTipoDetaBio;

        try {
            resultado = ActivityPrincipal.objHandPunch.handRecognizer_EnrolarMano(ActivityPrincipal.btSocket03.getOutputStream(), idDetaBio);
        } catch (Exception e) {
            Log.d("ThreadHandPunchEnroll", e.getMessage());
        }

        if (resultado) {
            mensajeRespuesta = "BIOMETRIA ENROLADA CON EXITO";
        } else {
            mensajeRespuesta = "FALLO AL ENROLAR";
        }

        cancelarEnroll();

        Thread.currentThread().interrupt();
    }


    public void cancelarEnroll(){

        ActivityBiometria.ocupado = false;

        ActivityBiometria.idTipoDetaBio = 0;
        ActivityBiometria.objEspacio01 = null;
        ActivityBiometria.objEspacio02 = null;

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (exito){
                    ActivityBiometria.txvHuellaGif.setVisibility(View.INVISIBLE);
                    ActivityBiometria.imgViewResultOK.setVisibility(View.VISIBLE);
                    ActivityBiometria.imgViewResultKO.setVisibility(View.INVISIBLE);
                    ActivityBiometria.txvHuellaTexto.setText(mensajeRespuesta);
                    ActivityBiometria.txvHuellaTexto.setVisibility(View.VISIBLE);
                } else {
                    ActivityBiometria.txvHuellaGif.setVisibility(View.INVISIBLE);
                    ActivityBiometria.imgViewResultKO.setVisibility(View.VISIBLE);
                    ActivityBiometria.imgViewResultOK.setVisibility(View.INVISIBLE);
                    ActivityBiometria.txvHuellaTexto.setText(mensajeRespuesta);
                    ActivityBiometria.txvHuellaTexto.setVisibility(View.VISIBLE);
                }
            }
        });

        util.sleep(1750);

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ActivityBiometria.manageScreenEnroll(false);
                ActivityBiometria.dialog.viewDialog.dismiss();
                ActivityBiometria.analizarRegistroBiometriaList(activity);
            }
        });
    }

}
