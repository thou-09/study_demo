package com.example.demo.aop;

import com.example.demo.annotation.HasRole;
import com.example.demo.util.AuthorityCacheManager;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AuthorityAopConfig.
 *
 * @author Thou
 * @date 2022/10/5
 */
@Component
@Aspect
public class AuthorityAopConfig {

    @Pointcut("@annotation(com.example.demo.annotation.HasRole)")
    public void pointcut() {}

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        // 这里仅从请求路径中获取用户名
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String username = request.getParameter("username");

        // 一般情况下需要从 session 中获取当前登录用户信息
        // request.getSession().getAttribute("loginUser");.

        // 获得当前用户的角色集合
        Set<String> userRoles = AuthorityCacheManager.USER_ROLE_MAP.get(username);

        // 获取当前请求方法上的注解 HasRole
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        HasRole hr = method.getDeclaredAnnotation(HasRole.class);
        // 获得接口所需权限和用户拥有权限之间的交集
        Set<String> uriRoles = Arrays.stream(hr.value()).collect(Collectors.toSet());

        // 用户没有任何权限，或交集为空，代表用户没有访问权限
        if (CollectionUtils.isEmpty(userRoles) || Sets.intersection(userRoles, uriRoles).isEmpty()) {
            throw new RuntimeException("用户没有访问权限");
        }
    }
}
