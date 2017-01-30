package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.InternalFile;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityConfigini extends Activity {

    /* --- Declaración de Objetos --- */

    UserInterfaceM ui;
    Shell shell;

    /* --- Declaración de Variables Globales --- */

    /* --- Declaración de Variables Locales --- */

    /* --- Declaración de Componentes de la Interfaz --- */

    ImageView btnMasterConfigini;
    Button btnAdm01;
    Button btnAdm02;
    Button btnAdm03;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configini);

        ui = new UserInterfaceM();
        shell = new Shell();

        ActivityPrincipal.activityActive = "Configini";


        btnMasterConfigini = (ImageView) findViewById(R.id.btnMasterConfigini);



        ui.initScreen(this);


        InternalFile file = new InternalFile();
        JSONArray read = file.readJsonArray(Environment.getExternalStorageDirectory().toString() + "tempus/config.json");
        init(read);

        btnMasterConfigini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityConfigini.this, ActivityMenu.class , "","");
            }
        });

        btnAdm01 = (Button) findViewById(R.id.btnAdm01);
        btnAdm02 = (Button) findViewById(R.id.btnAdm02);
        btnAdm03 = (Button) findViewById(R.id.btnAdm03);

        btnAdm01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DateFormat fechaHora = new SimpleDateFormat("ddMMyyyyHHmmss");
                String convertido = fechaHora.format(date);
                String []datos = {"su","-c","cp","/data/data/com.tempus.proyectos.tempusx/databases/TEMPUSPLUS.db","/storage/emulated/0/tempus/TEMPUSPLUS_"+convertido+".db"};
                shell.exec(datos);
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

    public void init(JSONArray array) {

        TableLayout stk = (TableLayout) findViewById(R.id.table_main);

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("ITEM");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER_VERTICAL);
        tv0.setWidth(300);
        tv0.setHeight(45);
        tv0.setPadding(10,0,0,0);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText("DESCRIPCION");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER_VERTICAL);
        tv1.setWidth(450);
        tv1.setHeight(45);
        tv1.setPadding(10,0,0,0);
        tbrow0.addView(tv1);

        stk.addView(tbrow0);

        try {
            for (int i = 0; i < array.getJSONObject(0).length(); i++) {

                try {
                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText("");
                    t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.CENTER_VERTICAL);
                    t1v.setPadding(10,0,0,0);
                    t1v.setWidth(300);
                    t1v.setHeight(45);
                    tbrow.addView(t1v);

                    EditText e2t = new EditText(this);
                    e2t.setText(array.getJSONObject(i).toString());
                    e2t.setTextColor(Color.WHITE);
                    e2t.setGravity(Gravity.CENTER_VERTICAL);
                    e2t.setPadding(10,0,0,0);
                    e2t.setWidth(450);
                    e2t.setHeight(45);
                    tbrow.addView(e2t);

                    stk.addView(tbrow);
                } catch (Exception e) {
                    Log.e("FAIL",e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("FAIL 2",e.getMessage());
        }


    }
}


