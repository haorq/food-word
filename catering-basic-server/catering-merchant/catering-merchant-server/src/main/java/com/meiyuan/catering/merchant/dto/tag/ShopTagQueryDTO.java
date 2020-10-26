package com.meiyuan.catering.merchant.dto.tag;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/3/18 14:20
 **/
@Data
@ApiModel("店铺标签查询条件")
public class ShopTagQueryDTO extends BasePageDTO {

    @ApiModelProperty("标签Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
}
