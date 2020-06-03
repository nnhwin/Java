package clustering.form;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.*;

import clustering.properties.ClassProperties;

import java.util.Arrays;



public class ClusteringMean1 extends JFrame implements ActionListener{

	float[] randNum=new float[20];
	JButton meancluster,saveBut,exitBut,csvBut;
	static HashMap<String,HashMap<String,String>> data;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	String output="";
	JComboBox kcb;
	String fname=null;
	float[][] finalWeightVector;
	String finalResult1;
	public HashMap<Integer,String> csvClusterData;
	String[][] dataWithClass;
	
	

	float seconds;
	
	JTextArea outputArea;
	
	public ClusteringMean1(String fname){
		this.fname=fname;
		df2.setRoundingMode(RoundingMode.UP);
		Container con=this.getContentPane();
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		     
	    kcb=new JComboBox(knumber); 
	    
	    
		JPanel inputPanel=new JPanel();
		inputPanel.setLayout(new BorderLayout());
		JLabel inputlbl=new JLabel("Enter K-Cluster numbers.........");
		//inputtext=new JTextField(30);
		inputPanel.add(inputlbl,BorderLayout.BEFORE_FIRST_LINE);
		inputPanel.add(kcb,BorderLayout.CENTER);
		
		
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new GridLayout(1,3));
		meancluster=new JButton("Calculate Fuzzy C-Mean Cluster");
		meancluster.addActionListener(this);
		saveBut=new JButton("Save Data to File");
		saveBut.addActionListener(this);
		exitBut=new JButton("Exit");
		exitBut.addActionListener(this);
		csvBut=new JButton("Export Clusters into CSV Files");
		csvBut.addActionListener(this);;
		buttonPanel.add(meancluster);
		buttonPanel.add(saveBut);
		buttonPanel.add(csvBut);
		buttonPanel.add(exitBut);
		
		JPanel outputPanel=new JPanel();
		outputPanel.setLayout(new GridLayout(1,1));
		outputArea=new JTextArea(300,300);
		JScrollPane scrolll = new JScrollPane(outputArea);
		outputPanel.add(scrolll);
		
		mainPanel.add(inputPanel,BorderLayout.NORTH);
		mainPanel.add(buttonPanel,BorderLayout.SOUTH);
		mainPanel.add(outputPanel,BorderLayout.CENTER);
		
		con.add(mainPanel);
		this.setTitle("C-Mean Clustering");
		this.setSize(700,500);
		this.setLocation(250,120);
	    setVisible(true);
	    con.add(mainPanel);
	}
	
	public void WriteCSVFile(String name,HashMap<Integer,String> clusterData){
		String outputFile="";
		String ss="C:/Users/USER/Desktop/"+name;
		 for (Map.Entry me : clusterData.entrySet()) {
			 outputFile=ss;
	          ////System.out.println("Cluster "+me.getKey() + "\n" + me.getValue());
	          outputFile+=me.getKey()+".csv";
	          ////System.out.println(outputFile);
	          FileWriter writer;
				try {
					writer = new FileWriter(outputFile);
					String mm=(String) me.getValue();
					String[] rr=mm.split(",");
					CSVUtils.writeLine(writer, Arrays.asList(rr));
					writer.flush();
			        writer.close();
			       // //System.out.println("Done for Cluster "+me.getKey()+"\n\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				outputFile=ss;
	        }
		
	}
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource()==exitBut){
			this.dispose();
		}
		else if(e.getSource()==csvBut){
			////System.out.println("CSV");
			////System.out.println("fname "+fname);
			String name="";
			if(fname.contains("/"))
				name=fname.substring(fname.lastIndexOf("/"));
			else if(fname.contains("\\")){
				name=fname.substring(fname.lastIndexOf("\\"));
			}
			
			if(name.contains("\\")){
				name=name.substring(1);
			}	
			name=name.substring(0,name.indexOf("."));
			////System.out.println("name "+name);
			////System.out.println("Total Cluster"+csvClusterData.size());
			WriteCSVFile(name+"FCM-",csvClusterData);
			
		}
		else if(e.getSource()==saveBut){
			String str="D:\\FuzzyKMeanResult.txt";
			try{    
		           FileWriter fw=new FileWriter(str);    
		           fw.write(output);    
		           fw.close();    
		    }
			catch(Exception ex){//System.out.println(ex.getMessage());}    
		     JOptionPane.showMessageDialog(null, "All caluation data are successfully saved in the file \""+str+"\".");
			}
		}
		else if(e.getSource()==meancluster){
			long start = System.currentTimeMillis();
			
			
			 int k=Integer.parseInt(kcb.getSelectedItem().toString());
			  
			  CSVFileRead Rd = new CSVFileRead();
			  
			  File DataFile = new File(fname);
			  int[] countData=Rd.GetRowColumn(DataFile);
			  int rowCount=countData[0];
			  int colCount=countData[1];
			  //System.out.println("Attribute Count "+colCount);
			  //System.out.println("Row Count "+rowCount+"\n");
			  output+="Data Size (row) ="+rowCount+"\t Attribute Count= "+colCount+"\n";	
			  
			  output+="\nImporting Data from CSV file.........................";
			  //System.out.println("Importing Data from CSV file.........................");
			  float[][] onlyData=Rd.ReadCSVfile(DataFile,rowCount,colCount);
			  PrintData(onlyData);
			  
			  dataWithClass=Rd.ReadCSVfileWithClass(DataFile,rowCount,colCount);
			  
			  
			  output+="\nWeight Vector........................";
			  //System.out.println("Assigning Weight Vector to Large Cluster Number needs Time.......Please Wait.......");
			  //Weight Assignation
			  //System.out.println("\nWeight Vector........................");
			  float[][] weightVector=GetRandomWeight(rowCount,k);
			 // float[][] weightVector=ConstantRandomWeight(rowCount,k);
			  //System.out.println("First Iteration>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			  PrintData(weightVector);
			  PrintAllDataAndWeight(onlyData,weightVector);  
			  
			  output+="\nStep 2 Calculation.........................";
			  //Step2 Calculation
			  //System.out.println("\nStep 2 Calculation.........................");
			  float[][] step2Centroid=Step2Calculation(onlyData,weightVector);
			  output+="*********************Centroid****************";
			  //System.out.println("*********************Centroid****************");
			  PrintData(step2Centroid);
			  
			  int r=step2Centroid.length;
			  int c=step2Centroid[0].length;
			  boolean exitOrNot=false;
			  
			  int loopCount=1;
			  float[][] newWeightVector=new float[rowCount][k];
			  Boolean shouldExit=false;
			  
			  while(shouldExit==false){
				  newWeightVector=weightVector;
				  //System.out.println("Iteration>>>"+(++loopCount));
				 		  
				//Step3 Distance
				  output+="\nStep 3 Distance Calculation..................";
				  //System.out.println("\nStep3 Distances..................");
				  float[][] distanceVector=Step3Calculation(onlyData,step2Centroid);
				  //System.out.println("Distances..........");
				  PrintData(distanceVector);
				  
				  //Step 4 Update Membership
				  output+="\nStep4 New Membership Calculation..................";
				  //System.out.println("\nStep4 New Membership Calculation.................");
				  
				  weightVector=Step4Calculation(distanceVector,k);
				  PrintData(weightVector);
				  PrintAllDataAndWeight(onlyData,weightVector);
				  
				  //Print Old & New Membership Values.............
				  output+="\n\nOld & New Membership Values............................";
				  //System.out.println("\nOld & New Membership Values............................");
				  output+="\nOld Membership Value................";
				  //System.out.println("Old Membership Value................");
				  PrintData(newWeightVector);
				  output+="\n\nNEW Membership Value................";
				  //System.out.println("\nNEW Membership Value................");
				  PrintData(weightVector);
				  
				  shouldExit=CheckConditionForE(weightVector, newWeightVector,k);
				  shouldExit=CheckLessThan(weightVector, newWeightVector,k);
					  			 
				  //Find Centroid Again
				  if(shouldExit==false)
				  {
					  output+="\nStep 2 Calculation.........................";
					  //Step2 Calculation
					  //System.out.println("\nStep 2 Calculation.........................");
					  step2Centroid=Step2Calculation(onlyData,weightVector);
					  output+="*********************Centroid****************";
					  //System.out.println("*********************Centroid****************");
					  PrintData(step2Centroid);
				  }
				  
				  output+="\nEXIT OR NOT>>>>>>>>>>>"+shouldExit;
				  
				  //System.out.println("EXIT OR NOT>>>>>>>>>>>"+shouldExit);
				  if(shouldExit==true){
					  //System.out.println("Iteration stop at......."+loopCount);
					  output+="\nIteration stops at ......."+loopCount;
				  }
					  
				  
			  }//end of while loop
			//Write Final Data
			  
			  //System.out.println("\nData & and Final Updated Weight......................\n");
			  output+="\nData & and Final Updated Weight......................\n";
			  PrintAllDataAndWeight(onlyData,weightVector);
			  
			  //Cluster Result
			  int[] DataCluster=FinalClusterResult(weightVector);
			
			  String finalResult=PrintCluster(DataCluster,k,onlyData);
			  output+=finalResult;
			  finalResult1=PrintCluster1(DataCluster,k,onlyData);
			  
			  output+="\n**************Total Elapsed Time*******************\n";
			  finalResult+="\n**************Total Elapsed Time*******************\n";
			  finalResult1+="\n**************Total Elapsed Time*******************\n";
			  long finish = System.currentTimeMillis();
			  long timeElapsed = finish - start;
			  seconds = (timeElapsed / 1000);
			  //long minutes = (timeElapsed / 1000) / 60;
			  output+=" ELASPED TIME in Seconds = "+seconds+" seconds \n" ;
			  output+=" ELASPED TIME in MiliSeconds= "+timeElapsed+" miliseconds " ;
			  finalResult+=" ELASPED TIME = "+seconds+" seconds\n";
			  finalResult+=" ELASPED TIME in MiliSeconds= "+timeElapsed+" miliseconds";
			  finalResult1+=" ELASPED TIME in Seconds= "+seconds+" seconds\n";
			  finalResult1+=" ELASPED TIME in MiliSeconds= "+timeElapsed+" miliseconds";
			  //System.out.println("**************Total Elapsed Time*******************\n");
			  //System.out.println(" Total Elaspsed Time in Seconds= "+seconds+" seconds");
			  //System.out.println(" Total Elaspsed Time in MiliSeconds = "+timeElapsed+" miliseconds");
			  
			  outputArea.setText(finalResult);
			 
		  }//Button if 
		
	}
	
	
	float[][] ChangeDecimal(float[][] value){
		float[][] changevalue=new float[value.length][value[0].length];
		for(int i=0;i<value.length;i++){
			for(int j=0;j<value[0].length;j++){
				value[i][j]=Float.parseFloat(df2.format(value[i][j]));
			}
		}
		return changevalue;
	}
	
	float[][] Step2FirstCalculation(float[][] data, int k,int colCount){
		float[][] centroidVector=new float[k][colCount];
		for(int i=0;i<k;i++){
			for(int j=0;j<colCount;j++){
				centroidVector[i][j]=data[i][j];
			}
		}
		
		return centroidVector;
	}
	
	boolean CheckLessThan(float[][] weightVector,float[][] newWeightVector,int k){
		////System.out.println("Old............................New");
		int size=weightVector.length;
		int count=0;
		int allCount=0;
		boolean mustExit=false;
		for(int i=0;i<size;i++){
			////System.out.println("For ID "+(i+1)+"-->");
			for(int j=0;j<k;j++)
			{
				
				float old=weightVector[i][j];
				float newD=newWeightVector[i][j];
				//System.out.print("\t"+weightVector[i][j]+".....**..........."+newWeightVector[i][j]+"\n");
				if(Math.abs(old-newD)<=0.01){
					++count;
				}
				++allCount;
			}
			
		}
		
		if(count==allCount)
			mustExit=true;

		return mustExit;
		
	}
	
	boolean CheckConditionForE(float[][] weightVector,float[][] newWeightVector,int k){
		////System.out.println("Old............................New");
		int size=weightVector.length;
		int count=0;
		boolean mustExit=true;
		for(int i=0;i<size;i++){
			////System.out.println("For ID "+(i+1)+"-->");
			for(int j=0;j<k;j++)
			{
				
				if(Float.compare(weightVector[i][j],newWeightVector[i][j])!=0){
					//System.out.println("For ID "+(i+1)+"-->");
					//System.out.print("\t"+weightVector[i][j]+"..........."+newWeightVector[i][j]+"\n");
					mustExit=false;
				}
				
			}
			
		}
		return mustExit;
		
	}
	
	
	public String PrintCluster1(int[] cluster,int k,float[][] data){
		String result="";
		result+="\n*********************************CMean Cluster Result****************************\n";
		//Show data by Cluster
		String print_data="";
		result+="\nCluster \n";
		print_data="\nCluster \n";
		String[] objID=new String[k];
		String[] str=new String[k];
		for(int i=0;i<cluster.length;i++){ 
			String temp="";
			for(int j=0;j<k;j++){
				if(cluster[i]==(j+1)){
					
						if(str[j]==null)
							str[j]="";
						
						if(objID[j]==null)
							objID[j]="";
						
						str[j]=str[j]+", ID-"+(i+1);
						objID[j]=objID[j]+","+(i+1);				
				}
				
			}
		}
		
		for(int j=0;j<k;j++)
		{
			if(str[j]==null){
				result+="\n"+"Cluster "+(j+1)+"-->{Empty}";
			}				
			else{
				result+="\nCluster "+(j+1)+"--> {"+str[j].substring(2)+"}";
				//System.out.println("Cluster "+(j+1)+"--> {"+str[j].substring(2)+"}");
				String tt=DetailData(data,objID[j].substring(1));
				//System.out.println(tt);
				result+=tt;
			}
				
		}			
		return result;
	}
	
	public String PrintCluster(int[] cluster,int k,float[][] data){
		String result="";
		result+="Cluster Result.................\n";
		//System.out.println("Cluster Result.................");
		result+="ID\t\t Cluster \n";
		//System.out.println("ID\t\t Cluster ");
		for(int i=0;i<cluster.length;i++){
			result+=(i+1)+"\t\t"+" Cluster "+cluster[i]+"\n";
			//System.out.println((i+1)+"\t\t"+" Cluster "+cluster[i]);
		}
		csvClusterData=new HashMap<Integer,String>();
		
		//Show data by Cluster
		//System.out.println("Cluster ");
		result+="\nCluster \n";
		String[] str=new String[k];
		String[] objID=new String[k];
		for(int i=0;i<cluster.length;i++){
			String temp="";
			for(int j=0;j<k;j++){
				if(cluster[i]==(j+1)){					
						if(str[j]==null)
							str[j]="";
						
						if(objID[j]==null)
							objID[j]="";
						
						str[j]=str[j]+", ID-"+(i+1);
						objID[j]=objID[j]+","+(i+1);
					
				}
				
			}
		}
		
		for(int j=0;j<k;j++)
		{
			if(str[j]==null){
				result+="\n"+"Cluster "+(j+1)+"-->{Empty}";
				//System.out.println("Cluster "+(j+1)+"-->{Empty}");
			}
			else{
				result+="\nCluster "+(j+1)+"--> {"+str[j].substring(2)+"}";
				//System.out.println("Cluster "+(j+1)+"--> {"+str[j].substring(2)+"}");
				String tt=DetailData(data,objID[j].substring(1));
				String pp=CSVPrintableData1(dataWithClass,objID[j].substring(1),j+1);
				
				
				//System.out.println(tt);
				result+=tt;
				////System.out.println("\n\nCSV FORMAT"+pp);
				csvClusterData.put(j+1, pp);
				
			} 	
		}
			
		return result;
	}
	
public String CSVPrintableData1(String[][] data,String str,int cluster){	
		String returnValue="";
		String[] objName=str.split(",");
		int col=data[0].length;
		//System.out.println("COL>>>>>>>>>>>>>>"+col);
		String tempVar="ID"+",";
		for(int i=0;i<col-1;i++)
			tempVar+="A"+(i+1)+",";
		tempVar+="Class\n";
		returnValue=tempVar;
		
		for(int i=0;i<data.length;i++){
			String str1=String.valueOf(i+1);
			for(int k=0;k<objName.length;k++){
				String str2=objName[k].trim();
				if(str1.equals(str2)){
					returnValue+=str2+",";
					for(int j=0;j<col;j++){
						returnValue+=data[i][j]+",";
					}
					//returnValue+="Class"+cluster;
					returnValue+="\n";
				}
			}//k
			
			
		}//i
		return returnValue;
	}

	/*public String CSVPrintableData(float[][] data,String str,int cluster){
		
		
		String returnValue="";
		String[] objName=str.split(",");
		int col=data[0].length;
		//System.out.println("COL>>>>>>>>>>>>>>"+col);
		String tempVar="ID"+",";
		for(int i=0;i<col;i++)
			tempVar+="A"+(i+1)+",";
		tempVar+="Class\n";
		returnValue=tempVar;
		
		//System.out.println("\n\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		for(int i=0;i<data.length;i++){
			int j=data[0].length;
			//System.out.println(j);
			for(int k=0;k<j;k++){
				System.out.print(data[i][k]+"###");
			}
			//System.out.println("\n");
		}
		
		for(int i=0;i<data.length;i++){
			String str1=String.valueOf(i+1);
			for(int k=0;k<objName.length;k++){
				String str2=objName[k].trim();
				if(str1.equals(str2)){
					returnValue+=str2+",";
					for(int j=0;j<col;j++){
						returnValue+=data[i][j]+",";
					}
					returnValue+="Class"+cluster;
					returnValue+="\n";
				}
			}//k
			
			
		}//i
		return returnValue;
	}*/
	
	public String DetailData(float[][] data,String str){
		String returnValue="\n";
		String[] objName=str.split(",");
		int col=data[0].length;
		for(int i=0;i<data.length;i++){
			String str1=String.valueOf(i+1);
			for(int k=0;k<objName.length;k++){
				String str2=objName[k].trim();
				if(str1.equals(str2)){
					returnValue+="ID-"+str2+"  -->  ";
					for(int j=0;j<col;j++){
						returnValue+=data[i][j]+"\t";
					}
					returnValue+="\n";
				}
			}//k
			
			
		}//i
		return returnValue;
	}
	
	int[] FinalClusterResult(float[][] weightVector){
		int row=weightVector.length;
		int col=weightVector[0].length;
		//String[] cluster=new String[col];
		////System.out.println("Row ="+row+"......col="+col);
		int[] DataCluster=new int[row];
		
		for(int i=0;i<row;i++){
			float max=weightVector[i][0];
			int pos=0;
			for(int j=0;j<col;j++){
				////System.out.println("Weight ="+ weightVector[i][j]);
				if(max<weightVector[i][j]){
					max=weightVector[i][j];
					pos=j;
				}
			}
			////System.out.println("..Max ="+max+"........place "+(pos+1));
			DataCluster[i]=(pos+1);
			
		}
		return DataCluster;
	}
	//////////////////////////////////////////////////////////////////////
	float[][] Step4Calculation(float[][] distanceVector,int k){
		int row=distanceVector.length;
		int col=distanceVector[0].length;
		float[][] newWeightVector=new float[row][col];
		
		//Let m=2;
		//m=k
		int m=2;
		for(int pp=0;pp<row;pp++){
			float sum=0.0f;
			for(int i=0;i<col;i++){
				sum=0;
				String str="";
				//float powervalue=1/(m-1);
				float v1=Float.parseFloat(df2.format(1/distanceVector[pp][i]));
				////System.out.println("V1="+distanceVector[pp][i]);
				float v2=Float.parseFloat(df2.format(1/(m-1)));
				float firstValue=(float) Math.pow(v1,v2);
				//firstValue=Float.parseFloat(df2.format(firstValue));
				str+=firstValue+"/";
				
				for(int j=0;j<col;j++){
					//if(i!=j){
						v2=Float.parseFloat(df2.format(1/(m-1)));
						v1=Float.parseFloat(df2.format(1/distanceVector[pp][j]));
						float value=(float)Math.pow(v1,v2);
						//value=Float.parseFloat(df2.format(value));
						////System.out.println("BEfore V1="+distanceVector[pp][i]+"After V1="+v1+"......v2="+v2);
						sum+=value;
					
				}
				
				float vv=firstValue/sum;
				DecimalFormat dff2 = new DecimalFormat("#.#");
				//dff2.setRoundingMode(RoundingMode.DOWN);
				vv=Float.parseFloat(dff2.format(vv));
				str+=sum+"="+vv;
				
				output+="\n"+str;
				//System.out.println(str);
				newWeightVector[pp][i]=vv;
			}
		}
		return newWeightVector;
				
	}
	
	float[][] Step3Calculation(float[][] data,float[][] centroid){
		int rowCount=data.length;
		int attriColCount=data[0].length;
		int centroidRow=centroid.length;
		float[][] distanceVector=new float[rowCount][centroid.length];
		
		/*centroid=ChangeDecimal(centroid);
		data=ChangeDecimal(data);*/
		
		
		for(int i=0;i<centroidRow;i++){
		output+="\nCentroid "+(i+1)+"....................................";
		//System.out.println("Centroid "+(i+1));
			for(int k=0;k<rowCount;k++){
				float sum=0.0f;
				for(int j=0;j<attriColCount;j++){
					float value=Math.abs(data[k][j]-centroid[i][j]);
					value=value*value;
					sum+=value;
					value=Float.parseFloat(df2.format(value));
					//System.out.print("Square ("+data[k][j]+"-"+centroid[i][j]+") = "+value+" ||  ");
					output+="\n"+"Square ("+data[k][j]+"-"+centroid[i][j]+") = "+value+" ||  ";		
				}
			float distance=(float)Math.sqrt(sum);	
			distance=Float.parseFloat(df2.format(distance));
			distanceVector[k][i]=distance;
			output+="\n Distance between Cluster "+(i+1)+" and Object "+(k)+"-->"+distance;
			//System.out.println("Distance between Cluster "+(i+1)+" and Object "+(k+1)+"-->"+distance);
			
		}//k
		
	 }//i
	return distanceVector;
		
	}
	
	float[][] Step2Calculation(float[][] data,float[][] weight){
		int rowCount=data.length;
		int attriColCount=data[0].length;
		int clusterColCount=weight[0].length;
		float[][] square=new float[rowCount][clusterColCount];
		float[] clusterSum=new float[clusterColCount];
		float[][] centroidValues=new float[clusterColCount][attriColCount];
		
		//cluster
		for(int i=0;i<clusterColCount;i++){
			float clusterColSum=0.0f;
			float attriColCentroid=0.0f;
			int m=2;
			
			for(int k=0;k<rowCount;k++){
				clusterColSum+=weight[k][i]*weight[k][i];
				square[k][i]=getMvalue(weight[k][i],m);
				//square[k][i]=weight[k][i]*weight[k][i];
				square[k][i]=Float.parseFloat(df2.format(square[k][i]));
				output+="\n"+weight[k][i]+"*"+weight[k][i]+"="+(square[k][i]);
				//System.out.println(weight[k][i]+"*"+weight[k][i]+"="+(square[k][i]));
			}
			String temp=df2.format(clusterColSum);
			clusterColSum=Float.parseFloat(temp);
			output+="\nCluster Col Sum ="+clusterColSum;
			//System.out.println("Cluster Col Sum ="+clusterColSum);
			clusterSum[i]=clusterColSum;		
			for(int j=0;j<attriColCount;j++){
				attriColCentroid=0;
				output+="\n\nAttribute "+(j+1)+"............";
				//System.out.println("\nAttribute "+(j+1)+"............");
				for(int k=0;k<rowCount;k++){
					attriColCentroid+=data[k][j]*square[k][i];					
					output+="\n"+data[k][j]+"*"+square[k][i]+"="+df2.format((data[k][j]*square[k][i]));
					//System.out.println(data[k][j]+"*"+square[k][i]+"="+(data[k][j]*square[k][i]));
				}
				
				temp=df2.format(attriColCentroid);
				attriColCentroid=Float.parseFloat(temp);
								
				float attrCen=attriColCentroid/clusterSum[i];
				temp=df2.format(attrCen);
				attrCen=Float.parseFloat(temp);
				output+="\n"+attriColCentroid+"/"+clusterSum[i]+"="+attrCen;
				//System.out.println(attriColCentroid+"/"+clusterSum[i]+"="+attrCen);
				
				centroidValues[i][j]=attrCen;
				
			}
		}
		
		return centroidValues;
	}
	
	float getMvalue(float base,float power){
		float vv=1.0f;
		for(int i=1;i<=power;i++)
			vv*=base;
		
		return vv;
		
	}
	void PrintAllDataAndWeight(float[][] data,float[][] weight){
		int rowCount=data.length;
		int attriColCount=data[0].length;
		int clusterColCount=weight[0].length;
		int id=0;
		
		output+="\nData & Weight .................................\n";
		//System.out.println("\nData & Weight .................................");
		output+="ID\t";
		//System.out.print("ID\t");
		for(int i=0;i<attriColCount;i++){
			output+=" Data "+i+"\t";
			//System.out.print("Data "+i+"\t");
		}
			
		
		for(int i=0;i<clusterColCount;i++){
			//System.out.print("Cluster "+i+"\t");
			output+="Cluster "+i+"\t";
		}
		
		output+="Total \t";
		//System.out.print("Total\t");
		//System.out.println();
		output+="\n";
		for(int i=0;i<rowCount;i++){
			id=i+1;
			//System.out.print(id+"\t");
			for(int k=0;k<attriColCount;k++){
				//System.out.print(data[i][k]+"\t");
				output+=data[i][k]+"\t";
			}
				
			float sum=0.0f;
			for(int p=0;p<clusterColCount;p++){
				//System.out.print(weight[i][p]+"\t");
				output+="\t"+weight[i][p]+"\t";
				sum+=weight[i][p];
			}
				
			output+="1\t\n";
			//System.out.println("1\t");
			output+=sum+"\t\n";
			//System.out.print(sum+"\t");
			//System.out.println();
		}
	}
	
	float[][] ConstantRandomWeight(int rowCount,int k){
		float[][] weightVector=new float[rowCount][k];
		weightVector[0][0]=0.8f;
		weightVector[0][1]=0.2f;
		weightVector[1][0]=0.9f;
		weightVector[1][1]=0.1f;
		weightVector[2][0]=0.7f;
		weightVector[2][1]=0.3f;
		weightVector[3][0]=0.3f;
		weightVector[3][1]=0.7f;
		weightVector[4][0]=0.5f;
		weightVector[4][1]=0.5f;
		weightVector[5][0]=0.2f;
		weightVector[5][1]=0.8f;
		
		return weightVector;		
	}
	
	float[][] GetRandomWeight(int rowCount,int k){
		float[][] weightVector=new float[rowCount][k];
		GenerateNum gn=new GenerateNum();
		for(int i=0;i<rowCount;i++){
			float[] weights=gn.GeneratingNum();
			float[] weightPair=gn.GetPair(weights,k);
			
			for(int j=0;j<k;j++){
				weightVector[i][j]=weightPair[j];
			}
		}
		
		return weightVector;		
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
	
	
	/*public static void main(String[] args) {
		ClusteringMean1 f=new ClusteringMean1("C:\\Users\\USER\\Iris1.csv");		
		
	}*/
	
}
