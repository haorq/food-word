package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/7/6
 * @description
 **/
@Data
@ApiModel("品牌授权商品查询 dto")
public class GoodsPushListQueryDTO  extends BasePageDTO {
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
}
