package com.tempus.proyectos.bluetoothSerial;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ecernar on 20/02/2017.
 */

public class BluetoothManager {

    private String TAG = "TX-PBT-BM";
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private String BTaddress;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private Context context;
    private boolean isConnected;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public BluetoothManager(String BTaddress, Context context){
        this.BTaddress = BTaddress;
        this.context = context;
    }

    public boolean Connect() {

        try {
            isConnected = false;
            socket.close();
            Log.d(TAG, "Cerrando conexión previa");
        } catch (Exception e) {
            Log.d(TAG, "Close retornó excepción: " + e);
        }

        if (isConnected) {
            // we got a call to Connect() even though we think we are already
            // connected
            Log.d(TAG, "Connect solicitado cuando ya está conectado");
            return true;
        }
        try {
            if (!findBT()) {
                Log.d(TAG, "findBT retornó false");
                return false;
            }
        } catch (IOException ex) {
            Log.d(TAG, "FindBT retornó excepción: " + ex);
            return false;
        }

        try {
            if (!openBT()) {
                Log.d(TAG, "openBT retornó false");
                return false;
            }
        } catch (IOException ex) {
            Log.d(TAG, "OpenBT retornó excepción: " + ex);
            return false;
        }
        isConnected = true;
        return true;
    }

    // Find Bt
    public boolean findBT() throws IOException {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d(TAG, "BT adapter no disponible");
            return false;
        }

        if (!bluetoothAdapter.isEnabled()) {
            if (bluetoothAdapter.enable()) {
                // The adapter state will immediately transition from STATE_OFF
                // to STATE_TURNING_ON,
                // and some time later transition to either STATE_OFF or
                // STATE_ON.
                // If this call returns false then there was an immediate
                // problem that
                // will prevent the adapter from being turned on - such as
                // Airplane mode,
                // or the adapter is already turned on.
                // So, basically, the bluetooth device is turning on and we need
                // to wait for it to finish
                int counter = 0;
                while (bluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    if (counter > 3) {
                        return false;
                    }
                    counter++;
                }

                if (bluetoothAdapter.getState() != BluetoothAdapter.STATE_ON)
                    Log.d(TAG, "BT encendido correctamente");
                else {
                    if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON)
                        Log.d(TAG, "BT está encendiendo, espere un poco y vuelva a intentarlo");
                    else
                        Log.d(TAG, "BT no pudo encender");
                    return false;
                }
            } else {
                Log.d(TAG, "BT no activado");
                return false;
            }
        } else
            Log.d(TAG, "BT activado previamente");

        // get remote BTs that are paired with us
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter
                .getBondedDevices();
        if (pairedDevices == null) {
            Log.d(TAG, "No hay dispositivos BT emparejados");
            return false;
        }

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equalsIgnoreCase(BTaddress)) {
                    bluetoothDevice = device;
                    Log.d(TAG, "Se encontró el dispositivo BT llamado "
                            + bluetoothDevice.getName());
                    Log.d(TAG, "MAC del dispositivo: "	+ bluetoothDevice.getAddress());
                    break;
                }
            }
        } else {
            Log.d(TAG, "BT adapter no está en STATE_ON, por lo que no pudimos listar los dispositivos BT emparejados");
            return false;
        }

        if (bluetoothDevice == null) {
            Log.d(TAG, "No se ha encontrado ningún BT que coincida con nuestro dispositivo BT");
            return false;
        }

        if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            Log.d(TAG, "No se puede encontrar BT emparejado");
            return false;
        }

        // for some reason, the device found through the above routine does not
        // work properly
        // when we use it and call connect, it generates an exception
        // "Service failed discovery"
        // so we have to find the device using the MAC address instead.

        if (BluetoothAdapter.checkBluetoothAddress(BTaddress)) {
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(BTaddress);
            // A BluetoothDevice will always be returned for a valid hardware address,
            // even if this adapter has never seen that device
            // so we need to check that we are paired with this one
            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                Log.d(TAG, "Nuestro dispositivo BT se reconoce como emparejado");
            } else {
                Log.d(TAG, "El BT encontrado con nuestra dirección no está emparejado");
                return false;
            }
        } else {
            Log.d(TAG, "Direccion BT inválida");
            return false;
        }

        return true;
    }

    // open the connection
    private boolean openBT() throws IOException {
        // in case discovery is still going on,
        // it is important to cancel it before trying to connect or the connect
        // may timeout.
        if (bluetoothAdapter != null)
            bluetoothAdapter.cancelDiscovery();
        else {
            Log.d(TAG, "Intentando conectar sin un BT adapter");
            return false;
        }
        // if the socket was used before, we have to close it before trying to reconnect
        // otherwise _scoket.connect() throws exception java.io.IOException:
        // Device or resource busy
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                Log.d(TAG, "socket.close retornó excepción: " + ex);
            }
        }

        try {
            socket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

        } catch (IOException ex) {
            Log.d(TAG, "createRfcommSocketToServiceRecord retornó excepción:  " + ex);
            return false;
        }

        // if we try to connect multiple times very fast
        // _socket.connect still throws the exception: java.io.IOException:
        // Service discovery failed
        // but the app survives and works OK if we just try connecting again
        try {
            socket.connect();
        } catch (IOException ex) {
            Log.d(TAG, "socket.connect retornó excepción: " + ex);
            return false;
        }

        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException ex) {
            Log.d(TAG, "obteniendo streams retornó excepción: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public void closeBT() throws IOException {
        //stopWorker = true;
        if (outputStream != null)
            outputStream.close();
        if (inputStream != null)
            inputStream.close();
        if (socket != null)
            socket.close();
        socket = null;
        isConnected = false;
    }

    public boolean resetConnection() {
        // start by forcing close
        try {
            closeBT();
        } catch (IOException ex) {
            Log.d(TAG, "closeBT retornó excepción: " + ex);
        }
        isConnected = false;
        if (!Connect())
            return false;
        return true;
    }

    public BluetoothSocket getSocket(){
        return this.socket;
    }

    public OutputStream getOutputStream(){
        return this.outputStream;
    }

    public InputStream getInputStream(){
        return this.inputStream;
    }
}
