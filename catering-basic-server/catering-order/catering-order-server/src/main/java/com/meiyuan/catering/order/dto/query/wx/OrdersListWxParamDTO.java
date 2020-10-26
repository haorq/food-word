package com.meiyuan.catering.order.dto.query.wx;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单列表请求参数——微信端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单列表请求参数——微信端")
public class OrdersListWxParamDTO extends BasePageDTO {
    @ApiModelProperty(value = "【非填-系统自行填充】会员ID",hidden = true)
    private Long memberId;
    @ApiModelProperty("订单状态：-1[负一]-全部；1-待评价；2-售后")
    private Integer orderStatus;
    @ApiModelProperty("关键词：（订单编号；店铺名称；用户姓名、手机）")
    private String keyWord;
}
