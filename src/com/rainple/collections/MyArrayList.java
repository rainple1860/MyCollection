package com.rainple.collections;

public class MyArrayList<E> {

    private Object[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private int capacity;
    private static final int MAX_CAPACITY = 1 << 30;

    public MyArrayList(){
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
        size = 0;
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    public MyArrayList(int initialCapacity){
        elements = new Object[initialCapacity];
        capacity = initialCapacity;
        size = 0;
    }

    public void add(E e){
        ensureNewCapacity(size+1);
        elements[size++] = e;
    }

    public void add(int index,E e){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException(index+"");
        }
        ensureNewCapacity(size+1);
        System.arraycopy(elements,index,elements,index+1,size - index);
        elements[index] = e;
        size++;
    }

    public void addAll(MyArrayList arrayList){
        if (arrayList == null)
            return;
        int len = arrayList.size();
        ensureNewCapacity(size+len);
        System.arraycopy(arrayList.elements,0,elements,size,len);
        size += len;
    }

    public void addAll(Object[] arrayList){
        if (arrayList == null)
            return;
        int len = arrayList.length;
        ensureNewCapacity(size+len);
        System.arraycopy(arrayList,0,elements,size,len);
        size += len;
    }

    public void set(int index,E e){
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("index: " + index);
        elements[index] = e;
    }

    public E get(int index){
        if (index < 0 || index >= size) return null;
        return (E) elements[index];
    }

    public E remove(int index){
        if (index < 0 || index >= size){
            throw new ArrayIndexOutOfBoundsException(index);
        }
        elements[--size] = null;
        System.arraycopy(elements,index + 1,elements,index,size - index);
        return (E) elements[index];
    }

    public E remove(E e){
        if (e == null || size == 0) throw new NullPointerException("arrayList is empty");
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == e || e.equals(elements[i])) {
                System.arraycopy(elements,i+1,elements,i,size - i);
                elements[--size] = null;
                return (E) elements[i];
            }
        }
        return null;
    }

    public MyArrayList<E> subList(int begin,int end){
        if (begin < 0)
            throw new IndexOutOfBoundsException("begin: " + begin);
        if (end > size)
            throw new IndexOutOfBoundsException("end: " + end);
        if (end < begin)
            throw new ArithmeticException("end <= begin");
        if (begin == end) return null;
        Object[] newList = new Object[end - begin];
        MyArrayList<E> newArray = new MyArrayList<>(end - begin);
        System.arraycopy(elements,begin,newList,0,end - begin);
        newArray.elements = newList;
        newArray.size = end - begin;
        return newArray;
    }

    public int indexOf(Object obj){
        for (int i = 0; i < size; i++) {
            if (elements[i] == obj || elements[i].equals(obj))
                return i;
        }
        return -1;
    }

    public int lastIndexOf(Object obj){
        for (int i = size - 1 ;i >= 0 ; i--){
            if (elements[i] == obj || elements[i].equals(obj))
                return i;
        }
        return -1;
    }

    public boolean contains(Object obj){
        for (int i = 0; i < size; i++) {
            if (elements[i] == obj || elements[i].equals(obj))
                return true;
        }
        return false;
    }

    /**
     * 把传入的列表中包含的元素全部删除掉
     * @param r
     */
    public void removeAll(MyArrayList<E> r){
        batchRemove(r,false);
    }

    /**
     * 把传入的列表中包含的元素之外的元素全部去掉
     * @param retains
     */
    public void retainsAll(MyArrayList<E> retains){
        batchRemove(retains,true);
    }

    public Object[] toArray(){
        if (size == 0) return null;
        Object[] objects = new Object[size];
        for (int i = 0; i < size; i++) {
            objects[i] = elements[i];
        }
        return objects;
    }

    private void batchRemove(MyArrayList<E> remove,boolean replacement){
        if (remove == null || remove.size() <= 0){
            return;
        }
        int r = 0;//指针位置
        int w = 0;//替换位置
        while (r < size){
            if (remove.contains(elements[r]) == replacement){
                elements[w++] = elements[r];
            }
            r++;
        }
        for (int i = w; i < size; i++) {
            elements[i] = null;
        }
        int mod = size - w;//修改的次数
        size -= mod;
    }

    /**
     * 自动扩容，每次扩容都扩大到原来的1.5倍
     * @param newCapacity
     */
    private void ensureNewCapacity(int newCapacity){
        if (size <= 0) return;
        if (newCapacity > MAX_CAPACITY){
            System.out.println("数组大小已达到最大值，无法再插入数据");
            return;
        }
        if (newCapacity <= capacity)
            return;
        int nc = capacity * 3 / 2;
        if (nc < newCapacity)
            nc = newCapacity;
        Object[] ne = new Object[nc];
        System.arraycopy(elements,0,ne,0,size);
        capacity = nc;
        elements = ne;
    }

    public int size(){
        return size;
    }
    public boolean isEmpty(){return size == 0;}

    @Override
    public String toString() {
        if (size <= 0) return "[]";
        StringBuilder builder = new StringBuilder(size + 4);
        builder.append("[");
        for (int i = 0; i < size; i++) {
            builder.append(elements[i]);
            builder.append(", ");
        }
        return builder.substring(0,builder.length() - 2) + "]";
    }
}
