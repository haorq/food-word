package com.meiyuan.catering.user.fegin.sharebill;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.cart.CartShareBillBaseDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillDTO;
import com.meiyuan.catering.user.dto.cart.JoinShareBillDTO;
import com.meiyuan.catering.user.dto.sharebill.CreateShareBillDTO;
import com.meiyuan.catering.user.dto.sharebill.ShareBillQueryDTO;
import com.meiyuan.catering.user.service.CateringCartShareBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaoozu
 * @description 拼单
 * @date 2020/5/1914:02
 * @since v1.0.0
 */
@Service
public class CartShareBillClient {
    @Autowired
    private CateringCartShareBillService shareBillService;

    /**
     * @param dto 拼单信息
     * @return String 拼单号
     * @description 生成拼单
     * @author yaozou
     * @date 2020/5/19 14:06
     * @since v1.1.0
     */
    public Result<String> create(CreateShareBillDTO dto) {
        String shareBillNo = shareBillService.create(dto);
        return Result.succ(shareBillNo);
    }

    /**
     * @param
     * @return {@link Result}
     * @description 加入拼单
     * @author yaozou
     * @date 2020/5/19 14:06
     * @since v1.1.0
     */
    public Result join(JoinShareBillDTO dto) {
        return Result.succ(shareBillService.join(dto.getShareBillNo(), dto.getUserId(), dto.getAvatar(), dto.getNickname()));
    }


    /**
     * @param
     * @return
     * @description 拼单信息
     * @author yaozou
     * @date 2020/5/19 14:56
     * @since v1.1.0
     */
    public Result<CartShareBillDTO> getShareBill(ShareBillQueryDTO dto) {
        return Result.succ(shareBillService.getShareBillByNo(dto.getShareBillNo(), dto.getUserInfoFlag()));
    }

    /**
     * @param orderId 订单Id
     *                shareBillNo 拼单号
     * @return Result
     * @description 生成订单时，拼单状态变为已结算，拼单中添加订单id
     * @author yaozou
     * @date 2020/4/2 16:09
     * @since v1.0.0
     */
    public Result shareBillCreateOrder(Long orderId, String shareBillNo) {
        return Result.succ(shareBillService.shareBillCreateOrder(orderId, shareBillNo));
    }

    /**
     * @param shareBillNo 拼单
     * @return Result
     * @description 结算拼单
     * @author yaozou
     * @date 2020/5/19 14:56
     * @since v1.1.0
     */
    public Result settleShareBill(String shareBillNo, Long merchantId, Long userId) {
        return Result.succ(shareBillService.settleShareBill(shareBillNo));
    }

    /**
     * @param shareBillNo 拼单号
     * @return Result
     * @description 返回点餐页面
     * @author yaozou
     * @date 2020/5/19 14:56
     * @since v1.1.0
     */
    public Result returnChooseGoods(String shareBillNo) {
        return Result.succ(shareBillService.returnChooseGoods(shareBillNo));
    }


    /**
     * 描述:查询未支付的拼单
     *
     * @param merchantId
     * @param userId
     * @return com.meiyuan.catering.core.util.Result<java.lang.String>
     * @author zengzhangni
     * @date 2020/6/18 14:15
     * @since v1.1.0
     */
    public Result<String> getNotPayShareBill(Long merchantId, Long shopId, Long userId) {
        return Result.succ(shareBillService.getNotPayShareBill(merchantId, shopId, userId));
    }


    public CartShareBillBaseDTO getShareBillInfo(Long userId, Long merchantId, Long shopId) {
        return shareBillService.getShareBillInfo(userId, merchantId, shopId);
    }

    public Long shareBillStatusVerify(String shareBillNo, Long userId) {
        return shareBillService.judgeShareBillStatus130(shareBillNo, userId);
    }

    public void cancel(String shareBillNo, Long userId) {
        shareBillService.cancel(shareBillNo, userId);
    }

    public void exist(String shareBillNo, Long userId) {
        shareBillService.exist(shareBillNo, userId);
    }
}
