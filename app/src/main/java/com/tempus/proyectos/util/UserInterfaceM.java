package com.tempus.proyectos.util;

import android.app.Activity;
import android.content.Intent;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;

/**
 * Created by ecernar on 20/10/2016.
 */

public class UserInterfaceM {

    protected PowerManager.WakeLock mWakeLock;

    public void initScreen(Activity activity) {

        activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View decorView = activity.getWindow().getDecorView();

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


        final PowerManager pm = (PowerManager) activity.getSystemService(activity.getApplicationContext().POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
    }

    public void goToActivity(Activity ActivityActual,Class ActivityDestino,String llave,String valor){
        Intent intent = new Intent(ActivityActual, ActivityDestino);
        if (!llave.equals("")) {
            intent.putExtra(llave, valor);
        }
        ActivityActual.startActivityForResult(intent, 1);
        ActivityActual.finish();
    }
}
