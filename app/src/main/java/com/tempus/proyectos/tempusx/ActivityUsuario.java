package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tempus.proyectos.util.UserInterfaceM;

public class ActivityUsuario extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;

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


    public void ActualizarUsuario(){
        String usuario = edtUsuarioNuevo.getText().toString();
        String contra = edtContraseñaNuevo.getText().toString();
    }

    public void CargarUsuario(){

        edtUsuarioNuevo.setText("");
        edtContraseñaNuevo.setText("");

    }
 }






























