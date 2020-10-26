package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringAdminMapper;
import com.meiyuan.catering.admin.dto.admin.admin.AdminAddDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminQueryDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminUpdateDTO;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.entity.CateringRoleEntity;
import com.meiyuan.catering.admin.entity.CateringSubjectRoleRelationEntity;
import com.meiyuan.catering.admin.service.CateringAdminService;
import com.meiyuan.catering.admin.service.CateringRoleService;
import com.meiyuan.catering.admin.vo.admin.admin.AdminDetailsVo;
import com.meiyuan.catering.admin.vo.admin.admin.AdminListQueryVo;
import com.meiyuan.catering.core.config.EncryptPasswordProperties;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.AesEncryptUtil;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.core.util.bcrypt.BcryptPasswordEncoder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author MeiTao
 * @Date 2020/6/23 0023 17:50
 * @Description 简单描述 :  CateringAdminServiceImpl
 * @Since version-1.0.0
 */
@Service
public class CateringAdminServiceImpl extends ServiceImpl<CateringAdminMapper, CateringAdmin> implements CateringAdminService {

    @Resource
    private CateringAdminMapper adminMapper;

    @Autowired
    private EncryptPasswordProperties encryptPasswordProperties;
    @Autowired
    private CateringRoleService roleService;




    public void add(CateringAdmin admin) {
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        adminMapper.insert(admin);
    }



    /**
     * 添加管理员
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer create(AdminAddDTO dto) {
        //1.验证手机号是否存在
        CateringAdmin admin = checkTel(dto.getPhone());
        if (null != admin) {
            throw new CustomException("该管理员手机号已存在");
        }

        CateringAdmin cateringAdmin = new CateringAdmin();
        //解密密码
        String desEncrypt = AesEncryptUtil.desEncrypt(dto.getPassword(), encryptPasswordProperties.getKey(), encryptPasswordProperties.getIv());

        BcryptPasswordEncoder encoder = new BcryptPasswordEncoder();
        String encodedPassword = encoder.encode(desEncrypt);
        BeanUtils.copyProperties(dto, cateringAdmin);
        cateringAdmin.setPassword(encodedPassword);
        cateringAdmin.setCreateTime(LocalDateTime.now());
        cateringAdmin.setIsDel(false);
        int insert = adminMapper.insert(cateringAdmin);
        if(insert>0){
            // 处理操作员角色
            insertAccountPermission(cateringAdmin.getId(),dto.getRoleList(),false);
        }
        return insert;

    }


    @Override
    public IPage<AdminListQueryVo> querySelective(AdminQueryDTO dto) {
        return this.baseMapper.querySelect(dto.getPage(),dto);

        /*LambdaQueryWrapper<CateringAdmin> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(dto.getNameOrTel())) {
            dto.setNameOrTel(CharUtil.disposeChar(dto.getNameOrTel()));
            queryWrapper.like(CateringAdmin::getUsername,  dto.getNameOrTel()).or();
            queryWrapper.like(CateringAdmin::getPhone, dto.getNameOrTel());
        }
        if (!StringUtils.isEmpty(dto.getStatus())) {
            queryWrapper.eq(CateringAdmin::getStatus, dto.getStatus());
        }
        if (dto.getEndTime() != null) {
            queryWrapper.le(CateringAdmin::getCreateTime, dto.getEndTime().plusDays(1));
        }
        if (dto.getStartTime() != null) {
            queryWrapper.ge(CateringAdmin::getCreateTime, dto.getStartTime());
        }
        queryWrapper.eq(CateringAdmin::getIsDel, false);

        //排序
        queryWrapper.orderByDesc(CateringAdmin::getCreateTime);
        return adminMapper.selectPage(dto.getPage(), queryWrapper);*/
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateAdmin(AdminUpdateDTO dto) {
        //正常编辑  手机号需要验证 不能重复
        //启用 禁用  删除
        CateringAdmin admin = checkTel(dto.getPhone());
        if (null != admin && !admin.getId().equals(dto.getId())) {
            throw new CustomException("该管理员手机号已存在");
        }
        int i;
        if (dto.getIsDel() != null && dto.getIsDel()) {
            i = adminMapper.deleteById(dto.getId());
        } else {
            CateringAdmin cateringAdmin = adminMapper.selectById(dto.getId());
            Integer status = cateringAdmin.getStatus();
            BeanUtils.copyProperties(dto, cateringAdmin);
            i = adminMapper.updateById(cateringAdmin);
            if(i>0 && (dto.getStatus().equals(status))){
                insertAccountPermission(dto.getId(),dto.getRoleList(),true);
    }
}
        return i;
    }

    @Override
    public AdminDetailsVo details(Long id) {
        AdminDetailsVo vo = new AdminDetailsVo();
        CateringAdmin admin = adminMapper.selectById(id);
        BeanUtils.copyProperties(admin, vo);
        vo.setRoleList(adminMapper.selectRole(id));
        return vo;
    }




    /**
     * 验证手机号
     *
     * @param phone
     * @return
     */
    public CateringAdmin checkTel(String phone) {
        //验证手机号是否存在
        QueryWrapper<CateringAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringAdmin::getPhone, phone);
        return adminMapper.selectOne(queryWrapper);

    }

    public void insertAccountPermission(Long accountId,List<Long> roleIds,boolean isUpdate){
        // 校验角色
        verifyRole(roleIds);
        if(isUpdate){
            // 编辑先删除原有关联的角色
            adminMapper.removeRoleByAccountId(accountId);
        }
        // 处理操作员角色
        List<CateringSubjectRoleRelationEntity> collect = roleIds.stream().map(e -> {
            CateringSubjectRoleRelationEntity entity = new CateringSubjectRoleRelationEntity();
            entity.setId(IdWorker.getId());
            entity.setRoleId(e);
            entity.setSubjectId(accountId);
            return entity;
        }).collect(Collectors.toList());
        adminMapper.insertRoleRelation(collect);
    }

    private void verifyRole(List<Long> roleIds){
        if(CollectionUtils.isEmpty(roleIds)){
            throw new CustomException("请选择角色");
        }
        Collection<CateringRoleEntity> list = roleService.listByIds(roleIds);
        List<String> collect = list.stream().filter(e -> e.getIsDel().equals(DelEnum.DELETE.getStatus())).map(CateringRoleEntity::getRoleName).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collect)){
            return;
        }
        StringBuilder builder = new StringBuilder("角色：");
        collect.forEach(e->{
            builder.append(e)
                    .append(",");
        });
        builder.append("被删除，请确认后重新选择");
        throw new CustomException(builder.toString());

    }

}
