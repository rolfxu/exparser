package com.yunpayroll.exparser.parser;

public class ParserException extends Exception{



    private final ParserPos pos;
    private final int[][] expectedTokenSequences;
    private final String[] tokenImages;

    /**
     * The original exception thrown by the generated parser. Unfortunately,
     * each generated parser throws exceptions of a different class. So, we keep
     * the exception for forensic purposes, but don't print it publicly.
     *
     * <p>Also, make it transient, because it is a ParseException generated by
     * JavaCC and contains a non-serializable Token.
     */
    private final transient Throwable parserException;


    ParserException(
            String message,
            ParserPos pos,
            int[][] expectedTokenSequences,
            String[] tokenImages,
            Throwable parserException){
        this.pos = pos;
        this.expectedTokenSequences = expectedTokenSequences;
        this.tokenImages = tokenImages;
        this.parserException = parserException;
    }
}
