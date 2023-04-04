/*  
 * Class for storing metadata related to last changes of recipes and ingredients
 */

import java.util.GregorianCalendar;
import java.util.Date;

public class Metadata {

    Date addTime;
    Date modifiedTime;
    String user;            //To track the last user to make a modification

    public Metadata() {

        this.addTime = (new GregorianCalendar()).getTime();
        this.modifiedTime = addTime;
        this.user = "NOT IMPLEMENTED";

    } //End Coinstructor

    public void updateChangeTime() {
        this.modifiedTime = (new GregorianCalendar()).getTime();
    } //End updateChangeTime
    
} //End Metadata
