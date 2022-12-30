import com.yunpayroll.exparser.parser.ExparserImpl;
import com.yunpayroll.exparser.parser.Node;

import java.io.Reader;
import java.io.StringReader;

public class ExparserImplTest {


    public static void main(String[] args) throws Exception {
        Reader reader = new StringReader("max(2,3,4,5)");
        ExparserImpl exparser = new ExparserImpl(reader);
        Node node = exparser.parseExpressionEof();
        System.out.println(node);
        System.out.println(2<<2);
    }

}
