package com.suyun.aopermission; //PackageElement

/**
 * Created by mq on 2018/3/27 下午6:54
 * mqcoder90@gmail.com
 */

public class ExampleClass { //TypeElement
    //Element 代表的是源代码 TypeElement代表的是源代码中的类型元素，例如类。然而，TypeElement
    //并不包含类本身的信息。你可以从TypeElement中获取类的名字，但是你获取不到类的信息，例如它的父类。
    //这种信息需要通过TypeMirror获取，通过调用elements.asType()获取元素的TypeMirror.

    private int name; //VariableElement

    public ExampleClass() {
    } //ExecutableElement

    public void setName(//ExecutableElement
                        int name//VariableElement
    ) {
        this.name = name;
    }
}
