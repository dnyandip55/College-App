package com.example.collegeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class FullImageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);

        String imageUrl=getIntent().getStringExtra("image");
        PhotoView photoView = findViewById(R.id.photoView);

        Glide.with(this).load(imageUrl).into(photoView);
    }
}