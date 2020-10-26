package com.meiyuan.catering.es.dto.wx;

import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.enums.DataType;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/5/20 14:43
 * @description 首页分类对应的商品的关系DTO
 **/
@Data
public class EsIndexCategoryGoodsRelationDTO {
    @ESMapping(datatype = DataType.text_type)
    private String indexCategoryId;
    @ESMapping(datatype = DataType.text_type)
    private String goodsIdOrMerchantId;
}
