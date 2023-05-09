/*  
 * Driver class for the entirity of the project
 * Credit to Jonathon Supple for fantastic feedback on performance tweaks
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
        output.append("1 - Diplay Titles in Data Base\n");
        output.append("2 - Search Data Base\n");
        output.append("3 - Add Recipe from File to database\n");
        output.append("4 - Add new Recipe via GUI\n");
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
        } //End of Else

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

    //Generates a long int for use as the UID of recipes
    //Doesn't need to be secure, just unique
    private static long generateUID() {
        
        Random rand = new Random(dataBase.size());
        long uid = rand.nextLong();
        return uid;

    } //End of generateUID

    public static void main(String[] args) {
        
        //Necessary Variables
        RecipeGUI gui = new RecipeGUI(true);
        Scanner scanner = new Scanner(System.in);
        int userChoice;
        String searchString;
        dataBase = new ArrayList<>();

        //Prime the database
        Recipe_Tools.validateDatabase();

        try {

            //Main do-While loop of the program
            do {
                
                showMenu();
                userChoice = scanner.nextInt();
                

                switch(userChoice) {

                    //Display Entire Data Base
                    case 1: 
                        gui.recipeListToConsole();
                        break;

                    //Search Data Base
                    case 2: 
                        System.out.print("\nSearch for which recipe?\n>");
                        scanner.nextLine();     //Gets rid of the hanging newline
                        searchString = scanner.nextLine();
                        System.out.println("Attempting to search for " + searchString);
                        Recipe_Tools.searchRecipes(true, searchString);
                        break;

                    //Add Recipe from File to database
                    case 3:
                        long uid = generateUID();
                        Recipe tempRecipe = new Recipe(readLinesFromFile(null), uid);
                        gui.saveRecipe(tempRecipe);
                        break;

                    //Add new Recipe via GUI
                    case 4:

                        //Thanks ChatGPT for helping me when google and stack overflow couldn't

                        //Spin up a fresh GUI
                        gui.setVisible(true);

                        //Wait for GUI to close
                        try {
                            System.out.println("\nWaiting on GUI\n");
                            while(gui.isVisible()) {
                                Thread.sleep(1000);    
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
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
