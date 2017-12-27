package com.tempus.proyectos.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.InternalFile;
import com.tempus.proyectos.util.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Calendar;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by gurrutiam on 20/07/2017.
 */

public class BatteryLife {

    String TAG = "BA-BL";

    public int levelBattery;
    public long capacity;
    public int currentBattery;
    public int voltage;
    public int healthBattery;
    public int statusBattery;
    public int plugged;
    public int temperature;

    private static final String DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/tempus/";
    private static final String FILE = "batterylife.txt";
    private static final String FILE_FULL = "batterylife_full.txt";
    InternalFile internalFile = new InternalFile();
    Fechahora fechahora = new Fechahora();
    private String logterminal = "";


    public void getBatteryLife(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        ActivityPrincipal.context.registerReceiver(mBroadcastReceiver,intentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get the battery scale
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

            // get the battery level
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            // Display the battery level in TextView
            //Log.v(TAG,"BatteryManager.EXTRA_LEVEL: " + level);

            // Calculate the battery charged percentage
            levelBattery = (int) (( level / (float) scale ) * 100);
            // Update the progress bar to display current battery charged percentage
            //Log.v(TAG,"percentage: " + levelBattery + "%");

            try{
                currentBattery = Integer.valueOf(readFile("/sys/class/power_supply/battery/BatteryAverageCurrent"));
            }catch (Exception e){
                currentBattery = -1;
                Log.v(TAG,"currentBattery " + e.getMessage());
            }

            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            //Log.v(TAG,"voltage " + voltage);

            //BATTERY_HEALTH_COLD = 7;
            //BATTERY_HEALTH_DEAD = 4;
            //BATTERY_HEALTH_GOOD = 2;
            //BATTERY_HEALTH_OVERHEAT = 3;
            //BATTERY_HEALTH_OVER_VOLTAGE = 5;
            //BATTERY_HEALTH_UNKNOWN = 1;
            //BATTERY_HEALTH_UNSPECIFIED_FAILURE = 6;
            healthBattery = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            //Log.v(TAG,"healthBattery " + healthBattery);

            //BATTERY_STATUS_CHARGING = 2;
            //BATTERY_STATUS_DISCHARGING = 3;
            //BATTERY_STATUS_FULL = 5;
            //BATTERY_STATUS_NOT_CHARGING = 4;
            //BATTERY_STATUS_UNKNOWN = 1;
            statusBattery = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            //Log.v(TAG,"statusBattery " + statusBattery);

            //BATTERY_PLUGGED_AC = 1;
            //BATTERY_PLUGGED_USB = 2;
            //BATTERY_PLUGGED_WIRELESS = 4;
            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            //Log.v(TAG,"plugged " + plugged);

            //current battery temperature in tenths of a degree Centigrade
            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            //Log.v(TAG,"temperature " + temperature);

        }
    };

    private String readFile(String filename){
        String linea = "";

        try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String tempLinea = "";
            while((tempLinea = br.readLine()) != null){
                try{
                    linea = tempLinea;
                    //Log.v(TAG,"linea = " + "<<<" + linea + ">>>");
                }catch (Exception e){
                    Log.e(TAG,"linea " + e.getMessage());
                }
            }

            fr.close();
        }catch (Exception e){
            Log.e(TAG,"readFile " + e.getMessage());
        }

        return linea;
    }

    private class BatteryLifeReading extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private int lB = -1;
        private int sB = -1;
        private int pl = -1;
        private double uC = -1;
        private int toEE = -1;
        private String line = "";

        /* --- ADC --- */
        private double exEy = -1;
        private double upsC = -1;
        private double levelU = -1;
        private int toLn = -1;
        private int toAd = -1;
        private int toSu = -1;
        private int toBa = -1;
        private int toPr = -1;
        private int toExEy = -1;

        private DecimalFormat decimalFormat = new DecimalFormat("#.00");
        //btnUsbMarcas.setText(decimalFormat.format(ProcessSyncUSB.lfilenamemarcaciones / 1000) + " KB");

        QueriesLogTerminal queriesLogTerminal = new QueriesLogTerminal();
        QueriesParameters queriesParameters = new QueriesParameters(ActivityPrincipal.context);

        public BatteryLifeReading(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void getStatusADC(){
            exEy = Double.valueOf(decimalFormat.format(ActivityPrincipal.ExternalEnergy).replace(",","."));
            upsC = Double.valueOf(decimalFormat.format(ActivityPrincipal.UPSCharge).replace(",","."));
            levelU = Double.valueOf(decimalFormat.format(ActivityPrincipal.levelUPS).replace(",","."));
            toLn = ActivityPrincipal.turnOnLan;
            toAd = ActivityPrincipal.turnOnAndroid;
            toSu = ActivityPrincipal.turnOnSuprema;
            toBa = ActivityPrincipal.turnOnLectorBarra;
            toPr = ActivityPrincipal.turnOnProximidad;
            toExEy = ActivityPrincipal.turnOnExternalEnergy;
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
            try{
                //line = fechahora.getFechahora() + ": " + "isC=" + isCharging + " " + "level=" + levelBattery + "%" + " " + "usb=" + usbCharge + " " + "ac=" + acCharge + " " + "mV=" + voltage + " ";
                //line += "exEy=" + exEy + " " + "upsC=" + upsC + " " + "levelU=" + levelU + "%" + " " + "toLn=" + toLn + " " + "toAd=" + toAd + " " + "toSu=" + toSu + " " + "toBa=" + toBa + " " + "toPr=" + toPr + " " + "toExEy=" + toExEy;
                //getBatteryLife();
                internalFile.writeToAppend(fechahora.getFechahora() + ": " + ">>>>>>>>>>>>> Iniciado Log ---------------", DIRECTORY + FILE);
                //internalFile.writeToAppend(line + voltage, DIRECTORY + FILE);
                //isC = isCharging;
                //levelB = levelBattery;

                internalFile.writeToAppend(fechahora.getFechahora() + ": " + ">>>>>>>>>>>>> Iniciado Log ---------------", DIRECTORY + FILE_FULL);
                //internalFile.writeToAppend(line, DIRECTORY + FILE_FULL);

            }catch (Exception e){
                Log.e(TAG,"BatteryLifeReading run " + e.getMessage());
            }

            while(true){
                try{
                    getBatteryLife();
                    getStatusADC();
                    line = "sB=" + statusBattery + " " + "lB=" + levelBattery + " " + "pl=" + plugged + " " + "mV=" + voltage + " " + "mAh=" + currentBattery + " " + "hB=" + healthBattery + " " + "T=" + temperature + " ";
                    line += "exEy=" + exEy + " " + "upsC=" + upsC + " " + "levelU=" + levelU + " " + "toLn=" + toLn + " " + "toAd=" + toAd + " " + "toSu=" + toSu + " " + "toBa=" + toBa + " " + "toPr=" + toPr + " " + "toExEy=" + toExEy;
                    String s = "|";
                    logterminal = statusBattery + s + levelBattery + s + plugged + s + voltage + s + currentBattery + s + healthBattery + s + temperature + s;
                    logterminal += exEy + s + upsC + s + levelU + s + toLn + s + toAd + s + toSu + s + toBa + s + toPr + s + toExEy;
                    //Log.v(TAG,"getBatteryLife() internalFile.writeToAppend...");
                    if(sB != statusBattery || uC != upsC || toEE != toExEy || lB != levelBattery){
                        if(sB != statusBattery || toEE != toExEy || lB != levelBattery){
                            try{
                                Log.v(TAG,"queriesLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG, logterminal, ""));
                            }catch (Exception e){
                                Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
                            }
                        }
                        //Log.v(TAG,"getBatteryLife() internalFile.writeToAppend... isCharging");
                        internalFile.writeToAppend(fechahora.getFechahora() + ": " + line, DIRECTORY + FILE);
                        sB = statusBattery;
                        uC = upsC;
                        toEE = toExEy;
                        lB = levelBattery;
                    }

                    //Log.v(TAG,"second = " + fechahora.getFechahora().substring(17,19));
                    if(Integer.valueOf(fechahora.getFechahora().substring(17,19)) == 0){
                        internalFile.writeToAppend(fechahora.getFechahora() + ": " + line, DIRECTORY + FILE_FULL);
                    }
                    Log.v(TAG,fechahora.getFechahora() + ": " + line);

                    if(levelBattery <= 30 && levelBattery > 0 && statusBattery == 4){
                        try{
                            Log.v(TAG,"queriesLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG,"Apagando " + logterminal,""));
                        }catch (Exception e){
                            Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
                        }

                        try {
                            Log.v(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: APAGAR");
                            Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                            proc.waitFor();
                        } catch (Exception e) {
                            Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: APAGAR -> " + e.getMessage());
                        }
                    }

                    // Si statusBattery = 4 (no cargando) o statusBattery = 1 (no cargando)
                    if(statusBattery == 4 || statusBattery == 1){
                        ActivityPrincipal.isCharging = false;
                    }else{
                        ActivityPrincipal.isCharging = true;
                    }

                    Thread.sleep(1000);
                }catch (Exception e){
                    Log.e(TAG,"Error General Hilo " + nombreHilo + ": " + e.getMessage());
                    try{
                        Thread.sleep(1000);
                    }catch(Exception ex){

                    }

                }
            }
        }

        public void start(){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }

    public void startBatteryLifeReading(){
        BatteryLifeReading batteryLifeReading = new BatteryLifeReading("BatteryLifeReading");
        batteryLifeReading.start();
    }

}
