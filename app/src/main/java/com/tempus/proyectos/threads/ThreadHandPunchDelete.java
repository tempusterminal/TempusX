package com.tempus.proyectos.threads;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

/**
 * Created by ecernar on 24/04/2017.
 */

public class ThreadHandPunchDelete implements Runnable {

    Utilities util;
    QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;
    public boolean exito;
    public String mensajeRespuesta;
    public Activity activity;


    public ThreadHandPunchDelete(Activity activity) {
        util = new Utilities();
        this.activity = activity;
        exito = false;
        mensajeRespuesta = "";
    }

    @Override
    public void run() {
        try {
            Biometrias objBiometria = ActivityBiometria.objEspacio02;
            // Borramos template
            queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(ActivityPrincipal.context);
            String respuesta = queriesPersonalTipolectoraBiometria.EliminarBiometrias(objBiometria);
            mensajeRespuesta = respuesta;

            if (respuesta.equalsIgnoreCase("Biometria eliminada")){
                exito = true;
            } else {
                exito = false;
            }

            cancelarEnroll();

        } catch(Exception e) {
            Log.d("ThreadHandPunchDelete",e.getMessage());
        }

        Thread.currentThread().interrupt();

    }

    public void cancelarEnroll(){

        ActivityBiometria.ocupado = false;
        ActivityBiometria.accionCancel = false;
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
