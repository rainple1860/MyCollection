package com.rainple.collections;

import java.util.*;

public class MyTreeMap<K,V> {

    private static final boolean BLACK = true;
    private static final boolean RED = false;

    private Entry<K,V> root;
    private int size = 0;
    private final Comparator<K> comparator;
    MyTreeMap(){
        comparator =null;
    }

    public MyTreeMap(Comparator comparator){
        this.comparator = comparator;
    }

    public V put(K key,V value){
        if (root == null){
            root = new Entry<>(key,value,null);
            size++;
            return null;
        }else {
            int ret = 0;
            Entry<K,V> p = null;
            Entry<K,V> current = root;
            if (comparator == null){
                if (key == null) throw  new NullPointerException("key = null");
                Comparable<K> k = (Comparable<K>) key;
                while (current != null){
                    p =current;
                    ret = k.compareTo(current.key);
                    if (ret < 0)
                        current = current.left;
                    else if(ret > 0)
                        current = current.right;
                    else {
                        current.value = value;
                        return current.value;
                    }
                }
            }else {
                do {
                    p = current;
                    ret = comparator.compare(key,current.key);
                    if (ret < 0)
                        current = current.left;
                    else if (ret > 0)
                        current = current.right;
                    else {
                        current.value = value;
                        return value;
                    }
                }while (current != null);
            }
            Entry<K,V> e = new Entry<>(key,value,p);
            if (ret < 0)
                p.left = e;
            else
                p.right = e;
            size++;
            fixAfterInsertion(e);
            return e.value;
        }
    }

    private void fixAfterInsertion(Entry<K, V> e) {
        setRed(e);
        Entry<K,V> p,g,u;//父节点和祖父节点和叔叔节点
        Entry<K,V> current = e;
        while ((p = parentOf(current)) != null && isRed(p)){
            g = parentOf(p);
            if (p == g.left){
                u = g.right;
                if (u != null && isRed(u)){
                    setBlack(p);
                    setBlack(u);
                    setRed(g);
                    current = g;
                    continue;
                }
                if (current == p.right){
                    leftRotate(p);
                    Entry<K,V> tmp = p;
                    p = current;
                    current = tmp;
                }
                setBlack(p);
                setRed(g);
                rightRotate(g);
            }else {
                u = g.left;
                if (u != null && isRed(u)){
                    setBlack(p);
                    setBlack(u);
                    setRed(g);
                    current = g;
                    continue;
                }
                if (current == p.left){
                    rightRotate(p);
                    Entry<K,V> tmp = p;
                    p = current;
                    current = tmp;
                }
                setBlack(p);
                setRed(g);
                leftRotate(g);
            }
        }
        setBlack(root);
    }

    public boolean containsKey(Object key){
        return getEntry(key) != null;
    }

    public Set<Entry<K,V>> entrySet(){
        Set<Entry<K,V>> list = new HashSet<>(size + 4);
        entries(root,list);
        return list;
    }

    private void entries(Entry<K,V> e,Set<Entry<K,V>> list){
        if (e != null){
            entries(e.left,list);
            list.add(e);
            entries(e.right,list);
        }
    }

    public boolean containsValue(V v){
        return values().contains(v);
    }

    public V get(Object key){
        Entry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    private void setColor(Entry<K,V> e,boolean color){
        if (e != null) e.color = color;
    }

    private void setRed(Entry<K,V> e){
        setColor(e,RED);
    }

    private void setBlack(Entry<K,V> e){
        setColor(e,BLACK);
    }

    private void setParent(Entry<K,V> e,Entry<K,V> p){
        if (e != null) e.parent = p;
    }

    private boolean isBlack(Entry<K,V> e){
        return colorOf(e) == BLACK;
    }

    private boolean isRed(Entry<K,V> e){
        return !isBlack(e);
    }

    private Entry<K,V> parentOf(Entry<K,V> e){
        return e == null ? null : e.parent;
    }

    private boolean colorOf(Entry<K,V> e){
        return e == null ? BLACK : e.color;
    }

    /**
     * 右旋
     * @param e 旋转支点
     */
    private void rightRotate(Entry<K,V> e){
        Entry<K,V> left = e.left;
        Entry<K,V> leftOfRight = left.right;
        left.parent = e.parent;
        if (e.parent == null){
            root = left;
        }else {
            if (e == e.parent.left)
                e.parent.left = left;
            else
                e.parent.right = left;
        }
        left.right = e;
        e.parent = left;
        e.left = leftOfRight;
        if (leftOfRight != null)
            leftOfRight.parent = e;
    }

    /**
     * 左旋
     * @param e 支点
     */
    private void leftRotate(Entry<K,V> e){
        Entry<K,V> right = e.right;
        Entry<K,V> rightOfLeft = right.left;
        right.parent = e.parent;
        if (e.parent == null){
            root = right;
        }else {
            if (e == e.parent.left)
                e.parent.left = right;
            else
                e.parent.right = right;
        }
        right.left = e;
        e.parent = right;
        e.right = rightOfLeft;
        if (rightOfLeft != null)
            rightOfLeft.parent = e;
    }

    public int getDeep(){
        return deep(root);
    }

    private int deep(Entry<K,V> e){
        int deep = 0;
        if (e != null){
            int leftDeep = deep(e.left);
            int rightDeep = deep(e.right);
            deep = leftDeep > rightDeep ? leftDeep + 1 : rightDeep + 1;
        }
        return deep;
    }

    public V remove(Object key){
        if (key == null) return null;
        Entry<K,V> current = root;
        int ret;
        Entry<K,V> delEntry = null;
        delEntry = getEntry(key);
        if (delEntry == null) return null;
        size--;
        Entry<K,V> p = delEntry.parent;
        if (delEntry.right == null && delEntry.left == null){
            if (p == null){
                root = null;
            }else {
                if (p.left == delEntry){
                    p.left = null;
                }else {
                    p.right = null;
                }
            }
        }else if (delEntry.right == null){//只有左节点
            Entry<K,V> lc = delEntry.left;
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
                Entry<K,V> rc = delEntry.right;
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
            Entry<K,V> successor = successor(delEntry);//获取到后继节点
            Entry<K,V> parent = successor.parent;
            Entry<K,V> child = successor.right;
            boolean color = successor.color;
            V old = delEntry.value;
            delEntry.value = successor.value;
            delEntry.key = successor.key;
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
            if (color == BLACK)
                //fixUpAfterRemove(child,parent);
            return old;
        }
        V old = delEntry.value;
        if (delEntry.color == BLACK)//删除为黑色时，需要重新平衡树
            if (delEntry.right != null)//删除节点的子节点只有右节点
                fixUpAfterRemove(delEntry.right,delEntry.parent);
            else if (delEntry.left != null)//删除节点只有左节点
                fixUpAfterRemove(delEntry.left,delEntry.parent);
            else
                fixUpAfterRemove(null,delEntry.parent);
        delEntry.parent = null;
        delEntry.left = null;
        delEntry.right = null;
        return old;
    }

    private Entry<K, V> getEntry(Object key) {
        if (key == null) return null;
        Entry<K, V> delEntry = null;
        Entry<K, V> current = root;
        int ret;
        if (comparator == null){
            Comparable<K> k = (Comparable<K>) key;
            while (current != null){
                ret = k.compareTo(current.key);
                if (ret < 0)
                    current = current.left;
                else if (ret > 0)
                    current = current.right;
                else{
                    delEntry = current;
                    break;
                }
            }
        }else {
            for (;current != null;){
                ret = comparator.compare(current.key, (K) key);
                if (ret < 0)
                    current = current.left;
                else if (ret > 0)
                    current = current.right;
                else{
                    delEntry = current;
                    break;
                }
            }
        }
        return delEntry;
    }

    //node表示待修正的节点，即后继节点的子节点（因为后继节点被挪到删除节点的位置去了）
    private void fixUpAfterRemove(Entry<K, V> node,Entry<K,V> parent) {
        Entry<K,V> other;
        while((node == null || isBlack(node)) && (node != root)) {
            if(parent.left == node) { //node是左子节点，下面else与这里的刚好相反
                other = parent.right; //node的兄弟节点
                if(isRed(other)) { //case1: node的兄弟节点other是红色的
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }

                //case2: node的兄弟节点other是黑色的，且other的两个子节点也都是黑色的
                if((other.left == null || isBlack(other.left)) &&
                        (other.right == null || isBlack(other.right))) {
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {
                    //case3: node的兄弟节点other是黑色的，且other的左子节点是红色，右子节点是黑色
                    if(other.right == null || isBlack(other.right)) {
                        setBlack(other.left);
                        setRed(other);
                        rightRotate(other);
                        other = parent.right;
                    }

                    //case4: node的兄弟节点other是黑色的，且other的右子节点是红色，左子节点任意颜色
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.right);
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else { //与上面的对称
                other = parent.left;

                if (isRed(other)) {
                    // Case 1: node的兄弟other是红色的
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // Case 2: node的兄弟other是黑色，且other的俩个子节点都是黑色的
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.left==null || isBlack(other.left)) {
                        // Case 3: node的兄弟other是黑色的，并且other的左子节点是红色，右子节点为黑色。
                        setBlack(other.right);
                        setRed(other);
                        leftRotate(other);
                        other = parent.left;
                    }

                    // Case 4: node的兄弟other是黑色的；并且other的左子节点是红色的，右子节点任意颜色
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.left);
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }
        if (node!=null)
            setBlack(node);
    }

    private Entry<K, V> successor(Entry<K, V> delEntry) {
        Entry<K,V> r = delEntry.right;//assert r != null;
        while (r.left != null){
            r = r.left;
        }
        return r;
    }

    List<V> values(){
        List<V> set = new ArrayList<>(size+4);
        midIterator(root,set);
        return set;
    }

    private void midIterator(Entry<K,V> e, List<V> values){
        if (e != null){
            midIterator(e.left,values);
            values.add(e.value);
            midIterator(e.right,values);
        }
    }

    public void clear(){
        clear(root);
        root = null;
    }

    private void clear(Entry<K,V> node) {
        if (node != null){
            clear(node.left);
            node.left = null;
            clear(node.right);
            node.right = null;
        }
    }

    public int size(){return size;}

    static final class Entry<K,V>{
        private K key;
        private V value;
        private Entry<K,V> left;
        private Entry<K,V> right;
        private Entry<K,V> parent;
        private boolean color = BLACK;
        Entry(K key,V value,Entry<K,V> parent){
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

}
