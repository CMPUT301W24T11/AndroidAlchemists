package com.example.qrcheckin;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Controls storing and retrieving data from the Attendees firebase collection
 */
public class AttendeeDatabaseManager {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference attendeesRef = db.collection("Attendees");

    /**
     * Searches Attendee collection for an Attendee whose docID matches fcmToken
     * Does nothing if attendee document is found
     * Calls storeAttendee to create a new attendee object if attendee document does not exist
     * @param fcmToken the fcmToken of the Attendee we're searching for
     *
     */
    public void checkExistingAttendees(String fcmToken){
        DocumentReference docRef = attendeesRef.document(fcmToken);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()){
                        // no such document exists
                        storeAttendee(fcmToken);
                    }
                    else{
                        Log.d("Firestore", "attendee already exists");
                    }
                }
            }
        });
    }

    /**
     * Creates a new Attendee object and stores it to the Attendee collection
     * @param fcmToken the fcmToken of the user we're creating an Attendee object for
     */
    public void storeAttendee(String fcmToken){
        Attendee attendee = new Attendee();
        // The docID of the attendee object is the associated user's fcmToken string
        attendeesRef.document(fcmToken).set(attendee);
        Log.d("Firestore", String.format("Attendee for token (%s) stored", fcmToken));
    }

    /**
     * Updates the trackGeolocation field of the Attendee with the docID fcmToken
     * @param fcmToken String ocID of the attendee to be updated
     * @param isShared Boolean value trackGeolocation is set to
     */
    public void updateAttendeeGeolocation(String fcmToken, Boolean isShared){
        DocumentReference attendeeRef = db.collection("Attendees").document(fcmToken);
        attendeeRef.update("profile.trackGeolocation", isShared).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "docsnapshot boolean updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "error updating doc",e);
            }
        });
    }

    /**
     * Retrieves and logs the Firebase Cloud Messaging (FCM) token for this app's installation
     * @param editor a SharedPreferences.Editor from the calling activity to save the token string value
     */
    public void getFcmToken(SharedPreferences.Editor editor) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(Utils.TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get and log the new FCM registration token
                    String token = task.getResult();
                    Log.d(Utils.TAG, token);
                    // save token string
                    editor.putString("token", token);
                    editor.apply();
                });
    }

    /**
     * Updates the profilePicture field in an Attendee's Profile
     * @param fcmToken String of the Attendee's docID in firebase
     * @param uri Uri of the ProfilePicture
     */
    public void updateProfilePicture(String fcmToken, Uri uri){
        // Create doc reference for the Attendee
        DocumentReference attendeeRef = db.collection("Attendees").document(fcmToken);
        // Update the uriString field
        attendeeRef.update("profile.profilePicture.uriString", uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "docsnapshot updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "error updating doc",e);
            }
        });
    }

    /**
     * Updates a string field in an Attendee's Profile in firestore
     * @param fcmToken String of the Attendee's docID in firebase
     * @param field String of the field to be updated in Profile
     * @param value String of the new value the field is set to
     */
    public void updateProfileString(String fcmToken, String field, String value){
        DocumentReference attendeeRef = db.collection("Attendees").document(fcmToken);
        attendeeRef.update("profile."+field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "docsnapshot string updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "error updating doc",e);
            }
        });
    }

}
