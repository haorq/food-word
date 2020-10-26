package com.meiyuan.catering.finance.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.finance.dto.AccountListDTO;
import com.meiyuan.catering.finance.dto.AddRechargeActivityDTO;
import com.meiyuan.catering.finance.entity.CateringRechargeActivityEntity;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;
import com.meiyuan.catering.finance.enums.RechargeActivityStatusEnum;
import com.meiyuan.catering.finance.service.CateringRechargeActivityService;
import com.meiyuan.catering.finance.service.CateringRechargeRuleService;
import com.meiyuan.catering.user.enums.UserTypeEnum;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020/5/19 15:23
 * @since v1.1.0
 */
public class ActivityUtil {


    public static void accountVerify(List<AccountListDTO> list) {
        long count = list.stream().map(AccountListDTO::getRechargeAccount).distinct().count();
        if (count < list.size()) {
            throw new CustomException("同一活动内，充值金额不能重复");
        }
    }

    public static void saveRule(CateringRechargeRuleService ruleService, Long id, AddRechargeActivityDTO dto) {
        List<CateringRechargeRuleEntity> collect = dto.getList().stream().map(listDTO -> {
            CateringRechargeRuleEntity ruleEntity = new CateringRechargeRuleEntity();
            ruleEntity.setActivityId(id);
            ruleEntity.setRechargeAccount(listDTO.getRechargeAccount());
            ruleEntity.setGivenAccount(listDTO.getGivenAccount());
            return ruleEntity;
        }).collect(Collectors.toList());
        ruleService.saveBatch(collect);
    }

    public static void removeRule(CateringRechargeRuleService ruleService, Long id) {
        LambdaQueryWrapper<CateringRechargeRuleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringRechargeRuleEntity::getActivityId, id);
        ruleService.remove(wrapper);
    }


    /**
     * 设置状态
     *
     * @param entity
     */
    public static void setStatus(CateringRechargeActivityEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginTime = entity.getBeginTime();
        LocalDateTime endTime = entity.getEndTime();

        if (now.isAfter(endTime.plusDays(1))) {
            throw new CustomException("无效的活动结束时间");
        }

        if (entity.getUp()) {
            if (now.isAfter(beginTime)) {
                entity.setStatus(RechargeActivityStatusEnum.UNDERWAY.getStatus());
            } else {
                entity.setStatus(RechargeActivityStatusEnum.NOT_START.getStatus());
            }
        } else {
            entity.setStatus(RechargeActivityStatusEnum.FINISHED.getStatus());
        }

    }

    /**
     * 验证
     */
    public static void verify(CateringRechargeActivityService service, CateringRechargeActivityEntity entity) {
        String name = entity.getName();
        LocalDateTime beginTime = entity.getBeginTime();
        LocalDateTime endTime = entity.getEndTime();
        Integer userType = entity.getUserType();
        Long id = entity.getId();

        timeOverVerify(entity.getBeginTime(), entity.getEndTime());
        //名称验证
        nameVerify(service, name, userType, id);
        //时间验证
        timeVerify(service, name, userType, id, beginTime, endTime);
    }

    private static void timeOverVerify(LocalDateTime beginTime, LocalDateTime endTime) {
        if (beginTime.isAfter(endTime)) {
            throw new CustomException("结束时间不能早于开始时间");
        }
    }

    private static void nameVerify(CateringRechargeActivityService service, String name, Integer userType, Long id) {
        LambdaQueryWrapper<CateringRechargeActivityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringRechargeActivityEntity::getName, name)
                .eq(CateringRechargeActivityEntity::getUserType, userType)
                .eq(CateringRechargeActivityEntity::getUp, true);
        if (id != null) {
            wrapper.ne(CateringRechargeActivityEntity::getId, id);
        }
        List<CateringRechargeActivityEntity> list = service.list(wrapper);
        if (list.size() > 0) {
            throw new CustomException(UserTypeEnum.parse(userType).getDesc() + "-活动名称已存在");
        }
    }

    private static void timeVerify(CateringRechargeActivityService service, String name, Integer userType, Long id, LocalDateTime beginTime, LocalDateTime endTime) {
        LambdaQueryWrapper<CateringRechargeActivityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(CateringRechargeActivityEntity::getName, name)
                .eq(CateringRechargeActivityEntity::getUserType, userType)
                .eq(CateringRechargeActivityEntity::getUp, true);
        if (id != null) {
            wrapper.ne(CateringRechargeActivityEntity::getId, id);
        }
        List<CateringRechargeActivityEntity> list = service.list(wrapper);
        if (list.size() > 0) {
            list.forEach(entity -> {
                if (isIntersection(beginTime, endTime, entity.getBeginTime(), entity.getEndTime())) {
                    throw new CustomException("活动时间内,已有其他活动,请重新选择时间");
                }
            });
        }
    }

    /**
     * 判断两个时间段是否存在交集
     *
     * @param newBeginTime 开始时间1
     * @param newEndTime   结束时间1
     * @param beginTime    开始时间2
     * @param endTime      结束时间2
     * @return
     */
    private static boolean isIntersection(LocalDateTime newBeginTime, LocalDateTime newEndTime, LocalDateTime beginTime, LocalDateTime endTime) {
        long ss1 = newBeginTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long ee1 = newEndTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long ss2 = beginTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long ee2 = endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        if ((ss1 <= ss2) && (ee1 >= ss2)) {
            return true;
        } else {
            return (ss1 >= ss2) && (ss1 <= ee2);
        }
    }
}
