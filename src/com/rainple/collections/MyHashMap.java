package com.rainple.collections;

import java.util.Arrays;

public class MyHashMap<K,V> {

    //默认初始化容量
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    //hash表的最大容量
    private int threshold;
    //转换二叉树阈值
    static final int TREEIFY_THRESHOLD = 1 << 3;
    //二叉树转链表阈值
    static final int UNTREEIFY_THRESHOLD = 6;
    //负载因子，当map的数据达到 负载因子和threshold的乘积时自动扩容，减少hash碰撞
    private float load_factory;
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;
    //数据数量
    private int size;
    //存在数据的数组，采用链地址的方式来解决碰撞问题
    //链表长度大于 TREEIFY_THRESHOLD 值时会将链表转化成二叉树
    private Node<K,V>[] table;

    public MyHashMap() {
        this.threshold = DEFAULT_INITIAL_CAPACITY;
        this.load_factory = DEFAULT_LOAD_FACTORY;
        table = new Node[threshold];
    }

    public MyHashMap(int initialCapacity){
        this.threshold = initialCapacity;
        this.load_factory = DEFAULT_LOAD_FACTORY;
        table = new Node[threshold];
    }

    public MyHashMap(int capacity, float load_factory) {
        this.threshold = capacity;
        this.load_factory = load_factory;
        table = new Node[threshold];
    }

    public boolean putAll(MyHashMap<K,V> map){
        if (map == null || map.size() <= 0){
            return false;
        }
        Node<K, V>[] nodes = map.entrySet();
        for (Node<K, V> node : nodes) {
            put(node.key,node.val);
        }
        return true;
    }

    public void put(K key, V value){
        int hash = hash(key);
        int index = indexFor(hash);
        /**数组位置没被占用，直接将插入的值放到该位置上
         * 如果已占用，则在该位置上生成链表
         * 当链表长度达到 TREEIFY_THRESHOLD 后将会转换成二叉树（暂时没研究红黑树）
         */
        if (table[index] == null) {
            Node<K, V> newNode = new Node<>(hash,key, value, null);
            table[index] = newNode;
            size++;
        }else {
            Node<K,V> first = table[index];
            if (first instanceof Tree){
                putTreeVal(hash,key, value, (Tree<K, V>) first);
            }else {
                putVal(key, value, hash, first,table);
            }
        }
        //扩容
        if (size > threshold*load_factory){
           threshold = threshold << 1;
           resize(threshold);
        }
    }

    /**
     * 扩容
     * @param newThreshold 数组的大小
     */
    private void resize(int newThreshold) {
        Node<K,V>[] oldTab = table;
        table = new Node[newThreshold];
        for (int i = 0; i < oldTab.length; i++) {
            Node<K,V> node = oldTab[i];
            if (node != null) {
                int index = node.hash & (newThreshold - 1);
                table[index] = node;
//                if (node instanceof Tree){
//                    MyArrayList<Node<K,V>> all = ((Tree<K,V>) node).getTreeNodes();
//                    for (int j = 0; j < all.size(); j++) {
//                        Node<K, V> n = all.get(j);
//                        int index = n.hash & (newThreshold - 1);
//                        putOltNodeToNewNode(table,n,index);
//                    }
//                }else {//不将链表每个元素重新hash，而是将整个链表直接放在数组中
//                    int index = node.hash & (newThreshold - 1);
//                    table[index] = node;
//                }
                oldTab[i] = null;
            }
        }
    }

    private void putOltNodeToNewNode(Node[] newTable, Node<K, V> current, int index) {
        Node newNode = newTable[index];
        if (newNode == null){
            newTable[index] = current;
        } else{
            Node n = newNode;
            int count = 0;
            while (n != null){
                Node next = n.next;
                count++;
                if (next == null){
                    if (count > TREEIFY_THRESHOLD){
                        transferToBinaryTree(newTable,current.hash);
                    }else {
                        n.next = current;
                    }
                }
                n = next;
            }
        }
    }

    /**
     * 将 key-value 放入链表当中，先计算存放的key对应的下表，根据下标确定存放在数组中的位置
     * 如果该位置已经有值，则判断key是否相等，相等则替换掉原来的值，否则查找链表下一个数据，有数据判断key值是否相等，相等替换掉，以此循环
     * 遍历完链表都没有相同的key时将该数据存在在表尾中
     * @param key 键，不可以存放key相同的两个数据
     * @param value 值，可以为任意值，可重复
     * @param hash 键对应的hash值
     * @param first 链表的头部
     */
    private void putVal(K key, V value, int hash, Node<K, V> first,Node[] tbl) {
        Node<K, V> parent = first;
        //用于记录是否达到转换二叉树
        for (int count = 0; ; count++) {
            Node<K, V> next = parent.getNext();
            if (parent.key.equals(key) || parent.hash == key.hashCode()) {
                parent.val = value;
                break;
            }
            if (next == null) {
                parent.setNext(new Node<>(hash,key, value, null));
                if (count >= TREEIFY_THRESHOLD - 2) {
                    //转化成二叉树
                    transferToBinaryTree(tbl, hash);
                }
                size++;
                break;
            }
            parent = next;
        }
    }

    /**
     * 将数据放入到 二叉树中,根据key的hashCode 的大小来确定节点存放的位置
     * @param key 键
     * @param value 值
     * @param root 二叉树头部
     */
    private void putTreeVal(int hash, K key, V value, Tree<K, V> root) {
        Node<K,V> newNode = new Node<K,V>(hash,key,value,null);
        root.insert(newNode);
        size++;
    }

    /**
     * 链表转二叉树
     * @param table
     * @param hash
     */
    private void transferToBinaryTree(Node<K, V>[] table, int hash) {
        int index = indexFor(hash);
        Node<K, V> node = table[index];
        Tree tree = new Tree();
        while (node != null) {
            tree.insert(node);
            Node<K, V> n = node.getNext();
            node.next = null;
            node = n;
        }
        table[index] = tree;
    }

    /**
     * 根据key获取数据
     * @param key
     * @return
     */
    public V get(Object key){
        int index = indexFor(hash(key));
        Node<K, V> first = table[index];
        if (first instanceof Tree){
            Node node = ((Tree) first).get(key);
            return (V) node.val;
        }
        Node<K, V> node = first;
        while (node != null){
            if (node.key.equals(key)){
                return node.val;
            }
            node = node.getNext();
        }
        return null;
    }

    /**
     * 确定数据存放的下表
     * @param hash
     * @return
     */
    private int indexFor(int hash){
        return hash & (table.length - 1);
    }

    /**
     * 高低位混合的计算方式
     * @param key
     * @return
     */
    private int hash(Object key){
       int h;
       return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    public int size(){
        return size;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    static class Tree<K,V> extends Node{
        private Tree<K, V> root;
        private Tree<K, V> leftChild;
        private Tree<K, V> rightChild;
        private Node<K,V> data;
        private int size;

        public Tree(){}

        public Tree(Tree<K, V> leftChild, Tree<K, V> rightChild, Node<K,V> data) {
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.data = data;
        }
        /**
         * 获取节点
         */
        public Node<K,V> get(Object key){
            Tree<K, V> current = root;
            int keyCode = key.hashCode();
            while (current != null) {
                int currentCode = current.data.key.hashCode();
                if (keyCode > currentCode) {
                    current = current.rightChild;
                }else if (keyCode < currentCode) {
                    current = current.leftChild;
                }else {
                    if (key.equals(current.data.key)) {
                        return current.data;
                    }
                    current = current.rightChild;
                }
            }
                return null;
        }

        public int size(){return size;}

        /**
         * 插入数据,根据key的hashCode来确定node在二叉树中的位置
         * 如果两个 key 的 hashCode 的值相等 后来的放进 右侧
         * @param node
         * @return
         */
        public Node<K,V> insert(Node<K,V> node){
            if (node == null)
                return null;
            Tree<K, V> nt = new Tree<>(null,null,node);
            if (root == null) {
                root = nt;
                size++;
                return root.data;
            }
            Tree<K, V> current = root;
            int insCode = node.key.hashCode();
            Tree<K, V> parent;
            while (current != null){
                int currentCode = current.data.key.hashCode();
                parent = current;
                if (insCode > currentCode){
                    current = parent.rightChild;
                    if (current == null){
                        parent.rightChild = nt;
                        size++;
                        break;
                    }
                }else if (insCode < currentCode){
                    current = parent.leftChild;
                    if (current == null){
                        parent.leftChild = nt;
                        size++;
                        break;
                    }
                }else {
                    Object key = node.key;
                    Object key1 = current.data.key;
                    if (key.equals(key1)){
                        current.data = node;
                        break;
                    }
                    current = current.rightChild;
                }
            }
            return nt.data;
        }

        /**
         * 删除数据
         * @param key
         */
        public void delete(Object key){
            if (root == null) return;
            Tree<K,V> current = root;
            Tree<K,V> parent = null;
            Tree<K,V> delTree = null;
            boolean isLeft = true;
            int keyCode = key.hashCode();
            /**查找删除节点以及其父节点*/
            if (keyCode == root.data.hash){//删除root节点
                delTree = root;
                parent = root;
            }else {
                while (current != null) {
                    int currentCode = current.data.hash;
                    parent = current;
                    if (keyCode > currentCode) {
                        current = current.rightChild;
                        if (current != null && current.data.hash == keyCode){
                            isLeft = false;
                            delTree = current;
                            break;
                        }
                    } else if (keyCode < currentCode) {
                        current = current.leftChild;
                        if (current != null && current.data.hash == keyCode){
                            isLeft = true;
                            delTree = current;
                            break;
                        }
                    }
                    if (current == null){
                        return;
                    }
                }
            }
            /**查找父节点和删除节点结束*/

            if (delTree.leftChild == null && delTree.rightChild == null){//删除叶子节点
                if (delTree == root) {
                    root = null;
                }else{
                    if (isLeft){
                        parent.leftChild = null;
                    }else {
                        parent.rightChild = null;
                    }
                }
            }else if (delTree.leftChild == null){//删除只有右子叶的节点
                if (delTree == root){
                    root = delTree.rightChild;
                }else {
                    if (isLeft){
                        Tree<K, V> leftChild = delTree.rightChild;
                        parent.leftChild = leftChild;
                    }else {
                        Tree<K, V> rightChild = delTree.rightChild;
                        parent.rightChild = rightChild;
                    }
                    delTree.rightChild = null;
                }
            }else if (delTree.rightChild == null){//删除只有左叶子的节点
                if (delTree == root){
                    root = delTree.leftChild;
                }else {
                    if (isLeft){
                        parent.leftChild = delTree.leftChild;
                    }else {
                        parent.rightChild = delTree.leftChild;
                    }
                    delTree.leftChild = null;
                }
            }else {//删除有两个子节点的节点
                /**
                 * 删除有两个子节点的情况比较复杂一些
                 * 分为两个情况：删除根节点或非根节点
                 * 1、删除根节点：查找后继节点，有一个规律：后继节点总是在删除节点的右子节点或右子节点最左侧的叶子节点
                 */
               if (delTree == root){//删根节点
                   Tree<K, V> rightChild = root.rightChild;
                   Tree<K, V> rep = rightChild.leftChild;
                   //右子节点没有左子节点，直接将该子节点替换成根节点
                   if (rep == null){
                       root.rightChild = root;
                       root = null;
                   }else {
                       //递归查找右子节点最左侧的叶子节点，将该叶子节点替换到跟节点中
                       while (rep != null) {
                            leftChild = rep.leftChild;
                            parent = rep;
                            if (leftChild.leftChild == null){
                                root.data = leftChild.data;
                                parent.leftChild = null;
                                break;
                            }
                            rep = leftChild;
                       }
                   }
               }else {
                   //查找后继节点并将其删除掉
                   Tree<K, V> replaceNode = findAndDeleteReplaceNode(delTree);
                   if (replaceNode == delTree.rightChild){//替换节点为删除节点右节点
                       if (isLeft){
                           parent.leftChild = replaceNode;
                           replaceNode.leftChild = delTree.leftChild;
                       }else {
                           parent.rightChild = replaceNode;
                           replaceNode.leftChild = delTree.leftChild;
                       }
                   }else {//非删除节点的右子节点
                       if (isLeft){
                           parent.leftChild.data = replaceNode.data;
                       }else {
                           parent.rightChild.data = replaceNode.data;
                       }
                   }
               }
            }
            size--;
        }

        /**
         * 查找二叉树的后继节点
         * @param delTree
         * @return
         */
        private Tree<K, V> findAndDeleteReplaceNode(Tree<K, V> delTree) {
            Tree<K, V> rightChild = delTree.rightChild;
            Tree<K, V> target = null;
            Tree<K, V> current = rightChild.leftChild;
            if (current == null){
                target = rightChild;
            }else {
                Tree<K,V> parent = rightChild;
                while (current.leftChild != null) {
                    parent = current;
                    current = current.leftChild;
                }
                target = current;
                parent.leftChild = null;
            }
            return target;
        }

        /**
         * 获取所有的二叉树value数据，根据键的hashcode升序排序
         * @return
         */
        public MyArrayList<Node<K,V>> getTreeNodes(){
            MyArrayList<Node<K,V>> list = new MyArrayList<>();
            midIterate(root,list);
            return list;
        }

        /**
         * 清楚二叉树数据
         */
        public void clear(){
           clear(root);
        }

        private void clear(Node<K,V> node) {
            if (node != null){
                clear(root.leftChild);
                clear(root.rightChild);
            }
        }

        /**
         * 中序遍历
         * @param root 根节点
         * @param list
         */
        public void midIterate(Tree<K, V> root, MyArrayList<Node<K,V>> list){
            if (root != null) {
                midIterate(root.leftChild, list);
                list.add(root.data);
                midIterate(root.rightChild, list);
            }
        }

        /**前序遍历
         * @param root
         */
        public void beforeIterate(Tree<K, V> root){
            if (root != null) {
                this.root = root;
                beforeIterate(root.leftChild);
                root = null;
                beforeIterate(root.rightChild);
            }
        }

        /**
         * 后续遍历
         * @param root
         * @param list
         */
        public void behindIterate(Tree<K, V> root, MyArrayList<Node<K,V>> list){
            if (root != null) {
                midIterate(root.leftChild, list);
                midIterate(root.rightChild, list);
                list.add(root.data);
            }
        }

        @Override
        public String toString() {
            return "Tree{" +
                    "root=" + root +
                    ", leftChild=" + leftChild +
                    ", rightChild=" + rightChild +
                    ", data=" + data +
                    '}';
        }
    }

    static class Node<K ,V>{
        int hash;//扩容的时候用到
        K key;
        V val;
        private Node<K,V> next;
        Node( int hash,K key, V val, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.setNext(next);
        }

        Node() {
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", val=" + val +
                    ", next=" + next +
                    '}';
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
    /**
     * 碰撞率
     * @return
     */
    public String boomRate(){
        double ds = Double.parseDouble(size + "");
        double boomCount = 0;
        for (Node<K, V> node : table) {
            if (node != null){
                boomCount++;
            }
        }
        String rate = String.valueOf(((ds - boomCount) / ds * 100));
        return rate + "%";
    }

    /**
     * 使用率
     * @return
     */
    public String useRate(){
        double used = 0;
        for (Node<K, V> node : table) {
            if (node != null) used++;
        }
        double v = Double.parseDouble(threshold + "");
        return String.valueOf((used / v * 100)) + "%";
    }

    public V replace(Object key,V v){
        Node<K, V> node = getNode(key);
        if (node == null) return null;
        V oldVal = node.val;
        node.val = v;
        return oldVal;
    }

    public boolean replace(Object key,V oldValue,V newValue){
        Node<K, V> node = getNode(key);
        if (node == null) return false;
        if (node.val == oldValue || node.val.equals(oldValue)){
            node.val = newValue;
            return true;
        }
        return false;
    }

    /**
     * 获取节点
     * @param key
     * @return
     */
    private Node<K,V> getNode(Object key){
        int hash = hash(key);
        int index = indexFor(hash);
        Node<K, V> node = table[index];
        if (node == null) return null;
        if (node instanceof Tree){
            return ((Tree) node).get(key);
        }
        Node<K, V> n = node;
        while (n != null){
            if (n.key.equals(key)){
                return n;
            }
            n = n.getNext();
        }
        return null;
    }

    /**
     * 判断是否存在key
     * @param key
     * @return
     */
    public boolean containsKey(Object key){
        return getNode(key) != null;
    }

    /**
     * 查找是否存在value
     * @param val
     * @return
     */
    public boolean containsValue(Object val){
        for (Node<K, V> node : entrySet()) {
            if ( val == node.val || node.val.equals(val))
                return true;
        }
        return false;
    }

    /**
     * 获取所有的value值
     * @return
     */
    public MyArrayList<V> values(){
        MyArrayList<V> vs = new MyArrayList<>();
        for (Node<K, V> node : entrySet()) {
            vs.add(node.val);
        }
        return vs;
    }

    public MyHashSet<K> keySet(){
        MyHashSet<K> hashSet = new MyHashSet<>();
        for (Node<K, V> node : entrySet()) {
            hashSet.add(node.key);
        }
        return hashSet;
    }

    /**
     * 清楚整个hash表
     */
    public void clear(){
        if (table != null && table.length > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = 0;
        }
    }

    /**
     * 根据key来删除数据
     * @param key
     * @return
     */
    public void remove(Object key){
        int index = indexFor(hash(key));
        Node<K, V> oldNode = table[index];
        if (oldNode == null) {
            return;
        } else {
            //从二叉树中删除节点
            if (oldNode instanceof Tree){
                Tree<K,V> current = (Tree<K, V>) oldNode;
                current.delete(key);
                //二叉树转链表
                if (current.size < UNTREEIFY_THRESHOLD){
                    transferChainList(current,key.hashCode());
                }
            }else {//从单项链表中删除节点
                Node<K, V> current = oldNode;
                //链表只有一个节点
                if (current.getNext() == null){
                    table[index] = null;
                }else if (current.key.equals(key)){//删除第一个节点
                    table[index] = current.getNext();
                }else {
                    Node<K, V> n;
                    do {
                        n = current.getNext();
                        if (n == null){//节点不存在
                            return;
                        }
                        if (n.key.equals(key)){
                            if (n.getNext() != null) {//删除中间节点
                                current.setNext(n.getNext());
                            } else {//删除最后一个节点
                                current.setNext(null);
                            }
                        }
                    }while ((current = current.getNext()) != null);
                }
            }
            size--;
        }
    }

    /**
     * 二叉树转链表
     * @param root
     */
    private void transferChainList(Tree<K, V> root,int hash) {
        MyArrayList<Node<K, V>> nodes = root.getTreeNodes();
        System.out.println("二叉树转链表");
        int index = indexFor(hash);
        table[index] = null;
        for (int i = 0 ; i < nodes.size() ; i ++){
           insertIntoChainList(index,nodes.get(i));
        }
    }

    /**
     * 将二叉树数据插入到链表中
     * @param index
     * @param n
     */
    private void insertIntoChainList(int index, Node<K, V> n){
        Node<K, V> node = table[index];
        if (node == null){
            table[index] = n;
        }else {
            while (node != null){
                if (node.next == null){
                    node.next = n;
                    break;
                }
                node = node.next;
            }
        }
    }

    /**
     * 获取所有Node节点的数据
     * @return
     */
    public Node<K,V>[] entrySet(){
        Node[] nodes = new Node[size];
        int index = 0;
        for (Node<K, V> node : table) {
            if (node != null){
                if (node instanceof Tree){
                    MyArrayList<Node<K,V>> all = ((Tree<K,V>) node).getTreeNodes();
                    for (int i = 0; i < all.size(); i++) {
                        nodes[index++] = all.get(i);
                    }
                }else {
                    while (node != null){
                        nodes[index++] = node;
                        node = node.next;
                    }
                }
            }
        }
        if (index < size)
            nodes = Arrays.copyOf(nodes,index);
        System.out.println(nodes.length);
        return nodes;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(size + 8);
        builder.append("{");
        String s = "";
        if (entrySet().length > 0) {
            for (Node<K, V> node : entrySet()) {
                if (node != null) {
                    builder.append(node.key);
                    builder.append("=");
                    builder.append(node.val);
                    builder.append(", ");
                }
            }
            s = builder.substring(0, builder.length() - 2) + "}";
        }else {
            s = "{}";
        }
        return s;
    }
}
