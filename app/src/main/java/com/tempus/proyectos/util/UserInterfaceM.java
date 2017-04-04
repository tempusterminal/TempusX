package com.tempus.proyectos.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.tempusx.R;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;

/**
 * Created by ecernar on 20/10/2016.
 */

public class UserInterfaceM {

    protected PowerManager.WakeLock mWakeLock;

    public void initScreen(Activity activity) {

        activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    public void showAlert(Activity activity, String tipo, String mensaje){
        Toast toast = Toast.makeText(activity, "   "+mensaje+"   ", Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);

        v.setTextColor(Color.WHITE);
        v.setTextSize(22);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 50);

        View view = toast.getView();

        switch (tipo){
            case "info":
                view.setBackgroundResource(R.drawable.toastinfo);
                break;
            case "danger":
                view.setBackgroundResource(R.drawable.toastdanger);
                break;
            case "success":
                view.setBackgroundResource(R.drawable.toastsuccess);
                break;
            case "warning":
                view.setBackgroundResource(R.drawable.toastwarning);
                break;
            default:
                break;
        }

        toast.setView(view);
        toast.show();
    }
}
