package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.core.enums.base.IsDefaultEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.redis.utils.DicUtils;
import com.meiyuan.catering.core.util.Md5Util;
import com.meiyuan.catering.user.dao.CateringUserCompanyMapper;
import com.meiyuan.catering.user.dao.CateringUserMapper;
import com.meiyuan.catering.user.dto.user.UserCompanyAddDTO;
import com.meiyuan.catering.user.dto.user.UserCompanyExitDTO;
import com.meiyuan.catering.user.entity.CateringUserCompanyEntity;
import com.meiyuan.catering.user.entity.CateringUserEntity;
import com.meiyuan.catering.user.query.user.RecycleQueryDTO;
import com.meiyuan.catering.user.query.user.UserCompanyQueryDTO;
import com.meiyuan.catering.user.query.user.UserQueryDTO;
import com.meiyuan.catering.user.service.CateringUserCompanyService;
import com.meiyuan.catering.user.vo.user.CompanyDetailVo;
import com.meiyuan.catering.user.vo.user.RecycleListVo;
import com.meiyuan.catering.user.vo.user.UserCompanyListVo;
import com.meiyuan.catering.user.vo.user.UserListVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户企业信息表(CateringUserCompany)表服务实现类
 *
 * @author mei-tao
 * @since 2020-03-10 15:31:41
 */
@Service("cateringUserCompanyService")
public class CateringUserCompanyServiceImpl extends ServiceImpl<CateringUserCompanyMapper, CateringUserCompanyEntity> implements CateringUserCompanyService {
    @Resource
    private CateringUserCompanyMapper cateringUserCompanyMapper;
    @Resource
    private CateringUserMapper cateringUserMapper;
    @Resource
    private NotifyService notifyService;
    @Resource
    private DicUtils dicUtils;


    @Override
    public IPage<UserCompanyListVo> companyList(UserCompanyQueryDTO dto) {
        return cateringUserCompanyMapper.companyList(dto.getPage(), dto);
    }

    @Override
    public CompanyDetailVo companyDetailById(Long id) {
        return cateringUserCompanyMapper.companyDetailById(id);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CateringUserCompanyEntity companyAdd(UserCompanyAddDTO dto) {
        if (checkCompanyUser(dto) != null) {
            throw new CustomException("该企业账号已存在，不能再次添加");
        }
        //添加企业用户
        CateringUserCompanyEntity userCompanyEntity = addCompany(dto);
        dicUtils.put(userCompanyEntity.getId(), userCompanyEntity.getCompanyStatus());
        return userCompanyEntity;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateUserCompany(UserCompanyExitDTO dto) {
        //启用 禁用  编辑
        CateringUserCompanyEntity entity = this.getById(dto.getId());
        if (dto.getType().equals(IsDefaultEnum.DEFAULT.getStatus())) {
            entity.setAccount(dto.getPhone());
            //不管修改的手机号和原来的相不相同  都要发送短信
            String code = CodeGenerator.randomCode(6);
            String encodedPassword = Md5Util.passwordEncrypt(code);
            notifyService.notifySmsTemplate(dto.getPhone(), NotifyType.CREATE_ENTERPRISE_ACCOUNT_SUCCESS_NOTIFY, new String[]{dto.getPhone(), code});
            entity.setPassword(encodedPassword);
            entity.setContantName(dto.getName());
            entity.setIsDel(dto.getDel());
            return cateringUserCompanyMapper.updateById(entity);

        }
        //1为编辑  2为删除  判断下面是否有解绑用户
        if (dto.getType().equals(IsDefaultEnum.NO_DEFAULT.getStatus())) {
            LambdaQueryWrapper<CateringUserEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CateringUserEntity::getUserCompanyId, dto.getId());
            CateringUserEntity selectOne = cateringUserMapper.selectOne(wrapper);
            Map<Object, Object> map = new HashMap<>(3);
            if (selectOne != null) {

                map.put("type", false);
                map.put("companyName", entity.getCompanyName());
                map.put("name", entity.getContantName());
                return map;
            }
            entity.setIsDel(true);
            entity.setCompanyStatus(1);
            entity.setDeleteTime(LocalDateTime.now());
            cateringUserCompanyMapper.updateById(entity);
            map.put("type", true);
            map.put("companyName", entity.getCompanyName());
            return map;
        } else {
            entity.setCompanyStatus(dto.getCompanyStatus());
            dicUtils.put(entity.getId(), entity.getCompanyStatus());
            return cateringUserCompanyMapper.updateById(entity);
        }

    }

    @Override
    public IPage<UserListVo> userList(UserQueryDTO dto) {
        LocalDateTime endTime = dto.getEndTime();
        dto.setEndTime(endTime == null ? null : endTime.plusDays(1));
        return cateringUserCompanyMapper.userList(dto.getPage(), dto);
    }

    @Override
    public IPage<RecycleListVo> recycleList(RecycleQueryDTO dto) {
        return cateringUserCompanyMapper.recycleList(dto.getPage(), dto);
    }


    public CateringUserCompanyEntity checkCompanyUser(UserCompanyAddDTO dto) {
        LambdaQueryWrapper<CateringUserCompanyEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringUserCompanyEntity::getAccount, dto.getPhone());
        return cateringUserCompanyMapper.selectOne(wrapper);
    }

    @Override
    public List<Long> getIdsByKeyword(String keyword) {
        LambdaQueryWrapper<CateringUserCompanyEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CateringUserCompanyEntity::getId);
        wrapper.like(CateringUserCompanyEntity::getCompanyName, keyword).or()
                .like(CateringUserCompanyEntity::getAccount, keyword);
        List<Object> list = baseMapper.selectObjs(wrapper);
        return list.stream().map(i -> Long.valueOf(i.toString())).collect(Collectors.toList());
    }


    public CateringUserCompanyEntity addCompany(UserCompanyAddDTO dto) {
        CateringUserCompanyEntity userCompanyEntity = new CateringUserCompanyEntity();
        BeanUtils.copyProperties(dto, userCompanyEntity);

        //  企业账号默认为联系人手机号  如果该手机已经绑定了 企业 则不能绑定了
        userCompanyEntity.setAccount(dto.getPhone());
        String code = CodeGenerator.randomCode(6);
        String encodedPassword = Md5Util.passwordEncrypt(code);
        userCompanyEntity.setPassword(encodedPassword);
        userCompanyEntity.setContantName(dto.getName());
        userCompanyEntity.setIsDel(false);
        userCompanyEntity.setCompanyStatus(1);
        userCompanyEntity.setCreateTime(LocalDateTime.now());
        userCompanyEntity.setId(IdWorker.getId());
        cateringUserCompanyMapper.insert(userCompanyEntity);
        notifyService.notifySmsTemplate(dto.getPhone(), NotifyType.CREATE_ENTERPRISE_ACCOUNT_SUCCESS_NOTIFY, new String[]{dto.getPhone(), code});
        return userCompanyEntity;
    }


    @Override
    public List<UserCompanyInfo> getListUcInfo(List<Long> uidOrCids) {
        return baseMapper.getListUcInfo(uidOrCids);
    }

}



