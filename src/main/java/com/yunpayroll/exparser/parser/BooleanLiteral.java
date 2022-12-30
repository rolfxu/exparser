package com.yunpayroll.exparser.parser;

 class BooleanLiteral extends Literal   {

    private Boolean value;

    @Override
    void visit(Visitor visitor) {
        visitor.visit(this);
    }

    public BooleanLiteral(ParserPos pos) {
        super(pos);
    }

    public BooleanLiteral(ParserPos pos, Object value) {
        super(pos, value);
        this.value = (Boolean) value;
    }

    @Override
   public Object toValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
