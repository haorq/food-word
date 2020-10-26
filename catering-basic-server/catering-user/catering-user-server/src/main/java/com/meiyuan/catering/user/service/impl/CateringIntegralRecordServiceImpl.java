package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.user.dao.CateringIntegralRecordMapper;
import com.meiyuan.catering.core.dto.user.AddIntegralRecordDTO;
import com.meiyuan.catering.user.entity.CateringIntegralRecordEntity;
import com.meiyuan.catering.user.enums.IntegralRuleEnum;
import com.meiyuan.catering.user.query.integral.IntegralRecordQueryDTO;
import com.meiyuan.catering.user.service.CateringIntegralActivityService;
import com.meiyuan.catering.user.service.CateringIntegralRecordService;
import com.meiyuan.catering.user.service.CateringUserService;
import com.meiyuan.catering.user.vo.integral.IntegralDetailVo;
import com.meiyuan.catering.user.vo.integral.IntegralListVo;
import com.meiyuan.catering.user.vo.integral.IntegralRecordListVo;
import com.meiyuan.catering.user.vo.integral.MonthIntegralVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 积分记录表(CateringIntegralRecord)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-09 18:42:07
 */
@Service
@Slf4j
public class CateringIntegralRecordServiceImpl extends ServiceImpl<CateringIntegralRecordMapper, CateringIntegralRecordEntity> implements CateringIntegralRecordService {

    @Resource
    private CateringIntegralRecordMapper cateringIntegralRecordMapper;
    @Resource
    private CateringIntegralActivityService activityService;
    @Resource
    private CateringUserService userService;


    @Override
    public IPage<IntegralRecordListVo> pageList(IntegralRecordQueryDTO query) {
        //处理关键字
        query.setKeyword(CharUtil.disposeChar(query.getKeyword()));
        return cateringIntegralRecordMapper.pageList(query.getPage(), query);
    }

    @Override
    public Integer addIntegralRecord(Long userId, IntegralRuleEnum ruleEnum, Integer integral) {
        UserCompanyInfo info = userService.getUcInfoFill(userId);
        if (info != null) {
            CateringIntegralRecordEntity entity = getEntity(info, ruleEnum);
            entity.setIntegral(integral);
            baseMapper.insert(entity);
        } else {
            log.debug("id:{},未查询到信息", userId);
        }
        return integral;
    }

    @Override
    public Integer canGetIntegral(Long userId, Long activityId, Long activityRuleId) {
        Integer getNum = this.baseMapper.selectGetNum(userId, activityId, activityRuleId);
        if(getNum==null){
            getNum =0;
        }
        return getNum;
    }

    private CateringIntegralRecordEntity getEntity(UserCompanyInfo info, IntegralRuleEnum ruleEnum) {
        CateringIntegralRecordEntity entity = new CateringIntegralRecordEntity();
        entity.setUserId(info.getId());
        entity.setUserName(info.getName());
        entity.setUserNickName(info.getName());
        entity.setUserPhone(info.getPhone());
        entity.setUserType(info.getUserType());
        entity.setIntegralNo(ruleEnum.getCode());
        entity.setReason(ruleEnum.getDesc());
        entity.setType(ruleEnum.getType());
        return entity;
    }




    @Override
    public List<IntegralRecordListVo> listByUserId(Long uid) {
        LambdaQueryWrapper<CateringIntegralRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringIntegralRecordEntity::getUserId, uid);
        List<CateringIntegralRecordEntity> list = baseMapper.selectList(wrapper);
        return list.stream().map(entity -> {
            IntegralRecordListVo listVo = new IntegralRecordListVo();
            BeanUtils.copyProperties(entity, listVo);
            return listVo;
        }).collect(Collectors.toList());
    }

    /**
     * 个人有效积分
     *
     * @param uid
     * @return
     */
    @Override
    public Integer sumByUserId(Long uid) {
        Integer integral = baseMapper.sumByUserId(uid);
        return integral == null ? 0 : integral;
    }

    @Override
    public List<String> monthList(Long userId, BasePageDTO pageDTO) {
        return baseMapper.monthList(pageDTO.getPage(), userId);
    }


    @Override
    public List<IntegralListVo> queryListByMonth(Long userId, String month) {
        return baseMapper.queryListByMonth(userId, month);
    }

    @Override
    public IntegralDetailVo integralDetail(Long userId, BasePageDTO pageDTO) {

        IntegralDetailVo detailVo = new IntegralDetailVo();
        detailVo.setIntegral(sumByUserId(userId));

        //查询月份集合
        List<String> monthList = monthList(userId, pageDTO);

        if (monthList != null && monthList.size() > 0) {
            List<MonthIntegralVo> list = baseMapper.sumByMonths(monthList, userId);
            List<IntegralListVo> listVos = baseMapper.listByMonths(monthList, userId);
            Map<String, List<IntegralListVo>> listMap = listVos.stream().collect(Collectors.groupingBy(IntegralListVo::getMonth));
            list.forEach(vos -> vos.setList(listMap.get(vos.getMonth())));
            list.sort(Comparator.comparing(MonthIntegralVo::getMonth).reversed());
            detailVo.setMonthIntegrals(list);
        }
        return detailVo;
    }

    @Override
    public void addIntegralGetRecord(List<AddIntegralRecordDTO> addList) {
        addList.forEach(e->{
            Integer record = this.baseMapper.countGetRecord(e.getActivityId(), e.getUserId(),e.getActivityRuleId());
            if(record==null){
                record=0;
            }
            if(record>0){
                this.baseMapper.updateIntegralGetRecord(e.getActivityId(), e.getUserId(),e.getActivityRuleId());
            }else {
                 this.baseMapper.addIntegralGetRecord(e);
            }
        });
    }
}
