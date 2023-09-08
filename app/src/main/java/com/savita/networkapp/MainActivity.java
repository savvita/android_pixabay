package com.savita.networkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.textfield.TextInputEditText;
import com.savita.networkapp.adapters.PixabayImageAdapter;
import com.savita.networkapp.controllers.PixabayController;
import com.savita.networkapp.models.PixabayImage;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity_tag";
    private List<PixabayImage> images = new ArrayList<>();
    private PixabayImageAdapter adapter;
    private GridView galleryView;

    private ProgressBar progressBar;
    private TextInputEditText searchInput;
    private ImageButton searchBtn;
    public static final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        galleryView = findViewById(R.id.gallery_view);
        adapter = new PixabayImageAdapter(this, R.layout.gallery_item_view, images);
        galleryView.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
        searchInput = findViewById(R.id.search_text_input);
        searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(view -> refresh());
    }

//    @Override
//    protected void onResume() {
//        refresh();
//        super.onResume();
//    }

    private void refresh() {
        String search = searchInput.getText().toString().trim();
        if(search.length() == 0) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<PixabayImage> hits = PixabayController.getImages(search);
            progressBar.post(() -> progressBar.setVisibility(View.GONE));
            images.clear();
            images.addAll(hits);
            galleryView.post(() -> adapter.notifyDataSetChanged());
        }).start();
    }
}