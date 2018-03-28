package com.javastudy.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mq on 2018/03/23 下午11:41
 * mqcoder90@gmail.com
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface AnnotationInfo {

    String[] value();

    int requestCode() default 0;
}
