package com.tempus.proyectos.usbSerial;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.util.Log;


import com.tempus.proyectos.tempusx.ActivityPrincipal;
import com.tempus.proyectos.util.Utilities;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * Created by gurrutiam on 11/01/2018.
 */

public class UsbInterface {

    private static final String ACTION_USB_PERMISSION ="com.tempus.proyectos.USB_PERMISSION";
    Utilities util = new Utilities();
    private final String TAG = "US-UI";

    private static final String CAB_INI = "245458";
    private static final String CAB_COMMAND = "04";
    private static final String CAB_PARAM = "0000";
    private static final String CAB_FLAG = "00";
    private static final String CAB_FIN = "5A";

    private UsbAccessory accessory;
    private PendingIntent mPermissionIntent;
    private ParcelFileDescriptor filedescriptor = null;

    private UsbManager usbmanager;
    public FileOutputStream outData;
    public FileInputStream inData;
    private byte[] rawBytes = new byte[4096];

    private boolean mPermissionRequestPending = false;


    private static final String BUZZER_CHANNEL = "07";
    private static final String BUZZER_DATA = "244F41584100133530303030303030303030303030303030049541";

    private static final String ADC_CHANNEL = "07";
    private static final String ADC_DATA = "244F4158410013443030303030303030303030303030303004A441";

    private static final String PERIF_OFF_CHANNEL = "07";
    private static final String PERIF_OFF_DATA = "244F4158410013423030304E30303131304E4E3030304E4E053A41";

    private static final String PERIF_ON_CHANNEL = "07";
    private static final String PERIF_ON_DATA = "244F4158410013423131314E30303131314E4E3030314E4E053F41";

    private static final String HUELLA_OFF_CHANNEL = "00";
    private static final String HUELLA_OFF_DATA = "4001000000003000000084F50A";

    private static final String HUELLA_ON_CHANNEL = "00";
    private static final String HUELLA_ON_DATA = "4001000000003100000084F60A";

    private static final String ETHERNET_CHANNEL = "03";
    private static final String ETHERNET_DATA = "4f3d442646494c454e414d453d46494c455f35613533646536643938353239";
    private static final String ETHERNET_DATA_2 = "4f5043494f4e3d47454e455241525f4441544126444154413d7b2273657373696f6e223a31313437352c226c6c616d616461223a223539345f455845435f4c32222c22706172616d6574726f73223a222753594e435f4645434841484f52415f5458272c27272c27272c2720272c27272c27272c274c4f54455f44415441272c2731272c2727227d";
    private static final String ETHERNET_DATA_3 = "OPCION=GENERAR_DATA&DATA={\"session\":11475,\"llamada\":\"594_EXEC_L2\",\"parametros\":\"'SYNC_FECHAHORA_TX','','',' ','','','LOTE_DATA','1',''\"}";
    private static final String ETHERNET_DATA_4 = "OPCION=GENERAR_DATA&DATA={\"session\":11475,\"llamada\":\"594_EXEC_L2\",\"parametros\":\"'SYNC_ESTADOS_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,27/11/2015 16:37:26.000'\"}";
    private static final String ETHERNET_DATA_5 = "OPCION=GENERAR_DATA&DATA={\"session\":11475,\"llamada\":\"594_EXEC_L2\",\"parametros\":\"'SYNC_EMPRESAS_TX','','',' ','','','LOTE_DATA','1','pFECHA_HORA_SINC,27/11/2015 16:37:15.000'\"}";

    // cabecera almacena la cabecera del protocolo, por ejemplo: 245458040300000088008f5A
    String cabecera = "";
    // checksumCabecera almacena la suma de los bytes de la cabecera (no se suma el inicio ni el fin de la cabecera)
    String checksumCabecera = "";
    // paquete almacena el mensaje entre dispositivos, por ejemplo: entre la tablet y el huellero
    String paquete = "";
    // checksumPaquete almacena la suma de los bytes el paquete
    String checksumPaquete = "";
    // lenPaquete almacena la longitud del paquete, cada byte '0F' por ejemplo es 2 len
    int lenPaquete = 0;
    // lenProtocolo almacena la longitud de toda la cadena recibida (cabecera + data)
    int lenProtocolo = 0;

    public UsbInterface() {
        super();
        usbmanager = (UsbManager) ActivityPrincipal.context.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(ActivityPrincipal.context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        ActivityPrincipal.context.registerReceiver(mUsbReceiver, filter);
        //inData = null;
        //outData = null;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Log.v(TAG,"Allow USB Permission");
                        OpenAccessory();
                    } else {
                        Log.v(TAG,"Deny USB Permission");
                    }
                    mPermissionRequestPending = false;
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                DestroyAccessory();
            }
        }
    };

    /**
     metodo que sera llamado para enviar informacion hacia el host usb
     este mensaje compuesto he implementado con su cabecera y checksum

     @param data cuerpo del mesaje a enviar en exastring
     @param channel numero del canal adonde  se enviara el mensaje
     @param hex determina el formato de @param data, true = hexa por ejemplo '24545804030000002900305a...' y false = assci por ejemplo 'OPCION=GENERAR_DATA&DATA={"sessio...'

     */
    public void sendData(String data, String channel, boolean hex) {

        // Verificar data es hexa o ascii
        if(hex){
            // data ya esta en el formato que se necesita
        }else{
            // data es ascii, se transforma en hexa
            data = util.asciiToHex(data);
        }
        Log.v(TAG,"data " + util.convertHexToString(data));
        String preCab = CAB_COMMAND + channel + CAB_PARAM + util.getSizeData(data) + CAB_FLAG;
        String checkSumCab = util.getChecksum(preCab, 2); // de la cabecera

        String newData = CAB_INI + preCab + checkSumCab + CAB_FIN + data + util.getChecksum(data, 4);
        Log.v(TAG, "sendData(" + data.length() + "," + channel + ") " + newData);
        write(util.hexStringToByteArray(newData));
    }


    public boolean isAttach() {
        return outData != null && inData != null;
    }

    private void write(byte[] data) {
        if (this.outData != null) {
            try {
                this.outData.write(data);
            } catch (IOException e) {
                Log.e(TAG, "write: " + e.getMessage(), e);
            }
        }
    }

    public int ResumeAccessory() {
        if (inData != null && outData != null) {
            return 1;
        }
        UsbAccessory[] accessories = usbmanager.getAccessoryList();
        if (accessories == null) {
            return 2;
        }
        accessory = (accessories == null ? null : accessories[0]);
        if (accessory != null) {
            Log.v(TAG, "host encontrado");
            if (usbmanager.hasPermission(accessory)) {
                Log.v(TAG,"OpenAccessory");
                OpenAccessory();
            } else {
                Log.v(TAG,"synchronized mUsbReceiver");
                synchronized (mUsbReceiver) {
                    if (!mPermissionRequestPending) {
                        Log.v(TAG,"Request USB Permission");
                        usbmanager.requestPermission(accessory, mPermissionIntent);
                        mPermissionRequestPending = true;
                    }
                }
            }
        }
        return 0;
    }

    public void OpenAccessory() {
        Log.v(TAG,"OpenAccessory " + accessory);
        filedescriptor = usbmanager.openAccessory(accessory);
        if (filedescriptor != null) {
            FileDescriptor fd = filedescriptor.getFileDescriptor();
            inData = new FileInputStream(fd);
            outData = new FileOutputStream(fd);

            // Iniciar hilo para lectura de data (recibir data)
            UsbRead usbRead = new UsbRead("usbRead");
            usbRead.start();

            // Iniciar hilo para escritura de data (enviar data)
            UsbWrite usbWrite = new UsbWrite("usbWrite");
            usbWrite.start();

        }else{
            Log.v(TAG,"filedescriptor " + filedescriptor);
        }
    }

    public void DestroyAccessory() {
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            Log.e(TAG, "DestroyAccessory: " + e.getMessage(), e);
        }
        CloseAccessory();
    }

    private void CloseAccessory() {
        try {
            if (filedescriptor != null)
                filedescriptor.close();
        } catch (IOException e) {
            Log.v(TAG,"CloseAccessory filedescriptor " + e.getMessage());
        }

        try {
            if (inData != null)
                inData.close();
        } catch (IOException e) {
            Log.v(TAG,"CloseAccessory inData " + e.getMessage());
        }

        try {
            if (outData != null)
                outData.close();
        } catch (IOException e) {
            Log.v(TAG,"CloseAccessory outData " + e.getMessage());
        }
        filedescriptor = null;
        inData = null;
        outData = null;
        Log.v(TAG,"close connection");
    }

    public String getFileContent( FileInputStream fis ) {
        StringBuilder sb = new StringBuilder();
        try{
            Reader r = new InputStreamReader(fis, "UTF-8");  //or whatever encoding
            int ch = r.read();
            while(ch >= 0) {
                sb.append(ch);
                ch = r.read();
            }
        }catch (Exception e){
            Log.v(TAG,"getFileContent " + e.getMessage());
        }
        return sb.toString();
    }

    private class UsbRead extends Thread {
        private Thread hilo;
        private String nombreHilo;


        UsbRead(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        private String getPaquete(String receivedData){


            Log.v(TAG,"receivedData " + receivedData);
            if(receivedData.length() >= 24){
                cabecera = receivedData.substring(0,24);
                checksumCabecera = cabecera.substring(20,22);
                Log.v(TAG,"cabecera " + cabecera);
                Log.v(TAG,"checksumCabecera " + checksumCabecera + " -> " + util.getChecksum(cabecera.substring(6,18),2));

                // Verificar si la cabecera cumple con tener el INICIO 245458 ($TX) y FIN (Z)
                if(cabecera.substring(0,6).equalsIgnoreCase("245458") && cabecera.substring(cabecera.length()-2).equalsIgnoreCase("5a")){

                    // Verificar el checksum de la cabecera
                    if(checksumCabecera.equalsIgnoreCase(util.getChecksum(cabecera.substring(6,18),2))){

                        // Obtener la longitud del paquete
                        lenPaquete = util.convertHexToDecimal(receivedData.substring(14,18));
                        lenProtocolo = 24 + lenPaquete * 2 + 4;
                        Log.v(TAG,"lenPaquete " + lenPaquete + " Longitud " + util.convertHexToDecimal(receivedData.substring(14,18)));

                        // Verificar si la data recibida es igual/mayor que la longitud
                        if(receivedData.length() >= lenProtocolo){
                            paquete = receivedData.substring(24, lenProtocolo - 4);
                            checksumPaquete = receivedData.substring(lenProtocolo - 4, lenProtocolo);
                            Log.v(TAG,"paquete " + paquete);
                            Log.v(TAG,"checksumPaquete " + checksumPaquete + " -> " + util.getChecksum(paquete,4));

                            // Verificar el checksum del paquete
                            if(checksumPaquete.equalsIgnoreCase(util.getChecksum(paquete,4))){
                                Log.v(TAG,"checksumPaquete OK");
                                Log.v(TAG,"paquete>>>" + util.convertHexToString(paquete));
                            }else{
                                Log.v(TAG,"receivedData checksumPaquete no es válido");
                            }

                        }else{
                            Log.v(TAG,"receivedData lenProtocolo no cumple con la longitud necesaria");
                        }

                    }else{
                        Log.v(TAG,"receivedData checksumCabecera no es válido");
                    }

                }else{
                    Log.v(TAG,"receivedData cabecera no cumple con INICIO y FIN");
                }


            }


            return "";
        }

        public void run() {
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
            try{
                while (true) {
                    try{
                        //Thread.sleep(250);
                        //Log.v(TAG,"Inicio de lectura");

                        if(inData == null){
                            Log.v(TAG,"inData " + inData.toString());
                        }else{
                            //Log.v(TAG,"inData ok " + inData.toString());
                            //Log.v(TAG,"getFileContent " + getFileContent(inData));
                            int len = inData.read(rawBytes);
                            //Log.v(TAG, "len bytes: " + len);

                            String hexString = util.byteArrayToHexString(rawBytes).substring(0, len * 2);
                            //Log.v(TAG, "received paquete: " + hexString);
                            //Log.v(TAG,">>>" + new String(rawBytes));
                            getPaquete(hexString);

                        }

                    }catch(Exception e) {
                        Log.e(TAG, "UsbRead " + e.getMessage());
                        if (inData == null) {
                            Log.e(TAG, "run: inData is null");
                        }
                    }
                }
            }catch (Exception e){
                Log.v(TAG,"UsbRead run" + e.getMessage());
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


    private class UsbWrite extends Thread {
        private Thread hilo;
        private String nombreHilo;

        UsbWrite(String nombreHilo) {
            this.nombreHilo = nombreHilo;
            Log.v(TAG,"Creando Hilo " + nombreHilo);
        }

        public void run() {
            Log.v(TAG,"Ejecutando Hilo " + nombreHilo);
            try{
                while (true) {
                    try{
                        // Consultar si logró conectarse a USB
                        boolean connected = isAttach();

                        // Si esta conectado se puede escribir (enviar paquete)
                        if (connected) {
                            //Log.v(TAG, "run: conectado");
                            //Log.v(TAG,"BUZZER_DATA=" + BUZZER_DATA);
                            //usbInterface.sendData(BUZZER_DATA, BUZZER_CHANNEL);
                            // Log.v(TAG,"ETHERNET_DATA=" + ETHERNET_DATA);
                            sendData(ETHERNET_DATA_5, ETHERNET_CHANNEL,false);
                            Thread.sleep(10000);
                        }
                    }catch(Exception e) {
                        try{
                            Thread.sleep(1000);
                        }catch (Exception ex){

                        }
                        Log.e(TAG, "UsbWrite " + e.getMessage());
                        if (inData == null) {
                            Log.e(TAG, "run: inData is null");
                        }
                    }
                }
            }catch (Exception e){
                Log.v(TAG,"UsbWrite run" + e.getMessage());
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

}
