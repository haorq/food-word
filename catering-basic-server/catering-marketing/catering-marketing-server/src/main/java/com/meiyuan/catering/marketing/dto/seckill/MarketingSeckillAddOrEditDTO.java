package com.meiyuan.catering.marketing.dto.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/06 10:08
 * @description 营销秒杀活动创建DTO
 **/

@Data
@ApiModel("营销秒杀活动DTO")
public class MarketingSeckillAddOrEditDTO {

    @ApiModelProperty(value = "秒杀活动ID 编辑必传")
    private Long id;
    @ApiModelProperty(value = "活动对象：0-全部；1-个人；2-企业", required = true)
    @NotNull(message = "活动对象不能为空")
    private List<Integer> objectLimit;
    @ApiModelProperty(value = "活动名称", required = true)
    @NotBlank(message = "活动名称不能为空")
    private String name;
    @ApiModelProperty(value = "活动开始时间", required = true)
    @NotNull(message = "活动开始时间不能为空")
    private LocalDate beginTime;
    @ApiModelProperty(value = "活动结束时间", required = true)
    @NotNull(message = "活动结束时间不能为空")
    private LocalDate endTime;
    @ApiModelProperty(value = "活动场次，场次ID集合V1.3.0", required = true)
    @NotNull(message = "活动场次ID集合不能为空")
    private List<Long> eventIds;
    @ApiModelProperty(value = "活动目的V1.3.0")
    private String activityTarget;
    @ApiModelProperty(value = "拉新目标V1.3.0")
    private Integer userTarget;
    @ApiModelProperty(value = "营业目标V1.3.0")
    private BigDecimal businessTarget;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "商品item")
    @Size(min = 1, message = "商品item不能为空")
    @Valid
    private List<MarketingSeckillGoodsDTO> goodsList;


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
     * 登录的账户ID
     */
    @ApiModelProperty(value = "登录的账户ID", hidden = true)
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

    public LocalDateTime getBeginTime() {
        return beginTime.atTime(0,0,0);
    }

    public LocalDateTime getEndTime() {
        return endTime.atTime(23,59,59);
    }
}
