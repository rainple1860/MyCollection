package com.rainple.collections;

/**
 * Ë«ÏòÁ´±í½á¹¹
 * @param <E>
 */
public class MyArrayLinkedList<E> {

    /**
     * 头部
     */
    private Node<E> first;
    private Node<E> last;
    private int size;

    public MyArrayLinkedList(){
        size = 0;
    }

    private void linkFirst(E e){
        Node f = first;
        Node n = new Node(e,null,f);
        first = n;
        if (f == null){
            last = n;
        }else {
            f.prev = first;
        }
    }

    private void linkLast(E e){
        Node l = last;
        Node n = new Node(e,l,null);
        last = n;
        if (l == null) {
            first = n;
        } else
            l.next = last;
    }

    private void unLinkFirst(){
        Node<E> next = first.next;
        Node f = first;
        first = next;
        f.next = null;
        next.prev = null;
    }

    private void unLinkLast(){
        Node<E> prev = last.prev;
        Node<E> l = last;
        last = prev;
        l.prev = null;
        last.next = null;
    }

    private void unLink(Node node){
        if (node == null)
            return;
        if (node == first){
            unLinkFirst();
        }else if (node == last){
            unLinkLast();
        }else {
            Node prev = node.prev;
            Node next = node.next;
            prev.next = next;
            next.prev = prev;
        }
        node.prev = null;
        node.next = null;
        size--;
    }

    public boolean add(E e){
        return addLast(e);
    }

    public boolean addLast(E e){
        linkLast(e);
        size++;
        return true;
    }

    public boolean addFirst(E e){
        linkFirst(e);
        size++;
        return true;
    }

    private Node getNode(int index){
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("" + index);
        int mid = index >> 1;
        if (index > mid){
            Node n = last;
            for (int i = size - 1 ; i > index ; i--)
                n = n.prev;
            return n;
        }else {
            Node n = first;
            for (int i = 0 ; i < index ; i++)
                n = n.next;
            return n;
        }
    }

    private Node getNode(E e){
        Node<E> f = this.first;
        while (f != null){
            if (f.data == e || f.data.equals(e))
                return f;
            f = f.next;
        }
        return null;
    }

    public E get(E e){
        Node node = getNode(e);
        return node == null ? null : (E) node.data;
    }

    public E getFirst(){
        Node f = first;
        return f == null ? null : (E) f.data;
    }

    public E getLast(){
        Node l = last;
        return l == null ? null : (E) l.data;
    }

    public E get(int index){
        Node node = getNode(index);
        return node == null ? null : (E) node.data;
    }

    public E remove(E e){
        Node node = getNode(e);
        unLink(node);
        return node == null ? null : (E) node.data;
    }
    public E removeFirst(){
        Node<E> f = this.first;
        if (f == null) return null;
        unLinkFirst();
        return f.data;
    }
    public E removeLast(){
        Node<E> l = this.last;
        if (l == null) return null;
        unLinkLast();
        return l.data;
    }

    static class Node<E>{
        E data;
        Node<E> prev;
        Node<E> next;
        public Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (first == null) return "[]";
        builder.append("[");
        Node f = first;
        while (f != null){
            builder.append(f.data);
            builder.append(", ");
            f = f.next;
        }
        return builder.substring(0,builder.length() - 2) + "]";
    }
}
