package com.tempus.proyectos.crash;

import android.app.Application;
import android.content.Context;

/**
 * Created by ecernar on 09/02/2017.
 */

public class TXApplication extends Application {
    public static TXApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
    public static TXApplication getInstance() {
        return instance;
    }
}
