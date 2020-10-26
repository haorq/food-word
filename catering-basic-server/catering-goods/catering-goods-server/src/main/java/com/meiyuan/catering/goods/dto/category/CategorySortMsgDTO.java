package com.meiyuan.catering.goods.dto.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/6/1 11:26
 * @description 简单描述
 **/
@Data
@ApiModel("分类排序消息数据模型")
public class CategorySortMsgDTO {
    @ApiModelProperty("验证标识 true-没有重复的排序 false-有重复的排序")
    private Boolean flag;
    @ApiModelProperty("返回的提升语， flag = true 则没有提示语")
    private String msg;
    public CategorySortMsgDTO() {

    }
    public CategorySortMsgDTO(Boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }
}
