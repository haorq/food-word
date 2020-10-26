package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户列表分页查询参数DTO
 * @Date  2020/3/16 0016 14:53
 */
@Data
@ApiModel("商户列表分页查询参数DTO")
public class MerchantLimitQueryDTO extends BasePageDTO {
    @ApiModelProperty("商户id【1.2.0】")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty("关键字：手机号，店铺名称【1.2.0】")
    private String keyword;

    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;

    @ApiModelProperty("省编码")
    private String addressProvinceCode;
    @ApiModelProperty("市编码")
    private String addressCityCode;
    @ApiModelProperty("区编码")
    private String addressAreaCode;

    @ApiModelProperty("门店身份/类型：1--店铺，2--自提点，3--店铺兼自提点[1.1.0]")
    private Integer type;
    @ApiModelProperty("店铺类型：1-店铺 2-店铺兼自提点 3-自提点")
    private Integer shopType;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("标签ids[1.1.0]")
    private List<Long> shopTagIds;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺来源：1- 商户申请 2-平台添加")
    private Integer shopSource;

    @ApiModelProperty("标签id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopTagId;

}
