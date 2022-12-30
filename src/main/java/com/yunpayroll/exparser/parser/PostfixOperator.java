package com.yunpayroll.exparser.parser;

public class PostfixOperator extends Operator {
    protected PostfixOperator(String name, SqlKind kind, int leftPrecedence, int rightPrecedence) {
        super(name, kind, leftPrecedence, rightPrecedence);
    }

    protected PostfixOperator(String name, SqlKind kind, int prec, boolean leftAssoc) {
        super(name, kind, prec, leftAssoc);
    }
}
