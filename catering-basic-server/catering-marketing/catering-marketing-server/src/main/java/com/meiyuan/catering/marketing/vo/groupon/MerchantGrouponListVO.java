package com.meiyuan.catering.marketing.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@Data
@ApiModel("团购列表VO")
public class MerchantGrouponListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("活动ID")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("活动状态（1：未开始，2：进行中，3：已结束）")
    private Integer status;

    @ApiModelProperty("数据来源：1-平台；2-商家")
    private Integer source;
    @ApiModelProperty("活动状态（1：未开始，2：进行中，3：已结束）")
    private String statusStr;

    @ApiModelProperty("数据来源：1-平台；2-商家")
    private String sourceStr;

    @ApiModelProperty("商品品种")
    private Integer goodsNumber;

    @ApiModelProperty("是否开启虚拟成团")
    private Boolean virtualGroupon;

    private Integer upDown;

    public String getVirtualGrouponStr() {
        return this.virtualGroupon ? "虚拟成团" : "";
    }

}
