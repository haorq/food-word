package com.meiyuan.catering.core.util;

import com.meiyuan.catering.core.constant.RabbitMqConstant;
import com.meiyuan.catering.core.dto.DateTimeDiffDto;
import com.meiyuan.catering.core.dto.PresellFlagDTO;
import com.meiyuan.catering.core.dto.base.TimeRangeDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.generator.CodeGenerator;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 日期格式化工具类
 *
 * @author admin
 */
public class DateTimeUtil {

    private final static String PAY_PATTERN = "yyyyMMddHHmmss";
    public final static String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String PATTERN_YY_MM_DD = "yyyy-MM-dd";
    public final static String H_M_PATTERN = "HH:mm";
    private final static DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyMMdd");
    private final static DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final static DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern(H_M_PATTERN);
    private final static DateFormat DATE_HH_MM = new SimpleDateFormat(H_M_PATTERN);
    private final static DateFormat DATE_Y_M_D = new SimpleDateFormat(PATTERN);
    private final static String SPECIAL_TIME = "23:59";
    private final static String SPECIAL_TIME_ZERO = "00";
    private final static String SPECIAL_TIME_THIRTY = "30";
    private final static String SPECIAL_TIME_TWENTY_NINE = "29";
    private final static String SPECIAL_TIME_FIFTY_NINE = "59";
    private final static String BUSINESS_TIME = "23:30";
    //下单可选最晚时间
    private final static String BUSINESS_TIME_V5 = "23:20";
    public final static String ZERO = "0";

    /**
     * 门店夜间不通知默认时间段
     */
    private final static String NOTICE_BEGIN = "08:00";
    /**
     * 门店夜间不通知默认时间段
     */
    private final static String NOTICE_END = "23:00";


    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 昨天
     *
     * @return
     */
    public static Date yesterday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, -24);
        return calendar.getTime();
    }

    /**
     * 格式 yyyy年MM月dd日 HH:mm:ss
     *
     * @param dateTime
     * @return
     */
    public static String getDateTimeDisplayString(LocalDateTime dateTime) {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        String strDate2 = dtf2.format(dateTime);
        return strDate2;
    }

    public static String getDateTimeDisplayString(LocalDateTime dateTime, String format) {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(format);
        String strDate2 = dtf2.format(dateTime);
        return strDate2;
    }

    public static String getDateDisplayString(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(pattern);
        String strDate2 = dtf2.format(dateTime);
        return strDate2;
    }

    public static Date parseDate(String dateStr, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new CustomException("日期转换失败");
        }
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }


    public static String getTime(LocalDateTime dateTime) {
        return HH_MM_SS.format(dateTime);
    }

    public static String getTimeHourMinute(LocalDateTime dateTime) {
        return HH_MM.format(dateTime);
    }

    public static String getPrevMonthEndDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            // 设置为指定日期
            c.setTime(date);
            // 指定日期月份减去一
            c.add(Calendar.MONTH, -1);
            // 指定日期月份减去一后的 最大天数
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
            // 获取最终的时间
            Date lastDateOfPrevMonth = c.getTime();
            return sdf.format(lastDateOfPrevMonth);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime parsePayTime(String time) {
        try {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(PAY_PATTERN));
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    public static LocalDateTime parseTime(String time, String pattern) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    public static String timeStr() {
        return FORMAT.format(LocalDateTime.now());
    }

    public static LocalTime toLocalTime(String time) {
        return LocalTime.parse(time, HH_MM_SS);
    }

    /**
     * 传入时间加一分钟 ：若传入时间为 23：59则返回24：00
     *
     * @param time
     * @return
     */
    public static String addOneMinute(String time) {
        if (ObjectUtils.equals(SPECIAL_TIME, time)) {
            return "24:00";
        }
        return HH_MM.format(LocalTime.parse(time, HH_MM).minusMinutes(-1));
    }

    public static void checkTimeRange(List<TimeRangeDTO> timeRangeList) {
        if (!BaseUtil.judgeList(timeRangeList)) {
            throw new CustomException("店铺配送时间段不能为空");
        }

        //判断开始时间是否大于结束时间
        timeRangeList.forEach(timeRange -> {
            compareTime(timeRange.getStartTime(), timeRange.getEndTime());
            checkTime(timeRange);
        });

        int size = timeRangeList.size();
        //若只有一个时间段则无需校验时间段是否重复
        if (size == 1) {
            return;
        }
        //若有多个时间段则对时间段先进行排序
        timeRangeList.sort(Comparator.comparing(TimeRangeDTO::getStartTime));

        for (int i = 0; i < size - 1; i++) {
            //判断排序后每个时间段的结束时间，是否大于下一个时间段开始时间
            TimeRangeDTO beforeDto = timeRangeList.get(i);
            TimeRangeDTO afterDto = timeRangeList.get(i + 1);
            compareTimeV1(beforeDto.getEndTime(), afterDto.getStartTime());
        }
    }

    /**
     * 比较时间大小
     * 1、开始时间是否大于结束时间
     *
     * @param stareTime
     * @param endTime
     */
    private static void compareTime(String stareTime, String endTime) {
        LocalTime start = LocalTime.parse(stareTime, HH_MM);
        LocalTime end = LocalTime.parse(endTime, HH_MM);
        //每个时间段开始时间是否小于结束时间
        if (!start.isBefore(end)) {
            throw new CustomException("开始时间必须早于结束时间");
        }
    }

    /**
     * 判断每个时间段结束时间是否小于开始时间
     *
     * @param stareTime
     * @param endTime
     */
    private static void compareTimeV1(String stareTime, String endTime) {
        LocalTime start = LocalTime.parse(stareTime, HH_MM);
        LocalTime end = LocalTime.parse(endTime, HH_MM);
        //每个时间段开始时间是否小于结束时间
        if (!start.isBefore(end)) {
            throw new CustomException("您所设置的时间段不能重复");
        }
    }


    /**
     * 比较时间大小
     *
     * @param stareTime
     * @param endTime
     */
    public static boolean compareTimeHourMinute(String stareTime, String endTime) {
        LocalTime start = LocalTime.parse(stareTime, HH_MM);
        LocalTime end = LocalTime.parse(endTime, HH_MM);
        //每个时间段开始时间是否小于结束时间
        return start.isBefore(end);
    }


    /**
     * 判断两个时间段是否是整点
     */
    private static void checkTime(TimeRangeDTO timeRange) {
        String startTime = timeRange.getStartTime();
        String endTime = timeRange.getEndTime();
        String start = startTime.substring(startTime.length() - 2);
        String end = endTime.substring(endTime.length() - 2);
        if (!Objects.equals(start, DateTimeUtil.SPECIAL_TIME_ZERO) && !Objects.equals(start, DateTimeUtil.SPECIAL_TIME_THIRTY)) {
            throw new CustomException("您所设置的时间格式错误");
        }

        if (!Objects.equals(end, DateTimeUtil.SPECIAL_TIME_TWENTY_NINE) && !Objects.equals(end, DateTimeUtil.SPECIAL_TIME_FIFTY_NINE)) {
            throw new CustomException("您所设置的时间格式错误");
        }
    }


    /**
     * 获取延迟消息时限
     *
     * @param target 目标时间
     * @return 延迟消息时限
     */
    public static Long getTtl(LocalDateTime target) {
        Long ttl = Duration.between(LocalDateTime.now(), target).toMillis();
        if (ttl > RabbitMqConstant.MAX_X_DELAY_TTL) {
            ttl = RabbitMqConstant.MAX_X_DELAY_TTL;
        }
        return ttl < 0L ? 0L : ttl;
    }

    /**
     * @param
     * @return
     * @description
     * @author yaozou
     * @date 2020/6/2 14:55
     * @since v1.0.0
     */
    public static Long timestampOfMonth(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.parse(sdf.format(date)).getTime();
    }

    /**
     * @description 今日时间点内
     * @author yaozou
     * @date 2020/6/2 14:59
     * @since v1.1.0
     */
    public static boolean duringToday(Date startSellTime, Date endSellTime) {
        boolean flag = true;
        try {
            // 精确到时分
            long time = DateTimeUtil.timestampOfMonth(new Date());
            // 开始售卖时间 大于当前时间 未到开始售卖时间
            if (startSellTime != null && DateTimeUtil.timestampOfMonth(startSellTime) > time) {
                flag = false;
            }
            // 结束售卖时间 小于当前时间 超过结束售卖时间
            if (endSellTime != null && DateTimeUtil.timestampOfMonth(endSellTime) < time) {
                flag = false;
            }
            if (null == startSellTime && null == endSellTime) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 描述:计算现在商品预售标识
     *
     * @param flagDTO
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/13 11:02
     * @since v1.2.0
     */
    public static Boolean nowPresellFlag(PresellFlagDTO flagDTO) {
        return nowPresellFlag(flagDTO, true);
    }


    /**
     * 描述:计算现在商品预售标识
     *
     * @param flagDTO
     * @param verifyClose 是否验证截止时间
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/14 9:04
     * @since v1.2.0
     */
    public static Boolean nowPresellFlag(PresellFlagDTO flagDTO, Boolean verifyClose) {
        Boolean presellFlag = flagDTO.getPresellFlag();
        Date startSellTime = flagDTO.getStartSellTime();
        String sellWeekTime = flagDTO.getSellWeekTime();
        String closeSellTime = flagDTO.getCloseSellTime();

        boolean flag = false;
        if (presellFlag) {
            if (startSellTime != null) {
                flag = System.currentTimeMillis() < startSellTime.getTime();
            }
            if (flag) {
                //计算新的预售开始时间
                flagDTO.setNewStartSellTime(startSellTime);
                return true;
            }

            if (StringUtils.isNotBlank(sellWeekTime)) {
                //当前星期
                Integer weekOfDate = getWeekOfDate();
                flag = !sellWeekTime.contains(weekOfDate.toString());

            }
            if (flag) {
                //计算新的预售开始时间
                setNewStartSellTime(flagDTO, true);
                return true;
            }

            //是否验证截止时间
            if (verifyClose) {
                //截止时间判断
                flag = isExceedEndTime(closeSellTime);
//                //计算新的预售开始时间
//                setNewStartSellTime(flagDTO, flag);
            }
        }
        return flag;
    }

    private static void setNewStartSellTime(PresellFlagDTO flagDTO, Boolean flag) {
        if (flag) {
            LocalDateTime dateTime = getTime();
            String sellWeekTime = flagDTO.getSellWeekTime();
            if (StringUtils.isNotBlank(sellWeekTime)) {
                String[] split = sellWeekTime.split(",");
                Integer weekOfDate = getWeekOfDate();
                Integer day = null;
                for (String s : split) {
                    Integer w = Integer.valueOf(s);
                    if (w > weekOfDate) {
                        day = w;
                        break;
                    }
                }
                if (day == null) {
                    day = Integer.valueOf(split[0]);
                }
                Integer dayByWeek = calculateDayByWeek(day, weekOfDate);
                //加天数 -1 是因为getTime() 获取的时间已经加了一天
                dateTime = dateTime.plusDays(dayByWeek - 1);
            }

            Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

            //计算最新的预售开始时间
            flagDTO.setNewStartSellTime(date);
        }

    }


    private static Integer calculateDayByWeek(Integer week1, Integer week2) {
        int day = week1 - week2;
        if (day <= 0) {
            day = 7 + day;
        }
        return day;
    }

    public static LocalDateTime getTime() {
        return LocalDateTime.now().plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
    }

    public static Long getSecond() {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), getTime());
    }

    public static long diffSeconds(LocalDateTime start, LocalDateTime end) {
        long startSeconds = start.toEpochSecond(ZoneOffset.of("+8"));
        long endSeconds = end.toEpochSecond(ZoneOffset.of("+8"));
        return endSeconds - startSeconds;
    }




    public static DateTimeDiffDto dateDiff(Date startTime, Date endTime) {
        // 一天的毫秒数
        long nd = 1000 * 24 * 60 * 60;
        // 一小时的毫秒数
        long nh = 1000 * 60 * 60;
        // 一分钟的毫秒数
        long nm = 1000 * 60;
        // 一秒钟的毫秒数
        long ns = 1000;
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        // 获得两个时间的毫秒时间差异
        diff = endTime.getTime() - startTime.getTime();
        // 计算差多少天
        day = diff / nd;
        // 计算差多少小时
        hour = diff % nd / nh + day * 24;
        // 计算差多少分钟
        min = diff % nd % nh / nm + day * 24 * 60;
        // 计算差多少秒
        sec = diff % nd % nh % nm / ns;
        DateTimeDiffDto item = new DateTimeDiffDto();
        item.setDay(day);
        item.setHour(hour);
        item.setMinute(min);
        item.setSeconds(sec);
        item.setMinuteTotal(day * 24 * 60 + hour * 60 + min + (sec > 0 ? 1 : 0));
        return item;
    }

    /**
     * 方法描述 : 根据营业时间段判断店铺当前营业状态
     * 营业时间段可跨天
     *
     * @param open  开始时间
     * @param close 结束时间
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 0:04
     * @return: java.lang.Integer   营业状态：1-营业 2-打样
     * @Since version-1.2.0
     */
    public static Integer getShopBusinessStatus(String open, String close) {
        Integer shopBusinessStatus = 2;
        LocalTime parseNow = LocalTime.parse(getDateTimeDisplayString(LocalDateTime.now(), DateTimeUtil.H_M_PATTERN), HH_MM);

        //截取时间中时分
        LocalTime parseOpen = LocalTime.parse(open, HH_MM);
        LocalTime parseClose = LocalTime.parse(close, HH_MM);

        //若开始时间等于结束时间全天营业
        if (parseOpen.equals(parseClose)) {
            shopBusinessStatus = 1;
        }

        //若开始时间小于结束时间
        if (parseOpen.isBefore(parseClose)) {
            boolean b = (parseNow.isBefore(parseClose) || parseNow.equals(parseClose)) &&
                    (parseNow.isAfter(parseOpen)) || parseNow.equals(parseOpen);
            if (b) {
                shopBusinessStatus = 1;
            }
        }

        //若开始时间小于结束时间
        if (parseOpen.isAfter(parseClose)) {
            if (parseNow.isAfter(parseOpen) || parseNow.equals(parseOpen)) {
                shopBusinessStatus = 1;
            }
            if (parseNow.isBefore(parseClose) || parseNow.equals(parseClose)) {
                shopBusinessStatus = 1;
            }
        }
        return shopBusinessStatus;
    }

    /**
     * 方法描述 :
     *
     * @param timeRangeList 去除
     * @Author: MeiTao
     * @Date: 2020/7/21 0021 17:12
     * @return: java.util.List<com.meiyuan.catering.core.dto.base.TimeRangeDTO>
     * @Since version-1.2.0
     */
    public static List<TimeRangeDTO> filterTimeRange(List<TimeRangeDTO> timeRangeList) {
        List<TimeRangeDTO> result = new ArrayList<>();
        LocalDateTime now2 = LocalDateTime.now();
        String nowStr = getDateTimeDisplayString(now2, DateTimeUtil.H_M_PATTERN);
        //当前时间小于23:30 则返回true
        Boolean j = nowStr.compareTo(DateTimeUtil.BUSINESS_TIME) < 0;
        if (!j) {
            return result;
        }
        if (BaseUtil.judgeList(timeRangeList)) {
            timeRangeList.forEach(timeRange -> {
                String nowStrThirty = getDateTimeDisplayString(now2.plus(30L, ChronoUnit.MINUTES), DateTimeUtil.H_M_PATTERN);

                //当前时间若小于时间段结束则返回true
                Boolean i = nowStrThirty.compareTo(timeRange.getEndTime()) <= 0;
                if (i) {
                    if (!BaseUtil.judgeList(result)) {
                        Boolean k = timeRange.getStartTime().compareTo(nowStrThirty) > 0;
                        if (!k) {
                            List<String> list = Arrays.asList(nowStr.split(":"));
                            String minStr = list.get(1);
                            Integer min = Integer.valueOf(minStr);
                            Integer hours = Integer.valueOf(list.get(0));
                            String hoursUpdate = CodeGenerator.autoGenericCode(String.valueOf(hours), 2);
                            timeRange.setStartTime(min < 30 ? (list.get(0) + ":" + "30") : (hoursUpdate + ":" + "00"));
                        }
                    }
                    result.add(timeRange);
                }
            });
        }
        return result;
    }

    /**
     * 方法描述 : 计算
     * @Author: MeiTao
     * @Date: 2020/10/19 0019 16:35
     * @param timeRangeList
     * @param addTime 请求参数
     * @return: java.util.List<com.meiyuan.catering.core.dto.base.TimeRangeDTO>
     * @Since version-1.5.0
     */
    public static List<TimeRangeDTO> filterTimeRangeV5(List<TimeRangeDTO> timeRangeList,Long addTime) {
        List<TimeRangeDTO> result = new ArrayList<>();
        LocalDateTime now2 = LocalDateTime.now();
        LocalDateTime now3 = now2.plus(addTime, ChronoUnit.MINUTES);
        String nowStr = getDateTimeDisplayString(now3, DateTimeUtil.H_M_PATTERN);
        //当前时间小于23:30 则返回true
        //获取当前剩余分钟数
        Long remainMin = getRemainMin(null);
        if(remainMin < addTime + 30){
            return result;
        }
        if (BaseUtil.judgeList(timeRangeList)) {
            timeRangeList.forEach(timeRange -> {
                String nowStrThirty = getDateTimeDisplayString(now3.plus(30L, ChronoUnit.MINUTES), DateTimeUtil.H_M_PATTERN);

                //当前时间若小于时间段结束则返回true
                Boolean i = nowStrThirty.compareTo(timeRange.getEndTime()) <= 0;
                if (i) {
                    if (!BaseUtil.judgeList(result)) {
                        Boolean k = timeRange.getStartTime().compareTo(nowStrThirty) > 0;
                        if (!k) {
                            List<String> list = Arrays.asList(nowStr.split(":"));
                            String minStr = list.get(1);
                            Integer min = Integer.valueOf(minStr);
                            Integer hours = Integer.valueOf(list.get(0));
                            String hoursUpdate = CodeGenerator.autoGenericCode(String.valueOf(hours), 2);
                            timeRange.setStartTime(min < 30 ? (list.get(0) + ":" + "30") : (hoursUpdate + ":" + "00"));
                        }
                    }
                    result.add(timeRange);
                }
            });
        }
        return result;
    }

    /**
     * 方法描述 : 获取立即送达时间
     *
     * @param timeRangeList 请求参数
     * @Author: MeiTao
     * @Date: 2020/10/9 0009 19:50
     * @return: java.util.List<com.meiyuan.catering.core.dto.base.TimeRangeDTO>
     * @Since version-1.5.0
     */
    public static List<String> immediateDeliveryTime(List<TimeRangeDTO> timeRangeList,Long addTime) {
        List<String> result = new ArrayList<>();
        LocalDateTime now2 = LocalDateTime.now();
        now2 = now2.plusMinutes(addTime);
        //获取当前剩余分钟数
        Long remainMin = getRemainMin(null);
        if(remainMin < addTime){
            return result;
        }
//        Boolean j = getDateTimeDisplayString(now2, DateTimeUtil.H_M_PATTERN).compareTo(DateTimeUtil.BUSINESS_TIME_V5) < 0;
//        if (!j) {
//            return result;
//        }
        String nowStr = getDateTimeDisplayString(now2, DateTimeUtil.H_M_PATTERN);

        if (BaseUtil.judgeList(timeRangeList)) {
            timeRangeList.forEach(timeRange -> {
                //当前时间若小于时间段结束则返回true
                Boolean i = nowStr.compareTo(timeRange.getEndTime()) <= 0;
                Boolean k = timeRange.getStartTime().compareTo(nowStr) <= 0;
                if (i && k) {
                    result.add(nowStr);
                }
            });
        }
        return result;
    }

    /**
     * 获取当前日期是星期几
     *
     * @return 当前日期是星期几
     */
    public static Integer getWeekOfDate() {
        return LocalDateTime.now().getDayOfWeek().getValue();
    }

    /**
     * 计算当天剩余分钟数
     *
     * @return 当前日期是星期几
     */
    public static Long getRemainMin(Long addTime){
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate today =  localDateTime.toLocalDate();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(Duration.between(localDateTime, tomorrowMidnight).toNanos());
        if (!org.springframework.util.ObjectUtils.isEmpty(addTime)){
            minutes = minutes+ 30;
        }
        return minutes;
    }

    public static void main(String[] args) {
        LocalDateTime now2 = DateTimeUtil.parseTime("2020-10-23 23:22:00",PATTERN);
        System.out.println(DateTimeUtil.dateDiff(DateTimeUtil.now(), DateTimeUtil.parseDate("2020-10-14 19:30:00",PATTERN)));
        //        System.out.println(DateTimeUtil.format(new Date(1602460800000L), PATTERN));

//        String dateTimeDisplayString = getDateTimeDisplayString(LocalDateTime.now().plus(30L, ChronoUnit.MINUTES), DateTimeUtil.H_M_PATTERN);
//        LocalDateTime close = LocalDateTime.parse("2020-07-06 09:58:00", DateTimeFormatter.ofPattern(PATTERN));
//        String dateTimeDisplayString1 = getDateTimeDisplayString(close, DateTimeUtil.H_M_PATTERN);
//        System.out.println(dateTimeDisplayString);
//
//        boolean b = "09:58".compareTo("10:58") < 0;
//        Integer i = 05;
//        System.out.println(i + 1);
//        LocalTime parseNow = LocalTime.parse(getDateTimeDisplayString(LocalDateTime.now(), DateTimeUtil.H_M_PATTERN), HH_MM);
//
//        LocalDateTime open = LocalDateTime.parse("2020-07-05 08:57:00", DateTimeFormatter.ofPattern(PATTERN));
//        LocalDateTime close = LocalDateTime.parse("2020-07-06 09:58:00", DateTimeFormatter.ofPattern(PATTERN));
//        String openStr = getDateTimeDisplayString(open, DateTimeUtil.H_M_PATTERN);
//        String closeStr = getDateTimeDisplayString(close, DateTimeUtil.H_M_PATTERN);
//        System.out.println(openStr + "         " + closeStr);
//        //截取时间中时分
//        System.out.println(getShopBusinessStatus(openStr, closeStr));
//        System.out.println(parseNow);
    }

    /**
     * 描述:当前时间是否超过截止时间
     *
     * @param closeSellTime 截止时间
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/14 8:58
     * @since v1.2.0
     */
    public static Boolean isExceedEndTime(String closeSellTime) {
        if (StringUtils.isNotBlank(closeSellTime)) {
            try {
                Date parse = DATE_HH_MM.parse(DATE_HH_MM.format(new Date()));
                //20:30  < 16:20
                return parse.getTime() > DATE_HH_MM.parse(closeSellTime).getTime();
            } catch (ParseException ignored) {
            }
        }
        return false;
    }

    public static List<String> weekString(String str) {
        List<String> resList = new ArrayList<>();
        Map<Integer, String> map = new HashMap<>(16);
        map.put(1, "周一");
        map.put(2, "周二");
        map.put(3, "周三");
        map.put(4, "周四");
        map.put(5, "周五");
        map.put(6, "周六");
        map.put(7, "周日");
        String[] split = str.split(",");
        for (int i = 0; i < split.length; i++) {
            resList.add(map.getOrDefault(Integer.valueOf(split[i]), "每天"));
        }
        return resList;
    }


    /**
     * describe: 判断开始时间是否小于结束事假,精确到秒
     *
     * @param stareTime
     * @param endTime
     * @author: yy
     * @date: 2020/8/8 16:55
     * @return: {@link boolean}
     * @version 1.3.0
     **/
    public static boolean compareLocalDateTime(LocalDateTime stareTime, LocalDateTime endTime) {
        if (null == stareTime && null == endTime) {
            return false;
        }
        if (null == stareTime) {
            return true;
        }
        if (null == endTime) {
            return false;
        }
        //每个时间段开始时间是否小于结束时间
        return stareTime.isBefore(endTime);
    }

    /**
     * 方法描述 : 判断当前时间是否在23：00 到8：00 之间
     *
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 18:31
     * @return: boolean
     * @Since version-1.3.0
     */
    public static boolean judgeTime() {
        Boolean result = Boolean.TRUE;
        LocalTime start = LocalTime.parse(NOTICE_BEGIN, HH_MM);
        LocalTime end = LocalTime.parse(NOTICE_END, HH_MM);
        LocalTime parseNow = LocalTime.parse(getDateTimeDisplayString(LocalDateTime.now(), DateTimeUtil.H_M_PATTERN), HH_MM);
        if (parseNow.isBefore(start)) {
            result = Boolean.FALSE;
        }
        if (parseNow.isAfter(end)) {
            result = Boolean.FALSE;
        }
        return result;
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }


}
