package com.amoozeshmelli;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        Bundle extras = getIntent().getExtras();
        String url = null;
        if (extras != null) {
            url = extras.getString("image");
        }
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

        Glide.with(getApplicationContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);

    }
}
