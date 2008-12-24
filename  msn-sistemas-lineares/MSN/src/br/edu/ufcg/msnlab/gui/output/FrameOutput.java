package br.edu.ufcg.msnlab.gui.output;


import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import br.edu.ufcg.msnlab.gui.MSNLab;
import br.edu.ufcg.msnlab.methods.Methods;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.util.ParsedSystem;


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
public class FrameOutput extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    protected MSNLab msnlab;

    private JScrollPane scroll;

    private Container screen;

	private JPanel panelButtons;

	private JPanel panelFields;

	private JLabel label;

	private JButton buttonPrevious;
	
	private JTextPane buttonHowMany;

	private JButton buttonNext;

	private Result result;

	private int currentResult;

	private ParsedSystem parserResult;

	private String method;

	private JTable tableResult;

    public FrameOutput(MSNLab msnLab) {
        this.msnlab = msnLab;
        initialize();
        positions();
        actions();
        makeScreen();
    }

    private void actions() {
		buttonNext.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
		    	currentResult++;
		    	setCurrentResult();
			}

		});
		
		buttonPrevious.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
		    	currentResult--;
		    	setCurrentResult();
			}

		});
		
	}

	private void initialize() {
        setLayout(null);
        screen = getContentPane();
        setSize(330, 250);
        setClosable(true);

        label = new JLabel("Method");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVisible(true);
        
        tableResult = new JTable();
        scroll = new JScrollPane(tableResult);
        
        panelButtons = new JPanel();
        FlowLayout panelButtonsLayout = new FlowLayout();
        panelButtons.setLayout(panelButtonsLayout);
        panelButtons.setBorder(new EtchedBorder());

        panelFields = new JPanel();
        panelFields.setLayout(null);
        panelFields.setBorder(new EtchedBorder());
        
        buttonHowMany = new JTextPane();
		buttonPrevious = new JButton("<-");
		buttonNext = new JButton("->");
        
    }

    private void positions() {
    	label.setBounds(7, 7, 310, 20);
		panelFields.setBounds(7, 7, 310, 165);
        scroll.setBounds(7, 27, 295, 130);
        panelButtons.setBounds(7, 175, 310, 39);

        buttonHowMany.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
        
    }
    
    public void setSolution(String method, Result result, ParsedSystem parse) {
    	this.setTitle("OUTPUT - " + method);
    	this.method = method;
    	this.result = result;
    	this.parserResult = parse;
    	this.currentResult = 1;
    	setCurrentResult();
    }

    private void setCurrentResult() {
        
    	SolutionTableModel solutionTableModel = new SolutionTableModel();
    	SystemTableModel systemTableModel = new SystemTableModel();

    	if (currentResult == 1) {
    		buttonPrevious.setEnabled(false);
    	} else {
    		buttonPrevious.setEnabled(true);
    	}
    	
    	if (currentResult == result.getValues().size()) {
    		buttonNext.setEnabled(false);
    	} else {
    		buttonNext.setEnabled(true);
    	}
    	buttonHowMany.setText(currentResult + "/" + result.getValues().size());

    	double[][] resultArray = (double[][]) result.getValues().get(currentResult - 1);

    	if (Methods.DecomposicaoSVD.equals(this.method)) {
    		solutionTableModel.setCurrentResult(label, result, this.currentResult, parserResult.getIncognitas());
        	tableResult.setModel(solutionTableModel);
		} else {
			if (resultArray[0].length == 1) {
				solutionTableModel.setCurrentResult(label, result, this.currentResult, parserResult.getIncognitas());
		    	tableResult.setModel(solutionTableModel);
			} else {
				systemTableModel.setCurrentResult(label, result, currentResult, parserResult.getIncognitas());
				tableResult.setModel(systemTableModel);
			}
    	}
    }
    
    private void makeScreen() {
    	panelFields.add(label);
    	panelFields.add(scroll);

    	panelButtons.add(buttonPrevious);
    	panelButtons.add(buttonHowMany);
    	panelButtons.add(buttonNext);
    	
        screen.add(panelFields);
        screen.add(panelButtons);
    }

}
