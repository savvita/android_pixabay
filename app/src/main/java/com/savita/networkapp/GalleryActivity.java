package com.savita.networkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.savita.networkapp.controllers.PixabayController;
import com.savita.networkapp.models.PixabayImage;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    private Animation animFlipInForward;
    private Animation animFlipOutForward;
    private Animation animFlipInBackward;
    private Animation animFlipOutBackward;
    private ImageSwitcher imageSwitcher;

    private GestureDetector gestureDetector;
    private List<PixabayImage> hits;
    private int position;
    private static final String LOG_TAG = "GalleryActivity_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        imageSwitcher = findViewById(R.id.imageSwitcher);

        animFlipInForward = AnimationUtils.loadAnimation(this, R.anim.flipin);
        animFlipOutForward = AnimationUtils.loadAnimation(this, R.anim.flipout);
        animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
        animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);

        gestureDetector = new GestureDetector(getBaseContext(), simpleOnGestureListener);

        hits = PixabayController.getHits();
        Intent intent = getIntent();

        imageSwitcher.setFactory(() -> {
            ImageView imgVw= new ImageView(GalleryActivity.this);
            PixabayController.loadImage(hits.get(position).getLargeUrl(), imgVw);
            return imgVw;
        });

        if(intent.hasExtra(MainActivity.POSITION)) {
            position = intent.getIntExtra(MainActivity.POSITION, 0);
            if(position >= 0 && position < hits.size()) {
                Log.d(LOG_TAG, imageSwitcher.getCurrentView().getClass().toString());
                setImage();
            }
        }
    }

    private void setImage() {
        PixabayController.loadImage(hits.get(position).getLargeUrl(), (ImageView)imageSwitcher.getCurrentView());
    }

    private void swipeLeft() {
        imageSwitcher.setInAnimation(animFlipInBackward);
        imageSwitcher.setOutAnimation(animFlipOutBackward);
        if(position == 0) return;
        position--;
        setImage();
    }

    private void swipeRight() {
        imageSwitcher.setInAnimation(animFlipInForward);
        imageSwitcher.setOutAnimation(animFlipOutForward);
        if(position == hits.size() - 1) return;
        position++;
        setImage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float sensitivity = 50;
            if ((e1.getX() - e2.getX()) > sensitivity) {
                swipeLeft();
            } else if ((e2.getX() - e1.getX()) > sensitivity) {
                swipeRight();
            }
            return true;
        }
    };
}