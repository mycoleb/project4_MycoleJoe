package SocialMediaPackage;

import java.util.ArrayList;

public class Profile {
    static enum statusTypes {
        ONLINE,
        OFFLINE,
        AWAY
    }

    private String name;
    private String image;
    private statusTypes status;
    private final ArrayList<Profile> friendProfiles;

    public Profile(String newName, String newImage) {
        this.name = newName;
        this.image = newImage;
        this.status = statusTypes.OFFLINE;
        this.friendProfiles = new ArrayList<>();
    }

    public String getName() { return name; }

    public void setName(String newName) { this.name = newName; }

    public String getImage() { return image; }

    public void setImage(String newImage) { this.image = newImage; }


    public statusTypes getStatus() { return status; }

    public void setStatus(statusTypes newStatus) { this.status = newStatus; }


    public void addFriend(Profile friendProfile) {
        if (!friendProfiles.contains(friendProfile))
            friendProfiles.add(friendProfile);
    }

    public void removeFriend(Profile friendProfile) { friendProfiles.remove(friendProfile); }


    public ArrayList<Profile> getFriends() { return new ArrayList<>(friendProfiles); }

    public void printProfileDetails() {
        System.out.println("Name: " + getName());
        if (image != null && !image.isEmpty())
            System.out.println("Image: " + getImage());

        System.out.println("Status: " + getStatus());
        System.out.println("Friends:");
        if (friendProfiles.isEmpty())
            System.out.println("   <None>");
        else {
            for (Profile friend : getFriends())
                System.out.println("   " + friend.getName());
        }
    }

    public String toString() { return getName(); }
}


