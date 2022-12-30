import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Test {
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, 3, 25);
        for( int i=0;i<22;i++ ) {
            cal.add(Calendar.DAY_OF_MONTH,1);
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            String dat = df.format(cal.getTime());
            DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
            String datDir = df1.format(cal.getTime());
            System.out.println( String.format("alter table  gateway_api add partition(`date`=%s) location '/datasource/tysjcj-log/bigdata-sjzt/gateway/%s'; ", dat,datDir) );

        }

    }
}
