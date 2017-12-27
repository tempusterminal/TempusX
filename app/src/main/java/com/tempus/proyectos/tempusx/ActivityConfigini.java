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

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;

    ImageView btnMasterConfigini;
    Button btnAdm01;
    Button btnAdm02;
    Button btnAdm03;

    Button btnMac;

    EditText edtMac1;
    EditText edtMac2;
    EditText edtMac3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configini);

        ui = new UserInterfaceM();
        shell = new Shell();

        ActivityPrincipal.activityActive = "Configini";

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

        btnMasterConfigini = (ImageView) findViewById(R.id.btnMasterConfigini);



        ui.initScreen(this);


        InternalFile file = new InternalFile();
        JSONArray read = file.readJsonArray(Environment.getExternalStorageDirectory().toString() + "/tempus/config.json");
        Log.d("TEMPUS:", String.valueOf(read));
        /*
        init(read);
        */

        btnMasterConfigini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ui.goToActivity(ActivityConfigini.this, ActivityMenu.class , "","");
            }
        });

        btnAdm01 = (Button) findViewById(R.id.btnAdm01);
        btnAdm02 = (Button) findViewById(R.id.btnAdm02);
        btnAdm03 = (Button) findViewById(R.id.btnAdm03);

        btnMac = (Button) findViewById(R.id.btnMac);

        edtMac1 = (EditText) findViewById(R.id.edtMac1);
        edtMac2 = (EditText) findViewById(R.id.edtMac2);
        edtMac3 = (EditText) findViewById(R.id.edtMac3);

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

        btnMac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mac1 = edtMac1.getText().toString();
                String mac2 = edtMac2.getText().toString();
                String mac3 = edtMac3.getText().toString();
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


    public void loadData(){

        String mac1 = "";
        String mac2 = "";
        String mac3 = "";

        edtMac1.setText(mac1);
        edtMac2.setText(mac2);
        edtMac3.setText(mac3);
    }

    /*
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

        String arrayFake[] = {"MACBT01","",""};

        try {
            for (int i = 0; i < array.length(); i++) {

                try {
                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(array.getJSONObject(0).get("ITEM").toString());
                    t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.CENTER_VERTICAL);
                    t1v.setPadding(10,0,0,0);
                    t1v.setWidth(300);
                    t1v.setHeight(45);
                    tbrow.addView(t1v);

                    EditText e2t = new EditText(this);
                    e2t.setText(array.getString(1));
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
    */
}


