package com.meiyuan.catering.admin.fegin;

import com.meiyuan.catering.admin.dto.version.VersionManageDTO;
import com.meiyuan.catering.admin.service.CateringVersionManageService;
import com.meiyuan.catering.admin.vo.version.AppVersionInfoVo;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/9/10 0010 13:57
 * @Description 简单描述 : 移动端版本管理
 * @Since version-1.4.0
 */
@Service
public class VersionManageClient{
    @Autowired
    CateringVersionManageService versionManageService;

    /**
     * 方法描述 : 添加修改移动端版本对应更新信息
     * @Author: MeiTao
     * @Since version-1.4.0
     */
    public Result<AppVersionInfoVo> saveOrUpdateVersionInfo(VersionManageDTO dto){
        return versionManageService.saveOrUpdateVersionInfo(dto);
    }

    /**
     * 方法描述 : 获取app版本对应更新信息
     * @Author: MeiTao
     * @Since version-1.4.0
     */
    public Result<AppVersionInfoVo> getVersionManageInfo(String appId){
        return versionManageService.versionManageService(appId);
    }

    /**
     * 方法描述 : 获取app版本对应更新信息
     * @Author: MeiTao
     * @Since version-1.4.0
     */
    public Result<List<AppVersionInfoVo>> getVersionInfo(VersionManageDTO dto) {
        return versionManageService.getVersionInfo(dto);
    }
}
