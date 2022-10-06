package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HasRole.<br>
 * 标注在方法上，标注访问当前方法需要的角色
 *
 * @author Thou
 * @date 2022/10/5
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRole {

    String[] value();
}
