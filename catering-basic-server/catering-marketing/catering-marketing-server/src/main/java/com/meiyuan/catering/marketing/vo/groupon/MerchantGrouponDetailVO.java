package com.meiyuan.catering.marketing.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author luohuan
 * @date 2020/3/18
 **/
@Data
@ApiModel("团购详情VO")
@NoArgsConstructor
public class MerchantGrouponDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "活动ID")
    private Long id;
    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty(value = "活动类型 （3:商品团购  固定为3）")
    private Integer marketingType=3;
    @ApiModelProperty(value = "活动状态（1：未进行 2：进行中 3：已结束 4：已冻结）")
    private Integer status;
    @ApiModelProperty("活动状态")
    private String statusStr;
    @ApiModelProperty(value = "是否支持虚拟成团 false-不支持 true-支持")
    private Boolean virtualGroupon;
    @ApiModelProperty(value = "活动对象（0：全部，1：个人，2：企业）")
    private Integer objectLimit;
    @ApiModelProperty("数据来源：1-平台；2-商家")
    private Integer source;
    @ApiModelProperty("数据来源")
    private String sourceStr;
    @ApiModelProperty(value = "开始日期")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动目的V1.3.0")
    private String activityTarget;
    @ApiModelProperty(value = "拉新目标V1.3.0")
    private Integer userTarget;
    @ApiModelProperty(value = "营业目标V1.3.0")
    private BigDecimal businessTarget;
    @ApiModelProperty(value = "活动描述")
    private String description;
    @ApiModelProperty("商品列表")
    private List<MerchantGrouponGoodsListVO> grouponGoodsList;
    @ApiModelProperty("活动数据--v1.3.0")
    private MarketingGrouponEffectVO effectVOS;

    public MerchantGrouponDetailVO(CateringMarketingGrouponEntity grouponEntity) {
        this.setId(grouponEntity.getId());
        this.setName(grouponEntity.getName());
        this.setObjectLimit(grouponEntity.getObjectLimit());
        this.setBeginTime(grouponEntity.getBeginTime());
        this.setEndTime(grouponEntity.getEndTime());
        this.setSource(grouponEntity.getSource());
        this.setVirtualGroupon(grouponEntity.getVirtualGroupon());
    }
}
