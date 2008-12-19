package br.edu.ufcg.msnlab.gui.controls;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CtrMatrixText extends javax.swing.JPanel implements IsMatrixControl{

	private static final long serialVersionUID = 1L;
	
    private JTextArea textArea;
	private JScrollPane scroll;

    public CtrMatrixText(){
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
    	textArea = new JTextArea();
    	scroll = new JScrollPane(textArea);
    }
    
    private void putComponents(){
    	add(scroll);
    }
    
    private void resizeComponents(){
		scroll.setBounds( 0, 0, getWidth(), getHeight() );
    }

	public String getMatrix() {
		return textArea.getText();
	}

}
