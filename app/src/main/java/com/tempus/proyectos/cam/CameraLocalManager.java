package com.tempus.proyectos.cam;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.*;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tempus.proyectos.tempusx.ActivityPrincipal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.util.Calendar;

/**
 * Created by gurrutiam on 07/08/2017.
 */

public class CameraLocalManager {

    private String TAG = "CA-CLM";

    private String filename;
    public static String rutalocal;
    //private File file;

    public CameraLocalManager() {
        rutalocal = Environment.getExternalStoragePublicDirectory("") + "/tempus/img/marcaciones/local/original/";
        //file = new File(rutalocal);
        //file.mkdirs();
        Log.v(TAG,"rutalocal " + rutalocal);
    }

    public void takePhotoFrontCamera(String fechahora, String idterminal){
        filename = fechahora + idterminalForFilename(idterminal);

        // Obtener Parametros de la Camara y set para girar la camara
        Camera.Parameters parameters = ActivityPrincipal.camera.getParameters();
        parameters.setRotation(180);
        ActivityPrincipal.camera.setParameters(parameters);

        try{
            ActivityPrincipal.camera.setPreviewDisplay(ActivityPrincipal.surfaceView.getHolder());
        }catch(IOException e){
            Log.v(TAG,"Error setPreviewDisplay: " + e.getMessage());
        }
        //camera.startFaceDetection();
        ActivityPrincipal.camera.startPreview();
        ActivityPrincipal.camera.takePicture(null, null,pictureCallback);
    }

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream fileOutputStream = null;
            try{
                fileOutputStream = new FileOutputStream(rutalocal + filename + ".jpg");
                fileOutputStream.write(data);
                fileOutputStream.close();
            }catch (FileNotFoundException e){
                Log.v(TAG,"Error FileNotFoundException:" + e.getMessage());
            }catch (IOException e){
                Log.v(TAG,"Error IOException:" + e.getMessage());
            }finally {
                //camera.startPreview();
                //camera.release();
            }
            Log.v(TAG,"Picture: " + rutalocal + filename + ".jpg");
        }
    };

    private String idterminalForFilename(String idterminal){
        //103
        for(int i = idterminal.length(); i < 4; i++ ){
            idterminal = "0" + idterminal;
        }

        return idterminal;
    }


}
