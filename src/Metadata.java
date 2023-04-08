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
        this.user = "NOT IMPLEMENTED";

    } //End Coinstructor

    public void updateChangeTime() {
        this.modifiedTime = (new GregorianCalendar()).getTime();
    } //End updateChangeTime

    public Date getCreationDate() {
        return this.creationDate;
    }

    public Date getModifiedDate() {
        return this.modifiedTime;
    }
    
} //End Metadata
