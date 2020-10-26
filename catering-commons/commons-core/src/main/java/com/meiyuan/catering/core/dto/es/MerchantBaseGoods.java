package com.meiyuan.catering.core.dto.es;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/23 11:49
 * @description 简单描述
 **/
@Data
public class MerchantBaseGoods {
    /**
     * 商品部分
     * 唯一id 对应数据库推送商家关联表的id
     * 平台新增商品  id为商品表中的ID
     * 商户新增商品  id为商品表中的ID
     * 平台推送商品  id为商品商户关联表中的ID
     */
    private String id;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 名称
     */
    private String goodsName;
    /**
     * 菜品编号
     */
    private String spuCode;
    /**
     * 菜品分类id
     */
    private String categoryId;
    /**
     * 菜品分类名称
     */
    private String categoryName;
    /**
     * 分类商品排序号
     */
    private Integer categoryGoodsSort;
    /**
     * 列表图片
     */
    private String listPicture;
    /**
     * 商品简介
     */
    private String goodsSynopsis;
    /**
     * 详情图片
     */
    private String infoPicture;
    /**
     * 门店 上下架 1-下架,2-上架
     */
    private Integer goodsStatus;
    /**
     * 商户上下架 1-下架,2-上架
     */
    private Integer merchantGoodsStatus;

    /**
     * 1-平台推送2-商家自创 3-门店自创
     */
    private Integer goodsAddType;
    /**
     * 原价(市场价)
     */
    private double marketPrice;
    /**
     * 销售价
     */
    private double salesPrice;
    /**
     * 企业价
     */
    private double enterprisePrice;
    /**
     * 商品详细介绍
     */
    private String goodsDescribeText;
    /**
     * 商品规格类型 1-统一规格 2-多规格
     */
    private Integer goodsSpecType;

    /**
     *@since v1.2.0  是否预售0-否 1-是
     */
    private Boolean presellFlag;
    /**
     *@since v1.2.0  开始售卖时间
     */
    private Date startSellTime;
    /**
     * @since v1.2.0 结束售卖时间
     */
    private Date endSellTime;

    /**
     *@since v1.2.0  星期一到天（1-7）
     */
    private String sellWeekTime;

    /**
     *@since v1.2.0  当天截止售卖时间（hh:ss）
     */
    private String closeSellTime;

    /**
     * @since v1.2.0 售卖渠道 1.外卖小程序 2:食堂 3.全部
     */
    private Integer salesChannels;

    /**
     *  @since v1.2.0 每单限x份优惠
     */
    private Integer discountLimit;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 商户商品标识 true-是推送给商家的商品 false-单纯的商品信息
     */
    private boolean merchantGoodsFlag;

    /**
     * 商户id
     */
    @ApiModelProperty("商户id")
    private String merchantId;
    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")
    private String merchantName;

    /**
     * 门店id
     */
    private String shopId;
    /**
     * 门店名称
     */
    private String shopName;
    /**
     * 删除标记
     */
    private Boolean del;
    /**
     * 经纬度 ES中  纬度在前 经度 在后
     */
    @ApiModelProperty("经纬度 ES中  纬度在前 经度 在后 / 返回给前端 会以 米 字符串返回")
    private String location;
    /**
     * 省
     */
    @ApiModelProperty("省")
    private String provinceCode;
    /**
     * 市
     */
    @ApiModelProperty("市")
    private String esCityCode;
    /**
     * 区
     */
    @ApiModelProperty("区")
    private String areaCode;

    /**
     * 标签集合
     */
    private List<MerchantBaseLabel> labelList;
    /**
     * 规格集合
     */
    private List<MerchantBaseGoodsSku> skuList;


}
