package com.meiyuan.catering.es.dto.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/28 16:28
 * @description 简单描述
 **/
@Data
@ApiModel("微信首页活动查询参数")
public class EsWxIndexMarketingQueryDTO {
    @ApiModelProperty("展示条数")
    private Integer showSize;
    @ApiModelProperty("活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；")
    private Integer ofType;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户",hidden = true)
    private Integer userType;
    private String locationFlag;
    @ApiModelProperty(value = "位置：经度,纬度")
    private String location;
    @ApiModelProperty("市编码")
    private String cityCode;
    @ApiModelProperty("秒杀场次ID")
    private Long eventId;
    public EsWxIndexMarketingQueryDTO(Integer showSize, Integer ofType, Integer userType, String cityCode,String location, Long eventId) {
        this.showSize = showSize;
        this.ofType = ofType;
        this.userType = userType;
        this.cityCode = cityCode;
        this.location = location;
        this.eventId = eventId;
    }
}
