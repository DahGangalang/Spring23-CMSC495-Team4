

/*  
 * Class for storing the totality of a recipe
 */

 import java.util.regex.PatternSyntaxException;

public class Ingredient {
    
    //Fields for data storage
    Metadata metadata;
    String ingredientName;
    String quantity;
    Double quantityAsDouble;

    //Constructor for class
    public Ingredient(String input) {

        this.metadata = new Metadata();

        try {

            String[] splitInput = input.split(" ", 2);

            try {

                this.quantity = Recipe_Tools.stripSurroundingWhiteSpace(splitInput[0]);
                this.quantityAsDouble = Double.valueOf(quantity);
                this.ingredientName = Recipe_Tools.stripSurroundingWhiteSpace(splitInput[1]);

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
                this.ingredientName = Recipe_Tools.stripSurroundingWhiteSpace(input);

            }//End of Try/Catch


        } catch(PatternSyntaxException pse) {

            /*
             * Should only pop this exception if theres an issue spliting the string
             * primary case for this should be if there's only one thing to be added
             * 
             * e.g recipe says "onion" it really means "1 onion"
             */
            this.quantity = "1";
            this.quantityAsDouble = Double.valueOf(quantity);
            this.ingredientName = Recipe_Tools.stripSurroundingWhiteSpace(input);

        } //End of Try/Catch

        //TODO: code to trim excess white space from ingredient name
         
    } //End of Ingredient Constructor

    public String toString() {

        StringBuilder output = new StringBuilder();
        output.append(quantity);
        output.append("x ");
        output.append(ingredientName);

        return output.toString();
    } //End of toString


    /*
     * OLD CODE

    //Constructors for this class
    //Assumes qualtity will be presented in decimal format
    public Ingredient (String sIN, String sUOM, float f) {

        this.ingredientName = sIN;
        this.unitOfMeasure = sUOM;
        this.quantity = floatToFractionConversion(f);

    } //End Constructor

    //Constructors for this class
    //Assumes qualtity will be presented in decimal format
    public Ingredient (String sIN, String sUOM, String sQ) {

        this.ingredientName = sIN;
        this.unitOfMeasure = sUOM;
        this.quantity = sQ;

    }

    //Constructors for this class
    //Assumes quantity is a count of the items to add
    public Ingredient (String sIN, String sQ) {

        this.ingredientName = sIN;
        StringBuilder sb = new StringBuilder(sIN + "s");
        this.unitOfMeasure = sb.toString();
        this.quantity = sb.toString();

    }

    //Function that will convert a float input into a cleaner fraction
    private String floatToFractionConversion(double d) {
        //TODO: This
        return "NOT IMPLEMENTED";
    }
    */
}
