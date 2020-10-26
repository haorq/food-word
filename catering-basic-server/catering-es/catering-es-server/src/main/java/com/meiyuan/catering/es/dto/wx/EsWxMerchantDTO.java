package com.meiyuan.catering.es.dto.wx;

import com.meiyuan.catering.es.dto.goods.EsSimpleGoodsInfoDTO;
import com.meiyuan.catering.es.vo.ShopDiscountInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/4/22 10:36
 * @description 简单描述
 **/
@Data
@ApiModel("商户列表分页数据模型")
public class EsWxMerchantDTO {
    @ApiModelProperty("商户id")
    private String merchantId;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty("品牌属性:1:自营，2:非自营")
    private Integer merchantAttribute;
    @ApiModelProperty("店铺id【1.2.0】")
    private String shopId;
    @ApiModelProperty("商户名称")
    private String merchantName;
    @ApiModelProperty("订单起送金额")
    private Double leastDeliveryPrice;
    @ApiModelProperty("配送费")
    private Double deliveryPrice;
    @ApiModelProperty("商家评分")
    private Double shopGrade;
    @ApiModelProperty("商家月售")
    private Integer ordersNum;
    @ApiModelProperty("距离")
    private String location;
    @ApiModelProperty("省")
    private String provinceCode;
    @ApiModelProperty("市")
    private String esCityCode;
    @ApiModelProperty("区")
    private String areaCode;
    @ApiModelProperty("售卖模式 ： 1-菜单售卖模式 2-商品售卖模式")
    private Integer sellType;
    @ApiModelProperty("门头图")
    private String doorHeadPicture;
    @ApiModelProperty("商户标签")
    private List<String> shopTag;
    @ApiModelProperty("商品集合")
    private List<EsSimpleGoodsInfoDTO> goodsList;
    @ApiModelProperty(value = "是否展示更多商家 true-展示")
    private Boolean manyMerchantFlag;
    @ApiModelProperty(value = "是否有商品标字段 true-有 false-没有")
    private Boolean haveGoodsFlag;

//    @ApiModelProperty(value = "折扣商品信息【1.3.0】")
//    private String discountGoodInfo;
//    @ApiModelProperty(value = "团购活动信息【1.3.0】")
//    private String grouponInfo;
//    @ApiModelProperty(value = "秒杀活动信息【1.3.0】")
//    private String seckillInfo;
//    @ApiModelProperty(value = "领：店内领券活动信息【1.3.0】")
//    private String ticketInfo;
//    @ApiModelProperty(value = "券：平台自动发放的券、商户PC端的店外发券活动信息[店外发券]【1.3.0】")
//    private String sendTicketInfo;
    @ApiModelProperty("业务支持：1：仅配送，2：仅自提，3：全部【1.3.0】")
    private Integer businessSupport;

    @ApiModelProperty("店铺优惠信息Vo【1.3.0】")
    private  List<ShopDiscountInfoVo> discountInfoList;

    @ApiModelProperty(value = "营业状态：1-营业 2-打样")
    private Integer businessStatus;
}
