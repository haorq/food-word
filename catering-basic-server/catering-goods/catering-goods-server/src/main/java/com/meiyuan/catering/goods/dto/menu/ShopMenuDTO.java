package com.meiyuan.catering.goods.dto.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.goods.GoodsNameAndIdDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/20 10:19
 * @description 简单描述
 **/
@Data
@ApiModel("店铺菜单")
public class ShopMenuDTO {
    @ApiModelProperty("菜单id 新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("类目名称")
    private String categoryName;
}
