/*  
 * Driver class for the entirity of the project
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Recipe_Repository {
    
    //TODO: Build the database
    private static ArrayList<Recipe> dataBase;

    //Function for 
    private static void showMenu() {

        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("Main Menu\n");
        output.append("------------------------------\n");
        output.append("1 - Display Entire Data Base\n");
        output.append("2 - Diplay Titles in Data Base\n");
        output.append("3 - Search Data Base\n");
        output.append("4 - Add new Recipe to database\n");
        output.append("5 - Add new Recipe via GUI\n");
        output.append("6 - Read in database\n");
        output.append("7 - Write out database\n");
        output.append("0 - Exit\n");
        output.append(">");

        System.out.printf("%s", output.toString());
    } //End ShowMenu

    //readLinesFromFile was greatly helped by ChatGPT
    private static ArrayList<String> readLinesFromFile(File fileInput) throws IOException {

        File fileToUse = Recipe_Tools.getTempFile();

        if(fileInput == null) {

            //Build a fileChooser that opens in the Present Working Directory
            JFileChooser fileChooser = new JFileChooser();
            String currentDirectory = System.getProperty("user.dir");
            File pwd = new File(currentDirectory);
            fileChooser.setCurrentDirectory(pwd);

            //Filter to only show text files
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            
            //Finally choose the file and import it
            int result = fileChooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                
                fileToUse = fileChooser.getSelectedFile();
                             
                
            } //End if
        } //End of If

        else{
            fileToUse = fileInput;
        }

        //Read the file line by line and store to arraylist of strings
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader  = new BufferedReader(new FileReader(fileToUse));
        String line;
        //Until we reach the last line of the file...
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;


    } //End readLinesFromFile

    private static void searchDataBase() {
        //TODO
    } //End searchDataBase

    private static void storeNewRecipe(File fileInput) {

        try {
            ArrayList<String> lines = readLinesFromFile(fileInput);
            if(lines != null) {
                Recipe tempRecipe = new Recipe(lines, generateUID());
                dataBase.add(tempRecipe);
            } //End if
            else {
                System.out.println("Error when reading recipe.");
            } //End else

        } catch(Exception e) {

            System.out.println("Caught an exception in storeNewRecipe function");
            e.printStackTrace();

        } //End Try/Catch
    } //End storeNewRecipe

    private static void displayEntries(String type) {

        //Make sure there's something in the database to display
        if(dataBase.isEmpty()) {
            System.out.println("\nDatabase is empty!");
            return;
        } //End If

        //TODO: Should we be using an enum here?
        switch(type) {

            //Option to dump entire database
            case "allAll":
                for(Recipe currRecipe : dataBase) {
                    System.out.printf("\n%s\n\n", currRecipe.toString());
                } //End For
                return;     //Acting as a break, but should cut processing a tiny bit

            //Option to show just the titles in the database
            case "allTitles":
                int i = 0;
                System.out.println();
                for(Recipe currRecipe : dataBase) {
                    System.out.printf("%d - %s\n", i++, currRecipe.getTitle());
                } //End for loop
                return;     //Acting as a break, but should cut processing a tiny bit

            //Takes input and displays that recipe number
            default:
                try {
                    int entryNumber = Integer.parseInt(type);
                    System.out.printf("\n%s\n\n", dataBase.get(entryNumber).toString());
                    return;
                } catch(Exception e) {
                    //TODO: implement error handling
                } //End Try/Catch
        } //End Switcjh
    } //End displayEntries

    //Generates a long int for use as the UID of recipes
    //Doesn't need to be secure, just unique
    private static long generateUID() {
        
        Random rand = new Random(dataBase.size());
        Boolean isUnique = false;
        long uid = rand.nextLong();

        while(isUnique == false) {

            //Reset data for next check
            uid = rand.nextLong();
            isUnique = true;

            //Check is UID has been used
            for(Recipe recipe : dataBase) {
                if(recipe.getUID() == uid) {
                    isUnique = false;
                    break;      //Attempts to break from for loop
                } //End of If
            } //End of For
        } //End of While
        
        rand = null;    //Attempts to clear the rand to free extra memory
        return uid;

    } //End of generateUID

    public static void main(String[] args) {
        

        Scanner scanner = new Scanner(System.in);
        int userChoice;

        //TODO: handle this 
        dataBase = new ArrayList<>();

        try {
            //Main do-While loop of the program
            do {
                
                showMenu();
                userChoice = scanner.nextInt();
                scanner.reset();

                switch(userChoice) {

                    case 1: 
                        displayEntries("allAll");
                        break;

                    case 2:
                        displayEntries("allTitles");
                        break;

                    case 3: 
                        searchDataBase();
                        break;

                    case 4:
                        storeNewRecipe(null);
                        break;

                    case 5:
                        Add_Recipe_GUI.main(null);
                        storeNewRecipe(Recipe_Tools.getTempFile());
                        break;

                    case 6:
                        ArrayList<Recipe> tempList = null; //Recipe_Tools.readDatabaseFromFile();
                        if(tempList == null) {
                            System.out.println("Something went wrong in reading Database.");
                        }
                        else {
                            dataBase = tempList;
                            tempList = null;
                        }
                        System.gc();                    //Attempts to clear null the tempList from memory
                        break;

                    case 7:
                        Recipe_Tools.exportRecipesToXML(dataBase);
                        break;

                    case 0:
                        scanner.close();
                        System.exit(0);
                        
                    default:

                } //End Switch

            } while(userChoice != 0);
            
        } catch(Exception e) {

            //This is primarily attemtpting to catch Interrupted Exceptions
            //Interrupted Exceptions may not apply to this code block ¯\_(ツ)_/¯
            scanner.close();
            System.exit(1);

        } //End Try-Catch



    } //End Main
} //End Recipe_Repository
