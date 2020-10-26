package com.meiyuan.catering.marketing.dto.special;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品创建/编辑DTO
 **/

@Data
@ApiModel(value = "营销特价商品创建/编辑DTO")
public class MarketingSpecialAddOrEditDTO {

    @ApiModelProperty(value = "特价商品活动ID 编辑必传")
    private Long id;
    @ApiModelProperty(value = "活动名称", required = true)
    @NotBlank(message = "活动名称不能为空")
    private String name;
    @ApiModelProperty(value = "活动开始时间", required = true)
    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间", required = true)
    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动目的")
    private String activityTarget;
    @ApiModelProperty(value = "拉新目标")
    private Integer userTarget;
    @ApiModelProperty(value = "营业目标")
    private BigDecimal businessTarget;
    @ApiModelProperty(value = "活动描述")
    private String description;
    @ApiModelProperty(value = "定价方式 1-统一折扣 2-折扣 3-固定价", required = true)
    @NotNull(message = "定价方式不能为空")
    private Integer fixType;
    @ApiModelProperty(value = "统一折扣(当定价方式为统一折扣时有值)")
    private BigDecimal unifySpecialNumber;
    @ApiModelProperty(value = "商品信息")
    private List<MarketingSpecialGoodsDTO> goodsList;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID", hidden = true)
    private Long shopId;
    /**
     * 商户ID
     */
    @ApiModelProperty(value = "商户ID", hidden = true)
    private Long merchantId;

    /**
     * 登录的账户ID
     */
    @ApiModelProperty(value = "登录的账户ID", hidden = true)
    private Long accountId;

}
