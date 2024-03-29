package com.example.qrcheckin;

import android.location.Location;

import java.io.FileNotFoundException;
public class Profile {
    private Boolean trackGeolocation;
    private String name;
    private String homepage;
    private String contact;
    private ProfilePicture profilePicture;
    private Location location;
    /**
     * Constructs a Profile for an Attendee
     */
    public Profile(){
        // default geolocation sharing permission set to false
        this.trackGeolocation = false;

        // TODO: randomize initial names in some way so that more unique profile pictures are generated.
        this.name = "new user";

        // TODO: generate a profile picture based on the profile name
        //generatePicture(this.firstName, this.lastName);
    }


    /**
     * Returns the ProfilePicture of the Profile
     * @return the ProfilePicture of the Profile
     */
    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the ProfilePicture of the Profile
     * @param profilePicture the ProfilePicture of the Profile
     */
    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the Location of the Profile
     * @return the Location of the Profile
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the Location of the Profile
     * @param location the Location of the Profile
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the status whether the Profile has agreed to Geolocation tracking
     * @return the Boolean representing Geolocation tracking permission
     */
    public Boolean getTrackGeolocation() {
        return trackGeolocation;
    }

    /**
     * Sets the Profile's permission to share Geolocation
     * @param trackGeolocation the Boolean representing Geolocation tracking permission
     */
    public void setTrackGeolocation(Boolean trackGeolocation) {
        this.trackGeolocation = trackGeolocation;
    }

    /**
     * Returns the Profile's homepage
     * @return String of the Profile's homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Sets the Profile's homepage
     * @param homepage String of the Profile's homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * Returns the Profile's contact information
     * @return String of the Profile's contact information
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the Profile's contact information
     * @param contact String of the Profile's contact information
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Returns the Profile's name
     * @return name String of the Profile's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Profile's name
     * @param name String the of the Profile's name
     */
    public void setName(String name){
        this.name = name;
    }
}
