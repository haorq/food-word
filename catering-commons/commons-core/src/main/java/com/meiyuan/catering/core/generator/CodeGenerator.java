package com.meiyuan.catering.core.generator;

import com.meiyuan.catering.core.constant.DefaultPwdMsg;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.DateTimeUtil;
import org.apache.commons.lang.time.FastDateFormat;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yaoozu
 * @description 编码生成器
 * @date 2020/3/1814:10
 * @since v1.0.0
 */
public class CodeGenerator {
    private static final FastDateFormat YEAR_PATTERN = FastDateFormat.getInstance("yy");
    private static final SnowflakeIdWorker SNOWFLAKE_ID_WORKER = new SnowflakeIdWorker();
    private static final int[] NATURAL_NUMBER = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final String PAY_PREFIX = "CYXS";

    /**
     * @param
     * @return
     * @description 订单号 tag(自提/配送)+雪花算法ID 向用户/平台/商户只展示后10位即可
     * @author yaozou
     * @date 2020/3/18 14:13
     * @since v1.0.0
     */
    public static String orderNo() {
        StringBuilder builder = new StringBuilder();
        builder.append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }

    public static Long createId() {
        return SNOWFLAKE_ID_WORKER.nextId();
    }

    /**
     * @param tag 前缀
     *            orderNo 订单号
     * @return
     * @description 短订单编码
     * @author yaozou
     * @date 2020/3/18 16:47
     * @since v1.0.0
     */
    public static String shortOrderNo(String tag, String orderNo) {
        int length = 10;
        String shortOrderNo = orderNo.length() <= length ? orderNo : orderNo.substring(orderNo.length() - length);
        return tag + shortOrderNo;
    }

    /**
     * @param
     * @return
     * @description 取餐码
     * @author yaozou
     * @date 2020/3/18 14:35
     * @since v1.0.0
     */
    public static String shortCode() {
        Integer code = ThreadLocalRandom.current().nextInt(0, 999999);
        return code.toString();
    }

    /**
     * 生成随机验证码
     *
     * @param length 验证码长度
     * @return
     */
    public static String randomCode(int length) {
        if (length <= 0) {
            throw new CustomException("随机验证码长度必须大于0");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(10);
            sb.append(NATURAL_NUMBER[index]);
        }
        return sb.toString();
    }

    /**
     * 店铺修改密码获取短信验证码
     *
     * @param length 验证码长度
     * @return
     */
    public static String msgCode(int length) {
        if (length <= 0) {
            throw new CustomException("随机验证码长度必须大于0");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(10);
            sb.append(NATURAL_NUMBER[index]);
        }
        String msgCode = sb.toString();
        if (DefaultPwdMsg.USE_DEFAULT_MSG) {
            msgCode = DefaultPwdMsg.DEFAULT_MSG;
        }
        return msgCode;
    }

    /**
     * @param
     * @return
     * @description 商户编码前缀 MC+年份+自增(MC+20+redis自增)
     * @author yaozou
     * @date 2020/3/18 14:33
     * @since v1.0.0
     */
    public static String merchantCodePrefix() {
        StringBuilder builder = new StringBuilder("MC");
        builder.append(YEAR_PATTERN.format(Instant.now().toEpochMilli()));
        return builder.toString();
    }

    /**
     * @param
     * @return
     * @description 店铺编码前缀 MC+年份+自增(MC+20+redis自增)
     * @author yaozou
     * @date 2020/3/18 14:33
     * @since v1.0.0
     */
    public static String shopCodePrefix() {
        StringBuilder builder = new StringBuilder("SH");
        builder.append(YEAR_PATTERN.format(Instant.now().toEpochMilli()));
        return builder.toString();
    }

    /**
     * 方法描述 : 编码位数不够前面自动补0
     *
     * @param code
     * @param num  请求参数
     * @Author: MeiTao
     * @Date: 2020/7/18 0018 10:53
     * @return: java.lang.String
     * @Since version-1.2.0
     */
    public static String autoGenericCode(String code, int num) {
        String result = "";
        result = String.format("%0" + num + "d", Integer.parseInt(code) + 1);
        return result;
    }

    /**
     * @param
     * @return
     * @description 地推员编码前缀 DT+年份+自增(MC+20+redis自增)
     * @date 2020/3/18 14:33
     * @since v1.0.0
     */
    public static String pusherCodePrefix() {
        StringBuilder builder = new StringBuilder("DT");
        builder.append(YEAR_PATTERN.format(Instant.now().toEpochMilli()));
        return builder.toString();
    }

    /**
     * @param
     * @return
     * @description 商品编码前缀 SP+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 14:34
     * @since v1.0.0
     */
    public static String goodsSpuCodePrefix(String merchantCode) {
        StringBuilder builder = new StringBuilder("SP");
        builder.append(merchantCode);
        return builder.toString();
    }

    /**
     * @param
     * @return
     * @description sku编码前缀 sku+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 14:34
     * @since v1.0.0
     */
    public static String skuCodePrefix(String merchantCode) {
        StringBuilder builder = new StringBuilder("SKU");
        builder.append(merchantCode);
        return builder.toString();
    }

    /**
     * @param
     * @return
     * @description 赠品编码前缀 ZP+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 14:34
     * @since v1.0.0
     */
    public static String giftCodePrefix(String merchantCode) {
        StringBuilder builder = new StringBuilder("ZP");
        builder.append(merchantCode);
        return builder.toString();
    }

    /**
     * @param
     * @return
     * @description 菜单编码前缀 ZP+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 14:34
     * @since v1.0.0
     */
    public static String menuCodePrefix(String merchantCode) {
        StringBuilder builder = new StringBuilder("CD");
        builder.append(merchantCode);
        return builder.toString();
    }

    /**
     * 描述: 充值订单号
     * <p>
     * CZ+时间(yyMMdd)+雪花算法
     *
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/27 14:01
     */
    public static String rechargeOrderNo() {
        StringBuilder builder = new StringBuilder(PAY_PREFIX)
                .append("CZ")
                .append(DateTimeUtil.timeStr())
                .append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }

    /**
     * 描述:微信支付系统流水号
     * <p>
     * WX+门店编码+雪花算法
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/5/21 9:09
     * @since v1.1.0
     */
    public static String wxPayNo(String shopCode) {
        StringBuilder builder = new StringBuilder(PAY_PREFIX)
                .append("WX")
                .append(shopCode)
                .append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }

    /**
     * 描述:余额退款流水号
     * <p>
     * BR+时间(yyMMdd)+雪花算法
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/31 13:55
     */
    public static String balanceRefundTransactionNo() {
        StringBuilder builder = new StringBuilder("BR")
                .append(DateTimeUtil.timeStr())
                .append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }

    /**
     * 描述:余额退款流水号
     * <p>
     * BO+时间(yyMMdd)+雪花算法
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/31 13:55
     */
    public static String balanceOrderTransactionNo() {
        StringBuilder builder = new StringBuilder("BO")
                .append(DateTimeUtil.timeStr())
                .append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }

    /**
     * 描述:余额退款流水号
     * <p>
     * SY+时间(yyMMdd)+雪花算法
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/31 13:55
     */
    public static String systemTransactionNo() {
        StringBuilder builder = new StringBuilder("SY")
                .append(DateTimeUtil.timeStr())
                .append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }

    /**
     * 描述:退款订单号
     * <p>
     * RO+门店编码+雪花算法
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/3/31 13:55
     */
    public static String refundOrderNo(String shopCode) {
        StringBuilder builder = new StringBuilder(PAY_PREFIX)
                .append("RO")
                .append(shopCode)
                .append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }

    /**
     * 描述:充值订单退款订单号
     * <p>
     * RC+时间(yyMMdd)+雪花算法
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/4/13
     */
    public static String refundChargeOrderNo() {
        StringBuilder builder = new StringBuilder("RC")
                .append(DateTimeUtil.timeStr())
                .append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }


    public static String spuCode(long id) {
        return "SP" + id;
    }

    public static long goodsId(long id) {
        return id;
    }

    public static String agentPayOrderNo() {
        return baseNo("AP");
    }

    public static String agentPayBatchNo() {
        return baseNo("APB");
    }

    public static String getTransferNo() {
        return baseNo("ZZ");
    }

    public static String baseNo(String prefix) {
        StringBuilder builder = new StringBuilder(prefix)
                .append(DateTimeUtil.timeStr())
                .append(SNOWFLAKE_ID_WORKER.nextId());
        return builder.toString();
    }


}
