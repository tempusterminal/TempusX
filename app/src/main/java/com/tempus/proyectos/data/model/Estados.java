package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class Estados {
    public String Estado;
    public String Descripcion;
    public int RequiereAsistencia;
    public String FechaHoraSinc;

    public Estados() {

    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getRequiereAsistencia() {
        return RequiereAsistencia;
    }

    public void setRequiereAsistencia(int requiereAsistencia) {
        RequiereAsistencia = requiereAsistencia;
    }

    public String getFechaHoraSinc() {
        return FechaHoraSinc;
    }

    public void setFechaHoraSinc(String fechaHoraSinc) {
        FechaHoraSinc = fechaHoraSinc;
    }

    @Override
    public String toString() {
        return "Estados{" +
                "Estado='" + Estado + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", RequiereAsistencia=" + RequiereAsistencia +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
