package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 19/10/2017.
 */

public class LogTerminal {

    public String Idterminal;
    public String Tag;
    public String Value;
    public String User;
    public int Sincronizado;
    public String Fechahora;

    public LogTerminal() {
    }

    public String getIdterminal() {
        return Idterminal;
    }

    public void setIdterminal(String idterminal) {
        Idterminal = idterminal;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public int getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(int sincronizado) {
        Sincronizado = sincronizado;
    }

    public String getFechahora() {
        return Fechahora;
    }

    public void setFechahora(String fechahora) {
        Fechahora = fechahora;
    }

    @Override
    public String toString() {
        return "LogTerminal{" +
                "Idterminal='" + Idterminal + '\'' +
                ", Tag='" + Tag + '\'' +
                ", Value='" + Value + '\'' +
                ", User='" + User + '\'' +
                ", Sincronizado=" + Sincronizado +
                ", Fechahora='" + Fechahora + '\'' +
                '}';
    }
}
