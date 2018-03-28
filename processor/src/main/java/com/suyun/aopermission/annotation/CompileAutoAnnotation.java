package com.suyun.aopermission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mq on 2018/3/27 下午6:31
 * mqcoder90@gmail.com
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface CompileAutoAnnotation {

    int value() default 0;
}
