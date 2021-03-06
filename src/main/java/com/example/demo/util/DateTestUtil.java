package com.example.demo.util;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;


public class DateTestUtil {

	private static Logger logger=LoggerFactory.getLogger(DateUtil.class);
	/**
	 * 一天的时间的毫秒数
	 */
	public  static final long ONE_DAY_TIME = 86400000L;

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public static final String DEFAULT_YEAR_MONTH_PATTERN = "yyyy-MM";
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String DEFAULT_DATE_MIN_TIME_PATTERN = "MM-dd HH:mm";

    private static final ZoneId SYSTEM_DEFAULT_ZONEID = ZoneId.systemDefault();

	public static final String DEFAULT_HOURS_MIN = "HH:mm";

	/** 锁对象 */
	private static final Object LOCK_OBJ = new Object();

	/** 存放不同的日期模板格式的sdf的Map */
	private static final Map<String, ThreadLocal<SimpleDateFormat>> SIMPLE_DATE_FORMAT_LOCAL_MAP = new HashMap<>();


	/**
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 *
	 * @param pattern
	 * @return
	 */
	private static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = SIMPLE_DATE_FORMAT_LOCAL_MAP.get(pattern);
		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (tl == null) {
			synchronized (LOCK_OBJ) {
				tl = SIMPLE_DATE_FORMAT_LOCAL_MAP.get(pattern);
				if (tl == null) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					//                    System.out.println("put new sdf of pattern " + pattern + " to map");

					// 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
					tl = new ThreadLocal<SimpleDateFormat>() {
						@Override
						protected SimpleDateFormat initialValue() {
							//                            System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
							return new SimpleDateFormat(pattern);
						}
					};
					SIMPLE_DATE_FORMAT_LOCAL_MAP.put(pattern, tl);
				}
			}
		}

		return tl.get();
	}



	private static DateFormat formatter(String pattern) {
		DateFormat  dateFormat =  getSdf(pattern);
		return dateFormat;
	}

	public static Date parseDate(String text, String pattern) {
		Date date = null;
		try {
			date = formatter(pattern).parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}


	public static String dateFmt(String pattern, Date date) {
		String dateStr = "";
		if (null == date) {
			return dateStr;
		}
		dateStr = formatter(pattern).format(date);
		return  dateStr;
	}

	public static String dateFmt(Long timeStamp) {
        if (timeStamp == null || timeStamp.equals(0L)) {
			return null;
		}
		return dateFmt(DEFAULT_YEAR_MONTH_PATTERN,timeStamp * 1000);
	}

	public static String dateFmtSec(Long timeStamp) {
		if (timeStamp == null || timeStamp.equals(0L)) {
			return null;
		}
		return dateFmt(DEFAULT_YEAR_MONTH_PATTERN,timeStamp);
	}

	public static String dateFmt(String pattern, long timeStamp) {
		String dateStr = "";
		dateStr = formatter(pattern).format(timeStamp);
		return  dateStr;
	}

    public static LocalDateTime toLocalDateTime(long milli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), systemDefault());
    }

	/**
	 * 获得距离当前时间某一天的的具体时间
	 * 
	 * @param currenttime
	 * @param days
	 * @return
	 */
	public static long getOneDayCurentime(long currenttime, int days) {

		return currenttime + ONE_DAY_TIME * days;
	}

	/**
	 * 判断传入时间是否为今天
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isToday(long time) {

		return time >= startOfToday() && time <= endOfToday();
	}

	/**
	 * 获取今天的开始时间
	 *
	 * @return
	 */
	public static long startOfToday() {
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		return startTime.getTimeInMillis();
	}


    public static long startOfDay(int year,int month,int day) {
        LocalDateTime localDateTime = LocalDateTime.of(year,month,day,0,0,0);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    public static long startOfDay(Date date){
        LocalDateTime localDateTime =  toLocalDateTime(date)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }


    public static long startOfDay(LocalDate date){
        LocalDateTime localDateTime =  date.atStartOfDay();
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    public static ZoneId systemDefault(){
        return SYSTEM_DEFAULT_ZONEID;
    }


    public static long startOfDay(int day) {
        LocalDateTime localDateTime = LocalDateTime.now()
                .with((temporal) -> temporal.with(DAY_OF_MONTH, day))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }


	/***
	 * 本月最后一秒
	 * @return
	 */
	public static long endOfMonth(){
		LocalDateTime lastDayOfMonth = LocalDateTime.now()
				.with(TemporalAdjusters.lastDayOfMonth())
				.withHour(23)
                .withMinute(59)
                .withSecond(59)
				.withNano(999);
		return lastDayOfMonth.atZone(systemDefault()).toInstant().toEpochMilli();
	}


    public static long endOfMonth(Date day){
        LocalDateTime lastDayOfMonth = toLocalDateTime(day)
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999);
        return lastDayOfMonth.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    public static long endOfMonth(LocalDate day){
        LocalDateTime lastDayOfMonth = day.atStartOfDay()
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999);
        return lastDayOfMonth.atZone(systemDefault()).toInstant().toEpochMilli();
    }

	public static int judgeDay(LocalDate localDate){
		Preconditions.checkNotNull(localDate);
		LocalDate now = LocalDate.now();
		return localDate.compareTo(now);
	}


    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), systemDefault());
    }


    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate date) {
        return toDate(date.atStartOfDay());
    }

    public static Date toDate(LocalTime time) {
        return toDate(LocalDateTime.of(LocalDate.now(), time));
    }

    /***
     * >0未来 =0 今天 <0过去
     * @param date
     * @return
     * @throws DateTimeException 时间不合法
     */
    public static int judgeDay(Date date){
        Preconditions.checkNotNull(date);
        LocalDate localDate = toLocalDate(date);
        LocalDate now = LocalDate.now();
        return localDate.compareTo(now);
    }


    /***
     *
     * @param day
     * @return >0未来 =0 今天 <0过去
     * @throws DateTimeException 时间不合法
     */
    public static int judgeDayInThisMonth(int day) throws DateTimeException{
            LocalDate localDate = LocalDate.now()
                    .with((temporal) -> temporal.with(DAY_OF_MONTH, day));
            LocalDate now = LocalDate.now();
            return localDate.compareTo(now);
    }


	/**
	 * 获取今天的结束时间
	 * 
	 * @return
	 */
	public static long endOfToday() {
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(new Date());
		startTime.set(Calendar.HOUR_OF_DAY, 23);
		startTime.set(Calendar.MINUTE, 59);
		startTime.set(Calendar.SECOND, 59);
		startTime.set(Calendar.MILLISECOND, 999);
		return startTime.getTimeInMillis();
	}


    public static long endOfDay(int day) {
        LocalDateTime localDateTime = LocalDateTime.now()
                .with((temporal) -> temporal.with(DAY_OF_MONTH, day))
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    public static long endOfDay(Date day){
        LocalDateTime localDateTime =  toLocalDateTime(day)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

	public static long endOfDay(int year,int month,int day){
        LocalDateTime localDateTime = LocalDateTime.of(year,month,day,23,59,59,999);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }


    public static boolean inSameWeek(LocalDate day, LocalDate other) {
        if (day.isEqual(other)) {
            return true;
        }
        LocalDate before;
        LocalDate after;
        if (day.isBefore(other)) {
            before = day;
            after = other;
        } else {
            before = other;
            after = day;
        }

        int diff = 0;
        switch (before.getDayOfWeek()) {
            case MONDAY:
                diff = 7;
                break;
            case TUESDAY:
                diff = 6;
                break;
            case WEDNESDAY:
                diff = 5;
                break;
            case THURSDAY:
                diff = 4;
                break;
            case FRIDAY:
                diff = 3;
                break;
            case SATURDAY:
                diff = 2;
                break;
            case SUNDAY:
                diff = 1;
                break;
            default:
                break;
        }
        return before.plusDays(diff).isAfter(after);
    }

	/**
	 * 获取今天的结束时间
	 *
	 * @return
	 */
	public static long startOfTomorrow() {
		LocalDateTime startTime = LocalDateTime.now()
				.plusDays(1)
				.withHour(0)
				.withMinute(0)
				.withSecond(0)
				.withNano(0);
		return startTime.atZone(systemDefault()).toInstant().toEpochMilli();
	}

    public static long startOfTomorrow(Date day) {
        LocalDateTime startTime = toLocalDateTime(day)
                .plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return startTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

	/**
	 * 判断给定的时间距离今天的时间天数
	 * 
	 * @param time
	 * @return
	 */
	public static int distanceToday(long time) {
		if (time > startOfToday() && time < endOfToday()) {
			return 0;
		}
		if (time < startOfToday()) {
			return (int) ((time - startOfToday()) / ONE_DAY_TIME) - 1;
		}

		return (int) ((time - startOfToday()) / ONE_DAY_TIME);

	}




	public static int getYear(String text, String pattern){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(text,pattern));
		int year = calendar.get(Calendar.YEAR);
		return year;
	}


	public static int getMonth(String text, String pattern){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(text,pattern));
		int month = calendar.get(Calendar.MONTH);
		return month;
	}


	public static int weekOfMonth(String text, String pattern){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(text,pattern));
		int week = calendar.get(Calendar.WEEK_OF_MONTH);
		return week;
	}

	public static int dayOfWeek(String text, String pattern){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(text,pattern));
		return calendar.get(Calendar.DAY_OF_WEEK);
	}


	/***
	 * 本月第n天
	 * @param day
	 * @return
	 */
	public static Date dayOfMonth(int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH,day);
		return calendar.getTime();
	}


	/***
	 * day天以后，负数代表之前
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date,int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,day);
		return calendar.getTime();
	}


	/**
	 * 获取去年最后一秒
	 *
	 * @return
	 */
	public static Date endOfPassToday() {
		Calendar startTime = Calendar.getInstance();
		int year=startTime.get(Calendar.YEAR);
		String end=year-1+"-12-31 23:59:59";
		Date temp=parseDate(end, "yyyy-MM-dd HH:mm:ss");
		if (temp == null) {
			return null;
		}
		return new Date(temp.getTime());
	}




	public static String now() {
		return formatter(DEFAULT_DATETIME_PATTERN).format(new Date());
	}

	/**
	 * 获取当前时间的format格式
	 * 
	 * @param Format
	 * @return
	 */
	public static String formatDate(String Format) {
		Date d = Calendar.getInstance().getTime();
		try {
			SimpleDateFormat f = new SimpleDateFormat(Format);
			return f.format(d);
		} catch (Exception e) {
			logger.error("formatDate",e);
			return "";
		}
	}


	
	


	/**
	 * 日期的天数比较
	 * @param date1 毫秒数
	 * @param date2
	 * @return
	 *  date1 > date2 正数
	 *  date1 < date2 负数
	 *  date1 = date2 0
	 */
	public static long getDateDiff(long date1, long date2) {
		long diffInMillies = date1 - date2;
		long convert = TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);
		Calendar instance1 = Calendar.getInstance();
		instance1.setTimeInMillis(date1);
		Calendar instance2 = Calendar.getInstance();
		instance2.setTimeInMillis(date2);
		instance2.set(Calendar.DATE, instance2.get(Calendar.DATE) + (int)convert);

		if (instance1.get(Calendar.DAY_OF_MONTH) == instance2.get(Calendar.DAY_OF_MONTH)) {
			return convert;
		} else if (instance1.before(instance2)) {
			return convert - 1;
		} else {
			return convert + 1;
		}
	}

    /***
     * 日期的天数比较
     * @param date1
     * @param date2
     * @return
     *   date1 > date2 正数
     *  date1 < date2 负数
     *  date1 = date2 0
     */
	public static long getDateDiff(Date date1,Date date2){
		if (date2 == null) date2 = new Date();
        return  ChronoUnit.DAYS.between(toLocalDate(date2),toLocalDate(date1));
	}



	 /**
	  * 
	  * @param stamp
	  * @return
	  */
	 public static String getStandardTimeText1(long stamp){
		 long current=System.currentTimeMillis();
		 long re=current-stamp;
		 if(re<0){
			 re=-1*re;
		 }
		 if(re>=0&&re<=60*1000L){
			 return "1分钟";
		 }
		 if(re>60*1000L&&re<60*60*1000L){
			 int rem=(int) (re/(60*1000));
			 return rem+"分钟";
		 }
		 if(re>=60*60*1000L&&re<24*60*60*1000L){
			 int rem=(int) (re/(60*60*1000L));
			 int min=(int) ((re-rem*60*60*1000L)/(60*1000L));
			 return rem+"小时"+min+"分钟";
		 }
		 if(re>=24*60*60*1000L){
			 int rem=(int) (re/(24*60*60*1000L));
			 return rem+"天";
		 }
		 return "3天";
	 }
	 
	 

}
