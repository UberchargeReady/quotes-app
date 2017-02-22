package com.dyejeekis.quotesapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by George on 2/16/2017.
 */

public class Util {

    /**
     * only run on ui thread
     * @param activity
     * @param text
     */
    public static void displayShortToast(final Activity activity, final String text) {
        try {
            Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDailyQuotesActive(Context context, boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("dailyQuotes", flag);
        editor.apply();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, QuoteService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, QuoteService.DAILY_QUOTE_REQUEST_CODE,
                intent, PendingIntent.FLAG_ONE_SHOT);
        if(flag) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60 * 1000, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        else {
            alarmManager.cancel(pendingIntent);
        }
    }
}
