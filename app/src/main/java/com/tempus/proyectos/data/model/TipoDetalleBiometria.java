package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class TipoDetalleBiometria {
    public int IdTipoDetaBio;
    public String Descripcion;
    public int IdTipoLect;
    public int IdAutorizacion;
    public String FechaHoraSinc;

    public TipoDetalleBiometria() {

    }

    public int getIdTipoDetaBio() {
        return IdTipoDetaBio;
    }

    public void setIdTipoDetaBio(int idTipoDetaBio) {
        IdTipoDetaBio = idTipoDetaBio;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getIdTipoLect() {
        return IdTipoLect;
    }

    public void setIdTipoLect(int idTipoLect) {
        IdTipoLect = idTipoLect;
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
        return "TipoDetalleBiometria{" +
                "IdTipoDetaBio=" + IdTipoDetaBio +
                ", Descripcion='" + Descripcion + '\'' +
                ", IdTipoLect=" + IdTipoLect +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
