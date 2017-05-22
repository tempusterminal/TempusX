package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.log.Battery;
import com.tempus.proyectos.log.LogManager;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.InternalFile;
import com.tempus.proyectos.util.Utilities;
import com.tempus.proyectos.util.WifiReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * Created by ecernar on 03/10/2016.
 */

public class FragmentBar extends Fragment {

    LogManager logManager;
    Fechahora fechahora;
    static final String DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/tempus/";
    static final String FILE = "log.txt";
    Utilities utilities;


    int rBatteryTemp;
    int rBatteryLevel;
    boolean rIsCharging;
    boolean rUSBCharge;
    boolean rACCharge;

    boolean activo;

    TextView txvINivelBateria;
    ImageView imgViewIBat;
    TextView txvIdDesc;
    TextView txvIdterminal;
    ImageView imgViewReplica;

    Battery battery;


    int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        utilities = new Utilities();
        battery = new Battery(getActivity());
        logManager = new LogManager();
        fechahora = new Fechahora();

        rBatteryTemp = 0;
        rBatteryLevel = 0;
        rIsCharging = false;
        rUSBCharge = false;
        rACCharge = false;

        View v;
        v = inflater.inflate(R.layout.fragment_bar, container, false);

        //txvIdDesc = (TextView) v.findViewById(R.id.txvIdDesc);
        //txvIdDesc.setText(dbManager.valexecSQL("SELECT IDTERMINAL FROM TERMINAL"));

        txvINivelBateria = (TextView) v.findViewById(R.id.txvINivelBateria);
        imgViewIBat = (ImageView) v.findViewById(R.id.imgViewIBat);
        imgViewReplica = (ImageView) v.findViewById(R.id.imgViewReplica);

        txvIdterminal = (TextView) v.findViewById(R.id.txvIdterminal);

        txvINivelBateria.setVisibility(View.INVISIBLE);
        imgViewIBat.setVisibility(View.INVISIBLE);

        //getActivity().registerReceiver(recieveObj, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        getActivity().registerReceiver(mYourBroadcastReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        //WifiReceiver receiverWifi = new WifiReceiver(getActivity());
        //IntentFilter mIntentFilter = new IntentFilter();
        //mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        //getActivity().registerReceiver(receiverWifi, mIntentFilter);

        activo = true;


        Thread routineIndicators = new Thread(new Runnable() {
            @Override
            public void run() {

                while (activo) {
                    try {
                        Thread.sleep(1000);

                        Thread t = Thread.currentThread();
                        long id = t.getId();
                        Log.d("PID THREAD", String.valueOf(id) + " isreplicating: " + ActivityPrincipal.isReplicating);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fnBatteryManager();
                                fnReplicaManager(ActivityPrincipal.isReplicating);
                            }
                        });

                        Log.d("FragmentBar", "Ciclo");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Thread.currentThread().interrupt();

                Log.d("PID THREAD", "DETENIENDO HILO " + Thread.currentThread().getId());


            }
        });

        routineIndicators.start();

        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        getActivity().unregisterReceiver(mYourBroadcastReceiver);
        activo = false;
        Log.e("FragmentBar","ONDESTROY");
    }

    public void onPause() {
        super.onPause();
        Log.e("FragmentBar","ONPAUSE");
    }

    public void onResume() {
        super.onResume();
        Log.e("FragmentBar","ONRESUME");
    }


    BroadcastReceiver mYourBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Bateria
            try {
                rBatteryTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                rBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                rIsCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;
                int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                rUSBCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                rACCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

                if (rIsCharging || rUSBCharge) {
                    ActivityPrincipal.isCharging = true;
                } else {
                    ActivityPrincipal.isCharging = false;
                }

                String fh = fechahora.getFechahora();
                String[] data = battery.updateBatteryData(intent);
                String BATTERY_HEALTH = data[0];
                String BATTERY_PERCENTAGE = data[1];
                String BATTERY_PLUGGED = data[2];
                String BATTERY_CHARGING_STATUS = data[3];
                String BATTERY_TEMPERATURE = data[5];
                String BATTERY_VOLTAGE = data[6];
//
                String cadena = fh + ":" + BATTERY_HEALTH + "~" + BATTERY_PERCENTAGE + "~" + BATTERY_PLUGGED
                        + "~" + BATTERY_CHARGING_STATUS  + "~" + BATTERY_TEMPERATURE
                        + "~" + BATTERY_VOLTAGE + "\n";
//
                //logManager.RegisterLogTXT(cadena);

                writeToFile(DIRECTORY,FILE,cadena);

                ADC();

            } catch(Exception e) {
                Log.e("BroadcastReceiver","Bateria Error: "+e.getMessage());
            }

            txvIdterminal.setText(ActivityPrincipal.idTerminal);
        }
    };

    public void writeToFile(String directory, String filename, String data ){
        File out;
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;

        out = new File(new File(directory), filename);

        if ( out.exists() == false ){
            try {
                out.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            outStream = new FileOutputStream(out, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        outStreamWriter = new OutputStreamWriter(outStream);

        try {
            outStreamWriter.append(data);
            outStreamWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ADC() {
        Log.v("ADC", "Enviando ... ");
        try {
            ActivityPrincipal.btSocket01.getOutputStream().write(utilities.hexStringToByteArray("244F4158410013440000000000000000000041"));
        } catch (IOException e) {
            Log.e("ADC", "Error: " + e.getMessage());
        }
    }


    /* ===================================== RUTINA ROOT ===================================== */



    /* ===================================== INDICADORES ===================================== */

    public void fnReplicaManager(boolean resultado) {
        if (resultado) {

            i += i;

            if (i == 1) {
                int imageresource = getResources().getIdentifier("@drawable/rep1", "drawable", getActivity().getPackageName());
                imgViewReplica.setImageResource(imageresource);
            } else if (i == 2){
                int imageresource = getResources().getIdentifier("@drawable/rep2", "drawable", getActivity().getPackageName());
                imgViewReplica.setImageResource(imageresource);
            } else if (i == 3){
                int imageresource = getResources().getIdentifier("@drawable/rep3", "drawable", getActivity().getPackageName());
                imgViewReplica.setImageResource(imageresource);
            } else if (i == 4){
                int imageresource = getResources().getIdentifier("@drawable/rep4", "drawable", getActivity().getPackageName());
                imgViewReplica.setImageResource(imageresource);
                i = 0;
            }

        } else {
            int imageresource = getResources().getIdentifier("@drawable/rep0", "drawable", getActivity().getPackageName());
            imgViewReplica.setImageResource(imageresource);
            i = 0;
        }
    }

    public void fnBatteryManager(){

        Log.v("LOG APP: ",rBatteryTemp +"-"+ rBatteryLevel +"-"+ rIsCharging +"-"+ rUSBCharge +"-"+ rACCharge);

        txvINivelBateria.setText(String.valueOf(rBatteryLevel)+"%");

        //Log.v("TEMPUS: ",String.valueOf(rBatteryLevel)+"%");

        /*
        try {
            if (rIsCharging || rUSBCharge) {
                int imageresource = getResources().getIdentifier("@drawable/b5", "drawable", getActivity().getPackageName());
                imgViewIBat.setImageResource(imageresource);
            } else {
                if (rBatteryLevel == 100){
                    int imageresource = getResources().getIdentifier("@drawable/b4", "drawable", getActivity().getPackageName());
                    imgViewIBat.setImageResource(imageresource);
                } else {
                    if (rBatteryLevel >= 75 && rBatteryLevel <= 99){
                        int imageresource = getResources().getIdentifier("@drawable/b3", "drawable", getActivity().getPackageName());
                        imgViewIBat.setImageResource(imageresource);
                    } else {
                        if (rBatteryLevel >= 50 && rBatteryLevel <= 74){
                            int imageresource = getResources().getIdentifier("@drawable/b2", "drawable", getActivity().getPackageName());
                            imgViewIBat.setImageResource(imageresource);
                        } else {
                            if (rBatteryLevel >= 25 && rBatteryLevel <= 49){
                                int imageresource = getResources().getIdentifier("@drawable/b1", "drawable", getActivity().getPackageName());
                                imgViewIBat.setImageResource(imageresource);
                            } else {
                                if (rBatteryLevel >= 0 && rBatteryLevel <= 24){
                                    int imageresource = getResources().getIdentifier("@drawable/b0", "drawable", getActivity().getPackageName());
                                    imgViewIBat.setImageResource(imageresource);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("FALLA",e.getMessage());
        }

        */
    }


}
