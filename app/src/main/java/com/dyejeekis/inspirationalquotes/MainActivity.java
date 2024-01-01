package com.dyejeekis.inspirationalquotes;

import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    TextView textQuote;
    TextView textAuthor;
    FloatingActionButton fabShare;
    Button buttonNext;
    Button buttonPrevious;
    CheckBox checkBoxDaily;
    ProgressBar progressBar;

    private OkHttpClient okHttpClient;
    private PlayIntegrityHelper playIntegrityHelper;
    private int currentKey;
    private List<Quote> quoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initHttpClient();
        initPlayIntegrity();
        quoteList = new ArrayList<>();
        currentKey = 0;
        randomQuote();
    }

    private void initViews() {
        textQuote = findViewById(R.id.textView_quote);
        textAuthor = findViewById(R.id.textView_author);
        fabShare = findViewById(R.id.fab_share);
        buttonNext = findViewById(R.id.button_next);
        buttonPrevious = findViewById(R.id.button_previous);
        checkBoxDaily = findViewById(R.id.checkBox_daily_quotes);
        progressBar = findViewById(R.id.progressBar);

        fabShare.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        checkBoxDaily.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dailyQuotes", false));
        checkBoxDaily.setOnCheckedChangeListener(this);
    }

    private void initHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        okHttpClient = builder.build();
    }

    private void initPlayIntegrity() {
        playIntegrityHelper = new PlayIntegrityHelper(this);
        playIntegrityHelper.prepareTokenProvider(467253172853L);
    }

    public void addQuote(final Quote quote) {
        if (checkIsQuoteNull(quote)) return;
        if (checkIsExistingQuote(quote)) return;
        quoteList.add(quote);
        currentKey = quoteList.size()-1;
        updateQuoteMainThread(getCurrentQuote());
    }

    private boolean checkIsQuoteNull(final Quote quote) {
        if (quote == null) {
            Util.displaySnackbar(
                    this,
                    "Oops, something went wrong! Try again maybe?",
                    Snackbar.LENGTH_SHORT
            );
            return true;
        }
        return false;
    }

    private boolean checkIsExistingQuote(final Quote newQuote) {
        try {
            for (Quote quote : quoteList) {
                if (quote.getQuoteText().equals(newQuote.getQuoteText())) {
                    Util.displaySnackbar(
                            this,
                            "Looking for new quotes. Give it a sec!",
                            Snackbar.LENGTH_LONG
                    );
                    return true;
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return false;
    }

    /**
     * only run on ui thread
     * @param newQuote
     */
    public void updateQuote(final Quote newQuote) {
        setProgressBarVisible(false);
        textQuote.setText(newQuote.getQuoteText());
        SpannableString authorSpannable = new SpannableString(newQuote.getQuoteAuthor());
        authorSpannable.setSpan(new UnderlineSpan(), 0, newQuote.getQuoteAuthor().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textAuthor.setText(authorSpannable);
        textAuthor.setOnClickListener(MainActivity.this);
        buttonPrevious.setEnabled(currentKey > 0);
    }

    public void updateQuoteMainThread(final Quote newQuote) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateQuote(newQuote);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab_share) {
            startActivity(getCurrentQuote().getShareIntent());
        }
        else if(v.getId() == R.id.textView_author) {
            try {
                startActivity(getCurrentQuote().getAuthorWikiIntent());
            } catch (Exception e) {
                e.printStackTrace();
                Util.displayShortToast(this, "Error creating wiki URL");
            }
        }
        else if(v.getId() == R.id.button_next) {
            if(currentKey == quoteList.size()-1 || quoteList.isEmpty()) {
                randomQuote();
            }
            else if(!quoteList.isEmpty()){
                nextQuote();
            }
        }
        else if(v.getId() == R.id.button_previous) {
            previousQuote();
        }
    }

    private void nextQuote() {
        currentKey++;
        updateQuote(quoteList.get(currentKey));
    }

    private void previousQuote() {
        if(currentKey > 0) {
            currentKey--;
            updateQuote(quoteList.get(currentKey));
        }
    }

    private void randomQuote() {
        setProgressBarVisible(true);
        QuoteRetrieval.randomQuote(this, okHttpClient);
    }

    /**
     * only run on ui thread
     * @param flag
     */
    public void setProgressBarVisible(boolean flag) {
        progressBar.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    private Quote getCurrentQuote() {
        return quoteList.get(currentKey);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.checkBox_daily_quotes) {
            Util.setDailyQuotesActive(this, isChecked);
        }
    }

}
