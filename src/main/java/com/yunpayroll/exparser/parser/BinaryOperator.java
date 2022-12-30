package com.yunpayroll.exparser.parser;

import java.util.List;

public class BinaryOperator extends Operator{


    private List<? extends Node> operandList;
    public BinaryOperator(String name, SqlKind kind, int prec, boolean leftAssoc) {
        super(name, kind, leftPrec(prec, leftAssoc), rightPrec(prec, leftAssoc));
    }

    public Node getLeft(){
        return operandList.get(0);
    }


    public Node getRight(){
        return operandList.get(1);
    }

    @Override
    public String toString() {
        return   this.getName() ;
    }

    public List<? extends Node> getOperandList() {
        return operandList;
    }

//    public void setOperandList(List<? extends Node> operandList) {
//        this.operandList = operandList;
//    }


}
