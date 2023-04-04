/*  
 * Class for storing the totality of a recipe
 */

import java.util.ArrayList;

public class Recipe {

    //Necessary fields
    Metadata metadata;
    Header header;
    ArrayList<Ingredient> ingredients;
    String instructions;

    //Constructor for class
    public Recipe(ArrayList<String> input) {

        this.metadata = new metadata();
        parseRecipe(input);
    } //End Constructor

    private void parseRecipe(ArrayList<String> input) {
        
        StringBuilder stringBuilder;
        
        for(String line : input) {
            while(line != ***BEGIN HEADER***) {
                continue
            } //End While
        } //End For
    } //End ParseRecipe

} //End Recipe