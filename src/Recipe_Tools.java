/*
 * Toolbox for some minor functions used throughout the recipe repoistory 
 */

import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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
