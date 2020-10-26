package com.meiyuan.catering.merchant.dto.shop.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("多列排序DTO")
public class BillListSortDTO {

    //7.平台优惠抵扣，8.商家优惠抵扣，9：商家实收，
    @ApiModelProperty("排序方式：1：默认(品牌+门店创建时间排序)，2：门店名，3：品牌名，4：有效订单数，5：订单总额，6.订单实收，7.平台优惠抵扣，8.商家优惠抵扣，9：商家实收，10：退款额")
    private Integer sortType;

    @ApiModelProperty("排序升/降序：1：降序，2：升序")
    private Integer sortBy;

}
