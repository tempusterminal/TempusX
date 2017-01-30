package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 12/01/2017.
 */

public class TerminalSerial {
    public String Idserial;
    public String Mac;
    public int Enable;
    public int LoopConn;
    public int TryConn;
    public int TryConnBt;
    public int TryReconn;
    public int TryReconnBt;
    public int EnableReboot;
    public String FechaHoraSinc;

    public TerminalSerial() {

    }

    public String getIdserial() {
        return Idserial;
    }

    public void setIdserial(String idserial) {
        Idserial = idserial;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public int getEnable() {
        return Enable;
    }

    public void setEnable(int enable) {
        Enable = enable;
    }

    public int getLoopConn() {
        return LoopConn;
    }

    public void setLoopConn(int loopConn) {
        LoopConn = loopConn;
    }

    public int getTryConn() {
        return TryConn;
    }

    public void setTryConn(int tryConn) {
        TryConn = tryConn;
    }

    public int getTryConnBt() {
        return TryConnBt;
    }

    public void setTryConnBt(int tryConnBt) {
        TryConnBt = tryConnBt;
    }

    public int getTryReconn() {
        return TryReconn;
    }

    public void setTryReconn(int tryReconn) {
        TryReconn = tryReconn;
    }

    public int getTryReconnBt() {
        return TryReconnBt;
    }

    public void setTryReconnBt(int tryReconnBt) {
        TryReconnBt = tryReconnBt;
    }

    public int getEnableReboot() {
        return EnableReboot;
    }

    public void setEnableReboot(int enableReboot) {
        EnableReboot = enableReboot;
    }

    public String getFechaHoraSinc() {
        return FechaHoraSinc;
    }

    public void setFechaHoraSinc(String fechaHoraSinc) {
        FechaHoraSinc = fechaHoraSinc;
    }

    @Override
    public String toString() {
        return "TerminalSerial{" +
                "Idserial='" + Idserial + '\'' +
                ", Mac='" + Mac + '\'' +
                ", Enable=" + Enable +
                ", LoopConn=" + LoopConn +
                ", TryConn=" + TryConn +
                ", TryConnBt=" + TryConnBt +
                ", TryReconn=" + TryReconn +
                ", TryReconnBt=" + TryReconnBt +
                ", EnableReboot=" + EnableReboot +
                ", FechaHoraSinc=" + FechaHoraSinc +
                '}';
    }
}
