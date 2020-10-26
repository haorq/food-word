package com.meiyuan.catering.order.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 订单流水列表查询入参
 *
 * @author lh
 */

@Data
public class OrderForMerchantPcParamDto extends BasePageDTO {

    @ApiModelProperty("订单创建日期开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderStartDate;
    @ApiModelProperty("订单创建日期结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderEndDate;
    @ApiModelProperty("订单状态")
    private Integer orderStatus;
    @ApiModelProperty("退款状态，商家审核结果（1：待审核，2：通过；3：拒绝）")
    private Integer refundStatus;
    @ApiModelProperty("订单类型")
    private Integer orderType;
    @ApiModelProperty("门店ID")
    private Long shopId;
    @ApiModelProperty("品牌ID")
    private Long merchantId;
    @ApiModelProperty("搜索关键字（单号，客户手机，姓名）")
    private String keyWord;

}
