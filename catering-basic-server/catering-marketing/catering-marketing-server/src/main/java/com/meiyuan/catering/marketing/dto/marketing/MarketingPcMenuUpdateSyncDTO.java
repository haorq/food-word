package com.meiyuan.catering.marketing.dto.marketing;

import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialSkuEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/08/26 11:08
 * @description 商户PC端修改销售菜单，返回的营销活动商品相关信息
 **/

@Data
@ApiModel(value = "商户PC端修改销售菜单，修改了数据库后返回的DTO")
public class MarketingPcMenuUpdateSyncDTO {

    @ApiModelProperty(value = "需要删除的营销商品ID集合")
    private List<Long> delMarketingGoodsIdList;
    @ApiModelProperty(value = "需要还原的营销商品ID集合")
    private List<Long> returnMarketingGoodsIdList;

    @ApiModelProperty(value = "营销特价商品活动当前的所有商品ID集合")
    private Set<Long> specialGoodsIds;
    @ApiModelProperty(value = "需要删除特价商品信息的商品SKU编码")
    private Set<String> delSpecialGoodsSkuCodes;
    @ApiModelProperty(value = "需要重新设置特价信息的商品SKU信息")
    private List<MarketingPcMenuUpdateSpecialReturnDTO> returnSpecialGoodsSkuList;

}