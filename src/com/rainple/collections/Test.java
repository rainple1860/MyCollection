package com.rainple.collections;

import sun.applet.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Test {

    public static void main(String[] args) {
        //testHashMap();
        testArrayList();
    }

    public static void testArrayList(){
        MyArrayList<Integer> myArrayList = new MyArrayList();
        for (int i = 0; i < 100; i++) {
            myArrayList.add(i);
        }
        System.out.println(myArrayList);
        myArrayList.clear();
        System.out.println(myArrayList);
    }

    public static void testHashMap(){
        Map<String,String> myHasMap = new HashMap<>();
        int size = 10000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            String s = UUID.randomUUID().toString();
            myHasMap.put(s,s);
        }
        long end = System.currentTimeMillis();
        System.out.println("HashMap插入"+ size + " 条数据所需时间：" + (end - start) + " 毫秒");

//        MyHasMap<String,String> map = new MyHasMap<>();
//        start = System.currentTimeMillis();
//        for (int i = 0; i < size; i++) {
//            String s = UUID.randomUUID().toString();
//            map.put(s,s);
//        }
//        end = System.currentTimeMillis();
//        System.out.println("jdk自带的HashMap插入"+ size + " 条数据所需时间：" + (end - start) + " 毫秒");
    }

}
