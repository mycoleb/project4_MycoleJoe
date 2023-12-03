package SocialMediaPackage;
import GraphPackage.UndirectedGraph;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

public class ProfileManager {
    private final HashMap<String, Profile> profiles;
    private final UndirectedGraph<Profile> network;

    public ProfileManager() {
        profiles = new HashMap<>();
        network = new UndirectedGraph<>();
    }

    public void printNetworkGraph() { // For testing
        network.printVertices();
    }

    // Method to add a profile to the network
    public void addProfile(String username, Profile profile) {
        if (!profiles.containsKey(profile.getName())) {
            profiles.put(username, profile);
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

    public int getNumProfiles() {
        return profiles.size();
    }

    public Profile getProfile(String username) {
        if (containsProfile(username))
            return profiles.get(username);
        else
            return null;
    }

    public boolean containsProfile(String username) {
        if ((username != null) && (profiles.containsKey(username)))
            return true;
        else
            return false;
    }

    public ArrayList<String> getAllUsernames() { return new ArrayList<>(profiles.keySet()); }

    // Method to create a friendship between two profiles
    public void createFriendship(String username1, String username2) {
        if (profiles.containsKey(username1) && profiles.containsKey(username2)) {
            Profile profile1 = profiles.get(username1);
            Profile profile2 = profiles.get(username2);
            network.addEdge(profile1, profile2);
            profile1.addFriend(profile2);
            profile2.addFriend(profile1);
        }
    }


    // Method to display a profile's friends
    public void displayFriends(String username) {
        if (profiles.containsKey(username)) {
            Profile profile = profiles.get(username);
            System.out.println("Friends of " + username + ":");
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






