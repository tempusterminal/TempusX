package com.tempus.proyectos.threads;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.tempus.proyectos.bluetoothSerial.MainHandPunch;
import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityGeomano;
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
    MainHandPunch hp;
    int idDetaBio;
    QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;

    public ThreadHandPunchEnroll(Activity activity){
        util = new Utilities();
        this.activity = activity;
        exito = false;
        mensajeRespuesta = "";
        hp = new MainHandPunch(activity);
    }

    @Override
    public void run() {

        boolean resultado = false;

        hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "ABORT", null);
        util.sleep(50);

        hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "ENROLL_USER", null);
        util.sleep(50);

        boolean continuar = true;

        while (continuar) {
            if (ActivityGeomano.ABORTAR) {
                hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "ABORT", null);
                util.sleep(50);
                continuar = false;
                ActivityGeomano.ABORTAR = false;
            } else {
                String res = hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "SEND_STATUS_CRC", null);
                String tmp = hp.OperarStatus(res,"enrolado");

                if (tmp.equalsIgnoreCase("Exito")){
                    Log.d("HandPunch","EXITO");
                    continuar = false;
                }

                if (tmp.equalsIgnoreCase("Fallo")){
                    Log.d("HandPunch","FALLO");
                    continuar = false;
                }
                util.sleep(50);
            }
        }

        if (!ActivityGeomano.ABORTAR) {
            String send_template = hp.SerialHandPunch(ActivityPrincipal.btSocket03.getOutputStream(), ActivityPrincipal.btSocket03.getInputStream(), "SEND_TEMPLATE", null);
            util.sleep(50);

            String template = send_template.substring(14,32);

            idDetaBio = ActivityGeomano.idTipoDetaBio;

            Biometrias objBiometria = null;

            try {
                if (idDetaBio == 1){
                    objBiometria = ActivityGeomano.objEspacio01;
                }
            } catch(Exception e) {
                Log.e("HandPunch","Error Al registrar Huella");
            }
            // Formatear Huella
            Log.d("HandPunch", "Template: " + template);

            if (template.length()!=0){
                // Insertamos template en base de datos
                queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(this.activity);
                String respuesta = queriesPersonalTipolectoraBiometria.RegistrarBiometrias(objBiometria, template);

                mensajeRespuesta = respuesta;

                Log.d("HandPunch", "Respuesta: " + mensajeRespuesta);

                if (respuesta.equalsIgnoreCase("Biometria enrolada")){
                    exito = true;
                    Log.d("HandPunch", "Biometria enrolada");
                } else {
                    exito = false;
                    Log.d("HandPunch", "Biometria no enrolada");
                }

            } else {
                Log.v("HandPunch","FAIL!");
            }

        }

        cancelarEnroll();

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
