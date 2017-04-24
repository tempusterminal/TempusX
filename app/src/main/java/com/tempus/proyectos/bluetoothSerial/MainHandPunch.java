package com.tempus.proyectos.bluetoothSerial;

import android.util.Log;

import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityBiometria;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ecernar on 12/04/2017.
 */

public class MainHandPunch {

    Utilities util;
    QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;

    public MainHandPunch(){
        util = new Utilities();
    }

    public void HandRecognizer_ConfigurarLector(OutputStream out){

        for (int i = 0 ; i < 3 ; i++) {
            try {
                out.write(util.hexStringToByteArray("FF0AFF30031C981E3297FFFF0A01440038F6FFFF"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            util.sleep(500);
        }

        try {
            out.write(util.hexStringToByteArray("FF0A013200C754FF"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        util.sleep(500);
    }


    public String HandRecognizer_Envio(OutputStream out, String identificador, String trama){

        String acumulador = "";
        String msjreturn = "";

        try {
            out.write(util.hexStringToByteArray(trama));
            Log.d("HandRecogniter", "PASO ENVIO");
        } catch (IOException e) {
            Log.e("HandRecogniter", "ENVIO: " + e.getMessage());
        }

        util.sleep(250);

        byte[] rawBytes = new byte[20];
        try {
            ActivityPrincipal.btSocket03.getInputStream().read(rawBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        acumulador = acumulador + util.byteArrayToHexString(rawBytes);

        Log.d("HAND PUNCH", "Llego: " + acumulador);

        switch(identificador) {
            case "Abort":
                Log.d("HAND PUNCH","ABORT >");
                msjreturn = "";
                break;
            case "EnrollUser":
                Log.d("HAND PUNCH","ENROLLUSER >");
                msjreturn = "";
                break;
            case "SendStatusCRC":
                Log.d("HAND PUNCH","SENDSTATUSCRC >");
                if (acumulador.length() < 16) {
                    Log.d("HAND PUNCH","Fallo >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    msjreturn = "Fallo";
                }else{
                    String b = "", f = ""; // busy, fail
                    String res = util.hexToBin(acumulador.substring(12,14));
                    b = res.substring(1,2);
                    f = res.substring(4,5);
                    Log.d("HAND PUNCH","BUSY " + b);
                    Log.d("HAND PUNCH","FAIL " + f);

                    if (b == "0" && f == "0") {
                        Log.d("HAND PUNCH","Exito");
                        msjreturn = "Exito";
                    }

                    if (b == "0" && f == "1") {
                        Log.d("HAND PUNCH","Fallo");
                        msjreturn = "Fallo";
                    }
                }
                break;
            case "SendTemplate":
                Log.d("HAND PUNCH","SENDTEMPLATE >");
                msjreturn = acumulador.substring(10,32);
                break;
            case "VerifyOnExternalData":
                Log.d("HAND PUNCH","VERIFYONEXTERNALDATA >");
                break;
        }

        return msjreturn;
    }


    public boolean handRecognizer_VerificarMano(OutputStream out, String template){

        Log.d("HandRecogniter", "Verificando Mano");

        boolean rpta = false;
        String tramaEnvio = "FF0A014A0B0001"+template+""+util.getCRC16CCITT("014A0B0001"+template, 0x1021, 0x0000, true)+"FF";

        Log.d("HandRecogniter", "TRAMA ENVIO: " + tramaEnvio);

        try {
            HandRecognizer_Envio(out, "Abort", "FF0A013200C754FF"); //ENVIA ABORT
            util.sleep(50);
            Log.d("HandRecogniter", "Abort");
        } catch (Exception e) {
            Log.e("HandRecogniter", "Abort: " + e.getMessage());
        }

        try {
            HandRecognizer_Envio(out, "VerifyOnExternalData", tramaEnvio); //ENVIA ABORT
            util.sleep(50);
            Log.d("HandRecogniter", "VerifyOnExternalData");
        } catch (Exception e) {
            Log.e("HandRecogniter", "VerifyOnExternalData: " + e.getMessage());
        }


        boolean espera = true;

        while (espera) {
            String valor = HandRecognizer_Envio(out, "SendStatusCRC", "FF0A01440038F6FF");
            util.sleep(50);

            if (valor.equalsIgnoreCase("Exito")){
                String score = HandRecognizer_Envio(out, "SendTemplate", "FF0A014B0006E6FF");
                score = score.substring(0,4);
                util.sleep(50);
                HandRecognizer_Envio(out, "Abort", "FF0A013200C754FF");

                int scoreInput = util.convertHexToDecimal(score.substring(2,4) + score.substring(0,2));

                if (scoreInput < 100) { // Score coincide
                    rpta = true;
                }else{ // Score no coincide, por encima del umbral
                    rpta = true;
                }
                espera = false;
            }

            if (valor.equalsIgnoreCase("Fallo")){
                Log.d("HandRecogniter", "Verificando Mano FALLO");
                HandRecognizer_Envio(out, "Abort", "FF0A013200C754FF");
                util.sleep(200);
                espera = false;
            }
        }

        return rpta;
    }


    public boolean handRecognizer_EnrolarMano(OutputStream out, int idDetaBio ){
        boolean rpta = false;

        Log.d("HandRecogniter", "Registrando Mano");

        HandRecognizer_Envio(out, "Abort", "FF0A013200C754FF"); //ENVIA ABORT
        util.sleep(200);
        HandRecognizer_Envio(out, "EnrollUser", "FF0A0149020001FB49FF"); //FF0A0149020001FB49FF
        util.sleep(200);

        boolean espera = true;

        while (espera) {
            String valor = HandRecognizer_Envio(out, "SendStatusCRC", "FF0A01440038F6FF");
            util.sleep(200);
            if (valor.equalsIgnoreCase("Exito")){

                String template = HandRecognizer_Envio(out, "SendTemplate", "FF0A014B0006E6FF");
                template = template.substring(4,template.length());
                Log.d("HandRecogniter", "Template: "+template);
                util.sleep(200);
                HandRecognizer_Envio(out, "Abort", "FF0A013200C754FF");
                util.sleep(200);

                Biometrias objBiometria = null;
                String templateSalida = "";

                try {
                    if (idDetaBio == 1){
                        templateSalida = template; // Huella 1
                        objBiometria = ActivityBiometria.objEspacio01;
                    } else {
                        if (idDetaBio == 2){
                            templateSalida = template; // Huella 2
                            objBiometria = ActivityBiometria.objEspacio02;
                        }
                    }
                } catch(Exception e) {
                    Log.e("TEMPUS: ","Error Al registrar Huella");
                }

                // REGISTRAR TEMPLATE EN BASE DE DATOS

                if (template.length()!=0){
                    // Insertamos template en base de datos
                    queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(ActivityPrincipal.context);
                    String respuesta = queriesPersonalTipolectoraBiometria.RegistrarBiometrias(objBiometria, template);

                    if (respuesta.equalsIgnoreCase("Biometria enrolada")){
                        rpta = true;
                    } else {
                        rpta = false;
                    }

                } else {
                    Log.v("TEMPUS:","FAIL!");
                }

                espera = false;
            }

            if (valor.equalsIgnoreCase("Fallo")){
                Log.d("HandRecogniter", "Registrando Mano FALLO");
                HandRecognizer_Envio(out, "Abort", "FF0A013200C754FF");
                util.sleep(200);
                espera = false;
            }

        }

        return rpta;
    }

}





























