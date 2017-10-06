package com.tempus.proyectos.bluetoothSerial;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by ECERNAR on 12/05/2017.
 */

public class Bluetooth {

    private String TAG = "BTS-BT";

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    String mac;
    String pin;
    OutputStream outputStream;
    InputStream inputStream;

    public Bluetooth(String mac) {
        this.mac = mac;
        this.pin = "1234";
    }

    public boolean ConnectBT(){

        clearDat();
        boolean bt = findBT();
        if (bt){
            boolean b = openBT(true);
            if (b) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public void clearDat(){
        this.mBluetoothAdapter = null;
        this.mmSocket = null;
        this.mmDevice = null;
        this.outputStream = null;
        this.inputStream = null;
    }

    public boolean findBT() {

        boolean encontrado = false;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.d("BT", "BT mBluetoothAdapter NULL");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            if(mBluetoothAdapter.enable()){
                if (mBluetoothAdapter.getState() != BluetoothAdapter.STATE_ON)
                    Log.d("BT", "BT encendido correctamente");
                else {
                    if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON)
                        Log.d("BT", "BT está encendiendo, espere un poco y vuelva a intentarlo");
                    else
                        Log.d("BT", "BT no pudo encender");
                    return false;
                }
            }
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equals(mac)) {
                    mmDevice = device;
                    encontrado = true;
                    break;
                }
            }
        }

        return encontrado;
    }

    public boolean openBT(boolean secure){

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID

        try {
            if (secure){
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            } else {
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            }
        } catch (IOException e) {
            Log.e("SOCKET","OpenBT Falló (1) " + e.getMessage());
            return false;
        }

        try {
            mmSocket.connect();
        } catch (IOException e) {
            Log.e("SOCKET","OpenBT Falló (2) " + e.getMessage());
            return false;
        }

        try {
            inputStream = mmSocket.getInputStream();
            outputStream = mmSocket.getOutputStream();
        } catch(Exception e) {
            Log.e("SOCKET","getStream Falló (3) " + e.getMessage());
            return false;
        }

        return true;

    }

    public boolean closeBT(){
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("SOCKET","closeBT Falló (1) "  + e.getMessage());
            return false;
        }
        return true;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public BluetoothSocket getSocket() {
        return this.mmSocket;
    }


    /* ---------------------------- ADMINISTRACIÓN DE USER INTERFACE ---------------------------- */



}
