package com.javastudy.Annotation;

/**
 * Created by mq on 2018/3/23 下午3:29
 * mqcoder90@gmail.com
 */

public class AnnotationExample {

    /**
     * 注解模拟请求权限
     */
    @AnnotationInfo(value = {"android.permission.CALL_PHONE", "android.permission.CAMERA"}, requestCode = 10)
    public void requestPermission() {
        //其他逻辑
    }
}
