package com.example.qrcheckin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class QRCodeScan extends AppCompatActivity {
    TextView title;
    TextView location;
    TextView dateAndtime;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    ImageView posterImage;
    ImageButton attendeeList;
    private boolean hasScanned = false;   // Boolean flag to track whether a scan has been performed
    String summary = null, destination = null, dateOfEvent = null, timeOfEvent = null, dtstart = null, uriPoster = null;
    // Retrieve the current list of attendees from the event document
    List<String> attendees = new ArrayList<>();
    Bitmap bitmapPoster = null;
    String fcmToken;

    // Get access to the Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eventsRef = db.collection("events");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);


        title = findViewById(R.id.topNavigationText);
        location = findViewById(R.id.location);
        dateAndtime = findViewById(R.id.dateAndtime);
        posterImage = findViewById(R.id.posterImage);
        attendeeList = findViewById(R.id.attendeeList);

        qrButton = findViewById(R.id.qrCode);
        eventButton = findViewById(R.id.events);
        addEventButton = findViewById(R.id.addEvent);
        profileButton = findViewById(R.id.profile);


        // uses the ZXing library to open the camera and proceed scanning
//        startScanner();

        attendeeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AttendeeList.class);
                startActivity(event);
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), QRCodeScan.class);
                startActivity(event);
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), EventListView.class);
                startActivity(event);
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), CreateNewEventScreen1.class);
                startActivity(event);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), ProfileFragment.class);
                startActivity(event);
            }
        });
    }

    /**
     * using the IntentIntegrator class from the ZXing library to integrate barcode scanning functionality into your Android application
     * The ZXing library will return the scanned barcode data to your activity once the scanning is complete.
     */
    private void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR code");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }


    /**
     * In order to get data embedded in the QR code
     * @param resultCode An integer code that identifies the request
     * @param requestCode An integer result code returned by the child activity
     * @param data An Intent object that contains additional data returned by the child activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Separate the scanned data into different variables
                String scannedData = result.getContents();

                // Query Firestore to find the document with the matching hashedContent in the checkInQRCode field
                Query query = eventsRef.whereEqualTo("checkInQRCode.hashedContent", scannedData);

                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Check if any document matches the query
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot != null && !querySnapshot.isEmpty()) {

                            // Retrieve the first matching document
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                            // Extract relevant information from the document
                            summary = documentSnapshot.getString("eventName");
                            destination = documentSnapshot.getString("eventLocation");
                            dateOfEvent = documentSnapshot.getString("eventDate");
                            timeOfEvent = documentSnapshot.getString("eventTime");
                            uriPoster = documentSnapshot.getString("poster.uriString");

                            Toast.makeText(this, "uriPoster: " + uriPoster, Toast.LENGTH_SHORT).show();

                            // make a string combined with date and time
                            String dtstart = dateOfEvent + 'T' + timeOfEvent + 'Z';

                            // get the date and time formatted
                            String formattedDateTime = null;
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.US);
                                Date date = inputFormat.parse(dtstart);
                                formattedDateTime = outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Check if URI is not null (means available)
                            if (uriPoster != null && !uriPoster.isEmpty()) {
                                // Convert the URI string back to a URI object
                                Uri uri = Uri.parse(uriPoster);

                                // Load the image using the URI
                                try {
                                    bitmapPoster = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                posterImage.setImageBitmap(bitmapPoster);

                            } else {
                                // Handle case where URI is null or empty
                                Toast.makeText(this, "No poster URI available", Toast.LENGTH_SHORT).show();
                            }

                            // set the event details on the event page
                            title.setText(summary);
                            location.setText(destination);
                            dateAndtime.setText(formattedDateTime);

                            hasScanned = true; // Set the flag to true after successful scan

                            // Display or use the separated variables as needed
                            Toast.makeText(this, "CHECKED IN " + summary, Toast.LENGTH_SHORT).show();

                        } else {
                            // No matching document found
                            Toast.makeText(this, "No event found!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Entered exception", Toast.LENGTH_SHORT).show();
                        // An error occurred during the query execution, handle the error
                        Exception exception = task.getException(); // Retrieve the exception that occurred

                        if (exception instanceof FirebaseFirestoreException) {
                            FirebaseFirestoreException firestoreException = (FirebaseFirestoreException) exception;
                            FirebaseFirestoreException.Code errorCode = firestoreException.getCode(); // Retrieve the error code
                            // Handle specific error codes if needed, for example:
                            switch (errorCode) {
                                case PERMISSION_DENIED:
                                    // Handle permissions denied error
                                    Toast.makeText(this, "Missing or insufficient permissions", Toast.LENGTH_SHORT).show();
                                    break;
                                case NOT_FOUND:
                                    // Handle document not found error
                                    Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    // Handle other errors
                                    System.err.println("Firestore error occurred: " + exception.getMessage());
                            }
                        } else {
                            // Handle other types of exceptions
                            assert exception != null;
                            System.err.println("An error occurred: " + exception.getMessage());
                        }
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}