package com.meiyuan.catering.es.dto.merchant;

import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.enums.DataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商户基础信息
 * @author wxf
 * @date 2020/5/26 11:01
 * @description 商户基础信息
 **/
@Data
public class EsMerchantBaseInfoDTO {
    /**
     * 店铺id
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

    @ESMapping(datatype = DataType.keyword_type)
    @ApiModelProperty("门店名称")
    private String shopName;
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
}
