package com.example.vietnamesecuisinehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClickMainMenu(View view) {
        switch(view.getId())
        {
            case R.id.galleryCamera:
                choosePictureGallery();
                break;
            case R.id.Btn_ToList:
                chooseListFood();
                break;
        }
    }

    private void choosePictureGallery() {
        Intent intent = new Intent(MainActivity.this, ImageChoose.class);
        startActivity(intent);
    }

    private void chooseListFood(){
        Intent intentListFood = new Intent(MainActivity.this, ListFood.class);
        startActivity(intentListFood);
    }
}