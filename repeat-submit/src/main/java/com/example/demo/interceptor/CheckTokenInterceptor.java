package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * CheckTokenInterceptor.
 *
 * @author Thou
 * @date 2022/9/27
 */
public class CheckTokenInterceptor extends RepeatSubmitInterceptor {

    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, int interval) {
        // 获取请求参数中的 token
        String token = request.getParameter("token");
        // 获得 session 中的 token
        String sessionToken = String.valueOf(request.getSession().getAttribute("token"));
        // 比较二者
        if (Objects.nonNull(sessionToken)) {
            return !sessionToken.equals(token);
        }
        // 初次访问，session 中没有 token
        return true;
    }
}
