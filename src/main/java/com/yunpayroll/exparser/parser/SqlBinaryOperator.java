//package com.yunpayroll.exparser.parser;
//
//
//public class SqlBinaryOperator extends SqlOperator {
//
//
//    public SqlBinaryOperator(String name, SqlKind kind, int prec, boolean leftAssoc) {
//        super(name, kind, leftPrec(prec, leftAssoc), rightPrec(prec, leftAssoc));
//    }
//
//
//
//    public String getSignatureTemplate(int operandsCount) {
//        return "{1} {0} {2}";
//    }
//
//    boolean needsSpace() {
//        return !this.getName().equals(".");
//    }
//
//
//
//
//
//
//
//}
