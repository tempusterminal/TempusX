package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.util.UserInterfaceM;

public class ActivityLogin extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    ImageView btnMasterLogin;
    Button btnAceptar;
    Button btnCancelar;
    EditText edtUsuario;
    EditText edtContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Login";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterLogin      = (ImageView) findViewById(R.id.btnMasterLogin);
        btnAceptar          = (Button) findViewById(R.id.btnAceptar);
        btnCancelar         = (Button) findViewById(R.id.btnCancelar);
        edtUsuario          = (EditText) findViewById(R.id.edtUsuario);
        edtContraseña       = (EditText) findViewById(R.id.edtContraseña);

        /* --- Inicialización de Métodos --- */

        /* --- Inicialización de Parametros Generales --- */

        ui.initScreen(this);

        /* --- Eventos --- */

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String usuario = edtUsuario.getText().toString();
                String contra = edtContraseña.getText().toString();
                try {
                    QueriesParameters queriesParameters = new QueriesParameters(ActivityPrincipal.context);
                    int i = queriesParameters.validateParameter("USUARIO_TERMINAL," + usuario + ";PASS_TERMINAL,"+contra, 0);// Obvia validaciones
                    // 0 = no coincide en base de datos
                    // 1 = todos son iguales
                    // 2 = devuelve si alguno de los parametros esta deshabilitado

                    if (i == 1){
                        ui.goToActivity(ActivityLogin.this, ActivityMenu.class, "","");
                    } else {
                        ui.showAlert(ActivityLogin.this,"warning","Usuario o Contrasela errado (s)");
                    }
                } catch(Exception e){
                    ui.showAlert(ActivityLogin.this,"warning","No se puede acceder.");
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToMain();
            }
        });

        btnMasterLogin.setOnClickListener(new View.OnClickListener() {
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
