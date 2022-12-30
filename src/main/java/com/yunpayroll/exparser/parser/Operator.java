package com.yunpayroll.exparser.parser;


import com.google.common.collect.Lists;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Arrays;
import java.util.List;

public abstract class Operator  {
    public static final String NL = System.getProperty("line.separator");
    public static final int MDX_PRECEDENCE = 200;
    private  String name;
    public  SqlKind kind;
    private  int leftPrec;
    private  int rightPrec;


    protected Operator( String name, SqlKind kind, int leftPrecedence, int rightPrecedence) {
        assert kind != null;
        this.name = name;
        this.kind = kind;
        this.leftPrec = leftPrecedence;
        this.rightPrec = rightPrecedence;

    }

    protected Operator( String name, SqlKind kind, int prec, boolean leftAssoc) {
        this(name, kind, leftPrec(prec, leftAssoc), rightPrec(prec, leftAssoc));
    }

    protected static int leftPrec(int prec, boolean leftAssoc) {
        assert prec % 2 == 0;

        if (!leftAssoc) {
            ++prec;
        }

        return prec;
    }

    protected static int rightPrec(int prec, boolean leftAssoc) {
        assert prec % 2 == 0;

        if (leftAssoc) {
            ++prec;
        }

        return prec;
    }

    public Node createCall(
            ParserPos pos,
            List<Node> operandList) {
        pos = pos.plusAll( operandList);
//        return new BinaryOperatorNode(pos,this,operandList );
        return new OperatorNode( this,operandList,pos );
    }

    public String getName() {
        return this.name;
    }

    public Identifier getNameAsId() {
        List<String> list = Lists.newArrayList(this.getName());
        return new Identifier(list, ParserPos.ZERO);
    }

    @Pure
    public SqlKind getKind() {
        return this.kind;
    }

    public String toString() {
        return this.name;
    }

    public int getLeftPrec() {
        return this.leftPrec;
    }

    public int getRightPrec() {
        return this.rightPrec;
    }

}