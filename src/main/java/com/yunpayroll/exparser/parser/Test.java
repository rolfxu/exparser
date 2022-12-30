package com.yunpayroll.exparser.parser;

import com.yunpayroll.exparser.parser.operator.BinaryCalc;

import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;

public class Test {
    public static void main(String[] args) throws ParseException {
        System.out.println(SqlKind.EQUALS);
//        Reader reader = new StringReader("'asdfasdfasdfasd'+\"dfdfdfd\"+sqrt(sin(1,1+2,a.b.cd)*(3+(((2+4))))*2+96-0)");


        int a= -1;
        int b = --a;
//        Reader reader = new StringReader("-(1000000000)");
        Reader reader = new StringReader("a+n*10 * 10 + 1 + 2 * 3 + 5 * 2");
        ExparserImpl exparser = new ExparserImpl(reader);
        Node node = exparser.ExpressionEof();
        System.out.println(node);
        StackVisitor visitor = new StackVisitor();
        node.visit(visitor);

        Stack<Object> stack = visitor.getStack();
        Stack<Object> tmp = new Stack<>();
         while(!stack.isEmpty()) {
            Object obj =  stack.pop();
            if(obj instanceof Literal) {
                tmp.push( ((Literal)obj).toValue() );
            } else if(obj instanceof Operator) {
                if( obj instanceof  BinaryOperator) {
                    tmp.push(new BinaryCalc( ((BinaryOperator) obj).getName(),tmp.pop()
                            ,tmp.pop() ).getValue());
                } else {

                }
            }

        }
        System.out.println(tmp);
    }
}
