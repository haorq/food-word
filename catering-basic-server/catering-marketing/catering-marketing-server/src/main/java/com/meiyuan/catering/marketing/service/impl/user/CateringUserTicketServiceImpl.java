package com.meiyuan.catering.marketing.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.dto.user.AddIntegralRecordDTO;
import com.meiyuan.catering.core.enums.base.ActivityTypeEnum;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dao.user.CateringUserTicketMapper;
import com.meiyuan.catering.marketing.dto.activity.ActivityInfoDTO;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketEntity;
import com.meiyuan.catering.marketing.entity.user.CateringUserTicketEntity;
import com.meiyuan.catering.marketing.enums.*;
import com.meiyuan.catering.marketing.service.*;
import com.meiyuan.catering.marketing.service.user.CateringUserTicketService;
import com.meiyuan.catering.marketing.vo.ticket.MarketingTicketAppDetailsVO;
import com.meiyuan.catering.marketing.vo.ticket.TicketBasicVO;
import com.meiyuan.catering.marketing.vo.ticket.WxMerchantIndexTicketInfoVO;
import com.meiyuan.catering.marketing.vo.ticket.WxTicketUseShopVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName CateringUserTicketServiceImpl
 * @Description
 * @Author gz
 * @Date 2020/3/19 18:05
 * @Version 1.1
 */
@Slf4j
@Service
public class CateringUserTicketServiceImpl extends ServiceImpl<CateringUserTicketMapper, CateringUserTicketEntity> implements CateringUserTicketService {

    @Autowired
    private CateringMarketingTicketService ticketService;
    @Autowired
    private CateringMarketingRepertoryService repertoryService;
    @Autowired
    private CateringMarketingRecordService recordService;
    @Autowired
    private CateringMarketingActivityService activityService;
    @Autowired
    private CateringMarketingTicketActivityService ticketActivityService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @SuppressWarnings("all")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertTicket(Long userId, Long ticketId, Integer userType, Integer restrict, Long activityId, Long shopId) {
        Result<TicketInfoDTO> result = ticketService.findTicketInfo(ticketId, activityId);
        if (result.failure()) {
            return result;
        }
        TicketInfoDTO data = result.getData();
        if (data == null) {
            return Result.fail("优惠券数据不存在");
        }
        if (data.getActivityId() != null) {
            MarketingTicketAppDetailsVO vo = ticketActivityService.ticketActivityDetails(data.getActivityId(), shopId);
            if (vo == null) {
                return Result.fail("门店活动可能已删除");
            }
            if (vo.getStatus().equals(MarketingStatusEnum.FREEZE.getStatus())) {
                return Result.fail("活动已冻结");
            }
            if (vo.getEndTime().isBefore(LocalDateTime.now())) {
                return Result.fail("活动已结束");
            }
            if (vo.getBeginTime().isAfter(LocalDateTime.now())) {
                return Result.fail("活动未开始");
            }
        }
        if (data.getResidualInventory() <= 0) {
            return Result.fail("优惠券已被领完，看看其他优惠吧~");
        }
        if (restrict != null && ticketCanGet(userId, ticketId, restrict)) {
            return Result.fail("平台券限制领取");
        }
        if (data.getLimitQuantity() != 0 && ticketCanGet(userId, ticketId, data.getLimitQuantity())) {
            return Result.fail("限制领取");
        }
        CateringUserTicketEntity entity = new CateringUserTicketEntity();
        entity.setUserType(userType);
        entity.setTicketId(ticketId);
        if (activityId == null) {
            entity.setTicketActivityId(data.getActivityId());
        } else {
            entity.setTicketActivityId(activityId);
        }
        entity.setTicketName(data.getTicketName());
        entity.setUserId(userId);
        entity.setGetTime(LocalDateTime.now());
        LocalDateTime useEndTime = data.getUseEndTime();
        if (MarketingTicketIndateTypeEnum.DAYS.getStatus().equals(data.getIndateType())) {
            useEndTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59)).plusDays(data.getUseDays());
        }
        entity.setUseEndTime(useEndTime);
        entityHandle(entity, false);
        boolean save = this.save(entity);
        if (save) {
            // 扣减优惠券库存
            repertoryService.deductingTheInventory(ticketId, 1, MarketingOfTypeEnum.TICKET, activityId);
            // 记录用户已领取数量
            recordService.recordTicketUserGet(ticketId, userId, 1);
        }
        return Result.succ();
    }

    @SuppressWarnings("all")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertTicketBatch(List<Long> ticketIds, Long userId, Integer userType, boolean oldUser) {
        if (CollectionUtils.isEmpty(ticketIds)) {
            return Result.fail("优惠券ids集合不能为空");
        }
        Result<List<TicketInfoDTO>> listResult = ticketService.findTicketInfo(ticketIds);
        if (listResult.failure() || CollectionUtils.isEmpty(listResult.getData())) {
            return Result.fail("优惠券数据不存在");
        }
        List<TicketInfoDTO> data = listResult.getData();
        /*if (oldUser) {
            List<TicketInfoDTO> dtos = data.stream().filter(i ->
                    i.getBeginTime().isBefore(LocalDateTime.now())
                            && i.getEndTime().isAfter(LocalDateTime.now())
                            && i.getResidualInventory() > 0
                            && MarketingTicketOnClickEnum.NO_ONCLICK.getStatus().equals(i.getOnClick())
            ).collect(Collectors.toList());
            if (dtos.size() == 0) {
                return Result.fail(ErrorCode.GROUND_PUSHER_NO_TICKET, "您已经是平台的老用户，暂不可领取本次优惠，去看看其他的吧~");
            }
        }*/
        // 获取用户存在的优惠券
        List<Long> list = this.getUserTicketIdList(userId);
        List<CateringUserTicketEntity> collect = data.stream()
                .filter(i -> {
                    boolean flag = i.getResidualInventory() > 0;
                    if (oldUser) {
                        // 过滤限制的优惠券
                        if (i.getLimitQuantity() != 0) {
                            flag &= !ticketCanGet(userId, i.getId(), i.getLimitQuantity());
                        }
                        flag &= !list.contains(i.getId());
                    }
                    return flag;
                })
                .map(e -> {
                    CateringUserTicketEntity entity = new CateringUserTicketEntity();
                    entity.setUserType(userType);
                    entity.setTicketId(e.getId());
                    entity.setTicketName(e.getTicketName());
                    entity.setUserId(userId);
                    entity.setGetTime(LocalDateTime.now());
                    LocalDateTime useEndTime = e.getUseEndTime();
                    if (MarketingTicketIndateTypeEnum.DAYS.getStatus().equals(e.getIndateType())) {
                        useEndTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59)).plusDays(e.getUseDays());
                    }
                    entity.setUseEndTime(useEndTime);
                    entityHandle(entity, false);
                    return entity;
                }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return Result.fail("该用户没有可以领取的优惠券");
        }
        boolean b = this.saveBatch(collect);
        if (b) {
            // 扣减优惠券库存
            List<Long> ids = collect.stream().map(CateringUserTicketEntity::getTicketId).collect(Collectors.toList());
            repertoryService.deductingTheInventoryBatch(ids, 1, MarketingOfTypeEnum.TICKET, null);
            // 记录用户领取数量
            recordService.recordTicketUserGetBatch(ids, userId, 1);
        }
        return Result.succ();
    }

    @Override
    public Result useOrderTickets(List<Long> userTicketIds, Long orderId) {
        Collection<CateringUserTicketEntity> list = this.listByIds(userTicketIds);
        for (CateringUserTicketEntity entity : list) {
            if (entity == null || entity.getDel()) {
                return Result.fail("数据不存在");
            }
            entity.setOrderId(orderId);
            entity.setUseTime(LocalDateTime.now());
            entity.setUsed(Boolean.TRUE);
            entityHandle(entity, true);
        }
        boolean b = this.updateBatchById(list);
        if (b) {
            Map<String, Object> map = this.baseMapper.selectOrdersInfo(orderId);
            Long merchantId = Long.valueOf(map.get("merchantId").toString());
            Long userId = Long.valueOf(map.get("userId").toString());
            // boolean isBrandNew = this.baseMapper.selectTicketDataRecord(merchantId,userId)==0;
            // 记录优惠券拉新数据
            List<TicketDataRecordAddDTO> collect = userTicketIds.stream().map(e -> {
                TicketDataRecordAddDTO dto = new TicketDataRecordAddDTO();
                dto.setId(IdWorker.getId());
                dto.setTicketId(e);
                dto.setUserId(userId);
                dto.setOrderAmount(new BigDecimal(map.get("orderAmount").toString()));
                dto.setShopId(Long.valueOf(map.get("shopId").toString()));
                dto.setMerchantId(merchantId);
                dto.setOrderId(orderId);
                dto.setDiscountBeforeFee(new BigDecimal(map.get("discountBeforeFee").toString()) );
                dto.setOrderSuccess(Boolean.FALSE);
                return dto;
            }).collect(Collectors.toList());
            this.baseMapper.insertTicketDataRecord(collect);
        }
        return Result.succ(b);
    }

    @Override
    public Result consumeTicket(Long userTicketId) {
        CateringUserTicketEntity entity = this.getById(userTicketId);
        if (entity == null || entity.getDel()) {
            return Result.fail("数据不存在");
        }
        entity.setConsume(Boolean.TRUE);
        boolean update = this.updateById(entity);
        if (update) {
            // 处理再次发放优惠券业务
            this.sendTicketAgain(userTicketId);
        }
        return update ? Result.succ() : Result.fail();
    }

    @Override
    public Result sendTicketAgain(Long userTicketId) {
        CateringUserTicketEntity entity = this.getById(userTicketId);
        if (entity == null || entity.getDel()) {
            return Result.fail("数据不存在");
        }
        // 优惠券活动ID
        Long ticketActivityId = entity.getTicketActivityId();
        PushTicketToUserDTO activity = activityService.findPlatFormActivity(ticketActivityId);
        if (activity == null) {
            log.debug("订单核销完成再次发券信息【系统没有可以发放的优惠券】");
            return Result.fail("没有可以发放的优惠券");
        }
        if (activity.getUpDownStatus().equals(MarketingUpDownStatusEnum.DOWN.getStatus())) {
            log.debug("订单核销完成再次发券【活动已冻结】");
            return Result.fail("活动已冻结");
        }
        if (activity.getEndTime().isBefore(LocalDateTime.now())) {
            log.debug("订单核销完成再次发券【活动已结束】");
            return Result.fail("活动已结束");
        }
        Integer restrict = activity.getReceiveRestrict();
        if (restrict != null) {
            // 再次进行发放优惠券处理
            Long ticketRuleRecordId = entity.getTicketRuleRecordId();
            Long userId = entity.getUserId();
            boolean b = activityCanGet(userId, ticketActivityId, ticketRuleRecordId, restrict);
            if (b) {
                log.debug("订单核销完成再次发券【限制领取】");
                return Result.fail("限制领取");
            }
            CateringMarketingTicketEntity one = ticketService.getOne(entity.getTicketId());
            if (one == null) {
                return Result.fail("优惠券数据不存在");
            }
            List<Long> ticketRuleRecList = Lists.newArrayList(ticketRuleRecordId);
            CateringUserTicketEntity newEntity = new CateringUserTicketEntity();
            newEntity.setGetTime(LocalDateTime.now());
            newEntity.setTicketName(entity.getTicketName());
            newEntity.setUsed(Boolean.FALSE);
            newEntity.setTicketRuleRecordId(ticketRuleRecordId);
            LocalDateTime useEndTime = one.getUseEndTime();
            if (MarketingTicketIndateTypeEnum.DAYS.getStatus().equals(one.getIndateType())) {
                useEndTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59)).plusDays(one.getUseDays());
            }
            newEntity.setUseEndTime(useEndTime);
            newEntity.setTicketActivityId(ticketActivityId);
            newEntity.setUserType(entity.getUserType());
            newEntity.setUserId(userId);
            newEntity.setTicketId(entity.getTicketId());

            boolean save = this.save(newEntity);
            if (save) {
                // 扣减优惠券库存
                repertoryService.deductingTheInventoryBatch(ticketRuleRecList);
                // 记录用户的领取数量
                this.addTicketGetRecord(ticketActivityId, Lists.newArrayList(userId), ticketRuleRecList);
            }

        }
        return Result.fail();
    }

    @Override
    public Result<List<UserTicketDetailsDTO>> listUserAvailableTicket(Long userId) {
        List<UserTicketDetailsDTO> list = this.baseMapper.listAvailableTicket(userId);
        if (CollectionUtils.isEmpty(list)) {
            return Result.succ(list);
        }
        // 设置优惠券可使用门店
        List<Long> ticketList = list.stream().map(UserTicketDetailsDTO::getUserTicketId).collect(Collectors.toList());
        List<WxTicketUseShopVo> vos = ticketService.canUseShop(ticketList);
        if (CollectionUtils.isNotEmpty(vos)) {
            Map<Long, List<Long>> shopMap = vos.stream().collect(Collectors.toMap(WxTicketUseShopVo::getTicketId, WxTicketUseShopVo::getShopList));
            list.forEach(e -> {
                e.setShopList(shopMap.getOrDefault(e.getUserTicketId(), Collections.EMPTY_LIST));
            });
        }
        return Result.succ(list);
    }

    @Override
    public Result<List<UserTicketDetailsDTO>> getUserTicketInfo(List<Long> userTicketIds) {
        if (CollectionUtils.isEmpty(userTicketIds)) {
            return Result.succ();
        }
        List<UserTicketDetailsDTO> userTicketInfoList = this.baseMapper.getUserTicketInfo(userTicketIds);

        if (CollectionUtils.isEmpty(userTicketInfoList)) {
            return Result.succ();
        }

        List<Long> ticketIds = userTicketInfoList.stream().map(UserTicketDetailsDTO::getUserTicketId).collect(Collectors.toList());
        List<WxTicketUseShopVo> vos = ticketService.canUseShop(ticketIds);
        if (CollectionUtils.isNotEmpty(vos)) {
            // 由于会出现不同补贴活动关联不同门店导致一张优惠券可以在多个门店使用的情况，key值变更为用户优惠券主键id
            Map<Long/* ticket_id */, List<Long>/* shop_id */> shopMap = vos.stream().collect(Collectors.toMap(WxTicketUseShopVo::getTicketId, WxTicketUseShopVo::getShopList));
            for (UserTicketDetailsDTO dto : userTicketInfoList) {
                dto.setShopList(shopMap.get(dto.getUserTicketId()));
            }
        }
        return Result.succ(userTicketInfoList);
    }


    @Override
    public PageData<TicketWechatListDTO> pageMyTicket(Long pageNo, Long pageSize, Integer status, Long userId, Integer sendTicketParty) {
        IPage<TicketWechatListDTO> page = this.baseMapper.pageMyTicket(new Page(pageNo, pageSize), status, userId, sendTicketParty);
        return new PageData<>(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushTicketToUser(Long ticket, String ticketName, Integer totalRepertory, Integer objectLimit, LocalDateTime useEndTime, Long activityId) {
        List<Long> ids = getUserIds(objectLimit);
        // 发送优惠券
        pushTicket(ticket, ticketName, totalRepertory, objectLimit, ids, useEndTime, activityId);

    }

    private void pushTicket(Long ticket, String ticketName, Integer totalRepertory, Integer objectLimit, List<Long> ids, LocalDateTime useEndTime, Long activityId) {
        int count = ids.size();
        if (totalRepertory < count) {
            count = totalRepertory;
        }
        List<CateringUserTicketEntity> collect = ids.stream().limit(count).map(userId -> {
            CateringUserTicketEntity entity = new CateringUserTicketEntity();
            entity.setUserType(objectLimit);
            entity.setUserId(userId);
            entity.setTicketId(ticket);
            entity.setTicketName(ticketName);
            entity.setGetTime(LocalDateTime.now());
            entity.setUsed(Boolean.FALSE);
            entity.setTicketActivityId(activityId);
            entity.setUseEndTime(useEndTime);
            return entity;
        }).collect(Collectors.toList());
        boolean batch = this.saveBatch(collect);
        if (batch) {
            // 扣减优惠券库存
            repertoryService.deductingTheInventory(ticket, count, MarketingOfTypeEnum.TICKET, null);
            // 记录用户的领取数量
            recordService.recordTicketUserGetBatchUserId(ids, ticket, 1);
        }
    }

    @Override
    public Result<List<TicketWxIndexDTO>> indexTicketInfo(Integer size, Long userId) {
        PageData<TicketWechatListDTO> result = this.pageMyTicket(1L, size.longValue(), 1, userId, null);
        List<TicketWechatListDTO> dtoList = result.getList();
        List<TicketWxIndexDTO> list = ConvertUtils.sourceToTarget(dtoList, TicketWxIndexDTO.class);
        return Result.succ(list);
    }

    @Override
    public void returnTicket(Long userTicketId) {
        CateringUserTicketEntity entity = this.getById(userTicketId);
        if (entity == null || entity.getDel()) {
            throw new CustomException("退还用户优惠券失败!数据不存在");
        }
        entity.setOrderId(null);
        entity.setUsed(Boolean.FALSE);
        entity.setUseTime(null);
        entityHandle(entity, true);
        this.updateById(entity);
    }

    @Override
    public Map<Integer, Integer> countMap(Long userId) {
        Map<Integer, Integer> map = Maps.newHashMap();
        List<Map<String, Object>> list = this.baseMapper.countStatus(userId);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for (Map<String, Object> resMap : list) {
            map.put(Integer.valueOf(resMap.get("status").toString()), Integer.valueOf(resMap.get("number").toString()));
        }
        return map;
    }

    @Override
    public List<WxMerchantIndexTicketInfoVO> listWxMerchantIndexTicket(Long shopId, Long userId) {
        List<WxMerchantIndexTicketInfoVO> vos = this.baseMapper.listWxMerchantIndexTicket(shopId, userId);
        if (CollectionUtils.isNotEmpty(vos)) {
            vos = vos.stream().filter(e -> {
                boolean equals = DateTimeUtil.ZERO.equals(e.getEffectiveDate());
                equals |= Arrays.asList(e.getEffectiveDate().split(",")).contains(String.valueOf(DateTimeUtil.getWeekOfDate()));
                return equals;
            }).collect(Collectors.toList());
            List<Long> collect = vos.stream().filter(i -> i.getActivityType().equals(MarketingTicketActivityTypeEnum.PLATFORM_SUBSIDY.getStatus())).map(WxMerchantIndexTicketInfoVO::getPActivityId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                List<WxMerchantIndexTicketInfoVO> list = this.baseMapper.listPlatFormTicket(collect);
                vos = vos.stream().filter(i -> !collect.contains(i.getPActivityId())).collect(Collectors.toList());
                vos.addAll(list);
            }
        }
        return vos;
    }

    @SuppressWarnings("all")
    @Override
    public void pushPlatFormTicketToUser(List<PushTicketToUserDTO> list) {
        PushTicketToUserDTO dto = list.stream().findFirst().get();
        Integer objectLimit = dto.getObjectLimit();
        List<Long> userIds = getUserIds(objectLimit);
        for (PushTicketToUserDTO ticket : list) {
            List<Long> userGetList = this.baseMapper.countGetRecord(ticket.getTicketRuleRecordId(), objectLimit);
            List<Long> canPushUserIds = userIds;
            if (CollectionUtils.isNotEmpty(userGetList)) {
                canPushUserIds = userIds.stream().filter(i -> !userGetList.contains(i)).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(canPushUserIds)) {
                    continue;
                }
            }
            //List<Long> userList = Lists.newArrayList();
            int count = canPushUserIds.size();
            if (ticket.getPublishQuantity() < count) {
                count = ticket.getPublishQuantity();
            }
            List<CateringUserTicketEntity> collect = canPushUserIds.stream().limit(count).map(userId -> {
                CateringUserTicketEntity entity = new CateringUserTicketEntity();
                entity.setTicketActivityId(ticket.getId());
                entity.setUserType(objectLimit);
                entity.setUserId(userId);
                entity.setTicketRuleRecordId(ticket.getTicketRuleRecordId());
                entity.setTicketId(ticket.getTicketId());
                entity.setTicketName(ticket.getTicketName());
                entity.setGetTime(LocalDateTime.now());
                entity.setUsed(Boolean.FALSE);
                LocalDateTime useEndTime = ticket.getUseEndTime();
                if (MarketingTicketIndateTypeEnum.DAYS.getStatus().equals(ticket.getIndateType())) {
                    useEndTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59)).plusDays(ticket.getUseDays());
                }
                entity.setUseEndTime(useEndTime);
                // userList.add(userId);
                return entity;
            }).collect(Collectors.toList());
            boolean batch = this.saveBatch(collect);
            if (batch) {
                List<Long> ticketRuleRecList = collect.stream().map(CateringUserTicketEntity::getTicketRuleRecordId).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ticketRuleRecList)) {
                    // 扣减优惠券库存
                    repertoryService.deductingTheInventoryBatch(ticketRuleRecList,collect.size());
                    // 记录用户的领取数量
                    this.addTicketGetRecord(ticket.getId(), collect.stream().map(CateringUserTicketEntity::getUserId).collect(Collectors.toList()), ticketRuleRecList);
                }
                //recordService.recordTicketUserGetBatchUserId(userList, ticket.getId(), 1);
            }
        }
    }

    private void addTicketGetRecord(Long activityId, List<Long> userIds, List<Long> ticketRuleRecList) {
        List<AddIntegralRecordDTO> collect = Lists.newArrayList();
        userIds.forEach(e -> {
            ticketRuleRecList.forEach(rule -> {
                AddIntegralRecordDTO dto = new AddIntegralRecordDTO();
                dto.setId(IdWorker.getId());
                dto.setActivityId(activityId);
                dto.setActivityType(ActivityTypeEnum.SUBSIDY.getStatus());
                dto.setActivityRuleId(rule);
                dto.setGetNum(1);
                dto.setUserId(e);
                dto.setRewardType(1);
                collect.add(dto);
            });
        });
        int size = collect.size();
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (int i = 0; i < size; i++) {
                this.baseMapper.addIntegralGetRecord(collect.get(i));
                if (i % 500 == 0 || i == size - 1) {
                    //手动每500条提交一次，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
        }catch (Exception e){
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
        // this.baseMapper.addIntegralGetRecordBatch(collect);
    }

    private void addRecordList(List<AddIntegralRecordDTO> recordList, AddIntegralRecordDTO dto) {
        if (CollectionUtils.isEmpty(recordList)) {
            recordList.add(dto);
        } else {
            List<Long> activityList = recordList.stream().map(AddIntegralRecordDTO::getActivityId).collect(Collectors.toList());
            List<Long> activityRuleList = recordList.stream().map(AddIntegralRecordDTO::getActivityRuleId).collect(Collectors.toList());
            boolean contains = activityList.contains(dto.getActivityId());
            if (!contains) {
                recordList.add(dto);
            } else {
                if (CollectionUtils.isNotEmpty(activityRuleList)) {
                    boolean containsRuleId = activityRuleList.contains(dto.getActivityRuleId());
                    if (!containsRuleId) {
                        recordList.add(dto);
                    }
                }
            }
        }


    }


    @SuppressWarnings("all")
    @Override
    public Result<Boolean> insertTicketBatch(List<ActivityInfoDTO> ticketList, Long userId, Integer userType) {
        boolean resFlag = false;
        List<AddIntegralRecordDTO> recordList = Lists.newArrayList();
        List<CateringUserTicketEntity> collect = ticketList.stream().filter(i -> {
            boolean flag = i.getBeginTime().isBefore(LocalDateTime.now())
                    && i.getEndTime().isAfter(LocalDateTime.now())
                    && i.getPublishQuantity() > 0;
            // 过滤限制的优惠券
            if (i.getLimitQuantity() != 0) {
                flag &= !activityCanGet(userId, i.getId(), i.getTicketRuleRecordId(), i.getLimitQuantity());
            }
            return flag;
        }).map(ticket -> {
            AddIntegralRecordDTO dto = new AddIntegralRecordDTO();
            dto.setId(IdWorker.getId());
            dto.setActivityRuleId(ticket.getTicketRuleRecordId());
            dto.setRewardType(1);
            dto.setUserId(userId);
            dto.setGetNum(1);
            dto.setActivityId(ticket.getId());
            dto.setActivityType(ticket.getActivityType());
            addRecordList(recordList, dto);
            CateringUserTicketEntity entity = new CateringUserTicketEntity();
            entity.setUserType(userType);
            entity.setUserId(userId);
            entity.setTicketId(ticket.getTicketId());
            entity.setTicketName(ticket.getTicketName());
            entity.setTicketRuleRecordId(ticket.getTicketRuleRecordId());
            entity.setGetTime(LocalDateTime.now());
            entity.setUsed(Boolean.FALSE);
            entity.setUseEndTime(ticket.getUseEndTime());
            entity.setTicketActivityId(ticket.getId());
            return entity;
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            resFlag = true;
        }
        boolean batch = this.saveBatch(collect);
        if (batch) {
            List<Long> tRuleRecList = collect.stream().map(CateringUserTicketEntity::getTicketRuleRecordId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tRuleRecList)) {
                // 扣减优惠券库存
                repertoryService.deductingTheInventoryBatch(tRuleRecList);
                // 记录用户的领取数量
                addTicketRecord(recordList);
            }
            // recordService.recordTicketUserGetBatch(ticketIds, userId, 1);
        }
        return Result.succ(resFlag);
    }


    @Override
    public List<Long> listUserNoUsedTicket(List<Long> userTicketIds) {
        LambdaQueryWrapper<CateringUserTicketEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CateringUserTicketEntity::getId, userTicketIds)
                .eq(CateringUserTicketEntity::getUsed, Boolean.FALSE);
        return this.list(wrapper).stream().map(CateringUserTicketEntity::getId).collect(Collectors.toList());
    }

    @Override
    public void saveBrandTicketForNewRegister(Long userId) {
        List<TicketBasicVO> vos = ticketActivityService.listBrandTicket();
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }
        List<CateringUserTicketEntity> collect = vos.stream().filter(i -> i.getStock() > 0).map(e -> {
            CateringUserTicketEntity entity = new CateringUserTicketEntity();
            entity.setGetTime(LocalDateTime.now());
            entity.setUserId(userId);
            entity.setUserType(MarketingUsingObjectEnum.ENTERPRISE.getStatus());
            entity.setUsed(Boolean.FALSE);
            entity.setTicketActivityId(e.getActivityId());
            entity.setTicketId(e.getTicketId());
            entity.setTicketName(e.getTicketName());
            LocalDateTime useEndTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59)).plusDays(e.getUseDays());
            entity.setUseEndTime(useEndTime);
            return entity;
        }).collect(Collectors.toList());
        boolean b = this.saveBatch(collect);
        if (b) {
            // 扣减优惠券库存
            List<Long> ids = collect.stream().map(CateringUserTicketEntity::getTicketId).collect(Collectors.toList());
            repertoryService.deductingTheInventoryBatch(ids, 1, MarketingOfTypeEnum.TICKET, null);
            // 记录用户领取数量
            recordService.recordTicketUserGetBatch(ids, userId, 1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer userH5TicketUpdate(Long userId, List<Long> recordIds) {
        Integer update = this.baseMapper.userH5TicketUpdate(userId, recordIds);
        this.baseMapper.userH5TicketRecord(userId,recordIds);
        this.baseMapper.userH5TicketActivityRecord(userId,recordIds);
        return update;
    }

    private List<Long> getUserIds(Integer objectLimit) {
        List<Long> ids = Lists.newArrayList();
        // 企业
        if (MarketingUsingObjectEnum.PERSONAL.getStatus().equals(objectLimit)) {
            ids.addAll(this.baseMapper.countUser());
        } else if (MarketingUsingObjectEnum.ENTERPRISE.getStatus().equals(objectLimit)) {
            // 个人
            ids.addAll(this.baseMapper.countCompany());
        } else {
            // 全部用户
            ids.addAll(this.baseMapper.countAll());
        }
        return ids;
    }

    private void entityHandle(CateringUserTicketEntity entity, boolean isUpdate) {
        if (isUpdate) {
            entity.setUpdateTime(LocalDateTime.now());
            return;
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 验证该优惠券该用户是否限制领取
     *
     * @param userId        用户id
     * @param ticketId      优惠券id
     * @param limitQuantity 限购数
     * @return true-达到限制，不能领取；false--未到限制，可以领取
     */
    private boolean ticketCanGet(Long userId, Long ticketId, Integer limitQuantity) {
        Integer record = recordService.getUserRecord(userId, ticketId, MarketingOfTypeEnum.TICKET);
        return record >= limitQuantity;
    }

    private boolean activityCanGet(Long userId, Long activityId, Long aRuleId, Integer limitQuantity) {
        Integer record = this.baseMapper.countUserGetRecord(activityId, userId, aRuleId);
        if (record == null) {
            record = 0;
        }
        return record >= limitQuantity;
    }


    private List<Long> getUserTicketIdList(Long userId) {
        LambdaQueryWrapper<CateringUserTicketEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringUserTicketEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringUserTicketEntity::getUserId, userId)
                .eq(CateringUserTicketEntity::getConsume, Boolean.FALSE)
                .isNull(CateringUserTicketEntity::getTicketActivityId);
        List<CateringUserTicketEntity> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(CateringUserTicketEntity::getTicketId).collect(Collectors.toList());
    }


    private void addTicketRecord(List<AddIntegralRecordDTO> recordList) {
        recordList.forEach(e -> {
            Integer record = this.baseMapper.countUserGetRecord(e.getActivityId(), e.getUserId(), e.getActivityRuleId());
            if (record == null) {
                record = 0;
            }
            if (record > 0) {
                this.baseMapper.updateIntegralGetRecord(e.getActivityId(), e.getUserId(), e.getActivityRuleId());
            } else {
                this.baseMapper.addIntegralGetRecord(e);
            }
        });
    }

    @Override
    public UserTicketDetailsDTO getUserTicketInfoByUserTicketId(Long ticketId) {
        UserTicketDetailsDTO userTicketDetailsDTO = this.baseMapper.getUserTicketInfoByUserTicketId(ticketId);
        return userTicketDetailsDTO;
    }
}
