package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.user.entity.CateringUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表(CateringUser)表数据库访问层
 *
 * @author mei-tao
 * @since 2020-03-10 15:29:17
 */
@Mapper
public interface CateringUserMapper extends BaseMapper<CateringUserEntity> {

    /**
     * zzn 通过用户名/手机号 查询id集合
     *
     * @param keyword 用户名/手机号
     * @return ids
     */
    List<Long> selectIdsByKeyword(@Param("keyword") String keyword);


    /**
     * 查询用户信息
     *
     * @param account
     * @return
     */
    CateringUserEntity queryByUserId(@Param("account") String account);


    /**
     * 描述:获取用户信息
     *
     * @param uidOrCid 用户或公司 id
     * @return com.meiyuan.catering.core.dto.base.UserCompanyInfo
     * @author zengzhangni
     * @date 2020/6/23 10:52
     * @since v1.1.1
     */
    UserCompanyInfo getUcInfo(Long uidOrCid);

    /**
     * 描述:批量获取信息
     *
     * @param uidOrCids 用户或公司 id集合
     * @return java.util.List<com.meiyuan.catering.core.dto.base.UserCompanyInfo>
     * @author zengzhangni
     * @date 2020/6/23 10:53
     * @since v1.1.1
     */
    List<UserCompanyInfo> getListUcInfo(@Param("uidOrCids") List<Long> uidOrCids);
    /**
     * 方法描述: 通过手机号获取用户ID<br>
     *
     * @author: gz
     * @date: 2020/10/9 11:05
     * @param phone
     * @return: {@link Long}
     * @version 1.5.0
     **/
    Long getUserIdByPhone(String phone);
}
