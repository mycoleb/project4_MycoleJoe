package SocialMediaPackage;

import java.util.ArrayList;

/**
 * Profile: Implementation of an object that stores profile data for a user of a social network.
 */
public class Profile {
    /** Represents the user's availability status. */
    enum statusTypes {
        /** The user is online and available. */
        ONLINE,
        /** The user is offline and unavailable. */
        OFFLINE,
        /** The user is online and unavailable. */
        AWAY
    }

    /** The user's display name. */
    private String name;
    /** A path or URL to the user's profile image. */
    private String image;
    /** Stores the user's current availability status. */
    private statusTypes status;
    /** Stores the profiles of this user's friends. */
    private final ArrayList<Profile> friendProfiles;

    /**
     * Constructor creates a new Profile object with the specified display name and image URL or path.
     * @param newName The user's display name.
     * @param newImage The user's image URL or path.
     */
    public Profile(String newName, String newImage) {
        setName(newName);
        setImage(newImage);
        setStatus(statusTypes.OFFLINE);
        this.friendProfiles = new ArrayList<>();
    }

    /**
     * Retrieves the user's display name.
     * @return The user's display name.
     */
    public String getName() { return name; }

    /**
     * Sets the user's display name.
     * @param newName The user's new display name.
     */
    public void setName(String newName) { this.name = newName; }

    /**
     * Retrieves the URL or path to the user's profile image.
     * @return The URL or path to the user's profile image.
     */
    public String getImage() { return image; }

    /**
     * Sets the URL or path to the user's profile image.
     * @param newImage The new URL or path to the user's profile image.
     */
    public void setImage(String newImage) { this.image = newImage; }

    /**
     * Retrieves the user's availability status.
     * @return The user's availability status.
     */
    public statusTypes getStatus() { return status; }

    /**
     * Sets the user's availability status.
     * @param newStatus The user's new availability status.
     */
    public void setStatus(statusTypes newStatus) { this.status = newStatus; }

    /**
     * Adds a new profile to the user's friends list if they are not already friends.
     * @param friendProfile The profile to add to the user's friends list.
     */
    public void addFriend(Profile friendProfile) {
        if (!friendProfiles.contains(friendProfile))
            friendProfiles.add(friendProfile);
    }

    /**
     * Removes a profile from the user's friends list if they are friends.
     * @param friendProfile The profile to remove from the user's friends list.
     */
    public void removeFriend(Profile friendProfile) { friendProfiles.remove(friendProfile); }

    /**
     * Retrieves a list of the profiles in the user's friends list.
     * @return A list of the profiles in the user's friends list.
     */
    public ArrayList<Profile> getFriends() { return new ArrayList<>(friendProfiles); }

    /**
     * Prints the user's profile information and the display names of profiles in the user's friends list.
     */
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

    /**
     * Builds a textual representation of the user's display name.
     * @return A textual representation of the user's display name.
     */
    public String toString() { return getName(); }
}


