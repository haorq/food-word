package com.meiyuan.catering.marketing.dto.seckill;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName MarketingSeckillDTO
 * @Description
 * @Author gz
 * @Date 2020/5/19 16:39
 * @Version 1.1
 */
@Data
public class MarketingSeckillDTO {
    private Long id;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 上架/下架
     */
    private Integer upDown;
    /**
     * 活动对象：企业、个人
     */
    private Integer objectLimit;
    /**
     * 订单作废时限
     */
    private Integer orderCancellationTime;
    /**
     * 说明
     */
    private String description;
    /**
     * 数据来源：1-平台；2-商家
     */
    private Integer source;

}
