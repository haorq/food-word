package com.meiyuan.catering.marketing.dto.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName SeckillMerchantListDTO
 * @Description 秒杀商户App列表数据DTO
 * @Author gz
 * @Date 2020/3/21 9:49
 * @Version 1.1
 */
@Data
public class SeckillMerchantListDTO {
    @ApiModelProperty(value = "秒杀活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动对象：0-全部；1-个人；2-企业")
    private Integer objectLimit;
    @ApiModelProperty(value = "活动来源：1-平台；2-商家App")
    private Integer source;
    @ApiModelProperty(value = "活动来源：1-平台；2-商家App")
    private String sourceStr;
    @ApiModelProperty(value = "秒杀商品品种")
    private Integer goodsNumber;
    @ApiModelProperty(value = "活动状态:1-未开始；2-进行中；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "活动状态")
    private String statusStr;
}
