package com.meiyuan.catering.goods.dto.label;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.goods.SimpleGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/12 13:51
 * @description 新增/查看 的标签DTO
 **/
@Data
@ApiModel("新增/查看 的标签模型")
public class LabelDTO {
    @ApiModelProperty("id 新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("标签名称")
    private String labelName;
    @ApiModelProperty("标签描述")
    private String labelDescribe;
    @ApiModelProperty("1-禁用 2-启用")
    private Integer labelStatus;
    @ApiModelProperty("默认标签 1-新增 2-默认")
    private Integer defaultLabel;
    @ApiModelProperty("菜品数量")
    private Integer goodsCount;
    @ApiModelProperty("商品信息")
    private List<SimpleGoodsDTO> simpleGoodsDTO;
    @ApiModelProperty("商户id：平台为1")
    private Long merchantId;
}
