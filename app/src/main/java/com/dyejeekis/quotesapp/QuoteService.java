package com.dyejeekis.quotesapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import okhttp3.OkHttpClient;

/**
 * Created by George on 2/17/2017.
 */

public class QuoteService extends IntentService {

    public static final String TAG = "QuoteService";

    public static final int QUOTE_NOTIF_ID = 12345;

    public static final int DAILY_QUOTE_REQUEST_CODE = 91824;

    public static final int SHARE_QUOTE_REQUEST_CODE = 91825;

    public static final int AUTHOR_WIKI_REQUEST_CODE = 91826;

    public QuoteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Quote quote = QuoteRetrieval.randomQuote(new OkHttpClient());
            showQuoteNotification(quote);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showQuoteNotification(Quote quote) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        PendingIntent shareIntent = PendingIntent.getActivity(this, SHARE_QUOTE_REQUEST_CODE, quote.getShareIntent(), PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent authorIntent = PendingIntent.getActivity(this, AUTHOR_WIKI_REQUEST_CODE, quote.getAuthorWikiIntent(), PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentTitle("Quote of the day")
                .setSmallIcon(R.drawable.ic_format_quote_white_18dp)
                .addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_share_grey_600_24dp, "Share", shareIntent))
                .addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_person_grey_600_24dp, "Author", authorIntent))
                .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(quote.getCompleteQuote()));

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(QUOTE_NOTIF_ID, builder.build());
    }

}
