package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempus.proyectos.util.UserInterfaceM;

public class ActivityMenu extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    ImageButton ibtn01;
    ImageButton ibtn02;
    ImageButton ibtn03;
    ImageButton ibtn04;
    ImageButton ibtn05;
    ImageButton ibtn06;
    ImageButton ibtn07;
    ImageButton ibtn08;
    ImageButton ibtn09;
    ImageButton ibtn10;

    TextView itxv01;
    TextView itxv02;
    TextView itxv03;
    TextView itxv04;
    TextView itxv05;
    TextView itxv06;
    TextView itxv07;
    TextView itxv08;
    TextView itxv09;
    TextView itxv10;

    TextView ilblNone1;
    TextView ilblNone2;
    TextView ilblNone3;

    ImageView btnMasterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Menu";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        ibtn01 = (ImageButton) findViewById(R.id.ibtn01);
        ibtn02 = (ImageButton) findViewById(R.id.ibtn02);
        ibtn03 = (ImageButton) findViewById(R.id.ibtn03);
        ibtn04 = (ImageButton) findViewById(R.id.ibtn04);
        ibtn05 = (ImageButton) findViewById(R.id.ibtn05);
        ibtn06 = (ImageButton) findViewById(R.id.ibtn06);
        ibtn07 = (ImageButton) findViewById(R.id.ibtn07);
        ibtn08 = (ImageButton) findViewById(R.id.ibtn08);
        ibtn09 = (ImageButton) findViewById(R.id.ibtn09);
        ibtn10 = (ImageButton) findViewById(R.id.ibtn10);

        itxv01 = (TextView) findViewById(R.id.itxv01);
        itxv02 = (TextView) findViewById(R.id.itxv02);
        itxv03 = (TextView) findViewById(R.id.itxv03);
        itxv04 = (TextView) findViewById(R.id.itxv04);
        itxv05 = (TextView) findViewById(R.id.itxv05);
        itxv06 = (TextView) findViewById(R.id.itxv06);
        itxv07 = (TextView) findViewById(R.id.itxv07);
        itxv08 = (TextView) findViewById(R.id.itxv08);
        itxv09 = (TextView) findViewById(R.id.itxv09);
        itxv10 = (TextView) findViewById(R.id.itxv10);

        btnMasterMenu = (ImageView) findViewById(R.id.btnMasterMenu);

        /* --- Inicialización de Métodos --- */

        itxv07.setVisibility(View.INVISIBLE);
        itxv08.setVisibility(View.INVISIBLE);
        itxv09.setVisibility(View.INVISIBLE);
        itxv10.setVisibility(View.INVISIBLE);

        ibtn07.setVisibility(View.INVISIBLE);
        ibtn08.setVisibility(View.INVISIBLE);
        ibtn09.setVisibility(View.INVISIBLE);
        ibtn10.setVisibility(View.INVISIBLE);

        /* --- Inicialización de Parametros Generales --- */

        ui.initScreen(this);

        /* --- Eventos --- */

        ibtn01.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv01.getText().toString());
            }
        });

        ibtn02.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv02.getText().toString());
            }
        });

        ibtn03.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv03.getText().toString());
            }
        });

        ibtn04.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv04.getText().toString());
            }
        });

        ibtn05.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv05.getText().toString());
            }
        });

        ibtn06.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv06.getText().toString());
            }
        });

        ibtn07.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv07.getText().toString());
            }
        });

        ibtn08.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv08.getText().toString());
            }
        });

        ibtn09.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv09.getText().toString());
            }
        });

        ibtn10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv10.getText().toString());
            }
        });

        btnMasterMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToMain();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == ActivityLogin.RESULT_OK) {
                /*
                Bundle b = data.getExtras();
                if (b != null) {
                    Log.v("TEMPUS: ", String.valueOf(b.getSerializable("llave")));
                }
                */
            } else if (resultCode == 0) {
                System.out.println("RESULT CANCELLED");
            }
        }
    }

    public void AccessMenuItem(String opc) {
        switch (opc) {
            case "COMUNICACIÓN":
                ui.goToActivity(ActivityMenu.this,ActivityComunicacion.class,"","");
                break;
            case "SISTEMA":
                ui.goToActivity(ActivityMenu.this,ActivitySistema.class,"","");
                break;
            case "EXPORTAR":
                ui.goToActivity(ActivityMenu.this,ActivityExportar.class,"","");
                break;
            case "HISTORIAL":
                //ui.goToActivity(ActivityMenu.this,ActivityHistorial.class,"","");
                break;
            case "FECHA Y HORA":
                ui.goToActivity(ActivityMenu.this,ActivityFechaHora.class,"","");
                break;
            case "BIOMETRIA":

                if (ActivityPrincipal.TIPO_TERMINAL == 2){
                    ui.goToActivity(ActivityMenu.this,ActivityBiometria.class,"","");
                }

                if (ActivityPrincipal.TIPO_TERMINAL == 3 ){
                    ui.goToActivity(ActivityMenu.this,ActivityGeomano.class,"","");
                }

                break;
            case "SINCRONIZACIÓN":
                ui.goToActivity(ActivityMenu.this,ActivitySincronizacion.class,"","");
                break;
            case "ACTUALIZACIÓN":
                ui.goToActivity(ActivityMenu.this,ActivityActualizar.class,"","");
                break;
            case "ADMINISTRAR":
                ui.goToActivity(ActivityMenu.this,ActivityConfigini.class,"","");
                break;
            case "USUARIO":
                ui.goToActivity(ActivityMenu.this,ActivityUsuario.class,"","");
                break;
            case "NONE":
                ui.goToActivity(ActivityMenu.this,ActivityProduccion.class,"","");
                break;
            default:
                ui.goToActivity(ActivityMenu.this,ActivityPrincipal.class,"","");
                break;
        }
    }

    public void goToMain(){

        Intent i = new Intent(ActivityMenu.this, ActivityPrincipal.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();



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
