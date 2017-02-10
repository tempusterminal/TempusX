package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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



    String[] val = {"EMP01 00000000 12345678 Carlos","EMP01 00000001 23456789 Jueba","EMP01 00000002 34567891 Carlos","EMP01 00000003 45678912 Pepe","EMP01 00000004 56789123 Carlos"};
    ListView list;
    Dialog listDialog;


    // Pantalla Lista Huella
    Button btnExitLstHuella;
    TextView txvLayerLstHuella;
    ListView lstHuella;


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


        btnExitLstHuella = (Button) findViewById(R.id.btnExitLstHuella);
        txvLayerLstHuella = (TextView) findViewById(R.id.txvLayerLstHuella);
        lstHuella = (ListView) findViewById(R.id.lstHuella);


        //ArrayAdapter<String> test = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,val);

        ArrayAdapter<String> test = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, val){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                final TextView ListItemShow = (TextView) view.findViewById(android.R.id.text1);


                if (ListItemShow.getText().toString().contains("Carlos")){
                    ListItemShow.setBackgroundColor(Color.parseColor("#5db85d"));
                } else {
                    ListItemShow.setBackgroundColor(Color.parseColor("#333333"));
                }

                return view;
            }

        };

        lstHuella.setAdapter(test);


        //ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, colors, R.layout.spinner_item);
        //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spnRegistros.setAdapter(adapter1);


        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);
        limpiarScreen();
        manageEnrollEmp(false,"");
        manageScreenEnroll(false);
        manageScreenListaHuella(false);


        /* --- Inicialización de Parametros Generales --- */

        btnExitLstHuella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageScreenListaHuella(false);
            }
        });


        btnConsultarHuella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                manageScreenListaHuella(true);








                /***

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

                    //PERSONAL NO TIENE PERMISOS

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

                 ***/

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

    }






    private void showdialog()
    {
        listDialog = new Dialog(this, R.style.dialog_theme);
        listDialog.setTitle("Select Item");
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.list, null, false);
        listDialog.setContentView(v);
        listDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        listDialog.getWindow().getDecorView().setSystemUiVisibility(ActivityBiometria.this.getWindow().getDecorView().getSystemUiVisibility());

        listDialog.setCanceledOnTouchOutside(true);
        listDialog.getWindow().setLayout(750, 400);

        //there are a lot of settings, for dialog, check them all out!

        final ListView list1 = (ListView) listDialog.findViewById(R.id.listview);
        Button btnDlgClose = (Button) listDialog.findViewById(R.id.btnDlgClose);

        btnDlgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.hide();
            }
        });

        list1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, val));
        //now that the dialog is set up, it's time to show it
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listDialog.show();
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

    public void manageScreenListaHuella(boolean visible) {
        if (visible) {
            btnExitLstHuella.setVisibility(View.VISIBLE);
            txvLayerLstHuella.setVisibility(View.VISIBLE);
            lstHuella.setVisibility(View.VISIBLE);
            btnMasterBiometria.setClickable(false);
            btnConsultarHuella.setClickable(false);
            edtHuellaDocumento.setEnabled(false);
        } else {
            btnExitLstHuella.setVisibility(View.INVISIBLE);
            txvLayerLstHuella.setVisibility(View.INVISIBLE);
            lstHuella.setVisibility(View.INVISIBLE);
            btnMasterBiometria.setClickable(true);
            btnConsultarHuella.setClickable(true);
            edtHuellaDocumento.setEnabled(true);
        }
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
