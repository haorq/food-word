package com.meiyuan.catering.merchant.service.merchant;

import com.meiyuan.catering.allinpay.enums.service.member.VerificationCodeTypeEnums;
import com.meiyuan.catering.allinpay.model.result.member.ApplyBindBankCardResult;
import com.meiyuan.catering.core.enums.base.merchant.tl.TlAuditStatusEnum;
import com.meiyuan.catering.core.enums.base.merchant.tl.TlSignStatusEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.bank.BankCardDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.ShopNameAuthDTO;
import com.meiyuan.catering.merchant.enums.ShopAuditStepsEnum;
import com.meiyuan.catering.merchant.feign.ShopBankClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;
import com.meiyuan.catering.pay.dto.member.ApplyBindBankCardDTO;
import com.meiyuan.catering.pay.dto.member.BindBankCardDTO;
import com.meiyuan.catering.pay.service.MyMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @Author MeiTao
 * @Date 2020/9/30 0030 9:34
 * @Description 简单描述 : 店铺结算信息完善
 * @Since version-1.5.0
 */
@Service
@Slf4j
public class MerchantShopBankService {

    @Autowired
    ShopBankClient shopBankClient;
    @Autowired
    MyMemberService myMemberService;
    @Autowired
    MerchantUtils merchantUtils;

    /**
     * 方法描述 : app-门店结算信息实名认证
     * @Author: MeiTao
     * @Date: 2020/9/30 0030 9:39
     * @Since version-1.5.0
     */
    public Result<String> realNameAuth(ShopNameAuthDTO dto) {
        //校验请求参数是否正确
        this.checkBankInfo(dto);

        if (Objects.equals(dto.getAuditStatus(),ShopAuditStepsEnum.NAME_AUTH.getStatus())){
            Result<ShopBankInfoVo> shopBankInfoData = shopBankClient.getShopBankInfo(dto.getShopId());
            ShopBankInfoVo shopBankInfoVo = shopBankInfoData.getData();
            //手机号验证
            if (ObjectUtils.isEmpty(shopBankInfoVo)){
                log.error("门店扩展信息查询失败，门店id ： " + dto.getShopId());
                return Result.fail("门店实名认证失败");
            }

            if (!Objects.equals(shopBankInfoVo.getAuditStatus(), TlAuditStatusEnum.BIND_PHONE.getStatus())){
                try{
                    myMemberService.bindPhone(dto.getShopId(),dto.getPhone(),dto.getCode());
                    shopBankClient.bindPhone(dto.getShopId(),dto.getPhone());
                }catch (Exception e){
                    log.error("当前门店已实名认证， + shopID :" + dto.getShopId(),e);
                    return Result.fail(e.getMessage());
                }
            }

            //验证门店实名认证信息是否正确
            try{
                myMemberService.setRealName(dto.getShopId(), dto.getUserName(), dto.getIdCard());
            }catch (Exception e){
                log.error("当前门店已实名认证， + shopID :" + dto.getShopId(),e);
                return Result.fail(e.getMessage());
            }
        }
        return shopBankClient.addShopAuthInfo(dto);
    }

    public Result<String> getSignUrl(Long shopId) {
        String url = null;
        Result<ShopBankInfoVo> shopBankInfo = shopBankClient.getShopBankInfo(shopId);
        ShopBankInfoVo data = shopBankInfo.getData();
        if (ObjectUtils.isEmpty(data)){
            log.error("=============门店信息查询失败，门店id ：" + shopId);
            return Result.fail();
        }
        try {
            if (Objects.equals(data.getSignStatus(), TlSignStatusEnum.NOT_SIGN.getStatus())){
                url = myMemberService.signContract(shopId);
            }else {
                url = myMemberService.signContractQuery(shopId);
            }
        }catch (Exception e){
            log.error("获取门店签约url失败，：门店id ："+ shopId);
            return Result.fail(e.getMessage());
        }
        return Result.succ(url);
    }

    public Result<BankCardDTO> getSmsCode(ApplyBindBankCardDTO dto) {
        String cardNo = dto.getCardNo().replaceAll(" ", "");
        boolean b = BaseUtil.allNumber(cardNo);
        if (!b){
            return Result.fail("银行卡号必须是纯数字");
        }
        dto.setCardNo(cardNo);
        //获取用户通联注册真实信息
        Result<ShopBankInfoVo> data = shopBankClient.getShopBankInfo(Long.valueOf(dto.getBizUserId()));
        ShopBankInfoVo shopBankInfo = data.getData();
        if (ObjectUtils.isEmpty(shopBankInfo)){
            return Result.fail("获取用户真实身份信息失败");
        }
        dto.setName(shopBankInfo.getUserName());
        dto.setIdentityNo(shopBankInfo.getIdCard());
        //获取短信验证码 @TODO 异常处理
        ApplyBindBankCardResult applyBindBankCardResult = null;
        try{
            applyBindBankCardResult = myMemberService.applyBindBankCard(dto);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }

        BankCardDTO result = BaseUtil.objToObj(applyBindBankCardResult, BankCardDTO.class);
        result.setName(shopBankInfo.getUserName());
        result.setIdentityNo(shopBankInfo.getIdCard());
        result.setCardNo(dto.getCardNo());
        result.setPhone(dto.getPhone());
        result.setBankName(applyBindBankCardResult.getBankName());
        return Result.succ(result);
    }

    public Result bindBankCard(BankCardDTO dto) {
        String cardNo = dto.getCardNo().replaceAll(" ", "");
        boolean b = BaseUtil.allNumber(cardNo);
        if (!b){
            return Result.fail("银行卡号必须是纯数字");
        }
        dto.setCardNo(cardNo);
        BindBankCardDTO bindBankCard = BaseUtil.objToObj(dto, BindBankCardDTO.class);
        bindBankCard.setVerificationCode(dto.getCode());
        try{
            myMemberService.bindBankCard(bindBankCard);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
        shopBankClient.bindBankCard(dto);
        return Result.succ();
    }

    public Result<ShopBankInfoVo> getShopBankInfo(Long shopId) {
        return shopBankClient.getShopBankInfo(shopId);
    }

    /**
     * 方法描述 : 校验请求参数是否正确
     * @Author: MeiTao
     * @Date: 2020/9/30 0030 10:25
     * @param dto 请求参数
     * @return: void
     * @Since version-1.5.0
     */
    private void checkBankInfo(ShopNameAuthDTO dto) {
        if (ObjectUtils.isEmpty(dto.getShopId())){
            log.error("店铺结算信息添加参数传入错误 ： " + dto.toString());
            throw new CustomException("店铺id必传必填");
        }

        if (Objects.equals(dto.getAuditStatus(), ShopAuditStepsEnum.NAME_AUTH.getStatus())){
            if (ObjectUtils.isEmpty(dto.getUserName()) || ObjectUtils.isEmpty(dto.getIdCard())){
                log.error("店铺结算信息添加参数传入错误 ： " + dto.toString());
                throw new CustomException("姓名、身份证号必填");
            }
        }
    }

    /**
     * 方法描述 : 实名认证获取短信验证码
     * @Author: MeiTao
     * @Date: 2020/10/10 0010 17:45
     * @param shopId
     * @param phone 请求参数
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.dto.shop.bank.BankCardDTO>
     * @Since version-1.5.0
     */
    public Result getNameAuthSmsCode(Long shopId, String phone) {
        try{
            myMemberService.sendVerificationCode(shopId, phone, VerificationCodeTypeEnums.BIND);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
        return Result.succ();
    }

}
