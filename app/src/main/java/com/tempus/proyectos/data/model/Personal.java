package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 04/11/2016.
 */

public class Personal {
    public String Empresa;
    public String Codigo;
    public String CentroDeCosto;
    public String ApellidoPaterno;
    public String ApellidoMaterno;
    public String Nombres;
    public String FechaDeNacimiento;
    public String FechaDeIngreso;
    public String FechaDeCese;
    public String Estado;
    public String TipoHorario;
    public String Icono;
    public String NroDocumento;
    public String FechaHoraSinc;

    public Personal() {

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

    public String getCentroDeCosto() {
        return CentroDeCosto;
    }

    public void setCentroDeCosto(String centroDeCosto) {
        CentroDeCosto = centroDeCosto;
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

    public String getFechaDeNacimiento() {
        return FechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        FechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeIngreso() {
        return FechaDeIngreso;
    }

    public void setFechaDeIngreso(String fechaDeIngreso) {
        FechaDeIngreso = fechaDeIngreso;
    }

    public String getFechaDeCese() {
        return FechaDeCese;
    }

    public void setFechaDeCese(String fechaDeCese) {
        FechaDeCese = fechaDeCese;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getTipoHorario() {
        return TipoHorario;
    }

    public void setTipoHorario(String tipoHorario) {
        TipoHorario = tipoHorario;
    }

    public String getIcono() {
        return Icono;
    }

    public void setIcono(String icono) {
        Icono = icono;
    }

    public String getNroDocumento() {
        return NroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        NroDocumento = nroDocumento;
    }

    public String getFechaHoraSinc() {
        return FechaHoraSinc;
    }

    public void setFechaHoraSinc(String fechaHoraSinc) {
        FechaHoraSinc = fechaHoraSinc;
    }

    @Override
    public String toString() {
        return "Personal{" +
                "Empresa='" + Empresa + '\'' +
                ", Codigo='" + Codigo + '\'' +
                ", CentroDeCosto='" + CentroDeCosto + '\'' +
                ", ApellidoPaterno='" + ApellidoPaterno + '\'' +
                ", ApellidoMaterno='" + ApellidoMaterno + '\'' +
                ", Nombres='" + Nombres + '\'' +
                ", FechaDeNacimiento='" + FechaDeNacimiento + '\'' +
                ", FechaDeIngreso='" + FechaDeIngreso + '\'' +
                ", FechaDeCese='" + FechaDeCese + '\'' +
                ", Estado='" + Estado + '\'' +
                ", TipoHorario='" + TipoHorario + '\'' +
                ", Icono='" + Icono + '\'' +
                ", NroDocumento='" + NroDocumento + '\'' +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
