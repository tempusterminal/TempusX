package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class TipoLectora {
    public int IdTipoLect;
    public String Descripcion;
    public int Biometria;
    public int IdAutorizacion;
    public String FechaHoraSinc;

    public TipoLectora() {

    }

    public int getIdTipoLect() {
        return IdTipoLect;
    }

    public void setIdTipoLect(int idTipoLect) {
        IdTipoLect = idTipoLect;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getBiometria() {
        return Biometria;
    }

    public void setBiometria(int biometria) {
        Biometria = biometria;
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
        return "TipoLectora{" +
                "IdTipoLect=" + IdTipoLect +
                ", Descripcion='" + Descripcion + '\'' +
                ", Biometria=" + Biometria +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
