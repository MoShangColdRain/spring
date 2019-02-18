package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author WangJn
 * @description 日期 时间工具类
 * @date Created on 2017/8/29.
 */
public class DateTimeUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);
    
	public static final int FIELD_YEAR = Calendar.YEAR;
	public static final int FIELD_MONTH = Calendar.MONTH;
	public static final int FIELD_DAY = Calendar.DAY_OF_MONTH;
	public static final int FIELD_HOUR = Calendar.HOUR_OF_DAY;
	public static final int FIELD_MINUTE = Calendar.MINUTE;
	public static final int FIELD_SECOND = Calendar.SECOND;
	
	/**
	 * 计算时间
	 * @param date	基准时间
	 * @param field	时间轴
	 * @param amount 加减值
	 * @return
	 */
	public static Date calculateTime(Date date, int field, int amount) {
		if(date==null){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}
	
	/**
     * 获取距离当天 指定偏移天数的 秒级时间戳
     * @param offsetDay
     * @return
     */
    public static long getSecondTimestampByOffset(int offsetDay){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0); // 时间 设置为当天零点
        cal.add(Calendar.DAY_OF_MONTH, offsetDay); // 设置偏移天数 负数表示向前偏移天数
        return cal.getTimeInMillis()/1000;
    }

    /**
     * 获取距离当天 指定偏移天数的 日期格式化字符串
     * @param offsetDay
     * @param pattern
     * @return
     */
    public static String getDateTimeStrByOffset(int offsetDay, String pattern){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, offsetDay);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    public static Date parse(String source) throws ParseException{
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return myFmt.parse(source);
    }
   /**
    * 获取现在时间之前ihour小时的时间,分秒归零
    * @param ihour
    * @return
    */
    public static Date getBeforeHourTime(int ihour){  
	    String returnstr = "";  
	    Calendar calendar = Calendar.getInstance();  
	    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);  
	    calendar.set(Calendar.MINUTE,0);
	    calendar.set(Calendar.SECOND,0);
	    calendar.set(Calendar.MILLISECOND,0);
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    returnstr = df.format(calendar.getTime());  
	    Date currentDate = null;
		 
		 try {
			  currentDate = df.parse(returnstr);
		} catch (ParseException e) {
			logger.error("时间转换失败了@:"+e.getMessage());
		} 
	  
	  
	    return currentDate;  
	}
    
    
    
	/**
	 * 获取当前日期时间，时分秒置零(注:传入0时，为今日零时零分零秒，传入-24为昨日零时零分零秒)
	 * @return
	 */
	
	public static Date getCurrentTime(int num){  
	    String returnstr = "";  
	    Calendar calendar = Calendar.getInstance();  
	    calendar.set(Calendar.HOUR_OF_DAY,num);  
	    calendar.set(Calendar.MINUTE,0);
	    calendar.set(Calendar.SECOND,0);
	    calendar.set(Calendar.MILLISECOND,0);
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    returnstr = df.format(calendar.getTime());  
	    Date currentDate = null;
		 
		 try {
			  currentDate = df.parse(returnstr);
		} catch (ParseException e) {
			logger.error("时间转换失败了@:"+e.getMessage());
		} 
	  
	  
	    return currentDate;  
	}
	
	
	/**
	 * 获取本月第一天,时分秒归零处理
	 * @return
	 */
	public static Date getThisMothFirthday(){  
	    String returnstr = "";  
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Calendar c = Calendar.getInstance();    
	        c.add(Calendar.MONTH, 0);
	        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	        c.set(Calendar.HOUR_OF_DAY,0);  
		    c.set(Calendar.MINUTE,0);
		    c.set(Calendar.SECOND,0);
		    c.set(Calendar.MILLISECOND,0);
	  
		    returnstr = df.format(c.getTime());
		    Date currentDate = null;
			 
			 try {
				  currentDate = df.parse(returnstr);
			} catch (ParseException e) {
				logger.error("时间转换失败了@:"+e.getMessage());
			} 
	    return currentDate;  
	}
	
	public static Date strToDate(String Formate, String date) {
		try {
			SimpleDateFormat f = new SimpleDateFormat(Formate);
			return f.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	
		
		
		public static List<Date> getStrTimeToData(String startTime,String endTime){
			Date startDate = null;
			Date endDate = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
				try {
					startDate = df.parse(startTime);
					endDate = df.parse(endTime);
				} catch (ParseException e) {
					logger.error("时间转换失败了@:"+e.getMessage());
				} 
				List<Date> dateList = new ArrayList<>();
				dateList.add(startDate);
				dateList.add(endDate);
				return dateList;
		}
    public static void main(String[] args) {
//    	
//    		String returnstr = "";  
//	    Calendar calendar = Calendar.getInstance();  
//    	
//    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//    		try {
//				Date date = df.parse("2018-04-20 12:01:00");
//				calendar.setTime(date);
//			} catch (ParseException e1) {
//				e1.printStackTrace();
//			}
//    	
//    		
//    		
//    	    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 2);  
//    	    calendar.set(Calendar.MINUTE,0);
//    	    calendar.set(Calendar.SECOND,0);
//    	    calendar.set(Calendar.MILLISECOND,0);
//    	    returnstr = df.format(calendar.getTime());  
//    	    Date currentDate = null;
//    		 
//    		 try {
//    			  currentDate = df.parse(returnstr);
//    		} catch (ParseException e) {
//    			logger.error("时间转换失败了"+e);
//    		} 
    	DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    	try {
			Date myDate1 = dateFormat1.parse("2018-06-23 12:22:22");
			Date myDate2 = dateFormat1.parse("2018-06-25 12:22:22");
			System.out.println(differentDaysByMillisecond(myDate1,myDate2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	  
    	DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println(dateFormat2.format(getCurrentTime(0)));
	  //  System.out.println(currentDate);
    }
    
    /**
     *计算两个日期之间的天数
     * */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
    	if((date2.getTime() < date1.getTime())){
    		return 0;
    	}
    	if(date1.getTime()>getCurrentTime(0).getTime()) {
    		return 0;
    	}
    	if(date2.getTime()>getCurrentTime(0).getTime()) {
    		date2 = getCurrentTime(0);
    	}
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days+1;
    }
    
    /**
     * 获取当前小时
     * @return
     */
    public static int getCurrentHour() {
    	LocalDateTime localDateTime = LocalDateTime.now();
    	return localDateTime.getHour();			
    }
}
