package com.javastudy.Collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * List在遍历时修改数据(添加、删除)
 *
 * 遍历删除：如果使用ArrayList/LinkedList，必须使用Iterator来遍历并调用Iterator.remove来删除，否则会抛出
 * java.util.ConcurrentModificationException异常；如果使用CopyOnWriteArrayList，可以直接操作删除，因其是读写分离的。
 *
 * 遍历添加：需要使用CopyOnWriteArrayList来遍历添加，如果使用ArrayList/LinkedList添加会直接抛出
 * java.util.ConcurrentModificationException异常
 */
public class List_Traversal_Modify {

    private static List<String> list = new ArrayList<>();

    public static void main(String[] args) {
        Traversal();
        TraversalDel();
        TraversalAdd();
    }

    /**
     * 遍历
     */
    private static void Traversal() {
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");
        System.out.println("**********遍历**********");
        //for循环遍历
        for (int i = 0; i < list.size(); i++) {
            System.out.println("for循环遍历：" + list.get(i));
        }
        System.out.println("----------------------");
        //foreach遍历
        for (String str : list) {
            System.out.println("foreach遍历：" + str);
        }
        System.out.println("----------------------");
        //Iterator遍历
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String str = (String) iterator.next();
            System.out.println("Iterator遍历：" + str);
        }
    }

    /**
     * 遍历时删除
     */
    private static void TraversalDel() {
        System.out.println("**********遍历删除**********");
        //1、使用ArrayList 的Iterator方式遍历并操作删除
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String str = (String) iterator.next();
            if (str.equals("bbb")) {
                iterator.remove();
            }
        }
        System.out.println("Iterator方式遍历并删除：" + Arrays.toString(list.toArray()));
        //执行结果： [aaa, ccc, ddd]

        System.out.println("----------------------");

        //2、使用CopyOnWriteArrayList方式进行遍历并删除第二条数据(读写分离)
        List<String> list1 = new CopyOnWriteArrayList<>();
        list1.add("aaa");
        list1.add("bbb");
        list1.add("ccc");
        list1.add("ddd");

        for (String str : list1) {
            if (str.equals("bbb")) {
                list1.remove("bbb");
                break;
            }
        }
        System.out.println("CopyOnWriteArrayList使用for循环遍历并删除：" + Arrays.toString(list1.toArray()));
        //输出结果： [aaa, ccc, ddd]
    }

    /**
     * 遍历时添加
     */
    private static void TraversalAdd() {
        System.out.println("**********遍历添加**********");
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");
        for (String str : list) {
            list.add("eee");
            System.out.println("循环中，值是 ：" + str);
        }

        System.out.println("CopyOnWriteArrayList 遍历并添加：" + Arrays.toString(list.toArray()));
        //输出：[aaa, bbb, ccc, ddd, eee, eee, eee, eee]
    }

}
