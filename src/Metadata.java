/*  
 * Class for storing metadata related to last changes of recipes and ingredients
 */

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

public class Metadata {

    Date addTime;
    Date modifiedTime;

    public Metadata() {

        Calendar c = new GregorianCalendar();
        this.addTime = c.getTime();
        this.modifiedTime = addTime;

    }
    
}
