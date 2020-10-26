package com.meiyuan.catering.user.fegin.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.user.UserCompanyAddDTO;
import com.meiyuan.catering.user.dto.user.UserCompanyDTO;
import com.meiyuan.catering.user.dto.user.UserCompanyExitDTO;
import com.meiyuan.catering.user.entity.CateringUserCompanyEntity;
import com.meiyuan.catering.user.query.user.RecycleQueryDTO;
import com.meiyuan.catering.user.query.user.UserCompanyQueryDTO;
import com.meiyuan.catering.user.query.user.UserQueryDTO;
import com.meiyuan.catering.user.service.CateringUserCompanyService;
import com.meiyuan.catering.user.vo.user.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhm
 * @date 2020/5/19 13:47
 * @description
 **/
@Service
public class UserCompanyClient {
    @Resource
    private CateringUserCompanyService userCompanyService;


    /**
     * @return {@link Result< IPage< UserCompanyListVo>>}
     * @Author lhm
     * @Description 企业用户列表分页
     * @Date 14:44 2020/5/19
     * @Param [dto]
     * @Version v1.1.0
     */
    public Result<IPage<UserCompanyListVo>> companyList(UserCompanyQueryDTO dto) {
        return Result.succ(userCompanyService.companyList(dto));
    }


    /**
     * @return {@link Result< CompanyDetailVo>}
     * @Author lhm
     * @Description 企业用户详情
     * @Date 14:44 2020/5/19
     * @Param [id]
     * @Version v1.1.0
     */
    public Result<CompanyDetailVo> companyDetailById(Long id) {
        return Result.succ(userCompanyService.companyDetailById(id));

    }

    /**
     * @return {@link Result< CateringUserCompanyEntity>}
     * @Author lhm
     * @Description 添加企业用户
     * @Date 14:44 2020/5/19
     * @Param [dto]
     * @Version v1.1.0
     */
    public Result<Long> companyAdd(UserCompanyAddDTO dto) {
        return Result.succ(userCompanyService.companyAdd(dto).getId());

    }

    /**
     * @return {@link Result< Object>}
     * @Author lhm
     * @Description 编辑企业用户
     * @Date 14:44 2020/5/19
     * @Param [dto]
     * @Version v1.1.0
     */
    public Result updateUserCompany(UserCompanyExitDTO dto) {
        return Result.succ(userCompanyService.updateUserCompany(dto));
    }


    /**
     * @return {@link Result< IPage< UserListVo>>}
     * @Author lhm
     * @Description 用户列表分页查询
     * @Date 14:44 2020/5/19
     * @Param [dto]
     * @Version v1.1.0
     */
    public Result<IPage<UserListVo>> userList(UserQueryDTO dto) {
        return Result.succ(userCompanyService.userList(dto));
    }


    /**
     * @return {@link Result< IPage< RecycleListVo>>}
     * @Author lhm
     * @Description 回收站列表分页查询
     * @Date 14:45 2020/5/19
     * @Param [dto]
     * @Version v1.1.0
     */
    public Result<IPage<RecycleListVo>> recycleList(RecycleQueryDTO dto) {
        return Result.succ(userCompanyService.recycleList(dto));
    }

    /**
     * @return {@link Result< String>}
     * @Author lhm
     * @Description 验证该企业账号是否存在
     * @Date 2020/5/20
     * @Param [phone]
     * @Version v1.1.0
     */
    public Result<UserCompanyDTO> checkCompanyUser(String phone) {
        LambdaQueryWrapper<CateringUserCompanyEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringUserCompanyEntity::getAccount, phone);
        CateringUserCompanyEntity one = userCompanyService.getOne(wrapper);
        return Result.succ(BaseUtil.objToObj(one, UserCompanyDTO.class));
    }

    public List<Long> getIdsByKeyword(String keyword) {
        return userCompanyService.getIdsByKeyword(keyword);
    }

    public Result<String> getById(Long userCompanyId) {
        String companyName = userCompanyService.getById(userCompanyId).getCompanyName();
        return Result.succ(companyName);
    }

    /**
     * @return {@link UserCompanyDetailVo}
     * @Author lhm
     * @Description 获取企业详情
     * @Date 2020/5/20
     * @Param [account]
     * @Version v1.1.0
     */
    public UserCompanyDTO getUserCompany(LambdaQueryWrapper<CateringUserCompanyEntity> queryWrapper) {
        CateringUserCompanyEntity one = userCompanyService.getOne(queryWrapper);
        return BaseUtil.objToObj(one, UserCompanyDTO.class);
    }


    /**
     * @return {@link Result< UserCompanyDetailInfoVo>}
     * @Author lhm
     * @Description wx--企业用户信息详情展示
     * @Date 2020/5/20
     * @Param [userCompanyId]
     * @Version v1.1.0
     */
    public Result<UserCompanyDetailInfoVo> getUserCompanyDetail(Long userCompanyId) {
        LambdaQueryWrapper<CateringUserCompanyEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringUserCompanyEntity::getId, userCompanyId);
        queryWrapper.eq(CateringUserCompanyEntity::getIsDel, false);
        CateringUserCompanyEntity userCompany = userCompanyService.getOne(queryWrapper);
        UserCompanyDetailInfoVo vo = new UserCompanyDetailInfoVo();
        BeanUtils.copyProperties(userCompany, vo);
        vo.setAddressFull(userCompany.getAddressDetail());
        return Result.succ(vo);
    }


}
