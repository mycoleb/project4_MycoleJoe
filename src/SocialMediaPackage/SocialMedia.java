package SocialMediaPackage;

import MenuPackage.TextMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class SocialMedia {
    private static final String name = "Social Media Network";
    private String activeUser;
    private final ProfileManager profiles;
    private final TextMenu mainMenu;

    public SocialMedia() {
        activeUser = null;
        profiles = new ProfileManager();
        mainMenu = new TextMenu(name, "Main Menu", "Please choose a function.");
        mainMenu.addMenuEntry("Add new user/profile", this::addNewProfile);
        mainMenu.addMenuEntry("Delete a user/profile", this::deleteProfile);
        mainMenu.addMenuEntry("Modify your profile", this::modifyProfile);
        mainMenu.addMenuEntry("View my profile information", this::displayProfileInfo);
        mainMenu.addMenuEntry("Select profile to look up information", this::lookUpProfileInfo);
        mainMenu.addMenuEntry("Select profile to add as friend", this::addFriendByProfile);
        mainMenu.addMenuEntry("Add a friend by username", this::addFriendByUsername);
        mainMenu.addMenuEntry("View friends", this::modifyProfile);
        mainMenu.addMenuEntry("View friends of friends", this::viewFriendsOfFriends);
        mainMenu.addMenuEntry("Log out and change user", this::logout);
        mainMenu.addMenuEntry("Exit", this::logout);
    }

    public void start() {
        int menuChoice = 0;

        while (menuChoice != mainMenu.getNumEntries()) {
            if (activeUser == null)
                login();
            if (activeUser != null) {
                mainMenu.setMenuName("Main Menu for " + profiles.getProfile(activeUser).getName());
                menuChoice = mainMenu.displayMenu();
            }
        }
    }

    private void login() {
        Scanner input = new Scanner(System.in);
        System.out.print("\nEnter your user name (not profile name): ");
        String username = input.next();

        if (profiles.containsProfile(username)) {
            System.out.println("User name " + username + " found. Welcome!");
            activeUser = username;
            profiles.getProfile(activeUser).setStatus(Profile.statusTypes.ONLINE);
        }
        else {
            System.out.println("User name " + username + " not found. Creating new profile.");
            activeUser = username;
            addProfile();
            profiles.getProfile(activeUser).setStatus(Profile.statusTypes.ONLINE);
            System.out.println("\nProfile created. Logging in with the user name " + username + "...");
        }
        //input.close();
    }

    private void addProfile() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nCreating new profile...");
        System.out.print("Enter your display name (not the same as user name): ");
        String displayName = input.next();
        System.out.print("Enter the URL for your image: ");
        String image = input.next();

        Profile newProfile = new Profile(displayName, image);
        profiles.addProfile(activeUser, newProfile);
    }

    private void addNewProfile() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nCreating new user/profile...");
        System.out.print("Enter your user name (not the same as display name): ");
        String username = input.next();
        System.out.print("Enter your display name (not the same as user name): ");
        String displayNname = input.next();
        System.out.print("Enter the URL for your image: ");
        String image = input.next();

        Profile newProfile = new Profile(displayNname, image);
        profiles.addProfile(username, newProfile);
    }

    private void modifyProfile() {
        Scanner input = new Scanner(System.in);
        Profile curProfile = profiles.getProfile(activeUser);
        System.out.println("\nEditing profile for " + activeUser + "...");
        System.out.print("Enter new display name (not the same as user name): ");
        String name = input.next();
        System.out.print("Enter new URL for your image: ");
        String image = input.next();

        curProfile.setName(name);
        curProfile.setImage(image);
    }

    private void displayProfileInfo() {
        Profile curProfile = profiles.getProfile(activeUser);
        System.out.println("\nProfile info:");
        curProfile.printProfileDetails();
        pause();
    }

    private String displayProfileMenu() {
        TextMenu profileMenu = new TextMenu(name, "Available Profiles",
                "Please choose a profile.");
        ArrayList<String> userList = profiles.getAllUsernames();
        Iterator<String> usernameIter = userList.iterator();
        String curUsername;

        while (usernameIter.hasNext()) {
            curUsername = usernameIter.next();
            profileMenu.addMenuEntry(profiles.getProfile(curUsername).getName(),this::noOp);
        }

        int choice = profileMenu.displayMenu();

        if (choice == userList.size())
            return null;
        else
            return userList.get(choice - 1);
    }

    private void noOp() {}

    private void pause() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nPress <enter> to continue...");
        String newLine = input.nextLine();
    }


    private void lookUpProfileInfo() {
        displayProfileMenu();

    }

    private void addFriendByProfile() {
        System.out.println("\nAdd Friend by profile stub:");


    }

    private void addFriendByUsername() {
        System.out.println("\nAdd friend stub");
    }

    private void viewFriends() {
        System.out.println("\nView friends stub");

    }

    private void deleteProfile() {
        System.out.println("\nDelete profile stub");

    }

    private void viewFriendsOfFriends() {
        System.out.println("\nView friends of friends stub");

    }

    private void logout() {
        System.out.println("\nLogging off user name " + activeUser);
        profiles.getProfile(activeUser).setStatus(Profile.statusTypes.OFFLINE);
        activeUser = null;
    }
}


