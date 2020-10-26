package com.meiyuan.catering.goods.dto.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/20 10:34
 * @description 简单描述
 **/
@Data
@ApiModel("推送菜单 模型")
public class PushMenuDTO {
    @ApiModelProperty("菜单id")
    private Long id;
    @ApiModelProperty("推送商户id集合")
    private List<Long> merchantIdList;
    @ApiModelProperty("固定商家还是全部商家 1-所有商家 2-指定商家")
    private Integer fixedOrAll;
}
