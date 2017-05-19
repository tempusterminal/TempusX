package com.tempus.proyectos.bluetoothSerial;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by gurrutiam on 10/05/2017.
 */

public class MainEthernet {

    public void writeData(OutputStream out, String data) {
        Log.v("ETHERNET BT", "Write Activado");
        try {
            byte[] a = data.getBytes();
            out.write(a);
            Log.v("ETHERNET BT", "Write Finalizado");
        } catch (Exception e) {
            Log.e("ETHERNET BT","ETHERNET BT ERROR: " + e.getMessage());
        }
    }





}
