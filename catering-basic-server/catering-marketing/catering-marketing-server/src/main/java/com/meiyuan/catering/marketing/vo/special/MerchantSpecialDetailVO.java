package com.meiyuan.catering.marketing.vo.special;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/03 20:09
 * @description 商户APP端营销特价商品详情VO
 **/

@Data
@ApiModel(value = "商户APP端营销特价商品详情VO")
public class MerchantSpecialDetailVO {

    @ApiModelProperty(value = "活动基本信息")
    private MerchantSpecialDetailBaseVO baseInfo;
    @ApiModelProperty(value = "商品信息集合")
    private List<MerchantSpecialDetailGoodsVO> goodsList;
    @ApiModelProperty(value = "活动效果数据")
    private MerchantSpecialDetailEffectVO effect;

}
