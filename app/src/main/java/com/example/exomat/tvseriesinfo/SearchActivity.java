package com.example.exomat.tvseriesinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.exomat.tvseriesinfo.pojo.TvShowResult;
import com.example.exomat.tvseriesinfo.requester.SearchRequester;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ListView listView;
    ListSearchAdapter listSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = findViewById(R.id.listViewSearch);
        listSearchAdapter = new ListSearchAdapter(new ArrayList<TvShowResult>(), this);
        final Context context = getApplicationContext();
        listView.setAdapter(listSearchAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TvShowResult itemAtPosition = (TvShowResult) listView.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ShowDetailsActivity.class);
                intent.putExtra("Show", itemAtPosition);
                startActivity(intent);

            }
        });
        final EditText searchText = findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmHandler(searchText, context);
            }
        });
        final ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmHandler(searchText, context);

            }
        });
    }

    private void confirmHandler(EditText searchText, Context context) {
        String search = searchText.getText().toString();
        if (search.isEmpty()) {
            return;
        }
        SearchRequester.searchRequest(search, listView, context);
    }
}
