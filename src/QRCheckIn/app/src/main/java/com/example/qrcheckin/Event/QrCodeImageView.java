
package com.example.qrcheckin.Event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcheckin.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class QrCodeImageView extends AppCompatActivity {

    // MainBar declarations
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    ImageView qrCodeImage;
    ImageButton shareImage;
    static String eventText;
    String eventName;
    String eventDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_image_view);

        // Initialize Mainbar Attributes
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);

        qrCodeImage = findViewById(R.id.qrCodeImageView);
        shareImage = findViewById(R.id.shareQR);


        // Retrieve the QR code bitmap from the Intent extras
        Bitmap qrCodeBitmap = getIntent().getParcelableExtra("QRCodeBitmap");
        // Retrieve event name and date
        eventText = getIntent().getStringExtra("EventName&Date");
        // Retrieve event name only
        eventName = getIntent().getStringExtra("EventName");
        // Retrieve event date only
        eventDate = getIntent().getStringExtra("EventDate");

        // Display the QR code bitmap in an ImageView
        qrCodeImage.setImageBitmap(qrCodeBitmap);

        // ToolBar
        Toolbar toolbar = findViewById(R.id.addEventToolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Check-In QrCode");

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCodeImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageAndText(bitmap);
            }
        });


        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Adding Event", Toast.LENGTH_LONG).show();
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Adding Event", Toast.LENGTH_LONG).show();
            }
        });


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Adding Event", Toast.LENGTH_LONG).show();
            }
        });
    }

    // openai, 2024, chatgpt: how to define a different action for the back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // Define the behavior when the back button is pressed
                // For example, navigate back to the previous activity or perform any other action
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void shareImageAndText(Bitmap bitmap) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("qrCode/jpeg");

        Uri uri = getImageToShare(bitmap, getApplicationContext());;
        String textToShare = "Scan and Share QR Code to CheckIn for " + eventName;

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, textToShare);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Attend " + eventName + " on " + eventDate + "." );

        startActivity(Intent.createChooser(intent, "Share via"));

    }

    private static Uri getImageToShare(Bitmap bitmap, Context context) {
        File folder = new File (context.getCacheDir(), "images");
        Uri uri = null;

        try{
            folder.mkdirs();
            File file = new File(folder, eventText + ".jpeg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG,90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()), "com.example.qrcheckin" + ".provider", file);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }
}