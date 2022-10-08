package com.example.demo.ioc;

/**
 * ClassPathXmlApplicationContext.
 *
 * @author Thou
 * @date 2022/9/22
 */
public class ClassPathXmlApplicationContext extends DefaultBeanFactory{

    public ClassPathXmlApplicationContext(String configPath) {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(this);
        // 1. 加载并解析 beanDefinition
        xmlBeanDefinitionReader.loadBeanDefinition(configPath);
        // 2. 预先初始化单例 bean
        this.preInstanceSingleton();
    }
}
