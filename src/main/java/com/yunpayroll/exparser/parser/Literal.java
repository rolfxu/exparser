package com.yunpayroll.exparser.parser;


import java.math.BigDecimal;

public abstract class Literal extends Node {



    public Literal(ParserPos pos){
        super(pos);
    }

    public Literal(ParserPos pos, Object value){
        super(pos);
    }



    public static Literal createBoolean(
            boolean b,
            ParserPos pos) {
        return b ? new BooleanLiteral(pos,Boolean.TRUE)
                : new BooleanLiteral(pos,Boolean.FALSE);
    }

    public static NumericLiteral createNegative(NumericLiteral num, ParserPos pos) {
        return new NumericLiteral(((BigDecimal)num.getValue()).negate(), num.getPrec(), num.getScale(), num.isExact(), pos);
    }

    public static NumericLiteral createExactNumeric(String s, ParserPos pos) {
        int i = s.indexOf(46);
        BigDecimal value;
        int prec;
        int scale;
        if (i >= 0 && s.length() - 1 != i) {
            value = SqlParserUtil.parseDecimal(s);
            scale = s.length() - i - 1;

            assert scale == value.scale() : s;

            prec = s.length() - 1;
        } else if (i >= 0 && s.length() - 1 == i) {
            value = SqlParserUtil.parseInteger(s.substring(0, i));
            scale = 0;
            prec = s.length() - 1;
        } else {
            value = SqlParserUtil.parseInteger(s);
            scale = 0;
            prec = s.length();
        }

        return new NumericLiteral(value, prec, scale, true, pos);
    }

    public static NumericLiteral createApproxNumeric(String s, ParserPos pos) {
        BigDecimal value = SqlParserUtil.parseDecimal(s);
        return new NumericLiteral(value, (Integer)null, (Integer)null, false, pos);
    }

    public static CharStringLiteral createCharString(String s, ParserPos pos) {
        return createCharString(s, (String)null, pos);
    }

    public static CharStringLiteral createCharString(String s, String charSet, ParserPos pos) {
        return new CharStringLiteral(pos,s );
    }

    @Override
    public String toString() {
        return toValue().toString();
    }
}
