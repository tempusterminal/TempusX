package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.UserInterfaceM;

import java.util.List;

public class ActivityUsuario extends Activity {

    private String TAG = "TX-AU";

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Parameters parameters1;
    Parameters parameters2;
    QueriesParameters queriesParameters;
    QueriesLogTerminal queriesLogTerminal;
    Fechahora fechahora;

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

    ImageView btnMasterUsuario;

    Button btnActualizarUsuario;
    EditText edtUsuarioNuevo;
    EditText edtContraseñaNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        /* --- Inicialización de Objetos --- */

        ui = new UserInterfaceM();
        parameters1 = new Parameters();
        parameters2 = new Parameters();
        queriesParameters = new QueriesParameters(ActivityPrincipal.context);
        queriesLogTerminal = new QueriesLogTerminal();
        fechahora = new Fechahora();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Usuario";

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

        btnMasterUsuario = (ImageView) findViewById(R.id.btnMasterUsuario);
        ActivityPrincipal.setImageBitmapOnImageView(btnMasterUsuario,"/tempus/img/config/","logo.png");

        btnActualizarUsuario = (Button) findViewById(R.id.btnActualizarUsuario);
        edtUsuarioNuevo = (EditText) findViewById(R.id.edtUsuarioNuevo);
        edtContraseñaNuevo = (EditText) findViewById(R.id.edtContraseñaNuevo);

        /* --- Inicialización de Métodos --- */

        ui.initScreen(this);

        CargarUsuario();

        /* --- Inicialización de Parametros Generales --- */

        /* --- Eventos --- */

        btnMasterUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityUsuario.this, ActivityMenu.class , "","");
            }
        });

        btnActualizarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ActivityUsuario.this, R.style.AlertDialogCustom));

                builder
                        .setTitle("Actualizar Usuario")
                        .setMessage("¿Desea el usuario a " + edtUsuarioNuevo.getText().toString() + "?")
                        .setIcon(android.R.drawable.ic_dialog_dialer)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try{
                                    Log.v(TAG,"Actualizando usuario");

                                    ActualizarUsuario();

                                }catch (Exception e){
                                    Log.e(TAG,"btnLimpiarAutoriza.setOnClickListener " + e.getMessage());
                                }

                            }})
                        .setNegativeButton(android.R.string.no, null).show();


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


    public void ActualizarUsuario(){
        String usuario = edtUsuarioNuevo.getText().toString();
        String contra = edtContraseñaNuevo.getText().toString();

        parameters1.setIdparameter("USUARIO_TERMINAL");
        parameters1.setParameter("");
        parameters1.setValue(usuario);
        parameters1.setSubparameters("");
        parameters1.setEnable(1);
        parameters1.setFechaHoraSinc(fechahora.getFechahora());
        //queriesParameters.updateParameter(parameters1);

        parameters2.setIdparameter("PASS_TERMINAL");
        parameters2.setParameter("");
        parameters2.setValue(contra);
        parameters2.setSubparameters("");
        parameters2.setEnable(1);
        parameters2.setFechaHoraSinc(fechahora.getFechahora());
        //queriesParameters.updateParameter(parameters2);

        Log.v(TAG,"Usuario actualizado a " + usuario);

        // Se registra el evento de actualizacion de nombre de usuario y contraseña
        // Se registra el anterior usuario seguido del nuevo usuario
        // anterior usuario > nuevo usuario
        queriesLogTerminal.insertLogTerminal(TAG,ActivityPrincipal.UserSession + ">" + usuario,ActivityPrincipal.UserSession);
        // Se actualiza el nuevo usuario en el UserSession
        ActivityPrincipal.UserSession = usuario;
        ui.showAlert(this,"info","Datos actualizados, " + "Usuario:" + queriesParameters.updateParameter(parameters1) + " Contraseña:" + queriesParameters.updateParameter(parameters2));
    }

    public void CargarUsuario(){

        parameters1 = queriesParameters.selectParameter("USUARIO_TERMINAL");
        parameters2 = queriesParameters.selectParameter("PASS_TERMINAL");

        edtUsuarioNuevo.setText(parameters1.getValue());
        edtContraseñaNuevo.setText(parameters2.getValue());

    }
 }






























