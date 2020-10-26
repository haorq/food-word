package com.meiyuan.catering.merchant.dto.tag;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/3/17 10:35
 **/
@Data
@ApiModel("店铺标签查询条件")
public class ShopTagListDTO  extends BasePageDTO{

    @ApiModelProperty("标签名称")
    private String shopTag;

}
