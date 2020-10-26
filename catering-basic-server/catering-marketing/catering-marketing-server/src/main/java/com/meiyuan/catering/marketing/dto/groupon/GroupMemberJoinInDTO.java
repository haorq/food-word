package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luohuan
 * @date 2020/3/27
 **/
@Data
@ApiModel("新用户加入拼团DTO")
public class GroupMemberJoinInDTO extends GroupMemberCommonDTO {
    @ApiModelProperty("营销商品表主键id")
    private Long mGoodsId;

    @ApiModelProperty("商品购买数量")
    private Integer goodsNumber;

}
