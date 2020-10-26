package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringVersionManageMapper;
import com.meiyuan.catering.admin.dto.version.VersionManageDTO;
import com.meiyuan.catering.admin.entity.CateringVersionManageEntity;
import com.meiyuan.catering.admin.service.CateringVersionManageService;
import com.meiyuan.catering.admin.vo.version.AppVersionInfoVo;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Date 2020/9/10 0010 13:57
 * @Description 简单描述 : 移动端版本管理
 * @Since version-1.4.0
 */
@Service
public class CateringVersionManageServiceImpl extends ServiceImpl<CateringVersionManageMapper, CateringVersionManageEntity>
        implements CateringVersionManageService {
    @Resource
    CateringVersionManageMapper versionManageMapper;

    @Override
    public Result<AppVersionInfoVo> versionManageService(String appId) {
        QueryWrapper<CateringVersionManageEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringVersionManageEntity::getAppId,appId)
                      .orderByDesc(CateringVersionManageEntity::getCreateTime);
        List<CateringVersionManageEntity> entities = versionManageMapper.selectList(query);
        if (!BaseUtil.judgeList(entities)){
            return Result.succ();
        }
        CateringVersionManageEntity entity = entities.get(0);
        AppVersionInfoVo appVersionInfoVo = BaseUtil.objToObj(entity, AppVersionInfoVo.class);
        if (BaseUtil.judgeString(entity.getRemindWord())){
            appVersionInfoVo.setRemindWords(Arrays.asList(entity.getRemindWord().split("split")));
        }
        return Result.succ(appVersionInfoVo);
    }

    @Override
    public Result<AppVersionInfoVo> saveOrUpdateVersionInfo(VersionManageDTO dto) {
        CateringVersionManageEntity entity = BaseUtil.objToObj(dto, CateringVersionManageEntity.class);
        this.saveOrUpdate(entity);
        return Result.succ(BaseUtil.objToObj(entity, AppVersionInfoVo.class));
    }

    @Override
    public Result<List<AppVersionInfoVo>> getVersionInfo(VersionManageDTO dto) {
        QueryWrapper<CateringVersionManageEntity> query = new QueryWrapper<>();
        query.lambda().eq(!ObjectUtils.isEmpty(dto.getAppId()),CateringVersionManageEntity::getAppId,dto.getAppId())
                .eq(!ObjectUtils.isEmpty(dto.getVersionNum()),CateringVersionManageEntity::getVersionNum,dto.getVersionNum())
                .orderByDesc(CateringVersionManageEntity::getCreateTime);
        List<CateringVersionManageEntity> entities = versionManageMapper.selectList(query);
        List<AppVersionInfoVo> appVersionInfoVos = new ArrayList<>();
        if (BaseUtil.judgeList(entities)){
            entities.forEach(entity->{
                AppVersionInfoVo appVersionInfoVo = BaseUtil.objToObj(entity, AppVersionInfoVo.class);
                if (BaseUtil.judgeString(entity.getRemindWord())){
                    appVersionInfoVo.setRemindWords(Arrays.asList(entity.getRemindWord().split("split")));
                }
                appVersionInfoVos.add(appVersionInfoVo);
            });
        }
        return Result.succ(appVersionInfoVos);
    }
}
