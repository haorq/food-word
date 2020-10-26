package com.meiyuan.catering.marketing.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/10 14:02
 */
@Data
@ApiModel("活动效果-参与平台活动品牌列表")
public class ActivityMerchantPageVO {

    @ApiModelProperty(value = "商户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty(value = "品牌名称")
    private String merchantName;

    @ApiModelProperty(value = "接受时间-创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "品牌属性")
    private Integer merchantAttribute;

    @ApiModelProperty(value = "品牌属性中文")
    private String merchantAttributeStr;

}
