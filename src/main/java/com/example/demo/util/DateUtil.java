package com.example.demo.util;

import com.example.demo.model.param.DateDto;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class DateUtil {

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    /**
     * 一天的时间的毫秒数
     */
    public static final long ONE_DAY_TIME = 86400000L;

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_YEAR_MONTH_PATTERN = "yyyy-MM";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_DATE_MIN_TIME_PATTERN = "MM-dd HH:mm";

    public static final int FIELD_YEAR = Calendar.YEAR;
    public static final int FIELD_MONTH = Calendar.MONTH;
    public static final int FIELD_DAY = Calendar.DAY_OF_MONTH;
    public static final int FIELD_HOUR = Calendar.HOUR_OF_DAY;
    public static final int FIELD_MINUTE = Calendar.MINUTE;
    public static final int FIELD_SECOND = Calendar.SECOND;

    /**
     * 锁对象
     */
    private static final Object LOCK_OBJ = new Object();


    private static final ZoneId SYSTEM_DEFAULT_ZONEID = ZoneId.systemDefault();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
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
        DateFormat dateFormat = getSdf(pattern);
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
        return dateStr;
    }

    public static String dateFmt(Long timeStamp) {
        if (timeStamp == null || timeStamp.equals(0)) {
            return null;
        }
        return dateFmt(DEFAULT_YEAR_MONTH_PATTERN, timeStamp * 1000);
    }

    public static String dateFmtSec(Long timeStamp) {
        if (timeStamp == null || timeStamp.equals(0)) {
            return null;
        }
        //兼容下。卖车线索有人传毫秒有人传秒
        if (Math.abs(timeStamp) < 10000000000L) {
            timeStamp = timeStamp * 1000;
        }
        return dateFmt(DEFAULT_YEAR_MONTH_PATTERN, timeStamp);
    }

    public static String dateFmt(String pattern, long timeStamp) {
        String dateStr = "";
        dateStr = formatter(pattern).format(timeStamp);
        return dateStr;
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


    public static long startOfDay(int year, int month, int day) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, 0, 0, 0);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }


    /**
     * 获取date当天的开始时间
     *
     * @param date
     * @return
     */
    public static long startOfDay(Date date) {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(date);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        return startTime.getTimeInMillis();
    }

//    public static long startOfYear() {
//        Calendar startTime = Calendar.getInstance();
//        int year = startTime.get(Calendar.YEAR);
//        String end = year - 1 + "-12-31 23:59:59";
//        Date temp = DateTimeUtil.parseStringToDate(end, "yyyy-MM-dd HH:mm:ss");
//        return temp.getTime();
//    }

//    public static long startOfMonth() {
//        Calendar startTime = Calendar.getInstance();
//        int year = startTime.get(Calendar.YEAR);
//        int month = startTime.get(Calendar.MONTH);
//        String start = year + "-" + (month + 1) + "-01 00:00:00";
//        Date temp = DateTimeUtil.parseStringToDate(start, "yyyy-MM-dd HH:mm:ss");
//        return temp.getTime();
//    }

    /***
     * 本月最后一秒
     * @return
     */
    public static long endOfMonth() {
        LocalDateTime lastDayOfMonth = LocalDateTime.now()
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999);
        return lastDayOfMonth.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    public static int judgeDay(LocalDate localDate) {
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

    public static int judgeDay(Date date) {
        Preconditions.checkNotNull(date);
        LocalDate localDate = toLocalDate(date);
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


    public static int getYear(String text, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(text, pattern));
        int year = calendar.get(Calendar.YEAR);
        return year;
    }


    public static int getMonth(String text, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(text, pattern));
        int month = calendar.get(Calendar.MONTH);
        return month;
    }


    public static int weekOfMonth(String text, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(text, pattern));
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        return week;
    }

    public static int dayOfWeek(String text, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(text, pattern));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    /***
     * 本月第n天
     * @param day
     * @return
     */
    public static Date dayOfMonth(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }


    /***
     * day天以后，负数代表之前
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }


    /**
     * 获取去年最后一秒
     *
     * @return
     */
    public static Date endOfPassToday() {
        Calendar startTime = Calendar.getInstance();
        int year = startTime.get(Calendar.YEAR);
        String end = year - 1 + "-12-31 23:59:59";
        Date temp = parseDate(end, "yyyy-MM-dd HH:mm:ss");
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
            logger.error("formatDate", e);
            return "";
        }
    }


//    /**
//     * 获取时间戳
//     *
//     * @param pattern
//     * @param date
//     * @return
//     */
//    public static long stamp(String pattern, String date) {
//        if (StringUtil.isEmpty(date)) {
//            return 0;
//        }
//        Date strToDate = parseDate(date, pattern);
//        if (null == strToDate) {
//            return 0;
//        } else {
//            return strToDate.getTime();
//        }
//    }

//
//    public static long getBirthDayByIDNumber(String idNumber) {
//        try {
//            if (StringUtil.isEmpty(idNumber) || idNumber.length() != 18) {
//                return 0;
//            }
//            String birthday = idNumber.substring(6, 14);
//            Date date = DateTimeUtil.parseStringToDate(birthday, "yyyyMMdd");
//            if (date == null) {
//                return 0;
//            }
//            if (date.getTime() > System.currentTimeMillis()) {
//                return 0;
//            }
//            return date.getTime();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    /**
     * 日期的天数比较
     *
     * @param date1 毫秒数
     * @param date2
     * @return date1 > date2 正数
     * date1 < date2 负数
     * date1 = date2 0
     */
    public static long getDateDiff(long date1, long date2) {
        long diffInMillies = date1 - date2;
        long convert = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        Calendar instance1 = Calendar.getInstance();
        instance1.setTimeInMillis(date1);
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(date2);
        instance2.set(Calendar.DATE, instance2.get(Calendar.DATE) + (int) convert);

        if (instance1.get(Calendar.DAY_OF_MONTH) == instance2.get(Calendar.DAY_OF_MONTH)) {
            return convert;
        } else if (instance1.before(instance2)) {
            return convert - 1;
        } else {
            return convert + 1;
        }
    }

    public static ZoneId systemDefault() {
        return SYSTEM_DEFAULT_ZONEID;
    }


//    /**
//     * 获取大搜车标准时间文本1
//     */
//    public static String getStandardTimeText(long stamp) {
//        long current = System.currentTimeMillis();
//        long re = current - stamp;
//        if (re <= 60 * 1000L) {
//            return "刚刚";
//        }
//        if (re > 60 * 1000L && re < 60 * 60 * 1000L) {
//            int rem = (int) (re / (60 * 1000));
//            return rem + "分钟前";
//        }
//        if (re >= 60 * 60 * 1000L && re < 24 * 60 * 60 * 1000L) {
//            int rem = (int) (re / (60 * 60 * 1000));
//            return rem + "小时前";
//        }
//        if (re >= 24 * 60 * 60 * 1000L && stamp >= startOfToday() - 24 * 60 * 60 * 1000L) {
//            String hour = DateTimeUtil.formatDate(new Date(stamp), "HH:mm");
//            return "昨天 " + hour;
//        }
//        String text = DateTimeUtil.formatDate(new Date(stamp), "yyyy/MM/dd hh:mm");
//        if (stamp > startOfYear()) {
//            return text.substring(5, text.length());
//        }
//        return text;
//    }

    /**
     * @param stamp
     * @return
     */
    public static String getStandardTimeText1(long stamp) {
        long current = System.currentTimeMillis();
        long re = current - stamp;
        if (re < 0) {
            re = -1 * re;
        }
        if (re >= 0 && re <= 60 * 1000L) {
            return "1分钟";
        }
        if (re > 60 * 1000L && re < 60 * 60 * 1000L) {
            int rem = (int) (re / (60 * 1000));
            return rem + "分钟";
        }
        if (re >= 60 * 60 * 1000L && re < 24 * 60 * 60 * 1000L) {
            int rem = (int) (re / (60 * 60 * 1000L));
            int min = (int) ((re - rem * 60 * 60 * 1000L) / (60 * 1000L));
            return rem + "小时" + min + "分钟";
        }
        if (re >= 24 * 60 * 60 * 1000L) {
            int rem = (int) (re / (24 * 60 * 60 * 1000L));
            return rem + "天";
        }
        return "3天";
    }

    public static String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String changeFormat(String dateTime, String dateFormatFrom, String dateFormatTo) {
        SimpleDateFormat formatFrom = new SimpleDateFormat(dateFormatFrom);
        SimpleDateFormat formatTo = new SimpleDateFormat(dateFormatTo);
        Date date;
        try {
            date = formatFrom.parse(dateTime);
            return formatTo.format(date);
        } catch (ParseException e) {
            logger.error("changeFormat error:", e);
            return dateTime;
        }
    }

    /**
     * 将英文状态的星期变为中文状态的星期
     */
    public static String weekFormat(String date) {
        if (date.contains("Mon")) {
            return date.replace("Mon", "周一");
        }
        if (date.contains("Tue")) {
            return date.replace("Tue", "周二");
        }
        if (date.contains("Wed")) {
            return date.replace("Wed", "周三");
        }
        if (date.contains("Thu")) {
            return date.replace("Thu", "周四");
        }
        if (date.contains("Fri")) {
            return date.replace("Fri", "周五");
        }
        if (date.contains("Sat")) {
            return date.replace("Sat", "周六");
        }
        if (date.contains("Sun")) {
            return date.replace("Sun", "周日");
        }
        return "";
    }

    /**
     * 计算时间
     *
     * @param date   基准时间
     * @param field  时间轴
     * @param amount 加减值
     * @return
     */
    public static Date calculateTime(Date date, int field, int amount) {
        if (date == null) {
            throw new NullPointerException("date is null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 获取当前时期
     *
     * @return
     */
    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }


    /**
     * @description 获取某月第一天
     * @Author wujiahuan
     * @Date 2018/5/17 上午9:52
     */
    public static Date getThisMonthFirstDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        Calendar thisCalendar = Calendar.getInstance();
        thisCalendar.set(Calendar.YEAR, year);
        thisCalendar.set(Calendar.MONTH, month);
        thisCalendar.set(Calendar.DAY_OF_MONTH, 1);
        thisCalendar.set(Calendar.HOUR_OF_DAY, 0);
        thisCalendar.set(Calendar.MINUTE, 0);
        thisCalendar.set(Calendar.SECOND, 0);
        thisCalendar.set(Calendar.MILLISECOND, 0);
        return thisCalendar.getTime();
    }

    public static Date getMonthFirstDate(String dateString) {
        LocalDate parse = LocalDate.parse(dateString);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = parse.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 可以解析"2018-08-12"或者"2018-08-12 13:31:13"形式
     *
     * @param dateString
     * @return
     */
    public static Date stringToDateCommon(String dateString) {
        String format = "((((19|20)\\d{2})-(0?(1|[3-9])|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$";
        String formatContainTime = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        try {
            if (dateString.matches(format)) {
                return new SimpleDateFormat(DEFAULT_DATE_PATTERN).parse(dateString);
            }
            if (dateString.matches(formatContainTime)) {
                return new SimpleDateFormat(DEFAULT_DATETIME_PATTERN).parse(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("日期格式不对啊");
    }

    /**
     * 只能解析"2018-08-12"的格式
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        try {
            return new SimpleDateFormat(DEFAULT_DATE_PATTERN).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算两个时间之差,字符串类型
     * @param beginDateString
     * @param endDateString
     * @return
     */
    public static long DisDay(String beginDateString, String endDateString) {
        LocalDate beginDate = LocalDate.parse(beginDateString);
        LocalDate endDate = LocalDate.parse(endDateString);
        return beginDate.until(endDate, ChronoUnit.DAYS);
    }

//    public static List<DateDto> getDateList(String beginDateString, String endDateString) {
//        Date beginDate = stringToDate(beginDateString);
//        Date endDate = stringToDate(endDateString);
//        long disDay = getDisDay(beginDate, endDate);
//        List<DateDto> list = Lists.newArrayList();
//        for (int i = 0; i < disDay + 1; i++) {
//            Date beginningDate = addDay(beginDate, i);
//            Date nextDate = addDay(beginDate, i + 1);
//            Date monthFirstDate = getThisMonthFirstDate(beginningDate);
//            DateDto dto = DateDto.builder()
//                    .beginDate(beginningDate)
//                    .monthFirstDate(monthFirstDate)
//                    .nextDate(nextDate)
//                    .build();
//            list.add(dto);
//        }
//        return list;
//    }

    private static long[] getDisTime(Date startDate, Date endDate) {
        long timesDis = Math.abs(startDate.getTime() - endDate.getTime());
        long day = timesDis / (1000 * 60 * 60 * 24);
        long hour = timesDis / (1000 * 60 * 60) - day * 24;
        long min = timesDis / (1000 * 60) - day * 24 * 60 - hour * 60;
        long sec = timesDis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        return new long[]{day, hour, min, sec};
    }

    /**
     * 计算两个日期的差值,Date类型
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDisDay(Date startDate, Date endDate) {
        long[] dis = getDisTime(startDate, endDate);
        long day = dis[0];
        if (dis[1] > 0 || dis[2] > 0 || dis[3] > 0) {
            day += 1;
        }
        logger.info("两个日期的差值为:{{}}天", day);
        return day;
    }

	public static void main(String[] args) {
		/*Calendar calendar = Calendar.getInstance();
		calendar.set(2018,4,16);
		Date date = calendar.getTime();
		Date first = DateUtil.getThisMonthFirstDate(date);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(first));
        Date format = parseDate("2018-08-12","yyyy-MM-dd");
        System.out.println(format);*/
        //List<DateDto> dtos = getDateList("2018-07-12", "2018-09-12");

    }


    public static List<DateDto> getDateList(String beginDateString, String endDateString) {
        Date beginDate = stringToDate(beginDateString);
        Date endDate = stringToDate(endDateString);
        long disDay = getDisDay(beginDate, endDate);
        List<DateDto> list = Lists.newArrayList();
        for (int i = 0; i < disDay + 1; i++) {
            Date beginningDate = addDay(beginDate, i);
            Date nextDate = addDay(beginDate, i + 1);
            Date monthFirstDate = getThisMonthFirstDate(beginningDate);
            DateDto dto = DateDto.builder()
                    .beginDate(beginningDate)
                    .monthFirstDate(monthFirstDate)
                    .nextDate(nextDate)
                    .build();
            list.add(dto);
        }
        return list;
    }
}
