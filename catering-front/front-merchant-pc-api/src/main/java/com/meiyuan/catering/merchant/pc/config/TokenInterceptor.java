package com.meiyuan.catering.merchant.pc.config;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.enums.base.TokenEnum;
import com.meiyuan.catering.core.exception.AdminUnauthorizedException;
import com.meiyuan.catering.core.util.HttpContextUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.enums.LogBackTypeEnum;
import com.meiyuan.catering.merchant.pc.service.merchant.MerchantPcMerchantService;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author admin
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private MerchantUtils merchantUtils;

    @Resource
    private MerchantPcMerchantService pcMerchantService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.debug("uri:{}", request.getRequestURI());


        String token = request.getHeader(TokenEnum.MERCHANT.getToken());

        if (token == null) {
            throw new AdminUnauthorizedException(401,"请登录");
        }
        //获取传入token信息
        MerchantAccountDTO accountDto;
        try {
            accountDto = TokenUtil.getFromToken(token, TokenEnum.MERCHANT, MerchantAccountDTO.class);
        } catch (Exception e) {
            throw new AdminUnauthorizedException(401,"无效token 或 登录信息过期 请重新登录");
        }
        if (accountDto == null) {
            throw new AdminUnauthorizedException(401,"无效token 或 登录信息过期 请重新登录");
        }

        //获取缓存中token信息
        String redisToken = merchantUtils.getMerchantPcToken(accountDto.getAccountTypeId().toString());
        if (redisToken == null) {
            throw new AdminUnauthorizedException(401,"登录信息过期 请重新登录");
        }

        MerchantAccountDTO newMerchantAccount;
        try {
            newMerchantAccount = TokenUtil.getFromToken(redisToken, TokenEnum.MERCHANT, MerchantAccountDTO.class);
        } catch (Exception e) {
            merchantUtils.removeMerchantPcToken(accountDto.getAccountTypeId().toString());
            throw new AdminUnauthorizedException(401,"无效token 或 登录信息过期 请重新登录");
        }

        //判断退出登录状态
        if (!ObjectUtils.isEmpty(newMerchantAccount.getLogBackType())){
            //员工禁用状态不可登录
            if(Objects.equals(newMerchantAccount.getAccountType(), AccountTypeEnum.EMPLOYEE.getStatus())){
                if (!Objects.equals(newMerchantAccount.getLogBackType(),LogBackTypeEnum.NORMAL.getStatus())){
                    throw new AdminUnauthorizedException(401,LogBackTypeEnum.parse(newMerchantAccount.getLogBackType()).getDesc());
                }
            }
            //商户、店铺、店铺兼自提点禁用状态可以登录
            if(!Objects.equals(newMerchantAccount.getAccountType(), AccountTypeEnum.EMPLOYEE.getStatus())){
                boolean b = Objects.equals(newMerchantAccount.getLogBackType(),LogBackTypeEnum.NORMAL.getStatus()) ||
                        Objects.equals(newMerchantAccount.getLogBackType(),LogBackTypeEnum.DISABLE_ACCOUNT.getStatus());
                if (!b){
                    throw new AdminUnauthorizedException(401,LogBackTypeEnum.parse(newMerchantAccount.getLogBackType()).getDesc());
                }
            }
        }
        //传入token与缓存中token不同，无需清除缓存
        if (!Objects.equals(redisToken, token)) {
            throw new AdminUnauthorizedException(401,"账号在其他地方登录 请重新登录");
        }

        //获取账号信息
        Result<MerchantAccountDTO> data = pcMerchantService.getAccountInfo(accountDto);
        MerchantAccountDTO account = data.getData();

        if (ObjectUtils.isEmpty(account)) {
            merchantUtils.removeMerchantPcToken(accountDto.getAccountTypeId().toString());
            throw new AdminUnauthorizedException(401,"账号可能已被删除,请联系管理员");
        }

        String ip = HttpContextUtils.getRealIp();
        log.debug("\nphone:{},ip:{} accountInfo:{}", account.getPhone(), ip, JSONObject.toJSONString(account));

        request.setAttribute(TokenUtil.INFO, account);
        return true;
    }

}
