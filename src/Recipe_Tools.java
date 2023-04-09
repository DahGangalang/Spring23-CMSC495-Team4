/*
 * Toolbox for some minor functions used throughout the recipe repoistory 
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Recipe_Tools {

    private static String DATABASEFILENAME = "LocalRecipeDataBase.wad";
    
    //For stripping out whitespace surrounding a string
    public static String stripSurroundingWhiteSpace(String input) {

        if(input == null) {
            return "";
        } //End If

        int start = 0;
        int end = input.length() - 1;
        while(start < end && Character.isWhitespace(input.charAt(start))) {
            start++;
        } //Enf While Loop

        while(end > start && Character.isWhitespace(input.charAt(end))) {
            end--;
        } //End While Loop

        return input.substring(start, end + 1);
    } //End of stripSurroundingWhiteSpace

    public static void writeDataBaseToFile(ArrayList<Recipe> recipeDataBase) {

        //Build the absolute file name
        String absFileName = System.getProperty("user.dir") + "/" + DATABASEFILENAME;

        try (FileOutputStream fos = new FileOutputStream(absFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            
            oos.writeObject(recipeDataBase);
            System.out.printf("\nWrote %s Successfully.\n", DATABASEFILENAME);
        
        } catch(Exception e) {
            e.printStackTrace();
        } //End Try/Catch
    } //End writeDataBaseToFile

    public static ArrayList<Recipe> readDatabaseFromFile() {

        //Built a receptacle arraylist and the absolute file name
        ArrayList<Recipe> output = new ArrayList<>();
        String absFileName = System.getProperty("user.dir") + "/" + DATABASEFILENAME;

        try (FileInputStream fis = new FileInputStream(absFileName);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            output = (ArrayList<Recipe>)ois.readObject();
            System.out.printf("\nRead %s Successfully.\n", DATABASEFILENAME);
            return output;

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        } //End Try/Catch
    } //End readDatabaseFromFile
}
