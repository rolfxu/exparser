package com.yunpayroll.exparser.parser;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ParserPos {
    //~ Static fields/initializers ---------------------------------------------

    /**
     * ParserPos representing line one, character one. Use this if the node
     * doesn't correspond to a position in piece of SQL text.
     */
    public static final ParserPos ZERO = new ParserPos(0, 0);

    /** Same as {@link #ZERO} but always quoted. **/
    public static final ParserPos QUOTED_ZERO = new QuotedParserPos(0, 0, 0, 0);

    private static final long serialVersionUID = 1L;

    //~ Instance fields --------------------------------------------------------

    private final int lineNumber;
    private final int columnNumber;
    private final int endLineNumber;
    private final int endColumnNumber;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new parser position.
     */
    public ParserPos(
            int lineNumber,
            int columnNumber) {
        this(lineNumber, columnNumber, lineNumber, columnNumber);
    }

    /**
     * Creates a new parser range.
     */
    public ParserPos(
            int startLineNumber,
            int startColumnNumber,
            int endLineNumber,
            int endColumnNumber) {
        this.lineNumber = startLineNumber;
        this.columnNumber = startColumnNumber;
        this.endLineNumber = endLineNumber;
        this.endColumnNumber = endColumnNumber;
        assert startLineNumber < endLineNumber
                || startLineNumber == endLineNumber
                && startColumnNumber <= endColumnNumber;
    }

    //~ Methods ----------------------------------------------------------------

    @Override public int hashCode() {
        return Objects.hash(lineNumber, columnNumber, endLineNumber, endColumnNumber);
    }

    @Override public boolean equals( Object o) {
        return o == this
                || o instanceof ParserPos
                && this.lineNumber == ((ParserPos) o).lineNumber
                && this.columnNumber == ((ParserPos) o).columnNumber
                && this.endLineNumber == ((ParserPos) o).endLineNumber
                && this.endColumnNumber == ((ParserPos) o).endColumnNumber;
    }

    /** Returns 1-based starting line number. */
    public int getLineNum() {
        return lineNumber;
    }

    /** Returns 1-based starting column number. */
    public int getColumnNum() {
        return columnNumber;
    }

    /** Returns 1-based end line number (same as starting line number if the
     * ParserPos is a point, not a range). */
    public int getEndLineNum() {
        return endLineNumber;
    }

    /** Returns 1-based end column number (same as starting column number if the
     * ParserPos is a point, not a range). */
    public int getEndColumnNum() {
        return endColumnNumber;
    }

    /** Returns a {@code ParserPos} the same as this but quoted. */
    public ParserPos withQuoting(boolean quoted) {
        if (isQuoted() == quoted) {
            return this;
        } else if (quoted) {
            return new QuotedParserPos(lineNumber, columnNumber, endLineNumber,
                    endColumnNumber);
        } else {
            return new ParserPos(lineNumber, columnNumber, endLineNumber,
                    endColumnNumber);
        }
    }

    /** Returns whether this ParserPos is quoted. */
    public boolean isQuoted() {
        return false;
    }


    /**
     * Combines this parser position with another to create a
     * position that spans from the first point in the first to the last point
     * in the other.
     */
    public ParserPos plus(ParserPos pos) {
        return new ParserPos(
                getLineNum(),
                getColumnNum(),
                pos.getEndLineNum(),
                pos.getEndColumnNum());
    }

    /**
     * Combines this parser position with an array of positions to create a
     * position that spans from the first point in the first to the last point
     * in the other.
     */
    public ParserPos plusAll(Node[] nodes) {
        final PosBuilder b = new PosBuilder(this);
        for (Node node : nodes) {
            if (node != null) {
                b.add(node.getParserPosition());
            }
        }
        return b.build(this);
    }

    /**
     * Combines this parser position with a list of positions.
     */
    public ParserPos plusAll(Collection<? extends Node> nodes) {
        final PosBuilder b = new PosBuilder(this);
        for (Node node : nodes) {
            if (node != null) {
                b.add(node.getParserPosition());
            }
        }
        return b.build(this);
    }

    /**
     * Combines the parser positions of an array of nodes to create a position
     * which spans from the beginning of the first to the end of the last.
     */
    public static ParserPos sum(final Node[] nodes) {
        if (nodes.length == 0) {
            throw new AssertionError();
        }
        final ParserPos pos0 = nodes[0].getParserPosition();
        if (nodes.length == 1) {
            return pos0;
        }
        final PosBuilder b = new PosBuilder(pos0);
        for (int i = 1; i < nodes.length; i++) {
            b.add(nodes[i].getParserPosition());
        }
        return b.build(pos0);
    }

    /**
     * Combines the parser positions of a list of nodes to create a position
     * which spans from the beginning of the first to the end of the last.
     */
    public static ParserPos sum(final List<? extends Node> nodes) {
        if (nodes.size() == 0) {
            throw new AssertionError();
        }
        ParserPos pos0 = nodes.get(0).getParserPosition();
        if (nodes.size() == 1) {
            return pos0;
        }
        final PosBuilder b = new PosBuilder(pos0);
        for (int i = 1; i < nodes.size(); i++) {
            b.add(nodes.get(i).getParserPosition());
        }
        return b.build(pos0);
    }

    /** Returns a position spanning the earliest position to the latest.
     * Does not assume that the positions are sorted.
     * Throws if the list is empty. */
    public static ParserPos sum(Iterable<ParserPos> poses) {
        final List<ParserPos> list =
                poses instanceof List
                        ? (List<ParserPos>) poses
                        : Lists.newArrayList(poses);
        if (list.size() == 0) {
            throw new AssertionError();
        }
        final ParserPos pos0 = list.get(0);
        if (list.size() == 1) {
            return pos0;
        }
        final PosBuilder b = new PosBuilder(pos0);
        for (int i = 1; i < list.size(); i++) {
            b.add(list.get(i));
        }
        return b.build(pos0);
    }

    public boolean overlaps(ParserPos pos) {
        return startsBefore(pos) && endsAfter(pos)
                || pos.startsBefore(this) && pos.endsAfter(this);
    }

    private boolean startsBefore(ParserPos pos) {
        return lineNumber < pos.lineNumber
                || lineNumber == pos.lineNumber
                && columnNumber <= pos.columnNumber;
    }

    private boolean endsAfter(ParserPos pos) {
        return endLineNumber > pos.endLineNumber
                || endLineNumber == pos.endLineNumber
                && endColumnNumber >= pos.endColumnNumber;
    }

    public boolean startsAt(ParserPos pos) {
        return lineNumber == pos.lineNumber
                && columnNumber == pos.columnNumber;
    }

    /** Parser position for an identifier segment that is quoted. */
    private static class QuotedParserPos extends ParserPos {
        QuotedParserPos(int startLineNumber, int startColumnNumber,
                        int endLineNumber, int endColumnNumber) {
            super(startLineNumber, startColumnNumber, endLineNumber,
                    endColumnNumber);
        }

        @Override public boolean isQuoted() {
            return true;
        }
    }

    /** Builds a parser position. */
    private static class PosBuilder {
        private int line;
        private int column;
        private int endLine;
        private int endColumn;

        PosBuilder(ParserPos p) {
            this(p.lineNumber, p.columnNumber, p.endLineNumber, p.endColumnNumber);
        }

        PosBuilder(int line, int column, int endLine, int endColumn) {
            this.line = line;
            this.column = column;
            this.endLine = endLine;
            this.endColumn = endColumn;
        }

        void add(ParserPos pos) {
            if (pos.equals(ParserPos.ZERO)) {
                return;
            }
            int testLine = pos.getLineNum();
            int testColumn = pos.getColumnNum();
            if (testLine < line || testLine == line && testColumn < column) {
                line = testLine;
                column = testColumn;
            }

            testLine = pos.getEndLineNum();
            testColumn = pos.getEndColumnNum();
            if (testLine > endLine || testLine == endLine && testColumn > endColumn) {
                endLine = testLine;
                endColumn = testColumn;
            }
        }

        ParserPos build(ParserPos p) {
            return p.lineNumber == line
                    && p.columnNumber == column
                    && p.endLineNumber == endLine
                    && p.endColumnNumber == endColumn
                    ? p
                    : build();
        }

        ParserPos build() {
            return new ParserPos(line, column, endLine, endColumn);
        }
    }
}
