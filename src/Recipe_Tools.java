/*
 * Toolbox for some minor functions used throughout the recipe repoistory 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Recipe_Tools {

    private static String DATABASEFILENAME = "jdbc:sqlite:recipes.db";
    private static String TEMPFILENAME = "/Recipe-Files/tempRecipeFile.txt";
    
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

    public static File getTempFile() {
        
        String currentDirectory = System.getProperty("user.dir");
        return new File(currentDirectory + TEMPFILENAME);
    } //End of getTempFile

    public static void writeGuiInputToFile(ArrayList<String> input) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getTempFile(), false));
            for(String s : input) {
                writer.write(s);
                writer.newLine();
            } //End of For
            writer.close();
        } catch(Exception e) {
            System.out.println("Something went wrong adding recipe from GUI!\n");
            e.printStackTrace();
        } //End of Try/Catch
    } //End of writeGuiInputToFile

    public static boolean hasMeasurement(String word) {
        
        //converts input to lowercase and removes any punctuation of easier switching
        switch(word.toLowerCase().replaceAll("\\p{Punct}","")) {
            case "lbs":
            case "pound":
            case "pounds":
            case "kilo":
            case "kilogram":
            case "kilograms":
            case "kg":
            case "g":
            case "gram":
            case "grams":
            case "miligram":
            case "miligrams":
            case "mililitre":
            case "mililitres":
            case "mililiter":
            case "mililiters":
            case "liter":
            case "litre":
            case "liters":
            case "litres":
            case "l":
            case "cup":
            case "cups":
            case "c":
            case "pint":
            case "pints":
            case "quart":
            case "quarts":
            case "qt":
            case "gallon":
            case "gallons":
            case "gal":
            case "tsp":
            case "teaspoon":
            case "teaspoons":
            case "tbsp":
            case "tablspoon":
            case "tablspoons":
            case "ounce":
            case "oz":
            case "ounces":
            case "bottle":
            case "bottles":
            case "can":
            case "cans":
            case "stick":
            case "sticks":
            case "dash":
            case "dashes":
            case "loaf":
            case "loaves":
            case "pinch":
            case "pinches":
            case "dram":
            case "drams":
                return true;

        } //End of Case

        return false;

    } //End of hasMeasurement

    public static void exportRecipesToXML(ArrayList<Recipe> recipes)
            throws XMLStreamException, IOException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = xmlOutputFactory.createXMLStreamWriter(new FileWriter(new File(DATABASEFILENAME)));

        // Start the XML document
        xmlWriter.writeStartDocument();
        xmlWriter.writeStartElement("Recipes");

        // Write each recipe as an XML element
        for (Recipe recipe : recipes) {

            // Start Individual recipe
            xmlWriter.writeStartElement("Recipe");

            // Write Header information
            xmlWriter.writeStartElement("Header");
            xmlWriter.writeStartElement("Title");
            xmlWriter.writeCharacters(recipe.getHeader().getTitle());
            xmlWriter.writeEndElement();

            xmlWriter.writeStartElement("Author");
            xmlWriter.writeCharacters(recipe.getHeader().getAuthor());
            xmlWriter.writeEndElement();

            xmlWriter.writeStartElement("Tags");
            for(String tag : recipe.getHeader().getTags()) {
                xmlWriter.writeStartElement("Tag");
                xmlWriter.writeCharacters(tag);
                xmlWriter.writeEndElement();
            } //End of For

            xmlWriter.writeEndElement();    //End of Header


            // Write Ingredient Information
            xmlWriter.writeStartElement("Ingredients");
            for (Ingredient ingredient : recipe.getIngredients()) {
                xmlWriter.writeStartElement("ingredient");

                xmlWriter.writeStartElement("Ingredient Name");
                xmlWriter.writeCharacters(ingredient.getIngredientName());
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("Quantity");
                xmlWriter.writeCharacters(ingredient.getQuantity());
                xmlWriter.writeEndElement();

                xmlWriter.writeEndElement();
            } //End of For

            xmlWriter.writeEndElement();    //End of Ingredient

            // Write Instruction Information
            xmlWriter.writeStartElement("Instructions");
            xmlWriter.writeCharacters(recipe.getInstructions());
            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        // End the XML document
        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();

        // Close the writer
        xmlWriter.close();
        
    } //End of exportRecipesToXML

    //Tries to connect to database or start a new if non-existant
    public static void validateDatabase() {

        try {

            //Some basic variables for the database
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASEFILENAME);
            String tableName = "recipes";

            //Check if Table exists
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rule = meta.getTables(null, null, tableName, null);

            //If the Table doesn't exist, create it
            if(rule.next() == false) {
                //Recipes table
                Statement createRecipesStmt = conn.createStatement();
                String createRecipesTableSql = "CREATE TABLE recipes (id INTEGER PRIMARY KEY, name TEXT, ingredients TEXT, instructions TEXT, created_at TEXT)";
                createRecipesStmt.executeUpdate(createRecipesTableSql);

                //If Recipes doesn't exist, seems fair that we'd need to make ingredients table too
                //Ingredients table
                Statement createIngredientsStmt = conn.createStatement();
                String createIngredientsTableSql = "CREATE TABLE ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER, qty TEXT, measurement TEXT, ingredient TEXT, FOREIGN KEY(recipe_id) REFERENCES recipes(id))";
                createIngredientsStmt.executeUpdate(createIngredientsTableSql);
            }

            //Be sure to close connections
            conn.close();

        } catch(Exception e) {
            System.out.println("Error in Recipe_Tools.validateDatabase():\n");
            e.printStackTrace();
        } //End Try/Catch
    } //End of validateDatabase

    /*
     *  Deprecated Code
    

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

    * End of Deprecated Code
    */

} //End of Recipe_Tools
