package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 04/11/2016.
 */

public class PerTipolectTerm {
    public String Empresa;
    public String Codigo;
    public int IdTerminalTipolect;
    public int IdTipoLect;
    public int Flag;
    public int IdAutorizacion;
    public String FechaHoraSinc;

    public PerTipolectTerm() {

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

    public int getIdTerminalTipolect() {
        return IdTerminalTipolect;
    }

    public void setIdTerminalTipolect(int idTerminalTipolect) {
        IdTerminalTipolect = idTerminalTipolect;
    }

    public int getIdTipoLect() {
        return IdTipoLect;
    }

    public void setIdTipoLect(int idTipoLect) {
        IdTipoLect = idTipoLect;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
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
        return "PerTipolectTerm{" +
                "Empresa='" + Empresa + '\'' +
                ", Codigo='" + Codigo + '\'' +
                ", IdTerminalTipolect=" + IdTerminalTipolect +
                ", IdTipoLect=" + IdTipoLect +
                ", Flag=" + Flag +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
