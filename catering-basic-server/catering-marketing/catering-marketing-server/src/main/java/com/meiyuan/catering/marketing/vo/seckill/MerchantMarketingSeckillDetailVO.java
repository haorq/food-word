package com.meiyuan.catering.marketing.vo.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/08 14:08
 * @description APP端秒杀活动详情VO
 **/

@Data
@ApiModel(value = "APP端秒杀活动详情VO")
public class MerchantMarketingSeckillDetailVO {

    @ApiModelProperty(value = "基本信息")
    private MarketingSeckillDetailVO baseInfo;
    @ApiModelProperty(value = "商品集合")
    private List<MarketingSeckillGoodsDetailVO> goodsList;
    @ApiModelProperty(value = "活动效果")
    private MarketingSeckillEffectVO effect;

}
