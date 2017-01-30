package com.tempus.proyectos.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ecernar on 03/01/2017.
 */

public class Shell {

    public String[] REBOOT = {"su", "-c", "reboot"};
    public String[] DATE = {};
    public String[] TOP = {};
    public String[] IFCONFIG = {};
    public String[] UPTIME = {};
    public String[] NC = {};

    public String exec(String ... params){
        try {
            Process proc = Runtime.getRuntime().exec(params);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            proc.waitFor();
            return output.toString();
        } catch (IOException e) {
            Log.e("Error","IOException: " + e.getMessage());
            return "Error";
        } catch (InterruptedException e) {
            Log.e("Error","InterruptedException: " + e.getMessage());
            return "Error";
        }
    }

}


