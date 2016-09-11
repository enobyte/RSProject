package com.rsproject.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hari Hendryan on 08/09/2015.
 */
public class Utils {
    private Context _context;
    private String longitude, latitude;
    public Utils(Context context) {
        _context = context;
    }

     /*
    |-----------------------------------------------------------------------------------------------
    | Method for Location
    |-----------------------------------------------------------------------------------------------
    */
    public void setGeoLocation() {
        GPSTracker gpsTracker = new GPSTracker(_context);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            longitude = String.valueOf(gpsTracker.getLatitude());
            latitude  = String.valueOf(gpsTracker.getLongitude());
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public String getLongitude() {
        if (this.longitude != null) {
            return this.longitude;
        }
        return "";
    }

    public String getLatitude() {
        if (this.latitude != null) {
            return this.latitude;
        }
        return "";
    }

    /**
     * Method to get current date
     */
    public String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public String getCurrentDateandTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public Calendar getAfterCurrentOneDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, +0);

        return null;
    }

    /**
     * Method to convert format date from yyyy-MM-dd to dd/MM/yyyy
     */
    public static String formatDateReverse (String date) throws ParseException {
        String initDateFormat; String endDateFormat;
        initDateFormat="yyyy-MM-dd";
        endDateFormat= "dd/MM/yyyy";
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    public static String formatDateTimeReverse (String date) throws ParseException {
        String initDateFormat; String endDateFormat;
        initDateFormat="yyyy-MM-dd HH:mm";
        endDateFormat= "dd-MM-yyyy HH:mm";
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    /**
     * Method to convert format date from dd/MM/yyyy to yyyy-MM-dd
     */
    public static String formatDate (String date) throws ParseException {
        String initDateFormat; String endDateFormat;
        endDateFormat ="yyyy-MM-dd";
        initDateFormat= "dd/MM/yyyy";
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    /**
     * Method for set time field
     */
    public void setTimeField(Context context, TimePickerDialog timePickerDialog, TextView textView) {
        final TextView time = textView;
        Calendar newCalendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfDay) {
                time.setText(hourOfDay + ":" + minuteOfDay);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }

    /**
     * Method for set date field
     */
    public void setDateField(Context context, DatePickerDialog datePickerDialog, SimpleDateFormat dateFormatter,
                             TextView textView) {
        final TextView date                      = textView;
        dateFormatter                            = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        final SimpleDateFormat  simpleDateFormat = dateFormatter;

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(simpleDateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * Part of Floating Button library
     */
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
