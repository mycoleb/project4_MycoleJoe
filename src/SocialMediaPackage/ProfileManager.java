package SocialMediaPackage;
import GraphPackage.UndirectedGraph;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Queue;

public class ProfileManager {
    private final HashMap<String, Profile> profiles;
    private final UndirectedGraph<Profile> network;

    public ProfileManager() {
        profiles = new HashMap<>();
        network = new UndirectedGraph<>();
    }

    public void addProfile(String username, Profile newProfile) {
        if (!profiles.containsKey(newProfile.getName())) {
            profiles.put(username, newProfile);
            network.addVertex(newProfile);
        }
    }

    public void removeProfile(String username) {
        if (profiles.containsKey(username)) {
            Profile remProfile = profiles.get(username);

            ArrayList<Profile> neighborFriends = network.getNeighbors(remProfile, 0);
            if (neighborFriends != null) {
                for (Profile curNeighbor : neighborFriends)
                    curNeighbor.removeFriend(remProfile);
            }

            network.removeVertex(remProfile);
            profiles.remove(username);
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

    public boolean containsProfile(String username) { return (username != null) && (profiles.containsKey(username)); }

    public ArrayList<String> getAllUsernames() { return new ArrayList<>(profiles.keySet()); }

    public ArrayList<String> getFriendUsernames(String username, boolean best) {
        Profile curProfile = profiles.get(username);
        ArrayList<String> returnList = new ArrayList<>();
        ArrayList<Profile> neighborFriends;

        if (best)
            neighborFriends = network.getNeighbors(curProfile, 1);
        else
            neighborFriends = network.getNeighbors(curProfile, 0);

        if (neighborFriends != null) {
            for (String curUser : profiles.keySet()) {
                if (neighborFriends.contains(profiles.get(curUser)))
                    returnList.add(curUser);
            }
        }
        return returnList;
    }


    public void createFriendship(String username1, String username2, boolean best) {
        if (profiles.containsKey(username1) && profiles.containsKey(username2)) {
            Profile profile1 = profiles.get(username1);
            Profile profile2 = profiles.get(username2);
            if (best)
                network.addEdge(profile1, profile2,1);
            else
                network.addEdge(profile1, profile2,0);
            profile1.addFriend(profile2);
            profile2.addFriend(profile1);
        }
    }

    public void removeFriendship(String username1, String username2) {
        if (profiles.containsKey(username1) && profiles.containsKey(username2)) {
            Profile profile1 = profiles.get(username1);
            Profile profile2 = profiles.get(username2);
            network.removeEdge(profile1, profile2);
            profile1.removeFriend(profile2);
            profile2.removeFriend(profile1);
        }
    }

    public ArrayList<String> getFriendsOfFriendsUsernames(String username) {
        ArrayList<String> returnList = new ArrayList<>();
        ArrayList<String> friendUserList = getFriendUsernames(username, false);

        for (String friend : friendUserList) {
            ArrayList<String> fofUserList = getFriendUsernames(friend, false);
            for (String fof : fofUserList) {
                if (!fof.equals(username) && !friendUserList.contains(fof) && !returnList.contains(fof))
                    returnList.add(fof);
            }
        }
        return returnList;
    }

    public void displayAllConnectedProfiles(String username) {
        Profile originProfile = profiles.get(username);
        Queue<Profile> profileQueue = network.getBreadthFirstTraversal(originProfile);
        while (!profileQueue.isEmpty()) {
            System.out.print("\n");
            profileQueue.remove().printProfileDetails();
        }
    }

    public void displayAllProfiles() {
        for (Profile curProfile : profiles.values()) {
            System.out.print("\n");
            curProfile.printProfileDetails();
        }
    }
}






