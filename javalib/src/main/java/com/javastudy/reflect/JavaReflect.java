package com.javastudy.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MQ on 2017/8/7.
 * https://www.zhihu.com/question/24304289
 * http://blog.csdn.net/sinat_38259539/article/details/71799078
 */

public class JavaReflect {

    public static void main(String[] args) {
        try {
            //第一种方式
            ReflectBean bean = new ReflectBean();
            Class rCls = bean.getClass();
            System.out.println(rCls.getName());

            //第二种方式
            Class rCls2 = ReflectBean.class;
            System.out.println(rCls2 == rCls);

            //第三种方式
            Class<?> rCls3 = Class.forName("com.javastudy.reflect.ReflectBean");
            System.out.println(rCls3 == rCls2);

            //1、通过反射获取构造函数
            System.out.println("*****************通过反射获取所有public构造函数*****************");
            Constructor<?>[] cons = rCls3.getConstructors();
            for (Constructor con : cons) {
                System.out.println(con);
            }

            System.out.println("------------------通过反射获取所有public、private、default、protected构造函数------------------");
            cons = rCls3.getDeclaredConstructors();
            for (Constructor con : cons) {
                System.out.println(con);
            }

            System.out.println("------------------通过反射获取public、无参数的构造函数------------------");
            Constructor<?> con = rCls3.getConstructor();
            System.out.println(con);
            //初始化构造函数 相当于 ReflectBean bean=new ReflectBean();
            Object obj = con.newInstance();

            System.out.println("------------------通过反射获取private构造函数并调用------------------");
            Constructor<?> pCon = rCls3.getDeclaredConstructor(String.class);
            //忽略掉访问修饰符
            pCon.setAccessible(true);
            Object pObj = pCon.newInstance("小强");
            System.out.println(((ReflectBean) pObj).getName());

            //2、通过反射获取成员变量
            System.out.println("*****************通过反射获取public成员变量*****************");
            Field[] fields = rCls3.getFields();
            for (Field field : fields) {
                System.out.println(field);
            }

            System.out.println("------------------通过反射获取所有成员变量------------------");
            fields = rCls3.getDeclaredFields();
            for (Field field : fields) {
                System.out.println(field);
            }

            System.out.println("------------------通过反射获取public的某个成员变量------------------");
            Field field = rCls3.getField("sex");
            System.out.println(field);
            Object objSex = rCls3.getConstructor().newInstance();
            field.set(objSex, "man");
            ReflectBean reflectBean = (ReflectBean) objSex;
            System.out.println("性别：" + reflectBean.sex);

            System.out.println("------------------通过反射获取某一个成员变量------------------");
            Field fName = rCls3.getDeclaredField("name");
            fName.setAccessible(true);
            Object objName = rCls3.getDeclaredConstructor().newInstance();
            fName.set(objName, "小马快跑");
            System.out.println("姓名：" + ((ReflectBean) objName).getName());

            //3、通过反射获取方法
            System.out.println("*****************通过反射获取public成员方法*****************");
            Method[] methods = rCls3.getMethods();
            for (Method method : methods) {
                System.out.println(method);
            }

            System.out.println("------------------通过反射获取所有成员方法------------------");
            methods = rCls3.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println(method);
            }

            System.out.println("------------------通过反射获取某一个public成员方法------------------");
            Method method = rCls3.getMethod("setName", String.class);
            Object objMethod = rCls3.getConstructor().newInstance();
            method.invoke(objMethod, "草丛盖伦");
            System.out.println(((ReflectBean) objMethod).getName());

            System.out.println("------------------通过反射获取某一个成员方法------------------");
            method = rCls3.getDeclaredMethod("setPrice", double.class);
            method.setAccessible(true);
            Object objPrice = rCls3.getConstructor().newInstance();
            method.invoke(objPrice, 10.0);
            System.out.println(((ReflectBean) objPrice).getPrice());

            System.out.println("*****************反射用途：越过泛型检查*****************");
            //用途之一：通过反射越过泛型检查
            //注：泛型用于编译期间，在运行期间泛型会被擦除，所以可以通过反射来越过泛型检查
            List<String> strList = new ArrayList<>();
            strList.add("LOL");
            strList.add("王者农药");
            //得到strList的字节码对象
            Class<?> objList = strList.getClass();
            //反射拿到add方法
            Method mAdd = objList.getDeclaredMethod("add", Object.class);
            mAdd.invoke(strList, 10);
            //遍历集合
            for (Object objStr : strList) {
                System.out.println(objStr);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
