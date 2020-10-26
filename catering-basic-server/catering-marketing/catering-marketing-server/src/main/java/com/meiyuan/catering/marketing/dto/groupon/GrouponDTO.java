package com.meiyuan.catering.marketing.dto.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@Data
@ApiModel("团购DTO")
public class GrouponDTO {

    @ApiModelProperty("活动ID")
    private Long id;

    @ApiModelProperty("活动名称")
    @NotBlank(message = "活动名称不能为空")
    private String name;

    @ApiModelProperty("活动对象（0：全部，1：个人，2：企业）")
    @NotNull(message = "活动对象不能为空")
    private Integer objectLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty("上架/下架（1：下架，2：上架）")
    private Integer upDown;

    @ApiModelProperty("活动描述")
    private String description;

    @ApiModelProperty("商家ID")
    private Long merchantId;

    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 是否支持虚拟成团
     */
    @ApiModelProperty("是否支持虚拟成团")
    private Boolean virtualGroupon;

    @ApiModelProperty("商品列表")
    @Size(min = 1, message = "商品列表不能为空")
    @Valid
    private List<GrouponGoodsDTO> grouponGoodsList;

}
