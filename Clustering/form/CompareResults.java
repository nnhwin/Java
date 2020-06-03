package clustering.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CompareResults extends JFrame implements ActionListener{

	
		JTextArea outputArea;
		JButton exitBut;
		public CompareResults(String plusStr,float plusTime,String meanStr,float meanTime){
			
			//df2.setRoundingMode(RoundingMode.UP);
			Container con=this.getContentPane();
			JPanel mainPanel=new JPanel();
			mainPanel.setLayout(new BorderLayout());
			
			JPanel buttonPanel=new JPanel();
			buttonPanel.setLayout(new GridLayout(1,3));
			exitBut=new JButton("Exit");
			buttonPanel.add(exitBut);
			exitBut.addActionListener(this);
			
			JPanel outputPanel=new JPanel();
			outputPanel.setLayout(new GridLayout(1,1));
			outputArea=new JTextArea(300,300);
			JScrollPane scrolll = new JScrollPane(outputArea);
			outputPanel.add(scrolll);
			
			String str="";
			str+=plusStr;
			//str+="\nElapsed Time for K-Mean Plus Plus..........\n";
			//str+=plusTime+" seconds\n\n";
			
			str+=meanStr;
			//str+="\nElapsed Time for K-Mean Plus Plus..........\n";
			//str+=meanTime+" seconds \n\n";
			
			outputArea.setText(str);
			
			mainPanel.add(buttonPanel,BorderLayout.SOUTH);
			mainPanel.add(outputPanel,BorderLayout.CENTER);
			
			con.add(mainPanel);
			this.setTitle("Compare Results of Two Algorithms");
			this.setSize(500,500);
			this.setLocation(250,120);
		    setVisible(true);
		    con.add(mainPanel);
		}
		
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==exitBut){
				this.dispose();
			}
		}
	

}
