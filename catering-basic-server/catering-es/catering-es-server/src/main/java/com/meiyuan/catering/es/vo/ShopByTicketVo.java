package com.meiyuan.catering.es.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/8/11 0011 17:06
 * @Description 简单描述 : 通过优惠券查询店铺店铺结果
 * @Since version-1.3.0
 */
@Data
@ApiModel("通过优惠券查询店铺店铺结果")
public class ShopByTicketVo {
    @ApiModelProperty("商户id")
    private String merchantId;
    @ApiModelProperty("品牌属性:1:自营，2:非自营")
    private Integer merchantAttribute;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("商户名称/门店名称")
    private String merchantName;
    @ApiModelProperty("商家评分")
    private Double shopGrade;
    @ApiModelProperty("商家月售")
    private Integer ordersNum;
    @ApiModelProperty("订单起送金额")
    private Double leastDeliveryPrice;
    @ApiModelProperty("配送费")
    private Double deliveryPrice;
    @ApiModelProperty("业务支持：1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;
    @ApiModelProperty("距离")
    private String location;
    @ApiModelProperty("省")
    private String provinceCode;
    @ApiModelProperty("市")
    private String esCityCode;
    @ApiModelProperty("区")
    private String areaCode;
    @ApiModelProperty("商户标签")
    private List<String> shopTag;

    /**
     * 显示顺序：
     * 1.第一，显示行膳平台发的券，『满XX减xx』；多张则并列显示；
     * 2.第二，显示商家店外发券的优惠，『满xx减xx』、『XXX元券』；『XXX元券』为无门槛的券显示形式；多张则并列显示；
     * 3.第三，显示商家店内领券的优惠，『领X元券』；多张则并列显示；
     * 4.第四，显示商品优惠/秒杀/团购商品的折扣，『X.x折』；显示商品的最低折扣；
     */
    @ApiModelProperty(value = "优惠信息")
    private List<String> discountInfo;
}
