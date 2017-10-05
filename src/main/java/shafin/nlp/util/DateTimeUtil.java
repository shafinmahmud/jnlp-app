/*
 */
package shafin.nlp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 *
 * @author SHAFIN
 */
public class DateTimeUtil {

    private static final String LOCAL_TIME_ZONE = "GMT+6:00";

    public static void main(String[] args) {
    	System.out.println(getSytemTimeStamp());
    	System.out.println(getLocalTimeStamp());
		System.out.println(getLocalTimeStamp12H());
		
		System.out.println(convertTimeStampToEpoch(getSytemTimeStamp()));
		System.out.println(convertTimeStampToEpoch(getLocalTimeStamp()));
	}
    
    public static String getSytemTimeStamp() {
    	//2016-01-12 21:55:09
        Date currentTime = new Date();
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return timeStampFormat.format(currentTime);
    }

    public static String getLocalTimeStamp() {
    	//2016-01-12 21:55:09
        Date currentTime = new Date();
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        timeStampFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIME_ZONE));
        return timeStampFormat.format(currentTime);
    }
    
    public static String getSytemTimeStamp12H() {
    	//2016-01-12 21:55:09
        Date currentTime = new Date();
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return timeStampFormat.format(currentTime);
    }
    
    public static String getLocalTimeStamp12H() {
    	//2016-01-12 21:55:09 AM
        Date currentTime = new Date();
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        timeStampFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIME_ZONE));
        return timeStampFormat.format(currentTime);
    }

    public static String getSytemTimeStampAgo(int hoursBack){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -hoursBack);
    	Date hoursBackDate = cal.getTime();
    	
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return timeStampFormat.format(hoursBackDate);
    }
    
    public static String getLocalTimeStampAgo(int hoursBack){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -hoursBack);
    	Date hoursBackDate = cal.getTime();
    	
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        timeStampFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIME_ZONE));
        return timeStampFormat.format(hoursBackDate);
    }
    
    public static String getSytemTimeStampAgo12H(int hoursBack){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -hoursBack);
    	Date hoursBackDate = cal.getTime();
    	
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        return timeStampFormat.format(hoursBackDate);
    }
    
    public static String getLocalTimeStampAgo12H(int hoursBack){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -hoursBack);
    	Date hoursBackDate = cal.getTime();
    	
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        timeStampFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIME_ZONE));
        return timeStampFormat.format(hoursBackDate);
    }
    
    public static String getFormatedTimeStamp(Date date) {
    	//2016-01-12 21:55:09
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");        
        return timeStampFormat.format(date);
    }
    
    public static String getFormatedTimeStamp12H(Date date) {
    	//2016-01-12 11:55:09 PM
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        return timeStampFormat.format(date);
    }
    
    public static String getFormatedTimeStamp(long miliseconds) {
    	//2016-01-12 21:55:09
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");        
        return timeStampFormat.format(new Date(miliseconds));
    }
    
    public static String getFormatedTimeStamp12H(long miliseconds) {
    	//2016-01-12 11:55:09 PM
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");        
        return timeStampFormat.format(new Date(miliseconds));
    }
    
    public static String getFormatedLocalTimeStamp(Date date) {
    	//2016-01-12 21:55:09
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        timeStampFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIME_ZONE));
        return timeStampFormat.format(date);
    }
    
    public static String getFormatedLocalTimeStamp12H(Date date) {
    	//2016-01-12 11:55:09 PM
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        timeStampFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIME_ZONE));
        return timeStampFormat.format(date);
    }
    
    public static String getFormatedLocalTimeStamp(long miliseconds) {
    	//2016-01-12 21:55:09
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        timeStampFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIME_ZONE));
        return timeStampFormat.format(new Date(miliseconds));
    }
    
    public static String getFormatedLocalTimeStamp12H(long miliseconds) {
    	//2016-01-12 11:55:09 PM
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); 
        timeStampFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIME_ZONE));
        return timeStampFormat.format(new Date(miliseconds));
    }
    
    public static String convertTimeStampToEpoch(String timestamp){
    	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    	DateTime dt = formatter.parseDateTime(timestamp);
    	return String.valueOf(dt.getMillis());
    }
    
    public static String getParsedFacebookTimeStamped(String timeStamp) {
        try {
            SimpleDateFormat facebookFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return targetFormat.format(facebookFormat.parse(timeStamp).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(DateTimeUtil.class.getName()).log(Level.SEVERE, null, ex);
            return "0000-00-00 00:00:00";
        }
    }
    
    
}
