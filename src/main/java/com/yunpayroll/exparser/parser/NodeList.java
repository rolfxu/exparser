package com.yunpayroll.exparser.parser;

import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;


public class NodeList extends Node implements List<Node>, RandomAccess {
    //~ Static fields/initializers ---------------------------------------------

    /**
     * An immutable, empty NodeList.
     */
    public static final NodeList EMPTY =
            new NodeList(ImmutableList.of(), ParserPos.ZERO);

    /**
     * A NodeList that has a single element that is an empty list.
     */
    public static final NodeList SINGLETON_EMPTY =
            new NodeList(ImmutableList.of(EMPTY), ParserPos.ZERO);

    

    //~ Instance fields --------------------------------------------------------

    // Sometimes null values are present in the list, however, it is assumed that callers would
    // perform all the required null-checks.
    private final List<@Nullable Node> list;

    //~ Constructors -----------------------------------------------------------

    /** Creates a NodeList with a given backing list.
     *
     * <p>Because NodeList implements {@link RandomAccess}, the backing list
     * should allow O(1) access to elements. */
    private NodeList(ParserPos pos, List<@Nullable Node> list) {
        super(pos);
        this.list = Objects.requireNonNull(list, "list");
    }

    @Override
    public Object toValue() {
        return null;
    }

    @Override
    void visit(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates a NodeList that is initially empty.
     */
    public NodeList(ParserPos pos) {
        this(pos, new ArrayList<>());
    }

    /**
     * Creates a <code>NodeList</code> containing the nodes in <code>
     * list</code>. The list is copied, but the nodes in it are not.
     */
    public NodeList(
            Collection<? extends @Nullable Node> collection,
            ParserPos pos) {
        this(pos, new ArrayList<@Nullable Node>(collection));
    }

    /**
     * Creates a NodeList with a given backing list.
     * Does not copy the list.
     */
    public static NodeList of(ParserPos pos, List<@Nullable Node> list) {
        return new NodeList(pos, list);
    }

    //~ Methods ----------------------------------------------------------------

    // List, Collection and Iterable methods


    @Override public int hashCode() {
        return list.hashCode();
    }

    @Override public boolean equals(@Nullable Object o) {
        return this == o
                || o instanceof NodeList && list.equals(((NodeList) o).list)
                || o instanceof List && list.equals(o);
    }

    @Override public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override public int size() {
        return list.size();
    }

    @SuppressWarnings("return.type.incompatible")
    @Override public Iterator</*Nullable*/ Node> iterator() {
        return list.iterator();
    }

    @SuppressWarnings("return.type.incompatible")
    @Override public ListIterator</*Nullable*/ Node> listIterator() {
        return list.listIterator();
    }

    @SuppressWarnings("return.type.incompatible")
    @Override public ListIterator</*Nullable*/ Node> listIterator(int index) {
        return list.listIterator(index);
    }

    @SuppressWarnings("return.type.incompatible")
    @Override public List</*Nullable*/ Node> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @SuppressWarnings("return.type.incompatible")
    @Override public /*Nullable*/ Node get(int n) {
        return list.get(n);
    }

    @Override public Node set(int n, @Nullable Node node) {
        return (list.set(n, node));
    }

    @Override public boolean contains(@Nullable Object o) {
        return list.contains(o);
    }

    @Override public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override public int indexOf(@Nullable Object o) {
        return list.indexOf(o);
    }

    @Override public int lastIndexOf(@Nullable Object o) {
        return list.lastIndexOf(o);
    }

    @SuppressWarnings("return.type.incompatible")
    @Override public Object[] toArray() {
        // Per JDK specification, must return an Object[] not Node[]; see e.g.
        // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6260652
        return list.toArray();
    }

    @SuppressWarnings("return.type.incompatible")
    @Override public <T> @Nullable T[] toArray(T @Nullable [] a) {
        return list.toArray(a);
    }

    @Override public boolean add(@Nullable Node node) {
        return list.add(node);
    }

    @Override public void add(int index, @Nullable Node element) {
        list.add(index, element);
    }

    @Override public boolean addAll(Collection<? extends @Nullable Node> c) {
        return list.addAll(c);
    }

    @Override public boolean addAll(int index, Collection<? extends @Nullable Node> c) {
        return list.addAll(index, c);
    }

    @Override public void clear() {
        list.clear();
    }

    @Override public boolean remove(@Nullable Object o) {
        return list.remove(o);
    }

    @Override public Node remove(int index) {
        return (list.remove(index));
    }

    @Override public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    // NodeList-specific methods

    public List<@Nullable Node> getList() {
        return list;
    }

     public NodeList clone(ParserPos pos) {
        return new NodeList(list, pos);
    }









    public static boolean isEmptyList(final Node node) {
        return node instanceof NodeList
                && ((NodeList) node).isEmpty();
    }

    public static NodeList of(Node node1) {
        final List<@Nullable Node> list = new ArrayList<>(1);
        list.add(node1);
        return new NodeList(ParserPos.ZERO, list);
    }

    public static NodeList of(Node node1, Node node2) {
        final List<@Nullable Node> list = new ArrayList<>(2);
        list.add(node1);
        list.add(node2);
        return new NodeList(ParserPos.ZERO, list);
    }

    public static NodeList of(Node node1, Node node2, @Nullable Node... nodes) {
        final List<@Nullable Node> list = new ArrayList<>(nodes.length + 2);
        list.add(node1);
        list.add(node2);
        Collections.addAll(list, nodes);
        return new NodeList(ParserPos.ZERO, list);
    }


}
