package com.meiyuan.catering.order.dto.query.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单配置表dto
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("订单配置信息——后台")
public class OrdersConfigDTO implements Serializable {
    private static final long serialVersionUID = 3814926256379366614L;
    @ApiModelProperty(value = "主键ID",required = true)
    @NotNull(message = "主键ID不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "配置名称",required = true)
    @NotBlank(message = "配置名称不能为空")
    private String configName;
    @ApiModelProperty(value = "配置key",required = true)
    @NotBlank(message = "配置key不能为空")
    private String configKey;
    @ApiModelProperty(value = "配置value",required = true)
    @NotNull(message = "配置value不能为空")
    private Integer configValue;
    @ApiModelProperty(value = "配置单位（year：年，month：月，day：天，hour：时，minute：分，second：秒）",required = true)
    @NotBlank(message = "配置单位不能为空")
    private String configUnit;
    @ApiModelProperty(value = "备注",required = true)
    @NotBlank(message = "备注不能为空")
    private String remark;

}
