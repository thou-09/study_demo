package com.example.demo.util;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AuthorityCacheManager.<br>
 * 缓存登录用户所拥有的角色.
 *
 * @author Thou
 * @date 2022/10/5
 */
public class AuthorityCacheManager {

    /**
     * 保存用户和具有的角色之间的对应关系
     */
    public static final Map<String, Set<String>> USER_ROLE_MAP = new HashMap<>(8);

    // 初始化用户角色，这一步可以在用户登录时执行，在用户退出登录时执行清除操作
    static {
        Set<String> roelSet3 = Sets.newHashSet("admin", "user");
        USER_ROLE_MAP.put("zhangsan", roelSet3);
        Set<String> roelSet4 = Sets.newHashSet("user");
        USER_ROLE_MAP.put("lisi", roelSet4);
    }

    public static void remove(String key) {
        USER_ROLE_MAP.keySet().removeIf(s -> s.equals(key));
    }
}
