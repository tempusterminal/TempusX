package com.tempus.proyectos.util;

import android.Manifest;
import android.content.Context;
import android.os.PowerManager;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import com.tempus.proyectos.tempusx.ActivityPrincipal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by gurrutiam on 18/01/2018.
 */

public class PowerAdmin {
    private static final String TAG = "UT-PWA";
    private PowerManager mPowerManager;

    public PowerAdmin() {
        mPowerManager = (PowerManager) ActivityPrincipal.context.getSystemService(Context.POWER_SERVICE);
    }

    /**
     Turn Off the device in safe mode
     @param confirm If true, shows a shutdown confirmation dialog.
     */
    public void shutdown(final boolean confirm) {

        boolean found = false;
        Method shutdow = null;
        boolean invoked = false;
        try {
            Method[] methods = mPowerManager.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals("shutdown")) {
                    found = true;
                    shutdow = method;
                    Log.v(TAG, "shutdown: invoked: " + String.valueOf(invoked));
                }
            }
            Log.v(TAG, "method: " + methods.toString());

            if (found) {
                if (confirm) {
                    invoked = (boolean) shutdow.invoke(mPowerManager, confirm, false);
                } else {
                    invoked = (boolean) shutdow.invoke(mPowerManager, confirm, true);
                }
                Log.v(TAG,"invoked: " + String.valueOf(invoked));

            } else
                Log.e(TAG, "onClick: method PowerManager.shutdown  not found: ");

        } catch (IllegalAccessException e) {
            Log.e(TAG, "shutdown: " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "shutdown: " + e.getMessage(), e);
        }

    }

    /**
     Turn Off the device in safe mode
     */
    public void shutdown() {
        this.shutdown(false);
    }

    @RequiresPermission(Manifest.permission.REBOOT)
    public void reboot() {
        mPowerManager.reboot(null);
    }
}
