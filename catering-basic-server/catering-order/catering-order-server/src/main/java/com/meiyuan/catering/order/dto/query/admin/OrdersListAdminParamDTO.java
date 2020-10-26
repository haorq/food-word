package com.meiyuan.catering.order.dto.query.admin;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单列表信息——后台端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单列表请求参数——后台端")
public class OrdersListAdminParamDTO extends BasePageDTO {

    @ApiModelProperty("导出类型 1：订单导出，2：备餐表导出")
    private Integer type;
    @ApiModelProperty("订单状态：-1[负一]：全部；1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消")
    private Integer orderStatus;
    @ApiModelProperty("支付方式： 1：余额支付；2：微信支付； 3：支付宝支付；4：POS刷卡支付；")
    private Integer payWay;
    @ApiModelProperty("配送方式：1：外卖配送，2：到店自取")
    private Integer deliveryWay;
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty("关键词：（用户姓名、手机、订单编号、商家名称、自提点、商品）")
    private String keyWord;
}
