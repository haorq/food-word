package com.meiyuan.catering.es.entity;

import com.meiyuan.catering.es.annotation.ESID;
import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.annotation.ESMetaData;
import com.meiyuan.catering.es.constant.EsIndexConstant;
import com.meiyuan.catering.es.dto.goods.EsGoodsCategoryAndLabelDTO;
import com.meiyuan.catering.es.dto.property.EsGoodsPropertyDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.enums.DataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/23 11:49
 * @description 简单描述
 **/
@Data
@ESMetaData(indexName = EsIndexConstant.GOODS_INDEX, indexType = "goods")
public class EsGoodsEntity {
    /**
     * 商品部分
     * 唯一id 对应数据库推送商家关联表的id
     * 平台新增商品  id为商品表中的ID
     * 商户新增商品  id为商品表中的ID
     * 平台推送商品  id为商品商户关联表中的ID
     */
    @ESID
    private String id;
    /**
     * 商品id
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String goodsId;
    /**
     * 名称
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String goodsName;
    /**
     * 菜品编号
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String spuCode;
    /**
     * 菜品分类id
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String categoryId;
    /**
     * 菜品分类名称
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String categoryName;
    /**
     * 分类商品排序号
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer categoryGoodsSort;

    /**
     * 列表图片
     */
    @ESMapping(datatype = DataType.text_type, allow_search = false)
    private String listPicture;
    /**
     * 详情图片
     */
    @ESMapping(datatype = DataType.text_type, allow_search = false)
    private String infoPicture;

    @ESMapping(datatype = DataType.keyword_type)
    private String goodsSynopsis;

    /**
     * 门店 上下架 1-下架,2-上架
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsStatus;
    /**
     * 商户上下架 1-下架,2-上架
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer merchantGoodsStatus;
    /**
     * 原价(市场价)
     */
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private double marketPrice;
    /**
     * 销售价
     */
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private double salesPrice;
    /**
     * 企业价
     */
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private double enterprisePrice;
    /**
     * @version 1.5.0
     * @author lh
     * @desc 餐盒费
     */
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private double packPrice;
    /**
     * 商品详细介绍
     */
    @ESMapping(datatype = DataType.text_type, allow_search = false)
    private String goodsDescribeText;
    /**
     * 商品规格类型 1-统一规格 2-多规格
     */
    @ESMapping(datatype = DataType.integer_type, allow_search = false)
    private Integer goodsSpecType;

    /**
     *@since v1.2.0  是否预售0-否 1-是
     */
    @ESMapping(datatype = DataType.boolean_type)
    private Boolean presellFlag;
    /**
     *@since v1.2.0  开始售卖时间
     */
    @ESMapping(datatype = DataType.date_type)
    private Date startSellTime;
    /**
     * @since v1.2.0 结束售卖时间
     */
    @ESMapping(datatype = DataType.date_type)
    private Date endSellTime;

    /**
     *@since v1.2.0  星期一到天（1-7）
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String sellWeekTime;

    /**
     *@since v1.2.0  当天截止售卖时间（hh:ss）
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String closeSellTime;

    /**
     * @since v1.2.0 售卖渠道 1.外卖小程序 2:食堂 3.全部
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer salesChannels;

    /**
     *  @since v1.2.0 每单限x份优惠
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer discountLimit;

    /**
     * 创建时间
     */
    @ESMapping(datatype = DataType.date_type)
    private Date createTime;
    /**
     * 商户商品标识 true-是推送给商家的商品 false-单纯的商品信息
     */
    @ESMapping(datatype = DataType.boolean_type)
    private boolean merchantGoodsFlag;

    /**
     * 商户id
     */
    @ESMapping(datatype = DataType.keyword_type)
    @ApiModelProperty("商户id")
    private String merchantId;
    /**
     * 商户名称
     */
    @ESMapping(datatype = DataType.keyword_type)
    @ApiModelProperty("商户名称")
    private String merchantName;

    /**
     * 门店id
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String shopId;
    /**
     * 门店名称
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String shopName;


    /**
     * 商品来源
     */
    @ApiModelProperty("1-平台推送2-商家自创3-门店自创")
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsAddType;
    /**
     * 删除标记
     */
    @ESMapping(datatype = DataType.boolean_type)
    private Boolean del;
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
     * 标签集合
     */
    @ESMapping(datatype = DataType.nested)
    private List<EsGoodsCategoryAndLabelDTO> labelList;
    /**
     * 规格集合
     */
    @ESMapping(datatype = DataType.nested)
    private List<EsGoodsSkuDTO> skuList;
    /**
     * 属性集合
     */
    @ESMapping(datatype = DataType.nested)
    private List<EsGoodsPropertyDTO> propertyList;
    /**
     * 小程序首页分类对应的商品集合
     * @update yaozou 暂时不要，还是通过取缓存的商品ID查询，提取单纯的商品实体
     */
    /*@ESMapping(datatype = DataType.nested)
    private List<EsIndexCategoryGoodsRelationDTO> indexCategoryGoodsList;*/

    @ESMapping(datatype = DataType.boolean_type)
    @ApiModelProperty(value = "V1.4.0 是否是特价商品 false-否 true-是")
    private Boolean specialState;

}
