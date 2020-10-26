package com.meiyuan.catering.merchant.goods.dto.es;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lhm
 * @date 2020/7/13
 * @description
 **/
@Data
public class ShopGoodsSkuDTO {

    private Long goodsId;

    private List<String> skuCodes;


}
