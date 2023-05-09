/*  
 * Class for storing metadata related to last changes of recipes and ingredients
 */

import java.util.GregorianCalendar;
import java.util.Date;

public class Metadata {

    private Date creationDate;
    private Date modifiedTime;
    private String user;            //To track the last user to make a modification

    public Metadata() {

        this.creationDate = (new GregorianCalendar()).getTime();
        this.modifiedTime = creationDate;
        try {
            this.user = System.getProperty("user.name");
        } catch(Exception e) {
            this.user = "Unable to Retrieve";
            e.printStackTrace();
        }

    } //End Constructor

    public boolean updateChangeTime() {
        try {
            this.modifiedTime = (new GregorianCalendar()).getTime();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        } //End of Try/Catch
    } //End updateChangeTime

    //Batch of Getters
    public Date getCreationDate() {
        return this.creationDate;
    }

    public Date getModifiedDate() {
        return this.modifiedTime;
    }

    public String getUser() {
        return this.user;
    }
    
} //End Metadata
