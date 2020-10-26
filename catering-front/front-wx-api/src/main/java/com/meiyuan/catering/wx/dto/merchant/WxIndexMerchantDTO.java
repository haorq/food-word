package com.meiyuan.catering.wx.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.es.dto.goods.EsGoodsListDTO;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yaoozu
 * @description 首页商户
 * @date 2020/3/2411:29
 * @since v1.0.0
 */
@ApiModel("首页商户dto")
@Data
public class WxIndexMerchantDTO implements Comparable<WxIndexMerchantDTO>, Serializable {
    private static final long serialVersionUID = 5195442664707178770L;
    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "门店联系电话")
    private String shopPhone;

    @ApiModelProperty(value = "门店LOGO图片")
    private String doorHeadPicture;

    @ApiModelProperty(value = "完整地址")
    private String addressFull;

    @ApiModelProperty(value = "经纬度")
    private String mapCoordinate;

    @ApiModelProperty(value = "距离 数字")
    private Double distance;

    @ApiModelProperty(value = "距离 文字")
    private String distanceStr;

    @ApiModelProperty(value = "门店类型：1-自营 2-入驻 3-自提点")
    private Integer shopType;

    @ApiModelProperty(value = "营业开始时间")
    private LocalDateTime openingTime;

    @ApiModelProperty(value = "营业结束时间")
    private LocalDateTime closingTime;

    @ApiModelProperty(value = "售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;


    @ApiModelProperty(value = "配送价格")
    private BigDecimal deliveryPrice;

    @ApiModelProperty(value = "满单免配送金额")
    private BigDecimal freeDeliveryPrice;

    @ApiModelProperty(value = "订单起送金额")
    private BigDecimal leastDeliveryPrice;

    @ApiModelProperty("商家评分")
    private Double shopGrade;

    @ApiModelProperty("商家月售")
    private Integer ordersNum;

    @ApiModelProperty(value = "标签 显示4个字，最多显示3个标签")
    private List<MerchantShopTagsVo> labelList;

    @ApiModelProperty(value = "商品列表 显示最多三个商品，商品标签显示第一个")
    private List<EsGoodsListDTO>  goodsList;

    @ApiModelProperty(hidden = true, value = "是否展示更多商家 true-展示")
    private Boolean manyMerchantFlag;
    @Override
    public int compareTo(WxIndexMerchantDTO o) {
        //升序
        return new BigDecimal((this.getDistance() - o.getDistance())).intValue();
    }
}
