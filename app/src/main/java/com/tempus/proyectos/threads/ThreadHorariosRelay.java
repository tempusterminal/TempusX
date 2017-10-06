package com.tempus.proyectos.threads;

import android.util.Log;

import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Utilities;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gurrutiam on 13/09/2017.
 */

public class ThreadHorariosRelay {

    String TAG = "TH-HR";

    public static ArrayList<ArrayList<String>> arrayListHorariosRelay = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<String>> arrayListRelays = new ArrayList<ArrayList<String>>();
    public static boolean recargarHorariosRelayActivos = false;
    public static boolean ActivarstartRelayReading = false;

    QueriesParameters queriesParameters;

    Utilities utilities = new Utilities();

    public ThreadHorariosRelay() {
        try{
            queriesParameters = new QueriesParameters(ActivityPrincipal.context);
            if(arrayListHorariosRelay.size() == 0){
                arrayListHorariosRelay = parametersToMatrix(queriesParameters.idparameterToValue("HORARIOSRELAY"));
            }
            if(arrayListRelays.size() == 0){
                arrayListRelays = parametersToMatrix(queriesParameters.idparameterToValue("RELAYS"));
            }
        }catch (Exception e){
            Log.e(TAG,"ThreadHorariosRelay Constructor " + e.getMessage());
        }
    }

    public ArrayList<ArrayList<String>> parametersToMatrix(String parameters){
        ArrayList<ArrayList<String>> matrixY = new ArrayList<ArrayList<String>>();
        ArrayList<String> matrixX = new ArrayList<String>();
        try{
            //RELAY_01,0,00:00;RELAY_02,0,00:00
            String[] arrayY = parameters.split(";");
            //arrayY[0] = [RELAY_01,0,00:00]
            //arrayY[1] = [RELAY_02,0,00:00]
            for(int i = 0; i < arrayY.length; i++){
                String[] arrayX = arrayY[i].split(",");
                // arrayX[0] = [RELAY_01]
                // arrayX[1] = [0]
                // arrayX[2] = [00:00]
                matrixX = new ArrayList<String>();
                for(int y = 0; y < arrayX.length; y++){
                    matrixX.add(y,arrayX[y]);
                    //Log.v(TAG,"matrixX " + matrixX.toString());
                }
                matrixY.add(i,matrixX);
                //Log.v(TAG,"matrixY " + matrixY.toString());
            }
        }catch (Exception e){
            Log.e(TAG,"parametersToMatrix " + e.getMessage());
            matrixY.clear();
        }
        Log.v(TAG,"parametersToMatrix " + matrixY.toString());
        return matrixY;
    }

    public void writeToArduino(OutputStream out, String opcion, boolean turnon) {

        //244F415841 0013 42 3131313131303031
        String cabecera = "244F415841";

        String longitud = "0013";

        String mensaje = "";
        String checksum = "0000";
        String cola = "41";

        //cabecera = "$0AXA";
        //longitud = "";
        //checksum = "";
        //cola = "A";

        switch (opcion){
            case "RELAY_01_RELAY_02":
                longitud = "0013";
                if(turnon){
                    mensaje = "42" + "4e4e4e4e 4e4e 4e4e 4e4e4e 3131 4e4e4e" + "00";
                }else{
                    mensaje = "42" + "4e4e4e4e 4e4e 4e4e 4e4e4e 3030 4e4e4e" + "00";
                }
                //mensaje = "42" + "31313131 3130 3131 313030 3131 303030" + "00";
                break;
            case "RELAY_01":
                longitud = "0013";
                if(turnon){
                    mensaje = "42" + "4e4e4e4e 4e4e 4e4e 4e4e4e 4e31 4e4e4e" + "00";
                }else{
                    mensaje = "42" + "4e4e4e4e 4e4e 4e4e 4e4e4e 4e30 4e4e4e" + "00";
                }
                //mensaje = "42" + "31313131 3130 3131 313030 3031 303030" + "00";
                break;
            case "RELAY_02":
                longitud = "0013";
                if(turnon){
                    mensaje = "42" + "4e4e4e4e 4e4e 4e4e 4e4e4e 314e 4e4e4e" + "00";
                }else{
                    mensaje = "42" + "4e4e4e4e 4e4e 4e4e 4e4e4e 304e 4e4e4e" + "00";
                }
                //mensaje = "42" + "31313131 3130 3131 313030 3130 303030" + "00";
                break;
            case "RELAY_OFF":
                longitud = "0013";
                mensaje = "42" + "4e4e4e4e 4e4e 4e4e 4e4e4e 3030 4e4e4e" + "00";
                //mensaje = "42" + "31313131 3130 3131 313030 3030 303030" + "00";
                break;
            default:
                break;
        }

        mensaje = mensaje.replace(" ","");
        String tramaFinal = cabecera + longitud + mensaje + checksum + cola;

        Log.v(TAG,"Salio - " + opcion + " -> " + tramaFinal);

        Log.v(TAG, "Write Activado");
        try {
            out.write(utilities.hexStringToByteArray(tramaFinal));
            Log.v(TAG, "Write Finalizado");
        } catch (Exception e) {
            Log.e(TAG,"writeToArduino: " + e.getMessage());
        }
    }


    private class HorariosRelayReading extends Thread{
        private Thread hilo;
        private String nombreHilo;

        ArrayList<ArrayList<String>> arrayListHorariosRelayActivos = new ArrayList<ArrayList<String>>();

        private Calendar diaactual;
        private int diaactualtemp;
        private Date horaactual;
        private Date horainicio;
        private Date horafin;

        private boolean horaactiva;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        public void validarHoras(){
            try{
                horaactual = new Date();
                horainicio = new Date();
                horafin = new Date();
                horaactual = simpleDateFormat.parse(simpleDateFormat.format(horaactual));
                // Test para aumentar hora ---------------------------------------------------------
                // horaactual.setTime(70000000 + horaactual.getTime());
                // ---------------------------------------------------------------------------------
                Log.v(TAG,"validarHoras ------------------------------------------------------------");
                horaactiva = false;
                for(int i = 0; i < arrayListHorariosRelayActivos.size(); i++){
                    //HORARIO_RELAY_01,00:00:00,00:00:00,0000000;HORARIO_RELAY_02,00:00:00,00:00:00,0000000 ...
                    horainicio = simpleDateFormat.parse(arrayListHorariosRelayActivos.get(i).get(1));
                    horafin = simpleDateFormat.parse(arrayListHorariosRelayActivos.get(i).get(2));

                    //Log.v(TAG,horainicio.getTime() + " -> " + horaactual.getTime() + " <- " + horafin.getTime() + " (" + arrayListHorariosRelayActivos.get(i).get(3) + ")");
                    Log.v(TAG,arrayListHorariosRelayActivos.get(i).get(0) + " " + simpleDateFormat.format(horainicio) + " -> " + simpleDateFormat.format(horaactual) + " <- " + simpleDateFormat.format(horafin) + " (" + arrayListHorariosRelayActivos.get(i).get(3) + ")");
                    //Se evalua un intervalo entre dos horarios
                    if(horaactual.getTime()>=horainicio.getTime() && horaactual.getTime()<horafin.getTime()){
                        //Log.v(TAG,"horaa OK " + simpleDateFormat.format(horaactual));
                        horaactiva = true;
                        i = arrayListHorariosRelayActivos.size();
                    }else{
                        //Log.v(TAG,"horaa KO " + simpleDateFormat.format(horaactual));
                    }
                }
            }catch (Exception e){
                Log.e(TAG,"validarHoras " + e.getMessage());
            }
        }

        public HorariosRelayReading(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void cargarHorariosRelay(){
            arrayListHorariosRelay = parametersToMatrix(queriesParameters.idparameterToValue("HORARIOSRELAY"));
        }



        public void cargarHorariosRelayActivos(){
            try{
                Log.v(TAG,"cargarHorariosRelayActivos inicio = " + arrayListHorariosRelay.size());
                arrayListHorariosRelayActivos = new ArrayList<ArrayList<String>>();
                for(int i = 0; i < arrayListHorariosRelay.size(); i++){
                    //Log.v(TAG,arrayListHorariosRelay.get(i).toString());
                    if(!arrayListHorariosRelay.get(i).get(1).equalsIgnoreCase(arrayListHorariosRelay.get(i).get(2))){
                        if(validarDia(arrayListHorariosRelay.get(i).get(3))){
                            //Log.v(TAG,"Hoy " + Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                            //Log.v(TAG,"arrayListHorariosRelay.get(i)" + arrayListHorariosRelay.get(i));
                            arrayListHorariosRelayActivos.add(arrayListHorariosRelay.get(i));
                        }
                    }
                }
                diaactualtemp = diaactual.get(Calendar.DAY_OF_WEEK);
            }catch (Exception e){
                Log.e(TAG,"cargarHorariosRelayActivos " + e.getMessage());
            }
        }

        public boolean validarDia(String dias){
            //0000000
            //0011001
            // Domingo = 1
            // Sabado = 7
            diaactual = Calendar.getInstance();
            switch (diaactual.get(Calendar.DAY_OF_WEEK)){
                case 1: //Domingo
                    if(dias.substring(6).equalsIgnoreCase("1")){
                        return true;
                    }else{
                        return false;
                    }
                case 2: //Lunes
                    if(dias.substring(0,1).equalsIgnoreCase("1")){
                        return true;
                    }else{
                        return false;
                    }
                case 3: //Martes
                    if(dias.substring(1,2).equalsIgnoreCase("1")){
                        return true;
                    }else{
                        return false;
                    }
                case 4: //Miercoles
                    if(dias.substring(2,3).equalsIgnoreCase("1")){
                        return true;
                    }else{
                        return false;
                    }
                case 5: //Jueves
                    if(dias.substring(3,4).equalsIgnoreCase("1")){
                        return true;
                    }else{
                        return false;
                    }
                case 6: //Viernes
                    if(dias.substring(4,5).equalsIgnoreCase("1")){
                        return true;
                    }else{
                        return false;
                    }
                case 7: //Sabado
                    if(dias.substring(5,6).equalsIgnoreCase("1")){
                        return true;
                    }else{
                        return false;
                    }
                default:
                    return false;
            }
        }


        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);

            // test para cambiar las horas y dias --------------------------------------------------
            //arrayListHorariosRelay.get(2).set(1,"04:19:33");
            //arrayListHorariosRelay.get(2).set(2,"04:19:58");
            //arrayListHorariosRelay.get(2).set(3,"0001000");
            // -------------------------------------------------------------------------------------

            cargarHorariosRelayActivos();

            while(true){
                try{

                    diaactual = Calendar.getInstance();
                    if(diaactualtemp != diaactual.get(Calendar.DAY_OF_WEEK)){
                        cargarHorariosRelayActivos();
                    }

                    if(recargarHorariosRelayActivos){
                        cargarHorariosRelayActivos();
                        recargarHorariosRelayActivos = false;
                    }

                    validarHoras();

                    Log.v(TAG,"RELAY_01="+ActivityPrincipal.turnOnRelay01 + " " + "RELAY_02=" + ActivityPrincipal.turnOnRelay02);
                    if(!ActivarstartRelayReading){
                        if(horaactiva){
                            boolean relay_01 = false;
                            boolean relay_02 = false;

                            if(arrayListRelays.get(0).get(1).equalsIgnoreCase("4")){
                                relay_01 = true;
                            }
                            if(arrayListRelays.get(1).get(1).equalsIgnoreCase("4")){
                                relay_02 = true;
                            }

                            if(relay_01 && relay_02){
                                if(relay_01){
                                    if (ActivityPrincipal.turnOnRelay01 == 0 || ActivityPrincipal.turnOnRelay02 == 0) {
                                        writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(), "RELAY_01_RELAY_02", relay_01);
                                        //Log.v(TAG,"RELAY_01 ON");
                                    }
                                }else{
                                    if (ActivityPrincipal.turnOnRelay01 == 1 || ActivityPrincipal.turnOnRelay02 == 1) {
                                        writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(), "RELAY_01_RELAY_02", relay_01);
                                        //Log.v(TAG,"RELAY_01 ON");
                                    }
                                }
                                Log.v(TAG,"HorariosRelayReading -> RELAY_01 RELAY_02 = " + relay_01);
                                Thread.sleep(200);
                            }else if(relay_01 != relay_02){
                                if(relay_01){
                                    if(ActivityPrincipal.turnOnRelay01 == 0){
                                        writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"RELAY_01",relay_01);
                                        //Log.v(TAG,"RELAY_01 ON");
                                    }
                                }else{
                                    //if(ActivityPrincipal.turnOnRelay01 == 1){
                                    //    writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"RELAY_01",relay_01);
                                    //    //Log.v(TAG,"RELAY_01 ON");
                                    //}
                                }
                                Log.v(TAG,"HorariosRelayReading -> RELAY_01 = " + relay_01);
                                Thread.sleep(100);

                                if(relay_02){
                                    if(ActivityPrincipal.turnOnRelay02 == 0){
                                        writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"RELAY_02",relay_02);
                                        //Log.v(TAG,"RELAY_02 ON");
                                    }
                                }else{
                                    //if(ActivityPrincipal.turnOnRelay02 == 1){
                                    //    writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"RELAY_02",relay_02);
                                    //    //Log.v(TAG,"RELAY_02 ON");
                                    //}
                                }
                                Log.v(TAG,"HorariosRelayReading -> RELAY_02 = " + relay_02);
                                Thread.sleep(100);
                            }


                        }else{
                            if(arrayListRelays.get(0).get(1).equalsIgnoreCase("4") && arrayListRelays.get(1).get(1).equalsIgnoreCase("4")){
                                if (ActivityPrincipal.turnOnRelay01 == 1 || ActivityPrincipal.turnOnRelay02 == 1) {
                                    writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(), "RELAY_01_RELAY_02", false);
                                    //Log.v(TAG,"RELAY_01 ON");
                                }
                                Log.v(TAG,"HorariosRelayReading(off) -> RELAY_01 RELAY_02 = " + false);
                            }else if(arrayListRelays.get(0).get(1).equalsIgnoreCase("4")){
                                if (ActivityPrincipal.turnOnRelay01 == 1) {
                                    writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(), "RELAY_01", false);
                                    //Log.v(TAG,"RELAY_01 ON");
                                }
                                Log.v(TAG,"HorariosRelayReading(off) -> RELAY_01 = " + false);
                            }else if(arrayListRelays.get(1).get(1).equalsIgnoreCase("4")){
                                if (ActivityPrincipal.turnOnRelay02 == 1) {
                                    writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(), "RELAY_02", false);
                                    //Log.v(TAG,"RELAY_01 ON");
                                }
                                Log.v(TAG,"HorariosRelayReading(off) -> RELAY_02 = " + false);
                            }

                            Thread.sleep(200);
                        }
                    }
                    Thread.sleep(800);
                }catch (Exception e){
                    Log.e(TAG,"Error General Hilo " + nombreHilo + ": " + e.getMessage());
                    try{
                        Thread.sleep(1000);
                    }catch(Exception ex){

                    }
                }
            }
        }

        public void start(){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }

    public void startHorariosRelayReading(){
        HorariosRelayReading horariosRelayReading = new HorariosRelayReading("HorariosRelayReading");
        horariosRelayReading.start();
    }

    private class RelayReading extends Thread{
        private Thread hilo;
        private String nombreHilo;
        private int event;
        private int tiempoactivoensegundosrelay;
        private int minutos;
        private int segundos;

        private boolean relay_01;
        private boolean relay_02;


        public RelayReading(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
            relay_01 = false;
            relay_02 = false;
            tiempoactivoensegundosrelay = 0;

            try{
                //RELAY_01,0,00:00
                for(int i = 0; i < arrayListRelays.size(); i++){
                    // Consultar si el evento accionado esta relacionado con los relay
                    if(arrayListRelays.get(i).get(1).equalsIgnoreCase(String.valueOf(event))){
                        //minutos = Integer.valueOf(arrayListRelays.get(i).get(2).substring(0,2));
                        //segundos = Integer.valueOf(arrayListRelays.get(i).get(2).substring(3));
                        segundos = Integer.valueOf(arrayListRelays.get(i).get(2).substring(0,2));
                        //tiempoactivoensegundosrelay = minutos * 60 + segundos;
                        tiempoactivoensegundosrelay = segundos;
                        Log.v(TAG,"tiempoactivoensegundosrelay = " + tiempoactivoensegundosrelay + " segundos");

                        if(arrayListRelays.get(i).get(0).equalsIgnoreCase("RELAY_01")){
                            relay_01 = true;
                            ActivityPrincipal.relay01 = "4e";
                            ActivityPrincipal.tiempoActivoRelay01 = utilities.decimalToHex(tiempoactivoensegundosrelay);
                            Log.v(TAG,"ActivityPrincipal.tiempoActivoRelay01(" + ActivityPrincipal.tiempoActivoRelay01 + ")");
                        }else if(arrayListRelays.get(i).get(0).equalsIgnoreCase("RELAY_02")) {
                            relay_02 = true;
                            ActivityPrincipal.relay02 = "4e";
                            ActivityPrincipal.tiempoActivoRelay02 = utilities.decimalToHex(tiempoactivoensegundosrelay);
                            Log.v(TAG,"ActivityPrincipal.tiempoActivoRelay02(" + ActivityPrincipal.tiempoActivoRelay02 + ")");
                        }
                    }
                }

                //Thread.sleep(500);
                /*
                for(int i = 0; i < tiempoactivoensegundosrelay; i++){
                    ActivarstartRelayReading = true;

                    if(relay_01 && relay_02){
                        if (ActivityPrincipal.turnOnRelay01 == 0 || ActivityPrincipal.turnOnRelay02 == 0) {
                            writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(), "RELAY_01_RELAY_02", relay_01);
                            //Log.v(TAG,"RELAY_01 ON");
                        }
                        Log.v(TAG,"HorariosRelayReading -> RELAY_01 RELAY_02 = " + relay_01);
                        //Thread.sleep(200);
                    }else if(relay_01 != relay_02){
                        if(relay_01){
                            if(ActivityPrincipal.turnOnRelay01 == 0){
                                writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"RELAY_01",relay_01);
                                //Log.v(TAG,"RELAY_01 ON");
                            }
                            Log.v(TAG,"HorariosRelayReading -> RELAY_01 = " + relay_01);
                        }else if(relay_02){
                            if(ActivityPrincipal.turnOnRelay02 == 0){
                                writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),"RELAY_02",relay_02);
                                //Log.v(TAG,"RELAY_02 ON");
                            }
                            Log.v(TAG,"HorariosRelayReading -> RELAY_02 = " + relay_01);
                        }
                        //Thread.sleep(200);
                    }

                    Thread.sleep(1000);
                }
                */

                //ActivarstartRelayReading = false;
                Log.v(TAG,"Fin de ejecución relay");
            }catch (Exception e){
                Log.e(TAG,"Error General Hilo " + nombreHilo + ": " + e.getMessage());
                try{
                    Thread.sleep(1000);
                }catch(Exception ex){

                }
            }

        }

        public void start(int event){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            this.event = event;
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }

    public void startRelayReading(int event){
        RelayReading relayReading = new RelayReading("RelayReading");
        relayReading.start(event);
    }


    private class RelayTesting extends Thread{
        private Thread hilo;
        private String nombreHilo;

        private String relay;


        public RelayTesting(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run(){
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
            ActivarstartRelayReading = true;
            try{

                writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),relay,false);
                Log.v(TAG,"RelayTesting -> " + relay + " = " + false);
                Thread.sleep(1000);
                writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),relay,true);
                Log.v(TAG,"RelayTesting -> " + relay + " = " + true);
                Thread.sleep(1000);
                writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),relay,false);
                Log.v(TAG,"RelayTesting -> " + relay + " = " + false);
                Thread.sleep(1000);
                writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),relay,true);
                Log.v(TAG,"RelayTesting -> " + relay + " = " + true);
                Thread.sleep(2000);
                writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),relay,false);
                Log.v(TAG,"RelayTesting -> " + relay + " = " + false);
                //Thread.sleep(1000);

                Log.v(TAG,"Fin de ejecución relay");
            }catch (Exception e){
                Log.e(TAG,"Error General Hilo " + nombreHilo + ": " + e.getMessage());
                try{
                    Thread.sleep(1000);
                }catch(Exception ex){

                }
            }
            ActivarstartRelayReading = false;

        }

        public void start(String relay){
            //Looper.prepare();
            Log.v(TAG,"Iniciando Hilo " + nombreHilo);
            this.relay = relay;
            if(hilo == null){
                hilo = new Thread(nombreHilo);
                super.start();
            }
        }

    }


    public void startRelayTesting(String relay){
        RelayTesting relayTesting = new RelayTesting("RelayReading");
        relayTesting.start(relay);
    }





}
