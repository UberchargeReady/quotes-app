package com.dyejeekis.quotesapp;

import android.content.Intent;

/**
 * Created by George on 2/16/2017.
 */

public class Quote {

    private String quoteText, quoteAuthor;

    public Quote(String quoteText, String quoteAuthor) {
        this.quoteText = quoteText;
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteText() {
        return "\"" + quoteText + "\"";
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public String getAuthorWikiUrl() {
        return "https://en.wikipedia.org/wiki/" + quoteAuthor.replace(" ", "_").replace(".", "%2E");
    }

    public String getCompleteQuote() {
        return "\"" + quoteText + "\"" + " -" + quoteAuthor;
    }

    public Intent getShareIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getCompleteQuote());
        return Intent.createChooser(intent, "Share via");
    }
}
