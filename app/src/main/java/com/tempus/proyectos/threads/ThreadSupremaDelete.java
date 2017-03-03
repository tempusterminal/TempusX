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

/**
 * Created by ecernar on 15/11/2016.
 */

public class ThreadSupremaDelete implements Runnable {

    Utilities util;
    private Activity activity;
    boolean control1 = true;
    String valorIndice;

    public boolean exito;
    public String mensajeRespuesta;

    QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;

    public ThreadSupremaDelete(Activity activity){
        util = new Utilities();
        this.activity = activity;
        exito = false;
        mensajeRespuesta = "";
    }

    @Override
    public void run() {
        int timeout = 50; // 10 segundos
        int contador = 0;

        String parametros[] = new String[1];
        valorIndice = String.valueOf(ActivityBiometria.indice);
        parametros[0] = ActivityPrincipal.objSuprema.convertCardToEnroll(valorIndice);
        ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"DeleteTemplate",parametros);

        while (control1){
            if (contador > timeout || ActivityBiometria.accionCancel) {
                Log.v("TEMPUS:","Tiempo excedido Delete");
                mensajeRespuesta = "Tiempo excedido";
                cancelarEnroll();
            } else {

                if (ActivityPrincipal.huellaDeleteFlag1.equals("SUCCESS") || ActivityPrincipal.huellaDeleteFlag1.equals("NOT_FOUND")) {

                    Log.e("TEMPUS:","VARIABLE" + ActivityPrincipal.huellaDeleteFlag1);

                    if (!ActivityPrincipal.huellaDelete1.isEmpty()) {
                        Log.v("TEMPUS:","Deleting (1) ... "+ActivityPrincipal.huellaDelete1);
                        ActivityPrincipal.huellaDelete1 = "";

                        Biometrias objBiometria = ActivityBiometria.objEspacio02;

                        // Insertamos template en base de datos
                        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(this.activity);
                        String respuesta = queriesPersonalTipolectoraBiometria.EliminarBiometrias(objBiometria);

                        mensajeRespuesta = respuesta;

                        if (respuesta.equalsIgnoreCase("Biometria eliminada")){
                            exito = true;
                        } else {
                            exito = false;
                        }

                        DBManager dbManager = new DBManager(this.activity);
                        dbManager.open();
                        dbManager.execSQL("UPDATE PERSONAL_TIPOLECTORA_BIOMETRIA SET VALOR_BIOMETRIA = NULL WHERE INDICE_BIOMETRIA = "+valorIndice+"");
                        dbManager.close();

                        control1 = false;
                        cancelarEnroll();
                    }else {
                        Log.v("TEMPUS:","Primer bucle");
                        contador = contador + 1;
                        util.sleep(200);
                    }
                } else {
                    Log.v("TEMPUS:","Primer bucle");
                    contador = contador + 1;
                    util.sleep(200);
                }

                if (ActivityPrincipal.huellaDeleteFlag1.equalsIgnoreCase("")){
                    Log.v("TEMPUS:","Primer bucle isEmpty Delete");
                    util.sleep(200);
                }
            }
        }

        Thread.currentThread().interrupt();
    }

    public void cancelarEnroll(){
        ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"Cancel",null);

        control1 = false;

        ActivityPrincipal.huellaDelete1 = "";
        ActivityPrincipal.isDeleting = false;
        ActivityBiometria.ocupado = false;
        ActivityBiometria.accionCancel = false;
        ActivityPrincipal.huellaDeleteFlag1 = "";

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
