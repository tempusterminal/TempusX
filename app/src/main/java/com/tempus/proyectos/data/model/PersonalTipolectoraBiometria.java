package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 04/11/2016.
 */

public class PersonalTipolectoraBiometria {

    public int IdPerTipolectBio;
    public int IndiceBiometria;
    public String Empresa;
    public String Codigo;
    public int IdTipoLect;
    public String ValorTarjeta;
    public int IdTipoDetaBio;
    public String ValorBiometria;
    public String FechaBiometria;
    public String ImagenBiometria;
    public int IdAutorizacion;
    public int Sincronizado;
    public String FechaHoraSinc;

    public PersonalTipolectoraBiometria() {

    }

    public int getIdPerTipolectBio() {
        return IdPerTipolectBio;
    }

    public void setIdPerTipolectBio(int idPerTipolectBio) {
        IdPerTipolectBio = idPerTipolectBio;
    }

    public int getIndiceBiometria() {
        return IndiceBiometria;
    }

    public void setIndiceBiometria(int indiceBiometria) {
        IndiceBiometria = indiceBiometria;
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

    public int getIdTipoDetaBio() {
        return IdTipoDetaBio;
    }

    public void setIdTipoDetaBio(int idTipoDetaBio) {
        IdTipoDetaBio = idTipoDetaBio;
    }

    public String getValorBiometria() {
        return ValorBiometria;
    }

    public void setValorBiometria(String valorBiometria) {
        ValorBiometria = valorBiometria;
    }

    public String getFechaBiometria() {
        return FechaBiometria;
    }

    public void setFechaBiometria(String fechaBiometria) {
        FechaBiometria = fechaBiometria;
    }

    public String getImagenBiometria() {
        return ImagenBiometria;
    }

    public void setImagenBiometria(String imagenBiometria) {
        ImagenBiometria = imagenBiometria;
    }

    public int getIdAutorizacion() {
        return IdAutorizacion;
    }

    public void setIdAutorizacion(int idAutorizacion) {
        IdAutorizacion = idAutorizacion;
    }

    public int getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(int sincronizado) {
        Sincronizado = sincronizado;
    }

    public String getFechaHoraSinc() {
        return FechaHoraSinc;
    }

    public void setFechaHoraSinc(String fechaHoraSinc) {
        FechaHoraSinc = fechaHoraSinc;
    }

    @Override
    public String toString() {
        return "PersonalTipolectoraBiometria{" +
                "IdPerTipolectBio=" + IdPerTipolectBio +
                ", IndiceBiometria=" + IndiceBiometria +
                ", Empresa='" + Empresa + '\'' +
                ", Codigo='" + Codigo + '\'' +
                ", IdTipoLect=" + IdTipoLect +
                ", ValorTarjeta='" + ValorTarjeta + '\'' +
                ", IdTipoDetaBio=" + IdTipoDetaBio +
                ", ValorBiometria='" + ValorBiometria + '\'' +
                ", FechaBiometria='" + FechaBiometria + '\'' +
                ", ImagenBiometria='" + ImagenBiometria + '\'' +
                ", IdAutorizacion=" + IdAutorizacion +
                ", Sincronizado=" + Sincronizado +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
