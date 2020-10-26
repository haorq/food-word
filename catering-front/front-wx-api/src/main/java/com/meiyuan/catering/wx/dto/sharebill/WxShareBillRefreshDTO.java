package com.meiyuan.catering.wx.dto.sharebill;

import com.meiyuan.catering.user.dto.cart.CartDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 定时任务刷新拼单推送信息
 * @date 2020/3/2810:19
 * @since v1.0.0
 */
@Data
@ApiModel("定时任务刷新拼单推送信息")
public class WxShareBillRefreshDTO {
    @ApiModelProperty("购物车信息")
    private CartDTO cart;

    @ApiModelProperty("是否取消拼单 true:已取消，false未取消")
    private Boolean cancelBill;
}
