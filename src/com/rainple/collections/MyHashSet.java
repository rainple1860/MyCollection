package com.rainple.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Iterable<E> {

    private static final Object OBJECT = new Object();
    private MyHasMap<E,Object> hasMap;

    public MyHashSet(){
        hasMap = new MyHasMap<>();
    }

    public MyHashSet(int initialCapacity){
        hasMap = new MyHasMap(initialCapacity,0.75f);
    }

    public void add(E e){
        hasMap.put(e,OBJECT);
    }

    public void remove(Object key){
        hasMap.remove(key);
    }

    public E get(Object key){
        return (E) hasMap.get(key);
    }

    public void clear(){
        hasMap.clear();
    }
    public int size(){
        return hasMap.size();
    }
    public boolean isEmpty(){
        return hasMap.isEmpty();
    }
    public boolean contains(Object key){
        return hasMap.containsKey(key);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (E e : hasMap.keySet()) {
            if (e != null){
                builder.append(e);
                builder.append(", ");
            }
        }
        return builder.substring(0,builder.length() - 2) + "]";
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E>{

        private int cursor;
        private int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public E next() {
            MyHasMap.Node<E, Object>[] nodes = hasMap.entrySet();
            int i = cursor;
            if (i >= size())
                throw new NoSuchElementException();
            cursor = i + 1;
            return nodes[lastRet = i].key;
        }
    }

}
