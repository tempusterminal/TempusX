package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import android.widget.Toast;

import com.tempus.proyectos.bluetoothSerial.MainEthernet;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.model.Servicios;
import com.tempus.proyectos.data.process.ProcessSyncUSB;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.tcpSerial.UsrTCP;
import com.tempus.proyectos.threads.ThreadHorariosRelay;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;
import com.tempus.proyectos.util.timerPicker.MyTimePickerDialog;
import com.tempus.proyectos.util.timerPicker.TimePicker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Created by gurrutiam on 07/09/2017.
 */

public class ActivityEnergia extends Activity {

    String TAG = "TX-EY";


        /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Utilities util;

    QueriesParameters queriesParameters;


    /* --- Declaración de Variables Globales --- */

    public static List<String> logSincronizacion;

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

    ImageView btnMaster;

    Spinner spinnerrelays;
    TextView lbltiempoactivorelay;
    ImageButton btnConftiempoactivorelay;
    RadioGroup opciones_relay;
    RadioButton desactivado;
    RadioButton marcaok;
    RadioButton marcako;
    RadioButton acceso;
    RadioButton horario;
    Button btnGuardarRelay;
    ImageButton btnTestingRelay;

    Spinner spinnerhorariosrelay;
    TextView lblhorainicio;
    ImageButton btnConfhorainiciohorario;
    TextView lblhorafin;
    ImageButton btnConfhorafinhorario;
    CheckBox checkbox_horario_lunes;
    CheckBox checkbox_horario_martes;
    CheckBox checkbox_horario_miercoles;
    CheckBox checkbox_horario_jueves;
    CheckBox checkbox_horario_viernes;
    CheckBox checkbox_horario_sabado;
    CheckBox checkbox_horario_domingo;
    Button btnGuardarHorarioRelay;
    ImageButton btnRestartHorarioRelay;


    //private ArrayList<ArrayList<String>> arrayListRelays = new ArrayList<ArrayList<String>>();
    //private ArrayList<ArrayList<String>> arrayListHorariosRelay = new ArrayList<ArrayList<String>>();
    private static int positionSpinnerRelays;
    private static int positionSpinnerHorariosRelay;

    private Fechahora fechahora = new Fechahora();

    ThreadHorariosRelay threadHorariosRelay = new ThreadHorariosRelay();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energia);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        util = new Utilities();


        queriesParameters = new QueriesParameters(ActivityPrincipal.context);

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Energia";
        logSincronizacion = new ArrayList<String>();

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

        btnMaster = (ImageView) findViewById(R.id.btnMaster);
        ActivityPrincipal.setImageBitmapOnImageView(btnMaster,"/tempus/img/config/","logo.png");

        spinnerrelays = (Spinner) findViewById(R.id.spinnerrelays);
        lbltiempoactivorelay = (TextView) findViewById(R.id.lbltiempoactivorelay);
        btnConftiempoactivorelay = (ImageButton) findViewById(R.id.btnConftiempoactivorelay);
        opciones_relay = (RadioGroup) findViewById(R.id.opciones_relay);
        desactivado = (RadioButton) findViewById(R.id.desactivado);
        marcaok = (RadioButton) findViewById(R.id.marcaok);
        marcako = (RadioButton) findViewById(R.id.marcako);
        acceso = (RadioButton) findViewById(R.id.acceso);
        horario = (RadioButton) findViewById(R.id.horario);
        btnGuardarRelay = (Button) findViewById(R.id.btnGuardarRelay);
        btnTestingRelay = (ImageButton) findViewById(R.id.btnTestingRelay);

        spinnerhorariosrelay = (Spinner) findViewById(R.id.spinnerhorariosrelay);
        lblhorainicio = (TextView) findViewById(R.id.lblhorainicio);
        btnConfhorainiciohorario = (ImageButton) findViewById(R.id.btnConfhorainiciohorario);
        lblhorafin = (TextView) findViewById(R.id.lblhorafin);
        btnConfhorafinhorario = (ImageButton) findViewById(R.id.btnConfhorafinhorario);
        checkbox_horario_lunes = (CheckBox) findViewById(R.id.checkbox_horario_lunes);
        checkbox_horario_martes = (CheckBox) findViewById(R.id.checkbox_horario_martes);
        checkbox_horario_miercoles = (CheckBox) findViewById(R.id.checkbox_horario_miercoles);
        checkbox_horario_jueves = (CheckBox) findViewById(R.id.checkbox_horario_jueves);
        checkbox_horario_viernes = (CheckBox) findViewById(R.id.checkbox_horario_viernes);
        checkbox_horario_sabado = (CheckBox) findViewById(R.id.checkbox_horario_sabado);
        checkbox_horario_domingo = (CheckBox) findViewById(R.id.checkbox_horario_domingo);
        btnGuardarHorarioRelay = (Button) findViewById(R.id.btnGuardarHorarioRelay);
        btnRestartHorarioRelay = (ImageButton) findViewById(R.id.btnRestartHorarioRelay);


        ArrayAdapter<String> spinnerArrayAdapterRelays;
        ArrayAdapter<String> spinnerArrayAdapterHorariosRelay;
        try{
            // RELAY_01,0,00:00;RELAY_02,0,00:00
            // RELAY_01 0   00:00
            // RELAY_02 0   00:00

            //Ya no sevan a cargar de bd los array
            //La carga sera en ThreadHorariosRelay
            //arrayListRelays = parametersToMatrix(queriesParameters.idparameterToValue("RELAYS"));
            //arrayListHorariosRelay = parametersToMatrix(queriesParameters.idparameterToValue("HORARIOSRELAY"));

            spinnerArrayAdapterRelays = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            spinnerArrayAdapterRelays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for(int i = 0;i < ThreadHorariosRelay.arrayListRelays.size(); i++){
                spinnerArrayAdapterRelays.add(ThreadHorariosRelay.arrayListRelays.get(i).get(0));
            }
            spinnerArrayAdapterHorariosRelay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            spinnerArrayAdapterHorariosRelay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for(int i = 0;i < ThreadHorariosRelay.arrayListHorariosRelay.size(); i++){
                spinnerArrayAdapterHorariosRelay.add(ThreadHorariosRelay.arrayListHorariosRelay.get(i).get(0) + " " + ThreadHorariosRelay.arrayListHorariosRelay.get(i).get(1) + " " + ThreadHorariosRelay.arrayListHorariosRelay.get(i).get(2));
            }

            spinnerrelays.setAdapter(spinnerArrayAdapterRelays);
            spinnerhorariosrelay.setAdapter(spinnerArrayAdapterHorariosRelay);
        }catch (Exception e){
            Log.e(TAG,"array spinner " + e.getMessage());
        }


        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tabs
        TabHost.TabSpec spec;
        //Tab 1
        spec = host.newTabSpec("Tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("RELAY");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("HORARIOS");
        host.addTab(spec);


        TabWidget widget = host.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tabline);
            tv.setTextSize(15);
        }



        /* --- Inicialización de Parametros Generales --- */

        btnMaster.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityEnergia.this, ActivityMenu.class , "","");
            }
        });


        spinnerrelays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Log.v(TAG,"arrayListRelays = " + ThreadHorariosRelay.arrayListRelays.get(position));
                positionSpinnerRelays = position;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lbltiempoactivorelay.setText(ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(2));
                        if(ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(1).equalsIgnoreCase("0")){
                            desactivado.setChecked(true);
                        }else if(ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(1).equalsIgnoreCase("1")){
                            marcaok.setChecked(true);
                        }else if(ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(1).equalsIgnoreCase("2")){
                            marcako.setChecked(true);
                        }else if(ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(1).equalsIgnoreCase("3")){
                            acceso.setChecked(true);
                        }else if(ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(1).equalsIgnoreCase("4")){
                            horario.setChecked(true);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnConftiempoactivorelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    //int m = 0;
                    int s = 0;
                    String val = lbltiempoactivorelay.getText().toString();
                    //m = Integer.parseInt(val.split(":")[0]);
                    //s = Integer.parseInt(val.split(":")[1]);
                    s = Integer.parseInt(val);

                    MyTimePickerDialog mTimePicker = new MyTimePickerDialog(ActivityEnergia.this, new MyTimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, final int hourOfDay, final int minute, final int seconds) {
                            // TODO Auto-generated method stub

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        lbltiempoactivorelay.setText(getTimems(-1,-1,seconds));
                                    }catch (Exception e){
                                        Log.e(TAG,"btnConftiempoactivorelay.setOnClickListener onTimeSet " + e.getMessage());
                                    }
                                }
                            });

                        }
                    }, s, true);
                    mTimePicker.setTitle(ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(0));
                    mTimePicker.show();

                }catch (Exception e){
                    Log.e(TAG,"btnConftiempoactivorelay.setOnClickListener " + e.getMessage());
                }
            }
        });

        spinnerhorariosrelay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Log.v(TAG,"arrayListHorariosRelay = " + ThreadHorariosRelay.arrayListHorariosRelay.get(position));
                positionSpinnerHorariosRelay = position;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            lblhorainicio.setText(ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(1));
                            lblhorafin.setText(ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(2));

                            String dias = ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(3);
                            if(dias.length() == 7){
                                if(dias.substring(0,1).equalsIgnoreCase("1")){
                                    checkbox_horario_lunes.setChecked(true);
                                }else{
                                    checkbox_horario_lunes.setChecked(false);
                                }

                                if(dias.substring(1,2).equalsIgnoreCase("1")){
                                    checkbox_horario_martes.setChecked(true);
                                }else{
                                    checkbox_horario_martes.setChecked(false);
                                }

                                if(dias.substring(2,3).equalsIgnoreCase("1")){
                                    checkbox_horario_miercoles.setChecked(true);
                                }else{
                                    checkbox_horario_miercoles.setChecked(false);
                                }

                                if(dias.substring(3,4).equalsIgnoreCase("1")){
                                    checkbox_horario_jueves.setChecked(true);
                                }else{
                                    checkbox_horario_jueves.setChecked(false);
                                }

                                if(dias.substring(4,5).equalsIgnoreCase("1")){
                                    checkbox_horario_viernes.setChecked(true);
                                }else{
                                    checkbox_horario_viernes.setChecked(false);
                                }

                                if(dias.substring(5,6).equalsIgnoreCase("1")){
                                    checkbox_horario_sabado.setChecked(true);
                                }else{
                                    checkbox_horario_sabado.setChecked(false);
                                }

                                if(dias.substring(6).equalsIgnoreCase("1")){
                                    checkbox_horario_domingo.setChecked(true);
                                }else{
                                    checkbox_horario_domingo.setChecked(false);
                                }
                            }
                        }catch (Exception e){
                            Log.e(TAG,"spinnerhorariosrelay.setOnItemSelectedListener " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGuardarRelay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivityEnergia.this, R.style.AlertDialogCustom));

                builder
                        .setTitle("Guardar Relay")
                        .setMessage("¿Desea guardar la configuración para " + ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(0) + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try{
                                    Long rowaffected;
                                    String ta = "00:00";
                                    String o = "0";
                                    ta = lbltiempoactivorelay.getText().toString();
                                    if(desactivado.isChecked()){
                                        o = "0";
                                    }else if(marcaok.isChecked()){
                                        o = "1";
                                    }else if(marcako.isChecked()){
                                        o = "2";
                                    }else if(acceso.isChecked()){
                                        o = "3";
                                    }else if(horario.isChecked()){
                                        o = "4";
                                    }

                                    ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).set(1,o);
                                    ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).set(2,ta);
                                    Log.v(TAG,ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).toString());

                                    Log.v(TAG,"parametro a guardar->" + parameterToString(ThreadHorariosRelay.arrayListRelays.toString()));
                                    rowaffected = saveParameter("RELAYS",parameterToString(ThreadHorariosRelay.arrayListRelays.toString()));
                                    if(rowaffected == 1){
                                        threadHorariosRelay.writeToArduino(ActivityPrincipal.btSocket01.getOutputStream(),ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(0),false);
                                        ThreadHorariosRelay.recargarHorariosRelayActivos = true;
                                        Toast.makeText(ActivityEnergia.this, "Se guardó la configuración: " + ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(0), Toast.LENGTH_LONG).show();
                                    }else if(rowaffected == -1){
                                        Toast.makeText(ActivityEnergia.this, "NO se guardó la configuración", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(ActivityEnergia.this, "NO se realizó ningún cambio", Toast.LENGTH_LONG).show();
                                    }
                                    //RELAY_01,0,00:00;RELAY_02,0,00:00
                                }catch (Exception e){
                                    Log.e(TAG,"btnGuardarRelay.setOnClickListener " + e.getMessage());
                                }

                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        btnTestingRelay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivityEnergia.this, R.style.AlertDialogCustom));

                builder
                        .setTitle("Probar Relay")
                        .setMessage("¿Desea probar el " + ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(0) + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try{
                                    threadHorariosRelay.startRelayTesting(ThreadHorariosRelay.arrayListRelays.get(positionSpinnerRelays).get(0));
                                    //RELAY_01,0,00:00;RELAY_02,0,00:00
                                }catch (Exception e){
                                    Log.e(TAG,"btnTestingRelay.setOnClickListener " + e.getMessage());
                                }

                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });





        btnConfhorainiciohorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    int h = 0;
                    int m = 0;
                    int s = 0;
                    String val = lblhorainicio.getText().toString();
                    h = Integer.parseInt(val.split(":")[0]);
                    m = Integer.parseInt(val.split(":")[1]);
                    s = Integer.parseInt(val.split(":")[2]);

                    MyTimePickerDialog mTimePicker = new MyTimePickerDialog(ActivityEnergia.this, new MyTimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, final int hourOfDay, final int minute, final int seconds) {
                            // TODO Auto-generated method stub

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        lblhorainicio.setText(getTimems(hourOfDay,minute,seconds));
                                    }catch (Exception e){
                                        Log.e(TAG,"btnConfhorainiciohorario.setOnClickListener onTimeSet " + e.getMessage());
                                    }
                                }
                            });

                        }
                    }, h, m, s, true);
                    mTimePicker.setTitle(ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(0));
                    mTimePicker.show();

                }catch (Exception e){
                    Log.e(TAG,"btnConfhorainiciohorario.setOnClickListener " + e.getMessage());
                }
            }
        });

        btnConfhorafinhorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    int h = 0;
                    int m = 0;
                    int s = 0;
                    String val = lblhorafin.getText().toString();
                    h = Integer.parseInt(val.split(":")[0]);
                    m = Integer.parseInt(val.split(":")[1]);
                    s = Integer.parseInt(val.split(":")[2]);

                    MyTimePickerDialog mTimePicker = new MyTimePickerDialog(ActivityEnergia.this, new MyTimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, final int hourOfDay, final int minute, final int seconds) {
                            // TODO Auto-generated method stub

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        lblhorafin.setText(getTimems(hourOfDay,minute,seconds));
                                    }catch (Exception e){
                                        Log.e(TAG,"btnConfhorafinhorario.setOnClickListener onTimeSet " + e.getMessage());
                                    }
                                }
                            });

                        }
                    }, h, m, s, true);
                    mTimePicker.setTitle(ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(0));
                    mTimePicker.show();

                }catch (Exception e){
                    Log.e(TAG,"btnConfhorafinhorario.setOnClickListener " + e.getMessage());
                }
            }
        });


        btnGuardarHorarioRelay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int diferencia = validatehorariosiniciofin(lblhorainicio.getText().toString(),lblhorafin.getText().toString());

                if(diferencia < 0){
                    Toast.makeText(ActivityEnergia.this, "Horario fin(" + lblhorafin.getText().toString() + ") no puede ser menor que Horario inicio(" + lblhorainicio.getText().toString() + ")", Toast.LENGTH_LONG).show();
                }else if(diferencia < 1){
                    Toast.makeText(ActivityEnergia.this, "Diferencia entre horarios debe ser mayor a 0 segundos", Toast.LENGTH_LONG).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivityEnergia.this, R.style.AlertDialogCustom));
                    builder
                            .setTitle("Guardar Horarios Relay")
                            .setMessage("¿Desea guardar la configuración para " + ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(0) + "?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    try{
                                        Long rowaffected;
                                        String hi = "00:00:00";
                                        String hf = "00:00:00";
                                        String d = "0000000";
                                        hi = lblhorainicio.getText().toString();
                                        hf = lblhorafin.getText().toString();
                                        if(checkbox_horario_lunes.isChecked()){
                                            d = "1";
                                        }else{
                                            d = "0";
                                        }
                                        if(checkbox_horario_martes.isChecked()){
                                            d += "1";
                                        }else{
                                            d += "0";
                                        }
                                        if(checkbox_horario_miercoles.isChecked()){
                                            d += "1";
                                        }else{
                                            d += "0";
                                        }
                                        if(checkbox_horario_jueves.isChecked()){
                                            d += "1";
                                        }else{
                                            d += "0";
                                        }
                                        if(checkbox_horario_viernes.isChecked()){
                                            d += "1";
                                        }else{
                                            d += "0";
                                        }
                                        if(checkbox_horario_sabado.isChecked()){
                                            d += "1";
                                        }else{
                                            d += "0";
                                        }
                                        if(checkbox_horario_domingo.isChecked()){
                                            d += "1";
                                        }else{
                                            d += "0";
                                        }

                                        ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).set(1,hi);
                                        ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).set(2,hf);
                                        ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).set(3,d);

                                        Log.v(TAG,ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).toString());

                                        Log.v(TAG,"parametro a guardar->" + ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay));
                                        Log.v(TAG,"HORARIOSRELAY->" + parameterToString(ThreadHorariosRelay.arrayListHorariosRelay.toString()));
                                        rowaffected = saveParameter("HORARIOSRELAY",parameterToString(ThreadHorariosRelay.arrayListHorariosRelay.toString()));

                                        if(rowaffected == 1){
                                            setSpinner(spinnerhorariosrelay,ThreadHorariosRelay.arrayListHorariosRelay,2,positionSpinnerHorariosRelay);
                                            ThreadHorariosRelay.recargarHorariosRelayActivos = true;
                                            Toast.makeText(ActivityEnergia.this, "Se guardó la configuración: " + ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(0), Toast.LENGTH_LONG).show();
                                        }else if(rowaffected == -1){
                                            Toast.makeText(ActivityEnergia.this, "NO se guardó la configuración", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(ActivityEnergia.this, "NO se realizó ningún cambio", Toast.LENGTH_LONG).show();
                                        }

                                        //HORARIO_RELAY_01,00:00:00,00:00:00,0000000;HORARIO_RELAY_02,00:00:00,00:00:00,0000000;HORARIO_RELAY_03,00:00:00,00:00:00,0000000;HORARIO_RELAY_04,00:00:00,00:00:00,0000000;HORARIO_RELAY_05,00:00:00,00:00:00,0000000;HORARIO_RELAY_06,00:00:00,00:00:00,0000000;HORARIO_RELAY_07,00:00:00,00:00:00,0000000;HORARIO_RELAY_08,00:00:00,00:00:00,0000000;HORARIO_RELAY_09,00:00:00,00:00:00,0000000;HORARIO_RELAY_10,00:00:00,00:00:00,0000000;HORARIO_RELAY_11,00:00:00,00:00:00,0000000;HORARIO_RELAY_12,00:00:00,00:00:00,0000000;HORARIO_RELAY_13,00:00:00,00:00:00,0000000;HORARIO_RELAY_14,00:00:00,00:00:00,0000000;HORARIO_RELAY_15,00:00:00,00:00:00,0000000;HORARIO_RELAY_16,00:00:00,00:00:00,0000000;HORARIO_RELAY_17,00:00:00,00:00:00,0000000;HORARIO_RELAY_18,00:00:00,00:00:00,0000000;HORARIO_RELAY_19,00:00:00,00:00:00,0000000;HORARIO_RELAY_20,00:00:00,00:00:00,0000000;HORARIO_RELAY_21,00:00:00,00:00:00,0000000;HORARIO_RELAY_22,00:00:00,00:00:00,0000000;HORARIO_RELAY_23,00:00:00,00:00:00,0000000;HORARIO_RELAY_24,00:00:00,00:00:00,0000000;HORARIO_RELAY_25,00:00:00,00:00:00,0000000;HORARIO_RELAY_26,00:00:00,00:00:00,0000000;HORARIO_RELAY_27,00:00:00,00:00:00,0000000;HORARIO_RELAY_28,00:00:00,00:00:00,0000000;HORARIO_RELAY_29,00:00:00,00:00:00,0000000;HORARIO_RELAY_30,00:00:00,00:00:00,0000000;HORARIO_RELAY_31,00:00:00,00:00:00,0000000;HORARIO_RELAY_32,00:00:00,00:00:00,0000000;
                                    }catch (Exception e){
                                        Log.e(TAG,"btnGuardarHorarioRelay.setOnClickListener " + e.getMessage());
                                    }

                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }

            }
        });

        btnRestartHorarioRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivityEnergia.this, R.style.AlertDialogCustom));
                    builder
                            .setTitle("Restaurar Horarios Relay")
                            .setMessage("¿Desea restaurar la configuración para " + ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(0) + "?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    try{

                                        Long rowaffected;
                                        String hi = "00:00:00";
                                        String hf = "00:00:00";
                                        String d = "0000000";

                                        ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).set(1,hi);
                                        ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).set(2,hf);
                                        ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).set(3,d);

                                        Log.v(TAG,ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).toString());

                                        Log.v(TAG,"parametro a reaturar->" + ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay));
                                        Log.v(TAG,"HORARIOSRELAY->" + parameterToString(ThreadHorariosRelay.arrayListHorariosRelay.toString()));
                                        rowaffected = saveParameter("HORARIOSRELAY",parameterToString(ThreadHorariosRelay.arrayListHorariosRelay.toString()));

                                        if(rowaffected == 1){
                                            ThreadHorariosRelay.recargarHorariosRelayActivos = true;

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try{
                                                        lblhorainicio.setText("00:00:00");
                                                        lblhorafin.setText("00:00:00");
                                                        checkbox_horario_lunes.setChecked(false);
                                                        checkbox_horario_martes.setChecked(false);
                                                        checkbox_horario_miercoles.setChecked(false);
                                                        checkbox_horario_jueves.setChecked(false);
                                                        checkbox_horario_viernes.setChecked(false);
                                                        checkbox_horario_sabado.setChecked(false);
                                                        checkbox_horario_domingo.setChecked(false);
                                                    }catch (Exception e){
                                                        Log.e(TAG,"btnRestartHorarioRelay UI " + e.getMessage());
                                                    }
                                                }
                                            });

                                            setSpinner(spinnerhorariosrelay,ThreadHorariosRelay.arrayListHorariosRelay,2,positionSpinnerHorariosRelay);

                                            Toast.makeText(ActivityEnergia.this, "Se restauró la configuración: " + ThreadHorariosRelay.arrayListHorariosRelay.get(positionSpinnerHorariosRelay).get(0), Toast.LENGTH_LONG).show();
                                        }else if(rowaffected == -1){
                                            Toast.makeText(ActivityEnergia.this, "NO se restauró la configuración", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(ActivityEnergia.this, "NO se realizó ningún cambio", Toast.LENGTH_LONG).show();
                                        }

                                        //HORARIO_RELAY_01,00:00:00,00:00:00,0000000;HORARIO_RELAY_02,00:00:00,00:00:00,0000000;HORARIO_RELAY_03,00:00:00,00:00:00,0000000;HORARIO_RELAY_04,00:00:00,00:00:00,0000000;HORARIO_RELAY_05,00:00:00,00:00:00,0000000;HORARIO_RELAY_06,00:00:00,00:00:00,0000000;HORARIO_RELAY_07,00:00:00,00:00:00,0000000;HORARIO_RELAY_08,00:00:00,00:00:00,0000000;HORARIO_RELAY_09,00:00:00,00:00:00,0000000;HORARIO_RELAY_10,00:00:00,00:00:00,0000000;HORARIO_RELAY_11,00:00:00,00:00:00,0000000;HORARIO_RELAY_12,00:00:00,00:00:00,0000000;HORARIO_RELAY_13,00:00:00,00:00:00,0000000;HORARIO_RELAY_14,00:00:00,00:00:00,0000000;HORARIO_RELAY_15,00:00:00,00:00:00,0000000;HORARIO_RELAY_16,00:00:00,00:00:00,0000000;HORARIO_RELAY_17,00:00:00,00:00:00,0000000;HORARIO_RELAY_18,00:00:00,00:00:00,0000000;HORARIO_RELAY_19,00:00:00,00:00:00,0000000;HORARIO_RELAY_20,00:00:00,00:00:00,0000000;HORARIO_RELAY_21,00:00:00,00:00:00,0000000;HORARIO_RELAY_22,00:00:00,00:00:00,0000000;HORARIO_RELAY_23,00:00:00,00:00:00,0000000;HORARIO_RELAY_24,00:00:00,00:00:00,0000000;HORARIO_RELAY_25,00:00:00,00:00:00,0000000;HORARIO_RELAY_26,00:00:00,00:00:00,0000000;HORARIO_RELAY_27,00:00:00,00:00:00,0000000;HORARIO_RELAY_28,00:00:00,00:00:00,0000000;HORARIO_RELAY_29,00:00:00,00:00:00,0000000;HORARIO_RELAY_30,00:00:00,00:00:00,0000000;HORARIO_RELAY_31,00:00:00,00:00:00,0000000;HORARIO_RELAY_32,00:00:00,00:00:00,0000000;
                                    }catch (Exception e){
                                        Log.e(TAG,"btnGuardarHorarioRelay.setOnClickListener " + e.getMessage());
                                    }

                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }catch (Exception e){
                    Log.e(TAG,"btnRestartHorarioRelay.setOnClickListener " + e.getMessage());
                }
            }
        });



        //boolean internet = util.isOnline(this);
        //Log.e("Tempus: ", "Coneccion Test -> " + String.valueOf(internet));






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



    private String getTimems(int hour, int minuto, int segundo) {


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

    private String parameterToString(String array){
        array = array.replace(" ","").replace("],[",";").replace("[","").replace("]","");
        return array;
    }

    private int validatehorariosiniciofin(String horarioinicio,String horariofin){
        int tiempoinicio = 0;
        int tiempofin = 0;
        int diferencia = 0;
        try{
            String[] arrayhorarioinicio = horarioinicio.split(":");
            String[] arrayhorariofin = horariofin.split(":");

            if(arrayhorarioinicio.length == 3){
                tiempoinicio = Integer.valueOf(arrayhorarioinicio[0])*60*60 + Integer.valueOf(arrayhorarioinicio[1])*60 + Integer.valueOf(arrayhorarioinicio[2]);
            }
            if(arrayhorariofin.length == 3){
                tiempofin = Integer.valueOf(arrayhorariofin[0])*60*60 + Integer.valueOf(arrayhorariofin[1])*60 + Integer.valueOf(arrayhorariofin[2]);
            }
            //diferencias maxima 84239 (23:23:23 - 00:00:00)
            diferencia = tiempofin - tiempoinicio;
            Log.v(TAG,"validatehorariosiniciofin diferencia " + diferencia);
        }catch (Exception e){
            Log.e(TAG,"validatehorariosiniciofin " + e.getMessage());
        }
        return diferencia;
    }

    private void setSpinner(Spinner spinner, ArrayList<ArrayList<String>> arrayLists, int x, int position){
        ArrayAdapter<String> arrayAdapter;
        try{
            arrayAdapter = new ArrayAdapter<String>(ActivityEnergia.this, android.R.layout.simple_spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for(int i = 0;i < arrayLists.size(); i++){
                String item = "";
                for(int y = 0; y <= x; y++){
                    item += arrayLists.get(i).get(y) + " ";
                }
                arrayAdapter.add(item);
            }
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(position);
        }catch (Exception e){
            Log.e(TAG,"setSpinner " + e.getMessage());
        }
    }









}
