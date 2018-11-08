package com.rainple.collections;

/**
 *栈数据结构
 */
public class MyStack {

    //存放数据
    private Object[] data;
    //数据量
    private int size;
    //栈顶
    private int top;
    //默认栈大小
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    //最大容量
    private int maxCapacity;

    public MyStack(){
        data = new Object[DEFAULT_INITIAL_CAPACITY];
        top = -1;
        size = 0;
        maxCapacity = DEFAULT_INITIAL_CAPACITY;
    }

    public MyStack(int initialCapacity){
        data = new Object[initialCapacity];
        top = -1;
        size = 0;
        maxCapacity = initialCapacity;
    }

    /**
     * 向栈中放数据
     * @param obj
     * @return
     */
    public boolean push(Object obj){
        if (size >= maxCapacity) return false;
        data[++top] = obj;
        size++;
        return true;
    }

    /**
     * 从栈中弹出数据
     * @return
     */
    public Object pop(){
        if (size <= 0) return null;
        size--;
        return data[top--];
    }

    /**
     * 查看数据
     */
    public Object peek(){
        if (isEmpty()) return null;
        return data[top];
    }

    /**
     * 清空栈数据
     */
    public void clear(){
        while (top > -1){
            data[top--] = null;
        }
        size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

}
