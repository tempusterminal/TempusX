package com.tempus.proyectos.data.model;

/**
 * Created by gurrutiam on 07/11/2016.
 */

public class Marcaciones {

    public String Empresa;
    public String Codigo;
    public String Fechahora;
    public String ValorTarjeta;
    public String HoraTxt;
    public String EntSal;
    public String Flag;
    public String Fecha;
    public String Hora;
    public String Idterminal;
    public int IdTipoLect;
    public String FlgActividad;
    public int IdUsuario;
    public String TmpListar;
    public int Autorizado;
    public int TipoOperacion;
    public int Sincronizado;
    public String Datos;
    public int ValorDatoContenido;

    public Marcaciones () {

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

    public String getFechahora() {
        return Fechahora;
    }

    public void setFechahora(String fechahora) {
        Fechahora = fechahora;
    }

    public String getValorTarjeta() {
        return ValorTarjeta;
    }

    public void setValorTarjeta(String valorTarjeta) {
        ValorTarjeta = valorTarjeta;
    }

    public String getHoraTxt() {
        return HoraTxt;
    }

    public void setHoraTxt(String horaTxt) {
        HoraTxt = horaTxt;
    }

    public String getEntSal() {
        return EntSal;
    }

    public void setEntSal(String entSal) {
        EntSal = entSal;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
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

    public String getFlgActividad() {
        return FlgActividad;
    }

    public void setFlgActividad(String flgActividad) {
        FlgActividad = flgActividad;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getTmpListar() {
        return TmpListar;
    }

    public void setTmpListar(String tmpListar) {
        TmpListar = tmpListar;
    }

    public int getAutorizado() {
        return Autorizado;
    }

    public void setAutorizado(int autorizado) {
        Autorizado = autorizado;
    }

    public int getTipoOperacion() {
        return TipoOperacion;
    }

    public void setTipoOperacion(int tipoOperacion) {
        TipoOperacion = tipoOperacion;
    }

    public int getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(int sincronizado) {
        Sincronizado = sincronizado;
    }

    public String getDatos() {
        return Datos;
    }

    public void setDatos(String datos) {
        Datos = datos;
    }

    public int getValorDatoContenido() {
        return ValorDatoContenido;
    }

    public void setValorDatoContenido(int valorDatoContenido) {
        ValorDatoContenido = valorDatoContenido;
    }

    @Override
    public String toString() {
        return "Marcaciones{" +
                "Empresa='" + Empresa + '\'' +
                ", Codigo='" + Codigo + '\'' +
                ", Fechahora='" + Fechahora + '\'' +
                ", ValorTarjeta='" + ValorTarjeta + '\'' +
                ", HoraTxt='" + HoraTxt + '\'' +
                ", EntSal='" + EntSal + '\'' +
                ", Flag='" + Flag + '\'' +
                ", Fecha='" + Fecha + '\'' +
                ", Hora='" + Hora + '\'' +
                ", Idterminal='" + Idterminal + '\'' +
                ", IdTipoLect=" + IdTipoLect +
                ", FlgActividad='" + FlgActividad + '\'' +
                ", IdUsuario=" + IdUsuario +
                ", TmpListar='" + TmpListar + '\'' +
                ", Autorizado=" + Autorizado +
                ", TipoOperacion=" + TipoOperacion +
                ", Sincronizado=" + Sincronizado +
                ", Datos='" + Datos + '\'' +
                ", ValorDatoContenido=" + ValorDatoContenido +
                '}';
    }
}
