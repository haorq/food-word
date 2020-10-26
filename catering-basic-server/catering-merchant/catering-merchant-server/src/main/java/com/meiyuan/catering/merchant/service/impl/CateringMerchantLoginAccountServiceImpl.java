package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Md5Util;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dao.CateringMerchantLoginAccountMapper;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountPwdDTO;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.enums.LogBackTypeEnum;
import com.meiyuan.catering.merchant.service.CateringMerchantLoginAccountService;
import com.meiyuan.catering.merchant.service.CateringShopPrintingConfigService;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:55
 * @Description 简单描述 : 商户登陆账号表服务实现类
 * @Since version-1.0.0
 */
@Service
public class CateringMerchantLoginAccountServiceImpl extends ServiceImpl<CateringMerchantLoginAccountMapper, CateringMerchantLoginAccountEntity>
        implements CateringMerchantLoginAccountService {
    @Resource
    CateringMerchantLoginAccountMapper loginAccountMapper;

    @Resource
    MerchantUtils merchantUtils;
    @Resource
    CateringShopPrintingConfigService shopPrintingConfigService;

    @Override
    public Result<List<MerchantLoginAccountDTO>> getAccount(MerchantLoginAccountDTO dto) {
        QueryWrapper<CateringMerchantLoginAccountEntity> query = new QueryWrapper<>();

        query.lambda().eq(CateringMerchantLoginAccountEntity::getIsDel, DelEnum.NOT_DELETE.getStatus()).
                eq(BaseUtil.judgeString(dto.getPhone()),CateringMerchantLoginAccountEntity::getPhone,dto.getPhone())
                .eq(!ObjectUtils.isEmpty(dto.getAccountTypeId()),CateringMerchantLoginAccountEntity::getAccountTypeId,dto.getAccountTypeId());
        List<CateringMerchantLoginAccountEntity> loginAccountEntities = loginAccountMapper.selectList(query);
        if (BaseUtil.judgeList(loginAccountEntities)){
            return Result.succ(BaseUtil.objToObj(loginAccountEntities, MerchantLoginAccountDTO.class));
        }
        return Result.succ();
    }

    @Override
    public Result updatePwdByPhone(MerchantAccountPwdDTO dto,CateringMerchantLoginAccountEntity accountEntity) {
        String oldPassword = accountEntity.getPassword();

        //短信验证码方式修改密码
        if (oldPassword.equals(Md5Util.passwordEncrypt(dto.getPassword()))){
            return Result.fail("新密码与旧密码一致，请重新输入");
        }
        accountEntity.setPassword(Md5Util.passwordEncrypt(dto.getPassword()));

        loginAccountMapper.updateById(accountEntity);
        //删除短信验证码
        merchantUtils.removeMerPcSmsAuthCode(dto.getPhone()+ dto.getAccountType());

        //修改密码后清除登陆token
        handelLoginToken(accountEntity.getAccountTypeId(),oldPassword,accountEntity.getAccountType());
        if (Objects.equals(accountEntity.getAccountType(),AccountTypeEnum.SHOP.getStatus())
                ||Objects.equals(accountEntity.getAccountType(),AccountTypeEnum.SHOP_PICKUP.getStatus())
                ||Objects.equals(accountEntity.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())){
            shopPrintingConfigService.delShopPrintingConfig(accountEntity.getAccountTypeId());
        }
        return Result.succ("修改密码成功！");
    }

    /**
     * 方法描述 : 处理登录token信息
     *            1、pc端新密码旧密码修改密码
     * @Author: MeiTao
     * @Date: 2020/8/19 0019 11:43
     * @param accountTypeId  登录账号id
     * @param oldPassword   登录密码
     * @param accountType  账号类型
     * @return: void
     * @Since version-1.3.0
     */
    private void handelLoginToken(Long accountTypeId,String oldPassword,Integer accountType){
        //处理pc端登陆token
        merchantUtils.removeMerchantPcToken(accountTypeId.toString());
        //若为店铺、店铺兼自提点、员工则处理其token
        if (!Objects.equals(accountType,AccountTypeEnum.MERCHANT.getStatus())){
            MerchantTokenDTO merchantAppToken = merchantUtils.getMerchantByToken(String.valueOf(accountTypeId));
            if (!ObjectUtils.isEmpty(merchantAppToken)){
                merchantAppToken.setLogBackType(LogBackTypeEnum.CHANGE_PASSWORD.getStatus());
                merchantUtils.saveAppMerchantToken(accountTypeId,merchantAppToken);
            }
//            String token = Md5Util.md5(oldPassword + accountTypeId);
//            merchantUtils.removeMerAppToken(token);
        }
    }

    @Override
    public Result updateAccountPwd(MerchantAccountPwdDTO dto) {
        QueryWrapper<CateringMerchantLoginAccountEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantLoginAccountEntity::getIsDel,DelEnum.NOT_DELETE.getStatus())
                .eq(CateringMerchantLoginAccountEntity::getPhone,dto.getPhone())
                //@todo 不允许自提点登陆
                .ne(CateringMerchantLoginAccountEntity::getAccountType, AccountTypeEnum.PICKUP.getStatus());

        List<CateringMerchantLoginAccountEntity> loginAccountList = loginAccountMapper.selectList(query);

        if (!BaseUtil.judgeList(loginAccountList)){
            return Result.fail("该账号不存在，请重新输入");
        }
        CateringMerchantLoginAccountEntity loginAccount = loginAccountList.get(0);

        String oldPassword = loginAccount.getPassword();
        //短信验证码方式修改密码
        if (!Objects.equals(loginAccount.getPassword(), Md5Util.md5(dto.getOldPassWord()))){
            return Result.fail("旧密码输入错误");
        }
        loginAccount.setPassword(Md5Util.md5(dto.getPassword()));
        loginAccountMapper.updateById(loginAccount);

        //修改密码后清除登陆token
        this.handelLoginToken(loginAccount.getAccountTypeId(),oldPassword,loginAccount.getAccountType());
        if (Objects.equals(loginAccount.getAccountType(),AccountTypeEnum.SHOP.getStatus())
                ||Objects.equals(loginAccount.getAccountType(),AccountTypeEnum.SHOP_PICKUP.getStatus())
                ||Objects.equals(loginAccount.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())){
            shopPrintingConfigService.delShopPrintingConfig(loginAccount.getAccountTypeId());
        }
        return Result.succ("修改密码成功！");
    }

    @Override
    public String getMerchantLoginAccount() {
        String merchantLoginAccount = merchantUtils.getMerchantLoginAccount(null);

        if (BaseUtil.judgeString(merchantLoginAccount)){
            return merchantLoginAccount;
        }

        //获取当前商户账号中最大值
        Integer maxAccountNum = loginAccountMapper.getMaxAccountNum();

        if (ObjectUtils.isEmpty(maxAccountNum)){
            maxAccountNum = 0;
        }
        return merchantUtils.getMerchantLoginAccount(maxAccountNum);
    }

    @Override
    public CateringMerchantLoginAccountEntity getLoginAccount(Long accountTypeId) {
        QueryWrapper<CateringMerchantLoginAccountEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId,accountTypeId)
                    .eq(CateringMerchantLoginAccountEntity::getIsDel,DelEnum.NOT_DELETE.getStatus());
        List<CateringMerchantLoginAccountEntity> loginAccountEntities = loginAccountMapper.selectList(query);
        if (!BaseUtil.judgeList(loginAccountEntities)){
            throw new CustomException("店铺对应登陆账号信息查询失败 ；账号id ： " + accountTypeId);
        }
        return loginAccountEntities.get(0);
    }

    @Override
    public CateringMerchantLoginAccountEntity getLoginAccountByPhone(String phone) {
        QueryWrapper<CateringMerchantLoginAccountEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantLoginAccountEntity::getPhone,phone)
                .eq(CateringMerchantLoginAccountEntity::getIsDel,DelEnum.NOT_DELETE.getStatus());
        List<CateringMerchantLoginAccountEntity> loginAccountEntities = loginAccountMapper.selectList(query);

        if (!BaseUtil.judgeList(loginAccountEntities)){
            return null;
        }
        return loginAccountEntities.get(0);
    }

    @Override
    public Result<CateringMerchantLoginAccountEntity> selectByPhone(String phone) {
        QueryWrapper<CateringMerchantLoginAccountEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantLoginAccountEntity::getPhone,phone)
                .in(CateringMerchantLoginAccountEntity::getAccountType, Arrays.asList(2,4,5))
                .eq(CateringMerchantLoginAccountEntity::getIsDel,DelEnum.NOT_DELETE.getStatus());
        List<CateringMerchantLoginAccountEntity> loginAccountEntities = loginAccountMapper.selectList(query);

        if (!BaseUtil.judgeList(loginAccountEntities)){
            return Result.succ();
        }
        return Result.succ(loginAccountEntities.get(0));
    }

    @Override
    public Result<CateringMerchantLoginAccountEntity> selectByAccountTypeId(Long accountTypeId) {
        QueryWrapper<CateringMerchantLoginAccountEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId,accountTypeId)
                .eq(CateringMerchantLoginAccountEntity::getIsDel,DelEnum.NOT_DELETE.getStatus());
        List<CateringMerchantLoginAccountEntity> loginAccountEntities = loginAccountMapper.selectList(query);

        if (!BaseUtil.judgeList(loginAccountEntities)){
            return Result.succ();
        }
        return Result.succ(loginAccountEntities.get(0));
    }

    @Override
    public void batchDelEmployee(List<Long> ids){
        loginAccountMapper.batchDelEmployee(ids);
    }

    /**
     * 方法描述 : 商户登录账号修改
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 10:40
     * @return: com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @Since version-1.3.0
     */
    public Result<Boolean> handelMerchantData() {
        QueryWrapper<CateringMerchantLoginAccountEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantLoginAccountEntity::getAccountType,AccountTypeEnum.MERCHANT.getStatus())
                .eq(CateringMerchantLoginAccountEntity::getIsDel,Boolean.FALSE);
        List<CateringMerchantLoginAccountEntity> accountEntities = loginAccountMapper.selectList(query);

        if (!BaseUtil.judgeList(accountEntities)){
            return Result.succ();
        }

        accountEntities.forEach(account->{
            String merchantLoginAccount = merchantUtils.getMerchantLoginAccount();
            account.setPhone(merchantLoginAccount);
        });
        //@TODO 暂时不发送短信验证码到对应手机
        return Result.succ(this.saveOrUpdateBatch(accountEntities));
    }
}
