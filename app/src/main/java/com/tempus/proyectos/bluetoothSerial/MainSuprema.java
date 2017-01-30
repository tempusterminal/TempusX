package com.tempus.proyectos.bluetoothSerial;

/**
 * Created by ecernar on 31/10/2016.
 */

import android.util.Log;

import com.tempus.proyectos.util.*;

import java.io.IOException;
import java.io.OutputStream;


public class MainSuprema {

    Utilities util = new Utilities();

    private String trama;

    private String Cabecera;
    private String Comando;
    private String Parametro;
    private String Tamano;
    private String Flag;
    private String Checksum;
    private String Cola;

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

    private String Data;

    public String getCabecera() {
        return Cabecera;
    }

    public void setCabecera(String cabecera) {
        Cabecera = cabecera;
    }

    public String getComando() {
        return Comando;
    }

    public void setComando(String comando) {
        Comando = comando;
    }

    public String getParametro() {
        return Parametro;
    }

    public void setParametro(String parametro) {
        Parametro = parametro;
    }

    public String getTamano() {
        return Tamano;
    }

    public void setTamano(String tamano) {
        Tamano = tamano;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getChecksum() {
        return Checksum;
    }

    public void setChecksum(String checksum) {
        Checksum = checksum;
    }

    public String getCola() {
        return Cola;
    }

    public void setCola(String cola) {
        Cola = cola;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public void limpiarTramaSuprema() {
        this.Cabecera = "";
        //this.trama = "";
        this.Comando = "";
        this.Parametro = "";
        this.Tamano = "";
        this.Flag = "";
        this.Checksum = "";
        this.Cola = "";
        this.Data = "";
    }

    public void estructurarTramaSupremaGeneral() {
        this.Cabecera = this.trama.substring(0,2);
        this.Comando = this.trama.substring(2,4);
        this.Parametro = this.trama.substring(4,12);
        this.Tamano = this.trama.substring(12,20);
        this.Flag = this.trama.substring(20,22);
        this.Checksum = this.trama.substring(22,24);
        this.Cola = this.trama.substring(24,26);
        this.Data = this.trama.substring(26,this.trama.length());
    }

    public String addChecksum(String trama) {
        int suma = 0;

        int[] input = new int[11];
        byte[] array = new byte[11];

        array = util.hexStringToByteArray(trama.substring(0,22));
        input = util.byteArrayToIntArray(array);

        for ( int i = 0; i < array.length; i++ ) {
            suma = suma + (int)input[i];
        }

        int resultado = suma % 256;
        String pretrama = Integer.toHexString(resultado);

        if(pretrama.length() < 2) {
            pretrama = "0"+pretrama;
        }
        // Add checksum trama
        return trama.substring(0,22) + pretrama + trama.substring(24,trama.length());
    }

    public void addSize() {
        int lenTempla = this.trama.length();
        int lenTemplaResult = lenTempla/2;

        String hexalen = Integer.toHexString(lenTemplaResult);
        String []sizeParams = new String[2];

        if (hexalen.length() == 3) {
            sizeParams[1] = hexalen.substring(0,1);
            sizeParams[0] = hexalen.substring(1,3);
        } else {
            sizeParams[1] = hexalen.substring(0,1);
            sizeParams[0] = "0" + hexalen.substring(1,2);
        }
        this.trama = this.trama.substring(0,12) + sizeParams[0] + "0" + sizeParams[1] + this.trama.substring(16,this.trama.length());
    }

    public String[] SizeTemplate(String template){

        String HexaLen = Integer.toHexString(template.length()/2);
        String[] sizeParams = ".,.".split(",");
        if(HexaLen.length() == 3){
            sizeParams[1] = HexaLen.substring(0,1);
            sizeParams[0] = HexaLen.substring(1,3);
        }else{
            sizeParams[1] = HexaLen.substring(0,1);
            sizeParams[0] = "0" + HexaLen.substring(1,2);
        }

        return sizeParams;
    }


    public String getFinalValue(String tarjeta) {
        Log.v("TEMPUS: ","Tarjeta >>> " + tarjeta);
        String resultado = "";
        String temporal = "";
        temporal = tarjeta.replaceAll("^\\s+","0");
        Log.v("TEMPUS: ","temporal >>> " + temporal);
        if (temporal.length() == 0) {
            resultado = "00";
        } else {
            resultado = temporal;
        }
        return resultado;
    }

    public void writeToSuprema(OutputStream out, String opcion, String[] parametros) {
        String trama = "";
        String mascara = "";
        switch (opcion) {
            case "Timeout":
                mascara = "400100000000" + parametros[0] + "00000062000a";
                trama = addChecksum(mascara);
                break;
            case "TemplateSize":
                mascara = "4001000000000000000064000a";
                trama = addChecksum(mascara);
                break;
            case "SecurityLevel":
                mascara = "4001000000000000000066000a";
                trama = addChecksum(mascara);
                break;
            case "FreeScanOn":
                mascara = "4001000000003100000084000a";
                trama = addChecksum(mascara);
                break;
            case "FreeScanOff":
                mascara = "4001000000003000000084000a";
                trama = addChecksum(mascara);
                break;
            case "Cancel":
                mascara = "4060000000000000000000000a";
                trama = addChecksum(mascara);
                break;
            case "EnrollByScan":
                mascara = "4005" + parametros[0] + "0000000071000a";
                trama = addChecksum(mascara);
                break;
            case "EnrollByTemplate":
                mascara = "4007" + parametros[0] + "" + parametros[1] + "71000a" + parametros[2]+ "0a";  //4007 00000603 00010000 71 00 0a - huella 0a
                //Log.d("TEMPUS: ", ">>>>>>>>>>>mascara: " + mascara);
                trama = addChecksum(mascara);
                //Log.d("TEMPUS: ", ">>>>>>>>>>>trama: " + trama);
                break;
            case "IdentifyByScan":
                mascara = "4011000000000000000070000a";
                trama = addChecksum(mascara);
                break;
            case "DeleteAllTemplates":
                mascara = "4017000000000000000000000a";
                trama = addChecksum(mascara);
                break;
            case "DeleteTemplate":
                mascara = "4016" + parametros[0] + "0000000000000a";
                trama = addChecksum(mascara);
                break;
            case "ListUserID":
                mascara = "4018000000000000000000000a";
                trama = addChecksum(mascara);
                break;
            case "ReadTemplate":
                mascara = "4014" + parametros[0] + "0000000000000a";
                trama = addChecksum(mascara);
                break;
            // ================================================================================== //
            case "NumberTemplate":
                mascara = "4003000000000000000073000a";
                trama = addChecksum(mascara);
                break;
            case "SendaScanSuccess":
                mascara = "4003000000000000000075000a";
                trama = addChecksum(mascara);
                break;
            case "EnrollMode":
                mascara = "4003000000000000000065000a";
                trama = addChecksum(mascara);
                break;
            case "ResponseDelay":
                mascara = "4003000000000000000087000a";
                trama = addChecksum(mascara);
                break;
            case "FastMode":
                mascara = "4003000000000000000093000a";
                trama = addChecksum(mascara);
                break;
            case "EnrollDisplacement":
                mascara = "400300000000000000008A000a";
                trama = addChecksum(mascara);
                break;
            case "SaveEnrollMode":
                mascara = "400200000000" + parametros[0] + "65000a";
                trama = addChecksum(mascara);
                break;
            case "SaveFastMode":
                mascara = "400200000000" + parametros[0] + "93000a";
                trama = addChecksum(mascara);
                break;
            case "SaveSendaScanSuccess":
                mascara = "400200000000" + parametros[0] + "75000a";
                trama = addChecksum(mascara);
                break;
            case "WriteEnrollMode":
                mascara = "400100000000" + parametros[0] + "65000a";
                trama = addChecksum(mascara);
                break;
            case "WriteFastMode":
                mascara = "400100000000" + parametros[0] + "93000a";
                trama = addChecksum(mascara);
                break;
            case "WriteSendaScanSuccess":
                mascara = "400100000000" + parametros[0] + "75000a";
                trama = addChecksum(mascara);
                break;

            default:
                break;
        }

        Log.v("TEMPUS: ","Salio - " + opcion + " -> " + trama);



        try {
            out.write(util.hexStringToByteArray(trama));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFlagError() {
        String msj = "";
        switch (this.trama) {
            case "62":
                msj = "SCAN_SUCCESS";
                break;
            case "63":
                msj = "SCAN_FAIL";
                break;
            case "6c":
                msj = "TIME_OUT";
                break;
            case "61":
                msj = "SUCCESS";
                break;
            case "6b":
                msj = "TRY_AGAIN";
                break;
            case "69":
                msj = "NOT_FOUND";
                break;
            case "6a":
                msj = "NOT_MATCH";
                break;
            case "90":
                msj = "REJECTED_ID";
                break;
            case "91":
                msj = "DURESS_FINGER";
                break;
            case "94":
                msj = "ENTRANCE_LIMIT";
                break;
            case "b0":
                msj = "FAKE_DETECTED";
                break;
            case "74":
                msj = "CONTINUE";
                break;
            default:
                break;
        }
        return msj;
    }

    public int GetSizeSuprema(String cadena) {
        int tam = 0;

        if (cadena.equals("00000000")) {
            tam = 0;
        } else {
            if (cadena.equals("00010000")) {
                tam = 514;
            } else {
                tam = 0;
            }
        }

        return tam;
    }

    public String convertCardToEnroll(String card){
        if ( card.length() < 8 ){
            int tam = 8 - card.length();
            for (int i = 0; i < tam; i++){
                card = "0" + card;
            }
        }
        return card;
    }

    @Override
    public String toString() {
        return "MainSuprema{" +
                "util=" + util +
                ", trama='" + trama + '\'' +
                ", Cabecera='" + Cabecera + '\'' +
                ", Comando='" + Comando + '\'' +
                ", Parametro='" + Parametro + '\'' +
                ", Tamano='" + Tamano + '\'' +
                ", Flag='" + Flag + '\'' +
                ", Checksum='" + Checksum + '\'' +
                ", Cola='" + Cola + '\'' +
                ", Data='" + Data + '\'' +
                '}';
    }
}
