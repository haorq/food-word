package com.meiyuan.catering.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.user.Notice;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.user.dao.CateringNoticeMapper;
import com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeQueryV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeSaveV101DTO;
import com.meiyuan.catering.user.entity.CateringNoticeEntity;
import com.meiyuan.catering.user.service.CateringNoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020-05-06
 */
@Service
public class CateringNoticeServiceImpl extends ServiceImpl<CateringNoticeMapper, CateringNoticeEntity> implements CateringNoticeService {

    @Override
    public PageData<NoticeDetailV101DTO> pageListV101(NoticeQueryV101DTO dto) {
        //处理关键字
        String keyword = CharUtil.disposeChar(dto.getKeyword());
        LocalDateTime startTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();
        Integer status = dto.getStatus();

        LambdaQueryWrapper<CateringNoticeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(startTime != null, CateringNoticeEntity::getCreateTime, startTime)
                .lt(endTime != null, CateringNoticeEntity::getCreateTime, endTime != null ? endTime.plusDays(1) : null)
                .eq(status != null, CateringNoticeEntity::getStatus, status)
                .and(StringUtils.isNotBlank(keyword), e -> e.like(CateringNoticeEntity::getName, keyword)
                        .or().like(CateringNoticeEntity::getContent, keyword))
                .orderByDesc(CateringNoticeEntity::getCreateTime);
        IPage<CateringNoticeEntity> list = baseMapper.selectPage(dto.getPage(), wrapper);
        List<NoticeDetailV101DTO> dtos = BaseUtil.objToObj(list.getRecords(), NoticeDetailV101DTO.class);

        return new PageData(dtos, list.getTotal());
    }

    @Override
    public Boolean pageViewAddV101(Long id) {
        return baseMapper.pageViewAddV101(id);
    }

    @Override
    public Boolean saveV101(NoticeSaveV101DTO dto) {
        CateringNoticeEntity entity = new CateringNoticeEntity();
        BeanUtils.copyProperties(dto, entity);
        return save(entity);
    }

    @Override
    public NoticeDetailV101DTO detailByIdV101(Long id) {
        return BaseUtil.objToObj(baseMapper.selectById(id), NoticeDetailV101DTO.class);
    }

    @Override
    public Boolean publishByIdV101(Long id) {
        CateringNoticeEntity entity = new CateringNoticeEntity();
        entity.setId(id);
        entity.setStatus(0);
        return updateById(entity);
    }

    @Override
    public Boolean stickByIdV101(Long id, Boolean isStick) {
        CateringNoticeEntity entity = new CateringNoticeEntity();
        entity.setId(id);
        entity.setIsStick(isStick);
        if (!isStick) {
            entity.setUpdateTime(getById(id).getCreateTime());
        }
        return updateById(entity);
    }

    @Override
    public PageData<NoticeDetailV101DTO> pageListWxV101(BasePageDTO dto) {
        IPage<CateringNoticeEntity> list = page(dto.getPage(), getWrapper());
        List<NoticeDetailV101DTO> collect = list.getRecords().stream().map(e -> {
            NoticeDetailV101DTO detailDTO = new NoticeDetailV101DTO();
            BeanUtils.copyProperties(e, detailDTO);
            return detailDTO;
        }).collect(Collectors.toList());
        return new PageData(collect, list);
    }

    private LambdaQueryWrapper<CateringNoticeEntity> getWrapper() {
        LambdaQueryWrapper<CateringNoticeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringNoticeEntity::getStatus, 0)
                .orderByDesc(CateringNoticeEntity::getIsStick, CateringNoticeEntity::getUpdateTime, CateringNoticeEntity::getCreateTime);
        return wrapper;
    }

    @Override
    public NoticeDetailV101DTO detailByIdWxV101(Long id) {
        CateringNoticeEntity entity = getById(id);
        if (entity == null) {
            throw new CustomException("公告已下架");
        }
        return BaseUtil.objToObj(entity, NoticeDetailV101DTO.class);
    }

    @Override
    public PageData<Notice> listV101(Integer size) {
        IPage<CateringNoticeEntity> page = page(new Page(1, size), getWrapper());
        List<Notice> notices = BaseUtil.objToObj(page.getRecords(), Notice.class);
        return new PageData<>(notices, page);
    }
}

