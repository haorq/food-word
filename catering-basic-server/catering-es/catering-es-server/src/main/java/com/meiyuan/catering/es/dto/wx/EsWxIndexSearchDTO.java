package com.meiyuan.catering.es.dto.wx;

import com.meiyuan.catering.es.dto.goods.EsSimpleGoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/31 16:32
 * @description 简单描述
 **/
@Data
@ApiModel("微信首页搜索数据模型")
public class EsWxIndexSearchDTO {
    @ApiModelProperty(value = "id", hidden = true)
    private String id;
    @ApiModelProperty("商户id")
    private String merchantId;
    @ApiModelProperty("商户名称")
    private String merchantName;
    @ApiModelProperty("门店LOGO图片")
    private String doorHeadPicture;
    @ApiModelProperty("经纬度")
    private String location;
    @ApiModelProperty("订单起送金额")
    private BigDecimal leastDeliveryPrice;
    @ApiModelProperty("配送费")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("商品集合")
    private List<EsSimpleGoodsInfoDTO> goodsList;
}
