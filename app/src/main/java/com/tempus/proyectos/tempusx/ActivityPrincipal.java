package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tempus.proyectos.bluetoothSerial.BluetoothSuperAdmin;
import com.tempus.proyectos.bluetoothSerial.MainArduino;
import com.tempus.proyectos.bluetoothSerial.MainSuprema;
import com.tempus.proyectos.crash.TXExceptionHandler;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Autorizaciones;
import com.tempus.proyectos.data.queries.QueriesAutorizaciones;
import com.tempus.proyectos.data.queries.QueriesBiometrias;
import com.tempus.proyectos.data.queries.QueriesLlamadas;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityPrincipal extends Activity {

    BluetoothSuperAdmin bs01;
    BluetoothSuperAdmin bs02;


    /* Variables Globales */

    String TAG = "TX-PTX-CAP";

    boolean MODO_EVENTO = false;

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

    int TIPO_TERMINAL = 2; // A=1; A+H=2; A+M=3; A+H+M=4

    boolean HARD_FAIL_01 = false;
    boolean HARD_FAIL_02 = false;
    boolean HARD_FAIL_03 = false;


    boolean MODO_PATRON = false;


    /* --- BLUETOOTH SOCKET ESTÁTICO --- */

    public static BluetoothSuperAdmin btSocket01;
    public static BluetoothSuperAdmin btSocket02;
    public static BluetoothSuperAdmin btSocket03;

    //public static BluetoothManager btSocket01;
    //public static BluetoothManager btSocket02;
    //public static BluetoothManager btSocket03;


    /* --- ACCESO ESTÁTICO --- */

    public static MainArduino objArduino;
    public static MainSuprema objSuprema;

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

    long lastDown;
    long lastDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // Inicialización nivel cero

        activityActive = "Principal";

        Thread.setDefaultUncaughtExceptionHandler(new TXExceptionHandler(this));

        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "Restarting app after crash ... ", Toast.LENGTH_SHORT).show();
        }


        // Inicializacion de componentes

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

        // Objetos
        tiempoPresente = new Date();
        tiempoPasado = new Date();

        tiempoPresentePatron = new Date();
        tiempoPasadoPatron = new Date();

        ui = new UserInterfaceM();
        util = new Utilities();
        fechahora = new Fechahora();

        dbManager = new DBManager(this);
        queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(this);

        // Iniciar UI
        ui.initScreen(this);

        // Administramos UI
        manageLayerMarcacion(false);        // Ocultar Layer de Marcación
        manageAccessButtons(false);         // Ocultar Botones de Acceso
        manageEventMode( MODO_EVENTO );     // Ocultar modo Evento
        manageKeyboard(false);              // Ocultar teclado

        //restartBluetooth();

        showMsgBoot(true,"Iniciando sistema, por favor espere ...");    // Bloquear pantalla hasta conectar

        buttonWarning01.setVisibility(View.INVISIBLE);
        buttonWarning02.setVisibility(View.INVISIBLE);
        buttonWarning03.setVisibility(View.INVISIBLE);
        buttonWarningHelp.setVisibility(View.INVISIBLE);

        areaMarcaEnabled = false;
        areaAccessEnabled = false;

        // Creacion de BD
        //crearBD();

        // Cargar Datos
        cargarDatosIniciales();

        // Seriales
        iniciarParametrosSeriales();
        //

        btSocket01 = new BluetoothSuperAdmin(MAC_BT_01);
        btSocket02 = new BluetoothSuperAdmin(MAC_BT_02);


        threadControlSerial01.start();
        threadControlSerial02.start();

        conectarSeriales();
        threadControlPrincipal.start();
        threadControlPantalla.start();
        threadReplicado.start();






        //
        //
        //
        //
        // threadControlSerial01.start();
        //
        //
        //
        //
        // threadControlSerial02.start();

        /*




        // Iniciar Rutinas en falso


        //ProcessSyncTS processSyncTS = new ProcessSyncTS("Hilo_SyncMarcas");
        //processSyncTS.start(this);
//
        //ProcessMarcas processMarcas = new ProcessMarcas("Sync_Autorizacion");
        //processMarcas.start(this);


        */

        /* --- EVENTOS SOBRE COMPONENTES --- */

        // Boton Master (Logo)
        btnMaster.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(areaAccessEnabled) {
                    patronAcceso = "";
                    areaAccessEnabled = false;
                    manageAccessButtons(false);

                    if (MODO_EVENTO){
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
                        actualizarFlag(null,null);
                    }

                    txvMensajePantalla.setText("");

                    //Date date = new Date();
                    //tiempoPasado = date;

                    MODO_PATRON = true;

                }
            }
        });


        // Boton Acceso Patron 1
        btnAccess1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("1");
            }
        });

        // Boton Acceso Patron 2
        btnAccess2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("2");
            }
        });

        // Boton Acceso Patron 3
        btnAccess3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("3");
            }
        });

        // Boton Acceso Patron 4
        btnAccess4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccessManager("4");
            }
        });

        // Boton Evento 01
        btnEvent01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "001";
                Log.v(TAG,"Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("001",btnEvent01);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton Evento 02
        btnEvent02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "009";
                Log.v(TAG,"Flg_Actividad: " + flag);
                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                actualizarFlag("009",btnEvent02);
                Date date = new Date();
                tiempoPasado = date;
            }
        });

        // Boton Evento 03
        btnEvent03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "3";
            }
        });

        // Boton Evento 04
        btnEvent04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "4";
            }
        });

        // Boton Evento 05
        btnEvent05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "5";
            }
        });

        // Boton Evento 06
        btnEvent06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "6";
            }
        });

        // Boton Evento 07
        btnEvent07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "7";
            }
        });

        // Boton Evento 08
        btnEvent08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "8";
            }
        });

        // Boton de Acceso a teclado
        btnAccessKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == null || flag == ""){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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


    }









    public void reboot() {

        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
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
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //        | View.SYSTEM_UI_FLAG_FULLSCREEN
        //        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

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
                    Log.v(TAG, String.valueOf(b.getSerializable("llave")));
                }
            } else if (resultCode == 0) {
                System.out.println("RESULT CANCELLED");
            }
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

        dbManager = new DBManager(this);
        dbManager.all("1,1,1,1,1,1");

    }

    public void cargarDatosIniciales(){

        idTerminal = dbManager.valexecSQL("SELECT IDTERMINAL FROM TERMINAL");

        contadorEventoPantalla = 0;

        if (MODO_EVENTO) {
            flag = null;
            txvMensajePantalla.setText("SELECCIONE UN EVENTO");
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

        MODO_EVENTO = false;

        TIEMPO_PRESENTE_BT01 = new Date();
        TIEMPO_PASADO_BT01 = new Date();

        TIEMPO_PRESENTE_BT02 = new Date();
        TIEMPO_PASADO_BT02 = new Date();

        TIEMPO_PRESENTE_BT03 = new Date();
        TIEMPO_PASADO_BT03 = new Date();


        // DIRESA

        MAC_BT_01 = "00:15:83:35:6C:85";
        MAC_BT_02 = "98:D3:33:80:91:98";
        MAC_BT_03 = "00:00:00:00:00:00";



        // DIRESA ID 100
        //MAC_BT_01 = "20:16:08:10:42:38";
        //MAC_BT_02 = "20:16:08:09:04:41";

        // DIRESA ID 101
        //MAC_BT_01 = "20:16:08:10:83:58";
        //MAC_BT_02 = "20:16:08:10:60:73";


        //CROVISA 101
        //MAC_BT_01 = "00:15:83:35:79:C9";
        //MAC_BT_02 = "20:16:08:10:65:03";



        BT_01_ENABLED = true;   //Arduino
        BT_02_ENABLED = true;   //Suprema
        BT_03_ENABLED = false;  //Handpunch

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
            threadControlSerial03.start();
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
                txvMensajePantalla.setText("PASE SU TARJETA");
                Intent intent01 = new Intent(ActivityPrincipal.this, ActivityLogin.class);
                intent01.putExtra("llave", "valor");
                startActivityForResult(intent01, 1);
                patronAcceso = "";
                try {
                    ActivityPrincipal.objSuprema.writeToSuprema(btSocket02.getOutputStream(),"FreeScanOff",null);
                } catch(Exception e) {
                    Log.v(TAG,"ERROR ESTABLECIENDO CONEXION CON HUELLERO");
                }
                break;

            case "42143231":
                Intent intent02 = new Intent(ActivityPrincipal.this, ActivityConfigini.class);
                intent02.putExtra("llave", "valor");
                startActivityForResult(intent02, 1);
                patronAcceso = "";
                try {
                    ActivityPrincipal.objSuprema.writeToSuprema(btSocket02.getOutputStream(),"FreeScanOff",null);
                } catch(Exception e) {
                    Log.v(TAG,"ERROR ESTABLECIENDO CONEXION CON HUELLERO");
                }
                break;

            case "444432":
                Log.d(TAG,"Patron 4444");
                objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
                break;

            case "222232":
                Log.d(TAG,"Patron 2222");
                try{
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"DeleteAllTemplates",null);
                    util.sleep(250);
                }catch(Exception e){
                    Log.d("Autorizaciones","Error Enrolamiento: " + e.getMessage());
                }
                objSuprema.limpiarTramaSuprema();
                break;

            case "333332":
                Log.d(TAG,"Patron 3333");
                try{
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"NumberTemplate",null);
                    util.sleep(800);
                }catch(Exception e){
                    Log.d(TAG,"Error Enrolamiento: " + e.getMessage());
                }
                objSuprema.limpiarTramaSuprema();
                break;

            case "111132":
                isReplicating = true;
                break;

            case "42423123":
                Shell sh1 = new Shell();
                String comando1[]={"su","-c","busybox","ifconfig","eth0","172.20.1.119","up"};
                try{
                    sh1.exec(comando1);
                }catch(Exception e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
                }
                break;

            case "42421111":
                Shell sh2 = new Shell();
                String comando2[]={"su","-c","busybox","route","add","default","gw","172.20.0.1","eth0"};
                try{
                    sh2.exec(comando2);
                }catch(Exception e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
                }
                break;

            case "1324111":
                crearBD();          // Iniciar Base de Datos desde Cero
                break;

            default:
                break;
        }
    }

    /* ---------------------- FUNCION MARCACION MASTER --------------------------- */

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
            Log.d(TAG,"nroLectora: " + nroLectora);

            Autorizaciones autorizaciones = new Autorizaciones();
            if(lectora != null){

                Log.v(TAG, lectora);
                if (lectora == "HUELLA SUPREMA") {
                    //tarjeta = String.valueOf(util.convertHexToDecimal(tarjeta.substring(6,8) + tarjeta.substring(4,6) + tarjeta.substring(2,4) + tarjeta.substring(0,2)));
                } else {
                    if (lectora == "PROXIMIDAD CHINA"){
                        Log.d(TAG,"MUNDO");
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
                    autorizaciones = queriesMarcaciones.GestionarMarcaciones(tarjeta,idTerminal,Integer.parseInt(nroLectora),flag,fechahora.getFechahora());


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

    public void manageVisibilityKeyboard(boolean visible){
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

    public void manageVisibilityLayerMarcacion(boolean visible){
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
                    MarcacionMaster("01",tarjetaKey);

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
                        util.sleep(1000);
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
                        util.sleep(1000);
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
        }
    });


    /* --- CONTROL PRINCIPAL --- */
    public void controlGeneral(boolean c1,boolean c2, boolean c3) {

        if (c1 && c2 && c3){ // Si todos los seriales estan conectados podemos hacer algo
            if (isBooting){ isBooting = false; }
            Log.v(TAG,"SERIALES OK");
            ctrlThreadPantallaEnabled = true;
            ctrlThreadSyncMarcasEnabled = false;
            ctrlThreadSyncAutorizacionesEnabled = false;
            ctrlThreadSerial01Enabled = true;
            ctrlThreadSerial02Enabled = true;
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
                    detail = detail + "\nNO CONECTADO A ENERGÍA";
                }
                showMsgBoot(true,"Reconectando Interfaces \n"+detail+"]");
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
                                if (HARD_FAIL_01 || STATUS_CONNECT_03) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                //mostrarReparador(HARD_FAIL_01,true,HARD_FAIL_03);
                                break;
                            case 4:
                                controlGeneral(STATUS_CONNECT_01,STATUS_CONNECT_02,STATUS_CONNECT_03);
                                if (HARD_FAIL_01 || STATUS_CONNECT_02 || STATUS_CONNECT_03) {
                                    buttonWarning01.setVisibility(View.VISIBLE);
                                    buttonWarning02.setVisibility(View.VISIBLE);
                                }
                                //mostrarReparador(HARD_FAIL_01,HARD_FAIL_02,HARD_FAIL_03);
                                break;
                            default:
                                break;
                        }
                    }
                });


                util.sleep(1000);
            }
        }
    });

    Thread threadReplicado = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (ctrlThreadReplicadoEnabled){
                    if(isReplicating){
                        try{
                            Log.v(TAG,"REPLICADO INICIADO");
                            queriesPersonalTipolectoraBiometria.ReplicarBiometria();
                        }catch(Exception e){
                            Log.v(TAG,"");
                            isReplicating = false;
                        }

                    }else{
                        Log.v(TAG,"REPLICADO ESPERANDO");
                        if(tiempoPresente.getHours() == 1){
                            if(tiempoPresente.getMinutes() == 29){
                                if(tiempoPresente.getSeconds() > 50){

                                    try{
                                        objSuprema.writeToSuprema(btSocket02.getOutputStream(),"DeleteAllTemplates",null);
                                        util.sleep(1000);
                                    }catch(Exception e){
                                        Log.d(TAG,"Error Enrolamiento: " + e.getMessage());
                                    }
                                    objSuprema.limpiarTramaSuprema();

                                    DBManager db = new DBManager(ActivityPrincipal.this);
                                    db.execSQL("UPDATE PERSONAL_TIPOLECTORA_BIOMETRIA SET SINCRONIZADO = 0 WHERE 1 = 1;");

                                    Log.v(TAG,"HABILITANDO REPLICADO");
                                    isReplicating = true;
                                }

                            }
                        }
                    }
                }
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

                    try {
                        Date date = new Date();
                        tiempoPresente = date;
                        long dif = (tiempoPresente.getTime() - tiempoPasado.getTime()) / 1000;

                        Log.v("TEMPUS: ","dif normal: " + dif);

                        if (dif >= tiempoMarcacion) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    manageKeyboard(false);
                                    manageLayerMarcacion(false);

                                    Log.d("TEMPUS: ","Flag: " + flag);

                                    if ( MODO_EVENTO ){
                                        if((tiempoPresente.getTime() - tiempoPasado.getTime()) / 1000 >= tiempoFlag){
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

}
