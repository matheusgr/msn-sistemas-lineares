package br.edu.ufcg.msnlab.gui.controls;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CtrMatrixTable extends JPanel implements IsMatrixControl {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private JScrollPane scroll;

	private JButton butSetNames;
	private JButton butSetNumber;
	private JButton butClear;

	public CtrMatrixTable(){
		initializeComponents();
	}

	private void initializeComponents(){

		setLayout(null);

		addListeners();
		createComponents();
		putComponents();
		resizeComponents();

	}

	private void addListeners(){

		addComponentListener( new ComponentAdapter(){
			public void componentResized(ComponentEvent arg0) {
				resizeComponents();
			}
		});

	}

	private void createComponents(){
		
		table = new JTable();
		table.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                },
                new String [] {
                    "X", "Y", "Z", "Term"
                }
            ));

        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

        scroll = new JScrollPane(table);
        
        
       	butSetNames = new JButton("Set variable's names");
    	butSetNames.setBounds(0,0,130,20);
		butSetNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showVariablesNameInput();
			}
		});    	
       	butSetNumber = new JButton("Set number of variables");
    	butSetNumber.setBounds(130,0,130,20);
		butSetNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showVariablesNumberInput();
			}
		});
		butClear = new JButton("Clear");
		butClear.setBounds(260,0,130,20);
		butClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clearTable();
			}
		});
	}

	private void putComponents(){
		
		add( butSetNames );
		add( butSetNumber );
		add( butClear );
		add(scroll);
	}

	private void resizeComponents(){
		scroll.setBounds( 0, 20, getWidth(), getHeight() -20 );
	}

	public String getMatrix() {
		
		String result = "";
		for( int row=0; row<table.getRowCount(); row++ ){
			for( int col=0; col<table.getColumnCount(); col++ ){

				String value = (String)table.getModel().getValueAt(row,col);
				
				if( value == null || value.equals("") ) continue;
				if( !value.startsWith("+") || !value.startsWith("-") ) value = "+" + value;
				
				if( col<table.getColumnCount()-1 )
					value += table.getColumnName(col);
				else
					value = "=" + value;
				
				result += value;
				
			}
			result += "\n";
		}
		return result;
	}

	private void showVariablesNameInput(){

	    try{
	        
	        String result = JOptionPane.showInputDialog(null,"Enter the variables names", "X Y Z");
	        if( result.length() == 0 ){ throw new Exception("You didn't entered any variable name"); }
	
	        String[] rs = result.split(" ");
	        for( String s : rs ){
	
	            if( s.length() == 0 ) continue;
	            if( s.charAt(0) >='0' && s.charAt(0) <='9' ){
	                throw new Exception("The name of an variable can't start with an numeric char");
	            }
	
	        }
	        
	        setVariables( rs );
	
	    }catch( Exception e ){
	        JOptionPane.showMessageDialog( null, "Error: " + e.getMessage(), "MSNGUI", JOptionPane.ERROR_MESSAGE );
	    }
	}
	
	private void showVariablesNumberInput(){

	    try{
	        
	        String result = JOptionPane.showInputDialog(null,"Enter the number of desired amount of variables\nThey will get names in this pattern: Xk, k>=1");
	        if( result.length() == 0 ){ throw new Exception("You didn't entered any number name"); }
	
	        int number = Integer.parseInt( result );

	        String[] rs = new String[number];
	        for( int k=1; k<=number; k++ ){
	        	rs[k-1] = "X" + k;
	        }
	        setVariables( rs );
	
	    }catch( Exception e ){
	        JOptionPane.showMessageDialog( null, "Error: " + e.getMessage(), "MSNGUI", JOptionPane.ERROR_MESSAGE );
	    }
	}

	public void setVariables( String[] vars ){

        String[] headers = new String[vars.length+1];
        for( int k=0; k<vars.length; k++ ){
            headers[k] = vars[k];
        }
        headers[headers.length-1] = "Term";
        
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [vars.length][headers.length],
            headers
        ));

    }
	
	public String[] getVariablesNames(){
		String[] headers = new String[table.getColumnCount()];
		for( int k=0; k<table.getColumnCount(); k++ ){
			headers[k] = table.getColumnName(k);
		}
		return headers;
	}
	
	public void clearTable(){

		table.setModel(new javax.swing.table.DefaultTableModel(
                new Object [table.getRowCount()][table.getColumnCount()],
                getVariablesNames()
            ));
		
	}
}