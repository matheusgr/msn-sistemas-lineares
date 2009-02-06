package br.edu.ufcg.msnlab.gui.controls;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
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
public class CtrMatrixEquations extends JPanel implements IsMatrixControl{

	private static final long serialVersionUID = 1L;

	private LinkedList<JTextField> equationsField = new LinkedList<JTextField>();

	private JPanel equationsPane;
	private JPanel buttonsPanel;
	private JScrollPane scroll;
	
	private JButton butAdd;
	private JButton butSub;
	private JButton butSet;
	
    public CtrMatrixEquations(){
		setSize(new Dimension(490, 219));
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
    	scroll.setViewportView(equationsPane);
   }

    private void addField(){
    	JTextField t = new JTextField();
    	t.setPreferredSize(new Dimension(440, 20));
    	t.setMaximumSize(new Dimension(440, 20));
    	equationsField.add(t);
    	equationsPane.add(t);
    	if (scroll != null) {
    		scroll.setViewportView(equationsPane);
    	}
    }
    
    private void initializeComponents(){
		setLayout(null);
    	putComponents();
    	
    }
    
    private void putComponents(){

    	this.add(getButtonsPanel());
    	{
    		equationsPane = new JPanel();
    		BoxLayout equationsPaneLayout = new BoxLayout(equationsPane, javax.swing.BoxLayout.PAGE_AXIS);
    		this.add(equationsPane);
    		equationsPane.setLayout(equationsPaneLayout);
    		equationsPane.setBounds(56, 41, 162, 102);
    		addField();
    	}
    	scroll = new JScrollPane(equationsPane);
    	scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    	scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    	scroll.setBounds(6, 48, 479, 166);
    	add(scroll);
    	validate();
    }

	public String getMatrix() {
		String result = "";
		for( JTextField t : equationsField ){
			result += t.getText() + "\n";
		}
		return result;
	}
	
	private JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setBounds(6, 5, 479, 37);
			buttonsPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			{
				butAdd = new JButton("Add Equation");
				buttonsPanel.add(butAdd);
				butAdd.setBounds(0,0,60,20);
				butAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						addField();
					}
				});    	
			}
			{
				butSub = new JButton("Remove Equation");
				buttonsPanel.add(butSub);
				butSub.setBounds(60,0,60,20);
				butSub.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						removeField();
					}
				});    	
			}
			{
				butSet = new JButton("Set Number of Equations");
				buttonsPanel.add(butSet);
				butSet.setBounds(120,0,60,20);
				butSet.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						
						try{
							String input = JOptionPane.showInputDialog("Type the number of desired fields");
							if (input == null) {
								return;
							}
							int number = Integer.parseInt(input);
							setNumberOfFields(number);
						}catch( Exception e ){
							JOptionPane.showMessageDialog(null, "An error occurred while reading the number:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
						}
					}
				});    	
			}
		}
		return buttonsPanel;
	}

}
