package com.tempus.proyectos.util.timerPicker;

/**
 * Created by gurrutiam on 13/09/2017.
 */

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.tempus.proyectos.tempusx.R;
import com.tempus.proyectos.util.timerPicker.TimePicker.OnTimeChangedListener;

public class MyTimePickerDialog extends AlertDialog implements OnClickListener, OnTimeChangedListener{

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */


    public interface OnTimeSetListener {

        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The hour that was set.
         * @param minute The minute that was set.
         */
        void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";
    private static final String IS_24_HOUR = "is24hour";

    private final TimePicker mTimePicker;
    private final OnTimeSetListener mCallback;
    private final Calendar mCalendar;
    private final java.text.DateFormat mDateFormat;

    int mInitialHourOfDay;
    int mInitialMinute;
    int mInitialSeconds;
    boolean mIs24HourView;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public MyTimePickerDialog(Context context,
                              OnTimeSetListener callBack,
                              int hourOfDay, int minute, int seconds, boolean is24HourView) {

        this(context, 0,
                callBack, hourOfDay, minute, seconds, is24HourView);
    }

    public MyTimePickerDialog(Context context,
                              OnTimeSetListener callBack,
                              int minute, int seconds, boolean is24HourView) {

        this(context, 0,
                callBack, minute, seconds, is24HourView);
    }

    public MyTimePickerDialog(Context context,
                              OnTimeSetListener callBack,
                              int seconds, boolean is24HourView) {

        this(context, 0,
                callBack, seconds, is24HourView);
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */


    public MyTimePickerDialog(Context context,
                              int theme,
                              OnTimeSetListener callBack,
                              int hourOfDay, int minute, int seconds, boolean is24HourView) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mInitialSeconds = seconds;
        mIs24HourView = is24HourView;

        mDateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();
        //updateTitle(mInitialHourOfDay, mInitialMinute, mInitialSeconds);

        setButton(context.getText(R.string.Aceptar), this);
        setButton2(context.getText(R.string.Cancelar), (OnClickListener) null);
        //setIcon(android.R.drawable.ic_dialog_time);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // initialize state
        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setCurrentSecond(mInitialSeconds);
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setOnTimeChangedListener(this);
    }

    public MyTimePickerDialog(Context context,
                              int theme,
                              OnTimeSetListener callBack,
                              int minute, int seconds, boolean is24HourView) {

        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        //mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mInitialSeconds = seconds;
        mIs24HourView = is24HourView;

        mDateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();
        //updateTitle(mInitialHourOfDay, mInitialMinute, mInitialSeconds);

        setButton(context.getText(R.string.Aceptar), this);
        setButton2(context.getText(R.string.Cancelar), (OnClickListener) null);
        //setIcon(android.R.drawable.ic_dialog_time);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // initialize state
        //mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setCurrentSecond(mInitialSeconds);
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setOnTimeChangedListener(this);
        mTimePicker.notHour();
    }

    public MyTimePickerDialog(Context context,
                              int theme,
                              OnTimeSetListener callBack,
                              int seconds, boolean is24HourView) {

        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        //mInitialHourOfDay = hourOfDay;
        //mInitialMinute = minute;
        mInitialSeconds = seconds;
        mIs24HourView = is24HourView;

        mDateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();
        //updateTitle(mInitialHourOfDay, mInitialMinute, mInitialSeconds);

        setButton(context.getText(R.string.Aceptar), this);
        setButton2(context.getText(R.string.Cancelar), (OnClickListener) null);
        //setIcon(android.R.drawable.ic_dialog_time);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // initialize state
        //mTimePicker.setCurrentHour(mInitialHourOfDay);
        //mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setCurrentSecond(mInitialSeconds);
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setOnTimeChangedListener(this);
        mTimePicker.notHour();
        mTimePicker.notMinute();
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                    mTimePicker.getCurrentMinute(), mTimePicker.getCurrentSeconds());
        }
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds) {
        updateTitle(hourOfDay, minute, seconds);
    }

    public void updateTime(int hourOfDay, int minutOfHour, int seconds) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minutOfHour);
        mTimePicker.setCurrentSecond(seconds);
    }

    private void updateTitle(int hour, int minute, int seconds) {
        // No actualizar el titulo con la HMS seleccionado
        /*
        String sHour = String.format("%02d", hour);
        String sMin = String.format("%02d", minute);
        String sSec = String.format("%02d", seconds);
        setTitle(sHour + ":" + sMin + ":" + sSec);
        */
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putInt(SECONDS, mTimePicker.getCurrentSeconds());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setCurrentSecond(seconds);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTimePicker.setOnTimeChangedListener(this);
        updateTitle(hour, minute, seconds);
    }

}
