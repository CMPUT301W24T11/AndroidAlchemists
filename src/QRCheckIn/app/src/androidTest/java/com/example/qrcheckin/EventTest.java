// Corrected EventTest.java
package com.example.qrcheckin;

import org.junit.Test;
import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void eventCreation_isCorrect() {
        // Create an Attendee example for the constructors
        Attendee attendeeExample = new Attendee();

        // Create a QrCode object with required parameters
        String qrCodeValue = "SampleQRCodeValue";
        String qrCodeAdditionalInfo = "SampleAdditionalInfo";
        QrCode checkInQRCode = new QrCode(qrCodeValue, attendeeExample, qrCodeAdditionalInfo);

        // Create a PromoQRCode object
        PromoQRCode promoQRCode = new PromoQRCode();

        // Create an EventPoster object with required parameters
        String posterImage = "SamplePosterImage";
        EventPoster poster = new EventPoster(posterImage, attendeeExample);

        boolean checkInStatus = true; // Example value

        // Create an Event object with all required parameters
        Event event = new Event(checkInQRCode, promoQRCode, poster, "Event1", "2024-01-01", "12:00", "Location1", "Description of Event1", checkInStatus);

        // Assert the event properties
        assertEquals("Event1", event.getEventName());
        assertEquals("Description of Event1", event.getEventDescription());
        assertEquals("2024-01-01", event.getEventDate());
        assertEquals("12:00", event.getEventTime());
        assertEquals("Location1", event.getEventLocation());
        assertTrue(event.isCheckInStatus());
    }
}