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


public class KDoublePlusAlgo extends JFrame implements ActionListener{

	float[] randNum=new float[20];
	JButton meancluster,saveBut,exitBut,csvBut;
	static HashMap<String,HashMap<String,String>> data;
	private static DecimalFormat df2 = new DecimalFormat("#.###");
	String output="";
	JComboBox kcb;
	String fname=null;
	float[][] finalWeightVector;
	JTextArea outputArea;
	String clusterResult;
	String[][] dataWithClass;
	public HashMap<Integer,String> csvClusterData;

	float seconds;
	public KDoublePlusAlgo(String fname){
		this.fname=fname;
		df2.setRoundingMode(RoundingMode.UP);
		Container con=this.getContentPane();
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		String knumber[]={"2","3","4","5","6","7"};        
	    kcb=new JComboBox(knumber); 
	    
	    
		JPanel inputPanel=new JPanel();
		inputPanel.setLayout(new BorderLayout());
		JLabel inputlbl=new JLabel("Enter K-Cluster numbers.........");
		//inputtext=new JTextField(30);
		inputPanel.add(inputlbl,BorderLayout.BEFORE_FIRST_LINE);
		inputPanel.add(kcb,BorderLayout.CENTER);
			
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new GridLayout(1,3));
		meancluster=new JButton("Calculate K-Means Plus Plus");
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
		this.setTitle("K-Means Plus Plus Clustering");
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
	          ////System.outprintln("Cluster "+me.getKey() + "\n" + me.getValue());
	          outputFile+=me.getKey()+".csv";
	          ////System.outprintln(outputFile);
	          FileWriter writer;
				try {
					writer = new FileWriter(outputFile);
					String mm=(String) me.getValue();
					String[] rr=mm.split(",");
					CSVUtils.writeLine(writer, Arrays.asList(rr));
					writer.flush();
			        writer.close();
			        ////System.outprintln("Done for Cluster "+me.getKey()+"\n\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				outputFile=ss;
	        }
		
	}
	
	public String getClusterResult() {
		return clusterResult;
	}


	public float getSeconds() {
		return seconds;
	}
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource()==exitBut){
			this.dispose();
		}
		else if(e.getSource()==csvBut){
			////System.outprintln("CSV");
			////System.outprintln("fname "+fname);
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
			////System.outprintln("name "+name);
			////System.outprintln("Total Cluster"+csvClusterData.size());
			WriteCSVFile(name+"KDoublePlus-",csvClusterData);
			
		}
		
		else if(e.getSource()==saveBut){
			String str="D:\\KPlusPlus.txt";
			try{    
		           FileWriter fw=new FileWriter(str);    
		           fw.write(output);    
		           fw.close();    
		    }
			catch(Exception ex){////System.outprintln(ex.getMessage());}    
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
			  ////System.outprintln("Attribute Count "+colCount);
			  ////System.outprintln("Row Count "+rowCount+"\n");
			  output+="Data Size (row) ="+rowCount+"\t Attribute Count= "+colCount+"\n";	
			  
			  output+="\nImporting Data from CSV file.........................";
			  ////System.outprintln("Importing Data from CSV file.........................");
			  float[][] onlyData=Rd.ReadCSVfile(DataFile,rowCount,colCount);
			  dataWithClass=Rd.ReadCSVfileWithClass(DataFile, rowCount, colCount);
			  PrintData(onlyData);
			  
			  //Find K centroid
			  HashMap<Integer,float[]> centroids=new HashMap<Integer,float[]>();
			  //First Random Assignation to Centroid
			  			  
			  for(int i=0;i<k;i++){
				  output+="\nIN K Iteration>>>>>>>>>>>>>>>>"+i;
				  ////System.outprintln("IN K Iteration>>>>>>>>>>>>>>>>"+i);
				  centroids=FindingKCentroid(centroids,rowCount,colCount,onlyData);				 
			  }
			  output+="Centroids............................\n";
			  ////System.outprintln("Centroids..........................");
			  //Show All Centriods
			  for (Map.Entry<Integer, float[]> entry : centroids.entrySet()) {
				  float[] ff=entry.getValue();
				  output+=entry.getKey()+" [";
				  ////System.outprint(entry.getKey()+" [");
				  for(int i=0;i<ff.length;i++){
					  output+=ff[i]+" ";
					 // //System.outprint(ff[i]+" ");
				  }
				  output+="]\n";
				  // //System.outprint("]\n");
				}
			 
			  //Start K-Means Algorithm
			  //Step 1: Get seeding centroid 
			  output+="\n***********************************************Starting K-Means Algorithm***********************************\n";
			  ////System.outprintln("\n***********************************************Starting K-Means Algorithm***********************************");
			  output+="Step 1: Get seeding centroid \n";
			  ////System.outprintln("Step 1: Get seeding centroid ");
			  for (Map.Entry<Integer, float[]> entry : centroids.entrySet()) {
				  float[] ff=entry.getValue();
				  output+=entry.getKey()+" [";
				  ////System.outprint(entry.getKey()+" [");
				  for(int i=0;i<ff.length;i++){
					  output+=ff[i]+" ";
					  ////System.outprint(ff[i]+" ");
				  }
				  output+="]\n";
				 //  //System.outprint("]\n");
				}
			  
			  //To Loop Here
			  int loopCount=0;
			  HashMap<Integer,float[]> newCentroid=new HashMap<Integer,float[]>();
			  HashMap<Integer,String> clusterValues=new HashMap<Integer,String>(); 
			  while(CheckHashMap(newCentroid,centroids)==false){
				  output+="\n\n#######################Iteration "+(++loopCount)+"#######################\n";
				  ////System.outprintln("\n\n#######################Iteration "+(++loopCount)+"#######################");
				  newCentroid=centroids;
				  HashMap<HashMap<Integer,float[]>,HashMap<Integer,String>> centroidAndCluster=KMeans(rowCount,colCount,onlyData,centroids);
				  for (Map.Entry<HashMap<Integer,float[]>,HashMap<Integer,String>> entry1 : centroidAndCluster.entrySet()) {
					  centroids=entry1.getKey();
					  clusterValues=entry1.getValue();
				  }
				  
			  }
			  
			  //Print Old Centroid
			  output+="\nPREVIOUS CENTROID.................................\n";
			  ////System.outprintln("\nPREVIOUS CENTROID.................................");
			  for (Map.Entry<Integer, float[]> entry1 : newCentroid.entrySet()) {
				    output+="Centroid "+entry1.getKey()+" = ";
					////System.outprint("Centroid "+entry1.getKey()+" = ");
					float[] avg_centroid_value2=entry1.getValue();
					for(int qq=0;qq<avg_centroid_value2.length;qq++){
						output+=df2.format(avg_centroid_value2[qq])+"\t";
						////System.outprint(df2.format(avg_centroid_value2[qq])+"\t");
					}
					output+="\n";
					////System.outprintln();
			  }
			  ////System.outprintln();	
			  output+="\n";
			  output+="\nCURRENT CENTROID.................................\n";
			  ////System.outprintln("\nCURRENT CENTROID.................................");
			  for (Map.Entry<Integer, float[]> entry1 : centroids.entrySet()) {
				    output+="Centroid "+entry1.getKey()+" = ";
					////System.outprint("Centroid "+entry1.getKey()+" = ");
					float[] avg_centroid_value2=entry1.getValue();
					for(int qq=0;qq<avg_centroid_value2.length;qq++){
						output+=df2.format(avg_centroid_value2[qq])+"\t";
						////System.outprint(df2.format(avg_centroid_value2[qq])+"\t");
					}
					output+="\n";
					////System.outprintln();
			  }
			  
			  if(CheckHashMap(newCentroid,centroids)==true){
				  output+="\nCentroids are now equal at Iteration "+loopCount+"\n";
				  ////System.outprintln("\nCentroids are now equal at Iteration "+loopCount);
				  output+="Finish\n";
				  ////System.outprintln("Finish");
			  }
			  
			  
			//Print Cluster
			  String oo=PrintCluster(output,clusterValues,onlyData);
			  output=oo;
			 
				 clusterResult=PrintFinalResult(clusterValues,onlyData);
				 output+="\n**************Total Elapsed Time*******************\n";
				 clusterResult+="\n**************Total Elapsed Time*******************\n";
				  long finish = System.currentTimeMillis();
				  long timeElapsed = finish - start;
				  seconds = (timeElapsed / 1000.0f);
				  //long minutes = (timeElapsed / 1000) / 60;
				  output+=" ELASPED TIME in Seconds= "+seconds+" seconds \n" ;
				  output+=" ELASPED TIME in MiliSeconds= "+timeElapsed+" miliseconds\n " ;
				  clusterResult+=" ELASPED TIME in Seconds = "+seconds+" seconds \n" ;
				  clusterResult+=" ELASPED TIME in MiliSeconds = "+timeElapsed+" seconds " ;
				  ////System.outprintln("**************Total Elapsed Time*******************\n");
				  ////System.outprintln(" Total Elaspsed Time = "+seconds+" seconds");
				  ////System.outprintln(" Total Elaspsed Time in MiliSeconds = "+timeElapsed+" miliseconds");
				  
				 
				 outputArea.setText(clusterResult);
		  }//Button if 
	}
	
	String PrintCluster(String output,HashMap<Integer,String> clusterValues,float[][] onlyData) {
		 output+="\n*********************************K-Means Plus Plus Clusters*********************************\n";
		  	////System.outprintln("\n*********************************K-Means Plus Plus Clusters*********************************");
		  	output+="Total Cluster ="+clusterValues.size()+"\n";
			////System.outprintln("Cluster count "+clusterValues.size());
			csvClusterData=new HashMap<Integer,String>();
			for (Entry<Integer, String> entry : clusterValues.entrySet()) {
				  String ff=entry.getValue();					  
				  String pp=entry.getValue();
				  String idName=pp;
				  int clusterID=entry.getKey();
				  pp="ID -"+pp.substring(0,pp.length()-1);
				  pp=pp.replace(","," ,ID-");
				 // //System.outprint("Cluster "+entry.getKey()+" ["+pp +"]");			 
				  output+="Cluster "+entry.getKey()+" ["+pp +"]";
				  idName=idName.substring(0,idName.length()-1);
				  String detailedDataPerCluster=DetailData(onlyData,idName);
				  String csvData=CSVPrintableData1(dataWithClass,idName,clusterID);
				  output+=detailedDataPerCluster+"\n\n";
				  ////System.outprintln(detailedDataPerCluster+"\n\n");
				  //////System.outprintln("CSV DATA\n");
				 // ////System.outprintln(csvData);
				  csvClusterData.put(clusterID, csvData);
				}
			
		return output;
	}
	public String CSVPrintableData1(String[][] data,String str,int cluster){
		String returnValue="";
		String[] objName=str.split(",");
		int col=data[0].length;
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
	String PrintFinalResult(HashMap<Integer,String> clusters,float[][] onlyData){
		String result="";
		result+="\n*********************************K-Means Plus Plus Clusters*********************************\n";
		  	result+="Cluster count "+clusters.size()+"\n";
			for (Entry<Integer, String> entry : clusters.entrySet()) {
				  String ff=entry.getValue();			  
				  String pp=entry.getValue();
				  String nn=entry.getValue();
				  pp="ID -"+pp.substring(0,pp.length()-1);
				  pp=pp.replace(","," ,ID -");			 		 
				  result+="Cluster "+entry.getKey()+" ["+pp +"]";
				  result+=DetailData(onlyData,nn.substring(0,nn.length()-1))+"\n\n";
				}	
		return result;
	}
	boolean CheckHashMap(HashMap<Integer,float[]> hm1,HashMap<Integer,float[]> hm2){
		boolean same=true;
		int count=0;
		////System.outprintln();
		float[] ff;
			
		if(!hm1.isEmpty()){
			for (Map.Entry<Integer, float[]> entry1 : hm1.entrySet()) {
				float[] avg_centroid_value1=entry1.getValue();
				ff=new float[avg_centroid_value1.length];
				for (Map.Entry<Integer, float[]> entry2 : hm2.entrySet()) {
					
					if(entry1.getKey()==entry2.getKey()){
						float[] avg_centroid_value2=entry2.getValue();
						if(!Arrays.equals(avg_centroid_value1, avg_centroid_value2))
						  same=false;
					}
								
				}			
			}	
		}
		else
			same=false;
			
		return same;
	}
	
	public HashMap<HashMap<Integer,float[]>,HashMap<Integer,String>> KMeans(int rCount, int cCount,float[][] data,HashMap<Integer,float[]> centroid){
		
		//Print Centroid Values
		HashMap<HashMap<Integer,float[]>,HashMap<Integer,String>> finalResult=new HashMap<HashMap<Integer,float[]>,HashMap<Integer,String>>();
		
		for (Map.Entry<Integer, float[]> entry2 : centroid.entrySet()) {
			output+="Cluster "+entry2.getKey()+" : ";
			//System.outprint("Cluster "+entry2.getKey()+" : ");
			float[] avg_centroid_value2=entry2.getValue();
			for(int qq=0;qq<avg_centroid_value2.length;qq++){
				output+=avg_centroid_value2[qq]+"\t";
				//System.outprint(avg_centroid_value2[qq]+"\t");
			}
			output+="\n";
			////System.outprintln();
		}
		output+="\n";
		////System.outprintln();
		float[] distance_values=new float[data.length];
		HashMap<Integer,float[]> newCentroid=new HashMap<Integer,float[]>();
		
		ArrayList<Integer> values=new ArrayList<Integer>();
		HashMap<Integer,String> clusterValues=new HashMap<Integer,String>(); 
		output+="\n";
		////System.outprintln();
		for(int i=0;i<rCount;i++){
			float[] distValue=new float[centroid.size()];
			for(int j=0;j<centroid.size();j++){
				//Step 2: Calculate Distance with 
				//Get collumns values
				float[] vv=centroid.get(j+1);
				////System.outprintln();
				float sum=0.0f;
				output+="Distance Object "+(i+1)+" and centroid "+(j+1)+"=";
				////System.outprint("Distance Object "+(i+1)+" and centroid "+(j+1)+"=");
				for(int pp=0;pp<cCount;pp++){					
					float num=data[i][pp]-vv[pp];
					num*=num;
					sum+=num;	
					output+="Square("+data[i][pp]+"-"+vv[pp]+")+";
					//System.outprint("Square("+data[i][pp]+"-"+vv[pp]+")+");
				}
				output+="=Sum="+df2.format(sum);
				////System.outprint("=Sum="+df2.format(sum));
				
				distValue[j]=(float)Math.sqrt(sum);
				output+="   -->  SqRT("+sum+")="+df2.format(distValue[j]);
				////System.outprint("   -->  SqRT("+sum+")="+df2.format(distValue[j]));
				
			}
			
			//Get Cluster
			int index=ClusterWithMinDistance(distValue);
			//String inputData=(i+1)+",";
			String str=(i+1)+",";
			if(clusterValues.isEmpty()){
				clusterValues.put(index, str);
				output+="\nPut in Cluster  "+index+"\n";
				////System.outprintln("\nPut in Cluster  "+index);
			}
			else{
				output+="\nPut in Cluster "+index+"\n";
				////System.outprintln("\nPut in Cluster "+index);
				if(clusterValues.containsKey(index)){
					str=clusterValues.get(index);
					str+=(i+1)+",";
					clusterValues.replace(index, str);
				}
				else{					
					str=(i+1)+",";
					clusterValues.put(index, str);
				}
			}			
		}//i	
		
		//Print Cluster
		output+="\nCluster count "+clusterValues.size()+"\n";
		////System.outprintln("\nCluster count "+clusterValues.size());
		for (Entry<Integer, String> entry : clusterValues.entrySet()) {
			  String ff=entry.getValue();
			  output+="Cluster "+entry.getKey()+" ["+entry.getValue()+"\n";
			  ////System.outprint("Cluster "+entry.getKey()+" ["+entry.getValue());
			  output+="]\n";
			 // //System.outprint("]\n");
			}		
		
		//Find New Centroid Average Value
		newCentroid=FindNewCentroidValue(clusterValues,data,rCount,cCount);
		finalResult.put(newCentroid, clusterValues);
		return finalResult;
	}
	
	HashMap<Integer,float[]> FindNewCentroidValue(HashMap<Integer,String> values,float[][] onlyData,int rCount,int colCount){
		float[] colValues=new float[colCount];
		HashMap<Integer,float[]> newCentroid=new HashMap<Integer,float[]>();
		for(int i=0;i<colValues.length;i++)
			colValues[i]=0.0f;
		
		for (Entry<Integer, String> entry : values.entrySet()) {
			 int indexNum=entry.getKey();
			 output+="\n";
			 ////System.outprintln();
			 output+="\nCluster "+indexNum+".................\n";
			 ////System.outprintln("\nCluster "+indexNum+".................");
			 String str=entry.getValue();
			 output+="Members "+str+"\n";
			 ////System.outprintln("Members "+str);
			 str=str.substring(0,str.length()-1);
			 String[] clusterMember=str.split(",");
			 int count=0;
			 for(int kk=0;kk<clusterMember.length;kk++){
				 output+="\n";
				 ////System.outprintln();
				 for(int i=0;i<rCount;i++){
					 
					 if((i+1)==Integer.parseInt(clusterMember[kk]))
					 {
						 output+="Object "+clusterMember[kk]+"  =>  ";
						 //System.outprint("Object "+clusterMember[kk]+"  =>  ");
						 for(int j=0;j<colCount;j++){
							 output+=onlyData[i][j] +"\t";
							 ////System.outprint(onlyData[i][j] +"\t");
							 colValues[j]+=onlyData[i][j];							
						 }
						 ++count;
						 break;
					 }
					 
				 }
			 }
			 output+="\n";
			 ////System.outprintln();
			 output+="................................................\n";
			 ////System.outprint("................................................\n");
			 output+="\t\t";
			 //System.outprint("\t\t");
			 for(int pp=0;pp<colCount;pp++){
				 output+=df2.format(colValues[pp])+"\t";
				 ////System.outprint(df2.format(colValues[pp])+"\t");				 
			 }
			 output+="\n";
			 ////System.outprintln();
			 float[] average=new float[colCount];
			// //System.outprint("\t\t");
			 output+="\t\t";
			 for(int pp=0;pp<colCount;pp++){
				 average[pp]=colValues[pp]/count;
				 output+=colValues[pp]+"/"+count+"="+average[pp]+"\t";
				 ////System.outprint(df2.format(colValues[pp])+"/"+count+"="+df2.format(average[pp])+"\t");
			 }	 
			 
			 for(int j=0;j<colCount;j++){
				 colValues[j]=0;
			 }
			 
			 newCentroid.put(indexNum,average);			 
		}		
		return newCentroid;
		
	}
	
	int ClusterWithMinDistance(float[] distValue){
		int index=0;
		float v=distValue[0];
		for(int i=0;i<distValue.length;i++){
			if(v>distValue[i])
			{
				v=distValue[i];
				index=i;
			}
		}
		return index+1;
	}
	
	public HashMap<Integer,float[]>  FindingKCentroid( HashMap<Integer,float[]> centroids,int rowCount,int colCount,float[][] onlyData){
		float[] distance=new float[rowCount];
		if(centroids.size()==0){
			int min=1;
			int max=rowCount;
			int randomCentroid=(int) ((Math.random() * ((max - min) + 1)) + min);
			
			float[] firstCentroid=onlyData[randomCentroid];
			centroids.put(1,firstCentroid);
			//Step -1 Take one center C1, choosing random from Dataset
			output+="\nStep -1 Take one center C1, choosing random from Dataset\nCentroid "+(randomCentroid+1)+" = [";
			////System.outprint("\nStep -1 Take one center , choosing random from Dataset\nCentroid "+(randomCentroid+1)+" = [");
			for(int q=0;q<firstCentroid.length;q++){
				output+=firstCentroid[q]+" ";
				// //System.outprint(firstCentroid[q]+" ");
			}
			 output+="]\n";
			 ////System.outprint("]\n");
		}
		else{
		float totalSum=0.0f;
		float[] temp_var=new float[centroids.size()];
		float[] dist_arr=new float[centroids.size()];
		output+="\nStep 2: Finding Distance...................\n";
		////System.outprintln("\nStep 2: Finding Distance...................");
		for(int rowNum=0;rowNum<rowCount;rowNum++){		
			for(int pp=0;pp<centroids.size();pp++){
				float sum=0.0f;
				output+="\nDistance for Object "+(rowNum+1)+" and Centroids "+(pp+1)+"=";
				//System.outprint("\nDistance for Object "+(rowNum+1)+" and Centroids "+(pp+1)+"=");
				//////System.outprintln("Centroid Size "+centroids.size());
				//find distance for both
				temp_var=centroids.get(pp+1);
				for(int q=0;q<colCount;q++){
					float num=temp_var[q]-onlyData[rowNum][q];
					num=num*num;
					output+="Square("+temp_var[q]+"-"+onlyData[rowNum][q]+")+";
					//System.outprint("Square("+temp_var[q]+"-"+onlyData[rowNum][q]+")+"); 
					sum+=num;
				}
				output=output.substring(0,output.length()-1)+"=";
				
				output+=df2.format(sum);
				//System.outprint(df2.format(sum));
				dist_arr[pp]=sum;
			}
			
			//Find minimum Distance
			float min=FindMinDistance(dist_arr);				
			////System.outprint("="+min+"\n");
			distance[rowNum]=min; 
			output+="\nDistance "+df2.format(distance[rowNum])+"\n";
			////System.outprintln("\nDistance "+df2.format(distance[rowNum]));
			
			//Step 3
			totalSum+=min;
		}
									 
			 output+="\nStep 3: Total Sum="+totalSum+"\n";
			 ////System.outprintln("\nStep 3: Total Sum="+totalSum);
			 float[] values=Step4CalProbablity(distance,totalSum);
			//Step 4: Calculate probability 
			 output+="\nStep 4 Distances.................\n";
			 ////System.outprintln("\nStep 4 Distances.................");
			 PrintValues(values);
			
			 
			 //Step 5 : Calculate Cumulative probability
			 //Get Random decimal number
			 output+="\nStep 5 : Calculate Cumulative probability\n";
			 ////System.outprintln("\nStep 5 : Calculate Cumulative probability");
			 float[] comuDist=Step5CumulativeProbablity(distance);
			 PrintValues(values);
			 
			 Random r = new Random();
			 double random = Math.abs((r.nextInt(21)-10) / 10.0);
			 if(random==0.0)
				 random=0.32;
			 else if(random==1.0)
				 random=0.4;
			 output+="Random Value =  "+random+"\n";
			 ////System.outprintln("Random Value =  "+random);
			 for(int pp=0;pp<values.length;pp++){
				 if(values[pp]>=random){
					 output+="Pick up Object "+(pp+1)+"\n";
					 ////System.outprintln("Pick up Object "+(pp+1));
					 centroids.put(centroids.size()+1, onlyData[pp]);
					 break;
				 }
			 }	
		}//end of else		
		return centroids;
	}
	
	public float[] Step5CumulativeProbablity(float[] dist){
		float sum=0.0f;
		for(int i=0;i<dist.length;i++){
			sum+=dist[i];
			dist[i]=sum;
		}
		return dist;
	}
	
	public void PrintValues(float[] vv){
		for(int i=0;i<vv.length;i++){
			output+="Distance of Object "+(i+1)+"="+df2.format(vv[i])+"\n";
			////System.outprintln("Distance of Object "+(i+1)+"="+df2.format(vv[i]));
		}
	}
	
	public float[] Step4CalProbablity(float[] dist,float totalsum){
		for(int i=0;i<dist.length;i++){
			dist[i]=dist[i]/totalsum;
			
		}
		return dist;
	}
	
	public float FindMinDistance(float[] darr){
		float min=darr[0]; 
		for(int i=0;i<darr.length;i++)
		{
			if(min>darr[i])
				min=darr[i];
		}
		return min;
	}
	
	
	void PrintAllDataAndWeight(float[][] data,float[][] weight){
		int rowCount=data.length;
		int attriColCount=data[0].length;
		int clusterColCount=weight[0].length;
		int id=0;
		
		output+="\nData & Weight .................................\n";
		////System.outprintln("\nData & Weight .................................");
		output+="ID\t";
		//System.outprint("ID\t");
		for(int i=0;i<attriColCount;i++){
			output+=" Data "+i+"\t";
			//System.outprint("Data "+i+"\t");
		}
			
		
		for(int i=0;i<clusterColCount;i++){
			//System.outprint("Cluster "+i+"\t");
			output+="Cluster "+i+"\t";
		}
		
		output+="Total \t";
		//System.outprint("Total\t");
		////System.outprintln();
		output+="\n";
		for(int i=0;i<rowCount;i++){
			id=i+1;
			//System.outprint(id+"\t");
			for(int k=0;k<attriColCount;k++){
				//System.outprint(data[i][k]+"\t");
				output+=data[i][k]+"\t";
			}
				
			float sum=0.0f;
			for(int p=0;p<clusterColCount;p++){
				//System.outprint(weight[i][p]+"\t");
				output+="\t"+weight[i][p]+"\t";
				sum+=weight[i][p];
			}
				
			output+=sum+"\t\n";
			//System.outprint(sum+"\t");
			////System.outprintln();
		}
	}
	
	
	
	void PrintData(float[][] onlyData){
		int rowCount=onlyData.length;
		int colCount=onlyData[0].length;
		
		output+="\n";
		for(int i=0;i<rowCount;i++){
			  for(int j=0;j<colCount;j++){
				  //System.outprint(onlyData[i][j]+"\t");
				  output+=onlyData[i][j ]+"\t";
			  }
			  //System.outprint("\n");
			  output+="\n";
		  }
	}	
	
	/*public static void main(String[] args) {
		//KDoublePlusAlgo f=new KDoublePlusAlgo("C:\\Users\\USER\\Desktop\\Book2.csv");
		KDoublePlusAlgo f=new KDoublePlusAlgo("C:\\Users\\USER\\Iris1.csv");		
	}*/
}
