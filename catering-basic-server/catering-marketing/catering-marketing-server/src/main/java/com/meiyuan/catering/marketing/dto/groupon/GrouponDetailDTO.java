package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/5/19
 **/
@Data
public class GrouponDetailDTO {

    @ApiModelProperty("团购ID")
    private Long id;

    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("店铺id")
    private Long shopId;
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("活动对象（0：全部，1：个人，2：企业）")
    private Integer objectLimit;


    @ApiModelProperty("数据来源：1-平台；2-商家")
    private Integer source;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("上架/下架（1：下架，2：上架）")
    private Integer upDown;

    @ApiModelProperty("是否支持虚拟成团")
    private Boolean virtualGroupon;

    @ApiModelProperty("活动描述")
    private String description;

    @ApiModelProperty("是否删除")
    private Boolean del;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人")
    private Long updateBy;
}
