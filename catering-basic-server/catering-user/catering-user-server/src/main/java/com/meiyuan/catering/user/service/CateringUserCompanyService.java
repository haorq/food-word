package com.meiyuan.catering.user.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.user.dto.user.UserCompanyAddDTO;
import com.meiyuan.catering.user.dto.user.UserCompanyExitDTO;
import com.meiyuan.catering.user.entity.CateringUserCompanyEntity;
import com.meiyuan.catering.user.query.user.RecycleQueryDTO;
import com.meiyuan.catering.user.query.user.UserCompanyQueryDTO;
import com.meiyuan.catering.user.query.user.UserQueryDTO;
import com.meiyuan.catering.user.vo.user.CompanyDetailVo;
import com.meiyuan.catering.user.vo.user.RecycleListVo;
import com.meiyuan.catering.user.vo.user.UserCompanyListVo;
import com.meiyuan.catering.user.vo.user.UserListVo;

import java.util.List;

/**
 * 用户企业信息表(CateringUserCompany)服务层
 *
 * @author mei-tao
 * @since 2020-03-10 15:30:41
 */
public interface CateringUserCompanyService extends IService<CateringUserCompanyEntity> {

    /**
     * 企业用户列表条件查询
     *
     * @param dto
     * @return
     */
    IPage<UserCompanyListVo> companyList(UserCompanyQueryDTO dto);

    /**
     * 企业用户详情
     *
     * @param id
     * @return
     */
    CompanyDetailVo companyDetailById(Long id);


    /**
     * web--企业用户添加
     *
     * @param dto
     * @return
     */
    CateringUserCompanyEntity companyAdd(UserCompanyAddDTO dto);

    /**
     * 编辑企业用户
     *
     * @param dto
     * @return
     */
    Object updateUserCompany(UserCompanyExitDTO dto);


    /**
     * web--用户列表查询
     *
     * @param dto
     * @return
     */
    IPage<UserListVo> userList(UserQueryDTO dto);


    /**
     * web--回收站列表
     *
     * @param dto
     * @return
     */
    IPage<RecycleListVo> recycleList(RecycleQueryDTO dto);

    /**
     * 描述:通过关键字返回id集合
     *
     * @param keyword 关键字
     * @return java.util.List<java.lang.Long>
     * @author zengzhangni
     * @date 2020/6/23 10:51
     * @since v1.1.1
     */
    List<Long> getIdsByKeyword(String keyword);

    /**
     * 描述:批量获取信息
     *
     * @param uidOrCids 用户或公司id集合
     * @return java.util.List<com.meiyuan.catering.core.dto.base.UserCompanyInfo>
     * @author zengzhangni
     * @date 2020/6/23 10:52
     * @since v1.1.1
     */
    List<UserCompanyInfo> getListUcInfo(List<Long> uidOrCids);

}
