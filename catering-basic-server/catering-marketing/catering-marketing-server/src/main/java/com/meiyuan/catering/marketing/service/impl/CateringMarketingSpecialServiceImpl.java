package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingSpecialMapper;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialAddOrEditDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialGoodsDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialBeginOrEndMsgDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialSkuEntity;
import com.meiyuan.catering.marketing.enums.MarketingSpecialFixTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingSpecialStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.mq.sender.MarketingEsMqSender;
import com.meiyuan.catering.marketing.mq.sender.MarketingSpecialBeginOrEndMqSender;
import com.meiyuan.catering.marketing.service.CateringMarketingSpecialService;
import com.meiyuan.catering.marketing.service.CateringMarketingSpecialSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品活动服务接口实现
 **/

@Slf4j
@Service("cateringMarketingSpecialService")
public class CateringMarketingSpecialServiceImpl extends ServiceImpl<CateringMarketingSpecialMapper, CateringMarketingSpecialEntity> implements CateringMarketingSpecialService {

    /**
     * 一天的小时数
     */
    private static final Integer DAY_HOURS = 24;

    @Autowired
    private CateringMarketingSpecialSkuService specialSkuService;
    @Autowired
    private MarketingSpecialBeginOrEndMqSender specialBeginOrEndMqSender;
    @Autowired
    private MarketingEsMqSender marketingEsMqSender;

    @Override
    public void verifyInfo(MarketingSpecialAddOrEditDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        if(null != dto.getId()) {
            CateringMarketingSpecialEntity specialEntity = getById(dto.getId());
            if (null == specialEntity) {
                throw new CustomException("需要编辑的数据不存在");
            }
            if (specialEntity.getUpDown().equals(MarketingUpDownStatusEnum.DOWN.getStatus())
                    || specialEntity.getDel().equals(DelEnum.DELETE.getFlag())) {
                throw new CustomException("该特价商品活动已被冻结，或者被删除，不能被编辑");
            }
            if (specialEntity.getBeginTime().isBefore(now) && specialEntity.getEndTime().isAfter(now)) {
                throw new CustomException("进行中的特价商品活动不能被编辑");
            }
        }
        LocalDateTime beginTime = dto.getBeginTime();
        LocalDateTime endTime = dto.getEndTime();
        if(now.isAfter(endTime)) {
            // 选择了之前的的过时时间，不做判断，直接通过
            return;
        }
        if(now.isAfter(beginTime)) {
            beginTime = now;
        }
        // 校验活动名称是否重复
        List<String> list = baseMapper.verifySpecialInfo(dto.getShopId(), dto.getId());
        if(BaseUtil.judgeList(list) && list.contains(dto.getName())) {
            throw new CustomException("活动名称已重复，请修改");
        }
        // 判断所选时间段是否有有效的活动存在
        List<String> specialExistedNameList = baseMapper.verifySpecialExisted(dto.getShopId(), dto.getId(), beginTime, endTime);
        if(BaseUtil.judgeList(specialExistedNameList)) {
            throw new CustomException("所选时间段已存在特价商品活动【" + specialExistedNameList.get(0) + "】");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MarketingSpecialBeginOrEndMsgDTO createOrEdit(MarketingSpecialAddOrEditDTO dto) {
        boolean isEdit = null != dto.getId();
        CateringMarketingSpecialEntity oldSpecialEntity = null;
        if(isEdit) {
            oldSpecialEntity = getById(dto.getId());
            if(null == oldSpecialEntity) {
                throw new CustomException("需要编辑的数据不存在");
            }
        }
        CateringMarketingSpecialEntity specialEntity = BaseUtil.objToObj(dto, CateringMarketingSpecialEntity.class);
        // 补全信息
        LocalDateTime now = LocalDateTime.now();
        if(isEdit) {
            specialEntity.setUpdateBy(dto.getAccountId());
            specialEntity.setUpdateTime(now);
        } else {
            specialEntity.setCreateBy(dto.getAccountId());
            specialEntity.setCreateTime(now);
        }
        // 创建/编辑信息
        boolean isSuccess = saveOrUpdate(specialEntity);
        // 保存商品SKU信息
        if(isSuccess) {
            if(isEdit) {
                // 编辑操作，删除之前的商品SKU信息
                specialSkuService.delByOfId(dto.getId());
            }
            // 开始保存商品SKU信息
            List<CateringMarketingSpecialSkuEntity> specialSkuEntityList = dto.getGoodsList().stream().map(item -> {
                CateringMarketingSpecialSkuEntity specialSkuEntity = BaseUtil.objToObj(item, CateringMarketingSpecialSkuEntity.class);
                specialSkuEntity.setId(IdWorker.getId());
                specialSkuEntity.setOfId(specialEntity.getId());
                specialSkuEntity.setShopId(dto.getShopId());
                specialSkuEntity.setCreateTime(now);
                specialSkuEntity.setCreateBy(dto.getAccountId());
                return specialSkuEntity;
            }).collect(Collectors.toList());
            // 批量增加营销特价商品活动商品SKU信息
            specialSkuService.saveBath(specialSkuEntityList);
            if(dto.getEndTime().isBefore(now)) {
                // 如果结束时间按小于当前时间，不发送mq消息
                // 直接设置活动结束
                baseMapper.updateStatusById(specialEntity.getId(), MarketingSpecialStatusEnum.ENDED.getStatus(), now);
                return null;
            }
            if(isEdit) {
                // 编辑状态，判断开始时间是否有改变，没有改变，则不发送mq消息
                LocalDateTime oldBeginTime = oldSpecialEntity.getBeginTime();
                if(oldBeginTime.isEqual(dto.getBeginTime())) {
                    return null;
                }
            }
            // 延迟定时结束消息
            long hours = Math.abs(ChronoUnit.HOURS.between(now, dto.getBeginTime()));
            //与结束时间相差小于24小时，则直接发送mq定时结束延迟消息
            if (hours <= DAY_HOURS) {
                MarketingSpecialBeginOrEndMsgDTO msgDTO = new MarketingSpecialBeginOrEndMsgDTO();
                msgDTO.setSpecialId(specialEntity.getId());
                msgDTO.setStatus(MarketingSpecialStatusEnum.ONGOING.getStatus());
                msgDTO.setBeginTime(dto.getBeginTime());
                return msgDTO;
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean freeze(Long specialId) {
        CateringMarketingSpecialEntity specialEntity = findById(specialId);
        if(null == specialEntity) {
            throw new CustomException("数据不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        if(specialEntity.getBeginTime().isAfter(now) || specialEntity.getEndTime().isBefore(now)) {
            throw new CustomException("活动未进行，无法执行冻结操作");
        }
        if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(specialEntity.getUpDown())) {
            throw new CustomException("活动已被冻结，无法执行冻结操作");
        }
        specialEntity.setUpDown(MarketingUpDownStatusEnum.DOWN.getStatus());
        specialEntity.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(specialEntity);
        if(success) {
            marketingEsMqSender.sendMarketingSpecialStatusUpdate(specialEntity.getId(), MarketingSpecialStatusEnum.FREEZE.getStatus());
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean del(Long specialId) {
        CateringMarketingSpecialEntity specialEntity = getById(specialId);
        if(null == specialEntity) {
            throw new CustomException("数据不存在");
        }
        if(!MarketingSpecialStatusEnum.NOT_START.getStatus().equals(specialEntity.getStatus())) {
            throw new CustomException("此活动无法进行删除操作");
        }
        if(Boolean.TRUE.equals(specialEntity.getDel())) {
            throw new CustomException("此活动已被删除");
        }
        specialEntity.setDel(DelEnum.DELETE.getFlag());
        specialEntity.setUpdateTime(LocalDateTime.now());
        updateById(specialEntity);
        return true;
    }

    @Override
    public CateringMarketingSpecialEntity findById(Long specialId) {
        LambdaQueryWrapper<CateringMarketingSpecialEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingSpecialEntity :: getId, specialId)
                    .eq(CateringMarketingSpecialEntity :: getDel, DelEnum.NOT_DELETE.getFlag());
        return getOne(queryWrapper);
    }

    @Override
    public void updateStatus(Long specialId, Integer status) {
        baseMapper.updateStatusById(specialId, status, LocalDateTime.now());
    }

    @Override
    public void sendMsg(LocalDateTime targetTime, MarketingSpecialBeginOrEndMsgDTO msgDTO) {
        specialBeginOrEndMqSender.sendSpecialTimedTaskMsg(targetTime, msgDTO);
    }

    @Override
    public void beginOrEndTimedTask() {
        // 查询需要延迟发送开始消息的活动
        log.info("=====查询需要延迟发送开始消息的活动=====");
        List<CateringMarketingSpecialEntity> needBeginList = baseMapper.needBeginList();
        if(BaseUtil.judgeList(needBeginList)) {
            needBeginList.forEach(item -> {
                MarketingSpecialBeginOrEndMsgDTO msgDTO = new MarketingSpecialBeginOrEndMsgDTO();
                msgDTO.setSpecialId(item.getId());
                msgDTO.setStatus(MarketingSpecialStatusEnum.ONGOING.getStatus());
                msgDTO.setBeginTime(item.getBeginTime());
                specialBeginOrEndMqSender.sendSpecialTimedTaskMsg(item.getBeginTime(), msgDTO);
            });
        }
        // 查询需要延迟发送结束消息的活动
        log.info("=====查询需要延迟发送结束消息的活动=====");
        List<CateringMarketingSpecialEntity> needEndList = baseMapper.needEndList();
        if(BaseUtil.judgeList(needEndList)) {
            needEndList.forEach(item -> {
                MarketingSpecialBeginOrEndMsgDTO endMsgDTO = new MarketingSpecialBeginOrEndMsgDTO();
                endMsgDTO.setSpecialId(item.getId());
                endMsgDTO.setStatus(MarketingSpecialStatusEnum.ENDED.getStatus());
                specialBeginOrEndMqSender.sendSpecialTimedTaskMsg(item.getEndTime(), endMsgDTO);
            });
        }
    }

    @Override
    public void updateUpDown(Long specialId, Integer upDownStatus) {
        baseMapper.updateUpDown(specialId, upDownStatus, LocalDateTime.now());
    }
}
