package com.meiyuan.catering.marketing.dto.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName MarketingSeckillListDTO
 * @Description
 * @Author gz
 * @Date 2020/3/16 18:01
 * @Version 1.1
 */
@Data
public class MarketingSeckillListDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "已售数量")
    private Integer soldOut;
    @ApiModelProperty(value = "活动状态:1-进行中；2-未开始；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "上下架状态")
    private Integer upDownState;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("商家名称")
    private String merchantName;
    @ApiModelProperty("店铺名称")
    private String shopName;

}
