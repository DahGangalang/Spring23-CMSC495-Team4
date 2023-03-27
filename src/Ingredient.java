/*  
 * Class for storing the totality of a recipe
 */

public class Ingredient {
    
    //Fields for data storage
    String ingredientName;
    String unitOfMeasure;
    String quantity;

    //Constructors for this class
    //Assumes qualtity will be presented in decimal format
    public Ingredient (String sIN, String sUOM, float f) {

        this.ingredientName = sIN;
        this.unitOfMeasure = sUOM;
        this.quantity = floatToFractionConversion(f);

    }

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
}
