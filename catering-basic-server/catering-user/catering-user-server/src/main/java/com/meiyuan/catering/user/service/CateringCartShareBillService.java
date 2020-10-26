package com.meiyuan.catering.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.cart.CartShareBillBaseDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillDTO;
import com.meiyuan.catering.user.dto.sharebill.CreateShareBillDTO;
import com.meiyuan.catering.user.entity.CateringCartShareBillEntity;

import java.util.List;

/**
 * @author yaozou
 * @description 拼单服务
 * ${@link CateringCartShareBillEntity}
 * @date 2020/3/25 14:18
 * @since v1.0.0
 */
public interface CateringCartShareBillService extends IService<CateringCartShareBillEntity> {

    /**
     * 描述:获取拼单信息
     *
     * @param shareBilNo   拼单号
     * @param userInfoFlag true 返回拼单人信息
     * @return com.meiyuan.catering.user.dto.cart.CartShareBillDTO
     * @author yaozou
     * @date 2020/3/26 17:28
     * @since v1.0.0
     */
    CartShareBillDTO getShareBillByNo(String shareBilNo, boolean userInfoFlag);

    /**
     * 描述:创建拼单
     *
     * @param dto 创建拼单数据
     * @return java.lang.String 拼单号
     * @author yaozou
     * @date 2020/3/27 12:09
     * @since v1.0.0
     */
    String create(CreateShareBillDTO dto);

    /**
     * 描述:加入拼单
     *
     * @param shareBillNo 拼单号
     * @param userId      用户id
     * @param avatar      头像
     * @param nickname    昵称
     * @return java.lang.Boolean
     * @author yaozou
     * @date 2020/3/27 14:07
     * @since v1.0.0
     */
    Boolean join(String shareBillNo, long userId, String avatar, String nickname);

    /**
     * 描述:取消拼单
     *
     * @param shareBillNo 拼单号
     * @param userId      用户id
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.user.dto.cart.CartShareBillDTO>
     * @author yaozou
     * @date 2020/3/27 15:00
     * @since v1.0.0
     */
    Result<CartShareBillDTO> cancel(String shareBillNo, long userId);

    /**
     * 描述:退出拼单
     *
     * @param shareBillNo 拼单号
     * @param userId      用户id
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.user.dto.cart.CartShareBillDTO>
     * @author zengzhangni
     * @date 2020/6/23 10:34
     * @since v1.1.1
     */
    void exist(String shareBillNo, long userId);

    /**
     * 描述:  定时任务自动取消拼单
     *
     * @param type    1:菜单 2:商品
     * @param day     处理N 天拼单
     * @param menuIds 菜单ids
     * @return java.util.List<com.meiyuan.catering.user.entity.CateringCartShareBillEntity>
     * @author yaozou
     * @date 2020/3/31 10:18
     * @since v1.0.0
     */
    List<CateringCartShareBillEntity> jobAutoCancelShareBill(Integer type, Integer day, List<Long> menuIds);

    /**
     * 描述:自动清理拼单数据（包含拼单人）
     *
     * @param shareBillNo 拼单号
     * @return void
     * @author yaozou
     * @date 2020/3/31 10:40
     * @since v1.0.0
     */
    void autoCleanBillData(String shareBillNo);

    /**
     * 描述:判断拼单状态是否可操作
     *
     * @param shareBillNo
     * @param userId
     * @return void
     * @author zengzhangni
     * @date 2020/8/5 14:49
     * @since v1.3.0
     */
    Long judgeShareBillStatus130(String shareBillNo, Long userId);


    /**
     * 描述: 根据商户Id和发起人Id获得拼单号信息
     *
     * @param merchantId 商户id
     * @param shopId     店铺id
     * @param userId     用户id
     * @param type       type
     * @return com.meiyuan.catering.user.entity.CateringCartShareBillEntity
     * @author yaozou
     * @date 2020/4/8 14:16
     * @since v1.0.0
     */
    CateringCartShareBillEntity notPayShareBillByMerchantAndUser(Long merchantId, Long shopId, Long userId, int type);

    /**
     * 描述: 生成订单时，拼单状态变为已结算，拼单中添加订单id
     *
     * @param orderId     订单Id
     * @param shareBillNo 拼单号
     * @return java.lang.Boolean
     * @author yaozou
     * @date 2020/5/19 15:21
     * @since v1.1.0
     */
    Boolean shareBillCreateOrder(Long orderId, String shareBillNo);


    /**
     * 描述:订单结算 拼单状态改为结算中
     *
     * @param shareBillNo 拼单号
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/22 11:32
     * @since v1.1.0
     */
    Boolean settleShareBill(String shareBillNo);


    /**
     * 描述:返回点餐页面 拼单状态改为选购中
     *
     * @param shareBillNo 拼单号
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/22 11:33
     * @since v1.1.0
     */
    Boolean returnChooseGoods(String shareBillNo);


    /**
     * 描述:查询未支付的拼单
     *
     * @param merchantId 商户id
     * @param shopId
     * @param userId     用户id
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/6/18 14:16
     * @since v1.1.0
     */
    String getNotPayShareBill(Long merchantId, Long shopId, Long userId);


    /**
     * 描述:切换拼单信息
     *
     * @param userId
     * @param merchantId
     * @param shopId
     * @return com.meiyuan.catering.user.dto.cart.CartShareBillBaseDTO
     * @author zengzhangni
     * @date 2020/6/29 16:59
     * @since v1.1.1
     */
    CartShareBillBaseDTO getShareBillInfo(Long userId, Long merchantId, Long shopId);

}
