package com.yunpayroll.exparser.parser;


import com.google.common.base.Preconditions;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Utility methods relating to parsing SQL.
 */
public final class SqlParserUtil {
    //~ Static fields/initializers ---------------------------------------------

    static final Logger LOGGER = LoggerFactory.getLogger(SqlParserUtil.class);

    //~ Constructors -----------------------------------------------------------

    private SqlParserUtil() {
    }

    //~ Methods ----------------------------------------------------------------



    /**
     * Converts the contents of an sql quoted string literal into the
     * corresponding Java string representation (removing leading and trailing
     * quotes and unescaping internal doubled quotes).
     */
    public static String parseString(String s) {
        int i = s.indexOf("'"); // start of body
        if (i > 0) {
            s = s.substring(i);
        }
        return strip(s, "'", "'", "''" );
    }

    public static BigDecimal parseDecimal(String s) {
        return new BigDecimal(s);
    }

    public static BigDecimal parseInteger(String s) {
        return new BigDecimal(s);
    }

    // CHECKSTYLE: IGNORE 1
    /** @deprecated this method is not localized for Farrago standards */
    @Deprecated // to be removed before 2.0
    public static java.sql.Date parseDate(String s) {
        return java.sql.Date.valueOf(s);
    }

    // CHECKSTYLE: IGNORE 1
    /** @deprecated Does not parse SQL:99 milliseconds */
    @Deprecated // to be removed before 2.0
    public static java.sql.Time parseTime(String s) {
        return java.sql.Time.valueOf(s);
    }

    // CHECKSTYLE: IGNORE 1
    /** @deprecated this method is not localized for Farrago standards */
    @Deprecated // to be removed before 2.0
    public static java.sql.Timestamp parseTimestamp(String s) {
        return java.sql.Timestamp.valueOf(s);
    }











    /**
     * Parses a positive int. All characters have to be digits.
     *
     * @see Integer#parseInt(String)
     * @throws java.lang.NumberFormatException if invalid number or leading '-'
     */
    public static int parsePositiveInt(String value) {
        value = value.trim();
        if (value.charAt(0) == '-') {
            throw new NumberFormatException(value);
        }
        return Integer.parseInt(value);
    }

    /**
     * Parses a Binary string. SQL:99 defines a binary string as a hexstring
     * with EVEN nbr of hex digits.
     */
    @Deprecated // to be removed before 2.0
    public static byte[] parseBinaryString(String s) {
        s = s.replace(" ", "");
        s = s.replace("\n", "");
        s = s.replace("\t", "");
        s = s.replace("\r", "");
        s = s.replace("\f", "");
        s = s.replace("'", "");

        if (s.length() == 0) {
            return new byte[0];
        }
        assert (s.length() & 1) == 0; // must be even nbr of hex digits

        final int lengthToBe = s.length() / 2;
        s = "ff" + s;
        BigInteger bigInt = new BigInteger(s, 16);
        byte[] ret = new byte[lengthToBe];
        System.arraycopy(
                bigInt.toByteArray(),
                2,
                ret,
                0,
                ret.length);
        return ret;
    }

    /**
     * Converts a quoted identifier, unquoted identifier, or quoted string to a
     * string of its contents.
     *
     * <p>First, if {@code startQuote} is provided, {@code endQuote} and
     * {@code escape} must also be provided, and this method removes quotes.
     *
     * <p>Finally, converts the string to the provided casing.
     */
    public static String strip(String s, @Nullable String startQuote,
                               @Nullable String endQuote, @Nullable String escape) {
        if (startQuote != null) {
            return stripQuotes(s, Objects.requireNonNull(startQuote, "startQuote"),
                    Objects.requireNonNull(endQuote, "endQuote"), Objects.requireNonNull(escape, "escape") );
        } else {
            return s;
        }
    }

    /**
     * Unquotes a quoted string, using different quotes for beginning and end.
     */
    public static String stripQuotes(String s, String startQuote, String endQuote,
                                     String escape ) {
        assert startQuote.length() == 1;
        assert endQuote.length() == 1;
        assert s.startsWith(startQuote) && s.endsWith(endQuote) : s;
        s = s.substring(1, s.length() - 1).replace(escape, endQuote);
        return s;
    }


    /**
     * Trims a string for given characters from left and right. E.g.
     * {@code trim("aBaac123AabC","abBcC")} returns {@code "123A"}.
     */
    public static String trim(
            String s,
            String chars) {
        if (s.length() == 0) {
            return "";
        }

        int start;
        for (start = 0; start < s.length(); start++) {
            char c = s.charAt(start);
            if (chars.indexOf(c) < 0) {
                break;
            }
        }

        int stop;
        for (stop = s.length(); stop > start; stop--) {
            char c = s.charAt(stop - 1);
            if (chars.indexOf(c) < 0) {
                break;
            }
        }

        if (start >= stop) {
            return "";
        }

        return s.substring(start, stop);
    }


    /**
     * Returns the (1-based) line and column corresponding to a particular
     * (0-based) offset in a string.
     *
     * <p>Converse of {@link #lineColToIndex(String, int, int)}.
     */
    public static int[] indexToLineCol(String sql, int i) {
        int line = 0;
        int j = 0;
        while (true) {
            int prevj = j;
            j = nextLine(sql, j);
            if ((j < 0) || (j > i)) {
                return new int[]{line + 1, i - prevj + 1};
            }
            ++line;
        }
    }

    public static int nextLine(String sql, int j) {
        int rn = sql.indexOf("\r\n", j);
        int r = sql.indexOf("\r", j);
        int n = sql.indexOf("\n", j);
        if ((r < 0) && (n < 0)) {
            assert rn < 0;
            return -1;
        } else if ((rn >= 0) && (rn < n) && (rn <= r)) {
            return rn + 2; // looking at "\r\n"
        } else if ((r >= 0) && (r < n)) {
            return r + 1; // looking at "\r"
        } else {
            return n + 1; // looking at "\n"
        }
    }

    /**
     * Finds the position (0-based) in a string which corresponds to a given
     * line and column (1-based).
     *
     * <p>Converse of {@link #indexToLineCol(String, int)}.
     */
    public static int lineColToIndex(String sql, int line, int column) {
        --line;
        --column;
        int i = 0;
        while (line-- > 0) {
            i = nextLine(sql, i);
        }
        return i + column;
    }

    /**
     * Converts a string to a string with one or two carets in it. For example,
     * <code>addCarets("values (foo)", 1, 9, 1, 12)</code> yields "values
     * (^foo^)".
     */
    public static String addCarets(
            String sql,
            int line,
            int col,
            int endLine,
            int endCol) {
        String sqlWithCarets;
        int cut = lineColToIndex(sql, line, col);
        sqlWithCarets = sql.substring(0, cut) + "^"
                + sql.substring(cut);
        if ((col != endCol) || (line != endLine)) {
            cut = lineColToIndex(sqlWithCarets, endLine, endCol);
            if (line == endLine) {
                ++cut; // for caret
            }
            if (cut < sqlWithCarets.length()) {
                sqlWithCarets =
                        sqlWithCarets.substring(0, cut)
                                + "^" + sqlWithCarets.substring(cut);
            } else {
                sqlWithCarets += "^";
            }
        }
        return sqlWithCarets;
    }

    public static @Nullable String getTokenVal(String token) {
        // We don't care about the token which are not string
        if (!token.startsWith("\"")) {
            return null;
        }

        // Remove the quote from the token
        int startIndex = token.indexOf("\"");
        int endIndex = token.lastIndexOf("\"");
        String tokenVal = token.substring(startIndex + 1, endIndex);
        char c = tokenVal.charAt(0);
        if (Character.isLetter(c)) {
            return tokenVal;
        }
        return null;
    }



    @Deprecated // to be removed before 2.0
    public static String[] toStringArray(List<String> list) {
        return list.toArray(new String[0]);
    }

    public static Node[] toNodeArray(List<Node> list) {
        return list.toArray(new Node[0]);
    }

    public static Node[] toNodeArray(NodeList list) {
        return list.toArray(new Node[0]);
    }


    @Deprecated // to be removed before 2.0
    public static String rightTrim(
            String s,
            char c) {
        int stop;
        for (stop = s.length(); stop > 0; stop--) {
            if (s.charAt(stop - 1) != c) {
                break;
            }
        }
        if (stop > 0) {
            return s.substring(0, stop);
        }
        return "";
    }

    /**
     * Replaces a range of elements in a list with a single element. For
     * example, if list contains <code>{A, B, C, D, E}</code> then <code>
     * replaceSublist(list, X, 1, 4)</code> returns <code>{A, X, E}</code>.
     */
    public static <T> void replaceSublist(
            List<T> list,
            int start,
            int end,
            T o) {
        requireNonNull(list, "list");
        Preconditions.checkArgument(start < end);
        for (int i = end - 1; i > start; --i) {
            list.remove(i);
        }
        list.set(start, o);
    }

    /**
     * Converts a list of {expression, operator, expression, ...} into a tree,
     * taking operator precedence and associativity into account.
     */
    public static @Nullable Node toTree(List<@Nullable Object> list) {
        if (list.size() == 1
                && list.get(0) instanceof Node) {
            // Short-cut for the simple common case
            return (Node) list.get(0);
        }
        LOGGER.trace("Attempting to reduce {}", list);
        final OldTokenSequenceImpl tokenSequence = new OldTokenSequenceImpl(list);
        final Node node = toTreeEx(tokenSequence, 0, 0, SqlKind.OTHER);
        LOGGER.debug("Reduced {}", node);
        return node;
    }

    /**
     * Converts a list of {expression, operator, expression, ...} into a tree,
     * taking operator precedence and associativity into account.
     *
     * @param list        List of operands and operators. This list is modified as
     *                    expressions are reduced.
     * @param start       Position of first operand in the list. Anything to the
     *                    left of this (besides the immediately preceding operand)
     *                    is ignored. Generally use value 1.
     * @param minPrec     Minimum precedence to consider. If the method encounters
     *                    an operator of lower precedence, it doesn't reduce any
     *                    further.
     * @param stopperKind If not {@link SqlKind#OTHER}, stop reading the list if
     *                    we encounter a token of this kind.
     * @return the root node of the tree which the list condenses into
     */
    public static Node toTreeEx(SpecialOperator.TokenSequence list,
                                   int start, final int minPrec, final SqlKind stopperKind) {
        PrecedenceClimbingParser parser = list.parser(start,
                token -> {
                    if (token instanceof PrecedenceClimbingParser.Op) {
                        PrecedenceClimbingParser.Op tokenOp = (PrecedenceClimbingParser.Op) token;
                        final Operator op = ((ToTreeListItem) tokenOp.o()).op;
                        return stopperKind != SqlKind.OTHER
                                && op.kind == stopperKind
                                || minPrec > 0
                                && op.getLeftPrec() < minPrec;
                    } else {
                        return false;
                    }
                });
        final int beforeSize = parser.all().size();
        parser.partialParse();
        final int afterSize = parser.all().size();
        final Node node = convert(parser.all().get(0));
        list.replaceSublist(start, start + beforeSize - afterSize + 1, node);
        return node;
    }

    private static Node convert(PrecedenceClimbingParser.Token token) {
        switch (token.type) {
            case ATOM:
                return requireNonNull((Node) token.o);
            case CALL:
                final PrecedenceClimbingParser.Call call =
                        (PrecedenceClimbingParser.Call) token;
                final List<@Nullable Node> list = new ArrayList<>();
                for (PrecedenceClimbingParser.Token arg : call.args) {
                    list.add(convert(arg));
                }
                final ToTreeListItem item = (ToTreeListItem) call.op.o();

                Node firstItem = list.get(0);
                if (item.op instanceof PrefixOperator
                        && item.op.kind==SqlKind.MINUS
                        && firstItem instanceof NumericLiteral) {
                    return Literal.createNegative((NumericLiteral) firstItem,
                            item.pos.plusAll(list));
                }
                if (item.op instanceof PrefixOperator
                        && item.op.kind==SqlKind.PLUS
                        && firstItem instanceof NumericLiteral) {
                    return firstItem;
                }

                return item.op.createCall(item.pos.plusAll(list), list);
            default:
                throw new AssertionError(token);
        }
    }


    /**
     * Returns whether the reported ParseException tokenImage
     * allows SQL identifier.
     *
     * @param tokenImage The allowed tokens from the ParseException
     * @param expectedTokenSequences Expected token sequences
     *
     * @return true if SQL identifier is allowed
     */
    public static boolean allowsIdentifier(String[] tokenImage, int[][] expectedTokenSequences) {
        // Compares from tailing tokens first because the <IDENTIFIER>
        // was very probably at the tail.
        for (int i = expectedTokenSequences.length - 1; i >= 0; i--) {
            int[] expectedTokenSequence = expectedTokenSequences[i];
            for (int j = expectedTokenSequence.length - 1; j >= 0; j--) {
                if (tokenImage[expectedTokenSequence[j]].equals("<IDENTIFIER>")) {
                    return true;
                }
            }
        }

        return false;
    }

    //~ Inner Classes ----------------------------------------------------------

    /** The components of a collation definition, per the SQL standard. */
    public static class ParsedCollation {
        private final Charset charset;
        private final Locale locale;
        private final String strength;

        public ParsedCollation(
                Charset charset,
                Locale locale,
                String strength) {
            this.charset = charset;
            this.locale = locale;
            this.strength = strength;
        }

        public Charset getCharset() {
            return charset;
        }

        public Locale getLocale() {
            return locale;
        }

        public String getStrength() {
            return strength;
        }
    }


    public static class ToTreeListItem {
        private final Operator op;
        private final ParserPos pos;

        public ToTreeListItem(
                Operator op,
                ParserPos pos) {
            this.op = op;
            this.pos = pos;
        }

        @Override public String toString() {
            return op.toString();
        }

        public Operator getOperator() {
            return op;
        }

        public ParserPos getPos() {
            return pos;
        }
    }


    private static class TokenSequenceImpl
            implements SpecialOperator.TokenSequence {
        final List<PrecedenceClimbingParser.Token> list;
        final PrecedenceClimbingParser parser;

        private TokenSequenceImpl(PrecedenceClimbingParser parser) {
            this.parser = parser;
            this.list = parser.all();
        }

        @Override public PrecedenceClimbingParser parser(int start,
                                                         Predicate<PrecedenceClimbingParser.Token> predicate) {
            return parser.copy(start, predicate);
        }

        @Override public int size() {
            return list.size();
        }

        @Override public Operator op(int i) {
            ToTreeListItem o = (ToTreeListItem) requireNonNull(list.get(i).o,
                    () -> "list.get(" + i + ").o is null in " + list);
            return o.getOperator();
        }

        private static ParserPos pos(PrecedenceClimbingParser.Token token) {
            switch (token.type) {
                case ATOM:
                    return requireNonNull((Node) token.o, "token.o").getParserPosition();
                case CALL:
                    final PrecedenceClimbingParser.Call call =
                            (PrecedenceClimbingParser.Call) token;
                    ParserPos pos = ((ToTreeListItem) call.op.o()).pos;
                    for (PrecedenceClimbingParser.Token arg : call.args) {
                        pos = pos.plus(pos(arg));
                    }
                    return pos;
                default:
                    return requireNonNull((ToTreeListItem) token.o, "token.o").getPos();
            }
        }

        @Override public ParserPos pos(int i) {
            return pos(list.get(i));
        }

        @Override public boolean isOp(int i) {
            return list.get(i).o instanceof ToTreeListItem;
        }

        @Override public Node node(int i) {
            return convert(list.get(i));
        }

        @Override public void replaceSublist(int start, int end, Node e) {
            SqlParserUtil.replaceSublist(list, start, end, parser.atom(e));
        }
    }
    public static <E> List<E> skip(List<E> list, int fromIndex) {
        return fromIndex == 0 ? list : list.subList(fromIndex, list.size());
    }
    private static class OldTokenSequenceImpl
            implements SpecialOperator.TokenSequence {
        final List<@Nullable Object> list;

        private OldTokenSequenceImpl(List<@Nullable Object> list) {
            this.list = list;
        }

        @Override public PrecedenceClimbingParser parser(int start,
                                                         Predicate<PrecedenceClimbingParser.Token> predicate) {
            final PrecedenceClimbingParser.Builder builder =
                    new PrecedenceClimbingParser.Builder();
            for (Object o : skip(list, start)) {
                if (o instanceof ToTreeListItem) {
                    final ToTreeListItem item = (ToTreeListItem) o;
                    final Operator op = item.getOperator();
                    if (op instanceof PrefixOperator) {
                        builder.prefix(item, op.getLeftPrec());
                    } else if (op instanceof PostfixOperator) {
                        builder.postfix(item, op.getRightPrec());
                    } else if (op instanceof BinaryOperator) {
                        builder.infix(item, op.getLeftPrec(),
                                op.getLeftPrec() < op.getRightPrec());
                    } else if (op instanceof SpecialOperator) {
                        builder.special(item, op.getLeftPrec(), op.getRightPrec(),
                                (parser, op2) -> {
                                    final List<PrecedenceClimbingParser.Token> tokens =
                                            parser.all();
                                    final SpecialOperator op1 =
                                            (SpecialOperator) requireNonNull((ToTreeListItem) op2.o, "op2.o").op;
                                    SpecialOperator.ReduceResult r =
                                            op1.reduceExpr(tokens.indexOf(op2),
                                                    new TokenSequenceImpl(parser));
                                    return new PrecedenceClimbingParser.Result(
                                            tokens.get(r.startOrdinal),
                                            tokens.get(r.endOrdinal - 1),
                                            parser.atom(r.node));
                                });
                    } else {
                        throw new AssertionError();
                    }
                } else {
                    builder.atom(requireNonNull(o, "o"));
                }
            }
            return builder.build();
        }

        @Override public int size() {
            return list.size();
        }

        @Override public Operator op(int i) {
            ToTreeListItem item = (ToTreeListItem) requireNonNull(list.get(i),
                    () -> "list.get(" + i + ")");
            return item.op;
        }

        @Override public ParserPos pos(int i) {
            final Object o = list.get(i);
            return o instanceof ToTreeListItem
                    ? ((ToTreeListItem) o).pos
                    : requireNonNull((Node) o, () -> "item " + i + " is null in " + list)
                    .getParserPosition();
        }

        @Override public boolean isOp(int i) {
            return list.get(i) instanceof ToTreeListItem;
        }

        @Override public Node node(int i) {
            return requireNonNull((Node) list.get(i));
        }

        @Override public void replaceSublist(int start, int end, Node e) {
            SqlParserUtil.replaceSublist(list, start, end, e);
        }
    }

    /** Pre-initialized {@link DateFormat} objects, to be used within the current
     * thread, because {@code DateFormat} is not thread-safe. */
    private static class Format {
        private static final ThreadLocal<@Nullable Format> PER_THREAD =
                ThreadLocal.withInitial(Format::new);

        private static Format get() {
            return requireNonNull(PER_THREAD.get(), "PER_THREAD.get()");
        }

        final DateFormat timestamp =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.ROOT);
        final DateFormat time =
                new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
        final DateFormat date =
                new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
    }
}
