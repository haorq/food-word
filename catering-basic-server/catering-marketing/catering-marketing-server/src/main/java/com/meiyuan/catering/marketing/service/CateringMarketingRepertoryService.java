package com.meiyuan.catering.marketing.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingRepertoryAddDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivityRepertoryDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingRepertoryEntity;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryEventSoldVo;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 营销库存表(CateringMarketingRepertory)服务层
 *
 * @author gz
 * @since 2020-03-10 11:30:31
 */
public interface CateringMarketingRepertoryService extends IService<CateringMarketingRepertoryEntity> {
    /**
     * 初始/更新库存数据 -- 通过关联id单独设置
     *
     * @param ofId      关联id
     * @param typeEnum  关联类型枚举
     * @param repertory 库存数据
     */
    void initOrUpdateForOfId(Long ofId, MarketingOfTypeEnum typeEnum, Integer repertory);

    /**
     * 通过mGoodsId初始库存数据--批量设置
     *
     * @param list     库存数据集合
     * @param typeEnum 关联类型枚举
     * @param ofId     关联活动ID
     */
    void initRepertoryFormGoodsId(List<MarketingRepertoryAddDTO> list, Long ofId, MarketingOfTypeEnum typeEnum);

    /**
     * 扣减库存
     *
     * @param id         ofId或mGoodsId
     * @param number     扣减数量
     * @param typeEnum   ofType
     * @param activityId 活动ID
     */
    void deductingTheInventory(Long id, Integer number, MarketingOfTypeEnum typeEnum, Long activityId);

    /**
     * 方法描述: 批量减库存<br>
     *
     * @param ids
     * @param number
     * @param typeEnum
     * @param activityId
     * @author: gz
     * @date: 2020/6/23 14:07
     * @return: {@link }
     * @version 1.1.1
     **/
    void deductingTheInventoryBatch(List<Long> ids, Integer number, MarketingOfTypeEnum typeEnum, Long activityId);

    /**
     * 通过活动优惠券记录id更新库存
     *
     * @param ticketRuleRecordList
     */
    void deductingTheInventoryBatch(List<Long> ticketRuleRecordList);
    /**
     * 通过活动优惠券记录id更新库存
     *
     * @param ticketRuleRecordList
     * @param total
     */
    void deductingTheInventoryBatch(List<Long> ticketRuleRecordList,Integer total);

    /**
     * 根据活动关联ID查询库存信息
     *
     * @param ofId 关联的活动ID
     * @return
     */
    CateringMarketingRepertoryEntity getByOfId(Long ofId);

    /**
     * 根据活动商品ID查询库存信息
     *
     * @param mGoodsId 活动商品ID
     * @return
     */
    CateringMarketingRepertoryEntity getBymGoodsId(Long mGoodsId);

    /**
     * 获取秒杀已售数量
     *
     * @param mGoodsId
     * @return
     */
    Integer getSoldOutFormGoodsId(Long mGoodsId);

    /**
     * 同步秒杀库存--数据库
     *
     * @param mGoodsId
     * @param number
     * @param isLess
     * @param seckillEventId
     */
    void syncSeckillInventory(Long mGoodsId, Integer number, boolean isLess, Long seckillEventId);

    /**
     * 同步团购已售数量
     *
     * @param mGoodsId
     * @param soldNumber
     */
    void syncGrouponSoldOut(Long mGoodsId, Integer soldNumber);

    /**
     * 描述: 通过ofId 查询商品库存
     *
     * @param ofId
     * @param userType
     * @return java.util.Map<java.lang.Long   ,       java.lang.Integer>
     * @author zengzhangni
     * @date 2020/7/13 14:05
     * @since v1.2.0
     */
    Map<String, Integer> getInventoryByOfId(Long ofId, Integer userType);

    /**
     * 根据营销活动ID删除营销商品库存信息
     *
     * @param marketingId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 14:13
     * @return: {@link Integer}
     **/
    void delByOfId(Long marketingId);

    /**
     * 查询未销售完毕的商品的预计成本
     *
     * @param marketingId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 18:48
     * @return: {@link List<MarketingRepertoryGoodsSoldVo>}
     **/
    List<MarketingRepertoryGoodsSoldVo> marketingProjectedCostCount(Long marketingId);

    /**
     * 查询指定营销商品集合售出的数量
     *
     * @param mGoodsIdSet 营销商品ID集合
     * @author: GongJunZheng
     * @date: 2020/8/11 15:20
     * @return: {@link Result<List<MarketingRepertoryEventSoldVo>>}
     * @version V1.3.0
     **/
    List<MarketingRepertoryEventSoldVo> soldBySeckillMarketingGoodsIds(Set<Long> mGoodsIdSet);

    /**
     * 根据秒杀活动ID集合刷新秒杀商品库存
     *
     * @param seckillIdList 秒杀活动ID集合
     * @author: GongJunZheng
     * @date: 2020/8/12 13:43
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    void refurbishSeckillGoodsRepertory(List<Long> seckillIdList);

    /**
     * 查询指定营销商品集合、指定场次售出的数量
     *
     * @param eventId      秒杀场次ID集合
     * @param mGoodsIdList 营销商品ID集合
     * @author: GongJunZheng
     * @date: 2020/8/12 18:28
     * @return: {@link List<MarketingRepertoryEventSoldVo>}
     * @version V1.3.0
     **/
    List<MarketingRepertoryEventSoldVo> soldByEventMarketingGoodsId(Long eventId, List<Long> mGoodsIdList);

    /**
     * 方法描述: 保存平台活动优惠券库存<br>
     *
     * @param list
     * @author: gz
     * @date: 2020/8/22 9:24
     * @return: {@link }
     * @version 1.3.0
     **/
    void saveActivityRepertory(List<ActivityRepertoryDTO> list);

    /**
     * describe: 更新优惠券库存
     *
     * @param list
     * @param activityId
     * @param ticketRuleRecordIdList
     * @author: yy
     * @date: 2020/8/24 18:04
     * @return: {@link }
     * @version 1.3.0
     **/
    void updateActivityRepertory(List<ActivityRepertoryDTO> list, Long activityId, List<Long> ticketRuleRecordIdList);

    /**
     * 描述:通过mmGoodsId和场次id查询剩余库存
     *
     * @param mGoodsId
     * @param seckillEventId
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/8/28 9:15
     * @since v1.3.0
     */
    Integer getInventoryByOfMGoodsId(Long mGoodsId, Long seckillEventId);
}
