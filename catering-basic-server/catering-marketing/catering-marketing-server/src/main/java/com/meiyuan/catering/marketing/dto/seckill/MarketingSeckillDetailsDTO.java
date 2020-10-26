package com.meiyuan.catering.marketing.dto.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingSeckillDetails
 * @Description 秒杀详情
 * @Author gz
 * @Date 2020/3/16 17:35
 * @Version 1.1
 */
@Data
public class MarketingSeckillDetailsDTO {
    @ApiModelProperty(value = "id")
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
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "订单作废时长：按秒为单位")
    private Integer orderCancellationTime;
    @ApiModelProperty(value = "上下架状态")
    private Integer upDownState;
    @ApiModelProperty(value = "商品item")
    private List<MarketingSeckillGoodsInfoDTO> goodsItem;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    @ApiModelProperty(value = "店铺Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
