package com.meiyuan.catering.goods.dto.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.goods.SimpleGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/11 14:39
 * @description 新增/查看 的分类DTO
 **/
@Data
@ApiModel("新增/查看分类模型")
public class CategoryDTO {
    @ApiModelProperty("id,新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("分类名称")
    private String categoryName;
    @ApiModelProperty("默认标签 1-新增 2-默认")
    private Integer defaultCategory;
    @ApiModelProperty("菜品数量")
    private Integer goodsCount;
    @ApiModelProperty("类目图片")
    private String categoryPicture;
    @ApiModelProperty("类目描述")
    private String categoryDescribe;
    @ApiModelProperty("商品信息")
    private List<SimpleGoodsDTO> simpleGoodsDTO;
}
