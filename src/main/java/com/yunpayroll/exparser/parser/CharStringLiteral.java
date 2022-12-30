package com.yunpayroll.exparser.parser;

public class CharStringLiteral extends Literal  {

    private String a ;

    @Override
    void visit(Visitor visitor) {
        visitor.visit(this);
    }

    public CharStringLiteral(ParserPos pos) {
        super(pos);
    }

    public CharStringLiteral(ParserPos pos, Object value) {
        super(pos, value);
        this.a = (String)value;
    }

    @Override
    public Object toValue() {
        return a;
    }

    @Override
    public String toString() {
        return a;
    }
}
