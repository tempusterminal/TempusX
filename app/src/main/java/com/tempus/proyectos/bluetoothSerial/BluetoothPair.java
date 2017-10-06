package com.tempus.proyectos.bluetoothSerial;

/**
 * Created by gurrutiam on 19/09/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import com.tempus.proyectos.tempusx.ActivityPrincipal;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BluetoothPair extends BroadcastReceiver{

    private String macDevice;
    private String pinDevice;
    private int contador = 0;

    private static final String TAG = "BTS-PR";
    ArrayList<String> deviceTemporalList;
    private final Activity         activity;
    private       IntentFilter     filter;
    private       BluetoothDevice  btDevice;
    private       BluetoothAdapter btAdapter;

    @Override
    public void onReceive(Context context, Intent intent) {
        context = ActivityPrincipal.context;

        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {

            contador++;

            btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (btDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                Log.d(TAG, "Found " + btDevice.getName() + " : " + btDevice.getAddress());
                if (btDevice.getAddress().equals(macDevice)) {
                    Log.d(TAG, "Found device solicitado: " + btDevice.getName() + " : " + btDevice.getAddress());
                    createPair(btDevice, macDevice);
                    // cancelDiscoverDevices();
                    Log.d(TAG, "canceled discovery.");
                    contador = 0;
                } else {
                    contador++;
                    Log.d(TAG, "onReceive: contador equals " + contador);
                    if (contador > 20) {
                        cancelDiscoverDevices();
                        disable();
                    }
                }
                contador = 0;
            } else {
                contador++;
                Log.d(TAG, "onReceive: contador bonded " + contador);
                if (contador > 50) {
                    cancelDiscoverDevices();
                    disable();
                }

            }
        } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
            int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.ERROR);
            if (type == BluetoothDevice.PAIRING_VARIANT_PIN) {
                setPinPairDevice(btDevice, pinDevice);
                abortBroadcast();
            }

        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            contador++;
            if (contador > 50) {
                cancelDiscoverDevices();
                disable();
                contador = 0;
            }
            Log.d(TAG, "onReceive: bonded " + getBondedDevices().size() + " contador:" + contador);
        }
    }

    private void disable() {
        if (isEnable()) {
            cancelDiscoverDevices();
            btAdapter.disable();
            Log.e(TAG, "disable: desactivando BT");
        }
    }

    /**
     * aqui tambien se inicializa los valores para los filtros requeridos para el escaneo
     *
     * @param activity es la actividad donde sera registrado el receptor de escaneo de dispositivos
     */
    public BluetoothPair(Activity activity) {
        btAdapter = getBluetoothAdapter();
        this.activity = activity;
    }

    /**
     * @param pinDevice clave del dispositovo qie se desea emparejar
     * @param macDevice numero de mac del dispositivo a emparejar
     * @return
     */
    public synchronized void pairDevice(String pinDevice, String macDevice) {
        this.pinDevice = pinDevice;
        this.macDevice = macDevice;
        this.discoverDevices();
    }

    /**
     * busca dispositivos no emparejados
     */
    //@RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void discoverDevices() {
        //if (btAdapter != null) {
            //solo si esta habilitado
            if (btAdapter.isEnabled()) {
                //si ya hay un scaneo cancelarlo
                if (btAdapter.isDiscovering()) {
                    btAdapter.cancelDiscovery();
                }
                //scanear dispositivos
            } else {
                btAdapter.enable();
                Log.d(TAG, "discoverDevices: bluetooth  desactivado");
            }
            btAdapter.startDiscovery();
        //}
    }

    /**
     * @return {@link BluetoothAdapter}, null si no soporta
     */
    private synchronized BluetoothAdapter getBluetoothAdapter() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Log.d(TAG, "getBluetoothAdapter: el dispositivo no soporta conexiones bluetooth");
            return null;
        }
        return btAdapter;
    }

    /**
     * @return false si el bt esta desactivado
     */
    //@RequiresPermission(Manifest.permission.BLUETOOTH)
    public synchronized boolean isEnable() {
        if (btAdapter.isEnabled())
            return true;
        else
            return false;
    }

    /**
     * habilitar bluetooth
     */
    public synchronized void enabled() {
        if (!isEnable())
            btAdapter.enable();
    }

    /**
     * iniciar el proceso de emparejamiento
     *
     * @param btDevice
     * @param macDevice
     */

    private void createPair(BluetoothDevice btDevice, String macDevice) {
        try {
            //intenta emparejar el dispositivo
            Method bondMethod = btDevice.getClass().getMethod("createBond");
            bondMethod.invoke(btDevice);
            Log.d(TAG, "createPair: pairing...");
        } catch (Exception e) {
            Log.e(TAG, "createPair: error al emparejar " + btDevice.getName() + "-" + btDevice.getAddress(), e);
        }
    }

    private synchronized void cancelDiscoverDevices() {
        if (btAdapter.isDiscovering())
            btAdapter.cancelDiscovery();
        Log.d(TAG, "finalizando scaneo de dispositivos");
    }

    /**
     * setear el pin del dispositivo para confirmar el emparejamiento
     *
     * @param btDevice
     * @param pinDevice
     */
    private synchronized void setPinPairDevice(BluetoothDevice btDevice, String pinDevice) {
        try {
            Method pairMethod = btDevice.getClass().getMethod("setPin", new Class[]{byte[].class});
            pairMethod.invoke(btDevice, pinDevice.getBytes(Charset.forName("UTF-8")));
            // btDevice.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(btDevice, false);
            Log.e(TAG, "setPinPairDevice: pasando pin " + pinDevice + " mac:" + macDevice);
        } catch (Exception ex) {
            Log.e(TAG, "createPair  error al setear el pin al dispositivo", ex);
        }
    }

    /**
     * llamar en el metodo onCreate de la activity
     */
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(this, filter);

        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        filter2.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY - 1);
        activity.registerReceiver(this, filter2);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(this, filter);
    }

    /**
     * llamamr en el metodo onDestroy() de la activity
     */
    public synchronized void unRegisterReceiver() {
        activity.unregisterReceiver(this);
    }

    /**
     * @return lista de los dispositivos vinculados "macDevice,pin" , null si no hay dipositivos vinculados
     */
    public synchronized ArrayList<String> getBondedDevices() {
        if (btAdapter != null) {

            if (!isEnable())
                enabled();
            deviceTemporalList = new ArrayList<>();
            for (BluetoothDevice currentDevice : btAdapter.getBondedDevices()) {
                deviceTemporalList.add(currentDevice.getAddress());
            }
        }
        return deviceTemporalList;
    }

    public boolean removeBond(String macDevice) {
        for (BluetoothDevice device : btAdapter.getBondedDevices()) {
            if (device.getAddress().equals(macDevice)) {
                Log.d(TAG, "removeBond: bt encontrado, desvinculando...");
                try {
                    Method  bondMethod = device.getClass().getMethod("removeBond");
                    boolean btremoved  = (Boolean) bondMethod.invoke(device);
                    if (btremoved) {
                        return true;
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "createPair  error al setear el pin al dispositivo", ex);
                }
            }
        }
        return false;
    }

}
