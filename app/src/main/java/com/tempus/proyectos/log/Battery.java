package com.tempus.proyectos.log;

/**
 * Created by ECERNAR on 16/05/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by ECERNAR on 10/05/2017.
 */

public class Battery {

    Activity activity;

    public Battery(Activity activity) {
        this.activity = activity;
    }

    public String[] updateBatteryData(Intent intent) { // Salud, Porcentaje, Plugged, Battery Charging Status, Technology, Temperature, Voltage

        String data[] = {"","","","","","",""};

        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);

        if (present) {
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            String healthLbl = "";

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_COLD:
                    healthLbl = "battery_health_cold";
                    break;

                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthLbl = "battery_health_dead";
                    break;

                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthLbl = "battery_health_good";
                    break;

                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthLbl = "battery_health_over_voltage";
                    break;

                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthLbl = "battery_health_overheat";
                    break;

                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthLbl = "battery_health_unspecified_failure";
                    break;

                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                default:
                    break;
            }

            if (healthLbl != "") {
                // display battery health ...
                Log.v("Sensor TX","Health : " + healthLbl);
                data[0] = healthLbl;
            }

            // Calculate Battery Pourcentage ...
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                Log.v("Sensor TX","Battery Pct : " + batteryPct + " %");
                data[1] = String.valueOf(batteryPct);
            }

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            String pluggedLbl = "";

            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    pluggedLbl = "battery_plugged_wireless";
                    break;

                case BatteryManager.BATTERY_PLUGGED_USB:
                    pluggedLbl = "battery_plugged_usb";
                    break;

                case BatteryManager.BATTERY_PLUGGED_AC:
                    pluggedLbl = "battery_plugged_ac";
                    break;

                default:
                    pluggedLbl = "battery_plugged_none";
                    break;
            }

            // display plugged status ...
            Log.v("Sensor TX","Plugged : " + pluggedLbl);
            data[2] = pluggedLbl;

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            String statusLbl = "";

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    statusLbl = "battery_status_charging";
                    break;

                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    statusLbl = "battery_status_discharging";
                    break;

                case BatteryManager.BATTERY_STATUS_FULL:
                    statusLbl = "battery_status_full";
                    break;

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    statusLbl = "";
                    break;

                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                default:
                    statusLbl = "battery_status_discharging";
                    break;
            }

            if (statusLbl != "") {
                Log.v("Sensor TX","Battery Charging Status : " + statusLbl);
                data[3] = statusLbl;
            }

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);

                if (!"".equals(technology)) {
                    Log.v("Sensor TX","Technology : " + technology);
                    data[4] = technology;
                }
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            if (temperature > 0) {
                float temp = ((float) temperature) / 10f;
                Log.v("Sensor TX","Temperature : " + temp + "°C");
                data[5] = String.valueOf(temp);
            }

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

            if (voltage > 0) {
                Log.v("Sensor TX","Voltage : " + voltage + " mV");
                data[6] = String.valueOf(voltage);
            }

            long capacity = getBatteryCapacity(activity);

            if (capacity > 0) {
                Log.v("Sensor TX","Capacity : " + capacity + " mAh");
                data[7] = String.valueOf(capacity);
            }

        } else {
            Log.e("Sensor TX","No Battery present");
        }

        return data;
    }

    public long getBatteryCapacity(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
            Long chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (chargeCounter != null && capacity != null) {
                long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
                return value;
            }
        }

        return 0;
    }

}
