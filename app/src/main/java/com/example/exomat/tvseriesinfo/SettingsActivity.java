package com.example.exomat.tvseriesinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView textView = findViewById(R.id.textViewLink);
        TextView textView2 = findViewById(R.id.textViewApiInfo);
        Linkify.addLinks(textView, Linkify.WEB_URLS);
        Linkify.addLinks(textView2, Linkify.WEB_URLS);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
