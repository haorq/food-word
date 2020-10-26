package com.meiyuan.catering.marketing.vo.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/12 10:08
 * @description  团购活动组团情况VO
 **/

@Data
@ApiModel(value = "团购活动组团情况VO")
public class MarketingGrouponGroupInfoVo {

    @ApiModelProperty(value = "已成团数量")
    private Integer finishGroup;
    @ApiModelProperty(value = "未成团数量")
    private Integer notGroup;
    @ApiModelProperty(value = "参团总人数")
    private Integer totalGroupMember;

}
