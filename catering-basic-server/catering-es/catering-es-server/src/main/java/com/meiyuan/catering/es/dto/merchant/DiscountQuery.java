package com.meiyuan.catering.es.dto.merchant;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/9/3 11:31
 * @since v1.1.0
 */
@Builder
@Data
public class DiscountQuery extends BasePageDTO {

    private String cityCode;

    private Integer deliveryType;
    private Integer activityType;
    @ApiModelProperty("指定查询的门店id集合, null 查询所有")
    private List<String> appointShopIds;

    private Integer ofType;
    private Integer objectLimit;

}
