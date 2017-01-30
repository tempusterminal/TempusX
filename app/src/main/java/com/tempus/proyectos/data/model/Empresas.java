package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class Empresas {
    public String Empresa;
    public String NombreCorto;
    public String Nombre;
    public String Icono;
    public String Direccion;
    public String Ruc;
    public int Bloqueado;
    public String FechaHoraSinc;

    public Empresas() {

    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public String getNombreCorto() {
        return NombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        NombreCorto = nombreCorto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getIcono() {
        return Icono;
    }

    public void setIcono(String icono) {
        Icono = icono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getRuc() {
        return Ruc;
    }

    public void setRuc(String ruc) {
        Ruc = ruc;
    }

    public int getBloqueado() {
        return Bloqueado;
    }

    public void setBloqueado(int bloqueado) {
        Bloqueado = bloqueado;
    }

    public String getFechaHoraSinc() {
        return FechaHoraSinc;
    }

    public void setFechaHoraSinc(String fechaHoraSinc) {
        FechaHoraSinc = fechaHoraSinc;
    }

    @Override
    public String toString() {
        return "Empresas{" +
                "Empresa='" + Empresa + '\'' +
                ", NombreCorto='" + NombreCorto + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", Icono='" + Icono + '\'' +
                ", Direccion='" + Direccion + '\'' +
                ", Ruc='" + Ruc + '\'' +
                ", Bloqueado=" + Bloqueado +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
