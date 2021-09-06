package com.example.vietnamesecuisinehelper;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageChoose extends AppCompatActivity {

    private static final String ROOT_URL = "http://192.168.1.2:8000/returnfoodname";

    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1 ;
    int REQUEST_IMAGE_CAPTURE = 2;

    private Bitmap bitmap;

    ImageView imageView;
    ImageView imgSampleOne;
    ImageView imgSampleTwo;
    ImageView imgSampleThree;
    
    TextView textView;
    TextView textViewPlaceHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_gallery);

        imageView =  findViewById(R.id.imageView);
        textView =  findViewById(R.id.recognitionName1);
        textViewPlaceHolder = (TextView) findViewById(R.id.tvPlaceholder);
        imgSampleOne = findViewById(R.id.imgSampleOne);
        imgSampleTwo = findViewById(R.id.imgSampleTwo);
        imgSampleThree = findViewById(R.id.imgSampleThree);

        imgSampleOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSingleImage(imgSampleOne);
            }
        });
        imgSampleTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSingleImage(imgSampleTwo);
            }
        });
        imgSampleThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSingleImage(imgSampleThree);
            }
        });

        findViewById(R.id.gallery).setOnClickListener(view -> {
            if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE))) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            REQUEST_PERMISSIONS);
                }
            } else {
                showFileChooser();
            }
        });

        findViewById(R.id.captureImageFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void uploadSingleImage(ImageView imageView1){
        BitmapDrawable drawable = (BitmapDrawable) imageView1.getDrawable();
        bitmap = drawable.getBitmap();

        imageView.setImageBitmap(bitmap);

        textViewPlaceHolder.setVisibility(View.INVISIBLE);

        uploadImage(bitmap);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            Uri picUri = data.getData();
            String filePath = picUri.getPath();

            if (filePath != null) {
                try {
                    textView.setText("File Selected");

                    Log.d("@@@ filePath", String.valueOf(filePath));

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    imageView.setImageBitmap(bitmap);
                    textViewPlaceHolder.setVisibility(View.INVISIBLE);

                    uploadImage(bitmap);
                } catch (IOException e) {
                    Toast.makeText(this, ROOT_URL, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(ImageChoose.this,"No image selected",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null ) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");

            textView.setText("File Selected");
            imageView.setImageBitmap(bitmap);
            textViewPlaceHolder.setVisibility(View.INVISIBLE);

            uploadImage(bitmap);
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }//javx.net.ssl.SSLHandshakeException: Handshake failed

    public void uploadImage(Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            textView.setText(new String(response.data));
                            Toast.makeText(getApplicationContext(),"Upload Successfully!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Upload Failed!",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },//11.155.252.101
                error -> {
                    Toast.makeText(this,"Unexpected response: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                })
        {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                long imageName = System.currentTimeMillis();

                params.put("image", new DataPart(imageName + ".png",
                        getFileDataFromDrawable(bitmap)));

                return params;
            }
        };

        Toast.makeText(this,"Sending image, please wait for response from server...",
                Toast.LENGTH_LONG).show();

        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
    }

    public void onClickMapBtn(View view) {
        startActivity(new Intent(this, MapActivity.class)
            .putExtra("food_name", textView.getText().toString()));
    }
}
