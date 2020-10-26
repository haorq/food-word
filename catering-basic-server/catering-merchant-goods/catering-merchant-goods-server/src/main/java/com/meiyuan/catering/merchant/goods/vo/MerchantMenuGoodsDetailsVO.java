package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * description：销售菜单详情查看返回参数
 * @author yy
 * @date 2020/7/7
 */
@Data
@ApiModel("销售菜单-详情查看")
public class MerchantMenuGoodsDetailsVO {

    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "门店集合")
    private List<GoodPushShopVo> shopList;

    @ApiModelProperty(value = "商品集合")
    private List<MerchantGoodsMenuListVO> goodsList;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
