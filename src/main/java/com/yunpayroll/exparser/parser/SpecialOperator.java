package com.yunpayroll.exparser.parser;


import java.util.function.Predicate;

/**
 * Generic operator for nodes with special syntax.
 */
public class SpecialOperator extends Operator {
    //~ Constructors -----------------------------------------------------------

    public SpecialOperator(
            String name,
            SqlKind kind) {
        this(name, kind, 2);
    }

    public SpecialOperator(
            String name,
            SqlKind kind,
            int prec) {
        this(name, kind, prec, true );
    }

    public SpecialOperator(
            String name,
            SqlKind kind,
            int prec,
            boolean leftAssoc) {
        super(
                name,
                kind,
                prec,
                leftAssoc );
    }


    public interface TokenSequence {
        int size();
        Operator op(int i);
        ParserPos pos(int i);
        boolean isOp(int i);
        Node node(int i);
        void replaceSublist(int start, int end, Node e);

        /** Creates a parser whose token sequence is a copy of a subset of this
         * token sequence. */
        PrecedenceClimbingParser parser(int start,
                                        Predicate<PrecedenceClimbingParser.Token> predicate);
    }
    public SpecialOperator.ReduceResult reduceExpr(
            int ordinal,
            SpecialOperator.TokenSequence list) {
        throw new RuntimeException();
    }

    public class ReduceResult {
        public final int startOrdinal;
        public final int endOrdinal;
        public final Node node;

        public ReduceResult(int startOrdinal, int endOrdinal, Node node) {
            this.startOrdinal = startOrdinal;
            this.endOrdinal = endOrdinal;
            this.node = node;
        }
    }
}
