package com.example.demo.ioc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * XmlBeanDefinitionReader. 解析 xml 配置文件中的 bean 标签，构建 beanDefinition
 *
 * @author Thou
 * @date 2022/9/22
 */
public class XmlBeanDefinitionReader {

    private final DefaultBeanFactory beanFactory;

    public XmlBeanDefinitionReader(DefaultBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 解析 xml 配置文件
     *
     * @param configPath xml 配置文件地址
     * @author Thou
     * @date 2022/9/22
     */
    public void loadBeanDefinition(String configPath) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(this.getClass().getClassLoader().getResourceAsStream(configPath));
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    // 1. 解析 beanDefinition
                    BeanDefinition beanDefinition = parseBeanDefinition((Element) beanNode);
                    // 2. 注册 beanDefinition
                    this.beanFactory.registerBeanDefinition(beanDefinition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析 beanDefinition
     *
     * @param element bean 节点
     * @return com.thou.spring.ioc.BeanDefinition
     * @author Thou
     * @date 2022/9/22
     */
    public BeanDefinition parseBeanDefinition(Element element) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(element.getAttribute("id"));
        beanDefinition.setClassName(element.getAttribute("class"));
        // 1. 判断 bean 作用域
        String scope = element.getAttribute("scope");
        if (!"".equals(scope)) {
            beanDefinition.setScope(scope);
            // 1.1 多例情况下默认懒加载
            if ("prototype".equals(scope)) {
                beanDefinition.setLazyInit(true);
            }
        }
        // 2. 判断是否设置了 lazy-init
        String lazyInit = element.getAttribute("lazy-init");
        if ("true".equals(lazyInit)) {
            beanDefinition.setLazyInit(true);
        }

        NodeList propertyNodeList = element.getElementsByTagName("property");
        for (int i = 0; i < propertyNodeList.getLength(); i++) {
            Node propertyNode = propertyNodeList.item(i);
            if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                // 3. 解析 property 标签，封装 Property 对象到 beanDefinition 中
                Property property = parseProperty((Element)propertyNode);
                beanDefinition.getPropertyList().add(property);
            }
        }
        return beanDefinition;
    }

    /**
     * 解析 property
     *
     * @param element property 节点
     * @return com.thou.spring.ioc.Property
     * @author Thou
     * @date 2022/9/22
     */
    private Property parseProperty(Element element) {
        Property property = new Property();
        property.setName(element.getAttribute("name"));
        property.setValue(element.getAttribute("value"));
        property.setRef(element.getAttribute("ref"));
        return property;
    }
}
