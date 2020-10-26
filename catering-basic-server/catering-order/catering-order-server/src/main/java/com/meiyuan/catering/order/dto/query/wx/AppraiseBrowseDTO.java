package com.meiyuan.catering.order.dto.query.wx;

import com.meiyuan.catering.order.dto.WxUserTokenDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单评价浏览记录
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("订单评价浏览记录——微信")
public class AppraiseBrowseDTO extends WxUserTokenDTO implements Serializable {
    private static final long serialVersionUID = -18183696017546575L;

    @ApiModelProperty(value = "评价id", required = true)
    private Long id;
}
