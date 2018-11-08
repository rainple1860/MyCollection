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

        MyHashMap<String,Integer> myHashMap = new MyHashMap<>();
        for (int i = 0; i < 100; i++) {
            myHashMap.put(UUID.randomUUID().toString().substring(0,5),i);
        }
        System.out.println("碰撞率：" + myHashMap.boomRate());
        System.out.println("使用率：" + myHashMap.useRate());
        System.out.println(myHashMap);
        //System.out.println(myHashMap);
//        Map<String,String> myHashMap = new HashMap<>();
//        int size = 10000000;
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < size; i++) {
//            String s = UUID.randomUUID().toString();
//            myHashMap.put(s,s);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("HashMap插入"+ size + " 条数据所需时间：" + (end - start) + " 毫秒");

//        MyHashMap<String,String> map = new MyHashMap<>();
//        start = System.currentTimeMillis();
//        for (int i = 0; i < size; i++) {
//            String s = UUID.randomUUID().toString();
//            map.put(s,s);
//        }
//        end = System.currentTimeMillis();
//        System.out.println("jdk自带的HashMap插入"+ size + " 条数据所需时间：" + (end - start) + " 毫秒");
    }

}
