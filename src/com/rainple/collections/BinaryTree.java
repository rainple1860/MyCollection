package com.rainple.collections;

/**
 * 二叉树
 * @param <T>
 */
public class BinaryTree<T extends Comparable<T>> {

    private Entry<T> root;
    private int size = 0;

    public BinaryTree(){}

    /**
     * 添加元素
     * @param item 待添加元素
     * @return 已添加元素
     */
    public T put(T item){
        Entry<T> t = root;
        size++;
        if (t == null){
            root = new Entry<>(item,null);
            return root.item;
        }
        int ret = 0;
        Entry<T> p = t;
        while (t != null){
            ret = item.compareTo(t.item);
            p = t;
            if (ret < 0)
                t = t.left;
            else if (ret > 0)
                t = t.right;
            else {
                t.item = item;
                return t.item;
            }
        }
        Entry<T> e = new Entry<>(item,p);
        if (ret < 0)
            p.left = e;
        else
            p.right = e;
        return e.item;
    }

    public void print(){
        midIterator(root);
    }

    /**
     * 中序遍历
     * @param e
     */
    public void midIterator(Entry<T> e){
        if (e != null){
            midIterator(e.left);
            System.out.print(e.item + " ");
            midIterator(e.right);
        }
    }

    /**
     * 获取根节点
     * @return 根节点
     */
    public Entry<T> getRoot(){return root;}

    /**
     * 前序遍历
     * @param e 开始遍历元素
     */
    public void prevIterator(Entry<T> e){
        if (e != null) {
            System.out.print(e.item + " ");
            prevIterator(e.left);
            prevIterator(e.right);
        }
    }

    /**
     * 后续遍历
     * @param e 开始遍历元素
     */
    public void subIterator(Entry<T> e){
        if (e != null) {
            subIterator(e.left);
            subIterator(e.right);
            System.out.print(e.item + " ");
        }
    }

    private Entry<T> getEntry(T item){
        Entry<T> t = root;
        int ret;
        for (;t != null;){
            ret = item.compareTo(t.item);
            if (ret < 0)
                t = t.left;
            else if (ret > 0)
                t = t.right;
            else
                return t;
        }
        return null;
    }

    /**
     * 判断是否存在该元素
     * @param item 查找元素
     * @return 结果
     */
    public boolean contains(T item){
        return getEntry(item) != null;
    }

    /**
     * 删除元素
     * @param item 删除元素
     * @return 删除结果
     */
    public boolean remove(T item){
        Entry<T> delEntry = getEntry(item);
        if (delEntry == null) return false;
        Entry<T> p = delEntry.parent;
        size--;
        if (delEntry.left == null && delEntry.right == null){
            if (delEntry == root){
                root = null;
            }else {
                if (delEntry == p.left){
                    p.left = null;
                }else {
                    p.right = null;
                }
            }
        }else if (delEntry.right == null){//只有左节点
            Entry<T> lc = delEntry.left;
            if (p == null) {
                lc.parent = null;
                root = lc;
            } else {
                if (delEntry == p.left){
                    p.left = lc;
                }else {
                    p.right = lc;
                }
                lc.parent = p;
            }
        }else if (delEntry.left == null){//只有右节点
            Entry<T> rc = delEntry.right;
            if (p == null) {
                rc.parent = null;
                root = rc;
            }else {
                if (delEntry == p.left)
                    p.left = rc;
                else
                    p.right = rc;
                rc.parent = p;
            }
        }else {//有两个节点,找到后继节点，将值赋给删除节点，然后将后继节点删除掉即可
            Entry<T> successor = successor(delEntry);//获取到后继节点
            delEntry.item = successor.item;
            if (delEntry.right == successor){//后继节点为右子节点，
                if (successor.right != null) {//右子节点有右子节点
                    delEntry.right = successor.right;
                    successor.right.parent = delEntry;
                }else {//右子节点没有子节点
                    delEntry.right = null;
                }
            }else {
                successor.parent.left = null;
            }
            return true;
        }
        delEntry.parent = null;
        delEntry.left = null;
        delEntry.right = null;
        return true;
    }

    /**
     * 查找后继节点
     * @param delEntry 删除节点
     * @return 后继节点
     */
    private Entry<T> successor(Entry<T> delEntry) {
        Entry<T> r = delEntry.right;//assert r != null;
        while (r.left != null){
            r = r.left;
        }
        return r;
    }

    public int size(){return size;}

    public boolean isEmpty(){return size == 0;}

    public void clear(){
        clear(getRoot());
        root = null;
    }

    private void clear(Entry<T> e){
        if (e != null){
            clear(e.left);
            e.left = null;
            clear(e.right);
            e.right = null;
        }
    }

    static final class Entry<T extends Comparable<T>>{
        private T item;
        private Entry<T> left;
        private Entry<T> right;
        private Entry<T> parent;
        Entry(T item,Entry<T> parent){
            this.item = item;
            this.parent = parent;
        }
    }

}
