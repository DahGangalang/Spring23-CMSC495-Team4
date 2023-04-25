/*  
 * Class for storing the totality of a recipe
 */

import java.util.Date;
import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {

    //Necessary fields
    private Metadata metadata;
    private Header header;
    private ArrayList<Ingredient> ingredients;
    private String instructions;
    //TODO: implement UID

    //Constructor for class
    public Recipe(ArrayList<String> input, long passedUID) {

        this.metadata = new Metadata();
        parseRecipe(input, passedUID);

    } //End Constructor

    private void parseRecipe(ArrayList<String> input, long passedUID) {
        
        StringBuilder instructionBuilder;
        ArrayList<String> passToConstructor;

        //TODO: can we assemble all the fields into a single iterable to cut down on code repetition?
        //probably not since each field is handled slightly differently
        
        //Iterate through lines of the recipe
        for(int i = 0; i < input.size(); i++) {
            
            //Throw out anything before beginning of header
            while(input.get(i).equals("***BEGIN HEADER***") == false) {
                //TODO: check for end of header line and throw error if found
                i++;
            } //End While
            i++;                                        //increment iterator to ignore begin header line

            //Import header information
            passToConstructor = new ArrayList<String>();
            while(input.get(i).equals("***END OF HEADER***") == false) {
                passToConstructor.add(input.get(i));
                i++;
            } //End While
            this.header = new Header(passToConstructor, passedUID);
            i++;                                        //increment iterator to ignore end header line
            
            //Throw out anything before beginning of ingredients
            while(input.get(i).equals("***BEGIN INGEDIENTS***") == false) {
                //TODO: check for end of ingredients line and throw error if found
                i++;
            } //End While
            i++;                                        //increment iterator to ignore begin ingredients line

            //Import ingredient information
            this.ingredients = new ArrayList<>();
            while(input.get(i).equals("***END OF INGEDIENTS***") == false) {
                this.ingredients.add(new Ingredient(input.get(i)));
                i++;
            } //End While
            i++;
            this.ingredients.trimToSize();                   //Trimming to size to maybe improve storage efficincy 

            //Throw out anything before beginning of Instructions
            while(input.get(i).equals("***BEGIN INSTRUCTIONS***") == false) {
                //TODO: check for end of ingredients line and throw error if found
                i++;
            } //End While
            i++;        //increment iterator to ignore begin ingredients line

            //Import instructions
            instructionBuilder = new StringBuilder();
            while(input.get(i).equals("***END OF INSTRUCTIONS***") == false) {
                instructionBuilder.append(input.get(i) + "\n");
                i++;
            } //End of while
            this.instructions = instructionBuilder.toString();

            //Display success message
            System.out.printf("Successfully parsed recipe %s.\n", header.getTitle());

        } //End For
    } //End ParseRecipe

    @Override
    public String toString() { 

        StringBuilder output = new StringBuilder();
        output.append(header.toString() + "\n");
        output.append("Ingredients:\n");
        for(Ingredient ingredient : ingredients) {
            output.append(ingredient.toString());
            output.append("\n");
        } //End For loop
        output.append("\n");
        output.append("Instructions:\n");
        output.append(instructions);
        output.append("\n\n");

        return output.toString();

    } //End toString

    public String getTitle() {
        return this.header.getTitle();
    } //End getTitle

    public Date getCreationDate() {
        return this.metadata.getCreationDate();
    } //End getCreationDate

    public Date getModifiedDate() {
        return this.metadata.getModifiedDate();
    } //End getCreationDate

    public Header getHeader() {
        return this.header;
    }

    public ArrayList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public long getUID() {
        return this.header.getUID();
    }

} //End Recipe