package com.meiyuan.catering.goods.dto.gift;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wxf
 * @date 2020/3/20 10:40
 * @description 简单描述
 **/
@Data
@ApiModel("新增/查看 赠品模型")
public class GiftDTO {
    @ApiModelProperty("赠品id 新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("赠品编码")
    private String giftCode;
    @ApiModelProperty("赠品名称")
    private String giftName;
    @ApiModelProperty("赠品价值")
    private BigDecimal giftPrice;
    @ApiModelProperty("赠品库存")
    private Long giftStock;
    @ApiModelProperty("赠品图片")
    private String giftPicture;
    @ApiModelProperty("赠品状态 1-禁用 2-启用")
    private Integer giftStatus;
}
