package com.meiyuan.catering.core.dto.base;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
public class ShopListV101DTO extends IdEntity {

    @ApiModelProperty("商家id")
    private String merchantId;
    @ApiModelProperty("商家名称")
    private String shopName;

}
