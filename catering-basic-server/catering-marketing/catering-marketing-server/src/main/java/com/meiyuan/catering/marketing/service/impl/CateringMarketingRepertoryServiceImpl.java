package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.marketing.dao.CateringMarketingRepertoryMapper;
import com.meiyuan.catering.marketing.dto.MarketingRepertoryAddDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivityRepertoryDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingRepertoryEntity;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingRepertoryService;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryEventSoldVo;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 营销库存表(CateringMarketingRepertory)表服务实现类
 *
 * @author gz
 * @since 2020-03-10 11:34:12
 */
@Service("cateringMarketingRepertoryService")
public class CateringMarketingRepertoryServiceImpl extends ServiceImpl<CateringMarketingRepertoryMapper, CateringMarketingRepertoryEntity>
        implements CateringMarketingRepertoryService {

    @Resource
    private CateringMarketingRepertoryMapper repertoryMapper;

    @Override
    public void initOrUpdateForOfId(Long ofId, MarketingOfTypeEnum typeEnum, Integer repertory) {
        CateringMarketingRepertoryEntity one = this.getByOfId(ofId);
        if (one == null) {
            one = new CateringMarketingRepertoryEntity();
            one.setResidualInventory(repertory);
            one.setSoldOut(0);
            one.setOfType(typeEnum.getStatus());
            one.setOfId(ofId);
            one.setTotalInventory(repertory);
            this.save(one);
            return;
        }
        // 编辑操作--设置总库存和剩余库存(剩余库存=设置后的总库存-现有的已售数量)
        one.setTotalInventory(repertory);
        one.setResidualInventory(repertory - one.getSoldOut());
        this.updateById(one);
    }

    @Override
    public void initRepertoryFormGoodsId(List<MarketingRepertoryAddDTO> list, Long ofId, MarketingOfTypeEnum typeEnum) {
        // 通过关联的活动id删除库存数据
        this.removeForOfId(ofId);
        List<CateringMarketingRepertoryEntity> collect = list.stream().map(e -> {
            CateringMarketingRepertoryEntity entity = ConvertUtils.sourceToTarget(e, CateringMarketingRepertoryEntity.class);
            entity.setOfId(ofId);
            entity.setOfType(typeEnum.getStatus());
            entity.setResidualInventory(e.getTotalInventory());
            entity.setSoldOut(0);
            return entity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

    @Override
    public void deductingTheInventory(Long id, Integer number, MarketingOfTypeEnum typeEnum, Long activityId) {
        this.baseMapper.updateRepertory(id, number, typeEnum.getStatus(), activityId);
    }

    @Override
    public void deductingTheInventoryBatch(List<Long> ids, Integer number, MarketingOfTypeEnum typeEnum, Long activityId) {
        this.baseMapper.updateRepertoryBatch(ids, number, activityId);
    }

    @Override
    public void deductingTheInventoryBatch(List<Long> ticketRuleRecordList) {
        this.baseMapper.updateRepertoryByTicketRuleRecordId(ticketRuleRecordList, 1);
    }

    @Override
    public void deductingTheInventoryBatch(List<Long> ticketRuleRecordList, Integer total) {
        this.baseMapper.updateRepertoryByTicketRuleRecordId(ticketRuleRecordList, total);
    }

    @Override
    public Integer getSoldOutFormGoodsId(Long mGoodsId) {
        LambdaQueryWrapper<CateringMarketingRepertoryEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingRepertoryEntity::getMGoodsId, mGoodsId).eq(CateringMarketingRepertoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringMarketingRepertoryEntity one = this.getOne(queryWrapper);
        if (one == null) {
            return 0;
        }
        return one.getSoldOut();
    }

    @Override
    public void syncSeckillInventory(Long mGoodsId, Integer number, boolean isLess, Long seckillEventId) {
        LambdaQueryWrapper<CateringMarketingRepertoryEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingRepertoryEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringMarketingRepertoryEntity::getMGoodsId, mGoodsId)
                .eq(CateringMarketingRepertoryEntity::getSeckillEventId, seckillEventId);
//        CateringMarketingRepertoryEntity repertoryEntity = this.getOne(queryWrapper);
//        if (repertoryEntity == null) {
//            throw new CustomException("没有获取到库存信息");
//        }
//        // 当前剩余库存
//        Integer residualInventory = repertoryEntity.getResidualInventory();
//        // 当前销量
//        Integer soldOut = repertoryEntity.getSoldOut();
//        if (isLess) {
//            // 扣库存
//            if (residualInventory.compareTo(number) < 0) {
//                // 库存不足
//                throw new CustomException(ErrorCode.SECKILL_INVERSTORY_EMTRY_ERROR, "库存不足");
//            }
//            residualInventory -= number;
//            soldOut += number;
//        } else {
//            residualInventory += number;
//            soldOut -= number;
//        }
//        repertoryEntity.setResidualInventory(residualInventory);
//        repertoryEntity.setSoldOut(soldOut);
//        this.updateById(repertoryEntity);

        // 剩余库存更新数量
        Integer numberWithStock = number;
        // 已售更新数量
        Integer numberWithSoldOut = number;

        if (isLess) {
            // 下单，扣库存
            numberWithStock = numberWithStock * -1;
        } else {
            // 取消订单，库存回退
            numberWithSoldOut = numberWithSoldOut * -1;
        }
        int ret = repertoryMapper.updateStockByMgoodsIdAndSeckKillEventId(mGoodsId, seckillEventId, numberWithStock, numberWithSoldOut);
        if (isLess && ret <= 0) {
            // 库存不足
            throw new CustomException(ErrorCode.SECKILL_INVERSTORY_EMTRY_ERROR, "库存不足");
        }
    }

    @Override
    public void syncGrouponSoldOut(Long mGoodsId, Integer soldNumber) {
        LambdaQueryWrapper<CateringMarketingRepertoryEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingRepertoryEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringMarketingRepertoryEntity::getMGoodsId, mGoodsId);
        synchronized (this) {
            CateringMarketingRepertoryEntity repertoryEntity = this.getOne(queryWrapper);
            if (repertoryEntity != null) {
                repertoryEntity.setSoldOut(repertoryEntity.getSoldOut() + soldNumber);
                updateById(repertoryEntity);
            }
        }
    }

    @Override
    public CateringMarketingRepertoryEntity getByOfId(Long ofId) {
        QueryWrapper<CateringMarketingRepertoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMarketingRepertoryEntity::getOfId, ofId).eq(CateringMarketingRepertoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public CateringMarketingRepertoryEntity getBymGoodsId(Long mGoodsId) {
        QueryWrapper<CateringMarketingRepertoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMarketingRepertoryEntity::getMGoodsId, mGoodsId)
                .eq(CateringMarketingRepertoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return this.baseMapper.selectOne(queryWrapper);
    }

    private void removeForOfId(Long ofId) {
        UpdateWrapper<CateringMarketingRepertoryEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(CateringMarketingRepertoryEntity::getOfId, ofId);
        this.remove(wrapper);
    }

    @Override
    public Map<String, Integer> getInventoryByOfId(Long shopId, Integer userType) {
        List<CateringMarketingRepertoryEntity> list = baseMapper.getInventoryByOfId(shopId, userType);
        return list.stream().collect(Collectors.toMap(e -> BaseUtil.seckillKey(e.getMGoodsId(), e.getSeckillEventId()), CateringMarketingRepertoryEntity::getResidualInventory));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByOfId(Long marketingId) {
        removeForOfId(marketingId);
    }

    @Override
    public List<MarketingRepertoryGoodsSoldVo> marketingProjectedCostCount(Long marketingId) {
        return repertoryMapper.marketingProjectedCostCount(marketingId);
    }

    @Override
    public List<MarketingRepertoryEventSoldVo> soldBySeckillMarketingGoodsIds(Set<Long> mGoodsIdSet) {
        return repertoryMapper.soldBySeckillMarketingGoodsIds(mGoodsIdSet);
    }

    @Override
    public List<MarketingRepertoryEventSoldVo> soldByEventMarketingGoodsId(Long eventId, List<Long> mGoodsIdList) {
        return repertoryMapper.soldByEventMarketingGoodsId(eventId, mGoodsIdList);
    }

    @Override
    public void saveActivityRepertory(List<ActivityRepertoryDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        ActivityRepertoryDTO activityRepertory = list.stream().findFirst().get();
        repertoryMapper.removeByActivityId(activityRepertory.getActivityId());
        List<CateringMarketingRepertoryEntity> collect = list.stream().map(e -> {
            CateringMarketingRepertoryEntity entity = new CateringMarketingRepertoryEntity();
            entity.setSoldOut(0);
            entity.setOfType(MarketingOfTypeEnum.TICKET.getStatus());
            entity.setTotalInventory(e.getTotalInventory());
            entity.setResidualInventory(e.getTotalInventory());
            entity.setActivityId(e.getActivityId());
            entity.setOfId(e.getTicketId());
            entity.setTicketRuleRecordId(e.getTicketRuleRecordId());
            return entity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

    @Override
    public void updateActivityRepertory(List<ActivityRepertoryDTO> list, Long activityId, List<Long> ticketRuleRecordIdList) {
        if (null == activityId || CollectionUtils.isEmpty(ticketRuleRecordIdList)) {
            return;
        }
        QueryWrapper<CateringMarketingRepertoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMarketingRepertoryEntity::getActivityId, activityId)
                .eq(CateringMarketingRepertoryEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .in(CateringMarketingRepertoryEntity::getTicketRuleRecordId, ticketRuleRecordIdList);
        List<CateringMarketingRepertoryEntity> entityList = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        Map<Long, Integer> map = list.stream()
                .collect(Collectors.toMap(ActivityRepertoryDTO::getTicketRuleRecordId, ActivityRepertoryDTO::getTotalInventory, (k1, k2) -> k1));
        entityList.forEach(e -> {
            Long ticketRuleRecordId = e.getTicketRuleRecordId();
            Integer newTotalInventory = map.get(ticketRuleRecordId);
            if (newTotalInventory == null) {
                return;
            }
            Integer soldOut = e.getSoldOut();
            int soldOutInt = 0;
            int newTotalInventoryInt = newTotalInventory;
            if (soldOut != null) {
                soldOutInt = soldOut;
            }
            int residualInventory = newTotalInventoryInt - soldOutInt;
            e.setResidualInventory(residualInventory);
            e.setTotalInventory(newTotalInventoryInt);
        });
        this.updateBatchById(entityList);
    }

    @Override
    public void refurbishSeckillGoodsRepertory(List<Long> seckillIdList) {
        repertoryMapper.refurbishSeckillGoodsRepertory(seckillIdList);
    }

    @Override
    public Integer getInventoryByOfMGoodsId(Long mGoodsId, Long seckillEventId) {
        LambdaQueryWrapper<CateringMarketingRepertoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CateringMarketingRepertoryEntity::getResidualInventory)
                .eq(CateringMarketingRepertoryEntity::getMGoodsId, mGoodsId)
                .eq(CateringMarketingRepertoryEntity::getSeckillEventId, seckillEventId);
        return getObj(wrapper, obj -> Integer.valueOf(obj.toString()));
    }
}
