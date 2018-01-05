package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.model.Personal;
import com.tempus.proyectos.data.queries.QueriesAutorizaciones;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.data.queries.QueriesPersonal;
import com.tempus.proyectos.data.queries.QueriesTerminal;
import com.tempus.proyectos.log.Database;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;

import com.tempus.proyectos.util.timerPicker.MyTimePickerDialog;
import com.tempus.proyectos.util.timerPicker.TimePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ActivitySistema extends Activity {

    private String TAG = "TX-AS";

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    String resultadoIP;

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;

    TabHost host;

    EditText       edtSistIdTerminal;
    TextView       txvSistFirmware;
    TextView       txvSistSoftware;
    TextView       txvSistIp;
    TextView       txvSistTec1;
    Button         btnActualizarID;
    RelativeLayout activity_sistema;

    TextView lblmarcacionrepetida;
    ImageButton btnConfmarcacionrepetida;
    Button btnGuardarAvanzado;

    TextView txvBDSize;
    TextView txvTEmpresas;
    TextView txvTEstados;
    TextView txvTLlamadas;
    TextView txvTLogTerminal;
    TextView txvTMarcaciones;
    TextView txvTParameters;
    TextView txvTPersonal;
    TextView txvTPersonalTipolectoraBiometria;
    TextView txvTPerTipolectTerm;
    TextView txvTServicios;
    TextView txvTTarjetaPersonalTipolectora;
    TextView txvTTerminal;
    TextView txvTTerminalTipolect;
    TextView txvTTipoDetalleBiometria;
    TextView txvTTipoLectora;

    EditText edtAutorizaValor;
    ImageView imgFotoPersonalAutoriza;
    TextView txvPersonalAutoriza;
    TextView txvTarjetasAutoriza;
    ImageButton btnBuscarAutoriza;
    ImageButton btnLimpiarAutoriza;

    ImageView btnMasterSistema;

    QueriesParameters queriesParameters;
    QueriesPersonal queriesPersonal;
    QueriesAutorizaciones queriesAutorizaciones;
    ArrayList<ArrayList<String>> informationBD =  new ArrayList<ArrayList<String>>();
    private Fechahora fechahora = new Fechahora();

    File fileFotoPersonalAutoriza;
    ArrayList<Personal> personalArrayList;
    ArrayList<Autorizaciones> autorizacionesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sistema);

        try {

        /* --- Inicialización de Objetos --- */
            Log.v(TAG, "INICIANDO SISTEMA");

            ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

            ActivityPrincipal.activityActive = "Sistema";

        /* --- Inicialización de Variables Locales --- */

            queriesParameters = new QueriesParameters(ActivityPrincipal.context);
            queriesPersonal = new QueriesPersonal(ActivityPrincipal.context);
            queriesAutorizaciones = new QueriesAutorizaciones(ActivityPrincipal.context);

        /* --- Inicialización de Componentes de la Interfaz --- */

            txvFondo1 = (TextView) findViewById(R.id.txvFondo1);
            txvFondo2 = (TextView) findViewById(R.id.txvFondo2);
            txvBarraInf = (TextView) findViewById(R.id.txvBarraInf);
            txvLinea1 = (TextView) findViewById(R.id.txvLinea1);
            txvLinea2 = (TextView) findViewById(R.id.txvLinea2);
            txvLinea3 = (TextView) findViewById(R.id.txvLinea3);
            txvLinea4 = (TextView) findViewById(R.id.txvLinea4);

            ActivityPrincipal.setBackgroundColorOnTextView(txvFondo1, ActivityPrincipal.parametersColorsUI.split(",")[0], "#cecece");
            ActivityPrincipal.setBackgroundColorOnTextView(txvFondo2, ActivityPrincipal.parametersColorsUI.split(",")[1], "#cecece");
            ActivityPrincipal.setBackgroundColorOnTextView(txvBarraInf, ActivityPrincipal.parametersColorsUI.split(",")[2], "#cecece");
            ActivityPrincipal.setBackgroundColorOnTextView(txvLinea1, ActivityPrincipal.parametersColorsUI.split(",")[3], "#777777");
            ActivityPrincipal.setBackgroundColorOnTextView(txvLinea2, ActivityPrincipal.parametersColorsUI.split(",")[4], "#777777");
            ActivityPrincipal.setBackgroundColorOnTextView(txvLinea3, ActivityPrincipal.parametersColorsUI.split(",")[5], "#777777");
            ActivityPrincipal.setBackgroundColorOnTextView(txvLinea4, ActivityPrincipal.parametersColorsUI.split(",")[6], "#777777");

            host = (TabHost) findViewById(R.id.tabHostSys);
            host.setup();

            //Tab 1
            TabHost.TabSpec spec = host.newTabSpec("Tab1");
            spec.setContent(R.id.tabSys1);
            spec.setIndicator("SISTEMA");
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec("Tab2");
            spec.setContent(R.id.tabSys2);
            spec.setIndicator("AVANZANDO");
            host.addTab(spec);

            //Tab 3
            spec = host.newTabSpec("Tab3");
            spec.setContent(R.id.tabSys3);
            spec.setIndicator("TERMINAL");
            host.addTab(spec);

            //Tab 4
            spec = host.newTabSpec("Tab4");
            spec.setContent(R.id.tabSys4);
            spec.setIndicator("DATA");
            host.addTab(spec);

            //Tab 5
            spec = host.newTabSpec("Tab5");
            spec.setContent(R.id.tabSys5);
            spec.setIndicator("AUTORIZA");
            host.addTab(spec);

            TabWidget widget = host.getTabWidget();
            for (int i = 0; i < widget.getChildCount(); i++) {
                View v = widget.getChildAt(i);
                // Look for the title view to ensure this is an indicator and not a divider.
                TextView tv = (TextView) v.findViewById(android.R.id.title);
                if (tv == null) {
                    continue;
                }
                v.setBackgroundResource(R.drawable.tabline);
                tv.setTextSize(15);
            }

            btnMasterSistema = (ImageView) findViewById(R.id.btnMasterSistema);
            ActivityPrincipal.setImageBitmapOnImageView(btnMasterSistema, "/tempus/img/config/", "logo.png");

            edtSistIdTerminal = (EditText) findViewById(R.id.edtSistIdTerminal);
            txvSistFirmware = (TextView) findViewById(R.id.txvSistFirmware);
            txvSistSoftware = (TextView) findViewById(R.id.txvSistSoftware);
            txvSistIp = (TextView) findViewById(R.id.txvSistIp);
            txvSistTec1 = (TextView) findViewById(R.id.txvSistTec1);
            btnActualizarID = (Button) findViewById(R.id.btnActualizarID);
            activity_sistema = (RelativeLayout) findViewById(R.id.activity_sistema);


            lblmarcacionrepetida = (TextView) findViewById(R.id.lblmarcacionrepetida);
            btnConfmarcacionrepetida = (ImageButton) findViewById(R.id.btnConfmarcacionrepetida);
            btnGuardarAvanzado = (Button) findViewById(R.id.btnGuardarAvanzado);

            lblmarcacionrepetida.setText(getTimehms(-1, -1, ActivityPrincipal.parameterMarcacionRepetida));

            txvBDSize = (TextView) findViewById(R.id.txvBDSize);
            txvTEmpresas = (TextView) findViewById(R.id.txvTEmpresas);
            txvTEstados = (TextView) findViewById(R.id.txvTEstados);
            txvTLlamadas = (TextView) findViewById(R.id.txvTLlamadas);
            txvTLogTerminal = (TextView) findViewById(R.id.txvTLogTerminal);
            txvTMarcaciones = (TextView) findViewById(R.id.txvTMarcaciones);
            txvTParameters = (TextView) findViewById(R.id.txvTParameters);
            txvTPersonal = (TextView) findViewById(R.id.txvTPersonal);
            txvTPersonalTipolectoraBiometria = (TextView) findViewById(R.id.txvTPersonalTipolectoraBiometria);
            txvTPerTipolectTerm = (TextView) findViewById(R.id.txvTPerTipolectTerm);
            txvTServicios = (TextView) findViewById(R.id.txvTServicios);
            txvTTarjetaPersonalTipolectora = (TextView) findViewById(R.id.txvTTarjetaPersonalTipolectora);
            txvTTerminal = (TextView) findViewById(R.id.txvTTerminal);
            txvTTerminalTipolect = (TextView) findViewById(R.id.txvTTerminalTipolect);
            txvTTipoDetalleBiometria = (TextView) findViewById(R.id.txvTTipoDetalleBiometria);
            txvTTipoLectora = (TextView) findViewById(R.id.txvTTipoLectora);

            edtAutorizaValor = (EditText) findViewById(R.id.edtAutorizaValor);
            imgFotoPersonalAutoriza = (ImageView) findViewById(R.id.imgFotoPersonalAutoriza);
            txvPersonalAutoriza = (TextView) findViewById(R.id.txvPersonalAutoriza);
            txvTarjetasAutoriza = (TextView) findViewById(R.id.txvTarjetasAutoriza);

            btnBuscarAutoriza = (ImageButton) findViewById(R.id.btnBuscarAutoriza);
            btnLimpiarAutoriza = (ImageButton) findViewById(R.id.btnLimpiarAutoriza);

        /* --- Inicialización de Métodos --- */

            ui.initScreen(this);
            //activity_sistema.setBackgroundColor(getResources().getColor(R.color.colorBlanco));

        /* --- Inicialización de Parametros Generales --- */

        /* --- Eventos --- */

            btnMasterSistema.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ui.goToActivity(ActivitySistema.this, ActivityMenu.class, "", "");
                }
            });

            btnActualizarID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySistema.this, R.style.AlertDialogCustom));
                    builder.setTitle("Restaurar Terminal").setMessage("¿Desea restaurar el Terminal con el ID " + edtSistIdTerminal.getText().toString() + "?").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            try {

                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySistema.this, R.style.AlertDialogCustom));

                                builder.setTitle("Confirmar Restauración").setMessage("Este proceso ELIMINARÁ TODAS LAS AUTORIZACIONES del terminal, ¿Está seguro que desea continuar?").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        try {

                                            QueriesTerminal queriesTerminal = new QueriesTerminal(ActivityPrincipal.context);
                                            queriesTerminal.startActualizarIdterminal(edtSistIdTerminal.getText().toString());

                                            Toast.makeText(getApplicationContext(), "Terminal restaurado, se REQUIERE SINCRONIZACIÓN para continuar", Toast.LENGTH_SHORT).show();

                                                        /*
                                                        if (i == 1) {
                                                            Toast.makeText(getApplicationContext(),"ID Actualizado Correctamente",Toast.LENGTH_SHORT).show();
                                                            ActivityPrincipal.idTerminal = edtSistIdTerminal.getText().toString();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(),"Fallo al Actualizar",Toast.LENGTH_SHORT).show();
                                                        }
                                                        */

                                        } catch (Exception e) {
                                            Log.e(TAG, "btnActualizarID.setOnClickListener confirmación " + e.getMessage());
                                        }

                                    }
                                }).setNegativeButton(android.R.string.no, null).show();

                            } catch (Exception e) {
                                Log.e(TAG, "btnActualizarID.setOnClickListener " + e.getMessage());
                            }
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
                }
            });

            ManageData();
            showIP();


            btnConfmarcacionrepetida.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        int m = 0;
                        String val = lblmarcacionrepetida.getText().toString();
                        m = Integer.parseInt(val);

                        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(ActivitySistema.this, new MyTimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, final int hourOfDay, final int minute, final int seconds) {
                                // TODO Auto-generated method stub

                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySistema.this, R.style.AlertDialogCustom));

                                builder
                                        .setTitle("Guardar Tiempo de Marcación Repetida")
                                        .setMessage("¿Desea guardar la configuración para Marcación Repetida?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                try {

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                lblmarcacionrepetida.setText(getTimehms(-1, -1, seconds));
                                                            } catch (Exception e) {
                                                                Log.e(TAG, "btnConfmarcacionrepetida.setOnClickListener onTimeSet " + e.getMessage());
                                                            }
                                                        }
                                                    });

                                                    Long rowaffected;
                                                    String mr = "00";
                                                    mr = lblmarcacionrepetida.getText().toString();

                                                    Log.v(TAG, "parametro a guardar->" + mr);
                                                    rowaffected = saveParameter("MARCACIONREPETIDA", mr);
                                                    if (rowaffected == 1) {
                                                        //El tiempo real es en minutos, solo se esta utilizando de picker de segundos (0-59 seg)
                                                        ActivityPrincipal.parameterMarcacionRepetida = seconds;
                                                        Toast.makeText(ActivitySistema.this, "Se guardó la configuración: " + ActivityPrincipal.parameterMarcacionRepetida + " minutos", Toast.LENGTH_LONG).show();
                                                    } else if (rowaffected == -1) {
                                                        Toast.makeText(ActivitySistema.this, "NO se guardó la configuración", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(ActivitySistema.this, "NO se realizó ningún cambio", Toast.LENGTH_LONG).show();
                                                    }
                                                    //RELAY_01,0,00:00;RELAY_02,0,00:00
                                                } catch (Exception e) {
                                                    Log.e(TAG, "btnConfmarcacionrepetida.setOnClickListener " + e.getMessage());
                                                }

                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();

                            }
                        }, m, true);
                        mTimePicker.setTitle("MARCACIÓN REPETIDA");
                        mTimePicker.show();

                    } catch (Exception e) {
                        Log.e(TAG, "btnConftiempoactivorelay.setOnClickListener " + e.getMessage());
                    }


                }
            });



            btnBuscarAutoriza.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.v(TAG,"btnBuscarAutoriza");

                    // Iniciar los array para Personal y Autorizaciones
                    personalArrayList = new ArrayList<Personal>();
                    autorizacionesArrayList = new ArrayList<Autorizaciones>();

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySistema.this, R.style.AlertDialogCustom));
                    builder
                            .setTitle("Buscar Autoriza")
                            .setMessage("¿Desea buscar autoriza " + edtAutorizaValor.getText().toString() + "?")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    try{

                                        Log.v(TAG,"Buscando autorizaciones");
                                        // Obtener la lista de personal que coincida con valor ingresado = CODIGO o NRO_DOCUMENTO
                                        personalArrayList = queriesPersonal.selectPersonalByCodNroDoc(edtAutorizaValor.getText().toString());
                                        Log.v(TAG,"PersonalAutoriza(" + personalArrayList.size() + ")=" + personalArrayList.toString());

                                        if(personalArrayList.size() > 0){
                                            // Crear un arraylist de String para mostrar los resultados
                                            ArrayList<String> stringArrayList = new ArrayList<String>();
                                            for(int i = 0; i < personalArrayList.size(); i++){
                                                stringArrayList.add("" + personalArrayList.get(i).getEmpresa() + " " +
                                                        "" + personalArrayList.get(i).getCodigo() + " " +
                                                        "" + personalArrayList.get(i).getApellidoPaterno() + " " + personalArrayList.get(i).getApellidoMaterno() + " " + personalArrayList.get(i).getNombres() + "\n" +
                                                        "" + personalArrayList.get(i).getEstado() + " " +
                                                        "" + personalArrayList.get(i).getFechaDeCese() + " " +
                                                        "" + personalArrayList.get(i).getNroDocumento() + " "
                                                );
                                            }

                                            // Cargar los resultados (Lista de Personal) en un CharSequence para insertarlo en el AlertDialog
                                            final CharSequence[] charSequenceItems = stringArrayList.toArray(new CharSequence[stringArrayList.size()]);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySistema.this, R.style.AlertDialogCustom));
                                            builder
                                                    .setTitle("Seleccione un resultado")
                                                    .setItems(charSequenceItems, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            // La variable which contiene el item selecionado
                                                            Log.v(TAG,"Opción " + which);

                                                            // Se buscan las autorizaciones que corresponden al item seleccionado
                                                            autorizacionesArrayList = queriesAutorizaciones.buscarAutorizacionesByEmpCod(personalArrayList.get(which).getEmpresa(), personalArrayList.get(which).getCodigo());
                                                            try{
                                                                // Se busca la foto según el campo ICONO en el directorio de PERSONAL
                                                                fileFotoPersonalAutoriza = new File(Environment.getExternalStorageDirectory().toString() + "/tempus/img/personal/sync/original/" + personalArrayList.get(which).getIcono());

                                                                // En el caso que encuentre el archivo de imagen, se insertará en el imgFotoPersonalAutoriza
                                                                if(fileFotoPersonalAutoriza.isFile()){
                                                                    try{
                                                                        Log.v(TAG,"imgFotoPersonalAutoriza " + fileFotoPersonalAutoriza.toString());
                                                                        imgFotoPersonalAutoriza.setVisibility(View.VISIBLE);
                                                                        imgFotoPersonalAutoriza.setImageBitmap(BitmapFactory.decodeFile(fileFotoPersonalAutoriza.toString()));
                                                                    }catch (Exception e){
                                                                        Log.e(TAG,"imgFotoPersonalAutoriza " + e.getMessage());
                                                                    }
                                                                // En el caso que no encuentre el archivo de imagen se mostrará la imagen por defecto
                                                                }else{
                                                                    imgFotoPersonalAutoriza.setVisibility(View.VISIBLE);
                                                                    imgFotoPersonalAutoriza.setImageResource(R.drawable.fotopersonal);
                                                                }
                                                            }catch (Exception e){
                                                                Log.e(TAG,"fileFotoPersonalAutoriza " + e.getMessage());
                                                            }

                                                            // Se listan todos los datos del personal seleccionado
                                                            txvPersonalAutoriza.setText("Empresa: " + personalArrayList.get(which).getEmpresa() + "\n" +
                                                                    "Codigo: " + personalArrayList.get(which).getCodigo() + "\n" +
                                                                    "Nombres: " + personalArrayList.get(which).getApellidoPaterno() + " " + personalArrayList.get(which).getApellidoMaterno() + " " + personalArrayList.get(which).getNombres() + "\n" +
                                                                    "FechaDeCese: " + personalArrayList.get(which).getFechaDeCese() + "\n" +
                                                                    "Estado: " + personalArrayList.get(which).getEstado() + "\n" +
                                                                    "Icono: " + personalArrayList.get(which).getIcono() + "\n" +
                                                                    "NroDocumento: " + personalArrayList.get(which).getNroDocumento() + "\n" +
                                                                    "FechaHoraSinc: " + personalArrayList.get(which).getFechaHoraSinc());

                                                            // Se loistan todas las autorizaciones del personal selecionado
                                                            txvTarjetasAutoriza.setText("");
                                                            for(int i = 0; i < autorizacionesArrayList.size(); i++){
                                                                txvTarjetasAutoriza.setText(txvTarjetasAutoriza.getText().toString() + "A(" + (i+1) + "): " + autorizacionesArrayList.get(i).getIdterminal() + " " + autorizacionesArrayList.get(i).getIdTipoLect()  + "\t\t" +
                                                                        "Permisos: " + autorizacionesArrayList.get(i).getEstadoRequiereAsistencia() + autorizacionesArrayList.get(i).getFlagTerminalTipoLect() + autorizacionesArrayList.get(i).getFlagPerTipoLectTerm() + "\t\t" +
                                                                        "ValorTarjeta: " + autorizacionesArrayList.get(i).getValorTarjeta() + "\n"
                                                                );
                                                            }

                                                        }
                                                    })
                                                    .setPositiveButton(android.R.string.cancel, null)
                                                    .show().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                        }else{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySistema.this, R.style.AlertDialogCustom));
                                            builder
                                                    .setTitle("Sin resultados")
                                                    .setIcon(android.R.drawable.ic_dialog_dialer)
                                                    .setPositiveButton(android.R.string.ok, null).show();
                                        }

                                    }catch (Exception e){
                                        Log.e(TAG,"btnBuscarAutoriza.setOnClickListener " + e.getMessage());
                                    }

                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });

            btnLimpiarAutoriza.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.v(TAG,"btnLimpiarAutoriza");


                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivitySistema.this, R.style.AlertDialogCustom));

                    builder
                            .setTitle("Limpiar Autoriza")
                            .setMessage("¿Desea limpiar búsqueda?")
                            .setIcon(android.R.drawable.ic_dialog_dialer)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    try{
                                        Log.v(TAG,"Limpiando autorizaciones");

                                        edtAutorizaValor.setText("");
                                        imgFotoPersonalAutoriza.setVisibility(View.INVISIBLE);
                                        imgFotoPersonalAutoriza.setImageResource(R.drawable.fotopersonal);
                                        txvPersonalAutoriza.setText("");
                                        txvTarjetasAutoriza.setText("");

                                    }catch (Exception e){
                                        Log.e(TAG,"btnLimpiarAutoriza.setOnClickListener " + e.getMessage());
                                    }

                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });

            threadAutoriza.start();

        }catch (Exception e){
            Log.e(TAG,"error en inicio de sistema " + e.getMessage());
        }

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

    public void showIP() {
        Shell  sh        = new Shell();
        String params[]  = {"su", "-c", "ifconfig"};
        String cadena    = sh.exec(params);
        String arreglo[] = cadena.split("\n");

        String wifi = " - ";
        String eth  = " - ";

        for (int i = 0; i < arreglo.length; i++) {
            if (ActivityPrincipal.INTERFACE_WLAN && arreglo[i].contains("wlan0")) {
                try {
                    wifi = arreglo[i + 1].toLowerCase().trim().split(":")[1].replace("bcast", "");
                } catch (Exception e) {
                    Log.wtf("showIP", "WLAN0" + e);
                }

            }

            if (ActivityPrincipal.INTERFACE_ETH && arreglo[i].contains("eth0")) {
                try {
                    eth = arreglo[i + 1].toLowerCase().trim().split(":")[1].replace("bcast", "");
                } catch (Exception e) {
                    Log.wtf("showIP", "WLAN0" + e);
                }
            }
        }

        String var1 = "";
        String var2 = "";

        if (ActivityPrincipal.INTERFACE_WLAN) {
            var1 = "wlan:" + wifi;
        }

        if (ActivityPrincipal.INTERFACE_ETH) {
            var2 = "eth:" + eth;
        }

        resultadoIP = var1 + "/" + var2;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txvSistIp.setText(resultadoIP);
            }
        });
    }

    public void ManageData() {
        edtSistIdTerminal.setText(ActivityPrincipal.idTerminal);
        txvSistFirmware.setText("J");
        txvSistSoftware.setText("E");
        txvSistIp.setText("");
        //txvSistTec1.setText("DNI");
        txvSistTec1.setText("HUELLA");
        //txvSistTec3.setText("TECLADO");
    }


    private String getTimehms(int hour, int minuto, int segundo) {


        String s = String.valueOf(segundo);
        if (s.length()==1){
            s = "0"+s;
        }

        if(minuto == -1){
            return s;
        }else{
            String m = String.valueOf(minuto);
            if (m.length()==1){
                m = "0"+m;
            }
            if(hour == -1){
                return m+":"+s;
            }else{
                String h = String.valueOf(hour);
                if (h.length()==1){
                    h = "0"+h;
                }
                return h+":"+m+":"+s;
            }
        }
    }


    private long saveParameter(String idparameter,String value){
        Parameters parameters = new Parameters();
        parameters.setIdparameter(idparameter);
        parameters.setParameter("");
        parameters.setValue(value);
        parameters.setSubparameters("");
        parameters.setEnable(1);
        parameters.setFechaHoraSinc(fechahora.getFechahora());
        Log.v(TAG,"parameters " + parameters.toString());
        long output = -2;
        try{
            output =  queriesParameters.updateParameter(parameters);
            Log.v(TAG,"saveParameter " + output);
        }catch (Exception e){
            Log.e(TAG,"saveParameter " + e.getMessage());
        }

        return output;

    }

    Thread threadAutoriza = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try{
                    Database.getActive = true;
                    informationBD = Database.informationDB;

                    if(informationBD.size()<=15){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txvBDSize.setText(Database.sizeDB + Database.lastModifiedDB);

                                setTxvAutoriza(txvTEmpresas,informationBD.get(0));
                                setTxvAutoriza(txvTEstados,informationBD.get(1));
                                setTxvAutoriza(txvTLlamadas,informationBD.get(2));
                                setTxvAutoriza(txvTLogTerminal,informationBD.get(3));
                                setTxvAutoriza(txvTMarcaciones,informationBD.get(4));
                                setTxvAutoriza(txvTParameters,informationBD.get(5));
                                setTxvAutoriza(txvTPersonal,informationBD.get(6));
                                setTxvAutoriza(txvTPersonalTipolectoraBiometria,informationBD.get(7));
                                setTxvAutoriza(txvTPerTipolectTerm,informationBD.get(8));
                                setTxvAutoriza(txvTServicios,informationBD.get(9));
                                setTxvAutoriza(txvTTarjetaPersonalTipolectora,informationBD.get(10));
                                setTxvAutoriza(txvTTerminal,informationBD.get(11));
                                setTxvAutoriza(txvTTerminalTipolect,informationBD.get(12));
                                setTxvAutoriza(txvTTipoDetalleBiometria,informationBD.get(13));
                                setTxvAutoriza(txvTTipoLectora,informationBD.get(14));

                            }
                        });
                    }
                    Thread.sleep(1000);
                }catch (Exception e){
                    Log.e(TAG,"threadAutoriza " + e.getMessage());
                }

            }
        }
    });

    private void setTxvAutoriza(TextView txv, ArrayList<String> info){
        try{
            if(info.get(1).equalsIgnoreCase("0")){
                txv.setTextColor(Color.RED);
            }else{
                txv.setTextColor(Color.BLACK);
            }
            txv.setText(info.get(0).substring(0,3) + "..." + info.get(0).substring(info.get(0).length()-3) + ": " + info.get(1) + ", " + info.get(2) + ", " + info.get(3));
        }catch (Exception e){
            Log.v(TAG,"setTxvAutoriza " + e.getMessage());
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy");
        try{
            if(threadAutoriza.isAlive()){
                Log.v(TAG,"Deteniendo threadAutoriza");
                threadAutoriza.interrupt();
                Log.v(TAG,"threadAutoriza detenido");
            }
        }catch (Exception e){
            Log.e(TAG,"threadAutoriza.stop() " + e.getMessage());
        }

        // Desactivar el modo activo al salir del Activity Sistema
        // la clase Database contiene esta variable
        Database.getActive = false;


    }
}
