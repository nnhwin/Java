package clustering.form;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVFileRead {
    private final ArrayList<String[]> Rs = new ArrayList<String[]>();
    private String[] OneRow;
    boolean hasRead=false;
    public int count=0;
    public String[] columnNames;
    public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public int getAttriCount() {
		return attriCount;
	}

	public void setAttriCount(int attriCount) {
		this.attriCount = attriCount;
	}

	int attriCount=0;
    
   
   /* public ArrayList<String[]> ReadAllData(File DataFile) {
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
                ////////System.out.println(Arrays.toString(OneRow));
            } // end of while
        } // end of try
        catch (Exception e) {
            String errmsg = e.getMessage();
            //////System.out.println("File not found:" + errmsg);
        } // end of Catch
        return Rs;
    }// end of ReadFile method
*/    
    public int[] GetRowColumn(File DataFile){
    	int[] countArr=new int[2];
    	 try {
             BufferedReader brd = new BufferedReader(new FileReader(DataFile));
            
             while (brd.ready()) {
                     		
                 String st = brd.readLine().trim();
                 if(!st.isEmpty()){
                 	++count;
                 	OneRow = st.split(",|\\s|;");
                 	attriCount=OneRow.length;
                 }
             } // end of while
             
             //Minus
             count=count-1;
             attriCount=attriCount-2;
             countArr[0]=count;
             countArr[1]=attriCount;
    	 } // end of try
         catch (Exception e) {
             String errmsg = e.getMessage();
             //////System.out.println("File not found:" + errmsg);
         } // end of Catch 
    	 return countArr;
    }
    
    public String[][] ReadCSVfileWithClass(File DataFile,int rCount,int cCount) {
    	String[][] onlyData=new String[rCount][cCount+1];
    	
    	String[] dataRow;
    	int rowCount=0;
        try {
            BufferedReader brd = new BufferedReader(new FileReader(DataFile));
            brd.read();
            String str;
            while((str=brd.readLine()) != null){
            	str=str.toLowerCase();
            	if(!str.contains("id"))
            	{          		
                	////////System.out.println("@@"+str);
                	dataRow=str.split(",|\\s|;");
                	
                	for(int i=1;i<dataRow.length;i++){
                		onlyData[rowCount][i-1]=dataRow[i];
                		//////System.out.println(dataRow[i]);
                	}
                	++rowCount;
            	}
            		          	
            }
           
        } // end of try
        catch (Exception e) {
            String errmsg = e.getMessage();
            //////System.out.println("File not found:" + errmsg);
        } // end of Catch
        return onlyData;
    }// end of ReadFile method
    
    public float[][] ReadCSVfile(File DataFile,int rCount,int cCount) {
    	float[][] onlyData=new float[rCount][cCount];
    	
    	String[] dataRow;
    	int rowCount=0;
        try {
            BufferedReader brd = new BufferedReader(new FileReader(DataFile));
            brd.read();
            String str;
            while((str=brd.readLine()) != null){
            	str=str.toLowerCase();
            	if(!str.contains("id") && !str.contains("class"))
            	{          		
                	////////System.out.println("@@"+str);
                	dataRow=str.split(",|\\s|;");
                	for(int i=1;i<dataRow.length-1;i++){
                		float value=Float.parseFloat(dataRow[i]);
                		onlyData[rowCount][i-1]=value;
                	}
                	++rowCount;
            	}
            		          	
            }
           
        } // end of try
        catch (Exception e) {
            String errmsg = e.getMessage();
            //////System.out.println("File not found:" + errmsg);
        } // end of Catch
        return onlyData;
    }// end of ReadFile method
}// end of CSVFile class