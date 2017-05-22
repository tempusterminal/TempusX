package com.tempus.proyectos.threads;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityGeomano;
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
            Biometrias objBiometria = ActivityGeomano.objEspacio01;

            // Borramos template
            queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(this.activity);
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

        ActivityGeomano.idTipoDetaBio = 0;
        ActivityGeomano.objEspacio01 = null;

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (exito){
                    ActivityGeomano.imgViewGeomano.setVisibility(View.INVISIBLE);
                    ActivityGeomano.imgViewResultOK.setVisibility(View.VISIBLE);
                    ActivityGeomano.imgViewResultKO.setVisibility(View.INVISIBLE);
                    ActivityGeomano.txvManoTexto.setText(mensajeRespuesta);
                    ActivityGeomano.txvManoTexto.setVisibility(View.VISIBLE);
                } else {
                    ActivityGeomano.imgViewGeomano.setVisibility(View.INVISIBLE);
                    ActivityGeomano.imgViewResultKO.setVisibility(View.VISIBLE);
                    ActivityGeomano.imgViewResultOK.setVisibility(View.INVISIBLE);
                    ActivityGeomano.txvManoTexto.setText(mensajeRespuesta);
                    ActivityGeomano.txvManoTexto.setVisibility(View.VISIBLE);
                }
            }
        });

        util.sleep(1750);


        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ActivityGeomano.manageScreenEnroll(false);
                //ActivityGeomano.dialog.viewDialog.dismiss();
                ActivityGeomano.analizarRegistroBiometriaList(activity);
            }
        });
    }
}
