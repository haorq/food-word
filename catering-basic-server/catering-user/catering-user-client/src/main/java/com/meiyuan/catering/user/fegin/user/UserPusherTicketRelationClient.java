package com.meiyuan.catering.user.fegin.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.user.PusherTicketDTO;
import com.meiyuan.catering.user.entity.CateringUserPusherTicketRelationEntity;
import com.meiyuan.catering.user.service.CateringUserPusherTicketRelationService;
import com.meiyuan.catering.user.vo.user.UserPusherTicketRelationListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @date 2020/5/19 13:50
 * @description
 **/
@Service
public class UserPusherTicketRelationClient {

    @Autowired
    private CateringUserPusherTicketRelationService userPusherTicketRelationService;

    /**
     * 功能描述: 通过地推员id获取优惠券IDS<br>
     * @Param: [groundPusherId]
     * @Return: com.meiyuan.catering.core.util.Result<java.util.List<java.lang.Long>>
     * @Author: gz
     * @Date: 2020/5/20 9:35
     * @Version 1.0.1
     */


    public Result<List<Long>> listPusherTicketId(Long groundPusherId) {
        return Result.succ(userPusherTicketRelationService.listPusherTicketId(groundPusherId));
    }

    /**
     * 功能描述: 地推员扫码优惠券列表<br>
     * @Param: [groundPusherId]
     * @Return: com.meiyuan.catering.core.util.Result<java.util.List<java.lang.Long>>
     * @Author: gz
     * @Date: 2020/5/20 9:35
     * @Version 1.0.1
     */
    public Result<List<PusherTicketDTO>> listPusherTicket(Long groundPusherId) {
        return Result.succ(userPusherTicketRelationService.listPusherTicket(groundPusherId));
    }


    /**
     * @Author lhm
     * @Description 批量新增和删除地推员与优惠券关系
     * @Date 2020/5/19
     * @Param [ticketIds, groundPusherId]
     * @return {@link }
     * @Version v1.1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatchRelation(List<Long> ticketIds, Long groundPusherId) {
        LambdaQueryWrapper<CateringUserPusherTicketRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringUserPusherTicketRelationEntity::getGroundPusherId, groundPusherId);
        List<CateringUserPusherTicketRelationEntity> list = userPusherTicketRelationService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> ids = list.stream().map(CateringUserPusherTicketRelationEntity::getId).collect(Collectors.toList());
            userPusherTicketRelationService.removeByIds(ids);
        }
        List<CateringUserPusherTicketRelationEntity> entities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ticketIds)) {
            ticketIds.forEach(e -> {
                CateringUserPusherTicketRelationEntity entity = new CateringUserPusherTicketRelationEntity();
                entity.setTicketId(e);
                entity.setGroundPusherId(groundPusherId);
                entity.setIsDel(false);
                entity.setCreateTime(LocalDateTime.now());
                entities.add(entity);
            });
            userPusherTicketRelationService.saveOrUpdateBatch(entities);
        }

    }

    public Result<List<CateringUserPusherTicketRelationEntity>> list(LambdaQueryWrapper<CateringUserPusherTicketRelationEntity> queryWrapper) {
        return Result.succ(userPusherTicketRelationService.list(queryWrapper));
    }

    public Result<Boolean> remove(LambdaQueryWrapper<CateringUserPusherTicketRelationEntity> queryWrapper) {
        return Result.succ(userPusherTicketRelationService.remove(queryWrapper));
    }


    /**
     * @return {@link List< CateringUserPusherTicketRelationEntity>}
     * @Author lhm
     * @Description 查询地推员详情---过滤已删除的和过期的优惠券
     * @Date 2020/5/19
     * @Param [pusherId]
     * @Version v1.1.0
     */
    public List<UserPusherTicketRelationListVo> selectByPusherId(Long pusherId) {
        LambdaQueryWrapper<CateringUserPusherTicketRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringUserPusherTicketRelationEntity::getGroundPusherId, pusherId);
        List<CateringUserPusherTicketRelationEntity> list = userPusherTicketRelationService.list(queryWrapper);
        List<UserPusherTicketRelationListVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            vos = list.stream().map(entity -> {
                UserPusherTicketRelationListVo vo = new UserPusherTicketRelationListVo();
                vo.setGroundPusherId(entity.getGroundPusherId());
                vo.setTicketId(entity.getTicketId());
                return vo;
            }).collect(Collectors.toList());
        }
        return vos;
    }
}
