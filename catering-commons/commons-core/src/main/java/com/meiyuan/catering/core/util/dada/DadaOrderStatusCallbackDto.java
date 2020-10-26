package com.meiyuan.catering.core.util.dada;

import lombok.Data;

/**
 * 达达订单状态变更回调
 *
 * @author lh
 */
@Data
public class DadaOrderStatusCallbackDto {

    /**
     * 达达运单号
     */
    private String client_id;
    /**
     * 业务系统订单ID
     */
    private String order_id;
    /**
     * 配送单状态
     */
    private int order_status;
    /**
     * 取消原因
     */
    private String cancel_reason;
    /**
     * 取消原因来源(1:达达配送员取消；2:商家主动取消；3:系统或客服取消；0:默认值)
     */
    private int cancel_from;
    /**
     * 更新时间
     */
    private Long update_time;
    /**
     * 达达配送员id，接单以后会传
     */
    private int dm_id;
    /**
     * 达达配送员姓名，接单以后会传
     */
    private String dm_name;
    /**
     * 达达配送员手机号，接单以后会传
     */
    private String dm_mobile;
    /**
     * 签名
     */
    private String signature;
}
