package com.meiyuan.catering.merchant.dto.shop.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.HashMap;


/**
 * @Author 何锐
 * @Date 2020/09/01
 * @Description 简单描述 : 对账报表接收参数DTO
 */
@Data
@ApiModel("对账报表接收参数DTO")
public class ShopBillDTO extends BasePageDTO {

    @ApiModelProperty("营业时间： 1：本日(默认)；2：本周； 3：本月；4：自定义；")
    private Integer businessTime;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("门店ID")
    private String shopId;

    @ApiModelProperty("品牌ID")
    private String merchantId;

//    @ApiModelProperty("排序参数")
//    private List<BillListSortDTO> sortParams;
//
//    @ApiModelProperty("排序方式：1：默认(品牌+门店创建时间排序)，2：门店名，3：品牌名，4：有效订单数，5：订单总额，6.订单实收，7.平台优惠抵扣，8.商家优惠抵扣，9：商家实收，10：退款额")
//    private Integer sortType;
//
//    @ApiModelProperty("排序升/降序：1：降序，2：升序")
//    private Integer sortBy;

    @ApiModelProperty("排序方式(Key)：1：默认(品牌+门店创建时间排序)，2：门店名，3：品牌名，4：有效订单数，5：订单总额，6.订单实收，7.平台优惠抵扣，8.商家优惠抵扣，9：商家实收，10：退款额 " +
            "排序升/降序(Value)：1：降序，2：升序"
    )
    HashMap<Integer,Integer> sortParams;

    @ApiModelProperty("城市码")
    private String cityCode;

    private Integer type;

}
