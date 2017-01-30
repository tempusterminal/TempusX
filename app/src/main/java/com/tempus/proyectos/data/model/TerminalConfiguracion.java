package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class TerminalConfiguracion {
    public String Idterminal;
    public int TipoInstalacion;
    public int TipoAhorroEnergia;
    public int BateriaResistencia;
    public int HusoHorario;
    public String Parametro;
    public String FechaHoraServidor;
    public int Reboot;
    public String HoraReboot;
    public int Replicado;
    public String HoraInicioReplicado;
    public String HoraFinReplicado;
    public int IdAutorizacion;
    public String FechaHoraSinc;

    public TerminalConfiguracion() {

    }

    public String getIdterminal() {
        return Idterminal;
    }

    public void setIdterminal(String idterminal) {
        Idterminal = idterminal;
    }

    public int getTipoInstalacion() {
        return TipoInstalacion;
    }

    public void setTipoInstalacion(int tipoInstalacion) {
        TipoInstalacion = tipoInstalacion;
    }

    public int getTipoAhorroEnergia() {
        return TipoAhorroEnergia;
    }

    public void setTipoAhorroEnergia(int tipoAhorroEnergia) {
        TipoAhorroEnergia = tipoAhorroEnergia;
    }

    public int getBateriaResistencia() {
        return BateriaResistencia;
    }

    public void setBateriaResistencia(int bateriaResistencia) {
        BateriaResistencia = bateriaResistencia;
    }

    public int getHusoHorario() {
        return HusoHorario;
    }

    public void setHusoHorario(int husoHorario) {
        HusoHorario = husoHorario;
    }

    public String getParametro() {
        return Parametro;
    }

    public void setParametro(String parametro) {
        Parametro = parametro;
    }

    public String getFechaHoraServidor() {
        return FechaHoraServidor;
    }

    public void setFechaHoraServidor(String fechaHoraServidor) {
        FechaHoraServidor = fechaHoraServidor;
    }

    public int getReboot() {
        return Reboot;
    }

    public void setReboot(int reboot) {
        Reboot = reboot;
    }

    public String getHoraReboot() {
        return HoraReboot;
    }

    public void setHoraReboot(String horaReboot) {
        HoraReboot = horaReboot;
    }

    public int getReplicado() {
        return Replicado;
    }

    public void setReplicado(int replicado) {
        Replicado = replicado;
    }

    public String getHoraInicioReplicado() {
        return HoraInicioReplicado;
    }

    public void setHoraInicioReplicado(String horaInicioReplicado) {
        HoraInicioReplicado = horaInicioReplicado;
    }

    public String getHoraFinReplicado() {
        return HoraFinReplicado;
    }

    public void setHoraFinReplicado(String horaFinReplicado) {
        HoraFinReplicado = horaFinReplicado;
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
        return "TerminalConfiguracion{" +
                "Idterminal='" + Idterminal + '\'' +
                ", TipoInstalacion=" + TipoInstalacion +
                ", TipoAhorroEnergia=" + TipoAhorroEnergia +
                ", BateriaResistencia=" + BateriaResistencia +
                ", HusoHorario=" + HusoHorario +
                ", Parametro='" + Parametro + '\'' +
                ", FechaHoraServidor='" + FechaHoraServidor + '\'' +
                ", Reboot=" + Reboot +
                ", HoraReboot='" + HoraReboot + '\'' +
                ", Replicado=" + Replicado +
                ", HoraInicioReplicado='" + HoraInicioReplicado + '\'' +
                ", HoraFinReplicado='" + HoraFinReplicado + '\'' +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
