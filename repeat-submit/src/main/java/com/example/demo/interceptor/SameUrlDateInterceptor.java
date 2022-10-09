package com.example.demo.interceptor;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SameUrlDateInterceptor.
 *
 * @author Thou
 * @date 2022/9/26
 */
public class SameUrlDateInterceptor extends RepeatSubmitInterceptor {

    private final String REPEAT_PARAMS = "repeatParams";
    private final String REPEAT_TIME = "repeatTime";
    private final String SESSION_REPEAT_KEY = "repeatData";
    private int interval;

    @Override
    @SuppressWarnings("unchecked")
    public boolean isRepeatSubmit(HttpServletRequest request, int interval) {
        this.interval = interval;

        // 获得当前请求参数
        String nowParam = JSONObject.toJSONString(request.getParameterMap());
        Map<String, Object> nowDataMap = new HashMap<>(4);
        nowDataMap.put(REPEAT_PARAMS, nowParam);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // 获得请求 uri
        String uri = request.getRequestURI();
        // 获得 session 中的 repeatData
        HttpSession session = request.getSession();
        Object sessionObj = session.getAttribute(SESSION_REPEAT_KEY);
        // repeatData 如果不为空
        if (Objects.nonNull(sessionObj)) {
            Map<String, Object> sessionMap = (Map<String, Object>)sessionObj;
            // 当前 uri 已经被访问了一次
            if (sessionMap.containsKey(uri)) {
                // 两次访问数据是否一样，或访问时间间隔是否少于规定
                Map<String, Object> preDataMap = (Map<String, Object>)sessionMap.get(uri);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap)) {
                    return true;
                }
            }
        }
        // repeatData 为空
        Map<String, Object> sessionMap = new HashMap<>(1);
        // 将本次访问 uri 和数据放入 session中
        sessionMap.put(uri, nowDataMap);
        session.setAttribute(SESSION_REPEAT_KEY, sessionMap);
        return false;
    }

    /**
     * 判断参数是否相同
     *
     * @param now nowDataMap
     * @param pre preDataMap
     * @return boolean
     * @author Thou
     * @date 2022/9/24
     */
    private boolean compareParams(Map<String, Object> now, Map<String, Object> pre) {
        String np = (String)now.get(REPEAT_PARAMS);
        String pp = (String)pre.get(REPEAT_PARAMS);
        return np.equals(pp);
    }

    /**
     * 判断时间是否相同
     *
     * @param now nowDataMap
     * @param pre preDataMap
     * @return boolean
     * @author Thou
     * @date 2022/9/24
     */
    private boolean compareTime(Map<String, Object> now, Map<String, Object> pre) {
        Long nt = (Long)now.get(REPEAT_TIME);
        Long pt = (Long)pre.get(REPEAT_TIME);
        if (interval <= 0) {
            interval = 10;
        }
        return (nt - pt) <= (this.interval * 1000L);
    }
}
