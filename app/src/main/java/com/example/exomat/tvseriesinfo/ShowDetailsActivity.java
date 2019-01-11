package com.example.exomat.tvseriesinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exomat.tvseriesinfo.pojo.TvShowResult;
import com.squareup.picasso.Picasso;

public class ShowDetailsActivity extends AppCompatActivity {
    private boolean ifFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        TvShowResult show = (TvShowResult) getIntent().getSerializableExtra("Show");
        Log.i("SDAI", "TVShow to display: " + show.toString());
        TextView name = findViewById(R.id.tvShowNameText);
        TextView premiere = findViewById(R.id.premiereText);
        TextView status = findViewById(R.id.statusText);
        TextView summary = findViewById(R.id.showSummaryText);
        ImageView image = findViewById(R.id.imageTVView);
        String originalImagePath = show.getShow().getImage().getOriginal();

        if (!originalImagePath.isEmpty())
            Picasso.with(getApplicationContext()).load(originalImagePath).into(image);

        name.setText(show.getShow().getName());
        premiere.setText(show.getShow().getPremiered());
        status.setText(show.getShow().getStatus());
        summary.setText(show.getShow().getSummary().replaceAll("(<[^>]+>)", ""));
        summary.setMovementMethod(new ScrollingMovementMethod());
        final ImageButton favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ifFavorite) {
                    ifFavorite = true;
                    favoriteButton.setBackgroundResource(R.drawable.likefull);
                    Toast.makeText(ShowDetailsActivity.this, "Tv Series add to favorite :)", Toast.LENGTH_SHORT).show();
                } else {
                    ifFavorite = false;
                    favoriteButton.setBackgroundResource(R.drawable.likeempty);
                    Toast.makeText(ShowDetailsActivity.this, "Tv Series remove from favorite :)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
