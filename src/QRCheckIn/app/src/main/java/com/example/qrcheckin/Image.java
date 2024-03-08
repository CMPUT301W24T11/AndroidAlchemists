package com.example.qrcheckin;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Represents an image associated with an event or attendee within the QR Code Event Check-In system.
 */
public class Image {
    //private File imageFile;
    private Attendee uploader;
    private Uri imageUri;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageReference = storage.getReference();
    LinearProgressIndicator progress;
    /**
     * Constructs an Image instance with specified image file and uploader.
     *
     * @param imageUri Uri of the image
     * @param uploader the attendee who uploaded the image.
     */
    public Image(Uri imageUri, Attendee uploader) {
        this.imageUri = imageUri;
        this.uploader = uploader;
    }

    /**
     * Gets the image file.
     *
     * @return the image file.
     */
    public Uri getImageUri() {
        return imageUri;
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

    public void uploadImage(String folderName, String fileName){
        // ISSUE: pathString argument does not rename file
        StorageReference reference = storageReference.child(folderName+fileName);
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess (UploadTask.TaskSnapshot taskSnapshot){

            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e){

            }
        });
        }
    }

//    public String encodeImageToBase64() {
//        try (FileInputStream imageInputStream = new FileInputStream(imageFile)) {
//            Bitmap bitmap = BitmapFactory.decodeStream(imageInputStream);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            return Base64.encodeToString(byteArray, Base64.DEFAULT);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     *  Pending:
     *      uploading image to Firebase Storage
     *      updating  database with the image metadata
     */

