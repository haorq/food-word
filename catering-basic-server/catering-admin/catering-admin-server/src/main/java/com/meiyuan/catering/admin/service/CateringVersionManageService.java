package com.meiyuan.catering.admin.service;

import com.meiyuan.catering.admin.dto.version.VersionManageDTO;
import com.meiyuan.catering.admin.entity.CateringVersionManageEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.vo.version.AppVersionInfoVo;
import com.meiyuan.catering.core.util.Result;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/9/10 0010 13:57
 * @Description 简单描述 :
 * @Since version-1.4.0
 */
public interface CateringVersionManageService extends IService<CateringVersionManageEntity> {

    /**
     * 方法描述 : 获取app版本对应更新信息
     * @Author: MeiTao
     * @Date: 2020/9/10 0010 14:11
     * @param appId 请求参数
     * @return: Result<AppVersionInfoVo>
     * @Since version-1.4.0
     */
    Result<AppVersionInfoVo> versionManageService(String appId);

    /**
     * 方法描述 : 添加修改移动端版本对应更新信息
     * @Author: MeiTao
     * @Date: 2020/9/10 0010 14:32
     * @param dto 请求参数
     * @return: Result<AppVersionInfoVo>
     * @Since version-1.4.0
     */
    Result<AppVersionInfoVo> saveOrUpdateVersionInfo(VersionManageDTO dto);

    /**
     * 方法描述 : 查询移动端版本更新信息
     * @Author: MeiTao
     * @Date: 2020/9/16 0016 14:45
     * @param dto 请求参数
     * @return: Result<AppVersionInfoVo>
     * @Since version-1.4.0
     */
    Result<List<AppVersionInfoVo>> getVersionInfo(VersionManageDTO dto);
}
