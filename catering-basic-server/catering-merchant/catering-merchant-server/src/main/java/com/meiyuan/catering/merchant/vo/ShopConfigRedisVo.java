package com.meiyuan.catering.merchant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 商户店铺配置信息存入reidsVo
 * @Date  2020/3/12 0012 10:32
 */
@Data
@ApiModel("商户店铺配置信息存入reidsVo")
public class ShopConfigRedisVo {
    @ApiModelProperty("是否自动接单:1-自动 2-不自动")
    private Integer autoReceipt;
    @ApiModelProperty("配送对象 :1-企业 2-个人 3-全部")
    private Integer deliveryObject;
    @ApiModelProperty("配送价格")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("配送范围")
    private String deliveryRange;
    @ApiModelProperty("配送规则：1-距离 2-固定")
    private Integer deliveryRule;
    @ApiModelProperty("到店自提：1-支持 2-不支持")
    private Integer shopYourself;
    @ApiModelProperty("订单接收过期时间")
    private LocalDateTime orderOvertime;

}
