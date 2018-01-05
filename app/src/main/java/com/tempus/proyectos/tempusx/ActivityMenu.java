package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.Toast;

import com.tempus.proyectos.util.UserInterfaceM;

public class ActivityMenu extends Activity {

    String TAG = "TX-AME";

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

    ImageButton ibtn01;
    ImageButton ibtn02;
    ImageButton ibtn03;
    ImageButton ibtn04;
    ImageButton ibtn05;
    ImageButton ibtn06;
    //ImageButton ibtn07;
    //ImageButton ibtn08;
    //ImageButton ibtn09;
    //ImageButton ibtn10;
    ImageButton ibtn11;

    TextView itxv01;
    TextView itxv02;
    TextView itxv03;
    TextView itxv04;
    TextView itxv05;
    TextView itxv06;
    //TextView itxv07;
    //TextView itxv08;
    //TextView itxv09;
    //TextView itxv10;
    TextView itxv11;

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

        ibtn01 = (ImageButton) findViewById(R.id.ibtn01);
        ibtn02 = (ImageButton) findViewById(R.id.ibtn02);
        ibtn03 = (ImageButton) findViewById(R.id.ibtn03);
        ibtn04 = (ImageButton) findViewById(R.id.ibtn04);
        ibtn05 = (ImageButton) findViewById(R.id.ibtn05);
        ibtn06 = (ImageButton) findViewById(R.id.ibtn06);
        //ibtn07 = (ImageButton) findViewById(R.id.ibtn07);
        //ibtn08 = (ImageButton) findViewById(R.id.ibtn08);
        //ibtn09 = (ImageButton) findViewById(R.id.ibtn09);
        //ibtn10 = (ImageButton) findViewById(R.id.ibtn10);
        ibtn11 = (ImageButton) findViewById(R.id.ibtn11);

        itxv01 = (TextView) findViewById(R.id.itxv01);
        itxv02 = (TextView) findViewById(R.id.itxv02);
        itxv03 = (TextView) findViewById(R.id.itxv03);
        itxv04 = (TextView) findViewById(R.id.itxv04);
        itxv05 = (TextView) findViewById(R.id.itxv05);
        itxv06 = (TextView) findViewById(R.id.itxv06);
        //itxv07 = (TextView) findViewById(R.id.itxv07);
        //itxv08 = (TextView) findViewById(R.id.itxv08);
        //itxv09 = (TextView) findViewById(R.id.itxv09);
        //itxv10 = (TextView) findViewById(R.id.itxv10);
        itxv11 = (TextView) findViewById(R.id.itxv11);

        btnMasterMenu = (ImageView) findViewById(R.id.btnMasterMenu);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterMenu,"/tempus/img/config/","logo.png");

        /* --- Inicialización de Métodos --- */

        //itxv07.setVisibility(View.INVISIBLE);
        //itxv08.setVisibility(View.INVISIBLE);
        //itxv09.setVisibility(View.INVISIBLE);
        //itxv10.setVisibility(View.INVISIBLE);

        //ibtn07.setVisibility(View.INVISIBLE);
        //ibtn08.setVisibility(View.INVISIBLE);
        //ibtn09.setVisibility(View.INVISIBLE);
        //ibtn10.setVisibility(View.INVISIBLE);

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

        /*
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

        */

        ibtn11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessMenuItem(itxv11.getText().toString());
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
                Log.v(TAG,"SUBMENU BIOMETRIA -> TIPO_TERMINAL = " + ActivityPrincipal.TIPO_TERMINAL);
                try {
                    if(ActivityPrincipal.TIPO_TERMINAL == 1){
                        Log.v(TAG,"Terminal NO habilitado para BIOMETRÍA");
                        Toast toast = Toast.makeText(ActivityMenu.this,"Terminal NO habilitado para BIOMETRÍA",Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        GradientDrawable gradientDrawable =  new GradientDrawable();
                        gradientDrawable.setCornerRadius(30);
                        gradientDrawable.setColor(getResources().getColor(R.color.colorWarning));
                        view.setPadding(20,20,20,20);
                        view.setBackground(gradientDrawable);
                        toast.show();
                    }else if(ActivityPrincipal.TIPO_TERMINAL == 2){
                        Log.v(TAG,"ABRIENDO " + ActivityPrincipal.TIPO_TERMINAL + " ActivityBiometria");
                        ui.goToActivity(ActivityMenu.this,ActivityBiometria.class,"","");
                    }else if (ActivityPrincipal.TIPO_TERMINAL == 3){
                        Log.v(TAG,"ABRIENDO " + ActivityPrincipal.TIPO_TERMINAL + " ActivityGeomano");
                        ui.goToActivity(ActivityMenu.this,ActivityGeomano.class,"","");
                    }
                } catch(Exception e) {
                    Log.e(TAG,"case BIOMETRIA: MENU - " + e.getMessage());
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
            case "ENERGÍA":
                ui.goToActivity(ActivityMenu.this,ActivityEnergia.class,"","");
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
            Log.e(TAG, "goToMain" + e.getMessage());
        }

        ActivityPrincipal.activityActive = "Principal";

        Intent intent = new Intent();
        intent.putExtra("llave","valor");
        setResult(ActivityPrincipal.RESULT_OK, intent);
        finish();


    }
}
