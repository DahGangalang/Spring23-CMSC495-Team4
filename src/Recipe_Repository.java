/*  
 * Driver class for the entirity of the project
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Recipe_Repository {
    
    //TODO: Build the database
    private static ArrayList<Recipe> dataBase;

    private static void showMenu() {

        StringBuilder output = new StringBuilder();
        output.append("\n\n");
        output.append("1 - Display Entire Data Base\n");
        output.append("2 - Search Data Base\n");
        output.append("3 - Add new Recipe to database\n");
        output.append("0 - Exit\n");
        output.append(">");

        System.out.printf("%s", output.toString());
    } //End ShowMenu

    //readLinesFromFile was greatly helped by ChatGPT
    private static ArrayList<String> readLinesFromFile() throws IOException {
        
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

            //Read the file line by line and store to arraylist of strings
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader  = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
            String line;
            
            //Until we reach the last line of the file...
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            return lines;
        } //End if

        else {
            return null;
        } //End else

    } //End readLinesFromFile

    private static void searchDataBase() {
        //TODO
    } //End searchDataBase

    private static void storeNewRecipe() {

        try {
            ArrayList<String> lines = readLinesFromFile();
            if(lines != null) {
                dataBase.add(new Recipe(lines));
            } //End if
            else {
                System.out.println("Error when reading recipe.");
            } //End else

        } catch(Exception e) {

            System.out.println("Caught an exception in storeNewRecipe function");
            e.printStackTrace();

        } //End Try/Catch

    } //End storeNewRecipe

    private static void displayEntries(int input) {

        if(input == -1) {
            for(Recipe recipe : dataBase) {
                System.out.printf("\n%s\n\n", recipe.toString());
            } //End For
        } //End If

        else {
            System.out.printf("\n%s\n\n", dataBase.get(input).toString());
        }

    } //End displayEntries

    public static void main(String[] args) {
        
        //Dummy function calls to reduce errors during testing
        //searchDataBase();
        //storeNewRecipe();

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
                        displayEntries(-1);
                        break;

                    case 2: 
                        searchDataBase();
                        break;

                    case 3:
                        storeNewRecipe();
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
