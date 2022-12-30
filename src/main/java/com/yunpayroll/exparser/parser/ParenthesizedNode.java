package com.yunpayroll.exparser.parser;

public class ParenthesizedNode  extends Node {

    private Node node;

    @Override
    public Object toValue() {
        return null;
    }

    @Override
    void visit(Visitor visitor) {
        visitor.visit(this);
    }

    public ParenthesizedNode(ParserPos pos) {
        super(pos);
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "("+node.toString()+")";
    }
}
