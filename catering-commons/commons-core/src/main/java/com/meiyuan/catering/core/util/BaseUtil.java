package com.meiyuan.catering.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.enums.base.InsertUpdateDelNameCountSizeEnum;
import com.meiyuan.catering.core.enums.base.SaleChannelsEnum;
import com.meiyuan.catering.core.enums.base.WeekEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author wxf
 * @date 2020/3/11 17:04
 * @description 基础工具类
 **/
@Slf4j
public class BaseUtil {

    public final static BigDecimal PRICE = BigDecimal.valueOf(-1);
    public final static BigDecimal B_100 = BigDecimal.valueOf(100);
    public final static double D_01 = 0.1;
    public final static double D_10 = 10;
    private final static Integer NUM = -1;
    public final static String CHANGE_FLAG = "-1";
    public final static Integer DEFAULT_INVENTORY = -1;
    public static final String COMMA = ",";
    public static final Integer INDEX_GOODS_SIZE = 3;
    public static final Long NULL_FLAG = -1L;
    public static final String NULL_STR = "";
    public static final Integer SIZE = 1;
    public static final Integer SIZE2 = 2;

    /**
     * 判断集合是否是null和size大于0
     * 不是null和size大于0 返回true
     * 否则返回false
     *
     * @param list 集合
     * @author: wxf
     * @date: 2020/3/11 14:47
     * @return: boolean 不是null和size大于0 返回true 否则返回false
     **/
    public static boolean judgeList(List list) {
        boolean flag = false;
        if (null != list && 0 < list.size()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断字符串是否是null和length大于0
     * 不是null和length大于0 返回true
     * 否则返回false
     *
     * @param str 字符串
     * @author: wxf
     * @date: 2020/3/11 14:46
     * @return: boolean  不是null和length大于0 返回true
     **/
    public static boolean judgeString(String str) {
        boolean flag = false;
        if (null != str && 0 < str.length()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 对象转对象
     *
     * @param source 源对象
     * @param target 目标对象class
     * @param <T>    泛型
     * @return 返回目标对象
     */
    public static <T> T objToObj(Object source, Class<T> target) {
        String jsonString = JSON.toJSONString(source);
        return JSON.parseObject(jsonString, target);
    }

    /**
     * 集合转集合
     *
     * @param sourceList 源集合
     * @param target     目标集合class
     * @param <T>        泛型
     * @return 返回目标集合
     */
    public static <T> List<T> objToObj(Collection<?> sourceList, Class<T> target) {
        String jsonString = JSON.toJSONString(sourceList);
        return JSON.parseArray(jsonString, target);
    }

    /**
     * 新增修改删除 返回的字符串
     *
     * @param size        sql 执行返回的条数
     * @param trueString  正确返回的字符串
     * @param falseString 错误返回的字符串
     * @author: wxf
     * @date: 2020/3/11 15:22
     * @return: java.lang.String
     **/
    public static String insertUpdateDelSetString(int size, String trueString, String falseString) {
        String returnString;
        if (InsertUpdateDelNameCountSizeEnum.SIZE.getStatus() == size) {
            returnString = trueString;
        } else {
            returnString = falseString;
        }
        return returnString;
    }

    /**
     * 新增修改删除 返回的字符串
     *
     * @param flag        成功还是失败
     * @param trueString  正确返回的字符串
     * @param falseString 错误返回的字符串
     * @author: wxf
     * @date: 2020/3/11 15:22
     * @return: java.lang.String
     **/
    public static String insertUpdateDelBatchSetString(Boolean flag, String trueString, String falseString) {
        String returnString;
        if (flag) {
            returnString = trueString;
        } else {
            returnString = falseString;
        }
        return returnString;
    }

    /**
     * localDateTimeToDate
     *
     * @param localDateTime localDateTime
     * @author: wxf
     * @date: 2020/3/23 19:35
     * @return: {@link Date}
     **/
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 日期是周几
     *
     * @param date 日期
     * @author: wxf
     * @date: 2020/4/1 10:05
     * @return: {@link String}
     **/
    public static String dayOfWeek(LocalDate date) {
        int value = date.getDayOfWeek().getValue();
        WeekEnum parse = WeekEnum.parse(value);
        return date.toString() + " " + "(" + parse.getDesc() + ")";
    }

    /**
     * date TO localDate
     *
     * @param date date
     * @author: wxf
     * @date: 2020/4/9 10:03
     * @return: {@link LocalDate}
     **/
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * localDate TO date
     *
     * @param date
     * @return
     */
    public static Date localDateTodate(LocalDate date) {
        if (null == date) {
            return null;
        }
        ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());

    }

    /**
     * 转换ES的 经纬度 维度在前
     *
     * @param str
     * @author: wxf
     * @date: 2020/4/22 14:04
     * @return: {@link String}
     **/
    public static String locationToEsConver(String str) {
        String[] split = str.split(",");
        return split[1] + "," + split[0];
    }

    /**
     * 非空集合转集合
     *
     * @param sourceList 源集合
     * @param target     目标集合class
     * @param <T>        泛型
     * @return 返回目标集合
     */
    public static <T> List<T> noNullAndListToList(List<?> sourceList, Class<T> target) {
        if (judgeList(sourceList)) {
            String jsonString = JSON.toJSONString(sourceList);
            return JSON.parseArray(jsonString, target);
        } else {
            return new ArrayList<T>();
        }
    }

    /**
     * 验证 result 返回的集合是否 非空 长度大于0
     *
     * @param result client 返回的数据包装类
     * @author: wxf
     * @date: 2020/5/26 15:18
     * @return: {@link boolean}
     * @version 1.0.1
     **/
    public static <T> boolean judgeResultList(Result<List<T>> result) {
        return result.success() && judgeList(result.getData());
    }

    /**
     * 非空集合转集合
     *
     * @param source 源对象
     * @param target 目标对象class
     * @param <T>    泛型
     * @return 返回目标集合
     */
    public static <T> T noNullAndObjToObj(Object source, Class<T> target) {
        if (null == source) {
            return null;
        } else {
            String jsonString = JSON.toJSONString(source);
            return JSON.parseObject(jsonString, target);
        }
    }

    /**
     * 验证 result 返回的集合是否 非空 长度大于0
     *
     * @param result client 返回的数据包装类
     * @author: wxf
     * @date: 2020/5/26 15:18
     * @return: {@link boolean}
     * @version 1.0.1
     **/
    public static <T> boolean judgeResultObject(Result<T> result) {
        return result.success() && null != result.getData();
    }

    public static <T> T getListFirstOne(List<T> categoryList) {
        if (categoryList != null && categoryList.size() > 0) {
            return categoryList.get(0);
        }
        return null;
    }

    /**
     * describe: 当前字符串是否为空（包含 “null” 字符 和 空格），是-true
     *
     * @param str
     * @author: yy
     * @date: 2020/7/10 9:34
     * @return: {boolean}
     * @version 1.2.0
     **/
    public static boolean isEmptyStr(String str) {
        return null == str || "".equals(str.trim()) || "null".equalsIgnoreCase(str.trim());
    }

    /**
     * 描述: price 是否为-1
     *
     * @param price
     * @return boolean
     * @author zengzhangni
     * @date 2020/7/14 17:15
     * @since v1.2.0
     */
    public static boolean isNegativeOne(BigDecimal price) {
        if (price == null) {
            return false;
        }
        return price.compareTo(PRICE) == 0;
    }

    public static boolean isNegativeOne(Integer num) {
        if (num == null) {
            return false;
        }
        return Objects.equals(num, NUM);
    }

    public static boolean isNullOrNegativeOne(Integer num) {
        if (num == null) {
            return true;
        }
        return Objects.equals(num, NUM);
    }

    public static boolean isNullOrNegativeOne(BigDecimal num) {
        if (num == null) {
            return true;
        }
        return num.compareTo(PRICE) == 0;
    }

    public static boolean isSupportWx(Integer salesChannels) {
        return !Objects.equals(salesChannels, SaleChannelsEnum.TS.getStatus());
    }

    public static Boolean priceEquals(BigDecimal price1, BigDecimal price2) {
        return price1.compareTo(price2) == 0;
    }

    public static Boolean priceIsLt(BigDecimal price1, BigDecimal price2) {
        return price1.compareTo(price2) < 0;
    }

    public static Boolean priceIsLte(BigDecimal price1, BigDecimal price2) {
        return price1.compareTo(price2) <= 0;
    }

    public static Boolean priceNoEquals(BigDecimal price1, BigDecimal price2) {
        return !priceEquals(price1, price2);
    }

    public static Boolean is0To10(BigDecimal discount) {
        double doubleValue = discount.doubleValue();
        return D_01 <= doubleValue && doubleValue < D_10;
    }

    public static String discountLabel(BigDecimal marketPrice, BigDecimal salesPrice) {
        BigDecimal decimal = discount(marketPrice, salesPrice);
        if (decimal == null) {
            return NULL_STR;
        }
        return decimal.toPlainString() + "折";
    }

    public static String discountLabel(BigDecimal discount) {
        if (isNullOrNegativeOne(discount) || !is0To10(discount)) {
            return NULL_STR;
        }
        return discount.setScale(1, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + "折";
    }

    public static BigDecimal discount(BigDecimal marketPrice, BigDecimal salesPrice) {
        if (isZeroOrNull(marketPrice) || isZeroOrNull(salesPrice) || priceEquals(marketPrice, salesPrice)) {
            return null;
        }
        BigDecimal discount = salesPrice.divide(marketPrice, 2, 4).multiply(BigDecimal.TEN);
        if (!BaseUtil.is0To10(discount)) {
            return null;
        }
        //保留一位小数 去尾数的0
        return discount.setScale(1, BigDecimal.ROUND_DOWN).stripTrailingZeros();

    }

    public static BigDecimal discountOther(BigDecimal salesPrice, BigDecimal marketPrice) {
        BigDecimal discount = salesPrice.divide(marketPrice, 2, 4).multiply(BigDecimal.TEN);
        if (discount.doubleValue() <= D_01) {
            return new BigDecimal(String.valueOf(D_01)).setScale(1, BigDecimal.ROUND_DOWN).stripTrailingZeros();
        }
        //保留一位小数 去尾数的0
        return discount.setScale(1, BigDecimal.ROUND_DOWN).stripTrailingZeros();
    }

    public static String billKey(Long shopId, Long goodsId) {
        return shopId + ":" + goodsId;
    }

    public static String billKey(Long shopId, String goodsId) {
        return shopId + ":" + goodsId;
    }

    public static String toPlainString(Double d) {
        if (Double.isInfinite(d)) {
            log.error("double is infinite");
            return NULL_STR;
        }
        return toPlainString(BigDecimal.valueOf(d), 2);
    }

    public static String toPlainString(BigDecimal price) {
        return toPlainString(price, 2);
    }

    public static String toPlainString(BigDecimal price, int newScale) {
        if (price == null) {
            return NULL_STR;
        }
        BigDecimal bigDecimal = price.setScale(newScale, BigDecimal.ROUND_DOWN);
        return bigDecimal.stripTrailingZeros().toPlainString();
    }

    public static void jsonLog(Object obj) {
        String jsonString = JSONObject.toJSONString(obj);
        log.debug("\n" + format(jsonString));
    }

    /**
     * 得到格式化json数据  退格用\t 换行用\r
     */
    public static String format(String jsonStr) {
        int level = 0;
        StringBuilder jsonForMatStr = new StringBuilder();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c).append("\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c).append("\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuilder levelStr = new StringBuilder();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    public static String seckillKey(Long mGoodsId, Long eventId) {
        return NULL_STR + mGoodsId + eventId;
    }

    /**
     * 方法描述 : 判断金额是否为0
     * 等于零：true，金额为空：true
     *
     * @param price 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/14 0014 10:47
     * @return: java.lang.Boolean
     * @Since version-1.3.0
     */
    public static Boolean judgeZero(BigDecimal price) {
        if (ObjectUtils.isEmpty(price)) {
            return Boolean.TRUE;
        }
        return price.doubleValue() == new BigDecimal(0).doubleValue();
    }

    /**
     * 方法描述 :组装店铺优惠券信息
     *
     * @param amount           优惠金额
     * @param consumeCondition 限制金额
     * @Author: MeiTao
     * @Date: 2020/8/14 0014 10:54
     * @return: java.lang.String
     * @Since version-1.3.0
     */
    public static String ticketInfoStr(BigDecimal amount, BigDecimal consumeCondition, Integer activityType) {
        StringBuilder builder = new StringBuilder();
        if (Objects.equals(activityType, 1)) {
            return builder.append("领").append(toPlainString(amount, 2)).append("元券").toString();
        }

        if (judgeZero(consumeCondition)) {
            return builder.append(ObjectUtils.isEmpty(amount) ? toPlainString(new BigDecimal(0), 2) : toPlainString(amount, 2)).append("元券").toString();
        }
        return builder.append("满").append(BaseUtil.toPlainString(consumeCondition, 2)).append("减").append(BaseUtil.toPlainString(amount, 2)).toString();
    }

    public static boolean isZero(BigDecimal consumeCondition) {
        if (consumeCondition == null) {
            return false;
        }
        return consumeCondition.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isZeroOrNull(BigDecimal consumeCondition) {
        if (consumeCondition == null) {
            return true;
        }
        return consumeCondition.compareTo(BigDecimal.ZERO) == 0;
    }

    public static long timeBetween(String time) {
        return timeBetween(LocalTime.parse(time));
    }

    public static long timeBetween(LocalTime localTime) {
        long time = 0;
        try {
            time = Duration.between(LocalTime.now(), localTime).toMillis() / 1000;
        } catch (Exception e) {
            log.error("获取当前时间与传入时间时间差出错，当前时间 ： " + LocalTime.now() + "; 传入时间 ： " + localTime, e);
        }
        return time;
    }

    public static Long milliSecond() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static Long milliSecond(LocalDateTime time) {
        return time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static String subFirstByComma(String str) {
        return substringBefore(str, COMMA);
    }

    public static String substringBefore(String str, String separator) {
        return StringUtils.substringBefore(str, separator);
    }

    public static BigDecimal multiply(BigDecimal price, Integer number) {
        return price.multiply(BigDecimal.valueOf(number));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }


    /**
     * 描述: 是否大于等于0
     *
     * @param amount
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/15 11:18
     * @since v1.5.0
     */
    public static Boolean isGteZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static Boolean isLteZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * 描述: 是否大于0
     *
     * @param amount
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/15 11:18
     * @since v1.5.0
     */
    public static Boolean isGtZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 描述: 是否等于0
     *
     * @param amount
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/15 11:18
     * @since v1.5.0
     */
    public static Boolean isEqualsZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public static Integer yuanToFen(BigDecimal amount) {
        return amount.multiply(BaseUtil.B_100).intValue();
    }

    /**是否是由数字组成*/
    private static Pattern ALL_NUMBER = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    /**
     * 判断一个字符串是否是数字。
     *
     * @param string
     * @return
     */
    public static boolean allNumber(String string) {
        if (string == null){
            return false;
        }
        return ALL_NUMBER.matcher(string).matches();
    }
}
