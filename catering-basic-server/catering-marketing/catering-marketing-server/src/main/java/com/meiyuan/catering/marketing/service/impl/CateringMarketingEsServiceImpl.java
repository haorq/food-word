package com.meiyuan.catering.marketing.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.enums.base.GoodsEditTypeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponAloneTestDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingEsService;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsService;
import com.meiyuan.catering.marketing.service.CateringMarketingGrouponService;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CateringMarketingEsServiceImpl
 * @Description
 * @Author gz
 * @Date 2020/3/24 19:49
 * @Version 1.1
 */
@Service
public class CateringMarketingEsServiceImpl implements CateringMarketingEsService {
    @Autowired
    private CateringMarketingSeckillService seckillService;
    @Autowired
    private CateringMarketingGrouponService grouponService;
    @Autowired
    private CateringMarketingGoodsService  marketingGoodsService;
    @Override
    public List<MarketingToEsDTO> findAll() {
        List<MarketingToEsDTO> res = new ArrayList<>();
        // 查询秒杀数据
        List<MarketingToEsDTO> seckillList = seckillService.findAllForEs();
        if(BaseUtil.judgeList(seckillList)) {
            res.addAll(seckillList);
        }
        //查询团购数据
        List<MarketingToEsDTO> grouponList = grouponService.findAllForEs();
        if(BaseUtil.judgeList(grouponList)) {
            res.addAll(grouponList);
        }
        return res;
    }

    @Override
    public Result updateMarketingGoods(MarketingGoodsUpdateDTO dto) {
        Boolean res;
        if(GoodsEditTypeEnum.WEB.getStatus().equals(dto.getEditType())) {
            res = marketingGoodsService.webUpdateGoods(dto);
        } else {
            res = marketingGoodsService.updateGoodsPicture(dto);
        }
        if(res){
            // 更新ES
            List<MarketingToEsDTO> esList = Lists.newArrayList();
            esList.addAll(seckillService.findByGoodsIdForEs(dto.getGoodsId()));
            esList.addAll(grouponService.findByGoodsIdForEs(dto.getGoodsId()));
            if (CollectionUtils.isNotEmpty(esList)) {
                seckillService.sendEsGoods(esList);
            }
        }
        return Result.succ(res);
    }

    @Override
    public LocalDateTime updateGrouponTime(MarketingGrouponAloneTestDTO dto) {
        CateringMarketingGrouponEntity grouponEntity = grouponService.getById(dto.getId());
        if(null == grouponEntity) {
            throw new CustomException("数据不存在");
        }
        LocalDateTime oldEndTime = grouponEntity.getEndTime();
        grouponEntity.setBeginTime(dto.getBeginTime());
        grouponEntity.setEndTime(dto.getEndTime());
        grouponService.updateById(grouponEntity);
        return oldEndTime;
    }

    @Override
    public void sendGrouponTimedTaskMsg(Long id, LocalDateTime endTime, MarketingUpDownStatusEnum down) {
        grouponService.sendGrouponTimedTaskMsg(id, endTime, down);
    }
}
