package com.example.demo.interceptor;

import com.example.demo.constant.StatusCodeMsgEnum;
import com.example.demo.exception.AppException;
import com.example.demo.util.AuthorityCacheManager;
import com.google.common.collect.Sets;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;

/**
 * AuthorityInterceptor.
 *
 * @author Thou
 * @date 2022/10/6
 */
public class AuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取用户正在访问的 uri
        String uri = request.getRequestURI();
        // 根据 uri 获取对应的权限
        Set<String> uriRoles = AuthorityCacheManager.URI_ROLE_MAP.get(uri);
        // 没有对应的缓存，或权限为空，代表允许匿名访问，直接放行
        if (Objects.isNull(uriRoles) || uriRoles.isEmpty()) {
            return true;
        }
        // 获取当前登录用户的用户名，用户已经登录，正常流程应该从 session 中获得登录用户信息
        String username = request.getParameter("username");
        // 根据 username 获得对应的权限
        Set<String> userRoles = AuthorityCacheManager.USER_ROLE_MAP.get(username);

        // 用户没有任何权限，或交集为空
        if (CollectionUtils.isEmpty(userRoles) || Sets.intersection(uriRoles, userRoles).isEmpty()) {
            throw new AppException(StatusCodeMsgEnum.USER_ACCESS_NOT_ALLOWED);
        }
        return true;
    }
}
