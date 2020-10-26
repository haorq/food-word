package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Date 2020/7/2 0002 18:21
 * @Description 简单描述 : 商户分页查询条件
 * @Since version-1.0.0
 */
@Data
@ApiModel("商户分页查询条件")
public class MerchantQueryPage extends BasePageDTO {

    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("关键字：品牌编号、名称、法人名称、联系方式")
    private String keyword;

    @ApiModelProperty("品牌类型/属性:1:自营，2：非自营")
    private Integer merchantAttribute;

    @ApiModelProperty("品牌/商户状态：1- 禁用 2-启用")
    private Integer merchantStatus;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

}
