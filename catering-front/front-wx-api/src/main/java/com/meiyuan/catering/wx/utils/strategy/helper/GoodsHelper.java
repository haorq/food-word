package com.meiyuan.catering.wx.utils.strategy.helper;

import com.meiyuan.catering.core.dto.PresellFlagDTO;
import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.goods.enums.GoodsSpecTypeEnum;
import com.meiyuan.catering.goods.enums.GoodsStatusEnum;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.user.enums.CartGoodsTypeEnum;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.utils.strategy.dto.CartGoodsCheckDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 商品验证公共方法
 *
 * @author zengzhangni
 * @date 2020/7/2 13:50
 * @since v1.1.0
 */
@Slf4j
@Component
public class GoodsHelper {

    @Autowired
    private EsGoodsClient esGoodsClient;
    @Autowired
    private ShopGoodsSkuClient shopGoodsSkuClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;


    public EsGoodsDTO getBySkuCode(Long shopId, Long goodsId, String skuCode) {
        Result<EsGoodsDTO> goodsResult = esGoodsClient.getBySkuCode(shopId.toString(), goodsId.toString(), skuCode);

        if (goodsResult.failure()) {
            throw new CustomException(goodsResult.getCode(), goodsResult.getMsg());
        }
        if (goodsResult.getData() == null) {
            Result<String> result = merchantGoodsClient.getGoodsNameFromDb(goodsId);
            if (result.failure()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
            String goodsName = result.getData();
            throw new CustomException(ErrorCode.CART_GOODS_CHANGED_ERROR, goodsName + "已下架");
        }
        EsGoodsDTO esGoodsDTO = goodsResult.getData();
        if (goodsIsLower(esGoodsDTO)) {
            throw new CustomException(ErrorCode.CART_GOODS_CHANGED_ERROR, goodsName(esGoodsDTO) + "已下架");
        }
        if (salesChannelIsIncludeWx(esGoodsDTO)) {
            throw new CustomException(ErrorCode.CART_GOODS_CHANGED_ERROR, goodsName(esGoodsDTO) + "已下架");
        }
        return esGoodsDTO;
    }

    public EsGoodsDTO getBySkuCode(String merchantId, String goodsId, String skuCode) {
        Result<EsGoodsDTO> goodsResult = esGoodsClient.getBySkuCode(merchantId, goodsId, skuCode);
        if (goodsResult.failure() || goodsResult.getData() == null) {
            return null;
        }
        return goodsResult.getData();
    }


    /**
     *========================================================================
     */


    /**
     * 描述:提交购物车,点击[去结算] 时判断
     *
     * @param
     * @return java.lang.Object
     * @author zengzhangni
     * @date 2020/7/3 10:33
     * @since v1.2.0
     */
    public void submitCartVerify(Cart cart, CartGoodsCheckDTO checkDTO) {
        // 获得商品信息
        Result<EsGoodsDTO> goodsResult = esGoodsClient.getBySkuCode(cart.getShopId().toString(), cart.getGoodsId().toString(), cart.getSkuCode());

        EsGoodsDTO goods = getEsGoodsDTO(cart, checkDTO, goodsResult);

        if (goods == null) {
            return;
        }

        String goodsName = goodsName(goods);

        //4.商品是否下架 -- [删除购物车商品,提示用户]
        Boolean lower = goodsLowerDispose(goods, cart, checkDTO);
        if (lower) {
            log.warn("{}:已下架", goodsName);
            return;
        }

        //5.商品售卖渠道是否发生改变 -- [删除购物车商品,提示用户]
        Boolean notIncludeWxDispose = salesChannelNotIncludeWxDispose(goods, cart, checkDTO);
        if (notIncludeWxDispose) {
            log.warn("{}:售卖渠道不支持小程序", goodsName);
            return;
        }

        //6.商品规格是否发生变化 -- [删除购物车商品,提示用户]
        Boolean skuChange = goodsSkuChangeDispose(goods, cart, checkDTO);
        if (skuChange) {
            log.warn("{}:规格发生变化", goodsName);
            return;
        }

        //7.商品是否超过预售时间(1.时间段 2.星期) -- [删除购物车商品,提示用户]
        Boolean yesPresell = goodsYesPresellDispose(goods, cart, checkDTO);
        if (yesPresell) {
            log.warn("{}:已调整为预售商品", goodsName);
            return;
        }

        //8.商品是否超过下单截止时间 -- [删除购物车商品,提示用户]
        Boolean exceedEndTime = goodsExceedEndTimeDispose(goods, cart, checkDTO);
        if (exceedEndTime) {
            log.warn("{}:超过下单截止时间", goodsName);
            return;
        }
        //9.起售数量是否发生变化 -- [返回到购物车列表，刷新数据，需手动调整购买数量后重新下单]
        Boolean minQuantityChange1 = minQuantityChange1Dispose(goods, cart, checkDTO);
        if (minQuantityChange1) {
            log.warn("{}:起售数量发生变化", goodsName);
            return;
        }

        //10.商品购买数量是否超过库存 -- [返回到购物车列表，提示商品剩余库存数量，剩余库存为0,提示xx商品已抢光,清空购物车中的该商品]
        Boolean notSufficient = goodsInventoryNotSufficientDispose(goods, cart, checkDTO);
        if (notSufficient) {
            log.warn("{}:库存不足/库存为0", goodsName);
            return;
        }

        //11.每单限x份优惠是否发生变化(购物车x份优惠 和 es商品x份优惠是否验证) -- [返回到购物车列表，重新计算价格，提示新的x份优惠]
        Boolean change = goodsDiscountLimitChangeDispose(cart, goods, checkDTO);
        if (change) {
            log.warn("{}:每单限x份优惠发生变化", goodsName);
            return;
        }

        //12.商品价格是否发生变化(结合7,重新计算价格判断购物车总价和计算总价是否一致)-- [返回到购物车列表，重新计算价格，需重新下单]
        Boolean priceChange = goodsPriceChangeDispose(cart, goods, checkDTO);
        if (priceChange) {
            log.warn("{}:价格发生变化", goodsName);
        }

        //13.商品包装费价格是否发生变化(结合7,重新计算价格判断购物车总价和计算总价是否一致)-- [返回到购物车列表，重新计算价格，需重新下单]
        Boolean packChange = goodsPackPriceChangeDispose(cart, goods, checkDTO);
        if (packChange) {
            log.warn("{}:包装费价格发生变化", goodsName);
        }
    }

    private EsGoodsDTO getEsGoodsDTO(Cart cart, CartGoodsCheckDTO checkDTO, Result<EsGoodsDTO> goodsResult) {
        //3.商品是否存在(1.平台删除商品 2.平台取消授权商品 3. 商户pc更换菜单商品) -- [删除购物车商品,提示用户]
        EsGoodsDTO goods = getGoodsNullDispose(goodsResult, cart, checkDTO);
        if (goods == null) {
            Result<ShopSkuDTO> shopSkuDtoResult = shopGoodsSkuClient.queryBySkuAndShopId(cart.getSkuCode(), cart.getShopId());

            if (shopSkuDtoResult.failure()) {
                log.warn("{}:商品已删除", shopSkuDtoResult.getMsg());
                return null;
            }
            ShopSkuDTO data = shopSkuDtoResult.getData();
            if (data == null) {
                log.warn("{}:商品已删除", cart.getGoodsId());
                return null;
            }
            log.warn("{}:商品已删除", data.getGoodsName() + "(" + data.getSkuName() + ")");
            return null;
        }
        return goods;
    }

    /**
     * 描述:商品是否存在(1.平台删除商品 2.平台取消授权商品 3. 商户pc更换菜单商品)
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/7/3 9:52
     * @since v1.2.0
     */
    public Boolean getGoods(Result<EsGoodsDTO> goodsResult) {
        if (goodsResult.failure()) {
            throw new CustomException(goodsResult.getCode(), goodsResult.getMsg());
        }

        return goodsResult.getData() == null;
    }

    public EsGoodsDTO getGoodsNullDispose(Result<EsGoodsDTO> goodsResult, Cart cart, CartGoodsCheckDTO checkDTO) {
        if (getGoods(goodsResult)) {
            Long goodsId = cart.getGoodsId();
            Result<String> result = merchantGoodsClient.getGoodsNameFromDb(goodsId);
            if (result.failure()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
            String goodsName = result.getData();
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(goodsName + "已下架");
        }
        return goodsResult.getData();
    }

    /**
     * 描述:商品是否下架
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/7/3 9:52
     * @since v1.2.0
     */
    public Boolean goodsIsUpper(EsGoodsDTO goods) {
        return Objects.equals(goods.getGoodsStatus(), GoodsStatusEnum.UPPER_SHELF.getStatus())
                && Objects.equals(goods.getMerchantGoodsStatus(), GoodsStatusEnum.UPPER_SHELF.getStatus());
    }

    public Boolean goodsIsLower(EsGoodsDTO goods) {
        return !goodsIsUpper(goods);
    }

    public Boolean goodsLowerDispose(EsGoodsDTO goods, Cart cart, CartGoodsCheckDTO checkDTO) {
        Boolean lower = goodsIsLower(goods);
        if (lower) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(goodsName(goods) + "已下架");
        }
        return lower;
    }

    /**
     * 描述:商品售卖渠道是否包含外卖
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/7/3 9:52
     * @since v1.2.0
     */
    public Boolean salesChannelIsIncludeWx(EsGoodsDTO goods) {
        if (goods.getSalesChannels() != null) {
            return !BaseUtil.isSupportWx(goods.getSalesChannels());
        }
        return false;
    }

    public Boolean salesChannelNotIncludeWxDispose(EsGoodsDTO goods, Cart cart, CartGoodsCheckDTO checkDTO) {
        Boolean includeWx = salesChannelIsIncludeWx(goods);
        if (includeWx) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(goodsName(goods) + "已下架");
        }
        return includeWx;
    }

    /**
     * 描述:商品规格是否发生变化
     * <p>
     * 判断商品规格是否发生改变，如果发生变化，则弹窗提示『XXX商品名称、XXX商品名称规格已调整，请重新下单！』
     * 点击『确定』，刷新数据，清空购物车中的该商品；需重新提交购物车；
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/7/3 9:52
     * @since v1.2.0
     */
    public Boolean goodsSkuIsChange(EsGoodsDTO goods, Cart cart) {
        return !Objects.equals(cart.getGoodsSpecType(), goods.getGoodsSpecType());
    }

    public Boolean goodsSkuChangeDispose(EsGoodsDTO goods, Cart cart, CartGoodsCheckDTO checkDTO) {
        Boolean change = goodsSkuIsChange(goods, cart);
        if (change) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(goodsName(goods) + ",规格已调整");
        }
        return change;
    }

    /**
     * 描述: 重新计算价格判断购物车总价和计算总价是否一致
     * (结合 x份优惠 ,重新计算价格判断购物车总价和计算总价是否一致)
     * -----1.商品价格是否发生变化
     * -----2.每单限x份优惠
     *
     * <p>
     * 判断商品价格是否改价，如果改价，则弹窗提示『XXX商品名称、XXX商品名称已变更价格，请重新下单！』；
     * 点击『确定』，刷新数据，自动重新计算商品价格；重新提交购物车；
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/7/3 9:52
     * @since v1.2.0
     */
    public Boolean goodsPriceIsChange(Cart cart, EsGoodsDTO goods) {
        Integer userType = cart.getUserType();
        Integer number = cart.getNumber();
        //购物车商品总价格
        BigDecimal cartGoodsPrice = cart.getTotalPrice();
        //es商品总价格
        BigDecimal esGoodsPrice = getGoodsPrice(userType, number, goods);

        return BaseUtil.priceNoEquals(cartGoodsPrice, esGoodsPrice);
    }

    public Boolean goodsPriceChangeDispose(Cart cart, EsGoodsDTO goods, CartGoodsCheckDTO checkDTO) {
        Boolean change = goodsPriceIsChange(cart, goods);
        if (change) {
            checkDTO.addRecalculateCartErrMsg(goodsName(goods) + "已变更价格");
            //根据购物车类型 保存不同的数据
            saveInfo(cart, goods, checkDTO);
        }
        return false;
    }

    public Boolean goodsPackIsChange(Cart cart, EsGoodsDTO goods) {
        BigDecimal cartPackPrice = cart.getPackPrice();
        BigDecimal esPackPrice = goods.getPackPrice();
        if (cartPackPrice == null || esPackPrice == null) {
            return false;
        }
        return BaseUtil.priceNoEquals(cartPackPrice, esPackPrice);
    }

    public Boolean goodsPackPriceChangeDispose(Cart cart, EsGoodsDTO goods, CartGoodsCheckDTO checkDTO) {
        Boolean change = goodsPackIsChange(cart, goods);
        if (change) {
            checkDTO.addRecalculateCartErrMsg(goodsName(goods) + ",餐盒费变更");
            //根据购物车类型 保存不同的数据
            saveInfo(cart, goods, checkDTO);
        }
        return false;
    }


    /**
     * 描述:商品优惠数量是否发生改变
     * <p>
     * 判断商品每单限X份优惠的『X』是否发生改变，如果发生变化，
     * 则弹窗提示『XXX商品名称每单限X份优惠、XXX商品名称每单限X份优惠，请重新下单！』；
     * 点击『确定』，需手动调整购买数量后重新下单；
     * <p>
     * -----实现(重新计算价格,刷新购物车,)
     *
     * @param
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/3 10:45
     * @since v1.2.0
     */
    public Boolean goodsDiscountLimitIsChange(Cart cart, EsGoodsDTO goods) {
        if (CartUtil.isCompanyUser(cart.getUserType())) {
            //企业用户不验证每单限优惠
            return false;
        }
        //es 每单限优惠
        Integer esDiscountLimit = goods.getDiscountLimit();
        //购物车 每单限优惠
        Integer cartDiscountLimit = cart.getDiscountLimit();

        return !Objects.equals(esDiscountLimit, cartDiscountLimit);
    }

    public Boolean goodsDiscountLimitChangeDispose(Cart cart, EsGoodsDTO goods, CartGoodsCheckDTO checkDTO) {
        Boolean change = goodsDiscountLimitIsChange(cart, goods);
        if (change) {
            Integer discountLimit = goods.getDiscountLimit();
            if (BaseUtil.isNegativeOne(discountLimit)) {
                checkDTO.addRecalculateCartErrMsg("订单金额发生变化~");
            } else {
                checkDTO.addRecalculateCartErrMsg(goodsName(goods) + ",每单限" + discountLimit + "份优惠");
            }
            //根据购物车类型 保存不同的数据
            saveInfo(cart, goods, checkDTO);
        }
        return change;
    }

    /**
     * 描述: 起售数量是否发生变化
     * <p>
     * ---- 实现(判断 起购数量是否 大于 已购数量)
     * <p>
     * 判断商品X份起售的『X』是否发生改变，如果发生变化，
     * 则弹窗提示『XXX商品名称X份起售、XXX商品名称X份起售，请重新下单！』；
     * 点击『确定』，需手动调整购买数量后重新下单；
     *
     * @param
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/3 10:18
     * @since v1.2.0
     */
//    public Boolean minQuantityIsChange(EsGoodsDTO goods, Cart cart) {
//        Integer lowestBuy = getLowestBuy(goods, cart);
//        if (lowestBuy != null) {
//            return lowestBuy > cart.getNumber();
//        }
//        return false;
//    }
    private Integer getLowestBuy(EsGoodsDTO goods, Cart cart) {
        Integer lowestBuy = goods.getLowestBuy();
        if (CartUtil.isCompanyUser(cart.getUserType())) {
            return lowestBuy;
        }
        Integer minQuantity = goods.getMinQuantity();
        return BaseUtil.isNullOrNegativeOne(minQuantity) ? lowestBuy : minQuantity;

    }

    public Boolean minQuantityChange1Dispose(EsGoodsDTO goods, Cart cart, CartGoodsCheckDTO checkDTO) {

        Integer lowestBuy = getLowestBuy(goods, cart);
        if (lowestBuy != null) {
            if (lowestBuy > cart.getNumber()) {
                checkDTO.addPromptMessage(goodsName(goods) + "," + lowestBuy + "份起售");
                return true;
            }
        }
        return false;
    }


    /**
     * 描述:商品是否超过预售时间(1.时间段 2.星期)
     *
     * @param
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/3 10:22
     * @since v1.2.0
     */
    public Boolean goodsIsPresell(EsGoodsDTO goods) {
        PresellFlagDTO presellFlagDTO = new PresellFlagDTO().setPresellFlag(goods.getPresellFlag())
                .setStartSellTime(goods.getStartSellTime())
                .setEndSellTime(goods.getEndSellTime())
                .setCloseSellTime(goods.getCloseSellTime())
                .setSellWeekTime(goods.getSellWeekTime());
        return DateTimeUtil.nowPresellFlag(presellFlagDTO, false);
    }

    public Boolean goodsYesPresellDispose(EsGoodsDTO goods, Cart cart, CartGoodsCheckDTO checkDTO) {
        Boolean presell = goodsIsPresell(goods);
        if (presell) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(goodsName(goods) + "不在预售时间范围内");
        }
        return presell;
    }


    /**
     * 描述:商品是否超过下单截止时间
     *
     * @param
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/3 10:27
     * @since v1.2.0
     */
    public Boolean goodsIsExceedEndTime(EsGoodsDTO goods) {
        if (goods.getPresellFlag()) {
            return DateTimeUtil.isExceedEndTime(goods.getCloseSellTime());
        }
        return false;
    }

    public Boolean goodsExceedEndTimeDispose(EsGoodsDTO goods, Cart cart, CartGoodsCheckDTO checkDTO) {
        Boolean exceedEndTime = goodsIsExceedEndTime(goods);
        if (exceedEndTime) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(goodsName(goods) + ",超过下单截止时间");
        }
        return exceedEndTime;
    }

    /**
     * 描述:商品购买数量是否超过库存
     * ------商品库存是否充足
     *
     * @param
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/3 10:29
     * @since v1.2.0
     */
    public Integer goodsInventoryIsSufficient(Cart cart) {
        Integer integer = shopGoodsSkuClient.getRemainStockBySku(cart.getMerchantId(), cart.getShopId(), cart.getSkuCode()).getData();
        if (integer == null) {
            return 0;
        }
        return integer;

    }

    public Boolean goodsInventoryNotSufficientDispose(EsGoodsDTO goods, Cart cart, CartGoodsCheckDTO checkDTO) {
        Integer notSufficient = goodsInventoryIsSufficient(cart);
        if (BaseUtil.isNegativeOne(notSufficient)) {
            return false;
        }
        if (notSufficient <= 0) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(goodsName(goods) + "已抢光");
            return true;
        }
        Integer number;
        if (CartUtil.isShareBill(cart.getType())) {
            number = checkDTO.getSkuInventory(cart.getSkuCode());
        } else {
            number = cart.getNumber();
        }
        if (notSufficient < number) {
            checkDTO.addPromptMessage(goodsName(goods) + ",当前库存仅剩" + notSufficient + "份");
            return true;
        }
        return false;
    }


    /**
     * 描述:=================================== private =========================================================
     *
     * @param cart
     * @return java.math.BigDecimal
     * @author zengzhangni
     * @date 2020/7/6 15:41
     * @since v1.2.0
     */
    public BigDecimal getGoodsPrice(Cart cart) {
        return CartUtil.getGoodsPrice(cart.getUserType(), cart.getMarketPrice(),
                cart.getSalesPrice(), cart.getEnterprisePrice(),
                cart.getDiscountLimit(), cart.getNumber(), 0
        );
    }

    /**
     * 描述:获取商品名称 多规格-名称(规格值)
     *
     * @param goods
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/7/18 10:41
     * @since v1.2.0
     */
    public String goodsName(EsGoodsDTO goods) {
        if (Objects.equals(goods.getGoodsSpecType(), GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus())) {
            return goods.getGoodsName();
        } else {
            return goods.getGoodsName() + "(" + goods.getPropertyValue() + ")";
        }

    }

    public BigDecimal getGoodsPrice(Integer userType, Integer number, EsGoodsDTO goods) {
        return CartUtil.getGoodsPrice(userType, goods.getMarketPrice(),
                goods.getSalesPrice(), goods.getEnterprisePrice(),
                goods.getDiscountLimit(), number, 0
        );
    }

    public BigDecimal getGoodsPrice(Integer userType, Integer number, Integer haveNumber, EsGoodsDTO goods) {
        return CartUtil.getGoodsPrice(userType, goods.getMarketPrice(),
                goods.getSalesPrice(), goods.getEnterprisePrice(),
                goods.getDiscountLimit(), number, haveNumber
        );
    }

    /**
     * 描述: 重新计算价格
     *
     * @param cart
     * @param goods
     * @return com.meiyuan.catering.core.dto.cart.Cart
     * @author zengzhangni
     * @date 2020/7/6 15:44
     * @since v1.2.0
     */
    private Cart recalculatePrice(Cart cart, EsGoodsDTO goods) {
        cart.setMarketPrice(goods.getMarketPrice());
        cart.setSalesPrice(goods.getSalesPrice());
        cart.setEnterprisePrice(goods.getEnterprisePrice());
        cart.setDiscountLimit(goods.getDiscountLimit());
        //重新计算价格
        BigDecimal goodsPrice = getGoodsPrice(cart);
        cart.setTotalPrice(goodsPrice);

        Boolean state = specialState(goods);
        cart.setGoodsType(state ? CartGoodsTypeEnum.SPECIAL.getStatus() : CartGoodsTypeEnum.ORDINARY.getStatus());
        cart.setSeckillEventId(state ? Long.valueOf(goods.getSpecialId()) : BaseUtil.NULL_FLAG);
        cart.setPackPrice(goods.getPackPrice());
        return cart;
    }


    private void saveInfo(Cart cart, EsGoodsDTO goods, CartGoodsCheckDTO checkDTO) {
        //重新计算价格
        Cart newCart = recalculatePrice(cart, goods);
        checkDTO.addRecalculateCart(newCart);
    }

    private Boolean specialState(EsGoodsDTO goods) {
        return goods.getSpecialState() != null && goods.getSpecialState();
    }
}
