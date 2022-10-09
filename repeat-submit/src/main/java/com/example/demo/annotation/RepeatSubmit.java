package com.example.demo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RepeatSubmit. 自定义注解防止表单重复提交
 *
 * @author Thou
 * @date 2022/9/24
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 判断重复提交时间间隔，默认 10，单位秒<br/>
     */
    int interval() default 10;
}
