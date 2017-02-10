package com.tempus.proyectos.crash;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Shell;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ecernar on 09/02/2017.
 */

public class TXExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public TXExceptionHandler(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = new Intent(activity, ActivityPrincipal.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(TXApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) TXApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        Log.e("LOG",ex.getMessage());
        Log.e("LOG",ex.toString());
        Date date = new Date();
        DateFormat fechaHora = new SimpleDateFormat("HHmmssddMMyyyy");
        String convertido = fechaHora.format(date);
        String []datos = {"su","-c","echo",ex.toString(),">","/storage/emulated/0/tempus/crash_"+convertido+".log"};
        Shell shell = new Shell();
        shell.exec(datos);
        System.exit(1);
    }

}
