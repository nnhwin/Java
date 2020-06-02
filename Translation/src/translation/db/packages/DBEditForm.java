package translation.db.packages;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBEditForm extends JFrame implements ActionListener{
	String tablename=null;
	Frame f; 
	
	JButton addBut,updateBut,searchBut,deleteBut,clearBut;
	Connection con;
	Label[] label;
	TextField[] text;
	int columnCount=0;
	private JButton btnFirst;
	private JButton btnLeft;
	private JButton btnRight;
	private JButton btnLast;
	Panel lowerPanel;
	public DBEditForm(String tablename) throws ClassNotFoundException, SQLException {
		this.tablename=tablename;
		//System.out.println(this.tablename);
		f=new JFrame();
		Panel panel=new Panel();
		panel.setLayout(new GridLayout(1,2));			
		addMyLayout(panel);
		loadData();

		
		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
				
		f.add(panel);
		f.setLocation(dim.width/2, dim.height/2);
		f.setSize(900,400);
		f.setLocationRelativeTo(null);
		f.setTitle("Edit Database Records.....................");
		f.setVisible(true);
		
	}
	
	public void loadData() throws ClassNotFoundException, SQLException{
		openConnection();
		Statement statement=con.createStatement();
		String query="select * from "+tablename;
		ResultSet resultSet=statement.executeQuery(query);
		ResultSetMetaData rsmd=resultSet.getMetaData();
		columnCount=rsmd.getColumnCount();
		if(resultSet.next()){
			for(int i=1;i<columnCount+1;i++){
				text[i-1].setText(resultSet.getString(i));;
			}
		}
		closeConnection();
	}
	
	public void addMyLayout(Panel p) throws ClassNotFoundException, SQLException{
		openConnection();
		p.setLayout(new BorderLayout());
		
		Panel upperpanel=new Panel();
		Panel lowerpanel=new Panel();
		p.add(upperpanel,BorderLayout.CENTER);
		p.add(lowerpanel,BorderLayout.AFTER_LAST_LINE);
		
		Statement statement=con.createStatement();
		String query="select * from "+tablename;
		ResultSet resultSet=statement.executeQuery(query);
		ResultSetMetaData rsmd=resultSet.getMetaData();
		columnCount=rsmd.getColumnCount();
		String[] str=new String[columnCount];
		label=new Label[columnCount];
		text=new TextField[columnCount];
		int layers=columnCount/2;
		System.out.println("Col count "+columnCount);
		if(columnCount>=6){
			upperpanel.setLayout(new GridLayout(layers,2,10,10));
			for(int i=1;i<columnCount+1;i++){
				upperpanel.add(label[i-1]=new Label(rsmd.getColumnName(i)));
				upperpanel.add(text[i-1]=new TextField(10));
				text[i-1].setSize(100,200);
			}
		}
		else{
			upperpanel.setLayout(new GridLayout(5,2,10,20));
		//	upperpanel.setLayout(new BorderLayout());
			for(int i=1;i<columnCount+1;i++){
				upperpanel.add(label[i-1]=new Label(rsmd.getColumnName(i)),BorderLayout.AFTER_LAST_LINE);
				upperpanel.add(text[i-1]=new TextField(10),BorderLayout.NORTH);			
				text[i-1].setSize(100,50);
				text[i-1].setFont(new Font("Myanmar Text",Font.PLAIN,14));		
			}
		}
		
		searchBut=new JButton("Search");
		searchBut.setToolTipText("Enter data TextBox to search");
		addBut=new JButton("Insert");
		updateBut=new JButton("Update");
		deleteBut=new JButton("Delete");
		clearBut=new JButton("Clear");
		
		lowerpanel.setLayout(new FlowLayout());
		lowerpanel.add(searchBut);
		lowerpanel.add(updateBut);
		lowerpanel.add(addBut);
		lowerpanel.add(deleteBut);
		lowerpanel.add(clearBut);
		
		btnFirst = new JButton("First");
		lowerpanel.add(btnFirst);
		
		btnLeft = new JButton("<<");
		lowerpanel.add(btnLeft);
		
		btnRight = new JButton(">>");
		lowerpanel.add(btnRight);
		
		btnLast = new JButton("Last");
		lowerpanel.add(btnLast);
		
		searchBut.addActionListener(this);
		addBut.addActionListener(this);
		updateBut.addActionListener(this);
		deleteBut.addActionListener(this);
		clearBut.addActionListener(this);
		btnLeft.addActionListener(this);
		btnLast.addActionListener(this);
		btnFirst.addActionListener(this);
		btnRight.addActionListener(this);
		closeConnection();
	}
	
	public List getTableList() throws ClassNotFoundException, SQLException{
		openConnection();
		List<String> tablenames=new ArrayList<String>();
		DatabaseMetaData md=con.getMetaData();
		ResultSet rs=md.getTables(null, null, "%", null);
		while(rs.next()){
			if(rs.getString(4).equalsIgnoreCase("TABLE"))
				tablenames.add(rs.getString(3));
		}
		return tablenames;
	}
	public void openConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/translationdb?useUnicode=yes&characterEncoding=UTF-8","root","root");
	}
	public void closeConnection() throws SQLException
	{
		con.close();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand()=="Insert"){
			
				String[] data=new String[columnCount+1];
					//get data
					for(int i=1;i<columnCount+1;i++){
						data[i-1]=text[i-1].getText();
					}
					try {
						InsertData(data);
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
		}
		else if(e.getActionCommand()=="Clear"){
			ClearData();
		}
		else if (e.getActionCommand()=="Delete"){
			String id=text[0].getText();
			if(id==null || id.equals("")){
				JOptionPane.showMessageDialog(null, "You should have data in the  text to update.");
			}
			else{
				try {
					openConnection();
					String sql_delete="delete from "+tablename+" where ID='"+id+"'";
					Statement st=con.createStatement();
					st.executeUpdate(sql_delete);
					if(true)
						JOptionPane.showMessageDialog(null, "Deleting data is successfully done.");
					closeConnection();
					ClearData();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if(e.getActionCommand()=="Update"){
			String id=text[0].getText();
			if(id==null || id.equals("")){
				JOptionPane.showMessageDialog(null, "You should have data in the  text to update.");
			}
			else{
				int confirm_ans=JOptionPane.showConfirmDialog(null, "Are you sure to update this Data");
				String[] data=new String[columnCount+1];
				if(confirm_ans==0){
					//get data
					for(int i=1;i<columnCount+1;i++){
						data[i-1]=text[i-1].getText();
					}
					try {
						UpdateData(data,id);
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}// confirm
				
			}//else
		}//else if
		else if(e.getActionCommand()=="First"){
			try {
				loadData();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getActionCommand()=="Last"){
			try {
				loadLastData();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getActionCommand()=="<<"){
			String id=text[0].getText();
			if(id==null || id.equals("")){
				JOptionPane.showMessageDialog(null, "You should have data in ID text to go next");
			}
			else{
				String temp=id.trim();
				int id_int=Integer.parseInt(temp);
				--id_int;
				temp=String.valueOf(id_int);
				try {
					SearchData(temp);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if(e.getActionCommand()==">>"){
			String id=text[0].getText();
			if(id==null || id.equals("")){
				JOptionPane.showMessageDialog(null, "You should have data in ID text to go next");
			}
			else{
				String temp=id.trim();
				int id_int=Integer.parseInt(temp);
				++id_int;
				temp=String.valueOf(id_int);
				try {
					SearchData(temp);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if(e.getActionCommand()=="Search"){
			String mmword=text[1].getText();
			
				String temp=mmword.trim();
				String id=text[0].getText();
				try {
					if((temp.equals("") || temp==null)&& (!id.equals("") || id!=null))
						SearchMMWordData(id,"num");
					else
						SearchMMWordData(temp,"string");
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
		}
		if(e.getActionCommand()=="Show"){
			lowerPanel.repaint();
			JButton b=new JButton("SS");
			lowerPanel.add(b);
			lowerPanel.setForeground(Color.red);
			lowerPanel.repaint();
			System.out.println("here@@");
		}
	}
	
	public void ClearData(){
		if(columnCount>0){
			for(int i=1;i<columnCount+1;i++){
				text[i-1].setText("");
			}
		}
	}
	public void SearchMMWordData(String mmword,String type) throws ClassNotFoundException, SQLException{
		openConnection();
		Statement statement=con.createStatement();
		String query=null;
		
		if(type.equals("num"))
			query="select * from "+tablename+" where id="+mmword.trim()+"";
		else if(type.equals("string"))
			query="select * from "+tablename+" where EngPatterns='"+mmword.trim()+"'";
		ResultSet resultSet=statement.executeQuery(query);
		ResultSetMetaData rsmd=resultSet.getMetaData();
		columnCount=rsmd.getColumnCount();
		if(resultSet.next())
		{
			for(int i=1;i<columnCount+1;i++){
				text[i-1].setText(resultSet.getString(i));;
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "No more data is found.");
		}
		closeConnection();
	}
	
	public void InsertData(String data[])throws ClassNotFoundException, SQLException{
		openConnection();
		Statement statement=con.createStatement();
		String id=data[0];
		ResultSet rs=statement.executeQuery("select * from "+tablename);
		ResultSetMetaData rsmd=rs.getMetaData();
		int count=rsmd.getColumnCount();
		
		String sql_str="insert into "+tablename+"(";
		for(int i=2;i<count+1;i++){
			String name=rsmd.getColumnName(i);
			sql_str+=name+",";
		}
		sql_str=sql_str.substring(0,sql_str.length()-1)+") values(";
			
		for(int i=2;i<columnCount+1;i++){
			sql_str+="'"+data[i-1]+"',";
		}
		sql_str=sql_str.substring(0,sql_str.length()-2)+"')";
		statement.executeUpdate(sql_str);
		if(true)
			JOptionPane.showMessageDialog(null, "Your new data is successfully inserted to the database.");
		else
			JOptionPane.showConfirmDialog(null, "Error in Data Insert");
		closeConnection();
	}
	
	public void UpdateData(String[] data,String idvalue) throws ClassNotFoundException, SQLException{
		openConnection();
		Statement statement=con.createStatement();
		ResultSet rs=statement.executeQuery("select * from "+tablename);
		ResultSetMetaData rsmd=rs.getMetaData();
		int count=rsmd.getColumnCount();
		//System.out.println("Column count = "+count);
		String sql_str="update "+tablename+" set ";
		for(int i=count;i>=2;i--){
			String name=rsmd.getColumnName(i);
			//System.out.println("Column name="+name+"...data="+data[i-1]);
			sql_str+=name+"='"+data[i-1]+"',";
		}
		sql_str=sql_str.substring(0,sql_str.length()-1);
		String idname=rsmd.getColumnName(1);
		sql_str+=" where "+idname+"='"+data[0]+"'";
		//System.out.println("SQL=>"+sql_str);
		statement.executeUpdate(sql_str);
		if(true)
			JOptionPane.showMessageDialog(null, "Updating Data is successfully done.");
		
		closeConnection();
	}
	
	public void loadLastData() throws ClassNotFoundException, SQLException{
		openConnection();
		Statement statement=con.createStatement();
		String query="select * from "+tablename;
		ResultSet resultSet=statement.executeQuery(query);
		ResultSetMetaData rsmd=resultSet.getMetaData();
		columnCount=rsmd.getColumnCount();
		resultSet.last();
	
			for(int i=1;i<columnCount+1;i++){
				text[i-1].setText(resultSet.getString(i));;
			}
		
		closeConnection();
	}
	public void SearchData(String id) throws ClassNotFoundException, SQLException{
		openConnection();
		Statement statement=con.createStatement();
		String query="select * from "+tablename+" where ID='"+id.trim()+"'";
		ResultSet resultSet=statement.executeQuery(query);
		ResultSetMetaData rsmd=resultSet.getMetaData();
		columnCount=rsmd.getColumnCount();
		if(resultSet.next())
		{
			for(int i=1;i<columnCount+1;i++){
				text[i-1].setText(resultSet.getString(i));;
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "No more data is found.");
		}
		closeConnection();
	}
	
	public static void main(String[] args){
		
		try {
			new DBEditForm("criteriatable");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
