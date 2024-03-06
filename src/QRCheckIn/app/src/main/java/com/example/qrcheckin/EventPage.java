package com.example.qrcheckin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventPage extends AppCompatActivity {
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        // Set and display the toolbar
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);

        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        eventButton.setPressed(true);

        // Find the text views
        TextView tvEventName = findViewById(R.id.text_event_name);
        TextView tvEventDate = findViewById(R.id.text_event_date);
        TextView tvEventLocation = findViewById(R.id.text_event_location);
        TextView tvEventDescription = findViewById(R.id.text_event_description);
        ImageView ivEventPoster = findViewById(R.id.image_event_poster);
        ImageView ivEventPromoQr = findViewById(R.id.image_event_promo_qr);

        // Retrieve the event that was clicked
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventsRef = db.collection("events");

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        DocumentReference docRef = eventsRef.document(documentId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Get and display event details
                Event event = documentSnapshot.toObject(Event.class);
                if (documentSnapshot != null && event != null) {
                    tvEventName.setText(event.getEventName());
                    tvEventLocation.setText(event.getEventLocation());
                    tvEventDate.setText(event.getEventDate());
                    tvEventDescription.setText(event.getEventDescription());
                } else {
                    Log.d("Firestore", String.format("No such document with id %s", documentId));
                }
                //ivEventPoster.setImageURI(event.getPoster()); TODO: Make event.getPoster() return a URI
                //ivEventPromoQr.setImageURI(event.getPromoQRCode());
            }
        });

        // Set the "Scan QR code" toolbar button listener
        qrButton.setOnClickListener(v -> {
            Intent event = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(event);
        });

        // Set the "Add event" toolbar button listener
        addEventButton.setOnClickListener(v -> {
            Intent event = new Intent(getApplicationContext(), createNewEventScreen1.class);
            startActivity(event);
        });

    }
}