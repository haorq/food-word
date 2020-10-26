package com.meiyuan.catering.marketing.service.impl;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.base.GrouponMemberQuitDTO;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingGroupOrderMapper;
import com.meiyuan.catering.marketing.dto.groupon.*;
import com.meiyuan.catering.marketing.entity.CateringGroupOrderMemberEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGroupOrderEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingRepertoryEntity;
import com.meiyuan.catering.marketing.enums.MarketingGroupOrderStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.mq.sender.GroupFailMqSender;
import com.meiyuan.catering.marketing.mq.sender.GroupSuccessMqSender;
import com.meiyuan.catering.marketing.redis.GrouponRedisUtil;
import com.meiyuan.catering.marketing.service.CateringGroupOrderMemberService;
import com.meiyuan.catering.marketing.service.CateringMarketingGroupOrderService;
import com.meiyuan.catering.marketing.service.CateringMarketingGrouponService;
import com.meiyuan.catering.marketing.service.CateringMarketingRepertoryService;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponEndVO;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGroupInfoVo;
import com.meiyuan.catering.marketing.vo.grouponorder.MarketingGrouponOrderSoldVo;
import com.meiyuan.catering.marketing.vo.groupordermember.MarketingGroupOrderMemberCountVO;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 团单数据表(CateringMarketingGroupOrder)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 11:34:12
 */
@Slf4j
@Service("cateringMarketingGroupOrderService")
public class CateringMarketingGroupOrderServiceImpl extends ServiceImpl<CateringMarketingGroupOrderMapper, CateringMarketingGroupOrderEntity>
        implements CateringMarketingGroupOrderService {

    @Autowired
    private CateringGroupOrderMemberService groupOrderMemberService;

    @Autowired
    private CateringMarketingRepertoryService repertoryService;

    @Autowired
    private CateringMarketingGrouponService grouponService;

    @Autowired
    private GroupFailMqSender groupFailMqSender;

    @Autowired
    private GroupSuccessMqSender groupSuccessMqSender;

    @Autowired
    private GrouponRedisUtil grouponRedisUtil;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.GROUPON_LOCK, area = JetcacheAreas.GOODS_AREA))
    private AdvancedCache lock;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initGroupOrder(GroupOrderDTO groupOrderDTO) {
        log.info("初始化团单数据，groupOrderDTO={}", groupOrderDTO);
        CateringMarketingGroupOrderEntity orderEntity = new CateringMarketingGroupOrderEntity();
        BeanUtils.copyProperties(groupOrderDTO, orderEntity);
        CateringMarketingGrouponEntity grouponEntity = grouponService.getById(groupOrderDTO.getOfId());
        if (grouponEntity != null) {
            orderEntity.setGroupStartTime(grouponEntity.getBeginTime());
            orderEntity.setGroupEndTime(grouponEntity.getEndTime());
            long groupValidTime = ChronoUnit.SECONDS.between(orderEntity.getGroupStartTime(), orderEntity.getGroupEndTime());
            orderEntity.setGroupValidTime(groupValidTime);
        }
        //活动刚发起时，已参团人数为1，就是活动发起者本人
        orderEntity.setNowMember(1);
        orderEntity.setGroupGoodsNumber(groupOrderDTO.getMinGrouponQuantity());
        orderEntity.setNowGoodsNumber(groupOrderDTO.getGoodsNumber());
        orderEntity.setStatus(MarketingGroupOrderStatusEnum.PROCESSING.getStatus());
        orderEntity.setCreateBy(groupOrderDTO.getUserId());
        save(orderEntity);
        //保存团单成员
        saveMember(groupOrderDTO, orderEntity.getId(), true);
        //初始化团购库存信息
        initGrouponRepertory(groupOrderDTO.getOfId(), groupOrderDTO.getMGoodsId(), groupOrderDTO.getGoodsNumber(), groupOrderDTO.getUserId());
    }

    /**
     * 初始化团购库存信息
     *
     * @param grouponId
     */
    private void initGrouponRepertory(Long grouponId, Long mGoodsId, Integer soldOut, Long userId) {
        CateringMarketingRepertoryEntity repertoryEntity = new CateringMarketingRepertoryEntity();
        repertoryEntity.setOfId(grouponId);
        repertoryEntity.setOfType(MarketingOfTypeEnum.GROUPON.getStatus());
        repertoryEntity.setMGoodsId(mGoodsId);
        //团购活动无库存限制，所以总库存和剩余库存都设为-1
        repertoryEntity.setTotalInventory(-1);
        repertoryEntity.setResidualInventory(-1);
        repertoryEntity.setSoldOut(soldOut);
        repertoryEntity.setCreateBy(userId);
        LocalDateTime now = LocalDateTime.now();
        repertoryEntity.setCreateTime(now);
        repertoryEntity.setUpdateTime(now);
        repertoryEntity.setDel(false);
        repertoryService.save(repertoryEntity);
        grouponRedisUtil.incrementSoldOut(mGoodsId, soldOut);
    }

    /**
     * groupOrderDTO转换为成员信息
     *
     * @param memberCommonDTO 团单DTO
     * @param groupOrderId    团单ID
     * @param isSponsor       是否是发起者
     * @return
     */
    private void saveMember(GroupMemberCommonDTO memberCommonDTO, Long groupOrderId, Boolean isSponsor) {
        GroupOrderMemberDTO memberDTO = new GroupOrderMemberDTO();
        memberDTO.setGroupOrderId(groupOrderId);
        memberDTO.setOrderNumber(memberCommonDTO.getOrderNumber());
        memberDTO.setMemberId(memberCommonDTO.getUserId());
        memberDTO.setMemberName(memberCommonDTO.getUserName());
        memberDTO.setMemberNickName(memberCommonDTO.getUserNickName());
        memberDTO.setIsSponsor(isSponsor);
        groupOrderMemberService.addMember(memberDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void newMemberJoinIn(GroupMemberJoinInDTO memberJoinInDTO) {
        log.info("用户加入团购活动，memberJoinInDTO={}", memberJoinInDTO);
        CateringMarketingGroupOrderEntity groupOrderEntity = handleMemberAndGoodsNumber(memberJoinInDTO);
        //保存团单成员
        saveMember(memberJoinInDTO, groupOrderEntity.getId(), false);
        //库存表增加已售数量
        repertoryService.syncGrouponSoldOut(groupOrderEntity.getMGoodsId(), memberJoinInDTO.getGoodsNumber());
        grouponRedisUtil.incrementSoldOut(groupOrderEntity.getMGoodsId(), memberJoinInDTO.getGoodsNumber());
    }

    /**
     * 同步处理团单加入的成员数量和商品数量
     *
     * @param memberJoinInDTO
     * @return
     */
    private CateringMarketingGroupOrderEntity handleMemberAndGoodsNumber(GroupMemberJoinInDTO memberJoinInDTO) {
        CateringMarketingGroupOrderEntity groupOrderEntity = getBymGoodsId(memberJoinInDTO.getMGoodsId());
        groupOrderEntity.setNowMember(groupOrderEntity.getNowMember() + 1);
        groupOrderEntity.setNowGoodsNumber(groupOrderEntity.getNowGoodsNumber() + memberJoinInDTO.getGoodsNumber());
        updateById(groupOrderEntity);
        return groupOrderEntity;
    }

    @Override
    public CateringMarketingGroupOrderEntity getBymGoodsId(Long mGoodsId) {
        LambdaQueryWrapper<CateringMarketingGroupOrderEntity> queryWrapper = new QueryWrapper<CateringMarketingGroupOrderEntity>().lambda()
                .eq(CateringMarketingGroupOrderEntity::getMGoodsId, mGoodsId);
        return getOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void memberQuit(GrouponMemberQuitDTO memberQuitDTO) {
        log.info("用户退出团购活动，memberQuitDTO={}", memberQuitDTO);
        Long mGoodsId = memberQuitDTO.getMGoodsId();
        AutoReleaseLock autoReleaseLock = lock.tryLock(mGoodsId.toString(), 5, TimeUnit.SECONDS);
        try {
            if(autoReleaseLock!=null){
                CateringMarketingGroupOrderEntity groupOrderEntity = getBymGoodsId(mGoodsId);
                groupOrderEntity.setNowGoodsNumber(groupOrderEntity.getNowGoodsNumber() - memberQuitDTO.getGoodsNumber());
                groupOrderEntity.setNowMember(groupOrderEntity.getNowMember() - 1);
                updateById(groupOrderEntity);
                groupOrderMemberService.removeByOrderNumber(memberQuitDTO.getOrderNumber());
                CateringMarketingRepertoryEntity repertoryEntity = repertoryService.getBymGoodsId(groupOrderEntity.getMGoodsId());
                repertoryEntity.setSoldOut(repertoryEntity.getSoldOut() - memberQuitDTO.getGoodsNumber());
                repertoryService.updateById(repertoryEntity);
                grouponRedisUtil.decrementSoldOut(mGoodsId, memberQuitDTO.getGoodsNumber());
            }
        } finally {
            if (autoReleaseLock != null) {
                autoReleaseLock.close();
            }
        }
    }

    @Override
    public MarketingGrouponEndVO endGroup(Long ofId,boolean isInitiative) {
        log.info("团购活动结束，ofId={}", ofId);
        LambdaQueryWrapper<CateringMarketingGroupOrderEntity> queryWrapper = new QueryWrapper<CateringMarketingGroupOrderEntity>().lambda()
                .eq(CateringMarketingGroupOrderEntity::getOfId, ofId);
        List<CateringMarketingGroupOrderEntity> groupOrderEntities = list(queryWrapper);
        CateringMarketingGrouponEntity grouponEntity = grouponService.getById(ofId);
        LocalDateTime now = LocalDateTime.now();
        // 返回的信息
        MarketingGrouponEndVO vo = new MarketingGrouponEndVO();
        if (grouponEntity != null && !groupOrderEntities.isEmpty()) {
            // 设置店铺ID
            vo.setShopId(grouponEntity.getMerchantId());
            List<String> orderNumbers = new ArrayList<>();
            groupOrderEntities.stream()
                    .filter(groupOrderEntity -> groupOrderEntity.getOfType() != null
                            && groupOrderEntity.getOfType().equals(MarketingOfTypeEnum.GROUPON.getStatus()))
                    .forEach(groupOrderEntity -> {
                        //如果团购是虚拟成团或商品售卖数量大于等于成团数量，则拼团成功
                        boolean goodsNumberSuccess = groupOrderEntity.getNowGoodsNumber() >= groupOrderEntity.getGroupGoodsNumber();
                        Boolean virtualGroupon = grouponEntity.getVirtualGroupon();
                        log.info("团购活动结束，判断拼团是否成功。ofId={},groupOrderId={},mgoodsId={},virtualGroupon={},nowGoodsNumber={},groupGoodsNumber={}", ofId,
                                groupOrderEntity.getId(), groupOrderEntity.getMGoodsId(), virtualGroupon, groupOrderEntity.getNowGoodsNumber(), groupOrderEntity.getGroupGoodsNumber());
                        boolean flag = virtualGroupon || goodsNumberSuccess;
                        if (flag && !isInitiative) {
                            groupOrderEntity.setStatus(MarketingGroupOrderStatusEnum.SUCCESS.getStatus());
                            groupOrderEntity.setUpdateTime(now);
                            updateById(groupOrderEntity);
                            log.info("团购成功，ofId={}", ofId);
                            List<String> successOrderNumbers = getOrderNumbers(groupOrderEntity.getId());
                            orderNumbers.addAll(successOrderNumbers);
                            if(BaseUtil.judgeList(successOrderNumbers)) {
                                //发送团购成功mq消息
                                groupSuccessMqSender.groupSuccessMsg(successOrderNumbers);
                            }
                        } else {
                            groupOrderEntity.setStatus(MarketingGroupOrderStatusEnum.FAILURE.getStatus());
                            groupOrderEntity.setUpdateTime(now);
                            updateById(groupOrderEntity);
                            log.info("团购失败，ofId={}", ofId);
                            List<String> failureOrderNumbers = getOrderNumbers(groupOrderEntity.getId());
                            if(BaseUtil.judgeList(failureOrderNumbers)) {
                                GrouponFailMsgDTO dto = new GrouponFailMsgDTO();
                                dto.setFailureOrderNumbers(failureOrderNumbers);
                                dto.setIsInitiative(isInitiative);
                                //发送团购失败mq消息
                                groupFailMqSender.groupFailMsg(dto);
                            }
                        }
                    });
            vo.setOrderNumbers(orderNumbers);
        }
        return vo;
    }

    /**
     * 根据团单ID获取订单编号集合
     *
     * @param grouponOrderId
     * @return
     */
    private List<String> getOrderNumbers(Long grouponOrderId) {
        LambdaQueryWrapper<CateringGroupOrderMemberEntity> queryWrapper = new QueryWrapper<CateringGroupOrderMemberEntity>().lambda()
                .eq(CateringGroupOrderMemberEntity::getGroupOrderId, grouponOrderId);
        List<CateringGroupOrderMemberEntity> memberEntities = groupOrderMemberService.list(queryWrapper);
        if(!BaseUtil.judgeList(memberEntities)) {
            return Collections.emptyList();
        }
        return memberEntities.stream()
                .map(CateringGroupOrderMemberEntity::getOrderNumber)
                .collect(Collectors.toList());
    }

    @Override
    public List<CateringMarketingGroupOrderEntity> listByOfId(Long ofId) {
        LambdaQueryWrapper<CateringMarketingGroupOrderEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingGroupOrderEntity :: getOfId, ofId);
        return list(queryWrapper);
    }

    @Override
    public List<MarketingGrouponOrderSoldVo> soldByGrouponMarketingGoodsIds(Set<Long> mGoodsIdSet) {
        LambdaQueryWrapper<CateringMarketingGroupOrderEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingGroupOrderEntity :: getOfId, mGoodsIdSet);
        List<CateringMarketingGroupOrderEntity> list = list(queryWrapper);
        return list.stream().map(item -> {
            MarketingGrouponOrderSoldVo vo = new MarketingGrouponOrderSoldVo();
            vo.setMGoodsId(item.getOfId());
            vo.setSold(item.getNowGoodsNumber());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public MarketingGrouponGroupInfoVo makeGroupInfo(CateringMarketingGrouponEntity grouponEntity,
                                                     List<CateringMarketingGroupOrderEntity> groupOrderList, Integer marketingGoodsNum) {
        MarketingGrouponGroupInfoVo vo = new MarketingGrouponGroupInfoVo();
        // 已经成团
        int finishGroup = 0;
        // 未成团
        int notGroup = 0;
        // 参团总人数
        int totalGroupMember = 0;
        if(BaseUtil.judgeList(groupOrderList)) {
            // 如果开始团购的商品个数小于团购商品本身的个数，说明有些商品没有进行团购，那么那些没有开始团购的商品就是未成团状态
            if(groupOrderList.size() < marketingGoodsNum) {
                notGroup = marketingGoodsNum - groupOrderList.size();
            }
            // 根据团购单ID查询详细的团购信息
            List<Long> groupOrderIdList = groupOrderList.stream().map(CateringMarketingGroupOrderEntity::getId).collect(Collectors.toList());
            List<MarketingGroupOrderMemberCountVO> groupOrderMemberCount = groupOrderMemberService.memberCount(groupOrderIdList);
            Map<Long, Integer> groupOrderMemberCountMap = groupOrderMemberCount.stream().collect(
                    Collectors.toMap(MarketingGroupOrderMemberCountVO::getGroupOrderId, MarketingGroupOrderMemberCountVO::getMemberCount));
            for (CateringMarketingGroupOrderEntity item : groupOrderList) {
                if(MarketingGroupOrderStatusEnum.SUCCESS.getStatus().equals(item.getStatus())) {
                    finishGroup ++;
                } else {
                    notGroup ++;
                }
                totalGroupMember = totalGroupMember + (groupOrderMemberCountMap.get(item.getId()) == null ? 0 : groupOrderMemberCountMap.get(item.getId()));
            }
        } else {
            notGroup = marketingGoodsNum;
        }
        vo.setFinishGroup(finishGroup);
        vo.setNotGroup(notGroup);
        vo.setTotalGroupMember(totalGroupMember);
        return vo;
    }
}
