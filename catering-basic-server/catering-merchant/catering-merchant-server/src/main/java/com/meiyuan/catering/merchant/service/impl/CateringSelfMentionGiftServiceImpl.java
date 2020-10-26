package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.merchant.dao.CateringSelfMentionGiftMapper;
import com.meiyuan.catering.merchant.dto.pickup.SelfMentionGiftDTO;
import com.meiyuan.catering.merchant.entity.CateringSelfMentionGiftEntity;
import com.meiyuan.catering.merchant.service.CateringSelfMentionGiftService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺自提赠品表(CateringSelfMentionGift)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 10:26:03
 */
@Service
public class CateringSelfMentionGiftServiceImpl extends ServiceImpl<CateringSelfMentionGiftMapper,CateringSelfMentionGiftEntity>
        implements CateringSelfMentionGiftService {
    @Resource
    private CateringSelfMentionGiftMapper selfMentionGiftMapper;


    @Override
    public List<SelfMentionGiftDTO> listShopGift(Long shopId,List<Long> giftIds) {
        QueryWrapper<CateringSelfMentionGiftEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringSelfMentionGiftEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringSelfMentionGiftEntity::getShopId, shopId);
         if (BaseUtil.judgeList(giftIds)){
             query.lambda().in(CateringSelfMentionGiftEntity::getGiftId,giftIds);
         }
        List<CateringSelfMentionGiftEntity> selfMentionGiftEntities = selfMentionGiftMapper.selectList(query);

        List<SelfMentionGiftDTO> result = new ArrayList<>();
        if (BaseUtil.judgeList(selfMentionGiftEntities)){
            result = ConvertUtils.sourceToTarget(selfMentionGiftEntities, SelfMentionGiftDTO.class);
        }
        return result;
    }
}