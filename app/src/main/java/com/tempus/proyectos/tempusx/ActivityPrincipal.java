package com.tempus.proyectos.tempusx;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.crash.TXExceptionHandler;
import com.tempus.proyectos.bluetoothSerial.MainArduino;
import com.tempus.proyectos.bluetoothSerial.MainSuprema;
import com.tempus.proyectos.data.*;
import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.model.Marcaciones;
import com.tempus.proyectos.data.process.ProcessMarcas;
import com.tempus.proyectos.data.process.ProcessSyncTS;
import com.tempus.proyectos.data.queries.*;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ActivityPrincipal extends Activity {

    /* --- Declaración de Objetos --- */

    public static Boolean isCharging;

    public static OutputStream outputStreamA;
    public static InputStream inputStreamA;

    public static OutputStream outputStreamS;
    public static InputStream inputStreamS;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice deviceArduino;
    BluetoothDevice deviceSuprema;
    BluetoothSocket socketArduino;
    BluetoothSocket socketSuprema;

    public static MainArduino objArduino;
    public static MainSuprema objSuprema;

    UserInterfaceM ui;
    Utilities util;
    Fechahora fechahora;

    private Context context;

    /* --- Declaración de Variables Globales --- */

    public static String idTerminal;

    public int cantSeriales = 0;

    public boolean enableThreadBT;
    public boolean enableThreadReconectBT;

    public boolean enableSerial01;
    public boolean enableSerial02;
    public boolean enableSerial03;

    public boolean banderaSerial01;
    public boolean banderaSerial02;
    public boolean banderaSerial03;

    public boolean infiniteCon01;
    public boolean infiniteCon02;
    public boolean infiniteCon03;

    public boolean enableRebootAtmRec01;
    public boolean enableRebootAtmRec02;
    public boolean enableRebootAtmRec03;

    public int intentosCon01;
    public int intentosCon02;
    public int intentosCon03;

    public int intentosRecon01;
    public int intentosRecon02;
    public int intentosRecon03;

    public int intentosActivarBt;
    public int intentosReconecBt;

    public String msjBoot;
    public boolean enableBoot;

    public static String activityActive;
    public static String flag;

    public String macBTSerial01;
    public String macBTSerial02;
    public String macBTSerial03;

    public static boolean isEnrolling;
    public static boolean isReplicating;
    public static boolean isReplicatingTemplate;
    public static boolean isDeleting;
    public static String huellaEnrollFlag1;
    public static String huellaEnrollFlag2;
    public static String huellaEnroll1;
    public static String huellaEnroll2;
    public static String huellaDelete1;
    public static String huellaDeleteFlag1;

    public boolean enableThreadReplicate;

    private DBManager dbManager;
    private QueriesEmpresa queriesEmpresa;
    private QueriesEstados queriesEstados;
    private QueriesMarcaciones queriesMarcaciones;
    private QueriesPersonal queriesPersonal;
    private QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;
    private QueriesPerTipolectTerm queriesPerTipolectTerm;
    private QueriesServicios queriesServicios;
    private QueriesTarjetaPersonalTipolectora queriesTarjetaPersonalTipolectora;
    private QueriesTerminal queriesTerminal;
    private QueriesTerminalConfiguracion queriesTerminalConfiguracion;
    private QueriesTerminalServicios queriesTerminalServicios;
    private QueriesTerminalTipolect queriesTerminalTipolect;
    private QueriesTipoDetalleBiometria queriesTipoDetalleBiometria;
    private QueriesTipoLectora queriesTipoLectora;
    private QueriesBiometrias queriesBiometrias;
    private QueriesLlamadas queriesLlamadas;

    private QueriesAutorizaciones queriesAutorizaciones;



    /* Teclado */

    ImageButton btnAccessKey;

    TextView txvKeyFondo;
    TextView txvKeyPantalla;
    Button btnKey0;
    Button btnKey1;
    Button btnKey2;
    Button btnKey3;
    Button btnKey4;
    Button btnKey5;
    Button btnKey6;
    Button btnKey7;
    Button btnKey8;
    Button btnKey9;
    Button btnKeyBorrar;
    Button btnKeyIntro;
    String tarjetaKey;

    Boolean visibleKey;


    /* --- Declaración de Variables Locales --- */

    Map<String, String> Lectoras;
    Date tiempoPresente;
    Date tiempoPasado;
    String mNombre;
    String mTarjeta;
    String mMensajePrincipal;
    String mMensajeSecundario;
    String patronAcceso;
    Boolean areaMarcaEnabled;
    Boolean areaAccessEnabled;
    int tiempoMarcacion;
    int tiempoFlag;
    Boolean modoEvento;
    int contadorEventoPantalla;

    /* --- Declaración de Componentes de la Interfaz --- */

    TextView txvMarcacionFondo;
    TextView txvMarcacionNombre;
    TextView txvMarcacionTarjeta;
    TextView txvMarcacionMsjPrincipal;
    TextView txvMarcacionMsjSecundario;

    ImageView btnMaster;
    Button btnAccess1;
    Button btnAccess2;
    Button btnAccess3;
    Button btnAccess4;

    Button buttonWarning01;
    Button buttonWarning02;
    Button buttonWarning03;
    Button buttonWarningHelp;

    TextView txvMensajePantalla;


    Button btnEvent01;
    Button btnEvent02;
    Button btnEvent03;
    Button btnEvent04;
    Button btnEvent05;
    Button btnEvent06;
    Button btnEvent07;
    Button btnEvent08;


    TextView txvFondoInicial;
    TextView txvTextoInicial;
    ProgressBar pbrCargaInicial;

    /* Declaración de fragment_bar */

    TextView txvIdterminal;

    /* Declaración de variables Replicado */

    //Date tiempoReplicar;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        activityActive = "Principal";

        Thread.setDefaultUncaughtExceptionHandler(new TXExceptionHandler(this));

        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
        }

        /* -------------------- Inicialización de Componentes de la Interfaz -------------------- */

        txvMarcacionFondo = (TextView) findViewById(R.id.txvMarcacionFondo);
        txvMarcacionNombre = (TextView) findViewById(R.id.txvMarcacionNombre);
        txvMarcacionTarjeta = (TextView) findViewById(R.id.txvMarcacionTarjeta);
        txvMarcacionMsjPrincipal = (TextView) findViewById(R.id.txvMarcacionMsjPrincipal);
        txvMarcacionMsjSecundario = (TextView) findViewById(R.id.txvMarcacionMsjSecundario);

        btnMaster = (ImageView) findViewById(R.id.btnMaster);

        btnAccess1 = (Button) findViewById(R.id.btnAccess1);
        btnAccess2 = (Button) findViewById(R.id.btnAccess2);
        btnAccess3 = (Button) findViewById(R.id.btnAccess3);
        btnAccess4 = (Button) findViewById(R.id.btnAccess4);

        txvFondoInicial = (TextView) findViewById(R.id.txvFondoInicial);
        txvTextoInicial = (TextView) findViewById(R.id.txvTextoInicial);
        pbrCargaInicial = (ProgressBar) findViewById(R.id.pbrCargaInicial);

        buttonWarning01 = (Button) findViewById(R.id.buttonWarning01);
        buttonWarning02 = (Button) findViewById(R.id.buttonWarning02);
        buttonWarning03 = (Button) findViewById(R.id.buttonWarning03);
        buttonWarningHelp = (Button) findViewById(R.id.buttonWarningHelp);

        txvMensajePantalla = (TextView) findViewById(R.id.txvMensajePantalla);

        btnEvent01 = (Button) findViewById(R.id.btnEvent01);
        btnEvent02 = (Button) findViewById(R.id.btnEvent02);
        btnEvent03 = (Button) findViewById(R.id.btnEvent03);
        btnEvent04 = (Button) findViewById(R.id.btnEvent04);
        btnEvent05 = (Button) findViewById(R.id.btnEvent05);
        btnEvent06 = (Button) findViewById(R.id.btnEvent06);
        btnEvent07 = (Button) findViewById(R.id.btnEvent07);
        btnEvent08 = (Button) findViewById(R.id.btnEvent08);


        btnAccessKey = (ImageButton) findViewById(R.id.btnAccessKey);

        txvKeyFondo = (TextView) findViewById(R.id.txvKeyFondo);
        txvKeyPantalla = (TextView) findViewById(R.id.txvKeyPantalla);
        btnKey0 = (Button) findViewById(R.id.btnKey0);
        btnKey1 = (Button) findViewById(R.id.btnKey1);
        btnKey2 = (Button) findViewById(R.id.btnKey2);
        btnKey3 = (Button) findViewById(R.id.btnKey3);
        btnKey4 = (Button) findViewById(R.id.btnKey4);
        btnKey5 = (Button) findViewById(R.id.btnKey5);
        btnKey6 = (Button) findViewById(R.id.btnKey6);
        btnKey7 = (Button) findViewById(R.id.btnKey7);
        btnKey8 = (Button) findViewById(R.id.btnKey8);
        btnKey9 = (Button) findViewById(R.id.btnKey9);
        btnKeyBorrar = (Button) findViewById(R.id.btnKeyBorrar);
        btnKeyIntro = (Button) findViewById(R.id.btnKeyIntro);


        txvIdterminal = (TextView) findViewById(R.id.txvIdterminal);


        /* -------------------------------------------------------------------------------------- */

        startUtils();           // Iniciar Utilitarios
        ui.initScreen(this);    // Iniciar Configuración de pantallas

        turnOnScreen();         // Encender Pantalla
        //startDBZero();          // Iniciar Base de Datos desde Cero
        listarMarcas();         // Listar Marcaciones
        startDBTerminal();      // Cargar datos BD terminal
        startSerialTerminal();  // Iniciar Serial Terminal



        manageLayerMarcacion(false);    // Ocultar Layer de Marcación
        manageAccessButtons(false);     // Ocultar Botones de Acceso
        manageEventMode(modoEvento);    // Ocultar modo Evento
        manageKeyboard(false);          // Ocultar teclado

        showMsgBoot(true,"Iniciando sistema, por favor espere ...");    // Bloquear pantalla hasta conectar

        buttonWarning01.setVisibility(View.INVISIBLE);
        buttonWarning02.setVisibility(View.INVISIBLE);
        buttonWarning03.setVisibility(View.INVISIBLE);
        buttonWarningHelp.setVisibility(View.INVISIBLE);


        routineBluetooth.start();       // Iniciar proceso de conexion BT

        tiempoPresente = new Date();
        tiempoPasado = new Date();

        tiempoMarcacion = 3;
        tiempoFlag = 8;

        patronAcceso = "";
        areaMarcaEnabled = false;
        areaAccessEnabled = false;


        if (modoEvento) {
            txvMensajePantalla.setText("SELECCIONE EVENTO");
        } else {
            txvMensajePantalla.setText("PASE SU TARJETA");
        }


        /* --- Eventos --- */

        btnMaster.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(areaAccessEnabled) {
                    patronAcceso = "";
                    areaAccessEnabled = false;
                    manageAccessButtons(false);

                    if (modoEvento){
                        txvMensajePantalla.setText("SELECCIONE EVENTO");
                    } else {
                        txvMensajePantalla.setText("PASE SU TARJETA");
                    }


                } else {
                    patronAcceso = "";
                    areaAccessEnabled = true;
                    manageAccessButtons(true);

                    if (modoEvento) {
                        actualizarFlag(null,null);
                    }

                    txvMensajePantalla.setText("PRESIONE LOGO");
                    //Date date = new Date();
                    //tiempoPasado = date;

                }
            }
        });

        btnAccess1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("1");
            }
        });

        btnAccess2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("2");
            }
        });

        btnAccess3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("3");
            }
        });

        btnAccess4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("4");
            }
        });

        buttonWarning01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableThreadReconectBT = true;
            }
        });

        buttonWarning02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reboot();
            }
        });

        btnEvent01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "001";
                Log.v("TEMPUS: ","Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("001",btnEvent01);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        btnEvent02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "009";
                Log.v("TEMPUS: ","Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("009",btnEvent02);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        btnEvent03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "3";
            }
        });

        btnEvent04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "4";
            }
        });

        btnEvent05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "5";
            }
        });

        btnEvent06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "6";
            }
        });

        btnEvent07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "7";
            }
        });

        btnEvent08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "8";
            }
        });


        /* Teclado */

        btnAccessKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == null || flag == ""){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(ActivityPrincipal.this, "DEBE SELECCIONAR UN EVENTO", Toast.LENGTH_LONG).show();
                            try {
                                ui.showAlert(ActivityPrincipal.this,"warning","   DEBE SELECCIONAR UN EVENTO   ");
                            } catch(Exception e) {
                                Toast.makeText(ActivityPrincipal.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    if (visibleKey) {
                        manageKeyboard(false);

                    } else {
                        manageKeyboard(true);
                    }
                }

            }
        });

        btnKey1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("1");
            }
        });

        btnKey2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("2");
            }
        });

        btnKey3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("3");
            }
        });

        btnKey4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("4");
            }
        });

        btnKey5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("5");
            }
        });

        btnKey6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("6");
            }
        });

        btnKey7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("7");
            }
        });

        btnKey8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("8");
            }
        });

        btnKey9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("9");
            }
        });

        btnKey0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("0");
            }
        });

        btnKeyIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("Intro");
            }
        });

        btnKeyBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("Borrar");
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        manageLayerMarcacion(false);
        manageAccessButtons(false);

        areaAccessEnabled = false;

        if (requestCode == 1) {
            if (resultCode == ActivityLogin.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    Log.v("TEMPUS: ", String.valueOf(b.getSerializable("llave")));
                }
            } else if (resultCode == 0) {
                System.out.println("RESULT CANCELLED");
            }
        }
    }

    public void reboot() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i("TEMPUS: ", "No se puede reiniciar!!!!!!!!!!!!!!!!", ex);
        }
    }

    public void startUtils(){
        ui = new UserInterfaceM();
        util = new Utilities();
        fechahora = new Fechahora();
    }

    public void startSerialTerminal() {

        if (enableSerial01) {
            objArduino = new MainArduino();
            banderaSerial01 = false;
        }

        if (enableSerial02) {
            objSuprema = new MainSuprema();
            banderaSerial02 = false;

            isEnrolling = false;
            isReplicating = false;
            isReplicatingTemplate = false;
            isDeleting = false;

            huellaEnrollFlag1 = "";
            huellaEnrollFlag2 = "";
            huellaDeleteFlag1 = "";

            huellaEnroll1 = "";
            huellaEnroll2 = "";
            huellaDelete1 = "";
        }

        if (enableSerial03) {

        }
    }

    public void startDBZero() {

        dbManager = new DBManager(this);
        dbManager.open();

        dbManager.create();
        dbManager.tables();
        dbManager.views();

        dbManager.poblar();

        dbManager.execSQL("DELETE FROM TERMINAL");
        //dbManager.execSQL("UPDATE TERMINAL SET IDTERMINAL = '104'");
        dbManager.execSQL("INSERT INTO TERMINAL(IDTERMINAL) VALUES('101')");

        queriesAutorizaciones = new QueriesAutorizaciones(this);
        queriesAutorizaciones.open();
        Log.d("Autorizaciones","Eliminación de vista Autorizaciones");
        queriesAutorizaciones.drop();
        Log.d("Autorizaciones","Creación de vista Autorizaciones");
        queriesAutorizaciones.create();

        Log.d("Autorizaciones","Listar Autorizaciones");
        List<Autorizaciones> autorizacionesList = queriesAutorizaciones.select();
        for(int i = 0; i < autorizacionesList.size(); i++){
            Log.d("Autorizaciones",autorizacionesList.get(i).toString());
        }

        queriesAutorizaciones.close();

        queriesBiometrias = new QueriesBiometrias(this);
        queriesBiometrias.open();
        Log.d("Autorizaciones","Eliminación de vista Biometrias");
        queriesBiometrias.create();
        Log.d("Autorizaciones","Creación de vista Biometrias");
        queriesBiometrias.close();

        dbManager.close();


        queriesLlamadas = new QueriesLlamadas(this);

        queriesLlamadas.poblar();



    }

    public void listarMarcas() {

        dbManager = new DBManager(this);
        dbManager.open();

        queriesMarcaciones = new QueriesMarcaciones(this);
        queriesMarcaciones.open();
        Log.d("Autorizaciones","Cantidad de registros MARCACIONES: " + String.valueOf(queriesMarcaciones.count()));

        List<Marcaciones> marcacionesList = queriesMarcaciones.select();
        for(int i = 0; i < marcacionesList.size(); i++){
            Log.d("Autorizaciones",marcacionesList.get(i).toString());
        }

        queriesMarcaciones.close();
        dbManager.close();
    }

    public void startDBTerminal(){

        isCharging = false;

        idTerminal = dbManager.valexecSQL("SELECT IDTERMINAL FROM TERMINAL");
        Log.d("Autorizaciones","Terminal: " + idTerminal);

        modoEvento = false;

        contadorEventoPantalla = 0;

        if (modoEvento) {
            flag = null;
        } else {
            flag = "127";
        }

        tarjetaKey = "";
        visibleKey = false;

        enableSerial01 = true; // Arduino
        enableSerial02 = true; // Suprema
        enableSerial03 = false; // HandPunch

        cantSeriales = 2; // Cantidad de Seriales Existentes para conectar

        enableThreadBT = true;
        enableThreadReconectBT = true;

        infiniteCon01 = false;
        infiniteCon02 = false;
        infiniteCon03 = false;

        intentosCon01 = 10;
        intentosCon02 = 10;
        intentosCon03 = 10;

        enableRebootAtmRec01 = false;
        enableRebootAtmRec02 = false;
        enableRebootAtmRec03 = false;

        intentosRecon01 = 10;
        intentosRecon02 = 10;
        intentosRecon03 = 10;

        intentosActivarBt = 3;
        intentosReconecBt = 3;

        Lectoras = new HashMap<String, String>();
        Lectoras.put("01","TECLADO");
        Lectoras.put("02","DNI");
        Lectoras.put("04","PROXIMIDAD");
        Lectoras.put("07","HUELLA SUPREMA");
        //Lectoras.put("09","DNI");

        //PERQUERA DIAMANTE
        //macBTSerial01 = "20:16:08:10:66:60";// corpac 98:D3:32:20:5B:7E
        //macBTSerial02 = "20:16:08:10:63:78";// corpac 98:D3:34:90:7D:C0

        //DEMO EDITORA WIN
        macBTSerial01 = "00:15:83:35:6C:85";
        macBTSerial02 = "98:D3:33:80:91:98";

        //CORPAC
        //macBTSerial01 = "98:D3:32:20:5B:7E";
        //macBTSerial02 = "98:D3:34:90:7D:C0";

        //macBTSerial01 = "98:D3:32:30:62:0E";
        //macBTSerial02 = "00:00:00:00:00:00";
        //macBTSerial03 = "00:00:00:00:00:00";

        //DIRESA
        //macBTSerial01 = "20:16:08:10:42:38";
        //macBTSerial02 = "20:16:08:09:04:41";
        //macBTSerial03 = "00:00:00:00:00:00";

        //EDITORA
        //macBTSerial01 = "00:15:83:35:6C:85";
        //macBTSerial02 = "98:D3:33:80:91:98";

        //DIRESA CLIENTE TX_Rev0.1
        //macBTSerial01 = "00:15:83:35:80:A5"; //hc-06
        //macBTSerial02 = "20:16:08:10:60:73"; //hc-06

        //DIRESA CLIENTE TX_Rev0.2
        //macBTSerial01 = "20:16:08:10:83:58";
        //macBTSerial02 = "20:16:08:10:60:73";

        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(this);

    }

    public void setScreenLock(boolean visible,String mensaje) {
        if (visible) {
            txvTextoInicial.setText(mensaje);
            txvFondoInicial.setVisibility(View.VISIBLE);
            txvTextoInicial.setVisibility(View.VISIBLE);
            pbrCargaInicial.setVisibility(View.VISIBLE);
        } else {
            txvTextoInicial.setText(mensaje);
            txvFondoInicial.setVisibility(View.INVISIBLE);
            txvTextoInicial.setVisibility(View.INVISIBLE);
            pbrCargaInicial.setVisibility(View.INVISIBLE);
        }
    }

    public void SetScreenLockReplicate(boolean visible, String mensaje){
        if (visible) {
            txvTextoInicial.setText(mensaje);
            txvFondoInicial.setVisibility(View.VISIBLE);
            txvTextoInicial.setVisibility(View.VISIBLE);
            pbrCargaInicial.setVisibility(View.VISIBLE);
        } else {
            txvTextoInicial.setText(mensaje);
            txvFondoInicial.setVisibility(View.INVISIBLE);
            txvTextoInicial.setVisibility(View.INVISIBLE);
            pbrCargaInicial.setVisibility(View.INVISIBLE);
        }

    }

    public void turnOnScreen(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
        wakeLock.release();
    }

    public void manageKeyboard(boolean visible){
        tarjetaKey = "";

        if (visible) {
            txvKeyFondo.setVisibility(View.VISIBLE);
            txvKeyPantalla.setVisibility(View.VISIBLE);
            btnKey0.setVisibility(View.VISIBLE);
            btnKey1.setVisibility(View.VISIBLE);
            btnKey2.setVisibility(View.VISIBLE);
            btnKey3.setVisibility(View.VISIBLE);
            btnKey4.setVisibility(View.VISIBLE);
            btnKey5.setVisibility(View.VISIBLE);
            btnKey6.setVisibility(View.VISIBLE);
            btnKey7.setVisibility(View.VISIBLE);
            btnKey8.setVisibility(View.VISIBLE);
            btnKey9.setVisibility(View.VISIBLE);
            btnKeyBorrar.setVisibility(View.VISIBLE);
            btnKeyIntro.setVisibility(View.VISIBLE);

            visibleKey = true;

            Date date = new Date();
            tiempoPasado = date;
        } else {
            txvKeyFondo.setVisibility(View.INVISIBLE);
            txvKeyPantalla.setVisibility(View.INVISIBLE);
            btnKey0.setVisibility(View.INVISIBLE);
            btnKey1.setVisibility(View.INVISIBLE);
            btnKey2.setVisibility(View.INVISIBLE);
            btnKey3.setVisibility(View.INVISIBLE);
            btnKey4.setVisibility(View.INVISIBLE);
            btnKey5.setVisibility(View.INVISIBLE);
            btnKey6.setVisibility(View.INVISIBLE);
            btnKey7.setVisibility(View.INVISIBLE);
            btnKey8.setVisibility(View.INVISIBLE);
            btnKey9.setVisibility(View.INVISIBLE);
            btnKeyBorrar.setVisibility(View.INVISIBLE);
            btnKeyIntro.setVisibility(View.INVISIBLE);
            //actualizarFlag(null,null);
            visibleKey = false;
        }
        txvKeyPantalla.setText(tarjetaKey);
    }

    public void manageLayerMarcacion(boolean visible){
        if(flag == null || flag == ""){
            if(visible){
                txvMarcacionNombre.setVisibility(View.INVISIBLE);
                txvMarcacionTarjeta.setVisibility(View.INVISIBLE);
                txvMarcacionMsjPrincipal.setVisibility(View.VISIBLE);
                txvMarcacionFondo.setVisibility(View.VISIBLE);
                txvMarcacionMsjPrincipal.setText("SELECIONE EVENTO");
                txvMarcacionMsjSecundario.setVisibility(View.INVISIBLE);

            }else{
                txvMarcacionNombre.setVisibility(View.INVISIBLE);
                txvMarcacionTarjeta.setVisibility(View.INVISIBLE);
                txvMarcacionFondo.setVisibility(View.INVISIBLE);
                txvMarcacionMsjPrincipal.setVisibility(View.INVISIBLE);
                txvMarcacionMsjSecundario.setVisibility(View.INVISIBLE);
                actualizarFlag(null,null);

            }
        }else{
            if (visible) {
                txvMarcacionNombre.setVisibility(View.VISIBLE);
                txvMarcacionTarjeta.setVisibility(View.VISIBLE);
                txvMarcacionFondo.setVisibility(View.VISIBLE);
                txvMarcacionMsjPrincipal.setVisibility(View.VISIBLE);
                txvMarcacionMsjSecundario.setVisibility(View.VISIBLE);

            } else {
                txvMarcacionNombre.setVisibility(View.INVISIBLE);
                txvMarcacionTarjeta.setVisibility(View.INVISIBLE);
                txvMarcacionFondo.setVisibility(View.INVISIBLE);
                txvMarcacionMsjPrincipal.setVisibility(View.INVISIBLE);
                txvMarcacionMsjSecundario.setVisibility(View.INVISIBLE);

            }
        }


    }

    public void manageEventMode(boolean visible) {

        if (visible) {
            btnEvent01.setVisibility(View.VISIBLE);
            btnEvent02.setVisibility(View.VISIBLE);
            btnEvent03.setVisibility(View.INVISIBLE);
            btnEvent04.setVisibility(View.INVISIBLE);
            btnEvent05.setVisibility(View.INVISIBLE);
            btnEvent06.setVisibility(View.INVISIBLE);
            btnEvent07.setVisibility(View.INVISIBLE);
            btnEvent08.setVisibility(View.INVISIBLE);

        } else {
            btnEvent01.setVisibility(View.INVISIBLE);
            btnEvent02.setVisibility(View.INVISIBLE);
            btnEvent03.setVisibility(View.INVISIBLE);
            btnEvent04.setVisibility(View.INVISIBLE);
            btnEvent05.setVisibility(View.INVISIBLE);
            btnEvent06.setVisibility(View.INVISIBLE);
            btnEvent07.setVisibility(View.INVISIBLE);
            btnEvent08.setVisibility(View.INVISIBLE);
        }
    }

    public void manageAccessButtons(boolean visible){
        if (visible) {
            btnAccess1.setVisibility(View.VISIBLE);
            btnAccess2.setVisibility(View.VISIBLE);
            btnAccess3.setVisibility(View.VISIBLE);
            btnAccess4.setVisibility(View.VISIBLE);
            if (modoEvento) {
                manageEventMode(false);
            }

        } else {
            btnAccess1.setVisibility(View.INVISIBLE);
            btnAccess2.setVisibility(View.INVISIBLE);
            btnAccess3.setVisibility(View.INVISIBLE);
            btnAccess4.setVisibility(View.INVISIBLE);
            if (modoEvento) {
                manageEventMode(true);
            }

        }
    }

    public void AccessManager(String n){
        patronAcceso = patronAcceso + n;

        switch(patronAcceso) {
            case "123432":
                txvMensajePantalla.setText("PASE SU TARJETA");
                Intent intent01 = new Intent(ActivityPrincipal.this, ActivityLogin.class);
                intent01.putExtra("llave", "valor");
                startActivityForResult(intent01, 1);
                patronAcceso = "";
                try {
                    ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"FreeScanOff",null);
                } catch(Exception e) {
                    Log.v("TEMPUS: ","ERROR ESTABLECIENDO CONEXION CON HUELLERO");
                }

                break;
            case "42143231":
                Intent intent02 = new Intent(ActivityPrincipal.this, ActivityConfigini.class);
                intent02.putExtra("llave", "valor");
                startActivityForResult(intent02, 1);
                patronAcceso = "";
                try {
                    ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"FreeScanOff",null);
                } catch(Exception e) {
                    Log.v("TEMPUS: ","ERROR ESTABLECIENDO CONEXION CON HUELLERO");
                }
                break;
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------
            // -----------------------------------------------------------------------------------------

            //NumberTemplate
            case "444432":
                Log.d("Autorizaciones","Patron 4444");
                Log.d("Autorizaciones","objSuprema.toString(): " + objSuprema.toString());
                objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"Cancel",null);
                break;
            case "222232":
                Log.d("Autorizaciones","Patron 2222");

                try{
                    objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"DeleteAllTemplates",null);
                    util.sleep(250);
                }catch(Exception e){
                    Log.d("Autorizaciones","Error Enrolamiento: " + e.getMessage());
                }
                objSuprema.limpiarTramaSuprema();

                break;
            case "333332":
                Log.d("Autorizaciones","Patron 3333");
                try{
                    objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"NumberTemplate",null);



                    util.sleep(800);
                }catch(Exception e){
                    Log.d("Autorizaciones","Error Enrolamiento: " + e.getMessage());
                }
                objSuprema.limpiarTramaSuprema();
                break;
            case "111132":

                isReplicating = true; //Habilitar Replicado

                 // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------

                break;

            case "42423123":
                Shell sh1 = new Shell();
                String comando1[]={"su","-c","busybox","ifconfig","eth0","172.20.1.119","up"};
                try{
                    sh1.exec(comando1);
                    //Toast.makeText(this,"ifconfig eth0 success!",Toast.LENGTH_SHORT);
                }catch(Exception e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
                }
                break;

            case "42421111":
                Shell sh2 = new Shell();
                String comando2[]={"su","-c","busybox","route","add","default","gw","172.20.0.1","eth0"};
                try{
                    sh2.exec(comando2);
                    //Toast.makeText(this,"route add gw success!",Toast.LENGTH_SHORT);
                }catch(Exception e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
                }
                break;

            case "1324111":

                startDBZero();          // Iniciar Base de Datos desde Cero

                break;
            default:
                break;
        }
    }

    public void fnInicializarRutinas(){

        if (enableSerial01) {
            routineArduino.start();
        }

        if (enableSerial02) {
            routineSuprema.start();
        }

        routineScreen.start();
        //Log.v("TEMPUS: ","routineScreen " + routineScreen.getName());
        routineRevSocket.start();
        //Log.v("TEMPUS: ","routineRevSocket " + routineRevSocket.getName());
        routineReplicate.start();     //Iniciar Replicado


        if (modoEvento) {
            routineEventScreen.start();
        }

        ProcessSyncTS processSyncTS = new ProcessSyncTS("Hilo_SyncMarcas");
        processSyncTS.start(this);

        ProcessMarcas processMarcas = new ProcessMarcas("Sync_Autorizacion");
        processMarcas.start(this);

    }

    public void fnBluetoothManager() {

        boolean control1 = false;
        boolean control2 = false;

        int contAct = 0;
        int contCon = 0;

        showMsgBoot(true,"Iniciando Sistema ... ");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonWarning01.setVisibility(View.INVISIBLE);
                buttonWarning02.setVisibility(View.INVISIBLE);
                buttonWarningHelp.setVisibility(View.INVISIBLE);
            }
        });



        if (bluetoothAdapter == null) {
            Log.v("TEMPUS: ", "DISPOSITIVO NO SOPORTA BLUETOOTH");
        } else {

            boolean iniciando = true;

            while (iniciando) {
                control1 = fnActivarBluetooth();    // Activamos Bluetooth
                if (control1) {
                    util.sleep(500);

                    control2 = fnConectarBluetooth();   // Conectamos Bluetooth
                    if (control2) {

                        showMsgBoot(true,"Finalizando ...");

                        util.sleep(500);

                        enableThreadReconectBT = false;
                        enableThreadBT = false;

                        fnInicializarRutinas();

                        showMsgBoot(false,"");

                        Log.v("TEMPUS: ", "SERIALES ESTABLECIDOS");
                        iniciando = false;

                    } else {
                        contCon = contCon + 1;
                        Log.v("TEMPUS: ", "ERROR CONECTANDO SERIALES");
                        showMsgBoot(true,"Reiniciando Interfaces ... 002");

                        if (intentosReconecBt == contCon) {
                            showMsgBoot(true,"Error General");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                    buttonWarningHelp.setVisibility(View.VISIBLE);
                                }
                            });
                            enableThreadReconectBT = false;
                            iniciando = false;
                            contCon = 0;
                        }
                    }
                } else {
                    contAct = contAct + 1;
                    Log.v("TEMPUS: ", "ERROR ACTIVANDO BLUETOOTH");
                    showMsgBoot(true,"Reiniciando Interfaces ... 001");

                    if (intentosActivarBt == contAct) {
                        showMsgBoot(true,"Error General");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonWarning01.setVisibility(View.VISIBLE);
                                buttonWarning02.setVisibility(View.VISIBLE);
                                buttonWarningHelp.setVisibility(View.VISIBLE);
                            }
                        });
                        enableThreadReconectBT = false;
                        iniciando = false;
                        contAct = 0;
                    }
                }
                util.sleep(500);
            }
        }
    }

    public boolean fnActivarBluetooth() {
        boolean resultado = false;

        if (!bluetoothAdapter.isEnabled()) {
            Log.v("TEMPUS: ", "BLUETOOTH DESHABILITADO");
            BluetoothAdapter.getDefaultAdapter().enable();
            resultado = true;
        } else {
            BluetoothAdapter.getDefaultAdapter().disable();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BluetoothAdapter.getDefaultAdapter().enable();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.v("TEMPUS: ", "BLUETOOTH HABILITADO");
            resultado = true;
        }

        return resultado;
    }

    public boolean fnConectarBluetooth() {

        int n = 0;
        int p = 0;
        int pairedSize = 0;

        boolean resultado = false;

        boolean existsSerial01 = false;
        boolean existsSerial02 = false;
        boolean existsSerial03 = false;

        boolean validar01 = false;
        boolean validar02 = false;
        boolean validar03 = false;

        Set bondedDevices = bluetoothAdapter.getBondedDevices();

        if (bondedDevices.isEmpty()) {
            Log.v("TEMPUS: ", "NINGUN DISPOSITIVO VINCULADO");

            showMsgBoot(true,"No se encuentran interfaces activas");

        } else {
            Log.v("TEMPUS: ", "DISPOSITIVOS VINCULADOS");

            showMsgBoot(true,"Iniciando interfaces ... ");

            Set<BluetoothDevice> bondedDevicesX = bluetoothAdapter.getBondedDevices();
            pairedSize = bondedDevicesX.size();


            for (BluetoothDevice dispositivo : bluetoothAdapter.getBondedDevices()) {

                p = p + 1;

                if (dispositivo.getAddress().equalsIgnoreCase(macBTSerial01) && enableSerial01) {
                    deviceArduino = dispositivo;
                    Log.v("TEMPUS: ", "DISPOSITIVO ENCONTRADO: ARDUINO");
                    existsSerial01 = true;
                    n = n + 1;
                    showMsgBoot(true,"Procesando I001 ... ");
                }

                if (dispositivo.getAddress().equalsIgnoreCase(macBTSerial02) && enableSerial02) {
                    deviceSuprema = dispositivo;
                    Log.v("TEMPUS: ", "DISPOSITIVO ENCONTRADO: SUPREMA");
                    existsSerial02 = true;
                    n = n + 1;
                    showMsgBoot(true,"Procesando I002 ... ");
                }

                if (dispositivo.getAddress().equalsIgnoreCase(macBTSerial03) && enableSerial03) {
                    existsSerial03 = true;
                    n = n + 1;
                    showMsgBoot(true,"Procesando I003 ... ");
                }

                if(pairedSize == p) {
                    break;
                }
            }


            if (cantSeriales == n) { // Si la cant de interfaces encontradas es igual a la matriculada
                if (enableSerial01 && existsSerial01) { // Conectamos Arduino
                    int cont01 = 0;
                    boolean conArduino = true;
                    util.sleep(1000);
                    while (conArduino){
                        cont01 = cont01 + 1;
                        showMsgBoot(true,"Conectando I001 ... ("+cont01+")");
                        if (ConectarSocketArduino()) {
                            Log.v("TEMPUS: ", "STREAM ARDUINO OK");
                            banderaSerial01 = true;
                            conArduino = false;
                            validar01 = true;
                        } else {
                            Log.v("TEMPUS: ", "STREAM ARDUINO KO");
                            util.sleep(1000);

                            if (!infiniteCon01) {
                                if (intentosCon01 == cont01) {
                                    Log.v("TEMPUS","Cantidad de intenos maximos superados I001");
                                    cont01 = 0;
                                    conArduino = false;
                                }
                            }
                        }
                    }
                }

                if (enableSerial02 && existsSerial02) { // Conectamos Suprema
                    int cont02 = 0;
                    boolean conSuprema = true;
                    util.sleep(1000);
                    while (conSuprema) {
                        cont02 = cont02 + 1;
                        showMsgBoot(true,"Conectando I002 ... ("+cont02+")");
                        if (ConectarSocketSuprema()) {
                            Log.v("TEMPUS: ", "STREAM SUPREMA OK");
                            banderaSerial02 = true;
                            conSuprema = false;
                            validar02 = true;
                        } else {
                            Log.v("TEMPUS: ", "STREAM SUPREMA KO");
                            util.sleep(1000);

                            if (!infiniteCon02) {
                                if (intentosCon02 == cont02) {
                                    Log.v("TEMPUS","Cantidad de intenos maximos superados I002");
                                    cont02 = 0;
                                    conSuprema = false;
                                }
                            }
                        }
                    }
                }

                if (enableSerial03 && existsSerial03) { // Conectamos HandPunch
                    int cont03 = 0;
                    showMsgBoot(true,"Conectando I003 ... ");
                    validar01 = false;

                    if (!infiniteCon03) {
                        if (intentosCon03 == cont03) {
                            Log.v("TEMPUS","Cantidad de intenos maximos superados I002");
                            cont03 = 0;
                        }
                    }
                }

            } else { // Si existe diferencia entre las cantidades encontradas y matriculadas
                String msj = "";
                if (enableSerial01 && !existsSerial01) { msj = msj + " IS001 "; }
                if (enableSerial02 && !existsSerial02) { msj = msj + " IS002 "; }
                if (enableSerial03 && !existsSerial03) { msj = msj + " IS003 "; }
                showMsgBoot(true,"Error: (" + msj + ")");
            }

            if ((enableSerial01 == validar01) && (enableSerial02 == validar02) && (enableSerial03 == validar03)) {
                resultado = true;
            }
        }

        return resultado;
    }

    public void showMsgBoot(boolean visible, String msj){
        enableBoot = visible;
        msjBoot = msj;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setScreenLock(enableBoot,msjBoot);
            }
        });
    }

    public boolean ConectarSocketArduino() {
        boolean resultado = false;
        socketArduino = null;
        outputStreamA = null;
        inputStreamA = null;
        try {
            //socketArduino =(BluetoothSocket) deviceArduino.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(deviceArduino,1);
            socketArduino = deviceArduino.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socketArduino.connect();
            outputStreamA = socketArduino.getOutputStream();
            inputStreamA = socketArduino.getInputStream();
            resultado = true;
        } catch (Exception e) {
            Log.v("TEMPUS: ", "ConectarSocketArduino > " + String.valueOf(e));
            try {
                socketArduino.close();
            } catch (IOException e1) {
                Log.v("TEMPUS: ", "ConectarSocketArduino > " + String.valueOf(e));
            }
        }
        return resultado;
    }

    public boolean ConectarSocketSuprema() {
        boolean resultado = false;
        socketSuprema = null;
        outputStreamS = null;
        inputStreamS = null;
        try {
            socketSuprema = deviceSuprema.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socketSuprema.connect();
            outputStreamS = socketSuprema.getOutputStream();
            inputStreamS = socketSuprema.getInputStream();
            resultado = true;
        } catch (Exception e) {
            Log.v("TEMPUS: ", "ConectarSocketSuprema > " + String.valueOf(e));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        return resultado;
    }

    public boolean ConectarSocketHandPunch() {
        boolean resultado = false;
        return resultado;
    }


    /* =============================== EVALUAR TRAMA ============================= */

    public void evaluarTrama(String origen, String trama) {
        //Log.v("TEMPUS: ","origen >>> " + origen);
        //Log.v("TEMPUS: ","trama >>> " + trama);
        switch (origen) {
            case "Arduino":
                objArduino.setTrama(trama);
                objArduino.estructurarTramaArduinoGeneral();
                objArduino.estructurarTramaArduinoMensaje();
                objArduino.setValorMascara();
                //26 4011000000000000000069ba0a
                switch (objArduino.getTipoMensaje()){
                    case "00":
                        if (activityActive.equals("Principal")){

                            if(modoEvento ){
                                // no ace==ptamos nada de marcaciones
                                if (flag!="" || flag!=null){
                                    String nroLector = "04";
                                    String tarjeta = objArduino.getDatosLector().substring(objArduino.getMascaraIni(),objArduino.getMascaraFin());
                                    Log.v("TEMPUS: ",tarjeta);
                                    MarcacionMaster(nroLector,tarjeta);

                                    // Marcando
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            turnOnScreen();
                                            manageLayerMarcacion(true);
                                            actualizarFlag(null,null);
                                        }
                                    });
                                } else {
                                    Log.d("TEMPUS: ","DEBE SELECCIONAR UN EVENTO");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Toast.makeText(ActivityPrincipal.this, "DEBE SELECCIONAR UN EVENTO", Toast.LENGTH_LONG).show();
                                            try {
                                                ui.showAlert(ActivityPrincipal.this,"warning","   DEBE SELECCIONAR UN EVENTO   ");
                                            } catch(Exception e) {
                                                Toast.makeText(ActivityPrincipal.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });




                                }
                            } else {
                                String nroLector = "04";
                                String tarjeta = objArduino.getDatosLector().substring(objArduino.getMascaraIni(),objArduino.getMascaraFin());
                                Log.v("TEMPUS: ",tarjeta);
                                MarcacionMaster(nroLector,tarjeta);

                                // Marcando
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        turnOnScreen();
                                        manageLayerMarcacion(true);
                                    }
                                });
                            }

                            /* -----------------------------------------


                            String nroLector = "02";
                            String tarjeta = objArduino.getDatosLector().substring(objArduino.getMascaraIni(),objArduino.getMascaraFin());
                            Log.v("TEMPUS: ",tarjeta);
                            MarcacionMaster(nroLector,tarjeta);

                            // Marcando
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    turnOnScreen();
                                    manageLayerMarcacion(true);
                                    actualizarFlag(null,null);
                                }
                            });

                            ---------------------------------------- */

                        }
                        break;
                    case "01":
                        break;
                    default:
                        Log.d("Autorizaciones","objSuprema.toString(): " + objSuprema.toString());
                        objArduino.limpiarTramaArduino();
                        break;
                }

                break;
            case "Suprema":
                objSuprema.setTrama(trama);
                objSuprema.estructurarTramaSupremaGeneral();

                switch (objSuprema.getComando()){
                    case "01": // Write
                        break;
                    case "02": // Save
                        break;
                    case "03": // Read

                        Log.d("TEMPUS: ","Llego READ 03 ---- " + objSuprema.getTrama());

                        objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"Cancel",null);
                        Log.d("Autorizaciones","objSuprema.toString(): " + objSuprema.toString());
                        break;
                    case "05": // EnrollByScan
                        Log.v("TEMPUS: ","EnrollByScan >>>");
                        if(isEnrolling) {
                            if (objSuprema.getFlagError().equalsIgnoreCase("SUCCESS")){
                                huellaEnroll1 = trama;
                                huellaEnrollFlag1 = objSuprema.getFlagError();
                            } else {
                                huellaEnroll1 = "";
                                huellaEnrollFlag1 = objSuprema.getFlagError();
                            }
                        }
                        break;
                    case "07": // EnrollByTemplate
                        Log.v("TEMPUS: ","EnrollByTemplate >>>");
                        if (isReplicatingTemplate) {
                            //llega la respuesta de enrolamiento ok y ko
                            try{
                                util.sleep(250);
                                objSuprema.writeToSuprema(ActivityPrincipal.outputStreamS,"Cancel",null);
                                util.sleep(200);

                                isReplicatingTemplate = false;
                            }catch(Exception e){
                                Log.v("TEMPUS: ","Error EnrollByTemplate >>> " + e.getMessage());
                            }

                        }
                        break;
                    case "11": // IdentifyByScan
                        Log.v("TEMPUS: ","IdentifyByScan >>>");

                        if (activityActive.equals("Principal")){



                            if (modoEvento){
                                if (flag == "" || flag!=null) {
                                    String nroLector = "07";
                                    String tarjeta = objSuprema.getFinalValue(objSuprema.getParametro());
                                    if (objSuprema.getFlag().equalsIgnoreCase("69")){
                                        tarjeta = "00000000";
                                    }
                                    Log.v("TEMPUS: ",tarjeta);
                                    //Log.v("TEMPUS: ",String.valueOf(Integer.parseInt(tarjeta)));
                                    MarcacionMaster(nroLector,String.valueOf(Integer.parseInt(tarjeta)));
                                    // Marcando
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            turnOnScreen();
                                            manageLayerMarcacion(true);
                                            actualizarFlag(null,null);
                                        }
                                    });
                                } else {
                                    Log.d("TEMPUS: ","DEBE SELEECIONAR UN EVENTO");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Toast.makeText(ActivityPrincipal.this, "DEBE SELECCIONAR UN EVENTO", Toast.LENGTH_LONG).show();

                                            try {

                                                ui.showAlert(ActivityPrincipal.this,"warning","   DEBE SELECCIONAR UN EVENTO   ");

                                            } catch(Exception e) {
                                                Toast.makeText(ActivityPrincipal.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                                }
                            } else {
                                String nroLector = "07";
                                String tarjeta = objSuprema.getFinalValue(objSuprema.getParametro());
                                if (objSuprema.getFlag().equalsIgnoreCase("69")){
                                    tarjeta = "00000000";
                                }
                                Log.v("TEMPUS: ",tarjeta);
                                //Log.v("TEMPUS: ",String.valueOf(Integer.parseInt(tarjeta)));
                                MarcacionMaster(nroLector,String.valueOf(Integer.parseInt(tarjeta)));
                                // Marcando
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        turnOnScreen();
                                        manageLayerMarcacion(true);
                                    }
                                });
                            }

                            /* -----------------------------------------

                            String nroLector = "07";
                            String tarjeta = objSuprema.getFinalValue(objSuprema.getParametro());
                            if (objSuprema.getFlag().equalsIgnoreCase("69")){
                                tarjeta = "00000000";
                            }
                            Log.v("TEMPUS: ",tarjeta);
                            //Log.v("TEMPUS: ",String.valueOf(Integer.parseInt(tarjeta)));
                            MarcacionMaster(nroLector,String.valueOf(Integer.parseInt(tarjeta)));
                            // Marcando
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                turnOnScreen();
                                Log.v("Autorizaciones","Flag_Actividad: " + flag);
                                manageLayerMarcacion(true);
                                //Date date = new Date();
                                //tiempoPasado = date;
                                actualizarFlag(null,null);
                                }
                            });

                            ------------------------------------------ */

                        }

                        try {
                            objSuprema.writeToSuprema(outputStreamS,"Cancel",null);
                        } catch (Exception e){

                        }
                        break;
                    case "14": // ReadTemplate
                        Log.v("TEMPUS: ","ReadTemplate >>>");
                        if(isEnrolling){
                            if (objSuprema.getFlagError().equalsIgnoreCase("SUCCESS") || objSuprema.getFlagError().equalsIgnoreCase("CONTINUE")){
                                huellaEnroll2 = trama;
                                huellaEnrollFlag2 = objSuprema.getFlagError();
                            } else {
                                huellaEnroll2 = "";
                                huellaEnrollFlag2 = objSuprema.getFlagError();
                            }
                            Log.v("TEMPUS: ",objSuprema.getFlagError());
                        }
                        break;
                    case "16": // DeleteTemplate
                        Log.v("TEMPUS: ","DeleteTemplate >>>");
                        if (isDeleting){
                            if (objSuprema.getFlagError().equalsIgnoreCase("SUCCESS") || objSuprema.getFlagError().equalsIgnoreCase("NOT_FOUND")){
                                huellaDelete1 = trama;
                                huellaDeleteFlag1 = objSuprema.getFlagError();
                            } else {
                                huellaDelete1 = "";
                                huellaDeleteFlag1 = objSuprema.getFlagError();
                            }
                            Log.v("TEMPUS: ",objSuprema.getFlagError() + " - " + huellaDeleteFlag1);
                        }
                        break;
                    case "17": // DeleteAllTemplates
                        Log.v("TEMPUS: ","DeleteAllTemplates >>>");
                        break;
                    case "18": // ListUserID
                        Log.v("TEMPUS: ","ListUserID >>>");
                        break;
                    case "60": // Cancel
                        Log.v("TEMPUS: ","Cancel >>>");
                        break;

                    default:
                        objSuprema.limpiarTramaSuprema();
                        break;
                }
                objSuprema.limpiarTramaSuprema();
                objSuprema.setTrama("");
                break;

            default:
                break;
        }
    }


    /* ================================ MARCACION ================================ */

    public void keyboard(String dato){
        if (dato.equalsIgnoreCase("intro")) {
            if (tarjetaKey.length()==8){
                MarcacionMaster("01",tarjetaKey);
                Date date = new Date();
                tiempoPasado = date;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        turnOnScreen();
                        manageLayerMarcacion(true);
                        if (modoEvento) {
                            actualizarFlag(null,null);
                        }
                    }
                });
            }
        } else {
            if (dato.equalsIgnoreCase("borrar")) {
                if (tarjetaKey.length()>0){
                    tarjetaKey = tarjetaKey.substring(0,(tarjetaKey.length()-1));
                    txvKeyPantalla.setText(tarjetaKey);
                    Date date = new Date();
                    tiempoPasado = date;
                } else {
                    manageKeyboard(false);
                    if (modoEvento) {
                        actualizarFlag(null,null);
                        txvMensajePantalla.setText("SELECCIONE EVENTO");
                    }

                }
            } else {
                if (tarjetaKey.length()<7){
                    Date date = new Date();
                    tiempoPasado = date;
                    tarjetaKey = tarjetaKey + dato;
                    txvKeyPantalla.setText(tarjetaKey);
                } else {
                    Date date = new Date();
                    tiempoPasado = date;
                    tarjetaKey = tarjetaKey + dato;
                    txvKeyPantalla.setText(tarjetaKey);
                    MarcacionMaster("01",tarjetaKey);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            turnOnScreen();
                            manageLayerMarcacion(true);
                            if (modoEvento) {
                                actualizarFlag(null,null);
                            }
                        }
                    });
                }
            }
        }
    }

    public void actualizarFlag(String flag,Button btn){
        this.flag = flag;

        if (btn != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnEvent01.setBackgroundColor(Color.GRAY);
                    btnEvent02.setBackgroundColor(Color.GRAY);
                    btnEvent03.setBackgroundColor(Color.GRAY);
                    btnEvent04.setBackgroundColor(Color.GRAY);
                    btnEvent05.setBackgroundColor(Color.GRAY);
                    btnEvent06.setBackgroundColor(Color.GRAY);
                    btnEvent07.setBackgroundColor(Color.GRAY);
                    btnEvent08.setBackgroundColor(Color.GRAY);
                }
            });
            btn.setBackgroundColor(Color.RED);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnEvent01.setBackgroundColor(Color.GRAY);
                    btnEvent02.setBackgroundColor(Color.GRAY);
                    btnEvent03.setBackgroundColor(Color.GRAY);
                    btnEvent04.setBackgroundColor(Color.GRAY);
                    btnEvent05.setBackgroundColor(Color.GRAY);
                    btnEvent06.setBackgroundColor(Color.GRAY);
                    btnEvent07.setBackgroundColor(Color.GRAY);
                    btnEvent08.setBackgroundColor(Color.GRAY);
                    txvMensajePantalla.setText("SELECCIONE EVENTO");
                }
            });
        }
    }

    public void MarcacionOK(OutputStream out) {
        Log.v("TEMPUS: ", "MARCACION OK");
        try {
            out.write(util.hexStringToByteArray("244F415841000C3531000041"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MarcacionKO(OutputStream out) {
        Log.v("TEMPUS: ", "MARCACION KO");
        try {
            out.write(util.hexStringToByteArray("244F415841000C3530000041"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LimpiarDatosMarcacion() {
        mNombre = "";
        mTarjeta = "";
        mMensajePrincipal = "";
        mMensajeSecundario = "";
    }

    public void MarcacionUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            txvMarcacionNombre.setText(mNombre);
            txvMarcacionTarjeta.setText(mTarjeta);
            txvMarcacionMsjPrincipal.setText(mMensajePrincipal);
            txvMarcacionMsjSecundario.setText(mMensajeSecundario);

            if (false) {
                try {
                    int id = getResources().getIdentifier("p" + mTarjeta, "drawable", getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    //imgViewFoto.setImageDrawable(drawable);
                } catch (Exception e) {
                    int id = getResources().getIdentifier("p", "drawable", getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    //imgViewFoto.setImageDrawable(drawable);
                    e.printStackTrace();
                }
            } else {
                // FAIL
            }

            }
        });
    }

    public String hexToDecimalStringProx(String s){
        String p1 = s.substring(0,2);
        String p2 = s.substring(2,4);
        String p3 = s.substring(4,6);
        String p4 = s.substring(6,8);

        String arreglo[] = {p1, p2, p3, p4};

        int arregloFin[] = new int[4];

        for(int i = 0; i < arreglo.length; i++) {

            int tmp = 0;

            switch (arreglo[i]) {
                case "30":
                    tmp = 0;
                    break;
                case "31":
                    tmp = 1;
                    break;
                case "32":
                    tmp = 2;
                    break;
                case "33":
                    tmp = 3;
                    break;
                case "34":
                    tmp = 4;
                    break;
                case "35":
                    tmp = 5;
                    break;
                case "36":
                    tmp = 6;
                    break;
                case "37":
                    tmp = 7;
                    break;
                case "38":
                    tmp = 8;
                    break;
                case "39":
                    tmp = 9;
                    break;
                case "41":
                    tmp = 10;
                    break;
                case "42":
                    tmp = 11;
                    break;
                case "43":
                    tmp = 12;
                    break;
                case "44":
                    tmp = 13;
                    break;
                case "45":
                    tmp = 14;
                    break;
                case "46":
                    tmp = 15;
                    break;
                default:
                    break;

            }

            arregloFin[i]=tmp;
        }

        Log.d("HOLA", String.valueOf(arreglo));
        Log.d("HOLA", String.valueOf(arregloFin));

        int v = (arregloFin[0] * 4096) + (arregloFin[1] * 256) + (arregloFin[2] * 16) + (arregloFin[3]);
        return String.valueOf(v);
    }

    public void MarcacionMaster(String nroLectora, String tarjeta){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                manageKeyboard(false);
            }
        });


        if(flag == null || flag == ""){


        }else{
            Date date = new Date();
            tiempoPasado = date;

            LimpiarDatosMarcacion();
            String lectora = Lectoras.get(nroLectora);
            Log.d("Autorizaciones","nroLectora: " + nroLectora);

            Autorizaciones autorizaciones = new Autorizaciones();
            if(lectora != null){

                Log.v("TEMPUS: ", lectora);
                if (lectora == "HUELLA SUPREMA") {
                    //tarjeta = String.valueOf(util.convertHexToDecimal(tarjeta.substring(6,8) + tarjeta.substring(4,6) + tarjeta.substring(2,4) + tarjeta.substring(0,2)));
                } else {
                    if (lectora == "PROXIMIDAD CHINA"){
                        Log.d("HOLA","MUNDO");
                        tarjeta = hexToDecimalStringProx(tarjeta);
                    } else {
                        if (lectora != "TECLADO") {
                            tarjeta = util.convertHexToString(tarjeta);
                        }
                    }
                }

                Log.v("TEMPUS: ", "TARJETA " + tarjeta);
                // Resultado de Marcacion I
                // D + LECTORA + TARJETA + FECHAHORA


                try {
                    queriesMarcaciones = new QueriesMarcaciones(this);
                    autorizaciones = queriesMarcaciones.GestionarMarcaciones(tarjeta,idTerminal,Integer.parseInt(nroLectora),flag,fechahora.getFechahora());


                    Log.d("Autorizaciones","Resultado de Busqueda de Autorizaciones: " + autorizaciones.toString());
                    String autorizacion = autorizaciones.getMensaje();
                    if (autorizacion.equalsIgnoreCase("marcacion autorizada")){

                    /*
                    queriesMarcaciones = new QueriesMarcaciones(this);
                    queriesMarcaciones.open();
                    Log.d("Autorizaciones","Cantidad de registros MARCACIONES: " + String.valueOf(queriesMarcaciones.count()));

                    List<Marcaciones> marcacionesList = queriesMarcaciones.select();
                    for(int i = 0; i < marcacionesList.size(); i++){
                        Log.d("Autorizaciones",marcacionesList.get(i).toString());
                    }
                    queriesMarcaciones.close();
                    */

                        MarcacionOK(outputStreamA);
                        mNombre = autorizaciones.getApellidoPaterno() + " " +  autorizaciones.getApellidoMaterno() + " " + autorizaciones.getNombres().substring(0,1);
                        mTarjeta = autorizaciones.getValorTarjeta();
                        mMensajePrincipal = autorizaciones.getMensaje();
                        mMensajeSecundario = autorizaciones.getMensajeDetalle();
                        MarcacionUI();
                    } else {
                        MarcacionKO(outputStreamA);
                        mNombre = autorizaciones.getApellidoPaterno() + " " +  autorizaciones.getApellidoMaterno() + " " + autorizaciones.getNombres().substring(0,1);
                        mTarjeta = autorizaciones.getValorTarjeta();
                        mMensajePrincipal = autorizaciones.getMensaje();
                        mMensajeSecundario = autorizaciones.getMensajeDetalle();
                        MarcacionUI();
                    }


                } catch (Exception e) {
                    Log.d("Marcacion SQLITE",e.getMessage());
                    MarcacionKO(outputStreamA);
                    mNombre = "";
                    mTarjeta = tarjeta;
                    mMensajePrincipal = autorizaciones.getMensaje();
                    mMensajeSecundario = autorizaciones.getMensajeDetalle();
                    MarcacionUI();
                }

            } else {
                Log.v("TEMPUS: ", "LECTORA NO HABILITADA");
                MarcacionKO(outputStreamA);
                mNombre = "";
                mTarjeta = "";
                mMensajePrincipal = autorizaciones.getMensaje();
                mMensajeSecundario = autorizaciones.getMensajeDetalle();
                MarcacionUI();
            }



        }






    }

    /* ================================ REPLICA ================================ */

    public void replicador(){



    }

    /* ================================ RUTINAS ================================ */

    Thread routineArduino = new Thread(new Runnable() {
        @Override
        public void run() {
            String acumulador = "";
            int contador = 0;

            while (true) {
                if (banderaSerial01) {
                    try {
                        // LECTURA NORMAL SERIAL

                        byte[] rawBytes = new byte[1];

                        inputStreamA.read(rawBytes);

                        acumulador = acumulador + util.byteArrayToHexString(rawBytes);

                        if (acumulador.length() == 10) {
                            if (!acumulador.equalsIgnoreCase("244f415841")) {
                                acumulador = acumulador.substring(2, acumulador.length());
                            }
                        }

                        if (acumulador.length() == 108) {

                            Log.v("TEMPUS: ", "Trama Arduino - " + acumulador);

                            evaluarTrama("Arduino",acumulador);

                            objArduino.limpiarTramaArduino();
                            acumulador = "";

                        }

                    } catch (Exception e) {

                        Log.v("TEMPUS: ", "Catch routineArduino: "+e.getMessage());
                        banderaSerial01 = false;

                        if (isCharging){
                            showMsgBoot(true,"Error interno 00A1 - intentando reparar ...");
                        } else {
                            showMsgBoot(true,"Conecte el terminal a cargador, bateria interna agotada (1).");
                            buttonWarning01.setVisibility(View.VISIBLE);
                            buttonWarning01.setVisibility(View.INVISIBLE);

                        }


                    }
                } else {
                    if (!banderaSerial01) {
                        if (isCharging) {
                            Log.v("TEMPUS: ", "INTENTAMOS REPARAR ARDUINO 1");
                            // INTENTAMOS REPARAR SOCKET
                            boolean estado = ConectarSocketArduino();
                            if (estado) {
                                Log.v("TEMPUS: ", "ARDUINO RECONECT");
                                banderaSerial01 = true;

                            } else {
                                contador = contador + 1;
                                banderaSerial01 = false;
                                Log.v("TEMPUS: ", "FALLA SOCKET ARDUINO");

                                if (contador >= intentosRecon01) { // si se cumple la cantidad total de reintentos

                                    if (enableRebootAtmRec01) {
                                        showMsgBoot(true,"Error interno 00A1 - Cantidad de reintentos excedido ... Reiniciando");
                                        reboot();
                                    } else {
                                        showMsgBoot(true,"Error interno 00A1 - Cantidad de reintentos excedido ... ("+String.valueOf(contador)+")");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                buttonWarning03.setVisibility(View.VISIBLE);
                                                buttonWarningHelp.setVisibility(View.VISIBLE);
                                            }
                                        });

                                    }

                                } else {
                                    showMsgBoot(true,"Error interno 00A1 - intentando reparar ... ("+String.valueOf(contador)+")");
                                }
                            }
                        } else {
                            showMsgBoot(true,"Conecte el terminal a cargador, bateria interna agotada.");
                            buttonWarning01.setVisibility(View.VISIBLE);
                            buttonWarning01.setVisibility(View.INVISIBLE);
                            util.sleep(1000);
                        }

                    }
                }
            }
        }
    });



    Thread routineSuprema = new Thread(new Runnable() {
        @Override
        public void run() {
            String acumulador = "";
            int contador = 0;
            int tamano = 26;

            while (true) {
                if (banderaSerial02) {
                    try {

                        // LECTURA NORMAL SERIAL

                        byte[] rawBytes = new byte[1];

                        inputStreamS.read(rawBytes);

                        acumulador = acumulador + util.byteArrayToHexString(rawBytes);

                        if (acumulador.length() == 2){
                            if (!acumulador.equals("40")) {
                                acumulador = "";
                            }
                        }

                        if (acumulador.length() == 20) {
                            tamano = objSuprema.GetSizeSuprema(acumulador.substring(12,20)) + 26;
                        }


                        if (acumulador.length() == tamano && (tamano % 540) == 0) {
                            String flag = acumulador.substring(tamano-520,tamano-518);
                            if (flag.equals("74")) {
                                // Continuamos leyendo
                                tamano = tamano + 540;
                            }
                        }

                        if (acumulador.length() == tamano) {

                            Log.v("TEMPUS: ",tamano + " " +acumulador);
                            evaluarTrama("Suprema",acumulador);
                            acumulador = "";
                            objSuprema.limpiarTramaSuprema();
                        }



                    } catch (Exception e) {
                        Log.v("TEMPUS: ", "Catch routineSuprema: "+e.getMessage());
                        banderaSerial02 = false;

                        showMsgBoot(true,"ERROR INTERNO 00S1 - INTENTANDO REPARAR ...");
                    }
                } else {
                    if (!banderaSerial02) {
                        if (isCharging){
                            Log.v("TEMPUS: ", "INTENTAMOS REPARAR SUPREMA 1");
                            // INTENTAMOS REPARAR SOCKET
                            boolean estado = ConectarSocketSuprema();
                            if (estado) {
                                Log.v("TEMPUS: ", "SUPREMA RECONECT");
                                banderaSerial02 = true;
                            } else {

                                contador = contador + 1;
                                banderaSerial01 = false;
                                Log.v("TEMPUS: ", "FALLA SOCKET SUPREMA");

                                if (contador >= intentosRecon02) { // si se cumple la cantidad total de reintentos

                                    if (enableRebootAtmRec01) {
                                        showMsgBoot(true,"Error interno 00S1 - Cantidad de reintentos excedido ... Reiniciando");
                                        reboot();
                                    } else {
                                        showMsgBoot(true,"Error interno 00S1 - Cantidad de reintentos excedido ... ("+String.valueOf(contador)+")");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                buttonWarning03.setVisibility(View.VISIBLE);
                                                buttonWarningHelp.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }

                                } else {
                                    showMsgBoot(true,"Error interno 00S1 - intentando reparar ... ("+String.valueOf(contador)+")");
                                }

                            }
                        } else {
                            showMsgBoot(true,"Conecte el terminal a cargador, bateria interna agotada.");
                            buttonWarning01.setVisibility(View.VISIBLE);
                            buttonWarning01.setVisibility(View.INVISIBLE);

                            util.sleep(1000);
                        }

                    }
                }
            }
        }
    });



    Thread routineScreen = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Date date = new Date();
                    tiempoPresente = date;
                    long dif = (tiempoPresente.getTime() - tiempoPasado.getTime()) / 1000;
                    //txvIdterminal.setText(idTerminal);
                    //Log.v("TEMPUS: ","tiempoMarcacion: " + tiempoMarcacion);
                    //Log.v("TEMPUS: ","tiempoPresente.getTime(): " + tiempoPresente.getTime());
                    //Log.v("TEMPUS: ","tiempoPasado.getTime(): " + tiempoPasado.getTime());
                    Log.v("TEMPUS: ","dif normal: " + dif);

                    if (dif >= tiempoMarcacion) {
                        //Log.v("TEMPUS: ","dif excedido: " + dif);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                manageKeyboard(false);
                                manageLayerMarcacion(false);

                                Log.d("TEMPUS: ","Flag: " + flag);

                                if (modoEvento){
                                    if((tiempoPresente.getTime() - tiempoPasado.getTime()) / 1000 >= tiempoFlag){
                                        actualizarFlag(null,null);
                                        manageAccessButtons(false);
                                        txvMensajePantalla.setText("SELECCIONE EVENTO");
                                        //Log.v("TEMPUS: ","Flg_Actividad: " + flag);
                                    }
                                }
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }




            }
        }
    });



    Thread routineRevSocket = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);




                    if (enableSerial01) {
                        if (socketArduino != null) {
                            if (socketArduino.isConnected()){
                                if (enableSerial02){
                                    if (socketSuprema != null && socketSuprema.isConnected()){
                                        Log.v("TEMPUS: ","ARDUINO CONECTADO");
                                        util.sleep(2000);
                                        showMsgBoot(false,"");
                                        buttonWarning03.setVisibility(View.INVISIBLE);
                                        buttonWarningHelp.setVisibility(View.INVISIBLE);
                                        enableThreadReconectBT = false;
                                        enableThreadBT = false;
                                    }
                                } else {
                                    Log.v("TEMPUS: ","ARDUINO CONECTADO");
                                    util.sleep(2000);
                                    showMsgBoot(false,"");
                                    buttonWarning03.setVisibility(View.INVISIBLE);
                                    buttonWarningHelp.setVisibility(View.INVISIBLE);
                                    enableThreadReconectBT = false;
                                    enableThreadBT = false;
                                }

                            } else {
                                Log.v("TEMPUS: ","ARDUINO DESCONECTADO");
                                banderaSerial01 = false;
                            }
                        } else {
                            Log.v("TEMPUS: ","DETECTADO OBJETO NULO SOCKETARDUINO");
                        }
                    }

                    if (enableSerial02) {
                        if (socketSuprema != null) {
                            if (socketSuprema.isConnected()){
                                if (enableSerial01){
                                    if (socketArduino != null && socketArduino.isConnected()){
                                        Log.v("TEMPUS: ","SUPREMA CONECTADO");
                                        util.sleep(2000);
                                        showMsgBoot(false,"");
                                        buttonWarning03.setVisibility(View.INVISIBLE);
                                        buttonWarningHelp.setVisibility(View.INVISIBLE);
                                        enableThreadReconectBT = false;
                                        enableThreadBT = false;
                                    }
                                } else {
                                    Log.v("TEMPUS: ","SUPREMA CONECTADO");
                                    util.sleep(2000);
                                    showMsgBoot(false,"");
                                    buttonWarning03.setVisibility(View.INVISIBLE);
                                    buttonWarningHelp.setVisibility(View.INVISIBLE);
                                    enableThreadReconectBT = false;
                                    enableThreadBT = false;
                                }

                            } else {
                                Log.v("TEMPUS: ","SUPREMA DESCONECTADO");
                                banderaSerial02 = false;
                            }
                        } else {
                            Log.v("TEMPUS: ","DETECTADO OBJETO NULO SOCKETSUPREMA");
                        }
                    }

                    /*
                    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                    Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

                    for (int i = 0; i < threadArray.length; i++){
                        Log.v("TEMPUS: ", "Thread: " + String.valueOf(threadArray[i]) + " - " + threadArray[i].getName());
                    }
                    */


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    Thread routineBluetooth = new Thread(new Runnable() {
        @Override
        public void run() {

            while(enableThreadBT) {
                if (enableThreadReconectBT) {
                    fnBluetoothManager();
                } else {
                    Log.v("TEMPUS: ","En espera de alguna accion THREAD BLUETOOTH " + String.valueOf(enableThreadBT) + " - " + String.valueOf(enableThreadReconectBT));
                }
                util.sleep(1000);
            }
        }
    });

    Thread routineDatabase = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                // Consultas en bd para Thread

                util.sleep(5000);
            }
        }
    });

    Thread XD = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                // Consultas en bd para Thread

                if (routineBluetooth.isAlive()){
                    Log.v("TEMPUS: ","routineBluetooth esta vivo!!!!!!!!!!!!!!!!!!!!!!");
                } else {
                    Log.v("TEMPUS: ","routineBluetooth is dead!!!!!!!!!!!!!!!!!!!!!!");
                }

                util.sleep(1000);
            }
        }
    });


    Thread routineReplicate = new Thread(new Runnable() {
        @Override
        public void run() {

            while (true) {
                util.sleep(1000);
                if(isReplicating){
                    try{
                        Log.v("TEMPUS: ","REPLICADO INICIADO");
                        queriesPersonalTipolectoraBiometria.ReplicarBiometria();
                    }catch(Exception e){
                        Log.v("TEMPUS: ","");
                        isReplicating = false;
                    }

                }else{
                    Log.v("TEMPUS: ","REPLICADO ESPERANDO");
                    if(tiempoPresente.getHours() == 1){
                        if(tiempoPresente.getMinutes() == 29){
                            if(tiempoPresente.getSeconds() > 50){
                                Log.v("TEMPUS: ","HABILITANDO REPLICADO");
                                isReplicating = true;
                            }

                        }
                    }
                    Log.v("TEMPUS: ","Tiempo de Presente: " + tiempoPresente.getHours());
                    Log.v("TEMPUS: ","Tiempo de Presente: " + tiempoPresente.getMinutes());
                    Log.v("TEMPUS: ","Tiempo de Presente: " + tiempoPresente.getSeconds());

                }


            }
        }

    });



    Thread routineEventScreen = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                //Log.d("TEMPUS: ","FLAGMARCACION: "+flag);



                if(contadorEventoPantalla>=10){
                    //LIMPIAR FLAGS
                    contadorEventoPantalla = 0;
                    //actualizarFlag("",null); // limpia el flag y el color de los botones
                }

                if(flag!="") {
                    contadorEventoPantalla = contadorEventoPantalla + 1;
                } else {
                    contadorEventoPantalla = 0;
                }

                util.sleep(1000);
            }
        }
    });


}
