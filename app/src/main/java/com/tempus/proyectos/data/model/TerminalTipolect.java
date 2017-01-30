package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class TerminalTipolect {
    public int IdTerminalTipolect;
    public String Idterminal;
    public int IdTipoLect;
    public int Flag;
    public int IdAutorizacion;
    public String FechaHoraSinc;

    public TerminalTipolect() {

    }

    public int getIdTerminalTipolect() {
        return IdTerminalTipolect;
    }

    public void setIdTerminalTipolect(int idTerminalTipolect) {
        IdTerminalTipolect = idTerminalTipolect;
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
        return "TerminalTipolect{" +
                "IdTerminalTipolect=" + IdTerminalTipolect +
                ", Idterminal='" + Idterminal + '\'' +
                ", IdTipoLect=" + IdTipoLect +
                ", Flag=" + Flag +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
