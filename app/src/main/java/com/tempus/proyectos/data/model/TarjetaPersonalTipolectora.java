package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 04/11/2016.
 */

public class TarjetaPersonalTipolectora {
    public String Empresa;
    public String Codigo;
    public int IdTipoLect;
    public String ValorTarjeta;
    public String FechaIni;
    public String FechaFin;
    public String FechaCreacion;
    public String FechaAnulacion;
    public int IdAutorizacion;
    public String FechaHoraSinc;

    public TarjetaPersonalTipolectora() {

    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public int getIdTipoLect() {
        return IdTipoLect;
    }

    public void setIdTipoLect(int idTipoLect) {
        IdTipoLect = idTipoLect;
    }

    public String getValorTarjeta() {
        return ValorTarjeta;
    }

    public void setValorTarjeta(String valorTarjeta) {
        ValorTarjeta = valorTarjeta;
    }

    public String getFechaIni() {
        return FechaIni;
    }

    public void setFechaIni(String fechaIni) {
        FechaIni = fechaIni;
    }

    public String getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(String fechaFin) {
        FechaFin = fechaFin;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public String getFechaAnulacion() {
        return FechaAnulacion;
    }

    public void setFechaAnulacion(String fechaAnulacion) {
        FechaAnulacion = fechaAnulacion;
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
        return "TarjetaPersonalTipolectora{" +
                "Empresa='" + Empresa + '\'' +
                ", Codigo='" + Codigo + '\'' +
                ", IdTipoLect=" + IdTipoLect +
                ", ValorTarjeta='" + ValorTarjeta + '\'' +
                ", FechaIni='" + FechaIni + '\'' +
                ", FechaFin='" + FechaFin + '\'' +
                ", FechaCreacion='" + FechaCreacion + '\'' +
                ", FechaAnulacion='" + FechaAnulacion + '\'' +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
