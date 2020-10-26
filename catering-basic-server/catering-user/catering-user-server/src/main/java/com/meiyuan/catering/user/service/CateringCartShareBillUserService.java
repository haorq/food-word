package com.meiyuan.catering.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.user.entity.CateringCartShareBillUserEntity;

import java.util.List;

/**
 * @author yaozou
 * @description 拼单服务
 * ${@link CateringCartShareBillUserEntity}
 * @date 2020/3/25 14:18
 * @since v1.0.0
 */
public interface CateringCartShareBillUserService extends IService<CateringCartShareBillUserEntity> {
    /**
     * 描述: 拼单用户列表
     *
     * @param shareBillNo 拼单号
     * @return java.util.List<com.meiyuan.catering.user.entity.CateringCartShareBillUserEntity>
     * @author yaozou
     * @date 2020/3/25 22:14
     * @since v1.0.0
     */
    List<CateringCartShareBillUserEntity> list(String shareBillNo);

    /**
     * 描述:
     *
     * @param shareBillNo 拼单号
     * @param userId      用户id
     * @return com.meiyuan.catering.user.entity.CateringCartShareBillUserEntity
     * @author yaozou
     * @date 2020/3/27 14:44
     * @since v1.0.0
     */
    CateringCartShareBillUserEntity getUser(String shareBillNo, long userId);

    /**
     * 描述:通过拼单号删除拼单人信息
     *
     * @param shareBillNo 拼单号
     * @param userId      用户id
     * @return void
     * @author yaozou
     * @date 2020/3/27 15:17
     * @since v1.0.0
     */
    void deleteByShareBillNo(String shareBillNo, Long userId);

}
