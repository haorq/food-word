package com.meiyuan.catering.core.dto.es;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * describe: 商品分类/标签信息模型
 * @author: zengzhangni
 **/
@Data
@ApiModel("商品分类/标签信息模型")
public class MerchantBaseLabel {

    private Long id;
    private Long goodsId;
    private String name;

    public MerchantBaseLabel() {
    }

    public MerchantBaseLabel(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
