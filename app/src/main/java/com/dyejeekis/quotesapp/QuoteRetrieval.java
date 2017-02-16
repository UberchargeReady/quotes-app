package com.dyejeekis.quotesapp;

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

    public static void randomQuote(final MainActivity activity, OkHttpClient client) {
        Request request = new Request.Builder().url(BASE_URL + RANDOM_QUOTE_ENDPOINT).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Util.displayShortToast(activity, "Error retrieving quote");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    activity.addQuote(parseResponse(response));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Util.displayShortToast(activity, "Error parsing quote data");
                }
            }
        });
    }

    public static Quote parseResponse(Response response) throws IOException, JSONException {
        String jsonData = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonData);
        return new Quote(jsonObject.getString("quoteText").trim(), jsonObject.getString("quoteAuthor").trim());
    }

}
