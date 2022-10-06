package com.example.demo.util;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AuthorityCacheManager.<br>
 * 缓存登录用户所拥有的角色.
 *
 * @author Thou
 * @date 2022/10/6
 */
@Slf4j
@Component
public class AuthorityCacheManager implements ApplicationRunner {

    /**
     * 接口 URI 与角色的对应关系
     */
    public static final Map<String, Set<String>> URI_ROLE_MAP = new HashMap<>(8);
    /**
     * 用户和角色之间的对应关系
     */
    public static final Map<String, Set<String>> USER_ROLE_MAP = new HashMap<>(8);

    // 初始化用户角色，这一步可以在用户登录时执行，在用户退出登录时执行清除操作
    static {
        USER_ROLE_MAP.put("zhangsan", Sets.newHashSet("admin"));
        USER_ROLE_MAP.put("lisi", Sets.newHashSet("user"));
    }

    /**
     * 项目启动后读取 authority-config.properties 配置文件，缓存 uri 对应的角色关系
     * @param args args
     * @throws Exception Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Properties properties = new Properties();
        properties.load(AuthorityCacheManager.class
                .getClassLoader()
                .getResourceAsStream("authority-config.properties"));
        properties.forEach((k, v) -> {
            String[] values = ((String)v).split(",");
            Set<String> valuesSet = Arrays.stream(values).collect(Collectors.toSet());
            valuesSet.removeIf(""::equals);
            URI_ROLE_MAP.put((String)k, valuesSet);
        });
        log.info("URI_ROLE_MAP=" + URI_ROLE_MAP);
    }

    public static void remove(String key) {
        USER_ROLE_MAP.keySet().removeIf(s -> s.equals(key));
    }
}
