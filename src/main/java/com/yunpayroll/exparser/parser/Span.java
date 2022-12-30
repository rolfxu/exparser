package com.yunpayroll.exparser.parser;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Builder for {@link ParserPos}.
 *
 * <p>Because it is mutable, it is convenient for keeping track of the
 * positions of the tokens that go into a non-terminal. It can be passed
 * into methods, which can add the positions of tokens consumed to it.
 *
 * <p>Some patterns:
 *
 * <ul>
 * <li>{@code final Span s;} declaration of a Span at the top of a production
 * <li>{@code s = span();} initializes s to a Span that includes the token we
 *   just saw; very often occurs immediately after the first token in the
 *   production
 * <li>{@code s.end(this);} adds the most recent token to span s and evaluates
 *   to a SqlParserPosition that spans from beginning to end; commonly used
 *   when making a call to a function
 * <li>{@code s.pos()} returns a position spanning all tokens in the list
 * <li>{@code s.add(node);} adds a Node's parser position to a span
 * <li>{@code s.addAll(nodeList);} adds several SqlNodes' parser positions to
 *   a span
 * <li>{@code s = Span.of();} initializes s to an empty Span, not even
 *   including the most recent token; rarely used
 * </ul>
 */
public final class Span {
    private final List<ParserPos> posList = new ArrayList<>();

    /** Use one of the {@link #of} methods. */
    private Span() {}

    /** Creates an empty Span. */
    public static Span of() {
        return new Span();
    }

    /** Creates a Span with one position. */
    public static Span of(ParserPos p) {
        return new Span().add(p);
    }

    /** Creates a Span of one node. */
    public static Span of(Node n) {
        return new Span().add(n);
    }

    /** Creates a Span between two nodes. */
    public static Span of(Node n0, Node n1) {
        return new Span().add(n0).add(n1);
    }

    /** Creates a Span of a list of nodes. */
    public static Span of(Collection<? extends Node> nodes) {
        return new Span().addAll(nodes);
    }

    /** Creates a Span of a node list. */
    public static Span of(NodeList nodeList) {
        // SqlNodeList has its own position, so just that position, not all of the
        // constituent nodes.
        return new Span().add(nodeList);
    }

    /** Adds a node's position to the list,
     * and returns this Span. */
    public Span add(Node n) {
        return add(n.getParserPosition());
    }

    /** Adds a node's position to the list if the node is not null,
     * and returns this Span. */
    public Span addIf(Node n) {
        return n == null ? this : add(n);
    }

    /** Adds a position to the list,
     * and returns this Span. */
    public Span add(ParserPos pos) {
        posList.add(pos);
        return this;
    }

    /** Adds the positions of a collection of nodes to the list,
     * and returns this Span. */
    public Span addAll(Iterable<? extends Node> nodes) {
        for (Node node : nodes) {
            add(node);
        }
        return this;
    }

    /** Adds the position of the last token emitted by a parser to the list,
     * and returns this Span. */
    public Span add(ExparserImpl parser) {
        try {
            final ParserPos pos = parser.getPos();
            return add(pos);
        } catch (Exception e) {
            // getPos does not really throw an exception
            throw new AssertionError(e);
        }
    }

    /** Returns a position spanning the earliest position to the latest.
     * Does not assume that the positions are sorted.
     * Throws if the list is empty. */
    public ParserPos pos() {
        return ParserPos.sum(posList);
    }

    /** Adds the position of the last token emitted by a parser to the list,
     * and returns a position that covers the whole range. */
    public ParserPos end(ExparserImpl parser) {
        return add(parser).pos();
    }

    /** Adds a node's position to the list,
     * and returns a position that covers the whole range. */
    public ParserPos end(Node n) {
        return add(n).pos();
    }

    /** Clears the contents of this Span, and returns this Span. */
    public Span clear() {
        posList.clear();
        return this;
    }
}
