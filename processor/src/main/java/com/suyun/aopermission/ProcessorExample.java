package com.suyun.aopermission;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * Created by mq on 2018/3/28 下午3:35
 * mqcoder90@gmail.com
 */

public class ProcessorExample extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        //processingEnvironment提供各种工具类  如Elements Filer Types SourceVersion等
        super.init(processingEnvironment);
    }

    /**
     * 扫描 评估和处理注解代码  生成Java代码
     *
     * @param set      注解类型
     * @param roundEnvironment 有关当前和以前的信息环境 查询出包含特定注解的被注解元素
     * @return 返回true 表示注解已声明 后续Processor不会再处理  false表示后续Processor会处理他们
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }
}
