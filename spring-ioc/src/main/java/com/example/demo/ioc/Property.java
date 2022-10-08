package com.example.demo.ioc;

import lombok.Data;

/**
 * Property. 用于封装 bean 中的属性
 *
 * @author Thou
 * @date 2022/9/22
 */
@Data
public class Property {

    /**
     * 属性名
     */
    private String name;
    /**
     * 属性值
     */
    private String value;
    /**
     * 属性值引用
     */
    private String ref;
}
