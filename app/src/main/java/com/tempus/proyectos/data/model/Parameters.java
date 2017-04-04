package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 30/03/2017.
 */

public class Parameters {
    public String Idparameter;
    public String Parameter;
    public String Value;
    public String Subparameters;
    public int Enable;
    public String FechaHoraSinc;

    public Parameters() {
    }

    public String getIdparameter() {
        return Idparameter;
    }

    public void setIdparameter(String idparameter) {
        Idparameter = idparameter;
    }

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getSubparameters() {
        return Subparameters;
    }

    public void setSubparameters(String subparameters) {
        Subparameters = subparameters;
    }

    public int getEnable() {
        return Enable;
    }

    public void setEnable(int enable) {
        Enable = enable;
    }

    public String getFechaHoraSinc() {
        return FechaHoraSinc;
    }

    public void setFechaHoraSinc(String fechaHoraSinc) {
        FechaHoraSinc = fechaHoraSinc;
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "Idparameter='" + Idparameter + '\'' +
                ", Parameter='" + Parameter + '\'' +
                ", Value='" + Value + '\'' +
                ", Subparameters='" + Subparameters + '\'' +
                ", Enable=" + Enable +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
