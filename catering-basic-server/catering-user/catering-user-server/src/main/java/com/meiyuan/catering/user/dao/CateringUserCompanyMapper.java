package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.user.entity.CateringUserCompanyEntity;
import com.meiyuan.catering.user.query.user.RecycleQueryDTO;
import com.meiyuan.catering.user.query.user.UserCompanyQueryDTO;
import com.meiyuan.catering.user.query.user.UserQueryDTO;
import com.meiyuan.catering.user.vo.user.CompanyDetailVo;
import com.meiyuan.catering.user.vo.user.RecycleListVo;
import com.meiyuan.catering.user.vo.user.UserCompanyListVo;
import com.meiyuan.catering.user.vo.user.UserListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户企业信息表(CateringUserCompany)表数据库访问层
 *
 * @author mei-tao
 * @since 2020-03-10 15:29:17
 */
@Mapper
public interface CateringUserCompanyMapper extends BaseMapper<CateringUserCompanyEntity> {

    /**
     * 企业用户列表查询
     * @param page
     * @param dto
     * @return
     */
    IPage<UserCompanyListVo> companyList(Page page, @Param("dto") UserCompanyQueryDTO dto);

    /**
     * 企业用户详情
     * @param id
     * @return
     */
    CompanyDetailVo companyDetailById(@Param("id") Long id);

    /**
     * web--个人用户列表
     * @param page
     * @param dto
     * @return
     */
    IPage<UserListVo> userList(Page page, @Param("dto") UserQueryDTO dto);


    /**
     * web--回收站列表
     * @param page
     * @param dto
     * @return
     */
    IPage<RecycleListVo> recycleList(Page page, @Param("dto") RecycleQueryDTO dto);

    /**
     * 描述:批量获取信息
     *
     * @param uidOrCids	 用户或公司id集合
     * @return java.util.List<com.meiyuan.catering.core.dto.base.UserCompanyInfo>
     * @author zengzhangni
     * @date 2020/6/23 10:51
     * @since v1.1.1
     */
    List<UserCompanyInfo> getListUcInfo(@Param("uidOrCids")List<Long> uidOrCids);
}
