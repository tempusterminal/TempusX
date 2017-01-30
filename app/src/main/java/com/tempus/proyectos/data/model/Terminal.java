package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 04/11/2016.
 */

public class Terminal {
    public String Idterminal;
    public String Descripcion;
    public int Habilitado;
    public String Mac;
    public String Modelo;
    public String Firmware;
    public String Software;
    public String Hardware;
    public String Chasis;
    public String Ups;
    public String NumCel;
    public int IdAutorizacion;
    public String FechaHoraSinc;


    public Terminal() {

    }

    public String getIdterminal() {
        return Idterminal;
    }

    public void setIdterminal(String idterminal) {
        Idterminal = idterminal;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getHabilitado() {
        return Habilitado;
    }

    public void setHabilitado(int habilitado) {
        Habilitado = habilitado;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getFirmware() {
        return Firmware;
    }

    public void setFirmware(String firmware) {
        Firmware = firmware;
    }

    public String getSoftware() {
        return Software;
    }

    public void setSoftware(String software) {
        Software = software;
    }

    public String getHardware() {
        return Hardware;
    }

    public void setHardware(String hardware) {
        Hardware = hardware;
    }

    public String getChasis() {
        return Chasis;
    }

    public void setChasis(String chasis) {
        Chasis = chasis;
    }

    public String getUps() {
        return Ups;
    }

    public void setUps(String ups) {
        Ups = ups;
    }

    public String getNumCel() {
        return NumCel;
    }

    public void setNumCel(String numCel) {
        NumCel = numCel;
    }

    public int getIdAutorizacion() {
        return IdAutorizacion;
    }

    public void setIdAutorizacion(int idAutorizacion) {
        IdAutorizacion = idAutorizacion;
    }

    public String getFechaHoraSinc() {
        return FechaHoraSinc;
    }

    public void setFechaHoraSinc(String fechaHoraSinc) {
        FechaHoraSinc = fechaHoraSinc;
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "Idterminal='" + Idterminal + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Habilitado=" + Habilitado +
                ", Mac='" + Mac + '\'' +
                ", Modelo='" + Modelo + '\'' +
                ", Firmware='" + Firmware + '\'' +
                ", Software='" + Software + '\'' +
                ", Hardware='" + Hardware + '\'' +
                ", Chasis='" + Chasis + '\'' +
                ", Ups='" + Ups + '\'' +
                ", NumCel='" + NumCel + '\'' +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
