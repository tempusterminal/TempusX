package com.tempus.proyectos.threads;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.view.View;

import com.tempus.proyectos.bluetoothSerial.Bluetooth;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

/**
 * Created by ECERNAR on 12/05/2017.
 */

public class ThreadConnectSerial extends Thread{

    // ---------------------------------------------------------------------------------------------

    public final String TAG = "THREAD_CONNECTSERIAL";

    // ---------------------------------------------------------------------------------------------

    Utilities util;
    Activity activity;

    // ---------------------------------------------------------------------------------------------

    public ThreadConnectSerial(Activity activity){
        util = new Utilities();
        this.activity = activity;
    }

    @Override
    public void run() {

        int contS01 = 0;
        int contS02 = 0;
        int contS03 = 0;

        while(true) {
            if (ActivityPrincipal.INICIADO){
                util.sleep(1000);
            } else {
                Log.d(TAG,"threadSerial > Restando BT ... ");
                restartBluetooth();

                Log.d(TAG,"threadSerial > Iniciando BT ... ");
                util.sleep(2000);

                Log.d(TAG,"threadSerial > Iniciando Seriales ... ");

                if (ActivityPrincipal.BT_01_ENABLED) {

                    boolean continuar01 = true;

                    while (continuar01){
                        try {
                            ActivityPrincipal.btSocket01.closeBT();
                        } catch( Exception e ) {
                            Log.e(TAG,"threadSerial > Arduino: " + e.getMessage());
                        }

                        Log.d(TAG,"threadSerial > Iniciando Conexion btSocketArduino");
                        boolean success = ActivityPrincipal.btSocket01.ConnectBT();
                        if (success) {
                            Log.d(TAG,"threadSerial > Arduino Conectado");
                            ActivityPrincipal.BT_01_IS_CONNECTED = true;
                            continuar01 = false;
                            contS01 = 0;
                        } else {
                            Log.d(TAG,"threadSerial > Arduino Desconectado");
                            ActivityPrincipal.BT_01_IS_CONNECTED = false;
                        }

                        util.sleep(5000);
                        contS01=contS01+1;

                        if (contS01 >= 36){
                            contS01 = 0;
                            this.activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ActivityPrincipal.buttonWarning01.setVisibility(View.VISIBLE);
                                    ActivityPrincipal.buttonWarning02.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }

                if (ActivityPrincipal.BT_02_ENABLED) {

                    boolean continuar02 = true;

                    while (continuar02){
                        try {
                            ActivityPrincipal.btSocket02.closeBT();
                        } catch( Exception e ) {
                            Log.e(TAG,"threadSerial > Suprema: " + e.getMessage());
                        }

                        Log.d(TAG,"threadSerial > Iniciando Conexion btSocketSuprema");
                        boolean success = ActivityPrincipal.btSocket02.ConnectBT();
                        if (success) {
                            Log.d(TAG,"threadSerial > Suprema Conectado");
                            ActivityPrincipal.BT_02_IS_CONNECTED = true;
                            continuar02 = false;
                            contS02=0;
                        } else {
                            Log.d(TAG,"threadSerial > Suprema Desconectado");
                            ActivityPrincipal.BT_02_IS_CONNECTED = false;
                        }

                        util.sleep(5000);
                        contS02=contS02+1;

                        if (contS02 >= 36){
                            contS02 = 0;
                            this.activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ActivityPrincipal.buttonWarning01.setVisibility(View.VISIBLE);
                                    ActivityPrincipal.buttonWarning02.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }

                if (ActivityPrincipal.BT_03_ENABLED) {

                    boolean continuar03 = true;

                    while (continuar03){
                        try {
                            ActivityPrincipal.btSocket03.closeBT();
                        } catch( Exception e ) {
                            Log.e(TAG,"threadSerial > HandPunch: " + e.getMessage());
                        }

                        Log.d(TAG,"threadSerial > Iniciando Conexion btSocketHandpunch");
                        boolean success = ActivityPrincipal.btSocket03.ConnectBT();
                        if (success) {
                            Log.d(TAG,"threadSerial > HandPunch Conectado");
                            ActivityPrincipal.BT_03_IS_CONNECTED = true;
                            continuar03 = false;
                            contS03=0;
                        } else {
                            Log.d(TAG,"threadSerial > HandPunch Desconectado");
                            ActivityPrincipal.BT_03_IS_CONNECTED = false;
                        }

                        util.sleep(5000);
                        contS03=contS03+1;

                        if (contS03 >= 36){
                            contS03 = 0;
                            this.activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ActivityPrincipal.buttonWarning01.setVisibility(View.VISIBLE);
                                    ActivityPrincipal.buttonWarning02.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }

                ActivityPrincipal.INICIADO = true;
                util.sleep(2500);
            }
        }
    }

    public void restartBluetooth(){
        BluetoothAdapter.getDefaultAdapter().disable();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG,"threadSerial > restartBluetooth (disable) " + e.getMessage());
        }

        BluetoothAdapter.getDefaultAdapter().enable();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG,"threadSerial > restartBluetooth (enable) " + e.getMessage());
        }
    }
}
