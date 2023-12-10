package SocialMediaPackage;

import MenuPackage.TextMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class SocialMedia {
    private static final String networkName = "Social Media Network";
    private String activeUser;
    private final ProfileManager profileMgr;
    private final TextMenu mainMenu;

    public SocialMedia() {
        activeUser = null;
        profileMgr = new ProfileManager();
        mainMenu = new TextMenu(networkName, "Main Menu", "Please choose a function.");
        mainMenu.addMenuEntry("Add new user/profile", this::addNewProfile);
        mainMenu.addMenuEntry("Delete a user/profile", this::deleteProfile);
        mainMenu.addMenuEntry("Modify my profile", this::modifyProfile);
        mainMenu.addMenuEntry("Select online status", this::selectStatus);
        mainMenu.addMenuEntry("Add profile as friend", this::addFriendProfile);
        mainMenu.addMenuEntry("Add profile as best friend", this::addBestFriendProfile);
        mainMenu.addMenuEntry("Remove profile as friend", this::removeFriendProfile);
        mainMenu.addMenuEntry("View my profile information", this::displayCurrentProfile);
        mainMenu.addMenuEntry("Look up profile information", this::lookUpProfileInfo);
        mainMenu.addMenuEntry("View friend profile information", this::lookupFriendProfile);
        mainMenu.addMenuEntry("View best friend profile information", this::lookupBestFriendProfile);
        mainMenu.addMenuEntry("View friends of friends profile information", this::viewFriendsOfFriends);
        mainMenu.addMenuEntry("View all connected profiles", this::viewAllConnectedProfiles);
        mainMenu.addMenuEntry("View all profiles on the network", this::viewAllProfiles);
        mainMenu.addMenuEntry("Log out and change user", this::logout);
        mainMenu.addMenuEntry("Exit", this::logout);

        addExampleProfiles(); // For testing
    }

    private void addExampleProfiles() {
        // Add example users
        profileMgr.addProfile("Joe", new Profile("Spencer", "spencer.jpg"));
        profileMgr.addProfile("Bob", new Profile("Robert", "robert.jpg"));
        profileMgr.addProfile("Bill", new Profile("William", "william.jpg"));
        profileMgr.addProfile("Jimmy", new Profile("James", "james.jpg"));
        profileMgr.addProfile("Alex", new Profile("Alexander", "alexander.jpg"));
        profileMgr.addProfile("Art", new Profile("Arthur", "arthur.jpg"));
        // Add example friendships
        profileMgr.createFriendship("Joe", "Alex",true);
        profileMgr.createFriendship("Joe", "Bill",false);
        profileMgr.createFriendship("Jimmy", "Art",false);
        profileMgr.createFriendship("Art", "Bob",true);
        profileMgr.createFriendship("Bob", "Jimmy",false);
        profileMgr.createFriendship("Jimmy", "Bob",false);
        profileMgr.createFriendship("Bill", "Bob",false);
        profileMgr.createFriendship("Alex", "Jimmy",false);
        profileMgr.createFriendship("Alex", "Art",false);
    }

    private void addNewProfile() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nCreating new user/profile...");
        System.out.print("Enter your user name (not the same as display name): ");
        String username = input.next();
        System.out.print("Enter your display name (not the same as user name): ");
        String displayName = input.next();
        System.out.print("Enter the URL for your image: ");
        String image = input.next();

        if (!profileMgr.containsProfile(username)) {
            Profile newProfile = new Profile(displayName, image);
            profileMgr.addProfile(username, newProfile);
            System.out.println("\nUser " + username + " added.");
        } else {
            System.out.println("\nUser " + username + " already exists. No new user added.");
        }
    }

    private void deleteProfile() {
        ArrayList<String> userList = profileMgr.getAllUsernames();
        userList.remove(activeUser);
        if (userList.isEmpty())
            System.out.println("\nThere are no profiles other than your own to delete.");
        else {
            String selectedUser = displayProfileMenu("Please choose a profile to delete.", userList);
            if (selectedUser != null) {
                profileMgr.removeProfile(selectedUser);
                System.out.println("\nUser " + selectedUser + " deleted.");
            } else
                System.out.println("\nDelete profile cancelled.");
        }
    }

    private void modifyProfile() {
        Scanner input = new Scanner(System.in);
        Profile curProfile = profileMgr.getProfile(activeUser);
        System.out.println("\nEditing profile for " + activeUser + "...");
        System.out.print("Enter new display name (not the same as user name): ");
        String name = input.next();
        System.out.print("Enter new URL for your image: ");
        String image = input.next();

        curProfile.setName(name);
        curProfile.setImage(image);
    }

    private void displayCurrentProfile() { displayProfileInfo(activeUser); }

    private void lookUpProfileInfo() {
        ArrayList<String> userList = profileMgr.getAllUsernames();
        userList.remove(activeUser);
        if (userList.isEmpty())
            System.out.println("\nThere are no profiles other than your own to view.");
        else {
            String selectedUser = displayProfileMenu("Please choose a profile to view.", userList);
            if (selectedUser != null)
                displayProfileInfo(selectedUser);
            else
                System.out.println("\nLookup cancelled.");
        }
    }

    private void addFriendProfile() {
        ArrayList<String> userList = profileMgr.getAllUsernames();
        userList.remove(activeUser);
        for (String friend : profileMgr.getFriendUsernames(activeUser,false))
            userList.remove(friend);

        if (userList.isEmpty())
            System.out.println("\nThere are no profiles available to add as a friend.");
        else {
            String selectedUser = displayProfileMenu("Please choose a profile to add as a friend.", userList);
            if (selectedUser != null) {
                addFriend(selectedUser, false);
                System.out.println("\nAdded " + profileMgr.getProfile(selectedUser).getName() + " as a friend.");
            }
            else
                System.out.println("\nAdd friend profile cancelled.");
        }
    }

    private void addBestFriendProfile() {
        ArrayList<String> userList = profileMgr.getAllUsernames();
        userList.remove(activeUser);
        for (String friend : profileMgr.getFriendUsernames(activeUser,true))
            userList.remove(friend);

        if (userList.isEmpty())
            System.out.println("\nThere are no profiles available to add as a best friend.");
        else {
            String selectedUser = displayProfileMenu("Please choose a profile to add as a best friend.", userList);
            if (selectedUser != null) {
                addFriend(selectedUser, true);
                System.out.println("\nAdded " + profileMgr.getProfile(selectedUser).getName() + " as a best friend.");
            }
            else
                System.out.println("\nAdd best friend profile cancelled.");
        }
    }

    private void removeFriendProfile() {
        ArrayList<String> userList = profileMgr.getFriendUsernames(activeUser, false);
        if (userList.isEmpty())
            System.out.println("\nNo friend profiles to remove.");
        else {
            String selectedUser = displayProfileMenu("Please choose a profile to remove as a friend.", userList);
            if (selectedUser != null) {
                profileMgr.removeFriendship(activeUser, selectedUser);
                System.out.println("Friendship with " + profileMgr.getProfile(selectedUser).getName() + " removed.");
            }
            else
                System.out.println("\nFriend profile removal cancelled.");
        }
    }

    private void lookupFriendProfile() {
        ArrayList<String> userList = profileMgr.getFriendUsernames(activeUser, false);
        if (userList.isEmpty())
            System.out.println("\nNo friend profiles to view.");
        else {
            String selectedUser = displayProfileMenu("Please choose a friend profile to view.", userList);
            if (selectedUser != null)
                displayProfileInfo(selectedUser);
            else
                System.out.println("\nFriend Lookup cancelled.");
        }
    }

    private void lookupBestFriendProfile() {
        ArrayList<String> userList = profileMgr.getFriendUsernames(activeUser, true);
        if (userList.isEmpty())
            System.out.println("\nNo best friend profiles to view.");
        else {
            String selectedUser = displayProfileMenu("Please choose a best friend profile to view.", userList);
            if (selectedUser != null)
                displayProfileInfo(selectedUser);
            else
                System.out.println("\nBest friend Lookup cancelled.");
        }
    }

    private void viewFriendsOfFriends() {
        ArrayList<String> userList = profileMgr.getFriendsOfFriendsUsernames(activeUser);
        if (userList.isEmpty())
            System.out.println("\nNo friends of friends profiles to view.");
        else {
            String selectedUser = displayProfileMenu("Please choose friend of friend profile to view.", userList);
            if (selectedUser != null)
                displayProfileInfo(selectedUser);
            else
                System.out.println("\nFriends of friends Lookup cancelled.");
        }
    }

    private void viewAllConnectedProfiles() {
        System.out.println("\nAll profiles connected to " + profileMgr.getProfile(activeUser).getName() + ":");
        profileMgr.displayAllConnectedProfiles(activeUser);
        pause();
    }

    private void viewAllProfiles() {
        System.out.println("\nAll " + profileMgr.getNumProfiles() + " profiles available on " + networkName + ":");
        profileMgr.displayAllProfiles();
        pause();
    }

    private void selectStatus() {
        TextMenu statusMenu = new TextMenu(networkName, ("Online Status Selection - Current status: " +
                profileMgr.getProfile(activeUser).getStatus()), "Please choose your online status.");
        for (Profile.statusTypes status : Profile.statusTypes.values())
            statusMenu.addMenuEntry(status.toString(), TextMenu::noOp);

        statusMenu.addMenuEntry("Cancel - Back to Main Menu", TextMenu::noOp);
        int choice = statusMenu.displayMenu();

        if (choice == statusMenu.getNumEntries())
            System.out.println("\nStatus selection cancelled.");
        else {
            profileMgr.getProfile(activeUser).setStatus(Profile.statusTypes.values()[choice - 1]);
            System.out.println("\nStatus set to " + profileMgr.getProfile(activeUser).getStatus());
        }

    }

    public void start() {
        int menuChoice = 0;

        while (menuChoice != mainMenu.getNumEntries()) {
            if (activeUser == null)
                login();
            if (activeUser != null) {
                mainMenu.setMenuName(profileMgr.getNumProfiles() + " profiles on the network - Main Menu for " +
                        profileMgr.getProfile(activeUser).getName());
                menuChoice = mainMenu.displayMenu();
            }
        }
    }

    private void login() {
        Scanner input = new Scanner(System.in);
        System.out.print("\nEnter your user name (not profile name): ");
        String username = input.next();

        if (profileMgr.containsProfile(username)) {
            System.out.println("User name " + username + " found. Welcome!");
            activeUser = username;
            profileMgr.getProfile(activeUser).setStatus(Profile.statusTypes.ONLINE);
        }
        else {
            System.out.println("User name " + username + " not found. Creating new profile.");
            activeUser = username;
            newProfileOnLogin();
            profileMgr.getProfile(activeUser).setStatus(Profile.statusTypes.ONLINE);
            System.out.println("\nProfile created. Logging in with the user name " + username + "...");
        }
    }

    private void newProfileOnLogin() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nCreating new profile...");
        System.out.print("Enter your display name (not the same as user name): ");
        String displayName = input.next();
        System.out.print("Enter the URL for your image: ");
        String image = input.next();

        Profile newProfile = new Profile(displayName, image);
        profileMgr.addProfile(activeUser, newProfile);
    }

    private void logout() {
        System.out.println("\nLogging off user name " + activeUser);
        profileMgr.getProfile(activeUser).setStatus(Profile.statusTypes.OFFLINE);
        activeUser = null;
    }

    private void addFriend(String username, boolean best) { profileMgr.createFriendship(activeUser, username, best); }

    private void displayProfileInfo(String username) {
        System.out.println("\nProfile info:\n");
        profileMgr.getProfile(username).printProfileDetails();
        pause();
    }

    private String displayProfileMenu(String prompt, ArrayList<String> userList) {
        TextMenu profileMenu = new TextMenu(networkName, "Available Profiles", prompt);
        Iterator<String> usernameIter = userList.iterator();
        String curUsername;

        while (usernameIter.hasNext()) {
            curUsername = usernameIter.next();
            profileMenu.addMenuEntry(profileMgr.getProfile(curUsername).getName(), TextMenu::noOp);
        }
        profileMenu.addMenuEntry("Cancel - Back to Main Menu", TextMenu::noOp);
        int choice = profileMenu.displayMenu();

        if (choice == userList.size() + 1)
            return null;
        else
            return userList.get(choice - 1);
    }

    private void pause() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nPress <enter> to continue...");
        String newLine = input.nextLine();
    }

}


