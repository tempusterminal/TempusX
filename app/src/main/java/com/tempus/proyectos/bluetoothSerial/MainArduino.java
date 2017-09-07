package com.tempus.proyectos.bluetoothSerial;

import android.util.Log;

import com.tempus.proyectos.util.Utilities;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ecernar on 03/10/2016.
 */

public class MainArduino {

    Utilities util = new Utilities();

    private String trama;

    private String cabecera;
    private String longitud;
    private String mensaje;
    private String checksum;
    private String cola;

    private String statusLed;
    private String statusRele;
    private String statusRTC;
    private String tipoMensaje;
    private String nroLector;
    private String datosLector;
    private String flagRead;
    private int mascaraIni;
    private int mascaraFin;

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getCola() {
        return cola;
    }

    public void setCola(String cola) {
        this.cola = cola;
    }

    public String getStatusLed() {
        return statusLed;
    }

    public void setStatusLed(String statusLed) {
        this.statusLed = statusLed;
    }

    public String getStatusRele() {
        return statusRele;
    }

    public void setStatusRele(String statusRele) {
        this.statusRele = statusRele;
    }

    public String getStatusRTC() {
        return statusRTC;
    }

    public void setStatusRTC(String statusRTC) {
        this.statusRTC = statusRTC;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getNroLector() {
        return nroLector;
    }

    public void setNroLector(String nroLector) {
        this.nroLector = nroLector;
    }

    public String getDatosLector() {
        return datosLector;
    }

    public void setDatosLector(String datosLector) {
        this.datosLector = datosLector;
    }

    public String getFlagRead() {
        return flagRead;
    }

    public void setFlagRead(String flagRead) {
        this.flagRead = flagRead;
    }

    public int getMascaraIni() {
        return mascaraIni;
    }

    public void setMascaraIni(int mascaraIni) {
        this.mascaraIni = mascaraIni;
    }

    public int getMascaraFin() {
        return mascaraFin;
    }

    public void setMascaraFin(int mascaraFin) {
        this.mascaraFin = mascaraFin;
    }

    public void limpiarTramaArduino() {
        this.cabecera = "";
        this.longitud = "";
        this.mensaje = "";
        this.checksum = "";
        this.cola = "";

        this.statusLed = "";
        this.statusRele = "";
        this.statusRTC = "";
        this.tipoMensaje = "";
        this.nroLector = "";
        this.datosLector = "";
        this.flagRead = "";
        this.mascaraIni = 0;
        this.mascaraFin = 0;
    }

    public void estructurarTramaArduinoGeneral() {
        this.cabecera = this.trama.substring(0,10);
        this.longitud = this.trama.substring(10,14);
        this.mensaje = this.trama.substring(14,102);
        this.checksum = this.trama.substring(102,106);
        this.cola = this.trama.substring(106,108);
    }

    public void estructurarTramaArduinoMensaje() {
        this.statusLed = this.mensaje.substring(0,2);
        this.statusRele = this.mensaje.substring(2,4);
        this.statusRTC = this.mensaje.substring(4,20);
        this.tipoMensaje = this.mensaje.substring(20,22);
        this.nroLector = this.mensaje.substring(22,24);
        this.datosLector = this.mensaje.substring(24,88);
    }

    public void setValorMascara() {
        switch (this.nroLector) {
            case "00":
                this.flagRead = "NO DATOS";
                this.mascaraIni = 0;
                this.mascaraFin = 64;
                break;
            case "01":
                this.flagRead = "PROXIMIDAD CHINA";
                this.mascaraIni = 50;
                this.mascaraFin = 60;
                break;
            case "02":
                this.flagRead = "PROXIMIDAD HID";
                this.mascaraIni = 48;
                this.mascaraFin = 64;
                break;
            case "03":
                this.flagRead = "";
                this.mascaraIni = 0;
                this.mascaraFin = 0;
                break;
            case "04":
                this.flagRead = "DNI";
                this.mascaraIni = 0;
                this.mascaraFin = 17;
                break;
            case "05":
                this.flagRead = "";
                this.mascaraIni = 0;
                this.mascaraFin = 0;
                break;
            case "06":
                this.flagRead = "TECLADO";
                this.mascaraIni = 14;
                this.mascaraFin = 16;
                break;
            case "07":
                this.flagRead = "";
                this.mascaraIni = 0;
                this.mascaraFin = 0;
                break;
            case "08":
                this.flagRead = "HUELLA SUPREMA";
                this.mascaraIni = 4;
                this.mascaraFin = 12;
                break;
            case "09":
                this.flagRead = "DNI";
                this.mascaraIni = 48;
                this.mascaraFin = 64;
                break;
            case "0a":
                this.flagRead = "HUELLA SUPREMA";
                this.mascaraIni = 0;
                this.mascaraFin = 8;
                break;
            case "44":
                this.flagRead = "RTC";
                this.mascaraIni = 4;
                this.mascaraFin = 32;
                break;
            case "0e":
                this.flagRead = "EVENTO PULSADOR";
                this.mascaraIni = 62;
                this.mascaraFin = 64;
                break;
            default:
                System.out.println("Error");
                break;
        }
    }

    public void writeToArduino(OutputStream out, String opcion, String[] parametros) {

        String cabecera = "244f415841";
        String longitud = "0000";
        String mensaje = "0000";
        String checksum = "0000";
        String cola = "41";

        String temporal = "";

        for (int i = 0; i < parametros.length; i++){
            temporal = temporal + parametros[i];
        }

        switch (opcion){
            case "ADC":
                longitud = "0013";
                mensaje = "44" + temporal;
                break;
            case "ACK":
                longitud = "0013";
                mensaje = "36" + temporal;
                break;
            case "Marcacion":
                longitud = "000c";
                mensaje = "35" + temporal;
                break;
            case "Switch":
                longitud = "0013";
                mensaje = "42" + temporal;
                break;
            case "RTC":
                longitud = "3030";
                mensaje = "31" + temporal;
                break;
            default:
                break;
        }

        String tramaFinal = cabecera + longitud + mensaje + checksum + cola;

        Log.v("TEMPUS: ","Salio - " + opcion + " -> " + tramaFinal);

        try {
            out.write(util.hexStringToByteArray(tramaFinal));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
