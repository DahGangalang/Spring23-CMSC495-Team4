/*  
 * Class for storing the header information of a recipe
 */


public class Header {
   
    private String name;

    //Constructor for class
    public Header(String[] input) {

        this.name = input[0];

    } //End Constructor

    //
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(name);

        return output.toString();
    } //End toString

    //Simple toString method for testing purposes
    public void toStringConsole() {
        StringBuilder output = new StringBuilder();
        output.append(name);

        System.out.println(output.toString());
    } //End toStringConsole

} //End Header
