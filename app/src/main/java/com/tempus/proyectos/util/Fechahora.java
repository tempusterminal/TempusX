package com.tempus.proyectos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gurrutiam on 21/11/2016.
 */

public class Fechahora {



    public Fechahora() {
    }



    public String getFechahora(){
        DateFormat formatofechahora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fechahora = new Date();
        return formatofechahora.format(fechahora);
    }

    public String getFechahoraFull(){
        DateFormat formatofechahora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date fechahora = new Date();
        return formatofechahora.format(fechahora);
    }

    public String getFechahoraName(){
        DateFormat formatofechahora = new SimpleDateFormat("yyyyMMddHHmmss");
        Date fechahora = new Date();
        return formatofechahora.format(fechahora);
    }

    public String getFecha(String fechahora){
        fechahora = fechahora.substring(0,10);
        return fechahora;
    }

    public String getHora(String fechahora){
        fechahora = fechahora.substring(11);
        return fechahora;
    }

    public String getFechahoracero(String fechahora){ //yyyy-MM-dd HH:mm:ss -> yyyy-MM-dd 00:00:00
        fechahora = fechahora.substring(0,10) + " 00:00:00";
        return fechahora;
    }

    public String getFechacerohora(String fechahora){ //yyyy-MM-dd HH:mm:ss -> 1899-12-30 HH:mm:ss
        fechahora = "1899-12-30 " + fechahora.substring(11);
        return fechahora;
    }

    public String getFechahoraSync(String fechahora){ //yyyy-MM-dd HH:mm:ss -> dd/MM/yyy HH:mm:ss.mmm
        fechahora = fechahora.substring(8,10) + "/" + fechahora.substring(5,7) + "/" + fechahora.substring(0,4) + " " + fechahora.substring(11,fechahora.length());
        return fechahora;
    }

    public String getFechahoraSqlServer(String fechahora){
        fechahora = fechahora.substring(0,5) + fechahora.substring(8,10) + "-" + fechahora.substring(5,7) + " " + fechahora.substring(11,19);
        //2016-14-12 12:37:13
        return fechahora;
    }

    public String getFechahoraFullSqlServer(String fechahora){
        fechahora = fechahora.substring(0,5) + fechahora.substring(8,10) + "-" + fechahora.substring(5,7) + " " + fechahora.substring(11,23);
        //2016-14-12 12:37:13
        return fechahora;
    }

    public String setFileName(String fechahora){
        //yyyy-MM-dd HH:mm:ss -> yyyyMMddHHmmss
        return fechahora.replace("-","").replace(":","").replace(" ","");
    }

    public String getFechaddmmyy(String fechahora){ //yyyy-MM-dd HH:mm:ss -> dd/MM/yy
        fechahora = fechahora.substring(8,10) + "/" + fechahora.substring(5,7) + "/" + fechahora.substring(2,4);
        return fechahora;
    }


}
