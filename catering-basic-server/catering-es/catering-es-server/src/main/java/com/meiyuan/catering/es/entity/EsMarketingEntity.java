package com.meiyuan.catering.es.entity;

import com.meiyuan.catering.es.annotation.ESID;
import com.meiyuan.catering.es.annotation.ESMapping;
import com.meiyuan.catering.es.annotation.ESMetaData;
import com.meiyuan.catering.es.constant.EsIndexConstant;
import com.meiyuan.catering.es.enums.DataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author wxf
 * @date 2020/3/26 15:10
 * @description 简单描述
 **/
@Data
@ESMetaData(indexName = EsIndexConstant.MARKETING, indexType = "marketing")
public class EsMarketingEntity {
    /**
     * 活动商品表主键ID--活动下唯一
     * ES中作为ID
     */
    @ESID
    private String mGoodsId;
    /**
     * 活动id 非唯一 ES中不作为ID
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String id;
    /**
     * 名称
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String name;
    /**
     * 活动开始时间
     */
    @ESMapping(datatype = DataType.date_type)
    private Date beginTime;
    /**
     * 活动结束时间
     */
    @ESMapping(datatype = DataType.date_type)
    private Date endTime;
    /**
     * 活动对象：0-全部；1-个人；2-企业
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer objectLimit;
    /**
     * 活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer ofType;
    /**
     * 商品ID
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String goodsId;
    /**
     * 商品名称
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String goodsName;
    /**
     * 商品市场价
     */
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private double storePrice;
    /**
     * 商品图片-列表图
     */
    @ESMapping(datatype = DataType.text_type, allow_search = false)
    private String goodsPicture;
    /**
     * 商品详情
     */
    @ESMapping(datatype = DataType.text_type,allow_search = false)
    private String goodsDescribeText;
    /**
     * 商品sku
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String sku;
    /**
     * 商品数量/发行数量--库存
     */
    @ESMapping(datatype = DataType.integer_type, allow_search = false)
    private Integer quantity;
    /**
     * 限购数量
     */
    @ESMapping(datatype = DataType.integer_type, allow_search = false)
    private Integer limitQuantity;
    /**
     * 个人-起购数量
     */
    @ESMapping(datatype = DataType.integer_type, allow_search = false)
    private Integer minQuantity;
    /**
     * 起团数量
     */
    @ESMapping(datatype = DataType.integer_type, allow_search = false)
    private Integer minGrouponQuantity;
    /**
     * 活动价
     */
    @ESMapping(datatype = DataType.double_type, allow_search = false)
    private double activityPrice;
    /**
     * 规格值
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String skuValue;
    /**
     * 已售数量
     */
    @ESMapping(datatype = DataType.integer_type, allow_search = false)
    private Integer soldOut;
    /**
     * 剩余库存
     */
    @ESMapping(datatype = DataType.integer_type, allow_search = false)
    private Integer residualInventory;
    /**
     * 上下架状态 1-下架 2-上架
     */
    @ESMapping(datatype = DataType.integer_type)
    private Integer upDownState;
    /**
     * 删除标记
     */
    @ESMapping(datatype = DataType.boolean_type)
    private Boolean del;
    /**
     * 商户id
     */
    @ESMapping(datatype = DataType.text_type)
    private String merchantId;
    /**
     * 商户名称
     */
    @ESMapping(datatype = DataType.keyword_type)
    private String merchantName;
    /**
     * 店铺id
     */
    @ESMapping(datatype = DataType.text_type)
    private String shopId;
    /**
     * 店铺名称
     */
    @ESMapping(datatype = DataType.keyword_type)
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
    @ESMapping(datatype = DataType.text_type)
    private String goodsLabel;

    /**
     * V1.3.0 商品上下架状态 1-下架 2-上架
     */
    @ApiModelProperty(value = "V1.3.0 商品上下架状态")
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsUpDownState;
    /**
     * V1.3.0 商品排序序号
     */
    @ApiModelProperty(value = "V1.3.0 商品排序序号")
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsSort;
    /**
     * V1.3.0 商户状态 1-启用 2-禁用
     */
    @ApiModelProperty(value = "V1.3.0 商户状态 1-启用 2-禁用")
    @ESMapping(datatype = DataType.integer_type)
    private Integer merchantState;
    /**
     * V1.3.0 门店状态 1-启用 2-禁用
     */
    @ApiModelProperty(value = "V1.3.0 门店状态 1-启用 2-禁用")
    @ESMapping(datatype = DataType.integer_type)
    private Integer shopState;
    /**
     * V1.3.0 门店服务类型 3: wx、堂食都可展示 ，2：堂食展示 ，1：wx端展示
     */
    @ApiModelProperty(value = "V1.3.0 门店服务类型 3: wx、堂食都可展示 ，2：堂食展示 ，1：wx端展示")
    @ESMapping(datatype = DataType.integer_type)
    private Integer shopServiceType;
    /**
     * V1.4.0 秒杀场次信息（优化）
     */
    @ApiModelProperty(value = "V1.4.0 秒杀场次信息（优化）")
    @ESMapping(datatype = DataType.text_type)
    private String seckillEventIds;
    /**
     * V1.4.0 商品简介
     */
    @ApiModelProperty(value = "V1.4.0 商品简介")
    @ESMapping(datatype = DataType.text_type)
    private String goodsSynopsis;
    /**
     * V1.4.0 商品添加方式
     */
    @ApiModelProperty(value = "V1.4.0 商品添加方式")
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsAddType;
    /**
     * V1.4.0 商品销售渠道 1.外卖小程序 2:食堂 3.全部
     */
    @ApiModelProperty(value = "V1.4.0 商品销售渠道 1.外卖小程序 2:食堂 3.全部")
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsSalesChannels;
    /**
     * V1.5.0 商品规格类型 1-统一规格 2-多规格
     */
    @ApiModelProperty(value = "V1.5.0 商品规格类型 1-统一规格 2-多规格")
    @ESMapping(datatype = DataType.integer_type)
    private Integer goodsSpecType;
    /**
     * V1.5.0 商品打包费
     */
    @ApiModelProperty(value = "V1.5.0 商品打包费")
    @ESMapping(datatype = DataType.double_type)
    private double packPrice;

}
