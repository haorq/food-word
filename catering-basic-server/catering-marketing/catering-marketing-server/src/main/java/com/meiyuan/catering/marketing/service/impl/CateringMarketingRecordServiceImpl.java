package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.marketing.dao.CateringMarketingRecordMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingRecordEntity;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsService;
import com.meiyuan.catering.marketing.service.CateringMarketingRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购买记录表(CateringMarketingRecord)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 11:34:12
 */
@Service("cateringMarketingRecordService")
public class CateringMarketingRecordServiceImpl extends ServiceImpl<CateringMarketingRecordMapper, CateringMarketingRecordEntity> implements CateringMarketingRecordService {
    @Resource
    private CateringMarketingGoodsService goodsService;



    @Override
    public void syncSeckillUserHaveGought(Long mGoodsId, Long userId, Integer number, boolean isLess) {
        LambdaQueryWrapper<CateringMarketingRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingRecordEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringMarketingRecordEntity::getUserId,userId)
                .eq(CateringMarketingRecordEntity::getMGoodsId,mGoodsId);
        CateringMarketingRecordEntity one = this.getOne(queryWrapper);
        if(one==null){
            if(isLess){
                throw new CustomException("没有获取到用户购买记录信息");
            }else {
                one = new CateringMarketingRecordEntity();
                CateringMarketingGoodsEntity entity = goodsService.getById(mGoodsId);
                if(entity!=null){
                    one.setOfId(entity.getOfId());
                }
                one.setOfType(MarketingOfTypeEnum.SECKILL.getStatus());
                one.setUserId(userId);
                one.setUserCount(number);
                one.setMGoodsId(mGoodsId);
                this.save(one);
            }
        }
        Integer count = one.getUserCount();
        if(isLess){
            count -= number;
        }else {
            count += number;
        }
        one.setUserCount(count);
        this.updateById(one);
    }

    @Override
    public void recordTicketUserGet(Long ticketId, Long userId, Integer number) {
        LambdaQueryWrapper<CateringMarketingRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingRecordEntity::getDel,DelEnum.NOT_DELETE)
                .eq(CateringMarketingRecordEntity::getUserId,userId)
                .eq(CateringMarketingRecordEntity::getOfId,ticketId)
                .eq(CateringMarketingRecordEntity::getOfType,MarketingOfTypeEnum.TICKET.getStatus());
        CateringMarketingRecordEntity one = this.getOne(queryWrapper);
        if(one!=null){
            number+=one.getUserCount();
            one.setUserCount(number);
            this.updateById(one);
        }else {
            CateringMarketingRecordEntity entity = new CateringMarketingRecordEntity();
            entity.setUserId(userId);
            entity.setOfType(MarketingOfTypeEnum.TICKET.getStatus());
            entity.setOfId(ticketId);
            entity.setUserCount(number);
            this.save(entity);
        }
    }

    @Override
    public Integer getUserRecord(Long userId, Long ofId, MarketingOfTypeEnum ofTypeEnum) {
        LambdaQueryWrapper<CateringMarketingRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingRecordEntity::getDel,DelEnum.NOT_DELETE)
                .eq(CateringMarketingRecordEntity::getUserId,userId)
                .eq(CateringMarketingRecordEntity::getOfId,ofId)
                .eq(CateringMarketingRecordEntity::getOfType,ofTypeEnum.getStatus());
        CateringMarketingRecordEntity one = this.getOne(queryWrapper);
        return one==null?0:one.getUserCount();
    }

    @Override
    public void recordTicketUserGetBatch(List<Long> ticketIds, Long userId, Integer number) {
        ticketIds.forEach(ticketId->recordTicketUserGet(ticketId,userId,number));
    }

    @Override
    public void recordTicketUserGetBatchUserId(List<Long> userIds, Long ticketId, Integer number) {
        userIds.forEach(userId->recordTicketUserGet(ticketId,userId,number));
    }
}