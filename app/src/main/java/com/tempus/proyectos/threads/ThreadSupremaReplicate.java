package com.tempus.proyectos.threads;

import android.app.Activity;

import com.tempus.proyectos.util.Utilities;

/**
 * Created by ecernar on 09/01/2017.
 */

public class ThreadSupremaReplicate implements Runnable {

    Utilities util;
    private Activity activity;
    Boolean isReplicTime;

    public ThreadSupremaReplicate(Activity activity){
        util = new Utilities();
        this.activity = activity;
        isReplicTime = false;
    }
    @Override
    public void run() {
        while (true){
            if (true){

            }
            util.sleep(1000);
        }
    }
}
