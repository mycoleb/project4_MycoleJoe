package SocialMediaPackage;
import GraphPackage.UndirectedGraph;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Queue;

/**
 * ProfileManager: Implementation of a manager for social media network users and their profiles. Performs operations
 * on profiles representing non-interactive functions of the social media network.
 */
public class ProfileManager {
    /** Stores usernames (not display names) as keys, corresponding to a value of that user's profile object. */
    private final HashMap<String, Profile> profiles;
    /** An undirected graph where each user is represented by a vertex and their friendships are represented by
     * bidirectional edges. */
    private final UndirectedGraph<Profile> network;

    /**
     * Constructor creates a new ProfileManager object containing no profiles and an empty network graph.
     */
    public ProfileManager() {
        profiles = new HashMap<>();
        network = new UndirectedGraph<>();
    }

    /**
     * Adds a new user and their profile to the social network if they are not already a member.
     * @param username The username of the new user.
     * @param newProfile The profile object that corresponds to the new user.
     */
    public void addProfile(String username, Profile newProfile) {
        if (!profiles.containsKey(newProfile.getName())) {
            profiles.put(username, newProfile);
            network.addVertex(newProfile);
        }
    }

    /**
     * Removes a user and their profile from the social network if they are a member.
     * @param username The username of the user and corresponding profile to remove.
     */
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

    /**
     * Retrieves the number of profiles currently part of the social media network.
     * @return The number of profiles currently part of the social media network.
     */
    public int getNumProfiles() {
        return profiles.size();
    }

    /**
     * Retrieves a profile object corresponding to the specified username.
     * @param username The username of the user corresponding to the profile to retrieve.
     * @return The profile object corresponding to the specified username.
     */
    public Profile getProfile(String username) {
        if (containsProfile(username))
            return profiles.get(username);
        else
            return null;
    }

    /**
     * Determines if the specified username is a member of the social media network.
     * @param username The username of the user to determine membership.
     * @return True if the user is a member, or false if they are not.
     */
    public boolean containsProfile(String username) { return (username != null) && (profiles.containsKey(username)); }

    /**
     * Retrieves a list of the usernames of all members of the social network.
     * @return A list of the usernames of all members of the social network.
     */
    public ArrayList<String> getAllUsernames() { return new ArrayList<>(profiles.keySet()); }

    /**
     * Retrieves a list of the usernames of all friends, or only best friends, of the specified user.
     * @param username The username of the user for which to retrieve friend usernames.
     * @param best False to retrieve all friend usernames, or true to only retrieve best friend usernames.
     * @return A list of the usernames of all friends, or best friends, of the specified user.
     */
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

    /**
     * Creates a new friendship or best friendship between the two specified users.
     * @param username1 The username of the first member of the friendship.
     * @param username2 The username of the second member of the friendship.
     * @param best True to create a best friendship, or false to create a normal friendship.
     */
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

    /**
     * Removes any friendship between the two specified users.
     * @param username1 The username of the first member of the friendship.
     * @param username2 The username of the second member of the friendship.
     */
    public void removeFriendship(String username1, String username2) {
        if (profiles.containsKey(username1) && profiles.containsKey(username2)) {
            Profile profile1 = profiles.get(username1);
            Profile profile2 = profiles.get(username2);
            network.removeEdge(profile1, profile2);
            profile1.removeFriend(profile2);
            profile2.removeFriend(profile1);
        }
    }

    /**
     * Retrieves a list of the specified user's friends of friends. Note that this list does not include the
     * specified user or any of their direct friends.
     * @param username The username for which to retrieve a list of friends of friends.
     * @return A list of the specified user's friends of friends.
     */
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

    /**
     * Displays the profiles of all users that can be reached through a breadth-first traversal of the network graph,
     * starting at the specified user. This will show all profiles ultimately connected to the specified user by a
     * chain of friendships.
     * @param username The username corresponding to the profile where to begin the graph traversal.
     */
    public void displayAllConnectedProfiles(String username) {
        Profile originProfile = profiles.get(username);
        Queue<Profile> profileQueue = network.getBreadthFirstTraversal(originProfile);
        while (!profileQueue.isEmpty()) {
            System.out.print("\n");
            profileQueue.remove().printProfileDetails();
        }
    }

    /**
     * Displays the profiles of all users on the social media network.
     */
    public void displayAllProfiles() {
        for (Profile curProfile : profiles.values()) {
            System.out.print("\n");
            curProfile.printProfileDetails();
        }
    }
}






