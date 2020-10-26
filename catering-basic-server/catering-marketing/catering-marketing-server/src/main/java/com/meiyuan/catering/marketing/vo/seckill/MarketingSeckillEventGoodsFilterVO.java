package com.meiyuan.catering.marketing.vo.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/13 19:08
 * @description 秒杀活动中场次与商品关系信息VO
 **/

@Data
@ApiModel(value = "秒杀活动中场次与商品关系信息VO")
public class MarketingSeckillEventGoodsFilterVO {

    @ApiModelProperty("秒杀活动ID")
    private Long seckillId;
    @ApiModelProperty("场次ID")
    private Long eventId;
    @ApiModelProperty("场次时间")
    private String eventTime;
    @ApiModelProperty("营销商品ID")
    private String mGoodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品SKU")
    private String goodsSku;
    @ApiModelProperty("商品SKU值")
    private String goodsSkuValue;

}
