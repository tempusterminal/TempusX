package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class TerminalServicios {

    public int IdTerminalServicios;
    public String Idterminal;
    public int IdServicios;
    public int Flag;
    public int IdAutorizacion;
    public String FechaHoraSinc;

    public TerminalServicios() {

    }

    public int getIdTerminalServicios() {
        return IdTerminalServicios;
    }

    public void setIdTerminalServicios(int idTerminalServicios) {
        IdTerminalServicios = idTerminalServicios;
    }

    public String getIdterminal() {
        return Idterminal;
    }

    public void setIdterminal(String idterminal) {
        Idterminal = idterminal;
    }

    public int getIdServicios() {
        return IdServicios;
    }

    public void setIdServicios(int idServicios) {
        IdServicios = idServicios;
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
        return "TerminalServicios{" +
                "IdTerminalServicios=" + IdTerminalServicios +
                ", Idterminal='" + Idterminal + '\'' +
                ", IdServicios=" + IdServicios +
                ", Flag=" + Flag +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
