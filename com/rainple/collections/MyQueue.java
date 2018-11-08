package com.rainple.collections;

/**
 * 队列
 */
public class MyQueue {

    /**
     * 队列管道，当管道中存放的数据大于管道的长度时将不会再push数据，直至从管道中pop数据后
     */
    private Object[] chanel;
    //队列的头部，获取数据时总是从头部获取
    private int head;
    //队列尾部，push数据时总是从尾部添加
    private int tail;
    //管道中已经存放的数据量
    private int size;
    //管道中能存放数据的最大容量
    private int maxCapacity;
    //最大下标
    private int maxIndex;

    public MyQueue(int initialCapacity){
        chanel = new Object[initialCapacity];
        maxCapacity = initialCapacity;
        maxIndex = initialCapacity - 1;
        head = tail = -1;
        size = 0;
    }
    public MyQueue(){
        chanel = new Object[16];
        maxCapacity = 16;
        head = tail = -1;
        size = 0;
        maxIndex = 15;
    }

    /**
     * 往管道中添加数据
     * @param object
     */
    public void push(Object object){
        if (size >= maxCapacity){
            return;
        }
        if (++tail > maxIndex){
            tail = 0;
        }
        chanel[tail] = object;
        size++;
    }

    /**
     * 从管道中弹出数据
     * @return
     */
    public Object pop(){
        if (size <= 0){
            return null;
        }
        if (++head > maxIndex){
            head = 0;
        }
        size--;
        Object old = chanel[head];
        chanel[head] = null;
        return old;
    }

    /**
     * 查看第一个数据
     * @return
     */
    public Object peek(){
        return chanel[head];
    }

    /**
     * 管道中存储的数据量
     * @return
     */
    public int size(){
        return size;
    }

    /**
     * 管道是否为空
     * @return
     */
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * 清空管道
     */
    public void clear(){
        for (int i = 0; i < chanel.length; i++) {
            chanel[i] = null;
        }
        tail = head = -1;
        size = 0;
    }

    @Override
    public String toString() {
        if (size <= 0) return "{}";
        StringBuilder builder = new StringBuilder(size + 8);
        builder.append("{");
        int h = head;
        int count = 0;
        while (count < size){
            if (++h > maxIndex) h = 0;
            builder.append(chanel[h]);
            builder.append(", ");
            count++;
        }
        return builder.substring(0,builder.length()-2) + "}";
    }
}
