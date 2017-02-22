package com.dyejeekis.quotesapp;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by George on 2/16/2017.
 */

public class QuoteRetrieval {

    public static final String BASE_URL = "http://api.forismatic.com/api/1.0/";

    public static final String RANDOM_QUOTE_ENDPOINT = "?method=getQuote&lang=en&format=json";

    /**
     * runs asynchronously on main activity
     * @param activity
     * @param client
     */
    public static void randomQuote(final MainActivity activity, OkHttpClient client) {
        Request request = new Request.Builder().url(BASE_URL + RANDOM_QUOTE_ENDPOINT).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                retrievalError(activity, "Error retrieving quote");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    activity.addQuote(parseResponse(response));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    retrievalError(activity, "Error parsing quote data");
                }
            }
        });
    }

    /**
     * runs synchronously
     * @param client
     * @return
     */
    public static Quote randomQuote(OkHttpClient client) throws IOException, JSONException {
        Request request = new Request.Builder().url(BASE_URL + RANDOM_QUOTE_ENDPOINT).build();
        return parseResponse(client.newCall(request).execute());
    }

    public static Quote parseResponse(Response response) throws IOException, JSONException {
        String jsonData = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonData);
        return new Quote(jsonObject.getString("quoteText").trim(), jsonObject.getString("quoteAuthor").trim());
    }

    private static void retrievalError(final MainActivity activity, String reason) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setProgressBarVisible(false);
            }
        });
        Util.displayShortToast(activity, reason);
    }

}
