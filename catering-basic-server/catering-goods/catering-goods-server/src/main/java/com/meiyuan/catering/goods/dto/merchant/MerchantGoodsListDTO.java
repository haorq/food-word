package com.meiyuan.catering.goods.dto.merchant;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsListDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author wxf
 * @date 2020/4/3 17:18
 * @description 简单描述
 **/
@Data
@ApiModel("商户商品列表模型")
public class MerchantGoodsListDTO {
    @ApiModelProperty("对应分类集合")
    private List<GoodsCategoryAndLabelDTO> categoryList;
    @ApiModelProperty("商品分页数据")
    private PageData<GoodsListDTO> goodsPage;
}
