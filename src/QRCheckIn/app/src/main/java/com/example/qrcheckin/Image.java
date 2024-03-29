package com.example.qrcheckin;

import com.google.android.material.progressindicator.LinearProgressIndicator;

/**
 * Represents an image associated with an event or attendee within the QR Code Event Check-In system.
 */
public class Image{
    //private File imageFile;
    private Attendee uploader;
    private String uriString;

    LinearProgressIndicator progress;
    /**
     * Constructs an Image instance with specified image file and uploader.
     *
     * @param uriString Uri of the image
     * @param uploader the attendee who uploaded the image.
     */
    public Image(String uriString, Attendee uploader) {
        this.uriString = uriString;
        this.uploader = uploader;
    }

    /**
     * Empty constructor for firebase
     */
    public Image(){}

    /**
     * Gets the image file.
     *
     * @return the image file.
     */
    public String getUriString() {
        return uriString;
    }

    /**
     * Gets the uploader of the image.
     *
     * @return the uploader of the image.
     */
    public Attendee getUploader() {
        return uploader;
    }

    /**
     * Deletes the image file from the system.
     */
    public void deleteImage() {

    }

    /**
     * Converts the image file to a BITMAP Base64 encoded string.
     *
     * @return a Base64 encoded string representing the image, or null if an error occurs.
     */
}





