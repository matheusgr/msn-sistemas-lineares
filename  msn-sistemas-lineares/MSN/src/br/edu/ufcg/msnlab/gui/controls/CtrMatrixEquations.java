package br.edu.ufcg.msnlab.gui.controls;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class CtrMatrixEquations extends JPanel implements IsMatrixControl{

	private static final long serialVersionUID = 1L;

	private ArrayList<JTextField> equationsField = new ArrayList<JTextField>();

	private JPanel equationsPane;
	private JScrollPane scroll;
	
	private JButton butAdd;
	private JButton butSub;
	private JButton butSet;
	
    public CtrMatrixEquations(){
    	initializeComponents();
    }
    
    private void setNumberOfFields( int number ){
    	
    	if( number > equationsField.size() ){
    		while( equationsField.size() < number ){
    			addField();
    		}
    	}else if( number < equationsField.size() ){
    		while( equationsField.size() > number ){
    			removeField();
    		}
    	}
    	
    }

    private void removeField(){
    	
    	if( equationsField.size() == 0 ) return;
    	
    	equationsPane.remove( equationsField.get(equationsField.size()-1) );
    	equationsField.remove( equationsField.size() -1 );

    	adjustEquationsPaneHeight();
    	
   }
    
    private void adjustEquationsPaneHeight(){
    	
    	int y = 0;
    	if( equationsField.size() > 0 ){
    		JTextField f = equationsField.get( equationsField.size() -1 );
    		y = (int)f.getLocation().getY() + f.getHeight()+2;
    	}

    	equationsPane.setPreferredSize( new Dimension(scroll.getWidth()-25,y+20) );
    	scroll.setViewportView(equationsPane);

    }
    
    private void addField(){
    	
    	JTextField t = new JTextField();
    	
    	int y;
    	if( equationsField.size() > 0 ){
    		JTextField f = equationsField.get( equationsField.size() -1 );
    		y = (int)f.getLocation().getY() + f.getHeight()+2;
    	}else{
    		y = 0;
    	}
    	
    	t.setBounds( 0, y, scroll.getWidth()-25, 20 );

    	equationsField.add( t );
    	equationsPane.add( t );

    	equationsPane.setPreferredSize( new Dimension(scroll.getWidth()-25,y+t.getHeight()) );
    	scroll.setViewportView(equationsPane);
    	
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

    	equationsPane = new JPanel();
    	equationsPane.setLayout(null);
    	
    	scroll = new JScrollPane(equationsPane);

    	butAdd = new JButton("Add");
    	butAdd.setBounds(0,0,60,20);
		butAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addField();
			}
		});    	
    	
    	butSub = new JButton("Sub");
    	butSub.setBounds(60,0,60,20);
		butSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				removeField();
			}
		});    	

    	butSet = new JButton("Set");
    	butSet.setBounds(120,0,60,20);
		butSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				try{
					String input = JOptionPane.showInputDialog("Type the number of desired fields");
					int number = Integer.parseInt(input);
					setNumberOfFields(number);
				}catch( Exception e ){
					JOptionPane.showMessageDialog(null, "An error occurred while reading the number:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
				}
			}
		});    	
    	
    }
    
    private void putComponents(){
    	add(butAdd);
    	add(butSub);
    	add(butSet);
    	
    	add(scroll);
    }
    
    private void resizeComponents(){
    	scroll.setBounds( 0, 30, getWidth(), getHeight()-30 );
    }

	public String getMatrix() {
		String result = "";
		for( JTextField t : equationsField ){
			result += t.getText() + "\n";
		}
		return result;
	}

	/*
	public void setMatrix(String matrixString) {
		
		ArrayList<String> ok = new ArrayList<String>();
		String[] ss = matrixString.split("\n");
		for( String s : ss ){
			if( s.trim().equals("") ) continue;
			ok.add(s);
		}

		setNumberOfFields(ok.size());
		
		for( int k=0; k<ok.size(); k++ ){
			equationsField.get(k).setText(ok.get(k));
		}
		return;
	}*/
}
