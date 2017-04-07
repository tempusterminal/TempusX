package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.tempus.proyectos.util.UserInterfaceM;

public class ActivitySuperadmin extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

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
        spec.setIndicator("Bluetooth");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Suprema");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Acciones");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Tab6");
        spec.setContent(R.id.tab4);
        spec.setIndicator("SQL");
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
            tv.setTextSize(20);
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

}
