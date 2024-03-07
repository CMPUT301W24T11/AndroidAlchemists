package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class createNewEventScreen1 extends AppCompatActivity implements SelectDateFragment.DatePickerDialogListener{
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    EditText eventNameEditText;
    Button selectDateButton;

    Button nextPageButton;
    private String inputEventName;
    private String inputEventDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event_screen_1);


        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        addEventButton.setPressed(true);
        profileButton = findViewById(R.id.profileButton);
        nextPageButton = findViewById(R.id.nextButton);

        // initialize Event attribute input views
        eventNameEditText = findViewById(R.id.eventNameInput);
        selectDateButton = findViewById(R.id.selectDateButton);

        Toolbar toolbar = findViewById(R.id.addEventToolBar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Create an Event");

        // Listener to show a DatePicker fragment when selectDateButton is clicked
        selectDateButton.setOnClickListener(v -> {
            new SelectDateFragment().show(getSupportFragmentManager(), "Select Date");
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), EventListView.class);
                startActivity(event);
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(event);
            }
        });

        nextPageButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Retrieves input fields for the Event's attributes and passes them to createNewEvent2 activity
             * upon the nextPageButton being clicked
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Get the Event attributes from the input fields
                inputEventName = eventNameEditText.getText().toString();
                Intent event = new Intent(getApplicationContext(), createNewEventScreen2.class);
                // Store Event attributes to pass to createNewEventScreen2
                // https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application , 2011, user914425
                event.putExtra("eventDate", inputEventDate);
                event.putExtra("eventName", inputEventName);
                //Log.d("event", String.format("going to pass %s %s", inputEventName, inputEventDate));
                startActivity(event);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), profileFragment.class);
                startActivity(event);
            }
        });

    }

    /**
     * Builds a string in the format "yyyy-mm-dd" and assigns it to inputEventDate
     * @param year Int of the year
     * @param month Int of the month (starts at 0)
     * @param dayOfMonth Int of the day
     */
    public void buildDate(int year, int month, int dayOfMonth){
        // https://stackoverflow.com/questions/6421874/how-to-get-the-date-from-the-datepicker-widget-in-android , 2011, rabby
        // Month is 0 based so add 1
        StringBuilder dateBuilder = new StringBuilder()
        .append(year).append("-")
                .append(month + 1).append("-")
                .append(dayOfMonth).append("");
        inputEventDate = dateBuilder.toString();
    }
}