package com.example.demo.ioc;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * BeanDefinition. 用于描述 bean 的定义
 *
 * @author Thou
 * @date 2022/9/22
 */
@Data
public class BeanDefinition {

    /**
     * bean 的唯一标识符
     */
    private String beanName;
    /**
     * bean 对应类的全路径名
     */
    private String className;
    /**
     * bean 的范围，默认为单例
     */
    private String scope = "singleton";
    /**
     * 是否懒加载 bean，默认关闭
     */
    private boolean isLazyInit = false;
    /**
     * bean 中属性的集合
     */
    private List<Property> propertyList = new ArrayList<>();
}
