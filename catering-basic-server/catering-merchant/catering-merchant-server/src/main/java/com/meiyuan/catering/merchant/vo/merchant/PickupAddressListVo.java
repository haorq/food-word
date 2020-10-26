package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/16 11:01
 **/
@Data
@ApiModel("C端下单时--自提点列表")
public class PickupAddressListVo implements Serializable {
    @ApiModelProperty("自提点id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("自提点名称")
    private String name;
    @ApiModelProperty("联系人")
    private String chargerName;
    @ApiModelProperty("电话")
    private String chargerPhone;
    @ApiModelProperty("省")
    private String addressProvince;
    @ApiModelProperty("市")
    private String addressCity;
    @ApiModelProperty("区")
    private String addressArea;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;
    @ApiModelProperty("和定位之间的距离")
    private String countDistance;
    @ApiModelProperty("和定位之间的距离(m)")
    private Double distance;



}
