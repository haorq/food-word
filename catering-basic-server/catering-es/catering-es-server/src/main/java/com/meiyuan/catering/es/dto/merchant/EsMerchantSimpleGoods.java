package com.meiyuan.catering.es.dto.merchant;

import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.enums.DataType;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/6/8 10:10
 * @description es商户索引简单的商品信息
 **/
@Data
public class EsMerchantSimpleGoods {
    @ESMapping(datatype = DataType.text_type)
    private String goodsId;
    @ESMapping(datatype = DataType.keyword_type)
    private String goodsName;
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsStatus;

}
