package com.yunpayroll.exparser.parser.operator;

import com.yunpayroll.exparser.parser.Literal;
import com.yunpayroll.exparser.parser.Node;

import java.math.BigDecimal;

public class BinaryCalc {

    private String op;

    private Object left;

    private Object right;

    public BinaryCalc( String op,Object left ,Object right ){
        this.op = op;

        this.left = left;

        this.right = right;
    }

    public Object getValue(){
        switch (op) {
            case "+":
                return add(left,right);
            case "-":

                return subtract(left,right);
            case "*":

                return multiply(left,right);
            case "/":
                return divide(left,right);
            default:
                return null;
        }
    }

    private Object add(Object a,Object b){
        if( a instanceof BigDecimal && b instanceof BigDecimal) {
            return  ((BigDecimal) a).add((BigDecimal) b);
        }

        return null;
    }
    private Object subtract(Object a,Object b){
        if( a instanceof BigDecimal && b instanceof BigDecimal) {
            return  ((BigDecimal) a).subtract((BigDecimal) b);
        }

        return null;
    }

    private Object multiply(Object a,Object b){
        if( a instanceof BigDecimal && b instanceof BigDecimal) {
            return  ((BigDecimal) a).multiply((BigDecimal) b);
        }

        return null;
    }
    private Object divide(Object a,Object b){
        if( a instanceof BigDecimal && b instanceof BigDecimal) {
            return  ((BigDecimal) a).divide((BigDecimal) b, BigDecimal.ROUND_HALF_UP);
        }

        return null;
    }
}
