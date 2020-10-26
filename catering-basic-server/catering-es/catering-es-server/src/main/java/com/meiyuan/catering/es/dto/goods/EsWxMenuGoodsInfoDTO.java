package com.meiyuan.catering.es.dto.goods;

import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.enums.DataType;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/4/1 17:31
 * @description 简单描述
 **/
@Data
public class EsWxMenuGoodsInfoDTO {


    @ESMapping(datatype = DataType.keyword_type)
    private String goodsId;
    @ESMapping(datatype = DataType.keyword_type)
    private String goodsName;
}
