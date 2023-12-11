package MenuPackage;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * TextMenu: A modular implementation of a text-based menu system for use with console applications.
 */
public class TextMenu {
    /** Stores the name of the application. */
    private String appName;
    /** Stores the name of the menu, appears in the menu header. */
    private String menuName;
    /** Stores the user prompt for the menu, appears immediately before the input. */
    private String prompt;
    /** Stores the text labels/description of each available menu option. */
    private final ArrayList<String> itemLabels;
    /** Stores references to functions to be executed when the user selects a menu option. */
    private final ArrayList<Runnable> itemFunctions;
    /** Tracks the number of menu options available. */
    private int numEntries;

    /**
     * Constructor creates a new TextMenu object with the specified app name, menu name, and prompt.
     * @param newAppName The name of the application for which this menu is for.
     * @param newMenuName The title or purpose of this menu.
     * @param newPrompt The prompt displayed before user input.
     */
    public TextMenu(String newAppName, String newMenuName, String newPrompt) {
        setAppName(newAppName);
        setMenuName(newMenuName);
        setPrompt(newPrompt);
        itemLabels = new ArrayList<>();
        itemFunctions = new ArrayList<>();
        numEntries = 0;
    }

    /**
     * Sets the name of the application for which this menu is for.
     * @param newAppName The name of the application for which this menu is for.
     */
    public void setAppName(String newAppName) {
        if (newAppName != null)
            appName = newAppName;
    }

    /**
     * Sets the header of this menu, containing its title or purpose.
     * @param newMenuName The header of this menu.
     */
    public void setMenuName(String newMenuName) {
        if (newMenuName != null)
            menuName = newMenuName;
    }

    /**
     * Sets the prompt displayed before user input.
     * @param newPrompt The prompt displayed before user input.
     */
    public void setPrompt(String newPrompt) {
        if (newPrompt != null)
            prompt = newPrompt;
    }

    /**
     * Adds a new menu entry to this menu with the specified label and function.
     * @param label The label or description of this menu option.
     * @param function The function to be executed when this menu option is selected in the format <class>::<method>
     * To disable this functionality, use the noOp method in this class like so: TextMenu::noOp
     * @return False if this menu already contains this label, the label is null, or the function is null. True if
     * the menu entry was added successfully.
     */
    public boolean addMenuEntry(String label, Runnable function) {
        if (itemLabels.contains(label) || label == null || function == null)
            return false;
        else {
            itemLabels.add(label);
            itemFunctions.add(function);
            numEntries++;
            return true;
        }
    }

    /**
     * Removes a menu entry from this menu with the specified label.
     * @param label The label of the menu option to remove.
     * @return False if the menu does not contain this label, or the label is null. True if the menu entry was
     * removed successfully.
     */
    public boolean removeMenuEntry(String label) {
        if (!itemLabels.contains(label) || label == null)
            return false;
        else {
            int index = itemLabels.indexOf(label);
            itemLabels.remove(index);
            itemFunctions.remove(index);
            numEntries--;

            return true;
        }
    }

    /**
     * Retrieves the number of menu entries available in this menu to select from.
     * @return The number of menu entries in this menu.
     */
    public int getNumEntries() { return numEntries; }

    /**
     * Displays the menu, including the header, menu entries, and prompt. Takes and validates user input, looping
     * until valid input has been obtained. Executes the method that corresponds to the chosen menu entry.
     * @return An integer that corresponds to the menu entry chosen, from 1 to the number of entries available.
     */
    public int displayMenu() {
        String header = (appName + " - " + menuName);
        String separator = "-".repeat(header.length());
        Scanner inputScanner = new Scanner(System.in);
        boolean choiceMade = false;
        String entry;
        int choice = 0;
        boolean validInput = true;

        while (!choiceMade) {
            System.out.println("\n" + header);
            System.out.println(separator);

            for (int index = 0; index < numEntries; index++)
                System.out.println((index + 1) + ": " + itemLabels.get(index));

            System.out.print("\n");
            if (!validInput)
                System.out.println("Previous input invalid.");

            System.out.println(prompt);
            System.out.print("Enter a number from 1 to " + numEntries + ": ");
            entry = inputScanner.next();

            try {
                choice = Integer.parseInt(entry);
            } catch (NumberFormatException e) {
                validInput = false;
            }

            if ((choice > 0) && (choice <= numEntries))
                choiceMade = true;
            else
                validInput = false;
        }

        Runnable function = itemFunctions.get(choice - 1);
        if (function != null)
            function.run();
        return choice;
    }

    /**
     * Menu entries must contain a valid method to execute. If execution of a method is not desired in lieu of the
     * returned selection index, specify this function (which performs no operation) with TextMenu::noOp when
     * creating the menu entry.
     */
    public static void noOp() {}

}
