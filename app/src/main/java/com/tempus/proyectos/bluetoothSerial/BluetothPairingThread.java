package com.tempus.proyectos.bluetoothSerial;

/**
 * Created by gurrutiam on 19/09/2017.
 */

import android.util.Log;

import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.tempusx.ActivityPrincipal;

import java.util.ArrayList;
import java.util.List;


public class BluetothPairingThread extends Thread{

    private String TAG = "BTS-PT";

    private BluetoothPair btmanager;
    private ArrayList<String>  listBonded;
    private ArrayList<String> listDevice = new ArrayList<String>();
    private java.lang.String datalog;
    private QueriesLogTerminal queriesLogTerminal = new QueriesLogTerminal();



    public BluetothPairingThread(BluetoothPair btmanager) {
        this.btmanager = btmanager;
    }

    int contador = 0;

    @Override
    public void run() {

        while(ActivityPrincipal.MAC_BT_03.equalsIgnoreCase("")){
            try {
                Log.v(TAG,"Esperando set MAC y PASS");
                Thread.sleep(1000);
            }catch (Exception e){
                Log.e(TAG,"Esperando set MAC y PASS " + e.getMessage());
            }
        }

        if(!ActivityPrincipal.MAC_BT_00.equalsIgnoreCase("00:00:00:00:00:00")){
            listDevice.add(ActivityPrincipal.MAC_BT_00 + "," + ActivityPrincipal.PASS_BT_00);
        }

        if(!ActivityPrincipal.MAC_BT_01.equalsIgnoreCase("00:00:00:00:00:00")){
            listDevice.add(ActivityPrincipal.MAC_BT_01 + "," + ActivityPrincipal.PASS_BT_01);
        }

        if(!ActivityPrincipal.MAC_BT_02.equalsIgnoreCase("00:00:00:00:00:00")){
            listDevice.add(ActivityPrincipal.MAC_BT_02 + "," + ActivityPrincipal.PASS_BT_02);
        }

        if(!ActivityPrincipal.MAC_BT_03.equalsIgnoreCase("00:00:00:00:00:00")){
            listDevice.add(ActivityPrincipal.MAC_BT_03 + "," + ActivityPrincipal.PASS_BT_03);
        }

        //ARIS 1 PULSADORES Y RELAY (DESARROLLADOR)
        //listDevice.add("00:21:13:01:7D:B9,1234");
        //listDevice.add("20:16:08:10:64:50,1234");
        //listDevice.add("20:16:08:10:65:26,1234");

        //ARIS 3
        //listDevice.add("00:21:13:01:7D:63,1234");
        //listDevice.add("20:16:09:21:91:21,1234");
        //listDevice.add("20:16:07:18:32:13,1234");

        //ARIS 4
        //listDevice.add("00:21:13:01:7F:25,1234");
        //listDevice.add("20:16:08:10:67:76,1234");
        //listDevice.add("20:16:08:10:58:54,1234");

        //ARIS 1
        //listDevice.add("00:21:13:01:7E:10,1234");
        //listDevice.add("20:16:07:14:09:92,1234");
        //listDevice.add("20:16:07:18:49:76,1234");

        //ARIS 2
        //listDevice.add("00:21:13:01:7F:1A,1234");
        //listDevice.add("20:16:06:30:84:11,1234");
        //listDevice.add("20:16:06:30:86:16,1234");

        //SHOUGANG
        //listDevice.add("00:21:13:00:CC:2D,1234");
        //listDevice.add("20:16:06:30:84:85,1234");
        //listDevice.add("20:16:09:21:44:42,1234");

        // CLINICA INTERNACIONAL - PUERTA (NUEVA VERSION)
        //listDevice.add("00:21:13:01:7E:47,1234");
        //listDevice.add("20:16:08:10:63:74,1234");
        //listDevice.add("20:16:08:10:68:89,1234");

        // CARRION ID 12
        //listDevice.add("20:17:05:23:47:80,1234");
        //listDevice.add("20:17:05:23:47:83,1234");


        while (true) {

            try{
                //dormir(3000);
                listBonded = btmanager.getBondedDevices();
                Log.v(TAG,"listBonded: " + listBonded.toString());
                if (devicesUnpaired(listDevice)) {
                    Log.v(TAG,"Intentando Vincular");
                    //lista de vinculados
                    for (int j = 0; j < listDevice.size(); j++) {
                        //actual devicedb
                        String[] currentDevice = listDevice.get(j).split(",");
                        String   currMac       = currentDevice[0];
                        String   currPin       = currentDevice[1];
                        listBonded = btmanager.getBondedDevices();
                        if (!listBonded.contains(currMac)) {
                            Log.v(TAG,"Intentando Vincular device " + currMac);
                            btmanager.pairDevice(currPin, currMac);
                            try{
                                Log.v(TAG,"queriesLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG,"Vinculando " + currMac,""));
                            }catch (Exception e){
                                Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
                            }
                            listBonded = btmanager.getBondedDevices();
                            Thread.sleep(10000);
                        }
                    }
                    Thread.sleep(1000);
                    /*
                    if (listBonded.size() <= listDevice.size()) {
                        for (int j = 0; j < listDevice.size(); j++) {
                            //actual devicedb
                            String[] currentDevice = listDevice.get(j).split(",");
                            String   currMac       = currentDevice[0];
                            String   currPin       = currentDevice[1];
                            Log.v(TAG,currMac + " - " + currPin);
                            btmanager.pairDevice(currPin, currMac);
                            listBonded = btmanager.getBondedDevices();
                            Thread.sleep(7000);
                        }
                    } else {

                        //lista de devices de la db
                        for (int j = 0; j < listDevice.size(); j++) {
                            //actual devicedb
                            String[] currentDevice = listDevice.get(j).split(",");
                            String   currMac       = currentDevice[0];
                            String   currPin       = currentDevice[1];
                            listBonded = btmanager.getBondedDevices();
                            if (!listBonded.contains(currMac)) {
                                btmanager.pairDevice(currPin, currMac);
                                listBonded = btmanager.getBondedDevices();
                                Thread.sleep(7000);
                            }
                        }
                    }
                    */
                    //listBonded = btmanager.getBondedDevices();
                } else {
                    //contador++;
                    datalog = "run: dispositivos todos vinculados";
                    Log.v(TAG, "run: dispositivos todos vinculados");
                    //Util.writeFile(TAG + " " + datalog);
                /*
                try {
                    sleep(8000);
                    if ( contador > 1) {
                        for (int i = 0; i < listBonded.size(); i++) {
                            if (!btmanager.removeBond(listBonded.get(i))) {
                                btmanager.removeBond(listBonded.get(i));
                            } else {
                                datalog = "run: bt " + listBonded.get(i) + " desvinculado de "+listBonded.size();
                                Log.d(TAG, datalog);
                                //Util.writeFile(TAG + " " + datalog);
                            }
                        }
                        contador = 0;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
                    //listBonded=btmanager.getBondedDevices();
                    Thread.sleep(1000);

                }
            }catch (Exception e){
                Log.e(TAG,"BluetothPairingThread " + e.getMessage());
            }

        }
    }

    private boolean devicesUnpaired(ArrayList<String> listDevice){

        try{
            /*
            for(int i = 0; i < listDevice.size(); i++){
                String mac = listDevice.get(i).toString().substring(0,17);
                Log.v(TAG,"listDevice(" + (i+1) + ") " + mac + " buscando");
                if(listDevice.contains(mac)){
                    Log.v(TAG,"listDevice(" + (i+1) + ") " + mac + " vinculado");
                }else{
                    return true;
                }
            }
            */
            if(listBonded.size() == 0){
                return true;
            }else{
                for(int i = 0; i < listDevice.size(); i++){
                    for(int y = 0; y < listBonded.size(); y++){
                        Log.v(TAG,"Buscando dispositivo vinculado " + listDevice.get(i).substring(0,17));
                        if(listDevice.get(i).substring(0,17).equalsIgnoreCase(listBonded.get(y))){
                            Log.v(TAG,"Dispositivo vinculado " + listDevice.get(i).substring(0,17));
                            y = listDevice.size();
                        }else{
                            Log.v(TAG,"Dispositivo NO vinculado " + listDevice.get(i).substring(0,17));
                            return true;
                        }
                    }
                }
            }

        }catch (Exception e){
            Log.e(TAG,"devicesUnpaired " + e.getMessage());
        }
        return false;
    }

}
