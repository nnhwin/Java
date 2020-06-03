package clustering.form;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.swing.*;

public class MainForm extends JFrame implements ActionListener{

	private JFrame frame;
	  private JMenuBar menuBar;
	  private JMenu fileMenu;
	  private JMenu editMenu;
	  private JMenuItem datasetMenuItem,fDoublePlusMenuItem,twoAlgoResultMenuItem,sseMenuItem;
	  private JMenuItem fcskemansMenuItem,exitMenuItem;
	  private JMenuItem copyMenuItem;
	  private JMenuItem pasteMenuItem;
	  HashMap<String,HashMap<String,String>> allData;
	  String fname=null;
	  DataSetForm dsf;
	  
	  public static void main(String[] args)
	  {
	    // needed on mac os x
	    System.setProperty("apple.laf.useScreenMenuBar", "true");

	    // the proper way to show a jframe (invokeLater)
	   // SwingUtilities.invokeLater((Runnable) new MainForm());
	    new MainForm();
	  }

	  public MainForm()
	  {
	    frame = new JFrame("Java Menubar Example");
	    menuBar = new JMenuBar();
	   
	    // build the File menu
	    fileMenu = new JMenu("File");
	    datasetMenuItem = new JMenuItem("Load Dataset............");
	    fcskemansMenuItem = new JMenuItem("FCM K-Mean............");
	    fDoublePlusMenuItem=new JMenuItem("K-Means ++ Clustering..........");
	    twoAlgoResultMenuItem=new JMenuItem("Two Items Result.............");
	    sseMenuItem=new JMenuItem("SSE Analysis..................");
	    exitMenuItem=new JMenuItem("Exit");
	    fDoublePlusMenuItem.addActionListener(this);
	    exitMenuItem.addActionListener(this);
	    fcskemansMenuItem.addActionListener(this);
	    datasetMenuItem.addActionListener(this);
	    twoAlgoResultMenuItem.addActionListener(this);
	    sseMenuItem.addActionListener(this);
	    fileMenu.add(datasetMenuItem);
	    fileMenu.add(fcskemansMenuItem);
	    fileMenu.add(fDoublePlusMenuItem);
	    fileMenu.add(twoAlgoResultMenuItem);
	    fileMenu.add(sseMenuItem);
	    fileMenu.add(exitMenuItem);
	    fcskemansMenuItem.setEnabled(false);
	    fDoublePlusMenuItem.setEnabled(false);
	    twoAlgoResultMenuItem.setEnabled(false);
	    sseMenuItem.setEnabled(false);
	    
	    // build the Edit menu
	    editMenu = new JMenu("Edit");
	    
	    copyMenuItem = new JMenuItem("Copy");
	    pasteMenuItem = new JMenuItem("Paste");
	    editMenu.add(copyMenuItem);
	    editMenu.add(pasteMenuItem);

	    
	    
	    
	    
	    // add menus to menubar
	    menuBar.add(fileMenu);
	    menuBar.add(editMenu);

	    // put the menubar on the frame
	    frame.setJMenuBar(menuBar);

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setPreferredSize(new Dimension(400, 600));
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	  }

	  KDoublePlusAlgo kplusplus;
	  ClusteringMean1 cmean;
	  
	  public void actionPerformed(ActionEvent ev)
	  {
		  if(ev.getSource()==exitMenuItem){
			 System.exit(0);
			  		  
		  }
		  else if(ev.getSource()==datasetMenuItem){
			  dsf=new DataSetForm();	
			  fcskemansMenuItem.setEnabled(true);
			  fDoublePlusMenuItem.setEnabled(true);
			  twoAlgoResultMenuItem.setEnabled(true);
			  sseMenuItem.setEnabled(true);		  
		  }
		  else if(ev.getSource()==fcskemansMenuItem){
			  this.fname=dsf.getFname();			
			  if(this.fname!=null){
				cmean=new ClusteringMean1(fname);	
				
			 }//not fname null			  
		  }//button if
		  else if(ev.getSource()==fDoublePlusMenuItem){
			  this.fname=dsf.getFname();			
			  if(this.fname!=null){
				kplusplus=new KDoublePlusAlgo(fname);
				
				
			 }//not fname null			  
		  }//button if
		  else if(ev.getSource()==twoAlgoResultMenuItem){
			  //kplusplus		  
			  String strPlus=kplusplus.getClusterResult();
			  float secondPlus=kplusplus.getSeconds();
				
			//Cmeans
			  String strC=cmean.getFinalResult();
			  float secondC=cmean.getSeconds();		
			  CompareResults compareR=new CompareResults(strPlus,secondPlus,strC,secondC);
		  }
		  else if(ev.getSource()==sseMenuItem){
			  //kplusplus		  
			  new AnalysisSSEForm();
		  }
		  		 
	  }//action performed
	  float[][] GetTwoDimensionDataSet(ArrayList<String[]> data){
		  //System.out.println("Array");
		  String[] attributeArr=new String[data.size()];
		  for(int i=0;i<data.size();i++){
			  String[] attributes=data.get(i);
			  String str="";
			  
			  //for title
			  if(i==0)
				  continue;
			 
			  for(int j=0;j<attributes.length;j++)
			  {
				  if(j==0){
					  continue;
				  }
				  else if(j==attributes.length-1)
					  break;
				  else{
					  //remove id & class
					  str+=attributes[j]+",";
					//  System.out.print(str);
				  }
				  
			  }
			str=str.substring(0,str.length()-1);
			attributeArr[i]=str;
		  }
		  
		 /*//AttributeArr
		  for(int i=0;i<attributeArr.length;i++){
			  //System.out.println(attributeArr[i]);
		  }*/
		  
		  int colCount=0;
		  //Change two dimensional array
		  
		  float[][] twoDiData=new float[data.size()][attributeArr.length];
		  for(int i=1;i<attributeArr.length;i++)
		  {
			  String str=attributeArr[i];
			  //System.out.println(str);
			  String[] attArr= str.split(",|\\s|;");
			  colCount=attArr.length;
			  for(int j=0;j<attArr.length;j++){
				  twoDiData[i][j]=Float.parseFloat(attArr[j]);
				  
			  }
		  }
		
		  //System.out.println("TWo Dimension Data");
		  //Output TWo Dimension DataSet
		  /*for(int l=1;l<data.size();l++){
			  for(int j=0;j<colCount;j++){
				  //System.out.print(twoDiData[l][j]+",");
			  }
			  //System.out.println();
		  }
		  */
		  return twoDiData;
	  }//function
	  
}
