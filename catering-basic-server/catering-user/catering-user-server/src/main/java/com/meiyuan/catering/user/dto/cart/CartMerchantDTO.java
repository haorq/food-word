package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/5/9
 **/
@Data
public class CartMerchantDTO {
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("拼单号，非拼单不传")
    private String shareBillNo;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "是否为拼单人 true:拼单人,不传默认为发起人")
    private Boolean sharePeople;

    @ApiModelProperty(value = "用户ID", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @ApiModelProperty(value = "用户类别", hidden = true)
    private Integer userType;


    public CartMerchantDTO() {
        this.sharePeople = false;
    }
}
