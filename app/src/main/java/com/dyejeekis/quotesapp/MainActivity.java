package com.dyejeekis.quotesapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.textView_quote) TextView textQuote;
    @BindView(R.id.textView_author) TextView textAuthor;
    @BindView(R.id.fab_share) FloatingActionButton fabShare;
    @BindView(R.id.button_next) Button buttonNext;
    @BindView(R.id.button_previous) Button buttonPrevious;
    @BindView(R.id.checkBox_daily_quotes) CheckBox checkBoxDaily;

    private OkHttpClient okHttpClient;
    private int currentKey;
    private List<Quote> quoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fabShare.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        checkBoxDaily.setOnCheckedChangeListener(this);

        okHttpClient = new OkHttpClient();
        quoteList = new ArrayList<>();
        currentKey = 0;
        randomQuote();
    }

    public void addQuote(final  Quote quote) {
        quoteList.add(quote);
        currentKey = quoteList.size()-1;
        updateQuote(getCurrentQuote());
    }

    public void updateQuote(final Quote newQuote) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textQuote.setText(newQuote.getQuoteText());
                SpannableString authorSpannable = new SpannableString(newQuote.getQuoteAuthor());
                authorSpannable.setSpan(new UnderlineSpan(), 0, newQuote.getQuoteAuthor().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                textAuthor.setText(authorSpannable);
                textAuthor.setOnClickListener(MainActivity.this);
                buttonPrevious.setEnabled(currentKey > 0);
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getCurrentQuote().getAuthorWikiUrl())));
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
        QuoteRetrieval.randomQuote(this, okHttpClient);
    }

    private Quote getCurrentQuote() {
        return quoteList.get(currentKey);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.checkBox_daily_quotes) {
            // TODO: 2/16/2017
        }
    }
}
