package com.meiyuan.catering.marketing.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/06 18:08
 * @description 秒杀活动整体活动数据VO
 **/

@Data
@ApiModel(value = "秒杀活动效果-整体活动数据VO")
public class MarketingSeckillEffectVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀活动ID")
    private Long id;
    @ApiModelProperty(value = "预计成本")
    private BigDecimal projectedCost;
    @ApiModelProperty(value = "实际成本")
    private BigDecimal realCost;
    @ApiModelProperty(value = "预计拉新")
    private Integer projectedPullNew;
    @ApiModelProperty(value = "实际拉新")
    private Integer realPullNew;
    @ApiModelProperty(value = "预计增长营业额")
    private BigDecimal projectedBusiness;
    @ApiModelProperty(value = "实际增长营业额")
    private BigDecimal realBusiness;
    @ApiModelProperty(value = "关联订单总数量")
    private Integer relationOrderCount;
    @ApiModelProperty(value = "商品销售额")
    private BigDecimal business;
    @ApiModelProperty(value = "商品列表")
    private List<MarketingSeckillGoodsEffectVO> goodsList;

}
