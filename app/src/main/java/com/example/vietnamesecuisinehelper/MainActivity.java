package com.example.vietnamesecuisinehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
                // TODO: Open camera to take new picture
                break;
            case R.id.Btn_ToGallery:
                // TODO: Open Gallery to select picture
                break;
            case R.id.Btn_ToList:
                // TODO: Open list of Vietnamese food
                break;
        }
    }
}