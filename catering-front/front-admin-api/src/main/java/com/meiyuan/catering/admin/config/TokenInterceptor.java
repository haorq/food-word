package com.meiyuan.catering.admin.config;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.fegin.AdminClient;
import com.meiyuan.catering.admin.utils.AdminUtils;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.enums.base.TokenEnum;
import com.meiyuan.catering.core.exception.AdminUnauthorizedException;
import com.meiyuan.catering.core.util.token.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminClient adminClient;
    @Autowired
    private AdminUtils adminUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.debug("uri:{}", request.getRequestURI());

        String token = request.getHeader(TokenEnum.ADMIN.getToken());

        if (token == null) {
            throw new AdminUnauthorizedException("请登录");
        }

        CateringAdmin user;
        try {
            user = TokenUtil.getFromToken(token, TokenEnum.ADMIN, CateringAdmin.class);
        } catch (Exception e) {
            throw new AdminUnauthorizedException("无效token 或 登录信息过期 请重新登录");
        }
        if (user == null) {
            throw new AdminUnauthorizedException("无效token 或 登录信息过期 请重新登录");
        }

        Long userId = user.getId();

        String redisToken = adminUtils.getAdminToken(userId);
        if (redisToken == null) {
            throw new AdminUnauthorizedException("登录信息过期 请重新登录");
        }
        if (!token.equals(redisToken)) {
            throw new AdminUnauthorizedException("账号在其他地方登录 请重新登录");
        }

        CateringAdmin admin = adminClient.getById(userId).getData();

        log.debug("\nadmin:" + JSON.toJSONString(admin));
        if (admin == null || admin.getIsDel().equals(true)) {
            throw new AdminUnauthorizedException("账号可能已被删除,请联系管理员");
        } else if (admin.getStatus().equals(StatusEnum.ENABLE_NOT.getStatus())) {
            throw new AdminUnauthorizedException("您的账号已被禁用,请联系管理员");
        }

        request.setAttribute("info", admin);
        return true;
    }

}
