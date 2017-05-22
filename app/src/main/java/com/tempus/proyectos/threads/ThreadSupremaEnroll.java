package com.tempus.proyectos.threads;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

import java.io.IOException;

/**
 * Created by ecernar on 15/11/2016.
 */

public class ThreadSupremaEnroll implements Runnable {

    Utilities util;
    private Activity activity;
    boolean control1 = true;
    boolean control2 = true;
    String templateBiometria;
    int idDetaBio;
    String valorIndice;

    public boolean exito;
    public String mensajeRespuesta;

    QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;

    public ThreadSupremaEnroll(Activity activity){
        util = new Utilities();
        this.activity = activity;
        exito = false;
        mensajeRespuesta = "";
    }

    @Override
    public void run() {

        int timeout = 50; // 10 segundos
        int contador = 0;

        // Enviamos Peticion para enrolar
        String parametros[] = new String[1];
        valorIndice = String.valueOf(ActivityBiometria.indice);
        parametros[0] = ActivityPrincipal.objSuprema.convertCardToEnroll(valorIndice);
        Log.e("TEMPUS: ", String.valueOf(parametros[0]));

        try {
            ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"EnrollByScan",parametros);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (control1) {
            if (contador > timeout || ActivityBiometria.accionCancel) {
                Log.v("TEMPUS:","Tiempo excedido");
                mensajeRespuesta = "Tiempo excedido";
                cancelarEnroll();
            } else {
                if (ActivityPrincipal.huellaEnrollFlag1.equals("SUCCESS")) {
                    if (!ActivityPrincipal.huellaEnroll1.isEmpty()) {
                        Log.v("TEMPUS:","Enrolling (1) ... "+ActivityPrincipal.huellaEnroll1);
                        ActivityPrincipal.huellaEnroll1 = "";
                        control1 = false;
                    } else {
                        Log.v("TEMPUS:","Primer bucle");
                        contador = contador + 1;
                        util.sleep(200);
                    }
                } else {
                    contador = contador + 1;
                    Log.v("TEMPUS:","Aquiiiiiiiii 1" + ActivityPrincipal.huellaEnrollFlag1);
                }

                if (ActivityPrincipal.huellaEnrollFlag1.equalsIgnoreCase("")){
                    Log.v("TEMPUS:","Primer bucle isEmpty");
                    util.sleep(200);
                }
            }
        }

        // Enviamos Peticion para extraer huella
        if (control2) {
            try {
                ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"ReadTemplate",parametros);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (control2) {

            if (ActivityPrincipal.huellaEnrollFlag2.equals("SUCCESS") || ActivityPrincipal.huellaEnrollFlag2.equals("CONTINUE")) {
                if (!ActivityPrincipal.huellaEnroll2.isEmpty()) {
                    Log.v("TEMPUS:","Enrolling (2) ... "+ActivityPrincipal.huellaEnroll2);
                    templateBiometria = ActivityPrincipal.huellaEnroll2;
                    idDetaBio = ActivityBiometria.idTipoDetaBio;

                    // Validamos biometria

                    /* VACIO */

                    String templateSalida = "";
                    Biometrias objBiometria = null;

                    try {
                        if (idDetaBio == 1){
                            templateSalida = templateBiometria.substring(26,538); // Huella 1
                            objBiometria = ActivityBiometria.objEspacio01;
                        } else {
                            if (idDetaBio == 2){
                                templateSalida = templateBiometria.substring(566,1078); // Huella 2
                                objBiometria = ActivityBiometria.objEspacio02;
                            }
                        }
                    } catch(Exception e) {
                        Log.e("TEMPUS: ","Error Al registrar Huella");
                    }
                    // Formatear Huella
                    String template = ActivityPrincipal.objSuprema.formatTemplate(templateSalida);
                    Log.v("TEMPUS:",template);

                    if (template.length()!=0){
                        // Insertamos template en base de datos
                        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(this.activity);
                        String respuesta = queriesPersonalTipolectoraBiometria.RegistrarBiometrias(objBiometria, template);

                        mensajeRespuesta = respuesta;

                        if (respuesta.equalsIgnoreCase("Biometria enrolada")){
                            exito = true;
                        } else {
                            exito = false;
                        }

                    } else {
                        Log.v("TEMPUS:","FAIL!");
                    }

                    ActivityPrincipal.huellaEnroll2 = "";
                    control2 = false;
                    cancelarEnroll();

                } else {
                    Log.v("TEMPUS:","Segundo bucle ");
                    util.sleep(200);
                }
            } else {
                Log.v("TEMPUS:","Aquiiiiiiiii 2" + ActivityPrincipal.huellaEnrollFlag2);
            }

            if (ActivityPrincipal.huellaEnrollFlag2.equalsIgnoreCase("")){
                Log.v("TEMPUS:","Segundo bucle isEmpty");
                util.sleep(200);
            }
        }

        Log.v("TEMPUS:","Terminando Procesos!!!!!!!!!!!!!!!!!!!!!");

        Thread.currentThread().interrupt();
    }

    public void cancelarEnroll(){
        try {
            ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"Cancel",null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        control1 = false;
        control2 = false;

        ActivityPrincipal.huellaEnrollFlag1 = "";
        ActivityPrincipal.huellaEnrollFlag2 = "";
        ActivityPrincipal.huellaEnroll1 = "";
        ActivityPrincipal.huellaEnroll2 = "";
        ActivityPrincipal.isEnrolling = false;
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
