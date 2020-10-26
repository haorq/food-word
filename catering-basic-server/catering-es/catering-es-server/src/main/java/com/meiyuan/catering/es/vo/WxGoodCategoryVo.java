package com.meiyuan.catering.es.vo;

import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/8/6 0006 16:56
 * @Description 简单描述 : 好物推荐二级页面Vo
 * @Since version-1.3.0
 */
@Data
@ApiModel("好物推荐二级页面Vo")
public class WxGoodCategoryVo {

    @ApiModelProperty("商品信息")
    private List<EsGoodsDTO> esGoodsDto;

    @ApiModelProperty("分类介绍")
    private List<WxGoodCategoryExtVo> wxCategoryExtList;
}
