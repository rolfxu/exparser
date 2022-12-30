package com.yunpayroll.exparser.parser;

import java.util.List;

public class EmprtyVisitor implements Visitor{


    @Override
    public void visit(BooleanLiteral booleanLiteral) {
        System.out.println(booleanLiteral);
    }

    @Override
    public void visit(CharStringLiteral charStringLiteral) {
        System.out.println(charStringLiteral);
    }

    @Override
    public void visit(Identifier identifier) {
        System.out.println(identifier);
    }

    @Override
    public void visit(Literal identifier) {
        System.out.println(identifier);
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
        System.out.println(identifier);
    }

    @Override
    public void visit(ParenthesizedNode identifier) {
        identifier.getNode().visit(this);
    }

    @Override
    public void visit(BinaryOperator identifier) {
        System.out.println(identifier.getName());
    }

    @Override
    public void visit(PrefixOperator identifier) {
        System.out.println(identifier.getName());
    }

    @Override
    public void visit(NodeList nodes) {
        System.out.println(nodes);
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
