package com.meiyuan.catering.wx.service.sharebill;

import com.meiyuan.catering.core.dto.cart.ExitCartDTO;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.sharebill.CreateShareBillDTO;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.sharebill.WxCartShareBillInfoDTO;
import com.meiyuan.catering.wx.dto.sharebill.WxShareBillRefreshDTO;
import com.meiyuan.catering.wx.utils.support.ShareBillSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yaoozu
 * @description 微信端拼单服务
 * @date 2020/3/2711:48
 * @since v1.0.0
 */
@Service
public class WxShareBillService {

    @Autowired
    private ShareBillSupport support;

    /**
     * @param dto 拼单信息
     * @return String 拼单号
     * @description 生成拼单
     * @author yaozou
     * @date 2020/3/27 11:49
     * @since v1.0.0
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> create(UserTokenDTO token, CreateShareBillDTO dto) {
        //企业用户判断
        support.userTypeVerify(token);

        //复制属性
        support.copyProperty(token, dto);

        //创建拼单
        Result<String> result = support.create(dto);

        String shareBillNo = ClientUtil.getDate(result);

        //改变购物车类型为拼单
        support.cartTypeToShareBill(dto.getMerchantId(), dto.getShopId(), shareBillNo, dto.getShareUserId());

        //更新缓存
        support.cacheUpdate(shareBillNo);

        return result;
    }


    /**
     * 描述:取消拼单
     *
     * @param shareBillNo
     * @param token
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/6/18 10:23
     * @since v1.1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public Result cancel(String shareBillNo, UserTokenDTO token) {

        //企业用户判断
        support.userTypeVerify(token);

        //取消拼单
        Result result = support.cancel(shareBillNo, token.getUserId());

        // 清理拼单购物车缓存数据
        if (result.success()) {

            //退出拼单删除其他选购人选购的商品
            support.cancelDelGoods(shareBillNo, token.getUserId());

            //改变购物车类型为商品
            support.cartTypeToGoods(shareBillNo);

            //缓存拼单取消标识 告诉其他人拼单以取消
            support.sendShareBillCancel(shareBillNo);

            //删除缓存信息
            support.cacheDelete(shareBillNo);
        }
        return result;
    }


    /**
     * 描述:退出拼单
     *
     * @param dto
     * @param token
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/6/18 10:24
     * @since v1.1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public Result exist(ExitCartDTO dto, UserTokenDTO token) {
        //企业用户判断
        support.userTypeVerify(token);

        Long userId = token.getUserId();
        String shareBillNo = dto.getShareBillNo();

        //退出拼单
        Result result = support.exist(shareBillNo, userId);

        if (result.success()) {

            //退出拼单删除自己选择商品
            support.existDelGoods(shareBillNo, userId);

            //更新缓存
            support.cacheUpdate(shareBillNo);
        }

        return result;
    }

    /**
     * @param token 用户ID
     *              shareBillNo 拼单号
     * @return
     * @description 定时任务--刷新信息
     * @author yaozou
     * @date 2020/3/28 10:22
     * @since v1.0.0
     */
    public Result<WxShareBillRefreshDTO> refresh(UserTokenDTO token, String shareBillNo) {
        return support.refresh(token, shareBillNo);
    }

    /**
     * @param shareBillNo 拼单号
     * @return Result
     * @description 返回点餐页面
     * @author yaozou
     * @date 2020/4/7 22:44
     * @since v1.0.0
     */
    public Result returnChooseGoods(String shareBillNo) {
        return support.returnChooseGoods(shareBillNo);
    }


    /**
     * 描述:拼单详情
     *
     * @param token
     * @param shareBillNo
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.wx.dto.sharebill.WxCartShareBillInfoDTO>
     * @author zengzhangni
     * @date 2020/6/18 10:34
     * @since v1.1.0
     */
    public Result<WxCartShareBillInfoDTO> detail(UserTokenDTO token, String shareBillNo) {
        return support.detail(token, shareBillNo);
    }


}
