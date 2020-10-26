package com.meiyuan.catering.es.dto.merchant;

import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.enums.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/5/26 11:14
 * @description 简单描述
 **/
@Data
@ApiModel("ES商户DTO")
public class EsMerchantDTO extends EsMerchantBaseInfoDTO {
    /**
     * 唯一id 对应门店id
     */
    @ApiModelProperty("唯一id 对应门店id")
    private String id;

    /**
     * 商家评分
     */
    @ApiModelProperty("商家评分")
    private Double shopGrade;
    /**
     * 商家月售
     */
    @ApiModelProperty("商家月售")
    private Integer ordersNum;
    /**
     * 商家是否有数据
     */
    @ApiModelProperty("商家是否有数据")
    private Boolean haveGoodsFlag;
    /**
     *  推送给商家的商品数据
     */
    @ESMapping(datatype = DataType.nested)
    @ApiModelProperty("推送给商家的商品数据")
    private List<EsMerchantSimpleGoods> merchantGoodsList;

    /**商户审核状态:1:待上传，2：未审核，3：已通过，4：未通过*/
    @ESMapping(datatype = DataType.integer_type)
    @ApiModelProperty("商户审核状态:1:待上传，2：未审核，3：已通过，4：未通过")
    private Integer auditStatus;

    /**
     * 商户状态：1- 禁用 2-启用
     */
    @ESMapping(datatype = DataType.integer_type)
    @ApiModelProperty("商户状态：1- 禁用 2-启用")
    private Integer merchantStatus;

    /**
     * 店铺服务类型:1;外卖小程序,2:堂食美食城,3，外卖小程序兼堂食美食城
     */
    @ESMapping(datatype = DataType.integer_type)
    @ApiModelProperty("店铺服务类型:1;外卖小程序,2:堂食美食城,3，外卖小程序兼堂食美食城")
    private Integer shopService;

    /**
     * 店铺状态:1：启用，2：禁用
     */
    @ESMapping(datatype = DataType.integer_type)
    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;
//    /**
//     * 商户对应小程序首页类目关系集合
//     */
//    @ApiModelProperty("商户对应小程序首页类目关系集合")
//    private List<EsIndexCategoryGoodsRelationDTO> indexCategoryMerchantList;
}
