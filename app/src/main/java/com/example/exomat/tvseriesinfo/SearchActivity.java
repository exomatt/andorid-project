package com.example.exomat.tvseriesinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

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
        listSearchAdapter = new ListSearchAdapter(new ArrayList<TVShow>(), this);
        listView.setAdapter(listSearchAdapter);
        final EditText searchText = findViewById(R.id.searchText);
        final ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString();
                if (search.isEmpty()) {
                    return;
                }
                SearchRequester.searchRequest("arrow");

            }
        });
    }
}
