package com.example.maask.deliveryagentchatbot;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    Toolbar toolbar;
    GoogleMap map;
    private GoogleMapOptions googleMapOptions;
    ArrayList<LatLng> latLngs = new ArrayList<>();

    LinearLayout statusLL;
    ImageView okIV,refreshIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        final String sessionId = getIntent().getExtras().getString("sessionId");

        statusLL = findViewById(R.id.status_ll);
        okIV = findViewById(R.id.ok_iv);
        refreshIV = findViewById(R.id.refresh_iv);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle("Google Map");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // google option ka locha ...
        googleMapOptions = new GoogleMapOptions();
        googleMapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL).compassEnabled(true).zoomControlsEnabled(true);

        // map load in fragment ...
        SupportMapFragment mapFragment = SupportMapFragment.newInstance(googleMapOptions);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.google_map_container, mapFragment);
        ft.commit();

        refreshIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusLL.setVisibility(View.GONE);
                latLngs.clear();
                map.clear();
            }
        });

        okIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double startLat = latLngs.get(0).latitude;
                double startLon = latLngs.get(0).longitude;

                double endLat = latLngs.get(1).latitude;
                double endLon = latLngs.get(1).longitude;

                Intent intent = new Intent(GoogleMapActivity.this,MainActivity.class);
                intent.putExtra("startLat",String.valueOf(startLat));
                intent.putExtra("startLon",String.valueOf(startLon));
                intent.putExtra("endLat",String.valueOf(endLat));
                intent.putExtra("endLon",String.valueOf(endLon));
                intent.putExtra("sessionId",sessionId);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });

        mapFragment.getMapAsync(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.profile:
                Toast.makeText(this, "Profile clicked !", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:

                Toast.makeText(this, "Logout clicked !", Toast.LENGTH_SHORT).show();
                break;

            case R.id.changed_pass:
                Toast.makeText(this, "Changed Password clicked !", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:

                double lat = latLngs.get(0).latitude;
                double lon = latLngs.get(1).longitude;

                Intent intent = new Intent(GoogleMapActivity.this,MainActivity.class);
                intent.putExtra("lat",String.valueOf(lat));
                intent.putExtra("lon",String.valueOf(lon));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return;
        }

        map.setOnMapLongClickListener(this);
        map.setMyLocationEnabled(true);

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        latLngs.add(latLng);

        if (latLngs.size() == 1){


            map.addMarker(new MarkerOptions().position(latLng).title("Start").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        }else if (latLngs.size() == 2){

            map.addMarker(new MarkerOptions().position(latLng).title("Destination").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            statusLL.setVisibility(View.VISIBLE);

        }else {

            statusLL.setVisibility(View.GONE);
            latLngs.clear();
            map.clear();

        }

    }


}
