package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingSeckillEventRelationMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventRelationEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillEventRelationService;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillDetailEventVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventIdsVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventInfoVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 平台秒杀活动场次与门店秒杀关系服务层实现
 **/

@Slf4j
@Service("cateringMarketingSeckillEventRelationService")
public class CateringMarketingSeckillEventRelationServiceImpl extends ServiceImpl<CateringMarketingSeckillEventRelationMapper,
        CateringMarketingSeckillEventRelationEntity> implements CateringMarketingSeckillEventRelationService {

    @Resource
    private CateringMarketingSeckillEventRelationMapper seckillEventRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delBySeckillId(Long seckillId) {
        return seckillEventRelationMapper.deleteBySeckillId(seckillId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBathList(List<CateringMarketingSeckillEventRelationEntity> seckillEventRelationList) {
        return saveBatch(seckillEventRelationList);
    }

    @Override
    public List<MarketingSeckillEventInfoVO> selectSeckillIds(LocalDateTime dateTime, Long eventId, List<Long> shopIds) {
        List<List<Long>> allShopIdList = makeShopIds(shopIds);
        List<MarketingSeckillEventInfoVO> list = new ArrayList<>();
        for (List<Long> selectShopIds : allShopIdList) {
            List<MarketingSeckillEventInfoVO> seckillEventInfoList = seckillEventRelationMapper.selectSeckillIds(dateTime, eventId, selectShopIds);
            if(BaseUtil.judgeList(seckillEventInfoList)) {
                list.addAll(seckillEventInfoList);
            }
        }
        return list;
    }

    private List<List<Long>> makeShopIds(List<Long> shopIds) {
        int count = 500;
        List<List<Long>> allShopIdList = new ArrayList<>();
        List<Long> shopIdList = new ArrayList<>();
        for (int i = 0; i < shopIds.size(); i++) {
            if(shopIdList.size() == count) {
                allShopIdList.add(shopIdList);
                shopIdList = new ArrayList<>();
            }
            shopIdList.add(shopIds.get(i));
            if(i == shopIds.size() - 1 && BaseUtil.judgeList(shopIdList)) {
                allShopIdList.add(shopIdList);
            }
        }
        return allShopIdList;
    }

    @Override
    public List<MarketingSeckillDetailEventVO> selectEventListBySeckillId(Long seckillId) {
        return seckillEventRelationMapper.selectEventListBySeckillId(seckillId);
    }

    @Override
    public List<MarketingSeckillEventVO> selectEventList(List<Long> seckillIdList) {
        return baseMapper.selectEventList(seckillIdList);
    }

    @Override
    public List<Long> selectEventMarketingGoodsIds(LocalDateTime dateTime, Long eventId, List<Long> shopIds) {
        return baseMapper.selectEventMarketingGoodsIds(dateTime, eventId);
    }

    @Override
    public List<CateringMarketingSeckillEventRelationEntity> selectSeckillEventList(List<Long> seckillIdList) {
        LambdaQueryWrapper<CateringMarketingSeckillEventRelationEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingSeckillEventRelationEntity :: getSeckillId, seckillIdList);
        return list(queryWrapper);
    }

    @Override
    public List<MarketingSeckillEventVO> selectUnderwayEventList(List<Long> seckillIdList) {
        return baseMapper.selectUnderwayEventList(seckillIdList);
    }

    @Override
    public List<Long> underwayEventIdList(List<Long> seckillEventIds) {
        return baseMapper.underwayEventIdList(seckillEventIds);
    }

    @Override
    public MarketingSeckillEventVO queryByEventId(Long seckillEventId) {
        return baseMapper.queryByEventId(seckillEventId);
    }

    @Override
    public List<MarketingSeckillEventIdsVO> selectEventIdsBySeckillIds(Set<Long> seckillIds) {
        List<Set<Long>> setList = makeSeckillIds(seckillIds);
        List<MarketingSeckillEventIdsVO> list = new ArrayList<>();
        for (Set<Long> seckillIdSet : setList) {
            List<MarketingSeckillEventIdsVO> selectList = baseMapper.selectEventIdsBySeckillIds(seckillIdSet);
            if(BaseUtil.judgeList(selectList)) {
                list.addAll(selectList);
            }
        }
        return list;
    }

    private List<Set<Long>> makeSeckillIds(Set<Long> seckillIds) {
        int count = 500;
        List<Set<Long>> res = new ArrayList<>();
        Set<Long> seckillIdSet = new HashSet<>();
        for (Long seckillId : seckillIds) {
            if (seckillIdSet.size() == count) {
                res.add(seckillIdSet);
                seckillIdSet = new HashSet<>();
            }
            seckillIdSet.add(seckillId);
        }
        if(!seckillIdSet.isEmpty()) {
            res.add(seckillIdSet);
        }
        return res;
    }

}
