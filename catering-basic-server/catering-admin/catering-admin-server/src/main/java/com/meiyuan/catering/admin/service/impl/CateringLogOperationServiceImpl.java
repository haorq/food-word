package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.dao.CateringLogOperationMapper;
import com.meiyuan.catering.admin.dto.log.LogQueryDTO;
import com.meiyuan.catering.admin.entity.CateringLogOperationEntity;
import com.meiyuan.catering.admin.service.CateringLogOperationService;
import com.meiyuan.catering.admin.vo.log.LogOperationQueryVo;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CharUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志
 * @author admin
 */
@Service
public class CateringLogOperationServiceImpl implements CateringLogOperationService {

    @Autowired
    private CateringLogOperationMapper logOperationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CateringLogOperationEntity entity) {
        logOperationMapper.insert(entity);
    }

    @Override
    public PageData<LogOperationQueryVo> pageList(LogQueryDTO dto) {
        LambdaQueryWrapper<CateringLogOperationEntity> wrapper = new LambdaQueryWrapper<>();
        LocalDateTime beginTime = dto.getBeginTime();
        LocalDateTime endTime = dto.getEndTime();
        //处理关键字
        String keyword = CharUtil.disposeChar(dto.getKeyword());
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like(CateringLogOperationEntity::getCreatorName, keyword);
        }
        if (beginTime != null) {
            wrapper.ge(CateringLogOperationEntity::getCreateTime, beginTime);
        }
        if (endTime != null) {
            wrapper.lt(CateringLogOperationEntity::getCreateTime, endTime);
        }
        wrapper.orderByDesc(CateringLogOperationEntity::getCreateTime);

        IPage<CateringLogOperationEntity> page = logOperationMapper.selectPage(dto.getPage(), wrapper);
        List<LogOperationQueryVo> vos = BaseUtil.objToObj(page.getRecords(),LogOperationQueryVo.class);

        return new PageData<>(vos, page.getTotal());
    }

}
