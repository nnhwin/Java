package clustering.form;

import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVFile {
    private final ArrayList<String[]> Rs = new ArrayList<String[]>();
    private String[] OneRow;
    boolean hasRead=false;
    public int count=0;
    public String[] columnNames;
    int attriCount=0;
    
    public int getDataSize(){
    	return this.Rs.size();
    }
    public int getAttriCount(){
    	return this.attriCount;
    }
    
    public ArrayList<String[]> ReadCSVfile(File DataFile) {
        try {
            BufferedReader brd = new BufferedReader(new FileReader(DataFile));
           
            while (brd.ready()) {
                    		
                String st = brd.readLine();
                OneRow = st.split(",|\\s|;");
                attriCount=OneRow.length;
            	if(hasRead==false){
            		columnNames=OneRow;
            		hasRead=true;
            	}
            		
                count=OneRow.length;
                Rs.add(OneRow);
                ////System.out.println(Arrays.toString(OneRow));
            } // end of while
        } // end of try
        catch (Exception e) {
            String errmsg = e.getMessage();
            //System.out.println("File not found:" + errmsg);
        } // end of Catch
        return Rs;
    }// end of ReadFile method
}// end of CSVFile class