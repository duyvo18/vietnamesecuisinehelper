package com.example.vietnamesecuisinehelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // TODO: Activity transition
    public void OnClickMainMenu(View view) {
        switch(view.getId())
        {
            case R.id.Btn_ToCamera:
                // TODO: Open camera to take new picture
                dispatchTakePictureIntent();
                break;
            case R.id.Btn_ToGallery:
                // TODO: Open Gallery to select picture
                choosePictureGallery();
                break;
            case R.id.Btn_ToList:
                // TODO: Open list of Vietnamese food
                break;
        }
    }

    private void choosePictureGallery() {
        Intent intent = new Intent(MainActivity.this, ImageChoose.class);
        startActivity(intent);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            //startActivity(takePictureIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}