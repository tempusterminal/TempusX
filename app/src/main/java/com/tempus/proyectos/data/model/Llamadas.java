package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 10/01/2017.
 */

public class Llamadas {
    public String Idllamada;
    public String Llamada;
    public String Parameters;
    public String TableName;
    public String Primarykey;
    public String Columns;
    public String FechaHoraSinc;


    public Llamadas() {

    }

    public String getIdllamada() {
        return Idllamada;
    }

    public void setIdllamada(String idllamada) {
        Idllamada = idllamada;
    }

    public String getLlamada() {
        return Llamada;
    }

    public void setLlamada(String llamada) {
        Llamada = llamada;
    }

    public String getParameters() {
        return Parameters;
    }

    public void setParameters(String parameters) {
        Parameters = parameters;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public String getPrimarykey() {
        return Primarykey;
    }

    public void setPrimarykey(String primarykey) {
        Primarykey = primarykey;
    }

    public String getColumns() {
        return Columns;
    }

    public void setColumns(String columns) {
        Columns = columns;
    }

    public String getFechaHoraSinc() {
        return FechaHoraSinc;
    }

    public void setFechaHoraSinc(String fechaHoraSinc) {
        FechaHoraSinc = fechaHoraSinc;
    }

    @Override
    public String toString() {
        return "Llamadas{" +
                "Idllamada='" + Idllamada + '\'' +
                ", Llamada='" + Llamada + '\'' +
                ", Parameters='" + Parameters + '\'' +
                ", TableName='" + TableName + '\'' +
                ", Primarykey='" + Primarykey + '\'' +
                ", Columns='" + Columns + '\'' +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}

