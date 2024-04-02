package com.example.qrcheckin.Attendee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcheckin.Event.EventDatabaseManager;
import com.example.qrcheckin.Common.LinearLayoutManagerWrapper;
import com.example.qrcheckin.Event.OrganizersEventPageActivity;
import com.example.qrcheckin.R;
import com.example.qrcheckin.ClassObjects.Attendee;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AttendeeList extends AppCompatActivity {

    Button getMap;
    String latitude, longitude;
    RecyclerView recyclerView;
    AttendeeAdapter attendeeAdapter;
    AttendeeDatabaseManager attendeeDb;
    private static final int REQUEST_LOCATION = 101;

    private EventDatabaseManager eventDb;
    private String fcmToken;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);

        // Set up recycler view of attendees
        recyclerView = findViewById(R.id.event_recycler_view);

        // Manage Toolbar
        Toolbar toolbar = findViewById(R.id.attendee_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Total Participants: ");

        // Retrieve values passed by previous activity to determine if the event's
        // signups list or attendee list should be displayed
        documentId = getIntent().getStringExtra("EVENT_DOC_ID");
        String fieldName = getIntent().getStringExtra("FIELD_NAME");

        // Set up the recycler view of events to be displayed (displays all by default)
        attendeeDb = new AttendeeDatabaseManager();
        assert documentId != null;
        assert fieldName != null;
        Query query = attendeeDb.getCollectionRef()
                .whereArrayContains(fieldName, documentId);
        setUpRecyclerView(query, fieldName);

        // TODO: setup recycler view

        getMap = findViewById(R.id.mapLocation);
        // Hide getMap button if we're viewing the signups
        if (Objects.equals(fieldName, "signupEvents")){
            getMap.setVisibility(View.INVISIBLE);
        }

        getMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });
    }

    private void getMyLocation() {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(AttendeeList.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
                            assert addresses != null;
                            latitude = String.valueOf(addresses.get(0).getLatitude());
                            longitude = String.valueOf(addresses.get(0).getLongitude());
                            Toast.makeText(AttendeeList.this, "latitude= " + latitude, Toast.LENGTH_SHORT).show();
                            Toast.makeText(AttendeeList.this, "longitude= " + longitude, Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            } else {
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Sets up an AttendeeAdapter on the recycler view and sends it the required
     * query. Do not call
     * this method more than once
     * 
     * @param query     The query the recycler view will use to display events
     * @param fieldName String from options: ["signupEvents", "attendedEvents"] to
     *                  display
     */
    private void setUpRecyclerView(Query query, String fieldName) {

        // Put the desired query into the adapter so it can use it to find the specified
        // events
        FirestoreRecyclerOptions<Attendee> options = new FirestoreRecyclerOptions.Builder<Attendee>()
                .setQuery(query, Attendee.class)
                .setLifecycleOwner(this)
                .build();

        attendeeAdapter = new AttendeeAdapter(options, position -> {
            if (Objects.equals(fieldName, "attendedEvents")) {
                return true;
            } else if (Objects.equals(fieldName, "signupEvents")) {
                return false;
            }
            return true; // Return the default attendedEvents if the fieldName is invalid

        });

        // Connect the recycler view to it's adapter and layout manager
        RecyclerView recyclerView = findViewById(R.id.recycler_view_attendee_check_ins);
        recyclerView.setHasFixedSize(true); // RecyclerView inside constraint layout, won't grow
        recyclerView.setItemAnimator(null); // ItemAnimator is buggy, keep this OFF if possible
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this));
        recyclerView.setAdapter(attendeeAdapter);
    }

    // openai, 2024, chatgpt: how to pass the document Id through the upButton

    /**
     * Pass information through the the up button
     * 
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate back to OrganizersEventPageActivity with documentId
                Intent intent = new Intent(getApplicationContext(), OrganizersEventPageActivity.class);
                intent.putExtra("DOCUMENT_ID", documentId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

// package com.example.qrcheckin;
//
// import androidx.annotation.NonNull;
// import androidx.appcompat.app.AppCompatActivity;
// import androidx.core.app.ActivityCompat;
// import androidx.core.content.ContextCompat;
//
// import android.Manifest;
// import android.annotation.SuppressLint;
// import android.content.Intent;
// import android.content.pm.PackageManager;
// import android.location.Location;
// import android.location.LocationListener;
// import android.location.LocationManager;
// import android.os.Bundle;
// import android.view.View;
// import android.widget.Button;
// import android.widget.Toast;
//
//
// public class AttendeeList extends AppCompatActivity implements
// LocationListener {
//
// Button getMap;
// LocationManager locationManager;
//
// @Override
// protected void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.attendee_list);
//
// getMap = findViewById(R.id.mapLocation);
//
// // Runtime Permissions
// if (ContextCompat.checkSelfPermission(AttendeeList.this,
// Manifest.permission.ACCESS_FINE_LOCATION) !=
// PackageManager.PERMISSION_GRANTED){
// ActivityCompat.requestPermissions(AttendeeList.this, new String[]{
// Manifest.permission.ACCESS_FINE_LOCATION
// }, 100);
// }
//
// getMap.setOnClickListener(new View.OnClickListener() {
// @Override
// public void onClick(View v) {
// getLocation();
// }
// });
// }
//
// @SuppressLint("MissingPermission")
// private void getLocation(){
// try {
// locationManager = (LocationManager)
// getApplicationContext().getSystemService(LOCATION_SERVICE);
// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5,
// AttendeeList.this);
// }
// catch (Exception e){
// e.printStackTrace();
// }
// }
//
// @Override
// public void onLocationChanged(@NonNull Location location) {
// Toast.makeText(this, location.getLatitude() + ", " + location.getLongitude(),
// Toast.LENGTH_SHORT).show();
// }
//
// @Override
// public void onStatusChanged(String provider, int status, Bundle extras) {
// LocationListener.super.onStatusChanged(provider, status, extras);
// }
//
// @Override
// public void onProviderEnabled(@NonNull String provider) {
// LocationListener.super.onProviderEnabled(provider);
// }
//
// @Override
// public void onProviderDisabled(@NonNull String provider) {
// LocationListener.super.onProviderDisabled(provider);
// }
// }