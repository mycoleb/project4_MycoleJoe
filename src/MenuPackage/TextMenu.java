package MenuPackage;

import java.util.ArrayList;
import java.util.Scanner;


public class TextMenu {
    private String appName;
    private String menuName;
    private final ArrayList<String> itemLabels;
    private final ArrayList<Runnable> itemFunctions;
    private int numEntries;

    public TextMenu() {
        this("Application", "Main Menu");
    }

    public TextMenu(String newAppName, String newMenuName) {
        setAppName(newAppName);
        setMenuName(newMenuName);
        itemLabels = new ArrayList<>();
        itemFunctions = new ArrayList<>();
        numEntries = 0;
    }

    public void setAppName(String newAppName) {
        if (newAppName != null)
            appName = newAppName;
    }

    public void setMenuName(String newMenuName) {
        if (newMenuName != null)
            menuName = newMenuName;
    }

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

    public int getNumEntries() { return numEntries; }

    public int displayMenu() {
        String header = (appName + " - " + menuName);
        String separator = "-".repeat(header.length());
        Scanner inputScanner = new Scanner(System.in);
        boolean choiceMade = false;
        boolean exiting = false;
        String entry;
        int choice = 0;
        boolean validInput = true;

        while (!choiceMade) {
            System.out.println("\n" + header);
            System.out.println(separator);

            for (int index = 1; index <= numEntries; index++)
                System.out.println(index + ": " + itemLabels.get(index - 1));

            System.out.print("\n");
            if (!validInput)
                System.out.println("Previous input invalid.");

            System.out.print("Please enter a number from 1 to " + numEntries + ": ");
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

        Runnable function = itemFunctions.get(choice);
        if (function != null)
            function.run();
        return choice;
    }
}
