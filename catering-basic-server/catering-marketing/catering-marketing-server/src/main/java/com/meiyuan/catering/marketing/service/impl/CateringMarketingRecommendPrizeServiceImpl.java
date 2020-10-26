package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.marketing.dao.CateringMarketingRecommendPrizeMapper;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeQueryDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeTicketDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendRecordDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingRecommendPrizeEntity;
import com.meiyuan.catering.marketing.enums.MarketingRecommendPrizeConditionEnum;
import com.meiyuan.catering.marketing.enums.MarketingRecommendPrizeStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingRecommendPrizeTypeEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingRecommendPrizeService;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeDetailVO;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/19
 **/
@Service("cateringMarketingRecommendPrizeServiceImpl")
public class CateringMarketingRecommendPrizeServiceImpl extends ServiceImpl<CateringMarketingRecommendPrizeMapper, CateringMarketingRecommendPrizeEntity> implements CateringMarketingRecommendPrizeService {

    @Override
    public IPage<RecommendPrizeListVO> listPage(RecommendPrizeQueryDTO queryDTO) {
        IPage<RecommendPrizeListVO> page = getBaseMapper().listPage(queryDTO.getPage(), queryDTO);
        page.getRecords().forEach(recommendPrizeListVO -> {
            Integer recommendPrizeStatus = getRecommendPrizeStatus(recommendPrizeListVO.getBeginTime(), recommendPrizeListVO.getEndTime());
            recommendPrizeListVO.setStatus(recommendPrizeStatus);
        });
        return page;
    }

    /**
     * 获取活动状态
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    private Integer getRecommendPrizeStatus(LocalDateTime beginTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        if (beginTime != null && beginTime.isAfter(now)) {
            return MarketingRecommendPrizeStatusEnum.NOT_START.getStatus();
        } else if (endTime != null && endTime.isBefore(now)) {
            return MarketingRecommendPrizeStatusEnum.ENDED.getStatus();
        } else {
            return MarketingRecommendPrizeStatusEnum.ONGOING.getStatus();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(RecommendPrizeDTO prizeDTO) {
        checkExists(prizeDTO);
        CateringMarketingRecommendPrizeEntity prizeEntity = new CateringMarketingRecommendPrizeEntity();
        BeanUtils.copyProperties(prizeDTO, prizeEntity);
        save(prizeEntity);
        if (prizeDTO.getPrizeType().equals(MarketingRecommendPrizeTypeEnum.COUPON.getStatus())) {
            RecommendPrizeTicketDTO ticketDTO = new RecommendPrizeTicketDTO();
            ticketDTO.setId(IdWorker.getId());
            ticketDTO.setRecommendPrizeId(prizeEntity.getId());
            ticketDTO.setReferrerTicketId(prizeDTO.getReferrerTicketId());
            ticketDTO.setReferralTicketId(prizeDTO.getReferralTicketId());
            getBaseMapper().insertTicket(ticketDTO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(RecommendPrizeDTO prizeDTO) {
        checkExists(prizeDTO);
        CateringMarketingRecommendPrizeEntity prizeEntity = getById(prizeDTO.getId());
        BeanUtils.copyProperties(prizeDTO, prizeEntity);
        updateById(prizeEntity);
        //删除原有的关联优惠券
        getBaseMapper().deleteTicket(prizeEntity.getId());
        //保存新的关联优惠券
        if (prizeDTO.getPrizeType().equals(MarketingRecommendPrizeTypeEnum.COUPON.getStatus())) {
            RecommendPrizeTicketDTO ticketDTO = new RecommendPrizeTicketDTO();
            ticketDTO.setId(IdWorker.getId());
            ticketDTO.setRecommendPrizeId(prizeEntity.getId());
            ticketDTO.setReferrerTicketId(prizeDTO.getReferrerTicketId());
            ticketDTO.setReferralTicketId(prizeDTO.getReferralTicketId());
            getBaseMapper().insertTicket(ticketDTO);
        }
    }

    /**
     * 检测推荐有奖活动是否已存在
     *
     * @param prizeDTO
     * @return
     */
    private void checkExists(RecommendPrizeDTO prizeDTO) {
        long count = getBaseMapper().countByTime(prizeDTO.getBeginTime(), prizeDTO.getEndTime(), prizeDTO.getRecommendCondition(), prizeDTO.getId());
        if (count > 0) {
            throw new CustomException("时间范围内已存在相同推荐条件的活动");
        }
    }

    @Override
    public void delete(Long id) {
        removeById(id);
    }

    @Override
    public RecommendPrizeDetailVO detail(Long id) {
        CateringMarketingRecommendPrizeEntity prizeEntity = getById(id);
        RecommendPrizeDetailVO detailVO = new RecommendPrizeDetailVO();
        BeanUtils.copyProperties(prizeEntity, detailVO);
        if (prizeEntity.getPrizeType().equals(MarketingRecommendPrizeTypeEnum.COUPON.getStatus())) {
            RecommendPrizeTicketDTO ticketDTO = getBaseMapper().getTicket(prizeEntity.getId());
            if (ticketDTO != null) {
                detailVO.setReferrerTicketId(ticketDTO.getReferrerTicketId());
                detailVO.setReferralTicketId(ticketDTO.getReferralTicketId());
            }
        }
        return detailVO;
    }

    @Override
    public CateringMarketingRecommendPrizeEntity getByRecommendCondition(MarketingRecommendPrizeConditionEnum prizeConditionEnum) {
        return getBaseMapper().getByRecommendCondition(prizeConditionEnum.getStatus());
    }

    @Override
    public void createRecommendRecord(Long referrerId, Long referralId) {
        RecommendRecordDTO recordDTO = new RecommendRecordDTO();
        recordDTO.setId(IdWorker.getId());
        recordDTO.setReferrerId(referrerId);
        recordDTO.setReferralId(referralId);
        getBaseMapper().insertRecommendRecord(recordDTO);
    }

    @Override
    public RecommendRecordDTO getRecommendRecordByReferralId(Long referralId) {
        return getBaseMapper().getRecommendRecordByReferralId(referralId);
    }
}
