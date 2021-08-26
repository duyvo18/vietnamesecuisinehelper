package com.example.vietnamesecuisinehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

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
                choosePictureGallery();
                break;
            case R.id.Btn_ToList:
                // TODO: Replace tmp function
                tmpMapTest();
                break;
        }
    }

    private void tmpMapTest() {
        startActivity(new Intent(this, MapActivity.class));
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