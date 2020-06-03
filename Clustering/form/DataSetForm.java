package clustering.form;

import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;

import java.io.File;

public class DataSetForm extends JFrame implements ActionListener{
	private final JTable table;
	private final JPanel mainPanel;
	JButton btn;
	HashMap<String,HashMap<String,String>> allData;
	public String fname;
	public DataSetForm(){
		
		//super(new BorderLayout(3, 3));
		//super();
		Container con = this.getContentPane();  
		// Panel is a container
		mainPanel=new JPanel();
		mainPanel.setLayout(new FlowLayout());
		btn = new JButton("Load CSV File..........."); // Button is a component
		btn.addActionListener(this);
		
		this.mainPanel.add(btn,BorderLayout.BEFORE_LINE_BEGINS); 
		
		this.table = new JTable(new MyModel());
        this.table.setPreferredScrollableViewportSize(new Dimension(700, 500));
        this.table.setFillsViewportHeight(true);
               
        JPanel ButtonOpen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(ButtonOpen, BorderLayout.SOUTH);
        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        // Add the scroll pane to this panel.
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
        // add a nice border
        //this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setSize(500,500);   
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Create and set up the content pane.
        
        setSize(500,500);
        pack();
        setVisible(true);
        con.add(mainPanel);
        setLocation(200,80);
	}
	public String getFname(){
		return this.fname;
	}
	 private static void createAndShowGUI() {
	        // Create and set up the window.
	        JFrame frame = new JFrame("DataSet Form....................");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        // Create and set up the content pane.
	        DataSetForm newContentPane = new DataSetForm();
	        frame.setContentPane(newContentPane);
	        // Display the window.
	        frame.setSize(500,500);
	        frame.pack();
	        frame.setVisible(true);
	    }
	public void actionPerformed(ActionEvent ev)
	{
	  if(ev.getSource()==btn){
		  System.out.println("Load file open");
		  JFileChooser fileChooser = new JFileChooser();
		  fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		  int result = fileChooser.showOpenDialog(this);
		  if (result == JFileChooser.APPROVE_OPTION) {
		      File selectedFile = fileChooser.getSelectedFile();
		      fname=selectedFile.getAbsolutePath();
		      System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		  }
		  
		  if(!fname.equals("") && fname!=null)
		  {
			  CSVFile Rd = new CSVFile();
			  File DataFile = new File(fname);
			  ArrayList<String[]> Rs1=Rd.ReadCSVfile(DataFile);
			  String[] rr=Rd.columnNames;
			  System.out.println("Column Names: ");
	 
		      MyModel NewModel = new MyModel(rr,Rd.count);
		      this.table.setModel(NewModel);
		   
		      NewModel.AddCSVData(Rs1);
		      		     	  
		  }
		 
	  }
	}
	/*public static void main(String[] args){
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
		DataSetForm f=new DataSetForm();
		
        
	}*/
}


