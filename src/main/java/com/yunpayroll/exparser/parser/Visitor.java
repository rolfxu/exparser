package com.yunpayroll.exparser.parser;

public interface Visitor {

   void visit(BooleanLiteral booleanLiteral);


    void visit(CharStringLiteral booleanLiteral);


    void visit(Identifier identifier );


    void visit(Literal identifier );
    void visit(NamedFunction identifier );

    void visit(NumericLiteral identifier );

    void visit(ParenthesizedNode identifier );


    void visit(BinaryOperator identifier );


    void visit(PrefixOperator identifier );


    void visit(NodeList nodes);

    void visit(OperatorNode operatorNode);
}
