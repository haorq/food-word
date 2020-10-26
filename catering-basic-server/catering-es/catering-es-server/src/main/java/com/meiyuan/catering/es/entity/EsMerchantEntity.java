package com.meiyuan.catering.es.entity;

import com.meiyuan.catering.es.annotation.ESID;
import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.annotation.ESMetaData;
import com.meiyuan.catering.es.constant.EsIndexConstant;
import com.meiyuan.catering.es.dto.merchant.EsMerchantSimpleGoods;
import com.meiyuan.catering.es.enums.DataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * ES商户索引实体
 *
 * @author wxf
 * @date 2020/5/26 10:47
 * @description ES 商户索引实体
 **/
@Data
@ESMetaData(indexName = EsIndexConstant.MERCHANT, indexType = "merchant")
public class EsMerchantEntity  {
    /**
     * 唯一id 对应门店id
     */
    @ESID
    private String id;

    /**
     * 门店id
     */
    @ESMapping(datatype = DataType.text_type)
    @ApiModelProperty("门店id")
    private String shopId;

    /**
     * 商户id
     */
    @ESMapping(datatype = DataType.text_type)
    @ApiModelProperty("商户id")
    private String merchantId;

    /**
     * 商户名称
     */
    @ESMapping(datatype = DataType.keyword_type)
    @ApiModelProperty("商户名称")
    private String merchantName;
    /**
     * 经纬度 ES中  纬度在前 经度 在后
     */
    @ESMapping(datatype = DataType.geo_point)
    @ApiModelProperty("经纬度 ES中  纬度在前 经度 在后 / 返回给前端 会以 米 字符串返回")
    private String location;
    /**
     * 省
     */
    @ESMapping(datatype = DataType.keyword_type)
    @ApiModelProperty("省")
    private String provinceCode;
    /**
     * 市
     */
    @ESMapping(datatype = DataType.keyword_type)
    @ApiModelProperty("市")
    private String esCityCode;
    /**
     * 区
     */
    @ESMapping(datatype = DataType.keyword_type)
    @ApiModelProperty("区")
    private String areaCode;

    /**
     * 商家评分
     */
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private double shopGrade;
    /**
     * 商家月售
     */
    @ESMapping(datatype = DataType.integer_type, allow_search = false)
    private Integer ordersNum;
    /**
     * 商家是否有数据
     */
    @ESMapping(datatype = DataType.boolean_type)
    private boolean haveGoodsFlag;
    /**
     *  推送给商家的商品数据
     */
    @ESMapping(datatype = DataType.nested)
    private List<EsMerchantSimpleGoods> merchantGoodsList;

    /**商户审核状态:1:待上传，2：未审核，3：已通过，4：未通过*/
    @ESMapping(datatype = DataType.integer_type)
    private Integer auditStatus;

    /**
     * 商户状态：1-启用  2-禁用
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer merchantStatus;

    /**
     * 商户服务:1;外卖小程序,2:堂食美食城,3，外卖小程序兼堂食美食城【1.2.0】
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer shopService;

    /**
     * 店铺状态:1：启用，2：禁用
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer shopStatus;

//    /**
//     * 商户对应小程序首页类目关系集合
//     */
//    @ESMapping(datatype = DataType.nested)
//    private List<EsIndexCategoryGoodsRelationDTO> indexCategoryMerchantList;
}
