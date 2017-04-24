package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

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

    ImageView btnMasterSuperadmin;
    TabHost host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superadmin);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        util = new Utilities();
        queriesParameters = new QueriesParameters(getApplicationContext());

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Superadmin";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterSuperadmin = (ImageView) findViewById(R.id.btnMasterSuperadmin);

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
        spec.setIndicator("Reserva");
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

            if (ui.isTablet(getApplicationContext())){
                tv.setTextSize(20);
            } else {
                tv.setTextSize(11);
            }

        }

        getParametersAll();
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
        List<String> spinnerArray =  new ArrayList<String>();

        for (int i = 0; i < parametersList.size(); i++){
            spinnerArray.add(parametersList.get(i).Idparameter);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spnSAPItem);
        sItems.setAdapter(adapter);

    }

}
