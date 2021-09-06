package com.example.vietnamesecuisinehelper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

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
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
                    public CancellationToken onCanceledRequested(
                            @NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }
                });

        currentLocationTask.addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Volley.newRequestQueue(MapActivity.this.getApplicationContext())
                                    .add(MapActivity.this.requestToServer(food_name, task.getResult()));
                        }
                    }
                })
                .addOnCanceledListener(
                        new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                Toast.makeText(MapActivity.this.getApplicationContext(),
                                        "Request failed unexpectedly",Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
        );
    }

    private JsonObjectRequest requestToServer(String food_name, Location curLoc) {
        StringBuilder urlBuilder = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?");

        urlBuilder.append("input=").append(food_name);
        urlBuilder.append("&inputtype=").append("textquery");
        urlBuilder.append("&fields=").append("formatted_address,name,rating,geometry");
        urlBuilder.append("&locationbias=").append("circle:5000")
                .append("@").append(curLoc.getLatitude())
                .append(",").append(curLoc.getLongitude());
        urlBuilder.append("&key=").append("AIzaSyAMCmyPwxfSMzksFw0jkMG_PcU9frcUIHg");

        return new JsonObjectRequest(Request.Method.GET, urlBuilder.toString(),
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
    }

    private void parseLocationResult(JSONObject result) {
        String address, name;
        double rating;
        double lat, lng;

        try {
            if (result.getString("status").equalsIgnoreCase("OK")) {
                JSONArray jsonArray = result.getJSONArray("candidates");

                Toast.makeText(getBaseContext(), jsonArray.length() + " results found!",
                        Toast.LENGTH_SHORT).show();

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
                    .snippet(address + "\n" + "Rating: " + rating + "/5"));
                }
            }
            else {
                Log.e("JSONRequestError",
                        "Status: " + result.get("status") + "\n" +
                        "Message: " + result.get("error_message"));

                Toast.makeText(getBaseContext(), "Request ends unexpectedly",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("gplace", "parseLocationResult: Error=" + e.getMessage());

            Toast.makeText(this,
                    "Unexpected exception in respond from server", Toast.LENGTH_SHORT).show();
        }
    }

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
        String message = "The following permissions denied:\n" + permissionsDenied +
                "\nPlease allow all permissions to runs this task properly";

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}