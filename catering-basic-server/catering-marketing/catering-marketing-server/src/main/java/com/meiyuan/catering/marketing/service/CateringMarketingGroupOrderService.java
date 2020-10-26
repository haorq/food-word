package com.meiyuan.catering.marketing.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.groupon.GroupMemberJoinInDTO;
import com.meiyuan.catering.marketing.dto.groupon.GroupOrderDTO;
import com.meiyuan.catering.core.dto.base.GrouponMemberQuitDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGroupOrderEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponEndVO;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGroupInfoVo;
import com.meiyuan.catering.marketing.vo.grouponorder.MarketingGrouponOrderSoldVo;

import java.util.List;
import java.util.Set;

/**
 * 团单数据表(CateringMarketingGroupOrder)服务层
 *
 * @author wxf
 * @since 2020-03-10 11:30:31
 */
public interface CateringMarketingGroupOrderService extends IService<CateringMarketingGroupOrderEntity> {

    /**
     * 初始化团单数据（用户发起活动支付成功后进行初始化）
     *
     * @param groupOrderDTO
     */
    void initGroupOrder(GroupOrderDTO groupOrderDTO);

    /**
     * 新用户加入拼团/团购
     *
     * @param memberJoinInDTO
     */
    void newMemberJoinIn(GroupMemberJoinInDTO memberJoinInDTO);

    /**
     * 用户退出拼团/团购（用户取消团购订单）
     *
     * @param memberQuitDTO
     */
    void memberQuit(GrouponMemberQuitDTO memberQuitDTO);

    /**
     * 根据营销商品ID获取团单
     *
     * @param mGoodsId
     * @return
     */
    CateringMarketingGroupOrderEntity getBymGoodsId(Long mGoodsId);

    /**
     * 方法描述: 结束拼团，检查拼团是否成功<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:58
     * @param ofId 活动关联ID
     * @param isInitiative 是否主动结束
     * @return: {@link }
     * @version 1.1.1
     **/
    MarketingGrouponEndVO endGroup(Long ofId, boolean isInitiative);

    /**
     * 根据营销活动查询团单数据
     * @param ofId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/9 17:31
     * @return: {@link Result <List<CateringMarketingGroupOrderEntity>>}
     * @version V1.3.0
     **/
    List<CateringMarketingGroupOrderEntity> listByOfId(Long ofId);

    /**
     * 根据营销商品ID集合查询团购商品的已团份数
     * @param mGoodsIdSet 营销商品ID集合
     * @author: GongJunZheng
     * @date: 2020/8/11 16:55
     * @return: {@link Result<List<MarketingGrouponOrderSoldVo>>}
     * @version V1.3.0
     **/
    List<MarketingGrouponOrderSoldVo> soldByGrouponMarketingGoodsIds(Set<Long> mGoodsIdSet);

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
    MarketingGrouponGroupInfoVo makeGroupInfo(CateringMarketingGrouponEntity grouponEntity, List<CateringMarketingGroupOrderEntity> groupOrderList, Integer marketingGoodsNum);
}