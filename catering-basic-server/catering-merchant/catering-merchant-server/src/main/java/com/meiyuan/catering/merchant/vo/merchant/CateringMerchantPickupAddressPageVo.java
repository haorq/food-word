package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 自提列表分页查询VO
 * @Date  2020/3/12 0012 10:32
 */
@Data
@ApiModel("自提列表分页查询VO")
public class CateringMerchantPickupAddressPageVo {
    @ApiModelProperty("自提点id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("自提点名称")
    private String shopName;
    @ApiModelProperty("联系人")
    private String primaryPersonName;
    @ApiModelProperty("电话")
    private String registerPhone;

    @ApiModelProperty("省")
    private String addressProvince;
    @ApiModelProperty("市")
    private String addressCity;
    @ApiModelProperty("区")
    private String addressArea;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("自提点状态:1：启用，2：禁用")
    private Integer shopStatus;

    @ApiModelProperty("自提点绑定状态:1：绑定，2：未绑定")
    private Integer shopBoundStatus;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("经纬度")
    private String mapCoordinate;


}
