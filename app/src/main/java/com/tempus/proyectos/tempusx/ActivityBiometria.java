package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.tempus.proyectos.data.model.Biometrias;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.threads.ThreadSupremaDelete;
import com.tempus.proyectos.threads.ThreadSupremaEnroll;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import pl.droidsonroids.gif.GifTextView;

public class ActivityBiometria extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    public static ViewDialog dialog;
    Utilities util;
    QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;

    /* --- Declaración de Variables Globales --- */

    List<Button> lstHuellaBtn;
    List<TextView> lstHuellaTxv;
    List<String> list;

    /* --- Declaración de Variables Locales --- */

    public static boolean ocupado;
    public static boolean accionCancel;
    public static String valorTarjeta;

    /* --- Declaración de Componentes de la Interfaz --- */

    ImageView btnMasterBiometria;
    static TextView txvHuellaFondo;
    static GifTextView txvHuellaGif;
    static TextView txvHuellaTexto;
    //ProgressBar pbrHuellaCarga;

    TextView txvHuellaEmpA1;
    TextView txvHuellaEmpA2;
    Button btnAcHuella1;
    Button btnAcHuella2;
    Button btnAcHuella3;
    TextView txvActHuella1;
    TextView txvActHuella2;
    TextView txvHuellaEmpresaCorner;



    Button btnConsultarHuella;

    TextView txvHuellaNombrePersonal;
    EditText edtHuellaDocumento;

    Spinner spnRegistros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometria);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        dialog = new ViewDialog(ActivityBiometria.this);
        util = new Utilities();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Biometria";

        lstHuellaBtn = new ArrayList<Button>();
        lstHuellaTxv = new ArrayList<TextView>();

        /* --- Inicialización de Variables Locales --- */

        ocupado = false;
        accionCancel = false;

        valorTarjeta = "";


        /* --- Inicialización de Componentes de la Interfaz --- */

        txvHuellaFondo = (TextView) findViewById(R.id.txvHuellaFondo);
        txvHuellaGif = (GifTextView) findViewById(R.id.txvHuellaGif);
        txvHuellaTexto = (TextView) findViewById(R.id.txvHuellaTexto);
        //pbrHuellaCarga = (ProgressBar) findViewById(R.id.pbrHuellaCarga);

        btnAcHuella1 = (Button) findViewById(R.id.btnAcHuella1);
        btnAcHuella2 = (Button) findViewById(R.id.btnAcHuella2);
        btnAcHuella3 = (Button) findViewById(R.id.btnAcHuella3);

        lstHuellaBtn.add(btnAcHuella1);
        lstHuellaBtn.add(btnAcHuella2);

        txvActHuella1 = (TextView) findViewById(R.id.txvAcHuella1);
        txvActHuella2 = (TextView) findViewById(R.id.txvAcHuella2);

        lstHuellaTxv.add(txvActHuella1);
        lstHuellaTxv.add(txvActHuella2);

        txvHuellaEmpA1 = (TextView) findViewById(R.id.txvHuellaEmpA1);
        txvHuellaEmpA2 = (TextView) findViewById(R.id.txvHuellaEmpA2);
        txvHuellaEmpresaCorner = (TextView) findViewById(R.id.txvHuellaEmpresaCorner);

        btnConsultarHuella = (Button) findViewById(R.id.btnConsultarHuella);

        btnMasterBiometria = (ImageView) findViewById(R.id.btnMasterBiometria);

        txvHuellaNombrePersonal = (TextView) findViewById(R.id.txvHuellaNombrePersonal);
        edtHuellaDocumento = (EditText) findViewById(R.id.edtHuellaDocumento);

        spnRegistros = (Spinner) findViewById(R.id.spnRegistros);

        list = new ArrayList<String>();
        list.add(" Seleccione Empresa");
        list.add("Empresa 1");
        list.add("Empresa 2");
        list.add("Empresa 3");
        list.add("Empresa 4");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRegistros.setAdapter(adapter);

        spnRegistros.setDropDownWidth(1);


        //ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, colors, R.layout.spinner_item);
        //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spnRegistros.setAdapter(adapter1);


        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);
        limpiarScreen();
        manageEnrollEmp(false,"");
        manageScreenEnroll(false);


        /* --- Inicialización de Parametros Generales --- */

        btnConsultarHuella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String documento = edtHuellaDocumento.getText().toString();
                valorTarjeta = documento;

                queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(ActivityBiometria.this);


                List<Biometrias> biometriasList = queriesPersonalTipolectoraBiometria.EvaluarBiometrias(7,valorTarjeta);
                Log.v("TEMPUS: ", "♫♫♫♫" + String.valueOf(biometriasList));


                if (!biometriasList.get(0).Mensaje.equalsIgnoreCase("personal no registrado")) {
                    txvHuellaNombrePersonal.setText(biometriasList.get(0).Nombres + " " + biometriasList.get(0).ApellidoPaterno + " " + biometriasList.get(0).ApellidoMaterno);
                    boolean btn1 = false;
                    boolean btn2 = false;
                    boolean btn3 = false;

                    /*
                    PERSONAL NO TIENE PERMISOS
                    */










                    for(int i = 0; i < biometriasList.size(); i++){
                        if (biometriasList.get(i).Mensaje.equalsIgnoreCase("Enrolamiento disponible")){
                            lstHuellaBtn.get(i).setEnabled(true);
                        } else {
                            lstHuellaBtn.get(i).setEnabled(false);
                        }
                        lstHuellaTxv.get(i).setText(biometriasList.get(i).Mensaje);
                    }

                } else {
                    txvHuellaNombrePersonal.setText(biometriasList.get(0).Mensaje);
                    manageButtonsAction(false,false,false);
                }

/*
                if (exito){
                    txvHuellaNombrePersonal.setText("JUAN PEREZ");
                    txvHuellaEstado.setText(" PERSONAL NO TIENE HUELLAS");
                    manageButtonsAction(true,true,true);
                } else {
                    limpiarScreen();
                    txvHuellaNombrePersonal.setText("PERSONAL NO REGISTRADO");
                    valorTarjeta = "";
                }
*/
            }
        });

        btnMasterBiometria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ocupado) {

                    dialog.showDialog("Regresar","¿Está seguro que desea salir?, se cancelará la operación actual.");

                    dialog.dialogButtonAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            accionCancel = true;
                            dialog.viewDialog.dismiss();
                            ui.initScreen(ActivityBiometria.this);
                        }
                    });

                    dialog.dialogButtonCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.viewDialog.dismiss();
                            ui.initScreen(ActivityBiometria.this);
                        }
                    });

                } else {
                    ui.goToActivity(ActivityBiometria.this, ActivityMenu.class , "","");
                }
            }
        });

        btnAcHuella1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Registrando huella 1
                ocupado = true;
                ActivityPrincipal.isEnrolling = true;
                manageScreenEnroll(true);
                Thread threadSupremaEnroll = new Thread(new ThreadSupremaEnroll(ActivityBiometria.this));
                threadSupremaEnroll.start();
            }
        });

        btnAcHuella2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Registrando huella 2
                ocupado = true;
                ActivityPrincipal.isEnrolling = true;
                manageScreenEnroll(true);
                Thread threadSupremaEnroll = new Thread(new ThreadSupremaEnroll(ActivityBiometria.this));
                threadSupremaEnroll.start();
            }
        });

        btnAcHuella3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Eliminando huella
                ocupado = true;
                ActivityPrincipal.isDeleting = true;
                manageScreenEnroll(true);
                Thread threadSupremaDelete = new Thread(new ThreadSupremaDelete());
                threadSupremaDelete.start();
            }
        });

        spnRegistros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    manageEnrollEmp(false, "");
                } else {
                    String nombreEmpresa = list.get(position);
                    manageEnrollEmp(true, nombreEmpresa);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("TEMPUS: "," > ---- > onNothingSelected");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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

    public static void manageScreenEnroll(boolean visible){
        if (visible) {
            txvHuellaFondo.setVisibility(View.VISIBLE);
            txvHuellaGif.setVisibility(View.VISIBLE);
            txvHuellaTexto.setVisibility(View.VISIBLE);
            //pbrHuellaCarga.setVisibility(View.VISIBLE);
        } else {
            txvHuellaFondo.setVisibility(View.INVISIBLE);
            txvHuellaGif.setVisibility(View.INVISIBLE);
            txvHuellaTexto.setVisibility(View.INVISIBLE);
            //pbrHuellaCarga.setVisibility(View.INVISIBLE);
        }
    }

    public void limpiarScreen(){
        txvHuellaNombrePersonal.setText("");
        btnAcHuella1.setEnabled(false);
        btnAcHuella2.setEnabled(false);
        btnAcHuella3.setEnabled(false);
    }

    public void manageButtonsAction(boolean btn1, boolean btn2, boolean btn3) {
        btnAcHuella1.setEnabled(btn1);
        btnAcHuella2.setEnabled(btn2);
        btnAcHuella3.setEnabled(btn3);
    }

    public void manageEnrollEmp(boolean visible,String empresa) {
        if (visible) {
            btnAcHuella1.setVisibility(View.VISIBLE);
            btnAcHuella2.setVisibility(View.VISIBLE);
            btnAcHuella3.setVisibility(View.VISIBLE);
            txvActHuella1.setVisibility(View.VISIBLE);
            txvActHuella2.setVisibility(View.VISIBLE);
            txvHuellaEmpA1.setVisibility(View.VISIBLE);
            txvHuellaEmpA2.setVisibility(View.VISIBLE);
            txvHuellaEmpresaCorner.setVisibility(View.VISIBLE);
            txvHuellaEmpresaCorner.setText(empresa);
        } else {
            btnAcHuella1.setVisibility(View.INVISIBLE);
            btnAcHuella2.setVisibility(View.INVISIBLE);
            btnAcHuella3.setVisibility(View.INVISIBLE);
            txvActHuella1.setVisibility(View.INVISIBLE);
            txvActHuella2.setVisibility(View.INVISIBLE);
            txvHuellaEmpA1.setVisibility(View.INVISIBLE);
            txvHuellaEmpA2.setVisibility(View.INVISIBLE);
            txvHuellaEmpresaCorner.setVisibility(View.INVISIBLE);
            txvHuellaEmpresaCorner.setText(empresa);
        }

    }

    public void aceptar(){

    }
}
