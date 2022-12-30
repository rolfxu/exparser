package com.yunpayroll.exparser.parser;

import java.util.Objects;

public abstract class Node {

    protected final ParserPos pos;


    public ParserPos getParserPosition() {
        return pos;
    }
    public abstract Object toValue();

    abstract void visit(Visitor visitor);


    public Node(ParserPos pos) {
        this.pos = Objects.requireNonNull(pos, "pos");
    }


}
