package com.meiyuan.catering.marketing.dto.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.dto.MarketingGoodsAddDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingSeckillAddDTO
 * @Description 新增秒杀DTO
 * @Author gz
 * @Date 2020/3/16 13:31
 * @Version 1.1
 */
@Data
public class MarketingSeckillAddDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;
    @ApiModelProperty(value = "活动开始时间")
    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动对象：0-全部；1-个人；2-企业")
    @NotNull(message = "活动对象不能为空")
    private Integer objectLimit;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "订单作废时长")
    private Integer orderCancellationTime;
    @ApiModelProperty(value = "商品item")
    private List<MarketingGoodsAddDTO> goodsItem;
    @ApiModelProperty(value = "是否上架")
    private Integer upDownState;
    @ApiModelProperty(value = "商家ID")
    @NotNull(message = "商家ID不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    @ApiModelProperty(value = "店铺ID")
    @NotNull(message = "店铺ID不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

}
