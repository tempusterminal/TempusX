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
import android.os.Environment;
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

import com.tempus.proyectos.bluetoothSerial.Bluetooth;
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
import com.tempus.proyectos.log.LogManager;
import com.tempus.proyectos.threads.ThreadConnectSerial;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.InternalFile;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

    int SCORE_MANO = 0;
    /* Variables Globales */
    String FLG_AUX = "";
    boolean esperando_mano = false;
    boolean fallo_mano = false;
    boolean marcacion_mano = false;
    boolean buttonsVisibility[] = new boolean[8];
    String buttonsText[] = new String[8];

    boolean MARCACION_ACTIVA;
    int TIEMPO_ACTIVO;
    String MODO_MARCACION;
    String TEMPLATE = "";
    String INDICE = "";
    String NOMBRE_PERSONAL_MANO;

    LogManager logManager;

    ThreadConnectSerial threadConnectSerial;

    String TAG = "TX-PTX-CAP";

    int c = 100;

    boolean MODO_EVENTO = false;

    Date TIEMPO_PRESENTE_BT01;
    Date TIEMPO_PASADO_BT01;
    Date TIEMPO_PRESENTE_BT02;
    Date TIEMPO_PASADO_BT02;
    Date TIEMPO_PRESENTE_BT03;
    Date TIEMPO_PASADO_BT03;

    String MAC_BT_00 = "";
    String MAC_BT_01 = "";
    String MAC_BT_02 = "";
    String MAC_BT_03 = "";

    boolean STATUS_ETHERNET = false;
    boolean STATUS_CONNECT_01 = false;
    boolean STATUS_CONNECT_02 = false;
    boolean STATUS_CONNECT_03 = false;

    boolean HARD_FAIL_01 = false;
    boolean HARD_FAIL_02 = false;
    boolean HARD_FAIL_03 = false;

    boolean MODO_PATRON = false;

    String PATRON_SECRET = "";


    /* --- ACCESO ESTÁTICO --- */

    public static boolean INICIADO;

    static int TIPO_TERMINAL = 0; // A=1; A+H=2; A+M=3; A+H+M=4

    public static boolean BT_00_ENABLED;
    public static boolean BT_01_ENABLED;
    public static boolean BT_02_ENABLED;
    public static boolean BT_03_ENABLED;

    public static boolean BT_01_IS_CONNECTED = false;
    public static boolean BT_02_IS_CONNECTED = false;
    public static boolean BT_03_IS_CONNECTED = false;

    public static Bluetooth btSocketEthernet;
    public static Bluetooth btSocket01;
    public static Bluetooth btSocket02;
    public static Bluetooth btSocket03;

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

    List<String> lectoras;

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
    boolean enableBoot;
    String msjBoot;
    boolean isBooting;

    /* --- OBjetos --- */
    Utilities util;
    UserInterfaceM ui;
    Fechahora fechahora;
    Connectivity connectivity;


    /* --- Variables de DATA --- */

    private DBManager dbManager;
    private QueriesPersonalTipolectoraBiometria queriesPersonalTipolectoraBiometria;
    private QueriesMarcaciones queriesMarcaciones;


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
    ImageView imgViewMarcacionGeomano;
    public static TextView txvMarcacionLed01;
    public static TextView txvMarcacionLed02;
    public static TextView txvMarcacionLed03;
    public static TextView txvMarcacionLed04;

    ImageView btnMaster;
    Button btnAccess1;
    Button btnAccess2;
    Button btnAccess3;
    Button btnAccess4;

    public static Button buttonWarning01;
    public static Button buttonWarning02;

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

        logManager = new LogManager();
        logManager.RegisterLogTXT("INICIO TEMPUSX");

        int id = android.os.Process.myPid();
        Log.wtf("PID_ACTIVITIE", String.valueOf(id));

        if (btSocket01 != null || btSocket02 != null) {
            Log.wtf("OBJ_STATUS", "EXISTE");
            //logManager.RegisterLogTXT("OBJ_STATUS EXISTE");
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
        imgViewMarcacionGeomano = (ImageView) findViewById(R.id.imgViewMarcacionGeomano);
        txvMarcacionLed01 = (TextView) findViewById(R.id.txvMarcacionLed01);
        txvMarcacionLed02 = (TextView) findViewById(R.id.txvMarcacionLed02);
        txvMarcacionLed03 = (TextView) findViewById(R.id.txvMarcacionLed03);
        txvMarcacionLed04 = (TextView) findViewById(R.id.txvMarcacionLed04);

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
        manageEventMode(false);             // Ocultar modo Evento
        manageKeyboard(false);              // Ocultar teclado


        showMsgBoot(true, "Iniciando sistema, por favor espere ...");    // Bloquear pantalla hasta conectar

        buttonWarning01.setVisibility(View.INVISIBLE);
        buttonWarning02.setVisibility(View.INVISIBLE);

        imgViewMarcacionGeomano.setVisibility(View.INVISIBLE);
        txvMarcacionLed01.setVisibility(View.INVISIBLE);
        txvMarcacionLed02.setVisibility(View.INVISIBLE);
        txvMarcacionLed03.setVisibility(View.INVISIBLE);
        txvMarcacionLed04.setVisibility(View.INVISIBLE);

        areaMarcaEnabled = false;
        areaAccessEnabled = false;

        CargarDatosInicialesUPD();
        ConectarSerialesUPD();

        threadSerial01UPD.start();
        threadSerial02UPD.start();
//
        threadControlPrincipal.start();
        threadControlPantalla.start();


        // Iniciar Rutinas en verdadero
        ProcessSyncTS processSyncTS = new ProcessSyncTS("Sync_Marcaciones_Biometrias");
        processSyncTS.start(this);
        ProcessSyncST processSyncST = new ProcessSyncST("Sync_Autorizacion");
        processSyncST.start(this);
        ProcessSyncDatetime processSyncDatetime = new ProcessSyncDatetime("Sync_Datetime");
        processSyncDatetime.start(this);

        threadFechahora.start();

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
                        //Date date = new Date();
                        //tiempoPasado = date;

                        TIEMPO_ACTIVO = 10;

                        actualizarFlag(null, null);
                    }

                    txvMensajePantalla.setText("");

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
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 10;
                    actualizarFlag(null, null);
                }
            }
        });

        // Boton Acceso Patron 2
        btnAccess2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("2");
                if (MODO_EVENTO) {
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 10;
                    actualizarFlag(null, null);
                }
            }
        });

        // Boton Acceso Patron 3
        btnAccess3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("3");
                if (MODO_EVENTO) {
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 10;
                    actualizarFlag(null, null);
                }
            }
        });

        // Boton Acceso Patron 4
        btnAccess4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("4");
                if (MODO_EVENTO) {
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 10;
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
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 6;
            }
        });

        // Boton Evento 02
        btnEvent02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "002";
                Log.v(TAG, "Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("002", btnEvent02);
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 6;
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
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 6;
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
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 6;
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
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 6;
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
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 6;
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
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 6;
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
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 6;
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

                STATUS_ETHERNET = false;
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

    public void reboot() {

        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot -p" });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i("TEMPUS: ", "No se puede reiniciar!!!!!!!!!!!!!!!!", ex);
        }

    }



    public void crearBD() {
        dbManager = new DBManager(context);
        dbManager.all("1,1,1,1,1,1");
    }

    public void CargarDatosInicialesUPD() {

        try {
            idTerminal = dbManager.valexecSQL("SELECT IDTERMINAL FROM TERMINAL");
        } catch(Exception e) {
            e.printStackTrace();
        }


        QueriesParameters queriesParameters = new QueriesParameters(this);
        Parameters parameters;

        try {
            parameters = queriesParameters.selectParameter("BT_01");
            MAC_BT_01 = parameters.getValue();
            MAC_BT_01 = "20:16:08:10:42:63";
            Log.d(TAG, "CargarDatosIniciales > MAC_BT_01 = OK");
        } catch (Exception e) {
            //MAC_BT_01 = "98:D3:34:90:87:DC";//CARRION
            //MAC_BT_01 = "20:16:05:03:24:64"; carrion 11
            MAC_BT_01 = "20:16:08:10:42:63";
        }

        try {
            parameters = queriesParameters.selectParameter("BT_02");
            MAC_BT_02 = parameters.getValue();
            MAC_BT_02 = "00:00:00:00:00:00";
            Log.d(TAG, "CargarDatosIniciales > MAC_BT_02 = OK");
        } catch (Exception e) {
            //MAC_BT_02 = "00:12:03:16:02:08";//CARRION
            //MAC_BT_02 = "20:16:08:10:42:29";// carrion 11
            MAC_BT_02 = "00:00:00:00:00:00";
        }

        try {
            parameters = queriesParameters.selectParameter("BT_03");
            MAC_BT_03 = parameters.getValue();
            Log.d(TAG, "CargarDatosIniciales > MAC_BT_03 = OK");
            MAC_BT_03 = "20:16:08:10:46:09";//
        } catch (Exception e) {
            //MAC_BT_03 = "00:00:00:00:00:00";//CARRION
            MAC_BT_03 = "20:16:08:10:46:09";//

        }

        try {
            parameters = queriesParameters.selectParameter("TIPO_TERMINAL");
            TIPO_TERMINAL = Integer.parseInt(parameters.getValue());
            Log.d(TAG, "CargarDatosIniciales > TIPO_TERMINAL = OK");
        } catch (Exception e) {
            TIPO_TERMINAL = 2;
        }

        try {
            parameters = queriesParameters.selectParameter("MODO_EVENTO");
            String enable_evento = parameters.getValue();
            if (enable_evento == "0") {
                MODO_EVENTO = false;
            } else {
                MODO_EVENTO = true;
            }
            Log.d(TAG, "CargarDatosIniciales > MODO_EVENTO = OK");
            MODO_EVENTO = false;
        } catch (Exception e) {
            MODO_EVENTO = false;
        }

        try {
            parameters = queriesParameters.selectParameter("INTERFACE_ETH");
            String enable_if01 = parameters.getValue();
            if (enable_if01 == "0") {
                INTERFACE_ETH = false;
            } else {
                INTERFACE_ETH = true;
            }
            Log.d(TAG, "CargarDatosIniciales > INTERFACE_ETH = OK");
        } catch (Exception e) {
            INTERFACE_ETH = false;
        }

        try {
            parameters = queriesParameters.selectParameter("INTERFACE_WLAN");
            String enable_if02 = parameters.getValue();
            if (enable_if02 == "0") {
                INTERFACE_WLAN = false;
            } else {
                INTERFACE_WLAN = true;
            }
            Log.d(TAG, "CargarDatosIniciales > INTERFACE_WLAN = OK");
        } catch (Exception e) {
            INTERFACE_WLAN = true;
        }

        try {
            parameters = queriesParameters.selectParameter("INTERFACE_PPP");
            String enable_if03 = parameters.getValue();
            if (enable_if03 == "0") {
                INTERFACE_PPP = false;
            } else {
                INTERFACE_PPP = true;
            }
            Log.d(TAG, "CargarDatosIniciales > INTERFACE_PPP = OK");
        } catch (Exception e) {
            INTERFACE_PPP = false;
        }

        MARCACION_ACTIVA = false;
        TIEMPO_ACTIVO = 0;
        MODO_MARCACION = "";
        MAC_BT_00 = "00:00:00:00:00:00";
        //RANSA 1 - 14;1F:78:24:1A:31
        //MAC_BT_01 = "20:16:08:10:42:63";
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "20:16:08:10:46:09";

        //RANSA 2 - 14:1F:78:86:2F:9C
        //MAC_BT_01 = "20:16:08:10:64:87";
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "20:16:08:10:65:94";

        //RANSA 3 - 14:1F:78:86:2F:B1
        MAC_BT_01 = "20:16:08:10:58:40";
        MAC_BT_02 = "00:00:00:00:00:00";
        MAC_BT_03 = "20:16:08:10:58:52";

        //CLINICA INTERNACIONAL 1
        //MAC_BT_01 = "00:12:06:04:99:06";
        //MAC_BT_02 = "00:12:06:04:98:90";
        //MAC_BT_03 = "00:00:00:00:00:00";

        MODO_EVENTO = true;
        TIPO_TERMINAL = 3;
        INTERFACE_ETH = false;
        INTERFACE_WLAN = true;
        INTERFACE_PPP = false;
        SCORE_MANO = 120;

        if (MODO_EVENTO) {

            buttonsVisibility[0] = true;
            buttonsVisibility[1] = true;
            buttonsVisibility[2] = true;
            buttonsVisibility[3] = true;
            buttonsVisibility[4] = false;
            buttonsVisibility[5] = false;
            buttonsVisibility[6] = false;
            buttonsVisibility[7] = false;

            buttonsText[0] = "1";
            buttonsText[1] = "2";
            buttonsText[2] = "3";
            buttonsText[3] = "4";
            buttonsText[4] = "5";
            buttonsText[5] = "6";
            buttonsText[6] = "7";
            buttonsText[7] = "8";

            /*
            try {
                parameters = queriesParameters.selectParameter("BTN_EVENTO_1");
                int visible_0 = parameters.getEnable();
                buttonsText[0] = parameters.getValue();
                if (visible_0 == 0) {
                    buttonsVisibility[0] = true;
                }
                Log.d(TAG, "CargarDatosIniciales > BTN_EVENTO_1 = OK");
            } catch (Exception e) {
            }

            try {
                parameters = queriesParameters.selectParameter("BTN_EVENTO_2");
                int visible_1 = parameters.getEnable();
                buttonsText[1] = parameters.getValue();
                if (visible_1 == 0) {
                    buttonsVisibility[1] = true;
                }
                Log.d(TAG, "CargarDatosIniciales > BTN_EVENTO_2 = OK");
            } catch (Exception e) {
            }

            try {
                parameters = queriesParameters.selectParameter("BTN_EVENTO_3");
                int visible_2 = parameters.getEnable();
                buttonsText[2] = parameters.getValue();
                if (visible_2 == 0) {
                    buttonsVisibility[2] = true;
                }
                Log.d(TAG, "CargarDatosIniciales > BTN_EVENTO_3 = OK");
            } catch (Exception e) {
            }

            try {
                parameters = queriesParameters.selectParameter("BTN_EVENTO_4");
                int visible_3 = parameters.getEnable();
                buttonsText[3] = parameters.getValue();
                if (visible_3 == 0) {
                    buttonsVisibility[3] = true;
                }
                Log.d(TAG, "CargarDatosIniciales > BTN_EVENTO_4 = OK");
            } catch (Exception e) {
            }

            try {
                parameters = queriesParameters.selectParameter("BTN_EVENTO_5");
                int visible_4 = parameters.getEnable();
                buttonsText[4] = parameters.getValue();
                if (visible_4 == 0) {
                    buttonsVisibility[4] = true;
                }
                Log.d(TAG, "CargarDatosIniciales > BTN_EVENTO_5 = OK");
            } catch (Exception e) {
            }

            try {
                parameters = queriesParameters.selectParameter("BTN_EVENTO_6");
                int visible_5 = parameters.getEnable();
                buttonsText[5] = parameters.getValue();
                if (visible_5 == 0) {
                    buttonsVisibility[5] = true;
                }
                Log.d(TAG, "CargarDatosIniciales > BTN_EVENTO_6 = OK");
            } catch (Exception e) {
            }

            try {
                parameters = queriesParameters.selectParameter("BTN_EVENTO_7");
                int visible_6 = parameters.getEnable();
                buttonsText[6] = parameters.getValue();
                if (visible_6 == 0) {
                    buttonsVisibility[6] = true;
                }
                Log.d(TAG, "CargarDatosIniciales > BTN_EVENTO_7 = OK");
            } catch (Exception e) {
            }

            try {
                parameters = queriesParameters.selectParameter("BTN_EVENTO_8");
                int visible_7 = parameters.getEnable();
                buttonsText[7] = parameters.getValue();
                if (visible_7 == 0) {
                    buttonsVisibility[7] = true;
                }
                Log.d(TAG, "CargarDatosIniciales > BTN_EVENTO_8 = OK");
            } catch (Exception e) {
            }
            */

            flag = null;
            txvMensajePantalla.setText("SELECCIONE UN EVENTO");
            try {
                InicializarModoEvento();
            } catch ( Exception e ) {
                Log.e(TAG, "CargarDatosIniciales > Evento ERROR: "+ e.getMessage());
            }

        } else {
            flag = "127";
            txvMensajePantalla.setText("PASE SU TARJETA");
        }

        tarjetaKey = "";
        visibleKey = false;
        tiempoMarcacion = 4;
        tiempoPatron = 5;
        isBooting = true;

        //lectoras = new HashMap<String, String>();
        //lectoras.put("01", "TECLADO");
        //lectoras.put("02", "DNI");
        //lectoras.put("04", "PROXIMIDAD");
        //lectoras.put("07", "HUELLA SUPREMA");

        lectoras = new ArrayList<String>();
        lectoras.add("TECLADO");
        lectoras.add("DNI");
        lectoras.add("PROXIMIDAD CHINA");
        lectoras.add("HUELLA SUPREMA");

        HabilitarBluetooth();
        HabilitarBluetoothEthernet();

    }

    public void InicializarModoEvento() {
        if (buttonsVisibility[0]) {
            btnEvent01.setVisibility(View.VISIBLE);
            btnEvent01.setText(buttonsText[0]);
        } else {
            btnEvent01.setVisibility(View.INVISIBLE);
        }

        if (buttonsVisibility[1]) {
            btnEvent02.setVisibility(View.VISIBLE);
            btnEvent02.setText(buttonsText[1]);
        } else {
            btnEvent02.setVisibility(View.INVISIBLE);
        }

        if (buttonsVisibility[2]) {
            btnEvent03.setVisibility(View.VISIBLE);
            btnEvent03.setText(buttonsText[2]);
        } else {
            btnEvent03.setVisibility(View.INVISIBLE);
        }

        if (buttonsVisibility[3]) {
            btnEvent04.setVisibility(View.VISIBLE);
            btnEvent04.setText(buttonsText[3]);
        } else {
            btnEvent04.setVisibility(View.INVISIBLE);
        }

        if (buttonsVisibility[4]) {
            btnEvent05.setVisibility(View.VISIBLE);
            btnEvent05.setText(buttonsText[4]);
        } else {
            btnEvent05.setVisibility(View.INVISIBLE);
        }

        if (buttonsVisibility[5]) {
            btnEvent06.setVisibility(View.VISIBLE);
            btnEvent06.setText(buttonsText[5]);
        } else {
            btnEvent06.setVisibility(View.INVISIBLE);
        }

        if (buttonsVisibility[6]) {
            btnEvent07.setVisibility(View.VISIBLE);
            btnEvent07.setText(buttonsText[6]);
        } else {
            btnEvent07.setVisibility(View.INVISIBLE);
        }

        if (buttonsVisibility[7]) {
            btnEvent08.setVisibility(View.VISIBLE);
            btnEvent08.setText(buttonsText[7]);
        } else {
            btnEvent08.setVisibility(View.INVISIBLE);
        }

    }

    public void HabilitarBluetooth() {
        switch (TIPO_TERMINAL) {
            case 1:
                BT_01_ENABLED = true;
                BT_02_ENABLED = false;
                BT_03_ENABLED = false;
                break;
            case 2:
                BT_01_ENABLED = true;
                BT_02_ENABLED = true;
                BT_03_ENABLED = false;
                break;
            case 3:
                BT_01_ENABLED = true;
                BT_02_ENABLED = false;
                BT_03_ENABLED = true;
                break;
            case 4:
                BT_01_ENABLED = true;
                BT_02_ENABLED = true;
                BT_03_ENABLED = true;
                break;
            case 5:
                BT_01_ENABLED = false;
                BT_02_ENABLED = true;
                BT_03_ENABLED = false;
                break;
            default:
                BT_01_ENABLED = false;
                BT_02_ENABLED = false;
                BT_03_ENABLED = false;
                break;
        }
    }

    public void HabilitarBluetoothEthernet() {
        if (INTERFACE_ETH) {
            BT_00_ENABLED = true;
        } else {
            BT_00_ENABLED = false;
        }
    }

    public void ConectarSerialesUPD() {

        InicializarObjetosSeriales();

        btSocketEthernet = new Bluetooth(MAC_BT_00);
        btSocket01 = new Bluetooth(MAC_BT_01);
        btSocket02 = new Bluetooth(MAC_BT_02);
        btSocket03 = new Bluetooth(MAC_BT_03);

        //logManager.RegisterLogTXT("Seriales: " + MAC_BT_01 + " - " + MAC_BT_02 + " - " + MAC_BT_03);

        threadConnectSerial = new ThreadConnectSerial(this);
        threadConnectSerial.start();

    }

    public void InicializarObjetosSeriales() {
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
            objHandPunch = new MainHandPunch(this);
        }
    }

    public void restartBluetooth(){
        BluetoothAdapter.getDefaultAdapter().disable();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BluetoothAdapter.getDefaultAdapter().enable();
        try {
            Thread.sleep(2000);
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
                //logManager.RegisterLogTXT("COMANDO: INGRESAR LOGIN");
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
                //logManager.RegisterLogTXT("COMANDO: SUPREMA (CANCEL)");
                try {
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
                } catch(Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN: ","COMANDO: SUPREMA (CANCEL) -> " + e.getMessage());
                }

                break;

            case "222232":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: SUPREMA (DELETE_ALL_TEMPLATES)");
                //logManager.RegisterLogTXT("COMANDO: SUPREMA (DELETE_ALL_TEMPLATES)");
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
                //logManager.RegisterLogTXT("COMANDO: SUPREMA (NUMBER_TEMPLATES)");
                try{
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"NumberTemplate",null);
                    util.sleep(800);
                    objSuprema.limpiarTramaSuprema();
                }catch(Exception e){
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: SUPREMA (NUMBER_TEMPLATES) -> " + e.getMessage());
                }

                break;

            case "111132":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: HABILITAR REPLICADO");
                //logManager.RegisterLogTXT("COMANDO: HABILITAR REPLICADO");
                isReplicating = true;
                break;

            case "1324111":
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: REINICIALIZAR BD");
                //logManager.RegisterLogTXT("COMANDO: REINICIALIZAR BD");
                try {
                    crearBD();
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: REINICIALIZAR BD -> " + e.getMessage());
                }
                break;

            case "33334334":
                // OTG (QUITAR CARGA)
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: OTG - QUITAR CARGA");
                //logManager.RegisterLogTXT("COMANDO: OTG - QUITAR CARGA");
                try {
                    AdministrarOTG(btSocket01.getOutputStream(),true);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: OTG - QUITAR CARGA -> " + e.getMessage());
                }

                break;
            case "33334331":
                // CARGAR
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: OTG - CARGAR");
                //logManager.RegisterLogTXT("COMANDO: OTG - CARGAR");
                try {
                    AdministrarOTG(btSocket01.getOutputStream(),false);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: OTG - CARGAR -> " + e.getMessage());
                }
                break;

            case "113322443241321": // OCULTAR SYSTEMUI
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: OCULTAR SYSTEMUI");
                //logManager.RegisterLogTXT("COMANDO: OCULTAR SYSTEMUI");
                try {
                    hideNavigationBar(true);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: OCULTAR SYSTEMUI -> " + e.getMessage());
                }
                break;

            case "113322443241322": // MOSTRAR SYSTEMUI
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: MOSTRAR SYSTEMUI");
                //logManager.RegisterLogTXT("COMANDO: MOSTRAR SYSTEMUI");
                try {
                    hideNavigationBar(false);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: MOSTRAR SYSTEMUI -> " + e.getMessage());
                }
                break;

            case "113322443241323": // REINICIAR
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: REINICIAR");
                //logManager.RegisterLogTXT("COMANDO: REINICIAR");
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: REINICIAR -> " + e.getMessage());
                }
                break;

            case "113322443241324": // APAGAR
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: APAGAR");
                //logManager.RegisterLogTXT("COMANDO: APAGAR");
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: APAGAR -> " + e.getMessage());
                }
                break;

            case "113322443241311": // ABRIR CONFIGURACION DE ANDROID
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: CONFIGURACION DE ANDROID");
                //logManager.RegisterLogTXT("COMANDO: CONFIGURACION DE ANDROID");
                try {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    Intent startMain = getPackageManager().getLaunchIntentForPackage("com.tempus.ecernar.overlaywifi");
                    startActivity(startMain);
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: CONFIGURACION DE ANDROID -> " + e.getMessage());
                }

                break;

            case "41444414": // ABRIR CONFIGURACION DE ANDROID
                Log.d("SYSTEM_MAIN_INSTRUCTION","COMANDO: APAGAR");
                //logManager.RegisterLogTXT("COMANDO: CONFIGURACION DE ANDROID");
                try {
                    ShutdownArduino(btSocket01.getOutputStream());
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: CONFIGURACION DE ANDROID -> " + e.getMessage());
                }

                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e("ERROR_SYSTEM_MAIN","COMANDO: APAGAR -> " + e.getMessage());
                }

                break;

            default:
                break;
        }
    }

    public void ShutdownArduino(OutputStream out) {
        Log.v(TAG, "MARCACION OK");
        try {
            out.write(util.hexStringToByteArray("244F4158410013423030303030303030000041"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AccesoSecreto(String dato) {
        PATRON_SECRET = PATRON_SECRET + dato;
        Log.d("AccesoSecreto",PATRON_SECRET);

        switch (PATRON_SECRET) {
            case "1111111232132122":
                Log.d("AccesoSecreto","COMANDO: MODO DIOS");
                //logManager.RegisterLogTXT("COMANDO: MODO DIOS");
                try {
                    showLoginDialog();
                } catch (Exception e) {
                    Log.e("MODO_DIOS", e.getMessage() );
                }
                PATRON_SECRET = "";
                break;
            case "3333333123123":
                Log.d("AccesoSecreto","COMANDO: REINICIAR");
                //logManager.RegisterLogTXT("COMANDO: REINICIAR");
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

        String TAG = "MarcacionMasterTAG";

        Log.d(TAG, "lectoraName: " + lectoraName);
        Log.d(TAG, "tarjeta: " + tarjeta);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                manageKeyboard(false);
            }
        });

        if (lectoras.contains(lectoraName)) {
            if (flag == null || flag == "") {

            } else {
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 5;

                LimpiarDatosMarcacion();
                String lectora = lectoraName;
                Log.d(TAG, "Lectora: " + lectora);

                if (lectora != null) {

                    Log.v(TAG, lectora);
                    if (lectora == "HUELLA SUPREMA") {
                        //tarjeta = String.valueOf(util.convertHexToDecimal(tarjeta.substring(6,8) + tarjeta.substring(4,6) + tarjeta.substring(2,4) + tarjeta.substring(0,2)));
                    } else {
                        if (lectora == "PROXIMIDAD CHINA") {
                            tarjeta = hexToDecimalStringProx(tarjeta);
                        } else {
                            if (lectora != "TECLADO") {
                                tarjeta = util.convertHexToString(tarjeta);
                            }
                        }
                    }

                    Log.v(TAG, "TARJETA " + tarjeta);

                    try {
                        FLG_AUX = flag;
                        queriesMarcaciones = new QueriesMarcaciones(this);
                        Log.v(TAG, "MarcacionMaster Exec >");
                        Log.v(TAG, " > Tarjeta: " + tarjeta);
                        Log.v(TAG, " > ID Terminal: " + idTerminal);
                        Log.v(TAG, " > ID Lectora: " + getNroLectora(lectora));
                        Log.v(TAG, " > Flag: " + flag + " - FLG_AUX: " + FLG_AUX);
                        Log.v(TAG, " > FechaHora: " + fechahora.getFechahora());

                        //autorizaciones = queriesMarcaciones.GestionarMarcaciones(tarjeta, idTerminal, Integer.parseInt(getNroLectora(lectora)), flag, fechahora.getFechahora(), 1);
                        String autorizacion = "";
                        String array_autorizaciones[] = {};


                        if (!MARCACION_ACTIVA) {
                            MODO_MARCACION = "";
                        }
                        /*
                        else {
                            if (marcacion_mano) {
                                esperando_mano = false;
                                fallo_mano = false;
                                marcacion_mano = false;
                            } else {
                        */
                                try {
                                    autorizacion = queriesMarcaciones.ModoMarcacion(tarjeta, idTerminal, Integer.parseInt(getNroLectora(lectora)), FLG_AUX, fechahora.getFechahora(), MODO_MARCACION);
                                    Log.d(TAG, "Resultado de ModoMarcacion: " + autorizacion);
                                    esperando_mano = false;
                                    //fallo_mano = true;
                                    array_autorizaciones = autorizacion.split(",");
                                    Log.d(TAG, "Resultado de array_autorizaciones: " + array_autorizaciones.toString());
                                    NOMBRE_PERSONAL_MANO = array_autorizaciones[0];
                                } catch (Exception e) { // Error NO EXISTE TARJETA
                                    Log.e(TAG, "Resultado de ModoMarcacion: " + autorizacion);
                                    Log.e(TAG, "Resultado de Busqueda de Autorizaciones: " + array_autorizaciones.toString());
                                    Log.e(TAG, "Error: " + e);
                                }

                                //String autorizacion = autorizaciones.getMensaje();

                                if (array_autorizaciones[2].equalsIgnoreCase("marcacion autorizada")) {

                                    if (array_autorizaciones[4].equals("0")){ // Finalizo Marcacion
                                        Log.d(TAG, "FINALIZO MARCACION");
                                        MARCACION_ACTIVA = false;
                                        MODO_MARCACION = "";
                                        TIEMPO_ACTIVO = 5;
                                        MarcacionOK(btSocket01.getOutputStream());
                                        mNombre = array_autorizaciones[0];
                                        mTarjeta = array_autorizaciones[1];
                                        mMensajePrincipal = array_autorizaciones[2];
                                        mMensajeSecundario = array_autorizaciones[3];
                                        MarcacionUI();

                                    } else { // Marcacion Continua
                                        Log.d(TAG, "CONTINUA MARCACION");
                                        String mensaje = analizarModoMarcacion(array_autorizaciones[4]);
                                        Log.d(TAG, "Mensaje ANALIZAR_MODO_MARCACION: " + mensaje);
                                        MARCACION_ACTIVA = true;
                                        MODO_MARCACION = array_autorizaciones[5];

                                        if (mensaje.contains("MANO")) {
                                            //marcacion_mano = true;
                                            TIEMPO_ACTIVO = 25;
                                            mMensajePrincipal = "";
                                            mMensajeSecundario = mensaje;

                                            Log.d(TAG, "MANO TIEMPO_ACTIVO: " + TIEMPO_ACTIVO);

                                            TEMPLATE = dbManager.valexecSQL("SELECT VALOR_BIOMETRIA FROM PERSONAL_TIPOLECTORA_BIOMETRIA INNER JOIN PERSONAL ON PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA = PERSONAL.EMPRESA AND PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO = PERSONAL.CODIGO WHERE VALOR_TARJETA = '"+tarjeta+"' AND ID_TIPO_LECT = 10 AND (PERSONAL.ESTADO != '002' OR PERSONAL.FECHA_DE_CESE IS NULL);");
                                            INDICE = dbManager.valexecSQL("SELECT INDICE_BIOMETRIA FROM PERSONAL_TIPOLECTORA_BIOMETRIA INNER JOIN PERSONAL ON PERSONAL_TIPOLECTORA_BIOMETRIA.EMPRESA = PERSONAL.EMPRESA AND PERSONAL_TIPOLECTORA_BIOMETRIA.CODIGO = PERSONAL.CODIGO WHERE VALOR_TARJETA = '"+tarjeta+"' AND ID_TIPO_LECT = 10 AND (PERSONAL.ESTADO != '002' OR PERSONAL.FECHA_DE_CESE IS NULL);");

                                            if (TEMPLATE!=null || false) {

                                                Log.d(TAG, "BIOMETRIA EXISTENTE ... INICIANDO GEOMANO");
                                                // Existe Biometria

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        txvMarcacionLed01.setVisibility(View.VISIBLE);
                                                        txvMarcacionLed02.setVisibility(View.VISIBLE);
                                                        txvMarcacionLed03.setVisibility(View.VISIBLE);
                                                        txvMarcacionLed04.setVisibility(View.VISIBLE);
                                                        imgViewMarcacionGeomano.setVisibility(View.VISIBLE);
                                                    }
                                                });

                                                Thread verificarMano = new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                    //Log.d("MarcacionMasterTAG", "Abort 1");
                                                    //objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "FIX", null);
                                                    //util.sleep(1000);

                                                    Log.d("MarcacionMasterTAG", "Abort 1");
                                                    objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "ABORT", null);
                                                    util.sleep(50);

                                                    Log.d("MarcacionMasterTAG", "Abort 2");
                                                    objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "VERIFY_ON_EXTERNAL_DATA", TEMPLATE);
                                                    util.sleep(50);

                                                    esperando_mano = true;
                                                    fallo_mano = false;

                                                    Log.d("MarcacionMasterTAG","Abort 3");
                                                    while (esperando_mano) {
                                                        String res = objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "SEND_STATUS_CRC", null);
                                                        String tmp = objHandPunch.OperarStatus(res,"");

                                                        if (tmp.equalsIgnoreCase("Exito")){
                                                            Log.d("MarcacionMasterTAG","EXITO");
                                                            esperando_mano = false;
                                                        }

                                                        if (tmp.equalsIgnoreCase("Fallo")){
                                                            Log.d("MarcacionMasterTAG","FALLO");
                                                            fallo_mano = true;
                                                            // FALLO AL CAPTURAR MANO

                                                            esperando_mano = false;
                                                        }

                                                        util.sleep(50);
                                                    }

                                                    if (fallo_mano) {

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                txvMarcacionLed01.setVisibility(View.INVISIBLE);
                                                                txvMarcacionLed02.setVisibility(View.INVISIBLE);
                                                                txvMarcacionLed03.setVisibility(View.INVISIBLE);
                                                                txvMarcacionLed04.setVisibility(View.INVISIBLE);
                                                                imgViewMarcacionGeomano.setVisibility(View.INVISIBLE);
                                                            }
                                                        });


                                                        Log.d("MarcacionMasterTAG", "FINALIZO MARCACION");
                                                        MARCACION_ACTIVA = false;
                                                        MODO_MARCACION = "";
                                                        TIEMPO_ACTIVO = 5;
                                                        MarcacionKO(btSocket01.getOutputStream());
                                                        mNombre = NOMBRE_PERSONAL_MANO;
                                                        mTarjeta = INDICE;
                                                        mMensajePrincipal = "MARCACION NO AUTORIZADA";
                                                        mMensajeSecundario = "ERROR AL CAPTURAR MANO";
                                                        MarcacionUI();

                                                    } else {
                                                        TEMPLATE = "";
                                                        String resultado = objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "SEND_TEMPLATE", null);
                                                        util.sleep(50);

                                                        Log.d("MarcacionMasterTAG", "OUTPUT: "+resultado);

                                                        // Analizar Score
                                                        if (resultado.length()>14) {
                                                            Log.d("MarcacionMasterTAG", "SCORE OK");
                                                            String SCORE_01 = resultado.substring(12,14);
                                                            String SCORE_02 = resultado.substring(10,12);

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    txvMarcacionLed01.setVisibility(View.INVISIBLE);
                                                                    txvMarcacionLed02.setVisibility(View.INVISIBLE);
                                                                    txvMarcacionLed03.setVisibility(View.INVISIBLE);
                                                                    txvMarcacionLed04.setVisibility(View.INVISIBLE);
                                                                    imgViewMarcacionGeomano.setVisibility(View.INVISIBLE);
                                                                }
                                                            });

                                                            int s = util.convertHexToDecimal(SCORE_01 + SCORE_02);
                                                            String array_autorizaciones[] = {};
                                                            if (s < SCORE_MANO) { // MARCACION AUTORIZADA
                                                                Log.d("MarcacionMasterTAG", "S<100");
                                                                String res_marcacion = "";
                                                                try {
                                                                    res_marcacion = queriesMarcaciones.ModoMarcacion(INDICE, idTerminal, 10, FLG_AUX, fechahora.getFechahora(), MODO_MARCACION);
                                                                } catch( Exception e) {
                                                                    Log.e("MarcacionMasterTAG", "Error res_marcacion: "+e.getMessage());
                                                                }

                                                                Log.d("MarcacionMasterTAG", "res_marcacion: "+res_marcacion);
                                                                array_autorizaciones = res_marcacion.split(",");
                                                                if (array_autorizaciones[2].equalsIgnoreCase("marcacion autorizada")) {

                                                                    Log.d("MarcacionMasterTAG", "FINALIZO MARCACION 1");
                                                                    MARCACION_ACTIVA = false;
                                                                    MODO_MARCACION = "";
                                                                    TIEMPO_ACTIVO = 5;
                                                                    MarcacionOK(btSocket01.getOutputStream());
                                                                    mNombre = array_autorizaciones[0];
                                                                    mTarjeta = array_autorizaciones[1];
                                                                    mMensajePrincipal = array_autorizaciones[2];
                                                                    mMensajeSecundario = array_autorizaciones[3];
                                                                    Log.d("MarcacionMasterTAG", "FINALIZO MARCACION 1");
                                                                    MarcacionUI();
                                                                } else {
                                                                    Log.d("MarcacionMasterTAG", "FINALIZO MARCACION 2");
                                                                    MARCACION_ACTIVA = false;
                                                                    MODO_MARCACION = "";
                                                                    TIEMPO_ACTIVO = 5;
                                                                    MarcacionKO(btSocket01.getOutputStream());
                                                                    mNombre = array_autorizaciones[0];
                                                                    mTarjeta = array_autorizaciones[1];
                                                                    mMensajePrincipal = array_autorizaciones[2];
                                                                    mMensajeSecundario = array_autorizaciones[3];
                                                                    Log.d("MarcacionMasterTAG", "FINALIZO MARCACION 3");
                                                                    MarcacionUI();
                                                                }

                                                            } else { // MARCACION NO AUTORIZADA, BIOMETRIA NO COINCIDE
                                                                Log.d("MarcacionMasterTAG", "S>100");
                                                                Log.d("MarcacionMasterTAG", "FINALIZO MARCACION 3");
                                                                MARCACION_ACTIVA = false;
                                                                MODO_MARCACION = "";
                                                                TIEMPO_ACTIVO = 5;
                                                                MarcacionKO(btSocket01.getOutputStream());
                                                                mNombre = NOMBRE_PERSONAL_MANO;
                                                                mTarjeta = INDICE;
                                                                mMensajePrincipal = "MARCACION NO AUTORIZADA";
                                                                mMensajeSecundario = "BIOMETRIA NO COINCIDE";
                                                                Log.d("MarcacionMasterTAG", "FINALIZO MARCACION 3");
                                                                MarcacionUI();
                                                            }
                                                        }
                                                    }

                                                    //marcacion_mano = false;

                                                    Thread.currentThread().interrupt();
                                                    }
                                                });

                                                verificarMano.start();

                                            } else {
                                                //marcacion_mano = false;
                                                Log.d(TAG, "BIOMETRIA NO EXISTENTE ... ");
                                                // No Existe Biometria
                                                Log.d(TAG, "FINALIZO MARCACION");
                                                MARCACION_ACTIVA = false;
                                                MODO_MARCACION = "";
                                                TIEMPO_ACTIVO = 5;
                                                mNombre = array_autorizaciones[0];
                                                mTarjeta = array_autorizaciones[1];
                                                mMensajePrincipal = "MARCACION NO AUTORIZADA";
                                                mMensajeSecundario = "NO EXISTE BIOMETRIA";
                                                MarcacionKO(btSocket01.getOutputStream());
                                            }

                                            MarcacionUI();



                                        } else {
                                            TIEMPO_ACTIVO = 8;
                                            mNombre = array_autorizaciones[0];
                                            mTarjeta = array_autorizaciones[1];
                                            mMensajePrincipal = "";
                                            mMensajeSecundario = mensaje;

                                            MarcacionUI();
                                        }

                                    }


                                } else {

                                    MARCACION_ACTIVA = false;
                                    MODO_MARCACION = "";
                                    TIEMPO_ACTIVO = 5;

                                    if (autorizacion.equalsIgnoreCase("")) { // TEMPORAL Error NO EXISTE TARJETA
                                        MarcacionKO(btSocket01.getOutputStream());
                                        mNombre = "";
                                        mTarjeta = tarjeta;
                                        mMensajePrincipal = "MARCACION NO AUTORIZADA";
                                        mMensajeSecundario = "(*)";
                                        MarcacionUI();
                                    } else {
                                        MarcacionKO(btSocket01.getOutputStream());
                                        mNombre = array_autorizaciones[0];
                                        mTarjeta = array_autorizaciones[1];
                                        mMensajePrincipal = array_autorizaciones[2];
                                        mMensajeSecundario = array_autorizaciones[3];
                                        MarcacionUI();
                                    }

                                }






                    } catch (Exception e) {
                        MARCACION_ACTIVA = false;
                        MODO_MARCACION = "";
                        TIEMPO_ACTIVO = 5;
                        Log.d(TAG, e.getMessage());
                        MarcacionKO(btSocket01.getOutputStream());
                        mNombre = "";
                        mTarjeta = tarjeta;
                        mMensajePrincipal = "MARCACION NO AUTORIZADA";
                        mMensajeSecundario = "e000";
                        MarcacionUI();
                    }

                } else {
                    MARCACION_ACTIVA = false;
                    MODO_MARCACION = "";
                    TIEMPO_ACTIVO = 5;
                    Log.v(TAG, "LECTORA NO HABILITADA");
                    MarcacionKO(btSocket01.getOutputStream());
                    mNombre = "";
                    mTarjeta = "";
                    mMensajePrincipal = "MARCACION NO AUTORIZADA";
                    mMensajeSecundario = "e001";
                    MarcacionUI();
                }
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

    public String analizarModoMarcacion(String lectora) {

        String msj = "";

        switch (lectora) {
            case "1": // TECLADO
                msj = "DIGITE TARJETA POR TECLADO";
                break;
            case "2": // DNI
                msj = "INSERTE DNI";
                break;
            case "3": // DNI ELECTRONICO
                msj = "INSERTE DNI ELECTRONICO";
                break;
            case "4": // PROXIMIDAD
                msj = "ESPERANDO TARJ. DE PROXIMIDAD";
                break;
            case "5": // PROXIMIDAD
                msj = "ESPERANDO TARJ. DE PROXIMIDAD";
                break;
            case "6": // PROXIMIDAD
                msj = "ESPERANDO TARJ. DE PROXIMIDAD";
                break;
            case "7": // HUELLA SUPREMA
                msj = "COLOQUE SU DEDO";
                break;
            case "8": // BARRA
                msj = "ESPERANDO BARRA";
                break;
            case "9": // BANDA MAGNETICA
                msj = "ESPERANDO BANDA MAGNETICA";
                break;
            case "10": // GEOMANO
                msj = "COLOQUE SU MANO";
                break;
            default:
                msj = "";
                break;
        }

        return msj;
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

            //Date date = new Date();
            //tiempoPasado = date;

            TIEMPO_ACTIVO = 5;
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
            InicializarModoEvento();
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
                //Date date = new Date();
                //tiempoPasado = date;

                TIEMPO_ACTIVO = 5;

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
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 5;
                } else {
                    manageKeyboard(false);
                    if ( MODO_EVENTO ) {
                        actualizarFlag(null,null);
                        txvMensajePantalla.setText("SELECCIONE EVENTO");
                    }

                }
            } else {
                if (tarjetaKey.length()<7){
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 5;
                    tarjetaKey = tarjetaKey + dato;
                    txvKeyPantalla.setText(tarjetaKey);
                } else {
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 5;
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

                        // Analizar ADC
                        boolean isADC = analizarADC(objArduino.getNroLector(), String.valueOf(util.convertHexToDecimal(objArduino.getDatosLector().substring(0, 2))));

                        if (activityActive.equals("Principal") && !isADC){

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


    public boolean analizarADC(String nroLector, String valor) {
        if (nroLector.equalsIgnoreCase("44")) {
            logManager.RegisterLogTXT("BA="+valor+"\n");
            return true;
        } else {
            return false;
        }
    }


    public boolean analizarEvento(String nroLector, String valor) {
        if (nroLector.equalsIgnoreCase("0e")) {
            logManager.RegisterLogTXT("EVENTO="+valor+"\n");
            return true;
        } else {
            return false;
        }
    }


    /* ------------------------- RUTINAS THREAD SERIALES ------------------------- */

    Thread threadSerial01UPD = new Thread(new Runnable() {
        @Override
        public void run() {

            String acumulador = "";

            while (true) {
                if (!BT_01_IS_CONNECTED) {
                    util.sleep(1000);
                } else {
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
                            evaluarTrama("Arduino", acumulador);
                            objArduino.limpiarTramaArduino();
                            acumulador = "";
                        }
                    } catch (Exception e) {
                        if (!isBooting) {
                            BT_01_IS_CONNECTED = false;
                        }
                    }
                }
            }
        }
    });


    Thread threadSerial02UPD = new Thread(new Runnable() {
        @Override
        public void run() {

            String acumulador = "";
            int tamano = 26;

            while (true) {
                if (!BT_02_IS_CONNECTED) {
                    util.sleep(1000);
                } else {
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
                        if (!isBooting){
                            BT_02_IS_CONNECTED = false;
                        }
                    }
                }
            }
        }
    });


    /* --- CONTROL PRINCIPAL --- */
    public void controlGeneral(boolean c1,boolean c2, boolean c3) {

        Log.v(TAG,"CONTROL GENERAL");

        if (c1 && c2 && c3){ // Si todos los seriales estan conectados podemos hacer algo

            if (isBooting){
                isBooting = false;
                INICIADO = true;
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

                    logManager.RegisterLogTXT("APAGANDO EQUIPO 15s");
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
                        }
                    });

                    logManager.RegisterLogTXT("Apagando");
                    threadShutdown.start();

                } else {
                    logManager.RegisterLogTXT("Reconectando Interfaces");
                    c = 100;
                    detail = detail + "\n";
                    showMsgBoot(true,"Reconectando Interfaces \n"+detail+"]");
                    INICIADO = false;
                }

            }

        }
    }

    /* --- RUTINAS THREAD CONTROL --- */

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
                                controlGeneral(BT_01_IS_CONNECTED,true,true);
                                if (HARD_FAIL_01) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 2:
                                controlGeneral(BT_01_IS_CONNECTED,BT_02_IS_CONNECTED,true);
                                if (HARD_FAIL_01 || HARD_FAIL_02) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 3:
                                controlGeneral(BT_01_IS_CONNECTED,true,BT_03_IS_CONNECTED);
                                if (HARD_FAIL_01 || HARD_FAIL_03) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 4:
                                controlGeneral(BT_01_IS_CONNECTED,BT_02_IS_CONNECTED,BT_03_IS_CONNECTED);
                                if (HARD_FAIL_01 || HARD_FAIL_02 || HARD_FAIL_03) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 5:
                                controlGeneral(true,BT_02_IS_CONNECTED,true);
                                if (HARD_FAIL_01 || HARD_FAIL_02 || HARD_FAIL_03) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
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

                    Log.d("threadControlPantalla: ","[FLAG=" + flag + "] - [TIEMPO_ACTIVO=" + TIEMPO_ACTIVO + "] - [MARCACION_ACTIVA=" + MARCACION_ACTIVA + "] - [MODO_MARCACION=" + MODO_MARCACION + "]");

                    try {
                        if (TIEMPO_ACTIVO <= 0) {

                            if (TIEMPO_ACTIVO <= -3600) { TIEMPO_ACTIVO = 0; }

                            MARCACION_ACTIVA = false;
                            MODO_MARCACION = "";

                            Log.v("threadControlPantalla: ","1");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    txvMarcacionLed01.setVisibility(View.INVISIBLE);
                                    txvMarcacionLed02.setVisibility(View.INVISIBLE);
                                    txvMarcacionLed03.setVisibility(View.INVISIBLE);
                                    txvMarcacionLed04.setVisibility(View.INVISIBLE);
                                    imgViewMarcacionGeomano.setVisibility(View.INVISIBLE);

                                    manageKeyboard(false);
                                    manageLayerMarcacion(false);

                                    if ( MODO_EVENTO ){
                                        Log.v("threadControlPantalla: ","2");
                                        actualizarFlag(null,null);
                                        manageAccessButtons(false);
                                        txvMensajePantalla.setText("SELECCIONE EVENTO");
                                    }
                                }
                            });
                        }

                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                    }

                    TIEMPO_ACTIVO = TIEMPO_ACTIVO - 1;
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
                        } catch (Exception e) {
                            Log.wtf("Fechahora_HILO","ERROR EN SET TEXT txcHora... " +e.getMessage());
                        }
                    }
                });
                util.sleep(250);
            }
        }
    });

}

