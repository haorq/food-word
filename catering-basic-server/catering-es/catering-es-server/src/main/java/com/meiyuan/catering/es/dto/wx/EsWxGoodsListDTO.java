package com.meiyuan.catering.es.dto.wx;

import com.meiyuan.catering.es.dto.goods.EsGoodsCategoryAndLabelDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/20 15:27
 * @description 简单描述
 **/
@Data
@Accessors(chain = true)
public class EsWxGoodsListDTO {
    @ApiModelProperty("商品id")
    private String goodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品列表图片")
    private String listPicture;
    @ApiModelProperty("商品分类id")
    private String categoryId;
    @ApiModelProperty("原价(市场价)")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价")
    private BigDecimal salesPrice;
    @ApiModelProperty("标签集合")
    private List<EsGoodsCategoryAndLabelDTO> labelList;
    @ApiModelProperty("商家id")
    private String merchantId;
    @ApiModelProperty("商家名称")
    private String merchantName;
    @ApiModelProperty("商家名称【1.2.0】")
    private String shopName;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("品牌属性:1:自营，2:非自营")
    private Integer merchantAttribute;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("门店LOGO图片")
    private String doorHeadPicture;
    @ApiModelProperty("商家评分")
    private Double shopGrade;
    @ApiModelProperty("商家月售")
    private Integer ordersNum;
    @ApiModelProperty("商家距离")
    private String location;

}
