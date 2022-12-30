package com.yunpayroll.exparser.parser;

import java.util.Iterator;
import java.util.List;

public class Identifier extends Node {

    private List<String> name;
    public Identifier(List<String> name, ParserPos pos) {
        super(pos);
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(name!=null) {
            for(Iterator<String> it = name.iterator();it.hasNext();) {
                String iname = it.next();
                stringBuilder.append(iname);
                if( it.hasNext() ) {
                    stringBuilder.append(".");
                }
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public Object toValue() {
        return null;
    }

    @Override
    void visit(Visitor visitor) {
        visitor.visit(this);
    }
}
