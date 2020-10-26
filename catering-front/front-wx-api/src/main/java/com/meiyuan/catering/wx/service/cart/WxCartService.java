package com.meiyuan.catering.wx.service.cart;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.cart.*;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.utils.support.CartSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yaoozu
 * @description 购物车服务
 * @date 2020/3/2516:49
 * @since v1.0.0
 */
@Service
@Slf4j
public class WxCartService {

    @Autowired
    private CartSupport support;

    @Transactional(rollbackFor = Exception.class)
    public Result add(UserTokenDTO token, AddCartDTO cartDTO) {


        long millis = System.currentTimeMillis();

        Long userId = token.getUserId();

        log.debug("userId:{},添加购物车", userId);

        //如果是拼单 验证拼单状态
        support.shareBillStatusVerify(cartDTO, userId);

        //复制属性
        support.copyProperty(token, cartDTO);

        //计算/验证购物车
        support.calculateCart(cartDTO);

        //添加购物车(加锁处理)
        Result result = support.lock(userId,
                id -> support.add(cartDTO),
                id -> Result.fail("操作太快了啦，小膳还在拼命的处理~~~~")
        );

        //如果是拼单 更新缓存信息
        support.cacheUpdate(result, cartDTO);

        //价格变化返回错误信息
        support.priceChangeResultFail(result, cartDTO.getGoodsName());

        log.debug("购物车请求时间:{}ms", System.currentTimeMillis() - millis);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result clear(UserTokenDTO token, ClearCartDTO cartDTO) {

        //如果是拼单 验证拼单状态
        support.shareBillStatusVerify(cartDTO.getType(), cartDTO.getShareBillNo(), token.getUserId());

        //复制属性
        support.copyProperty(token, cartDTO);

        Result result = support.clear(cartDTO);

        //如果是拼单 更新缓存信息
        support.cacheUpdate(result, cartDTO);

        return result;
    }

    public Result<CartDTO> list(UserTokenDTO token, CartGoodsQueryDTO queryDTO) {

        //验证
        support.cartVerify(queryDTO, token);

        queryDTO.setUserType(token.getUserType());

        //购物车是否是拼单状态
        if (CartUtil.isShareBill(queryDTO.getType())) {
            //加入拼单
            Result result = support.join(queryDTO.getShareBillNo(), token.getUserId(), token.getAvatar(), token.getNickname());
            log.debug("加入拼单结果：{}", JSON.toJSONString(result));
            //进入拼单成功 返回拼单购物车
            return support.shareBillCartListAndCache(queryDTO.getShareBillNo(), token.getUserId());
        }
        Long userId = token.getUserId();
        queryDTO.setUserId(userId);
        return support.listCartGoods(queryDTO);
    }

    public Result cartGoodsCheck(UserTokenDTO token, CartMerchantDTO merchantDTO) {
        merchantDTO.setUserId(token.getUserId());
        merchantDTO.setUserType(token.getUserType());
        return support.cartGoodsCheck(merchantDTO);
    }
}
