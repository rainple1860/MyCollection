package com.rainple.collections;

import java.util.UUID;

public class Test {

    public static void main(String[] args) {
        testHashMap();
        //testArrayList();
    }

    public static void testArrayList(){
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        for (int i = 0; i < 100; i++) {
            myArrayList.add(i);
        }
        System.out.println(myArrayList);
        myArrayList.clear();
        System.out.println(myArrayList);
    }

    public static void testHashMap(){
        MyHashMap<String,String> myHashMap = new MyHashMap<>(1<<20,0.75f);
        int size = 1800000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            String s = UUID.randomUUID().toString();
            myHashMap.put(s,s);
        }
        long end = System.currentTimeMillis();
        System.out.println(myHashMap.entrySet());
        System.out.println("HashMap插入"+ size + " 条数据所需时间：" + (end - start) + " 毫秒");

    }

}
