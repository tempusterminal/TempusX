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
import android.widget.TextView;

import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.util.UserInterfaceM;

public class ActivityLogin extends Activity {

    private String TAG = "TX-L";

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

    ImageView btnMasterLogin;
    Button btnAceptar;
    Button btnCancelar;
    EditText edtUsuario;
    EditText edtContraseña;

    QueriesLogTerminal queriesLogTerminal;

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

        btnMasterLogin      = (ImageView) findViewById(R.id.btnMasterLogin);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterLogin,"/tempus/img/config/","logo.png");
        btnAceptar          = (Button) findViewById(R.id.btnAceptar);
        btnCancelar         = (Button) findViewById(R.id.btnCancelar);
        edtUsuario          = (EditText) findViewById(R.id.edtUsuario);
        edtContraseña       = (EditText) findViewById(R.id.edtContraseña);

        queriesLogTerminal = new QueriesLogTerminal();

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
                    int i = queriesParameters.validateParameterValue("USUARIO_TERMINAL" + "," + usuario + ";" + "PASS_TERMINAL" + "," + contra, 0);// Obvia validaciones
                    // 0 = no coincide en base de datos
                    // 1 = todos son iguales
                    // 2 = devuelve si alguno de los parametros esta deshabilitado

                    //Parameters parameters = queriesParameters.selectParameter("USUARIO_TERMINAL");
                    //Parameters parameters1 = queriesParameters.selectParameter("PASS_TERMINAL");
                    //Log.d("Autorizaciones","Usuario " + parameters.toString());
                    //Log.d("Autorizaciones","Pass " + parameters1.toString());

                    if (i == 1){
                        ActivityPrincipal.UserSession = usuario;
                        queriesLogTerminal.insertLogTerminal(TAG,i + "|" + usuario + "|" +  "",usuario);
                        ui.goToActivity(ActivityLogin.this, ActivityMenu.class, "","");
                    } else {
                        queriesLogTerminal.insertLogTerminal(TAG,i + "|" + usuario + "|" +  contra,"");
                        ui.showAlert(ActivityLogin.this,"warning","Usuario o Contrasena errado(s) " + i);
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
