package SocialMediaPackage;

import MenuPackage.TextMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Implementation of the interactive portion of a social media network, including the user interface, login system
 * and support methods.
 */
public class SocialMedia {
    /** The name of the social media network. */
    private static final String NETWORK_NAME = "Social Media Network";
    /** Stores the username of the currently logged-in user. */
    private String activeUser;
    /** The profile manager used to perform non-interactive functions involving user profiles. */
    private final ProfileManager profileMgr;
    /** The text based main menu that allows users to interact with the social media network. */
    private final TextMenu mainMenu;

    /**
     * Constructor creates a new SocialMedia object and builds the main menu.
     */
    public SocialMedia() {
        activeUser = null;
        profileMgr = new ProfileManager();
        mainMenu = new TextMenu(NETWORK_NAME, "Main Menu", "Please choose a function.");
        mainMenu.addMenuEntry("Add new user/profile", this::addNewProfile);
        mainMenu.addMenuEntry("Delete a user/profile", this::deleteProfile);
        mainMenu.addMenuEntry("Modify my profile", this::modifyProfile);
        mainMenu.addMenuEntry("Select online status", this::selectStatus);
        mainMenu.addMenuEntry("Add profile as friend", this::addFriendProfile);
        mainMenu.addMenuEntry("Add profile as best friend", this::addBestFriendProfile);
        mainMenu.addMenuEntry("Remove profile as friend", this::removeFriendProfile);
        mainMenu.addMenuEntry("View my profile information", this::displayCurrentProfile);
        mainMenu.addMenuEntry("Look up complete profile list and information", this::lookUpProfileInfo);
        mainMenu.addMenuEntry("View friend list and profile information", this::lookupFriendProfile);
        mainMenu.addMenuEntry("View best friend list and profile information", this::lookupBestFriendProfile);
        mainMenu.addMenuEntry("View friends of friends and profile information", this::viewFriendsOfFriends);
        mainMenu.addMenuEntry("View all connected profiles", this::viewAllConnectedProfiles);
        mainMenu.addMenuEntry("View all profiles on the network", this::viewAllProfiles);
        mainMenu.addMenuEntry("Log out and change user", this::logout);
        mainMenu.addMenuEntry("Exit", this::logout);
    }

    /**
     * Adds example users, their profiles, and example friendships to the network for testing purposes.
     */
    public void addExampleProfiles() {
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
        profileMgr.createFriendship("Jimmy", "Art",true);
        profileMgr.createFriendship("Art", "Bob",true);
        profileMgr.createFriendship("Bob", "Jimmy",false);
        profileMgr.createFriendship("Jimmy", "Bob",true);
        profileMgr.createFriendship("Bill", "Bob",false);
        profileMgr.createFriendship("Alex", "Jimmy",false);
        profileMgr.createFriendship("Alex", "Art",false);
    }

    /**
     * Adds a new user and profile to the social media network based on the current user's text input if the new user
     * is not already a member.
     */
    private void addNewProfile() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nCreating new user/profile...");
        System.out.print("Enter new user's user name (not the same as display name): ");
        String username = input.next();
        System.out.print("Enter new user's display name (not the same as user name): ");
        String displayName = input.next();
        System.out.print("Enter the URL or path for this user's profile image: ");
        String image = input.next();

        if (!profileMgr.containsProfile(username)) {
            Profile newProfile = new Profile(displayName, image);
            profileMgr.addProfile(username, newProfile);
            System.out.println("\nUser " + username + " added.");
        } else {
            System.out.println("\nUser " + username + " already exists. No new user added.");
        }
    }

    /**
     * Removes a user and their profile from the social media network based on the current user's menu selection of
     * available profiles. Note that this will not allow the current user to remove their own profile.
     */
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

    /**
     * Modifies the current user's profile information based on the current user's text input.
     */
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

    /**
     * Displays profile information for the current user.
     */
    private void displayCurrentProfile() { displayProfileInfo(activeUser); }

    /**
     * Displays a list of all profiles, and profile information for a user based on the current user's menu
     * selection of available profiles. Note that this will not allow the current user to view their own profile
     * information, which can be accessed via the menu option that executes displayCurrentProfile()
     */
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

    /**
     * Adds a new friendship for the current user based on their menu selection of available profiles. Note that this
     * will not allow a user to add themselves or their existing friends as a new friend.
     */
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
                profileMgr.createFriendship(activeUser, selectedUser, false);
                System.out.println("\nAdded " + profileMgr.getProfile(selectedUser).getName() + " as a friend.");
            }
            else
                System.out.println("\nAdd friend profile cancelled.");
        }
    }

    /**
     * Adds a new best friendship for the current user based on their menu selection of available profiles. Note
     * that this will not allow a user to add themselves or their existing best friends as a new best friend.
     */
    private void addBestFriendProfile() {
        ArrayList<String> userList = profileMgr.getAllUsernames();
        userList.remove(activeUser);
        for (String friend : profileMgr.getFriendUsernames(activeUser,false))
            userList.remove(friend);

        if (userList.isEmpty())
            System.out.println("\nThere are no profiles available to add as a best friend.");
        else {
            String selectedUser = displayProfileMenu("Please choose a profile to add as a best friend.", userList);
            if (selectedUser != null) {
                profileMgr.createFriendship(activeUser, selectedUser, true);
                System.out.println("\nAdded " + profileMgr.getProfile(selectedUser).getName() + " as a best friend.");
            }
            else
                System.out.println("\nAdd best friend profile cancelled.");
        }
    }

    /**
     * Removes an existing friendship from the current user's friends based on their menu selection of friend
     * profiles.
     */
    private void removeFriendProfile() {
        ArrayList<String> userList = profileMgr.getFriendUsernames(activeUser, false);
        if (userList.isEmpty())
            System.out.println("\nNo friend profiles to remove.");
        else {
            String selectedUser = displayProfileMenu("Please choose a profile to remove as a friend.", userList);
            if (selectedUser != null) {
                profileMgr.removeFriendship(activeUser, selectedUser);
                System.out.println("\nFriendship with " + profileMgr.getProfile(selectedUser).getName() + " removed.");
            }
            else
                System.out.println("\nFriend profile removal cancelled.");
        }
    }

    /**
     * Displays a list of all friend profiles, and profile information for a user based on the current user's
     * menu selection of existing friends.
     */
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

    /**
     * Displays a list of all best friend profiles, and profile information for a user based on the current user's
     * menu selection of existing best friends.
     */
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

    /**
     * Displays a list of all friends of friends, and profile information for a user based on the current user's
     * menu selection of friends of friends. Note that this will not allow the user to view their own profile
     * information or that of directly connected friends.
     */
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

    /**
     * Displays the profiles of all users that can be reached through a breadth-first traversal of the network graph,
     * starting at the current user. This will show all profiles ultimately connected to the current user by a
     * chain of friendships.
     */
    private void viewAllConnectedProfiles() {
        System.out.println("\nAll profiles connected to " + profileMgr.getProfile(activeUser).getName() + ":");
        profileMgr.displayAllConnectedProfiles(activeUser);
        pause();
    }

    /**
     * Displays the profiles of all users on the social media network.
     */
    private void viewAllProfiles() {
        System.out.println("\nAll " + profileMgr.getNumProfiles() + " profiles available on " + NETWORK_NAME + ":");
        profileMgr.displayAllProfiles();
        pause();
    }

    /**
     * Allows the current user to select their availability status based on their menu selection of availability
     * statuses.
     */
    private void selectStatus() {
        TextMenu statusMenu = new TextMenu(NETWORK_NAME, ("Online Status Selection - Current status: " +
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

    /**
     * Starts the login and menu system for the social media network, allowing a user to log in and interact with
     * the network. This method will not terminate until the user interactively exits the application.
     */
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

    /**
     * Allows a user to log in to the social media network with their username. If they are a current member, they
     * will be presented with the main menu. If they are a new member, they will be prompted to create a new profile,
     * and then logged in.
     */
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

    /**
     * Adds a new profile for a new member on login. Called by login() if the login username is not found.
     */
    private void newProfileOnLogin() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nCreating new profile...");
        System.out.print("Enter your display name (not the same as user name): ");
        String displayName = input.next();
        System.out.print("Enter the URL or path for your profile image: ");
        String image = input.next();

        Profile newProfile = new Profile(displayName, image);
        profileMgr.addProfile(activeUser, newProfile);
    }

    /**
     * Logs the current user out and allows a new user to log in, or the application to cleanly exit.
     */
    private void logout() {
        System.out.println("\nLogging off user name " + activeUser);
        profileMgr.getProfile(activeUser).setStatus(Profile.statusTypes.OFFLINE);
        activeUser = null;
    }

    /**
     * Prints the profile information and friends list for the specified username.
     * @param username The username for which to print profile information and friends list.
     */
    private void displayProfileInfo(String username) {
        System.out.println("\nProfile info:\n");
        profileMgr.getProfile(username).printProfileDetails();
        pause();
    }

    /**
     * Support method allows the user to choose a profile from the specified list of usernames, based on menu input.
     * @param prompt The prompt describing the operation that will be performed on the selected profile.
     * @param userList The list of usernames to select from.
     * @return The username of the profile selected.
     */
    private String displayProfileMenu(String prompt, ArrayList<String> userList) {
        TextMenu profileMenu = new TextMenu(NETWORK_NAME, "Available Profiles", prompt);
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

    /**
     * Pauses console output until the enter key is pressed, allowing the user time to read output before returning
     * to the main menu.
     */
    private void pause() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nPress <enter> to continue...");
        String newLine = input.nextLine();
    }
}


