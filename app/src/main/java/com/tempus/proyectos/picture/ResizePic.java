package com.tempus.proyectos.picture;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tempus.proyectos.util.Fechahora;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by gurrutiam on 23/08/2017.
 */

public class ResizePic {

    private static final String TAG = "TX-PIC";
    private final CharSequence EXTENSION_IMAGES_JPEG = ".jpeg";
    private final CharSequence EXTENSION_IMAGES_JPG = ".jpg";
    private final CharSequence EXTENSION_IMAGES_PNG = ".png";
    private final CharSequence EXTENSION_IMAGES_WEBP = ".webp";
    private final File pathDirectory;
    private File currentFileImage = null;
    private File fileImage;
    private String nameImage;
    private Bitmap bitmap;
    private Matrix matrix;
    private int oldheight, oldwidht, resto;
    private float scalewidht;
    private float scaleheight;

    private ResizeAllPictures resizeAllPictures;

    private String folderResize;

    public ResizePic ( File pathDirectory ) {
        this.pathDirectory = pathDirectory;
    }

    public Bitmap searchPhoto ( String nameImage, int reqwidht, int reqheight ) {
        this.currentFileImage = searchInLocalStorageImage2( this.pathDirectory, nameImage );
        if ( currentFileImage != null ) {
            this.bitmap = decodeBitmapFromFile( currentFileImage, reqwidht, reqheight );
            float sizeKB = ( bitmap.getByteCount() / 1024 );
            Log.d( TAG, "onClick: sizeBitmap " + sizeKB + " KB" );
            saveImageResize( getFolderResize(), nameImage, bitmap );
            this.bitmap = reScalebitmap( bitmap, reqwidht, reqheight );
            sizeKB = ( bitmap.getByteCount() / 1024 );
            Log.d( TAG, "onClick: sizeBitmap " + sizeKB + " KB" );
        }
        return this.bitmap;
    }

    private Bitmap reScalebitmap ( Bitmap bitmap, int reqwidht, int reqheight ) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ( ( float ) reqwidht ) / width;
        float scaleHeight = ( ( float ) reqheight ) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale( scaleWidth, scaleHeight );

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap( bitmap, 0, 0, width, height, matrix, false );
        bitmap.recycle();
        return resizedBitmap;
    }

    //<editor-fold  desc="searchInLocalStorageImage" >
    private File searchInLocalStorageImage ( @NonNull File pathDirectory, @NonNull String nameImageToSearch ) {
        if ( pathDirectory != null || ( nameImageToSearch != null || !nameImageToSearch.equals( "" ) ) ) {

            File listFiles[] = pathDirectory.listFiles();
            if ( listFiles != null ) {
                try {
                    if ( !pathDirectory.exists() ) {
                        pathDirectory.mkdirs();
                    } else {
                        if ( listFiles != null && listFiles.length > 0 ) {
                            currentFileImage = null;
                            for ( int i = 0; i < listFiles.length; i++ ) {
                                if ( listFiles[ i ].isDirectory() ) {
                                    searchInLocalStorageImage( listFiles[ i ], nameImageToSearch );
                                } else {
                                    fileImage = listFiles[ i ];
                                    nameImage = fileImage.getName();
                                    if ( nameImage.contains( nameImageToSearch ) || nameImage.equals( nameImageToSearch ) ) {
                                        currentFileImage = fileImage;
                                    }
                                }
                            }
                            if ( currentFileImage == null || currentFileImage.equals( "" ) )
                                Log.e( TAG, "searchInLocalStorageImage: " + nameImageToSearch + " not found" );
                        } else {
                            Log.e( TAG, "searchInLocalStorageImage -> not found images in: " + pathDirectory.getAbsolutePath() );
                        }
                    }
                } catch ( SecurityException e ) {
                    Log.e( TAG, "searchInLocalStorageImage -> " + e.getMessage() );
                }
            } else {
                Log.e( TAG, "searchInLocalStorageImage: no hay archivo en " + pathDirectory );
            }
        } else {
            Log.e( TAG, "searchInLocalStorageImage: algunos parametros estan vacios" );
        }
        return currentFileImage;
    }

    private File searchInLocalStorageImage2 ( @NonNull File pathDirectory, @NonNull String nameImageToSearch ) {
        if ( pathDirectory != null ||  nameImageToSearch != null ) {
            if ( !pathDirectory.exists() ) {
                pathDirectory.mkdirs();
            } else {
                File listFiles[] = pathDirectory.listFiles();
                if ( listFiles.length>0 ) {
                    try {
                        currentFileImage = null;
                        for ( int i = 0; i < listFiles.length; i++ ) {
                            if ( listFiles[ i ].isDirectory() ) {
                                searchInLocalStorageImage( listFiles[ i ], nameImageToSearch );
                            } else {
                                fileImage = listFiles[ i ];
                                nameImage = fileImage.getName();
                                if ( nameImage.contains( nameImageToSearch ) || nameImage.equals( nameImageToSearch ) ) {
                                    currentFileImage = fileImage;
                                }
                            }
                        }
                        if ( currentFileImage == null || currentFileImage.equals( "" ) )
                            Log.e( TAG, "searchInLocalStorageImage: " + nameImageToSearch + " not found" );

                    } catch ( SecurityException e ) {
                        Log.e( TAG, "searchInLocalStorageImage -> " + e.getMessage() );
                    }
                } else {
                    Log.e( TAG, "searchInLocalStorageImage: no hay archivo en " + pathDirectory );
                }
            }
        } else {
            Log.e( TAG, "searchInLocalStorageImage: algunos parametros estan vacios" );
        }
        return currentFileImage;
    }

    private Bitmap decodeBitmapFromFile ( File fileToConvert, int reqwidht, int reqheight ) {
        String path = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        if ( fileToConvert != null ) {
            path = fileToConvert.getAbsolutePath();
            options.inJustDecodeBounds = true;
            try {

                BitmapFactory.decodeFile( path, options );
                Log.d( TAG, "decodeBitmapFromFile: height " + options.outHeight + " widht " + options.outWidth );
                options.inSampleSize = calculateInSampleSize2( options, reqwidht, reqheight );
                Log.d( TAG, "decodeBitmapFromFile: newheight " + options.outHeight + " newwidht " + options.outWidth );
            } catch ( Exception e ) {
                Log.e( TAG, "convertFileToBitMap -> " + e.getMessage() );
            }
        } else {
            Log.i( TAG, "convertFileToBitMap: parameter fileToConvert is null" );
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile( path, options );
    }

    private int calculateInSampleSize2 ( BitmapFactory.Options options, int reqWidth, int reqHeight ) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if ( width > reqWidth || height > reqHeight ) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            while ( halfWidth / inSampleSize >= reqWidth && halfHeight / inSampleSize >= reqHeight ) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    //</editor-fold>

    private int calculateInSampleSize ( BitmapFactory.Options options, int reqWidth, int reqHeight ) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if ( width > reqWidth || height > reqHeight ) {
            int halfWidth = width - 500;
            int halfHeight = height - 500;
            while ( halfWidth / inSampleSize >= reqWidth && halfHeight / inSampleSize >= reqHeight ) {
                inSampleSize += 1;
            }
        }
        return inSampleSize;
    }

    public void saveImageResize ( String pathfolder, String filename, Bitmap bitmap ) {
        File imagen;

        OutputStream outputStream;
        try {
            imagen = new File( pathfolder + filename );
            outputStream = new FileOutputStream( imagen );
            bitmap.compress( getCompressFormatImage( filename ), 100, outputStream );
            outputStream.flush();
            outputStream.close();
            Log.i( TAG, "saveImageInStorageLocal -> saved pic in: " + imagen.getAbsolutePath() );
        } catch ( FileNotFoundException e ) {
            Log.e( TAG, e.getMessage() );
        } catch ( IOException e ) {
            Log.e( TAG, e.getMessage() );
        }
    }

    public String getFolderResize () {
        return folderResize;
    }

    public void setFolderResize ( String folderResize ) {
        this.folderResize = folderResize;
    }

    private Bitmap.CompressFormat getCompressFormatImage ( String nameImage ) {
        Bitmap.CompressFormat formatCompression = null;
        if ( nameImage.contains( EXTENSION_IMAGES_JPEG ) )
            formatCompression = Bitmap.CompressFormat.JPEG;
        if ( nameImage.contains( EXTENSION_IMAGES_JPG ) )
            formatCompression = Bitmap.CompressFormat.JPEG;
        if ( nameImage.contains( EXTENSION_IMAGES_PNG ) )
            formatCompression = Bitmap.CompressFormat.PNG;
        if ( nameImage.contains( EXTENSION_IMAGES_WEBP ) )
            formatCompression = Bitmap.CompressFormat.WEBP;
        if ( formatCompression == null )
            new Exception( "invalid image format" );

        return formatCompression;
    }





    public class ResizeAllPictures extends Thread{
        private Thread hilo;
        private String nombreHilo;


        public ResizeAllPictures(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){

            try{
                File filemarcaciones = new File(Environment.getExternalStoragePublicDirectory("") + "/tempus/img/marcaciones/local/original/"); // /storage/emulated/0/
                ResizePic resizePicmarcaciones = new ResizePic(filemarcaciones);
                resizePicmarcaciones.setFolderResize(Environment.getExternalStoragePublicDirectory("") + "/tempus/img/marcaciones/local/resize/");
                String[] filesarraymarcaciones;
                int widhtmarcaciones = 80;
                int heightmarcaciones = 60;

                Fechahora fechahora = new Fechahora();

                while(true){
                    try{


                        filesarraymarcaciones = filemarcaciones.list(new FilenameFilter(){
                            @Override
                            public boolean accept(File directory, String fileName) {
                                if (!fileName.equalsIgnoreCase("Thumbs.db")) {
                                    return true;
                                }
                                return false;
                            }
                        });
                        Log.v(TAG,"filesarraymarcaciones (" + filesarraymarcaciones.length + ")");

                        if (filesarraymarcaciones == null) {
                            Log.v(TAG, "Sin archivos marcaciones por redimensionar " + "(" + filesarraymarcaciones.length + ")");
                        }else if(filesarraymarcaciones.length > 0) {
                            for (int i = 0; i < filesarraymarcaciones.length; i++) {
                                //El archivo por redimensionar es mayor a segundos, guardar en la lista de archivos por enviar
                                //201708231325380004
                                if ((Long.parseLong(fechahora.getFechahoraName()) - Long.parseLong(filesarraymarcaciones[i].substring(0, 14))) > 10) {
                                    resizePicmarcaciones.searchPhoto(filesarraymarcaciones[i],widhtmarcaciones,heightmarcaciones);
                                    Thread.sleep(500);
                                    if(new File(Environment.getExternalStoragePublicDirectory("") + "/tempus/img/marcaciones/local/original/" + filesarraymarcaciones[i]).delete()){
                                       Log.v(TAG,"archivo " + filesarraymarcaciones[i] + " eliminado");
                                    }else{
                                        Log.v(TAG,"archivo " + filesarraymarcaciones[i] + " NO eliminado");
                                    }
                                }
                            }
                        }

                        Thread.sleep(10000);

                    }catch (Exception e){
                        Log.e(TAG,"ResizeAllPictures " + e.getMessage());
                        try{
                            Thread.sleep(10000);
                        }catch (Exception ex){

                        }
                    }

                }

            }catch (Exception e){

            }


        }

        public void start(){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);


            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }

    public void startResizeAllPictures(){
        resizeAllPictures = new ResizeAllPictures("startResizeAllPictures");
        resizeAllPictures.start();
    }




}
