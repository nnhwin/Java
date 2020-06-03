/*package clustering.form;

import java.io.FileWriter;
import java.util.Arrays;

public class CSVUtilsExample {

    public static void main(String[] args) throws Exception {

        String csvFile = "C:/Users/USER/Desktop/bb.csv";
        FileWriter writer = new FileWriter(csvFile);

        CSVUtils.writeLine(writer, Arrays.asList("a", "b", "c", "d"));

        //custom separator + quote
        CSVUtils.writeLine(writer, Arrays.asList("aaa", "bb,b", "cc,c"), ',', '"');

        //custom separator + quote
        CSVUtils.writeLine(writer, Arrays.asList("aaa", "bbb", "cc,c"), '|', '\'');

        //double-quotes
        CSVUtils.writeLine(writer, Arrays.asList("aaa", "bbb", "cc\"c"));


        writer.flush();
        writer.close();
        System.out.println("Done");
    }

}*/