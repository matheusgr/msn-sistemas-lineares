package br.edu.ufcg.msnlab.gui.controls;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class CtrMatrixSelect extends javax.swing.JPanel {

	public static final int MATRIX_TEXT = 0;
	public static final int MATRIX_EQUATIONS = 1;
	public static final int MATRIX_TABLE = 2;
	
	private static final long serialVersionUID = 1L;
	
	private JPanel currentControl = null;
	
	private JButton butText;
	private JLabel inputLabel;
	private JPanel sysPanel;
	private JPanel buttonPanel;
	private JButton butEquations;
	private JButton butTable;
	
	public CtrMatrixSelect(){

		setLayout(null);
		this.setSize(518, 283);

		initializeComponents();
		try {
			setMatrixType(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private void initializeComponents(){

		addButtons();

	}
	
	public void setMatrixType( int matrixType ) throws Exception{

		if( currentControl != null ){
			sysPanel.remove(currentControl);
			validate();
		}
		
		switch( matrixType ){
		case 0: currentControl = new CtrMatrixText(); break;
		case 1: currentControl = new CtrMatrixEquations(); break;
		case 2: currentControl = new CtrMatrixTable(); break;
		default: throw new Exception("Invalid type of Matrix");
		}
		currentControl.setPreferredSize(new java.awt.Dimension(491, 215));
		currentControl.setBounds(7, 5, 491, 215);
		sysPanel.add( currentControl );
		validate();
	}
	
	private void addButtons(){
		{
			buttonPanel = new JPanel();
			buttonPanel.setLayout(null);
			this.add(buttonPanel);
			buttonPanel.setBounds(7, 5, 505, 36);
			buttonPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			{
				butText = new JButton("Text mode");
				buttonPanel.add(butText);
				butText.setBounds(97, 7, 130, 22);
				butText.addActionListener( new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						try{
							setMatrixType(MATRIX_TEXT);
						}catch( Exception ex ){
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "An error occurred while changing the matrix type:\n" + ex.getMessage() );
						}
					}
				});
			}
			{
				butEquations = new JButton("Equation mode");
				buttonPanel.add(butEquations);
				butEquations.setBounds(232, 7, 130, 22);
				butEquations.addActionListener( new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						try{
							setMatrixType(MATRIX_EQUATIONS);
						}catch( Exception ex ){
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "An error occurred while changing the matrix type:\n" + ex.getMessage() );
						}
					}
				});
			}
			{
				butTable = new JButton("Matrix mode");
				buttonPanel.add(butTable);
				butTable.setBounds(367, 7, 130, 22);
				butTable.addActionListener( new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						try{
							setMatrixType(MATRIX_TABLE);
						}catch( Exception ex ){
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "An error occurred while changing the matrix type:\n" + ex.getMessage() );
						}
					}
				});
			}
			{
				inputLabel = new JLabel();
				buttonPanel.add(inputLabel);
				inputLabel.setText("Input");
				inputLabel.setBounds(7, 10, 65, 15);
			}
		}
		{
			sysPanel = new JPanel();
			sysPanel.setLayout(new FlowLayout());
			this.add(sysPanel);
			sysPanel.setBounds(7, 46, 505, 229);
			sysPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		}
	}
	
	public IsMatrixControl getCurrentMatrixControl(){
		if( currentControl == null ) return null;
		return (IsMatrixControl)currentControl;
	}

}
