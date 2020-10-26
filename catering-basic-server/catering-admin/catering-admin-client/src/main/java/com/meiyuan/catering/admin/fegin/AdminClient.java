package com.meiyuan.catering.admin.fegin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.dto.admin.admin.AdminAddDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminQueryDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminUpdateDTO;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRolePageListParamsDTO;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.service.CateringAdminService;
import com.meiyuan.catering.admin.vo.admin.admin.AdminDetailsVo;
import com.meiyuan.catering.admin.vo.admin.admin.AdminListQueryVo;
import com.meiyuan.catering.admin.vo.admin.admin.CommonRoleVO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lhm
 * @date 2020/5/19 9:42
 **/
@Service
public class AdminClient {

    @Autowired
    private CateringAdminService cateringAdminService;


    /**
     * @Author lhm
     * @Description 创建管理员
     * @Date 2020/5/19
     * @Param [dto]
     * @return {@link Result< Integer>}
     * @Version v1.1.0
     */
    public Result<Integer> create(AdminAddDTO dto) {
        return Result.succ(cateringAdminService.create(dto));

    }

    /**
     * @Author lhm
     * @Description 管理员列表分页查询
     * @Date 2020/5/19
     * @Param [dto]
     * @return {@link Result<IPage<AdminListQueryVo>>}
     * @Version v1.1.0
     */
    public Result<IPage<AdminListQueryVo>> querySelective(AdminQueryDTO dto) {
        return Result.succ(cateringAdminService.querySelective(dto));
    }


    /**
     * @Author lhm
     * @Description 修改管理员信息
     * @Date 2020/5/19
     * @Param [dto]
     * @return {@link Result< Integer>}
     * @Version v1.1.0
     */
    public Result<Integer> updateAdmin(AdminUpdateDTO dto) {
        return Result.succ(cateringAdminService.updateAdmin(dto));
    }


    /**
     * @Author lhm
     * @Description 管理员详情
     * @Date 2020/5/19
     * @Param [id]
     * @return {@link Result< AdminDetailsVo>}
     * @Version v1.1.0
     */
    public Result<AdminDetailsVo> details(Long id) {
        return Result.succ(cateringAdminService.details(id));
    }

    /**
     * @Author lhm
     * @Description 修改byid
     * @Date 2020/5/19
     * @Param [checkTel]
     * @return {@link }
     * @Version v1.1.0
     */
    public void updateById(CateringAdmin checkTel) {
        cateringAdminService.updateById(checkTel);
    }

    /**
     * @Author lhm
     * @Description 获取管理员
     * @Date 2020/5/19
     * @Param [queryWrapper]
     * @return {@link Result< CateringAdmin>}
     * @Version v1.1.0
     */
    public Result<CateringAdmin> getOne(LambdaQueryWrapper<CateringAdmin> queryWrapper) {
        return Result.succ(cateringAdminService.getOne(queryWrapper));
    }

    public  Result<CateringAdmin> getById(Long id) {
        return Result.succ(cateringAdminService.getById(id));
    }

    public Result<PageData<CommonRoleVO>> queryShopRolePageList(CommonRolePageListParamsDTO merchantRolePageListParamsDTO) {
        return null;
    }
}
