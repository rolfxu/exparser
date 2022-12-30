package com.yunpayroll.exparser.parser;



public class StdOperatorTable {
    public static final BinaryOperator EQUALS = new BinaryOperator("=",SqlKind.EQUALS,30,true);
    public static final BinaryOperator GREATER_THAN=new BinaryOperator(">",SqlKind.GREATER_THAN,30,true);
    public static final BinaryOperator LESS_THAN=new BinaryOperator("<",SqlKind.LESS_THAN,30,true);
    public static final BinaryOperator GREATER_THAN_OR_EQUAL=new BinaryOperator(">=",SqlKind.GREATER_THAN_OR_EQUAL,30,true);
    public static final BinaryOperator LESS_THAN_OR_EQUAL=new BinaryOperator("<=",SqlKind.LESS_THAN_OR_EQUAL,30,true);
    public static final BinaryOperator DOT=new BinaryOperator(".",SqlKind.DOT,100,true);;
    public static final BinaryOperator NOT_EQUALS=new BinaryOperator("!=",SqlKind.NOT_EQUALS,30,true);;
    public static final BinaryOperator PLUS=new BinaryOperator("+",SqlKind.PLUS,40,true);;

    public static final BinaryOperator MINUS=new BinaryOperator("-",SqlKind.MINUS,40,true);;

    public static final BinaryOperator MULTIPLY=new BinaryOperator("*",SqlKind.TIMES,60,true);;


    public static final BinaryOperator DIVIDE=new BinaryOperator("/",SqlKind.DIVIDE,60,true);;
    public static final BinaryOperator MOD=new BinaryOperator("%",SqlKind.MOD,60,true);

}
