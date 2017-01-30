package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 15/11/2016.
 */

public class Autorizaciones {

    public String Empresa;
    public String Codigo;
    public String ApellidoPaterno;
    public String ApellidoMaterno;
    public String Nombres;
    public String Icono;
    public int EstadoRequiereAsistencia;
    public int FlagPerTipoLectTerm;
    public int FlagTerminalTipoLect;
    public String Idterminal;
    public int IdTipoLect;
    public String ValorTarjeta;
    public String Mensaje;
    public String MensajeDetalle;

    public Autorizaciones() {

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

    public String getIcono() {
        return Icono;
    }

    public void setIcono(String icono) {
        Icono = icono;
    }

    public int getEstadoRequiereAsistencia() {
        return EstadoRequiereAsistencia;
    }

    public void setEstadoRequiereAsistencia(int estadoRequiereAsistencia) {
        EstadoRequiereAsistencia = estadoRequiereAsistencia;
    }

    public int getFlagPerTipoLectTerm() {
        return FlagPerTipoLectTerm;
    }

    public void setFlagPerTipoLectTerm(int flagPerTipoLectTerm) {
        FlagPerTipoLectTerm = flagPerTipoLectTerm;
    }

    public int getFlagTerminalTipoLect() {
        return FlagTerminalTipoLect;
    }

    public void setFlagTerminalTipoLect(int flagTerminalTipoLect) {
        FlagTerminalTipoLect = flagTerminalTipoLect;
    }

    public String getIdterminal() {
        return Idterminal;
    }

    public void setIdterminal(String idterminal) {
        Idterminal = idterminal;
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

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getMensajeDetalle() {
        return MensajeDetalle;
    }

    public void setMensajeDetalle(String mensajeDetalle) {
        MensajeDetalle = mensajeDetalle;
    }


    @Override
    public String toString() {
        return "Autorizaciones{" +
                "Empresa='" + Empresa + '\'' +
                ", Codigo='" + Codigo + '\'' +
                ", ApellidoPaterno='" + ApellidoPaterno + '\'' +
                ", ApellidoMaterno='" + ApellidoMaterno + '\'' +
                ", Nombres='" + Nombres + '\'' +
                ", Icono='" + Icono + '\'' +
                ", EstadoRequiereMarcacion=" + EstadoRequiereAsistencia +
                ", FlagPerTipoLectTerm=" + FlagPerTipoLectTerm +
                ", FlagTerminalTipoLect=" + FlagTerminalTipoLect +
                ", Idterminal='" + Idterminal + '\'' +
                ", IdTipoLect=" + IdTipoLect +
                ", ValorTarjeta='" + ValorTarjeta + '\'' +
                ", Mensaje='" + Mensaje + '\'' +
                ", MensajeDetalle='" + MensajeDetalle + '\'' +
                '}';
    }

}
