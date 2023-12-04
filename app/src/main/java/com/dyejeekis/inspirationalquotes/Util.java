package com.dyejeekis.inspirationalquotes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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

    public static void displaySnackbar(Activity activity, String text, int duration) {
        try {
            View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(view, text, duration).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDailyQuotesActive(Context context, boolean isActive) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("dailyQuotes", isActive);
        editor.apply();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, QuoteService.class);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                QuoteService.DAILY_QUOTE_REQUEST_CODE,
                intent,
                flags
        );

        if (isActive) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60 * 1000, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }
}
