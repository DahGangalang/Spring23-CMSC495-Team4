

/*  
 * Class for storing the totality of a recipe
 */

import java.util.StringJoiner;
import java.util.regex.PatternSyntaxException;

public class Ingredient {
    
    //Fields for data storage
    Metadata metadata;
    String ingredientName;
    String measurement;
    String quantity;
    Double quantityAsDouble;

    //Constructor for class
    public Ingredient(String input) {

        //New MetaData - largely unused
        this.metadata = new Metadata();
        try {

            String[] splitInput = input.split(" ", 3);

            try {

                //Splits the String into fields and ingests as appropriate
                this.quantity = Recipe_Tools.stripSurroundingWhiteSpace(splitInput[0]);
                this.quantityAsDouble = Double.valueOf(quantity);
                if(Recipe_Tools.hasMeasurement(splitInput[1])) {
                    this.measurement = Recipe_Tools.stripSurroundingWhiteSpace(splitInput[1]);
                    this.ingredientName = Recipe_Tools.stripSurroundingWhiteSpace(splitInput[2]);
                } //End If
                else {
                    this.measurement = "";
                    this.ingredientName = Recipe_Tools.stripSurroundingWhiteSpace(splitInput[1] + " " + splitInput[2]);
                } //End Else

            } catch(NumberFormatException nfe) {
                
                /*
                 * Should only pop this exception if theres an issue parsing the double
                 * this probably indicates there was not a quantity used
                 * In this case, we'll assume they mean 1 on the thing
                 * 
                 * e.g. recipe says "yellow onion" when it should be "1 yellow onion"
                 */
                this.quantity = "1";
                this.quantityAsDouble = 1.0d;
                if(Recipe_Tools.hasMeasurement(splitInput[0])) {
                    this.measurement = Recipe_Tools.stripSurroundingWhiteSpace(splitInput[0]);
                    this.ingredientName = Recipe_Tools.stripSurroundingWhiteSpace(splitInput[1] + " " + splitInput[2]);   //Grab everything after the measurement
                } //End If
                else {
                    this.measurement = "";
                    this.ingredientName = Recipe_Tools.stripSurroundingWhiteSpace(input);   //Can get the whole input if no measurement
                } //End Else

            } //End of Try/Catch

        } catch(PatternSyntaxException pse) {

            /*
             * Should only pop this exception if theres an issue spliting the string
             * primary case for this should be if there's only one thing to be added
             * 
             * e.g recipe says "onion" it really means "1 onion"
             */
            this.quantity = "1";
            this.measurement = "";
            this.quantityAsDouble = Double.valueOf(quantity);
            this.ingredientName = Recipe_Tools.stripSurroundingWhiteSpace(input);

        } //End of Try/Catch
    } //End of Ingredient Constructor

    public String toString() {

        StringBuilder output = new StringBuilder();
        output.append(quantity);
        output.append(" ");
        if(measurement != "") {
            output.append(measurement + " ");
        }
        output.append(ingredientName);

        return output.toString();
    } //End of toString

    public String toGUIString() {

        StringJoiner output = new StringJoiner(", ");
        output.add(quantity);
        output.add(measurement);
        output.add(ingredientName);

        return output.toString();
    }

    //Getters
    public String getIngredientName() {
        return this.ingredientName;
    } //End of getIngredientName

    public String getQuantity() {
        return this.quantity;
    } //End of getQuantity

    public String getMeasurement() {
        return this.measurement;
    } //End of getMeasurement

} //End of Ingredient
