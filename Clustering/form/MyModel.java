package clustering.form;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

class MyModel extends AbstractTableModel {
    private String[] columnNames = { "1", "2", "3", "4", "5", "6" ,"7"};
    private ArrayList<String[]> Data = new ArrayList<String[]>();
    
    public MyModel(){
    	
    }
    public MyModel(String[] colNames,int count){
    	this.columnNames=colNames;
    }

    public void AddCSVData(ArrayList<String[]> DataIn) {
        this.Data = DataIn;
        this.fireTableDataChanged();
    }

    public int getColumnCount() {
        return columnNames.length;// length;
    }

    public int getRowCount() {
        return Data.size();
    }

    
    public String getColumnName(int col) {
        return columnNames[col];
    }

   
    public Object getValueAt(int row, int col) {
        return Data.get(row)[col];
    }
}
