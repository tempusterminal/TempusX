package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.UserInterfaceM;

import java.util.List;

public class ActivityUsuario extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Parameters parameters1;
    Parameters parameters2;
    QueriesParameters queriesParameters;
    Fechahora fechahora;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

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
        fechahora = new Fechahora();

        /* --- Inicialización de Variables Globales --- */

        ActivityPrincipal.activityActive = "Usuario";

        /* --- Inicialización de Variables Locales --- */

        /* --- Inicialización de Componentes de la Interfaz --- */

        btnMasterUsuario = (ImageView) findViewById(R.id.btnMasterUsuario);

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
                ActualizarUsuario();
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

        ui.showAlert(this,"info","Datos actualizados, " + "Usuario:" + queriesParameters.updateParameter(parameters1) + " Contraseña:" + queriesParameters.updateParameter(parameters2));
    }

    public void CargarUsuario(){

        parameters1 = queriesParameters.selectParameter("USUARIO_TERMINAL");
        parameters2 = queriesParameters.selectParameter("PASS_TERMINAL");

        edtUsuarioNuevo.setText(parameters1.getValue());
        edtContraseñaNuevo.setText(parameters2.getValue());

    }
 }






























