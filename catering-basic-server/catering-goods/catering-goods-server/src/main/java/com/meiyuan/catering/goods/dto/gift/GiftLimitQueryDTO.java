package com.meiyuan.catering.goods.dto.gift;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/3/20 10:48
 * @description 简单描述
 **/
@Data
@ApiModel("赠品列表分页查询参数模型")
public class GiftLimitQueryDTO extends BasePageDTO {
    @ApiModelProperty("赠品名称/编码")
    private String giftNameCode;
    @ApiModelProperty("赠品状态 1-禁用 2-启用")
    private Integer giftStatus;
}
