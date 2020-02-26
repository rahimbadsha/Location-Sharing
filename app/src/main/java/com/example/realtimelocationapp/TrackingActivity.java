package com.example.realtimelocationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.realtimelocationapp.Model.MyLocation;
import com.example.realtimelocationapp.Utils.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback, ValueEventListener {

    private GoogleMap mMap;
    DatabaseReference trackingUserLocation;
    FloatingActionButton chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        chatButton = (FloatingActionButton) findViewById(R.id.mt_chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TrackingActivity.this, chatActivity.class));
            }
        });

        registerEventRealtime();
    }

    private void registerEventRealtime() {

        trackingUserLocation = FirebaseDatabase.getInstance()
                .getReference(Common.PUBLIC_LOCATION)
                .child(Common.trackingUser.getUid());

        trackingUserLocation.addValueEventListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        trackingUserLocation.addValueEventListener(this);
    }

    @Override
    protected void onStop() {

        trackingUserLocation.removeEventListener(this);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Enable zoom ui
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Set skin for map
        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,
                R.raw.my_uber_style));

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if (dataSnapshot.getValue() != null)
        {
            MyLocation location = dataSnapshot.getValue(MyLocation.class);

            //Add Marker
            LatLng userMarker = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userMarker)
                    .title(Common.trackingUser.getEmail())
                    .snippet(Common.getDateFormatted(Common.convertTimeStampToDate(location.getTime()))));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userMarker, 16f ));
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
