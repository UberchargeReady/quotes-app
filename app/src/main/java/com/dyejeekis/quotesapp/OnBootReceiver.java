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
        boolean flag = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dailyQuotes", false);
        if(flag) {
            Util.setDailyQuotesActive(context, true);
        }
    }
}
