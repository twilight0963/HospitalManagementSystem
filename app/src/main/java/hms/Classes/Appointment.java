package hms.Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment{
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Date date;
    public int duration;
    public Doctor doc;
    
    public Appointment(String dateTime, int duration, Doctor doc){
        try {
            this.date = dateFormat.parse(dateTime);
            this.duration = duration;
            this.doc = doc;
        } catch (ParseException e) {
            //TODO: catch block
        }
    }
    public int modifyDate(String dateTime){
        try {
            this.date = dateFormat.parse(dateTime);
            return 0;
        } catch (ParseException e) {
            return -1;
        }
    }
}