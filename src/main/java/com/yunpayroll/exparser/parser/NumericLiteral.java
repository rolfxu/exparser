package com.yunpayroll.exparser.parser;


import java.math.BigDecimal;

public class NumericLiteral extends Literal {
    private Integer prec;
    private Integer scale;
    private boolean isExact;
    private BigDecimal value;

    protected NumericLiteral(BigDecimal value, Integer prec, Integer scale, boolean isExact, ParserPos pos) {
        super(pos,value );
        this.value = value;
        this.prec = prec;
        this.scale = scale;
        this.isExact = isExact;
    }

    @Override
    void visit(Visitor visitor) {
        visitor.visit(this);
    }

    public NumericLiteral(ParserPos pos) {
        super(pos);
    }

    public Integer getPrec() {
        return this.prec;
    }

    public Integer getScale() {
        return this.scale;
    }

    public boolean isExact() {
        return this.isExact;
    }

    public NumericLiteral clone(ParserPos pos) {
        return new NumericLiteral((BigDecimal)this.value, this.getPrec(), this.getScale(), this.isExact, pos);
    }

    public BigDecimal getValue(){
        return  value;
    }


    public BigDecimal toValue() {
        BigDecimal bd = (BigDecimal)this.value;
        return  this.value ;
    }


    public boolean isInteger() {
        return 0 == this.scale;
    }
}
