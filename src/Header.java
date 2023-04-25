/*  
 * Class for storing the header information of a recipe
 */

 import java.util.ArrayList;


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

        //Grab info from the input
        for(String line : input) {
            parseHeaderLines(line);
        } //End For loop

        //Fill in remainder of filled fields
        if(title == null) {
            this.title = "Title: Unknown Recipe";
        } //End If

        if(author == null) {
            this.author = "Author: Anonymous";
        }

    } //End Constructor

    private void parseHeaderLines(String input) {

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
            this.author = input;
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

        StringBuilder output = new StringBuilder();
        output.append(title + "\n");
        output.append("Author:\t" + author + "\n");
        for(String tag : tags) {
            output.append(tag + "\n");
        }

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
