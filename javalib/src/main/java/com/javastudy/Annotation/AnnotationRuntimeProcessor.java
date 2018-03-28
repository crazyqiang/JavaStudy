package com.javastudy.Annotation;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by mq on 2018/3/23 下午3:24
 * mqcoder90@gmail.com
 * 通过反射来解析注解
 */

public class AnnotationRuntimeProcessor {

    public static void main(String[] args) {
        try {
            //获取AnnotationExample的Class对象
            Class<?> cls = Class.forName("com.javastudy.Annotation.AnnotationExample");
            //获取AnnotationExample类中的方法
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                //过滤不含自定义注解AnnotationInfo的方法
                boolean isHasAnnotation = method.isAnnotationPresent(AnnotationInfo.class);
                if (isHasAnnotation) {
                    method.setAccessible(true);
                    //获取方法上的注解
                    AnnotationInfo aInfo = method.getAnnotation(AnnotationInfo.class);
                    if (aInfo == null) return;
                    //解析注解上对应的信息
                    String[] permissions = aInfo.value();
                    System.out.println("value: " + Arrays.toString(permissions));

                    int requestCode = aInfo.requestCode();
                    System.out.println("requestCode: " + requestCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
