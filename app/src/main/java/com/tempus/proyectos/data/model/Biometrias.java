package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 28/11/2016.
 */

public class Biometrias {
    public int IdTipoDetaBio;
    public int IndiceBiometria;
    public String Empresa;
    public String Codigo;
    public String NroDocumento;
    public String ApellidoPaterno;
    public String ApellidoMaterno;
    public String Nombres;
    public int IdTipoLect;
    public int ValorBiometria;
    public int FlagPerTipoLectTerm;
    public String Mensaje;

    public Biometrias() {
    }

    public int getIdTipoDetaBio() {
        return IdTipoDetaBio;
    }

    public void setIdTipoDetaBio(int idTipoDetaBio) {
        IdTipoDetaBio = idTipoDetaBio;
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

    public String getNroDocumento() {
        return NroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        NroDocumento = nroDocumento;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public int getIdTipoLect() {
        return IdTipoLect;
    }

    public void setIdTipoLect(int idTipoLect) {
        IdTipoLect = idTipoLect;
    }

    public int getValorBiometria() {
        return ValorBiometria;
    }

    public void setValorBiometria(int valorBiometria) {
        ValorBiometria = valorBiometria;
    }

    public int getFlagPerTipoLectTerm() {
        return FlagPerTipoLectTerm;
    }

    public void setFlagPerTipoLectTerm(int flagPerTipoLectTerm) {
        FlagPerTipoLectTerm = flagPerTipoLectTerm;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Biometrias{" +
                "IdTipoDetaBio=" + IdTipoDetaBio +
                ", IndiceBiometria=" + IndiceBiometria +
                ", Empresa='" + Empresa + '\'' +
                ", Codigo='" + Codigo + '\'' +
                ", NroDocumento='" + NroDocumento + '\'' +
                ", ApellidoPaterno='" + ApellidoPaterno + '\'' +
                ", ApellidoMaterno='" + ApellidoMaterno + '\'' +
                ", Nombres='" + Nombres + '\'' +
                ", IdTipoLect=" + IdTipoLect +
                ", ValorBiometria=" + ValorBiometria +
                ", FlagPerTipoLectTerm=" + FlagPerTipoLectTerm +
                ", Mensaje='" + Mensaje + '\'' +
                '}';
    }
}



