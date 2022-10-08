package com.example.demo.ioc;

/**
 * BeanFactory.
 *
 * @author Thou
 * @date 2022/9/22
 */
public interface BeanFactory {

    /**
     * 获得 bean
     *
     * @param beanName bean 的唯一标识符
     * @return java.lang.Object
     * @author Thou
     * @date 2022/9/22
     */
    Object getBean(String beanName);

    /**
     * 获得 bean
     *
     * @param beanName bean 的唯一标识符
     * @param clazz bean 的类对象
     * @return T
     * @author Thou
     * @date 2022/9/22
     */
    <T> T getBean(String beanName, Class<T> clazz);
}
