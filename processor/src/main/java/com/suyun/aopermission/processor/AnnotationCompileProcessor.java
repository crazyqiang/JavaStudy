package com.suyun.aopermission.processor;

import com.suyun.aopermission.annotation.CompileAnnotation;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created by mq on 2018/3/23 下午3:31
 * mqcoder90@gmail.com
 */

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.suyun.aopermission.annotation.CompileAnnotation")
public class AnnotationCompileProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        //processingEnvironment提供各种工具类  如Elements Filer Types SourceVersion等
        super.init(processingEnvironment);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    /**
     * 扫描 评估和处理注解代码  生成Java代码
     *
     * @param annotations      注解类型
     * @param roundEnvironment 有关当前和以前的信息环境 查询出包含特定注解的被注解元素
     * @return 返回true 表示注解已声明 后续Processor不会再处理  false表示后续Processor会处理他们
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        messager.printMessage(Diagnostic.Kind.NOTE, "----------start----------");

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element.getKind() != ElementKind.FIELD) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Only FIELD can be annotated with AnnotationInfo");
                    return true;
                }
                //获取注解
                CompileAnnotation annotationInfo = element.getAnnotation(CompileAnnotation.class);
                //获取注解中的值
                int value = annotationInfo.value();
                messager.printMessage(Diagnostic.Kind.NOTE, "value: " + value);
            }
        }
        return true;
    }

}
