package com.tempus.proyectos.tempusx;

// utimo

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.bluetoothSerial.BluetoothSuperAdmin;
import com.tempus.proyectos.bluetoothSerial.MainArduino;
import com.tempus.proyectos.bluetoothSerial.MainHandPunch;
import com.tempus.proyectos.bluetoothSerial.MainSuprema;
import com.tempus.proyectos.crash.TXExceptionHandler;
import com.tempus.proyectos.data.ConexionServidor;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.model.Llamadas;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.model.Servicios;
import com.tempus.proyectos.data.process.ProcessSyncDatetime;
import com.tempus.proyectos.data.process.ProcessSyncST;
import com.tempus.proyectos.data.process.ProcessSyncTS;
import com.tempus.proyectos.data.queries.QueriesAutorizaciones;
import com.tempus.proyectos.data.queries.QueriesBiometrias;
import com.tempus.proyectos.data.queries.QueriesLlamadas;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActivityPrincipal extends Activity {


    /* Variables Globales */

    String TAG = "TX-PTX-CAP";

    int c = 100;

    boolean MODO_EVENTO = true;

    int TIEMPO_CONN_BT = 120;
    Date TIEMPO_PRESENTE_BT01;
    Date TIEMPO_PASADO_BT01;
    Date TIEMPO_PRESENTE_BT02;
    Date TIEMPO_PASADO_BT02;
    Date TIEMPO_PRESENTE_BT03;
    Date TIEMPO_PASADO_BT03;

    String MAC_BT_01 = "";
    String MAC_BT_02 = "";
    String MAC_BT_03 = "";

    boolean BT_01_ENABLED = false;
    boolean BT_02_ENABLED = false;
    boolean BT_03_ENABLED = false;

    boolean REINTENTO_INFINITO = false;

    boolean STATUS_CONNECT_01 = false;
    boolean STATUS_CONNECT_02 = false;
    boolean STATUS_CONNECT_03 = false;

    boolean HARD_FAIL_01 = false;
    boolean HARD_FAIL_02 = false;
    boolean HARD_FAIL_03 = false;


    boolean MODO_PATRON = false;

    String PATRON_SECRET = "";




    int CONTROL_MASTER_ETHERNET_MARCAS = 0; // 0 = CARGA    1 = ETHERNET


    /* --- BLUETOOTH SOCKET ESTÁTICO --- */

    public static BluetoothSuperAdmin btSocket01;
    public static BluetoothSuperAdmin btSocket02;
    public static BluetoothSuperAdmin btSocket03;

    //public static BluetoothManager btSocket01;
    //public static BluetoothManager btSocket02;
    //public static BluetoothManager btSocket03;


    /* --- ACCESO ESTÁTICO --- */

    static int TIPO_TERMINAL = 3; // A=1; A+H=2; A+M=3; A+H+M=4

    public static boolean activo;
    public static Context context;
    public static boolean controlFlagSyncAutorizaciones;

    public static MainArduino objArduino;
    public static MainSuprema objSuprema;
    public static MainHandPunch objHandPunch;

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

    public static String activityActive;
    public static boolean isCharging;
    public static String idTerminal;

    public static boolean ctrlThreadPantallaEnabled;
    public static boolean ctrlThreadSyncMarcasEnabled;
    public static boolean ctrlThreadSyncAutorizacionesEnabled;
    public static boolean ctrlThreadSerial01Enabled;
    public static boolean ctrlThreadSerial02Enabled;
    public static boolean ctrlThreadSerial03Enabled;
    public static boolean ctrlThreadReplicadoEnabled;

    public static String flag;

    public static boolean INTERFACE_WLAN;
    public static boolean INTERFACE_ETH;
    public static boolean INTERFACE_PPP;


    Map<String, String> Lectoras;
    String tarjetaKey;
    boolean visibleKey;
    String mNombre;
    String mTarjeta;
    String mMensajePrincipal;
    String mMensajeSecundario;
    String patronAcceso;
    boolean areaMarcaEnabled;
    boolean areaAccessEnabled;
    int tiempoMarcacion;
    int tiempoPatron;
    int tiempoFlag;
    int contadorEventoPantalla;
    boolean enableBoot;
    String msjBoot;
    boolean isBooting;

    Date tiempoPresente;
    Date tiempoPasado;

    Date tiempoPresentePatron;
    Date tiempoPasadoPatron;


    /* --- OBjetos --- */
    Utilities util;
    UserInterfaceM ui;
    Fechahora fechahora;
    Connectivity connectivity;


    /* --- Variables de DATA --- */

    private DBManager dbManager;
    private QueriesServicios queriesServicios;
    private QueriesBiometrias queriesBiometrias;
    private QueriesLlamadas queriesLlamadas;
    private QueriesAutorizaciones queriesAutorizaciones;
    private QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;
    private QueriesMarcaciones queriesMarcaciones;


    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;


    /* --- Declaración de Componentes de la Interfaz --- */

    TextClock txcHora;

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

    TextView txvSecret01;
    TextView txvSecret02;
    TextView txvSecret03;


    /* Declaración de fragment_bar */

    TextView txvIdterminal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //if(savedInstanceState == null && (btSocket01 == null || btSocket02 == null)){

        /*
        if(activo == true) {
            finish();
        }

        activo = true;
        */

        //boolean b = isForeground("com.tempus.proyectos.tempusx");
        //Log.wtf("INITEMPUS", String.valueOf(b));


        int id = android.os.Process.myPid();
        Log.wtf("PID_ACTIVITIE", String.valueOf(id));

        if (btSocket01 != null || btSocket02 != null) {
            Log.wtf("OBJ_STATUS", "EXISTE");
            //Intent intent = getIntent();
            //finish();
            //try {
            //    Thread.sleep(1000);
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
            //startActivity(intent);
        } else {
            Log.wtf("OBJ_STATUS", "NO EXISTE");
        }


        // Inicialización nivel cero

        activityActive = "Principal";
        context = getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler(new TXExceptionHandler(this));

        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "Restarting app after crash ... ", Toast.LENGTH_SHORT).show();
        }


        // Inicializacion de componentes

        txcHora = (TextClock) findViewById(R.id.txcHora);

        txvFondoInicial = (TextView) findViewById(R.id.txvFondoInicial);
        txvTextoInicial = (TextView) findViewById(R.id.txvTextoInicial);
        pbrCargaInicial = (ProgressBar) findViewById(R.id.pbrCargaInicial);

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

        buttonWarning01 = (Button) findViewById(R.id.buttonWarning01);
        buttonWarning02 = (Button) findViewById(R.id.buttonWarning02);

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

        txvSecret01 = (TextView) findViewById(R.id.txvSecret01);
        txvSecret02 = (TextView) findViewById(R.id.txvSecret02);
        txvSecret03 = (TextView) findViewById(R.id.txvSecret03);

        // Objetos
        tiempoPresente = new Date();
        tiempoPasado = new Date();

        tiempoPresentePatron = new Date();
        tiempoPasadoPatron = new Date();

        ui = new UserInterfaceM();
        util = new Utilities();
        fechahora = new Fechahora();
        connectivity = new Connectivity();

        dbManager = new DBManager(this);
        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(this);

        // Iniciar UI
        ui.initScreen(this);

        // Administramos UI
        manageLayerMarcacion(false);        // Ocultar Layer de Marcación
        manageAccessButtons(false);         // Ocultar Botones de Acceso
        manageEventMode(MODO_EVENTO);     // Ocultar modo Evento
        manageKeyboard(false);              // Ocultar teclado

        //restartBluetooth();

        showMsgBoot(true, "Iniciando sistema, por favor espere ...");    // Bloquear pantalla hasta conectar

        buttonWarning01.setVisibility(View.INVISIBLE);
        buttonWarning02.setVisibility(View.INVISIBLE);

        areaMarcaEnabled = false;
        areaAccessEnabled = false;

        // Creacion de BD
        //crearBD();

        // Cargar Datos
        cargarDatosIniciales();

        // Seriales
        iniciarParametrosSeriales();
        //

        //Restart Bluetooth
        //restartBluetooth();
        //util.sleep(1000);

        btSocket01 = new BluetoothSuperAdmin(MAC_BT_01);
        btSocket02 = new BluetoothSuperAdmin(MAC_BT_02);
        btSocket03 = new BluetoothSuperAdmin(MAC_BT_03);


        if (BT_01_ENABLED){
            threadControlSerial01.start();
        }

        if (BT_02_ENABLED){
            threadControlSerial02.start();
        }

        if (BT_03_ENABLED){
            threadControlSerial03.start();
        }


        conectarSeriales();
        threadControlPrincipal.start();
        threadControlPantalla.start();


        try {
            connectivity.turnGPSOn(this);
        } catch(Exception e) {
            Log.wtf("GPS",e.getMessage());
        }


        //
        // threadControlSerial01.start();
        //
        // threadControlSerial02.start();

        // Iniciar Rutinas en verdadero
        ////////////ProcessSyncTS processSyncTS = new ProcessSyncTS("Sync_Marcaciones_Biometrias");
        ////////////processSyncTS.start(this);
        ////////////ProcessSyncST processSyncST = new ProcessSyncST("Sync_Autorizacion");
        ////////////processSyncST.start(this);
        ////////////ProcessSyncDatetime processSyncDatetime = new ProcessSyncDatetime("Sync_Datetime");
        ////////////processSyncDatetime.start(this);

        threadFechahora.start();


        if (INTERFACE_ETH){
            checkETH0.start();
        }

        /* --- EVENTOS SOBRE COMPONENTES --- */

        // Boton Master (Logo)
        btnMaster.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                PATRON_SECRET = "";

                if (areaAccessEnabled) {
                    patronAcceso = "";
                    areaAccessEnabled = false;
                    manageAccessButtons(false);

                    if (MODO_EVENTO) {
                        txvMensajePantalla.setText("SELECCIONE EVENTO");
                    } else {
                        txvMensajePantalla.setText("PASE SU TARJETA");
                    }

                    MODO_PATRON = false;

                } else {
                    patronAcceso = "";
                    areaAccessEnabled = true;
                    manageAccessButtons(true);

                    if (MODO_EVENTO) {
                        Date date = new Date();
                        tiempoPasado = date;
                        actualizarFlag(null, null);
                    }

                    txvMensajePantalla.setText("");

                    //Date date = new Date();
                    //tiempoPasado = date;

                    txvMensajePantalla.setText("MODO PATRON");

                    MODO_PATRON = true;

                }
            }
        });


        // Boton Acceso Patron 1
        btnAccess1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("1");
                if (MODO_EVENTO) {
                    Date date = new Date();
                    tiempoPasado = date;
                    actualizarFlag(null, null);
                }
            }
        });

        // Boton Acceso Patron 2
        btnAccess2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("2");
                if (MODO_EVENTO) {
                    Date date = new Date();
                    tiempoPasado = date;
                    actualizarFlag(null, null);
                }
            }
        });

        // Boton Acceso Patron 3
        btnAccess3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("3");
                if (MODO_EVENTO) {
                    Date date = new Date();
                    tiempoPasado = date;
                    actualizarFlag(null, null);
                }
            }
        });

        // Boton Acceso Patron 4
        btnAccess4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("4");
                if (MODO_EVENTO) {
                    Date date = new Date();
                    tiempoPasado = date;
                    actualizarFlag(null, null);
                }
            }
        });

        // Boton Evento 01
        btnEvent01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "001";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("001", btnEvent01);
                Date date = new Date();
                tiempoPasado = date;

                try {
                    boolean b = ActivityPrincipal.objHandPunch.handRecognizer_VerificarMano(btSocket03.getOutputStream(), "5e8b79816f806d7059");
                } catch (Exception e) {
                    Log.e("XDXDXD",e.getMessage());
                }


            }
        });

        // Boton Evento 02
        btnEvent02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "009";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("009", btnEvent02);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton Evento 03
        btnEvent03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "003";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("003", btnEvent03);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton Evento 04
        btnEvent04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "004";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("004", btnEvent04);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton Evento 05
        btnEvent05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "005";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("005", btnEvent05);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton Evento 06
        btnEvent06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "006";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("006", btnEvent06);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton Evento 07
        btnEvent07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "007";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("007", btnEvent07);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton Evento 08
        btnEvent08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "008";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("008", btnEvent08);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton de Acceso a teclado
        btnAccessKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == null || flag == "") {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ui.showAlert(ActivityPrincipal.this, "warning", "   DEBE SELECCIONAR UN EVENTO   ");
                            } catch (Exception e) {
                                Toast.makeText(ActivityPrincipal.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    if (visibleKey) {
                        manageKeyboard(false);

                    } else {
                        manageKeyboard(true);
                    }
                }
            }
        });

        // Boton Teclado 1
        btnKey1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("1");
            }
        });

        // Boton Teclado 2
        btnKey2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("2");
            }
        });

        // Boton Teclado 3
        btnKey3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("3");
            }
        });

        // Boton Teclado 4
        btnKey4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("4");
            }
        });

        // Boton Teclado 5
        btnKey5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("5");
            }
        });

        // Boton Teclado 6
        btnKey6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("6");
            }
        });

        // Boton Teclado 7
        btnKey7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("7");
            }
        });

        // Boton Teclado 8
        btnKey8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("8");
            }
        });

        // Boton Teclado 9
        btnKey9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("9");
            }
        });

        // Boton Teclado 0
        btnKey0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("0");
            }
        });

        // Boton Teclado Intro
        btnKeyIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("Intro");
            }
        });

        // Boton Teclado Borrar
        btnKeyBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard("Borrar");
            }
        });

        // Boton Intentar Otra Vez
        buttonWarning01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restartBluetooth();

                Date date = new Date();

                TIEMPO_PRESENTE_BT01 = date;
                TIEMPO_PASADO_BT01 = date;
                TIEMPO_PRESENTE_BT02 = date;
                TIEMPO_PASADO_BT02 = date;
                TIEMPO_PRESENTE_BT03 = date;
                TIEMPO_PASADO_BT03 = date;

                HARD_FAIL_01 = false;
                HARD_FAIL_02 = false;
                HARD_FAIL_03 = false;

                STATUS_CONNECT_01 = false;
                STATUS_CONNECT_02 = false;
                STATUS_CONNECT_03 = false;

                buttonWarning01.setVisibility(View.INVISIBLE);
                buttonWarning02.setVisibility(View.INVISIBLE);

            }
        });

        // Boton Reiniciar Terminal
        buttonWarning02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reboot();
            }
        });

        txvSecret01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccesoSecreto("1");
                Log.d("PATRON_SECRET",PATRON_SECRET);
            }
        });

        txvSecret02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccesoSecreto("2");
                Log.d("PATRON_SECRET",PATRON_SECRET);
            }
        });

        txvSecret03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccesoSecreto("3");
                Log.d("PATRON_SECRET",PATRON_SECRET);
            }
        });

        //} else {
        //    Log.wtf("PID WTF","FINALIZANDOOOOO");

        //System.exit(0);

        //this.recreate();
        /*
        try {
            Log.wtf("XD01","INICIO XD");
            Intent homeIntent = getPackageManager().getLaunchIntentForPackage("com.tempus.proyectos.tempusx");
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(homeIntent);
            Log.wtf("XD02","FIN XD");
        } catch (Exception e){
            Log.wtf("XD03","ERROR XD " + e.getMessage());
        }
        */
        //}
    }

    @Override
    public void onResume(){
        super.onResume();

        activityActive = "Principal";

        try {
            ActivityPrincipal.objSuprema.writeToSuprema(btSocket02.getOutputStream(),"FreeScanOn",null);
        } catch(Exception e) {
            Log.v(TAG,"ERROR ESTABLECIENDO CONEXION CON HUELLERO");
        }


    }

    public void reboot() {

        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot -p" });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i("TEMPUS: ", "No se puede reiniciar!!!!!!!!!!!!!!!!", ex);
        }

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
        ui.initScreen(this);
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

    private void AccesoSecreto(String dato) {
        PATRON_SECRET = PATRON_SECRET + dato;
        Log.d("AccesoSecreto",PATRON_SECRET);

        switch (PATRON_SECRET) {
            case "1111111232132122":
                Log.d("AccesoSecreto","COMANDO: MODO DIOS");
                try {
                    showLoginDialog();
                } catch (Exception e) {
                    Log.e("MODO_DIOS", e.getMessage() );
                }
                PATRON_SECRET = "";
                break;
            case "3333333123123":
                Log.d("AccesoSecreto","COMANDO: REINICIAR");
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: REINICIAR -> " + e.getMessage());
                }
                PATRON_SECRET = "";
                break;
            default:
                break;
        }


    }

    private void showLoginDialog() {

        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.searchprompt, null);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(ActivityPrincipal.this, R.style.AlertDialogCustom));

        builder
                .setView(promptsView);
        final EditText input = (EditText) promptsView.findViewById(R.id.edtPasswordMain);
        builder
                .setCancelable(false)
                .setTitle("CONFIGURACIÓN DE FÁBRICA")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().equalsIgnoreCase("tempus123+.")) {
                            try {
                                Log.d("SYSTEM_SUPERADMIN","INICIANDO");
                                manageAccessButtons(false);
                                Intent intent01 = new Intent(ActivityPrincipal.this, ActivitySuperadmin.class);
                                startActivityForResult(intent01, 1);
                                patronAcceso = "";
                                hideSoftKeyboard(promptsView);
                            } catch(Exception e) {
                                Log.e("hideSoftKeyboard", e.getMessage());
                            }
                        } else {
                            ui.showAlert(ActivityPrincipal.this,"warning","Acceso Denegado!");
                        }


                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            hideSoftKeyboard(promptsView);
                        } catch(Exception e) {
                            Log.e("hideSoftKeyboard", e.getMessage());
                        }

                    }
                }).show();
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    private void hideNavigationBar(boolean hide){
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            if (hide){
                os.writeBytes("pm disable com.android.systemui\n");
            } else {
                os.writeBytes("pm enable com.android.systemui\n");
            }
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            //////////////////////////////////////
        } catch (InterruptedException e) {
            Log.d("SYSTEM_MAIN hideNavBar1",e.getMessage());
        } catch (IOException e) {
            Log.d("SYSTEM_MAIN hideNavBar2",e.getMessage());
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

    public void crearBD() {
        dbManager = new DBManager(context);
        dbManager.all("1,1,1,1,1,1");
    }

    public void cargarDatosIniciales(){

        idTerminal = dbManager.valexecSQL("SELECT IDTERMINAL FROM TERMINAL");


        MODO_EVENTO = false;

        // Interfaces de RED

        INTERFACE_ETH = false;
        INTERFACE_PPP = false;
        INTERFACE_WLAN = true;

        contadorEventoPantalla = 0;

        if (MODO_EVENTO) {

            boolean buttonsVisibility[] = {true,true,true,true,true,true,true,true};
            String buttonsText[] = {"ENT","SAL","3","4","5","6","7","8"};

            flag = null;
            txvMensajePantalla.setText("SELECCIONE UN EVENTO");
            initEventsButtons(buttonsVisibility, buttonsText);
        } else {
            flag = "127";
            txvMensajePantalla.setText("PASE SU TARJETA");
        }

        tarjetaKey = "";
        visibleKey = false;
        tiempoMarcacion = 4;
        tiempoPatron = 5;
        isBooting = true;

        Lectoras = new HashMap<String, String>();
        Lectoras.put("01","TECLADO");
        Lectoras.put("02","DNI");
        Lectoras.put("04","PROXIMIDAD");
        Lectoras.put("07","HUELLA SUPREMA");
        //Lectoras.put("09","DNI");

        TIEMPO_PRESENTE_BT01 = new Date();
        TIEMPO_PASADO_BT01 = new Date();

        TIEMPO_PRESENTE_BT02 = new Date();
        TIEMPO_PASADO_BT02 = new Date();

        TIEMPO_PRESENTE_BT03 = new Date();
        TIEMPO_PASADO_BT03 = new Date();


        // PRUEBA CROVISA 01 9c

        //MAC_BT_01 = "00:15:83:35:7A:E1";
        //MAC_BT_02 = "20:16:08:10:64:80";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // PRUEBA CROVISA 02 2a

        //MAC_BT_01 = "20:16:08:10:65:03";
        //MAC_BT_02 = "00:15:83:35:79:C9";
        //MAC_BT_03 = "00:00:00:00:00:00";




        // CARRION 02
        //MAC_BT_01 = "20:16:08:10:66:91";
        //MAC_BT_02 = "00:12:03:16:02:08";
        //MAC_BT_03 = "00:00:00:00:00:00";



        // CARRION 01
        //MAC_BT_01 = "20:16:05:03:24:64";
        //MAC_BT_02 = "20:16:08:10:42:29";
        //MAC_BT_03 = "00:00:00:00:00:00";


        // DIRESA ID 100
        //MAC_BT_01 = "20:16:08:10:42:38";
        //MAC_BT_02 = "20:16:08:09:04:41";

        // DIRESA ID 101
        //MAC_BT_01 = "20:16:08:10:83:58";
        //MAC_BT_02 = "20:16:08:10:60:73";


        // PRUEBA EDITORA
        //MAC_BT_01 = "00:15:83:35:6C:85";
        //MAC_BT_02 = "98:D3:33:80:91:98";
        //MAC_BT_03 = "00:00:00:00:00:00";



        // Plenum 01
        //MAC_BT_01 = "20:16:08:10:64:87";
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // Plenum 02
        //MAC_BT_01 = "20:16:07:18:34:68";
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // Plenum 03
        //MAC_BT_01 = "00:15:83:35:7A:1D";
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "00:00:00:00:00:00";


        // CORPAC
        //MAC_BT_01 = "98:D3:32:20:5B:7E";
        //MAC_BT_02 = "98:D3:34:90:7D:C0";
        //MAC_BT_03 = "00:00:00:00:00:00";

        //RANSA
        MAC_BT_01 = "20:16:08:10:64:87";
        MAC_BT_02 = "00:00:00:00:00:00";
        MAC_BT_03 = "20:16:08:10:65:94";


        BT_01_ENABLED = true;   //Arduino
        BT_02_ENABLED = false;   //Suprema
        BT_03_ENABLED = true;  //Handpunch


    }

    public void conectarSeriales(){
        // Reseteamos la interfaz


        if (BT_01_ENABLED) {
            Log.i(TAG,"INICIANDO BT 01");
            //btSocket01 = new BluetoothManager(MAC_BT_01,getApplicationContext());
            threadSerial01.start();
        }

        if (BT_02_ENABLED) {
            Log.i(TAG,"INICIANDO BT 02");
            //btSocket02 = new BluetoothManager(MAC_BT_02,getApplicationContext());
            threadSerial02.start();
        }

        if (BT_03_ENABLED) {
            Log.i(TAG,"INICIANDO BT 03");
            //btSocket03 = new BluetoothManager(MAC_BT_03,getApplicationContext());
            threadSerial03.start();
        }

    }

    public void iniciarParametrosSeriales(){
        if (BT_01_ENABLED) {
            objArduino = new MainArduino();
        }

        if (BT_02_ENABLED) {
            objSuprema = new MainSuprema();

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

        if (BT_03_ENABLED) {
            objHandPunch = new MainHandPunch();
        }
    }

    public void restartBluetooth(){
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
    }





    /* ---------------------- CONTROL DE ACCESO POR PATRON ------------------------*/

    public void AccessManager(String n){
        patronAcceso = patronAcceso + n;

        switch(patronAcceso) {
            case "123432":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: INGRESAR LOGIN");
                txvMensajePantalla.setText("PASE SU TARJETA");
                manageAccessButtons(false);
                Intent intent01 = new Intent(ActivityPrincipal.this, ActivityLogin.class);
                intent01.putExtra("llave", "valor");
                startActivityForResult(intent01, 1);
                patronAcceso = "";
                try {
                    ActivityPrincipal.objSuprema.writeToSuprema(ActivityPrincipal.btSocket02.getOutputStream(),"FreeScanOff",null);
                } catch(Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN: ","COMANDO: INGRESAR LOGIN -> " + e.getMessage());
                }
                break;

            case "444432":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: SUPREMA (CANCEL)");
                try {
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
                } catch(Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN: ","COMANDO: SUPREMA (CANCEL) -> " + e.getMessage());
                }

                break;

            case "222232":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: SUPREMA (DELETE_ALL_TEMPLATES)");
                try{
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"DeleteAllTemplates",null);
                    util.sleep(250);
                }catch(Exception e){
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: SUPREMA (DELETE_ALL_TEMPLATES) -> " + e.getMessage());
                }
                objSuprema.limpiarTramaSuprema();
                break;

            case "333332":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: SUPREMA (NUMBER_TEMPLATES)");
                try{
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"NumberTemplate",null);
                    util.sleep(800);
                    objSuprema.limpiarTramaSuprema();
                }catch(Exception e){
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: SUPREMA (NUMBER_TEMPLATES) -> " + e.getMessage());
                }

                break;

            case "111132":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: INTENTAR HABILITAR REPLICADO");
                isReplicating = true;
                break;

            case "1324111":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: REINICIALIZAR BD");
                try {
                    crearBD();
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: REINICIALIZAR BD -> " + e.getMessage());
                }
                break;

            case "33334334":
                // OTG (QUITAR CARGA)
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: OTG - QUITAR CARGA");
                try {
                    AdministrarOTG(btSocket01.getOutputStream(),true);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: OTG - QUITAR CARGA -> " + e.getMessage());
                }

                break;
            case "33334331":
                // CARGAR
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: OTG - CARGAR");
                try {
                    AdministrarOTG(btSocket01.getOutputStream(),false);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: OTG - CARGAR -> " + e.getMessage());
                }
                break;

            case "113322443241321": // OCULTAR SYSTEMUI
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: OCULTAR SYSTEMUI");
                try {
                    hideNavigationBar(true);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: OCULTAR SYSTEMUI -> " + e.getMessage());
                }
                break;

            case "113322443241322": // MOSTRAR SYSTEMUI
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: MOSTRAR SYSTEMUI");
                try {
                    hideNavigationBar(false);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: MOSTRAR SYSTEMUI -> " + e.getMessage());
                }
                break;

            case "113322443241323": // REINICIAR
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: REINICIAR");
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: REINICIAR -> " + e.getMessage());
                }
                break;

            case "113322443241324": // APAGAR
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: APAGAR");
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: APAGAR -> " + e.getMessage());
                }
                break;

            case "113322443241311": // ABRIR CONFIGURACION DE ANDROID
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: CONFIGURACION DE ANDROID");
                try {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    Intent startMain = getPackageManager().getLaunchIntentForPackage("com.tempus.ecernar.overlaywifi");
                    startActivity(startMain);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: CONFIGURACION DE ANDROID -> " + e.getMessage());
                }

                break;

            default:
                break;
        }
    }

    /* ---------------------- FUNCION MARCACION MASTER --------------------------- */

    public String getNroLectora(String name){
        switch (name) {
            case "DNI":
                return "02";
            case "PROXIMIDAD CHINA":
                return "04";
            case "HUELLA SUPREMA":
                return "07";
            case "DNI ELECTRONICO":
                return "04";
            case "TECLADO":
                return "01";
            default:
                return "00";
        }
    }

    public void MarcacionMaster(String lectoraName, String tarjeta){

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
            String lectora = lectoraName;
            Log.d(TAG,"Lectora: " + lectora);

            Autorizaciones autorizaciones = new Autorizaciones();
            if(lectora != null){

                Log.v(TAG, lectora);
                if (lectora == "HUELLA SUPREMA") {
                    //tarjeta = String.valueOf(util.convertHexToDecimal(tarjeta.substring(6,8) + tarjeta.substring(4,6) + tarjeta.substring(2,4) + tarjeta.substring(0,2)));
                } else {
                    if (lectora == "PROXIMIDAD CHINA"){
                        tarjeta = hexToDecimalStringProx(tarjeta);
                    } else {
                        if (lectora != "TECLADO") {
                            tarjeta = util.convertHexToString(tarjeta);
                        }
                    }
                }

                Log.v(TAG, "TARJETA " + tarjeta);

                try {
                    queriesMarcaciones = new QueriesMarcaciones(this);
                    Log.v(TAG, "MarcacionMaster Exec >");
                    Log.v(TAG, " > Tarjeta: " + tarjeta);
                    Log.v(TAG, " > ID Terminal: " +idTerminal);
                    Log.v(TAG, " > ID Lectora: " +getNroLectora(lectora));
                    Log.v(TAG, " > Flag: " +flag);
                    Log.v(TAG, " > FechaHora: " +fechahora.getFechahora());
                    autorizaciones = queriesMarcaciones.GestionarMarcaciones(tarjeta,idTerminal,Integer.parseInt(getNroLectora(lectora)),flag,fechahora.getFechahora());


                    Log.d(TAG,"Resultado de Busqueda de Autorizaciones: " + autorizaciones.toString());
                    String autorizacion = autorizaciones.getMensaje();
                    if (autorizacion.equalsIgnoreCase("marcacion autorizada")){
                        MarcacionOK(btSocket01.getOutputStream());
                        mNombre = autorizaciones.getApellidoPaterno() + " " +  autorizaciones.getApellidoMaterno() + " " + autorizaciones.getNombres().substring(0,1);
                        mTarjeta = autorizaciones.getValorTarjeta();
                        mMensajePrincipal = autorizaciones.getMensaje();
                        mMensajeSecundario = autorizaciones.getMensajeDetalle();
                        MarcacionUI();
                    } else {
                        MarcacionKO(btSocket01.getOutputStream());
                        mNombre = autorizaciones.getApellidoPaterno() + " " +  autorizaciones.getApellidoMaterno() + " " + autorizaciones.getNombres().substring(0,1);
                        mTarjeta = autorizaciones.getValorTarjeta();
                        mMensajePrincipal = autorizaciones.getMensaje();
                        mMensajeSecundario = autorizaciones.getMensajeDetalle();
                        MarcacionUI();
                    }

                } catch (Exception e) {
                    Log.d(TAG,e.getMessage());
                    MarcacionKO(btSocket01.getOutputStream());
                    mNombre = "";
                    mTarjeta = tarjeta;
                    mMensajePrincipal = autorizaciones.getMensaje();
                    mMensajeSecundario = autorizaciones.getMensajeDetalle();
                    MarcacionUI();
                }

            } else {
                Log.v(TAG, "LECTORA NO HABILITADA");
                MarcacionKO(btSocket01.getOutputStream());
                mNombre = "";
                mTarjeta = "";
                mMensajePrincipal = autorizaciones.getMensaje();
                mMensajeSecundario = autorizaciones.getMensajeDetalle();
                MarcacionUI();
            }
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

        Log.d(TAG, String.valueOf(arreglo));
        Log.d(TAG, String.valueOf(arregloFin));

        int v = (arregloFin[0] * 4096) + (arregloFin[1] * 256) + (arregloFin[2] * 16) + (arregloFin[3]);
        return String.valueOf(v);
    }

    public void MarcacionOK(OutputStream out) {
        Log.v(TAG, "MARCACION OK");
        try {
            out.write(util.hexStringToByteArray("244F415841000C3531000041"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MarcacionKO(OutputStream out) {
        Log.v(TAG, "MARCACION KO");
        try {
            out.write(util.hexStringToByteArray("244F415841000C3530000041"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AdministrarOTG(OutputStream out, boolean enable) {
        // VOLTAJE (QUITAR) = 0 --- (REGRESAR) = 1
        // OTG = 1 --- CARGA = 0

        // 6 -> 0 10ms 5 -> 1 -----> ACTIVAR MODO OTG
        // 00 //200ms
        // 10
        // 5 -> 0 10ms 6 -> 1 -----> ACTIVAR CARGA
        // 00 //200ms
        // 01

        Log.v("OTG_MANAGER", "AdministrarOTG");

        try {
            if (enable){
                Log.v("OTG_MANAGER", "Activando OTG");
                out.write(util.hexStringToByteArray("244F4158410013423131313131303031")); // 00
                util.sleep(500);
                out.write(util.hexStringToByteArray("244F4158410013423131313131313031")); // 10
            } else {
                Log.v("OTG_MANAGER", "Desactivando OTG ... Pasando a carga");
                out.write(util.hexStringToByteArray("244F4158410013423131313131303031")); // 00
                util.sleep(500);
                out.write(util.hexStringToByteArray("244F4158410013423131313131303131")); // 01
            }
        } catch (IOException e) {
            Log.wtf("OTG_MANAGER",e.getMessage());
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
                    if (!areaAccessEnabled) {
                        txvMensajePantalla.setText("SELECCIONE EVENTO");
                    }
                }
            });
        }
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



    /* -------------------- ADMINISTRACIÓN DE USER INTERFACE --------------------- */

    public void initEventsButtons(boolean visible[], String nombre[]){
        if (visible[0]){
            btnEvent01.setVisibility(View.VISIBLE);
            btnEvent01.setText(nombre[0]);
        } else {
            btnEvent01.setVisibility(View.INVISIBLE);
        }

        if (visible[1]){
            btnEvent02.setVisibility(View.VISIBLE);
            btnEvent02.setText(nombre[1]);
        } else {
            btnEvent02.setVisibility(View.INVISIBLE);
        }

        if (visible[2]){
            btnEvent03.setVisibility(View.VISIBLE);
            btnEvent03.setText(nombre[2]);
        } else {
            btnEvent03.setVisibility(View.INVISIBLE);
        }

        if (visible[3]){
            btnEvent04.setVisibility(View.VISIBLE);
            btnEvent04.setText(nombre[3]);
        } else {
            btnEvent04.setVisibility(View.INVISIBLE);
        }

        if (visible[4]){
            btnEvent05.setVisibility(View.VISIBLE);
            btnEvent05.setText(nombre[4]);
        } else {
            btnEvent05.setVisibility(View.INVISIBLE);
        }

        if (visible[5]){
            btnEvent06.setVisibility(View.VISIBLE);
            btnEvent06.setText(nombre[5]);
        } else {
            btnEvent06.setVisibility(View.INVISIBLE);
        }

        if (visible[6]){
            btnEvent07.setVisibility(View.VISIBLE);
            btnEvent07.setText(nombre[6]);
        } else {
            btnEvent07.setVisibility(View.INVISIBLE);
        }

        if (visible[7]){
            btnEvent08.setVisibility(View.VISIBLE);
            btnEvent08.setText(nombre[7]);
        } else {
            btnEvent08.setVisibility(View.INVISIBLE);
        }

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
            btnEvent03.setVisibility(View.VISIBLE);
            btnEvent04.setVisibility(View.VISIBLE);
            btnEvent05.setVisibility(View.VISIBLE);
            btnEvent06.setVisibility(View.VISIBLE);
            btnEvent07.setVisibility(View.VISIBLE);
            btnEvent08.setVisibility(View.VISIBLE);

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
            if ( MODO_EVENTO ) {
                manageEventMode(false);
            }
        } else {
            btnAccess1.setVisibility(View.INVISIBLE);
            btnAccess2.setVisibility(View.INVISIBLE);
            btnAccess3.setVisibility(View.INVISIBLE);
            btnAccess4.setVisibility(View.INVISIBLE);
            if ( MODO_EVENTO ) {
                manageEventMode(true);
            }
        }
    }

    public void keyboard(String dato){
        if (dato.equalsIgnoreCase("intro")) {
            if (tarjetaKey.length()==8){
                MarcacionMaster("TECLADO",tarjetaKey);
                Date date = new Date();
                tiempoPasado = date;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        turnOnScreen();
                        manageLayerMarcacion(true);
                        if ( MODO_EVENTO ) {
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
                    if ( MODO_EVENTO ) {
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
                    MarcacionMaster("TECLADO",tarjetaKey);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            turnOnScreen();
                            manageLayerMarcacion(true);
                            if ( MODO_EVENTO ) {
                                actualizarFlag(null,null);
                            }
                        }
                    });
                }
            }
        }
    }


    /* -------------------------------EVALUAR TRAMA ------------------------------- */

    public void evaluarTrama(String origen, String trama) {

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

                            if( MODO_EVENTO ){
                                // no ace==ptamos nada de marcaciones
                                if (flag!="" || flag!=null){
                                    String flagRead = objArduino.getFlagRead();
                                    String tarjeta = objArduino.getDatosLector().substring(objArduino.getMascaraIni(),objArduino.getMascaraFin());
                                    Log.v("TEMPUS: ",tarjeta);
                                    MarcacionMaster(flagRead,tarjeta);

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
                                String flagRead = objArduino.getFlagRead();
                                String tarjeta = objArduino.getDatosLector().substring(objArduino.getMascaraIni(),objArduino.getMascaraFin());
                                Log.v("TEMPUS: ",tarjeta);
                                MarcacionMaster(flagRead,tarjeta);

                                // Marcando
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        turnOnScreen();
                                        manageLayerMarcacion(true);
                                    }
                                });
                            }
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

                        objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
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
                        Log.v("TEMPUS: ","EnrollByTemplate >>>" + objSuprema.getTrama());
                        if (isReplicatingTemplate) {
                            //llega la respuesta de enrolamiento ok y ko
                            try{
                                util.sleep(250);
                                objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
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



                            if ( MODO_EVENTO ){
                                if (flag == "" || flag!=null) {
                                    String flagRead = "HUELLA SUPREMA";
                                    String tarjeta = objSuprema.getFinalValue(objSuprema.getParametro());
                                    if (objSuprema.getFlag().equalsIgnoreCase("69")){
                                        tarjeta = "00000000";
                                    }
                                    Log.v("TEMPUS: ",tarjeta);
                                    //Log.v("TEMPUS: ",String.valueOf(Integer.parseInt(tarjeta)));
                                    MarcacionMaster(flagRead,String.valueOf(Integer.parseInt(tarjeta)));
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
                                    MarcacionKO(btSocket01.getOutputStream());

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
                                String flagRead = "HUELLA SUPREMA";
                                String tarjeta = objSuprema.getFinalValue(objSuprema.getParametro());
                                if (objSuprema.getFlag().equalsIgnoreCase("69")){
                                    tarjeta = "00000000";
                                }
                                Log.v("TEMPUS: ",tarjeta);
                                //Log.v("TEMPUS: ",String.valueOf(Integer.parseInt(tarjeta)));
                                MarcacionMaster(flagRead,String.valueOf(Integer.parseInt(tarjeta)));
                                // Marcando
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        turnOnScreen();
                                        manageLayerMarcacion(true);
                                    }
                                });
                            }
                        }

                        try {
                            objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
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




    /* ------------------------- RUTINAS THREAD SERIALES ------------------------- */

    Thread threadSerial01 = new Thread(new Runnable() {
        @Override
        public void run() {

            String acumulador = "";
            int contador = 0;

            while (true) {

                if (ctrlThreadSerial01Enabled) {
                    try {
                        byte[] rawBytes = new byte[1];
                        btSocket01.getInputStream().read(rawBytes);

                        acumulador = acumulador + util.byteArrayToHexString(rawBytes);

                        if (acumulador.length() == 10) {
                            if (!acumulador.equalsIgnoreCase("244f415841")) {
                                acumulador = acumulador.substring(2, acumulador.length());
                            }
                        }

                        if (acumulador.length() == 108) {
                            Log.v(TAG, "Trama Arduino - " + acumulador);
                            evaluarTrama("Arduino",acumulador);
                            objArduino.limpiarTramaArduino();
                            acumulador = "";
                        }

                    } catch (Exception e) {
                        try {
                            Log.d(TAG + "EX01","routine catch 1 returned exception "+e.getMessage() + " ~ " + String.valueOf(btSocket01.getSocket().isConnected()));
                            if (!btSocket01.getSocket().isConnected() || e.getMessage().contains("bt socket closed")){
                                Log.wtf(TAG + "EX11","Entro en reparacion");

                                Date date = new Date();
                                TIEMPO_PASADO_BT01 = date;
                                STATUS_CONNECT_01 = false;
                                btSocket01.closeBT();
                            }

                        } catch(Exception ex) {
                            Log.d(TAG,"routine catch 1.5 returned exception "+ex.getMessage());
                        }
                        ctrlThreadSerial01Enabled = false;
                    }
                } else {
                    Log.d(TAG,"ctrlThreadSerial01Enabled "+ctrlThreadSerial01Enabled);
                    util.sleep(1000);
                }

            }
        }
    });

    Thread threadSerial02 = new Thread(new Runnable() {
        @Override
        public void run() {

            String acumulador = "";
            int contador = 0;
            int tamano = 26;

            while (true) {
                if (ctrlThreadSerial02Enabled) {
                    try {
                        byte[] rawBytes = new byte[1];
                        btSocket02.getInputStream().read(rawBytes);

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
                            Log.v(TAG,tamano + " " +acumulador);
                            evaluarTrama("Suprema",acumulador);
                            objSuprema.limpiarTramaSuprema();
                            acumulador = "";
                        }

                    } catch (Exception e) {
                        try {
                            Log.d(TAG + "EX02","routine catch 1 returned exception "+e.getMessage() + " ~ " + String.valueOf(btSocket02.getSocket().isConnected()));
                            if (!btSocket02.getSocket().isConnected() || e.getMessage().contains("bt socket closed")){
                                Log.wtf(TAG + "EX12","Entro en reparacion");

                                Date date = new Date();
                                TIEMPO_PASADO_BT02 = date;
                                STATUS_CONNECT_02 = false;
                                btSocket02.closeBT();
                            }

                        } catch(Exception ex) {
                            Log.d(TAG,"routine catch 1.5 returned exception "+ex.getMessage());
                        }
                        ctrlThreadSerial02Enabled = false;
                    }
                } else {
                    Log.d(TAG,"ctrlThreadSerial02Enabled "+ctrlThreadSerial02Enabled);
                    util.sleep(1000);
                }

            }
        }
    });

    Thread threadSerial03 = new Thread(new Runnable() {
        @Override
        public void run() {
            /*
            while (true) {
                try {
                    byte[] rawBytes = new byte[1];
                    btSocket03.getInputStream().read(rawBytes);
                } catch (Exception e) {
                    try {
                        Log.d(TAG,"routine catch 1 returned exception "+e.getMessage() + " ~ " + String.valueOf(btSocket03.getSocket().isConnected()));
                        if (!btSocket03.getSocket().isConnected() || e.getMessage().contains("bt socket closed")){
                            //btSocket03.closeBT();
                            Date date = new Date();
                            TIEMPO_PASADO_BT03 = date;
                            STATUS_CONNECT_03 = false;
                        }

                    } catch(Exception ex) {
                        Log.d(TAG,"routine catch 1.5 returned exception "+ex.getMessage());
                    }
                }
            }
            */
        }
    });


    /* --- CONTROL PRINCIPAL --- */
    public void controlGeneral(boolean c1,boolean c2, boolean c3) {

        Log.v(TAG,"CONTROL GENERAL");

        if (c1 && c2 && c3){ // Si todos los seriales estan conectados podemos hacer algo
            if (isBooting){
                isBooting = false;
                util.sleep(1000);
                try {
                    ActivityPrincipal.objSuprema.writeToSuprema(btSocket02.getOutputStream(),"FreeScanOn",null);
                } catch(Exception e) {
                    Log.v(TAG,"ERROR ESTABLECIENDO CONEXION CON HUELLERO");
                }

            }
            Log.v(TAG,"SERIALES OK");
            ctrlThreadPantallaEnabled = true;
            ctrlThreadSyncMarcasEnabled = true;
            ctrlThreadSyncAutorizacionesEnabled = true;
            ctrlThreadSerial01Enabled = true;
            ctrlThreadSerial02Enabled = true;
            ctrlThreadSerial03Enabled = true;
            ctrlThreadReplicadoEnabled = true;
            showMsgBoot(false,"");
            buttonWarning01.setVisibility(View.INVISIBLE);
            buttonWarning02.setVisibility(View.INVISIBLE);

        } else {
            Log.v(TAG,"SERIALES KO");

            String detail = "[  ";

            if (BT_01_ENABLED){
                detail = detail + "ST01:"+c1 + "  ";
            }

            if (BT_02_ENABLED){
                detail = detail + "ST02:"+c2 + "  ";
            }

            if (BT_03_ENABLED){
                detail = detail + "ST03:"+c3 + "  ";
            }

            if (isBooting){
                showMsgBoot(true,"Iniciando Sistema \n"+detail+"]");
            } else {
                if (!isCharging) {

                    // Emitimos alerta de energia

                    //detail = detail + "\nNO CONECTADO A ENERGÍA";
                    showMsgBoot(true,"Apagando Equipo en 15 segundos ... espere ...");

                    c = 15;

                    Thread threadShutdown = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            util.sleep(15000);

                            try {
                                Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                                proc.waitFor();
                            } catch (Exception e) {
                                Log.wtf("WTF",e.getMessage());
                            }

                            /*
                            while(true){
                                if (c <= 0){
                                    c = 0;
                                    try {
                                        Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                                        proc.waitFor();
                                    } catch (Exception e) {
                                        Log.wtf("WTF",e.getMessage());
                                    }
                                    util.sleep(2000);
                                } else {
                                    c = c-1;
                                    runOnUiThread(new Runnable() { // Cruce con hilo padre WTF
                                        @Override
                                        public void run() {
                                            showMsgBoot(true,"Apagando Equipo en "+String.valueOf(c)+" segundos ...");
                                        }
                                    });
                                    util.sleep(1000);
                                }
                            }
                            */
                        }
                    });

                    threadShutdown.start();

                } else {
                    c = 100;
                    detail = detail + "\n";
                    showMsgBoot(true,"Reconectando Interfaces \n"+detail+"]");
                }

            }

        }
    }

    public void mostrarReparador(boolean r1,boolean r2,boolean r3) {
        if (r1 && r2 && r3){
            // MOSTRAR INTERFAZ DE RECONECCION
            buttonWarning01.setVisibility(View.VISIBLE);
            buttonWarning02.setVisibility(View.VISIBLE);
        } else {
            // DESAPARECER INTERFAZ DE RECONECCION
            buttonWarning01.setVisibility(View.INVISIBLE);
            buttonWarning02.setVisibility(View.INVISIBLE);
        }
    }

    /* --- RUTINAS THREAD CONTROL --- */

    Thread threadControlSerial01 = new Thread(new Runnable() {
        @Override
        public void run() {

            while (true) {

                Date date = new Date();
                TIEMPO_PRESENTE_BT01 = date;

                if (STATUS_CONNECT_01) {
                    Log.v(TAG,"STATUS_CONNECT_01: OK");
                } else {
                    long dif = (TIEMPO_PRESENTE_BT01.getTime()-TIEMPO_PASADO_BT01.getTime())/1000;
                    if (dif <= TIEMPO_CONN_BT) {
                        Log.v(TAG,
                                "Conectando SERIAL01 - " +
                                        "TIEMPO_PRESENTE_BT01: " + TIEMPO_PRESENTE_BT01.toString() + " | " +
                                        "TIEMPO_PASADO_BT01:" + TIEMPO_PASADO_BT01.toString());

                        STATUS_CONNECT_01 = btSocket01.Connect();
                        util.sleep(1000);
                    } else {
                        Log.wtf(TAG,"No pudo iniciar Serial01");
                        HARD_FAIL_01 = true;
                    }
                }

                if (REINTENTO_INFINITO){
                    Date dateP = new Date();
                    TIEMPO_PASADO_BT01 = dateP;
                }
                util.sleep(1000);
            }

        }
    });

    Thread threadControlSerial02 = new Thread(new Runnable() {
        @Override
        public void run() {

            while (true) {

                Date date = new Date();
                TIEMPO_PRESENTE_BT02 = date;

                if (STATUS_CONNECT_02) {
                    Log.v(TAG,"STATUS_CONNECT_02: OK");
                } else {
                    long dif = (TIEMPO_PRESENTE_BT02.getTime()-TIEMPO_PASADO_BT02.getTime())/1000;
                    if (dif <= TIEMPO_CONN_BT) {
                        Log.v(TAG,
                                "Conectando SERIAL02 - " +
                                        "TIEMPO_PRESENTE_BT02: " + TIEMPO_PRESENTE_BT02.toString() + " | " +
                                        "TIEMPO_PASADO_BT02:" + TIEMPO_PASADO_BT02.toString());

                        STATUS_CONNECT_02 = btSocket02.Connect();
                        util.sleep(1000);
                    } else {
                        Log.wtf(TAG,"No pudo iniciar Serial02");
                        HARD_FAIL_02 = true;
                    }
                }

                if (REINTENTO_INFINITO){
                    Date dateP = new Date();
                    TIEMPO_PASADO_BT02 = dateP;
                }

                util.sleep(1000);
            }
        }
    });

    Thread threadControlSerial03 = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                Date date = new Date();
                TIEMPO_PRESENTE_BT03 = date;

                if (STATUS_CONNECT_03) {
                    Log.v(TAG,"STATUS_CONNECT_03: OK");
                } else {
                    long dif = (TIEMPO_PRESENTE_BT03.getTime()-TIEMPO_PASADO_BT03.getTime())/1000;
                    if (dif <= TIEMPO_CONN_BT) {
                        Log.v(TAG,
                                "Conectando SERIAL03 - " +
                                        "TIEMPO_PRESENTE_BT03: " + TIEMPO_PRESENTE_BT03.toString() + " | " +
                                        "TIEMPO_PASADO_BT03:" + TIEMPO_PASADO_BT03.toString());
                        STATUS_CONNECT_03 = btSocket03.Connect();
                        util.sleep(1000);
                    } else {
                        Log.wtf(TAG,"No pudo iniciar Serial03");
                        HARD_FAIL_03 = true;
                    }
                }

                if (REINTENTO_INFINITO){
                    Date dateP = new Date();
                    TIEMPO_PASADO_BT03 = dateP;
                }

                util.sleep(1000);
            }
        }
    });

    Thread threadControlPrincipal = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                Log.d(TAG,"Debug Principal: ");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch ( TIPO_TERMINAL ) {
                            case 1:
                                controlGeneral(STATUS_CONNECT_01,true,true);
                                if (HARD_FAIL_01) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                //mostrarReparador(HARD_FAIL_01,true,true);
                                break;
                            case 2:
                                controlGeneral(STATUS_CONNECT_01,STATUS_CONNECT_02,true);
                                if (HARD_FAIL_01 || HARD_FAIL_02) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                //mostrarReparador(HARD_FAIL_01,HARD_FAIL_02,true);
                                break;
                            case 3:
                                controlGeneral(STATUS_CONNECT_01,true,STATUS_CONNECT_03);
                                if (HARD_FAIL_01 || HARD_FAIL_03) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                //mostrarReparador(HARD_FAIL_01,true,HARD_FAIL_03);
                                break;
                            case 4:
                                controlGeneral(STATUS_CONNECT_01,STATUS_CONNECT_02,STATUS_CONNECT_03);
                                if (HARD_FAIL_01 || HARD_FAIL_02 || HARD_FAIL_03) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                //mostrarReparador(HARD_FAIL_01,HARD_FAIL_02,HARD_FAIL_03);
                                break;
                            default:
                                break;
                        }

                        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                        Log.d("THREAD_COUNT", String.valueOf(threadSet));
                    }
                });

                util.sleep(1000);

            }
        }
    });

    Thread threadControlPantalla = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (ctrlThreadPantallaEnabled){

                    // Control de Marcación

                    Log.d("threadControlPantalla: ","Flag: " + flag);

                    try {
                        Date date = new Date();
                        tiempoPresente = date;
                        long dif = (tiempoPresente.getTime() - tiempoPasado.getTime()) / 1000;

                        Log.v("threadControlPantalla: ","dif normal: " + dif);

                        if (dif >= tiempoMarcacion) {

                            Log.v("threadControlPantalla: ","1");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    manageKeyboard(false);
                                    manageLayerMarcacion(false);

                                    if ( MODO_EVENTO ){

                                        Log.v("threadControlPantalla: ","2");

                                        if((tiempoPresente.getTime() - tiempoPasado.getTime()) / 1000 >= tiempoFlag){
                                            Log.v("threadControlPantalla: ","3");
                                            actualizarFlag(null,null);
                                            manageAccessButtons(false);
                                            txvMensajePantalla.setText("SELECCIONE EVENTO");
                                        }
                                    }
                                }
                            });
                        }

                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                    }

                    // Control de Patron

                    /*
                    try {
                        Date date = new Date();
                        tiempoPresentePatron = date;
                        long dif = (tiempoPresentePatron.getTime() - tiempoPasadoPatron.getTime()) / 1000;

                        if (dif >= tiempoPatron) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    patronAcceso = "";
                                    areaAccessEnabled = false;
                                    manageAccessButtons(false);

                                    if (MODO_EVENTO){
                                        txvMensajePantalla.setText("SELECCIONE EVENTO");
                                    } else {
                                        txvMensajePantalla.setText("PASE SU TARJETA");
                                    }

                                    MODO_PATRON = false;
                                }
                            });
                        }


                    } catch(Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                    */
                }
                util.sleep(1000);
            }
        }
    });


    Thread checkETH0 = new Thread(new Runnable() {
        @Override
        public void run() {

            Log.d("ETH_HILO","INICIO");

            util.sleep(15000); // 1 min

            while (true){

                //runOnUiThread(new Runnable() {
                //    @Override
                //    public void run() {
                //        Toast.makeText(getApplicationContext(), "CICLO ... ",Toast.LENGTH_SHORT).show();
                //    }
                //});

                Log.d("ETH_HILO","CICLO ... ");


                DBManager db = new DBManager(ActivityPrincipal.context);
                String resultado = db.valexecSQL("SELECT PARAMETRO FROM TERMINAL_CONFIGURACION");
                String valor = resultado.split(",")[1];

                if (valor.equalsIgnoreCase("1")){
                    Log.d("ETH_HILO","Eth0 Enabled ... ");
                    String tmp = "";

                    try {
                        tmp = db.valexecSQL("SELECT " +
                                "(SELECT COUNT(*) FROM MARCACIONES WHERE SINCRONIZADO = 0) + " +
                                "(SELECT COUNT(*) FROM PERSONAL_TIPOLECTORA_BIOMETRIA WHERE SINCRONIZADO = 2) " +
                                "AS DATAXENVIAR;");
                    } catch(Exception e) {
                        Log.wtf("ETH_HILO","ERROR EN VAL EXEC ... "+e.getMessage());
                    }

                    int cant = 0;

                    try {
                        cant = Integer.parseInt(tmp);
                    } catch (Exception e){
                        Log.wtf("ETH_HILO","ERROR EN CONVERSION ... "+e.getMessage());
                    }

                    Log.d("ETH_HILO","Validando Datos (Cantidad) ... ");

                    if ( cant == 0 ){ // SI NO HAY NADA POR ENVIAR
                        Log.d("ETH_HILO","Eth0 Enabled ... Sin data por enviar ...");


                        AdministrarOTG(btSocket01.getOutputStream(),false); // CARGAR


                        util.sleep(10000);
                    } else { // SI HAY ALGO POR ENVIAR
                        Log.d("ETH_HILO","Eth0 Enabled ... Con data por enviar ... ");

                        // Preguntamos por servidor
                        boolean res;
                        Connectivity c = new Connectivity();

                        String servidorDatos = "";

                        Log.d("IP_TEST","entrando");
                        QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
                        List<Servicios> servidor_datos_principal = queriesServicios.BuscarServicios("SERVIDOR_DATOS_PRINCIPAL");
                        Log.d("IP_TEST","tam: "+servidor_datos_principal.size());
                        for (int i = 0; i < servidor_datos_principal.size(); i++){
                            servidorDatos = servidor_datos_principal.get(i).getHost();
                            break;
                        }
                        Log.d("ETH_HILO","datos: "+ servidorDatos);
                        res = c.isURLReachable(getApplicationContext(),servidorDatos,"ip");

                        if (!res) {
                            //res = c.isValidConnection();

                            Log.d("ETH_HILO", "Conexion inválida (1) ... ");

                            // Si no hay conexion al servidor, activo otg por 5 seg
                            Log.d("ETH_HILO", "Eth0 Enabled ... Activando ... ");

                            AdministrarOTG(btSocket01.getOutputStream(), true); //PRENDER ETHERNET
                            Log.d("ETH_HILO", "Eth0 Enabled ... Activado ... ");

                            util.sleep(8000);
                            // Testear Si conexion es ok

                            res = c.isURLReachable(getApplicationContext(),servidorDatos,"ip");
                            Log.d("ETH_HILO", "Eth0 Enabled ... Procesado ... ");


                            if (res) {
                                Log.d("ETH_HILO", "Conexion válida (1) ... ");

                                //res = c.isValidConnection();
                                //if (res) {
                                util.sleep(10000);
                                //}
                                // Activado por 5 seg luego cargo por 12 seg

                                AdministrarOTG(btSocket01.getOutputStream(), false); //CARGAR

                                util.sleep(36000);
                            } else {
                                Log.d("ETH_HILO", "No hay Ping Activando ETH0 ... ");

                                // Si hay conexion al servidor, sigo cargando

                                AdministrarOTG(btSocket01.getOutputStream(), false); //CARGAR

                                util.sleep(36000);
                            }

                        } else {
                            Log.d("ETH_HILO", "Conexion valida (1) ... ");

                            // Si hay conexion al servidor, sigo cargando

                            AdministrarOTG(btSocket01.getOutputStream(), false); //CARGAR

                            util.sleep(36000);
                        }

                    }
                } else {
                    Log.d("ETH_HILO","Eth0 Disabled ... ");


                    AdministrarOTG(btSocket01.getOutputStream(), false); //CARGAR

                    util.sleep(10000);
                }



                /*

                DBManager db = new DBManager(ActivityPrincipal.context);
                String resultado = db.valexecSQL("SELECT PARAMETRO FROM TERMINAL_CONFIGURACION");
                String valor = resultado.split(",")[1];

                if (valor.equalsIgnoreCase("1")){
                    // levantamos ethernet
                    AdministrarOTG(btSocket01.getOutputStream(),"1");
                    util.sleep(600000); // 10min
                    AdministrarOTG(btSocket01.getOutputStream(),"0");
                    util.sleep(600000); // 10min
                } else {
                    // apagamos ethernet
                    AdministrarOTG(btSocket01.getOutputStream(),"0");
                    util.sleep(600000); // 10min
                }

                */

            }
        }
    });


    Thread threadFechahora = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            txcHora.setText(simpleDateFormat.format(calendar.getTime()));
//Log.d("Autorizaciones","Set Fechahora: " + simpleDateFormat.format(calendar.getTime()));
                        } catch (Exception e) {
                            Log.wtf("Fechahora_HILO","ERROR EN SET TEXT txcHora... " +e.getMessage());
                        }
                    }
                });

//txcHora.setText(simpleDateFormat.format(calendar.getTime()));
                util.sleep(250);
            }
        }
    });


}