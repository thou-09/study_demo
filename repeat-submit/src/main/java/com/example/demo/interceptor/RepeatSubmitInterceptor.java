package com.example.demo.interceptor;

import com.example.demo.annotation.RepeatSubmit;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * RepeatSubmitInterceptor. 防止重复提交拦截器
 *
 * @author Thou
 * @date 2022/9/24
 */
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit anno = method.getAnnotation(RepeatSubmit.class);
            if (Objects.nonNull(anno)) {
                if (this.isRepeatSubmit(request, anno.interval())) {
                    response.setContentType("text/html;charset=utf-8");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().println("不允许重复提交，请稍后再试");
                    return false;
                }
                request.getSession().removeAttribute("token");
            }
        }
        return true;
    }

    /**
     * 判断是否重复提交
     *
     * @param request http 请求
     * @param interval 间隔时间
     * @return boolean
     * @author Thou
     * @date 2022/9/24
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, int interval);
}
