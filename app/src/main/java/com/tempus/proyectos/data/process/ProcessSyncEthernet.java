package com.tempus.proyectos.data.process;

import android.os.AsyncTask;
import android.util.Log;

import com.tempus.proyectos.bluetoothSerial.MainEthernet;
import com.tempus.proyectos.tempusx.ActivityPrincipal;

/**
 * Created by gurrutiam on 23/06/2017.
 */

public class ProcessSyncEthernet extends AsyncTask<Void, Integer, Long> {

    String TAG = "BTS-MAET";


    int n = 0;


    @Override
    protected void onPreExecute() {
        Log.v(TAG,"EthernetExecute " + "onPreExecute");


    }

    @Override
    protected Long doInBackground(Void... values){
        long count = 0;

        try{
            //mainEthernet.writeData(ActivityPrincipal.btSocketEthernet.getOutputStream(),"OPCION=SUBSTR_FILE&FILENAME=" + "FILE_5941778175cc9" + "&START=" + 0 + "&LENGTH=" + 200000 + "");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.e(TAG,"EthernetExecute " + e.getMessage());
                }
            }
        }catch(Exception e){
            Log.v(TAG,"EthernetExecute " + e.getMessage());
        }
        return count;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        Log.v(TAG,"EthernetExecute " + " onProgressUpdate");
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.v(TAG,"EthernetExecute " + " onPostExecute");
        //new EthernetRead().execute();
    }






}
