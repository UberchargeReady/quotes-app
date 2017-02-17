package com.dyejeekis.quotesapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

/**
 * Created by George on 2/17/2017.
 */

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dailyQuotes", false)) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent quoteIntent = new Intent(context, QuoteService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, QuoteService.DAILY_QUOTE_REQUEST_CODE,
                    quoteIntent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
