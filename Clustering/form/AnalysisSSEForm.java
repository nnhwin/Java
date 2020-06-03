package clustering.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
//Input Sample
/*Text 1="3";
Text2="IrisFCM";
Text 3="C:\Users\USER\Desktop";*/


public class AnalysisSSEForm extends JFrame implements ActionListener{
	
	JButton sseBtn,saveBut,exitBut;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	String output="";
	JTextField kcb,directory;
	JTextField filenames;
	JTextArea outputArea;
	String[][] dataWithClass;
	String name="";
	JComboBox methodCombo;
	public AnalysisSSEForm(){
		
		df2.setRoundingMode(RoundingMode.UP);
		Container con=this.getContentPane();
		JPanel mainPanel=new JPanel();
		       
	    kcb=new JTextField(); 
	    filenames=new JTextField();
	    directory=new JTextField();
	    
		JPanel inputPanel=new JPanel();
		inputPanel.setLayout(new GridLayout(6,2,10,10));
		JLabel inputlbl=new JLabel("Enter K-Cluster numbers.........");
		//inputtext=new JTextField(30);
		inputPanel.add(inputlbl,BorderLayout.BEFORE_FIRST_LINE);
		inputPanel.add(kcb,BorderLayout.CENTER);
		
		JLabel namelbl=new JLabel("Enter File name to be analyzed.........");
		//inputtext=new JTextField(30);
		inputPanel.add(namelbl,BorderLayout.BEFORE_FIRST_LINE);
		inputPanel.add(filenames,BorderLayout.CENTER);
		
		JLabel dirlbl=new JLabel("Enter directory for file.........");
		//inputtext=new JTextField(30);
		inputPanel.add(dirlbl,BorderLayout.BEFORE_FIRST_LINE);
		inputPanel.add(directory,BorderLayout.CENTER);
		
		JLabel methodlbl=new JLabel("Choose Analysis Method........");
		//inputtext=new JTextField(30);
		inputPanel.add(methodlbl,BorderLayout.BEFORE_FIRST_LINE);
		String[] str=new String[2];
		str[0]="SSE";
		str[1]="Sillhouette";
		methodCombo=new JComboBox(str);
		inputPanel.add(methodCombo,BorderLayout.CENTER);
		
		
		JPanel p1=new JPanel();
		JPanel p2=new JPanel();
		JPanel p3=new JPanel();
		//p1.setLayout(new GridLayout(1,2));
		p2.setLayout(new GridLayout(1,3));		
		
		sseBtn=new JButton("Analysis");
		sseBtn.addActionListener(this);
		saveBut=new JButton("Save Data to File");
		saveBut.addActionListener(this);
		exitBut=new JButton("Exit");
		exitBut.addActionListener(this);
		//csvBut=new JButton("Analysis");
		//csvBut.addActionListener(this);;
		p2.add(sseBtn);
		p2.add(saveBut);
		//p2.add(csvBut);
		p2.add(exitBut);
		p3.add(new JLabel("."));
		inputPanel.add(p1);
		inputPanel.add(p2);
		inputPanel.add(p3);
		
		JPanel outputPanel=new JPanel();
		outputPanel.setLayout(new GridLayout(1,1));
		outputArea=new JTextArea(300,300);
		JScrollPane scrolll = new JScrollPane(outputArea);
		outputPanel.add(scrolll);
		
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(inputPanel,BorderLayout.NORTH);
		mainPanel.add(outputPanel,BorderLayout.CENTER);
		
		con.add(mainPanel);
		this.setTitle("Analysis for Clustering Algorithms");
		this.setSize(800,600);
		this.setLocation(250,60);
	    setVisible(true);
	    con.add(mainPanel);
	}
	void PrintData(float[][] onlyData){
		int rowCount=onlyData.length;
		int colCount=onlyData[0].length;
		
		output+="\n";
		for(int i=0;i<rowCount;i++){
			  for(int j=0;j<colCount;j++){
				  //System.out.print(onlyData[i][j]+"\t");
				  output+=onlyData[i][j ]+"\t";
			  }
			  //System.out.print("\n");
			  output+="\n";
		  }
	}
	public void actionPerformed(ActionEvent e){
		
		
	}
	
	void CalculateSillhouette(HashMap<Integer,float[][]> values){
		 
	}
	void FindFinalSCValue(float[] vv){
		output+="\nFinal Values for all clusters............................\n";
		output+="SC = ";
		String temp1="",temp2="(";
		float sum=0.0f,average=0.0f;
		for(int i=0;i<vv.length;i++){
			temp1+="SC"+(i+1)+"+";
			temp2+=vv[i]+"+";
			sum+=vv[i];
		}
		temp1="("+temp1.substring(0,temp1.length()-1)+")/"+vv.length+"=";
		temp2="("+temp2.substring(0,temp2.length()-1)+")=";
		
		average=(float)sum/vv.length;
		average=Float.parseFloat(df2.format(average));
		output+=temp1+temp2+average+"\n";
		
	}
	float CalculateFinalValueForCluster(float[] vv,int index){
		output+="\nSC Value for Cluster-"+(index+1)+"\n SC"+(index+1)+"=(";
		float sum=0.0f;
		for(int i=0;i<vv.length;i++){
			output+=vv[i]+"+";
			sum+=vv[i];
		}
		output=output.substring(0,output.length()-1)+")/"+vv.length;		
		float average=(float)sum/vv.length;
		average=Float.parseFloat(df2.format(average));
		output+="\n    "+average+"\n*********************************************************************\n\n";
		return average;
	}
	 
	
	float CalculateSZero(float avalue,float bvalue,int index){
		float finalvalue=0.0f;
		float maxValue=Math.max(avalue, bvalue);
		output+="\nS"+(index+1)+"(0)=";
		output+="bvalue-avalue/Max(bvalue,avalue)="+"("+bvalue+"-"+avalue+")/"+maxValue+"=";
		finalvalue=(bvalue-avalue)/maxValue;
		output+=finalvalue+"\n\n";
		return finalvalue;
	}
	float FindMinValue(float[] values){
		float minvalue=values[0];
		output+="Min value for B values --> Min(";
		for(int i=0;i<values.length;i++){
			output+=values[i]+",";
			if(minvalue>values[i])
				minvalue=values[i];
		}
		output=output.substring(0,output.length()-1)+") = "+minvalue;
		return minvalue;
	}
	
	String GetClusterLetter(int index){
		String[] str={"A","B","C","D","E","F","G","H","I","J","K","L"};
		return str[index];
	}
	
	float CalculateBValues(float[] AData,float[][] dataofB,int indexOfA,int clusterIndex){
		
	}
	float[] GetAData(int index,float[][] data,int rCount,int cCount){
		float[] AData=new float[cCount];
		for(int j=0;j<cCount;j++){
			AData[j]=data[index][j];		
		}
		return AData;	
	}
	
	HashMap<Integer,float[][]> GetData(String[] fnames){
		float[] SSEValue=new float[fnames.length];
		int fileCount=fnames.length;
		HashMap<Integer,float[][]> clustervalues=new HashMap<Integer,float[][]>();
		for(int i=0;i<fileCount;i++){
			CSVFileRead Rd = new CSVFileRead();
			File DataFile = new File(fnames[i]);
			 int[] countData=Rd.GetRowColumn(DataFile);
			  int rowCount=countData[0];
			  int colCount=countData[1];
			  output+="********************Cluster-"+(i+1)+"***************************\n";
			  output+="Data Size (row) ="+rowCount+"\t Attribute Count= "+colCount+"\n";	
			  //System.out.println("Importing Data from CSV file.........................");
			  float[][] onlyData=Rd.ReadCSVfile(DataFile,rowCount,colCount);
			  //PrintData(onlyData);			  
			  dataWithClass=Rd.ReadCSVfileWithClass(DataFile,rowCount,colCount);
			  PrintDataString(dataWithClass);
			  clustervalues.put(i, onlyData);
		}//end cluster count for
				
		return clustervalues;		
	}
	
	float CalcualteA0(int rnd,float[][] onlyData,int rCount,int cCount){
		rnd=rnd;
		output+="Random "+(rnd+1)+" --> ";
		float[] values=new float[rCount];
		for(int j=0;j<cCount;j++){
			output+=onlyData[rnd][j]+" , ";
		}
		output=output.substring(0,output.length()-1)+"\n";
		for(int i=0;i<rCount;i++){
			String temp="";
			if(i!=rnd){
				temp+="Dis(A"+(i+1)+",A"+(rnd+1)+") =";
				//Find Distance
				float sum=0.0f;
				for(int j=0;j<cCount;j++){
					float value=onlyData[i][j]-onlyData[rnd][j];
					value=value*value;
					sum+=value;
					temp+="("+onlyData[i][j]+"-"+onlyData[rnd][j]+")^2+";
				}//column
				values[i]=(float) Math.sqrt(sum);
				values[i]=Float.parseFloat(df2.format(values[i]));
				temp=temp.substring(0,temp.length()-1);
				temp+=" = Sqrt("+sum+")="+values[i]+"\n";
			}
			output+=temp;
		}
		
		//Calculate A(0)
		output+="\nA(0)=(";
		float sum=0.0f;
		for(int i=0;i<values.length;i++){
			sum+=values[i];
			output+=values[i]+"+";
		}
		output=output.substring(0,output.length()-1);	
		float aValue=(float)sum/(values.length-1);
		aValue=Float.parseFloat(df2.format(aValue));
		output+=")/"+(values.length-1)+"\n   ="+aValue+"\n";
		return aValue;
	}
	
	void CalculateSSE(String[] fnames){
		
	}
	
	float FindTotalSSE(float[] values){
		output+="\nSSE Values for each cluster............................\n";
		float sum=0.0f;
		for(int i=0;i<values.length;i++){
			sum+=values[i];
			output+="SSE-"+(i+1)+" = "+values[i]+"\n";
		}
		
		return sum;
	}
	
	float CalculateSSEDistance(float[] meanvalue,float[][] data,int rCount,int cCount,int clusterindex){
		output+="\nCalculate SSE Distance.................................\n";
		float sum=0.0f;
		for(int i=0;i<rCount;i++){
			float eachsum=0.0f;
			for(int j=0;j<cCount;j++){
				float value=data[i][j]-meanvalue[j];
				output+="("+data[i][j]+"-"+meanvalue[j]+")^2+";
				value=value*value;
				value=Float.parseFloat(df2.format(value));
				eachsum+=value;
			}
			output=output.substring(0,output.length()-1)+"="+eachsum+"\n";
			sum+=eachsum;
			
		}
		sum=Float.parseFloat(df2.format(sum));
		output+="SSE "+clusterindex+" = "+sum+"\n\n";
		return sum;
	}
	
	float[] MeanValue(float[][] data,int rCount,int cCount){
		float[] meanvalue=new float[cCount];
		for(int i=0;i<cCount;i++){
			float sum=0.0f;
			float mean=0.0f;
			for(int j=0;j<rCount;j++){
				sum+=data[j][i];
			}
			mean=sum/rCount;
			meanvalue[i]=Float.parseFloat(df2.format(mean));
			
		}
		
		output+="\n..............................................................\nMean value \n";
		for(int i=0;i<cCount;i++)
			output+=meanvalue[i]+"\t";
		output+="\n";
		
	return meanvalue;
	}

	void PrintDataString(String[][] onlyData){
		int rowCount=onlyData.length;
		int colCount=onlyData[0].length;
		
		output+="\n";
		for(int i=0;i<rowCount;i++){
			  for(int j=0;j<colCount;j++){
				  //System.out.print(onlyData[i][j]+"\t");
				  output+=onlyData[i][j ]+"\t";
			  }
			  //System.out.print("\n");
			  output+="\n";
		  }
	}
	public static void main(String[] args){
		AnalysisSSEForm af=new AnalysisSSEForm(); 
		
	}
}
