package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.tempus.proyectos.threads.ThreadHandPunchDelete;
import com.tempus.proyectos.threads.ThreadHandPunchEnroll;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ActivityGeomano extends Activity {

    /* --- Declaración de Objetos --- */

    static UserInterfaceM ui;
    Utilities util;

    static Button btnConsultarMano;
    static EditText edtManoDocumentoG;
    static ListView lstMano;
    static Button btnExitLstMano;
    static TextView txvLayerLstMano;
    static TextView txvCabeceraLstMano;


    static TextView txvManoEmpA1;
    static TextView txvManoEmpA2;
    static Button btnAcMano1;
    static Button btnAcMano3;
    static TextView txvAcMano1;
    static TextView txvManoEmpresaCorner;
    static TextView txvManoNombrePersonal;

    public static boolean ABORTAR = false;

    /* --- Declaración de Variables Globales --- */

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

    public static String valorTarjeta;


    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;

    static ImageView btnMasterGeomano;
    public static TextView led01, led02, led03, led04, txvManoTexto, txvManoFondo;
    public static ImageView imgViewGeomano, imgViewResultOK, imgViewResultKO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geomano);

        // Aumentar el brillo al maximo brillo establecido
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = (float) (ActivityPrincipal.maxBrilloAhorroEnergia/100);
        getWindow().setAttributes(layoutParams);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        util = new Utilities();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Geomano";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        txvFondo1 = (TextView) findViewById(R.id.txvFondo1);
        txvFondo2 = (TextView) findViewById(R.id.txvFondo2);
        txvBarraInf = (TextView) findViewById(R.id.txvBarraInf);
        txvLinea1 = (TextView) findViewById(R.id.txvLinea1);
        txvLinea2 = (TextView) findViewById(R.id.txvLinea2);
        txvLinea3 = (TextView) findViewById(R.id.txvLinea3);
        txvLinea4 = (TextView) findViewById(R.id.txvLinea4);

        ActivityPrincipal.setBackgroundColorOnTextView(txvFondo1,ActivityPrincipal.parametersColorsUI.split(",")[0],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvFondo2,ActivityPrincipal.parametersColorsUI.split(",")[1],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvBarraInf,ActivityPrincipal.parametersColorsUI.split(",")[2],"#cecece");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea1,ActivityPrincipal.parametersColorsUI.split(",")[3],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea2,ActivityPrincipal.parametersColorsUI.split(",")[4],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea3,ActivityPrincipal.parametersColorsUI.split(",")[5],"#777777");
        ActivityPrincipal.setBackgroundColorOnTextView(txvLinea4,ActivityPrincipal.parametersColorsUI.split(",")[6],"#777777");

        btnMasterGeomano = (ImageView) findViewById(R.id.btnMasterGeomano);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterGeomano,"/tempus/img/config/","logo.png");

        txvManoFondo = (TextView) findViewById(R.id.txvManoFondo);
        txvManoTexto = (TextView) findViewById(R.id.txvManoTexto);
        imgViewGeomano = (ImageView) findViewById(R.id.imgViewGeomano);

        led01 = (TextView) findViewById(R.id.led01);
        led02 = (TextView) findViewById(R.id.led02);
        led03 = (TextView) findViewById(R.id.led03);
        led04 = (TextView) findViewById(R.id.led04);

        btnAcMano1 = (Button) findViewById(R.id.btnAcMano1);
        btnAcMano3 = (Button) findViewById(R.id.btnAcMano3);

        txvAcMano1 = (TextView) findViewById(R.id.txvAcMano1);

        txvManoEmpA1 = (TextView) findViewById(R.id.txvManoEmpA1);
        txvManoEmpA2 = (TextView) findViewById(R.id.txvManoEmpA2);

        imgViewResultOK = (ImageView) findViewById(R.id.imgViewResultOK);
        imgViewResultKO = (ImageView) findViewById(R.id.imgViewResultKO);

        btnConsultarMano = (Button) findViewById(R.id.btnConsultarMano);
        edtManoDocumentoG = (EditText) findViewById(R.id.edtManoDocumentoG);
        txvManoNombrePersonal = (TextView) findViewById(R.id.txvManoNombrePersonal);
        txvManoEmpresaCorner = (TextView) findViewById(R.id.txvManoEmpresaCorner);

        btnExitLstMano = (Button) findViewById(R.id.btnExitLstMano);
        txvLayerLstMano = (TextView) findViewById(R.id.txvLayerLstMano);
        lstMano = (ListView) findViewById(R.id.lstMano);
        txvCabeceraLstMano = (TextView) findViewById(R.id.txvCabeceraLstMano);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);
        limpiarScreen();
        manageEnrollEmp(false, "");
        manageScreenEnroll(false);
        manageScreenListaMano(false);

        /* --- Inicialización de Parametros Generales --- */

        /* --- Eventos --- */

        btnMasterGeomano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityGeomano.this, ActivityMenu.class, "", "");
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

                //registrarMano();


                String numero = edtManoDocumentoG.getText().toString();

                if (numero.isEmpty()) {
                    ui.showAlert(ActivityGeomano.this, "info", "Debe ingresar un número válido");
                } else {

                    List<String> lista = new ArrayList<String>();

                    try {
                        Log.d("TEMPUS", numero);
                        queriesBiometrias = new QueriesBiometrias(ActivityGeomano.this);
                        biometriasList = queriesBiometrias.ListarPersonalBiometria(10, numero);

                        int cantidad = biometriasList.size();

                        if (cantidad > 0) {
                            for (int i = 0; i < biometriasList.size(); i++) {

                                Log.d("Autorizaciones", biometriasList.get(i).toString());
                                String empresa = biometriasList.get(i).getEmpresa();
                                String codigo = biometriasList.get(i).getCodigo();
                                String nrodocumento = biometriasList.get(i).getNroDocumento();
                                String apellidoPaterno = biometriasList.get(i).getApellidoPaterno();
                                int flag = biometriasList.get(i).getFlagPerTipoLectTerm();

                                String registro = String.valueOf(flag) + "|   " + empresa + "   " + codigo + "   " + nrodocumento + "   " + apellidoPaterno;
                                lista.add(registro);
                            }

                            ArrayAdapter<String> test = new ArrayAdapter<String>(ActivityGeomano.this, android.R.layout.simple_list_item_1, android.R.id.text1, lista) {

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {

                                    View view = super.getView(position, convertView, parent);

                                    final TextView ListItemShow = (TextView) view.findViewById(android.R.id.text1);

                                    if (ListItemShow.getText().toString().contains("1|")) {
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
                            ui.showAlert(ActivityGeomano.this, "warning", "Cod/Doc no se reconoce");
                        }

                    } catch (Exception e) {
                        Log.e("ERROR", e.getMessage());
                    }

                }

            }
        });


        lstMano.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                objBiometriaList = biometriasList.get(position);
                analizarRegistroBiometriaList(ActivityGeomano.this);
            }
        });

        btnAcMano1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickeable(false);
                registrarMano();
            }
        });

        btnAcMano3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickeable(false);
                eliminarMano();
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // En caso que se detecte un evento sobre la pantalla el contadorAhorroEnergia se reiniciara en 1
        ActivityPrincipal.contadorAhorroEnergia = 1;

        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                ui.initScreen(this);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public static void setClickeable(boolean clickeable){
        if (clickeable) {
            btnAcMano1.setClickable(true);
            btnAcMano3.setClickable(true);
            btnConsultarMano.setClickable(true);
            btnMasterGeomano.setClickable(true);
            edtManoDocumentoG.setClickable(true);
        } else {
            btnAcMano1.setClickable(false);
            btnAcMano3.setClickable(false);
            btnConsultarMano.setClickable(false);
            btnMasterGeomano.setClickable(false);
            edtManoDocumentoG.setClickable(false);
        }
    }

    public void registrarMano() {
        txvManoTexto.setText("");
        ABORTAR = false;
        idTipoDetaBio = 1;
        ActivityPrincipal.isEnrolling = true;
        manageScreenEnroll(true);
        Thread threadHandPunchThread = new Thread(new ThreadHandPunchEnroll(ActivityGeomano.this));
        threadHandPunchThread.start();
    }

    public void eliminarMano() {
        ABORTAR = false;
        ActivityPrincipal.isDeleting = true;
        txvManoTexto.setText("Borrando\nBiometria");
        manageScreenEnroll(true);
        Thread threadHandPunchDelete = new Thread(new ThreadHandPunchDelete(ActivityGeomano.this));
        threadHandPunchDelete.start();
    }

    public static void analizarRegistroBiometriaList(Activity activity){

        setClickeable(true);

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
                List<Biometrias> espaciosBiometrias = queriesBiometrias.BuscarBiometrias(10,empresaCodigo,codigo);
                int cantidad = espaciosBiometrias.size();

                if (cantidad == 1 ) {
                    for(int i = 0; i < espaciosBiometrias.size(); i++){
                        Log.d("Autorizaciones",espaciosBiometrias.get(i).toString());
                    }

                    // Espacio 01
                    int espacio01 = espaciosBiometrias.get(0).getValorBiometria();
                    objEspacio01 = espaciosBiometrias.get(0);


                    indice = espaciosBiometrias.get(0).getIndiceBiometria();
                    Log.e("TEMPUS: ", ":::::::::::::: " + String.valueOf(indice));

                    txvManoNombrePersonal.setText(nombre);

                    manageEnrollEmp(true,empresaNombre);

                    valorTarjeta = objBiometriaList.getNroDocumento();

                    // Analisis de espacios para enrolar
                    if (espacio01 == 0) {
                        //Enrolar Huella 1
                        manageButtonsAction(true,false);
                    } else {
                        //Eliminar Huellas
                        manageButtonsAction(false,true);
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
            btnConsultarMano.setClickable(false);
            edtManoDocumentoG.setEnabled(false);
        } else {
            btnExitLstMano.setVisibility(View.INVISIBLE);
            txvLayerLstMano.setVisibility(View.INVISIBLE);
            lstMano.setVisibility(View.INVISIBLE);
            txvCabeceraLstMano.setVisibility(View.INVISIBLE);
            btnMasterGeomano.setClickable(true);
            btnConsultarMano.setClickable(true);
            edtManoDocumentoG.setEnabled(true);
        }
    }

    public static void manageEnrollEmp(boolean visible,String empresa) {
        if (visible) {
            btnAcMano1.setVisibility(View.VISIBLE);
            btnAcMano3.setVisibility(View.VISIBLE);
            txvAcMano1.setVisibility(View.VISIBLE);
            txvManoEmpA1.setVisibility(View.VISIBLE);
            txvManoEmpA2.setVisibility(View.VISIBLE);
            txvManoEmpresaCorner.setVisibility(View.VISIBLE);
            txvManoEmpresaCorner.setText(empresa);
        } else {
            btnAcMano1.setVisibility(View.INVISIBLE);
            btnAcMano3.setVisibility(View.INVISIBLE);
            txvAcMano1.setVisibility(View.INVISIBLE);
            txvManoEmpA1.setVisibility(View.INVISIBLE);
            txvManoEmpA2.setVisibility(View.INVISIBLE);
            txvManoEmpresaCorner.setVisibility(View.INVISIBLE);
            txvManoEmpresaCorner.setText(empresa);
        }

    }

    public static void manageButtonsAction(boolean btn1, boolean btn3) {
        btnAcMano1.setEnabled(btn1);
        btnAcMano3.setEnabled(btn3);
    }

    public void limpiarScreen(){
        txvManoNombrePersonal.setText("");
        btnAcMano1.setEnabled(false);
        btnAcMano3.setEnabled(false);
    }

    public static void manageScreenEnroll(boolean visible){
        if (visible) {
            txvManoTexto.setVisibility(View.VISIBLE);
            txvManoFondo.setVisibility(View.VISIBLE);
            imgViewGeomano.setVisibility(View.VISIBLE);
            led01.setVisibility(View.VISIBLE);
            led02.setVisibility(View.VISIBLE);
            led03.setVisibility(View.VISIBLE);
            led04.setVisibility(View.VISIBLE);

        } else {
            txvManoTexto.setVisibility(View.INVISIBLE);
            txvManoFondo.setVisibility(View.INVISIBLE);
            imgViewGeomano.setVisibility(View.INVISIBLE);
            led01.setVisibility(View.INVISIBLE);
            led02.setVisibility(View.INVISIBLE);
            led03.setVisibility(View.INVISIBLE);
            led04.setVisibility(View.INVISIBLE);
        }
        imgViewResultOK.setVisibility(View.INVISIBLE);
        imgViewResultKO.setVisibility(View.INVISIBLE);
    }

}
