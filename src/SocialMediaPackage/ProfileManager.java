package SocialMediaPackage;
import GraphPackage.UndirectedGraph;

import java.util.HashMap;
import java.util.ArrayList;

public class ProfileManager {
    private final HashMap<String, Profile> profiles;
    private final UndirectedGraph<Profile> network;
    public ProfileManager() {
        profiles = new HashMap<>();
        network = new UndirectedGraph<>();
    }
    // Method to add a profile to the network
    public void addProfile(Profile profile) {
        if (!profiles.containsKey(profile.getName())) {
            profiles.put(profile.getName(), profile);
            network.addVertex(profile);
        }
    }
    // Method to remove a profile from the network
    public void removeProfile(String profileName) {
        if (profiles.containsKey(profileName)) {
            Profile profile = profiles.get(profileName);
            network.removeVertex(profile);
            profiles.remove(profileName);
        }
    }
    // Method to create a friendship between two profiles
    public void createFriendship(String profileName1, String profileName2) {
        if (profiles.containsKey(profileName1) && profiles.containsKey(profileName2)) {
            Profile profile1 = profiles.get(profileName1);
            Profile profile2 = profiles.get(profileName2);
            network.addEdge(profile1, profile2);
            profile1.addFriend(profile2);
            profile2.addFriend(profile1);
        }
    }
    // Method to display all profiles
    public void displayAllProfiles() {
        for (Profile profile : profiles.values())
            profile.printProfileDetails();
    }
    // Method to display a profile's friends
    public void displayFriends(String profileName) {
        if (profiles.containsKey(profileName)) {
            Profile profile = profiles.get(profileName);
            System.out.println("Friends of " + profileName + ":");
            for (Profile friend : profile.getFriends())
                System.out.println(friend.getName());
        }
    }
    // Method to display friends of friends
    public void displayFriendsOfFriends(String profileName) {
        if (profiles.containsKey(profileName)) {
            Profile profile = profiles.get(profileName);
            System.out.println("Friends of friends of " + profileName + ":");
            ArrayList<Profile> friendsOfFriends = network.getNeighbors(profile);
            for (Profile fof : friendsOfFriends) {
                if (!profile.getFriends().contains(fof))
                    System.out.println(fof.getName());
            }
        }
    }
}






