package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ActivitySuperadmin extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    QueriesParameters queriesParameters;
    Utilities util;

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

    ImageView btnMasterSuperadmin;
    TabHost host;

    Button btnSAAccion1;
    Button btnSAAccion2;
    Button btnSAAccion3;
    Button btnSAAccion4;
    Button btnSAAccion5;
    Button btnSAAccion6;
    Button btnSAAccion7;
    Button btnSAAccion8;
    Button btnSAAccion9;

    Spinner spnSAPItem;
    TextView txvSAPTitulo;
    EditText edtSAPDescription;

    List<String> spinnerArray;
    List<String> tituloArray;
    List<String> valorArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superadmin);

        // Aumentar el brillo al maximo brillo establecido
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = (float) (ActivityPrincipal.maxBrilloAhorroEnergia/100);
        getWindow().setAttributes(layoutParams);

        /* --- Inicialización de Objetos --- */

        Log.d("ActivitySuperAdmin","Log1");

        ui = new UserInterfaceM();
        util = new Utilities();
        queriesParameters = new QueriesParameters(getApplicationContext());
        Log.d("ActivitySuperAdmin","Log2");

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Superadmin";

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

        btnMasterSuperadmin = (ImageView) findViewById(R.id.btnMasterSuperadmin);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterSuperadmin,"/tempus/img/config/","logo.png");

        btnSAAccion1 = (Button) findViewById(R.id.btnSAAccion1);
        btnSAAccion2 = (Button) findViewById(R.id.btnSAAccion2);
        btnSAAccion3 = (Button) findViewById(R.id.btnSAAccion3);
        btnSAAccion4 = (Button) findViewById(R.id.btnSAAccion4);
        btnSAAccion5 = (Button) findViewById(R.id.btnSAAccion5);
        btnSAAccion6 = (Button) findViewById(R.id.btnSAAccion6);
        btnSAAccion7 = (Button) findViewById(R.id.btnSAAccion7);
        btnSAAccion8 = (Button) findViewById(R.id.btnSAAccion8);
        btnSAAccion9 = (Button) findViewById(R.id.btnSAAccion9);

        spnSAPItem = (Spinner) findViewById(R.id.spnSAPItem);
        txvSAPTitulo = (TextView) findViewById(R.id.txvSAPTitulo);
        edtSAPDescription = (EditText) findViewById(R.id.edtSAPDescription);

        Log.d("ActivitySuperAdmin","Log3");


        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);

        /* --- Inicialización de Parametros Generales --- */

        btnMasterSuperadmin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToMain();
            }
        });

        host = (TabHost)findViewById(R.id.tabHostAdmin);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Parámetros");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Lectoras");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Máscaras");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Tab4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("SQL");
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec("Tab5");
        spec.setContent(R.id.tab5);
        spec.setIndicator("Acciones");
        host.addTab(spec);

        Log.d("ActivitySuperAdmin","Log4");

        TabWidget widget = host.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tabline);

            if (ui.isTablet(getApplicationContext())){
                tv.setTextSize(20);
            } else {
                tv.setTextSize(11);
            }

        }

        spinnerArray =  new ArrayList<String>();
        tituloArray =  new ArrayList<String>();
        valorArray =  new ArrayList<String>();

        spinnerArray.add("Seleccionar");
        tituloArray.add("");
        valorArray.add("");

        Log.d("ActivitySuperAdmin","Log5");


        // ====================================================================================== //

        btnSAAccion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager dbManager = new DBManager(ActivitySuperadmin.this);
                dbManager.all("1,1,1,1,1,1");
            }
        });

        btnSAAccion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSAAccion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSAAccion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSAAccion5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSAAccion6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSAAccion7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSAAccion8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSAAccion9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        spnSAPItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txvSAPTitulo.setText(tituloArray.get(position));
                edtSAPDescription.setText(valorArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.d("ActivitySuperAdmin","Log6");


        try {
            getParametersAll();
        } catch (Exception e) {
            Log.e("ActivitySuperAdmin",e.getMessage());
        }


        Log.d("ActivitySuperAdmin","Log7");
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

    public void goToMain(){
        try {
            ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"FreeScanOn",null);
        } catch(Exception e) {
            Log.e("Error",e.getMessage());
        }

        ActivityPrincipal.activityActive = "Principal";
        Intent intent = new Intent();
        intent.putExtra("llave","valor");
        setResult(ActivityPrincipal.RESULT_OK, intent);
        finish();
    }

    public void getParametersAll(){
        List<Parameters> parametersList = queriesParameters.select();

        for (int i = 0; i < parametersList.size(); i++){
            spinnerArray.add(parametersList.get(i).Idparameter);
            tituloArray.add(parametersList.get(i).Parameter);
            valorArray.add(parametersList.get(i).Value);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spnSAPItem);
        sItems.setAdapter(adapter);

    }

}
