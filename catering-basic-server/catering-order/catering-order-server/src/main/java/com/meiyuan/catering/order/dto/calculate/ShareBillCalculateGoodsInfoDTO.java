package com.meiyuan.catering.order.dto.calculate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 拼单计算商品信息——微信
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ToString(callSuper = true)
@ApiModel("拼单计算商品信息——微信端展示")
public class ShareBillCalculateGoodsInfoDTO {
    @ApiModelProperty("拼单用户")
    WxBillUserDTO user;
    @ApiModelProperty("用户拼单商品")
    List<OrdersCalculateGoodsInfoDTO> goodsInfo;
}
