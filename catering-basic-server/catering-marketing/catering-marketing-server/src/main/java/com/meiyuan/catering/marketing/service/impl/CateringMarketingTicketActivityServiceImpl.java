package com.meiyuan.catering.marketing.service.impl;

import com.alibaba.druid.pool.WrapperAdapter;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingTicketActivityMapper;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.entity.*;
import com.meiyuan.catering.marketing.enums.*;
import com.meiyuan.catering.marketing.mq.sender.TicketMqSender;
import com.meiyuan.catering.marketing.service.*;
import com.meiyuan.catering.marketing.service.user.CateringUserTicketService;
import com.meiyuan.catering.marketing.vo.activity.ActivityDetailsVO;
import com.meiyuan.catering.marketing.vo.ticket.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName CateringMarketingTicketActivityServiceImpl
 * @Description
 * @Author gz
 * @Date 2020/8/5 16:18
 * @Version 1.3.0
 */
@Slf4j
@Service
public class CateringMarketingTicketActivityServiceImpl extends ServiceImpl<CateringMarketingTicketActivityMapper, CateringMarketingTicketActivityEntity>
        implements CateringMarketingTicketActivityService {
    @Autowired
    private CateringMarketingTicketService ticketService;
    @Autowired
    private CateringMarketingGoodsService goodsService;
    @Autowired
    private CateringMarketingGoodsCategoryService goodsCategoryService;
    @Autowired
    private CateringMarketingTicketActivityShopService activityShopService;
    @Autowired
    private CateringMarketingRepertoryService repertoryService;
    @Autowired
    private CateringMarketingActivityService activityService;
    @Autowired
    private TicketMqSender mqSender;

    @Override
    public Boolean create(MarketingTicketActivityAddDTO dto) {
        // 验证名称重复
        if (!verifyActivityName(dto.getActivityName(), dto.getId(), dto.getActivityType(), dto.getMerchantId())) {
            throw new CustomException(ErrorCode.TICKET_HAS_NAME_ERROR, ErrorCode.TICKET_HAS_NAME_ERROR_MSG);
        }
        boolean isShopOutGet = MarketingTicketActivityTypeEnum.SHOP_OUT_GET.getStatus().equals(dto.getActivityType());
        if (isShopOutGet) {
            // 判断发放开始时间是否超过rabbitMq延迟最大值
            Long ttl = Duration.between(LocalDateTime.now(), dto.getBeginTime()).toMillis();
            if (ttl > RabbitMqConstant.MAX_X_DELAY_TTL) {
                throw new CustomException("发放开始日期超过系统设置最大值");
            }
        }
        // 验证包含的门店id
        verifyHasShop(dto);
        Map<String, Object> map = saveTicket(dto);
        if (!map.isEmpty() && isShopOutGet) {
            mqSender.sendTicketPushMsg(map, dto.getBeginTime());
        }
        return !map.isEmpty();
    }


    @Override
    public Boolean update(MarketingTicketActivityAddDTO dto) {
        // 验证名称重复
        if (!verifyActivityName(dto.getActivityName(), dto.getId(), dto.getActivityType(), dto.getMerchantId())) {
            throw new CustomException("内容已存在，请修改");
        }
        // 验证包含的门店id
        verifyHasShop(dto);
        boolean isShopOutGet = MarketingTicketActivityTypeEnum.SHOP_OUT_GET.getStatus().equals(dto.getActivityType());
        // 标识是否更改活动开始时间
        boolean flag = false;
        CateringMarketingTicketActivityEntity activityEntity = this.getById(dto.getId());
        if (activityEntity != null) {
            flag = !activityEntity.getBeginTime().isEqual(dto.getBeginTime());
        }
        List<Long> list = null;
        // 保存活动信息
        CateringMarketingTicketActivityEntity entity = ConvertUtils.sourceToTarget(dto, CateringMarketingTicketActivityEntity.class);
        boolean update = this.updateById(entity);
        if (update) {
            Long id = dto.getId();
            // 通过活动id删除关联优惠券信息
            ticketService.deleteByActivityId(id);
            list = saveTicket(dto, id);
            // 删除门店信息
            activityShopService.removeByActivityId(id);
            saveTicketShop(dto.getShopList(), id);
        }
        if (flag && CollectionUtils.isNotEmpty(list) && isShopOutGet) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("ticketIdList", list);
            map.put("activityId", dto.getId());
            mqSender.sendTicketPushMsg(map, dto.getBeginTime());
        }
        return update;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveTicket(MarketingTicketActivityAddDTO dto) {
        Map<String, Object> map = Maps.newHashMap();
        List<Long> resList = null;
        // 保存活动信息
        CateringMarketingTicketActivityEntity entity = ConvertUtils.sourceToTarget(dto, CateringMarketingTicketActivityEntity.class);
        boolean save = this.save(entity);
        if (save) {
            Long activityId = entity.getId();
            map.put("activityId", activityId);
            // 保存优惠券信息
            resList = saveTicket(dto, activityId);
            // 保存活动门店信息
            saveTicketShop(dto.getShopList(), activityId);
        }
        map.put("ticketIdList", resList);
        return map;
    }


    @Override
    public PageData<MarketingTicketActivityListVO> listTicket(MarketingTicketActivityPageParamDTO pageDto) {
        IPage<MarketingTicketActivityListVO> page = this.baseMapper.pageTicketActivity(pageDto.getPage(), pageDto);
        return new PageData<>(page);
    }

    @Override
    public MarketingTicketActivityDetailsVO getDetails(Long id) {
        MarketingTicketActivityDetailsVO vo = this.baseMapper.getDetails(id);
        return vo;
    }

    @Override
    public MarketingTicketActivityEffectVO getEffectInfo(Long id) {
        MarketingTicketActivityEffectVO info = this.baseMapper.getEffectInfo(id);
        List<TicketActivityOrderVO> activityList = info.getActivityList();
        if (CollectionUtils.isNotEmpty(activityList)) {
            // 设置预计成本
            BigDecimal decimal = activityList.stream().map(TicketActivityOrderVO::getTotalCost).reduce(BigDecimal::add).get();
            info.setPredictCost(decimal);
            // 设置实际营业额
            BigDecimal nowTurnover = activityList.stream().map(TicketActivityOrderVO::getTurnoverNew).reduce(BigDecimal::add).get();
            info.setNowTurnover(nowTurnover);
            // 设置实际成本
            BigDecimal practicalCost = activityList.stream().map(TicketActivityOrderVO::getPracticalCost).reduce(BigDecimal::add).get();
            info.setPracticalCost(practicalCost);
        }
        return info;
    }

    @Override
    public List<Long> listTicketActivityHasShop(Long merchantId, LocalDateTime beginTime, LocalDateTime endTime, Long id, Integer activityType) {
        return this.baseMapper.listTicketActivityHasShop(merchantId, beginTime, endTime, id, activityType);
    }

    @Override
    public List<Long> listPlatFormTicketActivityHasShop(Long merchantId, Long activityId) {
        return this.baseMapper.listPlatFormTicketActivityHasShop(merchantId, activityId);
    }

    @Override
    public Boolean freezeActivity(Long id, Long shopId) {
        int num;
        if (shopId != null) {
            // 门店冻结
            num = activityShopService.updateShopTicketStatus(shopId, id);
        } else {
            num = this.baseMapper.updateUpDownStatus(id);
        }
        return num > 0;
    }

    @Override
    public Boolean freezeActivity(Long pActivityId) {
        return this.baseMapper.updateUpDownStatusPlatForm(pActivityId) > 0;
    }

    @Override
    public Boolean deleteActivity(Long id) {
        return this.baseMapper.deleteActivity(id) > 0;
    }

    @Override
    public MarketingTicketAppDetailsVO ticketDetailsApp(Long id, Long shopId) {
        MarketingTicketAppDetailsVO vo = this.baseMapper.ticketDetailsApp(id, shopId);
        if (vo != null) {
            if (vo.getActivityType().equals(MarketingTicketActivityTypeEnum.PLATFORM_SUBSIDY.getStatus())) {
                vo = this.baseMapper.platFormTicketDetailsApp(id, shopId);
                List<TicketAppInfoVO> item = vo.getTicketList();
                BigDecimal decimal = item.stream().map(TicketAppInfoVO::getActivityCost).reduce(BigDecimal::add).get();
                // 设置活动成本
                vo.setActivityCost(decimal);
                BigDecimal varDecimal = new BigDecimal(100);
                // 设置商户承担成本
                vo.setShopCost(decimal.multiply(varDecimal.subtract(vo.getBearDuty())).divide(varDecimal));
                // 设置平台承担成本
                vo.setPlatFormCost(decimal.multiply(vo.getBearDuty()).divide(varDecimal));
            }

        }
        return vo;
    }

    @Override
    public PageData<MarketingPlatFormActivityListVO> listPlatForm(Long merchantId, MarketingPlatFormActivityParamDTO paramDTO) {
        IPage<MarketingPlatFormActivityListVO> iPage = this.baseMapper.listPlatForm(paramDTO.getPage(), merchantId, paramDTO);
        return new PageData<>(iPage);
    }

    @Override
    public Boolean participationPlatFormActivity(MarketingPlatFormActivityShopDTO dto) {
        Long merchantId = dto.getMerchantId();
        // 判断当前商户是否参加了该平台活动
        Long activityId = verifyPlatForm(dto.getActivityId(), merchantId);
        if (activityId != null) {
            // 当前商户参与了该平台活动，直接进行添加门店业务处理
            saveTicketShop(dto.getShopList(), activityId);
            return Boolean.TRUE;
        }
        CateringMarketingActivityEntity one = activityService.findOne(dto.getActivityId());
        if (Objects.isNull(one)) {
            throw new CustomException("参与失败!该平台活动可能已经被删除了~");
        }
        // 保存活动信息
        CateringMarketingTicketActivityEntity entity = new CateringMarketingTicketActivityEntity();
        entity.setMerchantId(dto.getMerchantId());
        entity.setPActivityId(one.getId());
        entity.setActivityName(one.getName());
        entity.setBeginTime(one.getBeginTime());
        entity.setEndTime(one.getEndTime());
        entity.setActivityType(MarketingTicketActivityTypeEnum.PLATFORM_SUBSIDY.getStatus());
        entity.setSource(SourceEnum.PLATFORM.getStatus());
        boolean save = this.save(entity);
        if (save) {

            Long entityId = entity.getId();
            // 保存活动门店信息
            saveTicketShop(dto.getShopList(), entityId);
            // 处理优惠券发送业务
            mqSender.sendPlatFormTicketPushMsg(dto.getActivityId(), one.getBeginTime());
        }


        return save;
    }

    private void saveTicketGoods(Map<Long, List<MarketingGoodsTransferDTO>> goodsMap) {
        if (goodsMap.isEmpty()) {
            return;
        }
        List<CateringMarketingGoodsCategoryEntity> categoryEntityList = Lists.newArrayList();
        List<CateringMarketingGoodsEntity> goodsEntityList = Lists.newArrayList();
        // 商品限制
        goodsMap.forEach((k, v) -> {
            List<CateringMarketingGoodsEntity> collect = v.stream().map(goods -> {
                CateringMarketingGoodsEntity goodsEntity = ConvertUtils.sourceToTarget(goods, CateringMarketingGoodsEntity.class);
                long id = IdWorker.getId();
                goodsEntity.setId(id);
                goodsEntity.setOfId(k);
                goodsEntity.setOfType(MarketingOfTypeEnum.TICKET.getStatus());
                // 商品分类数据
                CateringMarketingGoodsCategoryEntity categoryEntity = new CateringMarketingGoodsCategoryEntity();
                categoryEntity.setGoodsCategoryId(goods.getCategoryId());
                categoryEntity.setGoodsCategoryName(goods.getCategoryName());
                categoryEntity.setMGoodsId(id);
                categoryEntityList.add(categoryEntity);
                return goodsEntity;
            }).collect(Collectors.toList());
            goodsEntityList.addAll(collect);
        });
        boolean saveBatch = goodsService.saveBatch(goodsEntityList);
        if (saveBatch) {
            goodsCategoryService.saveBatch(categoryEntityList);
        }
    }

    @Override
    public MarketingPlatFormActivityDetailsVO platFormActivityDetails(Long id) {
        return this.baseMapper.platFormActivityDetails(id);
    }

    @Override
    public MarketingPlatFormActivityEffectVO platFormActivityEffect(Long id, Long shopId) {
        MarketingPlatFormActivityEffectVO effectVO = null;
        if (shopId != null) {
            effectVO = this.baseMapper.platFormActivityEffectShop(id, shopId);
        } else {
            effectVO = this.baseMapper.platFormActivityEffect(id);
        }
        if (effectVO != null) {
            List<PlatFormTicketOrderInfoVO> item = effectVO.getTicketItem();
            BigDecimal decimal = item.stream().map(PlatFormTicketOrderInfoVO::getActivityCost).reduce(BigDecimal::add).get();
            // 设置活动成本
            effectVO.setActivityCost(decimal);
            BigDecimal varDecimal = new BigDecimal(100);
            // 设置平台承担成本
            effectVO.setPlatFormCost(decimal.multiply(effectVO.getBearDuty()).divide(varDecimal));
            // 设置商户承担成本
            effectVO.setMerchantCost(decimal.multiply(varDecimal.subtract(effectVO.getBearDuty())).divide(varDecimal));
        }
        return effectVO;
    }

    @Override
    public List<TicketGoodsVO> listTicketGoods(Long ticketId, Long merchantId) {
        return this.baseMapper.listTicketGoods(ticketId, merchantId);
    }

    @Override
    public MarketingTicketShopEffectVO shopEffect(Long id, Long shopId) {
        return this.baseMapper.shopEffect(id, shopId);
    }

    @Override
    public List<Long> findTicketShop(Integer ticketType, List<Long> shopList) {
        if(CollectionUtils.isEmpty(shopList)){
            return Lists.newArrayList();
        }
        return this.baseMapper.findTicketShop(ticketType, shopList);
    }

    @Override
    public Map<Long, List<WxMerchantIndexTicketInfoVO>> findShopTicket(List<Long> shopList) {
        if(CollectionUtils.isEmpty(shopList)){
            return Maps.newHashMap();
        }
        List<WxShopTicketInfoVo> list = this.baseMapper.findShopTicket(shopList);
        if (CollectionUtils.isEmpty(list)) {
            return Maps.newHashMap();
        }
        list.forEach(e -> {
            List<WxMerchantIndexTicketInfoVO> ticketList = e.getTicketList();
            if (CollectionUtils.isNotEmpty(ticketList)) {
                ticketList = ticketList.stream().filter(ticket -> {
                    boolean equals = DateTimeUtil.ZERO.equals(ticket.getEffectiveDate());
                    equals |= Arrays.asList(ticket.getEffectiveDate().split(",")).contains(String.valueOf(DateTimeUtil.getWeekOfDate()));
                    return equals;
                }).collect(Collectors.toList());
                List<Long> collect = ticketList.stream()
                        .filter(i -> i.getActivityType().equals(MarketingTicketActivityTypeEnum.PLATFORM_SUBSIDY.getStatus()))
                        .map(WxMerchantIndexTicketInfoVO::getPActivityId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    List<WxMerchantIndexTicketInfoVO> platFormTicket = this.baseMapper.listPlatFormTicket(collect);
                    ticketList = ticketList.stream().filter(i -> !collect.contains(i.getPActivityId())).collect(Collectors.toList());
                    ticketList.addAll(platFormTicket);
                }
                e.setTicketList(ticketList);
            }
        });
        Map<Long, List<WxMerchantIndexTicketInfoVO>> listMap = list.stream().collect(Collectors.toMap(WxShopTicketInfoVo::getShopId, WxShopTicketInfoVo::getTicketList));
        return listMap;
    }

    @Override
    public void saveTicketDataRecord(TicketDataRecordDTO dto) {
        log.debug("记录订单核销完成优惠券使用情况:dto={}", dto);
        if (dto != null && CollectionUtils.isNotEmpty(dto.getTicketIds())) {
            List<TicketDataRecordAddDTO> list = getTicketDataRecordList(dto);
            this.baseMapper.insertTicketDataRecord(list);
        }
    }


    @Override
    public void saveTicketDataRecordBatch(List<TicketDataRecordDTO> dtoList) {
        log.debug("记录订单超时未取餐优惠券使用情况:dtoList={}", dtoList);
        if (CollectionUtils.isNotEmpty(dtoList)) {
            List<TicketDataRecordAddDTO> addList = Lists.newArrayList();
            dtoList.forEach(e -> {
                List<TicketDataRecordAddDTO> list = getTicketDataRecordList(e);
                addList.addAll(list);
            });
            this.baseMapper.insertTicketDataRecord(addList);
        }
    }

    @Override
    public void cancelTicketDataRecord(Long orderId) {
        this.baseMapper.cancelTicketDataRecord(orderId);
    }

    @Override
    public PageData<TicketActivityShopVO> pageDetailsShop(MarketingTicketDetailsShopParamDTO dto) {
        IPage<TicketActivityShopVO> iPage = this.baseMapper.detailsPageShop(dto.getPage(), dto);
        return new PageData<>(iPage);
    }

    @Override
    public void fillTicketStock() {
        this.baseMapper.fillTicketStock();
    }

    @Override
    public void updatePlatFormActivity(Long activityId) {
        LambdaQueryWrapper<CateringMarketingTicketActivityEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingTicketActivityEntity::getPActivityId, activityId);
        List<CateringMarketingTicketActivityEntity> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        ActivityDetailsVO vo = activityService.queryDetailsById(activityId);
        if (vo != null) {
            list.forEach(e -> {
                e.setActivityName(vo.getName());
                e.setBeginTime(vo.getBeginTime());
                e.setEndTime(vo.getEndTime());
            });
            boolean batch = this.updateBatchById(list);
            if (batch) {
                // 处理优惠券发送业务
                mqSender.sendPlatFormTicketPushMsg(activityId, vo.getBeginTime());
            }
        }

    }

    @Override
    public MarketingTicketAppDetailsVO ticketActivityDetails(Long id, Long shopId) {
        return this.baseMapper.ticketActivityDetails(id, shopId);
    }

    @Override
    public List<TicketBasicVO> listBrandTicket() {
        return this.baseMapper.listBrandTicket();
    }

    @Override
    public void deleteForPlatFormActivityId(Long pActivityId) {
        this.baseMapper.removeForPlatFormActivityId(pActivityId);
    }

    @Override
    public Boolean verifyActivityName(String activityName, Long id, Integer activityType, Long merchantId) {
        LambdaQueryWrapper<CateringMarketingTicketActivityEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingTicketActivityEntity::getActivityName, activityName)
                .eq(CateringMarketingTicketActivityEntity::getActivityType, activityType)
                .eq(CateringMarketingTicketActivityEntity::getMerchantId, merchantId)
                .eq(CateringMarketingTicketActivityEntity::getUpDownStatus, MarketingUpDownStatusEnum.UP.getStatus())
                .gt(CateringMarketingTicketActivityEntity::getEndTime, LocalDateTime.now())
                .eq(CateringMarketingTicketActivityEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringMarketingTicketActivityEntity one = this.getOne(queryWrapper);
        if (one == null) {
            return true;
        }
        if (Objects.nonNull(id)) {
            return one.getId().equals(id);
        }
        return false;
    }

    /**
     * 验证当前商户是否参与了平台活动
     *
     * @param activityId
     * @param merchantId
     * @return 活动id
     */
    private Long verifyPlatForm(Long activityId, Long merchantId) {
        LambdaQueryWrapper<CateringMarketingTicketActivityEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingTicketActivityEntity::getPActivityId, activityId)
                .eq(CateringMarketingTicketActivityEntity::getMerchantId, merchantId)
                .eq(CateringMarketingTicketActivityEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringMarketingTicketActivityEntity one = this.getOne(queryWrapper);
        if (Objects.isNull(one)) {
            return null;
        }
        return one.getId();
    }


    private List<Long> saveTicket(MarketingTicketActivityAddDTO dto, Long activityId) {
        Map<Long, Integer> map = Maps.newHashMap();
        List<CateringMarketingTicketEntity> collect = dto.getTicketList().stream().map(e -> {
            CateringMarketingTicketEntity ticketEntity = ConvertUtils.sourceToTarget(e, CateringMarketingTicketEntity.class);
            long id = IdWorker.getId();
            ticketEntity.setId(id);
            ticketEntity.setMerchantId(dto.getMerchantId());
            ticketEntity.setActivityId(activityId);
            ticketEntity.setTicketCode("YH" + CodeGenerator.randomCode(10));
            ticketEntity.setChildType(MarketingTicketChildTypeEnum.DISCOUNT.getStatus());
            ticketEntity.setLimitQuantity(1);
            ticketEntity.setIndateType(MarketingTicketIndateTypeEnum.DAYS.getStatus());
            ticketEntity.setUsefulCondition(MarketingTicketUsefulConditionEnum.ORDER_TICKET.getStatus());
            ticketEntity.setObjectLimit(MarketingUsingObjectEnum.ALL.getStatus());
            ticketEntity.setSendTicketParty(MarketingTicketSendTicketPartyEnum.BRAND.getStatus());
            ticketEntity.setGoodsLimit(MarketingGoodsLimitEnum.ALL.getStatus());
            ticketEntity.setPublishQuantity(e.getStock());
            map.put(id, e.getStock());
            return ticketEntity;
        }).collect(Collectors.toList());
        boolean batch = ticketService.saveBatch(collect);
        if (batch) {
            // 保存库存数据
            saveTicketRepertory(map);
        }
        return new ArrayList<>(map.keySet());
    }

    private void saveTicketRepertory(Map<Long, Integer> map) {
        List<CateringMarketingRepertoryEntity> list = Lists.newArrayList();
        map.forEach((k, v) -> {
            CateringMarketingRepertoryEntity entity = new CateringMarketingRepertoryEntity();
            entity.setTotalInventory(v);
            entity.setOfId(k);
            entity.setResidualInventory(v);
            entity.setOfType(MarketingOfTypeEnum.TICKET.getStatus());
            entity.setSoldOut(0);
            list.add(entity);
        });
        repertoryService.saveBatch(list);
    }


    private void saveTicketShop(List<Long> shopIds, Long activityId) {
        List<CateringMarketingTicketActivityShopEntity> collect = shopIds.stream().map(i -> {
            CateringMarketingTicketActivityShopEntity entity = new CateringMarketingTicketActivityShopEntity();
            entity.setActivityId(activityId);
            entity.setShopId(i);
            entity.setShopTicketStatus(MarketingUpDownStatusEnum.UP.getStatus());
            return entity;
        }).collect(Collectors.toList());
        activityShopService.saveBatch(collect);
    }


    private void verifyHasShop(MarketingTicketActivityAddDTO dto) {
        List<Long> hasShop = listTicketActivityHasShop(dto.getMerchantId(), dto.getBeginTime(), dto.getEndTime(), dto.getId(), dto.getActivityType());
        if (CollectionUtils.isNotEmpty(hasShop)) {
            List<Long> collect = dto.getShopList().stream().filter(i -> hasShop.contains(i)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                throw new CustomException(ErrorCode.TICKET_HAS_SHOP_ERROR, ErrorCode.TICKET_HAS_SHOP_ERROR_MSG, collect);
            }
        }
    }


    private List<TicketDataRecordAddDTO> getTicketDataRecordList(TicketDataRecordDTO dto) {
        boolean isBrandNew = this.baseMapper.selectTicketDataRecord(dto.getMerchantId(), dto.getUserId()) == 0;
        List<TicketDataRecordAddDTO> collect = dto.getTicketIds().stream().map(e -> {
            TicketDataRecordAddDTO addDto = new TicketDataRecordAddDTO();
            addDto.setId(IdWorker.getId());
            addDto.setMerchantId(dto.getMerchantId());
            addDto.setTicketId(e);
            addDto.setOrderId(dto.getOrderId());
            addDto.setShopId(dto.getShopId());
            addDto.setNewMember(dto.getNewMember());
            addDto.setOrderAmount(dto.getOrderAmount());
            addDto.setBrandNew(isBrandNew);
            addDto.setUserId(dto.getUserId());
            addDto.setOrderSuccess(Boolean.TRUE);
            addDto.setDiscountBeforeFee(dto.getDiscountBeforeFee());
            return addDto;
        }).collect(Collectors.toList());
        return collect;
    }
}
