package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.dto.base.GrouponMemberQuitDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.groupon.GroupMemberJoinInDTO;
import com.meiyuan.catering.marketing.dto.groupon.GroupOrderDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGroupOrderEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingGroupOrderService;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGroupInfoVo;
import com.meiyuan.catering.marketing.vo.grouponorder.MarketingGrouponOrderSoldVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author luohuan
 * @date 2020/5/19
 **/
@Service("marketingGroupOrderClient")
public class MarketingGroupOrderClient {

    @Autowired
    private CateringMarketingGroupOrderService groupOrderService;

    /**
     * 初始化团单数据（用户发起活动支付成功后进行初始化）
     * 1.初始化团单
     * 2.初始化团单成员
     * 3.初始化库存（团购没有库存限制，只需更新已售数量）
     *
     * @param groupOrderDTO 团单DTO
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result initGroupOrder(GroupOrderDTO groupOrderDTO) {
        groupOrderService.initGroupOrder(groupOrderDTO);
        return Result.succ();
    }

    /**
     * 新用户加入拼团/团购
     * 1.更新团单信息
     * 2.添加团单成员
     * 3.更新库存（团购没有库存限制，只需更新已售数量）
     *
     * @param memberJoinInDTO 成员加入DTO
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result newMemberJoinIn(GroupMemberJoinInDTO memberJoinInDTO) {
        groupOrderService.newMemberJoinIn(memberJoinInDTO);
        return Result.succ();
    }

    /**
     * 用户退出拼团/团购（用户取消团购订单）
     * 1.更新团单信息
     * 2.删除团单成员
     * 3.更新库存（团购没有库存限制，只需更新已售数量）
     *
     * @param memberQuitDTO 成员退出DTO
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result memberQuit(GrouponMemberQuitDTO memberQuitDTO) {
        groupOrderService.memberQuit(memberQuitDTO);
        return Result.succ();
    }

    /**
     * 根据营销商品ID获取团单
     *
     * @param mGoodsId 营销商品ID
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<CateringMarketingGroupOrderEntity> getBymGoodsId(Long mGoodsId) {
        return Result.succ(groupOrderService.getBymGoodsId(mGoodsId));
    }

    /**
     * 结束拼团，检查拼团是否成功
     * 1.判断团购是否成功
     * 2.团购成功则发送成功mq消息
     * 3.团购失败则发送失败mq消息
     *
     * @param ofId 活动关联ID
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result endGroup(Long ofId) {
        groupOrderService.endGroup(ofId,false);
        return Result.succ();
    }

    /**
    * 冻结团购，失败
    * @param ofId 活动关联ID
    * @author: GongJunZheng
    * @date: 2020/8/9 17:04
    * @return: {@link }
    * @version V1.3.0
    **/
    public Result freezeEndGroup(Long ofId) {
        groupOrderService.endGroup(ofId,true);
        return Result.succ();
    }

    /**
    * 根据营销活动查询团单数据
    * @param ofId 营销活动ID
    * @author: GongJunZheng
    * @date: 2020/8/9 17:31
    * @return: {@link Result<List<CateringMarketingGroupOrderEntity>>}
    * @version V1.3.0
    **/
    public Result<List<CateringMarketingGroupOrderEntity>> listByOfId(Long ofId) {
        return Result.succ(groupOrderService.listByOfId(ofId));
    }

    /**
    * 根据营销商品ID集合查询团购商品的已团份数
    * @param mGoodsIdSet 营销商品ID集合
    * @author: GongJunZheng
    * @date: 2020/8/11 16:55
    * @return: {@link Result<List<MarketingGrouponOrderSoldVo>>}
    * @version V1.3.0
    **/
    public Result<List<MarketingGrouponOrderSoldVo>> soldByGrouponMarketingGoodsIds(Set<Long> mGoodsIdSet) {
        return Result.succ(groupOrderService.soldByGrouponMarketingGoodsIds(mGoodsIdSet));
    }

    /**
    * 处理团购活动的成团情况以及参团人数
    * @param grouponEntity 团购活动信息
    * @param groupOrderList 团购订单集合
    * @param marketingGoodsNum 团购商品个数
    * @author: GongJunZheng
    * @date: 2020/8/12 10:06
    * @return: {@link MarketingGrouponGroupInfoVo}
    * @version V1.3.0
    **/
    public Result<MarketingGrouponGroupInfoVo> makeGroupInfo(CateringMarketingGrouponEntity grouponEntity, List<CateringMarketingGroupOrderEntity> groupOrderList, Integer marketingGoodsNum) {
        return Result.succ(groupOrderService.makeGroupInfo(grouponEntity, groupOrderList, marketingGoodsNum));
    }
}
