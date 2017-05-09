package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tempus.proyectos.bluetoothSerial.BluetoothSuperAdmin;
import com.tempus.proyectos.bluetoothSerial.MainHandPunch;
import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.queries.QueriesBiometrias;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ActivityGeomano extends Activity {

    /* --- Declaración de Objetos --- */

    static UserInterfaceM ui;
    Utilities util;

    Button btnConectar;
    Button btnVerificar;
    Button btnEnrolar;

    Button btnConsultarMano;
    EditText edtManoDocumentoG;
    static ListView lstMano;
    static Button btnExitLstMano;
    static TextView txvLayerLstMano;
    static TextView txvCabeceraLstMano;


    static TextView txvManoEmpA1;
    static TextView txvManoEmpA2;
    static Button btnAcMano1;
    static Button btnAcMano2;
    static Button btnAcMano3;
    static TextView txvActMano1;
    static TextView txvActMano2;
    static TextView txvManoEmpresaCorner;
    static Button btnConsultarHuella;
    static TextView txvManoNombrePersonal;
    static EditText edtManoDocumento;

    BluetoothSuperAdmin socket01;
    MainHandPunch hp;

    /* --- Declaración de Variables Globales --- */

    List<Button> lstHuellaBtn;
    List<TextView> lstHuellaTxv;

    public static Biometrias objBiometriaList;
    public static QueriesBiometrias queriesBiometrias;
    public static List<Biometrias> biometriasList;

    // Parametros para enrolar

    public static String nombre;
    public static String empresaCodigo;
    public static String empresaNombre;
    public static String codigo;
    public static String template;
    public static int indice;
    public static int idTipoDetaBio;

    public static Biometrias objEspacio01;
    public static Biometrias objEspacio02;

    public static String valorTarjeta;


    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    static ImageView btnMasterGeomano;
    public static TextView led01, led02, led03, led04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_geomano);
        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        util = new Utilities();

        socket01 = new BluetoothSuperAdmin("20:16:08:10:65:94");
        hp = new MainHandPunch(this);

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Geomano";

        lstHuellaBtn = new ArrayList<Button>();
        lstHuellaTxv = new ArrayList<TextView>();

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterGeomano = (ImageView) findViewById(R.id.btnMasterGeomano);

        led01 = (TextView) findViewById(R.id.led01);
        led02 = (TextView) findViewById(R.id.led02);
        led03 = (TextView) findViewById(R.id.led03);
        led04 = (TextView) findViewById(R.id.led04);

        btnConsultarMano = (Button) findViewById(R.id.btnConsultarMano);
        edtManoDocumentoG = (EditText) findViewById(R.id.edtManoDocumentoG);
        txvManoNombrePersonal = (TextView) findViewById(R.id.txvManoNombrePersonal);

        btnExitLstMano = (Button) findViewById(R.id.btnExitLstMano);
        txvLayerLstMano= (TextView) findViewById(R.id.txvLayerLstMano);
        lstMano = (ListView) findViewById(R.id.lstMano);
        txvCabeceraLstMano = (TextView) findViewById(R.id.txvCabeceraLstMano);

        //btnConectar = (Button) findViewById(R.id.btnConectar);
        //btnVerificar = (Button) findViewById(R.id.btnVerificar);
        //btnEnrolar = (Button) findViewById(R.id.btnEnrolar);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);
        //limpiarScreen();
        manageEnrollEmp(false,"");
        //manageScreenEnroll(false);
        manageScreenListaMano(false);

        /* --- Inicialización de Parametros Generales --- */

        /* --- Eventos --- */

        btnMasterGeomano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityGeomano.this, ActivityMenu.class , "","");
            }
        });


        btnExitLstMano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageScreenListaMano(false);
            }
        });

        btnConsultarMano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = edtManoDocumentoG.getText().toString();

                if (numero.isEmpty()){
                    ui.showAlert(ActivityGeomano.this,"info","Debe ingresar un número válido");
                } else {

                    List<String> lista = new ArrayList<String>();

                    try {
                        Log.d("TEMPUS",numero);
                        queriesBiometrias = new QueriesBiometrias(ActivityGeomano.this);
                        biometriasList = queriesBiometrias.ListarPersonalBiometria(7,numero);

                        int cantidad = biometriasList.size();

                        if ( cantidad > 0 ) {
                            for(int i = 0; i < biometriasList.size(); i++){

                                Log.d("Autorizaciones",biometriasList.get(i).toString());
                                String empresa = biometriasList.get(i).getEmpresa();
                                String codigo = biometriasList.get(i).getCodigo();
                                String nrodocumento = biometriasList.get(i).getNroDocumento();
                                String apellidoPaterno = biometriasList.get(i).getApellidoPaterno();
                                int flag = biometriasList.get(i).getFlagPerTipoLectTerm();

                                String registro = String.valueOf(flag) + "|   " + empresa + "   " + codigo + "   " + nrodocumento + "   " + apellidoPaterno;
                                lista.add(registro);
                            }

                            ArrayAdapter<String> test = new ArrayAdapter<String>(ActivityGeomano.this,android.R.layout.simple_list_item_1, android.R.id.text1, lista){

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent){

                                    View view = super.getView(position, convertView, parent);

                                    final TextView ListItemShow = (TextView) view.findViewById(android.R.id.text1);

                                    if (ListItemShow.getText().toString().contains("1|")){
                                        ListItemShow.setBackgroundColor(Color.parseColor("#5db85d"));
                                    } else {
                                        ListItemShow.setBackgroundColor(Color.parseColor("#333333"));
                                    }
                                    return view;
                                }
                            };

                            lstMano.setAdapter(test);

                            manageScreenListaMano(true);

                        } else {
                            ui.showAlert(ActivityGeomano.this,"warning","Cod/Doc no se reconoce");
                        }

                    } catch (Exception e ){
                        Log.e("ERROR",e.getMessage());
                    }

                }
            }
        });


        lstMano.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                objBiometriaList =  biometriasList.get(position);
                analizarRegistroBiometriaList(ActivityGeomano.this);
            }
        });


        /*

        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket01.Connect();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String acumulador = "";

                        while (true) {

                            try {
                                byte[] rawBytes = new byte[1];
                                socket01.getInputStream().read(rawBytes);
                                acumulador = acumulador + util.byteArrayToHexString(rawBytes);
                                if (acumulador.length()>=20){
                                    Log.d("HandPunch", "LLEGO: " + acumulador);
                                    acumulador = "";
                                }

                            } catch (Exception e) {
                                Log.e("HandPunch",e.getMessage());
                            }

                        }
                    }
                });

                //t.start();
            }
        });


        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HandPunch","Boton Verificar");

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        hp.SerialHandPunch(socket01.getOutputStream(), socket01.getInputStream(), "ABORT", null);
                        util.sleep(50);

                        hp.SerialHandPunch(socket01.getOutputStream(), socket01.getInputStream(), "VERIFY_ON_EXTERNAL_DATA", "707d756a8189896e27");
                        util.sleep(50);

                        boolean continuar = true;

                        while (continuar) {
                            String res = hp.SerialHandPunch(socket01.getOutputStream(), socket01.getInputStream(), "SEND_STATUS_CRC", null);

                            String tmp = hp.OperarStatus(res);

                            if (tmp.equalsIgnoreCase("Exito")){
                                Log.d("HandPunch","EXITO");
                                continuar = false;
                            }

                            if (tmp.equalsIgnoreCase("Fallo")){
                                Log.d("HandPunch","FALLO");
                                continuar = false;
                            }

                            util.sleep(50);
                        }

                        hp.SerialHandPunch(socket01.getOutputStream(), socket01.getInputStream(), "SEND_TEMPLATE", null);
                        util.sleep(50);
                    }
                });

                t.start();

            }
        });



        btnEnrolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("HandPunch","Boton Enrolar");

                boolean cancel = false;

                hp.SerialHandPunch(socket01.getOutputStream(), socket01.getInputStream(), "ABORT", null);
                util.sleep(50);

                hp.SerialHandPunch(socket01.getOutputStream(), socket01.getInputStream(), "ENROLL_USER", null);
                util.sleep(50);

                boolean continuar = true;

                while (continuar) {

                    String res = hp.SerialHandPunch(socket01.getOutputStream(), socket01.getInputStream(), "SEND_STATUS_CRC", null);

                    String tmp = hp.OperarStatus(res);

                    if (tmp.equalsIgnoreCase("Exito")){
                        Log.d("HandPunch","EXITO");
                        continuar = false;
                    }

                    if (tmp.equalsIgnoreCase("Fallo")){
                        Log.d("HandPunch","FALLO");
                        continuar = false;
                    }

                    util.sleep(50);

                }

                hp.SerialHandPunch(socket01.getOutputStream(), socket01.getInputStream(), "SEND_TEMPLATE", null);
                util.sleep(50);
            }
        });

        */

    }


    public static void analizarRegistroBiometriaList(Activity activity){

        int permisos = objBiometriaList.getFlagPerTipoLectTerm();

        if (permisos == 1){
            nombre = objBiometriaList.getNombres() + " " + objBiometriaList.getApellidoPaterno() + " " + objBiometriaList.getApellidoMaterno();
            empresaCodigo = objBiometriaList.getEmpresa().split("-")[0];
            empresaNombre = objBiometriaList.getEmpresa().split("-")[1];
            codigo = objBiometriaList.getCodigo();
            template = "";

            Log.d("TEMPUS: ","MANO SELECCIONADA: " + objBiometriaList.toString());

            manageScreenListaMano(false);

            try {
                queriesBiometrias = new QueriesBiometrias(activity);
                List<Biometrias> espaciosBiometrias = queriesBiometrias.BuscarBiometrias(7,empresaCodigo,codigo);
                int cantidad = espaciosBiometrias.size();

                if (cantidad == 2 ) {
                    for(int i = 0; i < espaciosBiometrias.size(); i++){
                        Log.d("Autorizaciones",espaciosBiometrias.get(i).toString());
                    }

                    // Espacio 01
                    int espacio01 = espaciosBiometrias.get(0).getValorBiometria();
                    objEspacio01 = espaciosBiometrias.get(0);
                    // Espacio 02
                    int espacio02 = espaciosBiometrias.get(1).getValorBiometria();
                    objEspacio02 = espaciosBiometrias.get(1);


                    indice = espaciosBiometrias.get(0).getIndiceBiometria();
                    Log.e("TEMPUS: ", ":::::::::::::: " + String.valueOf(indice));

                    txvManoNombrePersonal.setText(nombre);

                    manageEnrollEmp(true,empresaNombre);

                    valorTarjeta = objBiometriaList.getNroDocumento();

                    // Analisis de espacios para enrolar
                    if (espacio01 == 0) {
                        //Enrolar Huella 1
                        manageButtonsAction(true,false,false);
                    } else {
                        if (espacio02 == 0) {
                            //Enrolar Huella 2
                            manageButtonsAction(false,true,false);
                        } else {
                            //Eliminar Huellas
                            manageButtonsAction(false,false,true);
                        }
                    }
                } else {
                    ui.showAlert(activity,"warning","Proceso no Soportado (!=2)");
                }

            } catch (Exception e){
                Log.e("Error: ",e.getMessage());
            }
        } else {
            ui.showAlert(activity,"warning","Registro no tiene permisos");
        }
    }

    public static void manageScreenListaMano(boolean visible) {
        if (visible) {
            btnExitLstMano.setVisibility(View.VISIBLE);
            txvLayerLstMano.setVisibility(View.VISIBLE);
            lstMano.setVisibility(View.VISIBLE);
            txvCabeceraLstMano.setVisibility(View.VISIBLE);
            btnMasterGeomano.setClickable(false);
            //btnConsultarHuella.setClickable(false);
            //edtManoDocumento.setEnabled(false);
        } else {
            btnExitLstMano.setVisibility(View.INVISIBLE);
            txvLayerLstMano.setVisibility(View.INVISIBLE);
            lstMano.setVisibility(View.INVISIBLE);
            txvCabeceraLstMano.setVisibility(View.INVISIBLE);
            btnMasterGeomano.setClickable(true);
            //btnConsultarHuella.setClickable(true);
            //edtManoDocumento.setEnabled(true);
        }
    }


    public static void manageEnrollEmp(boolean visible,String empresa) {
        if (visible) {
            //btnAcMano1.setVisibility(View.VISIBLE);
            //btnAcMano2.setVisibility(View.VISIBLE);
            //btnAcMano3.setVisibility(View.VISIBLE);
            //txvActMano1.setVisibility(View.VISIBLE);
            //txvActMano2.setVisibility(View.VISIBLE);
            //txvManoEmpA1.setVisibility(View.VISIBLE);
            //txvManoEmpA2.setVisibility(View.VISIBLE);
            //txvManoEmpresaCorner.setVisibility(View.VISIBLE);
            //txvManoEmpresaCorner.setText(empresa);
        } else {
            //btnAcMano1.setVisibility(View.INVISIBLE);
            //btnAcMano2.setVisibility(View.INVISIBLE);
            //btnAcMano3.setVisibility(View.INVISIBLE);
            //txvActMano1.setVisibility(View.INVISIBLE);
            //txvActMano2.setVisibility(View.INVISIBLE);
            //txvManoEmpA1.setVisibility(View.INVISIBLE);
            //txvManoEmpA2.setVisibility(View.INVISIBLE);
            //txvManoEmpresaCorner.setVisibility(View.INVISIBLE);
            //txvManoEmpresaCorner.setText(empresa);
        }

    }

    public static void manageButtonsAction(boolean btn1, boolean btn2, boolean btn3) {
        btnAcMano1.setEnabled(btn1);
        btnAcMano2.setEnabled(btn2);
        btnAcMano3.setEnabled(btn3);
    }

    public void limpiarScreen(){
        txvManoNombrePersonal.setText("");
        btnAcMano1.setEnabled(false);
        btnAcMano2.setEnabled(false);
        btnAcMano3.setEnabled(false);
    }

}
