package com.yunpayroll.exparser.parser;


import java.util.Iterator;
import java.util.List;

public class NamedFunction extends Node {

    String fucName;

    List<Node> args;

    @Override
    public Object toValue() {
        return null;
    }

    @Override
    void visit(Visitor visitor) {
        visitor.visit(this);
    }

    public NamedFunction(ParserPos pos) {
        super(pos);
    }

    public NamedFunction(ParserPos pos, String fucName, List<Node> args) {
        super(pos);
        this.fucName = fucName;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fucName).append("(");
        if( args!=null) {
            for(Iterator<Node> iterator = args.iterator();iterator.hasNext();) {
                Node node = iterator.next();
                stringBuilder.append(node);
                if( iterator.hasNext() ) {
                    stringBuilder.append(" , ");
                }
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
