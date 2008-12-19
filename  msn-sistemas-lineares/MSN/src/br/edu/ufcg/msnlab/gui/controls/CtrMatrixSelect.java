package br.edu.ufcg.msnlab.gui.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CtrMatrixSelect extends javax.swing.JPanel {

	public static final int MATRIX_TEXT = 0;
	public static final int MATRIX_EQUATIONS = 1;
	public static final int MATRIX_TABLE = 2;
	
	private static final long serialVersionUID = 1L;
	
	private JPanel currentControl = null;
	
	private JButton butText;
	private JButton butEquations;
	private JButton butTable;
	
	public CtrMatrixSelect(){

		setLayout(null);

		initializeComponents();
		try {
			setMatrixType(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		addComponentListener( new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {
				resizeComponents();
			}
		});
		
	}
	
	private void initializeComponents(){
		
		butText = new JButton("Text mode");
		butText.setBounds(0,0,100,20);
		butText.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					setMatrixType(MATRIX_TEXT);
				}catch( Exception ex ){
					JOptionPane.showMessageDialog(null, "An error occurred while changing the matrix type:\n" + ex.getMessage() );
				}
			}
		});

		butEquations = new JButton("Equation mode");
		butEquations.setBounds(100,0,100,20);
		butEquations.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					setMatrixType(MATRIX_EQUATIONS);
				}catch( Exception ex ){
					JOptionPane.showMessageDialog(null, "An error occurred while changing the matrix type:\n" + ex.getMessage() );
				}
			}
		});

		butTable = new JButton("Table mode");
		butTable.setBounds(200,0,100,20);
		butTable.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					setMatrixType(MATRIX_TABLE);
				}catch( Exception ex ){
					JOptionPane.showMessageDialog(null, "An error occurred while changing the matrix type:\n" + ex.getMessage() );
				}
			}
		});

		addButtons();

	}
	
	public void setMatrixType( int matrixType ) throws Exception{

		if( currentControl != null ){
			remove(currentControl);
			repaint();
		}
		
		switch( matrixType ){
		case 0: currentControl = new CtrMatrixText(); break;
		case 1: currentControl = new CtrMatrixEquations(); break;
		case 2: currentControl = new CtrMatrixTable(); break;
		default: throw new Exception("Invalid type of Matrix");
		}

		currentControl.setBounds(0,20,500,300);
		add( currentControl );
		resizeComponents();
		
	}
	
	private void addButtons(){
		add( butText );
		add( butEquations );
		add( butTable );
	}
	
	public IsMatrixControl getCurrentMatrixControl(){
		if( currentControl == null ) return null;
		return (IsMatrixControl)currentControl;
	}
	
	public void resizeComponents(){
		if( currentControl != null )currentControl.setBounds( 0, 20, getWidth(), getHeight() - 20);
	}

}
