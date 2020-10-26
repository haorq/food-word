package com.meiyuan.catering.es.dto.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/6/5 10:50
 * @description es商户相关查询通用queryBuilder字段参数类
 **/
@Data
public class EsMerchantCommonQueryBuilderDTO {
    @ApiModelProperty("城市编码")
    private String cityCode;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("商户id集合")
    private List<String> merchantIdList;
    @ApiModelProperty("店铺id集合【1.2.0】")
    private List<String> shopIdList;
    @ApiModelProperty("微信首页搜索名称")
    private String wxIndexSearchName;
    @ApiModelProperty("商家是否有数据")
    private Boolean haveGoodsFlag;
}
