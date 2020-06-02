package translation.db.packages;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ShowTableData extends JFrame implements ActionListener{

    JFrame frame1;
    Connection con;
    ResultSet rs, rs1;
    Statement st, st1;
    PreparedStatement pst;

    String ids;
   
    JComboBox tablecombo;
    String from;
    JButton showBut,editBut;
    DefaultTableModel model;
    JScrollPane scroll;
    JPanel tablePanel=new JPanel();
    JPanel mainPanel=new JPanel();
    Container cont;
    public ShowTableData(){	
    	cont=this.getContentPane();  	
    	
    	mainPanel.setLayout(new BorderLayout());
    	JPanel choosePanel=new JPanel();  	
    	tablePanel.setLayout(new GridLayout(1,1,10,10));
    	choosePanel.setLayout(new GridLayout(1,3));
    	
    	JPanel p1=new JPanel();
    	JPanel p2=new JPanel();
    	p2.setLayout(new GridLayout(3,1,10,10));
    	JPanel p3=new JPanel();
    	JLabel lbl1=new JLabel(".");
    	JLabel lbl2=new JLabel("Choose Table");
    	JLabel lbl3=new JLabel(".");
    	
    	p2.add(lbl2);
		String criteriaCombo[]={"Lexicon", "Rules Table"};        
		tablecombo=new JComboBox(criteriaCombo);    
	    p2.add(tablecombo);
	    
	    JPanel pp=new JPanel();
	    pp.setLayout(new GridLayout(1,2));
    	showBut=new JButton("Show");
    	showBut.addActionListener(this);
    	
    	editBut=new JButton("Edit");
    	editBut.addActionListener(this);
    	editBut.setEnabled(false);
    	pp.add(showBut);
    	pp.add(editBut);
    	
    	p2.add(pp);
    	p1.add(lbl1);
    	
    	p3.add(lbl3);
    	choosePanel.add(p1);
    	choosePanel.add(p2);
    	choosePanel.add(p3);

      
    	mainPanel.add(choosePanel,BorderLayout.BEFORE_FIRST_LINE);   	
    	cont.add(mainPanel);	
    	
    	this.setTitle("Show Database Tables.................");
    	this.setVisible(true);
    	this.setBounds(50,30,1000,600);
    	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e){
    	if(e.getSource()==showBut){
    		editBut.setEnabled(true);
    		JTable table=new JTable();
    		if(tablecombo.getSelectedIndex()==0){
    			tablePanel.removeAll();
    			tablePanel.repaint();
    			table.removeAll();
    			table=showDataFromDB("Lexicon");
    			scroll = new JScrollPane(table);
    		    scroll.setHorizontalScrollBarPolicy(
    		                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    		    scroll.setVerticalScrollBarPolicy(
    		                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);       
    		        tablePanel.add(scroll);
    		        mainPanel.add(tablePanel,BorderLayout.CENTER);
    		        this.setBounds(50,30,1100,600);
   		        
    		}else{
    			table.removeAll();
    			tablePanel.removeAll();
    			tablePanel.repaint();
    			table=showDataFromDB("rules");
    			scroll = new JScrollPane(table);
    		    scroll.setHorizontalScrollBarPolicy(
    		                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    		    scroll.setVerticalScrollBarPolicy(
    		                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);       
    		        tablePanel.add(scroll);
    		        mainPanel.add(tablePanel,BorderLayout.CENTER);
    		        this.setBounds(50,30,1150,600);
    		}
    		
    		
    	}//showBut
    	else if(e.getSource()==editBut){
    		if(tablecombo.getSelectedIndex()==0){
    			try {
					DBEditForm db=new DBEditForm("Lexicon");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    		else if(tablecombo.getSelectedIndex()==1){
    			try {
					DBEditForm db=new DBEditForm("rules");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    	}//else
    }
    
   /* public ShowTableData(String tablename) {
    	showDataFromDB(tablename);
    }
*/
    
    public JTable showDataFromDB(String tablename) {
        DefaultTableModel model = new DefaultTableModel();       
        try {
			ArrayList<String> colNames=GetColumnNames(tablename);
			String[] tempstr=new String[colNames.size()];
			//System.out.print("Col size "+colNames.size());
			for (int counter = 0; counter < colNames.size(); counter++) { 		      
		          tempstr[counter]=colNames.get(counter);
		    model.setColumnIdentifiers(tempstr);
		      } 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JTable table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);       

//String textvalue = textbox.getText();
        String eng = "";
        String chin = "";
        String id="";
        String phrasetype="";
        try {
        	Class.forName("com.mysql.jdbc.Driver");
    		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/translationdb?useUnicode=yes&characterEncoding=UTF-8","root","root"); 	
    		pst = con.prepareStatement("select * from "+tablename);
        	ResultSet rs = pst.executeQuery();
        	int i = 0;
            
            if(tablename.equals("Lexicon"))
            {           	
            	 while (rs.next()) {
                 	//id=rs.getString("ID");
            		 id=String.valueOf(++i);
                     eng = rs.getString("EngWord");
                     chin = rs.getString("ChinWord");   
                     model.addRow(new Object[]{id,eng, chin});
                 }
            }
            else if(tablename.equals("rules"))
            {
            	 while (rs.next()) {
                 	//id=rs.getString("ID");
            		 id=String.valueOf(++i);
                     eng = rs.getString("EngPatterns");
                     chin = rs.getString("ChinPatterns");   
                     phrasetype = rs.getString("PhraseTypes");   
                     model.addRow(new Object[]{id,eng, chin,phrasetype});
                 }
            }
        } catch (Exception ex) {

            System.out.println("Errror"+ex.getMessage());
        }      
        return table;
    }
    
    ArrayList<String> GetColumnNames(String tablename) throws SQLException{
    	ArrayList<String> columns = new ArrayList<>();
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/translationdb?useUnicode=yes&characterEncoding=UTF-8","root","root");
    	 Statement statement = con.createStatement();
         ResultSet resultSet = statement.executeQuery(
             "SELECT * FROM "+tablename);

         ResultSetMetaData metadata = resultSet.getMetaData();
         int columnCount = metadata.getColumnCount()+1;

         
         for (int i = 1; i < columnCount; i++) {
             String columnName=null;
			try {
				columnName = metadata.getColumnName(i);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             columns.add(columnName);
         }
    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return columns;
    }

    public static void main(String args[]) {

        new ShowTableData();

    }

}