package com.meiyuan.catering.marketing.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author luohuan
 * @date 2020/3/18
 **/
@Data
@ApiModel("团购详情VO")
@NoArgsConstructor
public class GrouponDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("活动ID")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动对象（0：全部，1：个人，2：企业）")
    private Integer objectLimit;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("上架/下架（1：下架，2：上架）")
    private Integer upDown;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商家ID")
    private Long merchantId;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "店铺Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty("商品列表")
    private List<GrouponGoodsListVO> grouponGoodsList;
    /**
     * 是否支持虚拟成团
     */
    @ApiModelProperty("是否支持虚拟成团")
    private Boolean virtualGroupon;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    public GrouponDetailVO(CateringMarketingGrouponEntity grouponEntity) {
        this.setId(grouponEntity.getId());
        this.setName(grouponEntity.getName());
        this.setObjectLimit(grouponEntity.getObjectLimit());
        this.setBeginTime(grouponEntity.getBeginTime());
        this.setEndTime(grouponEntity.getEndTime());
        this.setUpDown(grouponEntity.getUpDown());
        this.setVirtualGroupon(grouponEntity.getVirtualGroupon());
        this.setCreateTime(grouponEntity.getCreateTime());
    }
}
