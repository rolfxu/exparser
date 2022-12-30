package com.yunpayroll.exparser.parser;

import java.util.List;

public class OperatorNode  extends Node{

    private Operator operator;

    private List<Node> list;


    public OperatorNode(Operator operator, List<Node> list, ParserPos pos ) {
        super(pos);
        this.operator = operator;
        this.list = list;
    }

    public OperatorNode(ParserPos pos) {
        super(pos);
    }

    @Override
    public String toString() {
        if(operator instanceof PrefixOperator) {
            return operator.toString()+list.get(0);
        }
        return list.get(0)+operator.toString()+list.get(1);
    }

    @Override
    public Object toValue() {
        return null;
    }

    @Override
    void visit(Visitor visitor) {
        visitor.visit(this);
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public List<Node> getList() {
        return list;
    }

    public void setList(List<Node> list) {
        this.list = list;
    }
}
