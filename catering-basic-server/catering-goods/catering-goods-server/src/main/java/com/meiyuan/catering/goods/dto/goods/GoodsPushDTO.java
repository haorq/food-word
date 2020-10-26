package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author lhm
 * @date 2020/7/3
 * @description
 **/
@Data
public class GoodsPushDTO {
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @Size(min = 1,message="请至少选择一个商品")
    @NotNull(message = "请至少选择一个商品")
    @ApiModelProperty("推送商户id集合")
    private List<Long> goodsIdList;

}
