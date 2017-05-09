package com.tempus.proyectos.bluetoothSerial;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.tempusx.ActivityBiometria;
import com.tempus.proyectos.tempusx.ActivityGeomano;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.tempusx.R;
import com.tempus.proyectos.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ecernar on 12/04/2017.
 */

public class MainHandPunch {

    String ABORT = "FF0A013200C754FF";
    String ENROLL_USER = "FF0A0149020001FB49FF";
    String SEND_STATUS_CRC = "FF0A01440038F6FF";
    String SEND_TEMPLATE = "FF0A014B0006E6FF";
    String VERIFY_ON_EXTERNAL_DATA = "FF0A014A0B0001";
    Utilities util;
    Activity activity;
    String a;


    public MainHandPunch(Activity activity){
        util = new Utilities();
        this.activity = activity;
    }

    public String SerialHandPunch(OutputStream out, InputStream input, String opcion, String data) {

        String resultado = "";
        String trama = "";
        int tam = 0;

        switch (opcion) {
            case "ABORT":
                trama = ABORT;
                tam = 11;
                break;
            case "ENROLL_USER":
                trama = ENROLL_USER;
                tam = 11;
                break;
            case "SEND_STATUS_CRC":
                trama = SEND_STATUS_CRC;
                tam = 11;
                break;
            case "SEND_TEMPLATE":
                trama = SEND_TEMPLATE;
                tam = 19;
                break;
            case "VERIFY_ON_EXTERNAL_DATA":
                String crc = util.getCRC16CCITT("014A0B0001" + data, 0x1021, 0x0000, true);
                trama = VERIFY_ON_EXTERNAL_DATA + data + crc.substring(2,4) + crc.substring(0,2) + "FF";
                tam = 11;
                break;
            default:
                trama = ABORT;
                tam = 11;
                break;
        }

        try {
            out.write(util.hexStringToByteArray(trama));
            Log.d("HandPunch","SALIO: " + trama);
        } catch (IOException e) {
            e.printStackTrace();
        }
        util.sleep(500);

        byte[] rawBytes = new byte[1];
        String acumulador = "";

        for ( int i = 0 ; i < tam ; i++ ) {
            try {
                input.read(rawBytes);
                acumulador = acumulador + util.byteArrayToHexString(rawBytes);
                //Log.d("HandPunch","LLEGO (*): " + acumulador);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        resultado = acumulador;
        Log.d("HandPunch","LLEGO: " + acumulador);
        return resultado;
    }

    public String OperarStatus(String acumulador) {
        String msjreturn = "";
        String res1 = util.hexToBin(acumulador.substring(10,12));
        String res2 = util.hexToBin(acumulador.substring(12,14));
        String res3 = util.hexToBin(acumulador.substring(14,16));

        Log.d("HandPunch","HereIsStatus 1: "+res1); // BUSY - CMD
        Log.d("HandPunch","HereIsStatus 2: "+res2); // LEDS
        Log.d("HandPunch","HereIsStatus 3: "+res3); // AUX

        a = res1;

        AdministrarLed(a);

        String b = res2.substring(1,2);
        String f = res2.substring(4,5);
        Log.d("HandPunch","BUSY " + b);
        Log.d("HandPunch","FAIL " + f);

        if (b.equalsIgnoreCase("0") && f.equalsIgnoreCase("0")) {
            Log.d("HandPunch","Exito");
            msjreturn = "Exito";
        }

        if (b.equalsIgnoreCase("0") && f.equalsIgnoreCase("1")) {
            Log.d("HandPunch","Fallo");
            msjreturn = "Fallo";
        }
        return msjreturn;
    }


    public void AdministrarLed(String tramaLed) {
        String leds = tramaLed.substring(3,7);
        String led[] = leds.split("");

        if (led[1].equalsIgnoreCase("1")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.rounded_corner).mutate();
                    ActivityGeomano.led01.setBackground(drawable);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityGeomano.led01.setBackground(null);
                }
            });
        }

        if (led[2].equalsIgnoreCase("1")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.rounded_corner).mutate();
                    ActivityGeomano.led02.setBackground(drawable);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityGeomano.led02.setBackground(null);
                }
            });
        }

        if (led[3].equalsIgnoreCase("1")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.rounded_corner).mutate();
                    ActivityGeomano.led03.setBackground(drawable);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityGeomano.led03.setBackground(null);
                }
            });
        }

        if (led[4].equalsIgnoreCase("1")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.rounded_corner).mutate();
                    ActivityGeomano.led04.setBackground(drawable);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityGeomano.led04.setBackground(null);
                }
            });
        }
    }

}





























