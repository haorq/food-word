package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/07 11:08
 * @description 营销团购活动新增/编辑DTO
 **/

@Data
@ApiModel("营销团购活动新增/编辑DTO")
public class MarketingGrouponAddOrEditDTO {

    @ApiModelProperty(value = "团购活动ID 编辑必传")
    private Long id;
    @ApiModelProperty("活动对象（0：全部，1：个人，2：企业）")
    @NotNull(message = "活动对象不能为空")
    private List<Integer> objectLimit;
    @ApiModelProperty("活动名称")
    @NotBlank(message = "活动名称不能为空")
    private String name;
    @ApiModelProperty("是否支持虚拟成团 false-不支持 true-支持")
    private Boolean virtualGroupon;
    @ApiModelProperty("开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime beginTime;
    @ApiModelProperty("结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动目的V1.3.0")
    private String activityTarget;
    @ApiModelProperty(value = "拉新目标V1.3.0")
    private Integer userTarget;
    @ApiModelProperty(value = "营业目标V1.3.0")
    private BigDecimal businessTarget;
    @ApiModelProperty("活动描述")
    private String description;
    @ApiModelProperty("商品列表")
    @Size(min = 1, message = "商品列表不能为空")
    @Valid
    private List<MarketingGrouponGoodsDTO> goodsList;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID", hidden = true)
    private Long shopId;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称", hidden = true)
    private String shopName;
    /**
     * 商户ID
     */
    @ApiModelProperty(value = "商户ID", hidden = true)
    private Long merchantId;
    /**
     * 商户名称
     */
    @ApiModelProperty(value = "商户名称", hidden = true)
    private String merchantName;

    /**
     * 登录账号ID
     */
    @ApiModelProperty(value = "登录账号ID", hidden = true)
    private Long accountId;

    /**
     * 店铺服务类型 3: wx、堂食都可展示 ，2：堂食展示 ，1：wx端展示
     */
    @ApiModelProperty(value = "店铺服务类型 3: wx、堂食都可展示 ，2：堂食展示 ，1：wx端展示", hidden = true)
    private Integer shopServiceType;
    /**
     * 门店状态 1-启用 2-禁用
     */
    @ApiModelProperty(value = "门店状态 1-启用 2-禁用", hidden = true)
    private Integer shopState;

    /**
     * 商户状态 1-启用 2-禁用
     */
    @ApiModelProperty(value = "商户状态 1-启用 2-禁用", hidden = true)
    private Integer merchantState;

}
