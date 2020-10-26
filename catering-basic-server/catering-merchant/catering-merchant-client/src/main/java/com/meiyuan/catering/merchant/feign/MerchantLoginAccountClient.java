package com.meiyuan.catering.merchant.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountPwdDTO;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity;
import com.meiyuan.catering.merchant.service.CateringMerchantLoginAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:54
 * @Description 简单描述 : 商户登陆账号表服务类
 * @Since version-1.2.0
 */
@Service
public class MerchantLoginAccountClient {

     @Autowired
     private CateringMerchantLoginAccountService loginAccountService;

    /**
     * 方法描述 : 商户端登陆账号信息查询
     *            1、app发送短信验证码；2、pc端获取账号登录信息；3、pc端店铺、商户登录
     * @Author: MeiTao
     */
    public Result<List<MerchantLoginAccountDTO>> getAccount(MerchantLoginAccountDTO dto){
        return loginAccountService.getAccount(dto);
    }

    /**
     * 方法描述 : 修改密码
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 14:05
     * @param dto 请求参数
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    public Result updateAccountPwd(MerchantAccountPwdDTO dto) {
        return loginAccountService.updateAccountPwd(dto);
    }

    /**
     * 方法描述 : pc-通过手机号修改密码
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 14:05
     * @param dto 请求参数
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    public Result updatePwdByPhone(MerchantAccountPwdDTO dto,CateringMerchantLoginAccountEntity loginAccountEntity) {
        return loginAccountService.updatePwdByPhone(dto,loginAccountEntity);
    }

    /**
     * 方法描述 : 通过手机号查询登陆账号信息
     *           查询门店、门店兼自提点、员工
     * @Author: MeiTao
     * @Date: 2020/10/14 0014 15:01
     * @param phone 请求参数
     * @return: com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity
     * @Since version-1.5.0
     */
    public Result<CateringMerchantLoginAccountEntity> selectByPhone(String phone) {
        return loginAccountService.selectByPhone(phone);
    }

    /**
     * 方法描述 : 通过账号类型id查询账号信息
     * @Author: MeiTao
     * @Date: 2020/10/14 0014 15:38
     * @param accountTypeId 请求参数
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity>
     * @Since version-1.5.0
     */
    public Result<CateringMerchantLoginAccountEntity> selectByAccountTypeId(Long accountTypeId) {
        return loginAccountService.selectByAccountTypeId(accountTypeId);
    }
}
