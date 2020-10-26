package com.meiyuan.catering.marketing.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/07 11:08
 * @description 团购活动详情VO
 **/

@Data
@ApiModel(value = "团购活动详情VO")
public class MarketingGrouponDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "活动ID")
    private Long id;
    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty(value = "活动类型 （3:商品团购  固定为3）")
    private Integer marketingType;
    @ApiModelProperty(value = "活动状态（1：未进行 2：进行中 3：已结束 4：已冻结）")
    private Integer marketingStatus;
    @ApiModelProperty(value = "是否支持虚拟成团 false-不支持 true-支持")
    private Boolean virtualGroupon;
    @ApiModelProperty(value = "活动对象（0：全部，1：个人，2：企业）")
    private List<String> objectLimit;
    @ApiModelProperty(value = "开始日期")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动目的V1.3.0")
    private String activityTarget;
    private Integer userTarget;
    private BigDecimal businessTarget;
    @ApiModelProperty(value = "活动描述")
    private String description;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("商品列表")
    private List<MarketingGrouponGoodsDetailVO> goodsList;

    @ApiModelProperty(value = "拉新目标V1.3.0")
    public String getUserTarget() {
        return userTarget == null || userTarget == 0 ? "" : userTarget.toString();
    }
    @ApiModelProperty(value = "营业目标V1.3.0")
    public String getBusinessTarget() {
        return businessTarget == null || businessTarget.compareTo(new BigDecimal(0)) == 0 ? "" : businessTarget.setScale(0, BigDecimal.ROUND_DOWN).toString();
    }
}
