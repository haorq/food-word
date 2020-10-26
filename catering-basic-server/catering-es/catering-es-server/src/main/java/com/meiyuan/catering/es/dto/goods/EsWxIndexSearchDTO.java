package com.meiyuan.catering.es.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wxf
 * @date 2020/4/7 15:28
 * @description 简单描述
 **/
@Data
@ApiModel("微信首页查询数据模型")
@Accessors(chain = true)
public class EsWxIndexSearchDTO {
    @ApiModelProperty("商户id")
    private String merchantId;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("店铺名称")
    private String shopName;
    /**
     * @since v1.4.0
     */
    @ApiModelProperty("商家评分")
    private Double shopGrade;
    /**
     * @since v1.4.0
     */
    @ApiModelProperty("商家月售")
    private Integer ordersNum;
    /**
     * @since v1.4.0
     */
    @ApiModelProperty("商户标签")
    private List<String> shopTag;
    /**
     * @since v1.4.0
     */
    @ApiModelProperty("业务支持：1：仅配送，2：仅自提，3：全部【1.3.0】")
    private Integer businessSupport;
    /**
     * @since v1.4.0
     */
    @ApiModelProperty("营业状态：1-营业 2-打样")
    private Integer businessStatus;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("品牌属性:1:自营，2:非自营")
    private Integer merchantAttribute;
    @ApiModelProperty("商户名称")
    private String merchantName;
    @ApiModelProperty("经纬度")
    private String location;
    @ApiModelProperty("订单起送金额")
    private BigDecimal leastDeliveryPrice;
    @ApiModelProperty("配送费")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("门店LOGO图片")
    private String doorHeadPicture;
    @ApiModelProperty("商户对应商品集合")
    private List<EsSimpleGoodsInfoDTO> goodsList;

}
