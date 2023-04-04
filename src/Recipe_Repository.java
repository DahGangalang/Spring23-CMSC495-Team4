/*  
 * Driver class for the entirity of the project
 */

import java.io.BufferedReader;
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
        output.append("1 - Display Entire Data");
        output.append("2 - Search Data Base");
        output.append("3 - Add new Recipe to database");
        output.append("0 - Exit");
    } //End ShowMenu

    private static ArrayList<String> readLinesFromFile() throws IOException {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int result = fileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader =new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
            String line;
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
            dataBase.add(new Recipe(lines));

        } catch(Exception e) {

        } //End Catch
    } //End storeNewRecipe

    public static void main(String[] args) {
        
        //Dummy function calls to reduce errors during testing
        searchDataBase();
        storeNewRecipe();

        Scanner scanner = new Scanner(System.in);
        int userChoice;

        try {
            //Main do-While loop of the program
            do {
                
                showMenu();
                userChoice = scanner.nextInt();
                scanner.reset();

                switch(userChoice) {

                    case 1: 
                        dataBase.toString();
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
