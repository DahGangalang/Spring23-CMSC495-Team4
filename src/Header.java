/*  
 * Class for storing the header information of a recipe
 */

 import java.util.ArrayList;
import java.util.StringJoiner;

public class Header {
   
    private long UID;
    private String title;
    private String author;
    private ArrayList<String> tags;

    //Constructor for class
    public Header(ArrayList<String> input, long passedUID) {

        //Initializing tags
        this.tags = new ArrayList<>(1);
        this.UID = passedUID;

        //Grab info from the input - if input is good
        if(input.isEmpty() == false && input != null){
            for(String line : input) {
                parseHeaderLines(line);
            } //End For loop
        } //End of If

        //Fill in remainder of unfilled fields
        if(title == null) {
            this.title = "Unknown Recipe";
        } //End If

        if(author == null) {
            this.author = "Anonymous";
        }
    } //End Constructor

    private void parseHeaderLines(String input) {

        //TODO: implement this as a switch statement

        //read in title information
        if(input.toLowerCase().contains("title")) {
            //Split around a colon and store ending back to input
            input = input.split(":", 2)[1];
            input = Recipe_Tools.stripSurroundingWhiteSpace(input);
            this.title = input;
        } //End If

        //Read in author information
        if(input.toLowerCase().contains("author")) {
            //Split around a colon and store ending back to input
            input = input.split(":", 2)[1];
            input = Recipe_Tools.stripSurroundingWhiteSpace(input);
            if(this.author == null) {
                this.author = input;
                return;
            } //End of If
            else {
                this.author = this.author + ", " + input;
                return;
            } //End of Else
        } //End If

        //Read in tag information
        if(input.toLowerCase().contains("tag")) {

            //Split around a colon and store ending back to input
            input = input.split(":", 2)[1];
            
            if(input.contains(",")) {
                for(String s : input.split(",")) {
                    this.tags.add(Recipe_Tools.stripSurroundingWhiteSpace(s));
                } //End for
            } //End If

            else {
                input = Recipe_Tools.stripSurroundingWhiteSpace(input);
                this.tags.add(input);
            } //End Else
        } //End If
    } //End parseHeaderLines

    public String toString() {

        StringJoiner output = new StringJoiner("\n");
        output.add("Title: \t" + this.title);
        output.add("Author:\t" + author);
        for(String tag : tags) {
            output.add(tag);
        } //End of For

        //And return the result
        return output.toString();

    } //End toString

    //Simple toString method for testing purposes
    public void toStringConsole() {
        
        System.out.println(this.toString());
    } //End toStringConsole

    //Getters 
    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public long getUID() {
        return this.UID;
    }



} //End Header
