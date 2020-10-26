package com.meiyuan.catering.merchant.annotation.support;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.enums.base.TokenEnum;
import com.meiyuan.catering.core.exception.AppUnauthorizedException;
import com.meiyuan.catering.core.util.SpringContextUtils;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.AppJwtTokenDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.enums.LogBackTypeEnum;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * @Author MeiTao
 * @Date 2020/6/23 0023 10:28
 * @Description 简单描述 : 商户登录鉴权
 * @Since version-1.0.0
 */
@Slf4j
public class LoginMerchantHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	public static final String LOGIN_TOKEN_KEY = "X-Catering-Token";


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(MerchantTokenDTO.class)
				&& parameter.hasParameterAnnotation(LoginMerchant.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
			WebDataBinderFactory factory) throws Exception {
		LoginMerchant loginMerchant = parameter.getMethodAnnotation(LoginMerchant.class);
		String token = request.getHeader(LOGIN_TOKEN_KEY);
		if (StringUtils.isEmpty(token)) {
			log.error("登陆token未传" + JSONObject.toJSONString(loginMerchant));
			if (loginMerchant == null ||loginMerchant.required()){
				throw new AppUnauthorizedException();
			}
			return null;
		}

		//通过token获取店铺信息
		AppJwtTokenDTO merchantTokenDto = null;
		try {
			merchantTokenDto = TokenUtil.getFromToken(token, TokenEnum.MERCHANT_APP, AppJwtTokenDTO.class);
		} catch (Exception e) {
			log.error("token解析失败，token-" + JSONObject.toJSONString(loginMerchant));
			throw new AppUnauthorizedException("登陆过期请重新登录");
		}
		if (merchantTokenDto == null) {
			log.error("token解析失败，token-" + JSONObject.toJSONString(loginMerchant));
			throw new AppUnauthorizedException("登陆过期请重新登录");
		}
		MerchantUtils merchantUtils = SpringContextUtils.getBean(MerchantUtils.class);
		MerchantTokenDTO tokenDto = merchantUtils.getMerchantByToken(String.valueOf(merchantTokenDto.getAccountTypeId()));

		if (ObjectUtils.isEmpty(tokenDto)) {
			log.error("获取登录token失败，传入token ： " + merchantTokenDto);
			throw new AppUnauthorizedException("登陆过期请重新登录");
		}

		if (!ObjectUtils.isEmpty(tokenDto.getLogBackType())&&!Objects.equals(tokenDto.getLogBackType(),LogBackTypeEnum.NORMAL.getStatus())){
//			merchantUtils.removeMerAppToken(tokenDto.getAccountTypeId().toString());
			throw new AppUnauthorizedException(LogBackTypeEnum.parse(tokenDto.getLogBackType()).getDesc());
		}

		if (!Objects.equals(tokenDto.getToken(), token)) {
			throw new AppUnauthorizedException("账号在其他地方登录 请重新登录");
		}
		return tokenDto;
	}
}
