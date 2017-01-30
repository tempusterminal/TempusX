package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class Servicios {
    public int IdServicios;
    public String Descripcion;
    public String Host;
    public String Ip;
    public String Instance;
    public String Database;
    public String Port;
    public String User;
    public String Pass;
    public int IdAutorizacion;
    public String FechaHoraSinc;

    public Servicios() {

    }

    public int getIdServicios() {
        return IdServicios;
    }

    public void setIdServicios(int idServicios) {
        IdServicios = idServicios;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getInstance() {
        return Instance;
    }

    public void setInstance(String instance) {
        Instance = instance;
    }

    public String getDatabase() {
        return Database;
    }

    public void setDatabase(String database) {
        Database = database;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
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
        return "Servicios{" +
                "IdServicios=" + IdServicios +
                ", Descripcion='" + Descripcion + '\'' +
                ", Host='" + Host + '\'' +
                ", Ip='" + Ip + '\'' +
                ", Instance='" + Instance + '\'' +
                ", Database='" + Database + '\'' +
                ", Port='" + Port + '\'' +
                ", User='" + User + '\'' +
                ", Pass='" + Pass + '\'' +
                ", IdAutorizacion=" + IdAutorizacion +
                ", FechaHoraSinc='" + FechaHoraSinc + '\'' +
                '}';
    }
}
