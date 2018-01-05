package com.tempus.proyectos.tempusx;

// utimo

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Camera;

import com.tempus.proyectos.battery.BatteryLife;
import com.tempus.proyectos.bluetoothSerial.Bluetooth;
import com.tempus.proyectos.bluetoothSerial.BluetoothPair;
import com.tempus.proyectos.bluetoothSerial.BluetothPairingThread;
import com.tempus.proyectos.bluetoothSerial.MainArduino;
import com.tempus.proyectos.bluetoothSerial.MainEthernet;
import com.tempus.proyectos.bluetoothSerial.MainHandPunch;
import com.tempus.proyectos.bluetoothSerial.MainSuprema;
import com.tempus.proyectos.crash.TXExceptionHandler;
import com.tempus.proyectos.data.DBManager;
import com.tempus.proyectos.data.model.Parameters;
import com.tempus.proyectos.data.model.Servicios;
import com.tempus.proyectos.data.process.ProcessSync;
import com.tempus.proyectos.data.process.ProcessSyncDatetime;
import com.tempus.proyectos.data.process.ProcessSyncST;
import com.tempus.proyectos.data.process.ProcessSyncTS;
import com.tempus.proyectos.data.queries.QueriesLogTerminal;
import com.tempus.proyectos.data.queries.QueriesMarcaciones;
import com.tempus.proyectos.data.queries.QueriesParameters;
import com.tempus.proyectos.data.queries.QueriesPersonalTipolectoraBiometria;
import com.tempus.proyectos.data.queries.QueriesServicios;
import com.tempus.proyectos.data.tables.TableLogTerminal;
import com.tempus.proyectos.geolocation.GeoLocationListener;
import com.tempus.proyectos.log.Database;
import com.tempus.proyectos.log.LogManager;
import com.tempus.proyectos.picture.ResizePic;
import com.tempus.proyectos.threads.ThreadConnectSerial;
import com.tempus.proyectos.threads.ThreadHorariosRelay;
import com.tempus.proyectos.util.Connectivity;
import com.tempus.proyectos.util.Fechahora;
import com.tempus.proyectos.util.InternalFile;
import com.tempus.proyectos.util.Shell;
import com.tempus.proyectos.util.UserInterfaceM;
import com.tempus.proyectos.util.Utilities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ActivityPrincipal extends Activity {

    private static String TAG = "TX-AP";
    public static int versionSDK;

    int SCORE_MANO = 0;

    /* Variables Globales */
    String FLG_AUX = "";
    boolean esperando_mano = false;
    boolean fallo_mano = false;
    boolean marcacion_mano = false;

    boolean MARCACION_ACTIVA;
    int TIEMPO_ACTIVO;
    String MODO_MARCACION;
    String TEMPLATE = "";
    String INDICE = "";
    String NOMBRE_PERSONAL_MANO;

    LogManager logManager;

    ThreadConnectSerial threadConnectSerial;
    ThreadHorariosRelay threadHorariosRelay;

    int c = 100;

    boolean MODO_EVENTO = false;
    public static String BOTONES_EVENTO = "00000000";
    public static String TEXTO_BOTONES_EVENTO = "1,2,3,4,5,6,7,8";
    public static String VALOR_BOTONES_EVENTO = "001,002,003,004,005,006,007,008";
    public static boolean MODO_PRINTER = false;
    public static String VALUES_PRINTER_MESSAGE = "0,0;0,1;0,2;0,3;0,4;0,5";
    public static String PRINTER_MESSAGE = "";
    public static boolean MODO_REFRIGERIO = false;
    public static String MENSAJE_MARCACIONES = "MARCACION AUTORIZADA,;ERROR,NO TIENE PERMISO EN ESTA LECTORA;ERROR,ESTADO NO PERMITE MARCACION;MARCACION REPETIDA,;ERROR,TARJETA/BIOME NO REGISTRADA;ERROR,TARJETA/BIOME NO SE RECONOCE;ERROR,LECTORA NO HABILITADA";

    Date TIEMPO_PRESENTE_BT01;
    Date TIEMPO_PASADO_BT01;
    Date TIEMPO_PRESENTE_BT02;
    Date TIEMPO_PASADO_BT02;
    Date TIEMPO_PRESENTE_BT03;
    Date TIEMPO_PASADO_BT03;

    public static String MAC_BT_00 = "";
    public static String MAC_BT_01 = "";
    public static String MAC_BT_02 = "";
    public static String MAC_BT_03 = "";
    public static String PASS_BT_00 = "";
    public static String PASS_BT_01 = "";
    public static String PASS_BT_02 = "";
    public static String PASS_BT_03 = "";

    public static boolean STATUS_ETHERNET = false;
    public static boolean STATUS_CONNECT_01 = false;
    public static boolean STATUS_CONNECT_02 = false;
    public static boolean STATUS_CONNECT_03 = false;

    boolean HARD_FAIL_01 = false;
    boolean HARD_FAIL_02 = false;
    boolean HARD_FAIL_03 = false;

    boolean MODO_PATRON = false;

    String PATRON_SECRET = "";

    /* --- Battery Life --- */
    public static boolean isCharging;
    public static int levelBattery;
    public static int chargePlug;
    public int usbCharge;
    public int acCharge;

    /* --- ADC --- */
    public static double ExternalEnergy = -1;
    public static double UPSCharge = -1;
    public static double levelUPS = -1;
    public static int turnOnRelay01 = -1;
    public static int turnOnRelay02 = -1;
    public static int turnOnLan = -1;
    public static int turnOnAndroid = -1;
    public static int turnOnSuprema = -1;
    public static int turnOnLectorBarra = -1;
    public static int turnOnProximidad = -1;
    public static int turnOnExternalEnergy = -1;

    /* --- Variables UI --- */
    public static String parametersColorsUI = "";

    /* --- Variables Fotos --- */
    public static Camera camera;
    public static SurfaceView surfaceView;

    /* --- Variables Ethernet --- */
    public static ArrayList<String> parametersEthernet = new ArrayList<String>();
    public static ArrayList<String> parametersSock = new ArrayList<String>();

    /* --- Variables Marcaciones --- */
    public static String parametersInsertMarcaciones;
    public static int parameterMarcacionRepetida;
    public static boolean parameterFotoPersonal;

    public static String relay01 = "4e";
    public static String relay02 = "4e";
    public static String tiempoActivoRelay01 = "4e";
    public static String tiempoActivoRelay02 = "4e";

    /* --- Variables Servicios --- */
    public static String parametersWebService_01 = "";

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
    private boolean BT_01_IS_C = false;
    private boolean BT_02_IS_C = false;

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
    public static boolean iswithX;
    public static boolean isWriting;
    public static String huellaEnrollFlag;
    public static String huellaEnrollFlag1;
    public static String huellaEnrollFlag2;
    public static String huellaEnroll;
    public static String huellaEnroll1;
    public static String huellaEnroll2;
    public static String huellaDelete1;
    public static String huellaDeleteFlag1;
    public static String withx;
    public static String huellaWrite;
    public static String huellaWriteFlag;


    public static String activityActive;
    public static String idTerminal;

    public static boolean ctrlThreadPantallaEnabled;
    public static boolean ctrlThreadSyncMarcasEnabled;
    public static boolean ctrlThreadSyncAutorizacionesEnabled;
    public static boolean ctrlThreadSerial01Enabled;
    public static boolean ctrlThreadSerial02Enabled;
    public static boolean ctrlThreadSerial03Enabled;
    public static boolean ctrlThreadReplicadoEnabled;

    public static String flag;
    public static String flagPrint = "";

    public static boolean INTERFACE_WLAN;
    public static boolean INTERFACE_ETH;
    public static boolean INTERFACE_PPP;

    public static String macWlan;

    List<String> lectoras;

    String tarjetaKey;
    boolean visibleKey;
    String mNombre;
    String mTarjeta;
    String mMensajePrincipal;
    String mMensajeSecundario;
    String mFotoPersonal;
    File fileFotoPersonal;
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
    MainEthernet mainEthernet = new MainEthernet();
    BatteryLife batteryLife = new BatteryLife();
    Database database = new Database();


    /* --- Declaración de Componentes de la Interfaz --- */

    TextView txvFondo1;
    TextView txvFondo2;
    TextView txvBarraInf;
    TextView txvLinea1;
    TextView txvLinea2;
    TextView txvLinea3;
    TextView txvLinea4;


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

    ImageView imgFotoPersonal;
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

    TextView txvIdTerminalInfo;

    protected PowerManager.WakeLock mWakeLock;

    // Testing BT on off
    InternalFile internalFile = new InternalFile();

    private QueriesParameters queriesParameters;
    private QueriesLogTerminal queriesLogTerminal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        turnOnScreen();
        context = getApplicationContext();
        versionSDK = Build.VERSION.SDK_INT;
        queriesLogTerminal = new QueriesLogTerminal();

        try{
            Log.v(TAG,"versionSDK " + versionSDK);
            queriesParameters = new QueriesParameters(ActivityPrincipal.context);
            dbManager = new DBManager(ActivityPrincipal.context);

            //Set Parameters iniciales por carga de archivo
            Log.v(TAG, "SET TABLE PARAMETERS");
            //logManager.RegisterLogTXT("COMANDO: REINICIALIZAR BD");
            try {
                dbManager.startBackupBd(1,0);
                Thread.sleep(3000);
            } catch (Exception e) {
                Log.e(TAG, "SET TABLE PARAMETERS " + e.getMessage());
            }

            /* --- Variables Parameters Colors UI --- */
            try {
                parametersColorsUI = queriesParameters.idparameterToValue("COLORSUI");
                Log.v(TAG,"parametersColorsUI = " + parametersColorsUI);
            } catch (Exception e) {
                parametersColorsUI = "#cecece,#cecece,#cecece,#777777,#777777,#777777,#777777,#000000,#ffffff";
                Log.e(TAG, "parametersColorsUI " + e.getMessage());
            }

            /* --- Variables Parameters TABLE PARAMETERS --- */
            try{
                parametersInsertMarcaciones = queriesParameters.idparameterToValue("INSERTMARCACIONES");
            }catch (Exception e){
                parametersInsertMarcaciones = "10000000";
                Log.e(TAG,"parametersInsertMarcaciones " + e.getMessage());
            }
            Log.v(TAG,"parametersInsertMarcaciones = " + parametersInsertMarcaciones);

            try{
                parameterMarcacionRepetida = Integer.valueOf(queriesParameters.idparameterToValue("MARCACIONREPETIDA"));
            }catch (Exception e){
                parameterMarcacionRepetida = 0;
                Log.e(TAG,"parameterMarcacionRepetida " + e.getMessage());
            }
            Log.v(TAG,"parameterMarcacionRepetida = " + parameterMarcacionRepetida);

            try{
                parameterFotoPersonal = Boolean.valueOf(queriesParameters.idparameterToValue("FOTOPERSONAL"));
            }catch (Exception e){
                parameterFotoPersonal = false;
                Log.e(TAG,"parameterFotoPersonal " + e.getMessage());
            }
            Log.v(TAG,"parameterFotoPersonal = " + parameterFotoPersonal);

            try{
                parametersWebService_01 = queriesParameters.idparameterToValue("WEBSERVICE_01");
            }catch (Exception e){
                parametersWebService_01 = "";
                Log.e(TAG,"parametersWebService_01 " + e.getMessage());
            }
            Log.v(TAG,"parametersWebService_01 = " + parametersWebService_01);

            try {
                MODO_EVENTO = Boolean.valueOf(queriesParameters.idparameterToValue("MODO_EVENTO"));
            } catch (Exception e) {
                MODO_EVENTO = false;
                Log.e(TAG,"set MODO_EVENTO " + e.getMessage());
            }
            Log.v(TAG, "MODO_EVENTO = " + MODO_EVENTO);

            try {
                BOTONES_EVENTO = queriesParameters.idparameterToValue("BOTONES_EVENTO");
            } catch (Exception e) {
                BOTONES_EVENTO = "00000000";
                Log.e(TAG,"set BOTONES_EVENTO " + e.getMessage());
            }
            Log.v(TAG, "BOTONES_EVENTO = " + BOTONES_EVENTO);

            try {
                TEXTO_BOTONES_EVENTO = queriesParameters.idparameterToValue("TEXTO_BOTONES_EVENTO");
            } catch (Exception e) {
                TEXTO_BOTONES_EVENTO = "1,2,3,4,5,6,7,8";
                Log.e(TAG,"set TEXTO_BOTONES_EVENTO " + e.getMessage());
            }
            Log.v(TAG, "TEXTO_BOTONES_EVENTO = " + TEXTO_BOTONES_EVENTO);

            try {
                VALOR_BOTONES_EVENTO = queriesParameters.idparameterToValue("VALOR_BOTONES_EVENTO");
            } catch (Exception e) {
                VALOR_BOTONES_EVENTO = "001,002,003,004,005,006,007,008";
                Log.e(TAG,"set VALOR_BOTONES_EVENTO " + e.getMessage());
            }
            Log.v(TAG, "VALOR_BOTONES_EVENTO = " + VALOR_BOTONES_EVENTO);

            try {
                MODO_PRINTER = Boolean.valueOf(queriesParameters.idparameterToValue("MODO_PRINTER"));
            } catch (Exception e) {
                MODO_PRINTER = false;
                Log.e(TAG,"set MODO_PRINTER " + e.getMessage());
            }
            Log.v(TAG, "MODO_PRINTER = " + MODO_PRINTER);

            try {
                VALUES_PRINTER_MESSAGE = queriesParameters.idparameterToValue("VALUES_PRINTER_MESSAGE");
            } catch (Exception e) {
                VALUES_PRINTER_MESSAGE = "0,0;0,1;0,2;0,3;0,4;0,5";
                Log.e(TAG,"set VALUES_PRINTER_MESSAGE " + e.getMessage());
            }
            Log.v(TAG, "VALUES_PRINTER_MESSAGE = " + VALUES_PRINTER_MESSAGE);

            try {
                PRINTER_MESSAGE = queriesParameters.idparameterToValue("PRINTER_MESSAGE");
            } catch (Exception e) {
                PRINTER_MESSAGE = "";
                Log.e(TAG,"set PRINTER_MESSAGE " + e.getMessage());
            }
            Log.v(TAG, "PRINTER_MESSAGE = " + PRINTER_MESSAGE);

            try {
                MODO_REFRIGERIO = Boolean.valueOf(queriesParameters.idparameterToValue("MODO_REFRIGERIO"));
            } catch (Exception e) {
                MODO_REFRIGERIO = false;
                Log.e(TAG,"set MODO_REFRIGERIO " + e.getMessage());
            }
            Log.v(TAG, "MODO_REFRIGERIO = " + MODO_REFRIGERIO);

            try {
                MENSAJE_MARCACIONES = queriesParameters.idparameterToValue("MENSAJE_MARCACIONES");
            } catch (Exception e) {
                MENSAJE_MARCACIONES = "MARCACION AUTORIZADA,;ERROR,NO TIENE PERMISO EN ESTA LECTORA;ERROR,ESTADO NO PERMITE MARCACION;MARCACION REPETIDA,;ERROR,TARJETA/BIOME NO REGISTRADA;ERROR,TARJETA/BIOME NO SE RECONOCE;ERROR,LECTORA NO HABILITADA";
                Log.e(TAG,"set MENSAJE_MARCACIONES " + e.getMessage());
            }
            Log.v(TAG, "MENSAJE_MARCACIONES = " + MENSAJE_MARCACIONES);

            logManager = new LogManager();
            logManager.RegisterLogTXT("INICIO TEMPUSX");

            int id = android.os.Process.myPid();
            Log.wtf(TAG, "PID_ACTIVITIE " + String.valueOf(id));

            if (btSocket01 != null || btSocket02 != null) {
                Log.wtf(TAG,"OBJ_STATUS " + "EXISTE");
                //logManager.RegisterLogTXT("OBJ_STATUS EXISTE");
            } else {
                Log.wtf(TAG,"OBJ_STATUS " + "NO EXISTE");
            }


            // Inicialización nivel cero

            Log.v(TAG,"**********************************************01");
            activityActive = "Principal";


            Thread.setDefaultUncaughtExceptionHandler(new TXExceptionHandler(this));

            if (getIntent().getBooleanExtra("crash", false)) {
                Toast.makeText(this, "Restarting app after crash ... ", Toast.LENGTH_SHORT).show();
            }

            Log.v(TAG,"**********************************************02");


            // Inicializacion de componentes
            txvFondo1 = (TextView) findViewById(R.id.txvFondo1);
            txvFondo2 = (TextView) findViewById(R.id.txvFondo2);
            txvBarraInf = (TextView) findViewById(R.id.txvBarraInf);
            txvLinea1 = (TextView) findViewById(R.id.txvLinea1);
            txvLinea2 = (TextView) findViewById(R.id.txvLinea2);
            txvLinea3 = (TextView) findViewById(R.id.txvLinea3);
            txvLinea4 = (TextView) findViewById(R.id.txvLinea4);

            setBackgroundColorOnTextView(txvFondo1,parametersColorsUI.split(",")[0],"#cecece");
            setBackgroundColorOnTextView(txvFondo2,parametersColorsUI.split(",")[1],"#cecece");
            setBackgroundColorOnTextView(txvBarraInf,parametersColorsUI.split(",")[2],"#cecece");
            setBackgroundColorOnTextView(txvLinea1,parametersColorsUI.split(",")[3],"#777777");
            setBackgroundColorOnTextView(txvLinea2,parametersColorsUI.split(",")[4],"#777777");
            setBackgroundColorOnTextView(txvLinea3,parametersColorsUI.split(",")[5],"#777777");
            setBackgroundColorOnTextView(txvLinea4,parametersColorsUI.split(",")[6],"#777777");


            txvIdTerminalInfo = (TextView) findViewById(R.id.txvIdTerminalInfo);

            txcHora = (TextClock) findViewById(R.id.txcHora);

            txvFondoInicial = (TextView) findViewById(R.id.txvFondoInicial);
            txvTextoInicial = (TextView) findViewById(R.id.txvTextoInicial);
            pbrCargaInicial = (ProgressBar) findViewById(R.id.pbrCargaInicial);

            imgFotoPersonal = (ImageView) findViewById(R.id.imgFotoPersonal);
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

            setBackgroundColorOnTextView(txvMarcacionFondo,parametersColorsUI.split(",")[7],"#000000");
            setTextColorOnTextView(txvMarcacionNombre,parametersColorsUI.split(",")[8],"#ffffff");
            setTextColorOnTextView(txvMarcacionTarjeta,parametersColorsUI.split(",")[8],"#ffffff");
            setTextColorOnTextView(txvMarcacionMsjPrincipal,parametersColorsUI.split(",")[8],"#ffffff");
            setTextColorOnTextView(txvMarcacionMsjSecundario,parametersColorsUI.split(",")[8],"#ffffff");

            btnMaster = (ImageView) findViewById(R.id.btnMaster);
            setImageBitmapOnImageView(btnMaster,"/tempus/img/config/","logo.png");
            //+ "/tempus/img/config/" + "logo.png");

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



            /*
            try{
                Log.v(TAG,"btnMaster.setImageBitmap " + Environment.getExternalStorageDirectory().toString() + "/tempus/img/config/" + "logo.png");
                btnMaster.setImageBitmap(BitmapFactory.decodeFile(new  File(Environment.getExternalStorageDirectory().toString() + "/tempus/img/config/" + "logo.png").toString()));
            }catch (Exception e){
                Log.e(TAG,"btnMaster.setImageBitmap " + e.getMessage());
            }
            */

            Log.v(TAG,"**********************************************03");

            ui = new UserInterfaceM();
            util = new Utilities();
            fechahora = new Fechahora();
            connectivity = new Connectivity();

            Shell sh = new Shell();
            String params[] = {"su","-c","busybox ifconfig"};
            String cadena = sh.exec(params);
            macWlan = connectivity.getMacAddress(cadena,"wlan0");

            queriesPersonalTipolectoraBiometria = new QueriesPersonalTipolectoraBiometria(this);

            // Iniciar UI
            ui.initScreen(this);

            Log.v(TAG,"**********************************************04");
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

            Log.v(TAG,"**********************************************05");

            // Iniciar Rutinas en verdadero

            ProcessSyncTS processSyncTS = new ProcessSyncTS("Sync_Marcaciones_Biometrias");
            processSyncTS.start(this);
            Log.v(TAG,"**********************************************05-a");
            ProcessSyncST processSyncST = new ProcessSyncST("Sync_Autorizacion");
            processSyncST.start(this);
            Log.v(TAG,"**********************************************05-b");
            ProcessSyncDatetime processSyncDatetime = new ProcessSyncDatetime("Sync_Datetime");
            processSyncDatetime.start(this);
            Log.v(TAG,"**********************************************05-c");
            threadFechahora.start();
            threadStatusADC.start();
            Log.v(TAG,"**********************************************05-d");
            mainEthernet.startEthernetReading();
            mainEthernet.startEthernetExecuting();
            mainEthernet.startEthernetFixing();
            Log.v(TAG,"**********************************************05-e");


            batteryLife.startBatteryLifeReading();
            database.startDatabaseReading();
            Log.v(TAG,"**********************************************05-f");


            threadHorariosRelay = new ThreadHorariosRelay();
            threadHorariosRelay.startHorariosRelayReading();

            //threadTestingBTonoff.start();

            ResizePic resizePic = new ResizePic(null);
            resizePic.startResizeAllPictures();

            //MonitorApp monitorApp = new MonitorApp();
            //try{
            //    Log.v(TAG,"LG-MAPP monitorApp");
            //    //monitorApp.starLookErrorApp(ActivityPrincipal.this);
            //    monitorApp.execMonitorearApp(ActivityPrincipal.this);
            //    //LookErrorApp.start();
            //}catch (Exception e){
            //    Log.v(TAG,"monitorApp.starLookErrorApp " + e.getMessage());
            //}


            BluetoothPair bluetoothPair = new BluetoothPair(ActivityPrincipal.this);
            bluetoothPair.registerReceiver();
            new BluetothPairingThread(bluetoothPair).start();

            Log.v(TAG,"**********************************************06");

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

            Log.v(TAG,"**********************************************07");

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
                    flag = VALOR_BOTONES_EVENTO.split(",")[0];
                    flagPrint = btnEvent01.getText().toString();
                    Log.v(TAG, "Flg_Actividad: " + flag + " - " + "flagPrint: " + flagPrint);
                    txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                    actualizarFlag(flag, btnEvent01);
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 6;
                }
            });

            // Boton Evento 02
            btnEvent02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = VALOR_BOTONES_EVENTO.split(",")[1];
                    flagPrint = btnEvent02.getText().toString();
                    Log.v(TAG, "Flg_Actividad: " + flag + " - " + "flagPrint: " + flagPrint);
                    txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                    actualizarFlag(flag, btnEvent02);
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 6;
                }
            });

            // Boton Evento 03
            btnEvent03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = VALOR_BOTONES_EVENTO.split(",")[2];
                    flagPrint = btnEvent03.getText().toString();
                    Log.v(TAG, "Flg_Actividad: " + flag + " - " + "flagPrint: " + flagPrint);
                    txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                    actualizarFlag(flag, btnEvent03);
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 6;
                }
            });

            // Boton Evento 04
            btnEvent04.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = VALOR_BOTONES_EVENTO.split(",")[3];
                    flagPrint = btnEvent04.getText().toString();
                    Log.v(TAG, "Flg_Actividad: " + flag + " - " + "flagPrint: " + flagPrint);
                    txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                    actualizarFlag(flag, btnEvent04);
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 6;
                }
            });

            // Boton Evento 05
            btnEvent05.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = VALOR_BOTONES_EVENTO.split(",")[4];
                    flagPrint = btnEvent05.getText().toString();
                    Log.v(TAG, "Flg_Actividad: " + flag + " - " + "flagPrint: " + flagPrint);
                    txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                    actualizarFlag(flag, btnEvent05);
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 6;
                }
            });

            // Boton Evento 06
            btnEvent06.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = VALOR_BOTONES_EVENTO.split(",")[5];
                    flagPrint = btnEvent06.getText().toString();
                    Log.v(TAG, "Flg_Actividad: " + flag + " - " + "flagPrint: " + flagPrint);
                    txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                    actualizarFlag(flag, btnEvent06);
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 6;
                }
            });

            // Boton Evento 07
            btnEvent07.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = VALOR_BOTONES_EVENTO.split(",")[6];
                    flagPrint = btnEvent07.getText().toString();
                    Log.v(TAG, "Flg_Actividad: " + flag + " - " + "flagPrint: " + flagPrint);
                    txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                    actualizarFlag(flag, btnEvent07);
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 6;
                }
            });

            // Boton Evento 08
            btnEvent08.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = VALOR_BOTONES_EVENTO.split(",")[7];
                    flagPrint = btnEvent08.getText().toString();
                    Log.v(TAG, "Flg_Actividad: " + flag + " - " + "flagPrint: " + flagPrint);
                    txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                    actualizarFlag(flag, btnEvent08);
                    //Date date = new Date();
                    //tiempoPasado = date;

                    TIEMPO_ACTIVO = 6;
                }
            });

            Log.v(TAG,"**********************************************08");

            // Boton de Acceso a teclado
            btnAccessKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == null || flag == "") {

                        //Mofidicado el 28/09/2017 para cambiar el toast 'DEBE SELECCIONAR UN EVENTO'
                        //El nuevo mensaje es 'debe selecionar evento' y aparece en toda la pantalla
                        //Fondo negro y letras blancas, con un tiempo activo = 2 segundos

                        MarcacionMaster("TECLADO","");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                turnOnScreen();
                                manageLayerMarcacion(true);
                                actualizarFlag(null,null);
                            }
                        });



                        /*
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
                        */

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
                    Log.d(TAG,"PATRON_SECRET " + PATRON_SECRET);
                }
            });

            txvSecret02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccesoSecreto("2");
                    Log.d(TAG,"PATRON_SECRET" + PATRON_SECRET);
                }
            });

            txvSecret03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccesoSecreto("3");
                    Log.d(TAG,"PATRON_SECRET" + PATRON_SECRET);
                }
            });



            /* --- Variables Fotos --- */
            try{
                Log.v(TAG,"camera iniciando");
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                Log.v(TAG,"surfaceView iniciando");
                surfaceView = new SurfaceView(ActivityPrincipal.context);
                Log.v(TAG,"camera/surfaceView iniciado");
            }catch (Exception e){
                Log.e(TAG,"camera/surfaceView " + e.getMessage());
            }

        }catch (Exception e){
            Log.e(TAG,"onCreate " + e.getMessage());

        }

        try{
            Log.v(TAG,"queriesLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG,"Inicio completado",""));
        }catch (Exception e){
            Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
        }


    }

    @Override
    public void onResume(){
        try {
            super.onResume();
            activityActive = "Principal";
            ActivityPrincipal.objSuprema.writeToSuprema(btSocket02.getOutputStream(),"FreeScanOn",null);
        } catch(Exception e) {
            Log.e(TAG,"ERROR ESTABLECIENDO CONEXION CON HUELLERO");
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try{
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
        }catch (Exception e){
            Log.e(TAG,"onTouchEvent " + e.getMessage());
            return true;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        try{
            super.onWindowFocusChanged(hasFocus);
            ui.initScreen(this);
        }catch (Exception e){
            Log.e(TAG,"onWindowFocusChanged " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            manageLayerMarcacion(false);
            manageAccessButtons(false);

            areaAccessEnabled = false;

            if (requestCode == 1) {
                if (resultCode == ActivityLogin.RESULT_OK) {
                    Bundle b = data.getExtras();
                    if (b != null) {
                        Log.v(TAG,"TEMPUS: " + String.valueOf(b.getSerializable("llave")));
                    }
                } else if (resultCode == 0) {
                    System.out.println("RESULT CANCELLED");
                }
            }
        }catch (Exception e){
            Log.e(TAG,"onActivityResult " + e.getMessage());
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
                                Log.d(TAG,"SYSTEM_SUPERADMIN " + "INICIANDO");
                                manageAccessButtons(false);
                                Intent intent01 = new Intent(ActivityPrincipal.this, ActivitySuperadmin.class);
                                startActivityForResult(intent01, 1);
                                patronAcceso = "";
                                hideSoftKeyboard(promptsView);
                            } catch(Exception e) {
                                Log.e(TAG,"hideSoftKeyboard " + e.getMessage());
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
                            Log.e(TAG,"hideSoftKeyboard" + e.getMessage());
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
            Log.d(TAG,"SYSTEM_MAIN hideNavBar1 " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG,"SYSTEM_MAIN hideNavBar2" + e.getMessage());
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
            Log.i(TAG,"TEMPUS: " + "No se puede reiniciar!!!!!!!!!!!!!!!!", ex);
        }

    }



    public void crearBD() {
        dbManager = new DBManager(context);
        dbManager.all("1,1,1,1,1,1");
    }

    public void CargarDatosInicialesUPD() {

        try {
            idTerminal = dbManager.valexecSQL("SELECT IDTERMINAL FROM TERMINAL");
            //dbManager.execSQL("INSERT INTO PARAMETERS(IDPARAMETER,PARAMETER,VALUE,SUBPARAMETER,ENABLE,FECHA_HORA_SINC) VALUES('MARCACIONREPETIDA','','10','','0','" + fechahora.getFechahora() + "');");
            //dbManager.execSQL("UPDATE PARAMETERS SET VALUE = 'RELAY_01,0,00;RELAY_02,0,00' WHERE IDPARAMETER = 'RELAYS';");
            //dbManager.execSQL("UPDATE MARCACIONES SET SINCRONIZADO = 0");
            //dbManager.execSQL("UPDATE PERSONAL_TIPOLECTORA_BIOMETRIA SET SINCRONIZADO = 2 WHERE SINCRONIZADO = 4");
            //dbManager.execSQL("DELETE FROM LOG_TERMINAL");
            //dbManager.execSQL("DELETE FROM PERSONAL_TIPOLECTORA_BIOMETRIA");
            //dbManager.execSQL("UPDATE PERSONAL_TIPOLECTORA_BIOMETRIA SET SINCRONIZADO = 0 WHERE SINCRONIZADO = 1");
            //dbManager.execSQL("DELETE FROM PERSONAL");
            //dbManager.execSQL("DELETE FROM LOG_TERMINAL WHERE VALUE LIKE '% & %' AND TAG = 'LOG-DB'");
            dbManager.execSQL(TableLogTerminal.CREATE_TABLE);
        } catch(Exception e) {
            e.printStackTrace();
        }

        txvIdTerminalInfo.setText(idTerminal);

        Parameters parameters;

        try {
            TIPO_TERMINAL = Integer.valueOf(queriesParameters.idparameterToValue("TIPO_TERMINAL"));
            Log.v(TAG, "TIPO_TERMINAL=" + TIPO_TERMINAL);
        } catch (Exception e) {
            Log.e(TAG,"set TIPO_TERMINAL " + e.getMessage());
            TIPO_TERMINAL = 2;
        }

        try {
            String macypass = "00:00:00:00:00:00,1234";
            macypass = queriesParameters.idparameterToValue("BT_00");
            MAC_BT_00 = macypass.split(",")[0];
            PASS_BT_00 = macypass.split(",")[1];
            Log.v(TAG, "MAC_BT_00=" + MAC_BT_00 + "," + PASS_BT_00);
        } catch (Exception e) {
            Log.e(TAG,"set MAC_BT_00 " + e.getMessage());
            MAC_BT_00 = "00:00:00:00:00:00";
            PASS_BT_00 = "1234";
        }

        try {
            String macypass = "00:00:00:00:00:00,1234";
            macypass = queriesParameters.idparameterToValue("BT_01");
            MAC_BT_01 = macypass.split(",")[0];
            PASS_BT_01 = macypass.split(",")[1];
            Log.v(TAG, "MAC_BT_01=" + MAC_BT_01 + "," + PASS_BT_01);
        } catch (Exception e) {
            Log.e(TAG,"set MAC_BT_01 " + e.getMessage());
            MAC_BT_01 = "00:00:00:00:00:00";
            PASS_BT_01 = "1234";
        }

        try {
            String macypass = "00:00:00:00:00:00,1234";
            macypass = queriesParameters.idparameterToValue("BT_02");
            MAC_BT_02 = macypass.split(",")[0];
            PASS_BT_02 = macypass.split(",")[1];
            Log.v(TAG, "MAC_BT_02=" + MAC_BT_02 + "," + PASS_BT_02);
        } catch (Exception e) {
            Log.e(TAG,"set MAC_BT_02 " + e.getMessage());
            MAC_BT_02 = "00:00:00:00:00:00";
            PASS_BT_02 = "1234";
        }

        try {
            String macypass = "00:00:00:00:00:00,1234";
            macypass = queriesParameters.idparameterToValue("BT_03");
            MAC_BT_03 = macypass.split(",")[0];
            PASS_BT_03 = macypass.split(",")[1];
            Log.v(TAG, "MAC_BT_03=" + MAC_BT_03 + "," + PASS_BT_03);
        } catch (Exception e) {
            Log.e(TAG,"set MAC_BT_03 " + e.getMessage());
            MAC_BT_03 = "00:00:00:00:00:00";
            PASS_BT_03 = "1234";
        }

        /*
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
        */

        try {
            parameters = queriesParameters.selectParameter("TIPO_TERMINAL");
            TIPO_TERMINAL = Integer.parseInt(parameters.getValue());
            Log.d(TAG, "CargarDatosIniciales > TIPO_TERMINAL = OK");
        } catch (Exception e) {
            TIPO_TERMINAL = 2;
        }

        try {
            parameters = queriesParameters.selectParameter("INTERFACE_ETH");
            String enable_if01 = parameters.getValue();
            if (enable_if01 == "0") {
                INTERFACE_ETH = false;
            } else {
                INTERFACE_ETH = true;
            }
            Log.d(TAG, "CargarDatosIniciales > INTERFACE_ETH = OK " + "(" + INTERFACE_ETH + ")");
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
            Log.d(TAG, "CargarDatosIniciales > INTERFACE_WLAN = OK " + "(" + INTERFACE_WLAN + ")");
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
            Log.d(TAG, "CargarDatosIniciales > INTERFACE_PPP = OK " + "(" + INTERFACE_PPP + ")");
        } catch (Exception e) {
            INTERFACE_PPP = false;
        }

        MARCACION_ACTIVA = false;
        TIEMPO_ACTIVO = 0;
        MODO_MARCACION = "";
        //MAC_BTS

        //Ethernet test
        //MAC_BT_00 = "20:16:08:10:60:93"; //5555

        // CARRION 02
        //MAC_BT_01 = "98:D3:34:90:87:DC"; // hc 06
        //MAC_BT_02 = "00:12:03:16:02:08"; // linvor
        //MAC_BT_03 = "00:00:00:00:00:00";

        // CARRION ID 12 02/10/2017
        //MAC_BT_00 = "00:00:00:00:00:00";
        //MAC_BT_01 = "20:17:05:23:47:80";
        //MAC_BT_02 = "20:17:05:23:47:83";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // PUERTA ANTIGUO
        //MAC_BT_01 = "20:16:08:04:87:50";
        //MAC_BT_02 = "20:16:08:10:62:98";
        //MAC_BT_03 = "00:00:00:00:00:00";

        //RANSA 1 - 14:1F:78:24:1A:31
        //MAC_BT_01 = "20:16:08:10:42:63"; //ARDUINO
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "20:16:08:10:46:09";

        //RANSA 2 - 14:1F:78:86:2F:9C
        //MAC_BT_01 = "20:16:08:10:64:87"; //ARDUINO
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "20:16:08:10:65:94";

        //RANSA 3 - 14:1F:78:86:2F:B1
        //MAC_BT_01 = "20:16:08:10:58:40"; //ARDUINO
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "20:16:08:10:58:52";

        //RANSA BACKUP -
        //MAC_BT_01 = "20:16:08:10:57:20";
        //MAC_BT_02 = "00:00:00:00:00:00";
        //MAC_BT_03 = "20:16:08:10:60:02";

        //CLINICA INTERNACIONAL 1 (2)
        //MAC_BT_01 = "00:12:06:04:99:06";
        //MAC_BT_02 = "00:12:06:04:98:90";
        //MAC_BT_03 = "00:00:00:00:00:00";

        //CLINICA INTERNACIONAL TEST (1)
        //MAC_BT_00 = "00:21:13:01:7E:8F"; //1234
        //MAC_BT_02 = "20:16:08:10:68:89";
        //MAC_BT_01 = "20:16:08:10:63:74";
        //MAC_BT_03 = "00:00:00:00:00:00";

        //SHOUGANG
        //MAC_BT_00 = "00:21:13:00:CC:2D"; //1234
        //MAC_BT_02 = "00:15:83:35:7A:66";
        //MAC_BT_01 = "98:D3:34:90:88:89";
        //MAC_BT_03 = "00:00:00:00:00:00";
        // new bt
        //MAC_BT_00 = "00:21:13:00:CC:2D";
        //MAC_BT_01 = "20:16:06:30:84:85";
        //MAC_BT_02 = "20:16:09:21:44:42";
        //MAC_BT_03 = "00:00:00:00:00:00";


        //DIRESA 101 7p
        //MAC_BT_01 = "20:16:08:10:45:37";
        //MAC_BT_02 = "20:16:08:10:45:28";
        //MAC_BT_03 = "00:00:00:00:00:00";

        //DIRESA 100 7p
        //MAC_BT_01 = "20:16:08:10:62:10";
        //MAC_BT_02 = "20:16:08:10:64:43";
        //MAC_BT_03 = "00:00:00:00:00:00";

        //DIRESA 101 4p
        //MAC_BT_01 = "20:16:08:10:83:58";
        //MAC_BT_02 = "20:16:08:10:60:73";
        //MAC_BT_03 = "00:00:00:00:00:00";

        //DIRESA 100 4p
        //MAC_BT_01 = "20:16:08:10:42:38";
        //MAC_BT_02 = "20:16:08:09:04:41";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // CLINICA INTERNACIONAL - PUERTA (NUEVA VERSION)
        // old
        //MAC_BT_00 = "00:21:13:01:7E:47";
        //MAC_BT_01 = "20:16:09:21:89:49";
        //MAC_BT_02 = "20:16:07:14:10:45";
        //MAC_BT_03 = "00:00:00:00:00:00";
        // new
        //MAC_BT_00 = "00:21:13:01:7E:47";
        //MAC_BT_01 = "20:16:08:10:63:74";
        //MAC_BT_02 = "20:16:08:10:68:89";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // ARIS TESTER
        //MAC_BT_00 = "00:00:00:00:00:00";
        //MAC_BT_01 = "20:16:07:14:12:92";
        //MAC_BT_02 = "20:16:07:18:33:38"; //
        //MAC_BT_03 = "00:00:00:00:00:00";

        // DIRESA TEST BATERIA
        //MAC_BT_00 = "00:21:13:01:7A:CB"; //1234
        //MAC_BT_01 = "00:12:06:04:81:46";
        //MAC_BT_02 = "00:12:06:04:80:14";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // ARIS 2 (SECURITAS)
        //MAC_BT_00 = "00:00:00:00:00:00";
        //MAC_BT_01 = "20:16:08:04:87:50";
        //MAC_BT_02 = "20:16:08:10:62:98";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // VENTAS DEMO
        //MAC_BT_00 = "00:00:00:00:00:00";
        //MAC_BT_01 = "20:16:08:04:87:50";
        //MAC_BT_02 = "20:16:08:10:62:98";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // ARIS 1 PULSADORES Y RELAY (DESARROLLADOR)
        //MAC_BT_00 = "00:21:13:01:7D:B9";
        //MAC_BT_01 = "20:16:08:10:64:50";
        //MAC_BT_02 = "20:16:08:10:65:26";
        //MAC_BT_03 = "00:00:00:00:00:00";


        // ARIS ID=1 PULSADORES Y RELAY (OFICIAL)
        //MAC_BT_00 = "00:21:13:01:7E:10";
        //MAC_BT_01 = "20:16:07:14:09:92";
        //MAC_BT_02 = "20:16:07:18:49:76";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // ARIS ID=2 PULSADORES Y RELAY (OFICIAL)
        //MAC_BT_00 = "00:21:13:01:7F:1A";
        //MAC_BT_01 = "20:16:06:30:84:11";
        //MAC_BT_02 = "20:16:06:30:86:16";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // ARIS ID=3 PULSADORES Y RELAY (OFICIAL)
        //MAC_BT_00 = "00:21:13:01:7D:63";
        //MAC_BT_01 = "20:16:09:21:91:21";
        //MAC_BT_02 = "20:16:07:18:32:13";
        //MAC_BT_03 = "00:00:00:00:00:00";

        // ARIS ID=4 PULSADORES Y RELAY (OFICIAL)
        //MAC_BT_00 = "00:21:13:01:7F:25";
        //MAC_BT_01 = "20:16:08:10:67:76";
        //MAC_BT_02 = "20:16:08:10:58:54";
        //MAC_BT_03 = "00:00:00:00:00:00";


        //MODO_EVENTO = true;
        //TIPO_TERMINAL = 2;
        INTERFACE_ETH = true;
        INTERFACE_WLAN = true;
        INTERFACE_PPP = true;
        SCORE_MANO = 120;

        if (MODO_EVENTO) {

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
        lectoras.add("PROXIMIDAD HID");

        HabilitarBluetooth();
        HabilitarBluetoothEthernet();

    }

    public void InicializarModoEvento() {
        if (BOTONES_EVENTO.substring(0,1).equalsIgnoreCase("1")) {
            btnEvent01.setVisibility(View.VISIBLE);
            btnEvent01.setText(TEXTO_BOTONES_EVENTO.split(",")[0]);
        } else {
            btnEvent01.setVisibility(View.GONE);
        }

        if (BOTONES_EVENTO.substring(1,2).equalsIgnoreCase("1")) {
            btnEvent02.setVisibility(View.VISIBLE);
            btnEvent02.setText(TEXTO_BOTONES_EVENTO.split(",")[1]);
        } else {
            btnEvent02.setVisibility(View.GONE);
        }

        if (BOTONES_EVENTO.substring(2,3).equalsIgnoreCase("1")) {
            btnEvent03.setVisibility(View.VISIBLE);
            btnEvent03.setText(TEXTO_BOTONES_EVENTO.split(",")[2]);
        } else {
            btnEvent03.setVisibility(View.GONE);
        }

        if (BOTONES_EVENTO.substring(3,4).equalsIgnoreCase("1")) {
            btnEvent04.setVisibility(View.VISIBLE);
            btnEvent04.setText(TEXTO_BOTONES_EVENTO.split(",")[3]);
        } else {
            btnEvent04.setVisibility(View.GONE);
        }

        if (BOTONES_EVENTO.substring(4,5).equalsIgnoreCase("1")) {
            btnEvent05.setVisibility(View.VISIBLE);
            btnEvent05.setText(TEXTO_BOTONES_EVENTO.split(",")[4]);
        } else {
            btnEvent05.setVisibility(View.GONE);
        }

        if (BOTONES_EVENTO.substring(5,6).equalsIgnoreCase("1")) {
            btnEvent06.setVisibility(View.VISIBLE);
            btnEvent06.setText(TEXTO_BOTONES_EVENTO.split(",")[5]);
        } else {
            btnEvent06.setVisibility(View.GONE);
        }

        if (BOTONES_EVENTO.substring(6,7).equalsIgnoreCase("1")) {
            btnEvent07.setVisibility(View.VISIBLE);
            btnEvent07.setText(TEXTO_BOTONES_EVENTO.split(",")[6]);
        } else {
            btnEvent07.setVisibility(View.GONE);
        }

        if (BOTONES_EVENTO.substring(7).equalsIgnoreCase("1")) {
            btnEvent08.setVisibility(View.VISIBLE);
            btnEvent08.setText(TEXTO_BOTONES_EVENTO.split(",")[7]);
        } else {
            btnEvent08.setVisibility(View.GONE);
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
            iswithX = false;
            isWriting = false;

            huellaEnrollFlag = "";
            huellaEnrollFlag1 = "";
            huellaEnrollFlag2 = "";
            huellaDeleteFlag1 = "";
            huellaWriteFlag = "";

            huellaEnroll = "";
            huellaEnroll1 = "";
            huellaEnroll2 = "";
            huellaDelete1 = "";
            huellaWrite = "";
            withx = "";
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
            Log.e(TAG,"restartBluetooth disable " + e.getMessage());
        }

        BluetoothAdapter.getDefaultAdapter().enable();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e(TAG,"restartBluetooth enable " + e.getMessage());
        }
    }



    /* ---------------------- CONTROL DE ACCESO POR PATRON ------------------------*/

    public void AccessManager(String n){
        patronAcceso = patronAcceso + n;

        switch(patronAcceso) {
            case "123432":
                Log.d(TAG, "SYSTEM_MAIN_INSTRUCTION " + "COMANDO: INGRESAR LOGIN");
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
                    Log.e(TAG,"ERROR_SYSTEM_MAIN: " + "COMANDO: INGRESAR LOGIN -> " + e.getMessage());
                }
                Toast.makeText(ActivityPrincipal.this,"Ingreso a Login",Toast.LENGTH_SHORT).show();
                break;

            case "444432":
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: SUPREMA (CANCEL)");
                //logManager.RegisterLogTXT("COMANDO: SUPREMA (CANCEL)");
                try {
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
                    Toast.makeText(ActivityPrincipal.this,"Cancel Suprema",Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Log.e(TAG, "ERROR_SYSTEM_MAIN: " + "COMANDO: SUPREMA (CANCEL) -> " + e.getMessage());
                }
                break;

            case "222232":
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: SUPREMA (DELETE_ALL_TEMPLATES)");
                //logManager.RegisterLogTXT("COMANDO: SUPREMA (DELETE_ALL_TEMPLATES)");
                try{
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"DeleteAllTemplates",null);
                    Toast.makeText(ActivityPrincipal.this,"Eliminando Huellas",Toast.LENGTH_SHORT).show();
                    util.sleep(250);
                }catch(Exception e){
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: SUPREMA (DELETE_ALL_TEMPLATES) -> " + e.getMessage());
                }
                objSuprema.limpiarTramaSuprema();
                break;

            case "333332":
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: SUPREMA (NUMBER_TEMPLATES)");
                //logManager.RegisterLogTXT("COMANDO: SUPREMA (NUMBER_TEMPLATES)");
                try{
                    objSuprema.writeToSuprema(btSocket02.getOutputStream(),"NumberTemplate",null);
                    util.sleep(800);
                    objSuprema.limpiarTramaSuprema();
                    Toast.makeText(ActivityPrincipal.this,"Numero de Templates",Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: SUPREMA (NUMBER_TEMPLATES) -> " + e.getMessage());
                }

                break;

            case "111132":
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: HABILITAR REPLICADO");
                //logManager.RegisterLogTXT("COMANDO: HABILITAR REPLICADO");
                isReplicating = true;
                Toast.makeText(ActivityPrincipal.this,"Iniciando Replicado",Toast.LENGTH_SHORT).show();
                break;

            case "1324111":
                Log.d(TAG, "SYSTEM_MAIN_INSTRUCTION " + "COMANDO: REINICIALIZAR BD");
                //logManager.RegisterLogTXT("COMANDO: REINICIALIZAR BD");
                try {
                    crearBD();
                    Toast.makeText(ActivityPrincipal.this,"Creación de base de datos",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "ERROR_SYSTEM_MAIN " + "COMANDO: REINICIALIZAR BD -> " + e.getMessage());
                }
                break;

            case "33334334":
                // OTG (QUITAR CARGA)
                Log.d(TAG, "SYSTEM_MAIN_INSTRUCTION " + "COMANDO: OTG - QUITAR CARGA");
                //logManager.RegisterLogTXT("COMANDO: OTG - QUITAR CARGA");
                try {
                    AdministrarOTG(btSocket01.getOutputStream(),true);
                } catch (Exception e) {
                    Log.e(TAG, "ERROR_SYSTEM_MAIN " + "COMANDO: OTG - QUITAR CARGA -> " + e.getMessage());
                }

                break;
            case "33334331":
                // CARGAR
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: OTG - CARGAR");
                //logManager.RegisterLogTXT("COMANDO: OTG - CARGAR");
                try {
                    AdministrarOTG(btSocket01.getOutputStream(),false);
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: OTG - CARGAR -> " + e.getMessage());
                }
                break;

            case "113322443241321": // OCULTAR SYSTEMUI
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: OCULTAR SYSTEMUI");
                //logManager.RegisterLogTXT("COMANDO: OCULTAR SYSTEMUI");
                try {
                    if(versionSDK >= 23){
                        Log.v(TAG,"wm overscan -50,0,-50,0");
                        runShellCommand("wm overscan -50,0,-50,0 \n");
                    }else{
                        Log.v(TAG,"hideNavigationBar true");
                        hideNavigationBar(true);
                    }
                    Toast.makeText(ActivityPrincipal.this,"Modo Cliente",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: OCULTAR SYSTEMUI -> " + e.getMessage());
                }
                break;

            case "113322443241322": // MOSTRAR SYSTEMUI
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: MOSTRAR SYSTEMUI");
                //logManager.RegisterLogTXT("COMANDO: MOSTRAR SYSTEMUI");
                try {
                    if(versionSDK >= 23){
                        Log.v(TAG,"wm overscan reset");
                        runShellCommand("wm overscan reset \n");
                    }else{
                        Log.v(TAG,"hideNavigationBar false");
                        hideNavigationBar(false);
                    }
                    Toast.makeText(ActivityPrincipal.this,"Modo Desarrollo",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: MOSTRAR SYSTEMUI -> " + e.getMessage());
                }
                break;

            case "113322443241323": // REINICIAR
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: REINICIAR");
                //logManager.RegisterLogTXT("COMANDO: REINICIAR");
                try {
                    Toast.makeText(ActivityPrincipal.this,"Reiniciar Terminal",Toast.LENGTH_SHORT).show();
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: REINICIAR -> " + e.getMessage());
                }
                break;

            case "113322443241324": // APAGAR
                Log.v(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: APAGAR");
                //logManager.RegisterLogTXT("COMANDO: APAGAR");
                try {
                    Toast.makeText(ActivityPrincipal.this,"Apagar Terminal",Toast.LENGTH_SHORT).show();
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: APAGAR -> " + e.getMessage());
                }
                break;

            case "113322443241311": // ABRIR CONFIGURACION DE ANDROID
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: CONFIGURACION DE ANDROID");
                //logManager.RegisterLogTXT("COMANDO: CONFIGURACION DE ANDROID");
                try {
                    Toast.makeText(ActivityPrincipal.this,"Configurar dispositivo",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    Intent startMain = getPackageManager().getLaunchIntentForPackage("com.tempus.ecernar.overlaywifi");
                    startActivity(startMain);
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: CONFIGURACION DE ANDROID -> " + e.getMessage());
                }

                break;

            case "41444414":
                Log.d(TAG,"SYSTEM_MAIN_INSTRUCTION " + "COMANDO: APAGAR");
                //logManager.RegisterLogTXT("COMANDO: CONFIGURACION DE ANDROID");
                try {
                    ShutdownArduino(btSocket01.getOutputStream());
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: CONFIGURACION DE ANDROID -> " + e.getMessage());
                }

                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: APAGAR -> " + e.getMessage());
                }

                break;

            case "4411":
                //QueriesMarcaciones queriesMarcaciones = new QueriesMarcaciones(context);
                //QueriesParameters queriesParameters = new QueriesParameters(context);
                //queriesParameters.poblar();
                //queriesMarcaciones.ModoMarcacion(".#$%&$%","1",2,"127",fechahora.getFechahora(),"");
                //URRUTIA MENDOZA G,46388059,MARCACION AUTORIZADA,,10,DNI_MANO

                //queriesMarcaciones.ModoMarcacion("1315","1",10,"127",fechahora.getFechahora(),"DNI_MANO");
                //URRUTIA MENDOZA G,1315,MARCACION AUTORIZADA,,0,DNI_MANO

                //Log.v(TAG,"BTS-MAET ..............................................");
                //MainEthernet mainEthernet = new MainEthernet();
                //mainEthernet.testerLlamadas();


                /*
                Log.v(TAG,"AsyncTask---" + " Estado: " + ethernetRead.getStatus());

                try{
                    if(ethernetRead.getStatus() == AsyncTask.Status.PENDING){
                        ethernetRead.execute();
                        Log.v(TAG,"AsyncTask---" + " Ejecutando task (PENDING)");
                    }else if(ethernetRead.getStatus() == AsyncTask.Status.RUNNING){
                        ethernetRead.cancel(true);
                        Log.v(TAG,"AsyncTask---" + " Cancelando task");
                    }else if(ethernetRead.getStatus() == AsyncTask.Status.FINISHED){
                        ethernetRead = new EthernetRead();
                        ethernetRead.execute();
                        Log.v(TAG,"AsyncTask---" + " Ejecutando task (FINISHED)");
                    }
                }catch(Exception e){
                    Log.e(TAG,"AsyncTask---" + "Error " + e.getMessage());
                }
                */

                /*
                Log.v(TAG,"BTS-MAET ..............................................");
                try{
                    //ProcessSyncEthernet processSyncEthernet = new ProcessSyncEthernet();
                    //processSyncEthernet.execute();
                    mainEthernet.startEthernetReading();
                }catch(Exception e){
                    Log.e(TAG,"BTS-MAET " + e.getMessage());
                }
                */


                /*
                mysql mysql = new mysql();
                try{
                    mysql.conectar();
                    mysql.Consulta();
                    //ArrayAdapter adapter = new ArrayAdapter (ActivityPrincipal.context,android.R.layout.simple_spinner_item,mysql.Consulta());
                    //spinner.setAdapter(adapter);
                }catch(Exception e){

                }
                */

                /*
                Log.v(TAG,"LLAMADAS JSON");
                ProcessSync processSync = new ProcessSync();
                processSync.ProcessLlamadas(context);
                */

                //Log.v(TAG,"BT " + "Bluetooth");
                //btSocket01.closeBT();


                Log.d(TAG, "SYSTEM_MAIN_INSTRUCTION " + "COMANDO: BACKUP RESTARING BD");
                //logManager.RegisterLogTXT("COMANDO: REINICIALIZAR BD");
                try {
                    dbManager.startBackupBd(2,0);
                    Toast.makeText(ActivityPrincipal.this,"Ejecutar archivo SQL",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "ERROR_SYSTEM_MAIN " + "COMANDO: BACKUP RESTARING BD -> " + e.getMessage());
                }

                /*
                Log.v(TAG,"Prueba de Aplicacion no responde");
                try{
                    Thread.sleep(30000);
                    while(true){
                    }
                }catch (Exception e){
                    Log.e(TAG,"Prueba de Aplicacion no responde");
                }
                */

                //ProcessSyncUSB processSyncUSB = new ProcessSyncUSB();
                //processSyncUSB.startOTGOnOff();

                break;

            case "4422":
                try{
                    //244F4158410013443030303030303030303030303030303004A441
                    //24 4F 41 58 41 00 13 35 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 04 95 41
                    //Log.v(TAG,"Calcular Checksum <<<<" + util.getChecksum("00000602000046530000060200017479000007020000016c000007020001b163000000040000885e000000040001625d0000040500006b67000004050001ee5d0000060500008a65000006050001f459000007050000a266000007050001665d000000060000e060000000060001975f000007060000655d000007060001af5c00000707000048610000070700015b6b000004090000785c000004090001975d0000070900009a5a000007090001a3620000001000003d70000000100001e95d0000071000002a590000071000010d5f000007130000045d000007130001b05e000000140000ce5a000000140001a45c000003140000ef68000003140001d74e000000150000946e000000150001616a000007160000e85e000007160001ad660000071700002d4a0000071700014959000000180000c1760000001800014d6d000006180000bb69000006180001355400000718000032600000071800010c5a00000721000013770000072100015f59000007220000ef68000007220001b75a000003230000925e000003230001415e0000072600000f5d0000072600012d5d000006350000f3730000063500016465000006370000b45e000006370001175c0000064600009168000006460001845600000547000000630000054700013167000001480000b1610000065000006253000006500001d9610000065200004955000006520001b057000006550000a15a0000065500014e52000002640000236d000002640001c15c0000066500007a61000006650001bd580000056900003460000005690001e1740000066900009a6a000006690001b163000006780000ed56000006780001e55d000006790000c06400000679000123630000068800007f68000006880001355800000096000029480000009600014f5c00000699000010550000069900015d5d",8) + ">>>>");
                    // queriesPersonalTipolectoraBiometria.listarIndicesEnrrolados();
                    //queriesPersonalTipolectoraBiometria.freeScanOnOffSuprema(false);
                    //queriesPersonalTipolectoraBiometria.freeScanOnOffSuprema(true);

                    //Log.v(TAG,"geoLocationListener inicio");
                    //GeoLocationListener geoLocationListener = new GeoLocationListener();

                    Log.v(TAG,"dbManager copyDB");
                    dbManager.copyDB(Environment.getExternalStorageDirectory().toString() + "/tempus/","tempusplus");
                    //internalFile.writeToAppend("holaaaa",Environment.getExternalStorageDirectory().toString() + "/tempus/testing.txt");

                }catch (Exception e){
                    //Log.e(TAG,"Calcular Checksum " + e.getMessage());
                    Log.e(TAG,"queriesPersonalTipolectoraBiometria.EliminarBiometriasPersonalCesado " + e.getMessage());
                }
                break;

            case "4433":
                Log.v(TAG,"Suprema Commands----------------------------------------------------------------------------------------------");
                try{

                    //btSocket02.getOutputStream().write(util.hexStringToByteArray("4018000000000000000000580A")); // LT : List User ID


                    //iswithX = true;
                    //btSocket02.getOutputStream().write(util.hexStringToByteArray("4086000000000010000000D60A")); // LT : List User ID
                    //Thread.sleep(1000);
                    //iswithX = false;
                    //btSocket02.getOutputStream().write(util.hexStringToByteArray("4086000000000010000083490A")); // LT : List User ID


                    //Log.v(TAG,"objSuprema.convertCardToEnroll(602)=" + objSuprema.convertCardToEnroll("602"));
                    //Log.v(TAG,"getChecksum=" + util.getChecksum("44290a0ec22255461c01888c1f03110d44076786180b9009250c0c094a0ce40511121e883f146a083615f68a4f15e8041e1789101a19230b302108124f24e4841225258524279692002aa6821c2e2e09492fde8f4f30508d333415ab3c34f29a4638d9914f3b4d09343c385605402d8b37414ca1124b3c880e4c3e08444ed0903c52d88c0a58348a4d58d786135b4c0e0e634814286362063367e1844f69df87096b2a1b0c6e4aac286fe105ccdde01222bccde01233bbccde2233bbbcce2333aaabcd2344aaaabc2344aa99ab245599889955669877776667887776666687666655559765554444975544444497544444440000000000000000000000000000441a0f13962255464303fd85181192874e117903411304864e16fb89381b87032c1d130d5635060412379d84683f7c830f501c0a2753218868596c8d6164f495616761154d688c9c696fe38557706ea12c74a684577c4c97607c530c507e36941585220555893c8e5f8e4a922092a786fffddee00000ffffffddee000001ffffcddeee00011fffccdddee000111fccccccde0000112ccccccdde000112ccccccddee0011fccccccddee0011fcccccccdee00111ccccccccee00111bbbbbbbbde01112bbbbbbbbcd01122fababbbbcd01233fbbaabbbbd01334fabbaaaaaa74444fbaaaaaaa976554fbaaaaaa9986555ffabbbbaa987655fffbbbbaa98755f000",8));
                    //btSocket02.getOutputStream().write(util.hexStringToByteArray("4087000006020001000200D20A"));
                    //btSocket02.getOutputStream().write(util.hexStringToByteArray("4087010000000002000000CA0A" + "44290a0ec22255461c01888c1f03110d44076786180b9009250c0c094a0ce40511121e883f146a083615f68a4f15e8041e1789101a19230b302108124f24e4841225258524279692002aa6821c2e2e09492fde8f4f30508d333415ab3c34f29a4638d9914f3b4d09343c385605402d8b37414ca1124b3c880e4c3e08444ed0903c52d88c0a58348a4d58d786135b4c0e0e634814286362063367e1844f69df87096b2a1b0c6e4aac286fe105ccdde01222bccde01233bbccde2233bbbcce2333aaabcd2344aaaabc2344aa99ab245599889955669877776667887776666687666655559765554444975544444497544444440000000000000000000000000000441a0f13962255464303fd85181192874e117903411304864e16fb89381b87032c1d130d5635060412379d84683f7c830f501c0a2753218868596c8d6164f495616761154d688c9c696fe38557706ea12c74a684577c4c97607c530c507e36941585220555893c8e5f8e4a922092a786fffddee00000ffffffddee000001ffffcddeee00011fffccdddee000111fccccccde0000112ccccccdde000112ccccccddee0011fccccccddee0011fcccccccdee00111ccccccccee00111bbbbbbbbde01112bbbbbbbbcd01122fababbbbcd01233fbbaabbbbd01334fabbaaaaaa74444fbaaaaaaa976554fbaaaaaa9986555ffabbbbaa987655fffbbbbaa98755f000" + "BACC0000"));
                    //btSocket02.getOutputStream().write(util.hexStringToByteArray("4019000006020000000000610A")); //Check User ID
                    //btSocket02.getOutputStream().write(util.hexStringToByteArray("4066000006020000000000AE0A")); //Read Administration Level of a User
                }catch (Exception e){
                    Log.e(TAG,"Suprema Commands " + e.getMessage());
                }
                break;

            case "1324222":
                Log.v(TAG,"REINICIAR TABLA DE PARAMETROS");
                try{
                    //REINICIAR TABLA DE PARAMETROS
                    queriesParameters.poblar();
                    Toast.makeText(ActivityPrincipal.this,"Reiniciar parámetros",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e(TAG,"queriesParameters.poblar() (REINICIAR TABLA DE PARAMETROS): " + e.getMessage());
                }

                break;

            default:
                break;
        }
    }

    public void ShutdownArduino(OutputStream out) {
        Log.v(TAG, "SHUTDOWN OK");
        try {
            out.write(util.hexStringToByteArray("244F41584100134230303030303030303030303030303030" + util.getChecksum("244F41584100134230303030303030303030303030303030",4) + "41"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AccesoSecreto(String dato) {
        PATRON_SECRET = PATRON_SECRET + dato;
        Log.d(TAG, "AccesoSecreto " + PATRON_SECRET);

        switch (PATRON_SECRET) {
            case "1111111232132122":
                Log.d(TAG, "AccesoSecreto " + "COMANDO: MODO DIOS");
                //logManager.RegisterLogTXT("COMANDO: MODO DIOS");
                try {
                    showLoginDialog();
                } catch (Exception e) {
                    Log.e(TAG,"MODO_DIOS " + e.getMessage() );
                }
                PATRON_SECRET = "";
                break;
            case "3333333123123":
                Log.d(TAG,"AccesoSecreto " + "COMANDO: REINICIAR");
                //logManager.RegisterLogTXT("COMANDO: REINICIAR");
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot"});
                    proc.waitFor();
                } catch (Exception e) {
                    Log.e(TAG,"ERROR_SYSTEM_MAIN " + "COMANDO: REINICIAR -> " + e.getMessage());
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
            case "PROXIMIDAD HID":
                return "05";
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

        Log.v(TAG, "lectoraName: " + lectoraName);
        Log.v(TAG, "tarjeta: " + tarjeta);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                manageKeyboard(false);
            }
        });

        if (lectoras.contains(lectoraName)) {

            if (flag == null || flag == "") {

                Log.v(TAG,"No selecionó ningun evento");

                //Si no ha selecionado ningun evento(entrada, salida, etc)
                //Entonces se procede a asignar por defecto el valor 000 cuando modo evento no esta activo
                // So modo evento esta activo entonces se asignar el valor 001

                flag = "";

                try{
                    //LimpiarDatosMarcacion();
                    String lectora = lectoraName;
                    Log.v(TAG, "Lectora: " + lectora);

                    if (lectora != null) {


                        Log.v(TAG, lectora);
                        if (lectora == "HUELLA SUPREMA") {
                            //tarjeta = String.valueOf(util.convertHexToDecimal(tarjeta.substring(6,8) + tarjeta.substring(4,6) + tarjeta.substring(2,4) + tarjeta.substring(0,2)));
                        } else {
                            if (lectora == "PROXIMIDAD CHINA") {
                                tarjeta = hexToDecimalStringProx(tarjeta);
                            }else if(lectora == "PROXIMIDAD HID"){
                                tarjeta = util.convertHexToString(tarjeta);
                                if(tarjeta.length()>5){
                                    tarjeta = tarjeta.substring(tarjeta.length()-5);
                                }
                            }
                            else {
                                if (lectora != "TECLADO") {
                                    tarjeta = util.convertHexToString(tarjeta);
                                }
                            }
                        }


                        Log.v(TAG, "TARJETA " + tarjeta);


                        FLG_AUX = flag;
                        queriesMarcaciones = new QueriesMarcaciones(this);

                        Log.v(TAG, "MarcacionMaster Exec >");
                        Log.v(TAG, " > Tarjeta: " + tarjeta);
                        Log.v(TAG, " > ID Terminal: " + idTerminal);
                        Log.v(TAG, " > ID Lectora: " + getNroLectora(lectora));
                        Log.v(TAG, " > Flag: " + flag + " - FLG_AUX: " + FLG_AUX);
                        Log.v(TAG, " > FechaHora: " + fechahora.getFechahora());


                        String autorizacion = "";
                        autorizacion = queriesMarcaciones.ModoMarcacion(tarjeta, idTerminal, Integer.parseInt(getNroLectora(lectora)), FLG_AUX, fechahora.getFechahora(), "");
                        Log.v(TAG,"autorizacion: " + autorizacion.toString());

                    }

                }catch (Exception e){
                    Log.e(TAG,"No selecionó ningun evento " + e.getMessage());
                }



            }else{
                // MARCACION CON FLAG SELECCIONADO O CON FLAG POR DEFECTO 001 (MODO EVENTO deshabilitado)

                TIEMPO_ACTIVO = 2;

                LimpiarDatosMarcacion();
                String lectora = lectoraName;
                Log.v(TAG, "Lectora: " + lectora);

                if (lectora != null) {

                    Log.v(TAG, lectora);
                    if (lectora == "HUELLA SUPREMA") {
                        //tarjeta = String.valueOf(util.convertHexToDecimal(tarjeta.substring(6,8) + tarjeta.substring(4,6) + tarjeta.substring(2,4) + tarjeta.substring(0,2)));
                    } else {
                        if (lectora == "PROXIMIDAD CHINA") {
                            tarjeta = hexToDecimalStringProx(tarjeta);
                        }else if(lectora == "PROXIMIDAD HID"){
                            tarjeta = util.convertHexToString(tarjeta);
                            if(tarjeta.length()>5){
                                tarjeta = tarjeta.substring(tarjeta.length()-5);
                            }
                        }
                        else {
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
                                // Verificar que es MARCACION AUTORIZADA
                                if (array_autorizaciones[2].equalsIgnoreCase(ActivityPrincipal.MENSAJE_MARCACIONES.split(";")[0].split(",")[0])) {

                                    if (array_autorizaciones[4].equals("0")){ // Finalizo Marcacion
                                        Log.d(TAG, "FINALIZO MARCACION");
                                        MARCACION_ACTIVA = false;
                                        MODO_MARCACION = "";
                                        TIEMPO_ACTIVO = 2;
                                        MarcacionOK(btSocket01.getOutputStream());
                                        //threadHorariosRelay.startRelayReading(1);
                                        //Thread.sleep(100);
                                        //threadHorariosRelay.startRelayReading(1);
                                        //Thread.sleep(200);
                                        //btSocket01.getOutputStream().write(util.hexStringToByteArray("AAAAAAAAAA"+"244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e314e4e4e00000041"));
                                        //Thread.sleep(1000);
                                        //btSocket01.getOutputStream().write(util.hexStringToByteArray("AAAAAAAAAA"+"244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e304e4e4e00000041"));
                                        mNombre = array_autorizaciones[0];
                                        mTarjeta = array_autorizaciones[1];
                                        mMensajePrincipal = array_autorizaciones[2];
                                        mMensajeSecundario = array_autorizaciones[3];
                                        try{
                                            mFotoPersonal = array_autorizaciones[6];
                                        }catch (Exception e){
                                            Log.e(TAG,"mFotoPersonal = array_autorizaciones[6] " + e.getMessage());
                                            mFotoPersonal = "";
                                        }


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

                                                    Log.d(TAG,"MarcacionMasterTAG " + "Abort 1");
                                                    objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "ABORT", null);
                                                    util.sleep(50);

                                                    Log.d(TAG,"MarcacionMasterTAG " + "Abort 2");
                                                    objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "VERIFY_ON_EXTERNAL_DATA", TEMPLATE);
                                                    util.sleep(50);

                                                    esperando_mano = true;
                                                    fallo_mano = false;

                                                    Log.d(TAG,"MarcacionMasterTAG " + "Abort 3");
                                                    while (esperando_mano) {
                                                        String res = objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "SEND_STATUS_CRC", null);
                                                        String tmp = objHandPunch.OperarStatus(res,"");

                                                        if (tmp.equalsIgnoreCase("Exito")){
                                                            Log.d(TAG,"MarcacionMasterTAG " + "EXITO");
                                                            esperando_mano = false;
                                                        }

                                                        if (tmp.equalsIgnoreCase("Fallo")){
                                                            Log.d(TAG,"MarcacionMasterTAG " + "FALLO");
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


                                                        Log.d(TAG,"MarcacionMasterTAG " + "FINALIZO MARCACION");
                                                        MARCACION_ACTIVA = false;
                                                        MODO_MARCACION = "";
                                                        TIEMPO_ACTIVO = 2;
                                                        MarcacionKO(btSocket01.getOutputStream());
                                                        //threadHorariosRelay.startRelayReading(2);
                                                        mNombre = NOMBRE_PERSONAL_MANO;
                                                        mTarjeta = INDICE;
                                                        mMensajePrincipal = "MARCACION NO AUTORIZADA";
                                                        mMensajeSecundario = "ERROR AL CAPTURAR MANO";
                                                        MarcacionUI();

                                                    } else {
                                                        TEMPLATE = "";
                                                        String resultado = objHandPunch.SerialHandPunch(btSocket03.getOutputStream(), btSocket03.getInputStream(), "SEND_TEMPLATE", null);
                                                        util.sleep(50);

                                                        Log.d(TAG, "MarcacionMasterTAG " + "OUTPUT: " + resultado);

                                                        // Analizar Score
                                                        if (resultado.length()>14) {
                                                            Log.d(TAG,"MarcacionMasterTAG " + "SCORE OK");
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
                                                                Log.d(TAG,"MarcacionMasterTAG " + "S<100");
                                                                String res_marcacion = "";
                                                                try {
                                                                    res_marcacion = queriesMarcaciones.ModoMarcacion(INDICE, idTerminal, 10, FLG_AUX, fechahora.getFechahora(), MODO_MARCACION);
                                                                } catch( Exception e) {
                                                                    Log.e(TAG,"MarcacionMasterTAG " + "Error res_marcacion: " + e.getMessage());
                                                                }

                                                                Log.d(TAG,"MarcacionMasterTAG " + "res_marcacion: "+res_marcacion);
                                                                array_autorizaciones = res_marcacion.split(",");
                                                                // Verificar que es MARCACION AUTORIZADA
                                                                if (array_autorizaciones[2].equalsIgnoreCase(ActivityPrincipal.MENSAJE_MARCACIONES.split(";")[0].split(",")[0])) {

                                                                    Log.d(TAG,"MarcacionMasterTAG " + "FINALIZO MARCACION 1");
                                                                    MARCACION_ACTIVA = false;
                                                                    MODO_MARCACION = "";
                                                                    TIEMPO_ACTIVO = 2;
                                                                    MarcacionOK(btSocket01.getOutputStream());
                                                                    //threadHorariosRelay.startRelayReading(1);
                                                                    mNombre = array_autorizaciones[0];
                                                                    mTarjeta = array_autorizaciones[1];
                                                                    mMensajePrincipal = array_autorizaciones[2];
                                                                    mMensajeSecundario = array_autorizaciones[3];
                                                                    Log.d(TAG,"MarcacionMasterTAG " + "FINALIZO MARCACION 1");
                                                                    MarcacionUI();
                                                                } else {
                                                                    Log.d(TAG,"MarcacionMasterTAG " + "FINALIZO MARCACION 2");
                                                                    MARCACION_ACTIVA = false;
                                                                    MODO_MARCACION = "";
                                                                    TIEMPO_ACTIVO = 2;
                                                                    MarcacionKO(btSocket01.getOutputStream());
                                                                    //threadHorariosRelay.startRelayReading(2);
                                                                    mNombre = array_autorizaciones[0];
                                                                    mTarjeta = array_autorizaciones[1];
                                                                    mMensajePrincipal = array_autorizaciones[2];
                                                                    mMensajeSecundario = array_autorizaciones[3];
                                                                    Log.d(TAG,"MarcacionMasterTAG " + "FINALIZO MARCACION 3");
                                                                    MarcacionUI();
                                                                }

                                                            } else { // MARCACION NO AUTORIZADA, BIOMETRIA NO COINCIDE
                                                                Log.d(TAG,"MarcacionMasterTAG " + "S>100");
                                                                Log.d(TAG,"MarcacionMasterTAG " + "FINALIZO MARCACION 3");
                                                                MARCACION_ACTIVA = false;
                                                                MODO_MARCACION = "";
                                                                TIEMPO_ACTIVO = 2;
                                                                MarcacionKO(btSocket01.getOutputStream());
                                                                //threadHorariosRelay.startRelayReading(2);
                                                                mNombre = NOMBRE_PERSONAL_MANO;
                                                                mTarjeta = INDICE;
                                                                mMensajePrincipal = "MARCACION NO AUTORIZADA";
                                                                mMensajeSecundario = "BIOMETRIA NO COINCIDE";
                                                                Log.d(TAG,"MarcacionMasterTAG " + "FINALIZO MARCACION 3");
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
                                                TIEMPO_ACTIVO = 2;
                                                mNombre = array_autorizaciones[0];
                                                mTarjeta = array_autorizaciones[1];
                                                //mMensajePrincipal = "MARCACION NO AUTORIZADA";
                                                mMensajePrincipal = "ERROR";
                                                mMensajeSecundario = "NO EXISTE BIOMETRIA";
                                                MarcacionKO(btSocket01.getOutputStream());
                                                //threadHorariosRelay.startRelayReading(2);
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
                                    TIEMPO_ACTIVO = 2;

                                    if (autorizacion.equalsIgnoreCase("")) { // TEMPORAL Error NO EXISTE TARJETA
                                        MarcacionKO(btSocket01.getOutputStream());
                                        //threadHorariosRelay.startRelayReading(2);
                                        mNombre = "";
                                        mTarjeta = tarjeta;
                                        //mMensajePrincipal = "MARCACION NO AUTORIZADA";
                                        mMensajePrincipal = "ERROR";
                                        mMensajeSecundario = "(*)";
                                        MarcacionUI();
                                    } else {
                                        MarcacionKO(btSocket01.getOutputStream());
                                        //threadHorariosRelay.startRelayReading(2);
                                        mNombre = array_autorizaciones[0];
                                        mTarjeta = array_autorizaciones[1];
                                        mMensajePrincipal = array_autorizaciones[2];
                                        mMensajeSecundario = array_autorizaciones[3];
                                        try{
                                            mFotoPersonal = array_autorizaciones[6];
                                        }catch (Exception e){
                                            Log.e(TAG,"mFotoPersonal = array_autorizaciones[6] " + e.getMessage());
                                            mFotoPersonal = "";
                                        }
                                        MarcacionUI();
                                    }

                                }






                    } catch (Exception e) {
                        Log.e(TAG," Intento de setear valores para marcacion " + e.getMessage());
                        MARCACION_ACTIVA = false;
                        MODO_MARCACION = "";
                        TIEMPO_ACTIVO = 2;
                        MarcacionKO(btSocket01.getOutputStream());
                        //threadHorariosRelay.startRelayReading(2);
                        mNombre = "";
                        mTarjeta = tarjeta;
                        //mMensajePrincipal = "MARCACION NO AUTORIZADA";
                        mMensajePrincipal = "ERROR";
                        mMensajeSecundario = "e000";
                        mFotoPersonal = "";
                        MarcacionUI();
                    }

                } else {
                    MARCACION_ACTIVA = false;
                    MODO_MARCACION = "";
                    TIEMPO_ACTIVO = 2;
                    Log.v(TAG, "LECTORA NO HABILITADA");
                    MarcacionKO(btSocket01.getOutputStream());
                    //threadHorariosRelay.startRelayReading(2);
                    mNombre = "";
                    mTarjeta = "";
                    //mMensajePrincipal = "MARCACION NO AUTORIZADA";
                    mMensajePrincipal = "ERROR";
                    mMensajeSecundario = "No existe Base de Datos";
                    mFotoPersonal = "";
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
        mFotoPersonal = "";
    }

    public void MarcacionUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                txvMarcacionNombre.setText(mNombre);
                txvMarcacionTarjeta.setText(mTarjeta);
                txvMarcacionMsjPrincipal.setText(mMensajePrincipal);
                txvMarcacionMsjSecundario.setText(mMensajeSecundario);
                if(parameterFotoPersonal){
                    fileFotoPersonal = new File(Environment.getExternalStorageDirectory().toString() + "/tempus/img/personal/sync/original/" + mFotoPersonal);
                    if(fileFotoPersonal.isFile()){
                        try{
                            Log.v(TAG,"imgFotoPersonal " + fileFotoPersonal.toString());
                            imgFotoPersonal.setImageBitmap(BitmapFactory.decodeFile(fileFotoPersonal.toString()));
                        }catch (Exception e){
                            Log.e(TAG,"imgFotoPersonal " + e.getMessage());
                        }
                    }else{
                        imgFotoPersonal.setImageResource(R.drawable.fotopersonal);
                    }
                }else{
                    try{
                        Log.v(TAG,"imgFotoPersonal " + parameterFotoPersonal);
                        imgFotoPersonal.setColorFilter(Color.parseColor(parametersColorsUI.split(",")[7]));
                    }catch (Exception e){
                        Log.e(TAG,"imgFotoPersonal " + e.getMessage());
                    }
                }




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
            //txvMarcacionFondo.setBackgroundColor(getResources().getColor(R.color.colorMarcacionOK));
            //out.write(util.hexStringToByteArray("244F415841000C3531000041"));
            //out.write(util.hexStringToByteArray("244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e4e314e4e00000041"));
            threadHorariosRelay.startRelayReading(1);
            Thread.sleep(200);
            out.write(util.hexStringToByteArray("AAAAAAAAAA" + "244F4158410013424e4e4e4e4e4e4e4e4e4e4e" + relay02 + relay01 + "31" + tiempoActivoRelay02 + tiempoActivoRelay01 + util.getChecksum("244F4158410013424e4e4e4e4e4e4e4e4e4e4e" + relay02 + relay01 + "31" + tiempoActivoRelay02 + tiempoActivoRelay01,4) + "41"));
            Log.v(TAG,"244F4158410013424e4e4e4e4e4e4e4e4e4e4e" + relay02 + relay01 + "31" + tiempoActivoRelay02 + tiempoActivoRelay01 + util.getChecksum("244F4158410013424e4e4e4e4e4e4e4e4e4e4e" + relay02 + relay01 + "31" + tiempoActivoRelay02 + tiempoActivoRelay01,4) + "41");
            relay01 = "4e";
            relay02 = "4e";
            tiempoActivoRelay01 = "4e";
            tiempoActivoRelay02 = "4e";
            //Thread.sleep(100);
        } catch (Exception e) {
            Log.e(TAG,"MarcacionOK " + e.getMessage());
        }
    }

    public void MarcacionKO(OutputStream out) {
        Log.v(TAG, "MARCACION KO");
        try {
            //txvMarcacionFondo.setBackgroundColor(getResources().getColor(R.color.colorMarcacionKO));
            //out.write(util.hexStringToByteArray("244F415841000C3530000041"));
            //out.write(util.hexStringToByteArray("244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e4e304e4e00000041"));
            threadHorariosRelay.startRelayReading(2);
            Thread.sleep(200);
            out.write(util.hexStringToByteArray("AAAAAAAAAA" + "244F4158410013424e4e4e4e4e4e4e4e4e4e4e" + relay02 + relay01 + "30" + tiempoActivoRelay02 + tiempoActivoRelay01 + util.getChecksum("244F4158410013424e4e4e4e4e4e4e4e4e4e4e" + relay02 + relay01 + "30" + tiempoActivoRelay02 + tiempoActivoRelay01,4) + "41"));
            Log.v(TAG,"244F4158410013424e4e4e4e4e4e4e4e4e4e4e" + relay01 + relay02 + "30" + tiempoActivoRelay01 + tiempoActivoRelay02 + util.getChecksum("244F4158410013424e4e4e4e4e4e4e4e4e4e4e" + relay02 + relay01 + "30" + tiempoActivoRelay02 + tiempoActivoRelay01,4) + "41");
            relay01 = "4e";
            relay02 = "4e";
            tiempoActivoRelay01 = "4e";
            tiempoActivoRelay02 = "4e";
            //Thread.sleep(100);
        } catch (Exception e) {
            Log.e(TAG,"MarcacionKO " + e.getMessage());
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

        Log.v(TAG,"OTG_MANAGER " + "AdministrarOTG");

        try {
            if (enable){
                Log.v(TAG,"OTG_MANAGER " + "Activando OTG");
                out.write(util.hexStringToByteArray("244F4158410013423131313131303031")); // 00
                util.sleep(500);
                out.write(util.hexStringToByteArray("244F4158410013423131313131313031")); // 10
            } else {
                Log.v(TAG,"OTG_MANAGER " + "Desactivando OTG ... Pasando a carga");
                out.write(util.hexStringToByteArray("244F4158410013423131313131303031")); // 00
                util.sleep(500);
                out.write(util.hexStringToByteArray("244F4158410013423131313131303131")); // 01
            }
        } catch (IOException e) {
            Log.wtf(TAG,"OTG_MANAGER " + e.getMessage());
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
                try{
                    //txvMarcacionFondo.setBackgroundColor(getResources().getColor(R.color.colorMarcacionKO));
                    threadHorariosRelay.startRelayReading(2);
                    Thread.sleep(200);
                    btSocket01.getOutputStream().write(util.hexStringToByteArray("AAAAAAAAAA" + "244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e4e30" + tiempoActivoRelay02 + tiempoActivoRelay01 + util.getChecksum("244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e4e30" + tiempoActivoRelay02 + tiempoActivoRelay01,4) + "41"));
                    Log.v(TAG,"244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e4e30" + tiempoActivoRelay02 + tiempoActivoRelay01 + util.getChecksum("244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e4e30" + tiempoActivoRelay02 + tiempoActivoRelay01,4) + "41");
                }catch (Exception e){
                    Log.e(TAG,"threadHorariosRelay + btSocket01 " + e.getMessage());
                }

                relay01 = "4e";
                relay02 = "4e";
                tiempoActivoRelay01 = "4e";
                tiempoActivoRelay02 = "4e";
                //Modificado para todaslas tecnologias, al marcar sin evento emitirá mensaje selecionar evento
                TIEMPO_ACTIVO = 1;
                imgFotoPersonal.setVisibility(View.INVISIBLE);
                txvMarcacionNombre.setVisibility(View.INVISIBLE);
                txvMarcacionTarjeta.setVisibility(View.INVISIBLE);
                txvMarcacionMsjPrincipal.setVisibility(View.VISIBLE);
                txvMarcacionFondo.setVisibility(View.VISIBLE);
                txvMarcacionMsjPrincipal.setText("SELECIONE EVENTO");
                txvMarcacionMsjSecundario.setVisibility(View.INVISIBLE);

            }else{
                imgFotoPersonal.setVisibility(View.INVISIBLE);
                txvMarcacionNombre.setVisibility(View.INVISIBLE);
                txvMarcacionTarjeta.setVisibility(View.INVISIBLE);
                txvMarcacionFondo.setVisibility(View.INVISIBLE);
                txvMarcacionMsjPrincipal.setVisibility(View.INVISIBLE);
                txvMarcacionMsjSecundario.setVisibility(View.INVISIBLE);
                actualizarFlag(null,null);

            }
        }else{
            if (visible) {
                imgFotoPersonal.setVisibility(View.VISIBLE);
                txvMarcacionNombre.setVisibility(View.VISIBLE);
                txvMarcacionTarjeta.setVisibility(View.VISIBLE);
                txvMarcacionFondo.setVisibility(View.VISIBLE);
                txvMarcacionMsjPrincipal.setVisibility(View.VISIBLE);
                txvMarcacionMsjSecundario.setVisibility(View.VISIBLE);

            } else {
                imgFotoPersonal.setVisibility(View.INVISIBLE);
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
                //Log.v(TAG,"trama -------------------------> " + trama);
                //26 4011000000000000000069ba0a


                //Log.v(TAG,"evaluar objArduino.getTipoMensaje=" + objArduino.getTipoMensaje() + " - " + "objArduino.getNroLector=" + objArduino.getNroLector() + " - " + "objArduino.getMensaje=" + objArduino.getMensaje());
                //Log.v(TAG,"evaluar objArduino.toString()=" + objArduino.toString());



                switch (objArduino.getTipoMensaje()){
                    case "00":

                        // Analizar ADC
                        //boolean isADC = analizarADC(objArduino.getNroLector(), String.valueOf(util.convertHexToDecimal(objArduino.getDatosLector().substring(0, 2))));
                        //Log.v(TAG,"isADC " + isADC);
                        if(objArduino.getNroLector().equalsIgnoreCase("44")){
                            statusADC(objArduino.getMensaje());
                            break;
                        }

                        if (activityActive.equals("Principal") && lectoras.contains(objArduino.getFlagRead())){

                            // Analizar Flg_Actividad --------------------------------------
                            if(objArduino.getNroLector().equalsIgnoreCase("06")){
                                keytoFlg_Actividad(objArduino.getMensaje());
                                break;
                            }
                            // -------------------------------------------------------------

                            if( MODO_EVENTO ){



                                // no aceptamos nada de marcaciones
                                if (flag !="" && flag !=null){

                                    // Enviar Tarjeta para evaluar marcacion -----------------------
                                    String flagRead = objArduino.getFlagRead();
                                    String tarjeta = objArduino.getDatosLector().substring(objArduino.getMascaraIni(),objArduino.getMascaraFin());
                                    Log.v(TAG,tarjeta);
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
                                    // -------------------------------------------------------------
                                } else {
                                    Log.v(TAG,"DEBE SELECCIONAR UN EVENTO");

                                    // Enviar Tarjeta para evaluar marcacion -----------------------
                                    String flagRead = objArduino.getFlagRead();
                                    String tarjeta = objArduino.getDatosLector().substring(objArduino.getMascaraIni(),objArduino.getMascaraFin());
                                    Log.v(TAG,tarjeta);
                                    MarcacionMaster(flagRead,tarjeta);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            turnOnScreen();
                                            manageLayerMarcacion(true);
                                            actualizarFlag(null,null);
                                        }
                                    });

                                    /*
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
                                    */
                                }
                            } else {

                                // Analizar Flg_Actividad --------------------------------------
                                if(objArduino.getNroLector().equalsIgnoreCase("06")){
                                    //keytoFlg_Actividad(objArduino.getMensaje());
                                    break;
                                }
                                // -------------------------------------------------------------


                                // Enviar Tarjeta para evaluar marcacion ---------------------------
                                String flagRead = objArduino.getFlagRead();
                                String tarjeta = objArduino.getDatosLector().substring(objArduino.getMascaraIni(),objArduino.getMascaraFin());
                                Log.v(TAG, "TEMPUS: " + tarjeta);
                                MarcacionMaster(flagRead,tarjeta);
                                // Marcando
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        turnOnScreen();
                                        manageLayerMarcacion(true);
                                    }
                                });
                                // -----------------------------------------------------------------
                            }
                        }
                        break;
                    case "01":
                        break;
                    default:
                        Log.d(TAG,"Autorizaciones " + "objSuprema.toString(): " + objSuprema.toString());
                        objArduino.limpiarTramaArduino();
                        break;
                }

                break;
            case "Suprema":
                objSuprema.setTrama(trama);
                objSuprema.estructurarTramaSupremaGeneral();

                switch (objSuprema.getComando()){
                    case "01": // Write
                        Log.v(TAG,"objSuprema>>> Write>>>" + trama);
                        if(isWriting){
                            huellaWrite = trama;
                            huellaWriteFlag = objSuprema.getFlagError();
                        }
                        //Log.v(TAG,"objSuprema>>> Write>>>" + objSuprema.getFlagError() + " - " + huellaWriteFlag + " - " + huellaWrite);
                        break;
                    case "02": // Save
                        Log.v(TAG,"objSuprema>>>" + trama);
                        break;
                    case "03": // Read

                        Log.v(TAG,"TEMPUS: " + "Llego READ 03 ---- " + objSuprema.getTrama());

                        objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
                        Log.d(TAG,"Autorizaciones " + "objSuprema.toString(): " + objSuprema.toString());
                        break;
                    case "05": // EnrollByScan
                        Log.v(TAG,"TEMPUS: " + "EnrollByScan >>>");
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
                        Log.v(TAG,"TEMPUS: " + "EnrollByTemplate >>>");
                        Log.v(TAG,"TEMPUS: " + "EnrollByTemplate >>>" + objSuprema.getTrama());
                        if (isReplicatingTemplate) {
                            //llega la respuesta de enrolamiento ok y ko
                            try{
                                util.sleep(250);
                                objSuprema.writeToSuprema(btSocket02.getOutputStream(),"Cancel",null);
                                util.sleep(200);

                                isReplicatingTemplate = false;
                            }catch(Exception e){
                                Log.v(TAG,"TEMPUS: " + "Error EnrollByTemplate >>> " + e.getMessage());
                            }
                        }
                        break;
                    case "11": // IdentifyByScan
                        Log.v(TAG,"TEMPUS: " + "IdentifyByScan >>>");

                        if (activityActive.equals("Principal")){

                            if ( MODO_EVENTO ){
                                if (flag !="" && flag !=null) {
                                    try{
                                        String flagRead = "HUELLA SUPREMA";
                                        String tarjeta = objSuprema.getFinalValue(objSuprema.getParametro());
                                        if (objSuprema.getFlag().equalsIgnoreCase("69")){
                                            tarjeta = "00000000";
                                        }
                                        Log.v(TAG,"TEMPUS: " + tarjeta);
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
                                    }catch(Exception e){
                                        Log.e(TAG,"IdentifyByScan" + e.getMessage());
                                    }

                                } else {
                                    //En el caso que no seleccione un evento
                                    //solo se emitira el sonido delñ buzzer y luz de led
                                    //no se incluye el encendido de relay ni el tiempo activo de relay
                                    Log.v(TAG,"DEBE SELECCIONAR UN EVENTO");
                                    //MarcacionKO(btSocket01.getOutputStream());
                                    //threadHorariosRelay.startRelayReading(2);

                                    /*
                                    try{
                                        btSocket01.getOutputStream().write(util.hexStringToByteArray("244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e4e304e4e00000041"));
                                    }catch (Exception e){
                                    }
                                    */
                                    try{
                                        String flagRead = "HUELLA SUPREMA";
                                        String tarjeta = objSuprema.getFinalValue(objSuprema.getParametro());
                                        if (objSuprema.getFlag().equalsIgnoreCase("69")){
                                            tarjeta = "00000000";
                                        }
                                        Log.v(TAG,"TEMPUS: " + tarjeta);
                                        //Log.v("TEMPUS: ",String.valueOf(Integer.parseInt(tarjeta)));
                                        MarcacionMaster(flagRead,String.valueOf(Integer.parseInt(tarjeta)));

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                turnOnScreen();
                                                manageLayerMarcacion(true);
                                                actualizarFlag(null,null);
                                            }
                                        });
                                    }catch(Exception e){
                                        Log.e(TAG,"IdentifyByScan" + e.getMessage());
                                    }
                                    /*
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Toast.makeText(ActivityPrincipal.this, "DEBE SELECCIONAR UN EVENTO", Toast.LENGTH_LONG).show();
                                            try {
                                                btSocket01.getOutputStream().write(util.hexStringToByteArray("244F4158410013424e4e4e4e4e4e4e4e4e4e4e4e4e304e4e00000041"));
                                                ui.showAlert(ActivityPrincipal.this,"warning","   DEBE SELECCIONAR UN EVENTO   ");

                                            } catch(Exception e) {
                                                Toast.makeText(ActivityPrincipal.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                                    */
                                }
                            } else {
                                String flagRead = "HUELLA SUPREMA";
                                String tarjeta = objSuprema.getFinalValue(objSuprema.getParametro());
                                if (objSuprema.getFlag().equalsIgnoreCase("69")){
                                    tarjeta = "00000000";
                                }
                                Log.v(TAG,"TEMPUS: " + tarjeta);
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
                        Log.v(TAG,"TEMPUS: " + "ReadTemplate >>>");
                        if(isEnrolling){
                            if (objSuprema.getFlagError().equalsIgnoreCase("SUCCESS") || objSuprema.getFlagError().equalsIgnoreCase("CONTINUE")){
                                huellaEnroll2 = trama;
                                huellaEnrollFlag2 = objSuprema.getFlagError();
                            } else {
                                huellaEnroll2 = "";
                                huellaEnrollFlag2 = objSuprema.getFlagError();
                            }
                            Log.v(TAG,"TEMPUS: " + objSuprema.getFlagError());
                        }
                        break;
                    case "16": // DeleteTemplate
                        Log.v(TAG,"objSuprema>>> DeleteTemplate>>>");
                        if (isDeleting){
                            huellaDelete1 = trama;
                            huellaDeleteFlag1 = objSuprema.getFlagError();
                            /*
                            if (objSuprema.getFlagError().equalsIgnoreCase("SUCCESS") || objSuprema.getFlagError().equalsIgnoreCase("NOT_FOUND")){
                                huellaDelete1 = trama;
                                huellaDeleteFlag1 = objSuprema.getFlagError();
                            } else {
                                huellaDelete1 = "";
                                huellaDeleteFlag1 = objSuprema.getFlagError();
                            }
                            */
                            Log.v(TAG,"objSuprema>>> DeleteTemplate>>>" + objSuprema.getFlagError() + " - " + huellaDeleteFlag1 + " - " + huellaDelete1);
                        }
                        break;
                    case "17": // DeleteAllTemplates
                        Log.v(TAG,"objSuprema>>> DeleteAllTemplates >>>");
                        Log.v(TAG,"objSuprema>>>" + trama);
                        break;
                    case "18": // ListUserID
                        Log.v(TAG,"TEMPUS: " + "ListUserID >>>");
                        //Log.v(TAG,"TEMPUS: " + "ListUserID >>>" + trama);
                        Log.v(TAG,"TEMPUS: " + "ListUserID >>>" + objSuprema.getTrama());
                        break;
                    case "60": // Cancel
                        Log.v(TAG,"objSuprema>>> Cancel >>>");
                        Log.v(TAG,"objSuprema>>>" + trama);
                        break;
                    case "86": // Enroll Template with Extended Data Transfer Protocol
                        Log.v(TAG,"objSuprema>>> ListUserIDX>>>");
                        //Log.v(TAG,"TEMPUS: " + "ListUserID >>>" + trama);
                        Log.v(TAG,"objSuprema>>> ListUserIDX>>>" + objSuprema.getTrama());
                        break;
                    case "87": // Enroll Template with Extended Data Transfer Protocol
                        Log.v(TAG,"objSuprema>>> Enroll Template with Extended Data Transfer Protocol>>>");
                        Log.v(TAG,"objSuprema>>> Enroll Template with Extended Data Transfer Protocol>>>" + objSuprema.getTrama());

                        if(isReplicatingTemplate){
                            huellaEnroll = trama;
                            huellaEnrollFlag = objSuprema.getFlagError();
                        }

                        break;
                    default:
                        Log.v(TAG,"objSuprema>>>" + objSuprema.getTrama());
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
            //logManager.RegisterLogTXT("BA="+valor+"\n");
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
                //Log.v(TAG,"threadSerial01UPD loop");
                if (!BT_01_IS_CONNECTED) {
                    util.sleep(1000);
                } else {
                    try {
                        byte[] rawBytes = new byte[1];
                        //Log.v(TAG,"btSocket01 ----- 1");
                        btSocket01.getInputStream().read(rawBytes);
                        //Log.v(TAG,"btSocket01 ----- 2");
                        acumulador = acumulador + util.byteArrayToHexString(rawBytes);
                        //Log.v(TAG,"btSocket01 ----- 3");
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
                        //Log.v(TAG,"btSocket01 ----- 4");
                    } catch (Exception e) {
                        if (!isBooting) {
                            BT_01_IS_CONNECTED = false;
                            /*
                            try{
                                Thread.sleep(3000);
                                //btSocket01.closeBT();
                                Log.v(TAG,"btSocket01.ConnectBT()");
                                btSocket01.ConnectBT();
                                internalFile.writeToAppend(fechahora.getFechahora() + ": " + "btSocket01.ConnectBT() ",Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
                            }catch (Exception ex){
                                Log.e(TAG,"btSocket01.ConnectBT() " + ex.getMessage());
                            }
                            */
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
                //Log.v(TAG,"threadSerial02UPD loop");
                if (!BT_02_IS_CONNECTED) {
                    util.sleep(1000);
                } else {
                    try {
                        byte[] rawBytes = new byte[1];
                        //Log.v(TAG,"btSocket02 ----- 1");
                        btSocket02.getInputStream().read(rawBytes);
                        //Log.v(TAG,"btSocket02 ----- 2");
                        acumulador = acumulador + util.byteArrayToHexString(rawBytes);
                        //Log.v(TAG,"btSocket02 ----- 3");

                        Log.v(TAG,"acumulador>>>" + acumulador);
                        if(iswithX){
                            withx += acumulador;
                            acumulador = "";
                            Log.v(TAG,"withx=" + withx);
                        }else if(!iswithX){
                            withx = "";
                        }

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
                            Log.v(TAG,tamano + " " + acumulador);
                            evaluarTrama("Suprema",acumulador);
                            objSuprema.limpiarTramaSuprema();
                            acumulador = "";
                        }
                        //Log.v(TAG,"btSocket02 ----- 4");


                    } catch (Exception e) {
                        if (!isBooting){
                            BT_02_IS_CONNECTED = false;
                            /*
                            try{
                                Thread.sleep(3000);
                                //btSocket02.closeBT();
                                Log.v(TAG,"btSocket02.ConnectBT()");
                                btSocket02.ConnectBT();
                                internalFile.writeToAppend(fechahora.getFechahora() + ": " + "btSocket02.ConnectBT() ",Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
                            }catch (Exception ex){
                                Log.e(TAG,"btSocket02.ConnectBT() " + ex.getMessage());
                            }
                            */
                        }
                    }
                }
            }
        }
    });


    /* --- CONTROL PRINCIPAL --- */
    public void controlGeneral(boolean c1,boolean c2, boolean c3) {
        Log.v(TAG,"CONTROL GENERAL");
        Log.v(TAG,"isCharging: " + isCharging);
        Log.v(TAG,"isBooting: " + isBooting);
        Log.v(TAG,"BT_01_IS_CONNECTED: " + BT_01_IS_CONNECTED);
        Log.v(TAG,"BT_02_IS_CONNECTED: " + BT_02_IS_CONNECTED);

        if (c1 && c2 && c3){ // Si todos los seriales estan conectados podemos hacer algo

            //Obtener status ADC
            //statusADC("");

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

                    //logManager.RegisterLogTXT("APAGANDO EQUIPO 15s");
                    //showMsgBoot(true,"Apagando Equipo en 15 segundos ... espere ...");
                    showMsgBoot(true,"Energía insuficiente, conecte cargador");

                    //Intent intent01 = new Intent(ActivityPrincipal.this, ActivityLogin.class);
                    //intent01.putExtra("llave", "valor");
                    //startActivityForResult(intent01, 1);

                    //UserInterfaceM userInterfaceM = new UserInterfaceM();
                    //userInterfaceM.goToActivity(ActivityPrincipal.this, ActivityLogin.class,"","");

                    /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    */

                    c = 15;

                    Thread threadShutdown = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                ShutdownArduino(btSocket01.getOutputStream());
                            } catch (Exception e) {
                                Log.e(TAG,"Error 15s -> " + e.getMessage());
                            }

                            try {
                                Thread.sleep(15000);
                                Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                                proc.waitFor();
                            } catch (Exception e) {
                                Log.wtf(TAG,"WTF " + e.getMessage());
                            }
                        }
                    });

                    //logManager.RegisterLogTXT("Apagando");
                    //threadShutdown.start();

                } else {
                    logManager.RegisterLogTXT("Reconectando Interfaces");
                    c = 100;
                    detail = detail + "\n";
                    /*
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if(bluetoothAdapter.disable()){
                        bluetoothAdapter.enable();
                    }
                    */
                    showMsgBoot(true,"Reconectando Interfaces \n"+detail+"]");
                    if(INICIADO){
                        try{
                            Log.v(TAG,"queriesLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG,"Reconectando Interfaces",""));
                        }catch (Exception e){
                            Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
                        }
                    }
                    INICIADO = false;
                }

            }

        }

        if(BT_01_IS_C != BT_01_IS_CONNECTED){
            try{
                Log.v(TAG,"queriesLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG,"BT_01_IS_CONNECTED=" + BT_01_IS_CONNECTED,""));
            }catch (Exception e){
                Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
            }
            BT_01_IS_C = BT_01_IS_CONNECTED;
        }

        if(BT_02_IS_C != BT_02_IS_CONNECTED){
            try{
                Log.v(TAG,"queriesLogTerminal " + queriesLogTerminal.insertLogTerminal(TAG,"BT_02_IS_CONNECTED=" + BT_02_IS_CONNECTED,""));
            }catch (Exception e){
                Log.e(TAG, "queriesLogTerminal.insertLogTerminal " + e.getMessage());
            }
            BT_02_IS_C = BT_02_IS_CONNECTED;
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
                        //Solo para test bluetooth true ------------------------------------
                        // BT_01_IS_CONNECTED = BT_02_IS_CONNECTED = BT_03_IS_CONNECTED = true;
                        // -----------------------------------------------------------------

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
                        //Log.v(TAG, "THREAD_COUNT" + String.valueOf(threadSet));

                    }
                });

                util.sleep(3000);

            }
        }
    });

    Thread threadControlPantalla = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (ctrlThreadPantallaEnabled){

                    Log.d(TAG,"threadControlPantalla: [FLAG=" + flag + "] - [TIEMPO_ACTIVO=" + TIEMPO_ACTIVO + "] - [MARCACION_ACTIVA=" + MARCACION_ACTIVA + "] - [MODO_MARCACION=" + MODO_MARCACION + "]");

                    try {
                        if (TIEMPO_ACTIVO <= 0) {

                            if (TIEMPO_ACTIVO <= -3600) { TIEMPO_ACTIVO = 0; }

                            MARCACION_ACTIVA = false;
                            MODO_MARCACION = "";

                            Log.v(TAG,"threadControlPantalla: 1");
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
                                        Log.v(TAG,"threadControlPantalla: 2");
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

            Log.d(TAG,"ETH_HILO " + "INICIO");

            util.sleep(15000); // 1 min

            while (true){

                //runOnUiThread(new Runnable() {
                //    @Override
                //    public void run() {
                //        Toast.makeText(getApplicationContext(), "CICLO ... ",Toast.LENGTH_SHORT).show();
                //    }
                //});

                Log.d(TAG,"ETH_HILO CICLO ... ");


                DBManager db = new DBManager(ActivityPrincipal.context);
                String resultado = db.valexecSQL("SELECT PARAMETRO FROM TERMINAL_CONFIGURACION");
                String valor = resultado.split(",")[1];

                if (valor.equalsIgnoreCase("1")){
                    Log.d(TAG,"ETH_HILO Eth0 Enabled ... ");
                    String tmp = "";

                    try {
                        tmp = db.valexecSQL("SELECT " +
                                "(SELECT COUNT(*) FROM MARCACIONES WHERE SINCRONIZADO = 0) + " +
                                "(SELECT COUNT(*) FROM PERSONAL_TIPOLECTORA_BIOMETRIA WHERE SINCRONIZADO = 2) " +
                                "AS DATAXENVIAR;");
                    } catch(Exception e) {
                        Log.wtf(TAG,"ETH_HILO ERROR EN VAL EXEC ... "+e.getMessage());
                    }

                    int cant = 0;

                    try {
                        cant = Integer.parseInt(tmp);
                    } catch (Exception e){
                        Log.wtf(TAG,"ETH_HILO ERROR EN CONVERSION ... " + e.getMessage());
                    }

                    Log.d(TAG,"ETH_HILO Validando Datos (Cantidad) ... ");

                    if ( cant == 0 ){ // SI NO HAY NADA POR ENVIAR
                        Log.d(TAG,"ETH_HILO Eth0 Enabled ... Sin data por enviar ...");


                        AdministrarOTG(btSocket01.getOutputStream(),false); // CARGAR


                        util.sleep(10000);
                    } else { // SI HAY ALGO POR ENVIAR
                        Log.d(TAG,"ETH_HILO Eth0 Enabled ... Con data por enviar ... ");

                        // Preguntamos por servidor
                        boolean res;
                        Connectivity c = new Connectivity();

                        String servidorDatos = "";

                        Log.d(TAG,"IP_TEST entrando");
                        QueriesServicios queriesServicios = new QueriesServicios(ActivityPrincipal.context);
                        List<Servicios> servidor_datos_principal = queriesServicios.BuscarServicios("SERVIDOR_DATOS_PRINCIPAL");
                        Log.d(TAG,"IP_TEST tam: "+servidor_datos_principal.size());
                        for (int i = 0; i < servidor_datos_principal.size(); i++){
                            servidorDatos = servidor_datos_principal.get(i).getHost();
                            break;
                        }
                        Log.d(TAG,"ETH_HILO datos: "+ servidorDatos);
                        res = c.isURLReachable(getApplicationContext(),servidorDatos,"ip");

                        if (!res) {
                            //res = c.isValidConnection();

                            Log.d(TAG,"ETH_HILO Conexion inválida (1) ... ");

                            // Si no hay conexion al servidor, activo otg por 5 seg
                            Log.d(TAG,"ETH_HILO Eth0 Enabled ... Activando ... ");

                            AdministrarOTG(btSocket01.getOutputStream(), true); //PRENDER ETHERNET
                            Log.d(TAG,"ETH_HILO Eth0 Enabled ... Activado ... ");

                            util.sleep(8000);
                            // Testear Si conexion es ok

                            res = c.isURLReachable(getApplicationContext(),servidorDatos,"ip");
                            Log.d(TAG,"ETH_HILO Eth0 Enabled ... Procesado ... ");


                            if (res) {
                                Log.d(TAG,"ETH_HILO Conexion válida (1) ... ");

                                //res = c.isValidConnection();
                                //if (res) {
                                util.sleep(10000);
                                //}
                                // Activado por 5 seg luego cargo por 12 seg

                                AdministrarOTG(btSocket01.getOutputStream(), false); //CARGAR

                                util.sleep(36000);
                            } else {
                                Log.d(TAG,"ETH_HILO No hay Ping Activando ETH0 ... ");

                                // Si hay conexion al servidor, sigo cargando

                                AdministrarOTG(btSocket01.getOutputStream(), false); //CARGAR

                                util.sleep(36000);
                            }

                        } else {
                            Log.d(TAG,"ETH_HILO Conexion valida (1) ... ");

                            // Si hay conexion al servidor, sigo cargando

                            AdministrarOTG(btSocket01.getOutputStream(), false); //CARGAR

                            util.sleep(36000);
                        }

                    }
                } else {
                    Log.d(TAG,"ETH_HILO Eth0 Disabled ... ");

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
                            Log.wtf(TAG,"Fechahora_HILO ERROR EN SET TEXT txcHora... " +e.getMessage());
                        }
                    }
                });
                util.sleep(250);
            }
        }
    });


    Thread threadStatusADC = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try{
                    statusADC("");
                    Thread.sleep(1000);
                }catch (Exception e){
                    try{
                        Thread.sleep(1000);
                    }catch (Exception ex){
                    }
                    Log.e(TAG,"threadStatusADC " + e.getMessage());
                }
            }
        }
    });


    Thread threadTestingBTonoff = new Thread(new Runnable() {
        @Override
        public void run() {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            internalFile.writeToAppend(fechahora.getFechahora() + ": " + ">>>>>>>>>>>>> Iniciando threadTestingBTonoff >>>>>>>>>>> ",Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
            internalFile.writeToAppend(fechahora.getFechahora() + ": " + ">>>>>>>>>>>>> MAC_BT_00: " + MAC_BT_00,Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
            internalFile.writeToAppend(fechahora.getFechahora() + ": " + ">>>>>>>>>>>>> MAC_BT_01: " + MAC_BT_01,Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
            internalFile.writeToAppend(fechahora.getFechahora() + ": " + ">>>>>>>>>>>>> MAC_BT_02: " + MAC_BT_02,Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
            internalFile.writeToAppend(fechahora.getFechahora() + ": " + ">>>>>>>>>>>>> MAC_BT_03: " + MAC_BT_03,Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");

            while (true) {
                try{
                    Log.v(TAG,"threadTestingBTonoff Esperando para apagar BTs");
                    Thread.sleep(180000);
                    internalFile.writeToAppend(fechahora.getFechahora() + ": " + "---------- Iniciando desconexion de bluetooth ------------- ",Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");

                    Log.v(TAG,"threadTestingBTonoff Desactivando BTs");
                    if(bluetoothAdapter.disable()){
                        internalFile.writeToAppend(fechahora.getFechahora() + ": " + "bluetoothAdapter.disable() ",Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
                        bluetoothAdapter.enable();
                        internalFile.writeToAppend(fechahora.getFechahora() + ": " + "bluetoothAdapter.enable() ",Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
                        Log.v(TAG,"threadTestingBTonoff Activando BTs");
                    }else{
                        Log.v(TAG,"threadTestingBTonoff no se puede desactivar BTs");
                    }

                    /*
                    Log.v(TAG,"threadTestingBTonoff Apagando BTs");
                    btSocket01.closeBT();
                    internalFile.writeToAppend(fechahora.getFechahora() + ": " + "btSocket01.closeBT() ",Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
                    btSocket02.closeBT();
                    internalFile.writeToAppend(fechahora.getFechahora() + ": " + "btSocket02.closeBT() ",Environment.getExternalStorageDirectory().toString() + "/tempus/" + "blueetoothonoff.txt");
                    */
                }catch (Exception e){
                    Log.e(TAG,"threadTestingBTonoff " + e.getMessage());
                }
            }

        }
    });


    Thread LookErrorApp = new Thread(new Runnable() {
        @Override
        public void run() {

            int indexFor = 0;
            //Activity activity;
            ActivityManager manager;
            int condicionApp = -1;
            List<ActivityManager.ProcessErrorStateInfo> stateInRun;
            //List<ActivityManager.RunningAppProcessInfo> infoRunProcess;
            String app;

            manager = (ActivityManager) ActivityPrincipal.this.getSystemService(Context.ACTIVITY_SERVICE);

            while (true) {

                try {
                    Thread.sleep(1000);
                    Log.v(TAG,"LookErrorApp - Inicio de Revisión ");
                    stateInRun = manager.getProcessesInErrorState();
                    Log.v(TAG,"stateInRun " + stateInRun.toString());
                    if (stateInRun != null) {
                        Log.v(TAG,"stateInRun " + stateInRun.toString());
                        if (stateInRun.size() > 0) {
                            for (indexFor = 0; indexFor < stateInRun.size(); indexFor++) {
                                condicionApp = stateInRun.get(indexFor).condition;
                                //obteniendo el nombre de la app para forzar el cierre
                                app=stateInRun.get(indexFor).processName;
                                switch (condicionApp) {
                                    case ActivityManager.ProcessErrorStateInfo.CRASHED:
                                        Log.v(TAG,"LookErrorApp = " + ActivityManager.ProcessErrorStateInfo.CRASHED);
                                        appCaida(app);
                                        break;
                                    case ActivityManager.ProcessErrorStateInfo.NOT_RESPONDING:
                                        Log.v(TAG,"LookErrorApp = " + ActivityManager.ProcessErrorStateInfo.NOT_RESPONDING);
                                        appNoResponde(app);
                                        break;
                                    case ActivityManager.ProcessErrorStateInfo.NO_ERROR:
                                        Log.v(TAG,"LookErrorApp = " + ActivityManager.ProcessErrorStateInfo.NO_ERROR);
                                        break;
                                }
                            }
                        }
                    }else{
                        Log.v(TAG,"stateInRun " + stateInRun.toString());
                        //Thread.sleep(1000);
                    }
                    Log.v(TAG,"LookErrorApp - Fin de Revisión");
                } catch (InterruptedException e) {
                    Log.e(TAG,"LookErrorApp " + e.getMessage());
                }



            }

        }
    });

    private void appCaida(String app) {
        Log.e( TAG, "appCaida: Preparando para cerrar"  );
        this.forceStop(app);
    }

    private void appNoResponde(String app) {
        Log.e( TAG, "appNoResponde: Preparando para cerrar" );
        this.forceStop(app);
    }

    private void forceStop(String app) {

        try {
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

            os.writeBytes("adb shell" + "\n");
            os.flush();
            os.writeBytes("am force-stop " + app + "\n");
            os.flush();
            os.close();
            suProcess.waitFor();
        } catch (IOException e) {
            Log.e(TAG,"forceStop IOException " + e.getMessage());
            //Toast.makeText(ActivityPrincipal.context, ex.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Log.e(TAG,"forceStop SecurityException " + e.getMessage());
            //Toast.makeText(ActivityPrincipal.context, "Can't get root access2", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG,"forceStop Exception " + e.getMessage());
            //Toast.makeText(ActivityPrincipal.context, "Can't get root access3", Toast.LENGTH_LONG).show();
        }
    }


    private void statusADC(String status){
        if(status.equalsIgnoreCase("")){
            try{
                btSocket01.getOutputStream().write(util.hexStringToByteArray("244F41584100134430303030303030303030303030303030" + util.getChecksum("244F41584100134430303030303030303030303030303030",4) + "41"));
                //Thread.sleep(100);
            }catch (Exception e){
                Log.e(TAG,"statusADC write " + e.getMessage());
                ExternalEnergy = -1;
                UPSCharge = -1;
                levelUPS = -1;
                turnOnRelay02 = -1;
                turnOnRelay01 = -1;
                turnOnLan = -1;
                turnOnAndroid = -1;
                turnOnSuprema = -1;
                turnOnLectorBarra = -1;
                turnOnProximidad = -1;
                turnOnExternalEnergy = -1;
            }
            //longitud es de 88
        }else if(status.length()>80){
            try{
                //244f4158410036000000000000000000000044bcba4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e3131313131314e303041
                //000000000000000000000044bcba4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e3131313131314e

                //Log.v(TAG,"ExternalEnergy=" + util.convertHexToDecimal(status.substring(24,26)) * 0.0613725490196078); // (x * 5 / 255) * 3.13 -> voltios
                //Log.v(TAG,"UPSCharge=" + util.convertHexToDecimal(status.substring(26,28)) * 0.0613725490196078); // (x * 5 / 255) * 3.13 -> voltios
                //Log.v(TAG,"turnOnLan=" + util.convertHexToString(status.substring(74,76)));
                //Log.v(TAG,"turnOnAndroid=" + util.convertHexToString(status.substring(76,78)));
                //Log.v(TAG,"turnOnSuprema=" + util.convertHexToString(status.substring(78,80)));
                //Log.v(TAG,"turnOnLectorBarra=" + util.convertHexToString(status.substring(80,82)));
                //Log.v(TAG,"turnOnProximidad=" + util.convertHexToString(status.substring(82,84)));
                //Log.v(TAG,"turnOnExternalEnergy=" + util.convertHexToString(status.substring(84,86)));

                ExternalEnergy = util.convertHexToDecimal(status.substring(24,26)) * 0.0613725490196078;
                UPSCharge = util.convertHexToDecimal(status.substring(26,28)) * 0.0613725490196078;
                levelUPS = ((UPSCharge-9) * 100)/3.3;
                turnOnRelay02 = Integer.valueOf(util.convertHexToString(status.substring(70,72)));
                turnOnRelay01 = Integer.valueOf(util.convertHexToString(status.substring(72,74)));
                turnOnLan = Integer.valueOf(util.convertHexToString(status.substring(74,76)));
                turnOnAndroid = Integer.valueOf(util.convertHexToString(status.substring(76,78)));
                turnOnSuprema = Integer.valueOf(util.convertHexToString(status.substring(78,80)));
                turnOnLectorBarra = Integer.valueOf(util.convertHexToString(status.substring(80,82)));
                turnOnProximidad = Integer.valueOf(util.convertHexToString(status.substring(82,84)));
                turnOnExternalEnergy = Integer.valueOf(util.convertHexToString(status.substring(84,86)));

                //Log.v(TAG,"ExternalEnergy=" + ExternalEnergy); // (x * 5 / 255) * 3.13 -> voltios
                //Log.v(TAG,"UPSCharge=" + UPSCharge); // (x * 5 / 255) * 3.13 -> voltios
                //Log.v(TAG,"levelUPS=" + levelUPS); // (x * 5 / 255) * 3.13 -> voltios
                //Log.v(TAG,"turnOnRelay02=" + turnOnRelay02);
                //Log.v(TAG,"turnOnRelay01=" + turnOnRelay01);
                //Log.v(TAG,"turnOnLan=" + turnOnLan);
                //Log.v(TAG,"turnOnAndroid=" + turnOnAndroid);
                //Log.v(TAG,"turnOnSuprema=" + turnOnSuprema);
                //Log.v(TAG,"turnOnLectorBarra=" + turnOnLectorBarra);
                //Log.v(TAG,"turnOnProximidad=" + turnOnProximidad);
                //Log.v(TAG,"turnOnExternalEnergy=" + turnOnExternalEnergy);

                Log.v(TAG,"parametersInsertMarcaciones = " + parametersInsertMarcaciones);
                Log.v(TAG,"parameterMarcacionRepetida = " + parameterMarcacionRepetida);
            }catch (Exception e){
                Log.e(TAG,"statusADC status" + e.getMessage());
            }
        }else{
            ExternalEnergy = -1;
            UPSCharge = -1;
            levelUPS = -1;
            turnOnRelay02 = -1;
            turnOnRelay01 = -1;
            turnOnLan = -1;
            turnOnAndroid = -1;
            turnOnSuprema = -1;
            turnOnLectorBarra = -1;
            turnOnProximidad = -1;
            turnOnExternalEnergy = -1;
        }

    }

    private void keytoFlg_Actividad(String trama){
        try{

            Thread setFlg_Actividad = new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                turnOnScreen();
                                manageAccessButtons(false);
                                manageLayerMarcacion(false);

                                txvMensajePantalla.setText("PASE SU TARJ/BIOM");
                                if(flag.equalsIgnoreCase("001")){
                                    actualizarFlag("001", btnEvent01);
                                }else if(flag.equalsIgnoreCase("002")){
                                    actualizarFlag("002", btnEvent02);
                                }
                                TIEMPO_ACTIVO = 6;
                            } catch (Exception e) {
                                Log.e(TAG,"setFlg_Actividad " + e.getMessage());
                            }
                        }
                    });
                }
            });
            //244f41584100360000000000000000000000 06 30303030303030 31 303030303030303030303030303030303030303030303030303041
            //00000000000000000000000630303030303030 31 303030303030303030303030303030303030303030303030303041
            flag = "00" + util.convertHexToString(trama.substring(38,40));
            Log.v(TAG,"keytoFlg_Actividad -> Flg_Actividad " + flag );
            //Log.v(TAG, "Flg_Actividad: " + flag);
            setFlg_Actividad.start();
            //Log.v(TAG,"keytoFlg_Actividad fin");
        }catch (Exception e){
            Log.e(TAG,"keytoFlg_Actividad trama " + e.getMessage());
        }
    }

    public static void setImageBitmapOnImageView(ImageView imageView, String ruta, String filename){

        try{
            Log.v(TAG,"setImageBitmapOnImageView " + Environment.getExternalStorageDirectory().toString() + ruta + filename);
            imageView.setImageBitmap(BitmapFactory.decodeFile(new  File(Environment.getExternalStorageDirectory().toString() + ruta + filename).toString()));
        }catch (Exception e){
            Log.e(TAG,"setImageBitmapOnImageView " + e.getMessage());
        }

    }

    public static void setBackgroundColorOnTextView(TextView textView, String color, String colorex){
        try{
            Log.v(TAG,"setBackgroundColorOnTextView ");
            textView.setBackgroundColor(Color.parseColor(color));
        }catch (Exception e){
            textView.setBackgroundColor(Color.parseColor(colorex));
            Log.e(TAG,"setBackgroundColorOnTextView " + e.getMessage());
        }

    }

    public static void setTextColorOnTextView(TextView textView, String color, String colorex){
        try{
            Log.v(TAG,"setTextColorOnTextView ");
            textView.setTextColor(Color.parseColor(color));
        }catch (Exception e){
            textView.setTextColor(Color.parseColor(colorex));
            Log.e(TAG,"setTextColorOnTextView " + e.getMessage());
        }

    }


    private void runShellCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            Log.v(TAG,"runShellCommand ejecutado");
        } catch (InterruptedException e) {
            Log.e(TAG, "runShellCommand: " + e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, "runShellCommand: " + e.getMessage(), e);
        }
    }


}

