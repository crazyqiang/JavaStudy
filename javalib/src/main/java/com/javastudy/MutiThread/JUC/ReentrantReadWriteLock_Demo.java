package com.javastudy.MutiThread.JUC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock实现的读写锁例子
 * Created by mq on 2019-09-02 13:07
 * mqcoder90@gmail.com
 */
public class ReentrantReadWriteLock_Demo {
    private static final String THREAD_READ = "读线程";
    private static final String THREAD_WRITE = "写线程";

    public static void main(String[] args) {

        Resource resource = new Resource();
        //模拟三个线程去执行写操作
        for (int i = 0; i < 3; i++) {
            new Thread(new Task(resource), THREAD_WRITE + i).start();
        }
        //模拟10个线程去执行读操作
        for (int i = 0; i < 10; i++) {
            new Thread(new Task(resource), THREAD_READ + i).start();
        }
    }

    public static class Task implements Runnable {
        Resource resource;

        Task(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            String curThreadName = Thread.currentThread().getName();
            Person person = new Person(curThreadName, new Random().nextInt(100));
            if (curThreadName.startsWith(THREAD_READ)) {
                //读操作
                resource.get();
            } else if (Thread.currentThread().getName().startsWith(THREAD_WRITE)) {
                //写操作
                resource.put(person, person.rank);
            }
        }
    }

    public static class Resource<K extends Comparable, V> {
        TreeMap<K, V> rankMap = new TreeMap<>();
        final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        final Lock readLock = rwLock.readLock(); // 读取锁
        final Lock writeLock = rwLock.writeLock(); // 写入锁

        //写入值
        void put(K key, V value) {
            try {
                writeLock.lock();
                System.out.println(Thread.currentThread().getName() + "准备写入数据");
                Thread.sleep(new Random().nextInt(500));
                System.out.println(Thread.currentThread().getName() + "写入数据完毕:" + key.toString());
                rankMap.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        }

        //获取值
        public List<K> get() {
            try {
                readLock.lock();
                System.out.println(Thread.currentThread().getName() + "准备读取数据");
                Thread.sleep(new Random().nextInt(500));
                //treeMap中取出的数据是按rank从大到小排序的
                List<K> list = new ArrayList<>(rankMap.keySet());
                System.out.println(Thread.currentThread().getName() + "读取数据完毕:" + Arrays.toString(list.toArray()));
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
            return null;
        }

    }

    static class Person implements Comparable<Person> {
        public String name;//姓名
        public int rank;//得分

        Person(String name, int rank) {
            this.name = name;
            this.rank = rank;
        }

        @Override
        public int compareTo(Person person) {
            //分数少的在后面
            if (rank <= person.rank) {
                return 1;
            }
            return -1;
        }

        @Override
        public String toString() {
            return "name: " + name + ",rank: " + rank;
        }
    }

}
