package com.tempus.proyectos.tempusx;

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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.util.WifiReceiver;


/**
 * Created by ecernar on 03/10/2016.
 */

public class FragmentBar extends Fragment {


    int rBatteryTemp;
    int rBatteryLevel;
    boolean rIsCharging;
    boolean rUSBCharge;
    boolean rACCharge;

    Button btnRestart;

    boolean activo;

    TextView txvINivelBateria;
    ImageView imgViewIBat;
    TextView txvIdDesc;
    TextView txvIdterminal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

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

        txvIdterminal = (TextView) v.findViewById(R.id.txvIdterminal);

        btnRestart = (Button) v.findViewById(R.id.btnRestart);
        btnRestart.setVisibility(View.INVISIBLE);

        txvINivelBateria.setVisibility(View.INVISIBLE);
        imgViewIBat.setVisibility(View.INVISIBLE);

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PendingIntent intent = PendingIntent.getActivity(getActivity().getBaseContext(), 0, new Intent(getActivity().getIntent()), 0);
                    AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, intent);
                    System.exit(2);
                } catch (Exception e) {
                    Log.v("TEMPUS: ",e.getMessage());
                }

            }
        });


        //getActivity().registerReceiver(recieveObj, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        getActivity().registerReceiver(mYourBroadcastReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        //WifiReceiver receiverWifi = new WifiReceiver(getActivity());
        //IntentFilter mIntentFilter = new IntentFilter();
        //mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        //getActivity().registerReceiver(receiverWifi, mIntentFilter);


        routineIndicators.start();
        activo = true;

        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        activo = false;
        getActivity().unregisterReceiver(mYourBroadcastReceiver);
    }

    public void onPause() {
        super.onPause();
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

            } catch(Exception e) {
                Log.e("BroadcastReceiver","Bateria Error: "+e.getMessage());
            }

            txvIdterminal.setText(ActivityPrincipal.idTerminal);
        }
    };


    /* ===================================== RUTINA ROOT ===================================== */

    Thread routineIndicators = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);

                    if(activo){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fnBatteryManager();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    /* ===================================== INDICADORES ===================================== */

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
