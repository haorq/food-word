package com.meiyuan.catering.es.vo;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/8/6 0006 16:56
 * @Description 简单描述 : 类目-商户列表数据
 * @Since version-1.3.0
 */
@Data
@ApiModel("类目-商户列表数据Vo")
public class WxShopPageVo {

    @ApiModelProperty("商户列表信息")
    private PageData<EsWxMerchantDTO> merchantPage;

    @ApiModelProperty("分类介绍")
    private List<WxGoodCategoryExtVo> wxCategoryExtList;
}
