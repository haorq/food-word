package com.meiyuan.catering.user.utils;

import com.meiyuan.catering.core.enums.base.SaleChannelsEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.user.enums.CartGoodsTypeEnum;
import com.meiyuan.catering.user.enums.CartTypeEnum;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author zengzhangni
 * @date 2020/6/17 16:26
 * @since v1.1.0
 */
public class CartUtil {

    public static final Long SECKILL_CATEGORY_ID = -1L;
    public static final String SECKILL_CATEGORY_NAME = "秒杀";
    public static final Long DISCOUNT_CATEGORY_ID = -2L;
    public static final String DISCOUNT_CATEGORY_NAME = "折扣";


    /**
     * 描述:是否是拼单 true:是  false:不是
     *
     * @param type
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/17 16:27
     * @since v1.1.0
     */
    public static Boolean isShareBill(Integer type) {
        return Objects.equals(type, CartTypeEnum.SHARE_BILL.getStatus());
    }

    public static Boolean isShareBill(String shareBillNo) {
        return StringUtils.isNotBlank(shareBillNo);
    }


    /**
     * 描述:是否是秒杀商品 true:是  false:不是
     *
     * @param type
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/22 10:17
     * @since v1.1.0
     */
    public static Boolean isSeckillGoods(Integer type) {
        return Objects.equals(type, CartGoodsTypeEnum.SECKILL.getStatus());
    }

    /**
     * 是否是特价商品 true:是  false:不是
     *
     * @param type
     * @return
     */
    public static Boolean isSpecialGoods(Integer type) {
        return CartGoodsTypeEnum.SPECIAL.getStatus().equals(type);
    }

    /**
     * 描述:描述:是否是普通商品 true:是  false:不是
     *
     * @param type
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/22 10:18
     * @since v1.1.0
     */
    public static Boolean isOrdinaryGoods(Integer type) {
        return
                Objects.equals(type, CartGoodsTypeEnum.ORDINARY.getStatus()) ||
                        Objects.equals(type, CartGoodsTypeEnum.SPECIAL.getStatus())
                ;
    }

    /**
     * 描述:商品是否上架
     *
     * @param goodsStatus
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/2 15:41
     * @since v1.2.0
     */
    public static Boolean goodsIsUpperShelf(Integer goodsStatus) {
        return Objects.equals(GoodsStatusEnum.UPPER_SHELF.getStatus(), goodsStatus);
    }


    /**
     * 描述: 是否是企业用户
     *
     * @param userType
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/2 15:41
     * @since v1.2.0
     */
    public static Boolean isCompanyUser(Integer userType) {
        return Objects.equals(userType, UserTypeEnum.COMPANY.getStatus());
    }


    /**
     * 描述:  获取商品价格
     *
     * @param userType        优惠类型
     * @param marketPrice     原价
     * @param salesPrice      现价
     * @param enterprisePrice 企业价
     * @param discountLimit   每单限x份优惠
     * @param number          购买数量
     * @param haveNumber      已购买数量
     * @return java.math.BigDecimal
     * @author zengzhangni
     * @date 2020/7/2 15:38
     * @since v1.2.0
     */
    public static BigDecimal getGoodsPrice(Integer userType, BigDecimal marketPrice, BigDecimal salesPrice, BigDecimal enterprisePrice, Integer discountLimit, Integer number, Integer haveNumber) {

        //是否是添加购物车
        boolean isAdd = number > 0;

        //企业用户 有企业价 企业价计算 没有原价计算
        if (CartUtil.isCompanyUser(userType)) {
            if (!BaseUtil.isNegativeOne(enterprisePrice)) {
                return BigDecimal.valueOf(number).multiply(enterprisePrice);
            } else {
                return BigDecimal.valueOf(number).multiply(marketPrice);
            }
        }

        /* 现价为空的情况，原价计算总价 */
        if (BaseUtil.isNegativeOne(salesPrice)) {
            return BigDecimal.valueOf(number).multiply(marketPrice);
        }

        //每单限x份优惠
        if (discountLimit != null && discountLimit > 0) {
            //原价
            if (isAdd) {
                //可以优惠的数量
                int discountNumber = discountLimit - haveNumber;
                //不优惠的数量
                int notDiscountNumber = discountNumber > 0 ? number - discountNumber : number;

                BigDecimal price = BigDecimal.ZERO;

                if (notDiscountNumber > 0) {
                    price = price.add(BigDecimal.valueOf(notDiscountNumber).multiply(marketPrice));
                    if (discountNumber > 0) {
                        price = price.add(BigDecimal.valueOf(discountNumber).multiply(salesPrice));
                    }
                } else {
                    price = price.add(BigDecimal.valueOf(number).multiply(salesPrice));
                }

                return price;
            } else {
                BigDecimal price = BigDecimal.ZERO;

                if (haveNumber > discountLimit) {
                    //购物车剩余数量 3[优惠] -3[扣减]  5[已买]  = 2
                    //购物车剩余数量 3[优惠] -3[扣减]  8[已买]  = 5
                    int surplusNumber = haveNumber + number;
                    //优惠数量  3[优惠] - 2[剩余] = 1[优惠]
                    //优惠数量  3[优惠] - 5[剩余] = -2[优惠]
                    int discountNumber = discountLimit - surplusNumber;
                    //小于0 说明扣减数量都是按原价减
                    if (discountNumber <= 0) {
                        price = price.add(BigDecimal.valueOf(number).multiply(marketPrice));
                    } else {
                        //部分原价 部分现价
                        price = price.add(BigDecimal.valueOf(number + discountNumber).multiply(marketPrice))
                                .add(BigDecimal.valueOf(-discountNumber).multiply(salesPrice));

                    }
                } else {
                    //如果已购买数量 小于 每单限制优惠 全部以现价扣减
                    price = price.add(BigDecimal.valueOf(number).multiply(salesPrice));
                }
                return price;
            }

        }
        /* 没有优惠，用现价计算总价 */
        return BigDecimal.valueOf(number).multiply(salesPrice);
    }


    public static String seckillKey(String skuCode, Long seckillEventId) {
        return skuCode + seckillEventId;
    }

    public static Boolean nonsupportWx(EsMarketingDTO marketingDTO) {
        return !takeout(marketingDTO);
    }

    public static Boolean takeout(EsMarketingDTO marketingEsDTO) {
        return Objects.equals(marketingEsDTO.getGoodsSalesChannels(), SaleChannelsEnum.ALL.getStatus())
                || Objects.equals(marketingEsDTO.getGoodsSalesChannels(), SaleChannelsEnum.TAKEOUT.getStatus());
    }
}
