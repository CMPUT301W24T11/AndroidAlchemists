package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class EventListView extends AppCompatActivity {
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    RecyclerView eventListView;
    private EventAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eventsRef = db.collection("events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_view);

        setUpRecyclerView();

        // Set up tool bar
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        eventListView = findViewById(R.id.event_recycler_view);

        eventButton.setPressed(true);       // https://stackoverflow.com/questions/9318331/keep-android-button-selected-state, 2024, Prompt: how  to keep a button selected

        Toolbar toolbar = findViewById(R.id.eventListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Ongoing Events");

        // Set listener for "Scan QR code" toolbar button
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(event);
            }
        });

        // Set listener for "Add event" toolbar button
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), createNewEventScreen1.class);
                startActivity(event);
            }
        });

        // If an event is clicked, open its event page
        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();

                // Send the document id of the event to the Event Page before opening it
                Intent intent = new Intent(getApplicationContext(), EventPage.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);

            }
        });
    }

    /**
     * Sets up an EventAdapter on the recycler view and sends it the required query
     */
    private void setUpRecyclerView() {
        // Set up a general query that returns "event" items from the database
        Query query = eventsRef.orderBy("eventName", Query.Direction.DESCENDING);

        // Put this query into the adapter so it can use it
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();

        adapter = new EventAdapter(options);

        // Connect the recycler view to it's adapter and layout manager
        RecyclerView recyclerView = findViewById(R.id.event_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        eventsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Access each document's data and print it
                        String eventName = document.getString("eventName");
                        String eventDescription = document.getString("eventDescription");
                        // Access other fields as needed

                        // Print the data
                        System.out.println("Event Name: " + eventName);
                        System.out.println("Event Description: " + eventDescription);
                        // Print other fields as needed
                    }
                } else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.out.println("Failed to fetch documents: " + task.getException());
            }
        });
    }

    /**
     * Ensure recycler view doesn't update if the app is in the background
     */
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}