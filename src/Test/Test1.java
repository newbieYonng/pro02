package Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Test1 {

    public static void main(String[] args) {

        LocalDate date = LocalDate.parse("20220301", DateTimeFormatter.BASIC_ISO_DATE);
        String format = date.minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(date);
        System.out.println(format);
    }
}
