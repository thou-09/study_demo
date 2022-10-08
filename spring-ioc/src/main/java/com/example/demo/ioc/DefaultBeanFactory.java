package com.example.demo.ioc;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultBeanFactory. beanFactory 的默认实现类
 *
 * @author Thou
 * @date 2022/9/22
 */
public class DefaultBeanFactory implements BeanFactory {

    /**
     * 用于存放 BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(8);
    /**
     * 用于存放单例的 bean
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(8);

    /**
     * 注册 beanDefinition 到容器中
     *
     * @param beanDefinition bean 定义
     * @author Thou
     * @date 2022/9/22
     */
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
    }

    /**
     * 预先初始化非懒加载的单例 bean 对象
     *
     * @author Thou
     * @date 2022/9/22
     */
    public void preInstanceSingleton() {
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            // 不是懒加载，则初始化，多例和单例懒加载都为懒加载 isLazyInit = true
            // 只有单例非懒加载对象才会被初始化
            boolean isLazyInit = beanDefinition.isLazyInit();
            if (!isLazyInit) {
                getBean(beanName);
            }
        });
    }

    /**
     * 创建 bean 对象
     *
     * @param beanDefinition bean 定义
     * @return java.lang.Object
     * @author Thou
     * @date 2022/9/22
     */
    public Object createBean(BeanDefinition beanDefinition) {
        // 1. 获得类对象
        String className = beanDefinition.getClassName();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到类：" + e.getMessage());
        }

        // 2. 创建对象
        Object obj;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("创建对象失败：" + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("非法访问：" + e.getMessage());
        }

        // 3. 依赖注入
        List<Property> propertyList = beanDefinition.getPropertyList();
        for (Property property : propertyList) {
            String name = property.getName();
            String value = property.getValue();
            String ref = property.getRef();
            if (!"".equals(name)) {
                if (!"".equals(value) && !"".equals(ref)) {
                    throw new RuntimeException("value 和 ref 只允许同时存在一个");
                }
                // 3.1 value 注入
                if (!"".equals(value)) {
                    Map<String, String> params = new HashMap<>(8);
                    params.put(name, value);
                    try {
                        BeanUtils.populate(obj, params);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("非法访问：" + e.getMessage());
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException("调用目标对象失败：" + e.getMessage());
                    }
                }
                // 3.2 ref 注入
                if (!"".equals(ref)) {
                    try {
                        BeanUtils.setProperty(obj, name, getBean(ref));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("非法访问：" + e.getMessage());
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException("调用目标对象失败：" + e.getMessage());
                    }
                }
            }
        }
        return obj;
    }

    @Override
    public Object getBean(String beanName) {
        // 1. 先从 singletonObjects 中获取 bean
        Object singletonObj = singletonObjects.get(beanName);
        if (null != singletonObj) {
            return singletonObj;
        }

        // 2. 单例集合获取不到，属于懒加载情况，再从定义集合中获取 bean 的定义
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        Assert.notNull(beanDefinition, "不存在" + beanName + "对象");

        // 3. 判断 bean 的范围
        String scope = beanDefinition.getScope();
        if ("singleton".equals(scope)) {
            // 3.1 单例
            Object singletonObject = createBean(beanDefinition);
            singletonObjects.put(beanName, singletonObject);
            return singletonObject;
        } else {
            // 3.2 多例
            return createBean(beanDefinition);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        return (T)getBean(beanName);
    }
}
