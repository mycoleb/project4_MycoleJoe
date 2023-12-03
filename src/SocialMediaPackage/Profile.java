package SocialMediaPackage;

import java.util.ArrayList;

public class Profile {
    enum statusTypes {
        ONLINE,
        OFFLINE
    }

    private String name;
    private String image; // URL or path to the image; optional
    private statusTypes status;
    private final ArrayList<Profile> friendProfiles;



    // Additional attributes like location, age, etc., can be added here
    public Profile(String name, String image) {
        this.name = name;
        this.image = image;
        this.status = statusTypes.OFFLINE;
        this.friendProfiles = new ArrayList<>();
    }

    // Getter and setter for name
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    // Getter and setter for image
    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    // Getter and setter for status
    public statusTypes getStatus() { return status; }

    public void setStatus(statusTypes status) { this.status = status; }

    // Method to add a friend
    public void addFriend(Profile friendProfile) {
        if (!friendProfiles.contains(friendProfile))
            friendProfiles.add(friendProfile);
    }

    // Method to remove a friend
    public void removeFriend(Profile friendProfile) { friendProfiles.remove(friendProfile); }

    // Method to get the list of friends
    public ArrayList<Profile> getFriends() { return new ArrayList<>(friendProfiles); }

    // Method to print profile details
    public void printProfileDetails() {
        System.out.println("Name: " + name);
        if (image != null && !image.isEmpty())
            System.out.println("Image: " + image);

        System.out.println("Status: " + status);
        System.out.println("Friends:");
        for (Profile friend : friendProfiles)
            System.out.println(friend.getName());
    }

    public String toString() {
        return this.name;
    }
}


