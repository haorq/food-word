package com.meiyuan.catering.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.dto.admin.admin.AdminAddDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminPwdUpdateDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminQueryDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminUpdateDTO;
import com.meiyuan.catering.admin.dto.admin.auth.UserLoginDto;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.vo.admin.admin.AdminDetailsVo;
import com.meiyuan.catering.admin.vo.admin.admin.AdminListQueryVo;

/**
 * @author lhm
 * @date 2020/3/18 13:41
 **/
public interface CateringAdminService  extends IService<CateringAdmin> {



    /**
     * 添加操作员
     * @param dto
     * @return
     */
    Integer create(AdminAddDTO dto);

    /**
     * 管理员列表查询
     * @param dto
     * @return
     */
    IPage<AdminListQueryVo>  querySelective(AdminQueryDTO dto);


    /**
     * 管理员编辑
     * @param dto
     * @return
     */
    Integer updateAdmin(AdminUpdateDTO dto);


    /**
     * 管理员详情
     * @param id
     * @return
     */
    AdminDetailsVo details(Long id);

}
