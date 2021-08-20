package com.example.vietnamesecuisinehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

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