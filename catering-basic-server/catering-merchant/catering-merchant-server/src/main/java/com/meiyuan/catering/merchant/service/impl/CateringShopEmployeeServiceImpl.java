package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.constant.DefaultPwdMsg;
import com.meiyuan.catering.core.entity.IdEntity;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dao.CateringMerchantLoginAccountMapper;
import com.meiyuan.catering.merchant.dao.CateringShopEmployeeMapper;
import com.meiyuan.catering.merchant.dao.CateringShopMapper;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopEmployeeDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopVerifyDTO;
import com.meiyuan.catering.merchant.dto.shop.config.EmployeeQueryPageDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.enums.LogBackTypeEnum;
import com.meiyuan.catering.merchant.service.CateringMerchantLoginAccountService;
import com.meiyuan.catering.merchant.service.CateringShopEmployeeService;
import com.meiyuan.catering.merchant.service.CateringShopService;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fql
 */
@Service
public class CateringShopEmployeeServiceImpl extends ServiceImpl<CateringShopEmployeeMapper, CateringShopEmployeeEntity> implements CateringShopEmployeeService {

    @Resource
    private NotifyService notifyService;
    @Resource
    CateringMerchantLoginAccountService cateringMerchantLoginAccountService;
    @Resource
    CateringShopMapper cateringShopMapper;

    @Resource
    MerchantUtils merchantUtils;

    @Override
    public PageData<ShopEmployeeDTO> queryPageShopEmployee(EmployeeQueryPageDTO dto){
        return new PageData<>(this.baseMapper.queryPageShopEmployee(dto.getPage(),dto));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveUpdateEmployeeInfo(ShopEmployeeDTO shopEmployeeDTO){
        CateringShopEmployeeEntity employeeEntity = ConvertUtils.sourceToTarget(shopEmployeeDTO, CateringShopEmployeeEntity.class);
        //验证店铺手机号是否重复
        ShopVerifyDTO shopVerify = new ShopVerifyDTO();
        shopVerify.setRegisterPhone(shopEmployeeDTO.getPhone());
        List<CateringShopEntity> shopEntities = cateringShopMapper.verifyShopInfoUniqueDto(shopVerify);
        boolean phoneExist = BaseUtil.judgeList(shopEntities);

        //Objects.isNull(shopEmployeeDTO.getId())
        if(Objects.isNull(shopEmployeeDTO.getId()) && phoneExist){
            throw new CustomException("该手机号已注册，请重新输入");
        }
        //验证员工手机号是否重复
        List<String> allPhone = baseMapper.selectAll();
        if(CollectionUtils.isNotEmpty(allPhone) && allPhone.stream().anyMatch(phone -> Objects.equals(phone,shopEmployeeDTO.getPhone())) && Objects.isNull(shopEmployeeDTO.getId())){
            throw new CustomException("该手机号已注册，请重新输入");
        }
        employeeEntity.setIsDel(0);
        employeeEntity.setUpdateTime(LocalDateTime.now());
        String code = PasswordUtil.getPassword();
        String encodedPassword = Md5Util.passwordEncrypt(code);
        //更新
        encodedPassword = updateEmployeeInfo(shopEmployeeDTO, employeeEntity, encodedPassword, allPhone, phoneExist);
        //添加
        if(Objects.isNull(shopEmployeeDTO.getId())){
            employeeEntity.setCreateTime(LocalDateTime.now());
            employeeEntity.setAccountNumber(IdWorker.getIdStr().substring(0,8));
            employeeEntity.setPassword(encodedPassword);
            employeeEntity.setId(IdWorker.getId());
            baseMapper.insert(employeeEntity);
            insertLoginAccount(employeeEntity);
            //账号创建成功通知商户
            if (!DefaultPwdMsg.USE_DEFAULT_PWD){
                notifyService.notifySmsTemplate(shopEmployeeDTO.getPhone(), NotifyType.CREATE_ACCOUNT_SUCCESS_NOTIFY, new String[]{shopEmployeeDTO.getPhone(), code});
            }

        }
        return employeeEntity.getId();
    }

    private String updateEmployeeInfo(ShopEmployeeDTO shopEmployeeDTO, CateringShopEmployeeEntity employeeEntity, String encodedPassword, List<String> allPhone, Boolean phoneExist) {
        if(Objects.nonNull(shopEmployeeDTO.getId())){
            String pcToken;
            MerchantTokenDTO appToken;
            UpdateWrapper<CateringMerchantLoginAccountEntity> updateWrapper = new UpdateWrapper<>();
            if(Objects.nonNull(shopEmployeeDTO.getPassword())){
                encodedPassword = Md5Util.passwordEncrypt(shopEmployeeDTO.getPassword());
                employeeEntity.setPassword(encodedPassword);
                updateWrapper.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId, shopEmployeeDTO.getId())
                        .set(CateringMerchantLoginAccountEntity::getPassword, encodedPassword);
                pcToken = merchantUtils.getMerchantPcToken(shopEmployeeDTO.getId().toString());
                if(StringUtils.isNotEmpty(pcToken)){
                    pcToken = TokenUtil.generatePcToken(shopEmployeeDTO.getId(), 5, LogBackTypeEnum.CHANGE_PASSWORD.getStatus());
                    merchantUtils.putMerchantPcToken(shopEmployeeDTO.getId().toString(), pcToken, true);
                }

                appToken = merchantUtils.getMerchantByToken(shopEmployeeDTO.getId().toString());
                if(Objects.nonNull(appToken)){
                    appToken.setLogBackType(LogBackTypeEnum.CHANGE_PASSWORD.getStatus());
                    merchantUtils.saveAppMerchantToken(shopEmployeeDTO.getId(),appToken);
                }
            }
            if(Objects.nonNull(shopEmployeeDTO.getStatus())){
                updateWrapper.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId, shopEmployeeDTO.getId()).set(CateringMerchantLoginAccountEntity::getAccountStatus,shopEmployeeDTO.getStatus().equals(0)?2:1);
            }
            if(Objects.nonNull(shopEmployeeDTO.getPhone())){
                if(Objects.nonNull(phoneExist) && phoneExist){
                    throw new CustomException("该手机号已注册，请重新输入");
                }
                CateringShopEmployeeEntity employee = baseMapper.selectById(shopEmployeeDTO.getId());
                if(CollectionUtils.isNotEmpty(allPhone)) {
                    boolean anyMatch = allPhone.stream().anyMatch(phone -> Objects.equals(phone, shopEmployeeDTO.getPhone()));
                    if (anyMatch && !Objects.equals(employee.getPhone(), shopEmployeeDTO.getPhone())) {
                        throw new CustomException("该手机号已注册，请重新输入");
                    }
                }
                updateWrapper.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId, shopEmployeeDTO.getId()).set(CateringMerchantLoginAccountEntity::getPhone,shopEmployeeDTO.getPhone());
                if(!Objects.equals(employee.getPhone(),shopEmployeeDTO.getPhone())){
                    pcToken = merchantUtils.getMerchantPcToken(shopEmployeeDTO.getId().toString());
                    if(StringUtils.isNotEmpty(pcToken)){
                        pcToken = TokenUtil.generatePcToken(shopEmployeeDTO.getId(), 5, LogBackTypeEnum.CHANGE_PHONE.getStatus());
                        merchantUtils.putMerchantPcToken(shopEmployeeDTO.getId().toString(), pcToken, true);
                    }

                    appToken = merchantUtils.getMerchantByToken(shopEmployeeDTO.getId().toString());
                    if(Objects.nonNull(appToken)){
                        appToken.setLogBackType(LogBackTypeEnum.CHANGE_PHONE.getStatus());
                        merchantUtils.saveAppMerchantToken(shopEmployeeDTO.getId(),appToken);
                    }
                    String code = PasswordUtil.getPassword();
                    encodedPassword = Md5Util.passwordEncrypt(code);
                    employeeEntity.setPassword(encodedPassword);
                    updateWrapper.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId, shopEmployeeDTO.getId())
                            .set(CateringMerchantLoginAccountEntity::getPassword, encodedPassword);
                    if(!DefaultPwdMsg.USE_DEFAULT_PWD){
                        notifyService.notifySmsTemplate(shopEmployeeDTO.getPhone(), NotifyType.CREATE_ACCOUNT_SUCCESS_NOTIFY, new String[]{shopEmployeeDTO.getPhone(), code});
                    }
                }
            }
            cateringMerchantLoginAccountService.update(updateWrapper);
            this.updateById(employeeEntity);
            if(Objects.nonNull(shopEmployeeDTO.getStatus()) && shopEmployeeDTO.getStatus() == 0){
                pcToken = merchantUtils.getMerchantPcToken(shopEmployeeDTO.getId().toString());
                if(StringUtils.isNotEmpty(pcToken)){
                    pcToken = TokenUtil.generatePcToken(shopEmployeeDTO.getId(), 5, LogBackTypeEnum.DISABLE_ACCOUNT.getStatus());
                    merchantUtils.putMerchantPcToken(shopEmployeeDTO.getId().toString(), pcToken, true);
                }
                appToken = merchantUtils.getMerchantByToken(shopEmployeeDTO.getId().toString());
                if(Objects.nonNull(appToken)){
                    appToken.setLogBackType(LogBackTypeEnum.DISABLE_ACCOUNT.getStatus());
                    merchantUtils.saveAppMerchantToken(shopEmployeeDTO.getId(),appToken);
                }
            }
        }
        return encodedPassword;
    }


    private void insertLoginAccount(CateringShopEmployeeEntity employeeEntity) {
        //增加员工登陆账号信息
        CateringMerchantLoginAccountEntity loginAccountEntity;
        loginAccountEntity = ConvertUtils.sourceToTarget(employeeEntity, CateringMerchantLoginAccountEntity.class);
        if(Objects.equals(employeeEntity.getStatus(),1)){
            loginAccountEntity.setAccountStatus(1);
        }else if(Objects.equals(employeeEntity.getStatus(),0)){
            loginAccountEntity.setAccountStatus(2);
        }
        loginAccountEntity.setAccountType(5);
        loginAccountEntity.setAccountTypeId(employeeEntity.getId());
        loginAccountEntity.setIsDel(false);
        cateringMerchantLoginAccountService.getBaseMapper().insert(loginAccountEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String delEmployeeById(Long id){
        //删除员工信息
        CateringShopEmployeeEntity cateringShopEmployeeEntity = baseMapper.selectById(id);
        if(Objects.isNull(cateringShopEmployeeEntity)){
            throw new CustomException("未查到员工信息");
        }
        cateringShopEmployeeEntity.setIsDel(1);
        int i = baseMapper.updateById(cateringShopEmployeeEntity);


        UpdateWrapper<CateringMerchantLoginAccountEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId, id).set(CateringMerchantLoginAccountEntity::getIsDel, DelEnum.DELETE.getFlag());
        cateringMerchantLoginAccountService.update(updateWrapper);
        return i==1?"操作成功":"操作失败";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String delAllEmployee(Long shopId) {
        QueryWrapper<CateringShopEmployeeEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CateringShopEmployeeEntity::getShopId,shopId);
        List<CateringShopEmployeeEntity> allEmployee = baseMapper.selectList(queryWrapper);
        allEmployee.forEach(employee -> employee.setIsDel(1));
        boolean success = this.saveOrUpdateBatch(allEmployee);

        //同时删除登陆账号数据

        if(CollectionUtils.isNotEmpty(allEmployee)){
            List<Long> ids = allEmployee.stream().map(IdEntity::getId).collect(Collectors.toList());
            cateringMerchantLoginAccountService.batchDelEmployee(ids);
        }
        return success?"操作成功":"操作失败";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<List<Long>> delAllEmployeePc(Long shopId) {
        QueryWrapper<CateringShopEmployeeEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CateringShopEmployeeEntity::getShopId,shopId).eq(CateringShopEmployeeEntity::getIsDel,DelEnum.NOT_DELETE.getFlag());
        List<CateringShopEmployeeEntity> allEmployee = baseMapper.selectList(queryWrapper);
        List<Long> employeeIds = new ArrayList<>();
        if (!BaseUtil.judgeList(allEmployee)){
            return Result.succ(employeeIds);
        }
        allEmployee.forEach(employee -> {
            employee.setIsDel(1);
            employeeIds.add(employee.getId());
        });
        //同时删除登陆账号数据

        if(CollectionUtils.isNotEmpty(allEmployee)){
            List<Long> ids = allEmployee.stream().map(IdEntity::getId).collect(Collectors.toList());
            cateringMerchantLoginAccountService.batchDelEmployee(ids);
        }
        boolean success = this.saveOrUpdateBatch(allEmployee);
        return Result.succ(employeeIds);
    }

    @Override
    public List<CateringShopEmployeeEntity> selectShopEmployee(Long shopId){
        QueryWrapper<CateringShopEmployeeEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CateringShopEmployeeEntity::getShopId,shopId);
        return baseMapper.selectList(queryWrapper);
    }
}
