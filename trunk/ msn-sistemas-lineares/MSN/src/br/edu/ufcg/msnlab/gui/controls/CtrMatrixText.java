package br.edu.ufcg.msnlab.gui.controls;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


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
public class CtrMatrixText extends javax.swing.JPanel implements IsMatrixControl{

	private static final long serialVersionUID = 1L;
	
    private JTextArea textArea;
	private JScrollPane scroll;

    public CtrMatrixText(){
    	initializeComponents();
    	repaint();
    }
    
    private void initializeComponents(){

    	BorderLayout thisLayout = new BorderLayout();
		this.setLayout(thisLayout);
		setSize(new Dimension(490, 215));
		{
			textArea = new JTextArea();
			textArea.setBounds(0, 0, 500, 300);
			scroll = new JScrollPane(textArea);
			scroll.setBounds(0, 0, 500, 300);;
			this.add(scroll, BorderLayout.CENTER);
		}
    }

	public String getMatrix() {
		return textArea.getText();
	}

}
