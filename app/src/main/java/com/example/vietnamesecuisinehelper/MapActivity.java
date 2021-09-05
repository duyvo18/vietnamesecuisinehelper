// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.example.vietnamesecuisinehelper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int ALL_PERMISSION_REQUEST_CODE = 100;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;


    String food_name;

    ArrayList<String> deniedPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        food_name = this.getIntent().getStringExtra("food_name");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg_mapView);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        askForPermissions();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    private void askForPermissions() {
        ActivityCompat.requestPermissions(this,
                getPermissionList(), ALL_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != ALL_PERMISSION_REQUEST_CODE) {
            return;
        }

        getDeniedPermissions();
    }

    private void getDeniedPermissions() {
        deniedPermissions = new ArrayList<>();

        for (String permission : getPermissionList()) {
            if (ActivityCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if (deniedPermissions == null)
        {
            return;
        }
        else if (deniedPermissions.size() > 0)
        {
            showMissingPermissionsError(deniedPermissions);
        }
        else
        {
            enableMyLocation();
            searchForNearbyRestaurant(food_name);
        }
    }


    private void searchForNearbyRestaurant(String food_name) {
        if (ActivityCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }


        Task<Location> currentLocationTask = fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                new CancellationToken() {
                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }

                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }
                });

        currentLocationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    sendRequestToServer(food_name, (Location) task.getResult());
                }
            }
        });
    }

    private void sendRequestToServer(String food_name, Location curLoc) {
        StringBuilder urlBuilder = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?");

        urlBuilder.append("input=").append(food_name);
        urlBuilder.append("&inputtype=").append("textquery");
        urlBuilder.append("&fields=").append("formatted_address,name,rating,geometry");
        urlBuilder.append("&locationbias=").append("circle:5000")
                .append("@").append(curLoc.getLatitude())
                .append(",").append(curLoc.getLongitude());
        urlBuilder.append("&key=").append("AIzaSyAMCmyPwxfSMzksFw0jkMG_PcU9frcUIHg");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlBuilder.toString(),
                null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseLocationResult(response);
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error + "\n" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    private void parseLocationResult(JSONObject result) {
        String address, name;
        double rating;
        double lat, lng;

        try {
            if (result.getString("status").equalsIgnoreCase("OK")) {
                JSONArray jsonArray = result.getJSONArray("candidates");

                Toast.makeText(getBaseContext(), jsonArray.length() + " results found!",
                        Toast.LENGTH_LONG).show();

                map.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject place = jsonArray.getJSONObject(i);

                    address = place.getString("formatted_address");
                    name = place.getString("name");
                    rating = place.getDouble("rating");

                    lat = place.getJSONObject("geometry")
                            .getJSONObject("location")
                            .getDouble("lat");
                    lng = place.getJSONObject("geometry")
                            .getJSONObject("location")
                            .getDouble("lng");

                    LatLng latLng = new LatLng(lat, lng);

                    map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(name)
                    .snippet(address + "\nRating: " + rating + "/5"));
                }
            }
            else {
                String message = "Request ended unexpectedly\n" +
                        "Status: " + result.get("status") + "\n" +
                        "Message: " + result.get("error_message");
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("gplace", "parseLocationResult: Error=" + e.getMessage());
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        }
    }

    private String[] getPermissionList() {
        return new String[] {
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,
                INTERNET
        };
    }

    private void showMissingPermissionsError(ArrayList<String> permissionsDenied) {
        String message = "The following permissions denied: " + permissionsDenied +
                "\nPlease allow all permissions to runs this task properly";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}