package com.jd.hackathon.shooting.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 时间相关的通用类
 * @author dubiaopei
 */

public class DateUtils {

    public static final String               YYYY_MM_DD                 = "yyyy-MM-dd";

    public static final String               YYYYMMDD                   = "yyyyMMdd";

    public static final String               YYYY_MM                    = "yyyy-MM";

    public static final String               YYYYMM                     = "yyyyMM";

    public static final String               MM_DD                      = "MM/dd";

    public static final String               C_MM_DD                    = "MM-dd";

    public static final String               YYYY_MM_DD_TWO             = "yyyy/MM/dd";

    public static final String               YYYY_MM_DD_THER            = "yyyy.MM.dd";

    public static final String               YYYY_MM_DD_24HH_MM         = "yyyy-MM-dd HH:mm";

    public static final String               YYYY_MM_DD_HH_MM_SS        = "yyyy-MM-dd hh:mm:ss";

    public static final String               YYYY_MM_DD_24HH_MM_SS      = "yyyy-MM-dd HH:mm:ss";

    public static final String               YYYY_MM_DD_24HH_MM_SS_     = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String               HH_MM_SS                   = "HH:mm:ss";

    public static final String               YYYYMMDDHHMMSS             = "yyyyMMddHHmmss";

    public static final String               CHINESEYYYYMMDD            = "yyyy年MM月dd日";

    public static final String               CHINESE_YYYYMMDD_HHMMSS    = "yyyy年MM月dd日 HH时MM分SS秒";

    public static final String               CHINESEMMDD                = "MM月dd日";

    public static final String               CHINESEDD                  = "dd号";

    public static final String               CHINESEYYYYMM              = "yyyy年MM月";

    public static final String               START_YYYYMM               = "yyyy-MM-01";

    public static final String               END_HHMMSS                 = " 23:59:59";

    public static final String               START_HHMMSS               = " 00:00:00";

    public static final Integer              DAY_OF_WEEK                = 7;

    public static final Integer              DAY_OF_MIN                 = 1;

    public static final Map<Integer, String> WEEK_CHINE_MAP             = new HashMap<Integer, String>();

    public static final Map<Integer, String> WEEK_CHINE_SIMPLATE_MAP    = new HashMap<Integer, String>();

    public static final Map<Integer, String> WEEK_CHINE_SIMPLATEST_MAP_ = new HashMap<Integer, String>();

    public static final DateTimeFormatter    default_formatter          = DateTimeFormat
            .forPattern(YYYY_MM_DD_24HH_MM_SS);
//    protected static ExecutorService asyncService     = Executors.newFixedThreadPool(Runtime.getRuntime()
//            .availableProcessors() * 2 + 10);

    @Resource
    private static ThreadPoolExecutor globalPool;


    static {
        WEEK_CHINE_MAP.put(1, "星期一");
        WEEK_CHINE_MAP.put(2, "星期二");
        WEEK_CHINE_MAP.put(3, "星期三");
        WEEK_CHINE_MAP.put(4, "星期四");
        WEEK_CHINE_MAP.put(5, "星期五");
        WEEK_CHINE_MAP.put(6, "星期六");
        WEEK_CHINE_MAP.put(7, "星期日");

        WEEK_CHINE_SIMPLATE_MAP.put(1, "周一");
        WEEK_CHINE_SIMPLATE_MAP.put(2, "周二");
        WEEK_CHINE_SIMPLATE_MAP.put(3, "周三");
        WEEK_CHINE_SIMPLATE_MAP.put(4, "周四");
        WEEK_CHINE_SIMPLATE_MAP.put(5, "周五");
        WEEK_CHINE_SIMPLATE_MAP.put(6, "周六");
        WEEK_CHINE_SIMPLATE_MAP.put(7, "周日");

        WEEK_CHINE_SIMPLATEST_MAP_.put(1, "一");
        WEEK_CHINE_SIMPLATEST_MAP_.put(2, "二");
        WEEK_CHINE_SIMPLATEST_MAP_.put(3, "三");
        WEEK_CHINE_SIMPLATEST_MAP_.put(4, "四");
        WEEK_CHINE_SIMPLATEST_MAP_.put(5, "五");
        WEEK_CHINE_SIMPLATEST_MAP_.put(6, "六");
        WEEK_CHINE_SIMPLATEST_MAP_.put(7, "日");

    }

    /**
     * 获得两个日期之间的年月集合
     * @param start
     * @param end
     * @return返回结果集的Date都是1号
     */
    public static List<Date> periodYearMonth(Date start, Date end) {
        if (start == null || end == null || start.after(end)) {
            return null;
        }
        start = new DateTime(start).withDayOfMonth(1).toDate();
        start = toStartTime(start);
        end = new DateTime(end).dayOfMonth().withMaximumValue().toDate();
        end = toEndTime(end);
        List<Date> dates = new ArrayList<Date>();
        Date _date = start;
        while (_date.before(end) || _date.equals(end)) {
            dates.add(_date);
            _date = new DateTime(_date).plusMonths(1).toDate();
        }
        return dates;
    }

    /**
     * 获得两个日期之间的日期集合
     * @param start
     * @param end
     * @return返回结果集的Date都是1号
     */
    public static List<Date> periodDay(Date start, Date end) {
        if (start == null || end == null || start.after(end)) {
            return null;
        }
        start = toStartTime(start);
        end = toEndTime(end);
        List<Date> dates = new ArrayList<Date>();
        Date _date = start;
        while (_date.before(end) || _date.equals(end)) {
            dates.add(_date);
            _date = new DateTime(_date).plusDays(1).toDate();
        }
        return dates;
    }

    public static boolean isSameTime(Date date1, Date date2) {
        return date1 == date2 || (date1 != null && date2 != null && date1.equals(date2));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return date1 == date2
                || (date1 != null && date2 != null && new DateTime(date1).withMillisOfDay(0).equals(
                new DateTime(date2).withMillisOfDay(0)));
    }

    /**
     * 是否为今天
     */
    public static boolean isToday(Date checkDate) {
        return isSameDay(checkDate, new Date());
    }

    /**
     * 是否大于今天时间
     */
    public static boolean isGreaterToDay(Date checkDate) {
        return checkDate.after(toEndTime(new Date()));
    }

    public static boolean isToday(Long time) {
        return isToday(new Date(time));
    }

    public static Double checkDoubleEmpty(Double value) {
        return null == value ? 0.0 : value;
    }

    /**
     * 获取指定日期上班时间，6：00
     */
    public static Date getWorkEarliest(Date date) {
        Date _date = DateUtils.toStartTime(date);
        return new DateTime(_date).withHourOfDay(6).toDate();
    }

    /**
     * 获取指定日期上班时间，9：00
     */
    public static Date getWorkStart(Date date) {
        Date _date = DateUtils.toStartTime(date);
        return new DateTime(_date).withHourOfDay(9).toDate();
    }

    /**
     * 获取指定日期中午休息开始时间 12：00
     */
    public static Date getMiddayStart(Date date) {
        return new DateTime(DateUtils.toStartTime(date)).withHourOfDay(12).toDate();
    }

    /**
     * 获取指定日期中午休息结束时间 13:00
     */
    public static Date getMiddayEnd(Date date) {
        return new DateTime(DateUtils.toStartTime(date)).withHourOfDay(13).toDate();
    }

    /**
     * 获取指定日期下班时间 18:00
     */
    public static Date getWorkEnd(Date date) {
        return new DateTime(DateUtils.toStartTime(date)).withHourOfDay(18).toDate();
    }

    /**
     * 获取指定日期加班休息结束 19:00
     */
    public static Date getDinnerEnd(Date date) {
        return new DateTime(DateUtils.toStartTime(date)).withHourOfDay(19).toDate();
    }

    /**
     * 获取指定日期将计入加班开始时间 20:00
     */
    public static Date getOvertimeStart(Date date) {
        return new DateTime(DateUtils.toStartTime(date)).withHourOfDay(20).toDate();
    }

    /**
     * 判断日期是否属于今天
     */
    public static boolean equalsCurrentDate(Date date) {
        return getCurrentYMD(date).equals(getCurrentYMD());
    }

    /**
     * 生成当前年-月-日的字符串:YYYY-MM-DD
     */
    public static String getCurrentYYYYMMMDD() {
        return dateToStr(new Date(), YYYY_MM_DD);
    }

    /**
     * 生成当前年-月-日的字符串:YYYYMMDDHHMMSS
     */
    public static String getCurrentAll() {
        return dateToStr(new Date(), YYYYMMDDHHMMSS);
    }

    public static String getCurrent24All() {
        return dateToStr(new Date(), YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 生成当前年-月-日的字符串:精确到毫秒
     */
    public static String getCurrent24All_() {
        return dateToStr(new Date(), YYYY_MM_DD_24HH_MM_SS_);
    }

    /**
     * 生成当前年-月-日的字符串:精确到毫秒
     */
    public static String getChineCurrent24All() {
        return dateToStr(new Date(), CHINESE_YYYYMMDD_HHMMSS);
    }

    /**
     * 生成当前年月日的字符串:YYYYMMDD
     */
    public static String getCurrentYMD() {
        return dateToStr(new Date(), YYYYMMDD);
    }

    /**
     * 生成指定的年月日的字符串: YYYYMMDD
     * @param date
     * @return
     */
    public static String getCurrentYMD(Date date) {
        return dateToStr(date, YYYYMMDD);
    }

    /**
     * 格式：2016-01-19 to 20160119
     * @param date
     * @return
     */
    public static Integer getYYYYMMDD(Date date) {
        String ymd = getCurrentYMD(date);
        return StringUtils.isBlank(ymd) ? -1 : Integer.parseInt(ymd);
    }

    public static String getCurrentYMD(Long time) {
        return getCurrentYMD(new Date(time));
    }

    /**
     * 获取当前年月的字符串：YYYYMM
     */
    public static String getCurrentYearMonth() {
        return dateToStr(new Date(), YYYYMM);
    }

    /**
     * 时间按照指定格式转换为日期，去掉时分秒数据 Wed May 08 00:00:00 CST 2013
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date timeToDate(Date date, String formatStr) {
        return strToDate(dateToStr(date, formatStr), formatStr);
    }

    // 获取当前时间 2013-05-08 10:12:42.333
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取用户指定的时间
     * @param time
     * @return Timestamp
     */
    public static Timestamp getUserTimestamp(long time) {
        return new Timestamp(time);
    }

    /**
     * 获取用户指定的时间
     * @param date
     * @return Timestamp
     */
    public static Timestamp getCurrentTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 获取用户指定的时间，与Timestamp getUserTimestamp(long time)相同
     * @param time
     * @return Timestamp
     */
    public static Timestamp getCurrentTimestamp(long time) {
        return new Timestamp(time);
    }

    /**
     * 获取用户指定的时间
     * @param dateStr
     * @return Timestamp
     */
    public static Timestamp getCurrentTimestamp(String dateStr) {
        Date date = strToDate(dateStr, YYYY_MM_DD);
        return new Timestamp(date.getTime());
    }

    /**
     * Timestamp 转换为字符串YYYY_MM_DD
     * @param time
     * @return String
     */
    public static String timestampToStr(Timestamp time) {
        String result = null;
        if (time != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
            result = sdf.format(time);
        }
        return result;
    }

    /**
     * 字符串转换为TimeStamp
     * @param dateStr 格式为 new Date().getTime()
     * @return
     */
    public static Timestamp strToTimestamp(String dateStr) {
        return new Timestamp(Long.parseLong(dateStr));
    }

    @SuppressWarnings("deprecation")
    public static Timestamp setDayMaxStr(Timestamp time) {
        time.setHours(23);
        time.setMinutes(59);
        time.setSeconds(59);
        return time;
    }

    /**
     * 获取前一天的开始时间,2012-09-23 00:00:00
     * @return String
     */
    public static String getBeforeStartDateToStr() {
        return new DateTime(getBeforeStartDate()).toString(YYYY_MM_DD_24HH_MM_SS);
    }

    public static Date getBeforeStartDate() {
        return DateTime.now().minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    /**
     * 获取当前日期的前xx天
     * @return Date
     */
    public static Date getBeforeCurrentDate(int days) {
        return DateTime.now().minusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    /**
     * 获取前一天的结束时间,2012-09-23 23:59:59
     */
    public static String getBeforeEndDateToStr() {
        return getBeforeEndDateToStr(new Date());
    }

    /**
     * 获取设定日的前一天的结束时间
     * @param date
     * @return String
     */
    public static String getBeforeEndDateToStr(Date date) {
        return new DateTime(date).minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString(
                YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取当前年第一天的开始时间
     * @return Date
     */
    public static Date getCurrentYearStart() {
        return DateTime.now().withDayOfYear(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    public static Date getCurrentYearEnd() {
        return DateTime.now().dayOfYear().withMaximumValue().toDate();
    }

    public static Date getUserSetYearStart(Date day) {
        return new DateTime(day).withDayOfYear(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    public static Date getUserSetYearEnd(Date day) {
        return new DateTime(day).dayOfYear().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate();
    }

    /**
     * 获取前一天的结束时间 yyyy-MM-dd hh:mm:ss Sun Sep 23 23:59:59 CST 2012
     * @return Date
     */
    public static Date getBeforeEndDate() {
        return DateTime.now().minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate();
    }

    public static Date getBeforeEndDate(Date date) {
        return new DateTime(date).minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate();
    }

    /**
     * 获取前一天的结束时间 yyyy-MM-dd hh:mm:ss 2012-09-23 23:59:59.0
     * @return Timestamp
     */
    public static Timestamp getBeforeEndTimeStamp() {
        Date date = getBeforeEndDate();
        return new Timestamp(date.getTime());
    }

    /**
     * 获取今天的开始时间,2012-09-24 00:00:00
     * @return String
     */
    public static String getCurrentStartDateToStr() {
        return DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
                .toString(YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取今天的结束时间,2012-09-24 23:59:59
     * @return String
     */
    public static String getCurrentEndDateToStr() {
        return DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString(
                YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取今天的开始时间
     * @return String
     * @author xiehuixia
     * @date 2014年11月5日下午5:01:46
     */
    public static Date getCurrentStartDate() {
        return DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    /**
     * 获取今天的结束时间yyyy-MM-dd hh:mm:ss Mon Sep 24 00:00:00 CST 2012
     * @return Date
     */
    public static Date getCurrentEndDate() {
        return DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate();
    }

    /**
     * 获取今天的结束时间yyyy-MM-dd hh:mm:ss 2012-09-24 00:00:00.0
     * @return Timestamp
     */
    public static Timestamp getCurrentEndTimeStamp() {
        Date date = getCurrentEndDate();
        return new Timestamp(date.getTime());
    }

    /**
     * 获取设定的结束时间 ：2013-05-07 23:59:59
     * @param date
     * @param day
     * @return String
     */
    public static String getUserSetEndDateToStr(Date date, int day) {
        return new DateTime(date).plusDays(day).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString(
                YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取设定的结束时间 2013-05-08 23:59:59
     * @param date
     * @return String
     */
    public static String getUserSetEndDateToStr(Date date) {
        return new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString(
                YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取设定的结束时间Tue May 07 23:59:59 CST 2013
     * @param date
     * @param day
     * @return Date
     */
    public static Date getUserSetEndDate(Date date, int day) {
        return DateTime.parse(getUserSetEndDateToStr(date, day), default_formatter).toDate();
    }

    /**
     * 获取设定的结束时间Wed May 08 23:59:59 CST 2013
     * @param date
     * @return Date
     */
    public static Date getUserSetEndDate(Date date) {
        return getUserSetEndDate(date, 0);
    }

    /**
     * 获取设定的的结束时间2013-05-07 23:59:59.0
     * @throws ParseException
     * @param date
     * @param day
     * @return Timestamp
     */
    public static Timestamp getUserSetEndTimeStamp(Date date, int day) {
        return new Timestamp(getUserSetEndDate(date, day).getTime());
    }

    /**
     * 获取设定的的结束时间2013-05-08 23:59:59.0
     * @param date
     * @return Timestamp
     */
    public static Timestamp getUserSetEndTimeStamp(Date date) {
        return getUserSetEndTimeStamp(date, 0);
    }

    /**
     * 获取设定的开始时间 2013-05-07 00:00:00
     * @param date
     * @param day
     * @return String
     */
    public static String getUserSetStartDateToStr(Date date, int day) {
        return new DateTime(date).plusDays(day).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString(
                YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取设定的开始时间2013-05-08 00:00:00
     * @param date
     * @return String
     */
    public static String getUserSetStartDateToStr(Date date) {
        return new DateTime(date).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString(
                YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取设定的开始时间Tue May 07 00:00:00 CST 2013
     * @param date
     * @param day
     * @return Date
     */
    public static Date getUserSetStartDate(Date date, int day) {
        return DateTime.parse(getUserSetStartDateToStr(date, day), default_formatter).toDate();
    }

    /**
     * 获取设定的开始时间Wed May 08 00:00:00 CST 2013
     * @param date
     * @return Date
     */
    public static Date getUserSetStartDate(Date date) {
        return getUserSetStartDate(date, 0);
    }

    /**
     * 获取设定的的开始时间2013-05-07 00:00:00.0
     * @param date
     * @param day
     * @return Timestamp
     */
    public static Timestamp getUserSetStartTimeStamp(Date date, int day) {
        return new Timestamp(getUserSetStartDate(date, day).getTime());
    }

    /**
     * 获取设定的的开始时间 2013-05-08 00:00:00.0
     * @param date
     * @return Timestamp
     */
    public static Timestamp getUserSetStartTimeStamp(Date date) {
        return getUserSetStartTimeStamp(date, 0);
    }

    /**
     * 取得两个日期间隔的月数
     * @param beginTime
     * @param endTime
     * @return
     */
    public static int monthsBetween(Date beginTime, Date endTime) {
        return Months.monthsBetween(new DateTime(beginTime), new DateTime(endTime)).getMonths();
    }

    /**
     * 取得两个日期之间间隔的天数
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 间隔天数，如果是正数代表结束时间大于开始时间,负数代表结束时间小于开始时间,0代表两个时间相同
     */
    public static long daysBetween(Date beginTime, Date endTime) {
        return Days.daysBetween(new LocalDate(beginTime), new LocalDate(endTime)).getDays();
    }

    /**
     * 取得两个日期之间间隔的小时数
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 间隔小时数，如果是正数代表结束时间大于开始时间,负数代表结束时间小于开始时间,0代表两个时间相同
     */
    public static int hoursBetween(Date beginTime, Date endTime) {
        return Hours.hoursBetween(new LocalDate(beginTime), new LocalDate(endTime)).getHours();
    }

    /**
     * 取得两个日期之间间隔的分钟数
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 间隔分钟数，如果是正数代表结束时间大于开始时间,负数代表结束时间小于开始时间,0代表两个时间相同
     */
    public static int minutesBetween(Date beginTime, Date endTime) {
        return Minutes.minutesBetween(new DateTime(beginTime), new DateTime(endTime)).getMinutes();
    }

    /**
     * 取得两个日期之间间隔的秒数
     */
    public static long secondBetween(Date beginTime, Date endTime) {
        if (null == beginTime || null == endTime) {
            return 0L;
        }
        Long dt = Math.abs(endTime.getTime() - beginTime.getTime());
        return dt / 1000;

    }

    /**
     * 获取指定日期与当前日期相关的秒数
     */
    public static long secondBetween(Date customerDate) {
        if (null == customerDate) {
            return 0L;
        }
        Long dt = Math.abs(System.currentTimeMillis() - customerDate.getTime());
        return dt / 1000;

    }

    /**
     * 获取指定日期与当前日期相关的分数
     */
    public static Long minBetween(Date customerDate) {
        if (null == customerDate) {
            return 0L;
        }
        Long dt = Math.abs(System.currentTimeMillis() - customerDate.getTime());
        return dt / (1000 * 60);

    }

    /**
     * 工具类：计算两个日期间隔的工作日（除掉周末）
     * @param start
     * @param end
     * @return
     */
    public static int calcWorkDays(long start, long end) {
        long interval = 24 * 3600 * 1000;
        int days = 0;
        while (start <= end) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(start);
            int week = cal.get(Calendar.DAY_OF_WEEK);
            if (!(week == 1 || week == 7)) {
                days++;
            }
            start = start + interval;
        }
        return days;
    }

    /**
     * 获取本周开始与结束日期
     * @return
     */
    public static Date[] getCurrWeekStartEndTime() {
        Date[] dates = new Date[2];
        dates[0] = DateTime.parse(DateTime.now().dayOfWeek().withMinimumValue().toString(YYYY_MM_DD)).toDate();
        dates[1] = DateTime.parse(DateTime.now().dayOfWeek().withMaximumValue().toString(YYYY_MM_DD)).toDate();
        return dates;
    }

    /**
     * 获取当前时间的指定天数中的一周的开始与结束时间
     */
    public static Date[] getCurrWeekStartEndTime(int day) {
        Date[] dates = new Date[2];
        dates[0] = DateTime.parse(DateTime.now().plusDays(day).dayOfWeek().withMinimumValue().toString(YYYY_MM_DD))
                .toDate();
        dates[1] = DateTime.parse(DateTime.now().plusDays(day).dayOfWeek().withMaximumValue().toString(YYYY_MM_DD))
                .toDate();
        return dates;
    }

    /**
     * 获取当前时间的上一周的开始到结束时间
     * @param tag true表示最后时间取的是周末时间(周一到周七)，false表示只取周五的时间(周一到周五)
     */
    public static Date[] getPreviousWeekStartEndTime(boolean tag) {
        int maxSize = tag ? 7 : 5;
        Date[] dates = new Date[maxSize];
        Date startDate = addDay(getFristDayOfWeek(new Date()), -7);
        dates[0] = startDate;
        for (int i = 1; i < maxSize; i++) {
            dates[i] = addDay(startDate, i);
        }
        return dates;
    }

    /**
     * key=星期下标,value=日期字符串 顺序：星期一为1，星期日为 7
     */
    public static Map<Integer, String> dateToStrMap(Date[] dates) {
        Map<Integer, String> dateMap = new HashMap<Integer, String>();
        if (null == dates) {
            return dateMap;
        }
        for (Date date : dates) {
            if (null == date) {
                continue;
            }
            int weekIndex = new DateTime(date).getDayOfWeek();
            dateMap.put(weekIndex, dateToStr(date, YYYY_MM_DD));
        }
        return dateMap;
    }

    public static Map<Integer, String> listDateToStrMap(List<Date> dates) {
        Map<Integer, String> dateMap = new HashMap<Integer, String>();
        if (null == dates) {
            return dateMap;
        }
        for (Date date : dates) {
            if (null == date) {
                continue;
            }
            int weekIndex = new DateTime(date).getDayOfWeek();
            dateMap.put(weekIndex, dateToStr(date, YYYY_MM_DD));
        }
        return dateMap;
    }

    public static Map<String, Integer> dateToMapKeyStr(Date[] dates) {
        Map<String, Integer> dateMap = new HashMap<String, Integer>();
        if (null == dates) {
            return dateMap;
        }
        for (Date date : dates) {
            if (null == date) {
                continue;
            }
            int weekIndex = new DateTime(date).getDayOfWeek();
            dateMap.put(dateToStr(date, YYYY_MM_DD), weekIndex);
        }
        return dateMap;
    }

    public static List<String> dateToStrList(Date[] dates) {
        List<String> list = new ArrayList<String>();
        if (null == dates) {
            return list;
        }
        for (Date date : dates) {
            if (null == date) {
                continue;
            }
            int weekIndex = new DateTime(date).getDayOfWeek();
            String weekStr = null;
            switch (weekIndex) {
                case 1:
                    weekStr = "一";
                    break;
                case 2:
                    weekStr = "二";
                    break;
                case 3:
                    weekStr = "三";
                    break;
                case 4:
                    weekStr = "四";
                    break;
                case 5:
                    weekStr = "五";
                    break;
                case 6:
                    weekStr = "六";
                    break;
                case 7:
                    weekStr = "日";
                    break;
                default:
                    break;
            }
            String dateStr = dateToStr(date, YYYY_MM_DD);
            list.add("星期" + weekStr + "（" + dateStr + "）");
        }
        return list;
    }

    public static Map<Integer, Date> dateToMap(Date[] dates) {
        Map<Integer, Date> dateMap = new HashMap<Integer, Date>();
        if (null == dates) {
            return dateMap;
        }
        for (Date date : dates) {
            if (null == date) {
                continue;
            }
            int weekIndex = new DateTime(date).getDayOfWeek();
            dateMap.put(weekIndex, date);
        }
        return dateMap;
    }

    /**
     * 计算某个时间指定天数后（前）的时间值
     * @param beginTime 开始时间
     * @param day 天数，如果为正，代表计算开始时间N天后的时间；为负代表开始时间N天前的时间
     * @return 指定天数后（前）的时间值
     */
    public static Date addDay(Date beginTime, int day) {
        return new DateTime(beginTime).plusDays(day).toDate();
    }

    public static Date addSeconds(int seconds) {
        return addSeconds(new Date(), seconds);
    }

    public static Date addSeconds(Date beginTime, int seconds) {
        return new DateTime(beginTime).plusSeconds(seconds).toDate();
    }

    public static Date addWeek(Date beginTime, int week) {

        if (0 == week) {
            return beginTime;
        }
        return new DateTime(beginTime).plusWeeks(week).toDate();
    }

    /**
     * 计算某个时间指定天数后（前）的时间值
     * @param beginTime 开始时间
     * @param minute 分钟天数
     * @return 指定分钟数后（前）的时间值
     */
    public static Date addMinute(Date beginTime, int minute) {
        if (0 == minute || minute >= 60) {
            return beginTime;
        }
        return new DateTime(beginTime).plusMinutes(minute).toDate();
    }

    public static Date addYear(Date beginTime, int year) {
        if (0 == year) {
            return beginTime;
        }
        return new DateTime(beginTime).plusYears(year).toDate();
    }

    public static Date addMonth(Date beginTime, int month) {
        if (0 == month) {
            return beginTime;
        }
        return new DateTime(beginTime).plusMonths(month).toDate();
    }

    public static Date getUserSetYearMonthFirstDay(int year, int month) {
        return new DateTime().withYear(year).withMonthOfYear(month).dayOfMonth().withMinimumValue().withHourOfDay(0)
                .withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    public static Date getUserSetYearMonthFirstDay(Date date) {
        DateTime dt = new DateTime(date);
        return getUserSetYearMonthFirstDay(dt.getYear(), dt.getMonthOfYear());
    }

    /**
     * 获取 指定年月的开始时间：格式有：yyyyMM,yyyy-MM
     * @param
     * @return
     */
    public static Date getUserSetYearMonthFirstDay(String yearMonth) {
        return DateTime.parse(yearMonth).plusDays(0).withMillisOfDay(0).toDate();
    }

    public static Integer getUserSetYearMonthFirstDayInt(String yearMonth) {
        int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(4, yearMonth.length()));
        return getYYYYMMDD(getUserSetYearMonthFirstDay(year,month));
    }

    public static Date getUserSetYearMonthLastDay(String yearMonth) {
        int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(4, yearMonth.length()));
        return getUserSetYearMonthLastDay(year,month);
    }

    public static Integer getUserSetYearMonthLastDayInt(String yearMonth) {
        return getYYYYMMDD(getUserSetYearMonthLastDay(yearMonth));
    }

    public static Date getUserSetYearMonthLastDay(int year, int month) {
        return new DateTime().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue().withHourOfDay(23)
                .withMinuteOfHour(59).withSecondOfMinute(59).toDate();
    }

    public static Date getUserSetYearMonthLastDay(Date date) {
        DateTime dt = new DateTime(date);
        return getUserSetYearMonthLastDay(dt.getYear(), dt.getMonthOfYear());
    }

    /**
     * 获取当前24小时制的时间
     */
    public static int getCurrentHourOfDay() {
        return DateTime.now().getHourOfDay();
    }

    /**
     * 获取指定时间24小时制的小时
     */
    public static int getCurrentHourOfDay(Date date) {
        return new DateTime(date).getHourOfDay();
    }

    /**
     * 计算当前时间指定天数后的时间值，例如，当前时间为 2008-9-28号 要获得上一个星期的时间，则day为-7，依次类推，查询当前的时间前后均可
     * 注意：获得过去时间,加(-day)
     * @param day 天数，如果为正，代表计算开始时间N天后的时间；为负代表开始时间N天前的时间
     * @return 指定天数后（前）的时间值
     */
    public static Date addDay(int day) {
        return DateTime.now().plusDays(day).toDate();
    }

    public static Timestamp addDaysByTime(int day) {
        if (0 == day) {
            return new Timestamp(System.currentTimeMillis());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        return new Timestamp(calendar.getTime().getTime());

    }

    /**
     * 获取当前号数
     */
    public static Integer getCurrentDay() {
        return getCurrentDay(new Date());
    }

    public static Integer getCurrentDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 获取当前周
     * @description ：注意这里获取的周，是按周日为第一天计算的周，注意！！！！！！！！！！！！！！ add by dubiaopei
     */
    public static Integer getCurrentWeek() {
        return getCurrentWeek(new Date());
    }

    /**
     * @description ：注意这里获取的周，是按周日为第一天计算的周，注意！！！！！！！！！！！！！！ add by dubiaopei
     */
    public static Integer getCurrentWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 计算工作日的时间 当前时间+dayNumber就多少时间，除掉周末（周一 ~ 周五）
     * @param dayNumber
     * @return
     */
    public static Date callWorkday(int dayNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DATE);
        int tempDay = day;
        // 最后要相加的天数
        int lastDay = 0;
        if (dayNumber > 0) {
            for (int i = 1; i <= dayNumber; i++) {
                calendar.set(Calendar.DATE, i);
                Date newDate = calendar.getTime();
                // 获得指定时间的星期数，星期日：1，星期一：2...
                int weekday = calendar.get(Calendar.DAY_OF_WEEK);
                if (weekday == 1 || weekday == 7) {
                    ++lastDay;
                }
                calendar.setTime(newDate);
            }
            int sumDay = dayNumber + lastDay;
            calendar.set(Calendar.DATE, tempDay);
            calendar.add(Calendar.DATE, sumDay);
        }
        return calendar.getTime();

    }

    /**
     * @Title: strToDate
     * @Description:将日期字符串解析为java.util.Date对象
     * @param dateStr
     * @return Date
     */
    public static Date strToDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        return DateTime.parse(dateStr).toDate();
    }

    /**
     * 通过制定的格式，将日期字符串解析为java.util.Date对象
     * @param dateStr 日期字符串
     * @param formatStr 解析的格式
     * @return 转换后的结果：Date对象
     * @throws ParseException
     */
    public static Date strToDate(String dateStr, String formatStr) {
        Date date = null;
        if (dateStr != null && !"".equals(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return date;
    }

    /**
     * 通过制定的格式，将Date对象格式化为字符串
     * @param date 需要转换的Date对象
     * @param formatStr 转换的格式
     * @return 转换之后的字符串
     */
    public static String dateToStr(Date date, String formatStr) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).toString(formatStr);
    }

    public static String dateToStr(Long time, String formatStr) {
        if (null == time) {
            return null;
        }
        return new DateTime(time).toString(formatStr);
    }

    /**
     * 通过制定的格式，将Date对象格式化为字符串
     * @param date 需要转换的Date对象
     * @param formatStr 转换的格式
     * @return 转换之后的字符串
     */
    public static String dateToStr(Date date, String formatStr, String defaultValue) {
        if (null == date) {
            return defaultValue;
        }
        return new DateTime(date).toString(formatStr);
    }

    /***
     * 获取当前的中文日期:yyyy年MM月dd日
     */
    public static String chineseDate() {
        return chineseDate(new Date());
    }

    public static String chineseDate(Date date) {
        return dateToStr(date, CHINESEYYYYMMDD);
    }

    public static String chineseMMDD(Long time) {
        return dateToStr(time, CHINESEMMDD);
    }

    public static String chineseDD(Long time) {
        return dateToStr(time, CHINESEDD);
    }

    /**
     * 获取设定时间的中文日期:yyyy年MM月dd日
     */
    public static String getUserSetChineseDate(Date date, int day) {
        Date beforeDay = addDay(date, day);
        return chineseDate(beforeDay);
    }

    /***
     * 获取当前的中文日期:yyyy-MM-dd hh:mm:ss
     */
    public static String currentDateTime() {
        return dateToStr(new Date(), YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateToStrYMD(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).toString(YYYY_MM_DD);
    }

    public static String dateToStrYMDHMS(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).toString(YYYY_MM_DD_24HH_MM_SS);
    }

    public static String dateToStrMMDD(DateTime date) {
        if (null == date) {
            return null;
        }
        return date.toString(C_MM_DD);
    }

    /**
     * 比较时间的Time
     * @param d1
     * @param d2
     * @return
     */
    public static int compareDate(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            return 0;
        } else {
            if (d1 == null) {
                return -1;
            }
            if (d2 == null) {
                return 1;
            }
            if (d1.getTime() == d2.getTime()) {
                return 0;
            }
            return d1.getTime() > d2.getTime() ? 1 : -1;
        }
    }

    /**
     * 日期比较，忽略时分秒
     */
    public static int compareIgnoreTime(Date date_1, Date date_2) {
        if (null == date_1 || null == date_2) {
            throw new RuntimeException("至少一个时间为空");
        }
        return new DateTime(date_1).withMillisOfDay(0).compareTo(new DateTime(date_2).withMillisOfDay(0));
    }

    /**
     * @author dubiaopei
     * @date 2015年7月27日下午6:01:01
     * @return int
     * @description 比较指定两个时间所属周的大小
     */
    public static int compareWeek(Date d1, Date d2) {
        int year1 = DateUtils.getYear(d1);
        int year2 = DateUtils.getYear(d2);
        if (year1 != year2) {
            return year1 - year2;
        }

        int week1 = DateUtils.getWeekNumberOfYear(d1);
        int week2 = DateUtils.getWeekNumberOfYear(d2);
        return week1 - week2;
    }

    /**
     * @author dubiaopei
     * @date 2015年7月28日下午2:50:47
     * @return int
     * @description 获取指定日期所在年的周数，以周一为周的第一天考虑
     */
    public static int getWeekNumberOfYear(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        return dateTime.getWeekOfWeekyear();
    }

    /**
     * 通过制定的格式，将time格式化为字符串
     * @param time 需要转换的time值
     * @param formatStr 转换的格式
     * @return 转换之后的字符串
     */
    public static String longToStr(Long time, String formatStr) {
        String result = null;
        if (time != null && time != 0) {
            result = dateToStr(new Date(time), formatStr);
        }
        return result;
    }

    /**
     * 工具类：根据季度值获取时间范围
     * @param year
     * @param quarter
     * @return
     */
    public static String getDateStrScale(int year, int quarter) {
        String dayStart = "";
        String dayEnd = "";
        String y = year + "-";
        switch (quarter) {
            case 1:
                dayStart = "01-01";
                dayEnd = "03-31";
                break;
            case 2:
                dayStart = "04-01";
                dayEnd = "06-30";
                break;
            case 3:
                dayStart = "07-01";
                dayEnd = "09-30";
                break;
            case 4:
                dayStart = "10-01";
                dayEnd = "12-31";
                break;
            default:
                dayStart = "01-01";
                dayEnd = "03-31";
                break;
        }
        String result = y + dayStart + "," + y + dayEnd;
        return result;
    }

    /**
     * 获取季度的开始时间
     * @param year
     * @param quarter
     * @return
     */
    public static Timestamp getStartTimeOfQuarter(int year, int quarter) throws ParseException {
        String result = getDateStrScale(year, quarter);
        String startTimeStr = result.split(",")[0] + START_HHMMSS;
        return new Timestamp(strToDate(startTimeStr, YYYY_MM_DD_24HH_MM_SS).getTime());
    }

    /**
     * 获取季度的结束时间
     * @param year
     * @param quarter
     * @return
     */
    public static Timestamp getEndTimeOfQuarter(int year, int quarter) throws ParseException {
        String result = getDateStrScale(year, quarter);
        String endTimeStr = result.split(",")[1] + END_HHMMSS;
        return new Timestamp(strToDate(endTimeStr, YYYY_MM_DD_24HH_MM_SS).getTime());
    }

    /**
     * 工具类：根据当前月份返回季度值
     * @param month
     * @return
     */
    public static int getQuarterByMonth(int month) {
        int quarter = 0;
        if(1 <= month && month <= 3){
            quarter = 1;
        }else if(4 <= month && month <= 6){
            quarter = 2;
        }else if(7 <= month && month <= 9){
            quarter = 3;
        }else if(10 <= month && month <= 12){
            quarter = 4;
        }
        return quarter;
    }

    /**
     * 根据时间获取该时间在一年中所处的周数
     * @param date
     * @return
     * @deprecated 千万不要用啊，这个写法永远只能知道当前时间所属周，太坑了。。。。请用getCurrentWeek方法。comment by
     *             dubiaopei
     */
    public static int getWeekOfYearByDate(Date date) {
        return DateTime.now().getWeekOfWeekyear();
    }

    /**
     * 获取指定日期所处一周的开始时间
     * @param date
     * @return
     * @comment:注意这里周的第一天为星期一 ,add by dubiaopei
     */
    public static Date getFristDayOfWeek(Date date) {
        return new DateTime(date).dayOfWeek().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0)
                .withSecondOfMinute(0).toDate();
    }

    /**
     * 获取指定日期所处一周的结束时间
     * @param date
     * @return
     * @comment:注意这里周的第一天为星期一 ,add by dubiaopei
     */
    public static Date getEndDayOfWeek(Date date) {
        return new DateTime(date).dayOfWeek().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59)
                .withSecondOfMinute(59).toDate();
    }

    /**
     * 获取指定日期所处月份 的开始时间
     * @param date
     * @return
     */
    public static Date getFristDayOfMonth(Date date) {
        return new DateTime(date).dayOfMonth().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0)
                .withSecondOfMinute(0).toDate();
    }

    /**
     * 获取指定日期月份 的结束时间
     * @param date
     * @return
     */
    public static Date getEndDayOfMonth(Date date) {
        return new DateTime(date).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59)
                .withSecondOfMinute(59).toDate();
    }

    /**
     * 得到当前时间是星期? 星期四=4...
     */
    public static int getDayOfWeek() {
        return getDayOfWeek(new Date());
    }

    /**
     * 得到指定时间是星期数 星期四=4...
     */
    public static int getDayOfWeek(Date date) {
        return new DateTime(date).getDayOfWeek();
    }

    /**
     * 判断当前时间是否是工作日(非周六,周日)
     */
    public static boolean isWorkDay(Date date) {
        int weekIndex = DateUtils.getDayOfWeek(date);
        if (weekIndex != 6 && weekIndex != 7) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定时间所在一周的结束时间
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        return new DateTime(date).dayOfWeek().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59)
                .withSecondOfMinute(59).toDate();
    }

    /**
     * 获取指定时间的年份
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        return new DateTime(date).getYear();
    }

    /**
     * 获取当前时间的年份
     * @return
     */
    public static Integer getCurrentYear() {
        return getYear(new Date());
    }

    /**
     * 获取指定时间的月份
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        return new DateTime(date).getMonthOfYear();
    }

    /**
     * 获取当前月
     * @return
     */
    public static Integer getCurrentMonth() {
        return getMonth(new Date());
    }

    /**
     * 校验查询日报有效时间
     * @param date
     */
    public static Date getValidDate(Date date) {
        Date nowDate = new Date();
        // 判断季度
        if (date == null || date.after(getLastDayOfWeek(nowDate))) {
            date = nowDate;
        }
        return getFristDayOfWeek(date);
    }

    /**
     * 将周报时间String转换成Timestamp
     */
    public static Timestamp getTimestampFromStr(String dateStr) {
        Timestamp rsTime = null;
        Date rsDate = strToDate(dateStr, YYYY_MM_DD);
        rsTime = new Timestamp(rsDate.getTime());
        return rsTime;
    }

    /**
     * 根据指定日期查询可以查询的周数,默认获取指定时间之前7周
     * @param queryDate
     * @return
     */
    public static Map<String, String> showWeekByDate(Date queryDate) {
        Map<String, String> weekMap = new TreeMap<String, String>();
        // 获取当前时间一周的开始时间
        Date nowDate = getFristDayOfWeek(new Date());
        // 获取指定时间一周的开始时间
        Date queryFristDate = getFristDayOfWeek(queryDate);

        // 获取之前4周的周信息
        for (int wk = 0; wk < 4; wk++) {
            Date fristDayOfWeek = addDay(queryFristDate, -wk * DAY_OF_WEEK);
            String dateKey = getUserSetStartDateToStr(fristDayOfWeek, 0).substring(0, 11);
            String dateValue = getYear(fristDayOfWeek) + "年第" + getWeekOfYearByDate(fristDayOfWeek) + "周";
            dateValue += "(" + getUserSetStartDateToStr(fristDayOfWeek, 0).substring(0, 11) + "~"
                    + getUserSetEndDateToStr(fristDayOfWeek, DAY_OF_WEEK - 1).substring(0, 11) + ")";
            weekMap.put(dateKey, dateValue);
        }
        // 获取之后有效的周信息
        while (queryFristDate.before(getLastDayOfWeek(nowDate))) {
            String dateKey = getUserSetStartDateToStr(queryFristDate, 0).substring(0, 11);
            String dateValue = getYear(queryFristDate) + "年第" + getWeekOfYearByDate(queryFristDate) + "周";
            dateValue += "(" + getUserSetStartDateToStr(queryFristDate, 0).substring(0, 11) + "~"
                    + getUserSetEndDateToStr(queryFristDate, DAY_OF_WEEK - 1).substring(0, 11) + ")";
            weekMap.put(dateKey, dateValue);
            queryFristDate = addDay(queryFristDate, DAY_OF_WEEK);
        }

        return weekMap;
    }

    /***
     * 计算二个日期之内的年周,格式为：
     */
    public static Map<String, String> callTwoDatTimeYearWeek(Date startTime, Date endTime) {
        Map<String, String> yearWeekMap = new LinkedHashMap<String, String>();
        yearWeekMap.putAll(getYearWeek(startTime));
        Date _startTime = startTime;
        while (true) {
            Date addWeek = addWeek(_startTime, 1);
            if (addWeek.after(endTime)) {
                break;
            }
            yearWeekMap.putAll(getYearWeek(addWeek));
            _startTime = addWeek;
        }
        return yearWeekMap;
    }

    public static Map<String, String> getYearWeek(Date dateTime) {
        Map<String, String> yearWeekMap = new HashMap<String, String>();
        Map<String, String> map = getYearWeekMap(dateTime);
        String year = map.get("year");
        String week = map.get("week");
        /*
         * if(null == week || 0 == week){ week = 1; }
         */
        String key = String.valueOf(year) + week;
        String value = getYearWeek(year, week);
        yearWeekMap.put(key, value);
        return yearWeekMap;
    }

    /**
     * 年 2016
     * @return 设置年所有周
     */
    public static Map<String, String> callUserSetYearWeek(Integer year) {
        DateTime dateTime = new DateTime(year,1,1,0,0,0);
        return callTwoDatTimeYearWeek(dateTime.dayOfYear().withMinimumValue().toDate(),dateTime.dayOfYear().withMaximumValue().toDate());
    }

    public static String getYearWeek(String year, String week) {
        return year + "年" + (Integer.parseInt(week)) + "周";
    }


    /**
     * @author cdjinwei
     * @param t1
     * @param t2
     * @return
     */
    public static int compare(Timestamp t1, Timestamp t2) {
        try {
            if (t1 == null && t2 == null) {
                return 0;
            } else {
                if (t1 == null) {
                    return -1;
                }
                if (t2 == null) {
                    return 1;
                }
                SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_24HH_MM_SS);
                String str1 = format.format(t1);
                String str2 = format.format(t2);

                Date d1 = format.parse(str1);
                Date d2 = format.parse(str2);
                return d1.compareTo(d2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取当前的开始时间
     * @param millis
     * @return
     */
    public static long getStartTime(long millis) {
        return new DateTime(millis).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
                .getMillis();
    }

    public static Long getStartTime(Date date) {
        if (null == date) {
            return null;
        }
        return getStartTime(date.getTime());
    }

    public static Date getDateStart(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
                .toDate();
    }

    public static Timestamp getStartTimestamp(Timestamp date) {
        if (null == date) {
            return null;
        }
        return new Timestamp(getStartTime(date.getTime()));
    }

    /**
     * 获取当前的结束时间
     * @param millis
     * @return
     */
    public static long getEndTime(long millis) {
        return new DateTime(millis).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(
                999).getMillis();
    }

    public static Long getTimeEnd(Date date) {
        if (null == date) {
            return null;
        }
        return getEndTime(date.getTime());
    }

    public static Date getDateEnd(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999)
                .toDate();
    }

    public static Timestamp getEndTimestamp(Timestamp date) {
        if (null == date) {
            return null;
        }
        return new Timestamp(getEndTime(date.getTime()));
    }

    public static Timestamp getEndTimestamp(Date date) {
        if (null == date) {
            return null;
        }
        return getEndTimestamp(new Timestamp(date.getTime()));
    }

    /**
     * 获取当前月的开始时间
     * @return Date
     * @author xiehuixia
     * @date 2014年10月31日下午2:05:46
     */
    public static Date getCurrentMonthStart() {
        return DateTime.now().withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
                .withMillisOfSecond(0).toDate();
    }

    /**
     * 获取当前月结束时间
     * @return
     */
    public static Date getCurrentMonthEnd() {
        return DateTime.now().dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59)
                .withSecondOfMinute(59).withMillisOfSecond(999).toDate();
    }

    /**
     * 获取指定月开始时间
     * @param date
     * @return
     */
    public static Date getMonthStart(Date date) {
        return new DateTime(date).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
                .withMillisOfSecond(0).toDate();
    }

    /**
     * 获取指定月结束时间
     * @param date
     * @return
     */
    public static Date getMonthEnd(Date date) {
        return new DateTime(date).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59)
                .withSecondOfMinute(59).withMillisOfSecond(999).toDate();
    }

    /**
     * 是否为同年月
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameMonth(long time1, long time2) {
        Date date1 = getMonthStart(new Date(time1));

        Date date2 = getMonthStart(new Date(time2));

        if (0 == date1.compareTo(date2)) {
            return true;
        }
        return false;
    }

    /**
     * 昨天结束时间
     * @return
     */
    public static long getYesterdayEndTime() {
        return DateTime.now().minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
                .withMillisOfSecond(999).getMillis();
    }

    public static Timestamp getYesterdayEndTimestamp() {
        return new Timestamp(getYesterdayEndTime());
    }

    public static Date getYesterdayEndDate() {
        return DateTime.now().minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
                .withMillisOfSecond(999).toDate();
    }

    /**
     * 比较二个时间的大小（只比较年月日） 返回-2说明数据有问题 返回值说明： 返回 1 说明第一个时间比第二个时间大 返回 0
     * 说明第一个时间等于第二个时间 返回 -1 说明第一个时间比第二个时间小
     */
    public static int compareDateByYMD(Date startTime, Date endTime) {
        int compare = -2;
        if (null == startTime || null == endTime) {
            return compare;
        }
        DateTime start = new DateTime(startTime);
        DateTime end = new DateTime(endTime);
        return start.toLocalDate().compareTo(end.toLocalDate());
    }

    /**
     * 找出二个时间内的所有年周集合
     */
    public static List<String> getYearWeekByTwoDateTime(Date startTime, Date endTime) {
        List<String> yearWeeks = new ArrayList<String>();
        Date firstStartDt = getFristDayOfWeek(startTime);
        Date lastEndDt = getLastDayOfWeek(endTime);
        DateTime startDt = new DateTime(firstStartDt);
        DateTime endDt = new DateTime(lastEndDt);
        if (startDt.isBefore(endDt)) {
            while (startDt.isBefore(endDt)) {
                yearWeeks.add(getYearWeekStr(startDt));
                startDt = startDt.plusWeeks(1);
            }
        }
        return yearWeeks;

    }

    /**
     * Dashboard 使用
     */
    public static List<String> getYearWeekInDateTimes(Date startTime, Date endTime) {
        List<String> yearWeeks = new ArrayList<String>();
        String startYearWeek = getYearWeekStr(startTime);
        String endYearWeek = getYearWeekStr(endTime);
        Date firstStartDt = getStartDateByYearWeek(Integer.parseInt(startYearWeek.substring(0, 4)),Integer.parseInt(startYearWeek.substring(4, 6)));
        Date lastEndDt = getEndDateByYearWeek(Integer.parseInt(endYearWeek.substring(0, 4)),Integer.parseInt(endYearWeek.substring(4, 6)));
        DateTime startDt = new DateTime(firstStartDt);
        DateTime endDt = new DateTime(lastEndDt);
        if (startDt.isBefore(endDt)) {
            while (startDt.isBefore(endDt)) {
                yearWeeks.add(getYearWeekStr(startDt));
                startDt = startDt.plusWeeks(1);
            }
        }
        return yearWeeks;

    }

    /**
     * 获取指定日期的指定年份和周数（解决了跨年问题）
     */
    public static Map<String, String> getYearWeekMap(Date date) {
        Map<String, String> yearWeekMap = new HashMap<String, String>();
        if (null == date) {
            return yearWeekMap;
        }
        DateTime dt = new DateTime(date);
        yearWeekMap.put("year", String.valueOf(dt.getWeekyear()));
        int week = dt.getWeekOfWeekyear();
        yearWeekMap.put("week", week < 10 ? "0" + week : String.valueOf(week));
        return yearWeekMap;
    }

    public static String getCurrYearWeekStr() {
        return getYearWeekStr(new Date());
    }

    /**
     * 获取指定日期的指定年份周数,如：201201
     */
    public static String getYearWeekStr(Date date) {
        if (null == date) {
            return null;
        }
        return getYearWeekStr(date.getTime());
    }

    /**
     * 周用此方法
     * @return
     */
    public static Integer getYearWeekInt(Date date) {
        String weekStr = getYearWeekStr(date);
        return weekStr == null ? null : Integer.parseInt(weekStr);
    }

    /**
     * Dashboard周用此方法
     * @return
     */
    public static String getYearWeekStr(Long time) {
        if (null == time) {
            return null;
        }
        return getYearWeekStr(new DateTime(time));
    }

    public static Integer getYearWeekInt(Long time) {
        String weekStr = getYearWeekStr(time);
        return weekStr == null ? null : Integer.parseInt(weekStr);
    }

    /**
     * Dashboard周用此方法
     * @return
     */
    public static Integer getWeekInt(Date date){
        if (null == date) {
            return null;
        }
        DateTime dt = new DateTime(date);
        return dt.getWeekOfWeekyear();
    }

    /**
     * 获取指定日期的指定年份周数,如：2012年01周
     */
    public static String getYearWeekChineseStr(Date date) {
        if (null == date) {
            return null;
        }
        DateTime dt = new DateTime(date);
        int year = dt.getWeekyear();
        int week = dt.getWeekOfWeekyear();
        return year + "年第" + (week < 10 ? "0" + week : week) + "周";
    }

    public static String getYearWeekStr(DateTime dt) {
        int year = dt.getWeekyear();
        int week = dt.getWeekOfWeekyear();
        return year + "" + (week < 10 ? "0" + week : week);
    }

    public static String getYearMonth(Date day) {
        return dateToStr(day, YYYYMM);
    }

    /**
     * 获取指定年月在指定月份后的年月值，如距201505隔-1个月的年月值是201504
     * @author xiehuixia
     * @date 2015年5月14日上午11:20:56
     * @param yearMonth
     * @param month
     * @throws ParseException
     */
    public static String getYearMonth(String yearMonth, int month) throws ParseException {
        if (StringUtils.isBlank(yearMonth)) {
            return null;
        }
        Date day = DateUtils.strToDate(yearMonth, DateUtils.YYYYMM);
        return new DateTime(day).plusMonths(month).toString(DateUtils.YYYYMM);
    }

    /**
     * 获取年周的开始时间（以周一开始）
     */
    public static Date getStartDateByYearWeek(int year, int week) {
        DateTime dateTime = new DateTime().withWeekyear(year).withWeekOfWeekyear(week);
        return getDateStart(dateTime.dayOfWeek().withMinimumValue().toDate());
    }

    public static Date getCurrentStartDateByYearWeek(Date date) {
        DateTime dateTime = new DateTime(date);
        return getDateStart(dateTime.dayOfWeek().withMinimumValue().toDate());
    }

    public static Date getCurrentStartDateByYearWeek() {
        DateTime dateTime = DateTime.now();
        return getDateStart(dateTime.dayOfWeek().withMinimumValue().toDate());
    }

    /**
     * 获取年周的结束时间（以周末结束）
     */
    public static Date getEndDateByYearWeek(int year, int week) {
        DateTime dateTime = new DateTime().withWeekyear(year).withWeekOfWeekyear(week);
        return getDateEnd(dateTime.dayOfWeek().withMaximumValue().toDate());
    }

    public static Date getCurrentEndDateByYearWeek(Date date) {
        DateTime dateTime = new DateTime(date);
        return getDateStart(dateTime.dayOfWeek().withMaximumValue().toDate());
    }

    /**
     * 获取年周的开始时间(startTime)，结束时间(endTime)
     */
    public static Map<String, Date> getWeekStartEndTimeByYearWeek(int year, int week) {
        Map<String, Date> weekMap = new HashMap<String, Date>();
        Date startTime = getStartDateByYearWeek(year, week);
        Date endTime = getEndDateByYearWeek(year, week);
        weekMap.put("startTime", startTime);
        weekMap.put("endTime", endTime);
        return weekMap;
    }

    /**
     * @param yearWeek
     * @param isMonday true:从周一开始,false:周日开始
     * @return
     */
    public static Map<String, Date> getWeekStartEndTimeByYearWeek(String yearWeek, boolean isMonday) {
        Map<String, Date> weekMap = new HashMap<String, Date>();
        int year = Integer.parseInt(yearWeek.substring(0, 4));
        int week = Integer.parseInt(yearWeek.substring(4));
        Date startTime = getStartDateByYearWeek(year, week);
        Date endTime = getEndDateByYearWeek(year, week);
        if (!isMonday) {
            startTime = addDay(startTime, -1);
            endTime = addDay(endTime, -1);
        }
        weekMap.put("startTime", startTime);
        weekMap.put("endTime", endTime);
        return weekMap;
    }

    /**
     * 获取指定年周的上一天时间（相当于上一周的最后一天日期）
     */
    public static Date getLastDayByYearWeek(int year, int week) {
        // 获取指定年周的开始时间（周一的时间）
        Date startTime = getStartDateByYearWeek(year, week);
        return new DateTime(startTime).minusDays(1).toDate();
    }

    /**
     * 获取指定时间的最大时间 （yyyy-MM-dd HH:mm:ss）
     */
    public static String getMaxDateStr() {
        return getMaxDateStr(new Date());
    }

    public static String getMaxDateStr(Date date) {
        return getMaxDateTime(date).toString(YYYY_MM_DD_24HH_MM_SS);
    }

    private static DateTime getMaxDateTime(Date date) {
        return /* new LocalDate(date).toDateTimeAtStartOfDay().minusSeconds(1) */
                new DateTime(date).millisOfDay().withMaximumValue();
    }

    public static Date getMaxDate(Date date) {
        return getMaxDateTime(date).toDate();
    }

    /**
     * 获取指定时间的最小时间 （yyyy-MM-dd HH:mm:ss）
     */
    public static String getMinDateStr() {
        return getMinDateStr(new Date());
    }

    public static String getMinDateStr(Date date) {
        return getMinDateTime(date).toString(YYYY_MM_DD_24HH_MM_SS);
    }

    private static DateTime getMinDateTime(Date date) {
        return new LocalDate(date).toDateTimeAtStartOfDay();
    }

    public static Date getMinDate(Date date) {
        return getMinDateTime(date).toDate();
    }

    /**
     * 获取指定时间的最大时间
     * @param date 日期
     * @param day 天数 正数表示时间向前推，负数表示时间向后推
     */
    public static Date getMaxDateByDay(Date date, int day) {
        return getMaxDateTime(date).minusDays(day).toDate();
    }

    /**
     * 默认为当前时间（取决于day的正负数取值）
     * @param day 天数 正数表示时间向前推，负数表示时间向后推
     */
    public static Date getMaxDateByDay(int day) {
        return getMaxDateByDay(new Date(), day);
    }

    /**
     * 获取当前时间的最大时间
     */
    public static Date getMaxDateByDay() {
        return getMaxDateByDay(new Date(), 0);
    }

    public static String getMaxDateByDayStr(Date date, int day) {
        return getMaxDateTime(date).minusDays(day).toString(YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取指定时间的最小时间
     * @param date 日期
     * @param day 天数 正数表示时间向前推，负数表示时间向后推
     */
    public static Date getMinDateByDay(Date date, int day) {
        return getMinDateTime(date).minusDays(day).toDate();
    }

    /**
     * 获取当前时间的最小时间
     * @param day 天数 正数表示时间向前推，负数表示时间向后推
     */
    public static Date getMinDateByDay(int day) {
        return getMinDateByDay(new Date(), day);
    }

    /**
     * 获取当前时间的最小时间
     */
    public static Date getMinDateByDay() {
        return getMinDateByDay(new Date(), 0);
    }

    public static String getMinDateByDayStr(Date date, int day) {
        return getMinDateTime(date).minusDays(day).toString(YYYY_MM_DD_24HH_MM_SS);
    }

    /**
     * 获取当前时间的周数
     */
    public static int getWeeekByDate() {
        return getWeeekByDate(new Date());
    }

    public static int getWeeekByDate(Date date) {
        return new DateTime(date).dayOfWeek().get();
    }

    public static Date getWeekStart(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).dayOfWeek().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0)
                .withSecondOfMinute(0).withMillisOfSecond(0).toDate();
    }

    public static Date getWeekEnd(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).dayOfWeek().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59)
                .withSecondOfMinute(59).withMillisOfSecond(999).toDate();
    }

    public static boolean isBefore(Date startDate, Date endDate) {
        return new DateTime(startDate).isBefore(new DateTime(endDate));
    }

    /**
     * 时间是否在当前时间之前
     */
    public static boolean isBefore(Date startDate) {
        return new DateTime(startDate).isBefore(new DateTime(new Date()));
    }

    /**
     * 注意：只能是：yyyy-MM-dd格式
     */
    public static boolean isBefore(String startDateStr) {
        return new DateTime(startDateStr).isBefore(new DateTime(new Date()));
    }

    public static boolean isBefore(String startDateStr, String endDateStr) {
        return new DateTime(startDateStr).isBefore(new DateTime(endDateStr));
    }

    public static Date strToStartTime(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        return getDateStart(strToDate(dateStr));
    }

    public static Date strToEndTime(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        return getDateEnd(strToDate(dateStr));
    }

    public static Date toStartTime(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
                .toDate();
    }

    public static Date toEndTime(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999)
                .toDate();
    }

    /**
     * 获取当前年份开始的时间: 2014-01-01 00:00:00
     * @param date
     * @return
     */
    public static Date toYearStartTime(Date date) {
        return new DateTime(null == date ? new Date() : date).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(0)
                .withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
    }

    /**
     * 获取当前年份结束的时间: 2014-12-31 23:59:59
     * @param date
     * @return
     */
    public static Date toYearEndTime(Date date) {
        if (null == date) {
            return null;
        }
        return new DateTime(null == date ? new Date() : date).withMonthOfYear(12).withDayOfMonth(31).withHourOfDay(23)
                .withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).toDate();
    }

    /**
     * 返回**年**月**日类型
     * @param date
     * @return
     */
    public static String toChineseStr(Date date) {
        if (null == date) {
            return null;
        }
        DateTime jodaTime = new DateTime(date);
        return jodaTime.getYear() + "年" + jodaTime.getMonthOfYear() + "月" + jodaTime.getDayOfMonth() + "日";
    }

    /**
     * 获取当前中文的星期?
     */
    public static String getWeekNumChina(int year, int month, int day) {
        int weekDay = new DateTime().withDate(year, month, day).dayOfWeek().get();
        return DateUtils.WEEK_CHINE_MAP.get(weekDay);
    }

    public static String getWeekNumChina(long time) {
        int weekDay = new DateTime(time).dayOfWeek().get();
        return DateUtils.WEEK_CHINE_MAP.get(weekDay);
    }

    public static String getWeekNumSimplateChina(long time) {
        int weekDay = new DateTime(time).dayOfWeek().get();
        return DateUtils.WEEK_CHINE_SIMPLATE_MAP.get(weekDay);
    }

    public static String getWeekNumSimplateChina(DateTime dateTime) {
        int weekDay = dateTime.dayOfWeek().get();
        return DateUtils.WEEK_CHINE_SIMPLATE_MAP.get(weekDay);
    }

    public static String getWeekNumMostSimpleChina(long time) {
        int weekDay = new DateTime(time).dayOfWeek().get();
        return DateUtils.WEEK_CHINE_SIMPLATEST_MAP_.get(weekDay);
    }

    public static String getWeekNumMostSimpleChina(DateTime dateTime) {
        int weekDay = dateTime.dayOfWeek().get();
        return DateUtils.WEEK_CHINE_SIMPLATEST_MAP_.get(weekDay);
    }

    /**
     * @author dubiaopei
     * @date 2015年7月28日下午3:17:42
     * @return String
     * @description 获取当前中文的星期?
     */
    public static String getWeekNumChina(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        int weekDay = dateTime.dayOfWeek().get();
        return DateUtils.WEEK_CHINE_MAP.get(weekDay);
    }

    /**
     * 获取当地短时间
     * @param pDate required
     * @return like "今天 11:11","昨天 11:11","11月6日 11:11"
     */
    public static String getSortLocalTime(Date pDate) {
        if (null == pDate) {
            return null;
        }
        long now = DateTime.now().withMillisOfDay(0).getMillis();
        long date = new DateTime(pDate).withMillisOfDay(0).getMillis();
        long days = (now - date) / 86400000;
        // TODO 国际化
        if (0 == days) {
            return "今天 " + DateUtils.dateToStr(pDate, "HH:mm");
        } else if (1 == days) {
            return "昨天 " + DateUtils.dateToStr(pDate, "HH:mm");
        } else {
            return DateUtils.dateToStr(pDate, "MM月dd日 HH:mm");
        }
    }

    /**
     * @author：dubiaopei
     * @date:2015年6月8日上午9:54:39
     * @description: 判断某日期是否为当月第一天
     * @return:boolean
     */
    public static boolean isFirstDayOfMonth(Date date) {
        Date startDate = DateUtils.getFristDayOfMonth(date);
        return DateUtils.isSameDay(startDate, date);
    }

    /**
     * @author dubiaopei
     * @date 2015年7月28日下午2:22:45
     * @return List<Date>
     * @description 返回指定时间对应周的所有日期集合。每天的都是当天的初始时间
     */
    public static List<Date> getDatesOfSpecificWeek(Date dayOfWeek) {
        List<Date> dates = Lists.newArrayList();
        Date startDate = DateUtils.getFristDayOfWeek(dayOfWeek);
        dates.add(startDate);
        for (int index = 1; index < 7; index++) {
            dates.add(DateUtils.addDay(startDate, index));
        }

        return dates;
    }

    /**
     * 获取当地短日期
     * @param pDate required
     * @return like "今天","昨天","11月06日 "
     */
    public static String getSortLocalDate(Date pDate) {
        if (null == pDate) {
            return null;
        }
        long now = DateTime.now().withMillisOfDay(0).getMillis();
        long date = new DateTime(pDate).withMillisOfDay(0).getMillis();
        long days = (now - date) / 86400000;
        // TODO 国际化
        if (0 == days) {
            return "今天";
        } else if (1 == days) {
            return "昨天";
        } else {
            return DateUtils.dateToStr(pDate, "MM月dd日");
        }
    }

    /**
     * 排序日期集合
     * @param dates
     * @return 以升序集合返回
     */
    public static List<Date> sortDate(List<Date> dates) {
        if (CollectionUtils.isNotEmpty(dates)) {
            Collections.sort(dates, new Comparator<Date>() {

                @Override
                public int compare(Date o1, Date o2) {
                    if (null == o1 || null == o2) {
                        return 0;
                    }
                    return o1.compareTo(o2);
                }
            });
        }
        return dates;
    }

    /**
     * 排序日期集合
     * @param dates
     * @return 以升序集合返回
     */
    public static List<Long> sortLong(List<Long> dates) {
        if (CollectionUtils.isNotEmpty(dates)) {
            Collections.sort(dates, new Comparator<Long>() {

                @Override
                public int compare(Long o1, Long o2) {
                    if (null == o1 || null == o2) {
                        return 0;
                    }
                    return o1 > o2 ? 1 : -1;
                }
            });
        }
        return dates;
    }

    /**
     * 获取开始与结束时间的字符串集合
     * @param startTime
     * @param endTime
     * @return 默认返回以：YYYYMMDD的集合
     */
    public static List<String> getDateTimeRange(Date startTime, Date endTime) {
        return getDateTimeRange(startTime, endTime, YYYYMMDD);
    }

    /**
     * 获取开始与结束时间的字符串集合
     * @param startTime
     * @param endTime
     * @param dateformatStr 如为空，默认为：YYYYMMDD
     * @return
     */
    public static List<String> getDateTimeRange(Date startTime, Date endTime, String dateformatStr) {
        List<String> dateFormats = Lists.newArrayList();
        if (StringUtils.isBlank(dateformatStr)) {
            dateformatStr = YYYYMMDD;
        }
        Date dateTime = startTime;
        while (true) {
            if (compareIgnoreTime(dateTime, endTime) == 1) {
                break;
            }
            dateFormats.add(dateToStr(dateTime, dateformatStr));
            dateTime = addDay(dateTime, 1);
        }
        return dateFormats;
    }

    public static List<Date> getDateByTimeRange(Date startTime, Date endTime) {
        List<Date> dates = Lists.newArrayList();
        Date dateTime = startTime;
        while (true) {
            if (compareIgnoreTime(dateTime, endTime) == 1) {
                break;
            }
            dates.add(dateTime);
            dateTime = addDay(dateTime, 1);
        }
        return dates;
    }

    /**
     * 获取指定时间范围内的时间Time集合
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<Long> getDateTimeRangeTimes(Date startTime, Date endTime) {
        List<Long> dateTimes = Lists.newArrayList();
        Date dateTime = startTime;
        while (true) {
            if (compareIgnoreTime(dateTime, endTime) == 1) {
                break;
            }
            dateTimes.add(getDateSimplateLog(dateTime));
            dateTime = addDay(dateTime, 1);
        }
        return dateTimes;
    }

    /**
     * 获取时间的年月日，去除时分秒，格式：2015-11-08 00:00:00
     * @param date
     * @return
     */
    public static DateTime getDateTimeSimplate(Date date) {
        return new DateTime(date).withMillisOfDay(0);
    }

    public static Date getDateSimplate(Date date) {
        return getDateTimeSimplate(date).toDate();
    }

    /**
     * 获取时间的年月日 > 秒数，去除时分秒，格式：2015-11-08 00:00:00
     * @param date
     * @return
     */
    public static long getDateSimplateLog(Date date) {
        return getDateTimeSimplate(date).getMillis();
    }

    public static Date getDateByLog(Long time) {
        return new DateTime(time).toDate();
    }

    public static Date plusHours(int hours) {
        return new DateTime().plusHours(hours).toDate();
    }

    /**
     * 增加分钟数
     * @param minutes
     * @return
     */
    public static Date plusMinutes(int minutes) {
        return new DateTime().plusMinutes(minutes).toDate();
    }

    /**
     * 格式：2015-11-08 00:00:00 转换为：20151108
     * @param statsDay
     * @return
     */
    public static Integer getYearMonthDay(Date statsDay) {
        return Integer.valueOf(new DateTime(statsDay).toString(YYYYMMDD));
    }

    /**
     * 是否为这个月的开始时间
     * @return
     */
    public static boolean isMonthBeginDay() {
        return DAY_OF_MIN == getMonthDay();
    }

    /**
     * 获取这个月的号数
     * @return
     */
    public static int getMonthDay() {
        return new DateTime().getDayOfMonth();
    }

    public static Date max(Date time, Date time2) {
        boolean isEmpty = null == time;
        boolean isEmpty2 = null == time2;
        if (isEmpty && isEmpty2) {
            return null;
        }
        if (isEmpty) {
            return time2;
        }
        if (isEmpty2) {
            return time;
        }
        return time.after(time2) ? time : time2;
    }

    public static Date min(Date time, Date time2) {
        boolean isEmpty = null == time;
        boolean isEmpty2 = null == time2;
        if (isEmpty && isEmpty2) {
            return null;
        }
        if (isEmpty) {
            return time2;
        }
        if (isEmpty2) {
            return time;
        }
        return time.after(time2) ? time2 : time;
    }

    /**
     * @author liuzidong 2016年7月19日 上午10:56:38
     * @Title: differenceSeconds
     * @Description: 二个日期相关秒数
     * @param startTime
     * @param endTime
     * @return  int
     */
    public static int differenceSeconds(Date startTime, Date endTime) {
        Long diff = (new DateTime(startTime).getMillis() - new DateTime(endTime).getMillis()) / 1000;
        return Math.abs(Long.valueOf(diff).intValue());
    }


    // /////////////////////////////test/////////////////////////////////

    static void testApi() {

        DateTime dt = new DateTime();
        // 年
        int year = dt.getYear();
        // 月
        int month = dt.getMonthOfYear();
        // 日
        int day = dt.getDayOfMonth();
        // 星期
        int week = dt.getDayOfWeek();
        // 点
        int hour = dt.getHourOfDay();
        // 分
        int min = dt.getMinuteOfHour();
        // 秒
        int sec = dt.getSecondOfMinute();
        // 毫秒
        int msec = dt.getMillisOfSecond();

        // 星期
        switch (dt.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                System.out.println("星期日");
                break;
            case DateTimeConstants.MONDAY:
                System.out.println("星期一");
                break;
            case DateTimeConstants.TUESDAY:
                System.out.println("星期二");
                break;
            case DateTimeConstants.WEDNESDAY:
                System.out.println("星期三");
                break;
            case DateTimeConstants.THURSDAY:
                System.out.println("星期四");
                break;
            case DateTimeConstants.FRIDAY:
                System.out.println("星期五");
                break;
            case DateTimeConstants.SATURDAY:
                System.out.println("星期六");
                break;
            default:
                break;
        }

        // 昨天
        DateTime yesterday = dt.minusDays(1);
        // 明天
        DateTime tomorrow = dt.plusDays(1);
        // 1个月前
        DateTime before1month = dt.minusMonths(1);
        // 3个月后
        DateTime after3month = dt.plusMonths(3);
        // 2年前
        DateTime before2year = dt.minusYears(2);
        // 5年后
        DateTime after5year = dt.plusYears(5);

        // 月末日期
        DateTime lastday = dt.dayOfMonth().withMaximumValue();

        // 90天后那周的周一
        DateTime firstday = dt.plusDays(90).dayOfWeek().withMinimumValue();

        // 默认设置为日本时间
        DateTimeZone.setDefault(DateTimeZone.forID("Asia/Tokyo"));
        DateTime dt1 = new DateTime();

        // 伦敦时间
        DateTime dt2 = new DateTime(DateTimeZone.forID("Europe/London"));

        DateTime begin = new DateTime("2012-02-01");
        DateTime end = new DateTime("2012-05-01");

        // 计算区间毫秒数
        Duration d = new Duration(begin, end);
        long time = d.getMillis();

        // 计算区间天数
        Period p = new Period(begin, end, PeriodType.days());
        int days = p.getDays();

        // 计算特定日期是否在该区间内
        Interval i = new Interval(begin, end);
        boolean contained = i.contains(new DateTime("2012-03-01"));

        Date monthStart = new DateTime().withYear(year).withMonthOfYear(month).dayOfMonth().withMinimumValue()
                .withMillisOfDay(0).toDate();
        Date monthEnd = new DateTime().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue()
                .withMillisOfDay(0).toDate();
    }

    static void testDateTime() {

        DateTime dateTime1 = DateTime.now();
        String d = dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_);
        System.out.println("当前时间 : " + d);
        DateTime cacheTime = dateTime1;
        // "MM/dd/yyyy hh:mm:ss.SSSa"
        // YYYY_MM_DD_24HH_MM_SS_

        int i = 1;
        dateTime1 = dateTime1.plusDays(30);
        System.out.println((i++) + " : " + dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_));

        dateTime1 = DateTime.now();
        dateTime1 = dateTime1.plusHours(3);
        System.out.println((i++) + " : " + dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_));

        dateTime1 = DateTime.now();
        dateTime1 = dateTime1.plusMinutes(3);
        System.out.println((i++) + " : " + dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_));

        dateTime1 = DateTime.now();
        dateTime1 = dateTime1.plusMonths(2);
        System.out.println((i++) + " : " + dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_));

        dateTime1 = DateTime.now();
        dateTime1 = dateTime1.plusSeconds(4);
        System.out.println((i++) + " : " + dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_));

        dateTime1 = DateTime.now();
        dateTime1 = dateTime1.plusWeeks(5);
        System.out.println((i++) + " : " + dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_));

        dateTime1 = DateTime.now();
        dateTime1 = dateTime1.plusYears(3);
        System.out.println((i++) + " : " + dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_));

        DateTime dt = new DateTime();
        DateTime year2000 = dt.withYear(2000);
        DateTime twoHoursLater = dt.plusHours(2);
        System.out.println(year2000);
        System.out.println(twoHoursLater);
        String monthName = dt.monthOfYear().getAsText();
        System.out.println(monthName);
        System.out.println(dateTime1.toString(YYYY_MM_DD_24HH_MM_SS_));

        DateTime now = new DateTime(new Date());
        System.out.println(">>> " + dateToStr(now.toDate(), YYYY_MM_DD_24HH_MM_SS_));
        DateTime added = now.plus(Period.seconds(6));
        System.out.println(" seconds >>> " + dateToStr(added.toDate(), YYYY_MM_DD_24HH_MM_SS_));
        DateTime added2 = now.plus(Period.millis(6));
        System.out.println(" millis >>> " + dateToStr(added2.toDate(), YYYY_MM_DD_24HH_MM_SS_));
        DateTime added3 = now.plus(Period.minutes(6));
        System.out.println(" minutes >>> " + dateToStr(added3.toDate(), YYYY_MM_DD_24HH_MM_SS_));
        System.out.println(" >>> " + dateToStr(new Date(), YYYY_MM_DD_24HH_MM_SS_));
        System.out.println(" minutes2 >>> " + dateToStr(plusMinutes(6), YYYY_MM_DD_24HH_MM_SS_));

        /**
         *
         2016-04-13 15:23:42.268 1 : 2016-05-13 15:23:42.268 2 : 2016-04-13
         * 18:23:42.292 3 : 2016-04-13 15:26:42.292 4 : 2016-06-13 15:23:42.292
         * 5 : 2016-04-13 15:23:46.292 6 : 2016-05-18 15:23:42.292 7 :
         * 2019-04-13 15:23:42.292 2000-04-13T15:23:42.292+08:00
         * 2016-04-13T17:23:42.292+08:00 四月 2019-04-13 15:23:42.292
         */

    }


    public static void main(String[] args) throws ParseException {

        Date yesterday = DateUtils.getYesterdayEndDate();
        Date d1 = strToDate("2016-07-19 13:43:03.444",YYYY_MM_DD_24HH_MM_SS);
        Date d2 = strToDate("2016-07-19 13:43:26.999",YYYY_MM_DD_24HH_MM_SS);
        Date now = DateUtils.getFristDayOfMonth(new Date());
        Date tew = DateUtils.getUserSetStartDate(now,19);
        Date startDate = DateUtils.addMonth(tew,-2);
        System.out.println(startDate);
        System.out.println(getCurrentDay(yesterday));
        System.out.println(getUserSetYearEnd(yesterday));

        asyncDealWithData(d1,d2);
        System.out.println("主方法执行完了");

    }

    private static void asyncDealWithData(final Date d1, final Date d2) {
        globalPool.submit(new Runnable() {
            //        asyncService.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10000);
                    System.out.println(d1.getDate());
                    System.out.println(d2.getDate());
                }catch(Exception e){

                }finally {
                    System.out.println("异步方法执行完了");
                }
            }
        });

    }
}
