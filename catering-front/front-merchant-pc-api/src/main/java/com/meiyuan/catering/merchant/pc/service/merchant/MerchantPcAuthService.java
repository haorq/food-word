package com.meiyuan.catering.merchant.pc.service.merchant;

import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.core.config.EncryptPasswordProperties;
import com.meiyuan.catering.core.constant.DefaultPwdMsg;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.enums.base.TokenEnum;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.AesEncryptUtil;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Md5Util;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.*;
import com.meiyuan.catering.merchant.dto.merchant.MerchantDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.enums.LogBackTypeEnum;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.feign.MerchantLoginAccountClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.feign.ShopPrintingConfigClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Description 后台商户管理服务
 * @Date 2020/3/12 0012 15:37
 */
@Service
@Slf4j
public class MerchantPcAuthService {
    @Resource
    MerchantLoginAccountClient loginAccountClient;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    MerchantClient merchantClient;
    @Resource
    ShopClient shopClient;
    @Resource
    NotifyService notifyService;
    @Resource
    EncryptPasswordProperties encryptProperties;
    @Resource
    ShopPrintingConfigClient shopPrintingConfigClient;

    @Autowired
    private RoleClient roleClient;


    public Result<String> login(MerchantPcUserDTO dto) {
        List<MerchantLoginAccountDTO> accountList = null;

        if (Objects.equals(dto.getAccountType(), AccountTypeEnum.MERCHANT.getStatus())) {
            accountList = merchantClient.listMerchantAccount(dto.getUsername()).getData();
        } else {
            MerchantLoginAccountDTO loginAccountQuery = new MerchantLoginAccountDTO();
            loginAccountQuery.setPhone(dto.getUsername());
            accountList = loginAccountClient.getAccount(loginAccountQuery).getData();
            //
        }

        //判断账号是否可用
        if (!BaseUtil.judgeList(accountList)) {
            return Result.fail("该账号不存在，请重新输入");
        }

        MerchantLoginAccountDTO loginAccount = accountList.get(0);
        if (loginAccount.getIsDel()) {
            return Result.fail("您的账号已被删除,请联系管理员");
        }

        //自提点身份不可登陆
        if (Objects.equals(loginAccount.getAccountType(), AccountTypeEnum.PICKUP.getStatus())) {
            return Result.fail("该账号不存在，请重新输入");
        }

        //判断密码是否正确
        String password = loginAccount.getPassword();

        //解密密码
        String desEncrypt = AesEncryptUtil.desEncrypt(dto.getPassword(), encryptProperties.getKey(), encryptProperties.getIv());
        if (!Objects.equals(password, Md5Util.passwordEncrypt(desEncrypt))) {
            return Result.fail("密码错误，请重新输入");
        }

        if (Objects.equals(loginAccount.getAccountType(), AccountTypeEnum.EMPLOYEE.getStatus())){
            if (Objects.equals(loginAccount.getAccountStatus(), StatusEnum.ENABLE_NOT.getStatus())){
                return Result.fail("账号已被禁用");
            }
            Boolean permissionLogin = roleClient.checkIfHasPermissionLogin(loginAccount.getAccountTypeId(), 0).getData();
            if (!permissionLogin){
                return Result.fail("无登录权限，请联系门店负责人");
            }
        }

        Map<String, Object> map = new HashMap<>(3);
        //账号类型，账号类型对应id
        map.put("accountTypeId", loginAccount.getAccountTypeId());
        map.put("accountType", loginAccount.getAccountType());
        map.put("logBackType", LogBackTypeEnum.NORMAL.getStatus());
        String token = TokenUtil.generateToken(map, TokenEnum.MERCHANT);
        merchantUtils.putMerchantPcToken(loginAccount.getAccountTypeId().toString(), token, dto.getAutoLogin());
        return Result.succ(token);
    }

    /**
     * 方法描述 : 修改密码
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 14:05
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    public Result updateAccountPwd(MerchantAccountPwdDTO dto) {
        //处理登录密码
        this.handelPassword(dto);
        return loginAccountClient.updateAccountPwd(dto);
    }


    /**
     * 方法描述 : 修改密码-通过短信验证码修改密码
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/19 0019 11:52
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.3.0
     */
    public Result updatePwdByPhone(MerchantAccountPwdDTO dto) {
        String merPcSmsAuthCode = merchantUtils.getMerPcSmsAuthCode(dto.getPhone() + dto.getAccountType());
        if (!Objects.equals(merPcSmsAuthCode, dto.getSmsCode())) {
            return Result.fail("验证码错误");
        }

        //处理登录密码
        this.handelPassword(dto);

        CateringMerchantLoginAccountEntity loginAccountEntity = null;
        if (Objects.equals(AccountTypeEnum.MERCHANT.getStatus(), dto.getAccountType())) {
            MerchantDTO merchantEntity = merchantClient.pcLoginInfo(dto.getPhone()).getData();
            if (ObjectUtils.isEmpty(merchantEntity)) {
                return Result.fail("该账号不存在，请重新输入");
            }
            dto.setAccountTypeId(merchantEntity.getId());
            Result<CateringMerchantLoginAccountEntity> loginAccountResult = loginAccountClient.selectByAccountTypeId(merchantEntity.getId());
            loginAccountEntity = loginAccountResult.getData();
            if (ObjectUtils.isEmpty(loginAccountEntity)){
                return Result.fail("该账号不存在，请重新输入");
            }

        }

        if (!Objects.equals(AccountTypeEnum.MERCHANT.getStatus(), dto.getAccountType())) {
            Result<CateringMerchantLoginAccountEntity> loginAccountResult = loginAccountClient.selectByPhone(dto.getPhone());
            loginAccountEntity = loginAccountResult.getData();
            if (ObjectUtils.isEmpty(loginAccountEntity)){
                return Result.fail("该账号不存在，请重新输入");
            }

            if (Objects.equals(loginAccountEntity.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())&&
                    Objects.equals(loginAccountEntity.getAccountStatus(),StatusEnum.ENABLE_NOT.getStatus())){
                return Result.fail(LogBackTypeEnum.DISABLE_ACCOUNT.getDesc());
            }

        }
        Result result = loginAccountClient.updatePwdByPhone(dto,loginAccountEntity);
        return result;
    }

    /**
     * 方法描述 : 发送短信验证码
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 21:21
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    public Result<String> sendSmsCodeCache(MerchantAccountPwdDTO dto) {
        if (Objects.equals(AccountTypeEnum.MERCHANT.getStatus(), dto.getAccountType())) {
            MerchantDTO merchantEntity = merchantClient.pcLoginInfo(dto.getPhone()).getData();
            if (ObjectUtils.isEmpty(merchantEntity)) {
                return Result.fail("该账号不存在，请重新输入");
            }
        }

        if (!Objects.equals(AccountTypeEnum.MERCHANT.getStatus(), dto.getAccountType())) {
            Result<CateringMerchantLoginAccountEntity> loginAccountData = loginAccountClient.selectByPhone(dto.getPhone());
            CateringMerchantLoginAccountEntity loginAccountEntity = loginAccountData.getData();
            if (ObjectUtils.isEmpty(loginAccountEntity)){
                return Result.fail("该账号不存在，请重新输入");
            }
            if (Objects.equals(loginAccountEntity.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())&&
                Objects.equals(loginAccountEntity.getAccountStatus(),StatusEnum.ENABLE_NOT.getStatus())){
                return Result.fail(LogBackTypeEnum.DISABLE_ACCOUNT.getDesc());
            }
        }

        String code = CodeGenerator.msgCode(6);
        merchantUtils.saveMerPcSmsAuthCode(dto.getPhone() + dto.getAccountType(), code);

        //发送短息验证码
        if (!DefaultPwdMsg.USE_DEFAULT_MSG) {
            notifyService.notifySmsTemplate(dto.getPhone(), NotifyType.VERIFY_CODE_NOTIFY, new String[]{code});
        }
        return Result.succ();
    }

    private void handelPassword(MerchantAccountPwdDTO dto) {
        if (BaseUtil.judgeString(dto.getPassword())) {
            String desEncrypt = AesEncryptUtil.desEncrypt(dto.getPassword(), encryptProperties.getKey(), encryptProperties.getIv());
            dto.setPassword(desEncrypt);
        }

        if (BaseUtil.judgeString(dto.getOldPassWord())) {
            String desEncrypt = AesEncryptUtil.desEncrypt(dto.getOldPassWord(), encryptProperties.getKey(), encryptProperties.getIv());
            dto.setOldPassWord(desEncrypt);
        }
    }

    public Result<MerchantAccountInfoDTO> infoV3(MerchantAccountDTO dto) {
        MerchantAccountInfoDTO merchantAccountInfo = BaseUtil.objToObj(dto, MerchantAccountInfoDTO.class);
        merchantAccountInfo.setAccountChange(Boolean.FALSE);
        //获取账号退出登录前账号状态
        Integer pcAccountStatus = merchantUtils.getPcAccountStatus(dto.getAccountTypeId());

        //若账号退出登录状态未被记住则
        if (ObjectUtils.isEmpty(pcAccountStatus)) {
            merchantUtils.putPcAccountStatus(dto.getAccountTypeId(), pcAccountStatus = dto.getAccountStatus());
            return Result.succ(merchantAccountInfo);
        }

        //若状态发生改变则记录当前状态为最后一次登录状态
        if (!Objects.equals(pcAccountStatus, dto.getAccountStatus())) {
            merchantAccountInfo.setAccountChange(Boolean.TRUE);
            merchantUtils.putPcAccountStatus(dto.getAccountTypeId(), dto.getAccountStatus());
        }
        if (Objects.equals(dto.getAccountType(), AccountTypeEnum.EMPLOYEE.getStatus())){
            merchantAccountInfo.setAccountTypeId(dto.getEmployeeId());
        }
        return Result.succ(merchantAccountInfo);
    }

    public Result<List<Map<String, Object>>> getPermissionInfo(MerchantAccountDTO merchantAccountDTO) {
        // 门店负责人获取所有权限
      /*  ShopDTO shopDTO = shopClient.getByPhone(merchantAccountDTO.getPhone()).getData();
        if (shopDTO != null) {
            return Result.succ(roleClient.getAllPermission(Arrays.asList(PermissionTypeEnum.MERCHANT_PC.getStatus(),
                    PermissionTypeEnum.MERCHANT_APP.getStatus())));
        }*/
        // 员工获取授权权限
        return Result.succ(roleClient.loginAccountPermission(merchantAccountDTO.getAccountTypeId(),false));
    }
}
