package com.example.qrcheckin;

import java.io.File;

public class EventPoster extends Image {
    private File imageFile;
    private Attendee uploader;

    public EventPoster(File imageFile, Attendee uploader, File imageFile1, Attendee uploader1) {
        super(imageFile, uploader);
        this.imageFile = imageFile1;
        this.uploader = uploader1;
    }
}

//package com.example.qrcheckin;
//
// import android.media.Image;
//
// import java.io.File;
//
// public class EventPoster extends Image {
// private Event event;
// private Event event; // Association with the Event class
//
// // Constructor for creating a new EventPoster
// public EventPoster(File imageFile, Attendee uploader, Event event) {
// super(imageFile, uploader); // Call to the superclass (Image) constructor
// this.event = event;
// }
// }
