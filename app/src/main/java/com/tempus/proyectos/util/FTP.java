package com.tempus.proyectos.util;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by ecernar on 09/01/2017.
 */

public class FTP {

    private String SERVIDOR;
    private String USUARIO;
    private String CONTRASEÑA;

    public String getSERVIDOR() {
        return SERVIDOR;
    }

    public void setSERVIDOR(String SERVIDOR) {
        this.SERVIDOR = SERVIDOR;
    }

    public String getUSUARIO() {
        return USUARIO;
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public String getCONTRASEÑA() {
        return CONTRASEÑA;
    }

    public void setCONTRASEÑA(String CONTRASEÑA) {
        this.CONTRASEÑA = CONTRASEÑA;
    }

    public void uploadFile(String datos){

        FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect(this.SERVIDOR);

            if (con.login(this.USUARIO, this.CONTRASEÑA)) {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                String data = "/sdcard/vivekm4a.m4a";

                FileInputStream in = new FileInputStream(new File(data));
                boolean result = con.storeFile("/vivekm4a.m4a", in);
                in.close();
                if (result) Log.v("upload result", "succeeded");
                con.logout();
                con.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(){
        FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect(this.SERVIDOR);

            if (con.login(this.USUARIO, this.CONTRASEÑA)) {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                String data = "/sdcard/vivekm4a.m4a";

                OutputStream out = new FileOutputStream(new File(data));
                boolean result = con.retrieveFile("vivekm4a.m4a", out);
                out.close();
                if (result) Log.v("download result", "succeeded");
                con.logout();
                con.disconnect();
            }
        } catch (Exception e) {
            Log.v("download result","failed");
            e.printStackTrace();
        }



    }
}
