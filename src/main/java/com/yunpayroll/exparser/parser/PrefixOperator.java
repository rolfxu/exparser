package com.yunpayroll.exparser.parser;

public class PrefixOperator extends Operator {
    private String prefix;


    protected PrefixOperator(String name, SqlKind kind, int leftPrecedence, int rightPrecedence) {
        super(name, kind, leftPrecedence, rightPrecedence);
    }
    protected PrefixOperator( String name, SqlKind kind, int prec, boolean leftAssoc) {
        this(name, kind, leftPrec(prec, leftAssoc), rightPrec(prec, leftAssoc));
    }
}
