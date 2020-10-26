package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.marketing.dao.CateringMarketingSeckillEventMapper;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventPageQueryDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 平台秒杀活动场次服务层实现
 **/

@Slf4j
@Service("cateringMarketingSeckillEventService")
public class CateringMarketingSeckillEventServiceImpl extends ServiceImpl<CateringMarketingSeckillEventMapper,
        CateringMarketingSeckillEventEntity> implements CateringMarketingSeckillEventService {

    @Resource
    private CateringMarketingSeckillEventMapper seckillEventMapper;

    @Override
    public PageData<CateringMarketingSeckillEventEntity> pageQuery(MarketingSeckillEventPageQueryDTO dto) {
        // 查询条件
        LambdaQueryWrapper<CateringMarketingSeckillEventEntity> pageQuery = Wrappers.lambdaQuery();
        pageQuery.eq(CateringMarketingSeckillEventEntity :: getDel , DelEnum.NOT_DELETE.getFlag())
                .orderByAsc(CateringMarketingSeckillEventEntity :: getBeginTime);
        // 分页条件
        Page<CateringMarketingSeckillEventEntity> pageCondition = new Page<>(dto.getPageNo(), dto.getPageSize());
        // 分页查询
        IPage<CateringMarketingSeckillEventEntity> pageResult = page(pageCondition, pageQuery);
        return new PageData<>(pageResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addOrEdit(CateringMarketingSeckillEventEntity seckillEventEntity) {
        if(null == seckillEventEntity.getId()) {
            log.info("开始进行秒杀场次信息新增操作");
            return add(seckillEventEntity);
        } else {
            log.info("开始进行秒杀场次信息编辑操作，编辑ID为：{}", seckillEventEntity.getId());
            return edit(seckillEventEntity);
        }
    }


    private boolean add(CateringMarketingSeckillEventEntity entity) {
        // 校验场次结束时间晚于场次开始时间，不能等于
        if(entity.getBeginTime().equals(entity.getEndTime()) || entity.getBeginTime().isAfter(entity.getEndTime())) {
            throw new CustomException("结束时间应晚于场次开始时间");
        }
        // 查询当前场次开始时间是否已存在
        LambdaQueryWrapper<CateringMarketingSeckillEventEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingSeckillEventEntity :: getBeginTime, entity.getBeginTime())
                    .eq(CateringMarketingSeckillEventEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        int count = count(queryWrapper);
        if(count > 0) {
            throw new CustomException("时间场次已存在，请确认后再保存");
        }
        // 开始进行新增保存
        // 设置主键ID
        entity.setId(IdWorker.getId());
        return save(entity);
    }


    private boolean edit(CateringMarketingSeckillEventEntity entity) {
        // 校验场次结束时间晚于场次开始时间，不能等于
        if(entity.getBeginTime().equals(entity.getEndTime()) || entity.getBeginTime().isAfter(entity.getEndTime())) {
            throw new CustomException("结束时间应晚于场次开始时间");
        }
        // 查询当前场次开始时间是否已存在
        LambdaQueryWrapper<CateringMarketingSeckillEventEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingSeckillEventEntity :: getBeginTime, entity.getBeginTime())
                .eq(CateringMarketingSeckillEventEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .notIn(CateringMarketingSeckillEventEntity :: getId, entity.getId());
        int count = count(queryWrapper);
        if(count > 0) {
            throw new CustomException("时间场次已存在，请确认后再保存");
        }
        // 校验当前场次是否存在商户端在使用，有则不能进行编辑
        int useCount = seckillEventMapper.verificationUse(entity.getId(), LocalDateTime.now());
        if(useCount > 0) {
            throw new CustomException("该场次商户端存在秒杀，不能编辑");
        }
        // 开始进行编辑保存
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean del(Long eventId) {
        log.info("开始进行秒杀场次信息的删除操作");
        // 校验当前场次是否存在商户端在使用，有则不能进行编辑
        Boolean canDel = canDel(eventId, LocalDateTime.now());
        if(canDel) {
            throw new CustomException("该场次商户端存在秒杀，不能删除");
        }
        // 开始逻辑删除
        return removeById(eventId);
    }

    @Override
    public Boolean canDel(Long eventId, LocalDateTime dateTime) {
        int useCount = seckillEventMapper.verificationUse(eventId, LocalDateTime.now());
        return useCount > 0;
    }

    @Override
    public List<CateringMarketingSeckillEventEntity> listByIds(List<Long> ids) {
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public CateringMarketingSeckillEventEntity get(Long eventId) {
        return getById(eventId);
    }

    @Override
    public List<CateringMarketingSeckillEventEntity> entityList() {
        LambdaQueryWrapper<CateringMarketingSeckillEventEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByAsc(CateringMarketingSeckillEventEntity :: getBeginTime);
        return list(queryWrapper);
    }

    @Override
    public List<CateringMarketingSeckillEventEntity> selectListByIds(Set<Long> eventIds) {
        LambdaQueryWrapper<CateringMarketingSeckillEventEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingSeckillEventEntity :: getId, eventIds);
        return list(queryWrapper);
    }
}
