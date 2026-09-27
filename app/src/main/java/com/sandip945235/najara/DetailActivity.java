package com.sandip945235.najara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    String title, poster, category, embedLink, downloadLink,
           rating, print, industry, language, quality;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_detail);

        title = getIntent().getStringExtra("title");
        poster = getIntent().getStringExtra("poster");
        category = getIntent().getStringExtra("category");
        embedLink = getIntent().getStringExtra("embedLink");
        downloadLink = getIntent().getStringExtra("downloadLink");
        rating = getIntent().getStringExtra("rating");
        print = getIntent().getStringExtra("print");
        industry = getIntent().getStringExtra("industry");
        language = getIntent().getStringExtra("language");
        quality = getIntent().getStringExtra("quality");

        ImageView header = findViewById(R.id.headerImage);
        ImageView sideThumb = findViewById(R.id.sideThumb);
        TextView titleTxt = findViewById(R.id.titleTxt);
        TextView printTxt = findViewById(R.id.printTxt);
        TextView industryTxt = findViewById(R.id.industryTxt);
        TextView categoryTxt = findViewById(R.id.categoryTxt);
        TextView languageTxt = findViewById(R.id.languageTxt);
        TextView qualityTxt = findViewById(R.id.qualityTxt);
        TextView ratingTxt = findViewById(R.id.ratingTxt);
        Button watchNow = findViewById(R.id.watchNowBtn);
        View thumbnailContainer = findViewById(R.id.thumbnailContainer);

        titleTxt.setText(title);
        printTxt.setText(print);
        industryTxt.setText(industry);
        categoryTxt.setText(category);
        languageTxt.setText(language);
        qualityTxt.setText(quality);
        ratingTxt.setText("★ " + rating);

        Glide.with(this).load(poster).into(header);
        Glide.with(this).load(poster).into(sideThumb);

        thumbnailContainer.setOnClickListener(v -> openPlayer());
        watchNow.setOnClickListener(v -> openPlayer());
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        findViewById(R.id.shareBtn).setOnClickListener(v -> {
            Intent s = new Intent(Intent.ACTION_SEND);
            s.setType("text/plain");
            s.putExtra(Intent.EXTRA_TEXT, "Watch \"" + title + "\" on NAJARA!");
            startActivity(Intent.createChooser(s, "Share via"));
        });

        findViewById(R.id.downloadBtn).setOnClickListener(v -> {
            if (downloadLink != null && !downloadLink.isEmpty()) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadLink));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Cannot open download link",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Download link उपलब्ध नहीं",
                        Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.likeBtn).setOnClickListener(v ->
                Toast.makeText(this, "❤️ Liked!", Toast.LENGTH_SHORT).show());

        findViewById(R.id.myListBtn).setOnClickListener(v ->
                Toast.makeText(this, "➕ Added to My List",
                        Toast.LENGTH_SHORT).show());
    }

    private void openPlayer() {
        if (embedLink == null || embedLink.isEmpty()) {
            Toast.makeText(this, "Video link उपलब्ध नहीं",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this, PlayerActivity.class);
        i.putExtra("url", embedLink);
        i.putExtra("title", title);
        i.putExtra("downloadLink", downloadLink);
        startActivity(i);
    }
                                          }
