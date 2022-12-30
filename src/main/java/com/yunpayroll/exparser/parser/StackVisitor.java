package com.yunpayroll.exparser.parser;

import java.util.List;
import java.util.Stack;

public class StackVisitor implements Visitor{

    private Stack<Object> stack = new Stack<>();

    public Stack<Object> getStack() {
        return stack;
    }

    @Override
    public void visit(BooleanLiteral booleanLiteral) {
        stack.push(booleanLiteral);
    }

    @Override
    public void visit(CharStringLiteral charStringLiteral) {
        stack.push(charStringLiteral);
    }

    @Override
    public void visit(Identifier identifier) {
        stack.push(identifier);
    }

    @Override
    public void visit(Literal identifier) {
        stack.push(identifier);
    }

    @Override
    public void visit(NamedFunction identifier) {
        List<Node> nodes = identifier.args;
        for(Node node: nodes) {
            node.visit(this);
        }
    }

    @Override
    public void visit(NumericLiteral identifier) {
        stack.push(identifier);
    }

    @Override
    public void visit(ParenthesizedNode identifier) {
        identifier.getNode().visit(this);
    }

    @Override
    public void visit(BinaryOperator identifier) {
        stack.push(identifier);
    }

    @Override
    public void visit(PrefixOperator identifier) {
        stack.push(identifier);
    }

    @Override
    public void visit(NodeList nodes) {
        stack.push(nodes);
    }

    @Override
    public void visit(OperatorNode operatorNode) {
        Operator operator = operatorNode.getOperator();
        if( operator instanceof PrefixOperator ) {
            visit((PrefixOperator)operator);
            for(Node node: operatorNode.getList()  )
                node.visit(this);
        } else if( operator instanceof BinaryOperator){
            BinaryOperator binaryOperator =(BinaryOperator) operator;
            visit(binaryOperator);
            for(Node node: operatorNode.getList()  )
                node.visit(this);
        }
    }
}
