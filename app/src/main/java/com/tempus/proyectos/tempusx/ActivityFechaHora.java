package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempus.proyectos.util.UserInterfaceM;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityFechaHora extends Activity {

    String TAG = "TX-AFH";

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;

    ImageView btnMasterFechaHora;
    Button btnGuardarFechaHora;
    CalendarView calendarFechaHora;
    EditText edtConfHora;
    EditText edtConfMinuto;
    Button btnConfHoraUp;
    Button btnConfMinutoUp;
    Button btnConfHoraDown;
    Button btnConfMinutoDown;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha_hora);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "FechaHora";

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

        btnMasterFechaHora = (ImageView) findViewById(R.id.btnMasterFechaHora);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterFechaHora,"/tempus/img/config/","logo.png");
        btnGuardarFechaHora = (Button) findViewById(R.id.btnGuardarFechaHora);
        calendarFechaHora = (CalendarView) findViewById(R.id.calendarFechaHora);

        edtConfHora = (EditText) findViewById(R.id.edtConfHora);
        edtConfMinuto = (EditText) findViewById(R.id.edtConfMinuto);

        btnConfHoraUp = (Button) findViewById(R.id.btnConfHoraUp);
        btnConfMinutoUp = (Button) findViewById(R.id.btnConfMinutoUp);
        btnConfHoraDown = (Button) findViewById(R.id.btnConfHoraDown);
        btnConfMinutoDown = (Button) findViewById(R.id.btnConfMinutoDown);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);
        setDateTime();

        /* --- Inicialización de Parametros Generales --- */

        btnMasterFechaHora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityFechaHora.this, ActivityMenu.class , "","");
            }
        });

        btnGuardarFechaHora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String selectedDate = sdf.format(new Date(calendarFechaHora.getDate()));
                String array[] = selectedDate.split("/");
                int dia = Integer.parseInt(array[0]);
                int mes = Integer.parseInt(array[1])-1;
                int año = Integer.parseInt(array[2]);
                int hor = Integer.parseInt(edtConfHora.getText().toString());
                int min = Integer.parseInt(edtConfMinuto.getText().toString());

                Log.v(TAG,"setOnClickListener " + selectedDate);

                updateDateTime(año,mes,dia,hor,min,0);
            }
        });

        btnConfHoraUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageHora("up","hora");
            }
        });

        btnConfHoraDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageHora("down","hora");
            }
        });

        btnConfMinutoUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageHora("up","minuto");
            }
        });

        btnConfMinutoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageHora("down","minuto");
            }
        });

        calendarFechaHora.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.v(TAG,"::::::::::::::" + year + " " + month + " " + dayOfMonth);
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

    public void updateDateTime(int anio, int mes, int dia, int hora, int minuto, int segundo){
        try {
            //set Time to device
            Calendar c = Calendar.getInstance();
            c.set(anio,mes,dia,hora,minuto,segundo);
            AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            am.setTime(c.getTimeInMillis());
        } catch(Exception e) {
            Log.e(TAG,"updateDateTime " + e.getMessage());
        }
    }

    public void setDateTime(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        edtConfHora.setText(String.valueOf(hour));
        edtConfMinuto.setText(String.valueOf(minute));
    }

    public void manageHora(String accion, String tipo){

        String hora = edtConfHora.getText().toString();
        String minuto = edtConfMinuto.getText().toString();

        String myTime = hora+":"+minuto;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(myTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        if (accion.equalsIgnoreCase("up")){
            if (tipo.equalsIgnoreCase("hora")) {
                cal.add(Calendar.HOUR_OF_DAY, 1);
            } else {
                cal.add(Calendar.MINUTE, 1);
            }
        } else {
            if (tipo.equalsIgnoreCase("hora")) {
                cal.add(Calendar.HOUR_OF_DAY, -1);
            } else {
                cal.add(Calendar.MINUTE, -1);
            }
        }
        String result = df.format(cal.getTime());

        edtConfHora.setText(result.substring(0,2));
        edtConfMinuto.setText(result.substring(3,5));

        Log.v(TAG,"manageHora " + result);
    }

    public String manageMonth(int month){
        String mes = "";
        switch (month) {
            case 0:
                mes = "Enero";
                break;
            case 1:
                mes = "Febrero";
                break;
            case 2:
                mes = "Marzo";
                break;
            case 3:
                mes = "Abril";
                break;
            case 4:
                mes = "Mayo";
                break;
            case 5:
                mes = "Junio";
                break;
            case 6:
                mes = "Julio";
                break;
            case 7:
                mes = "Agosto";
                break;
            case 8:
                mes = "Setiembre";
                break;
            case 9:
                mes = "Octubre";
                break;
            case 10:
                mes = "Noviembre";
                break;
            case 11:
                mes = "Diciembre";
                break;
            default:
                mes = "";
                break;

        }
        return mes;
    }


}