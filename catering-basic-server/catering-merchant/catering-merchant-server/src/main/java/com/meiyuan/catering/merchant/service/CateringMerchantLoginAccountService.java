package com.meiyuan.catering.merchant.service;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.auth.*;
import com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:54
 * @Description 简单描述 : 商户登陆账号表服务类
 * @Since version-1.2.0
 */
public interface CateringMerchantLoginAccountService extends IService<CateringMerchantLoginAccountEntity> {

    /**
     * 方法描述 : 商户、店铺通过登录账号发送短信验证码【不包含已删除店铺】
     *          1、app发送短信验证码；2、app短信验证码修改密码；3、pc端店铺、商户登录；4pc端获取账号登录信息
     *          4、app端登录
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 10:25
     * @param dto 请求参数
     * @return: MerchantLoginAccountDTO
     * @Since version-1.1.0
     */
    Result<List<MerchantLoginAccountDTO>> getAccount(MerchantLoginAccountDTO dto);

    /**
     * 方法描述 :  商户端修改密码
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 14:05
     * @param dto 请求参数
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    Result updateAccountPwd(MerchantAccountPwdDTO dto);

    /**
     * 方法描述 :  商户端修改密码
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 14:05
     * @param dto 请求参数
     * @param loginAccountEntity 请求参数
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    Result updatePwdByPhone(MerchantAccountPwdDTO dto,CateringMerchantLoginAccountEntity loginAccountEntity);

    /**
     * 方法描述 : 获取当前商户账号中最大值
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 9:09
     * @return: java.lang.Integer
     * @Since version-1.3.0
     */
    String getMerchantLoginAccount();

    /**
     * 方法描述 : 通过登陆账号对应类型id查询登陆账号信息
     * @Author: MeiTao
     * @Date: 2020/8/26 0026 15:45
     * @param accountTypeId 账号类型id
     * @return: com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity
     * @Since version-1.3.0
     */
    CateringMerchantLoginAccountEntity getLoginAccount(Long accountTypeId);

    /**
     * 方法描述 : 通过电话号码获取登陆用户
     * @Author: MeiTao
     * @Date: 2020/8/26 0026 15:45
     * @param phone 账号类型id
     * @return: com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity
     * @Since version-1.3.0
     */
    CateringMerchantLoginAccountEntity getLoginAccountByPhone(String phone);

    /**
     * 方法描述 : 通过手机号查询登陆账号信息
     *           查询门店、门店兼自提点、员工
     * @Author: MeiTao
     * @Date: 2020/10/14 0014 15:01
     * @param phone 请求参数
     * @return: com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity
     * @Since version-1.5.0
     */
    Result<CateringMerchantLoginAccountEntity> selectByPhone(String phone);

    /**
     * 方法描述 :
     * @Author: MeiTao
     * @Date: 2020/10/14 0014 15:39
     * @param accountTypeId 请求参数
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity>
     * @Since version-1.5.0
     */
    Result<CateringMerchantLoginAccountEntity> selectByAccountTypeId(Long accountTypeId);

    /**
     * describe: 根据员工id批量删除
     * @author: fql
     * @date: 2020/10/22 16:03
     * @param ids
     * @return: {@link}
     * @version 1.5.0
     **/
    void batchDelEmployee(List<Long> ids);

}
