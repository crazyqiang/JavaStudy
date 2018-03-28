package com.suyun.aopermission.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.suyun.aopermission.annotation.CompileAutoAnnotation;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by mq on 2018/3/27 下午3:05
 * mqcoder90@gmail.com
 * 引入Google库 在注解处理器中使用注解 将注解处理器注册到javac中
 */
@AutoService(Processor.class)
public class GoogleProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //typeUtils处理TypeMirror的工具类
        typeUtils = processingEnvironment.getTypeUtils();
        //elementUtils处理Element的工具类
        elementUtils = processingEnvironment.getElementUtils();
        //使用Filer来创建.class文件
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations == null || annotations.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Annotation set is empty");
            return true;
        }
        for (TypeElement annotation : annotations) {
            //getElementsAnnotatedWith()返回所有被@AutoProAnnotation注解的Element列表（Element可以是类 方法 变量等）
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
            if (elements.isEmpty()) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Annotation set is empty");
                return true;
            }
            for (Element element : elements) {
                //检查注解是否作用在Class上
                if (element.getKind() != ElementKind.CLASS) {
                    error(element, "Only classes can be annotated with AutoProAnnotation ");
                    return true;
                }
                CompileAutoAnnotation proAnnotation = element.getAnnotation(CompileAutoAnnotation.class);

//                //创建method方法类
                MethodSpec methodSpec = MethodSpec.methodBuilder("getValue")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(int.class)
                        .addStatement("return " + proAnnotation.value())
                        .build();
                //创建.class类
                TypeSpec typeSpec = TypeSpec.classBuilder("autoGenerate")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(methodSpec)
                        .build();

                String packageName = processingEnv.getElementUtils().
                        getPackageOf(element).getQualifiedName().toString();
                try {
                    JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                            .addFileComment("this is auto generated")
                            .build();
                    javaFile.writeTo(filer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add(CompileAutoAnnotation.class.getCanonicalName());
        return hashSet;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
