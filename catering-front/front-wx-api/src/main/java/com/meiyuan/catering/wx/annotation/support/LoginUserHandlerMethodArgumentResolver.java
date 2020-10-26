package com.meiyuan.catering.wx.annotation.support;

import com.meiyuan.catering.core.exception.UnauthorizedException;
import com.meiyuan.catering.core.util.SpringContextUtils;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.utils.WechatUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author admin
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String LOGIN_TOKEN_KEY = "X-Catering-Token";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserTokenDTO.class)
                && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
                                  WebDataBinderFactory factory) throws Exception {

        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            if (loginUser.required()) {
                throw new UnauthorizedException();
            }
            return null;
        }
        WechatUtils wechatUtils = SpringContextUtils.getBean(WechatUtils.class);
        UserTokenDTO user = wechatUtils.getUser(token);
        if (user == null) {
            if (loginUser.required()) {
                throw new UnauthorizedException();
            }
            return null;
        }
        String redisToken = wechatUtils.getToken(user.getUserIdReal());
        if (redisToken == null) {
            if (loginUser.required()) {
                throw new UnauthorizedException("登录信息过期 请重新登录");
            }
            return null;
        }
        if (!token.equals(redisToken)) {
            if (loginUser.required()) {
                throw new UnauthorizedException("账号在其他地方登录 请重新登录");
            }
            return null;
        }
        return user;

    }
}
