package com.meiyuan.catering.admin.service.admin;

import com.meiyuan.catering.admin.dto.version.VersionManageDTO;
import com.meiyuan.catering.admin.fegin.VersionManageClient;
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
public class AdminVersionManageService {
    @Autowired
    VersionManageClient versionManageClient;

    /**
     * 方法描述 : 添加修改移动端版本对应更新信息
     * @Author: MeiTao
     * @Since version-1.4.0
     */
    public Result<AppVersionInfoVo> saveOrUpdateVersionInfo(VersionManageDTO dto){
        return versionManageClient.saveOrUpdateVersionInfo(dto);
    }

    /**
     * 方法描述 : 查询移动端版本对应更新信息
     * @Author: MeiTao
     * @Date: 2020/9/16 0016 14:39
     * @param dto 请求参数
     * @return: Result<AppVersionInfoVo>
     * @Since version-1.4.0
     */
    public Result<List<AppVersionInfoVo>> getVersionInfo(VersionManageDTO dto) {
        return versionManageClient.getVersionInfo(dto);
    }
}
