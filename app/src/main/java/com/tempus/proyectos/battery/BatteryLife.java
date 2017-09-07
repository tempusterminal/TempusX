package com.tempus.proyectos.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.InternalFile;
import com.tempus.proyectos.util.Utilities;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by gurrutiam on 20/07/2017.
 */

public class BatteryLife {

    String TAG = "BA-BL";

    public static int isCharging;
    public static int levelBattery;
    public static int chargePlug;
    public static long capacity;
    public int usbCharge;
    public int acCharge;
    public int voltage;

    private static final String DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/tempus/";
    private static final String FILE = "batterylife.txt";
    private static final String FILE_FULL = "batterylife_full.txt";
    InternalFile internalFile = new InternalFile();
    Fechahora fechahora = new Fechahora();

    Utilities util = new Utilities();
    private byte[] rawBytes = new byte[64];
    private String statusEB;

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
            /*
                BatteryManager
                    The BatteryManager class contains strings and constants used for values in the
                    ACTION_BATTERY_CHANGED Intent, and provides a method for querying battery
                    and charging properties.
            */
            /*
                public static final String EXTRA_SCALE
                    Extra for ACTION_BATTERY_CHANGED: integer containing the maximum battery level.
                    Constant Value: "scale"
            */

            // Get the Battery status
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            //Log.v(TAG,"batteryManager.EXTRA_STATUS: " + status);

            // get BATTERY_STATUS_CHARGING

            if(status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL){
                isCharging = 1;
                ActivityPrincipal.isCharging = true;
            }else{
                isCharging = 0;
                ActivityPrincipal.isCharging = false;
            }

            //Log.v(TAG,"BatteryManager.BATTERY_STATUS_CHARGING: " + isCharging);

            // Get the battery scale
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            // Display the battery scale in TextView
            //Log.v(TAG,"BatteryManager.EXTRA_SCALE: " + (float) scale);

            /*
                public static final String EXTRA_LEVEL
                    Extra for ACTION_BATTERY_CHANGED: integer field containing the current battery
                    level, from 0 to EXTRA_SCALE.

                    Constant Value: "level"
            */
            // get the battery level
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            // Display the battery level in TextView
            //Log.v(TAG,"BatteryManager.EXTRA_LEVEL: " + level);

            // Calculate the battery charged percentage
            levelBattery = (int) (( level / (float) scale ) * 100);
            // Update the progress bar to display current battery charged percentage
            //Log.v(TAG,"percentage: " + levelBattery + "%");

            chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if(chargePlug == BatteryManager.BATTERY_PLUGGED_USB){
                usbCharge = 1;
            }else{
                usbCharge = 0;
            }
            if(chargePlug == BatteryManager.BATTERY_PLUGGED_AC){
                acCharge = 1;
            }else{
                acCharge = 0;
            }

            capacity = getBatteryCapacity();
            //Log.v(TAG,"capacity: " + capacity);


            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            //Log.v(TAG,"voltage " + voltage);


            // Show the battery charged percentage text inside progress bar
            //mTextViewPercentage.setText("" + mProgressStatus + "%");

            // Show the battery charged percentage in TextView
            //mTextViewInfo.setText(mTextViewInfo.getText() +
            //        "\nPercentage : "+ mProgressStatus + "%");

            // Display the battery charged percentage in progress bar
            //mProgressBar.setProgress(mProgressStatus);

            /*
            try{
                Calendar calendar = Calendar.getInstance();
                Toast toast = Toast.makeText(ActivityPrincipal.context,"isCharging " + isCharging + "/" + levelBattery + "%" + " - " + "usbCharge->" + usbCharge + "/" + "acCharge->" + acCharge + " - " + "voltage " + voltage + " " + calendar.getTime(), Toast.LENGTH_SHORT);
                //toast.cancel();
                toast.show();
            }catch(Exception e){
                Log.e(TAG,"BroadcastReceiver " + e.getMessage());
            }
            */




        }
    };


    private long getBatteryCapacity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) ActivityPrincipal.context.getSystemService(Context.BATTERY_SERVICE);
            Long chargeCounter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (chargeCounter != null && capacity != null) {
                long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
                return value;
            }
        }

        return 0;
    }


    private long getCapacity() {
        Object mPowerProfile_ = null;
        long batteryCapacity = -1;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class);
        } catch (Exception e) {
            Log.e(TAG,"mPowerProfile_ " + e.getMessage());
        }

        try {
            batteryCapacity = (Long) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
        } catch (Exception e) {
            Log.e(TAG,"batteryCapacity " + e.getMessage());
        }

        return batteryCapacity;
    }


    private class BatteryLifeReading extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private int levelB = -1;
        private int isC = -1;
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
                    line = fechahora.getFechahora() + ": " + "isC=" + isCharging + " " + "level=" + levelBattery + "%" + " " + "usbC=" + usbCharge + " " + "acC=" + acCharge + " " + "mV=" + voltage + " ";
                    line += "exEy=" + exEy + " " + "upsC=" + upsC + " " + "levelU=" + levelU + "%" + " " + "toLn=" + toLn + " " + "toAd=" + toAd + " " + "toSu=" + toSu + " " + "toBa=" + toBa + " " + "toPr=" + toPr + " " + "toExEy=" + toExEy;
                    //Log.v(TAG,"getBatteryLife() internalFile.writeToAppend...");
                    if(isC != isCharging || uC != upsC || toEE != toExEy || levelB != levelBattery){
                        //Log.v(TAG,"getBatteryLife() internalFile.writeToAppend... isCharging");
                        internalFile.writeToAppend(line, DIRECTORY + FILE);
                        isC = isCharging;
                        uC = upsC;
                        toEE = toExEy;
                        levelB = levelBattery;
                    }

                    /*
                    if(isC != isCharging){
                        //Log.v(TAG,"getBatteryLife() internalFile.writeToAppend... isCharging");
                        internalFile.writeToAppend(line, DIRECTORY + FILE);
                        isC = isCharging;
                    }else if(uC != upsC){
                        //Log.v(TAG,"getBatteryLife() internalFile.writeToAppend... levelBattery");
                        internalFile.writeToAppend(line, DIRECTORY + FILE);
                        uC = upsC;
                    }else if(toEE != toExEy){
                        //Log.v(TAG,"getBatteryLife() internalFile.writeToAppend... levelBattery");
                        internalFile.writeToAppend(line, DIRECTORY + FILE);
                        toEE = toExEy;
                    }else if(levelB != levelBattery){
                        //Log.v(TAG,"getBatteryLife() internalFile.writeToAppend... levelBattery");
                        internalFile.writeToAppend(line, DIRECTORY + FILE);
                        levelB = levelBattery;
                    }
                    */

                    //Log.v(TAG,"second = " + fechahora.getFechahora().substring(17,19));
                    if(Integer.valueOf(fechahora.getFechahora().substring(17,19)) == 0){
                        internalFile.writeToAppend(line, DIRECTORY + FILE_FULL);
                    }
                    Log.v(TAG,"" + line);
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
