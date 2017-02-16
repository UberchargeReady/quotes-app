package com.dyejeekis.quotesapp;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by George on 2/16/2017.
 */

public class Util {

    public static void displayShortToast(final Activity activity, final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
