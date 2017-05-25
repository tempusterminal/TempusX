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
    String FIX = "FF0AFF30031C981E3297FFFF0A01440038F6FFFF";


    public MainHandPunch(Activity activity){
        util = new Utilities();
        this.activity = activity;
    }

    public String SerialHandPunch(OutputStream out, InputStream input, String opcion, String data) {

        Log.d("Handpunch","data: " + data);

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
                Log.d("HandPunch","entro Verify");
                String crc = util.getCRC16CCITT("014A0B0001" + data, 0x1021, 0x0000, true);
                Log.d("HandPunch","crc: " + crc + " - length: " + crc.length());
                int tamanio = crc.length();
                try {
                    if (crc.length()<4){
                        Log.d("HandPunch","<4");
                        for (int i = 0 ; i < (4-tamanio) ; i++) {
                            crc = "0" + crc;
                            Log.d("HandPunch","VUELTA " + i);
                        }
                        Log.d("HandPunch","crc final: " + crc);
                    }else {
                        Log.d("HandPunch",">4");
                    }
                } catch (Exception e ){
                    Log.e("HandPunch","FIN DEL MUNDO XD - " + e.getMessage());
                }



                Log.d("HandPunch","calculo trama: " + trama + " - " + data +  " - " + crc.substring(2,4) + crc.substring(0,2));

                trama = VERIFY_ON_EXTERNAL_DATA + data + crc.substring(2,4) + crc.substring(0,2) + "FF";
                tam = 11;

                Log.d("HandPunch","salio Verify");
                break;
            case "FIX":
                trama = FIX;
                tam = 19;
                break;
            default:
                trama = ABORT;
                tam = 11;
                break;
        }

        try {
            Log.d("HandPunch","INTENTANDO SALIR: " + trama);
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

    public String OperarStatus(String acumulador,String operacion) {
        String msjreturn = "";
        String res1 = util.hexToBin(acumulador.substring(10,12));
        String res2 = util.hexToBin(acumulador.substring(12,14));

        Log.d("HandPunch","HereIsStatus 1: "+res1); // LEDS
        Log.d("HandPunch","HereIsStatus 2: "+res2); // BUSY - CMD

        a = res1;



        String b = res2.substring(1,2);
        String f = res2.substring(4,5);
        String m = res1.substring(3,7);
        String o = res1.substring(7,8);
        String n = res1.substring(3,8);
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

        if (operacion.equalsIgnoreCase("enrolado")) {
            if (m.equalsIgnoreCase("0000") || o.equalsIgnoreCase("0") ) {
                Log.d("HandPunch","REMUEVA SU MANO");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActivityGeomano.txvManoTexto.setText("Remueva\nsu\nmano");
                    }
                });
            }

            if (n.equalsIgnoreCase("11111")){
                Log.d("HandPunch","COLOQUE SU MANO");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActivityGeomano.txvManoTexto.setText("Coloque\nsu\nmano");
                    }
                });
            }
            AdministrarLed(a);
        } else {
            AdministrarLedPrincipal(a);
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


    public void AdministrarLedPrincipal(String tramaLed) {
        String leds = tramaLed.substring(3,7);
        String led[] = leds.split("");

        if (led[1].equalsIgnoreCase("1")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.rounded_corner).mutate();
                    ActivityPrincipal.txvMarcacionLed01.setBackground(drawable);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityPrincipal.txvMarcacionLed01.setBackground(null);
                }
            });
        }

        if (led[2].equalsIgnoreCase("1")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.rounded_corner).mutate();
                    ActivityPrincipal.txvMarcacionLed02.setBackground(drawable);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityPrincipal.txvMarcacionLed02.setBackground(null);
                }
            });
        }

        if (led[3].equalsIgnoreCase("1")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.rounded_corner).mutate();
                    ActivityPrincipal.txvMarcacionLed03.setBackground(drawable);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityPrincipal.txvMarcacionLed03.setBackground(null);
                }
            });
        }

        if (led[4].equalsIgnoreCase("1")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.rounded_corner).mutate();
                    ActivityPrincipal.txvMarcacionLed04.setBackground(drawable);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityPrincipal.txvMarcacionLed04.setBackground(null);
                }
            });
        }
    }


    public void verificarMano(){

    }

}





























