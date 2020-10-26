package com.meiyuan.catering.wx.dto.index;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述:
 *
 * @author zengzhangni
 * @date 2020/9/2 9:31
 * @since v1.4.0
 */
@Data
public class WxCategoryVO implements Serializable {

    @ApiModelProperty("小程序类目列表")
    private List<WxCategoryDTO> categoryList;

    @ApiModelProperty("小程序分类列表")
    private List<WxCategoryDTO> typeList;

}
