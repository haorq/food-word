package com.meiyuan.catering.marketing.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dao.CateringMarketingTicketMapper;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsCategoryEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketEntity;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketGoodsLimitEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketSendTicketPartyEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketUsefulConditionEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsCategoryService;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsService;
import com.meiyuan.catering.marketing.service.CateringMarketingRepertoryService;
import com.meiyuan.catering.marketing.service.CateringMarketingTicketService;
import com.meiyuan.catering.marketing.vo.activity.ActivityPageVO;
import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import com.meiyuan.catering.marketing.vo.ticket.WxShopTicketInfoVo;
import com.meiyuan.catering.marketing.vo.ticket.WxTicketUseShopVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 营销券(CateringMarketingTicket)表服务实现类
 *
 * @author gz
 * @since 2020-03-10 11:34:12
 */
@Service("cateringMarketingTicketService")
public class CateringMarketingTicketServiceImpl extends ServiceImpl<CateringMarketingTicketMapper, CateringMarketingTicketEntity> implements CateringMarketingTicketService {
    @Autowired
    private CateringMarketingGoodsService goodsService;
    @Autowired
    private CateringMarketingGoodsCategoryService goodsCategoryService;
    @Autowired
    private CateringMarketingRepertoryService repertoryService;
    private Long merchantId;

    @Value("${goods.merchant.id}")
    public void setMerchantId(Long id) {
        this.merchantId = id;
    }

    @Override
    public Result saveOrUpdate(TicketAddDTO dto, List<MarketingGoodsTransferDTO> dtos) {
        return saveTodb(dto, dtos);
    }

    @Override
    public Result<PageData<TicketListDTO>> pageList(TicketPageParamDTO paramDTO) {
        IPage<TicketListDTO> iPage = this.baseMapper.pageList(paramDTO.getPage(), paramDTO);
        return Result.succ(new PageData<>(iPage));
    }

    @Override
    public Result<TicketDetailsDTO> getInfo(Long id) {
        TicketDetailsDTO dto = baseMapper.selectInfo(id);
        List<ActivityPageVO> vos = baseMapper.getActivityPage(id);
        if(BaseUtil.judgeList(vos)){
            dto.setActivityList(vos);
        }
        return Result.succ(dto);
    }

    @Override
    public List<TicketBasicInfoDTO> selectBasicInfo(List<Long> ids) {
        return this.baseMapper.selectBasicInfo(ids);
    }

    @Override
    public Result delete(Long id) {
        CateringMarketingTicketEntity one = this.getOne(id);
        if (one == null) {
            return Result.fail("数据不存在");
        }
        if (this.baseMapper.findTicketActivity(id) > 0) {
            return Result.fail("该优惠券关联活动，不允许删除");
        }
        one.setDel(DelEnum.DELETE.getFlag());
        return Result.succ(this.updateById(one));
    }

    @Override
    public Result<List<TicketSelectListDTO>> selectListForRegister(Long referrerId, Integer objectLimit) {
        return Result.succ(this.baseMapper.selectListForRegister(referrerId, objectLimit));
    }

    @Override
    public Result<List<TicketSelectListDTO>> selectListForOrder(Long referrerId, Integer objectLimit) {
        return Result.succ(this.baseMapper.selectListForOrder(referrerId, objectLimit));
    }

    @Override
    @SuppressWarnings("all")
    public CateringMarketingTicketEntity getOne(Long id) {
        QueryWrapper<CateringMarketingTicketEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMarketingTicketEntity::getId, id).eq(CateringMarketingTicketEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return this.getOne(queryWrapper);
    }

    @Override
    public Result<TicketInfoDTO> findTicketInfo(Long id, Long activityId) {
        return Result.succ(this.baseMapper.findTicketInfo(id,activityId));
    }

    @Override
    public Result<TicketInfoDTO> findTicketInfo(Long id) {
        return Result.succ(this.baseMapper.findTicketInfo(id,null));
    }

    @Override
    public Result<List<TicketInfoDTO>> findTicketInfo(List<Long> ids) {
        return Result.succ(this.baseMapper.findTicketInfoByIds(ids));
    }

    @Override
    public Boolean verifyTicket(TicketAddDTO dto) {
        LambdaQueryWrapper<CateringMarketingTicketEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingTicketEntity::getTicketName, dto.getTicketName())
                .eq(CateringMarketingTicketEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringMarketingTicketEntity one = this.getOne(queryWrapper);
        if (one == null) {
            return Boolean.TRUE;
        }
        if (dto.getId() != null) {
            return one.getId().equals(dto.getId());
        }
        return Boolean.FALSE;
    }

    @Override
    public List<TicketSelectListDTO> selectTicket(MarketingTicketSendTicketPartyEnum partyEnum) {
        LambdaQueryWrapper<CateringMarketingTicketEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingTicketEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringMarketingTicketEntity::getSendTicketParty, partyEnum.getStatus())
                .eq(CateringMarketingTicketEntity::getMerchantId, merchantId)
                .orderByDesc(CateringMarketingTicketEntity::getCreateTime);
        IPage<CateringMarketingTicketEntity> iPage = this.page(new Page<>(1, 50), queryWrapper);
        List<CateringMarketingTicketEntity> list = iPage.getRecords();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }
        return ConvertUtils.sourceToTarget(list, TicketSelectListDTO.class);
    }

    @Override
    public int deleteByActivityId(Long activityId) {
        return this.baseMapper.deleteByActivityId(activityId);
    }

    @Override
    public List<CateringMarketingTicketEntity> listByActivityId(Long activityId) {
        LambdaQueryWrapper<CateringMarketingTicketEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingTicketEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringMarketingTicketEntity::getActivityId, activityId);
        return this.list(queryWrapper);
    }

    @Override
    public PageData<MarketingPlatFormActivitySelectListVO> listPlatFormTicket(TicketPageParamDTO dto,MarketingTicketSendTicketPartyEnum partyEnum) {
        IPage<MarketingPlatFormActivitySelectListVO> iPage = this.baseMapper.pagePlatFormTicket(dto.getPage(),dto,partyEnum.getStatus(),merchantId);
        return new PageData<>(iPage);
    }

    @Override
    public List<WxTicketUseShopVo> canUseShop(List<Long> ticketIds) {
        return this.baseMapper.canUseShop(ticketIds);
    }

    @Override
    public List<WxShopTicketInfoVo> findTicketCanUseShop(Long ticketId) {
        return this.baseMapper.findTicketCanUseShop(ticketId);
    }


    @Transactional(rollbackFor = Exception.class)
    public Result saveTodb(TicketAddDTO dto, List<MarketingGoodsTransferDTO> dtos) {
        // 订单优惠券 -- 设置商品限制条件为不限制
        if (MarketingTicketUsefulConditionEnum.ORDER_TICKET.getStatus().equals(dto.getUsefulCondition())) {
            dto.setGoodsLimit(MarketingTicketGoodsLimitEnum.NO_LIMIT.getStatus());
        }
        CateringMarketingTicketEntity entity = ConvertUtils.sourceToTarget(dto, CateringMarketingTicketEntity.class);
        // 生成优惠券编码
        if (dto.getId() == null) {
            entity.setTicketCode("YH"+CodeGenerator.randomCode(10));
            entity.setMerchantId(merchantId);
            entity.setSendTicketParty(MarketingTicketSendTicketPartyEnum.PLATFORM.getStatus());
        }
        boolean b = this.saveOrUpdate(entity);
        if (!b) {
            return Result.fail();
        }
        // 商品限制
        if (CollectionUtils.isNotEmpty(dtos)) {
            dtos.forEach(goods -> {
                CateringMarketingGoodsEntity goodsEntity = ConvertUtils.sourceToTarget(goods, CateringMarketingGoodsEntity.class);
                goodsEntity.setOfId(entity.getId());
                goodsEntity.setOfType(MarketingOfTypeEnum.TICKET.getStatus());
                boolean save = goodsService.save(goodsEntity);
                // 保存商品分类数据
                if (save) {
                    CateringMarketingGoodsCategoryEntity categoryEntity = new CateringMarketingGoodsCategoryEntity();
                    categoryEntity.setGoodsCategoryId(goods.getCategoryId());
                    categoryEntity.setGoodsCategoryName(goods.getCategoryName());
                    categoryEntity.setMGoodsId(goodsEntity.getId());
                    goodsCategoryService.save(categoryEntity);
                }
            });
        }
        // 保存库存数据
        repertoryService.initOrUpdateForOfId(entity.getId(), MarketingOfTypeEnum.TICKET, 9999999);

        return Result.succ(entity.getId());
    }
}