package imageopen.rishabh.andimage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by RISHABH on 2/25/2017.
 */

public class FileName {
    public String getName(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        return "rishabhPixabay_"+df.format(c.getTime());

    }
}
